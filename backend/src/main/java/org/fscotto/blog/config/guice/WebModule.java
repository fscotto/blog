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

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.CookieHandler;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import org.fscotto.blog.http.ext.MyFreeMarkerTemplateEngine;

public class WebModule extends PrivateModule {

  private final Vertx vertx;

  public WebModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  protected void configure() {
    // it MUST be override because it's abstract but I don't use classic container binding
  }

  @Provides
  @Singleton
  @Exposed
  public HttpServer getHttpServer() {
    var options = new HttpServerOptions();
    options.setCompressionSupported(true);
    return vertx.createHttpServer(options);
  }

  @Provides
  @Singleton
  @Exposed
  public WebClient getWebClient() {
    return WebClient.wrap(getHttpClient());
  }

  @Provides
  @Singleton
  @Exposed
  public HttpClient getHttpClient() {
    return vertx.createHttpClient();
  }

  @Provides
  @Singleton
  @Exposed
  public TemplateEngine getTemplateEngine() {
    return MyFreeMarkerTemplateEngine.create(vertx.getDelegate());
  }

  @Provides
  @Singleton
  @Exposed
  public Router getRouter() {
    var router = Router.router(vertx);
    router.route().handler(CookieHandler.create());
    router.route().handler(BodyHandler.create());
    router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
    router.route().handler(getCorsHandler());
    router.post().handler(BodyHandler.create());
    return router;
  }

  private CorsHandler getCorsHandler() {
    return CorsHandler.create("/")
      .allowCredentials(true)
      .allowedMethod(HttpMethod.GET)
      .allowedMethod(HttpMethod.POST)
      .allowedMethod(HttpMethod.PUT)
      .allowedMethod(HttpMethod.DELETE)
      .allowedMethod(HttpMethod.OPTIONS)
      .allowedHeader("X-PINGARUNER")
      .allowedHeader("Content-Type");
  }

}
