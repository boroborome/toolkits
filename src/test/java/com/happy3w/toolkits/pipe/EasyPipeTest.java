package com.happy3w.toolkits.pipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EasyPipeTest {
    @Test
    public void should_happy_pass() {
        List<String> big = new ArrayList<>();
        List<String> small = new ArrayList<>();
        List<String> pass = new ArrayList<>();
        List<String> fail = new ArrayList<>();

        IEasyPipe<Student> scorePipe = EasyPipe.<Student, Boolean>classify(s -> s.getScore() > 60)
                .pipe(Boolean.TRUE,
                        EasyPipe.<Student>visit(s -> System.out.println(s.getName()))
                                .forEach(s -> pass.add(s.getName())))
                .pipe(Boolean.FALSE,
                        EasyPipe.<Student>visit(s -> System.out.println(s.getName()))
                                .forEach(s -> fail.add(s.getName())))
                .build();
        IEasyPipe<Student> agePipe = EasyPipe.<Student, Boolean>classify(s -> s.getAge() > 18)
                .pipe(Boolean.TRUE, EasyPipe.<Student>visit(s -> System.out.println(s.getName()))
                        .peek(s -> big.add(s.getName()))
                        .next(scorePipe))
                .pipe(Boolean.FALSE, EasyPipe.<Student>visit(s -> System.out.println(s.getName()))
                        .peek(s -> small.add(s.getName()))
                        .next(scorePipe))
                .build();
        agePipe.accept(Arrays.asList(new Student("Tom", 10, 50, null),
                new Student("Jim", 10, 70, null),
                new Student("Jerry", 28, 70, null),
                new Student("Lucy", 28, 50, null)));
        agePipe.flush();

        Assert.assertArrayEquals(new String[]{"Jerry", "Lucy"}, big.toArray());
        Assert.assertArrayEquals(new String[]{"Tom", "Jim"}, small.toArray());
        Assert.assertArrayEquals(new String[]{"Jim", "Jerry"}, pass.toArray());
        Assert.assertArrayEquals(new String[]{"Tom", "Lucy"}, fail.toArray());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Student {
        private String name;
        private int age;
        private int score;
        private String remark;

        public void appendRemark(String remark) {
            if (this.remark == null) {
                this.remark = remark;
            } else {
                this.remark = this.remark + ";" + remark;
            }
        }
    }
}
