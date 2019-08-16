package it.plague.blog.util;

import io.vertx.core.json.JsonObject;
import it.plague.blog.domain.Article;
import it.plague.blog.domain.Author;
import it.plague.blog.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class JsonConverterTest {

	private JsonObject json;
	private Article object;

	@BeforeEach
	public void setUp() {
		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		var now = LocalDateTime.now().withNano(0);
		this.json = new JsonObject()
			.put("id", 1)
			.put("createdBy", new JsonObject()
				.put("id", 1)
				.put("name", "J.R.")
				.put("lastName", "Tolkien")
				.put("user", new JsonObject()
					.put("id", 1)
					.put("username", "joe")
					.put("password", "secret")))
			.put("created", formatter.format(now))
			.putNull("modifiedBy")
			.putNull("modified")
			.put("title", "Lord of the rings")
			.put("content", "");

		this.object = Builder.of(Article::new)
			.with(Article::setId, 1L)
			.with(Article::setCreatedBy, new Author(1L, "J.R.", "Tolkien", new User(1L, "joe", "secret")))
			.with(Article::setCreated, now)
			.with(Article::setTitle, "Lord of the rings")
			.with(Article::setContent, "")
			.build();
	}

	@Test
	public void shouldBeConvertFromJsonToObject() {
		var actual = JsonUtils.fromJson(json, Article.class).get();
		assertThat(actual).isEqualTo(object);
	}

	@Test
	public void shouldBeConvertFromObjectToJson() {
		var actual = JsonUtils.toJson(object);
		assertThat(actual).isEqualTo(json);
	}
}