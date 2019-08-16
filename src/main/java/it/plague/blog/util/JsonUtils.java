package it.plague.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import it.plague.blog.serializer.LocalDateDeserializer;
import it.plague.blog.serializer.LocalDateSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public final class JsonUtils {

	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

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

	public static String fromDateToString(TemporalAccessor date) {
		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date != null ? formatter.format(date) : "";
	}
}
