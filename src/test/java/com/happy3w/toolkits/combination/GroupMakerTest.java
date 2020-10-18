package com.happy3w.toolkits.combination;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GroupMakerTest {

    @Test
    public void should_make_success_when_normal() {
        String result = GroupMaker.make(5, 3, true)
                .map(it -> Arrays.toString(it))
                .collect(Collectors.joining("\n"));
        Assert.assertEquals("[1, 1, 3]\n" +
                "[1, 2, 2]\n" +
                "[1, 3, 1]\n" +
                "[2, 1, 2]\n" +
                "[2, 2, 1]\n" +
                "[3, 1, 1]", result);
    }

    @Test
    public void should_make_success_when_single() {
        String result = GroupMaker.make(5, 1, true)
                .map(it -> Arrays.toString(it))
                .collect(Collectors.joining("\n"));
        Assert.assertEquals("[5]", result);
    }

    @Test
    public void should_make_success_when_full() {
        String result = GroupMaker.make(5, 5, true)
                .map(it -> Arrays.toString(it))
                .collect(Collectors.joining("\n"));
        Assert.assertEquals("[1, 1, 1, 1, 1]", result);
    }
}
