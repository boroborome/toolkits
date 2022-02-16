package com.happy3w.toolkits.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TypeItemManagerTest {

    @Test
    public void should_find_date_success() {
        TypeItemManager manager = TypeItemManager.inherit();
        manager.registItem(new MyTypeItem(String.class, "Str"));
        manager.registItem(new MyTypeItem(Date.class, "Date"));

        MyTypeItem item = (MyTypeItem) manager.findByType(Date.class);
        Assertions.assertEquals("Date", item.getValue());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MyTypeItem implements ITypeItem {
        private Class<?> type;
        private String value;
    }
}
