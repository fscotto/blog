package it.plague.blog.config.guice

import com.google.inject.Exposed
import com.google.inject.PrivateModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions

class DatabaseModule(private val vertx: Vertx) : PrivateModule() {

	override fun configure() {}

	@Exposed
	@Singleton
	@Provides
	fun providePgPool(): PgPool {
		return PgPool.pool(vertx, PgConnectOptions.fromEnv(), PoolOptions())
	}
}
