package it.plague.blog.config.guice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import it.plague.blog.serializer.LocalDateDeserializer
import it.plague.blog.serializer.LocalDateSerialiazer
import java.time.LocalDateTime

class JacksonModule : AbstractModule() {

	@Provides
	@Singleton
	fun provideObjectMapper(): ObjectMapper {
		val mapper = jacksonObjectMapper()
		mapper.registerModules(
			JavaTimeModule(),
			SimpleModule().apply {
				addSerializer(LocalDateSerialiazer())
				addDeserializer(LocalDateTime::class.java, LocalDateDeserializer())
			})
		return mapper
	}
}