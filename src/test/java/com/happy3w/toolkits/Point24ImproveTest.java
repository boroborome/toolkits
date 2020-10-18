package com.happy3w.toolkits;

import com.happy3w.toolkits.combination.GroupCombinationMaker;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Point24ImproveTest {

    private static final boolean ACCEPT_DECIMAL = false;
    private static final boolean ACCEPT_NEGATIVE = false;

    @Test
    public void should_split_two_group_success() {
        System.out.println(splitTwoGroups(new Integer[]{6, 6, 6, 6})
                .map(result -> Arrays.asList(Arrays.asList(result[0]), Arrays.asList(result[1])))
                .map(result -> result.toString())
                .collect(Collectors.joining("\n")));
    }

    @Test
    public void test24() {
        Integer[] constValues = new Integer[]{1, 3, 7, 4, 5, 8};

        AtomicLong caseCounter = new AtomicLong(0);
        AtomicLong resultCounter = new AtomicLong(0);
        makeExpressions(constValues)
                .peek(meta -> caseCounter.incrementAndGet())
                .filter(exp -> doubleEqual(exp.getValue(), 24))
                .peek(meta -> resultCounter.incrementAndGet())
                .forEach(exp -> System.out.println(exp.toString()));

        System.out.println(MessageFormat.format("Total case count:{0}", caseCounter.get()));
        System.out.println(MessageFormat.format("Total result count:{0}", resultCounter.get()));
    }

    public static Stream<IExpression> makeExpressions(Integer[] constValues) {
        if (constValues.length == 1) {
            return Stream.of(new ConstExpression(constValues[0]));
        }
        return operExpMakers()
                .flatMap(oper -> oper.makeOperExp(splitTwoGroups(constValues)));
    }

    public static Stream<Integer[][]> splitTwoGroups(Integer[] constValues) {
        int dataCount = constValues.length;
        if (dataCount == 2) {
            Integer[][] groups = new Integer[2][];
            groups[0] = new Integer[]{constValues[0]};
            groups[1] = new Integer[]{constValues[1]};
            return Stream.of(groups, null)
                    .filter(Objects::nonNull);
        }

        GroupCombinationMaker<Integer> maker = new GroupCombinationMaker<>(constValues, Integer::equals);
        return IntStream.range(1, constValues.length / 2 + 1)
                .mapToObj(i -> new int[]{i, constValues.length - i})
                .flatMap(itemCountsInGroup -> maker.makeByItemCounts(itemCountsInGroup));
    }

    public interface IExpression {
        double getValue();
    }

    private static abstract class IOperExpMaker {
        protected final char operator;
        protected final boolean switchEqual;

        public IOperExpMaker(char operator, boolean switchEqual) {
            this.operator = operator;
            this.switchEqual = switchEqual;
        }

        public Stream<IExpression> makeOperExp(Stream<Integer[][]> constValues) {
            return constValues.flatMap(this::makeOperExpByGroup);
        }

        private Stream<IExpression> makeOperExpByGroup(Integer[][] groupValues) {
            Integer[] g0 = groupValues[0];
            Integer[] g1 = groupValues[1];
            if (switchEqual || groupEqual(g0, g1)) {
                return makeExpByLeftRight(g0, g1);
            }
            return Stream.concat(makeExpByLeftRight(g0, g1),
                    makeExpByLeftRight(g1, g0));
        }

        private boolean groupEqual(Integer[] g0, Integer[] g1) {
            if (g0.length != g1.length) {
                return false;
            }
            List<Integer> g0List = new ArrayList<>(Arrays.asList(g0));
            for (Integer v : g1) {
                int pos = g0List.indexOf(v);
                if (pos < 0) {
                    return false;
                }
                g0List.remove(pos);
            }
            return true;
        }

        private Stream<IExpression> makeExpByLeftRight(Integer[] leftValues, Integer[] rightValues) {
            return makeExpressions(leftValues)
                    .flatMap(left -> makeExpressions(rightValues)
                            .map(right -> new OperatorExpression(operator, this::calculate, left, right))
                    );

        }

        public abstract double calculate(double left, double right);
    }

    @Getter
    public static class OperatorExpression implements IExpression {
        protected final char operator;
        protected final BiFunction<Double, Double, Double> calculate;
        protected IExpression leftExp;
        protected IExpression rightExp;

        public OperatorExpression(char operator,
                                  BiFunction<Double, Double, Double> calculate,
                                  IExpression leftExp,
                                  IExpression rightExp) {
            this.operator = operator;
            this.calculate = calculate;
            this.leftExp = leftExp;
            this.rightExp = rightExp;
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

        @Override
        public double getValue() {
            double left = leftExp.getValue();
            if (!validateValue(left)) {
                return Double.POSITIVE_INFINITY;
            }

            double right = rightExp.getValue();
            if (!validateValue(right)) {
                return Double.POSITIVE_INFINITY;
            }

            return calculate.apply(left, right);
        }

        public String toString() {
            return MessageFormat.format("({0}{1}{2})",
                    leftExp.toString(),
                    operator,
                    rightExp.toString());
        }
    }
    private static Stream<IOperExpMaker> operExpMakers() {
        return Stream.of(AddMaker, DelMaker, MultiMaker, DivMaker);
    }


    private static final IOperExpMaker AddMaker = new IOperExpMaker('+', true) {
        @Override
        public double calculate(double left, double right) {
            return left + right;
        }
    };

    private static final IOperExpMaker DelMaker = new IOperExpMaker('-', false) {
        @Override
        public double calculate(double left, double right) {
            return left - right;
        }
    };

    private static final IOperExpMaker MultiMaker = new IOperExpMaker('*', true) {
        @Override
        public double calculate(double left, double right) {
            return left * right;
        }
    };

    private static final IOperExpMaker DivMaker = new IOperExpMaker('/', false) {
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
    };

    private static boolean doubleEqual(double a, double b) {
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return Math.abs(a - b) < 0.0000001d;
    }

    @Getter
    @Setter
    public static class ConstExpression implements IExpression {
        private double value;

        public ConstExpression(double value) {
            this.value = value;
        }

        @Override
        public double getValue() {
            return value;
        }

        public String toString() {
            return String.valueOf((int) value);
        }
    }
}
