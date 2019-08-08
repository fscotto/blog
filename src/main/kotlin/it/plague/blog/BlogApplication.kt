package it.plague.blog

import com.google.inject.Guice
import com.google.inject.Module
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import it.plague.blog.config.guice.*
import it.plague.blog.verticle.HttpVerticle
import it.plague.blog.verticle.PgArticleVerticle

object BlogApplication {

	private val log = LoggerFactory.getLogger(this.javaClass)

	@JvmStatic
	fun main(args: Array<String>) {
		log.info("Start Vert.x Home Blog")
		val vertx = Vertx.vertx(options())
		val injector = Guice.createInjector(*modules(vertx))
		val factory = GuiceVerticleFactory(injector)
		vertx.registerVerticleFactory(factory)
		deploy(vertx, factory.prefix(), HttpVerticle::class.java.name)
		deploy(vertx, factory.prefix(), PgArticleVerticle::class.java.name)
	}

	@JvmStatic
	private fun modules(vertx: Vertx): Array<Module> {
		return arrayOf(
			ConfigModule(),
			RouterModule(vertx),
			DatabaseModule(vertx),
			CommonModule()
		)
	}

	@JvmStatic
	private fun deploy(vertx: Vertx, prefix: String, verticleName: String) {
		vertx.deployVerticle("$prefix:$verticleName")
	}
}