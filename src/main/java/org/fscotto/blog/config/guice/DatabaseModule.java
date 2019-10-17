package org.fscotto.blog.config.guice;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.fscotto.blog.config.DatabaseConstant;
import org.fscotto.blog.config.EventBusAddress;
import org.fscotto.blog.database.ArticleDatabaseService;

public class DatabaseModule extends PrivateModule {

  private final Vertx vertx;

  public DatabaseModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  protected void configure() {
    // it MUST be override because it's abstract but I don't use classic container binding
  }

  @Exposed
  @Singleton
  @Provides
  public PgPool getPool() {
    return PgPool.pool(this.vertx, getConnectOptions(), getPoolOptions());
  }

  @Exposed
  @Singleton
  @Provides
  public PgConnectOptions getConnectOptions() {
    return PgConnectOptions.fromEnv();
  }

  @Exposed
  @Singleton
  @Provides
  public ArticleDatabaseService getArticleDatabaseServiceProxy() {
    return ArticleDatabaseService.createProxy(vertx, EventBusAddress.ARTICLE_DB_SERVICE);
  }

  @Exposed
  @Singleton
  @Provides
  public MongoClient getMongoClient() {
    JsonObject config = new JsonObject()
      .put("connection_string", System.getenv(DatabaseConstant.MONGODB_URI));
    return MongoClient.createNonShared(vertx, config);
  }

  private PoolOptions getPoolOptions() {
    return new PoolOptions();
  }

}
