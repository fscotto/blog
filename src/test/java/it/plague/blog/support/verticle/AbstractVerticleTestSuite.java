package it.plague.blog.support.verticle;

import com.google.inject.Guice;
import com.google.inject.name.Names;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.junit5.VertxExtension;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.pgclient.PgPool;
import it.plague.blog.config.WebConstant;
import it.plague.blog.config.guice.GuiceVerticleFactory;
import it.plague.blog.database.ArticleDatabaseService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.Properties;

@Slf4j
@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class AbstractVerticleTestSuite {

  @Getter
  private final Vertx vertx;

  @Getter
  private final WebClient webClient;

  @Mock
  protected TemplateEngine templateEngine;

  @Mock
  protected ArticleDatabaseService articleDbService;

  @Mock
  protected PgPool client;

  private String prefix;

  public AbstractVerticleTestSuite() {
    this.vertx = Vertx.vertx();
    this.webClient = WebClient.create(vertx);
  }

  @BeforeEach
  void setUp() {
    log.info("Initializing Vert.x instance...");
    var injector = Guice.createInjector(binder -> {
      var properties = new Properties();
      properties.setProperty(WebConstant.HTTP_SERVER_HOST, "localhost");
      properties.setProperty(WebConstant.HTTP_SERVER_PORT, String.valueOf(getRandomPort(2000, 50000)));
      binder.bind(Vertx.class).toInstance(vertx);
      binder.bind(HttpServer.class).toInstance(vertx.createHttpServer());
      binder.bind(Router.class).toInstance(Router.router(vertx));
      binder.bind(TemplateEngine.class).toInstance(templateEngine);
      binder.bind(ArticleDatabaseService.class).toInstance(articleDbService);
      binder.bind(PgPool.class).toInstance(client);
      Names.bindProperties(binder, properties);
    });
    var factory = new GuiceVerticleFactory(injector);
    prefix = factory.prefix();
    vertx.registerVerticleFactory(factory);
    injector.injectMembers(this);
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

  private int getRandomPort(int start, int end) {
    var random = new SecureRandom();
    return random.nextInt(end) + start;
  }

}
