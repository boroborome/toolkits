package com.happy3w.toolkits.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;

public class ReflectUtil {
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed create instanceof type:" + type, e);
        }
    }

    public static Method findMethod(String methodName, Method[] methods) {
        for (Method method : methods) {
            if (Objects.equals(method.getName(), methodName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Can't find method:" + methodName);
    }

    public static Object invoke(Method method, Object owner, Object...params) {
        try {
            return method.invoke(owner, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(MessageFormat.format("Failed to run method {0} for object {1}, Error is :{2}",
                    method, owner, e.getMessage(), e));
        }
    }
}
