package ru.shift.view.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ru.shift.data.ConnectionData;

public class ConnectionDialog {

   public ConnectionData askConnectionData() {
      while (true) {
         //TODO: очистить поля перед продажей чата
         JTextField ipField = new JTextField("localhost");
         JTextField portField = new JTextField("8899");

         JPanel panel = new JPanel();
         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
         panel.add(new JLabel("Server IP:"));
         panel.add(ipField);
         panel.add(new JLabel("Port:"));
         panel.add(portField);

         int result = JOptionPane.showConfirmDialog(null, panel, "Connect to Server",
             JOptionPane.OK_CANCEL_OPTION);

         if (result != JOptionPane.OK_OPTION) {
            return null;
         }

         String ip = ipField.getText().trim();
         int port;

         try {
            port = Integer.parseInt(portField.getText().trim());
            if (port < 1 || 65535 < port) {
               showError("Порт должен быть от 1 до 65535.");
               continue;
            }
         } catch (NumberFormatException e) {
            showError("Порт должен быть числом.");
            continue;
         }
         return new ConnectionData(ip, port);
      }


   }

   public void showError(String message) {
      JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
   }
}
