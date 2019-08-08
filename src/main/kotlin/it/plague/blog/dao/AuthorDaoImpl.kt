package it.plague.blog.dao

import com.google.inject.Inject
import io.vertx.pgclient.PgPool
import it.plague.blog.domain.Author

class AuthorDaoImpl @Inject constructor(private val pool: PgPool) : AuthorDao {

	override fun findAll(): List<Author> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun findOne(id: Long): Author {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun save(o: Author) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun merge(o: Author) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun delete(id: Long) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}