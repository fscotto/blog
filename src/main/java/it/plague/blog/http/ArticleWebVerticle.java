package it.plague.blog.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import it.plague.blog.config.WebConstant;
import it.plague.blog.database.ArticleDatabaseService;
import it.plague.blog.domain.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class ArticleWebVerticle extends AbstractHttpArticle {

  private final ArticleDatabaseService articleDbService;

  @Inject
  public ArticleWebVerticle(HttpServer httpServer, Router router, TemplateEngine templateEngine, ArticleDatabaseService articleDbService,
                            @Named(WebConstant.HTTP_SERVER_HOST) String host, @Named(WebConstant.HTTP_SERVER_PORT) String port) {
    super(httpServer, router, templateEngine, host, port);
    this.articleDbService = articleDbService;
  }

  @Override
  public void start(Promise<Void> promise) {
    log.info("Setting routing urls handled from ArticleWebArticle...");
    router.get("/").handler(this::indexHandler);
    router.get("/article/:id").handler(this::articleHandler);
    super.start(promise);
  }

  private void indexHandler(RoutingContext context) {
    articleDbService.fetchAllArticles(reply -> {
      if (reply.succeeded()) {
        final var articles = reply.result()
          .stream()
          .filter(Objects::nonNull)
          .filter(JsonObject.class::isInstance)
          .map(JsonObject.class::cast)
          .map(Article::new)
          .sorted(Comparator.comparing(Article::getCreated))
          .collect(Collectors.toList());
        context.put("title", "Home");
        context.put("articles", articles);
        context.put("viewPagination", articles.size() > 10);
        renderPage(context, "index");
      } else {
        log.error("Loading index failed", reply.cause());
        context.fail(reply.cause());
      }
    });
  }

  private void articleHandler(RoutingContext context) {
    var id = NumberUtils.toLong(context.request().getParam("id"));
    articleDbService.fetchArticle(id, reply -> {
      if (reply.succeeded()) {
        var article = new Article(reply.result().getJsonObject("article"));
        context.put("title", article.getTitle());
        context.put("article", article);
        renderPage(context, "article");
      } else {
        log.error("Loading article failed", reply.cause());
        context.fail(reply.cause());
      }
    });
  }

}
