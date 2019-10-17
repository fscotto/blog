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