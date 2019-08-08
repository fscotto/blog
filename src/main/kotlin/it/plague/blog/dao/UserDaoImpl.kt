package it.plague.blog.dao

import com.google.inject.Inject
import io.vertx.pgclient.PgPool
import it.plague.blog.domain.User

class UserDaoImpl @Inject constructor(private val pool: PgPool) : UserDao {

	override fun findAll(): List<User> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun findOne(id: Long): User {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun save(o: User) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun merge(o: User) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun delete(id: Long) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}