package com.happy3w.toolkits.utils;

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
}
