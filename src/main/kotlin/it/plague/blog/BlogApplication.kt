package it.plague.blog

import com.google.inject.Guice
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import it.plague.blog.config.GuiceVerticleFactory
import it.plague.blog.config.options
import it.plague.blog.module.ConfigModule
import it.plague.blog.module.RouterModule
import it.plague.blog.verticle.HttpVerticle

object BlogApplication {

	private val log = LoggerFactory.getLogger(this.javaClass)

	@JvmStatic
	fun main(args: Array<String>) {
		log.info("Start Vert.x Home Blog")
		val vertx = Vertx.vertx(options())
		val injector = Guice.createInjector(ConfigModule(), RouterModule(vertx))
		val factory = GuiceVerticleFactory(injector)
		vertx.registerVerticleFactory(factory)
		deploy(vertx, factory.prefix(), HttpVerticle::class.java.name)
	}

	@JvmStatic
	private fun deploy(vertx: Vertx, prefix: String, verticleName: String) {
		vertx.deployVerticle("$prefix:$verticleName")
	}
}
