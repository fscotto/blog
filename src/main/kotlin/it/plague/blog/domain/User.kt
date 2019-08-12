package it.plague.blog.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(val id: Long? = 0,
								val username: String,
								val password: String)
