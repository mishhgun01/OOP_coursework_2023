package com.example.model

import com.example.plugins.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

/**
 * Класс маршрута.
 * @property id - уникальный идентификатоор маршрута.
 * @property name - название маршрута.
 * @property color - цвет маршрута в формате HEX.
 * @property stops - список остановок на маршруте.
 * @property timeInterval - интервалы движения в формате ['HH.MM', 'HH.MM'].
 * @property machinists - машинисты, работающие на данном маршруте.
 * @property dispatchers - диспетчеры, работающие на данном маршруте.
 */
@Serializable
data class Route(val id: Int, val name: String, val color: String, val stops: List<Stop>, val timeInterval: List<String>,
                 val machinists: List<Employee>, val dispatchers: List<Employee>)

/**
 * Класс для создания интерфейса между API БД.
 * @property connection - соединенние с БД.
 * Реализует интерфейс @see Service.
 */
class RouteService(private val connection: Connection) : Service {

    /**
     * Статические переменные класса.
     */
    companion object {
        private const val CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS routes( id SERIAL PRIMARY KEY," +
                "name TEXT NOT NULL, color TEXT NOT NULL, stop_ids integer[], time_interval text[]);"
        private const val SELECT_ALL_ROUTES = "SELECT * FROM routes;"
        private const val SELECT_ROUTE_BY_ID = "SELECT * FROM routes WHERE id = ?;"
        private const val INSERT_ROUTE = "INSERT INTO routes(name,  color, stop_ids, time_interval)" +
                "VALUES (?, ?, ?, ?);"
        private const val UPDATE_ROUTE = "UPDATE routes SET name = ?, color = ?, stop_ids = ?, time_interval = ?" +
                "WHERE id = ?;"
        private const val DELETE_ROUTE = "DELETE FROM routes WHERE id = ?;"
    }

    /**
     * Метод, срабатывающий при инициализации объекта.
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ROUTES)
    }

    /**
     * Метод получения всех сотрудников из БД.
     * @return возвращает список объектов типа @see Route.
     */
    override suspend fun getAll() = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL_ROUTES)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val allStops = StopService(connection).getAll()
        val routeList = mutableListOf<Route>()
        while (resultSet.next()) {
            val idx = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val color = resultSet.getString("color")

            val stops = Helper.convertSQLArrayToIntList(resultSet, "stop_ids")
                .map { id:Int -> allStops.filter { it.id==id } }
                .flatten()

            val machinists = RouteEmployeeService(connection).getByRouteId(idx)
                .employee.filter { it.role.id == 2 }
            val dispatchers = RouteEmployeeService(connection).getByRouteId(idx)
                .employee.filter { it.role.id == 1 }

            val timeInterval = Helper.convertSQLArrayToStringList(resultSet, "time_interval")

            routeList.add(Route(idx, name, color, stops, timeInterval, machinists, dispatchers))
        }
        return@withContext routeList
    }

    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа @see Employee.
     * @throws Exception("no routes with id = $id found").
     */
    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ROUTE_BY_ID)
        statement.setInt(1, id)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val allStops = StopService(connection).getAll()
        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val name = resultSet.getString("name")
            val color = resultSet.getString("color")

            val stops = Helper.convertSQLArrayToIntList(resultSet, "stop_ids")
                .map { id:Int -> allStops.filter { it.id==id } }
                .flatMap { it }

            val timeInterval = Helper.convertSQLArrayToStringList(resultSet, "time_interval")
            val machinists = RouteEmployeeService(connection).getByRouteId(id)
                .employee.filter {it.role.id == 2}
            val dispatchers = RouteEmployeeService(connection).getByRouteId(id)
                .employee.filter {it.role.id == 1}

            return@withContext Route(id, name, color, stops, timeInterval, machinists, dispatchers)
        } else {
            throw Exception("no routes with id = $id found")
        }
    }

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа @see Route.
     * @return возвращает уникальный идентификатор созданного объекта.
     * @throws Exception("error in creating route")
     */
    override suspend fun create(obj: Any) = withContext(Dispatchers.IO){
        if (obj is Route) {
            val statement = connection.prepareStatement(INSERT_ROUTE)
            statement.setString(1, obj.name)
            statement.setString(2, obj.color)
            statement.setArray(3, Helper.prepareSQLArrayFromIntList(connection, obj.stops.map { it.id }))
            statement.setArray(4, Helper.prepareSQLArrayFromStringList(connection, obj.timeInterval))
            val resultSet = statement.resultSet
            if(resultSet.next())  {
                val sequenceOfMachinists = RouteEmployee(mutableListOf(obj), obj.machinists)
                RouteEmployeeService(connection).create(sequenceOfMachinists)
                val sequenceOfDispatchers = RouteEmployee(mutableListOf(obj), obj.dispatchers)
                RouteEmployeeService(connection).create(sequenceOfDispatchers)
                return@withContext resultSet.getInt(1)
            } else {
                throw Exception("error in creating route")
            }        } else {
            throw Exception("error in create route")
        }
    }

    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Employee.
     * @throws Exception("error in updating route")
     */
    override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
        if (obj is Route) {
            val statement = connection.prepareStatement(UPDATE_ROUTE)
            statement.setString(1, obj.name)
            statement.setString(2, obj.color)
            statement.setArray(3, Helper.prepareSQLArrayFromIntList(connection, obj.stops.map { it.id }))
            statement.setArray(4, Helper.prepareSQLArrayFromStringList(connection, obj.timeInterval))
            statement.setInt(7, obj.id)
            statement.executeUpdate()

            val sequenceOfMachinists = RouteEmployee(mutableListOf(obj), obj.machinists)
            RouteEmployeeService(connection).updateByRoute(sequenceOfMachinists)
            val sequenceOfDispatchers = RouteEmployee(mutableListOf(obj), obj.dispatchers)
            RouteEmployeeService(connection).updateByRoute(sequenceOfDispatchers)
        }else {
            throw Exception("error in updating route")
        }
    }

    /**
     * Метод удаления объекта в БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     */
    override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_ROUTE)
        statement.setInt(1, id)
        val obj = getById(id)
        val sequenceOfMachinists = RouteEmployee(mutableListOf(obj), obj.machinists)
        RouteEmployeeService(connection).deleteSequence(sequenceOfMachinists)
        val sequenceOfDispatchers = RouteEmployee(mutableListOf(obj), obj.dispatchers)
        RouteEmployeeService(connection).deleteSequence(sequenceOfDispatchers)
        statement.executeUpdate()
    }
}

