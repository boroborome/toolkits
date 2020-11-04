package com.happy3w.toolkits.convert;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SimpleConverter {
    private static final SimpleConverter INSTANCE = new SimpleConverter();

    public static SimpleConverter getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE.register(new BooleanConverter());
        INSTANCE.register(new DateConverter());
        INSTANCE.register(new DoubleConverter());
        INSTANCE.register(new IntegerConverter());
        INSTANCE.register(new LongConverter());
        INSTANCE.register(new StringConverter());
    }

    private Map<Class, ISimpleConverter> converterMap = new HashMap<>();

    public SimpleConverter() {
    }

    public SimpleConverter newCopy() {
        SimpleConverter converter = new SimpleConverter();
        converter.converterMap.putAll(converterMap);
        return converter;
    }

    public <T> T convert(Object source, Class<T> targetType) {
        ISimpleConverter targetConverter = findConverter(targetType);
        if (targetConverter == null) {
            throw new UnsupportedOperationException(
                    MessageFormat.format("Can''t convert {0} to {1} for no convert define for {1}.",
                            source, targetType));
        }

        String strSource = null;
        if (source != null) {
            ISimpleConverter sourceConverter = findConverter(source.getClass());
            if (sourceConverter == null) {
                throw new UnsupportedOperationException(
                        MessageFormat.format("Can''t convert {0} to {1} for no convert define for {2}.",
                                source, targetType, source.getClass()));
            }
            strSource = sourceConverter.convertToString(source);
        }
        return (T) targetConverter.convertFromString(strSource);
    }

    public void register(ISimpleConverter converter) {
        Class[] dataTypes = converter.dataTypes();
        for (Class dataType : dataTypes) {
            converterMap.put(dataType, converter);
        }
    }

    public ISimpleConverter findConverter(Class dataType) {
        ISimpleConverter converter = converterMap.get(dataType);
        if (converter == null && dataType.isEnum()) {
            converter = new EnumConverter(dataType);
            register(converter);
        }
        return converter;
    }

    public boolean isPrimitive(Class dataType) {
        return findConverter(dataType) != null;
    }
}
