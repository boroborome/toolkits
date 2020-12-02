package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.reflect.ReflectUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigManager<CT, ST extends AbstractConfigManager<CT, ST>> {
    protected Map<Class<?>, CT> configMap = new HashMap<>();

    @Getter
    @Setter
    protected IConfigDetector<CT> configDetector;

    @Getter
    @Setter
    protected IConfigFinder<CT> configFinder;

    public CT findByType(Class<?> dataType) {
        return configFinder == null
                ? findByTypeStep(dataType)
                : configFinder.find(dataType, this);
    }

    public ST regist(Class<?> dataType, CT config) {
        configMap.put(dataType, config);
        return (ST) this;
    }

    public ST finder(IConfigFinder<CT> finder) {
        this.configFinder = finder;
        return (ST) this;
    }

    public ST detector(IConfigDetector<CT> detector) {
        this.configDetector = detector;
        return (ST) this;
    }

    public CT findByTypeStep(Class<?> dataType) {
        CT config = configMap.get(dataType);
        if (config == null && configDetector != null) {
            config = configDetector.detectConfig(dataType);
            if (config != null) {
                configMap.put(dataType, config);
            }
        }
        return config;
    }

    public ST newCopy() {
        AbstractConfigManager newManager = ReflectUtil.newInstance(this.getClass());
        newManager.configMap.putAll(configMap);
        newManager.configFinder = configFinder;
        newManager.configDetector = configDetector;
        return (ST) newManager;
    }
}
