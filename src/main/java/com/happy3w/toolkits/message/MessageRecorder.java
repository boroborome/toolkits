package com.happy3w.toolkits.message;

import com.happy3w.toolkits.utils.ListUtils;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MessageRecorder {
    /**
     * Record messages in each level
     */
    private Map<String, LevelInfo> levelInfos = new HashMap<>();

    /**
     * Message will formatted by this formatter
     */
    private MessageFormatter formatter;

    /**
     * If some level in this list and recorder contains any message in this level then isSuccess will be false.
     */
    private List<String> failedLevels = new ArrayList<>();

    public MessageRecorder() {
        this(new String[]{MessageType.ERROR});
    }

    public MessageRecorder(String[] failedLevels) {
        this.failedLevels.addAll(Arrays.asList(failedLevels));
    }

    public MessageFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(MessageFormatter formatter) {
        this.formatter = formatter;
    }

    public void appendDistinctError(String messageKey, String format, Object... parameters) {
        appendMessage(MessageType.ERROR, messageKey, format, parameters);
    }

    public void appendError(String format, Object... parameters) {
        appendMessage(MessageType.ERROR, null, format, parameters);
    }

    public void appendDistinctWarning(String messageKey, String format, Object... parameters) {
        appendMessage(MessageType.WARNING, messageKey, format, parameters);
    }

    public void appendWarning(String format, Object... parameters) {
        appendMessage(MessageType.WARNING, null, format, parameters);
    }

    public void appendMessage(String messageType, String messageKey, String format, Object... parameters) {
        LevelInfo info = levelInfos.computeIfAbsent(messageType, key -> new LevelInfo(key));

        if (messageKey != null) {
            if (info.messageKeys.contains(messageKey)) {
                return;
            }
            info.messageKeys.add(messageKey);
        }
        String message = MessageFormat.format(format, parameters);
        if (formatter != null) {
            message = formatter.format(message);
        }
        info.messages.add(message);
    }

    public List<String> getMessage(String messageType) {
        LevelInfo info = levelInfos.get(messageType);
        return info == null ? Collections.emptyList() : info.messages;
    }

    public List<String> getErrors() {
        return getMessage(MessageType.ERROR);
    }

    public List<String> getWarnings() {
        return getMessage(MessageType.WARNING);
    }

    public boolean isSuccess() {
        Set<String> levelKeys = levelInfos.keySet();
        for (String failedLevel : failedLevels) {
            if (levelKeys.contains(failedLevel)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsError(String error) {
        List<String> errors = getMessage(MessageType.ERROR);
        if (errors.isEmpty()) {
            return false;
        }
        for (String message : errors) {
            if (Objects.equals(error, message)) {
                return true;
            }
        }
        return false;
    }

    public static MessageRecorder errorExceptionRecorder() {
        return new ErrorExceptionRecorder();
    }

    private static class ErrorExceptionRecorder extends MessageRecorder {
        @Override
        public void appendMessage(String messageType, String errorKey, String format, Object... parameters) {
            super.appendMessage(messageType, errorKey, format, parameters);
            if (MessageType.ERROR.equals(messageType)) {
                throw new MessageRecorderException(this.getErrors().get(0));
            }
        }
    }

    public void reset() {
        levelInfos.clear();
    }

    public Map<String, List<String>> toMessageResponse() {
        return ListUtils.toMap(levelInfos.values(), LevelInfo::getMessageType, LevelInfo::getMessages);
    }

    public void appendMessages(Map<String, List<String>> messageResponse) {
        for (Map.Entry<String, List<String>> messageEntry : messageResponse.entrySet()) {
            levelInfos.computeIfAbsent(messageEntry.getKey(), key -> new LevelInfo(key))
                    .getMessages()
                    .addAll(messageEntry.getValue());
        }
     }

    public MessageFilter startErrorFilter() {
        return new MessageFilter(this, MessageType.ERROR);
    }

    public MessageFilter startMessageFilter(String messageType) {
        return new MessageFilter(this, messageType);
    }

    public interface MessageFormatter {
        String format(String message);
    }

    @Getter
    private static class LevelInfo {
        private String messageType;
        private List<String> messages = new ArrayList<>();
        private Set<String> messageKeys = new HashSet<>();

        public LevelInfo(String messageType) {
            this.messageType = messageType;
        }
    }
}
