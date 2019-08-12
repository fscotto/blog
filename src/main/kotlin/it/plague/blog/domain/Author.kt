package it.plague.blog.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Author(val id: Long? = 0,
									val name: String,
									val lastName: String)