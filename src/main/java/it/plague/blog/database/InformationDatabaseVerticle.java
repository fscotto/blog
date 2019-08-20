package it.plague.blog.database;

import com.google.inject.Inject;
import io.reactivex.MaybeObserver;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.mongo.MongoClient;
import it.plague.blog.config.EventBusAddress;

import java.util.function.Function;

public class InformationDatabaseVerticle extends AbstractVerticle {

  private final MongoClient client;

  @Inject
  public InformationDatabaseVerticle(MongoClient client) {
    this.client = client;
  }

  @Override
  public void start(Promise<Void> promise) {
    registerInfoConsumer(EventBusAddress.ABOUT_ADDR, this::aboutHandler);
    registerInfoConsumer(EventBusAddress.CURRICULUM_ADDR, this::curriculumHandler);
    registerInfoConsumer(EventBusAddress.CONTACT_ADDR, this::contactHandler);
  }

  private MaybeObserver<JsonObject> aboutHandler(Message<JsonObject> message) {
    return MaybeHelper.toObserver(document -> {
      if (document.succeeded()) {
        String about = document.result().getString("about");
        message.reply(about != null ? about : "");
      } else {
        message.fail(1000, "Info not found!!!");
      }
    });
  }

  private MaybeObserver<JsonObject> curriculumHandler(Message<JsonObject> message) {
    return MaybeHelper.toObserver(document -> {
      if (document.succeeded()) {
        String curriculum = document.result().getString("curriculum");
        message.reply(curriculum != null ? curriculum : "");
      } else {
        message.fail(1000, "Info not found!!!");
      }
    });
  }

  private MaybeObserver<JsonObject> contactHandler(Message<JsonObject> message) {
    return MaybeHelper.toObserver(document -> {
      if (document.succeeded()) {
        String contact = document.result().getString("contact");
        message.reply(contact != null ? contact : "");
      } else {
        message.fail(1000, "Info not found!!!");
      }
    });
  }

  private void registerInfoConsumer(String address, Function<Message<JsonObject>, MaybeObserver<JsonObject>> observerHandler) {
    vertx
      .eventBus()
      .<JsonObject>consumer(address)
      .handler(message -> {
        JsonObject query = new JsonObject()
          .put("who", "fscotto");
        client
          .rxFindOne("info", query, null)
          .subscribe(observerHandler.apply(message));
      });
  }

}
