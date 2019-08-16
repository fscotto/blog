package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import it.plague.blog.database.ArticleDatabaseService;
import it.plague.blog.util.Constant;

public class HttpVerticle extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(HttpVerticle.class);

	private final HttpServer httpServer;
	private final Router router;
	private final ArticleDatabaseService dbService;
	private final String host;
	private final String port;

	@Inject
	public HttpVerticle(HttpServer httpServer, Router router, ArticleDatabaseService dbService,
											@Named(Constant.HTTP_SERVER_HOST) String host, @Named(Constant.HTTP_SERVER_PORT) String port) {
		this.httpServer = httpServer;
		this.router = router;
		this.dbService = dbService;
		this.host = host;
		this.port = port;
	}

	@Override
	public void start(Promise<Void> promise) {
		log.info("Try to start WebServer on " + host + ":" + port);
		httpServer
			.requestHandler(router)
			.listen(Integer.parseInt(port));
		log.info("Server listening on " + host + ":" + port);
	}
}
