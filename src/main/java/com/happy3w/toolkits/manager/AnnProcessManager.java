package com.happy3w.toolkits.manager;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.function.Function;

@Getter
public class AnnProcessManager<DT, AT extends Annotation, PT> extends TypeProcessorManager<DT, PT, AnnProcessManager<DT, AT, PT>> {
    protected final Class<AT> annType;
    protected final Function<AT, PT> processGenerator;

    public AnnProcessManager(Class<AT> annType, Function<AT, PT> processGenerator) {
        this.annType = annType;
        this.processGenerator = processGenerator;
    }

    @Override
    public PT findProcessByType(Class<? extends DT> dataType) {
        return processMap.computeIfAbsent(dataType, $ ->generateProcessor(dataType));
    }

    @Override
    public PT findProcessByInheritType(Class<? extends DT> dataType) {
        PT processor = super.findProcessByInheritType(dataType);
        if (processor == null) {
            processor = generateProcessor(dataType);
            registProcess(dataType, processor);
        }
        return processor;
    }

    protected PT generateProcessor(Class<? extends DT> dataType) {
        AT ann = dataType.getDeclaredAnnotation(annType);
        if (ann == null) {
            throw new UnsupportedOperationException("Unsupported data type:" + dataType + ", lost required annotation:" + annType);
        }
        return processGenerator.apply(ann);
    }

    @Override
    public AnnProcessManager<DT, AT, PT> newCopy() {
        AnnProcessManager<DT, AT, PT> copy = new AnnProcessManager<>(annType, processGenerator);
        copy.processMap.putAll(processMap);
        return copy;
    }
}
