package com.happy3w.toolkits.iterator;

public abstract class NeedFindIterator<T> extends EasyIterator<T> {

    protected IteratorStatus status = IteratorStatus.needFind;
    protected T nextItem;

    @Override
    public boolean hasNext() {
        if (status == IteratorStatus.end) {
            return false;
        }

        if (status == IteratorStatus.needFind) {
            findNext();
        }
        return status == IteratorStatus.found;
    }


    @Override
    public T next() {
        if (status == IteratorStatus.end) {
            return null;
        }
        if (status == IteratorStatus.needFind) {
            findNext();
        }

        if (status == IteratorStatus.found) {
            status = IteratorStatus.needFind;
            return nextItem;
        }
        return null;
    }

    protected abstract void findNext();

    protected enum IteratorStatus {
        needFind,
        found,
        end
    }
}
