package ru.shift.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.common.ChatMessageData;
import ru.shift.common.Message;
import ru.shift.common.MessageType;
import ru.shift.common.MessageUtils;
import ru.shift.common.PlainText;
import ru.shift.common.UsersData;

public class ClientManager {

   private static final Logger log = LoggerFactory.getLogger(ClientManager.class);
   private static final String SERVER_NAME = "Server";

   private final Collection<ClientConnection> clients = new ConcurrentLinkedQueue<>();

   public void register(ClientConnection client) {
      clients.add(client);
      try {
         client.send(new Message(MessageType.ENTER_NAME, null));
      } catch (IOException e) {
         log.warn("Failed to send ENTER_NAME to {}", client, e);
         handleDisconnect(client, e);
      }
   }

   public void handleMessage(ClientConnection client, String json) throws IOException {
      Message msg = MessageUtils.mapper().readValue(json, Message.class);

      switch (msg.getType()) {
         case TEXT -> {
            String text = ((PlainText) msg.getData()).text();
            msg.setData(new ChatMessageData(client.getName(), text));
            broadcastMessage(msg);
         }
         case USER_NAME -> {
            if (!client.isRegistered()) {
               String name = ((PlainText) msg.getData()).text();
               if (registerClientName(client, name)) {
                  broadcastUsers();
                  broadcastMessage(createServerMessage(client.getName() + " joined the chat"));
               }
            }
         }
         default -> log.warn("Unknown message type: {}", msg.getType());
      }
   }

   public void handleDisconnect(ClientConnection client, Exception e) {
      clients.remove(client);
      if (e != null) {
         log.warn("Client {} disconnected with error: ", client.getName(),  e);
      } else {
         log.info("Client {} disconnected", client.getName());
      }
      if(client.getName() != null) {
         broadcastMessage(createServerMessage(client.getName() + " disconnected"));
         broadcastUsers();
      }
      client.close();
   }

   public void broadcastUsers() {
      UsersData usersData = new UsersData(
          clients.stream().map(ClientConnection::getName).filter(Objects::nonNull).toList());

      for (ClientConnection client : clients) {
         if (!client.isRegistered()) {
            continue;
         }
         try {
            client.send(new Message(MessageType.USERS, usersData));
         } catch (IOException e) {
            log.warn("Failed to send USERS to {}", client.getName(), e);
            handleDisconnect(client, e);
         }
      }
   }

   public void broadcastMessage(Message message) {

      for (ClientConnection client : clients) {
         if (!client.isRegistered()) {
            continue;
         }

         try {
            client.send(message);
         } catch (IOException e) {
            log.warn("Client write error: {}", client.getName(), e);
            handleDisconnect(client, e);
         }
      }
   }

   private boolean registerClientName(ClientConnection client, String proposedName) {
      if (proposedName == null || proposedName.isEmpty()) {
         try {
            client.send(new Message(MessageType.NAME_EMPTY, null));
         } catch (IOException e) {
            handleDisconnect(client, e);
         }
         return false;
      }

      boolean nameTaken = clients.stream()
          .anyMatch(c -> proposedName.equalsIgnoreCase(c.getName()));

      if (nameTaken || proposedName.equalsIgnoreCase(SERVER_NAME)) {
         try {
            client.send(new Message(MessageType.NAME_TAKEN, null));
         } catch (IOException e) {
            handleDisconnect(client, e);
         }
         return false;
      }

      client.setName(proposedName);
      return true;
   }

   public ClientConnection createClient(Socket socket) throws IOException {
      return new ClientConnection(socket);
   }

   private Message createServerMessage(String text) {
      return new Message(MessageType.TEXT, new ChatMessageData(SERVER_NAME, text));
   }
}
