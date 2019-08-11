package it.plague.blog.domain

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonObject
import it.plague.blog.json.JsonConvertable

data class User(val id: Long? = 0,
								val username: String,
								val password: String) : JsonConvertable<User> {

	override fun fromJson(jsonObject: JsonObject): User {
		val mapper = ObjectMapper()
		return mapper.readValue(jsonObject.encode(), User::class.java)
	}

	override fun toJson(): JsonObject {
		return JsonObject().apply {
			put("id", id)
			put("username", username)
			put("password", password)
		}
	}
}