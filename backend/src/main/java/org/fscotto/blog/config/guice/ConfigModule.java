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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.fscotto.blog.config.WebConstant;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class ConfigModule extends AbstractModule {

  @Override
  protected void configure() {
    try {
      enviriromentConfiguration();
    } catch (UnknownHostException e) {
      log.error(e.getMessage());
    }
  }

  private void enviriromentConfiguration() throws UnknownHostException {
    log.info("Loading new environment variable...");
    var properties = new HashMap<String, String>();
    Configuration envConfig = new EnvironmentConfiguration();
    properties.put(WebConstant.HTTP_SERVER_HOST, envConfig.getString("HOST", InetAddress.getLocalHost().getHostName()));
    properties.put(WebConstant.HTTP_SERVER_PORT, envConfig.getString("PORT", "9000"));
    Names.bindProperties(binder(), properties);
    log.info("Environment variable:\n " + new JsonObject(Collections.unmodifiableMap(properties)).encodePrettily());
    log.info("...loading complete!!!");
  }

}
