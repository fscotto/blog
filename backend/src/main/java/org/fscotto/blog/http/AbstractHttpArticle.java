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

package org.fscotto.blog.http;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractHttpArticle extends AbstractVerticle {

  protected final HttpServer httpServer;
  protected final Router router;
  protected final TemplateEngine templateEngine;
  protected final String host;
  protected final String port;

  public AbstractHttpArticle(HttpServer httpServer, Router router, TemplateEngine templateEngine, String host, String port) {
    this.httpServer = httpServer;
    this.router = router;
    this.templateEngine = templateEngine;
    this.host = host;
    this.port = port;
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Try to start WebServer on " + host + ":" + port);
    httpServer
      .requestHandler(router)
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
    httpServer
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

  protected void renderPage(RoutingContext context, String template) {
    var layoutSelector = new LayoutSelector("layout3", template);
    templateEngine.render(context.data(), layoutSelector.getTemplate(), page -> {
      if (page.succeeded()) {
        context.response()
          .putHeader("Content-Type", "text/html")
          .end(page.result().toString());
      } else {
        log.error("Page loading error", page.cause());
        context.fail(page.cause());
      }
    });
  }

}
