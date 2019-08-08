package it.plague.blog.config.guice

import com.google.inject.Injector
import io.vertx.core.Verticle
import io.vertx.core.VertxOptions
import io.vertx.core.spi.VerticleFactory
import java.util.concurrent.TimeUnit

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

fun options(eventLoops: Int = 2 * Runtime.getRuntime().availableProcessors()): VertxOptions {
	return VertxOptions()
		.setEventLoopPoolSize(eventLoops)
		.setWarningExceptionTime(1)
		.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
		.setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
		.setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1))
}
