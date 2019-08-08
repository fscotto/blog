package it.plague.blog.dao

import com.google.inject.Inject
import io.vertx.pgclient.PgPool
import it.plague.blog.domain.Article

class ArticleDaoImpl @Inject constructor(private val pool: PgPool) : ArticleDao {

	override fun findAll(): List<Article> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun findOne(id: Long): Article {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun save(o: Article) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun merge(o: Article) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun delete(id: Long) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
