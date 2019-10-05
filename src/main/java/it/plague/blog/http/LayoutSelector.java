package it.plague.blog.http;

import io.vertx.reactivex.core.http.HttpServerRequest;
import org.apache.commons.lang3.StringUtils;

public class LayoutSelector {
  private final HttpServerRequest request;
  private final String template;
  private final String layout;

  LayoutSelector(HttpServerRequest request, final String template) {
    this.request = request;
    this.template = template;
    this.layout = null;
  }

  LayoutSelector(final String layout, final String template) {
    this.request = null;
    this.template = template;
    this.layout = layout;
  }

  public String getTemplate() {
    String defaultTemplate = new StringBuilder("templates/")
      .append(this.template)
      .toString();

    if (this.layout != null) {
      return new StringBuilder("templates/")
        .append(this.layout)
        .append("/")
        .append(this.template)
        .toString();
    } else if (this.request != null) {
      var layout = this.request.getParam("layout");
      if (StringUtils.isNotBlank(layout)) {
        return new StringBuilder("templates/")
          .append(layout)
          .append("/")
          .append(this.template)
          .toString();
      }
    }
    return defaultTemplate;
  }

}
