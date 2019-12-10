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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Builder<T> {

  private final Supplier<T> instantiator;

  private List<Consumer<T>> instanceModifiers = new ArrayList<>();

  private Builder(Supplier<T> instantiator) {
    this.instantiator = instantiator;
  }

  public static <T> Builder<T> of(Supplier<T> instantiator) {
    return new Builder<>(instantiator);
  }

  public <U> Builder<T> with(BiConsumer<T, U> consumer, U value) {
    Consumer<T> c = instance -> consumer.accept(instance, value);
    instanceModifiers.add(c);
    return this;
  }

  public T build() {
    T value = instantiator.get();
    instanceModifiers.forEach(modifier -> modifier.accept(value));
    instanceModifiers.clear();
    return value;
  }
}
