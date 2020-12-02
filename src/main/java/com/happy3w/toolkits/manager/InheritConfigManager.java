package com.happy3w.toolkits.manager;

import lombok.NoArgsConstructor;

import java.util.Stack;

@NoArgsConstructor
public class InheritConfigManager<DT, CT> extends AbstractConfigManager<DT, CT, InheritConfigManager<DT, CT>> {

    public InheritConfigManager(IConfigDetector<CT> configDetector) {
        this.configDetector = configDetector;
    }

    @Override
    public CT findByType(Class<? extends DT> dataType) {
        CT config = findByTypeStep(dataType);
        if (config != null) {
            return config;
        }

        Stack<Class<?>> typeStack = new Stack<>();
        collectSuper(dataType, typeStack);
        while (!typeStack.isEmpty()) {
            Class<?> type = typeStack.pop();
            CT c = findByTypeStep(type);
            if (c != null) {
                regist(dataType, c);
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
