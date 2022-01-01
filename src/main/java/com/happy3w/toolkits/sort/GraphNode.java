package com.happy3w.toolkits.sort;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class GraphNode<V, K> {
    private V value;
    private Set<K> needs;
    private Set<K> provides;

    private Set<GraphNode<V, K>> upstreamNodes = new HashSet<>();
    private Set<GraphNode<V, K>> downstreamNodes = new HashSet<>();
    private int depth;

    public GraphNode(V value, Set<K> needs, Set<K> provides) {
        this.value = value;
        this.needs = needs;
        this.provides = provides;
        ensureNoSelfCircle();
    }

    private void ensureNoSelfCircle() {
        Set<K> retains = new HashSet<>(needs);
        retains.retainAll(provides);
        if (!retains.isEmpty()) {
            throw new IllegalArgumentException("There must be a circle with node:" + value);
        }
    }

}
