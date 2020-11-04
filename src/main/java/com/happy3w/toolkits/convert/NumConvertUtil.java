package com.happy3w.toolkits.convert;

public class NumConvertUtil {
    public static String dropPointValue(String value) {
        int pos = value.indexOf('.');
        return pos < 0 ? value : value.substring(0, pos);
    }
}
