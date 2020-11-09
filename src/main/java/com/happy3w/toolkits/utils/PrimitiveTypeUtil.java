package com.happy3w.toolkits.utils;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeUtil {
    private static final Map<Class, Class> primitiveToObjMap = new HashMap<>();
    private static final Map<Class, Class> objToPrimitiveMap = new HashMap<>();
    static {
        regist(new TypePair(int.class, Integer.class));
        regist(new TypePair(long.class, Long.class));
        regist(new TypePair(boolean.class, Boolean.class));
        regist(new TypePair(float.class, Float.class));
        regist(new TypePair(char.class, Character.class));
        regist(new TypePair(short.class, Short.class));
        regist(new TypePair(byte.class, Byte.class));
    }

    private static void regist(TypePair pair) {
        primitiveToObjMap.put(pair.primitiveType, pair.objType);
        objToPrimitiveMap.put(pair.objType, pair.primitiveType);
    }

    public static Class toObjType(Class type) {
        Class objType = primitiveToObjMap.get(type);
        return objType == null ? type : objType;
    }

    public static Class toPrimitiveType(Class type) {
        Class objType = objToPrimitiveMap.get(type);
        return objType == null ? type : objType;
    }

    private static class TypePair {
        private Class primitiveType;
        private Class objType;

        public TypePair(Class primitiveType, Class objType) {
            this.primitiveType = primitiveType;
            this.objType = objType;
        }
    }
}
