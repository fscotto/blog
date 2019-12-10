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

import org.fscotto.blog.support.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;

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
    Calendar cal = Calendar.getInstance();
    cal.set(2019, Calendar.JANUARY, 01, 00, 00, 00);
    assertThat(DateUtil.toDate(LocalDateTime.of(2019, Month.JANUARY, 01, 00, 00, 00)))
      .isEqualToIgnoringMillis(cal.getTime());
  }

}
