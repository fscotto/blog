package org.fscotto.blog.config.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;

public class VertxModule extends AbstractModule {

  private final Vertx vertx;

  public VertxModule(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  protected void configure() {
    bind(Vertx.class).toInstance(vertx);
  }

  @Provides
  @Singleton
  public EventBus getEventBus() {
    return this.vertx.eventBus();
  }
}
