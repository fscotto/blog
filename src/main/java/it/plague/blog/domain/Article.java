package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.JsonConverter;

import java.time.LocalDateTime;

@DataObject
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

	private Long id;
	private User createdBy;
	private LocalDateTime created;
	private User modifiedBy;
	private LocalDateTime modified;
	private Author author;
	private String title;
	private String content;

	public Article() {
		this(0L, null, LocalDateTime.now().withNano(0), null, null, null, "", "");
	}

	public Article(Long id, User createdBy, LocalDateTime created, User modifiedBy, LocalDateTime modified,
								 Author author, String title, String content) {
		this.id = id;
		this.createdBy = createdBy;
		this.created = created;
		this.modifiedBy = modifiedBy;
		this.modified = modified;
		this.author = author;
		this.title = title;
		this.content = content;
	}

	public Article(JsonObject jsonObject) {
		JsonConverter.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(Article article) {
		this.id = article.id;
		this.createdBy = article.createdBy;
		this.created = article.created;
		this.modifiedBy = article.modifiedBy;
		this.modified = article.modified;
		this.author = article.author;
		this.content = article.content;
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

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Article article = (Article) o;
		return Objects.equal(id, article.id) &&
			Objects.equal(createdBy, article.createdBy) &&
			Objects.equal(created, article.created) &&
			Objects.equal(modifiedBy, article.modifiedBy) &&
			Objects.equal(modified, article.modified) &&
			Objects.equal(author, article.author) &&
			Objects.equal(title, article.title) &&
			Objects.equal(content, article.content);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, createdBy, created, modifiedBy, modified, author, title, content);
	}

	@Override
	public String toString() {
		return "Article{" +
			"id=" + id +
			", createdBy=" + createdBy +
			", created=" + created +
			", modifiedBy=" + modifiedBy +
			", modified=" + modified +
			", author=" + author +
			", title='" + title + '\'' +
			", content='" + content + '\'' +
			'}';
	}
}
