package it.plague.blog.module

import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import it.plague.blog.handler.IndexHandler

class RouterModule(private val vertx: Vertx) : PrivateModule() {

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
	fun router(): Router {
		val router = Router.router(vertx)
		router.route("/").handler(IndexHandler())
		return router
	}
}
