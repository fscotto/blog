package it.plague.blog.verticle

import com.google.inject.Inject
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.logging.LoggerFactory
import it.plague.blog.service.ArticleService

class PgArticleVerticle @Inject constructor(private val articleService: ArticleService) : AbstractVerticle() {

	private val log = LoggerFactory.getLogger(javaClass)

	override fun start(promise: Promise<Void>) {
		log.info("Start ${this.javaClass.name}")
		val articles = articleService.getAll()
		for (article in articles)
			println(article)
	}
}