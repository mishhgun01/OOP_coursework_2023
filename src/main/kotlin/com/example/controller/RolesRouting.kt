package com.example.controller

import com.example.model.AbstractService
import com.example.model.Role
import com.example.model.RoleService
import connectToPostgres
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Route.configureRolesRouting(roleService: AbstractService) {
    route("/api/v1/roles") {
        // Roles
        get("/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respond(roleService.getAll())
            val role = roleService.getById(id.toInt())
            call.respond(role)
        }
        post {
            val role = call.receive<Role>()
            roleService.create(role)
            call.respondText("Role stored correctly", status = HttpStatusCode.Created)
        }
        patch {
            val role = call.receive<Role>()
            roleService.update(role)
            call.respondText("Role updated correctly", status = HttpStatusCode.OK)
        }
    }
}