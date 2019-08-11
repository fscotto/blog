package it.plague.blog.config.guice

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import java.net.InetAddress
import java.util.*

class ConfigModule : AbstractModule() {

	override fun configure() {
		Names.bindProperties(binder(), Properties().apply {
			setProperty("HOST", System.getenv("HOST") ?: InetAddress.getLocalHost().hostName)
			setProperty("PORT", System.getenv("PORT") ?: "9000")
		})
	}
}