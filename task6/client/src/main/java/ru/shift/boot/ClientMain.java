package ru.shift.boot;

import java.io.IOException;
import javax.swing.SwingUtilities;
import ru.shift.controller.ChatController;
import ru.shift.controller.NetworkService;
import ru.shift.data.ConnectionData;
import ru.shift.model.ChatModel;
import ru.shift.view.ui.ChatClientUI;
import ru.shift.view.ui.ConnectionDialog;

public class ClientMain {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         NetworkService net = connect();
         if (net == null) {
            return;
         }

         ChatModel model = new ChatModel();
         ChatClientUI view = new ChatClientUI();
         ChatController controller = new ChatController(model, view, net);

         controller.start();


      });

   }

   private static NetworkService connect() {
      while (true) {
         ConnectionDialog dialog = new ConnectionDialog();
         ConnectionData data = dialog.askConnectionData();
         if (data == null) {
            return null;
         }

         try {
            NetworkService.testConnection(data.ip(), data.port());
            return new NetworkService(data);
         } catch (IOException e) {
            dialog.showError("Не удалось подключиться к серверу: " + e.getMessage());
         }
      }
   }


}
