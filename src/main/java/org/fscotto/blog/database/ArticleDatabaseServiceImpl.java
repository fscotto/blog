package org.fscotto.blog.database;

import com.google.common.collect.Lists;
import io.reactivex.Flowable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.fscotto.blog.domain.Article;
import org.fscotto.blog.util.DateUtil;

@Slf4j
class ArticleDatabaseServiceImpl implements ArticleDatabaseService {

  private final Vertx vertx;
  private final PgPool client;

  ArticleDatabaseServiceImpl(Vertx vertx, PgPool client, Handler<AsyncResult<ArticleDatabaseService>> readyHandler) {
    this.vertx = vertx;
    this.client = client;
    readyHandler.handle(Future.succeededFuture(this));
  }

  /**
   * @param readyHandler
   * @deprecated
   */
  @Deprecated(forRemoval = false)
  private void prepareDatabase(Handler<AsyncResult<ArticleDatabaseService>> readyHandler) {
    vertx.fileSystem()
      .rxReadFile("sql/1-init.sql")
      .map(Buffer::toString)
      .subscribe(SingleHelper.toObserver(query -> client
        .rxBegin()
        .flatMapCompletable(transaction -> transaction
          .rxQuery(query.result())
          .flatMapCompletable(result -> transaction.rxCommit())
          .doOnError(cause -> transaction.rxRollback()))
        .subscribe(CompletableHelper.toObserver(result -> {
          if (result.succeeded()) {
            log.info("Database preparation successful");
            readyHandler.handle(Future.succeededFuture(this));
          } else {
            log.error("Database preparation error", result.cause());
            readyHandler.handle(Future.failedFuture(result.cause()));
          }
        }))));
  }

  @Override
  public ArticleDatabaseService fetchAllArticles(Handler<AsyncResult<JsonArray>> resultHandler) {
    client.rxQuery(SqlQuery.FETCH_ALL_ARTICLES)
      .flatMapPublisher(Flowable::fromIterable)
      .map(this::articleRowMapper)
      .sorted()
      .collect(JsonArray::new, JsonArray::add)
      .subscribe(SingleHelper.toObserver(result -> {
        if (result.succeeded()) {
          log.info(String.format("Found %d articles!!!", result.result().size()));
          resultHandler.handle(Future.succeededFuture(result.result()));
        } else {
          log.error("fetchAllArticles failed with error", result.cause());
          resultHandler.handle(Future.failedFuture(result.cause()));
        }
      }));
    return this;
  }

  @Override
  public ArticleDatabaseService fetchArticle(Long id, Handler<AsyncResult<JsonObject>> resultHandler) {
    client.rxPreparedQuery(SqlQuery.FETCH_ONE_ARTICLE, Tuple.of(id))
      .map(fetch -> {
        JsonObject response = new JsonObject();
        if (fetch.rowCount() > 0) {
          log.info(String.format("Article with id %d found!!!", id));
          response.put("found", Boolean.TRUE);
          response.put("article", articleRowMapper(Lists.newArrayList(fetch).get(0)));
        } else {
          log.info(String.format("Article with id %d not found!!!", id));
          response.put("found", Boolean.FALSE);
        }
        return response;
      })
      .subscribe(SingleHelper.toObserver(resultHandler));
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
    client.rxBegin()
      .flatMapCompletable(transaction -> transaction
        .rxPreparedQuery(query, tuple)
        .flatMapCompletable(result -> transaction.rxCommit())
        .doOnError(cause -> transaction.rxRollback()))
      .subscribe(CompletableHelper.toObserver(result -> {
        if (result.succeeded()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          log.error("Execute update failed cause: ", result.cause());
          resultHandler.handle(Future.failedFuture(result.cause()));
        }
      }));
  }

  private JsonObject articleRowMapper(Row row) {
    var json = new JsonObject()
      .put("id", row.getLong("id"))
      .put("createdBy", new JsonObject()
        .put("id", row.getLong("createdby_id"))
        .put("name", row.getString("createdby_name"))
        .put("lastName", row.getString("createdby_lastname"))
        .put("user", new JsonObject()
          .put("id", row.getLong("createdby_user_id"))
          .put("username", row.getString("createdby_user_username"))
          .put("password", row.getString("createdby_user_password"))))
      .put("created", DateUtil.toString(row.getLocalDateTime("created").withNano(0)))
      .put("title", row.getString("title"))
      .put("content", row.getString("content"));

    if (row.getLong("modifiedby_id") != null) {
      json.put("modifiedBy", new JsonObject()
        .put("id", row.getLong("modifiedby_id"))
        .put("name", row.getString("modifiedby_name"))
        .put("lastName", row.getString("modifiedby_lastname"))
        .put("user", new JsonObject()
          .put("id", row.getLong("modifiedby_user_id"))
          .put("username", row.getString("modifiedby_user_username"))
          .put("password", row.getString("modifiedby_user_password"))))
        .put("modified", DateUtil.toString(row.getLocalDateTime("modified").withNano(0)));
    }
    return json;
  }

}
