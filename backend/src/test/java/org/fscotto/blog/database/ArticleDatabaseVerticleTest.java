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
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxTestContext;
import org.fscotto.blog.config.WebConstant;
import org.fscotto.blog.support.annotation.IntegrationTest;
import org.fscotto.blog.support.verticle.AbstractVerticleTestSuite;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.TimeUnit;

@IntegrationTest
class ArticleDatabaseVerticleTest extends AbstractVerticleTestSuite {

  @Inject
  @Named(WebConstant.HTTP_SERVER_HOST)
  String host;

  @Inject
  @Named(WebConstant.HTTP_SERVER_PORT)
  String port;

  @Override
  protected Injector getInjector() {
    return null;
  }

  @BeforeEach
  @Timeout(value = 3, timeUnit = TimeUnit.SECONDS)
  void setUp(VertxTestContext context) {
    deployVerticle(ArticleDatabaseVerticle.class.getName(), context.succeeding(id -> context.completeNow()));
  }

}
