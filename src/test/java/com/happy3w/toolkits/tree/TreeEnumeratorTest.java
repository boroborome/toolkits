package com.happy3w.toolkits.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TreeEnumeratorTest {
    @Test
    public void should_enum_all_tree() {
        List<BinTreeNode> treeTypes = TreeEnumerator.enumFullBinTree(7)
                .map(BinTreeNode::from)
//                .peek(tree -> System.out.println(BinTreeNode.serialize(tree, String::valueOf)))
                .collect(Collectors.toList());
        Assert.assertEquals(5, treeTypes.size());
    }

}
