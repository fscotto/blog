package it.plague.blog.config.guice

import com.google.inject.Injector
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory

class GuiceVerticleFactory(private val injector: Injector) : VerticleFactory {

	companion object {
		private const val prefix = "it.plague"
	}

	override fun prefix(): String = prefix

	override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
		val verticle = VerticleFactory.removePrefix(verticleName)
		return injector.getInstance(classLoader.loadClass(verticle)) as Verticle
	}
}
