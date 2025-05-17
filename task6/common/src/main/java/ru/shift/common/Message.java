package ru.shift.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;

public class Message {

   private MessageType type;

   @JsonTypeInfo(
       use = JsonTypeInfo.Id.NAME,
       include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
       property = "type"
   )
   @JsonSubTypes({
       @JsonSubTypes.Type(value = UserName.class, name = "USER_NAME"),
       @JsonSubTypes.Type(value = PlainText.class, name = "PLAIN_TEXT"),
       @JsonSubTypes.Type(value = ChatMessageData.class, name = "CHAT_MESSAGE"),
       @JsonSubTypes.Type(value = UsersData.class, name = "USERS")
   })
   private MessageData data;
   private long timestamp;

   public Message() {
   }  // для Jackson

   public Message(MessageType type, MessageData data) {
      this.type = type;
      this.data = data;
      this.timestamp = Instant.now().toEpochMilli();
   }

   public static Message createChatMessage(ChatMessageData chatData) {
      return new Message(MessageType.CHAT_MESSAGE, chatData);
   }

   public static Message createUsersDataMessage(UsersData usersData) {
      return new Message(MessageType.USERS, usersData);
   }

   public static Message createUserNameMessage(UserName userName) {
      return new Message(MessageType.USER_NAME, userName);
   }

   public static Message createPlainTextMessage(PlainText plainText) {
      return new Message(MessageType.PLAIN_TEXT, plainText);
   }

   public static Message newSignal(MessageType type) {
      return new Message(type, null);
   }

   public MessageType getType() {
      return type;
   }

   public MessageData getData() {
      return data;
   }

   public long getTimestamp() {
      return timestamp;
   }
}
