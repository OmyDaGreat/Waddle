package xyz.malefic.waddlewaddle

import xyz.malefic.waddlewaddle.formats.JacksonMessage
import xyz.malefic.waddlewaddle.formats.YamlMessage
import xyz.malefic.waddlewaddle.formats.jacksonMessageLens
import xyz.malefic.waddlewaddle.formats.yamlMessageLens
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/ping" bind GET to {
        Response(OK).body("pong")
    },

    "/formats/yaml" bind GET to {
        Response(OK).with(yamlMessageLens of YamlMessage("Barry", "Hello there!"))
    },

    "/formats/json/jackson" bind GET to {
        Response(OK).with(jacksonMessageLens of JacksonMessage("Barry", "Hello there!"))
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}
