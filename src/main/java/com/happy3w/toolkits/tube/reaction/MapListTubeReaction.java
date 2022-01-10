package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapListTubeReaction<T> implements ListTubeReaction<T> {
    private Function<List<T>, Map<String, List<?>>> function;

    public MapListTubeReaction(Function<List<T>, Map<String, List<?>>> function) {
        this.function = function;
    }

    @Override
    public void reaction(List<T> datas, ReactDataHolder holder) {

    }
}
