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

package org.fscotto.blog.web;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fscotto.blog.config.HttpStatusCode;
import org.fscotto.blog.config.WebConstant;
import org.fscotto.blog.database.ArticleDatabaseService;
import org.fscotto.blog.domain.Article;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BlogResource extends AbstractVerticle {
  private final HttpServer server;
  private final Router router;
  private final ArticleDatabaseService articleDbService;
  private final String host;
  private final String port;

  @Inject
  public BlogResource(HttpServer server, Router router,
                      ArticleDatabaseService articleDbService,
                      @Named(WebConstant.HTTP_SERVER_HOST) String host,
                      @Named(WebConstant.HTTP_SERVER_PORT) String port) {
    this.server = server;
    this.router = router;
    this.articleDbService = articleDbService;
    this.host = host;
    this.port = port;
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Try to start WebServer on " + host + ":" + port);
    final var myRouter = router.mountSubRouter("/api", getApiRoute());
    server
      .requestHandler(myRouter)
      .rxListen(NumberUtils.toInt(port))
      .timeout(20, TimeUnit.SECONDS)
      .subscribe(SingleHelper.toObserver(result -> {
        if (result.succeeded()) {
          log.info("Server listening on " + host + ":" + port);
          promise.complete();
        } else {
          promise.fail("Could not start a HTTP server: " + result.cause().getMessage());
        }
      }));
  }

  @Override
  public void stop(Promise<Void> promise) {
    server
      .rxClose()
      .subscribe(CompletableHelper.toObserver(close -> {
        if (close.succeeded()) {
          log.info("Shutdown HTTP Server successful!!!");
          promise.handle(Future.succeededFuture());
        } else {
          log.error("Shutdown HTTP Server failed", close.cause());
          promise.handle(Future.failedFuture(close.cause()));
        }
      }));
  }

  private Router getApiRoute() {
    final var apiRouter = Router.router(Vertx.vertx());
    apiRouter.get("/articles")
      .produces(WebConstant.APPLICATION_JSON)
      .handler(this::indexHandler);
    apiRouter.get("/article/:id")
      .produces(WebConstant.APPLICATION_JSON)
      .handler(this::articleHandler);
    apiRouter.get("/about")
      .produces(WebConstant.APPLICATION_JSON)
      .handler(Handler::aboutHandler);
    return apiRouter;
  }

  void indexHandler(RoutingContext context) {
    articleDbService.fetchAllArticles(reply -> {
      if (reply.succeeded()) {
        final var articles = reply.result();
        context
          .put("title", "Home")
          .put("viewPagination", articles.size() > 10)
          .response()
          .putHeader("Content-Type", WebConstant.APPLICATION_JSON)
          .setStatusCode(HttpStatusCode.OK)
          .end(Json.encodePrettily(articles));
      } else {
        log.error("Loading index failed", reply.cause());
        context
          .response()
          .setStatusCode(HttpStatusCode.SERVER_ERROR)
          .end(makeError(reply.cause()));
      }
    });
  }

  void articleHandler(RoutingContext context) {
    var id = NumberUtils.toLong(context.request().getParam("id"));
    articleDbService.fetchArticle(id, reply -> {
      if (reply.succeeded()) {
        Optional.ofNullable(reply.result().getJsonObject("article"))
          .map(Article::new)
          .ifPresentOrElse(article -> context
              .put("title", article.getTitle())
              .response()
              .putHeader("Content-Type", WebConstant.APPLICATION_JSON)
              .setStatusCode(HttpStatusCode.OK)
              .end(Json.encodePrettily(article)),
            () -> context
              .response()
              .setStatusCode(HttpStatusCode.NOT_FOUND)
              .end(Json.encodePrettily(new JsonObject()))
          );
      } else {
        log.error("Loading article failed", reply.cause());
        context
          .response()
          .setStatusCode(HttpStatusCode.SERVER_ERROR)
          .end(makeError(reply.cause()));
      }
    });
  }

  private String makeError(Throwable cause) {
    JsonObject error = new JsonObject()
      .put("error", cause.getClass().getName())
      .put("cause", cause.getLocalizedMessage())
      .put("stacktrace", ExceptionUtils.getStackTrace(cause));
    return Json.encodePrettily(error);
  }

}
