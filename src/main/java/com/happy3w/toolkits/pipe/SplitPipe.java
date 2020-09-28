package com.happy3w.toolkits.pipe;

import java.util.ArrayList;
import java.util.List;

public class SplitPipe<InType> extends EasyPipe<InType, List<InType>> {
    protected final int size;
    protected List<InType> buf = new ArrayList<>();

    public SplitPipe(int size) {
        this.size = size;
    }

    @Override
    public void accept(InType data) {
        buf.add(data);
        if (buf.size() >= size) {
            nextPipe.accept(buf);
            buf = new ArrayList<>();
        }
    }

    @Override
    public void flush() {
        if (!buf.isEmpty()) {
            nextPipe.accept(buf);
        }
        
        super.flush();
    }
}
