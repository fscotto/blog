package it.plague.blog.database;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import it.plague.blog.domain.Article;

import java.util.stream.Collector;

public class ArticleDatabaseServiceImpl implements ArticleDatabaseService {

	private static final Logger log = LoggerFactory.getLogger(ArticleDatabaseService.class);

	private final Vertx vertx;
	private final PgPool client;

	public ArticleDatabaseServiceImpl(Vertx vertx, PgPool client, Handler<AsyncResult<ArticleDatabaseService>> readyHandler) {
		this.vertx = vertx;
		this.client = client;

		this.client.getConnection(connectionResult -> {
			if (connectionResult.failed()) {
				log.error("Could not open a database connection", connectionResult.cause());
				readyHandler.handle(Future.failedFuture(connectionResult.cause()));
			} else {
				var connection = connectionResult.result();
				Future<SqlConnection> future = Future.future();
				this.vertx.fileSystem().readFile("sql/1-init.sql", fileResult -> {
					if (fileResult.failed()) {
						future.fail(fileResult.cause());
					} else {
						connection.query(fileResult.result().toString(), queryResult -> {
							connection.close();
							if (queryResult.failed()) {
								log.error("Database preparation error", queryResult.cause());
								readyHandler.handle(Future.failedFuture(queryResult.cause()));
							} else {
								readyHandler.handle(Future.succeededFuture(this));
							}
						});
					}
				});
			}
		});
	}

	@Override
	public ArticleDatabaseService fetchAllArticles(Handler<AsyncResult<JsonArray>> resultHandler) {
		this.client.query(SqlQuery.FETCH_ALL_ARTICLES,
			Collector.of(JsonArray::new, (ja, row) -> ja.add(articleRowMapper(row)), JsonArray::addAll),
			result -> {
				if (result.succeeded()) {
					log.info(String.format("Found %d articles!!!", result.result().size()));
					resultHandler.handle(Future.succeededFuture(result.result().value()));
				} else {
					log.error("fetchAllArticles failed with error", result.cause());
					resultHandler.handle(Future.failedFuture(result.cause()));
				}
			});
		return this;
	}

	@Override
	public ArticleDatabaseService fetchArticle(Long id, Handler<AsyncResult<JsonObject>> resultHandler) {
		this.client.preparedQuery(SqlQuery.FETCH_ONE_ARTICLE, Tuple.of(id), fetch -> {
			if (fetch.succeeded()) {
				JsonObject response = new JsonObject();
				if (fetch.result().size() == 0) {
					log.info(String.format("Article with id %d not found!!!", id));
					response.put("found", Boolean.FALSE);
				} else {
					log.info(
						String.format("Article with id %d found!!!", id));
					response.put("found", Boolean.TRUE);
					response.put("article", articleRowMapper(fetch.result().iterator().next()));
				}
				resultHandler.handle(Future.succeededFuture(response));
			} else {
				log.error("fetchArticle failed with error", fetch.cause());
				resultHandler.handle(Future.failedFuture(fetch.cause()));
			}
		});
		return this;
	}

	@Override
	public ArticleDatabaseService createArticle(Article article, Handler<AsyncResult<Void>> resultHandler) {
		executeUpdate(SqlQuery.INSERT_ARTICLE, article.toCreateTuple(), resultHandler);
		return this;
	}

	@Override
	public ArticleDatabaseService saveArticle(Article article, Handler<AsyncResult<Void>> resultHandler) {
		executeUpdate(SqlQuery.UPDATE_ARTICLE, article.toUpdateTuple(), resultHandler);
		return this;
	}

	@Override
	public ArticleDatabaseService deleteArticle(Long id, Handler<AsyncResult<Void>> resultHandler) {
		executeUpdate(SqlQuery.DELETE_ARTICLE, Tuple.of(id), resultHandler);
		return this;
	}

	private void executeUpdate(String query, Tuple tuple, Handler<AsyncResult<Void>> resultHandler) {
		this.client.preparedQuery(query, tuple, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				log.error("Execute update failed cause: ", ar.cause());
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	private JsonObject articleRowMapper(Row row) {
		return new JsonObject()
			.put("id", row.getLong("id"))
			.put("createdBy", new JsonObject()
				.put("id", row.getLong("createdby_id"))
				.put("name", row.getString("createdby_name"))
				.put("lastName", row.getString("createdby_lastname"))
				.put("user", new JsonObject()
					.put("id", row.getLong("createdby_user_id"))
					.put("username", row.getString("createdby_user_username"))
					.put("password", row.getString("createdby_user_password"))))
			.put("created", row.getLocalDateTime("created"))
			.put("modifiedBy", new JsonObject()
				.put("id", row.getLong("modifiedby_id"))
				.put("name", row.getString("modifiedby_name"))
				.put("lastName", row.getString("modifiedby_lastname"))
				.put("user", new JsonObject()
					.put("id", row.getLong("modifiedby_user_id"))
					.put("username", row.getString("modifiedby_user_username"))
					.put("password", row.getString("modifiedby_user_password"))))
			.put("modified", row.getLocalDateTime("modified"))
			.put("title", row.getString("title"))
			.put("content", row.getString("content"));
	}
}
