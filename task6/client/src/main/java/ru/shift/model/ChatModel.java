package ru.shift.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ru.shift.common.Message;

public class ChatModel {

   private final List<ChatListener> listeners = new CopyOnWriteArrayList<>();

   public void addListener(ChatListener listener) {
      listeners.add(listener);
   }

   public void notifyMessageReceived(Message message) {
      for (ChatListener l : listeners) {
         l.onNewMessage(message);
      }
   }

   public void notifyUserListReceived(List<String> users) {
      for (ChatListener l : listeners) {
         l.onUserListUpdate(users);
      }
   }

   public interface ChatListener {

      void onNewMessage(Message message);

      void onUserListUpdate(List<String> users);
   }
}
