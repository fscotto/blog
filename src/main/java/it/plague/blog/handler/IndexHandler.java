package it.plague.blog.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class IndexHandler implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(IndexHandler.class);

	@Override
	public void handle(RoutingContext context) {
		log.debug("Received request on /");
		context.response()
			.setStatusCode(HttpResponseStatus.OK.code())
			.end("<h1>Hello from Plague's Blog Homepage</h1>");
	}
}
