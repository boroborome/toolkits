package com.happy3w.toolkits.reflect;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FieldAccessorTest {

    @Test
    public void givenChildFieldNameReturnFieldSuccess() {
        FieldAccessor accessor = FieldAccessor.from("childName", ChildClass.class);
        Assert.assertEquals("childName", accessor.getFieldName());
        Assert.assertEquals("getChildName", accessor.getGetMethod().getName());
        Assert.assertEquals("setChildName", accessor.getSetMethod().getName());
        Assert.assertNotNull("childName", accessor.getField().getName());
    }

    @Test
    public void givenParentFieldNameReturnFieldSuccess() {
        FieldAccessor accessor = FieldAccessor.from("parentName", ChildClass.class);
        Assert.assertEquals("parentName", accessor.getFieldName());
        Assert.assertEquals("getParentName", accessor.getGetMethod().getName());
        Assert.assertEquals("setParentName", accessor.getSetMethod().getName());
        Assert.assertNotNull("parentName", accessor.getField().getName());
    }

    @Test
    public void givenChildClassReturnAllFieldsSuccess() {
        List<FieldAccessor> accessors = FieldAccessor.allFieldAccessors(ChildClass.class);
        Assert.assertEquals(2, accessors.size());
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
