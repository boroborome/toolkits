package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.utils.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

public class TypeItemManager<T extends ITypeItem> {
    private Map<Class, T> itemMap = new HashMap<>();

    public void registItem(T item) {
        itemMap.put(item.getType(), item);
    }

    public T findItemByType(Class type) {
        return itemMap.get(type);
    }

    public T findItemInheritType(Class type) {
        for (Map.Entry<Class, T> entry : itemMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public TypeItemManager<T> newCopy() {
        TypeItemManager newManager = ReflectUtil.newInstance(this.getClass());
        newManager.itemMap.putAll(itemMap);
        return newManager;
    }
}
