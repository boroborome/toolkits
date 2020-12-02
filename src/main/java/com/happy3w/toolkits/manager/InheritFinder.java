package com.happy3w.toolkits.manager;

import java.util.Stack;

public class InheritFinder<CT> implements IConfigFinder<CT> {
    public static final InheritFinder<?> INSTANCE = new InheritFinder<>();

    @Override
    public CT find(Class<?> dataType, AbstractConfigManager<CT, ?> manager) {
        CT config = manager.findByTypeStep(dataType);
        if (config != null) {
            return config;
        }

        Stack<Class<?>> typeStack = new Stack<>();
        collectSuper(dataType, typeStack);
        while (!typeStack.isEmpty()) {
            Class<?> type = typeStack.pop();
            CT c = manager.findByTypeStep(type);
            if (c != null) {
                manager.regist(dataType, c);
                return c;
            }
            collectSuper(type, typeStack);
        }
        return null;
    }

    private void collectSuper(Class<?> type, Stack<Class<?>> typeStack) {
        Class<?> s = type.getSuperclass();
        if (s != null && s != Object.class) {
            typeStack.push(s);
        }

        Class<?>[] is = type.getInterfaces();
        if (is != null && is.length > 0) {
            for (Class<?> c : is) {
                typeStack.push(c);
            }
        }
    }
}
