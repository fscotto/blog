package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.JsonConverter;

@DataObject
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

	private Long id;
	private String name;
	private String lastName;

	public Author() {
		this(0L, "", "");
	}

	public Author(Long id, String name, String lastName) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
	}

	public Author(JsonObject jsonObject) {
		JsonConverter.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(Author author) {
		this.id = author.id;
		this.name = author.name;
		this.lastName = author.lastName;
	}

	public JsonObject toJson() {
		return JsonConverter.toJson(this);
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
			Objects.equal(lastName, author.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, name, lastName);
	}

	@Override
	public String toString() {
		return "Author{" +
			"id=" + id +
			", name='" + name + '\'' +
			", lastName='" + lastName + '\'' +
			'}';
	}
}
