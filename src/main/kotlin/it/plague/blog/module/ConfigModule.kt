package it.plague.blog.module

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import java.util.*

class ConfigModule : AbstractModule() {

	override fun configure() {
		val properties = Properties()
		properties.setProperty("HOST", System.getenv("HOST") ?: "localhost")
		properties.setProperty("PORT", System.getenv("PORT") ?: "9000")
		Names.bindProperties(binder(), properties)
	}
}