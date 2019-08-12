package it.plague.blog.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.core.json.JsonObject
import it.plague.blog.serializer.LocalDateDeserializer
import it.plague.blog.serializer.LocalDateSerialiazer
import java.time.LocalDateTime

object JsonConverter {

	fun <T> fromJson(jsonObject: JsonObject, clazz: Class<T>): T {
		val mapper = createObjectMapper()
		return mapper.readValue(jsonObject.toString(), clazz)
	}

	fun toJson(obj: Any): JsonObject {
		val mapper = createObjectMapper()
		return JsonObject(mapper.writeValueAsString(obj))
	}

	private fun createObjectMapper(): ObjectMapper {
		return jacksonObjectMapper().registerModules(
			JavaTimeModule(),
			SimpleModule().apply {
				addSerializer(LocalDateSerialiazer())
				addDeserializer(LocalDateTime::class.java, LocalDateDeserializer())
			}
		)
	}
}