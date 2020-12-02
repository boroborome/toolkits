package com.happy3w.toolkits.manager;

public interface IConfigFinder<CT> {
    CT find(Class<?> dataType, ConfigManager<CT> manager);
}
