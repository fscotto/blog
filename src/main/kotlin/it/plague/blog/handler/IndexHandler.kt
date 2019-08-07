package it.plague.blog.handler

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Handler
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext

class IndexHandler : Handler<RoutingContext> {

	private val log = LoggerFactory.getLogger(this.javaClass)

	override fun handle(context: RoutingContext) {
		log.debug("Received request on /")
		context.response()
			.setStatusCode(HttpResponseStatus.OK.code())
			.end("<h1>Hello from Plague's Blog Homepage</h1>")
	}
}