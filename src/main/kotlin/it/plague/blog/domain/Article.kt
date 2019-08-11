package it.plague.blog.domain

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import it.plague.blog.json.JsonConvertable
import java.time.LocalDateTime

data class Article(var id: Long? = 0,
									 var createdBy: User,
									 var created: LocalDateTime = LocalDateTime.now(),
									 var modifiedBy: User? = null,
									 var modified: LocalDateTime? = null,
									 var author: Author,
									 var title: String,
									 var content: Buffer? = null) : JsonConvertable<Article> {

	override fun fromJson(jsonObject: JsonObject): Article {
		val mapper = ObjectMapper()
		return mapper.readValue(jsonObject.encode(), Article::class.java)
	}

	override fun toJson(): JsonObject {
		return JsonObject().apply {
			put("id", id)
			put("createdBy", createdBy.toJson())
			put("created", created)
			put("modifiedBy", modifiedBy?.toJson())
			put("modified", modified)
			put("author", author.toJson())
			put("title", title)
			put("content", content)
		}
	}
}
