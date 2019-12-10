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
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import org.fscotto.blog.http.ext.MyFreeMarkerTemplateEngine;
import org.fscotto.blog.support.annotation.UnitTest;
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