package it.plague.blog.config.guice;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseModule extends PrivateModule {

	private final Vertx vertx;

	public DatabaseModule(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	protected void configure() {
	}

	@Exposed
	@Singleton
	@Provides
	public PgPool getPool() {
		return PgPool.pool(this.vertx, getConnectOptions(), getPoolOptions());
	}

	@Exposed
	@Singleton
	@Provides
	public PgConnectOptions getConnectOptions() {
		return PgConnectOptions.fromEnv();
	}

	private PoolOptions getPoolOptions() {
		return new PoolOptions();
	}
}
