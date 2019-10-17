/*
 * Copyright (C) 2019 Fabio Scotto di Santolo <fabio.scottodisantolo@gmail.com>
 *
 * This file is part of plague-blog.
 *
 * plague-blog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * plague-blog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with plague-blog.  If not, see <http://www.gnu.org/licenses/>.
 */

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
