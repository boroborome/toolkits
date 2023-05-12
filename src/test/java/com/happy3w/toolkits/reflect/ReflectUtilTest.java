package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.Pair;
import com.happy3w.toolkits.utils.MapBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ReflectUtilTest {

    @Test
    void enumMethods() {
        Set<String> names = ReflectUtil.enumMethods(B.class)
                .map(Method::getName)
                .toSet();
        Assertions.assertEquals(new HashSet<>(Arrays.asList("getName", "getAge", "isMan", "getNewName")),
                names);
    }

    @Test
    void should_enum_success_with_map() {
        Map<String, Object> map = MapBuilder.of("name", (Object) "a")
                .and("age", 18)
                .build();

        Map<String, Object> newMap = ReflectUtil.enumValues(map)
                .toMap(Pair::getKey, Pair::getValue);

        Assertions.assertEquals(map, newMap);
    }

    @Test
    void should_enum_success_with_object() {
        B b = new B();

        Map<String, Object> newMap = ReflectUtil.enumValues(b)
                .toMap(Pair::getKey, Pair::getValue);

        Assertions.assertEquals(MapBuilder.of("name", (Object) "a")
                        .and("age", 18)
                        .and("man", Boolean.TRUE)
                        .build(),
                newMap);
    }

    public static class A {
        public String getName() {
            return "a";
        }
    }

    public static class B extends A {
        public int getAge() {
            return 18;
        }

        public boolean isMan() {
            return true;
        }

        public String getNewName(String prefix) {
            return "aaa";
        }
    }
}