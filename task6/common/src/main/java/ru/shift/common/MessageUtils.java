package ru.shift.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {

   private static final ObjectMapper mapper = new ObjectMapper();

   private MessageUtils() {
   }

   public static String serialize(Message msg) throws JsonProcessingException {
      return mapper.writeValueAsString(msg);
   }

   public static Message deserialize(String json) throws JsonProcessingException {
      return mapper.readValue(json, Message.class);
   }
}
