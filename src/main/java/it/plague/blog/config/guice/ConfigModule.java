package it.plague.blog.config.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;

public class ConfigModule extends AbstractModule {

	private static final Logger log = LoggerFactory.getLogger(ConfigModule.class);

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
		properties.put("HOST", System.getenv("HOST") != null ? System.getenv("HOST") :
			InetAddress.getLocalHost().getHostName());
		properties.put("PORT", System.getenv("PORT") != null ? System.getenv("PORT") : "9000");
		Names.bindProperties(binder(), properties);
		log.info("Environment variable:\n " + new JsonObject(Collections.unmodifiableMap(properties)).encodePrettily());
		log.info("...loading complete!!!");
	}
}
