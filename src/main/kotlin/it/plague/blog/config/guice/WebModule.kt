package it.plague.blog.config.guice

import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import it.plague.blog.handler.IndexHandler

class WebModule(private val vertx: Vertx) : PrivateModule() {

	override fun configure() {}

	@Provides
	@Singleton
	@Exposed
	fun httpServer(): HttpServer {
		val options = HttpServerOptions()
		options.isCompressionSupported = true
		return vertx.createHttpServer(options)
	}

	@Provides
	@Singleton
	@Exposed
	fun httpClient(): HttpClient {
		return vertx.createHttpClient()
	}

	@Provides
	@Singleton
	@Exposed
	fun router(): Router {
		val router = Router.router(vertx)
		router.route().handler(corsHandler())
		router.route("/").handler(IndexHandler())
		return router
	}

	private fun corsHandler(): CorsHandler {
		return CorsHandler.create("/")
			.allowCredentials(true)
			.allowedMethod(HttpMethod.GET)
			.allowedMethod(HttpMethod.POST)
			.allowedMethod(HttpMethod.OPTIONS)
			.allowedHeader("X-PINGARUNER")
			.allowedHeader("Content-Type")
	}
}
