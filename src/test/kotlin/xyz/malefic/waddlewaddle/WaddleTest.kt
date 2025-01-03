package xyz.malefic.waddlewaddle

import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WaddleTest {
    private val moduleLens = Body.auto<Module>().toLens()

    @Test
    fun `Ping test`() {
        assertEquals(Response(Status.OK).body("pong"), app(Request(GET, "/ping")))
    }

    @Test
    fun `Store module test`() {
        val module = Module(id = 1, min = 10, max = 50)
        val request = Request(POST, "/module").with(moduleLens of module)
        val response = app(request)

        assertEquals(Status.OK, response.status)
        assertEquals("Stored module with id: 1", response.bodyString())
    }

    @Test
    fun `Get stored module test`() {
        // Store a module first
        val module = Module(id = 2, min = 20, max = 60)
        val storeRequest = Request(POST, "/module").with(moduleLens of module)
        app(storeRequest)

        // Retrieve the module
        val getRequest = Request(GET, "/module/2")
        val response = app(getRequest)

        assertEquals(Status.OK, response.status)
        assertEquals("Module: id=2, min=20, max=60", response.bodyString())
    }

    @Test
    fun `Get non-existent module test`() {
        val getRequest = Request(GET, "/module/999")
        val response = app(getRequest)

        assertEquals(Status.NOT_FOUND, response.status)
        assertEquals("Module with id 999 not found", response.bodyString())
    }

    @Test
    fun `Invalid ID in GET request`() {
        val getRequest = Request(GET, "/module/abc")
        val response = app(getRequest)

        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("Invalid or missing id", response.bodyString())
    }

    @Test
    fun `Update existing module test`() {
        // Store an initial module
        val initialModule = Module(id = 1, min = 10, max = 50)
        val storeRequest1 = Request(POST, "/module").with(moduleLens of initialModule)
        app(storeRequest1)

        // Store a module with the same ID but different values
        val updatedModule = Module(id = 1, min = 15, max = 45)
        val storeRequest2 = Request(POST, "/module").with(moduleLens of updatedModule)
        app(storeRequest2)

        // Retrieve the module and verify it has been updated
        val getRequest = Request(GET, "/module/1")
        val response = app(getRequest)

        assertEquals(Status.OK, response.status)
        assertEquals("Module: id=1, min=15, max=45", response.bodyString())
    }
}
