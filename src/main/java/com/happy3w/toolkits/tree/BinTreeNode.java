package com.happy3w.toolkits.tree;

import com.happy3w.toolkits.utils.ArrayUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
public class BinTreeNode<T> {
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

    public boolean[] toShapeDesc() {
        List<Boolean> shapeDesc = new ArrayList<>();
        visitTree(null, shapeDesc::add);
        return ArrayUtils.toBooleanArray(shapeDesc);
    }

    public static BinTreeNode from(boolean[] shapeDesc) {
        BinTreeNode head = new BinTreeNode();
        head.buildTree(null, ArrayUtils.toBooleanList(shapeDesc).iterator()::next);
        return head;
    }

    public static <T> String serialize(BinTreeNode<T> head, Function<T, String> dataConvertor) {
        if (head == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        head.serialize(dataConvertor, String::valueOf, item -> {
            builder.append(item);
            builder.append(',');
        });
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public <K> void serialize(Function<T, K> dataConvertor,
                              Function<Boolean, K> shapeConvertor,
                              Consumer<K> itemCollector) {
        visitTree(data -> itemCollector.accept(dataConvertor.apply(data)),
                shape -> itemCollector.accept(shapeConvertor.apply(shape)));
    }

    public static <T> BinTreeNode<T> deserialize(String encodeStr, Function<String, T> dataConvertor) {
        if (encodeStr == null || encodeStr.isEmpty()) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(encodeStr, ",");
        if (!tokenizer.hasMoreElements()) {
            return null;
        }

        BinTreeNode<T> head = new BinTreeNode<>();
        head.deserialize(tokenizer::nextToken, dataConvertor, Boolean::parseBoolean);
        return head;
    }

    public <K> void deserialize(Supplier<K> itemSupplier,
                                Function<K, T> dataConvertor,
                                Function<K, Boolean> shapeConvertor) {
        buildTree(() -> dataConvertor.apply(itemSupplier.get()),
                () -> shapeConvertor.apply(itemSupplier.get()));
    }

    public void buildTree(Supplier<T> dataSupplier, Supplier<Boolean> shapeSupplier) {
        Stack<BinTreeNode> nodeStack = new Stack<>();
        nodeStack.push(this);

        while (!nodeStack.isEmpty()) {
            BinTreeNode node = nodeStack.pop();
            if (dataSupplier != null) {
                node.data = dataSupplier.get();
            }

            if (shapeSupplier.get()) {
                node.left = new BinTreeNode<>();
            }
            if (shapeSupplier.get()) {
                node.right = new BinTreeNode<>();
                nodeStack.push(node.right);
            }

            if (node.left != null) {
                nodeStack.push(node.left);
            }
        }
    }

    public void visitTree(Consumer<T> dataVisitor, Consumer<Boolean> shapeVisitor) {
        Stack<BinTreeNode<T>> nodeStack = new Stack<>();
        nodeStack.push(this);
        while (!nodeStack.isEmpty()) {
            BinTreeNode<T> node = nodeStack.pop();

            if (dataVisitor != null) {
                dataVisitor.accept(node.data);
            }

            boolean hasLeft = node.left != null;
            boolean hasRight = node.right != null;

            shapeVisitor.accept(hasLeft);
            shapeVisitor.accept(hasRight);

            if (hasRight) {
                nodeStack.push(node.right);
            }
            if (hasLeft) {
                nodeStack.push(node.left);
            }
        }
    }
}
