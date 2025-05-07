package ru.shift.model;

import ru.shift.Message;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatModel {

    public interface ChatListener {
        void onNewMessage(Message message);
        void onUserListUpdate(List<String> users);
        //void fireError(List<String> users);
    }

    private final List<ChatListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(ChatListener listener) {
        listeners.add(listener);
    }

    public void fireMessage(Message message) {
        for (ChatListener l : listeners) {
            l.onNewMessage(message);
        }
    }

    public void fireUserList(List<String> users) {
        for (ChatListener l : listeners) {
            l.onUserListUpdate(users);
        }
    }
}
