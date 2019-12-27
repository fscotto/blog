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

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fscotto.blog.config.EventBusAddress;
import org.fscotto.blog.config.HttpStatusCode;
import org.fscotto.blog.config.WebConstant;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Handler {

  public static void aboutHandler(RoutingContext context) {
    context.vertx()
      .eventBus()
      .<JsonObject>rxRequest(EventBusAddress.ABOUT_ADDR, new JsonObject())
      .doOnSuccess(reply -> {
        context
          .put("title", "About me")
          .response()
          .putHeader("Content-Type", WebConstant.APPLICATION_JSON)
          .setStatusCode(HttpStatusCode.OK)
          .end(Json.encodePrettily(reply.body()));
      })
      .doOnError(cause -> {
        log.error("Contact address {} failed ", EventBusAddress.ABOUT_ADDR, cause);
        context
          .response()
          .setStatusCode(HttpStatusCode.SERVER_ERROR)
          .end(makeError(cause));
      })
      .subscribe(SingleHelper.toObserver(Future.succeededFuture()));
  }

  private static String makeError(Throwable cause) {
    JsonObject error = new JsonObject()
      .put("error", cause.getClass().getName())
      .put("cause", cause.getLocalizedMessage())
      .put("stacktrace", ExceptionUtils.getStackTrace(cause));
    return Json.encodePrettily(error);
  }

}
