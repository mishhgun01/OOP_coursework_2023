package com.example.controller

import com.example.model.Service
import com.example.model.Route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun io.ktor.server.routing.Route.configureRoutesRouting(routeService: Service) {

    route("/api/v1/routes"){
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respond(routeService.getAll())
            val routes = routeService.getById(id.toInt())
            call.respond(routes)
        }
        post {
            val route = call.receive<Route>()
            routeService.create(route)
            call.respondText("Route stored correctly", status = HttpStatusCode.Created)
        }
        patch {
            val route = call.receive<Route>()
            routeService.update(route)
            call.respondText("Route updated correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            routeService.delete(id.toInt())
            call.respondText("Route deleted correctly", status = HttpStatusCode.OK)
        }
    }
}