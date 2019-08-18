package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.sqlclient.Tuple;
import it.plague.blog.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@DataObject
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

	private Long id;
	private Author createdBy;
	private LocalDateTime created;
	private Author modifiedBy;
	private LocalDateTime modified;
	private String title;
	private String content;

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

	public Tuple toCreateTuple() {
		return Tuple.of(this.createdBy.getId(), this.created, this.title, this.content);
	}

	public Tuple toUpdateTuple() {
		return Tuple.of(this.modifiedBy.getId(), this.modified, this.title, this.content, this.id);
	}
}
