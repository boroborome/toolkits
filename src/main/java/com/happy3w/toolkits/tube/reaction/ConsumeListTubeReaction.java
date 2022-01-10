package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

import java.util.List;
import java.util.function.Consumer;

public class ConsumeListTubeReaction<T> implements ListTubeReaction<T> {
    private Consumer<List<T>> consumer;

    public ConsumeListTubeReaction(Consumer<List<T>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void reaction(List<T> datas, ReactDataHolder holder) {

    }
}
