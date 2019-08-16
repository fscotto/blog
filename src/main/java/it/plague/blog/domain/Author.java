package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.JsonUtils;

@DataObject
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

	private Long id;
	private String name;
	private String lastName;
	@JsonSerialize
	@JsonDeserialize
	private User user;

	public Author() {
		this(0L, "", "", null);
	}

	public Author(Long id, String name, String lastName, User user) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.user = user;
	}

	public Author(JsonObject jsonObject) {
		JsonUtils.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(Author author) {
		this.id = author.id;
		this.name = author.name;
		this.lastName = author.lastName;
		this.user = author.user;
	}

	public JsonObject toJson() {
		return JsonUtils.toJson(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Author author = (Author) o;
		return Objects.equal(id, author.id) &&
			Objects.equal(name, author.name) &&
			Objects.equal(lastName, author.lastName) &&
			Objects.equal(user, author.user);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name, lastName, user);
	}

	@Override
	public String toString() {
		return "Author{" +
			"id=" + id +
			", name='" + name + '\'' +
			", lastName='" + lastName + '\'' +
			", user=" + user +
			'}';
	}
}
