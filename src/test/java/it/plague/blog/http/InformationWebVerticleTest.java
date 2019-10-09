package it.plague.blog.http;

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
import it.plague.blog.config.EventBusAddress;
import it.plague.blog.config.WebConstant;
import it.plague.blog.support.annotation.UnitTest;
import it.plague.blog.support.verticle.AbstractVerticleTestSuite;
import org.apache.commons.lang3.math.NumberUtils;
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
