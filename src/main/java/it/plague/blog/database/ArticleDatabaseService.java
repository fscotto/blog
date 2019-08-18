package it.plague.blog.database;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import it.plague.blog.domain.Article;

@ProxyGen
@VertxGen
public interface ArticleDatabaseService {

	@GenIgnore
	static ArticleDatabaseService create(Vertx vertx, PgPool client, Handler<AsyncResult<ArticleDatabaseService>> readyHandler) {
		return new ArticleDatabaseServiceImpl(vertx, client, readyHandler);
	}

	@GenIgnore
	static ArticleDatabaseService createProxy(Vertx vertx, String address) {
		return new ArticleDatabaseServiceVertxEBProxy(vertx.getDelegate(), address);
	}

	@Fluent
	ArticleDatabaseService fetchAllArticles(Handler<AsyncResult<JsonArray>> resultHandler);

	@Fluent
	ArticleDatabaseService fetchArticle(Long id, Handler<AsyncResult<JsonObject>> resultHandler);

	@Fluent
	ArticleDatabaseService createArticle(Article article, Handler<AsyncResult<Void>> resultHandler);

	@Fluent
	ArticleDatabaseService saveArticle(Article article, Handler<AsyncResult<Void>> resultHandler);

	@Fluent
	ArticleDatabaseService deleteArticle(Long id, Handler<AsyncResult<Void>> resultHandler);
}
