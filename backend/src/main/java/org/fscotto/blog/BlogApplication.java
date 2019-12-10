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

package org.fscotto.blog;

import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.fscotto.blog.config.guice.*;
import org.fscotto.blog.database.ArticleDatabaseVerticle;
import org.fscotto.blog.database.InformationDatabaseVerticle;
import org.fscotto.blog.http.ArticleWebVerticle;
import org.fscotto.blog.http.InformationWebVerticle;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BlogApplication {

  public static void main(String[] args) {
    log.info("Start Plague's Blog");
    var vertx = Vertx.vertx(getOptions());
    GuiceRunner.newInstance(vertx)
      .registerModule(new VertxModule(vertx))
      .registerModule(new ConfigModule())
      .registerModule(new WebModule(vertx))
      .registerModule(new DatabaseModule(vertx))
      .deployVerticle(ArticleWebVerticle.class.getName())
      .deployVerticle(ArticleDatabaseVerticle.class.getName())
      .deployVerticle(InformationWebVerticle.class.getName())
      .deployVerticle(InformationDatabaseVerticle.class.getName())
      .start();
    log.info("Plague's Blog started");
  }

  private static VertxOptions getOptions() {
    var eventLoops = 2 * Runtime.getRuntime().availableProcessors();
    return new VertxOptions()
      .setEventLoopPoolSize(eventLoops)
      .setWarningExceptionTime(1)
      .setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
      .setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
      .setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1));
  }

}
