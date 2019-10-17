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

package org.fscotto.blog.support.verticle;

import com.google.inject.Injector;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.junit5.VertxExtension;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fscotto.blog.config.guice.GuiceVerticleFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;

@Slf4j
@ExtendWith({VertxExtension.class, MockitoExtension.class})
public abstract class AbstractVerticleTestSuite {

  @Getter
  private final Vertx vertx;

  @Getter
  private final WebClient webClient;

  private String prefix;

  public AbstractVerticleTestSuite() {
    this.vertx = Vertx.vertx();
    this.webClient = WebClient.create(vertx);
  }

  @BeforeEach
  void setUp() {
    Injector injector = getInjector();
    initVerticleFactory(injector);
    injector.injectMembers(this);
  }

  protected abstract Injector getInjector();

  private void initVerticleFactory(Injector injector) {
    var factory = new GuiceVerticleFactory(injector);
    prefix = factory.prefix();
    vertx.registerVerticleFactory(factory);
  }

  @AfterEach
  void tearDown() {
    vertx.rxClose();
    log.info("...Vert.x close");
  }

  protected void deployVerticle(String verticleName, Handler<AsyncResult<String>> completionHandler) {
    log.debug(String.format("Deploy verticle with name %s", verticleName));
    vertx.rxDeployVerticle(prefix + ":" + verticleName)
      .subscribe(SingleHelper.toObserver(completionHandler));
  }

  protected void undeployVerticle(String deploymentID, Handler<AsyncResult<Void>> completionHandler) {
    log.debug(String.format("Undeploy verticle with deploymentID %s", deploymentID));
    vertx.rxUndeploy(deploymentID)
      .subscribe(CompletableHelper.toObserver(completionHandler));
  }

  protected int getRandomPort(int start, int end) {
    var random = new SecureRandom();
    return random.nextInt(end) + start;
  }

}
