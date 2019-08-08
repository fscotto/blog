package it.plague.blog.dao

import it.plague.blog.domain.Article

interface ArticleDao : GenericDao<Article, Long> {
}