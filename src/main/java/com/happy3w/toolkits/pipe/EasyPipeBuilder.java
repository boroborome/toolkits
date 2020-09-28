package com.happy3w.toolkits.pipe;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class EasyPipeBuilder<HeaderType, InType, OutType> {
    private final IEasyPipe<HeaderType> head;
    private final EasyPipe<InType, OutType> current;

    public EasyPipeBuilder(IEasyPipe<HeaderType> head, EasyPipe<InType, OutType> current) {
        this.head = head;
        this.current = current;
    }

    public EasyPipeBuilder<HeaderType, OutType, List<OutType>> split(int size) {
        SplitPipe<OutType> pipe = new SplitPipe<>(size);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public <R> EasyPipeBuilder<HeaderType, OutType, R> map(Function<OutType, R> mapMethod) {
        MapPipe<OutType, R> pipe = new MapPipe<OutType, R>(mapMethod);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public EasyPipeBuilder<HeaderType, OutType, OutType> peek(Consumer<OutType> consumeMethod) {
        PeekPipe<OutType> pipe = new PeekPipe<>(consumeMethod);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public <R> EasyPipeBuilder<HeaderType, OutType, R> flatMap(Function<OutType, Iterator<R>> mapMethod) {
        FlatMapPipe<OutType, R> pipe = new FlatMapPipe<>(mapMethod);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public EasyPipeBuilder<HeaderType, OutType, OutType> filter(Predicate<OutType> predicate) {
        FilterPipe<OutType> pipe = new FilterPipe<>(predicate);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public EasyPipeBuilder<HeaderType, OutType, OutType> filter(Predicate<OutType> predicate, IEasyPipe<OutType> elsePipe) {
        FilterPipe<OutType> pipe = new FilterPipe<>(predicate, elsePipe);
        this.current.nextPipe = pipe;
        return new EasyPipeBuilder<>(head, pipe);
    }

    public <TagType> ClassifyPipeBuilder<HeaderType, OutType, TagType> classify(Function<OutType, TagType> mapMethod) {
        ClassifyPipe<OutType, TagType> pipe = new ClassifyPipe<>(mapMethod);
        this.current.nextPipe = pipe;
        return new ClassifyPipeBuilder<>(head, pipe);
    }

    public IEasyPipe<HeaderType> forEach(Consumer<OutType> consumer) {
        ConsumePipe<OutType> pipe = new ConsumePipe<>(consumer);
        this.current.nextPipe = pipe;
        return head;
    }

    public IEasyPipe<HeaderType> next(IEasyPipe<OutType> nextPipe) {
        this.current.nextPipe = nextPipe;
        return head;
    }

    public static class ConsumePipeBuilder<HeaderType, InType> {
        private final IEasyPipe<HeaderType> head;
        private final IEasyPipe<InType> current;

        public ConsumePipeBuilder(IEasyPipe<HeaderType> head, IEasyPipe<InType> current) {
            this.head = head;
            this.current = current;
        }

        public IEasyPipe<?> build() {
            return head;
        }
    }
}
