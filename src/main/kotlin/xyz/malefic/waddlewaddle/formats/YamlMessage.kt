package xyz.malefic.waddlewaddle.formats

import org.http4k.core.Body
import org.http4k.format.JacksonYaml.auto

data class Module(
    val id: Int,
    val min: Int,
    val max: Int,
)

val moduleLens = Body.auto<Module>().toLens()
