package com.happy3w.toolkits.reaction;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class DataReaction {
    private Map<String, BasketHolder> basketsMap = new HashMap<>();

    public <T> DataReaction withBasket(String basketName, BiConsumer<Set<T>, DataReaction> basketConsumer) {
        basketsMap.put(basketName, new BasketHolder(basketName, basketConsumer));
        return this;
    }

    public DataReaction acceptDatas(String basketName, Collection values) {
        basketsMap.get(basketName).acceptValues(values);
        return this;
    }

    public void react() {
        while (true) {
            Optional<BasketHolder> holderOpt = basketsMap.values().stream()
                    .filter(holder -> !holder.values.isEmpty())
                    .findFirst();
            if (!holderOpt.isPresent()) {
                break;
            }

            BasketHolder curHolder = holderOpt.get();
            Set orgValue = new HashSet(curHolder.getValues());
            curHolder.getValues().clear();
            curHolder.getConsumer().accept(orgValue, this);
        }
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

        public void acceptValues(Collection values) {
            for (Object v : values) {
                if (!existValues.contains(v)) {
                    existValues.add(v);
                    this.values.add(v);
                }
            }
        }
    }
}
