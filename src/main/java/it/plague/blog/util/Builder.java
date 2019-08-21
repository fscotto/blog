package it.plague.blog.util;

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
    return new Builder<T>(instantiator);
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
