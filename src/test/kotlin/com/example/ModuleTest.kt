package com.example;

import com.example.module

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ModuleTest {

//    @Test
//    fun testPatchApiV1Authentication() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/authentication").apply {
//            TODO("Please write your test here")
//        }
//    }

//    @Test
//    fun testPostApiV1Authentication() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/authentication").apply {
//            TODO("Please write your test here")
//        }
//    }

//    @Test
//    fun testPatchApiV1Classification() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/classification").apply {
//            TODO("Please write your test here")
//        }
//    }

//    @Test
//    fun testPostApiV1Classification() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/classification").apply {
//            TODO("Please write your test here")
//        }
//    }

    @Test
    fun testGetApiV1ClassificationId() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/classification").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())
        }
    }

//    @Test
//    fun testPatchApiV1Employees() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/employees").apply {
//            TODO("Please write your test here")
//        }
//    }

//    @Test
//    fun testPostApiV1Employees() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/employees").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testDeleteApiV1EmployeesId() = testApplication {
//        application {
//            module()
//        }
//        client.delete("/api/v1/employees/{id}").apply {
//            TODO("Please write your test here")
//        }
//    }

    @Test
    fun testGetApiV1EmployeesId() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/employees").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())
        }
    }

//    @Test
//    fun testPatchApiV1Roles() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/roles").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testPostApiV1Roles() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/roles").apply {
//            TODO("Please write your test here")
//        }
//    }

    @Test
    fun testGetApiV1RolesId() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/roles").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())        }
    }

//    @Test
//    fun testPatchApiV1Routes() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/routes").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testPostApiV1Routes() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/routes").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testDeleteApiV1RoutesId() = testApplication {
//        application {
//            module()
//        }
//        client.delete("/api/v1/routes/{id}").apply {
//            TODO("Please write your test here")
//        }
//    }

    @Test
    fun testGetApiV1RoutesId() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/routes").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())        }
    }

    @Test
    fun testGetApiV1Stops() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/stops").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())
        }
    }

//    @Test
//    fun testPatchApiV1Stops() = testApplication {
//        application {
//            module()
//        }
//        client.patch("/api/v1/stops").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testPostApiV1Stops() = testApplication {
//        application {
//            module()
//        }
//        client.post("/api/v1/stops").apply {
//            TODO("Please write your test here")
//        }
//    }
//
//    @Test
//    fun testDeleteApiV1StopsId() = testApplication {
//        application {
//            module()
//        }
//        client.delete("/api/v1/stops/{id}").apply {
//            TODO("Please write your test here")
//        }
//    }

    @Test
    fun testGetApiV1StopsId() = testApplication {
        application {
            module()
        }
        client.get("/api/v1/stops?id=1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assert(bodyAsText().isNotEmpty())
        }
    }
}