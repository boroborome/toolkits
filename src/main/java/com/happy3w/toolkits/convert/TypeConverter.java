package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.PrimitiveTypeUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TypeConverter {
    public static final TypeConverter INSTANCE = new TypeConverter();
    static {
        INSTANCE.regist(new DateLongBiTci());
        INSTANCE.regist(new NumStrTci());
        INSTANCE.regist(new NumBigDecimalTci());
        INSTANCE.regist(new NullStrTci());
        INSTANCE.regist(new DateStrBiTci());
        INSTANCE.regist(new StrLongTci());
        INSTANCE.regist(new StrIntegerTci());
        INSTANCE.regist(new StrBigDecimalTci());
        INSTANCE.regist(new StrDoubleTci());
        INSTANCE.regist(new StrFloatTci());
        INSTANCE.regist(new BoolStrBiTci());
    }

    private Map<ITypeConvertItemKey<?, ?>, ITypeConvertItem<?, ?>> convertItemMap = new HashMap<>();
    private Map<Class<?>, List<ITypeConvertItem<?, ?>>> sourceTciMap = new HashMap<>();
    private Map<Class<?>, ITypeConvertItem<?, ?>> tciMap = new HashMap<>();

    public TypeConverter newCopy() {
        TypeConverter newConverter = new TypeConverter();
        newConverter.convertItemMap.putAll(convertItemMap);
        newConverter.sourceTciMap.putAll(sourceTciMap);
        newConverter.tciMap.putAll(tciMap);
        return newConverter;
    }

    public void regist(ITypeConvertItem<?, ?> convertItem) {
        tciMap.put(convertItem.getClass(), convertItem);
        regist(convertItem, convertItem);
        if (convertItem instanceof IBiTypeConvertItem) {
            IBiTypeConvertItem<?, ?> biTci = (IBiTypeConvertItem) convertItem;
            ITypeConvertItem<?, ?> reversedTci = biTci.reverse();
            regist(reversedTci, reversedTci);
        }
    }

    private void regist(ITypeConvertItemKey<?, ?> key, ITypeConvertItem<?, ?> item) {
        if (key.getSourceType() == null) {
            key = new TciKey<>(Void.class, key.getTargetType());
        }
        convertItemMap.put(key, item);
        List<ITypeConvertItem<?, ?>> items = sourceTciMap.computeIfAbsent(key.getSourceType(), k -> new ArrayList<>());
        final Class<?> targetType = key.getTargetType();
        items.removeIf(it -> it.getTargetType() == targetType);
        items.add(item);
    }

    public <S, T> T convert(S source, Class<T> targetType) {
        Class<?> sourceType = source == null ? Void.class : source.getClass();
        targetType = PrimitiveTypeUtil.toObjType(targetType);
        if (targetType.isAssignableFrom(sourceType)) {
            return (T) source;
        }
        TciKey<?, ?> tciKey = new TciKey<>(sourceType , targetType);

        ITypeConvertItem<S, T> convertItem = (ITypeConvertItem<S, T>) convertItemMap.get(tciKey);
        if (convertItem == null) {
            convertItem = (ITypeConvertItem<S, T>) findTci(tciKey);
            if (convertItem == null) {
                throw new UnsupportedOperationException(
                        MessageFormat.format("Failed to convert {0} to {1}. No direct path and indirect path.", source, targetType));
            }
            convertItemMap.put(tciKey, convertItem);
        }
        return convertItem.toTarget(source);
    }

    public <T extends ITypeConvertItem<?, ?>> T findTci(Class<T> tciType) {
        return (T) tciMap.get(tciType);
    }

    public <S, T> ITypeConvertItem<S, T> findTci(TciKey<S, T> tciKey) {
        return findTci(tciKey.getSourceType(), tciKey.getTargetType());
    }
    public <S, T> ITypeConvertItem<S, T> findTci(Class<S> sourceTypeIn, Class<T> targetTypeIn) {
        Class<S> sourceType = PrimitiveTypeUtil.toObjType(sourceTypeIn);
        Class<T> targetType = PrimitiveTypeUtil.toObjType(targetTypeIn);

        List<ITypeConvertItem<?, ?>> items = findAllSourceTci(sourceType);
        if (items == null || items.isEmpty()) {
            return null;
        }
        List<PathInfo> pathInfos = ListUtils.map(items, PathInfo::new);
        for (int level = 1 ;!pathInfos.isEmpty(); ++level) {
            List<PathInfo> newPathInfos = new ArrayList<>(pathInfos.size() * 2);
            for (PathInfo pathInfo : pathInfos) {
                int pathInfoLength = pathInfo.path.size();
                if (pathInfoLength == level) {
                    if (pathInfo.getLastType() == targetType) {
                        return pathInfo.createTci(sourceType, targetType);
                    }
                    findMorePathInfo(pathInfo, newPathInfos);
                } else if (pathInfoLength > level) {
                    newPathInfos.add(pathInfo);
                }
            }
            pathInfos = newPathInfos;
        }

        return null;
    }

    private <S> List<ITypeConvertItem<?, ?>> findAllSourceTci(Class<S> sourceType) {
        Stack<Class> typeStack = new Stack<>();
        typeStack.push(sourceType);
        List<ITypeConvertItem<?, ?>> tcis = new ArrayList<>();
        Set<Class> typeProcessed = new HashSet<>();
        while (!typeStack.isEmpty()) {
            Class curType = typeStack.pop();
            if (typeProcessed.contains(curType)) {
                continue;
            }
            List<ITypeConvertItem<?, ?>> curTypeTcis = sourceTciMap.get(curType);
            if (curTypeTcis != null && !curTypeTcis.isEmpty()) {
                tcis.addAll(curTypeTcis);
            }

            Class superClass = curType.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                typeStack.push(superClass);
            }
            for (Class ifType : curType.getInterfaces()) {
                typeStack.push(ifType);
            }
        }
        return tcis;
    }

    private <S, T> void findMorePathInfo(PathInfo pathInfo, List<PathInfo> newPathInfos) {
        List<ITypeConvertItem<?, ?>> tciList = findAllSourceTci(pathInfo.getLastType());
        if (tciList == null || tciList.isEmpty()) {
            return;
        }
        for (ITypeConvertItem<?, ?> curTci : tciList) {
            if (pathInfo.canAccept(curTci)) {
                newPathInfos.add(pathInfo.newPathInfo(curTci));
            }
        }
    }

    private static class PathInfo {
        private List<ITypeConvertItem<?, ?>> path;
        private Set<Class<?>> refTypes;

        public PathInfo(PathInfo pathInfo) {
            path = new ArrayList<>(pathInfo.path);
            refTypes = new HashSet<>(pathInfo.refTypes);
        }

        public PathInfo(ITypeConvertItem<?, ?> startTci) {
            path = new ArrayList<>();
            refTypes = new HashSet<>();
            addPath(startTci);
        }

        public boolean canAccept(ITypeConvertItem<?, ?> tci) {
            return !refTypes.contains(tci.getSourceType())
                    || !refTypes.contains(tci.getTargetType());
        }

        public PathInfo newPathInfo(ITypeConvertItem<?, ?> tci) {
            PathInfo newPathInfo = new PathInfo(this);
            newPathInfo.addPath(tci);
            return newPathInfo;
        }

        public boolean addPath(ITypeConvertItem<?, ?> tci) {
            if (tci instanceof IndirectTci) {
                for (ITypeConvertItem<?, ?> item : ((IndirectTci<?, ?>) tci).getConvertItems()) {
                    addPathDirect(item);
                }
            } else {
                addPathDirect(tci);
            }
            return true;
        }

        private void addPathDirect(ITypeConvertItem<?, ?> tci) {
            path.add(tci);
            refTypes.add(tci.getSourceType());
            refTypes.add(tci.getTargetType());
        }

        public Class<?> getLastType() {
            return path.get(path.size() - 1).getTargetType();
        }

        public <S, T> ITypeConvertItem<S, T> createTci(Class<S> sourceType, Class<T> targetType) {
            return new IndirectTci<S, T>(sourceType, targetType, path);
        }
    }
}
