package it.plague.blog.util

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.vertx.core.json.JsonObject
import it.plague.blog.domain.Article
import it.plague.blog.domain.Author
import it.plague.blog.domain.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JsonConvertTest {

	private var jsonArticle: JsonObject? = null
	private var article: Article? = null

	@BeforeEach
	fun setUp() {
		val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
		val now = LocalDateTime.now().withNano(0)
		this.jsonArticle = JsonObject().apply {
			put("id", 1)
			put("createdBy", JsonObject()
				.put("id", 1)
				.put("username", "joe")
				.put("password", "secret"))
			put("created", formatter.format(now))
			putNull("modifiedBy")
			putNull("modified")
			put("author", JsonObject()
				.put("id", 1)
				.put("name", "J.R.")
				.put("lastName", "Tolkien")
			)
			put("title", "Lord of the rings")
			put("content", "")
		}

		this.article = Article(
			id = 1,
			createdBy = User(id = 1, username = "joe", password = "secret"),
			created = now,
			author = Author(id = 1, name = "J.R.", lastName = "Tolkien"),
			title = "Lord of the rings"
		)
	}

	@Test
	fun `should be convert from json to object`() {
		val actual = this.jsonArticle?.let { json -> JsonConverter.fromJson(json, Article::class.java) }
		Assertions.assertThat(actual).isEqualTo(this.article)
	}

	@Test
	fun `should be throw MissingKotlinParameterException with empty json`() {
		assertThrows<MissingKotlinParameterException> {
			JsonConverter.fromJson(JsonObject(), Article::class.java)
		}.also { exception ->
			Assertions.assertThat(exception.message)
				.isEqualTo(
					"Instantiation of [simple type, class it.plague.blog.domain.Article] value failed for JSON property createdBy due " +
						"to missing (therefore NULL) value for creator parameter createdBy which is a non-nullable type\n" +
						" at [Source: (String)\"{}\"; line: 1, column: 2] (through reference chain: it.plague.blog.domain.Article[\"createdBy\"])"
				)
		}
	}

	@Test
	fun `should be throw MissingKotlinParameterException with malformed json`() {
		assertThrows<MissingKotlinParameterException> {
			val jsonObject = JsonObject().apply {
				putNull("modifiedBy")
				putNull("modified")
				put("author", JsonObject()
					.put("name", "J.R.")
					.put("lastName", "Tolkien")
				)
				put("title", "Lord of the rings")
			}
			JsonConverter.fromJson(jsonObject, Article::class.java)
		}
	}

	@Test
	fun `should be convert from object to json`() {
		val actual = this.article?.let { obj -> JsonConverter.toJson(obj) }
		Assertions.assertThat(actual).isEqualTo(jsonArticle)
	}
}