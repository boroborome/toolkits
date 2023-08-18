package com.happy3w.toolkits.reflect;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ParentTypeIterator extends NeedFindIterator<Class> {
    private Stack<Class> typeShouldBeReturn = new Stack<>();
    private Set<Class> visitedTypes = new HashSet<>();

    public ParentTypeIterator(Class dataType) {
        collectParents(dataType);
    }

    private void collectParents(Class dataType) {
        collectType(dataType.getSuperclass());
        Class[] ifTypes = dataType.getInterfaces();
        if (ifTypes != null) {
            for (Class ifType : ifTypes) {
                collectType(ifType);
            }
        }
    }

    private void collectType(Class type) {
        if (type == null
                || visitedTypes.contains(type)
                || type.equals(Object.class)) {
            return;
        }
        typeShouldBeReturn.push(type);
        visitedTypes.add(type);
    }

    @Override
    protected NullableOptional<Class> findNext() {
        if (typeShouldBeReturn.isEmpty()) {
            return NullableOptional.empty();
        }

        Class curType = typeShouldBeReturn.pop();
        collectParents(curType);
        return NullableOptional.of(curType);
    }
}
