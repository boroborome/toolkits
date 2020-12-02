package com.happy3w.toolkits.manager;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public class AnnotationDetector<AT extends Annotation, CT> implements IConfigDetector<CT> {
    private final Class<AT> annType;
    private final Function<AT, CT> configGenerator;

    public AnnotationDetector(Class<AT> annType, Function<AT, CT> configGenerator) {
        this.annType = annType;
        this.configGenerator = configGenerator;
    }

    @Override
    public CT detectConfig(Class<?> dataType) {
        AT ann = dataType.getDeclaredAnnotation(annType);
        if (ann == null) {
            return null;
        }
        return configGenerator.apply(ann);
    }
}
