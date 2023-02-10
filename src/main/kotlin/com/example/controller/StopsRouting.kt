package com.example.controller

import com.example.model.AbstractService
import com.example.model.Stop
import com.example.model.StopService
import connectToPostgres
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Route.configureStopsRouting(stopService: AbstractService) {

    // Stops
    route("/api/v1/stops") {
        get {
            call.respond(stopService.getAll())
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respond(stopService.getAll())
            val stop = stopService.getById(id.toInt())
            call.respond(stop)
        }
        post {
            val stop = call.receive<Stop>()
            stopService.create(stop)
            call.respondText("Stop stored correctly", status = HttpStatusCode.Created)
        }
        patch {
            val stop = call.receive<Stop>()
            stopService.update(stop)
            call.respondText("Stop updated correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            stopService.delete(id.toInt())
            call.respondText("Stop deleted correctly", status = HttpStatusCode.OK)
        }
    }
}
