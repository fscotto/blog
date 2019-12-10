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

package org.fscotto.blog.util;

import io.vertx.core.json.JsonObject;
import org.fscotto.blog.domain.Article;
import org.fscotto.blog.domain.Author;
import org.fscotto.blog.domain.User;
import org.fscotto.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class JsonUtilTest {

  private JsonObject json;
  private Article object;

  @BeforeEach
  void setUp() {
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    var now = LocalDateTime.now().withNano(0);
    this.json = new JsonObject()
      .put("id", 1)
      .put("createdBy", new JsonObject()
        .put("id", 1)
        .put("name", "J.R.")
        .put("lastName", "Tolkien")
        .put("user", new JsonObject()
          .put("id", 1)
          .put("username", "joe")
          .put("password", "secret")))
      .put("created", formatter.format(now))
      .putNull("modifiedBy")
      .putNull("modified")
      .put("title", "Lord of the rings")
      .put("content", "");

    this.object = Builder.of(Article::new)
      .with(Article::setId, 1L)
      .with(Article::setCreatedBy, new Author(1L, "J.R.", "Tolkien", new User(1L, "joe", "secret")))
      .with(Article::setCreated, now)
      .with(Article::setTitle, "Lord of the rings")
      .with(Article::setContent, "")
      .build();
  }

  @Test
  @DisplayName("Should be convert from JsonObject to Object")
  void shouldBeConvertFromJsonToObject() {
    var actual = JsonUtil.fromJson(json, Article.class).get();
    assertThat(actual).isEqualTo(object);
  }

  @Test
  @DisplayName("Should be convert from Object to JsonObject")
  void shouldBeConvertFromObjectToJson() {
    var actual = JsonUtil.toJson(object);
    assertThat(actual).isEqualTo(json);
  }

}