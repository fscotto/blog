package it.plague.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import it.plague.blog.util.JsonUtil;

@DataObject
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private Long id;
	private String username;
	private String password;

	public User() {
		this(0L, "", "");
	}

	public User(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public User(JsonObject jsonObject) {
		JsonUtil.fromJson(jsonObject, this.getClass()).ifPresent(this::init);
	}

	private void init(User user) {
		this.id = user.id;
		this.username = user.username;
		this.password = user.password;
	}

	public JsonObject toJson() {
		return JsonUtil.toJson(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equal(id, user.id) &&
			Objects.equal(username, user.username) &&
			Objects.equal(password, user.password);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, username, password);
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", username='" + username + '\'' +
			", password='" + password + '\'' +
			'}';
	}
}
