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

package org.fscotto.blog.database;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import org.fscotto.blog.domain.Article;

@ProxyGen
@VertxGen
public interface ArticleDatabaseService {

  @GenIgnore
  static ArticleDatabaseService create(Vertx vertx, PgPool client, Handler<AsyncResult<ArticleDatabaseService>> readyHandler) {
    return new ArticleDatabaseServiceImpl(vertx, client, readyHandler);
  }

  @GenIgnore
  static ArticleDatabaseService createProxy(Vertx vertx, String address) {
    return new ArticleDatabaseServiceVertxEBProxy(vertx.getDelegate(), address);
  }

  @Fluent
  ArticleDatabaseService fetchAllArticles(Handler<AsyncResult<JsonArray>> resultHandler);

  @Fluent
  ArticleDatabaseService fetchArticle(Long id, Handler<AsyncResult<JsonObject>> resultHandler);

  @Fluent
  ArticleDatabaseService createArticle(Article article, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ArticleDatabaseService saveArticle(Article article, Handler<AsyncResult<Void>> resultHandler);

  @Fluent
  ArticleDatabaseService deleteArticle(Long id, Handler<AsyncResult<Void>> resultHandler);
}
