package com.happy3w.toolkits.tube;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.toolkits.tube.reaction.ConsumeOneTubeReaction;
import com.happy3w.toolkits.tube.reaction.MapListTubeReaction;
import com.happy3w.toolkits.tube.reaction.MapSingleTubeReaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataTube {
    private static Map<String, ReactDataInfo> tubeReactions = new HashMap<>();
    private TubeProcessor tubProcessor;

    public DataTube react(String typeName, Function<List, Map<String, List>> mapFunction) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(typeName, k -> new ReactDataInfo());
        // TODO
//        rdi.addReaction(new MapListTubeReaction<>(mapFunction));
        return this;
    }

    public DataTube consumeOne(String typeName, Consumer<?> consumer) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(typeName, k -> new ReactDataInfo());
        rdi.addReaction(new ConsumeOneTubeReaction(consumer));
        return this;
    }

    public DataTube reactOne(String typeName, Function<?, ?> mapFun) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(typeName, k -> new ReactDataInfo());
        rdi.addReaction(new MapSingleTubeReaction(mapFun));
        return this;
    }

    public <T> DataTube consumeOne(Class<T> type, Consumer<T> consumer) {
        return consumeOne(type.getName(), consumer);
    }

    public <T> DataTube reactOne(Class<T> type, Function<T, ?> mapFun) {
        return reactOne(type.getName(), mapFun);
    }

    public DataTube consumeList(String typeName, Consumer<List<?>> consumer) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(typeName, k -> new ReactDataInfo());
        rdi.addReaction(new ConsumeOneTubeReaction(consumer));
        return this;
    }

    public DataTube reactList(String typeName, Function<List<?>, List<?>> function) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(typeName, k -> new ReactDataInfo());
        rdi.addReaction(new MapListTubeReaction<>(list -> {
            List<?> result = function.apply(list);

            Map<String, List<?>> finalResult;
            if (ListUtils.isEmpty(result)) {
                finalResult = Collections.emptyMap();
            } else {
                Object first = result.get(0);
                finalResult = new HashMap<>();
                finalResult.put(first.getClass().getName(), result);
            }
            return finalResult;
        }));
        return this;
    }

    public <T> DataTube consumeList(Class<T> type, Consumer<List<T>> consumer) {
        Consumer listConsumer = consumer;
        return consumeList(type.getName(), listConsumer);
    }

    public <T> DataTube reactList(Class<T> type, Function<List<T>, List<?>> function) {
        Function listFunction = function;
        return reactList(type.getName(), listFunction);
    }

    public <T> DataTube withValue(Class<T> type, List<T> values) {
        ReactDataInfo rdi = tubeReactions.computeIfAbsent(type.getName(), k -> new ReactDataInfo());
        rdi.addDatas(values);
        return this;
    }

    public DataTube withProcessor(TubeProcessor tubProcessor) {
        this.tubProcessor = tubProcessor;
        return this;
    }

    public TubReactResult reactAll() {
        return null;
    }
}
