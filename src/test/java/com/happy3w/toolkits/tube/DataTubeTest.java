package com.happy3w.toolkits.tube;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataTubeTest extends TestCase {
    @Test
    public void should_act_success() {
        TubReactResult trr = new DataTube()
                .reactOne(String.class, m -> m.chars().mapToObj(ch -> Character.valueOf((char) ch)).collect(Collectors.toList()))
                .reactList(Character.class, cs -> cs.stream().map(ch -> (long) ch.charValue()).collect(Collectors.toList()))
                .reactOne(Long.class, l -> l + 1)
                .withValue(String.class, Arrays.asList("1234567"))
                .withProcessor(new SimpleTubProcessor())
                .reactAll();

        Assert.assertEquals(Arrays.asList(3l), trr.getResult(Long.class));
    }
}
