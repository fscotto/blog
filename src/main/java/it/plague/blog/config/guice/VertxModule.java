package it.plague.blog.config.guice;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class VertxModule extends PrivateModule {

	private final Vertx vertx;

	public VertxModule(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@Exposed
	public EventBus getEventBus() {
		return this.vertx.eventBus();
	}
}
