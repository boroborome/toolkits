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

    @Test
    public void should_serialize_value_success() {
        String encodeStr = "1,true,true,2,false,false,3,true,true,4,false,false,5,false,false";
        BinTreeNode head = BinTreeNode.deserialize(encodeStr, Integer::parseInt);
        String newEncodeStr = BinTreeNode.serialize(head, String::valueOf);
        Assert.assertEquals(encodeStr, newEncodeStr);
    }


    @Test
    public void should_serialize_null_success() {
        String encodeStr = null;
        BinTreeNode head = BinTreeNode.deserialize(encodeStr, Integer::parseInt);
        String newEncodeStr = BinTreeNode.serialize(head, String::valueOf);
        Assert.assertEquals(encodeStr, newEncodeStr);
    }
}
