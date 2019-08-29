package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import it.plague.blog.config.EventBusAddress;
import it.plague.blog.config.WebConstant;
import it.plague.blog.support.annotation.UnitTest;
import it.plague.blog.support.verticle.AbstractVerticleTestSuite;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@UnitTest
class InformationWebVerticleTest extends AbstractVerticleTestSuite {

  private static final String ABOUT_ADDR = "/about";
  private static final String CURRICULUM_ADDR = "/cv";
  private static final String CONTACT_ADDR = "/contacts";

  @Inject
  @Named(WebConstant.HTTP_SERVER_HOST)
  String host;

  @Inject
  @Named(WebConstant.HTTP_SERVER_PORT)
  String port;

  private static Map<String, String> responseBody;

  static {
    responseBody = Map.of(
      ABOUT_ADDR, ResponseBody.ABOUT_MSG,
      CURRICULUM_ADDR, ResponseBody.CURRICULUM_MSG,
      CONTACT_ADDR, ResponseBody.CONTACT_MSG
    );
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
    final var expected = responseBody(ABOUT_ADDR);
    mockingDependencyMethodCall(expected);
    getWebClient()
      .get(NumberUtils.toInt(port), host, ABOUT_ADDR)
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
      .get(NumberUtils.toInt(port), host, ABOUT_ADDR)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNull();
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok calling /cv address")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingCurriculumAddress(VertxTestContext context) {
    final var expected = responseBody(CURRICULUM_ADDR);
    mockingDependencyMethodCall(expected);
    getWebClient()
      .get(NumberUtils.toInt(port), host, CURRICULUM_ADDR)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(expected);
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok calling /cv address with empty content")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingCurriculumAddressWithEmptyContent(VertxTestContext context) {
    mockingDependencyMethodCall("");
    getWebClient()
      .get(NumberUtils.toInt(port), host, CURRICULUM_ADDR)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNull();
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok calling /contacts address")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingContactAddress(VertxTestContext context) {
    final var expected = responseBody(CONTACT_ADDR);
    mockingDependencyMethodCall(expected);
    getWebClient()
      .get(NumberUtils.toInt(port), host, CONTACT_ADDR)
      .as(BodyCodec.string())
      .send(context.succeeding(response -> {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(expected);
        context.completeNow();
      }));
  }

  @Test
  @DisplayName("Should be status ok calling /contacts address with empty content")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldBeStatusOkCallingContactAddressWithEmptyContent(VertxTestContext context) {
    mockingDependencyMethodCall("");
    getWebClient()
      .get(NumberUtils.toInt(port), host, CONTACT_ADDR)
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
      registerInfoConsumer(EventBusAddress.CURRICULUM_ADDR, ResponseBody.CURRICULUM_MSG);
      registerInfoConsumer(EventBusAddress.CONTACT_ADDR, ResponseBody.CONTACT_MSG);
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
    static final String CURRICULUM_MSG = "there is curriculum";
    static final String CONTACT_MSG = "there is contacts";
  }

}
