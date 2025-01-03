package xyz.malefic.waddlewaddle

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

val app: HttpHandler =
    routes(
        "/ping" bind GET to {
            Response(OK).body("pong")
        },
        "/module" bind POST to { request: Request ->
            val module = moduleLens.extract(request)
            moduleStore[module.id] = module
            saveModules(moduleStore) // Save to file
            Response(OK).body("Stored module with id: ${module.id}")
        },
        "/module/{id}" bind GET to { request: Request ->
            val id = request.path("id")?.toIntOrNull()
            id?.let {
                val module = moduleStore[id]
                if (module != null) {
                    Response(OK).body("Module: id=${module.id}, min=${module.min}, max=${module.max}")
                } else {
                    Response(NOT_FOUND).body("Module with id $id not found")
                }
            } ?: Response(BAD_REQUEST).body("Invalid or missing id")
        },
    )

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}
