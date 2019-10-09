package it.plague.blog;

import io.vertx.core.VertxOptions;
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

  public static void main(String[] args) {
    log.info("Start Plague's Blog");
    var vertx = Vertx.vertx(getOptions());
    GuiceRunner.newInstance(vertx)
      .registerModule(new VertxModule(vertx))
      .registerModule(new ConfigModule())
      .registerModule(new WebModule(vertx))
      .registerModule(new DatabaseModule(vertx))
      .deployVerticle(ArticleWebVerticle.class.getName())
      .deployVerticle(ArticleDatabaseVerticle.class.getName())
      .deployVerticle(InformationWebVerticle.class.getName())
      .deployVerticle(InformationDatabaseVerticle.class.getName())
      .start();
    log.info("Plague's Blog started");
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
