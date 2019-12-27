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

package org.fscotto.blog.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpStatusCode {
  // 2xx
  public static final int OK = 200;
  public static final int CREATED = 201;
  public static final int NO_CONTENT = 204;

  // 4xx
  public static final int NOT_FOUND = 404;
  public static final int METHOD_NOT_ALLOWED = 405;
  public static final int CONFLICT = 409;

  // 5xx
  public static final int SERVER_ERROR = 500;
}
