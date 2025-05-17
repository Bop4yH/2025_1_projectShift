package ru.shift.server;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.common.TcpConnection;

public class ClientHandler implements Runnable {

   private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
   private final ClientManager manager;
   private final ClientConnection client;

   public ClientHandler(ClientConnection client, ClientManager manager) {
      this.client = client;
      this.manager = manager;
   }

   @Override
   public void run() {
      manager.register(client);
      client.addListener(new TcpConnection.Listener() {
         @Override
         public void onRawMessage(String json) {
            try {
               manager.handleMessage(client, json);
            } catch (IOException e) {
               manager.handleDisconnect(client, e);
            }
         }

         @Override
         public void onError(Throwable t) {
            manager.handleDisconnect(client,
                t instanceof Exception ex ? ex : new IOException(t));
         }

         @Override
         public void onDisconnect() {
            manager.handleDisconnect(client);
            log.info("Client {} disconnected", client.getName());
         }
      });
      client.readLoop();
   }
}
