package it.plague.blog.database;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.vertx.junit5.VertxTestContext;
import it.plague.blog.config.WebConstant;
import it.plague.blog.support.annotation.UnitTest;
import it.plague.blog.support.verticle.AbstractVerticleTestSuite;
import org.junit.jupiter.api.BeforeEach;

@UnitTest
class InformationDatabaseVerticleTest extends AbstractVerticleTestSuite {
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
  void setUp(VertxTestContext context) {
    deployVerticle(InformationDatabaseVerticle.class.getName(), context.succeeding(ar -> context.completeNow()));
  }

}
