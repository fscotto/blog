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

package org.fscotto.blog.http.ext;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;

public class LocalDateTemplateModel implements TemplateMethodModelEx {

  @Override
  public Object exec(List args) throws TemplateModelException {
    if (args.size() != 2) {
      throw new TemplateModelException("Wrong arguments");
    }
    TemporalAccessor time = (TemporalAccessor) ((StringModel) args.get(0)).getWrappedObject();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(((SimpleScalar) args.get(1)).getAsString(), Locale.ITALY);
    return formatter.format(time);
  }

}
