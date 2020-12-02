package com.happy3w.toolkits.manager;

import java.util.function.Consumer;

public class ConsumerManager<CT> extends AbstractConfigManager<Consumer<? extends CT>, ConsumerManager<CT>> {

    public <T extends CT> void registConsumer(Class<T> dataType, Consumer<T> consumer) {
        this.regist(dataType, consumer);
    }

    @SuppressWarnings("unchecked")
    public void consumeData(CT data) {
        if (data == null) {
            return;
        }
        Consumer consumer = findByType(data.getClass());
        if (consumer != null) {
            consumer.accept(data);
        }
    }

    public static <CT> ConsumerManager<CT> inherit() {
        return new ConsumerManager<CT>()
                .finder((IConfigFinder<Consumer<? extends CT>>) InheritFinder.INSTANCE);
    }
}
