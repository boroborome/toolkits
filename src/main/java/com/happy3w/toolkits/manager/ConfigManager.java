package com.happy3w.toolkits.manager;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager<CT> {
    protected Map<Class<?>, CT> configMap = new HashMap<>();

    @Getter
    @Setter
    protected IConfigDetector<CT> configDetector;

    @Getter
    @Setter
    protected final IConfigFinder<CT> configFinder;

    public ConfigManager(IConfigFinder<CT> configFinder) {
        this.configFinder = configFinder;
    }

    public CT findByType(Class<?> dataType) {
        return configFinder.find(dataType, this);
    }

    public ConfigManager<CT> regist(Class<?> dataType, CT config) {
        configMap.put(dataType, config);
        return this;
    }

    public ConfigManager<CT> detector(IConfigDetector<CT> detector) {
        this.configDetector = detector;
        return this;
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

    public ConfigManager<CT> newCopy() {
        ConfigManager<CT> newManager = new ConfigManager<>(configFinder);
        newManager.configMap.putAll(configMap);
        newManager.configDetector = configDetector;
        return newManager;
    }

    public static <CT> ConfigManager<CT> simple() {
        return new ConfigManager<CT>((IConfigFinder<CT>) SimpleFinder.INSTANCE);
    }


    public static <CT> ConfigManager<CT> inherit() {
        return new ConfigManager<CT>((IConfigFinder<CT>) InheritFinder.INSTANCE);
    }
}
