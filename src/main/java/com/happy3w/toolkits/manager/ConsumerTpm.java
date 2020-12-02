package com.happy3w.toolkits.manager;

import java.util.function.Consumer;

public class ConsumerTpm<CT> extends AbstractConfigManager<Consumer<? extends CT>, ConsumerTpm<CT>> {

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

    public static <CT> ConsumerTpm<CT> inherit() {
        return new ConsumerTpm<CT>()
                .finder((IConfigFinder<Consumer<? extends CT>>) InheritFinder.INSTANCE);
    }
}
