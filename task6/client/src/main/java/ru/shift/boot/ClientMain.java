package ru.shift.boot;

import javax.swing.SwingUtilities;
import ru.shift.controller.ChatController;
import ru.shift.data.ConnectionData;
import ru.shift.model.ChatModel;
import ru.shift.view.ui.ChatClientUI;
import ru.shift.view.ui.ConnectionDialog;

public class ClientMain {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         ChatModel model = new ChatModel();
         ChatClientUI view = new ChatClientUI();
         ChatController controller = new ChatController(model, view);

         while (true) {
            ConnectionDialog dialog = new ConnectionDialog();
            ConnectionData data = dialog.askConnectionData();
            if (data == null) {
               controller.close();
               return;
            }

            if (controller.attemptConnection(data)) {
               break;
            }
         }
      });

   }
}
