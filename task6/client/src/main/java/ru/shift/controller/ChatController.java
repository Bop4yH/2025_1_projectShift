package ru.shift.controller;



import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.common.Message;
import ru.shift.common.MessageType;
import ru.shift.common.MessageUtils;
import ru.shift.common.UsersData;
import ru.shift.model.ChatModel;
import ru.shift.view.format.ChatMessageFormatter;
import ru.shift.view.format.ChatMessageView;
import ru.shift.view.ui.ChatClientUI;


public class ChatController implements AutoCloseable {
   private static final Logger log = LoggerFactory.getLogger(ChatController.class);
   private final ChatModel model;
   private final ChatClientUI view;
   private final NetworkService net;


   public ChatController(ChatModel model, ChatClientUI view, NetworkService net) {
      this.model = model;
      this.view = view;
      this.net = net;
   }

   public void start() {
      wireUi();
      wireModel();
      wireNetwork();
      net.start();
   }

   private void wireModel() {
      model.addListener(new ChatModel.ChatListener() {
         public void onNewMessage(Message msg) {
            ChatMessageView line = ChatMessageFormatter.format(msg);
            SwingUtilities.invokeLater(() -> view.appendChatLine(line));
         }

         public void onUserListUpdate(List<String> users) {
            SwingUtilities.invokeLater(() -> view.updateUserList(users));
         }
      });
   }

   private void wireNetwork() {
      net.addListener(new NetworkService.NetListener() {
         public void onRawMessage(String json) {
            handleIncoming(json);
         }

         public void onError(Throwable t) {
            showError(t);
            try {
               close();


            } catch (Exception ex) {
               log.error("Error with connector closing after disconnect", ex);
            }
         }
      });
   }

   private void wireUi() {
      view.addSendListener(e -> sendText());
      view.onClose(() -> {
         try {
            close();
         } catch (Exception ex) {
            log.error("Controller close error", ex);
         }
      });
   }

   private void sendText() {
      String text = view.pollInputText();
      if (text.isBlank()) {
         return;
      }
      try {
         net.send(new Message(MessageType.TEXT, text));
      } catch (IOException ex) {
         showError(ex);
      }
   }

   private void handleIncoming(String json) {
      try {
         Message msg = MessageUtils.mapper().readValue(json, Message.class);
         switch (msg.getType()) {
            case TEXT -> model.notifyMessageReceived(msg);
            case USERS -> {
               UsersData data = MessageUtils.mapper().convertValue(msg.getData(), UsersData.class);
               model.notifyUserListReceived(data.users());
            }
            case ENTER_NAME -> askNameLoop(null);
            case NAME_EMPTY -> askNameLoop("Имя не может быть пустым.");
            case NAME_TAKEN -> askNameLoop("Имя уже занято.");
            default -> log.warn("Unknown message type: {}", msg.getType());
         }
      } catch (Exception e) {
         showError(e);
      }
   }

   private void askNameLoop(String error) {
      if (error != null) {
         view.showError(error);
      }
      String name = view.askName();

      if (name == null) {
         view.shutdown();
         return;
      }

      try {
         net.send(new Message(MessageType.USER_NAME, name));
      } catch (IOException e) {
         showError(e);
      }
   }

   private void showError(Throwable t) {
      log.error("Controller error", t);
      view.showError(t.getMessage());
   }

   @Override
   public void close() {
      net.close();
   }
}
