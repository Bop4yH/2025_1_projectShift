package ru.shift.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.common.ChatMessageData;
import ru.shift.common.Message;
import ru.shift.common.MessageType;
import ru.shift.common.MessageUtils;
import ru.shift.common.UsersData;

public class ClientManager {

   private static final Logger log = LoggerFactory.getLogger(ClientManager.class);
   private static final String SERVER_NAME = "Server";
   private final Collection<ClientConnection> clients = new ConcurrentLinkedQueue<>();

   public void acceptNewClient(ServerSocket serverSocket) {
      try {
         Socket socket = serverSocket.accept();
         log.info("New client connected: {}", socket.getRemoteSocketAddress());

         ClientConnection client = createClient(socket);

         MessageUtils.sendMessage(client.writer, MessageType.ENTER_NAME);
         clients.add(client);

      } catch (IOException e) {
         log.warn("Failed to accept client: {}", e.getMessage());
      }
   }

   public void processClientInput(ClientConnection client, List<ClientConnection> toRemove) {
      try {
         String line = client.reader.readLine();
         if (line == null) {
            throw new IOException("Client disconnected");
         }

         Message msg = MessageUtils.mapper().readValue(line, Message.class);

         switch (msg.getType()) {
            case TEXT -> {
               String text = (String) msg.getData();
               msg.setData(new ChatMessageData(client.name, text));
               broadcastMessage(msg);
            }
            case USER_NAME -> {
               if (!client.isRegistered()) {
                  String name = (String) msg.getData();
                  if (registerClientName(client, name)) {
                     broadcastUsers();
                     broadcastMessage(createServerMessage(client.name + " joined the chat"));
                  }
               }
            }
            default -> log.warn("Unknown message type: {}", msg.getType());
         }
      } catch (SocketTimeoutException ignore) {
      } catch (IOException e) {
         broadcastMessage(createServerMessage(client.name + " disconnected the chat"));
         log.info("Client disconnected: {}", client.name);
         toRemove.add(client);
         closeClient(client);
      }
   }

   public void broadcastUsers() {
      UsersData usersData = new UsersData(
          clients.stream().map(c -> c.name).filter(Objects::nonNull).toList());
      for (ClientConnection client : clients) {
         if (!client.isRegistered()) {
            continue;
         }
         MessageUtils.sendMessage(client.writer, MessageType.USERS, usersData);
      }
   }

   public void broadcastMessage(Message message) {
      List<ClientConnection> disconnected = new ArrayList<>();
      for (ClientConnection client : clients) {
         if (!client.isRegistered()) {
            continue;
         }
         MessageUtils.sendMessage(client.writer, message);
         if (client.writer.checkError()) {
            log.warn("Client write error: {}", client.name);
            disconnected.add(client);
            closeClient(client);
         }
      }
      if (!disconnected.isEmpty()) {
         clients.removeAll(disconnected);
         broadcastUsers();
      }
   }

   private boolean registerClientName(ClientConnection client, String proposedName) {
      if (proposedName == null || proposedName.isEmpty()) {
         MessageUtils.sendMessage(client.writer, MessageType.NAME_EMPTY);
         return false;
      }
      boolean nameTaken = clients.stream().anyMatch(c -> proposedName.equalsIgnoreCase(c.name));
      if (nameTaken || proposedName.equalsIgnoreCase(SERVER_NAME)) {
         MessageUtils.sendMessage(client.writer, MessageType.NAME_TAKEN);
         return false;
      }
      client.name = proposedName;
      return true;
   }

   private ClientConnection createClient(Socket socket) throws IOException {
      socket.setSoTimeout(100);
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      PrintWriter writer = new PrintWriter(
          new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      return new ClientConnection(socket, reader, writer);
   }

   private void closeClient(ClientConnection client) {
      try {
         client.socket.close();
      } catch (IOException e) {
         log.error("Error closing client: {}", e.getMessage(), e);
      }
   }

   private Message createServerMessage(String text) {
      return new Message(MessageType.TEXT, new ChatMessageData(SERVER_NAME, text));
   }

   public Collection<ClientConnection> allClients() {
      return clients;
   }
}
