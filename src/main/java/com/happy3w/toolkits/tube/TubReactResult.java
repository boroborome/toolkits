package com.happy3w.toolkits.tube;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class TubReactResult {
    private final Map<String, List<?>> results;

    public TubReactResult(Map<String, List<?>> results) {
        this.results = results;
    }

    public <T> List<T> getResult(Class<T> type) {
        return (List<T>) results.get(type.getName());
    }
}
