package org.fscotto.blog.config.guice;

import com.google.inject.Injector;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

public class GuiceVerticleFactory implements VerticleFactory {

  private static final String PREFIX = "it.plague";

  private final Injector injector;

  public GuiceVerticleFactory(Injector injector) {
    this.injector = injector;
  }

  @Override
  public String prefix() {
    return PREFIX;
  }

  @Override
  public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
    var verticle = VerticleFactory.removePrefix(verticleName);
    return (Verticle) injector.getInstance(classLoader.loadClass(verticle));
  }
}
