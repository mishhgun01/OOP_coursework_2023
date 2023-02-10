package com.example.controller

import com.example.model.*
import connectToPostgres
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val dbConnection = connectToPostgres()
    val employeeService = EmployeeService(dbConnection)
    val roleService = RoleService(dbConnection)
    val routeService = RouteService(dbConnection)
    val stopService = StopService(dbConnection)

    routing {
        configureEmployeesRouting(employeeService)
        configureRolesRouting(roleService)
        configureRoutesRouting(routeService)
        configureStopsRouting(stopService)
    }
}