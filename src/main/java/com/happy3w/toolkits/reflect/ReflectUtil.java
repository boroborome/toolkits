package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.java.ext.Pair;
import com.happy3w.toolkits.iterator.EasyIterator;
import com.happy3w.toolkits.iterator.IEasyIterator;
import com.happy3w.toolkits.utils.NameUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Method[] methods = field.getDeclaringClass().getMethods();
        Method getter = findMethod(NameUtil.fieldToGetter(field.getName(), false), methods);
        if (getter != null) {
            return getter;
        }

        return findMethod(NameUtil.fieldToGetter(field.getName(), true), methods);
    }

    public static Method findSetter(Field field) {
        return findMethod(NameUtil.fieldToSetter(field.getName()), field.getDeclaringClass().getMethods());
    }

    public static Object invoke(Method method, Object owner, Object... params) {
        try {
            return method.invoke(owner, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(MessageFormat.format("Failed to run method {0} for object {1}, Error is :{2}",
                    method, owner, e.getMessage()), e);
        }
    }

    public static Field accessibleField(Class dataType, String fieldName) {
        Field field = findField(dataType, fieldName);
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
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

    public static IEasyIterator<Class> enumAllTypes(Class type) {
        return EasyIterator.of(type)
                .concat(EasyIterator.fromIterator(new ParentTypeIterator(type)));
    }

    public static IEasyIterator<Class> enumAllParentTypes(Class type) {
        return EasyIterator.fromIterator(new ParentTypeIterator(type));
    }

    public static IEasyIterator<Pair<String, Object>> enumValues(Object data) {
        if (data == null) {
            return EasyIterator.emptyIterator();
        } else if (data instanceof Map) {
            return enumMapValues((Map) data);
        } else {
            return enumObjectValues(data);
        }
    }

    private static IEasyIterator<Pair<String, Object>> enumObjectValues(Object data) {
        if (data == null) {
            return EasyIterator.emptyIterator();
        }
        return enumMethods(data.getClass())
                .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                .filter(m -> m.getParameterCount() == 0)
                .map(m -> {
                    String fieldName = NameUtil.methodToField(m.getName());
                    Object value = invoke(m, data);
                    return new Pair<>(fieldName, value);
                });
    }

    public static IEasyIterator<Field> enumField(Class dataType) {
        return enumAllTypes(dataType)
                .flatMapArray(type -> type.getDeclaredFields())
                .filter(f -> f.getDeclaringClass() != Object.class);
    }

    public static IEasyIterator<Method> enumMethods(Class dataType) {
        return EasyIterator.of(dataType.getMethods())
                .filter(m -> m.getDeclaringClass() != Object.class);
    }

    private static IEasyIterator enumMapValues(Map data) {
        return EasyIterator.fromIterable(data.entrySet())
                .map(e -> {
                    Map.Entry entry = (Map.Entry) e;
                    return new Pair<>(String.valueOf(entry.getKey()), entry.getValue());
                });
    }
}
