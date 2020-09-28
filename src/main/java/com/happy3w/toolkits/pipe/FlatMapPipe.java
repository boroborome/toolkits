package com.happy3w.toolkits.pipe;

import java.util.Iterator;
import java.util.function.Function;

public class FlatMapPipe<InType, OutType> extends EasyPipe<InType, OutType> {
    protected final Function<InType, Iterator<OutType>> mapMethod;

    public FlatMapPipe(Function<InType, Iterator<OutType>> mapMethod) {
        this.mapMethod = mapMethod;
    }

    @Override
    public void accept(InType data) {
        Iterator<OutType> subInnerIterator = mapMethod.apply(data);
        while (subInnerIterator.hasNext()) {
            nextPipe.accept(subInnerIterator.next());
        }
    }
}
