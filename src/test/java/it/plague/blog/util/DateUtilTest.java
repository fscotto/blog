package it.plague.blog.util;

import it.plague.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;

@UnitTest
class DateUtilTest {

  @Test
  @DisplayName("Should be not throw NullPointerException with null call toString method")
  void shouldBeNotThrowNPEWithNullValueToString() {
    assertThatCode(() -> DateUtil.toString(null))
      .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Should be convert from LocalDateTime to String with toString method")
  void shouldBeConvertFromLocalDateTimeToString() {
    assertThat(DateUtil.toString(LocalDateTime.of(2019, Month.JANUARY, 01, 23, 59, 59)))
      .isNotBlank()
      .isEqualTo("2019-01-01 23:59:59");
  }

  @Test
  @DisplayName("Should be throw NullPointerException with input null call toLocalDateTime method")
  void shouldBeThrowNPEWithNullValueToLocalDateTime() {
    assertThatNullPointerException()
      .isThrownBy(() -> DateUtil.toLocalDateTime(null))
      .withMessage("date is marked non-null but is null");
  }

  @Test
  @DisplayName("Should be convert from java.util.Date to LocalDateTime with toLocalDateTime method")
  void shouldBeConvertFromDateToLocalDateTime() {
    assertThat(DateUtil.toLocalDateTime(new java.util.Date()))
      .isEqualToIgnoringNanos(LocalDateTime.now());
  }

  @Test
  @DisplayName("Should be throw NullPointerException with input call toDate method")
  void shouldBeThrowNPEWithNullValueToDate() {
    assertThatNullPointerException()
      .isThrownBy(() -> DateUtil.toDate(null))
      .withMessage("localDateTime is marked non-null but is null");
  }

  @Test
  @DisplayName("Should be convert from LocalDateTime to java.util.Date with toDate method")
  void shouldBeConvertFromLocalDateTimeToDate() {
    assertThat(DateUtil.toDate(LocalDateTime.now()))
      .isEqualToIgnoringMillis(new java.util.Date());
  }

}
