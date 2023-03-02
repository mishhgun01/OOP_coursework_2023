package com.example.model

import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class RouteEmployee(val route : List<Route>, val employee: List<Employee>)

class RouteEmployeeService(private val connection: Connection): Service {

    companion object {
        private const val CREATE_TABLE_ROUTES_EMPLOYEES = "CREATE TABLE IF NOT EXISTS route_employees_pkey(" +
                "route_id INTEGER NOT NULL, employee_id INTEGER NOT NULL, PRIMARY KEY (route_id, employee_id)," +
                "FOREIGN KEY (route_id) REFERENCES routes(id),"+
                "FOREIGN KEY (employee_id) REFERENCES employees(id));"
        private const val GET_ROUTES_BY_EMPLOYEE_ID = "SELECT * FROM route_employees_pkey WHERE route_id = ?;"
        private const val GET_EMPLOYEE_BY_ROUTE_ID = "SELECT * FROM route_employees_pkey WHERE employee_ = ?;"
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
    override suspend fun getById(id: Int): RouteEmployee {
        val statement = connection.prepareStatement(GET_EMPLOYEE_BY_ROUTE_ID)
        statement.setInt(1,id)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val route = RouteService(connection).getById(id)
        val employees = EmployeeService(connection).getAll()
        val routes = mutableListOf<Route>()
        routes.add(route)
        val employeesOutput = mutableListOf<Employee>()
        while(resultSet.next()){
            val employeeID = resultSet.getInt("employee_id")
            employees.find { it.id==employeeID }?.let {
                employeesOutput.add(it)
            }
        }
        return RouteEmployee(routes, employeesOutput)
    }

    override suspend fun getAll(): RouteEmployee {
        val statement = connection.prepareStatement(GET_ALL)
        statement.executeQuery()
        val resultSet = statement.resultSet
        val routesList = mutableListOf<Route>()
        val employeesList = mutableListOf<Employee>()
        val routes = RouteService(connection).getAll()
        val employees = EmployeeService(connection).getAll()

        while (resultSet.next()) {
            val routeID = resultSet.getInt("route_id")
            val employeeID = resultSet.getInt("employee_id")
            routes.find { it.id==routeID }?.let{routesList.add(it)}
            employees.find {it.id == employeeID}?.let { employeesList.add(it) }
        }
        return RouteEmployee(routesList, employeesList)
    }

    suspend fun getByEmployeeID(id: Int) : RouteEmployee {
        val statement = connection.prepareStatement(GET_ROUTES_BY_EMPLOYEE_ID)
        statement.setInt(1,id)
        statement.executeQuery()
        val resultSet = statement.resultSet

        val routes = RouteService(connection).getAll()
        val employee = EmployeeService(connection).getById(id)
        val employees = mutableListOf<Employee>()
        employees.add(employee)
        val routesOuptut = mutableListOf<Route>()
        while(resultSet.next()){
            val routeID = resultSet.getInt("route_id")
            routes.find { it.id==routeID }?.let {
                routesOuptut.add(it)
            }
        }
        return RouteEmployee(routesOuptut, employees)
    }

    override suspend fun create(obj: Any): Int {
        val statement = connection.prepareStatement(INSERT_NEW_SEQUENCE)
        return if (obj is RouteEmployee) {
            for (route in obj.route) {
                for (employee in obj.employee) {
                    statement.setInt(1, route.id)
                    statement.setInt(2, employee.id!!)
                    statement.executeUpdate()
                }
            }
            1
        } else {
            0
        }
    }

    override suspend fun update(obj: Any) {
        val statement = connection.prepareStatement(UPDATE_SEQUENCE_BY_ROUTE)
        if (obj is RouteEmployee) {
            for (route in obj.route) {
                for (employee in obj.employee) {
                    statement.setInt(1, route.id)
                    statement.setInt(2, employee.id!!)
                    statement.setInt(3, route.id)
                    statement.executeUpdate()
                }
            }
        } else {
            throw Exception()
        }
    }
    suspend fun updateByEmployee(obj: Any) {
        val statement = connection.prepareStatement(UPDATE_SEQUENCE_BY_EMPLOYEE)
        if (obj is RouteEmployee) {
            for (route in obj.route) {
                for (employee in obj.employee) {
                    statement.setInt(1, route.id)
                    statement.setInt(2, employee.id!!)
                    statement.setInt(3, employee.id)
                    statement.executeUpdate()
                }
            }
        } else {
            throw Exception()
        }
    }

    override suspend fun delete(id: Int) = throw Exception()

    suspend fun deleteSequence(obj: RouteEmployee) {
        val statement = connection.prepareStatement(DELETE_SEQUENCE)
        for (route in obj.route) {
            for (employee in obj.employee) {
                statement.setInt(1, route.id)
                statement.setInt(2, employee.id!!)
                statement.executeUpdate()
            }
        }
    }
}