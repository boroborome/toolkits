package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.java.ext.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
        return null;
    }

    public static Method findGetter(Field field) {
        String capitalizeName = StringUtils.capitalize(field.getName());
        String getterName = "get" + capitalizeName;
        Method[] methods = field.getDeclaringClass().getMethods();
        Method getter = findMethod(getterName, methods);
        if (getter != null) {
            return getter;
        }

        return findMethod("is" + capitalizeName, methods);
    }

    public static Method findSetter(Field field) {
        String capitalizeName = StringUtils.capitalize(field.getName());
        return findMethod("set" + capitalizeName, field.getDeclaringClass().getMethods());
    }

    public static Object invoke(Method method, Object owner, Object... params) {
        try {
            return method.invoke(owner, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(MessageFormat.format("Failed to run method {0} for object {1}, Error is :{2}",
                    method, owner, e.getMessage()), e);
        }
    }

    public static Field findField(Class dataType, String fieldName) {
        return findOneInExtend(dataType, curType -> {
            for (Field field : curType.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            return null;
        });
    }

    public static <T> T findOneInExtend(Class dataType, Function<Class, T> finder) {
        Class curType = dataType;
        while (curType != null && curType != Object.class) {
            T data = finder.apply(curType);
            if (data != null) {
                return data;
            }
            curType = curType.getSuperclass();
        }
        return null;
    }

    public static <T> List<T> findAllInExtend(Class dataType, Function<Class, List<T>> finder) {
        List<T> results = new ArrayList<>();
        Class curType = dataType;
        while (curType != null && curType != Object.class) {
            List<T> datas = finder.apply(curType);
            if (!ListUtils.isEmpty(datas)) {
                results.addAll(datas);
            }
            curType = curType.getSuperclass();
        }
        return results;
    }

    public static Method findGetterByFieldName(Class dataType, String fieldName) {
        try {
            Field field = findField(dataType, fieldName);
            return findGetter(field);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field:" + fieldName, e);
        }
    }

    public static <T> T findAnnOnType(Class dataType, Class<T> annType) {
        return findOneInExtend(dataType, curType -> (T) curType.getDeclaredAnnotation(annType));
    }

    public static Field findOneFieldWithAnn(Class dataType, Class annType) {
        return findOneInExtend(dataType, type -> {
            Field[] fields = type.getDeclaredFields();
            if (fields == null || fields.length == 0) {
                return null;
            }
            for (Field field : fields) {
                Annotation[] anns = field.getDeclaredAnnotationsByType(annType);
                if (anns != null && anns.length > 0) {
                    return field;
                }
            }
            return null;
        });
    }
}
