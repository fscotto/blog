package it.plague.blog.config.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import it.plague.blog.config.WebConstant;
import it.plague.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ConfigModuleTest {

  @Inject
  @Named(WebConstant.HTTP_SERVER_HOST)
  String host;

  @Inject
  @Named(WebConstant.HTTP_SERVER_PORT)
  String port;

  @BeforeEach
  void setUp() {
    Guice.createInjector(new ConfigModule(), BoundFieldModule.of(this)).injectMembers(this);
  }

  private void equalAndNotBlankBehaviour(String actual, String expected) {
    assertThat(actual).isEqualTo(expected).isNotBlank();
  }

  @Test
  @DisplayName("Should be default host value")
  void shouldBeDefaultHostValue() throws UnknownHostException {
    equalAndNotBlankBehaviour(host, InetAddress.getLocalHost().getHostName());
  }

  @Test
  @DisplayName("Should be default port value")
  void shouldBeDefaultPortValue() {
    equalAndNotBlankBehaviour(port, "9000");
  }

  private static class EnvironmentMock {

    static void injectEnvironmentVariable(String key, String value) throws Exception {
      Class<?> processEnvironment = Class.forName("java.lang.ProcessEnvironment");
      Field unmodifiableMapField = getAccessibleField(processEnvironment, "theUnmodifiableEnvironment");
      Object unmodifiableMap = unmodifiableMapField.get(null);
      injectIntoUnmodifiableMap(key, value, unmodifiableMap);
      Field mapField = getAccessibleField(processEnvironment, "theEnvironment");
      Map<String, String> map = (Map<String, String>) mapField.get(null);
      map.put(key, value);
    }

    private static Field getAccessibleField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    }

    private static void injectIntoUnmodifiableMap(String key, String value, Object map) throws ReflectiveOperationException {
      Class unmodifiableMap = Class.forName("java.util.Collections$UnmodifiableMap");
      Field field = getAccessibleField(unmodifiableMap, "m");
      Object obj = field.get(map);
      ((Map<String, String>) obj).put(key, value);
    }
  }

  @Nested
  @DisplayName("Setting environment variable")
  class WhenSettingEnvironmentVariable {

    @Inject
    @Named(WebConstant.HTTP_SERVER_HOST)
    String host;

    @Inject
    @Named(WebConstant.HTTP_SERVER_PORT)
    String port;

    @BeforeEach
    void setUp() throws Exception {
      bindNewEnvironmentVariable();
      Guice.createInjector(new ConfigModule(), BoundFieldModule.of(this)).injectMembers(this);
    }

    private void bindNewEnvironmentVariable() throws Exception {
      EnvironmentMock.injectEnvironmentVariable("HOST", "myhost");
      EnvironmentMock.injectEnvironmentVariable("PORT", "8080");
    }

    @Test
    @DisplayName("Should be environment host value")
    void shouldBeEnvironmentHostValue() {
      equalAndNotBlankBehaviour(host, "myhost");
    }

    @Test
    @DisplayName("Should be default port value")
    void shouldBeEnvironmentPortValue() {
      equalAndNotBlankBehaviour(port, "8080");
    }

  }

}