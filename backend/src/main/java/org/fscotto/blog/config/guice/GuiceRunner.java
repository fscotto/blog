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
