package com.happy3w.toolkits.tube.holder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleDataHolder<T> implements ReactDataHolder<T> {
    private Queue<T> queue = new LinkedList<>();
    @Override
    public void addValues(Collection<T> values) {
        queue.addAll(values);
    }
}
