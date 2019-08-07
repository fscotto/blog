package it.plague.blog.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {

	private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

	override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): LocalDateTime {
		val text = jsonParser.readValueAs(String::class.java)
		return if (text == null) LocalDateTime.MAX else LocalDateTime.parse(text, formatter)
	}
}
