package it.plague.blog;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.reactivex.core.Vertx;
import it.plague.blog.config.guice.*;
import it.plague.blog.database.ArticleDatabaseVerticle;
import it.plague.blog.database.InformationDatabaseVerticle;
import it.plague.blog.http.ArticleWebVerticle;
import it.plague.blog.http.InformationWebVerticle;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BlogApplication {

  private static Vertx vertx;
  private static VerticleFactory factory;

  public static void main(String[] args) {
    log.info("Start Vert.x Home Blog");
    vertx = Vertx.vertx(getOptions());
    guiceBootstrap();
    deployVerticle(ArticleWebVerticle.class.getName());
    deployVerticle(ArticleDatabaseVerticle.class.getName());
    deployVerticle(InformationWebVerticle.class.getName());
    deployVerticle(InformationDatabaseVerticle.class.getName());
  }

  private static void guiceBootstrap() {
    var injector = Guice.createInjector(getModules());
    factory = new GuiceVerticleFactory(injector);
    vertx.registerVerticleFactory(factory);
  }

  private static Module[] getModules() {
    return new Module[]{
      new VertxModule(vertx),
      new ConfigModule(),
      new WebModule(vertx),
      new DatabaseModule(vertx)
    };
  }

  private static void deployVerticle(String verticleName) {
    log.debug(String.format("Deploy verticle with name %s", verticleName));
    vertx.deployVerticle(factory.prefix() + ":" + verticleName);
  }

  private static VertxOptions getOptions() {
    var eventLoops = 2 * Runtime.getRuntime().availableProcessors();
    return new VertxOptions()
      .setEventLoopPoolSize(eventLoops)
      .setWarningExceptionTime(1)
      .setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(1))
      .setMaxWorkerExecuteTime(TimeUnit.SECONDS.toNanos(1))
      .setBlockedThreadCheckInterval(TimeUnit.SECONDS.toMillis(1));
  }

}
