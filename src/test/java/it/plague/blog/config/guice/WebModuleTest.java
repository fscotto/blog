package it.plague.blog.config.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import it.plague.blog.http.ext.MyFreeMarkerTemplateEngine;
import it.plague.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class WebModuleTest {

  @Inject
  HttpServer httpServer;

  @Inject
  WebClient webClient;

  @Inject
  HttpClient httpClient;

  @Inject
  TemplateEngine templateEngine;

  @Inject
  Router router;

  @BeforeEach
  void setUp() {
    Vertx vertx = Vertx.vertx();
    Guice.createInjector(new WebModule(vertx), BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  @DisplayName("Should be inject HTTP server")
  void shouldBeInjectHttpServer() {
    assertNotNull(httpServer);
  }

  @Test
  @DisplayName("Should be inject Web client")
  void shouldBeInjectWebClient() {
    assertNotNull(webClient);
  }

  @Test
  @DisplayName("Should be inject HTTP client")
  void shouldBeInjectHttpClient() {
    assertNotNull(httpClient);
  }

  @Test
  @DisplayName("Should be inject a template engine")
  void shouldBeInjectTemplateEngine() {
    assertNotNull(templateEngine);
  }

  @Test
  @DisplayName("Should be FreeMarker template engine instance")
  void shouldBeFreeMarkerTemplateEngineInstance() {
    assertTrue(templateEngine instanceof MyFreeMarkerTemplateEngine);
  }

  @Test
  @DisplayName("Should be inject router")
  void shouldBeInjectRouter() {
    assertNotNull(router);
  }

}