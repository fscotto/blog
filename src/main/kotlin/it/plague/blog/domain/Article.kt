package it.plague.blog.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Article(var id: Long? = 0,
									 var createdBy: User,
									 var created: LocalDateTime = LocalDateTime.now().withNano(0),
									 var modifiedBy: User? = null,
									 var modified: LocalDateTime? = null,
									 var author: Author,
									 var title: String,
									 var content: String = "")