package com.happy3w.toolkits.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {
    public static <T, V> V findValueFirstMatch(Collection<T> list, Function<T, V> fieldGetter, Predicate<V> predicate) {
        if (isEmpty(list)) {
            return null;
        }

        for (T item : list) {
            if (item == null) {
                continue;
            }
            V v = fieldGetter.apply(item);
            if (predicate.test(v)) {
                return v;
            }
        }
        return null;
    }

    public static <T, V> T findFirstMatch(Collection<T> list, Function<T, V> fieldGetter, V expectValue) {
        if (isEmpty(list)) {
            return null;
        }

        for (T item : list) {
            if (Objects.equals(fieldGetter.apply(item), expectValue)) {
                return item;
            }
        }
        return null;
    }

    public static <T> Optional<T> findFirstMatch(Iterable<T> iterable, Predicate<? super T> predicate) {

        for (T element : iterable) {
            if (predicate.test(element)) {
                return Optional.of(element);
            }
        }

        return Optional.empty();
    }

    public static <T, V extends Comparable> T maxItem(Collection<T> list, Function<T, V> fieldGetter) {
        T max = null;
        V maxValue = null;
        for (T t : list) {
            V v = fieldGetter.apply(t);
            if (v == null) {
                continue;
            }
            if (max == null || v.compareTo(maxValue) > 0) {
                max = t;
                maxValue = v;
            }
        }
        return max;
    }

    public static <T, R> Set<R> mapSet(Iterable<T> iterable, Function<? super T, ? extends R> mapper) {
        Set<R> result = new HashSet<>();
        for (T element : iterable) {
            result.add(mapper.apply(element));
        }
        return result;
    }

    public static <T, R> List<R> map(Iterable<T> iterable, Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();

        iterable.forEach(element -> result.add(mapper.apply(element)));

        return result;
    }

    public static <T> List<T> filter(T[] arrays, Predicate<T> predicate) {
        return filter(Arrays.asList(arrays), predicate);
    }

    public static <T> List<T> filter(Iterable<T> iterable, Predicate<T> predicate) {
        List result = new ArrayList();

        for (T element : iterable) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }

        return result;
    }

    public static <T> boolean anyMatch(Iterable<T> elements, Predicate<? super T> predicate) {
        for (T element : elements) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
    }

    public static <T, K> Map<K, List<T>> groupingBy(
            Iterable<T> iterable,
            Function<? super T, ? extends K> classifier) {
        return groupingBy(iterable, classifier, e -> e);
    }

    public static <T, K, V> Map<K, List<V>> groupingBy(
            Iterable<T> iterable,
            Function<? super T, ? extends K> classifier,
            Function<T, V> valueMapper) {
        return groupingBy(iterable, classifier, valueMapper, null);
    }

    public static <T, K, V> Map<K, List<V>> groupingBy(
            Iterable<T> iterable,
            Function<? super T, ? extends K> classifier,
            Function<T, V> valueMapper,
            Predicate<T> filter) {
        Map<K, List<V>> map = new HashMap<>();

        for (T element : iterable) {
            if (filter != null && !filter.test(element)) {
                continue;
            }

            List<V> list = MapUtils.safeRead(map, classifier.apply(element), () -> new ArrayList<>());
            list.add(valueMapper.apply(element));
        }

        return map;
    }

    public static <K, T> Map<K, T> toMap(Collection<T> list, Function<T, K> keyGenerator) {
        return toMap(list, keyGenerator, e -> e);
    }

    public static <K, V, T> Map<K, V> toMap(Collection<T> list, Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, V> map = new HashMap<>();
        if (list != null) {
            for (T item : list) {
                map.put(keyGenerator.apply(item), valueGenerator.apply(item));
            }
        }
        return map;
    }

    public static <T> List<T> fixSizeList(int size, T emptyValue, T... initValues) {
        List<T> lst = new ArrayList<>(size);
        if (initValues != null) {
            for (T v : initValues) {
                lst.add(v);
            }
        }
        while (lst.size() < size) {
            lst.add(emptyValue);
        }
        return lst;
    }

    public static <T> List<T> subList(List<T> data, int startIndex, int size) {
        if (isEmpty(data)) {
            return data;
        }

        if (startIndex > data.size()) {
            return Collections.EMPTY_LIST;
        }

        int toIndex = startIndex + size;
        if (size < 0 || toIndex > data.size()) {
            toIndex = data.size();
        }
        return data.subList(startIndex, toIndex);
    }

    public static <T> List<T> newList(int size, T defaultValue) {
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(defaultValue);
        }
        return list;
    }

    public static <T> List<T> newList(Collection<T> initData, T... newDatas) {
        List<T> list = new ArrayList<>(initData);
        for (T t : newDatas) {
            list.add(t);
        }
        return list;
    }

    public static String encodeToString(Collection collection) {
        StringBuilder buffer = new StringBuilder();

        for (Object v : collection) {
            if (buffer.length() > 0) {
                buffer.append(',');
            }
            buffer.append(v);
        }
        return buffer.toString();
    }

    public static List<String> decodeWithString(String listString) {
        if (StringUtils.isEmpty(listString)) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        for (String v : listString.split(",")) {
            list.add(v);
        }
        return list;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> int indexOf(List<T> values, T value, BiPredicate<T, T> equalChecker) {
        for (int index = values.size() - 1; index >= 0; --index) {
            T existValue = values.get(index);
            if (equalChecker.test(existValue, value)) {
                return index;
            }
        }
        return -1;
    }


}
