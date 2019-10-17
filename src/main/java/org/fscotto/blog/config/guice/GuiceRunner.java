package org.fscotto.blog.config.guice;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.vertx.reactivex.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;

@Slf4j
public class GuiceRunner {
  private Vertx vertx;
  private Collection<Module> modules;
  private Collection<String> verticles;

  private GuiceRunner(Vertx vertx) {
    this.vertx = vertx;
    this.modules = new LinkedList<>();
    this.verticles = new LinkedList<>();
  }

  public static GuiceRunner newInstance(Vertx vertx) {
    return new GuiceRunner(vertx);
  }

  public void start() {
    var injector = Guice.createInjector(modules);
    var factory = new GuiceVerticleFactory(injector);
    vertx.registerVerticleFactory(factory);
    for (var verticle : verticles) {
      vertx.deployVerticle(factory.prefix() + ":" + verticle);
    }
  }

  public GuiceRunner registerModule(Module module) {
    this.modules.add(module);
    return this;
  }

  public GuiceRunner deployVerticle(String verticleName) {
    log.debug(String.format("Deploy verticle with name %s", verticleName));
    this.verticles.add(verticleName);
    return this;
  }

}
