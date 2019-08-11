package it.plague.blog.config.guice

import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus

class VertxModule(private val vertx: Vertx) : PrivateModule() {

	override fun configure() {}

	@Provides
	@Singleton
	@Exposed
	fun provideVertx(): Vertx {
		return vertx;
	}

	@Provides
	@Singleton
	@Exposed
	fun provideEventBus(): EventBus {
		return vertx.eventBus()
	}
}
