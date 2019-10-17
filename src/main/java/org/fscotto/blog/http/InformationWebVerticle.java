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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.fscotto.blog.config.EventBusAddress;
import org.fscotto.blog.config.WebConstant;

@Slf4j
public class InformationWebVerticle extends AbstractHttpArticle {

  @Inject
  public InformationWebVerticle(HttpServer httpServer, Router router, TemplateEngine templateEngine,
                                @Named(WebConstant.HTTP_SERVER_HOST) String host, @Named(WebConstant.HTTP_SERVER_PORT) String port) {
    super(httpServer, router, templateEngine, host, port);
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Setting routing urls handled from InformationWebVerticle...");
    router.get("/about").handler(this::aboutHandler);
    super.start(promise);
  }

  private void aboutHandler(RoutingContext context) {
    vertx
      .eventBus()
      .request(EventBusAddress.ABOUT_ADDR, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          context.put("title", "About me");
          context.put("info", reply.result().body());
          renderPage(context, "about");
        } else {
          log.error(String.format("Contact address %s failed", EventBusAddress.ABOUT_ADDR), reply.cause());
          context.fail(reply.cause());
        }
      });
  }

}
