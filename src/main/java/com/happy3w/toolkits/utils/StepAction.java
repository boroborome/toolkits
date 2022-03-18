package com.happy3w.toolkits.utils;

import lombok.Getter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public class StepAction<T> {
    private long count;
    private final long step;
    private final BiConsumer<Long, T> action;

    public StepAction(long step, Consumer<Long> action) {
        this.step = step;
        this.action = (s, p) -> action.accept(s);
    }

    public StepAction(long step, BiConsumer<Long, T> action) {
        this.step = step;
        this.action = action;
    }

    public void increase() {
        increase(null);
    }

    public void increase(T param) {
        count++;
        if (count % step == 0 && action != null) {
            action.accept(count, param);
        }
    }
}
