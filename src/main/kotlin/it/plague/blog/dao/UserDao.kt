package it.plague.blog.dao

import it.plague.blog.domain.User

interface UserDao : GenericDao<User, Long> {
}