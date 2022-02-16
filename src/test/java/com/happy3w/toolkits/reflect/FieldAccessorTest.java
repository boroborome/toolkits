package com.happy3w.toolkits.reflect;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FieldAccessorTest {

    @Test
    public void givenChildFieldNameReturnFieldSuccess() {
        FieldAccessor accessor = FieldAccessor.from("childName", ChildClass.class);
        Assertions.assertEquals("childName", accessor.getFieldName());
        Assertions.assertEquals("getChildName", accessor.getGetMethod().getName());
        Assertions.assertEquals("setChildName", accessor.getSetMethod().getName());
        Assertions.assertNotNull("childName", accessor.getField().getName());
    }

    @Test
    public void givenParentFieldNameReturnFieldSuccess() {
        FieldAccessor accessor = FieldAccessor.from("parentName", ChildClass.class);
        Assertions.assertEquals("parentName", accessor.getFieldName());
        Assertions.assertEquals("getParentName", accessor.getGetMethod().getName());
        Assertions.assertEquals("setParentName", accessor.getSetMethod().getName());
        Assertions.assertNotNull("parentName", accessor.getField().getName());
    }

    @Test
    public void givenChildClassReturnAllFieldsSuccess() {
        List<FieldAccessor> accessors = FieldAccessor.allFieldAccessors(ChildClass.class);
        Assertions.assertEquals(2, accessors.size());
    }

    @Getter
    @Setter
    public static class ParentClass {
        private String parentName;
    }

    @Getter
    @Setter
    public static class ChildClass extends ParentClass {
        private String childName;
    }
}
