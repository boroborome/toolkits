package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.StringUtils;
import com.happy3w.toolkits.utils.NameUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class FieldAccessor {
    private String fieldName;
    private Field field;
    private Class propertyType;
    private Method getMethod;
    private Method setMethod;

    private FieldAccessor(String fieldName, Class propertyType) {
        this.fieldName = fieldName;
        this.propertyType = propertyType;
    }

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
        Map<String, FieldAccessor> fieldMap = new HashMap<>();
        collectAllMethod(dataType, fieldMap);
        collectAllField(dataType, fieldMap);
        return new ArrayList<>(fieldMap.values());
    }

    private static void collectAllField(Class dataType, Map<String, FieldAccessor> fieldMap) {
        ReflectUtil.enumField(dataType)
                .forEach(field -> {
                    fieldMap.computeIfAbsent(field.getName(), k -> new FieldAccessor(field.getName(), field.getType()))
                            .field = field;
                });
    }

    private static void collectAllMethod(Class dataType, Map<String, FieldAccessor> fieldMap) {
        ReflectUtil.enumMethods(dataType)
                .forEach(method -> {
                    String methodName = method.getName();
                    if (methodName.startsWith("get") || methodName.startsWith("is")) {
                        String fieldName = NameUtil.methodToField(methodName);
                        fieldMap.computeIfAbsent(fieldName, k -> new FieldAccessor(fieldName, method.getReturnType()))
                                .getMethod = method;
                    } else if (methodName.startsWith("set") && method.getParameterCount() == 1) {
                        String fieldName = NameUtil.methodToField(methodName);
                        fieldMap.computeIfAbsent(fieldName, k -> new FieldAccessor(fieldName, method.getParameterTypes()[0]))
                                .setMethod = method;
                    }
                });
    }
}
