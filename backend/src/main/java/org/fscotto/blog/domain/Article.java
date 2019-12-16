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

package org.fscotto.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.sqlclient.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fscotto.blog.util.BeanUtil;
import org.fscotto.blog.util.JsonUtil;

import java.time.LocalDateTime;

@Data
@DataObject
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

  private Long id;
  private Author createdBy;
  private LocalDateTime created;
  private Author modifiedBy;
  private LocalDateTime modified;
  private String title;
  private String content;

  public Article(JsonObject jsonObject) {
    JsonUtil.fromJson(jsonObject, this.getClass())
      .ifPresent(o -> BeanUtil.copy(this, o));
  }

  public JsonObject toJson() {
    return JsonUtil.toJson(this);
  }

  public Tuple toCreateTuple() {
    return Tuple.of(this.createdBy.getId(), this.created, this.title, this.content);
  }

  public Tuple toUpdateTuple() {
    return Tuple.of(this.modifiedBy.getId(), this.modified, this.title, this.content, this.id);
  }

}
