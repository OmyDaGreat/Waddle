package xyz.malefic.waddlewaddle

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.http4k.core.Body
import org.http4k.format.JacksonYaml.auto
import java.io.File
import java.util.concurrent.ConcurrentHashMap

data class Module(
    val id: Int,
    val min: Int,
    val max: Int,
)

val moduleLens = Body.auto<Module>().toLens()

// Jackson object mapper for serialization/deserialization
private val yamlmapper = YAMLMapper().registerKotlinModule()

// File to save modules
private const val MODULE_STORE_FILE = "modules.yaml"

// Persistent storage for modules
val moduleStore: ConcurrentHashMap<Int, Module> = loadModules()

// Load modules from file
fun loadModules(): ConcurrentHashMap<Int, Module> {
    val file = File(MODULE_STORE_FILE)
    return if (file.exists()) {
        val modules: Map<Int, Module> = yamlmapper.readValue(file)
        ConcurrentHashMap(modules)
    } else {
        ConcurrentHashMap()
    }
}

// Save modules to file
fun saveModules(modules: ConcurrentHashMap<Int, Module>) {
    val file = File(MODULE_STORE_FILE)
    yamlmapper.writeValue(file, modules)
}
