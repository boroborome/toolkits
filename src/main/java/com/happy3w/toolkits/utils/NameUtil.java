package com.happy3w.toolkits.utils;

import com.happy3w.java.ext.StringUtils;

public class NameUtil {
    public static String fieldToGetter(String fieldName) {
        String capitalizeName = StringUtils.capitalize(fieldName);
        return "get" + capitalizeName;
    }

    public static String fieldToBoolGetter(String fieldName) {
        String capitalizeName = StringUtils.capitalize(fieldName);
        return "is" + capitalizeName;
    }

    public static String fieldToGetter(String fieldName, boolean isBool) {
        return isBool ? fieldToBoolGetter(fieldName) : fieldToGetter(fieldName);
    }

    public static String fieldToSetter(String fieldName) {
        String capitalizeName = StringUtils.capitalize(fieldName);
        return "set" + capitalizeName;
    }

    public static String getterToField(String methodName) {
        String orgFieldName;
        if (methodName.startsWith("get")) {
            orgFieldName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            orgFieldName = methodName.substring(2);
        } else {
            orgFieldName = methodName;
        }

        return StringUtils.uncapitalize(orgFieldName);
    }
}
