package org.fscotto.blog.database;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxTestContext;
import org.fscotto.blog.config.WebConstant;
import org.fscotto.blog.support.annotation.UnitTest;
import org.fscotto.blog.support.verticle.AbstractVerticleTestSuite;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.TimeUnit;

@UnitTest
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
