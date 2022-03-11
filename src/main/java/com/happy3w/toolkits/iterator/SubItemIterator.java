package com.happy3w.toolkits.iterator;

import com.happy3w.java.ext.Pair;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

public class SubItemIterator<T, K> extends NeedFindIterator<T> {
    private Function<T, K> keyGetter;
    private Function<T, Iterator<T>> listSubFun;

    private Set<K> existKeys = new HashSet<>();
    private Stack<T> dataToListSub = new Stack<>();
    private IEasyIterator<T> dataIterator;

    public SubItemIterator(T source, Function<T, K> keyGetter, Function<T, Iterator<T>> listSubFun) {
        this.keyGetter = keyGetter;
        this.listSubFun = listSubFun;
        dataIterator = createFilteredIt(EasyIterator.<T>of(source));
    }

    private IEasyIterator<T> createFilteredIt(IEasyIterator<T> it) {
        return it.map(s -> new Pair<>(keyGetter.apply(s), s))
                .filter(p -> !existKeys.contains(p.getKey()))
                .peek(p -> existKeys.add(p.getKey()))
                .map(Pair::getValue);
    }

    @Override
    protected NullableOptional<T> findNext() {
        if (!dataIterator.hasNext()) {
            while (!dataToListSub.isEmpty()) {
                T data = dataToListSub.pop();
                Iterator<T> it = listSubFun.apply(data);
                IEasyIterator<T> filteredIt = createFilteredIt(EasyIterator.fromIterator(it));
                if (filteredIt.hasNext()) {
                    dataIterator = filteredIt;
                    break;
                }
            }
        }

        if (dataIterator.hasNext()) {
            T data = dataIterator.next();
            dataToListSub.push(data);
            return NullableOptional.of(data);
        }
        return NullableOptional.empty();
    }
}
