package com.happy3w.toolkits.iterator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SubItemIteratorTest {

    @Test
    void findNext() {
        Map<String, Item> itemMap = new HashMap<>();
        newItem(itemMap, "a", "b", "c", "d");
        newItem(itemMap, "b", "a", "c", "d");
        newItem(itemMap, "c", "b", "a", "d");
        newItem(itemMap, "d", "b", "c", "d");

        List<String> result = new SubItemIterator<>(itemMap.get("a"), Item::getCode,
                item -> EasyIterator.fromIterable(item.getDepends())
                        .map(c -> itemMap.get(c))
        ).map(Item::getCode)
                .sorted()
                .toList();

        Assertions.assertEquals("[a, b, c, d]", result.toString());
    }

    private void newItem(Map<String, Item> itemMap, String code, String... depends) {
        Item item = new Item(code, Arrays.asList(depends));
        itemMap.put(item.getCode(), item);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Item {
        private String code;
        private List<String> depends;
    }
}
