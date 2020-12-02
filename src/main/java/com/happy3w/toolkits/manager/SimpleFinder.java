package com.happy3w.toolkits.manager;

public class SimpleFinder<CT> implements IConfigFinder<CT> {
    public static final SimpleFinder<?> INSTANCE = new SimpleFinder<>();

    @Override
    public CT find(Class<?> dataType, ConfigManager<CT> manager) {
        return manager.findByTypeStep(dataType);
    }
}
