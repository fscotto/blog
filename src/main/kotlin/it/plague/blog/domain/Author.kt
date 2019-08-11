package it.plague.blog.domain

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonObject
import it.plague.blog.json.JsonConvertable

data class Author(val id: Long? = 0,
									val name: String,
									val lastName: String) : JsonConvertable<Author> {

	override fun fromJson(jsonObject: JsonObject): Author {
		val mapper = ObjectMapper()
		return mapper.readValue(jsonObject.encode(), Author::class.java)
	}

	override fun toJson(): JsonObject {
		return JsonObject().apply {
			put("id", id)
			put("name", name)
			put("lastName", lastName)
		}
	}
}