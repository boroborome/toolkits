package com.happy3w.toolkits.iterator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexedItem<T> {
    private long index;
    private T value;
}
