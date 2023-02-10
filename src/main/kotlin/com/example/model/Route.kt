package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class Route(val id: Int, val name: String, val color: String, val stops: List<Stop>, val timeInterval: List<String>,
                 val machinists: List<Employee>, val dispatchers: List<Employee>): SQLHelper

class RouteService(private val connection: Connection) : AbstractService(connection) {

    companion object {
        private const val CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS routes( id SERIAL PRIMARY KEY," +
                "name TEXT NOT NULL, color TEXT NOT NULL, stop_ids integer[], time_interval text[]," +
                "machinist_ids integer[], dispatcher_ids integer[]);"
        private const val SELECT_ALL_ROUTES = "SELECT * FROM routes;"
        private const val SELECT_ROUTE_BY_ID = "SELECT * FROM routes WHERE id = ?;"
        private const val INSERT_ROUTE = "INSERT INTO routes(name,  color, stop_ids, time_interval, machinist_ids, dispatcher_ids)" +
                "VALUES (?, ?, ?, ?, ?, ?);"
        private const val UPDATE_ROUTE = "UPDATE routes SET name = ?, color = ?, stop_ids = ?, time_interval = ?, machinist_ids = ?, dispatcher_ids = ?" +
                "WHERE id = ?;"
        private const val DELETE_ROUTE = "DELETE FROM routes WHERE id = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ROUTES)
    }

    override suspend fun getAll() = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_ROUTES)
        val resultSet = statement.resultSet

        val allEmployees = EmployeeService(connection).getAll()
        val allStops = StopService(connection).getAll()

        val routeList = mutableListOf<Route>()
        while (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val color = resultSet.getString("color")

            val stops = SQLHelper.convertToIntList(resultSet, "stop_ids")
                .map { id:Int -> allStops.filter { it.id==id } }
                .flatMap { it }

            val timeInterval = SQLHelper.convertToStringList(resultSet, "time_interval")
            val machinists = SQLHelper.convertToIntList(resultSet, "machinist_ids")
                .map { id: Int -> allEmployees.filter { it.id == id } }
                .flatMap { it }

            val dispatchers = SQLHelper.convertToIntList(resultSet, "dispatcher_ids")
                .map { id: Int -> allEmployees.filter { it.id == id } }
                .flatMap { it }
            routeList.add(Route(id, name, color, stops, timeInterval, machinists, dispatchers))
        }
        return@withContext routeList
    }

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ROUTE_BY_ID)
        statement.setInt(1, id)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val allEmployees = EmployeeService(connection).getAll()
        val allStops = StopService(connection).getAll()

        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val color = resultSet.getString("color")

            val stops = SQLHelper.convertToIntList(resultSet, "stop_ids")
                .map { id:Int -> allStops.filter { it.id==id } }
                .flatMap { it }

            val timeInterval = SQLHelper.convertToStringList(resultSet, "time_interval")
            val machinists = SQLHelper.convertToIntList(resultSet, "machinist_ids")
                .map { id: Int -> allEmployees.filter { it.id == id } }
                .flatMap { it }

            val dispatchers = SQLHelper.convertToIntList(resultSet, "dispatcher_ids")
                .map { id: Int -> allEmployees.filter { it.id == id } }
                .flatMap { it }
            return@withContext Route(id, name, color, stops, timeInterval, machinists, dispatchers)
        } else {
            throw Exception("ERROR in get routes by id = $id")
        }
    }

    override suspend fun create(obj: Any) = withContext(Dispatchers.IO){
        if (obj is Route) {
            val statement = connection.prepareStatement(INSERT_ROUTE)
            statement.setString(1, obj.name)
            statement.setString(2, obj.color)
            statement.setArray(3, obj.prepareArrayFromIntList(connection, obj.stops.map { it.id }))
            statement.setArray(4, obj.prepareArrayFromStringList(connection, obj.timeInterval))
            statement.setArray(5, obj.prepareArrayFromIntList(connection, obj.machinists.map { it.id }))
            statement.setArray(6, obj.prepareArrayFromIntList(connection, obj.dispatchers.map { it.id }))
            statement.executeUpdate()
        } else {
            throw Exception("error in create role")
        }
    }

    override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
        if (obj is Route) {
            val statement = connection.prepareStatement(UPDATE_ROUTE)
            statement.setString(1, obj.name)
            statement.setString(2, obj.color)
            statement.setArray(3, obj.prepareArrayFromIntList(connection, obj.stops.map { it.id }))
            statement.setArray(4, obj.prepareArrayFromStringList(connection, obj.timeInterval))
            statement.setArray(5, obj.prepareArrayFromIntList(connection, obj.machinists.map { it.id }))
            statement.setArray(6, obj.prepareArrayFromIntList(connection, obj.dispatchers.map { it.id }))
            statement.setInt(7, obj.id)
            statement.executeUpdate()
        }else {
            throw Exception("error in update role")
        }
    }

    override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_ROUTE)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}

