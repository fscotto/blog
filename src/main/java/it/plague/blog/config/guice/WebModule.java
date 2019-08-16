package it.plague.blog.config.guice;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import it.plague.blog.handler.IndexHandler;

public class WebModule extends PrivateModule {

	private final Vertx vertx;

	public WebModule(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@Exposed
	public HttpServer getHttpServer() {
		var options = new HttpServerOptions();
		options.setCompressionSupported(true);
		return vertx.createHttpServer(options);
	}

	@Provides
	@Singleton
	@Exposed
	public HttpClient getHttpClient() {
		return vertx.createHttpClient();
	}

	@Provides
	@Singleton
	@Exposed
	public Router getRouter() {
		var router = Router.router(vertx);
		router.route().handler(getCorsHandler());
		router.route("/").handler(new IndexHandler());
		return router;
	}

	private Handler<RoutingContext> getCorsHandler() {
		return CorsHandler.create("/")
			.allowCredentials(true)
			.allowedMethod(HttpMethod.GET)
			.allowedMethod(HttpMethod.POST)
			.allowedMethod(HttpMethod.OPTIONS)
			.allowedHeader("X-PINGARUNER")
			.allowedHeader("Content-Type");
	}
}
