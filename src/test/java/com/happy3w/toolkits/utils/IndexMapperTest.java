package com.happy3w.toolkits.utils;

import org.junit.Assert;
import org.junit.Test;

public class IndexMapperTest {

    @Test
    public void should_init_success() {
        IndexMapper<String> mapper = new IndexMapper<>(new String[]{"1", "2", "3", "1"}, String::equals);
        Assert.assertArrayEquals(new int[]{2, 1, 1}, mapper.getMetaCounts());
        Assert.assertArrayEquals(new int[]{0, 0, 1, 2}, mapper.createStartValues());
        Assert.assertArrayEquals(new String[]{"2", "1", "3", "1"}, mapper.convertValues(new int[]{1, 0, 2, 0}));
    }
}
