package ru.shift;

import ru.shift.view.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class ChatClientLauncher {

    public static void launch() {
        String ip = askForIp();
        if (ip == null) return;

        Integer port = askForPort();
        if (port == null) return;


        Socket socket = connectToServer(ip, port);
        if (socket == null) return;

        String name = askForName();
        if (name == null || name.isBlank()) {
            closeSocket(socket);
            return;
        }

        startChatUI(socket, name, ip, port);
    }

    private static String askForIp() {
        return JOptionPane.showInputDialog(null, "Enter server IP:", "127.0.0.1");
    }

    private static Integer askForPort() {
        String portStr = JOptionPane.showInputDialog(null, "Enter server port:", "12345");
        if (portStr == null) return null;

        try {
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            showError("Invalid port number.");
            return null;
        }
    }

    private static Socket connectToServer(String ip, int port) {
        try {
            return new Socket(ip, port);
        } catch (IOException e) {
            showError("Can't connect to server: " + e.getMessage());
            return null;
        }
    }

    private static String askForName() {
        return JOptionPane.showInputDialog(null, "Enter your name:");
    }

    private static void startChatUI(Socket socket, String name, String ip, int port) {
        MainWindow ui = new MainWindow();
        ui.appendMessage("Connected to " + ip + ":" + port);
        ui.appendMessage("Your name: " + name);

        // Здесь пока просто демонстрация. Свяжи со своим клиентом:
        // ChatClient client = new ChatClient(socket, name, ui);
        // client.start();
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
