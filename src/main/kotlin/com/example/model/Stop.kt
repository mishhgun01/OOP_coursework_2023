package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class Stop(val id: Int, val name: String, val lat: Double,val lon: Double, val timeInterval: Int, val isEnd: Boolean)

class StopService(private val connection: Connection): AbstractService(connection) {
    companion object {
        private const val CREATE_TABLE_STOPS = "CREATE TABLE IF NOT EXISTS stops(" +
                "id SERIAL PRIMARY KEY, name TEXT NOT NULL, lat REAL NOT NULL, lon REAL NOT NULL," +
                "time_interval INTEGER NOT NULL, is_end BOOLEAN DEFAULT FALSE);"
        private const val SELECT_ALL_STOPS = "SELECT * FROM stops;"
        private const val SELECT_STOP_BY_ID = "SELECT * FROM stops WHERE id = ?;"
        private const val INSERT_STOP = "INSERT INTO stops(name, lat, lon, time_interval, is_end)" +
                "VALUES (?, ?, ?, ?, ?);"
        private const val UPDATE_STOP = "UPDATE stops SET name = ?, lat = ?, lon = ?, time_interval = ?, is_end = ?"
        private const val DELETE_STOP = "DELETE FROM stops WHERE id = ?;"
    }
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_STOPS)
    }

    override suspend fun getAll(): List<Stop> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_STOPS)
        statement.executeQuery()

        val resultSet = statement.resultSet
        val stopsList = mutableListOf<Stop>()
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val lat = resultSet.getDouble("lat")
            val lon = resultSet.getDouble("lon")
            val timeInterval = resultSet.getInt("time_interval")
            val isEnd = resultSet.getBoolean("is_end")
            stopsList.add(Stop(id, name, lat, lon, timeInterval, isEnd))
        }
        return@withContext stopsList
    }

    override suspend fun getById(id: Int): Stop = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_STOP_BY_ID)
        statement.setInt(1, id)

        val resultSet = statement.resultSet
        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val lat = resultSet.getDouble("lat")
            val lon = resultSet.getDouble("lon")
            val timeInterval = resultSet.getInt("time_interval")
            val isEnd = resultSet.getBoolean("is_end")
            return@withContext Stop(id, name, lat, lon, timeInterval, isEnd)
        } else {
            throw Exception("error in select stops by id=$id")
        }
    }

    override suspend fun create(obj: Any) = withContext(Dispatchers.IO) {
        if (obj is Stop) {
            val statement = connection.prepareStatement(INSERT_STOP)
            statement.setString(1, obj.name)
            statement.setDouble(2, obj.lat)
            statement.setDouble(3, obj.lon)
            statement.setInt(4, obj.timeInterval)
            statement.setBoolean(5, obj.isEnd)
            statement.executeUpdate()
        } else {
            throw Exception("error in create stop")
        }
    }

    override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
        if (obj is Stop) {
            val statement = connection.prepareStatement(UPDATE_STOP)
            statement.setString(1, obj.name)
            statement.setDouble(2, obj.lat)
            statement.setDouble(3, obj.lon)
            statement.setInt(4, obj.timeInterval)
            statement.setBoolean(5, obj.isEnd)
            statement.executeUpdate()
        } else {
            throw Exception("error in update role")
        }
    }

    override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_STOP)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}
