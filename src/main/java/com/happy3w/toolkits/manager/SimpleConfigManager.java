package com.happy3w.toolkits.manager;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SimpleConfigManager<DT, CT> extends AbstractConfigManager<DT, CT, SimpleConfigManager<DT, CT>> {
    public SimpleConfigManager(IConfigDetector<CT> configDetector) {
        this.configDetector = configDetector;
    }

    @Override
    public CT findByType(Class<? extends DT> dataType) {
        return findByTypeStep(dataType);
    }
}
