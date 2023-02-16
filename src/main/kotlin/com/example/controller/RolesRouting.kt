package com.example.controller

import com.example.model.Service
import com.example.model.Role
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Extension-функция, расширяющая класс Route для маршрутизации запросов, касающихся ролей.
 * @param roleService - сервис для взаимодействия с БД.
 */
fun Route.configureRolesRouting(roleService: Service) {
    route("/api") {
        route("/v1"){
            route("/roles"){
                get("{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respond(roleService.getAll())
                    val role = roleService.getById(id.toInt())
                    call.respond(role)
                }
                post {
                    val role = call.receive<Role>()
                    val id = roleService.create(role)
                    call.respond(id)
                }
                patch {
                    val role = call.receive<Role>()
                    roleService.update(role)
                    call.respondText("Role updated correctly", status = HttpStatusCode.OK)
                }
            }
        }
        // Roles
    }
}