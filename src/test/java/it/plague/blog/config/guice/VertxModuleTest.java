package it.plague.blog.config.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import it.plague.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@UnitTest
class VertxModuleTest {

  Vertx vertx;

  @Inject
  Vertx injectedVertx;

  @Inject
  EventBus eventBus;

  @BeforeEach
  void setUp() {
    this.vertx = Vertx.vertx();
    Guice.createInjector(new VertxModule(vertx), BoundFieldModule.of(this)).injectMembers(this);
  }

  @Test
  @DisplayName("Should be inject Vert.x instance")
  void shouldBeInjectVertxInstance() {
    assertNotNull(injectedVertx);
  }

  @Test
  @DisplayName("Should be inject Vert.x instance same that in constructor")
  void shouldBeInjectSameInstance() {
    assertSame(injectedVertx, vertx);
  }

  @Test
  @DisplayName("Should be inject EventBus instance")
  void shouldBeInjectEventBusInstance() {
    assertNotNull(eventBus);
  }

}