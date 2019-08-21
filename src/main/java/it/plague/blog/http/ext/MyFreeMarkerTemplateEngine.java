package it.plague.blog.http.ext;

import io.vertx.core.Vertx;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

public interface MyFreeMarkerTemplateEngine extends FreeMarkerTemplateEngine {

  static MyFreeMarkerTemplateEngine create(Vertx vertx) {
    return new MyFreeMarkerTemplateEngineImpl(vertx);
  }
}
