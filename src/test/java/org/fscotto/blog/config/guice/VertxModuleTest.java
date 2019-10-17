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

package org.fscotto.blog.config.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import org.fscotto.blog.support.annotation.UnitTest;
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