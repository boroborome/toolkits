package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.manager.ann.TestAnn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigManagerTest {

    @Test
    public void should_find_null_without_detector() {
        ConfigManager<String> manager = ConfigManager.inherit();
        String name = manager.findByType(TestData.class);
        Assertions.assertEquals(null, name);
    }

    @Test
    public void should_find_when_regist() {
        ConfigManager<String> manager = ConfigManager.<String>inherit()
                .regist(TestData.class, "Hi2");

        String name = manager.findByType(SubTestData.class);
        Assertions.assertEquals("Hi2", name);
    }

    @Test
    public void should_find_with_detector() {
        ConfigManager<String> manager = ConfigManager.<String>inherit()
                .detector(new AnnotationDetector<>(TestAnn.class, a -> a.name()));

        String name = manager.findByType(SubTestData.class);
        Assertions.assertEquals("Hi", name);
    }

    private interface ITestData {

    }
    @TestAnn(name = "Hi")
    private static class TestData implements ITestData {

    }

    private static final class SubTestData extends TestData {

    }
}
