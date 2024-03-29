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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import io.vertx.reactivex.pgclient.PgPool;
import org.apache.commons.lang3.math.NumberUtils;
import org.fscotto.blog.config.WebConstant;
import org.fscotto.blog.database.ArticleDatabaseService;
import org.fscotto.blog.domain.Article;
import org.fscotto.blog.domain.Author;
import org.fscotto.blog.domain.User;
import org.fscotto.blog.support.annotation.UnitTest;
import org.fscotto.blog.support.verticle.AbstractVerticleTestSuite;
import org.fscotto.blog.util.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@UnitTest
class ArticleWebVerticleTest extends AbstractVerticleTestSuite {
  private static final String INDEX_URL = "/";

  private static final String ARTICLE_URL = "/article/1";

  @Inject
  @Named(WebConstant.HTTP_SERVER_HOST)
  String host;

  @Inject
  @Named(WebConstant.HTTP_SERVER_PORT)
  String port;

  @Mock
  protected TemplateEngine templateEngine;

  @Mock
  protected ArticleDatabaseService articleDbService;

  @Mock
  protected PgPool client;

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
      binder.bind(ArticleDatabaseService.class).toInstance(articleDbService);
      binder.bind(PgPool.class).toInstance(client);
    });
  }

  @BeforeEach
  @Timeout(value = 3, timeUnit = TimeUnit.SECONDS)
  void setUp(VertxTestContext context) {
    deployVerticle(ArticleWebVerticle.class.getName(), context.succeeding(id -> context.completeNow()));
  }

  @Test
  @DisplayName("Should be status ok call index if fetch 2 record from database")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallIndexIfFetchTwoRecordFromDB(VertxTestContext context) {
    mockingDependencyMethodCall(getArticles());
    getWebClient()
      .get(NumberUtils.toInt(port), host, INDEX_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.getDelegate().statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(getPage());
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok call index if fetch 0 record from database")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallIndexIfFetchZeroRecordFromDB(VertxTestContext context) {
    mockingDependencyMethodCall(new JsonArray());
    getWebClient()
      .get(NumberUtils.toInt(port), host, INDEX_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.getDelegate().statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(getPage());
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status 500 call index if JsonArray is null")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusInternalServerErrorCallIndexIfJsonArrayIsNull(VertxTestContext context) {
    mockingDependencyMethodCall(null);
    getWebClient()
      .get(NumberUtils.toInt(port), host, INDEX_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.getDelegate().statusCode()).isEqualTo(500);
        assertThat(response.body()).isEqualTo("Internal Server Error");
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok call article endpoint")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallArticleEndpoint(VertxTestContext context) {
    mockingDependencyMethodCall(getArticles());
    getWebClient()
      .get(NumberUtils.toInt(port), host, ARTICLE_URL)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.getDelegate().statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(getPage());
        context.completeNow();
      }));
  }

  private void mockingDependencyMethodCall(JsonArray data) {
    lenient().when(articleDbService.fetchAllArticles(any(Handler.class)))
      .thenAnswer(invocation -> {
        ((Handler<AsyncResult<JsonArray>>) invocation.getArgument(0))
          .handle(Future.succeededFuture(data));
        return null;
      });

    lenient().when(articleDbService.fetchArticle(anyLong(), any(Handler.class)))
      .thenAnswer(invocation -> {
        ((Handler<AsyncResult<JsonObject>>) invocation.getArgument(1))
          .handle(Future.succeededFuture(new JsonObject()
            .put("found", Boolean.TRUE)
            .put("article", data.getJsonObject(0))));
        return null;
      });

    lenient().doAnswer(invocation -> {
      ((Handler<AsyncResult<Buffer>>) invocation.getArgument(2))
        .handle(Future.succeededFuture(Buffer.buffer(getPage())));
      return null;
    })
      .when(templateEngine)
      .render(anyMap(), anyString(), any(Handler.class));
  }

  private JsonArray getArticles() {
    return Stream.of(
      Builder.of(Article::new)
        .with(Article::setId, 1L)
        .with(Article::setCreatedBy,
          new Author(1L, "J.R", "Tolkien",
            new User(1L, "user", "secret")))
        .with(Article::setCreated, LocalDateTime.of(2019, Month.JANUARY, 01, 00, 00, 00))
        .with(Article::setTitle, "Lord of the rings")
        .with(Article::setContent, "")
        .build().toJson(),
      Builder.of(Article::new)
        .with(Article::setId, 2L)
        .with(Article::setCreatedBy,
          new Author(2L, "Isaac", "Asimov",
            new User(2L, "user", "secret")))
        .with(Article::setCreated, LocalDateTime.of(2019, Month.JANUARY, 01, 00, 00, 00))
        .with(Article::setTitle, "The Secret of the Universe")
        .with(Article::setContent, "")
        .build().toJson()
    )
      .collect(
        Collector.of(
          JsonArray::new,
          JsonArray::add,
          JsonArray::add)
      );
  }

  private String getPage() {
    var sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<head>");
    sb.append("<title>");
    sb.append("Test Page");
    sb.append("</title>");
    sb.append("</head>");
    sb.append("<body>");
    sb.append("<h1>");
    sb.append("Test from Vert.x");
    sb.append("</h1>");
    sb.append("</body>");
    sb.append("</html>");
    return sb.toString();
  }

}