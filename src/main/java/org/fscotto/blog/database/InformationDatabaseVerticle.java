/*
 * Copyright (C) 2019 Fabio Scotto di Santolo <fabio.scottodisantolo@gmail.com>
 *
 * This file is part of plague-blog.
 *
 * plague-blog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * plague-blog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with plague-blog.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fscotto.blog.database;

import com.google.inject.Inject;
import io.reactivex.MaybeObserver;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.mongo.MongoClient;
import org.fscotto.blog.config.EventBusAddress;

import java.util.function.Function;

public class InformationDatabaseVerticle extends AbstractVerticle {

  private static final String ERROR_MESSAGE = "Info not found!!!";
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
        message.fail(1000, ERROR_MESSAGE);
      }
    });
  }

  private MaybeObserver<JsonObject> curriculumHandler(Message<JsonObject> message) {
    return MaybeHelper.toObserver(document -> {
      if (document.succeeded()) {
        String curriculum = document.result().getString("curriculum");
        message.reply(curriculum != null ? curriculum : "");
      } else {
        message.fail(1000, ERROR_MESSAGE);
      }
    });
  }

  private MaybeObserver<JsonObject> contactHandler(Message<JsonObject> message) {
    return MaybeHelper.toObserver(document -> {
      if (document.succeeded()) {
        String contact = document.result().getString("contact");
        message.reply(contact != null ? contact : "");
      } else {
        message.fail(1000, ERROR_MESSAGE);
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
