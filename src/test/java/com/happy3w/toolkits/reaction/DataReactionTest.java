package com.happy3w.toolkits.reaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DataReactionTest {
    private static Map<String, List<String>> stubDataMap = new HashMap<>();
    static {
        stubDataMap.put("M6", Arrays.asList("M5", "M4", "P5", "P4", "T5", "T4"));
        stubDataMap.put("M5", Arrays.asList("M6", "P51", "P52", "T51", "T52"));
        stubDataMap.put("P5", Arrays.asList("P51", "P42", "T41", "T42"));
    }

    @Test
    public void should_success() {
        String methodCode = "M6";
        String callStack = new MyBuilder()
                .initByMethods(Arrays.asList(methodCode))
                .buildData();
        Assertions.assertEquals("M4,M5,M6,P4,P42,P5,P51,P52,T4,T41,T42,T5,T51,T52", callStack);
    }

    public static class MyBuilder {
        private final String Methods = "method";
        private final String Procedures = "procedure";

        private DataReaction dataReaction;
        private Set<String> allNodes = new HashSet<>();

        public MyBuilder() {
            dataReaction = new DataReaction()
                    .withBasket(Methods, this::processByMethods)
                    .withBasket(Procedures, this::processByProcedures);
        }

        public void processByMethods(Set<String> ids, DataReaction reaction) {
            List<String> newMethods = queryRefMethod(ids);
            acceptNode(newMethods);
            reaction.acceptDatas(Methods, newMethods);

            List<String> newProcedures = queryRefProcedure(ids);
            acceptNode(newProcedures);
            reaction.acceptDatas(Procedures, newProcedures);

            List<String> newTables = queryRefTables(ids);
            acceptNode(newTables);
        }

        private List<String> queryRefTables(Set<String> ids) {
            return queryRef(ids, "T");
        }

        private List<String> queryRef(Set<String> ids, String resPrefix) {
            return ids.stream()
                    .flatMap(id -> {
                        List<String> list = stubDataMap.get(id);
                        return list == null ? Stream.empty() : list.stream();
                    })
                    .filter(code -> code.startsWith(resPrefix))
                    .collect(Collectors.toList());
        }

        private void acceptNode(Collection<String> nodes) {
            allNodes.addAll(nodes);
        }

        private List<String> queryRefMethod(Set<String> ids) {
            return queryRef(ids, "M");
        }

        private List<String> queryRefProcedure(Set<String> ids) {
            return queryRef(ids, "P");
        }

        public void processByProcedures(Set<String> ids, DataReaction reaction) {
            List<String> newProcedures = queryRefProcedure(ids);
            acceptNode(newProcedures);
            reaction.acceptDatas(Procedures, newProcedures);

            List<String> newTables = queryRefTables(ids);
            acceptNode(newTables);
        }

        public MyBuilder initByMethods(List<String> methodCodes) {
            dataReaction.acceptDatas(Methods, methodCodes);
            return this;
        }

        public String buildData() {
            dataReaction.react();
            return allNodes.stream().sorted().collect(Collectors.joining(","));
        }
    }

}
