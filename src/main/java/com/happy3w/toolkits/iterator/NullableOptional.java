package com.happy3w.toolkits.iterator;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NullableOptional<T> {
    public static final NullableOptional<?> EMPTY = new NullableOptional(null) {
        @Override
        public boolean isPresent() {
            return false;
        }
    };

    private final T value;

    public NullableOptional(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }

    public void ifPresent(Consumer<? super T> action) {
        if (isPresent()) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (isPresent()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public NullableOptional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(value) ? this : empty();
        }
    }

    public static <T> NullableOptional<T> of(T value) {
        return new NullableOptional<>(value);
    }

    public static<T> NullableOptional<T> empty() {
        @SuppressWarnings("unchecked")
        NullableOptional<T> t = (NullableOptional<T>) EMPTY;
        return t;
    }
}
