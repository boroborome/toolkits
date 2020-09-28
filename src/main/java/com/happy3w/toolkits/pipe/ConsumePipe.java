package com.happy3w.toolkits.pipe;

import java.util.function.Consumer;

public class ConsumePipe<InType> implements IEasyPipe<InType> {
    protected final Consumer<InType> consumer;

    public ConsumePipe(Consumer<InType> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(InType data) {
        consumer.accept(data);
    }

    @Override
    public void flush() {

    }
}
