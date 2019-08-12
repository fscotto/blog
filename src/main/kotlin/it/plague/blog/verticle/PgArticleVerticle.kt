package it.plague.blog.verticle

import com.google.inject.Inject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.pgclient.PgPool


class PgArticleVerticle @Inject constructor(private val client: PgPool,
																						private val eventBus: EventBus) : CoroutineVerticle() {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun start(promise: Promise<Void>) {
		log.info("Start ${this.javaClass.name}")

		eventBus
			.consumer<JsonObject>("articles.getAll")
			.handler { msg ->
				getAllArticles(Handler { ar ->
					if (ar.succeeded()) {
						msg.reply(ar.result())
					} else {
						msg.fail(1, ar.cause().message)
						log.error("Something went wrong while retrieving all artices ${ar.cause().message}")
					}
				})
			}
	}

	private fun getAllArticles(handler: Handler<AsyncResult<JsonArray>>) {

	}
}