package it.plague.blog

import com.google.inject.Guice
import com.google.inject.Module
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.logging.LoggerFactory
import it.plague.blog.config.guice.*
import it.plague.blog.verticle.HttpVerticle
import it.plague.blog.verticle.PgArticleVerticle
import java.util.concurrent.TimeUnit

object BlogApplication {

	private val log = LoggerFactory.getLogger(this.javaClass)

	@JvmStatic
	fun main(args: Array<String>) {
		log.info("Start Vert.x Home Blog")
		Vertx.vertx(options()).apply {
			val factory = guiceBootstrap()
			deploy(factory.prefix(), HttpVerticle::class.java.name)
			deploy(factory.prefix(), PgArticleVerticle::class.java.name)
		}
	}

	private fun Vertx.guiceBootstrap(): GuiceVerticleFactory {
		val injector = Guice.createInjector(*modules(this))
		val factory = GuiceVerticleFactory(injector)
		this.registerVerticleFactory(factory)
		return factory
	}

	private fun Vertx.deploy(prefix: String, verticleName: String) {
		this.deployVerticle("$prefix:$verticleName")
	}

	private fun modules(vertx: Vertx): Array<Module> {
		return arrayOf(
			VertxModule(vertx),
			ConfigModule(),
			WebModule(vertx),
			DatabaseModule(vertx),
			JacksonModule()
		)
	}

	private fun options(eventLoops: Int = 2 * Runtime.getRuntime().availableProcessors()): VertxOptions {
		return VertxOptions()
			.setEventLoopPoolSize(eventLoops)
			.setWarningExceptionTime(1)
			.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
			.setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
			.setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1))
	}
}
