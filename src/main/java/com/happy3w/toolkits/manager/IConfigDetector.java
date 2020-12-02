package com.happy3w.toolkits.manager;

public interface IConfigDetector<CT> {
    CT detectConfig(Class<?> dataType);
}
