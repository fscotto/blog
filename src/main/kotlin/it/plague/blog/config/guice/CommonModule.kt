package it.plague.blog.config.guice

import com.google.inject.AbstractModule
import io.vertx.core.logging.LoggerFactory
import it.plague.blog.dao.*
import it.plague.blog.service.ArticleService
import it.plague.blog.service.ArticleServiceImpl

class CommonModule : AbstractModule() {

	private val log = LoggerFactory.getLogger(this.javaClass)

	override fun configure() {
		log.info("Binding services...")
		bind(ArticleService::class.java) to ArticleServiceImpl::class.java
		log.info("...end service binding")

		log.info("Binding daos...")
		bind(ArticleDao::class.java) to ArticleDaoImpl::class.java
		bind(AuthorDao::class.java) to AuthorDaoImpl::class.java
		bind(UserDao::class.java) to UserDaoImpl::class.java
		log.info("...end daos binding")
	}
}