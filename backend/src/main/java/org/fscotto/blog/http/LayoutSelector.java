/*
 * Copyright (C) 2019 Fabio Scotto di Santolo <fabio.scottodisantolo@gmail.com>
 *
 * This file is part of plague-blog.
 *
 * plague-blog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * plague-blog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with plague-blog.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fscotto.blog.http;

import io.vertx.reactivex.core.http.HttpServerRequest;
import org.apache.commons.lang3.StringUtils;

public class LayoutSelector {
  private static final String ROOT_PATH = "templates/";

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
    String defaultTemplate = new StringBuilder(ROOT_PATH)
      .append(this.template)
      .toString();

    if (this.layout != null) {
      return new StringBuilder(ROOT_PATH)
        .append(this.layout)
        .append("/")
        .append(this.template)
        .toString();
    } else if (this.request != null) {
      var requestParam = this.request.getParam("layout");
      if (StringUtils.isNotBlank(requestParam)) {
        return new StringBuilder(ROOT_PATH)
          .append(requestParam)
          .append("/")
          .append(this.template)
          .toString();
      }
    }
    return defaultTemplate;
  }

}
