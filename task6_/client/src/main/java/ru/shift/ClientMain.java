package ru.shift;

import ru.shift.controller.ChatController;
import ru.shift.data.ConnectionData;
import ru.shift.model.ChatModel;
import ru.shift.view.ChatClientUI;
import ru.shift.view.ConnectionDialog;

import javax.swing.*;

public class ClientMain {
    public static final int PORT = 8899;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConnectionDialog dialog = new ConnectionDialog(); // view
            ConnectionData data = dialog.askConnectionData(); // ip, port, name
            if (data == null) return;

            ChatModel model = new ChatModel();
            ChatClientUI view = new ChatClientUI();
            ChatController controller = new ChatController(data, model, view);
            controller.start();
        });
    }
}
