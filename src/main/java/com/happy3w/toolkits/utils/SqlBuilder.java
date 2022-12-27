package com.happy3w.toolkits.utils;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class SqlBuilder {
    private StringBuilder buffer = new StringBuilder();
    private List<Object> params = new ArrayList<>();
    private Set<String> tags = new HashSet<>();

    public SqlBuilder(String sql) {
        buffer.append(sql);
    }

    private static final Object[] emptyParams = new Object[0];

    public SqlBuilder appendWhenFirst(String sql, String tag) {
        if (!tags.contains(tag)) {
            append(sql);
            tags.add(tag);
        }
        return this;
    }

    public SqlBuilder appendWhenNext(String sql, String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        } else {
            append(sql);
        }
        return this;
    }

    public SqlBuilder append(String sql) {
        return append(sql, emptyParams);
    }

    public SqlBuilder append(String sql, Object... params) {
        buffer.append(sql);
        if (params != null && params.length > 0) {
            this.params.addAll(Arrays.asList(params));
        }
        return this;
    }

    public String getSql() {
        return buffer.toString();
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public void cleanLast(char ch) {
        while (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == ch) {
            buffer.setLength(buffer.length() - 1);
        }
    }
}
