package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class EndActionIteratorTest {

    @Test
    void should_action_at_end_success() {
        AtomicInteger flag = new AtomicInteger(0);
        IEasyIterator<String> it = EasyIterator.of("a", "b", "c")
                .onEnd(() -> {
                    flag.incrementAndGet();
                });

        it.hasNext();
        it.next();
        Assertions.assertEquals(0, flag.get());

        it.hasNext();
        it.next();
        Assertions.assertEquals(0, flag.get());

        it.hasNext();
        it.next();
        Assertions.assertEquals(0, flag.get());

        it.hasNext();
        it.next();
        Assertions.assertEquals(1, flag.get());

        it.hasNext();
        it.next();
        Assertions.assertEquals(1, flag.get());


    }
}
