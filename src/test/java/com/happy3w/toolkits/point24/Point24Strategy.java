package com.happy3w.toolkits.point24;

import java.util.stream.Stream;

public interface Point24Strategy {
    Stream<String> judge(int[] numbers);
    long getJudgedExpCount();
}
