package com.happy3w.toolkits.permutation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PermutationGeneratorTest {

    @Test
    public void should_gen_success() {
        List<List<String>> results = new PermutationGenerator<>(new String[]{"1", "2", "3"})
                .generate()
                .map(vs -> Arrays.asList(vs))
                .collect(Collectors.toList());
        Assert.assertEquals("[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]",
                results.toString());
    }
}
