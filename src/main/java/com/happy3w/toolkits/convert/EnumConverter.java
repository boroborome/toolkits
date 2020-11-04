package com.happy3w.toolkits.convert;

import java.util.HashMap;
import java.util.Map;

public class EnumConverter<T extends Enum> implements ISimpleConverter<T, EnumConverter<T>> {
    private Class<T> dataType;
    private Map<String, T> nameToValueMap = new HashMap<>();
    private Map<T, String> valueToNameMap = new HashMap<>();

    public EnumConverter(Class<T> dataType) {
        this.dataType = dataType;
        for (T v : dataType.getEnumConstants()) {
            nameToValueMap.put(v.name(), v);
            valueToNameMap.put(v, v.name());
        }
    }

    @Override
    public Class[] dataTypes() {
        return new Class[] {dataType};
    }

    @Override
    public String convertToString(T value) {
        return valueToNameMap.get(value);
    }

    @Override
    public T convertFromString(String name) {
        return nameToValueMap.get(name);
    }
}
