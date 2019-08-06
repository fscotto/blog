package it.plague.blog.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateSerialiazer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  override fun serialize(value: LocalDateTime, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
    jsonGenerator.writeString(value.format(formatter))
  }
}
