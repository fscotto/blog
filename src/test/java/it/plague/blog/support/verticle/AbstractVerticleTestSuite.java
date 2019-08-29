package it.plague.blog.support.verticle;

import com.google.inject.Injector;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.junit5.VertxExtension;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import it.plague.blog.config.guice.GuiceVerticleFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;

@Slf4j
@ExtendWith({VertxExtension.class, MockitoExtension.class})
public abstract class AbstractVerticleTestSuite {

  @Getter
  private final Vertx vertx;

  @Getter
  private final WebClient webClient;

  private String prefix;

  public AbstractVerticleTestSuite() {
    this.vertx = Vertx.vertx();
    this.webClient = WebClient.create(vertx);
  }

  @BeforeEach
  void setUp() {
    Injector injector = getInjector();
    initVerticleFactory(injector);
    injector.injectMembers(this);
  }

  protected abstract Injector getInjector();

  private void initVerticleFactory(Injector injector) {
    var factory = new GuiceVerticleFactory(injector);
    prefix = factory.prefix();
    vertx.registerVerticleFactory(factory);
  }

  @AfterEach
  void tearDown() {
    vertx.rxClose();
    log.info("...Vert.x close");
  }

  protected void deployVerticle(String verticleName, Handler<AsyncResult<String>> completionHandler) {
    log.debug(String.format("Deploy verticle with name %s", verticleName));
    vertx.rxDeployVerticle(prefix + ":" + verticleName)
      .subscribe(SingleHelper.toObserver(completionHandler));
  }

  protected void undeployVerticle(String deploymentID, Handler<AsyncResult<Void>> completionHandler) {
    log.debug(String.format("Undeploy verticle with deploymentID %s", deploymentID));
    vertx.rxUndeploy(deploymentID)
      .subscribe(CompletableHelper.toObserver(completionHandler));
  }

  protected int getRandomPort(int start, int end) {
    var random = new SecureRandom();
    return random.nextInt(end) + start;
  }

}
