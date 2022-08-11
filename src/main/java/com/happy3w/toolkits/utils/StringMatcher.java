package com.happy3w.toolkits.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class StringMatcher {
    private final String regHead;

    private Set<String> constStrings = new HashSet<>();
    private List<Pattern> patterns = new ArrayList<>();

    public StringMatcher(String regHead) {
        this.regHead = regHead;
    }

    public StringMatcher withRules(Collection<String> rules) {
        for (String rule : rules) {
            if (rule.startsWith(regHead)) {
                Pattern pattern = Pattern.compile(rule.substring(regHead.length()));
                patterns.add(pattern);
            } else {
                constStrings.add(rule);
            }
        }
        return this;
    }

    public boolean isMatch(String text) {
        if (constStrings.contains(text)) {
            return true;
        }

        for (Pattern pattern : patterns) {
            if (pattern.matcher(text).matches()) {
                return true;
            }
        }
        return false;
    }
}
