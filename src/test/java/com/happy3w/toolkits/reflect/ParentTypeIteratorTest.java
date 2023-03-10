package com.happy3w.toolkits.reflect;

import com.happy3w.toolkits.iterator.EasyIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ParentTypeIteratorTest {

    @Test
    void should_find_all_parents_success() {
        Set<Class> types = EasyIterator.fromIterator(new ParentTypeIterator(B.class))
                .toSet();

        Assertions.assertEquals(2, types.size());
    }

    private static class A implements C {

    }

    private interface C {

    }

    public static class B extends A implements C {

    }
}