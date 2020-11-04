package com.happy3w.toolkits.message;

import java.util.List;

public class MessageFilter {
    private final MessageRecorder messageRecorder;
    private final String messageType;
    private final int startMessageSize;

    public MessageFilter(MessageRecorder messageRecorder, String messageType) {
        this.messageRecorder = messageRecorder;
        this.messageType = messageType;
        this.startMessageSize = messageRecorder.getMessage(messageType).size();
    }

    public String getMessage() {
        List<String> messageList = messageRecorder.getMessage(messageType);
        int newMessageSize = messageList.size();

        if (newMessageSize == startMessageSize) {
            return null;
        } else if (newMessageSize == startMessageSize + 1) {
            return messageList.get(startMessageSize);
        } else {
            List<String> periodMessages = messageList.subList(startMessageSize, newMessageSize);
            return String.join(";", periodMessages);
        }
    }
}
