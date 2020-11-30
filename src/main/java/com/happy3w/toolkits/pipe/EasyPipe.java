package com.happy3w.toolkits.pipe;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class EasyPipe<InType, OutType> implements IEasyPipe<InType> {
    protected IEasyPipe<OutType> nextPipe;

    public void flush() {
        nextPipe.flush();
    }

    public static <T> EasyPipeBuilder<T, T, T> of(Class<T> dataType) {
        return createBuilder(new HeadPipe<>());
    }

    public static <T> IEasyPipe<T> end() {
        return new EndPipe<T>();
    }

    public static <T, R> EasyPipeBuilder<T, T, R> map(Function<T, R> mapMethod) {
        return createBuilder(new MapPipe<T, R>(mapMethod));
    }

    public static <T> EasyPipeBuilder<T, T, List<T>> split(int size) {
        return createBuilder(new SplitPipe<>(size));
    }

    public static <T> EasyPipeBuilder<T, T, T> visit(Consumer<T> consumeMethod) {
        return createBuilder(new PeekPipe<T>(consumeMethod));
    }

    public static <T, R> EasyPipeBuilder<T, T, R> flatMap(Function<T, Iterator<R>> mapMethod) {
        return createBuilder(new FlatMapPipe<T, R>(mapMethod));
    }

    public static <T> EasyPipeBuilder<T, T, T> filter(Predicate<T> predicate) {
        return createBuilder(new FilterPipe<T>(predicate));
    }

    public static <T> EasyPipeBuilder<T, T, T> filter(Predicate<T> predicate, IEasyPipe<T> elsePipe) {
        return createBuilder(new FilterPipe<T>(predicate, elsePipe));
    }

    private static <T, R> EasyPipeBuilder<T, T, R> createBuilder(EasyPipe<T, R> pipe) {
        return new EasyPipeBuilder<>(pipe, pipe);
    }

    public static <T, R> ClassifyPipeBuilder<T, T, R> classify(Function<T, R> mapMethod) {
        ClassifyPipe<T, R> pipe = new ClassifyPipe<>(mapMethod);
        return new ClassifyPipeBuilder<T, T, R>(pipe, pipe);
    }

    public static <T> ConsumePipe<T> forEach(Consumer<T> consumer) {
        return new ConsumePipe<T>(consumer);
    }

}
