package it.plague.blog.database;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;

public class ArticleDatabaseVerticle extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(ArticleDatabaseVerticle.class);

	private final PgPool client;
	private final EventBus eventBus;

	@Inject
	public ArticleDatabaseVerticle(PgPool client, EventBus eventBus) {
		this.client = client;
		this.eventBus = eventBus;
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
		log.info("Start " + this.getClass().getCanonicalName());
		eventBus
			.consumer("articles.getAll")
			.handler(msg -> {
				getAllArticles(ar -> {
					if (ar.succeeded()) {
						msg.reply(ar.result());
					} else {
						msg.fail(1, ar.cause().getMessage());
						log.error("Something went wrong while retrieving all artices " + ar.cause().getMessage());
					}
				});
			});
	}

	private void getAllArticles(Handler<AsyncResult<JsonArray>> handler) {

	}
}
