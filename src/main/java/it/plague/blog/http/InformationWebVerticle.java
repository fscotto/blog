package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import it.plague.blog.config.EventBusAddress;
import it.plague.blog.config.WebConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InformationWebVerticle extends AbstractHttpArticle {

  @Inject
  public InformationWebVerticle(HttpServer httpServer, Router router, TemplateEngine templateEngine,
                                @Named(WebConstant.HTTP_SERVER_HOST) String host, @Named(WebConstant.HTTP_SERVER_PORT) String port) {
    super(httpServer, router, templateEngine, host, port);
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Setting routing urls handled from InformationWebVerticle...");
    router.get("/about").handler(this::aboutHandler);
    //router.get("/cv").handler(this::curriculumHandler);
    //router.get("/contacts").handler(this::contactHandler);
    super.start(promise);
  }

  private void aboutHandler(RoutingContext context) {
    vertx
      .eventBus()
      .request(EventBusAddress.ABOUT_ADDR, new JsonObject(), reply -> {
        if (reply.succeeded()) {
          context.put("title", "About me");
          context.put("info", reply.result().body());
          renderPage(context, "about");
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
          renderPage(context, "curriculum");
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
          renderPage(context, "contact");
        } else {
          log.error(String.format("Contact address %s failed", EventBusAddress.CONTACT_ADDR), reply.cause());
          context.fail(reply.cause());
        }
      });
  }

}
