package com.example.controller

import com.example.model.Service
import com.example.model.Employee
import com.example.model.RouteEmployeeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Extension-функция, расширяющая класс Route для маршрутизации запросов, касающихся сотрудников.
 * @param employeeService - сервис для взаимодействия с БД.
 */
fun Route.configureEmployeesRouting(employeeService: Service) {

    route("/api") {
        route("/v1") {
            route("/employees") {
                get("{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respond(employeeService.getAll())
                    val employee = employeeService.getById(id.toInt())
                    call.respond(employee)
                }
                post() {
                    val employee = call.receive<Employee>()
                    val id = employeeService.create(employee)
                    call.respond(id)
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
    }
}