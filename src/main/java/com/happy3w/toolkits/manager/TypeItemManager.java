package com.happy3w.toolkits.manager;

public class TypeItemManager<DT, T extends ITypeItem<DT>> extends TypeProcessorManager<DT, T, TypeItemManager<DT, T>> {

    public void registItem(T item) {
        registProcess(item.getType(), item);
    }
}
