package com.happy3w.toolkits.tree;

import java.text.MessageFormat;
import java.util.Spliterators;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TreeEnumerator {
    public static Stream<boolean[]> enumFullBinTree(int nodeCount) {
        return enumFullTree(nodeCount, 2);
    }

    public static Stream<boolean[]> enumFullTree(int nodeCount, int bifurcationCount) {
        int bifurcationNodeCount = nodeCount / bifurcationCount;
        if (bifurcationNodeCount * bifurcationCount + 1 != nodeCount) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "Full {0} bifurcation tree node count wrong.expected is {1}, but input {2}.",
                    bifurcationCount,
                    bifurcationNodeCount * bifurcationCount + 1,
                    nodeCount));
        }
        return StreamSupport.stream(new TreeShapeSpliterator(bifurcationNodeCount, bifurcationCount), false);
    }

    private static class TreeShapeSpliterator extends Spliterators.AbstractSpliterator<boolean[]> {
        private int bifurcationNodeCount;
        private int bifurcationCount;

        /**
         * 表示子节点是否是分叉节点。true为分叉节点；false为叶子节点
         */
        private boolean[] curBifurcationNodeShape;

        protected TreeShapeSpliterator(int bifurcationNodeCount, int bifurcationCount) {
            super(0, 0);
            this.bifurcationNodeCount = bifurcationNodeCount;
            this.bifurcationCount = bifurcationCount;
        }

        @Override
        public boolean tryAdvance(Consumer<? super boolean[]> action) {
            if (curBifurcationNodeShape == null) {
                curBifurcationNodeShape = initBifurcationShape();
            } else {
                if (!nextShape()) {
                    return false;
                }
            }
            boolean[] normalShape = generateNormalShape(curBifurcationNodeShape);
            action.accept(normalShape);
            return true;
        }

        private boolean[] generateNormalShape(boolean[] curBifurcationNodeShape) {
            Stack<Boolean> nodeTypeStack = new Stack<>();
            Stack<boolean[]> groupStack = initGroupStack(curBifurcationNodeShape);
            boolean[] shape = new boolean[(bifurcationNodeCount * bifurcationCount + 1) * bifurcationCount];
            int shapeIndex = 0;

            while (true) {
                if (nodeTypeStack.isEmpty()) {
                    if (groupStack.isEmpty()) {
                        break;
                    }
                    shapeIndex = fetchFromGroup(groupStack, nodeTypeStack, shape, shapeIndex);
                }

                boolean shapeType = nodeTypeStack.pop();
                if (shapeType == true) {
                    shapeIndex = fetchFromGroup(groupStack, nodeTypeStack, shape, shapeIndex);
                } else {
                    shapeIndex = writeShape(shape, shapeIndex, false, bifurcationCount);
                }
            }
            return shape;
        }

        private int writeShape(boolean[] shape, int shapeIndex, boolean shapeValue, int shapeCount) {
            for (int i = 0; i < shapeCount; i++) {
                shape[shapeIndex + i] = shapeValue;
            }
            return shapeIndex + shapeCount;
        }

        private Stack<boolean[]> initGroupStack(boolean[] curBifurcationNodeShape) {
            Stack<boolean[]> groupStack = new Stack<>();
            groupStack.push(new boolean[bifurcationCount]); // add the ignored group which is always be leaf node in Bifurcation Shape
            for (int i = curBifurcationNodeShape.length; i > 0; i-= bifurcationCount) {
                boolean[] group = new boolean[bifurcationCount];
                System.arraycopy(curBifurcationNodeShape, i - bifurcationCount, group, 0, bifurcationCount);
                groupStack.push(group);
            }

            return groupStack;
        }

        private int fetchFromGroup(Stack<boolean[]> groupStack, Stack<Boolean> nodeTypeStack, boolean[] shape, int shapeIndex) {
            boolean[] bitGroup = groupStack.pop();
            pushToTypeStack(bitGroup, nodeTypeStack);
            return writeShape(shape, shapeIndex, true, bifurcationCount);
        }

        private void pushToTypeStack(boolean[] bitGroup, Stack<Boolean> nodeTypeStack) {
            for (int i = bitGroup.length - 1; i >= 0; i--) {
                nodeTypeStack.push(bitGroup[i]);
            }
        }

        private boolean nextShape() {
            int lastBifIndex = lastIndexOf(curBifurcationNodeShape, curBifurcationNodeShape.length - 1, true);
            int lastLeafIndex = lastIndexOf(curBifurcationNodeShape, lastBifIndex - 1, false);
            if (lastLeafIndex == -1) {
                return false;
            }

            curBifurcationNodeShape[lastLeafIndex] = true;
            for (int index = lastLeafIndex + 1; index <= lastBifIndex; index++) {
                curBifurcationNodeShape[index] = false;
            }

            resetSubShape(curBifurcationNodeShape, lastBifIndex - lastLeafIndex - 1);
            return true;
        }

        private int lastIndexOf(boolean[] curBifurcationNodeShape, int index, boolean targetBit) {
            for (;index >= 0; index--) {
                if (curBifurcationNodeShape[index] == targetBit) {
                    return index;
                }
            }
            return -1;
        }

        private void resetSubShape(boolean[] bifurcationShape, int bifBitCount) {
            for (int curIndex = bifurcationShape.length - 1;
                 bifBitCount > 0 && curIndex >= 0;
                 curIndex -= bifurcationCount, bifBitCount--) {
                bifurcationShape[curIndex] = true;
            }
        }

        private boolean[] initBifurcationShape() {
            boolean[] bifurcationShape = new boolean[(bifurcationNodeCount - 1) * bifurcationCount];
            resetSubShape(bifurcationShape, bifurcationNodeCount - 1);
            return bifurcationShape;
        }
    }
}
