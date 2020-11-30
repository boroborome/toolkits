package com.happy3w.toolkits.manager;

import java.util.function.Consumer;

public class ConsumerTpm<DT> extends AbstractTypeProcessorManager<DT, Consumer<? extends DT>, ConsumerTpm<DT>> {
    public <T extends DT> void registConsumer(Class<T> dataType, Consumer<T> consumer) {
        this.processMap.put(dataType, consumer);
    }

    public <T extends DT> Consumer<T> findConsumerByType(Class<T> dataType) {
        return (Consumer<T>) this.findProcessByType(dataType);
    }

    public <T extends DT> Consumer<T> findConsumerByInheritType(Class<T> dataType) {
        return (Consumer<T>) this.findProcessByInheritType(dataType);
    }

    @SuppressWarnings("unchecked")
    public void consumeData(DT data) {
        if (data == null) {
            return;
        }
        Consumer consumer = findProcessByInheritType((Class<? extends DT>) data.getClass());
        if (consumer != null) {
            consumer.accept(data);
        }
    }
}
