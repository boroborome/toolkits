package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.ListUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeConverter {
    public static final TypeConverter INSTANCE = new TypeConverter();
    static {
        INSTANCE.regist(new NumStrTci());
        INSTANCE.regist(new NullStrTci());
        INSTANCE.regist(new DateStrTci());
        INSTANCE.regist(new StrLongTci());
    }

    private Map<ITypeConvertItemKey<?, ?>, ITypeConvertItem<?, ?>> convertItemMap = new HashMap<>();
    private Map<Class<?>, List<ITypeConvertItem<?, ?>>> sourceTciMap = new HashMap<>();

    public void regist(ITypeConvertItem<?, ?> convertItem) {
        regist(convertItem, convertItem);
    }

    private void regist(ITypeConvertItemKey<?, ?> key, ITypeConvertItem<?, ?> item) {
        if (key.getSourceType() == null) {
            key = new TciKey<>(Void.class, key.getTargetType());
        }
        convertItemMap.put(key, item);
        List<ITypeConvertItem<?, ?>> items = sourceTciMap.computeIfAbsent(key.getSourceType(), k -> new ArrayList<>());
        items.add(item);
    }

    public <S, T> T convert(S source, Class<T> targetType) {
        Class<?> sourceType = source == null ? Void.class : source.getClass();
        TciKey<?, ?> tciKey = new TciKey<>(sourceType , targetType);

        ITypeConvertItem<S, T> convertItem = (ITypeConvertItem<S, T>) convertItemMap.get(tciKey);
        if (convertItem == null) {
            convertItem = (ITypeConvertItem<S, T>) findConvertPath(tciKey);
            if (convertItem == null) {
                throw new UnsupportedOperationException(
                        MessageFormat.format("Failed to toTarget {0} to {1}. No direct path and indirect path.", source, targetType));
            }
            convertItemMap.put(tciKey, convertItem);
        }
        return convertItem.toTarget(source);
    }

    private <S, T> ITypeConvertItem<S, T> findConvertPath(TciKey<S, T> tciKey) {
        List<ITypeConvertItem<?, ?>> items = sourceTciMap.get(tciKey.getSourceType());
        if (items == null || items.isEmpty()) {
            return null;
        }
        List<PathInfo> pathInfos = ListUtils.map(items, PathInfo::new);
        for (int level = 1 ;!pathInfos.isEmpty(); ++level) {
            List<PathInfo> newPathInfos = new ArrayList<>(pathInfos.size() * 2);
            for (PathInfo pathInfo : pathInfos) {
                int pathInfoLength = pathInfo.path.size();
                if (pathInfoLength == level) {
                    if (pathInfo.getLastType() == tciKey.targetType) {
                        return pathInfo.createTci(tciKey);
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

    private <S, T> void findMorePathInfo(PathInfo pathInfo, List<PathInfo> newPathInfos) {
        List<ITypeConvertItem<?, ?>> tciList = sourceTciMap.get(pathInfo.getLastType());
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

        public <S, T> ITypeConvertItem<S, T> createTci(TciKey<S, T> tciKey) {
            return new IndirectTci<S, T>(tciKey.getSourceType(), tciKey.getTargetType(), path);
        }
    }
}
