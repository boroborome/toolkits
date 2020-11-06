package com.happy3w.toolkits.utils;

public class StringUtils {
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }
}
