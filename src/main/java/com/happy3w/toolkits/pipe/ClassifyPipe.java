package com.happy3w.toolkits.pipe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClassifyPipe<InType, TagType> implements IEasyPipe<InType> {

    protected final Function<InType, TagType> tagGenerator;
    protected Map<TagType, IEasyPipe<InType>> pipeMap = new HashMap<>();
    protected IEasyPipe<InType> defaultPipe;

    public ClassifyPipe(Function<InType, TagType> tagGenerator) {
        this.tagGenerator = tagGenerator;
    }

    public void pipe(TagType tag, IEasyPipe<InType> pipe) {
        pipeMap.put(tag, pipe);
    }

    public void setDefaultPipe(IEasyPipe<InType> defaultPipe) {
        this.defaultPipe = defaultPipe;
    }

    @Override
    public void accept(InType data) {
        TagType tag = tagGenerator.apply(data);
        IEasyPipe<InType> pipe = pipeMap.get(tag);
        if (pipe != null) {
            pipe.accept(data);
        } else if (defaultPipe != null) {
            defaultPipe.accept(data);
        }
    }

    @Override
    public void flush() {
        for (IEasyPipe<InType> pipe : pipeMap.values()) {
            pipe.flush();
        }
    }
}
