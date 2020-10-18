package com.happy3w.toolkits.point24;

import org.junit.Test;

import java.text.MessageFormat;

public class Point24Playground {
    @Test
    public void test_operFlowStrategy() {
        Point24Strategy point24Strategy = new OperFlowStrategy();
        long resultCount = point24Strategy.judge(new int[]{1, 2, 3, 4, 5, 6})
                .peek(exp -> System.out.println(exp))
                .count();
        System.out.println(MessageFormat.format("Total case count:{0}", point24Strategy.getJudgedExpCount()));
        System.out.println(MessageFormat.format("Total result count:{0}", resultCount));
    }
}
