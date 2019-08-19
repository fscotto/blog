package it.plague.blog.config.guice;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import it.plague.blog.config.Constant;
import it.plague.blog.database.ArticleDatabaseService;

public class DatabaseModule extends PrivateModule {

  private final Vertx vertx;

  public DatabaseModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  protected void configure() {
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
    return ArticleDatabaseService.createProxy(vertx, Constant.CONFIG_BLOGDB_QUEUE);
  }

  private PoolOptions getPoolOptions() {
    return new PoolOptions();
  }
}
