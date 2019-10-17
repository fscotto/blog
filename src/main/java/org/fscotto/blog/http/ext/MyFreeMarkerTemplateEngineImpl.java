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

package org.fscotto.blog.http.ext;

import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.templ.freemarker.impl.FreeMarkerTemplateEngineImpl;
import io.vertx.ext.web.templ.freemarker.impl.VertxWebObjectWrapper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

public class MyFreeMarkerTemplateEngineImpl extends FreeMarkerTemplateEngineImpl implements MyFreeMarkerTemplateEngine {
  protected final Configuration configuration;

  public MyFreeMarkerTemplateEngineImpl(Vertx vertx) {
    super(vertx);
    this.configuration = new Configuration(Configuration.VERSION_2_3_28);
    this.configuration.setObjectWrapper(new VertxWebObjectWrapper(this.configuration.getIncompatibleImprovements()));
    this.configuration.setTemplateLoader(new MyFreeMarkerTemplateLoader(vertx));
    this.configuration.setCacheStorage(new NullCacheStorage());
    this.configuration.setSharedVariable("formatDateTime", new LocalDateTemplateModel());
    this.configuration.setSharedVariable("markdownToHtml", new MarkdownToHtmlTransformerTemplateModel());
  }

  @Override
  public void render(Map<String, Object> context, String templateFile, Handler<AsyncResult<Buffer>> handler) {
    try {
      Template template = this.isCachingEnabled() ? this.cache.get(templateFile) : null;
      if (template == null) {
        synchronized (this) {
          template = this.configuration.getTemplate(this.adjustLocation(templateFile));
        }
        if (this.isCachingEnabled()) {
          this.cache.put(templateFile, template);
        }
      }
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        template.process(context, new OutputStreamWriter(baos));
        handler.handle(Future.succeededFuture(Buffer.buffer(baos.toByteArray())));
      }
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
    }
  }
}
