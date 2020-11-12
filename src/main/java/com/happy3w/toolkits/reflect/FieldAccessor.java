package com.happy3w.toolkits.reflect;

import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class FieldAccessor {
    private String fieldName;
    private Class dataType;
    private Method getMethod;
    private Method setMethod;

    public static FieldAccessor from(Field field) {
        String capitalizeName = StringUtils.capitalize(field.getName());
        Method[] methods = field.getDeclaringClass().getMethods();
        Method getter = findGetter(capitalizeName, methods);
        Method setter = findSetter(capitalizeName, methods);
        if (getter == null) {
            throw new RuntimeException("No Property define for field:" + field.toString());
        }

        return new FieldAccessor(field.getName(),
                field.getType(),
                getter,
                setter);
    }

    private static Method findSetter(String capitalizeName, Method[] methods) {
        return ReflectUtil.findMethod("set" + capitalizeName, methods);
    }

    private static Method findGetter(String capitalizeName, Method[] methods) {
        Method getter = ReflectUtil.findMethod("get" + capitalizeName, methods);
        if (getter != null) {
            return getter;
        }

        return ReflectUtil.findMethod("is" + capitalizeName, methods);
    }

    public Object getValue(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return getMethod.invoke(data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to read field:" + fieldName + " from:" + data, e);
        }
    }
}
