package com.happy3w.toolkits.pipe;

import java.util.function.Function;

public class MapPipe<InType, OutType> extends EasyPipe<InType, OutType> {
    private final Function<InType, OutType> mapMethod;

    public MapPipe(Function<InType, OutType> mapMethod) {
        this.mapMethod = mapMethod;
    }

    @Override
    public void accept(InType data) {
        nextPipe.accept(mapMethod.apply(data));
    }
}
