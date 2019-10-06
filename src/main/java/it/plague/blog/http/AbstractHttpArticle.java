package it.plague.blog.http;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractHttpArticle extends AbstractVerticle {

  protected final HttpServer httpServer;
  protected final Router router;
  protected final TemplateEngine templateEngine;
  protected final String host;
  protected final String port;

  public AbstractHttpArticle(HttpServer httpServer, Router router, TemplateEngine templateEngine, String host, String port) {
    this.httpServer = httpServer;
    this.router = router;
    this.templateEngine = templateEngine;
    this.host = host;
    this.port = port;
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Try to start WebServer on " + host + ":" + port);
    httpServer
      .requestHandler(router)
      .rxListen(NumberUtils.toInt(port))
      .timeout(20, TimeUnit.SECONDS)
      .subscribe(SingleHelper.toObserver(result -> {
        if (result.succeeded()) {
          log.info("Server listening on " + host + ":" + port);
          promise.complete();
        } else {
          promise.fail("Could not start a HTTP server: " + result.cause().getMessage());
        }
      }));
  }

  @Override
  public void stop(Promise<Void> promise) {
    httpServer
      .rxClose()
      .subscribe(CompletableHelper.toObserver(close -> {
        if (close.succeeded()) {
          log.info("Shutdown HTTP Server successful!!!");
          promise.handle(Future.succeededFuture());
        } else {
          log.error("Shutdown HTTP Server failed", close.cause());
          promise.handle(Future.failedFuture(close.cause()));
        }
      }));
  }

  protected void renderPage(RoutingContext context, String template) {
    var layoutSelector = new LayoutSelector("layout3", template);
    templateEngine.render(context.data(), layoutSelector.getTemplate(), page -> {
      if (page.succeeded()) {
        context.response()
          .putHeader("Content-Type", "text/html")
          .end(page.result().toString());
      } else {
        log.error("Page loading error", page.cause());
        context.fail(page.cause());
      }
    });
  }

}
