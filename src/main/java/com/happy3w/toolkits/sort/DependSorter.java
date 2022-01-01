package com.happy3w.toolkits.sort;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.java.ext.Pair;
import com.happy3w.toolkits.iterator.EasyIterator;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DependSorter<V, K> {

    public static <V> List<V> sortByNeed(Iterable<Pair<V, V>> depends) {
        Map<V, Set<V>> needsMap = new HashMap<>();

        for (Pair<V, V> pair : depends) {
            Set<V> needs = needsMap.computeIfAbsent(pair.getKey(), k -> new HashSet<>());
            needsMap.computeIfAbsent(pair.getValue(), k -> new HashSet<>());
            needs.add(pair.getValue());
        }

        return sort(needsMap.keySet(), needsMap::get, v -> new HashSet<>(Arrays.asList(v)));
    }

    public static <V, K> List<V> sort(Iterable<V> items,
                                      Function<V, Set<K>> needsGenerator,
                                      Function<V, Set<K>> providesGenerator) {
        Map<K, GraphNode<V, K>> providesMap = new HashMap<>();
        List<GraphNode<V, K>> allNodes = EasyIterator.fromIterable(items)
                .map(item -> new GraphNode<>(item, needsGenerator.apply(item), providesGenerator.apply(item)))
                .peek(node -> saveProvideRelation(node, providesMap))
                .toList();
        initUpDownRelation(allNodes, providesMap);
        calculateDepth(allNodes);
        return allNodes.stream()
                .sorted(Comparator.comparing(GraphNode::getDepth))
                .map(node -> node.getValue())
                .collect(Collectors.toList());
    }

    private static <V, K> void calculateDepth(List<GraphNode<V, K>> allNodes) {
        int maxDepth = allNodes.size();
        long matchCount = allNodes.stream()
                .filter(node -> ListUtils.isEmpty(node.getUpstreamNodes()))
                .peek(head -> calculateDepthStart(head, maxDepth))
                .count();
        if (matchCount <= 0) {
            throw new IllegalArgumentException("There must be a big circle.");
        }
        checkCircleNode(allNodes);
    }

    private static <V, K> void checkCircleNode(List<GraphNode<V, K>> allNodes) {
        for (GraphNode<V, K> node : allNodes) {
            if (node.getDepth() == 0 && !ListUtils.isEmpty(node.getUpstreamNodes())) {
                throw new IllegalArgumentException("There must be a circle with node:" + node.getValue());
            }
        }
    }

    private static <V, K> void calculateDepthStart(GraphNode<V, K> head, int maxDepth) {
        Stack<GraphNode<V, K>> nodeStack = new Stack<>();
        nodeStack.push(head);
        while (!nodeStack.isEmpty()) {
            GraphNode<V, K> node = nodeStack.pop();
            node.setDepth(maxDepth(node.getUpstreamNodes()) + 1);
            checkCircleByDepth(node, maxDepth);
            for (GraphNode<V, K> g : node.getDownstreamNodes()) {
                nodeStack.push(g);
            }
        }
    }

    private static void checkCircleByDepth(GraphNode node, int maxDepth) {
        if (node.getDepth() > maxDepth) {
            throw new IllegalArgumentException("There must be a circle with node:" + node.getValue());
        }
    }

    private static <V, K> int maxDepth(Set<GraphNode<V, K>> nodes) {
        int max = 0;
        for (GraphNode<V, K> g : nodes) {
            max = Math.max(max, g.getDepth());
        }
        return max;
    }

    private static <V, K> void initUpDownRelation(List<GraphNode<V, K>> allNodes, Map<K, GraphNode<V, K>> providesMap) {
        for (GraphNode<V, K> node : allNodes) {
            for (K need : node.getNeeds()) {
                GraphNode<V, K> upNode = providesMap.get(need);
                if (upNode != null) {
                    node.getUpstreamNodes().add(upNode);
                    upNode.getDownstreamNodes().add(node);
                }
            }
        }
    }

    private static <V, K> void saveProvideRelation(GraphNode<V, K> node, Map<K, GraphNode<V, K>> providesMap) {
        for (K provide : node.getProvides()) {
            GraphNode<V, K> existNode = providesMap.get(provide);
            if (existNode != null) {
                throw new IllegalArgumentException(
                        MessageFormat.format("Multiple nodes provide the same value.[{0}, {1}] provide {2}.",
                                node.getValue(), existNode.getValue(), provide));
            }
            providesMap.put(provide, node);
        }
    }
}
