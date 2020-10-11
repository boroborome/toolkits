package com.happy3w.toolkits.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BinTreeNodeTest {

    @Test
    public void should_serialize_shape_1_success() {
        List<Boolean> shapeDesc = Arrays.asList(true, true, false, false, false, false);
        BinTreeNode head = BinTreeNode.from(shapeDesc);
        List<Boolean> newShapeDesc = head.toShapeDesc();
        Assert.assertEquals(shapeDesc, newShapeDesc);
    }

    @Test
    public void should_serialize_shape_2_success() {
        List<Boolean> shapeDesc = Arrays.asList(true, false, true, false, false, false);
        BinTreeNode head = BinTreeNode.from(shapeDesc);
        List<Boolean> newShapeDesc = head.toShapeDesc();
        Assert.assertEquals(shapeDesc, newShapeDesc);
    }
}
