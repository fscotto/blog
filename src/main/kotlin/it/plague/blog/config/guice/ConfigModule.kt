package it.plague.blog.config.guice

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import java.net.InetAddress

class ConfigModule() : AbstractModule() {

	private val log = LoggerFactory.getLogger(this.javaClass)

	override fun configure() {
		enviriromentConfiguration()
	}

	private fun enviriromentConfiguration(properties: MutableMap<String, String> = mutableMapOf()) {
		log.info("Loading new environment variable...")
		Names.bindProperties(binder(), properties.apply {
			set("HOST", System.getenv("HOST") ?: InetAddress.getLocalHost().hostName)
			set("PORT", System.getenv("PORT") ?: "9000")
		})
		log.info("Environment variable:\n ${JsonObject(properties as Map<String, Any>?).encodePrettily()}")
		log.info("...loading complete!!!")
	}
}