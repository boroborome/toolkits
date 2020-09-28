package com.happy3w.toolkits.pipe;

import java.util.Collection;

public class HeadPipe<InType> extends EasyPipe<InType, InType> {
    @Override
    public void accept(InType data) {
        nextPipe.accept(data);
    }

    public void accept(Collection<InType> lst) {
        for (InType d : lst) {
            nextPipe.accept(d);
        }
    }
}
