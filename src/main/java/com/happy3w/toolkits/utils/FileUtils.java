package com.happy3w.toolkits.utils;

import java.io.File;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileUtils {
    public static Stream<File> enumerateFiles(File file) {
        if (file.isFile()) {
            return Stream.of(file);
        }
        return StreamSupport.stream(new EnumerateFileSpliterator(file), false);
    }

    private static class EnumerateFileSpliterator extends Spliterators.AbstractSpliterator<File> {
        private Stack<File> fileStack = new Stack<>();
        public EnumerateFileSpliterator(File file) {
            super(0, 0);
            fileStack.push(file);
        }

        @Override
        public boolean tryAdvance(Consumer<? super File> action) {
            while (!fileStack.isEmpty()) {
                File file = fileStack.pop();
                if (file.isFile()) {
                    action.accept(file);
                    return true;
                }
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        fileStack.push(f);
                    }
                }
            }
            return false;
        }
    }
}
