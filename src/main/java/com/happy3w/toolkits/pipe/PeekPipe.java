package com.happy3w.toolkits.pipe;

import java.util.function.Consumer;

public class PeekPipe<InType> extends EasyPipe<InType, InType> {
    private final Consumer<InType> consumeMethod;

    public PeekPipe(Consumer<InType> consumeMethod) {
        this.consumeMethod = consumeMethod;
    }

    @Override
    public void accept(InType data) {
        consumeMethod.accept(data);
        nextPipe.accept(data);
    }
}
