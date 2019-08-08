package it.plague.blog.domain

data class User(val id: Long? = 0,
								val username: String,
								val password: String)