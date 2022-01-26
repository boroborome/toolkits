package com.happy3w.toolkits.tube;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataTubeTest extends TestCase {
    @Test
    public void should_act_success() {
        TubReactResult trr = new DataTube()
                .reactList(String.class, "mCode", mCodes -> calcParentCodeAndValue(mCodes)) // m2342 -> mCode:"m234" & sValue:"2"
                .reactOne(String.class, "sValue", Integer::parseInt, "iValue")
                .consumeOne(Integer.class, "iValue", v -> System.out.println(v))
                .consumeOne(Integer.class, "iValue", 0, (org, v) -> org + v, "sumValue")
                .withValue(String.class, "mCode", Arrays.asList("m"))
                .withProcessor(new SimpleTubProcessor())
                .reactAll();

        Assert.assertEquals(Arrays.asList(3l), trr.getResult(Long.class));
    }
}
