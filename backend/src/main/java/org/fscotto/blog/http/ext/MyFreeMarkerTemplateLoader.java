package org.fscotto.blog.http.ext;

import freemarker.cache.TemplateLoader;
import io.vertx.core.Vertx;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

class MyFreeMarkerTemplateLoader implements TemplateLoader {
  private final Vertx vertx;

  MyFreeMarkerTemplateLoader(Vertx vertx) {
    this.vertx = vertx;
  }

  public Object findTemplateSource(String name) throws IOException {
    try {
      if (this.vertx.fileSystem().existsBlocking(name)) {
        String templ = this.vertx.fileSystem().readFileBlocking(name).toString(Charset.defaultCharset());
        return new MyFreeMarkerTemplateLoader.StringTemplateSource(name, templ, System.currentTimeMillis());
      } else {
        return null;
      }
    } catch (Exception var3) {
      throw new IOException(var3);
    }
  }

  public long getLastModified(Object templateSource) {
    return ((MyFreeMarkerTemplateLoader.StringTemplateSource) templateSource).lastModified;
  }

  public Reader getReader(Object templateSource, String encoding) throws IOException {
    return new StringReader(((MyFreeMarkerTemplateLoader.StringTemplateSource) templateSource).source);
  }

  public void closeTemplateSource(Object templateSource) throws IOException {
    // I don't know, write this comment for Sonarlint only
  }

  private static class StringTemplateSource {
    private final String name;
    private final String source;
    private final long lastModified;

    StringTemplateSource(String name, String source, long lastModified) {
      if (name == null) {
        throw new IllegalArgumentException("name == null");
      } else if (source == null) {
        throw new IllegalArgumentException("source == null");
      } else if (lastModified < -1L) {
        throw new IllegalArgumentException("lastModified < -1L");
      } else {
        this.name = name;
        this.source = source;
        this.lastModified = lastModified;
      }
    }

    public boolean equals(Object obj) {
      return obj instanceof MyFreeMarkerTemplateLoader.StringTemplateSource && this.name.equals(((MyFreeMarkerTemplateLoader.StringTemplateSource) obj).name);
    }

    public int hashCode() {
      return this.name.hashCode();
    }
  }
}