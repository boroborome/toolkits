package com.happy3w.toolkits.utils;

public class BooleanUtils {
    public static boolean valueOf(String strValue) {
        return "yes".equalsIgnoreCase(strValue)
                || "true".equalsIgnoreCase(strValue);
    }
}
