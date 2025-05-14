package ru.shift.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtils {

   private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);
   private static final ObjectMapper mapper = new ObjectMapper();

   private MessageUtils() {
   }

   public static void sendMessage(PrintWriter writer, MessageType type, MessageData data) {
      Message msg = new Message(type, data);
      msg.setTimestamp(Instant.now().toEpochMilli());
      sendMessage(writer, msg);
   }

   public static void sendMessage(PrintWriter writer, MessageType type) {
      sendMessage(writer, type, new PlainText(""));
   }

   public static void sendMessage(PrintWriter writer, Message msg) {
      try {
         String json = mapper.writeValueAsString(msg);
         writer.println(json);
         writer.flush();
         log.info("Message sent: {}", json);
      } catch (JsonProcessingException e) {
         log.error("Error serializing JSON: {}", msg, e);
      }
   }

   public static ObjectMapper mapper() {
      return mapper;
   }

}
