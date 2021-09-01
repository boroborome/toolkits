package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class FieldAccessor {
    private String fieldName;
    private Field field;
    private Class propertyType;
    private Method getMethod;
    private Method setMethod;

    public static FieldAccessor from(String fieldName, Class owner) {
        return from(fieldName, owner.getMethods());
    }

    public static FieldAccessor from(Field field) {
        return from(field.getName(), field.getDeclaringClass().getMethods());
    }

    public static FieldAccessor from(String fieldName, Method[] methods) {
        return from(fieldName, methods, null);
    }

    private static FieldAccessor from(String fieldName, Method[] methods, Class owner) {
        String capitalizeName = StringUtils.capitalize(fieldName);
        Method getter = findGetter(capitalizeName, methods);
        Method setter = findSetter(capitalizeName, methods);
        if (getter == null) {
            return null;
        }

        Field field = findField(fieldName, owner == null ? getter.getDeclaringClass() : owner);

        return new FieldAccessor(fieldName,
                field,
                getter.getReturnType(),
                getter,
                setter);
    }

    private static Field findField(String fieldName, Class ownerType) {
        Class currentType = ownerType;
        while (currentType != null) {
            Field field = findField(fieldName, currentType.getDeclaredFields());
            if (field != null) {
                return field;
            }
            currentType = currentType.getSuperclass();
        }
        return null;
    }

    private static Field findField(String fieldName, Field[] fields) {
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            if (Objects.equals(fieldName, field.getName())) {
                return field;
            }
        }
        return null;
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


    public static List<FieldAccessor> allFieldAccessors(Class dataType) {
        List<FieldAccessor> accessors = new ArrayList<>();
        Class currentType = dataType;
        while (currentType != null) {
            collectFieldAccessor(currentType, accessors);
            currentType = currentType.getSuperclass();
        }
        return accessors;
    }

    private static void collectFieldAccessor(Class currentType, List<FieldAccessor> accessors) {
        for (Field field : currentType.getDeclaredFields()) {
            FieldAccessor accessor = FieldAccessor.from(field);
            if (accessor != null) {
                accessors.add(accessor);
            }
        }
    }
}
