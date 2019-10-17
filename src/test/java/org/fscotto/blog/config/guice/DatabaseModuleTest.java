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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import org.fscotto.blog.database.ArticleDatabaseService;
import org.fscotto.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@UnitTest
class DatabaseModuleTest {

  @Inject
  PgPool client;

  @Inject
  ArticleDatabaseService dbService;

  @Inject
  PgConnectOptions options;

  @BeforeEach
  void setUp() {
    Vertx vertx = Vertx.vertx();
    Guice.createInjector(new DatabaseModule(vertx), BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  @DisplayName("Should be inject Postgres db client")
  void shouldBeInjectPgPool() {
    assertNotNull(client);
  }

  @Test
  @DisplayName("Should be inject ArticleDatabaseService class")
  void shouldBeInjectPgConnectioOptions() {
    assertNotNull(options);
  }

  @Test
  @DisplayName("Should be inject ArticleDatabaseService class")
  void shouldBeInjectArticleDatabaseService() {
    assertNotNull(dbService);
  }

}