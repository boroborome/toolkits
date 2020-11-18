package com.happy3w.toolkits.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MessageResponse<T> {
    private T data;
    private Map<String, List<String>> msgs;

    public MessageResponse(T data, Map<String, List<String>> msgs) {
        this.data = data;
        this.msgs = msgs;
    }

    public static <T> MessageResponse<T> fromData(T data) {
        return new MessageResponse<>(data, null);
    }

    public static <T> MessageResponse<T> fromMsgs(Map<String, List<String>> msgs) {
        return new MessageResponse<>(null, msgs);
    }

    public static <T> MessageResponse<T> fromError(String error) {
        Map<String, List<String>> msgs = new HashMap<>();
        msgs.put(MessageType.ERROR, Arrays.asList(error));
        return new MessageResponse<>(null, msgs);
    }
}
