package com.happy3w.toolkits.manager;

public class TypeItemManager<DT extends ITypeItem> extends AbstractConfigManager<DT, TypeItemManager<DT>> {

    public void registItem(DT item) {
        regist(item.getType(), item);
    }

    public static <DT extends ITypeItem> TypeItemManager<DT> inherit() {
        return new TypeItemManager<DT>()
                .finder((IConfigFinder<DT>) InheritFinder.INSTANCE);
    }
}
