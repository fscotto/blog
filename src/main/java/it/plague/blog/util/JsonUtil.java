package it.plague.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.JsonObject;
import it.plague.blog.serializer.LocalDateDeserializer;
import it.plague.blog.serializer.LocalDateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public final class JsonUtil {

	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

	public static <T> Optional<T> fromJson(final JsonObject jsonObject, final Class<T> clazz) {
		try {
			var mapper = createObjectMapper();
			return Optional.ofNullable(mapper.readValue(jsonObject.encode(), clazz));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return Optional.empty();
	}

	public static JsonObject toJson(final Object o) {
		try {
			var mapper = createObjectMapper();
			return new JsonObject(mapper.writeValueAsString(o));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		return new JsonObject();
	}

	private static ObjectMapper createObjectMapper() {
		var mapper = new ObjectMapper();
		mapper.registerModules(
			new JavaTimeModule(),
			new SimpleModule()
				.addSerializer(new LocalDateSerializer(LocalDateTime.class))
				.addDeserializer(LocalDateTime.class, new LocalDateDeserializer(LocalDateTime.class))
		);
		return mapper;
	}
}
