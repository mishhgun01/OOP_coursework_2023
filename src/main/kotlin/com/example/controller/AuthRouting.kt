package com.example.controller

import com.example.filework.Logger
import com.example.model.Employee
import com.example.model.EmployeeService
import com.example.model.UserForAuth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Route.configureAuthRouting(employeeService: EmployeeService) {
    route("/api") {
        route("/v1") {
            route("/authentication") {
                post {
                    val employee = call.receive<Employee>()
                    val logging: Job = launch { Logger("logger.log").upload("user $employee is registering...") }
                    val id = employeeService.create(employee)
                    logging.join()
                    call.respond(id)
                }
                 patch {
                    val employee = call.receive<UserForAuth>()
                     val id = employeeService.authEmployee(employee)
                     var answerString = ""
                     val logging: Job = launch { Logger("logger.log").upload("user $employee is trying to auth...$answerString") }
                     if (id==0) answerString+="Failed..."
                     else answerString+="Success!"
                     logging.join()
                    call.respond(id)
                }
            }
        }
    }
}