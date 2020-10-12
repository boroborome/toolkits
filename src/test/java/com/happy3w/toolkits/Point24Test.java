package com.happy3w.toolkits;

import com.happy3w.toolkits.combination.CombinationGenerator;
import com.happy3w.toolkits.permutation.PermutationGenerator;
import com.happy3w.toolkits.tree.BinTreeNode;
import com.happy3w.toolkits.tree.TreeEnumerator;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Point24Test {

    @Test
    public void test24() {
        IExpression[] valueExps = new IExpression[]{
                new ConstExpression(2),
                new ConstExpression(3),
                new ConstExpression(4),
                new ConstExpression(5),
                new ConstExpression(6),
        };

        TreeEnumerator.enumFullBinTree(valueExps.length * 2 - 1)
                .map(head -> new TreeWrapper(BinTreeNode.from(head)))
                .flatMap(wrapper -> enumOperator(wrapper, valueExps.length - 1))
                .flatMap(meta -> enumConstValue(meta, valueExps))
                .filter(meta -> checkValue(meta, 24))
                .forEach(meta -> System.out.println(toExpStr(meta.wrapper.exp)));
    }

    private String toExpStr(BinTreeNode<IExpression> expTree) {
        if (expTree.isLeaf()) {
            return expTree.getData().toString();
        } else {
            return MessageFormat.format("({0}{1}{2})",
                    toExpStr(expTree.getLeft()),
                    expTree.getData().toString(),
                    toExpStr(expTree.getRight()));
        }
    }

    private boolean checkValue(FinalExpressionMeta meta, int expectValue) {
        fillExp(meta.opers, meta.wrapper.operNodes);
        fillExp(meta.values, meta.wrapper.valueNodes);

        BinTreeNode<IExpression> expTree = meta.wrapper.exp;
        return expTree.getData().getValue(expTree) == expectValue;
    }

    private void fillExp(List<IExpression> opers, List<BinTreeNode<IExpression>> operNodes) {
        for (int i = operNodes.size() - 1; i >= 0; i --) {
            operNodes.get(i).setData(opers.get(i));
        }
    }

    private Stream<FinalExpressionMeta> enumOperator(TreeWrapper wrapper, int operCount) {
        List<IExpression> operatorRange = Arrays.asList(new AddOperator(), new DelOperator(), new MultOperator(), new DivOperator());
        List<Pair<String, List<IExpression>>> dimensions = new ArrayList<>();
        for (int i = 0; i < operCount; i++) {
            dimensions.add(new Pair<>("d" + i, operatorRange));
        }
        return CombinationGenerator.<String, IExpression>builder()
                .dimensions(dimensions)
                .build().generateSimple()
                .map(opers -> newWrapper(wrapper, opers));
    }

    private static class FinalExpressionMeta {
        private TreeWrapper wrapper;
        private List<IExpression> opers;
        private List<IExpression> values;

        public FinalExpressionMeta cloneMeta() {
            FinalExpressionMeta meta = new FinalExpressionMeta();
            meta.wrapper = wrapper;
            meta.opers = opers;
            meta.values = values;
            return meta;
        }

        public FinalExpressionMeta withValues(IExpression[] values) {
            this.values = Arrays.asList(values);
            return this;
        }
    }

    private FinalExpressionMeta newWrapper(TreeWrapper wrapper, List<IExpression> opers) {
        FinalExpressionMeta meta = new FinalExpressionMeta();
        meta.wrapper = wrapper;
        meta.opers = opers;
        return meta;
    }

    private Stream<FinalExpressionMeta> enumConstValue(FinalExpressionMeta expMeta, IExpression[] valueNodes) {
        return new PermutationGenerator<>(valueNodes)
                .generate()
                .map(values -> expMeta.cloneMeta().withValues(values));
    }

    private static class TreeWrapper {
        private BinTreeNode<IExpression> exp;
        private List<BinTreeNode<IExpression>> operNodes;
        private List<BinTreeNode<IExpression>> valueNodes;

        public TreeWrapper(BinTreeNode<IExpression> exp) {
            this.exp = exp;
            operNodes = new ArrayList<>();
            valueNodes = new ArrayList<>();
            exp.visitNode(node -> {
                if (node.isLeaf()) {
                    valueNodes.add(node);
                } else {
                    operNodes.add(node);
                }
            });
        }
    }

    public interface IExpression {
        int getValue(BinTreeNode<IExpression> self);
    }

    @Getter
    public static abstract class OperatorExpression implements IExpression {
        protected final char operator;
        public OperatorExpression(char operator) {
            this.operator = operator;
        }

        protected int getNodeValue(BinTreeNode<IExpression> node) {
            return node.getData().getValue(node);
        }

        public int getValue(BinTreeNode<IExpression> self) {
            int left = getNodeValue(self.getLeft());
            if (left < 0) {
                return -1;
            }
            int right = getNodeValue(self.getRight());
            if (right < 0) {
                return -1;
            }
            return calculate(left, right);
        }

        protected abstract int calculate(int left, int right);

        public String toString() {
            return String.valueOf(operator);
        }
    }

    public static class AddOperator extends OperatorExpression {
        public AddOperator() {
            super('+');
        }

        @Override
        public int calculate(int left, int right) {
            return left + right;
        }
    }

    public static class DelOperator extends OperatorExpression {
        public DelOperator() {
            super('-');
        }

        @Override
        public int calculate(int left, int right) {
            return left - right;
        }
    }

    public static class MultOperator extends OperatorExpression {
        public MultOperator() {
            super('*');
        }

        @Override
        public int calculate(int left, int right) {
            return left * right;
        }
    }

    public static class DivOperator extends OperatorExpression {
        public DivOperator() {
            super('/');
        }

        @Override
        public int calculate(int left, int right) {
            if (right == 0) {
                return -1;
            }

            int v = left / right;
            if (v * right != left) {
                return -1;
            }
            return v;
        }
    }

    @Getter
    @Setter
    public static class ConstExpression implements IExpression {
        private int value;
        public ConstExpression(int value) {
            this.value = value;
        }

        @Override
        public int getValue(BinTreeNode<IExpression> self) {
            return value;
        }

        public String toString() {
            return String.valueOf(value);
        }
    }
}
