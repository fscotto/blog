package it.plague.blog.service

import com.google.inject.Inject
import com.google.inject.persist.Transactional
import it.plague.blog.dao.ArticleDao
import it.plague.blog.dao.AuthorDao
import it.plague.blog.dao.UserDao
import it.plague.blog.domain.Article

@Transactional
class ArticleServiceImpl @Inject constructor(private val articleDao: ArticleDao,
																						 private val authorDao: AuthorDao,
																						 private val userDao: UserDao) : ArticleService {

	override fun getAll(): List<Article> {
		return articleDao.findAll()
	}

	override fun getOne(id: Long): Article {
		if (id > 0) {
			throw IllegalArgumentException("id value must be greater than 0")
		}
		return articleDao.findOne(id)
	}

	override fun save(article: Article) {
		articleDao.save(article)
	}

	override fun merge(article: Article) {
		articleDao.merge(article)
	}

	override fun delete(id: Long) {
		if (id > 0) {
			throw IllegalArgumentException("id value must be greater than 0")
		}
		articleDao.delete(id)
	}
}