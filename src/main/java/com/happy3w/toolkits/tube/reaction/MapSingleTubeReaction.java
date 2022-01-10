package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

import java.util.function.Consumer;
import java.util.function.Function;

public class MapSingleTubeReaction<T, R> implements SingleTubeReaction<T> {
    private Function<T, R> function;

    public MapSingleTubeReaction(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public void reaction(T data, ReactDataHolder holder) {
        // TODO
    }
}
