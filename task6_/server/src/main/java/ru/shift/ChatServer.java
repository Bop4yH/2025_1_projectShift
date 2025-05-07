package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shift.config.Config;
import ru.shift.config.ServerConfig;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatServer {
    public static final Logger log = LoggerFactory.getLogger(ChatServer.class);
    private static final Collection<ClientConnection> clients = new ConcurrentLinkedQueue<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String SERVER_NAME = "Server";

    public static void main(String[] args) {



        ServerConfig config = Config.loadConfig("config.properties")
                .orElseThrow(() -> new IllegalStateException("Can't load config"));
        final int PORT = config.port();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            // Поток для обработки сообщений клиентов
            new Thread(ChatServer::processAllClientInputs).start();

            // Главный поток — принимает новых клиентов
            while (true) {
                acceptNewClient(serverSocket);
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void acceptNewClient(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected: " + socket.getRemoteSocketAddress());

            ClientConnection client = createClient(socket);

            MessageUtils.sendMessage(client.writer, MessageType.ENTER_NAME);
            clients.add(client);

        } catch (IOException e) {
            System.err.println("Failed to accept client: " + e.getMessage());
        }
    }

    private static void processAllClientInputs() {
        while (true) {

            List<ClientConnection> toRemove = new ArrayList<>();

            for (ClientConnection client : clients) {
                try {
                    processClientInput(client, toRemove);
                } catch (Exception e) {
                    System.err.println("Ошибка при обработке клиента " + client + ": " + e.getMessage());
                    e.printStackTrace();
                    toRemove.add(client); // Можно удалить клиента, если он "сломался"
                }
            }

            if (!toRemove.isEmpty()) {
                clients.removeAll(toRemove);
                broadcastUsers();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("Поток был прерван: " + e.getMessage());
                e.printStackTrace();
                // Можно выйти из цикла, если нужно корректно завершить:
                // break;
            }

        }
    }

    private static boolean registerClientName(ClientConnection client, String proposedName) {
        if (proposedName == null || proposedName.isEmpty()) {
            MessageUtils.sendMessage(client.writer, MessageType.NAME_EMPTY);
            return false;
        }

        boolean nameTaken = clients.stream()
                .anyMatch(c -> proposedName.equalsIgnoreCase(c.name));

        if (nameTaken || proposedName.equalsIgnoreCase(SERVER_NAME)) {
            MessageUtils.sendMessage(client.writer, MessageType.NAME_TAKEN);
            return false;
        }

        client.name = proposedName;
        return true;
    }



    private static void processClientInput(ClientConnection client, List<ClientConnection> toRemove) {
        try {
            String line = tryReadLine(client);

            if (line == null) {
                throw new IOException("Client disconnected");
            }

            Message msg = mapper.readValue(line, Message.class);

            switch (msg.getType()) {
                case TEXT:
                    String text = (String) msg.getData();
                    ChatMessageData chatData = new ChatMessageData(client.name, text);
                    msg.setData(chatData); // форматируем сообщение
                    broadcastMessage(msg);
                    break;

                case USER_NAME:
                    if (!client.isRegistered()){
                        String name = (String) msg.getData();
                        if (registerClientName(client, name)) {
                            broadcastUsers();
                            broadcastMessage(createServerMessage(client.name + " joined the chat"));
                        }
                    }
                    break;

                default:
                    System.err.println("Неизвестный тип сообщения: " + msg.getType());
            }

        } catch (SocketTimeoutException ignore) {
            // Нет данных — это нормально
        } catch (IOException e) {
            broadcastMessage(createServerMessage(client.name + " disconnected the chat"));
            System.out.println("Client disconnected: " + client.name);
            toRemove.add(client);
            closeClient(client);
        }
    }


    private static Message createServerMessage(String text) {
        Message msg = new Message();
        msg.setType(MessageType.TEXT);
        ChatMessageData chatData = new ChatMessageData(SERVER_NAME, text);
        msg.setData(chatData);
        msg.setTimestamp(Instant.now().toEpochMilli());
        return msg;
    }

    private static String tryReadLine(ClientConnection client) throws IOException {
        return client.reader.readLine();
    }

    private static void broadcastMessage(Message message) {
        List<ClientConnection> disconnected = new ArrayList<>();

        for (ClientConnection client : clients) {
            if (!client.isRegistered()) continue;

            MessageUtils.sendMessage(client.writer, message);

            if (client.writer.checkError()) {
                System.out.println("Client write error: " + client.name);
                disconnected.add(client);
                closeClient(client);
            }
        }

        if (!disconnected.isEmpty()) {
            clients.removeAll(disconnected);
            broadcastUsers();
        }
    }

    private static void broadcastUsers() {
        UsersData usersData = new UsersData(clients.stream()
                .map(c -> c.name)
                .filter(Objects::nonNull)
                .toList());

        for (ClientConnection client : clients) {
            if (!client.isRegistered()) continue;
            MessageUtils.sendMessage(client.writer, MessageType.USERS, usersData);

        }
    }

    private static void closeClient(ClientConnection client) {
        try {
            client.socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }

    private static ClientConnection createClient(Socket socket) throws IOException {
        socket.setSoTimeout(100);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        return new ClientConnection(socket, reader, writer);
    }

    private static class ClientConnection {
        final Socket socket;
        final BufferedReader reader;
        final PrintWriter writer;
        final long connectedAt;
        String name;

        public ClientConnection(Socket socket, BufferedReader reader, PrintWriter writer) {
            this.socket = socket;
            this.reader = reader;
            this.writer = writer;
            this.connectedAt = System.currentTimeMillis();
        }

        public boolean isRegistered() {
            return name != null;
        }

        @Override
        public String toString() {
            return name != null ? name : socket.getRemoteSocketAddress().toString();
        }
    }
}