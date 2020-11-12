package com.happy3w.toolkits.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public class TypeDescription implements Type {
    private Class<?> parentType;
    private Type[] subTypes;

    public Type toRealType() {
        Type parentRealType = getRealType(parentType);
        Type[] subRealType = null;

        if (subTypes != null && subTypes.length > 0) {
            subRealType = new Type[subTypes.length];
            for (int i = 0; i < subTypes.length; i++) {
                subRealType[i] = getRealType(subTypes[i]);
            }
        }
        return new ParameterizedTypeImpl(parentType, subRealType, parentRealType);
    }

    public static Type getRealType(Type type) {
        if (type instanceof TypeDescription) {
            return ((TypeDescription) type).toRealType();
        }
        return type;
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Type[] actualTypeArguments;
        private final Class<?> rawType;
        private final Type ownerType;

        private ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            this.actualTypeArguments = actualTypeArguments;
            this.rawType = rawType;
            this.ownerType = (Type) (ownerType != null ? ownerType : rawType.getDeclaringClass());
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[]) this.actualTypeArguments.clone();
        }

        @Override
        public Class<?> getRawType() {
            return this.rawType;
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
    }
}
