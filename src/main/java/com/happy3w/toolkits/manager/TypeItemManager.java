package com.happy3w.toolkits.manager;

public class TypeItemManager<DT, T extends ITypeItem<DT>> extends AbstractConfigManager<T, TypeItemManager<DT, T>> {

    public void registItem(T item) {
        regist(item.getType(), item);
    }

    public static <DT, T extends ITypeItem<DT>> TypeItemManager<DT, T> inherit() {
        return new TypeItemManager<DT, T>()
                .finder((IConfigFinder<T>) InheritFinder.INSTANCE);
    }
}
