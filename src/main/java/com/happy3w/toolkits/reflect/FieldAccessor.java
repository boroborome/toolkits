package com.happy3w.toolkits.reflect;

import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class FieldAccessor {
    private String fieldName;
    private Class dataType;
    private Method getMethod;
    private Method setMethod;

    public static FieldAccessor from(String fieldName, Class owner) {
        return from(fieldName, owner.getDeclaredMethods());
    }

    public static FieldAccessor from(Field field) {
        return from(field.getName(), field.getDeclaringClass().getMethods());
    }

    public static FieldAccessor from(String fieldName, Method[] methods) {
        String capitalizeName = StringUtils.capitalize(fieldName);
        Method getter = findGetter(capitalizeName, methods);
        Method setter = findSetter(capitalizeName, methods);
        if (getter == null) {
            throw new RuntimeException("No Property define for field:" + fieldName);
        }

        return new FieldAccessor(fieldName,
                getter.getReturnType(),
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

    public Object getValue(Object owner) {
        if (owner == null) {
            return null;
        }
        return ReflectUtil.invoke(getMethod, owner);
    }

    public void setValue(Object owner, Object data) {
        ReflectUtil.invoke(setMethod, owner, data);
    }
}
