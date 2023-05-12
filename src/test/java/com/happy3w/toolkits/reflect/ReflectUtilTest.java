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
        Assertions.assertEquals(new HashSet<>(Arrays.asList("getName", "getAge", "isMan", "getNewName", "setName", "setAge", "setMan")),
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
        b.setName("a");
        b.setAge(18);
        b.setMan(true);

        Map<String, Object> newMap = ReflectUtil.enumValues(b)
                .toMap(Pair::getKey, Pair::getValue);

        Assertions.assertEquals(MapBuilder.of("name", (Object) "a")
                        .and("age", 18)
                        .and("man", Boolean.TRUE)
                        .build(),
                newMap);
    }

    public static class A {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class B extends A {
        private int age;
        private boolean man;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isMan() {
            return man;
        }

        public void setMan(boolean man) {
            this.man = man;
        }

        public String getNewName(String prefix) {
            return "aaa";
        }
    }
}