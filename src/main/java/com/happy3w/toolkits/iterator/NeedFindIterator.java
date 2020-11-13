package com.happy3w.toolkits.iterator;

import java.util.Optional;

public abstract class NeedFindIterator<T> extends EasyIterator<T> {

    private IteratorStatus status = IteratorStatus.needFind;
    private T nextItem;

    @Override
    public boolean hasNext() {
        if (status == IteratorStatus.end) {
            return false;
        }

        if (status == IteratorStatus.needFind) {
            Optional<T> optNext = findNext();
            if (optNext.isPresent()) {
                nextItem = optNext.get();
                status = IteratorStatus.found;
            } else {
                status = IteratorStatus.end;
            }
        }
        return status == IteratorStatus.found;
    }


    @Override
    public T next() {
        if (status == IteratorStatus.end) {
            return null;
        }
        if (status == IteratorStatus.needFind) {
            Optional<T> optNext = findNext();
            if (optNext.isPresent()) {
                nextItem = optNext.get();
            } else {
                status = IteratorStatus.end;
            }
        }

        if (status == IteratorStatus.found) {
            status = IteratorStatus.needFind;
            return nextItem;
        }
        return null;
    }

    protected abstract Optional<T> findNext();

    public enum IteratorStatus {
        needFind,
        found,
        end
    }
}
