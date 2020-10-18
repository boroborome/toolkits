package com.happy3w.toolkits.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayUtils {
    public static boolean[] toBooleanArray(List<Boolean> list) {
        boolean[] bs = new boolean[list.size()];

        Iterator<Boolean> it = list.iterator();
        for (int i = 0; it.hasNext(); i++) {
            bs[i] = it.next();
        }
        return bs;
    }

    public static List<Boolean> toBooleanList(boolean[] bs) {
        List<Boolean> list = new ArrayList<>(bs.length);
        for (boolean b : bs) {
            list.add(b);
        }
        return list;
    }

    public static Integer[] boxInt(int[] array) {
        Object boxArray = Array.newInstance(Integer.class, array.length);
        for (int i = 0; i < array.length; i++) {
            Array.set(boxArray, i,
                    Array.get(array, i));
        }
        return (Integer[]) boxArray;
    }

    public static int[] unboxInt(Integer[] array) {
        Object boxArray = Array.newInstance(int.class, array.length);
        for (int i = 0; i < array.length; i++) {
            Array.set(boxArray, i,
                    Array.get(array, i));
        }
        return (int[]) boxArray;
    }
}
