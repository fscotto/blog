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

package org.fscotto.blog.http;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.apache.commons.lang3.math.NumberUtils;
import org.fscotto.blog.config.EventBusAddress;
import org.fscotto.blog.config.WebConstant;
import org.fscotto.blog.support.annotation.UnitTest;
import org.fscotto.blog.support.verticle.AbstractVerticleTestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@UnitTest
class InformationWebVerticleTest extends AbstractVerticleTestSuite {

  private static final String ABOUT_URL = "/about";
  private static final String CURRICULUM_URL = "/cv";
  private static final String CONTACT_URL = "/contacts";

  @Inject
  @Named(WebConstant.HTTP_SERVER_HOST)
  String host;

  @Inject
  @Named(WebConstant.HTTP_SERVER_PORT)
  String port;

  @Mock
  protected TemplateEngine templateEngine;

  private static Map<String, String> responseBody;

  static {
    responseBody = Map.of(
      ABOUT_URL, ResponseBody.ABOUT_MSG
    );
  }

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(binder -> {
      var properties = new Properties();
      properties.setProperty(WebConstant.HTTP_SERVER_HOST, "localhost");
      properties.setProperty(WebConstant.HTTP_SERVER_PORT, String.valueOf(getRandomPort(2000, 50000)));
      Names.bindProperties(binder, properties);
      binder.bind(HttpServer.class).toInstance(getVertx().createHttpServer());
      binder.bind(Router.class).toInstance(Router.router(getVertx()));
      binder.bind(TemplateEngine.class).toInstance(templateEngine);
      binder.bind(TemplateEngine.class).toInstance(templateEngine);
    });
  }

  @BeforeEach
  @Timeout(value = 3, timeUnit = TimeUnit.SECONDS)
  void setUp(VertxTestContext context) {
    deployVerticle(ConsumerVerticleMock.class.getName(), context.succeeding(id -> context.completeNow()));
    deployVerticle(InformationWebVerticle.class.getName(), context.succeeding(id -> context.completeNow()));
  }

  @Test
  @DisplayName("Should be status ok calling /about address")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingAboutAddress(VertxTestContext context) {
    final var expected = responseBody(ABOUT_URL);
    mockingDependencyMethodCall(expected);
    getWebClient()
      .get(NumberUtils.toInt(port), host, ABOUT_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(expected);
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok calling /about address with empty content")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingAboutAddressWithEmptyContent(VertxTestContext context) {
    mockingDependencyMethodCall("");
    getWebClient()
      .get(NumberUtils.toInt(port), host, ABOUT_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNull();
        context.completeNow();
      }));
  }

  private void mockingDependencyMethodCall(final String content) {
    lenient().doAnswer(invocation -> {
      ((Handler<AsyncResult<Buffer>>) invocation.getArgument(2))
        .handle(Future.succeededFuture(Buffer.buffer(content)));
      return null;
    })
      .when(templateEngine)
      .render(anyMap(), anyString(), any(Handler.class));
  }

  private String responseBody(final String address) {
    return responseBody.get(address);
  }

  private static class ConsumerVerticleMock extends AbstractVerticle {

    @Override
    public void start(Promise<Void> promise) {
      registerInfoConsumer(EventBusAddress.ABOUT_ADDR, ResponseBody.ABOUT_MSG);
    }

    private void registerInfoConsumer(final String address, final String content) {
      getVertx()
        .eventBus()
        .<JsonObject>consumer(address)
        .handler(message -> message.reply(content));
    }

  }

  private static class ResponseBody {
    static final String ABOUT_MSG = "there is about";
  }

}
