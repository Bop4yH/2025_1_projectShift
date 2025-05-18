package ru.shift.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;

public class Message {
    private MessageType type;
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = PlainText.class, name = "plain"),
        @JsonSubTypes.Type(value = ChatMessageData.class, name = "chat"),
        @JsonSubTypes.Type(value = UsersData.class, name = "users")
    })
    private MessageData data;
    private long timestamp;

    public Message() {}  // для Jackson

   private Message(MessageType type, MessageData data) {
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

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
