package com.example.controller

import com.example.model.AbstractService
import com.example.model.Employee
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureEmployeesRouting(employeeService: AbstractService) {

    route("/api/v1/employees") {
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get  call.respond(employeeService.getAll())
            val employee = employeeService.getById(id.toInt())
            call.respond(employee)
        }
        post() {
            val employee = call.receive<Employee>()
            employeeService.create(employee)
            call.respondText("Employee stored correctly", status = HttpStatusCode.Created)
        }
        patch() {
            val employee = call.receive<Employee>()
            employeeService.create(employee)
            call.respondText("Employee updated correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            employeeService.delete(id.toInt())
            call.respondText("Employee deleted correctly", status = HttpStatusCode.OK)
        }
    }
}