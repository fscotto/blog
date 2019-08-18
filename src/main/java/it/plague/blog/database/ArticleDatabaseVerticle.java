package it.plague.blog.database;

import com.google.inject.Inject;
import io.vertx.core.Promise;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import it.plague.blog.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleDatabaseVerticle extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(ArticleDatabaseVerticle.class);

	private final PgPool client;

	@Inject
	public ArticleDatabaseVerticle(PgPool client) {
		this.client = client;
	}

	@Override
	public void start(Promise<Void> promise) {
		log.info("Start " + this.getClass().getCanonicalName());
		ArticleDatabaseService.create(vertx, client, ready -> {
			if (ready.succeeded()) {
				var binder = new ServiceBinder(vertx.getDelegate());
				binder
					.setAddress(Constant.CONFIG_BLOGDB_QUEUE)
					.register(ArticleDatabaseService.class, ready.result());
				promise.complete();
			} else {
				promise.fail(ready.cause());
			}
		});
		log.info("...loading end");
	}
}
