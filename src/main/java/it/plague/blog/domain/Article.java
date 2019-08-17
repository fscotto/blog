package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;
import it.plague.blog.util.JsonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DataObject
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

	private Long id;
	private Author createdBy;
	private LocalDateTime created;
	private Author modifiedBy;
	private LocalDateTime modified;
	private String title;
	private String content;

	public Article() {
		this(0L, null, LocalDateTime.now().withNano(0), null, null, "", "");
	}

	public Article(Long id, Author createdBy, LocalDateTime created, Author modifiedBy, LocalDateTime modified,
								 String title, String content) {
		this.id = id;
		this.createdBy = createdBy;
		this.created = created;
		this.modifiedBy = modifiedBy;
		this.modified = modified;
		this.title = title;
		this.content = content;
	}

	public Article(JsonObject jsonObject) {
		JsonUtil.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(Article article) {
		this.id = article.id;
		this.createdBy = article.createdBy;
		this.created = article.created;
		this.modifiedBy = article.modifiedBy;
		this.modified = article.modified;
		this.title = article.title;
		this.content = article.content;
	}

	public JsonObject toJson() {
		return JsonUtil.toJson(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Author getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Author createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@JsonIgnore
	public String getCreationDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return formatter.format(this.created);
	}

	public Author getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Author modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
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
			Objects.equal(title, article.title) &&
			Objects.equal(content, article.content);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, createdBy, created, modifiedBy, modified, title, content);
	}

	@Override
	public String toString() {
		return "Article{" +
			"id=" + id +
			", createdBy=" + createdBy +
			", created=" + created +
			", modifiedBy=" + modifiedBy +
			", modified=" + modified +
			", title='" + title + '\'' +
			", content='" + content + '\'' +
			'}';
	}

	public Tuple toCreateTuple() {
		return Tuple.of(this.createdBy.getId(), this.created, this.title, this.content);
	}

	public Tuple toUpdateTuple() {
		return Tuple.of(this.modifiedBy.getId(), this.modified, this.title, this.content, this.id);
	}
}
