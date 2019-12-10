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

package org.fscotto.blog.database;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SqlQuery {
  public static final String FETCH_ALL_ARTICLES =
    "select " +
      "	art.id," +
      "	art.createdby createdby_id, " +
      "	aut1.name createdby_name, " +
      "	aut1.lastname createdby_lastname, " +
      "	aut1.userid createdby_user_id, " +
      "	u1.username createdby_user_username, " +
      "	u1.password createdby_user_password, " +
      "	art.created, " +
      "	art.modifiedby modifiedby_id, " +
      "	aut2.name modifiedby_name, " +
      "	aut2.lastname modifiedby_lastname, " +
      "	aut2.userid modifiedby_user_id, " +
      "	u2.username modifiedby_user_username, " +
      "	u2.password modifiedby_user_password, " +
      " art.modified, " +
      "	art.title, " +
      "	art.content " +
      "from " +
      "	blog.articles art " +
      "join blog.authors aut1 on " +
      "	aut1.id = art.createdby " +
      "join blog.users u1 on " +
      "	u1.id = aut1.userid " +
      "left join blog.authors aut2 on " +
      "	aut2.id = art.modifiedby " +
      "left join blog.users u2 on " +
      "	u2.id = aut2.userid";

  public static final String FETCH_ONE_ARTICLE = FETCH_ALL_ARTICLES + " where art.id = $1";
  public static final String INSERT_ARTICLE = "insert into blog.articles (id, createdby, created, title, content) " +
    "value (default, $1, $2, $3, $4)";
  public static final String UPDATE_ARTICLE = "update blog.articles set modifiedBy = $1, modified = $2, title = $3, content = $4 where id = $5";
  public static final String DELETE_ARTICLE = "delete from blog.articles where id = $1";
}
