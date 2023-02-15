package com.example.controller

import com.example.model.Classification
import com.example.model.ClassificationService
import com.example.model.Service
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureClassificationRouting(classificationService: Service) {
    route("/api") {
        route("/v1") {
            route("/classification") {
                get("{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respond(classificationService.getAll())
                    val classification = classificationService.getById(id.toInt())
                    call.respond(classification)
                }
                post {
                    val classification = call.receive<Classification>()
                    val id = classificationService.create(classification)
                    call.respond(id)
                }
                patch {
                    val classification = call.receive<Classification>()
                    val id = classificationService.update(classification)
                    call.respond(id)
                }
            }
        }
    }
}