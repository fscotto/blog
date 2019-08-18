package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import it.plague.blog.database.ArticleDatabaseService;
import it.plague.blog.domain.Article;
import it.plague.blog.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class HttpVerticle extends AbstractVerticle {

	private final HttpServer httpServer;
	private final Router router;
	private final ArticleDatabaseService dbService;
	private final TemplateEngine templateEngine;
	private final String host;
	private final String port;

	@Inject
	public HttpVerticle(HttpServer httpServer, Router router, TemplateEngine templateEngine, ArticleDatabaseService dbService,
											@Named(Constant.HTTP_SERVER_HOST) String host, @Named(Constant.HTTP_SERVER_PORT) String port) {
		this.httpServer = httpServer;
		this.router = router;
		this.dbService = dbService;
		this.templateEngine = templateEngine;
		this.host = host;
		this.port = port;
	}

	@Override
	public void start(Promise<Void> promise) {
		log.info("Setting routing urls handled...");
		router.get("/").handler(this::indexHandler);

		log.info("Try to start WebServer on " + host + ":" + port);
		httpServer
			.requestHandler(router)
			.rxListen(NumberUtils.toInt(port))
			.subscribe(success -> {
					log.info("Server listening on " + host + ":" + port);
					promise.complete();
				},
				cause -> {
					log.error("Could not start a HTTP server", cause);
					promise.fail(cause);
				});
	}

	private void indexHandler(RoutingContext context) {
		dbService.fetchAllArticles(reply -> {
			if (reply.succeeded()) {

				context.put("title", "Home");
				context.put("articles", reply.result()
					.stream()
					.filter(Objects::nonNull)
					.filter(obj -> obj instanceof JsonObject)
					.map(obj -> (JsonObject) obj)
					.map(jsonObject -> new Article(jsonObject))
					.collect(Collectors.toList()));

				templateEngine.render(context.data(), "templates/index", page -> {
					if (page.succeeded()) {
						context.response().putHeader("Content-Type", "text/html");
						context.response().end(page.result().toString());
					} else {
						log.error("Page loading error", page.cause());
						context.fail(page.cause());
					}
				});
			} else {
				context.fail(reply.cause());
			}
		});
	}
}
