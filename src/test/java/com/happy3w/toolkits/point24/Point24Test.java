package com.happy3w.toolkits.point24;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.MessageFormat;

@RunWith(Parameterized.class)
public class Point24Test {

    @Parameterized.Parameter(0)
    public Point24Strategy point24Strategy;

    @Parameterized.Parameters(name = "server [{0}]")
    public static Object[][] arguments() {
        return new Object[][] {
                {new OperFlowStrategy()},
                {new BinTreeStrategy()},
        };
    }

    private void testNumber(int[] numbers, int expectResultCount) {
        long resultCount = point24Strategy.judge(numbers)
//                .peek(exp -> System.out.println(exp))
                .count();
        System.out.println(MessageFormat.format("Total case count:{0}", point24Strategy.getJudgedExpCount()));
        System.out.println(MessageFormat.format("Total result count:{0}", resultCount));
        Assert.assertEquals(expectResultCount, resultCount);
    }

    @Test
    public void should_judge_normal_success() {
        testNumber(new int[]{1, 7, 4, 8}, 15);
    }

    @Test
    public void should_judge_all_same_success() {
        testNumber(new int[]{6, 6, 6, 6}, 4);
    }
}
