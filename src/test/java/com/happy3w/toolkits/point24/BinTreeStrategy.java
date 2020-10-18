package com.happy3w.toolkits.point24;

import com.happy3w.toolkits.combination.DimCombinationMaker;
import com.happy3w.toolkits.permutation.DuplicatedPermutationMaker;
import com.happy3w.toolkits.tree.BinTreeNode;
import com.happy3w.toolkits.tree.TreeEnumerator;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class BinTreeStrategy implements Point24Strategy {
    private static final boolean ACCEPT_DECIMAL = false;
    private static final boolean ACCEPT_NEGATIVE = false;
    private static final boolean FILTER_SWITCH = true;

    private AtomicLong caseCounter;

    @Override
    public Stream<String> judge(int[] numbers) {
        caseCounter = new AtomicLong(0);
        return TreeEnumerator.enumFullBinTree(numbers.length * 2 - 1)
                .flatMap(head -> enumOperator(head, numbers.length - 1))
                .flatMap(meta -> enumConstValue(meta, numbers))
                .peek(meta -> caseCounter.incrementAndGet())
                .filter(meta -> checkValue(meta, 24))
                .map(meta -> toExpStr(meta.wrapper.exp));
    }

    @Override
    public long getJudgedExpCount() {
        return caseCounter == null ? 0 : caseCounter.get();
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

    private static boolean doubleEqual(double a, double b) {
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return Math.abs(a - b) < 0.0000001d;
    }

    private boolean checkValue(FinalExpressionMeta meta, double expectValue) {
        fillExpToNode(meta.opers, meta.wrapper.operNodes);
        fillExpToNode(meta.values, meta.wrapper.valueNodes);

        BinTreeNode<IExpression> expTree = meta.wrapper.exp;
        return doubleEqual(expTree.getData().getValue(expTree), expectValue);
    }

    private void fillExpToNode(List<IExpression> opers, List<BinTreeNode<IExpression>> operNodes) {
        for (int i = operNodes.size() - 1; i >= 0; i--) {
            operNodes.get(i).setData(opers.get(i));
        }
    }

    private Stream<FinalExpressionMeta> enumOperator(boolean[] expShape, int operCount) {
        ExpShapeWrapper shapeWrapper = new ExpShapeWrapper(BinTreeNode.from(expShape));

        List<IExpression> operatorRange = Arrays.asList(new AddOperator(), new DelOperator(), new MultOperator(), new DivOperator());
        List<Pair<String, List<IExpression>>> dimensions = new ArrayList<>();
        for (int i = 0; i < operCount; i++) {
            dimensions.add(new Pair<>("d" + i, operatorRange));
        }
        return DimCombinationMaker.<String, IExpression>builder()
                .dimensions(dimensions).build()
                .generateSimple()
                .map(opers -> new FinalExpressionMeta(shapeWrapper, opers));
    }

    private static class FinalExpressionMeta {
        private ExpShapeWrapper wrapper;
        private List<IExpression> opers;
        private List<IExpression> values;

        public FinalExpressionMeta(ExpShapeWrapper wrapper, List<IExpression> opers) {
            this.wrapper = wrapper;
            this.opers = opers;
        }

        public FinalExpressionMeta cloneMeta() {
            FinalExpressionMeta meta = new FinalExpressionMeta(wrapper, opers);
            meta.values = values;
            return meta;
        }

        public FinalExpressionMeta withValues(IExpression[] values) {
            this.values = Arrays.asList(values);
            return this;
        }
    }

    private Stream<FinalExpressionMeta> enumConstValue(FinalExpressionMeta expMeta, int[] constValues) {
        IExpression[] valueNodes = new IExpression[constValues.length];
        for (int i = 0; i < constValues.length; i++) {
            valueNodes[i] = new ConstExpression(constValues[i]);
        }

        return new DuplicatedPermutationMaker<>(valueNodes, (a, b) ->
                ((ConstExpression) a).value == ((ConstExpression) b).value)
                .generate()
                .map(values -> expMeta.cloneMeta().withValues(values));
    }

    private static class ExpShapeWrapper {
        private BinTreeNode<IExpression> exp;
        private List<BinTreeNode<IExpression>> operNodes;
        private List<BinTreeNode<IExpression>> valueNodes;

        public ExpShapeWrapper(BinTreeNode<IExpression> exp) {
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
        double getValue(BinTreeNode<IExpression> self);
    }

    @Getter
    public static abstract class OperatorExpression implements IExpression {
        protected final char operator;

        public OperatorExpression(char operator) {
            this.operator = operator;
        }

        protected double getNodeValue(BinTreeNode<IExpression> node) {
            return node.getData().getValue(node);
        }

        private boolean validateValue(double value) {
            if (Double.isInfinite(value)) {
                return false;
            }
            if (!ACCEPT_NEGATIVE && value < 0) {
                return false;
            }
            return true;
        }

        public double getValue(BinTreeNode<IExpression> self) {
            double left = getNodeValue(self.getLeft());
            if (!validateValue(left)) {
                return Double.POSITIVE_INFINITY;
            }

            double right = getNodeValue(self.getRight());
            if (!validateValue(right)) {
                return Double.POSITIVE_INFINITY;
            }

            return calculate(left, right);
        }

        protected abstract double calculate(double left, double right);

        public String toString() {
            return String.valueOf(operator);
        }
    }

    public static class AddOperator extends OperatorExpression {
        public AddOperator() {
            super('+');
        }

        @Override
        public double calculate(double left, double right) {
            if (FILTER_SWITCH && left < right) {
                return Double.POSITIVE_INFINITY;
            }
            return left + right;
        }
    }

    public static class DelOperator extends OperatorExpression {
        public DelOperator() {
            super('-');
        }

        @Override
        public double calculate(double left, double right) {
            return left - right;
        }
    }

    public static class MultOperator extends OperatorExpression {
        public MultOperator() {
            super('*');
        }

        @Override
        public double calculate(double left, double right) {
            if (FILTER_SWITCH && left < right) {
                return Double.POSITIVE_INFINITY;
            }

            return left * right;
        }
    }

    public static class DivOperator extends OperatorExpression {
        public DivOperator() {
            super('/');
        }

        @Override
        public double calculate(double left, double right) {
            if (right == 0) {
                return Double.POSITIVE_INFINITY;
            }

            double v = left / right;
            if (ACCEPT_DECIMAL) {
                return v;
            }

            if (v != (int) v) {
                return Double.POSITIVE_INFINITY;
            }
            return v;
        }
    }

    @Getter
    @Setter
    public static class ConstExpression implements IExpression {
        private double value;

        public ConstExpression(double value) {
            this.value = value;
        }

        @Override
        public double getValue(BinTreeNode<IExpression> self) {
            return value;
        }

        public String toString() {
            return String.valueOf((int) value);
        }
    }
}
