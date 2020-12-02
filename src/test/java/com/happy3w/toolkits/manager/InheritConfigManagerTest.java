package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.manager.ann.TestAnn;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class InheritConfigManagerTest {

    @Test
    public void should_find_null_without_detector() {
        InheritConfigManager<ITestData, String> manager = new InheritConfigManager<>();
        String name = manager.findByType(TestData.class);
        Assert.assertEquals(null, name);
    }

    @Test
    public void should_find_when_regist() {
        InheritConfigManager<ITestData, String> manager = new InheritConfigManager<>();
        manager.regist(TestData.class, "Hi2");

        String name = manager.findByType(SubTestData.class);
        Assert.assertEquals("Hi2", name);
    }

    @Test
    public void should_find_with_detector() {
        InheritConfigManager<ITestData, String> manager = new InheritConfigManager<>();
        manager.setConfigDetector(new AnnotationDetector<>(TestAnn.class, a -> a.name()));

        String name = manager.findByType(SubTestData.class);
        Assert.assertEquals("Hi", name);
    }

    private interface ITestData {

    }
    @TestAnn(name = "Hi")
    private static class TestData implements ITestData {
        
    }

    private static final class SubTestData extends TestData {

    }
}
