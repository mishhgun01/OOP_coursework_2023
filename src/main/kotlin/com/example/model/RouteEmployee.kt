package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class RouteEmployee(val routeIDs : List<Int>, val employeeIDs: List<Int>)

class RouteEmployeeService(private val connection: Connection) {

    companion object {
        private const val CREATE_TABLE_ROUTES_EMPLOYEES = "CREATE TABLE IF NOT EXISTS route_employees_pkey(" +
                "route_id INTEGER NOT NULL, employee_id INTEGER NOT NULL, PRIMARY KEY (route_id, employee_id)," +
                "FOREIGN KEY (route_id) REFERENCES routes(id),"+
                "FOREIGN KEY (employee_id) REFERENCES employees(id));"
        private const val GET_ROUTES_BY_EMPLOYEE_ID = "SELECT * FROM route_employees_pkey WHERE employee_id = ?;"
        private const val GET_EMPLOYEE_BY_ROUTE_ID = "SELECT * FROM route_employees_pkey WHERE route_id = ?;"
        private const val INSERT_NEW_SEQUENCE = "INSERT INTO route_employees_pkey(route_id, employee_id) VALUES (?, ?);"
        private const val UPDATE_SEQUENCE_BY_ROUTE = "UPDATE route_employees_pkey SET route_id = ?, employee_id = ? WHERE route_id = ?;"
        private const val UPDATE_SEQUENCE_BY_EMPLOYEE = "UPDATE route_employees_pkey SET route_id = ?, employee_id = ? WHERE employee_id = ?;"
        private const val DELETE_SEQUENCE = "DELETE FROM route_employees_pkey WHERE route_id = ? AND employee_id = ?;"
        private const val GET_ALL = "SELECT * FROM route_employees_pkey;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ROUTES_EMPLOYEES)
    }

    suspend fun getAll(): RouteEmployee = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(GET_ALL)
        statement.executeQuery()
        val resultSet = statement.resultSet
        val routesList = mutableListOf<Int>()
        val employeesList = mutableListOf<Int>()

        while (resultSet.next()) {
            val routeID = resultSet.getInt("route_id")
            val employeeID = resultSet.getInt("employee_id")
            routesList.add(routeID)
            employeesList.add(employeeID)
        }
        return@withContext RouteEmployee(routesList, employeesList)
    }


    suspend fun getByRouteId(id: Int): RouteEmployee = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(GET_EMPLOYEE_BY_ROUTE_ID)
        statement.setInt(1,id)
        statement.executeQuery()
        val resultSet = statement.resultSet
        val routes = mutableListOf<Int>()
        val employees = mutableListOf<Int>()
        routes.add(id)
        while(resultSet.next()){
            val employeeID = resultSet.getInt("employee_id")
            employees.add(employeeID)
        }
        return@withContext RouteEmployee(routes, employees)
    }


    suspend fun getByEmployeeId(id: Int) : RouteEmployee = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(GET_ROUTES_BY_EMPLOYEE_ID)
        statement.setInt(1,id)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val employees = mutableListOf<Int>()
        employees.add(id)
        val routes = mutableListOf<Int>()
        while(resultSet.next()){
            val routeID = resultSet.getInt("route_id")
            routes.add(routeID)
        }
        return@withContext RouteEmployee(routes, employees)
    }

    suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_NEW_SEQUENCE)
        return@withContext if (obj is RouteEmployee) {
            for (routeID in obj.routeIDs) {
                for (employeeID in obj.employeeIDs) {
                    statement.setInt(1, routeID)
                    statement.setInt(2, employeeID)
                    statement.executeUpdate()
                }
            }
            1
        } else {
            0
        }
    }

    suspend fun updateByRoute(obj: Any) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_SEQUENCE_BY_ROUTE)
        if (obj is RouteEmployee) {
            for (routeID in obj.routeIDs) {
                for (employeeID in obj.employeeIDs) {
                    statement.setInt(1, routeID)
                    statement.setInt(2, employeeID)
                    statement.setInt(3, routeID)
                    statement.executeUpdate()
                }
            }
        } else {
            throw Exception()
        }
    }
    suspend fun updateByEmployee(obj: Any) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_SEQUENCE_BY_EMPLOYEE)
        if (obj is RouteEmployee) {
            for (routeID in obj.routeIDs) {
                for (employeeID in obj.employeeIDs) {
                    statement.setInt(1, routeID)
                    statement.setInt(2, employeeID)
                    statement.setInt(3, employeeID)
                    statement.executeUpdate()
                }
            }
        } else {
            throw Exception()
        }
    }

    suspend fun deleteSequence(obj: RouteEmployee) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_SEQUENCE)
        for (routeID in obj.routeIDs) {
            for (employeeID in obj.employeeIDs) {
                statement.setInt(1, routeID)
                statement.setInt(2, employeeID)
                statement.executeUpdate()
            }
        }
    }
}