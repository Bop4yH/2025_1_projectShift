package ru.shift;

import java.time.Instant;

public class Message {
    private MessageType type;
    private Object data;
    private long timestamp;

    public Message() {}  // для Jackson

    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
