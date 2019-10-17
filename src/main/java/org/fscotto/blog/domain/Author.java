package org.fscotto.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fscotto.blog.util.BeanMonad;
import org.fscotto.blog.util.JsonUtil;

@Data
@DataObject
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

  private Long id;
  private String name;
  private String lastName;

  @JsonSerialize
  @JsonDeserialize
  private User user;

  public Author(JsonObject jsonObject) {
    JsonUtil.fromJson(jsonObject, this.getClass())
      .ifPresent(o -> BeanMonad.copy(this, o));
  }

  public JsonObject toJson() {
    return JsonUtil.toJson(this);
  }

}
