package com.happy3w.toolkits.convert;

public class StringConverter implements ISimpleConverter<String, StringConverter> {
    @Override
    public Class[] dataTypes() {
        return new Class[] {String.class};
    }

    @Override
    public String convertToString(String value) {
        return value;
    }

    @Override
    public String convertFromString(String str) {
        return str;
    }
}
