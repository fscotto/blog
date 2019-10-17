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
