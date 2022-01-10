package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

import java.util.function.Consumer;

public class ConsumeOneTubeReaction<T> implements SingleTubeReaction<T> {
    private Consumer<T> consumer;

    public ConsumeOneTubeReaction(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void reaction(T data, ReactDataHolder holder) {
        // TODO
    }
}
