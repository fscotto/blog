package org.fscotto.blog.database;

import com.google.inject.Inject;
import io.vertx.core.Promise;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import lombok.extern.slf4j.Slf4j;
import org.fscotto.blog.config.EventBusAddress;

@Slf4j
public class ArticleDatabaseVerticle extends AbstractVerticle {

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
          .setAddress(EventBusAddress.ARTICLE_DB_SERVICE)
          .register(ArticleDatabaseService.class, ready.result());
        promise.complete();
      } else {
        promise.fail(ready.cause());
      }
    });
    log.info("...loading end");
  }
}
