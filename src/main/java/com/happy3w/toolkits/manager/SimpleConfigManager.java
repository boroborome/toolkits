package com.happy3w.toolkits.manager;

public class SimpleConfigManager<DT, CT> extends AbstractConfigManager<DT, CT, SimpleConfigManager<DT, CT>> {
    @Override
    public CT findByType(Class<? extends DT> dataType) {
        return findByTypeStep(dataType);
    }
}
