package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DataObject
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

	private Long id;
	private String name;
	private String lastName;

	@JsonSerialize
	@JsonDeserialize
	private User user;

	public Author(JsonObject jsonObject) {
		JsonUtil.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(Author author) {
		this.id = author.id;
		this.name = author.name;
		this.lastName = author.lastName;
		this.user = author.user;
	}

	public JsonObject toJson() {
		return JsonUtil.toJson(this);
	}
}
