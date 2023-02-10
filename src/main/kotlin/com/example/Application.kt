package com.example

import com.example.controller.configureRolesRouting
import com.example.controller.configureRoutesRouting
import com.example.controller.configureRouting
import com.example.controller.configureStopsRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.hooks.CallFailed.install
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureRouting()
}
