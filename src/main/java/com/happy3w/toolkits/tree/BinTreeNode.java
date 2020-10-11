package com.happy3w.toolkits.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
public class BinTreeNode<T>{
    private T data;
    private BinTreeNode<T> left;
    private BinTreeNode<T> right;

    public BinTreeNode() {
    }

    public BinTreeNode(T data) {
        this.data = data;
    }

    public BinTreeNode(T data, BinTreeNode<T> left, BinTreeNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public List<Boolean> toShapeDesc() {
        List<Boolean> shapeDesc = new ArrayList<>();
        saveShapeDesc(shapeDesc::add);
        return shapeDesc;
    }

    public void saveShapeDesc(Consumer<Boolean> shapeDescConsumer) {
        Stack<BinTreeNode> nodeStack = new Stack<>();
        nodeStack.push(this);

        while (!nodeStack.isEmpty()) {
            BinTreeNode node = nodeStack.pop();
            boolean hasLeft = node.left != null;
            boolean hasRight = node.right != null;

            shapeDescConsumer.accept(hasLeft);
            shapeDescConsumer.accept(hasRight);

            if (hasRight) {
                nodeStack.push(node.right);
            }
            if (hasLeft) {
                nodeStack.push(node.left);
            }
        }
    }

    public static BinTreeNode from(List<Boolean> shapeDesc) {
        BinTreeNode head = new BinTreeNode();
        head.loadShapeDesc(shapeDesc.iterator()::next);
        return head;
    }

    private void loadShapeDesc(Supplier<Boolean> shapeDescSupplier) {
        Stack<BinTreeNode> nodeStack = new Stack<>();
        nodeStack.push(this);

        while (!nodeStack.isEmpty()) {
            BinTreeNode node = nodeStack.pop();

            if (shapeDescSupplier.get()) {
                node.left = new BinTreeNode<>();
            }
            if (shapeDescSupplier.get()) {
                node.right = new BinTreeNode<>();
                nodeStack.push(node.right);
            }

            if (node.left != null) {
                nodeStack.push(node.left);
            }
        }
    }
}
