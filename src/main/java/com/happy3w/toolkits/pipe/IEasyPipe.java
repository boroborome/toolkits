package com.happy3w.toolkits.pipe;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public interface IEasyPipe<InType> {

    void flush();

    void accept(InType data);

    default void accept(Collection<InType> list) {
        for (InType d : list) {
            accept(d);
        }
    }

    default void accept(Iterator<InType> it) {
        while (it.hasNext()) {
            accept(it.next());
        }
    }

    default void accept(Stream<InType> stream) {
        accept(stream.iterator());
    }
}
