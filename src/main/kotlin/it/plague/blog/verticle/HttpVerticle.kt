package it.plague.blog.verticle

import com.google.inject.Inject
import com.google.inject.name.Named
import io.vertx.core.http.HttpServer
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult

class HttpVerticle @Inject constructor(private val httpServer: HttpServer,
																			 private val router: Router,
																			 @Named("HOST") private val host: String,
																			 @Named("PORT") private val port: String) : CoroutineVerticle() {

	private val log = LoggerFactory.getLogger(this.javaClass)

	override suspend fun start() {
		log.info("Try to start WebServer on $host:$port")
		awaitResult<HttpServer> {
			httpServer
				.requestHandler(router)
				.listen(port.toInt(), it)
			log.info("Server listening on $host:$port")
		}
	}
}
