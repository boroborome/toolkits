package com.happy3w.toolkits.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NameUtilTest {

    @Test
    void getterToField() {
        Assertions.assertEquals("abcDef", NameUtil.getterToField("getAbcDef"));
        Assertions.assertEquals("abcDef", NameUtil.getterToField("isAbcDef"));
        Assertions.assertEquals("abcDef", NameUtil.getterToField("abcDef"));
    }

    @Test
    void fieldToGetter() {
        Assertions.assertEquals("getAbcDef", NameUtil.fieldToGetter("abcDef", false));
        Assertions.assertEquals("isAbcDef", NameUtil.fieldToGetter("abcDef", true));
    }

    @Test
    void fieldToSetter() {
        Assertions.assertEquals("setAbcDef", NameUtil.fieldToSetter("abcDef"));
    }
}