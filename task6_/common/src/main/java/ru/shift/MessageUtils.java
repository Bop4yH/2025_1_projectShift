package ru.shift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.PrintWriter;
import java.time.Instant;

public class MessageUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendMessage(PrintWriter writer, MessageType type, Object data) {
        Message msg = new Message(type, data);
        msg.setTimestamp(Instant.now().toEpochMilli());
        sendMessage(writer, msg);
    }

    public static void sendMessage(PrintWriter writer, MessageType type) {
        sendMessage(writer, type, "");
    }


    public static void sendMessage(PrintWriter writer, Message msg) {
        try {
            String json = mapper.writeValueAsString(msg);
            writer.println(json);
            System.out.println("Sending JSON: " + json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации JSON", e);
        }
    }
}
