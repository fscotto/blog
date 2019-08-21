package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import it.plague.blog.config.EventBusAddress;
import it.plague.blog.config.WebConstant;
import it.plague.blog.database.ArticleDatabaseService;
import it.plague.blog.domain.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class HttpVerticle extends AbstractVerticle {

  private final HttpServer httpServer;
  private final Router router;
  private final ArticleDatabaseService articleDbService;
  private final TemplateEngine templateEngine;
  private final String host;
  private final String port;


  @Inject
  public HttpVerticle(HttpServer httpServer, Router router, TemplateEngine templateEngine, ArticleDatabaseService articleDbService,
                      @Named(WebConstant.HTTP_SERVER_HOST) String host, @Named(WebConstant.HTTP_SERVER_PORT) String port) {
    this.httpServer = httpServer;
    this.router = router;
    this.articleDbService = articleDbService;
    this.templateEngine = templateEngine;
    this.host = host;
    this.port = port;
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Setting routing urls handled...");
    router.get("/").handler(this::indexHandler);
    router.get("/about").handler(this::aboutHandler);
    router.get("/cv").handler(this::curriculumHandler);
    router.get("/contacts").handler(this::contactHandler);

    log.info("Try to start WebServer on " + host + ":" + port);
    httpServer
      .requestHandler(router)
      .rxListen(NumberUtils.toInt(port))
      .subscribe(success -> {
          log.info("Server listening on " + host + ":" + port);
          promise.complete();
        },
        cause -> {
          log.error("Could not start a HTTP server", cause);
          promise.fail(cause);
        });
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

  private void indexHandler(RoutingContext context) {
    articleDbService.fetchAllArticles(reply -> {
      if (reply.succeeded()) {
        context.put("title", "Home");
        context.put("articles", reply.result()
          .stream()
          .filter(Objects::nonNull)
          .filter(obj -> obj instanceof JsonObject)
          .map(obj -> (JsonObject) obj)
          .map(jsonObject -> new Article(jsonObject))
          .collect(Collectors.toList()));
        renderPage(context, "templates/index");
      } else {
        context.fail(reply.cause());
      }
    });
  }

  private void aboutHandler(RoutingContext context) {
    vertx
      .eventBus()
      .request(EventBusAddress.ABOUT_ADDR, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          context.put("title", "About");
          context.put("info", reply.result().body());
          renderPage(context, "templates/about");
        } else {
          log.error(String.format("Contact address %s failed", EventBusAddress.ABOUT_ADDR), reply.cause());
          context.fail(reply.cause());
        }
      });
  }

  private void curriculumHandler(RoutingContext context) {
    vertx
      .eventBus()
      .request(EventBusAddress.CURRICULUM_ADDR, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          context.put("title", "Curriculum Vitae");
          context.put("curriculum", reply.result().body());
          renderPage(context, "templates/curriculum");
        } else {
          log.error(String.format("Contact address %s failed", EventBusAddress.CURRICULUM_ADDR), reply.cause());
          context.fail(reply.cause());
        }
      });
  }

  private void contactHandler(RoutingContext context) {
    vertx
      .eventBus()
      .request(EventBusAddress.CONTACT_ADDR, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          context.put("title", "Contatti");
          context.put("contact", reply.result().body());
          renderPage(context, "templates/contact");
        } else {
          log.error(String.format("Contact address %s failed", EventBusAddress.CONTACT_ADDR), reply.cause());
          context.fail(reply.cause());
        }
      });
  }

  private void renderPage(RoutingContext context, String template) {
    templateEngine.render(context.data(), template, page -> {
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
