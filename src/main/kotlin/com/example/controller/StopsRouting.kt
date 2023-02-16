package com.example.controller

import com.example.model.Service
import com.example.model.Stop
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Extension-функция, расширяющая класс Route для маршрутизации запросов, касающихся остановок.
 * @param stopService - сервис для взаимодействия с БД.
 */
fun Route.configureStopsRouting(stopService: Service) {

    // Stops
    route("/api") {
        route("/v1") {
            route("/stops") {
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
    }
}
