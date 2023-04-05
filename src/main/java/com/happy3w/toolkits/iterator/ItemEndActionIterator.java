package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Consumer;

public class ItemEndActionIterator<T> implements IEasyIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final Consumer<T> endAction;
    protected T preItem = null;
    protected boolean containsItem = false;
    protected boolean performed = false;

    public ItemEndActionIterator(Iterator<T> innerIterator, Consumer<T> endAction) {
        this.innerIterator = innerIterator;
        this.endAction = endAction;
    }

    @Override
    public boolean hasNext() {
        boolean result = innerIterator.hasNext();
        if (!result && !performed && containsItem) {
            endAction.accept(preItem);
            performed = true;
        }
        return result;
    }

    @Override
    public T next() {
        if (containsItem) {
            endAction.accept(preItem);
        }
        preItem = innerIterator.next();
        containsItem = true;
        return preItem;
    }
}
