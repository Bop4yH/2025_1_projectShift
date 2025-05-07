package ru.shift.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {

    private JTextArea chatArea;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JTextField messageField;
    private JButton sendButton;

    public MainWindow() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Центрирование окна

        // Основной layout
        setLayout(new BorderLayout());

        // ===== Центр: поле чата =====
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        add(chatScroll, BorderLayout.CENTER);

        // ===== Справа: список пользователей =====
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setPreferredSize(new Dimension(150, 0));
        add(userScroll, BorderLayout.EAST);

        // ===== Снизу: поле ввода и кнопка =====
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== Методы взаимодействия с UI =====

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    public void setUserList(List<String> users) {
        userListModel.clear();
        for (String user : users) {
            userListModel.addElement(user);
        }
    }

    public String getMessageText() {
        return messageField.getText();
    }

    public void clearMessageField() {
        messageField.setText("");
    }

    public void addSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public void addMessageFieldListener(ActionListener listener) {
        messageField.addActionListener(listener); // Нажатие Enter внутри поля
    }
}
