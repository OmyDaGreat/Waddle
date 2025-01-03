package xyz.malefic.waddlewaddle.formats

import org.http4k.core.Body
import org.http4k.format.JacksonYaml.auto

data class YamlMessage(
    val subject: String,
    val message: String,
)

val yamlMessageLens = Body.auto<YamlMessage>().toLens()
