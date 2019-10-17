package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.BeanMonad;
import it.plague.blog.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DataObject
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  private Long id;
  private String username;
  private String password;

  public User(JsonObject jsonObject) {
    JsonUtil.fromJson(jsonObject, this.getClass())
      .ifPresent(o -> BeanMonad.copy(this, o));
  }

  public JsonObject toJson() {
    return JsonUtil.toJson(this);
  }

}
