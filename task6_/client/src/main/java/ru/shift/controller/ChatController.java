package ru.shift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shift.*;
import ru.shift.MessageUtils;
import ru.shift.data.ConnectionData;
import ru.shift.model.ChatModel;
import ru.shift.view.ChatClientUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import java.util.List;

public class ChatController implements AutoCloseable {
    private final ChatModel model;
    private final ChatClientUI view;
    private final NetworkService net;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatController(ConnectionData cd,ChatModel model, ChatClientUI view) {
        this.model = model;
        this.view = view;
        this.net   = new NetworkService(cd);
    }

    public void start() {
        /* подписываем UI на модель */
        model.addListener(new ChatModel.ChatListener() {
            public void onNewMessage(Message m) {
                SwingUtilities.invokeLater(() -> view.appendMessage(m));
            }
            public void onUserListUpdate(List<String> u) {
                SwingUtilities.invokeLater(() -> view.updateUserList(u));
            }
        });

        /* подписываем контроллер на сеть */
        net.addListener(new NetworkService.NetworkListener() {
            public void onRawMessage(String json) { handleIncoming(json); }
            public void onError(Throwable t)       { showError(t); }
        });

        /* реакции UI-кнопок */
        view.addSendListener(e -> sendText());

        net.start(); // поехали
    }

    /* ui -> controller */
    private void sendText() {
        String text = view.getInputText();
        if (text.isBlank()) return;

        Message msg = new Message(MessageType.TEXT, text);
        try {
            net.send(msg);
        } catch (IOException ex) {
            showError(ex);
        }
    }

    /* network -> controller -> model */
    private void handleIncoming(String json) {
        try {
            Message msg = mapper.readValue(json, Message.class);
            switch (msg.getType()) {
                case TEXT -> model.fireMessage(msg);
                case USERS -> {
                    UsersData data = mapper.convertValue(msg.getData(), UsersData.class);
                    model.fireUserList(data.getUsers());
                }
                case ENTER_NAME -> askNameLoop(null);
                case NAME_EMPTY -> askNameLoop("Имя не может быть пустым.");
                case NAME_TAKEN -> askNameLoop("Имя уже занято.");
                default -> System.err.println("Unknown: " + msg.getType());
            }
        } catch (Exception e) { showError(e); }
    }

    private void askNameLoop(String error) {
        if (error != null) view.showNameError(error);
        String name = view.askName();
        try {
            net.send(new Message(MessageType.USER_NAME, name));
        } catch (IOException e) { showError(e); }
    }

    private void showError(Throwable t) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(view, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
    }

    @Override public void close() throws Exception { net.close(); }
}



