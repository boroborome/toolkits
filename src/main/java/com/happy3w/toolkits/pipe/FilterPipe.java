package com.happy3w.toolkits.pipe;

import java.util.function.Predicate;

public class FilterPipe<InType> extends EasyPipe<InType, InType> {
    protected final Predicate<InType> predicate;
    protected IEasyPipe<InType> elsePipe;

    public FilterPipe(Predicate<InType> predicate) {
        this.predicate = predicate;
    }

    public FilterPipe(Predicate<InType> predicate, IEasyPipe<InType> elsePipe) {
        this.predicate = predicate;
        this.elsePipe = elsePipe;
    }

    @Override
    public void accept(InType data) {
        if (predicate.test(data)) {
            nextPipe.accept(data);
        } else if (elsePipe != null) {
            elsePipe.accept(data);
        }
    }

    @Override
    public void flush() {
        if (elsePipe != null) {
            elsePipe.flush();
        }
        super.flush();
    }
}
