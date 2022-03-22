package com.happy3w.toolkits.utils;

import lombok.Getter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Getter
public class TimeStepAction<T> {
    private long count;
    private long nextTimestamp;
    private final long millisSecondStep;
    private final BiConsumer<Long, T> action;

    public TimeStepAction(long millisSecondStep, Consumer<Long> action) {
        this(millisSecondStep, (s, p) -> action.accept(s));
    }

    public TimeStepAction(long millisSecondStep, BiConsumer<Long, T> action) {
        this.millisSecondStep = millisSecondStep;
        this.action = action;
        this.nextTimestamp = System.currentTimeMillis() + millisSecondStep;
    }

    public void increase() {
        increase(null);
    }

    public void increase(T param) {
        count++;
        long curTimestamp = System.currentTimeMillis();
        if (curTimestamp >= nextTimestamp && action != null) {
            action.accept(count, param);
            nextTimestamp = curTimestamp + millisSecondStep;
        }
    }
}
