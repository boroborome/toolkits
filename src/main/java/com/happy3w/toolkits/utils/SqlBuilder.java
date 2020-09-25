package com.happy3w.toolkits.utils;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class SqlBuilder {
    private StringBuilder buffer = new StringBuilder();
    private List<Object> params = new ArrayList<>();

    public SqlBuilder(String sql) {
        buffer.append(sql);
    }

    public SqlBuilder append(String sql) {
        buffer.append(sql);
        return this;
    }

    public SqlBuilder append(String sql, Object... params) {
        buffer.append(sql);
        this.params.addAll(Arrays.asList(params));
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
