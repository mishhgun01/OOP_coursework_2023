package com.example.controller

import com.example.model.Employee
import com.example.model.EmployeeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureAuthRouting(employeeService: EmployeeService) {
    route("/api") {
        route("/v1") {
            route("/authentication") {
                patch {
                    val employee = call.receive<Employee>()
                    val id = employeeService.create(employee)
                    call.respond(id)
                }
                post {
                    val employee = call.receive<Employee>()
                    val id = employeeService.authEmployee(employee)
                    call.respond(id)
                }
            }
        }
    }
}