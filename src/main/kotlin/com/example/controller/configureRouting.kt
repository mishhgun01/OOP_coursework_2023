package com.example.controller

import com.example.model.*
import connectToPostgres
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val dbConnection = connectToPostgres()
    val classificationService = ClassificationService(dbConnection)
    val roleService = RoleService(dbConnection)
    val employeeService = EmployeeService(dbConnection)
    val stopService = StopService(dbConnection)
    val routeService = RouteService(dbConnection)
    routing {
        configureRolesRouting(roleService)
        configureClassificationRouting(classificationService)
        configureEmployeesRouting(employeeService)
        configureStopsRouting(stopService)
        configureRoutesRouting(routeService)
        configureAuthRouting(employeeService)
    }
}