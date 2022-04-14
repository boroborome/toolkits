package com.happy3w.toolkits.reaction;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class DataReaction {
    private List<BasketHolder> holders = new ArrayList<>();
    private Map<String, BasketHolder> basketsMap = new HashMap<>();

    public <T> DataReaction withBasket(String basketName, BiConsumer<Set<T>, DataReaction> basketConsumer) {
        BasketHolder existHolder = basketsMap.get(basketName);
        if (existHolder != null) {
            holders.remove(existHolder);
        }

        BasketHolder newHolder = new BasketHolder(basketName, basketConsumer);
        holders.add(newHolder);
        basketsMap.put(basketName, newHolder);
        return this;
    }

    public DataReaction acceptDatas(String basketName, Collection values) {
        basketsMap.get(basketName).acceptValues(values.stream());
        return this;
    }

    public DataReaction acceptDatas(String basketName, Stream values) {
        basketsMap.get(basketName).acceptValues(values);
        return this;
    }

    public void react() {
        while (true) {
            BasketHolder runHolder = findNeedRunHolder();
            if (runHolder == null) {
                break;
            }

            Set orgValue = new HashSet(runHolder.getValues());
            runHolder.getValues().clear();
            runHolder.getConsumer().accept(orgValue, this);
        }
    }

    private BasketHolder findNeedRunHolder() {
        for (BasketHolder holder : holders) {
            if (!holder.values.isEmpty()) {
                return holder;
            }
        }
        return null;
    }

    @Getter
    private static class BasketHolder {
        private final String name;
        private final BiConsumer consumer;
        private final Set values = new HashSet();
        private final Set existValues = new HashSet();

        private BasketHolder(String name, BiConsumer consumer) {
            this.name = name;
            this.consumer = consumer;
        }

        public void acceptValues(Stream values) {
            values.forEach(v -> {
                if (!existValues.contains(v)) {
                    existValues.add(v);
                    this.values.add(v);
                }
            });
        }
    }
}
