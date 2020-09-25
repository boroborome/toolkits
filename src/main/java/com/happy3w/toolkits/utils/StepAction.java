package com.happy3w.toolkits.utils;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class StepAction {
    private long count;
    private final long step;
    private final Consumer<Long> action;

    public StepAction(long step, Consumer<Long> action) {
        this.step = step;
        this.action = action;
    }

    public void increase() {
        count++;
        if (count % step == 0 && action != null) {
            action.accept(count);
        }
    }
}
