package ru.shift.view.format;

import java.awt.Color;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import ru.shift.common.ChatMessageData;
import ru.shift.common.Message;

public class ChatMessageFormatter {

   private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss")
       .withZone(ZoneId.systemDefault());

   private ChatMessageFormatter() {
   }

   public static ChatMessageView format(Message message) {
      ChatMessageData data = (ChatMessageData) message.getData();
      String time = TIME_FORMATTER.format(Instant.ofEpochMilli(message.getTimestamp()));
      SimpleAttributeSet style = new SimpleAttributeSet();

      if ("Server".equalsIgnoreCase(data.sender())) {
         StyleConstants.setForeground(style, Color.BLUE);
         StyleConstants.setBold(style, true);
      } else {
         StyleConstants.setForeground(style, Color.BLACK);
      }

      return new ChatMessageView(data.sender(), data.text(), time, style);
   }


}
