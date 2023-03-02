package com.example.model

import com.example.plugins.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

/**
 * Класс остановки.
 * @property id - уникальный идентификатоор остановки.
 * @property name - название остановки.
 * @property lat - широта, на которой расположена остановка в формате десятичных градусов.
 * @property lon - долгота, на которой расположена остановка в формате десятичных градусов.
 * @property timeInterval - время ожидания ТС на остановке.
 * @property isEnd - флаг - является ли остановка конечной.
 * @property notes - список объявлений для данной остановки.
 */
@Serializable
data class Stop(val id: Int, val name: String, val lat: Double,val lon: Double,
                val timeInterval: Int, val isEnd: Boolean, val notes: String? = ""
)

/**
 * Класс для создания интерфейса между API БД.
 * @property connection - соединенние с БД.
 * Реализует интерфейс @see Service.
 */
class StopService(private val connection: Connection): Service {

    /**
     * Статические переменные класса.
     */
    companion object {
        private const val CREATE_TABLE_STOPS = "CREATE TABLE IF NOT EXISTS stops(" +
                "id SERIAL PRIMARY KEY, name TEXT NOT NULL, lat REAL NOT NULL, lon REAL NOT NULL," +
                "time_interval INTEGER NOT NULL, is_end BOOLEAN DEFAULT FALSE, notes text NOT NULL DEFAULT '');"
        private const val SELECT_ALL_STOPS = "SELECT * FROM stops;"
        private const val SELECT_STOP_BY_ID = "SELECT * FROM stops WHERE id = ?;"
        private const val INSERT_STOP = "INSERT INTO stops(name, lat, lon, time_interval, is_end, notes)" +
                "VALUES (?, ?, ?, ?, ?, ?);"
        private const val UPDATE_STOP = "UPDATE stops SET name = ?, lat = ?, lon = ?, time_interval = ?, is_end = ?, notes = ? WHERE id = ?"
        private const val DELETE_STOP = "DELETE FROM stops WHERE id = ?;"
    }

    /**
     * Метод, срабатывающий при инициализации объекта.
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_STOPS)
    }

    /**
     * Метод получения всех сотрудников из БД.
     * @return возвращает список объектов типа @see Stop.
     */
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
            val notes = resultSet.getString("notes")
            stopsList.add(Stop(id, name, lat, lon, timeInterval, isEnd, notes))
        }
        return@withContext stopsList
    }

    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа @see Stop.
     * @throws Exception("No employees with id=$id found").
     */
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
            val notes = resultSet.getString("notes")
            return@withContext Stop(id, name, lat, lon, timeInterval, isEnd, notes)
        } else {
            throw Exception("No stops with id=$id found")
        }
    }

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа @see Stop.
     * @return возвращает уникальный идентификатор созданного объекта.
     * @throws Exception("error in creating stop").
     */
    override suspend fun create(obj: Any) = withContext(Dispatchers.IO) {
        if (obj is Stop) {
            val statement = connection.prepareStatement(INSERT_STOP)
            statement.setString(1, obj.name)
            statement.setDouble(2, obj.lat)
            statement.setDouble(3, obj.lon)
            statement.setInt(4, obj.timeInterval)
            statement.setBoolean(5, obj.isEnd)
            statement.setString(6, obj.notes)
            statement.executeUpdate()
        } else {
            throw Exception("error in creating stop")
        }
    }

    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Stop.
     * @throws Exception("error in updating role").
     */
    override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
        if (obj is Stop) {
            val statement = connection.prepareStatement(UPDATE_STOP)
            statement.setString(1, obj.name)
            statement.setDouble(2, obj.lat)
            statement.setDouble(3, obj.lon)
            statement.setInt(4, obj.timeInterval)
            statement.setBoolean(5, obj.isEnd)
            statement.setString(6, obj.notes)
            statement.setInt(7, obj.id)
            statement.executeUpdate()
        } else {
            throw Exception("error in updating role")
        }
    }

    /**
     * Метод удаления объекта в БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     */
    override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_STOP)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}
