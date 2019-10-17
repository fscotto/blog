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
