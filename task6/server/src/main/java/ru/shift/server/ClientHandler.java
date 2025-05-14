package ru.shift.server;

import java.io.IOException;
import ru.shift.common.TcpConnection;

public class ClientHandler implements Runnable {
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
      });
      client.readLoop();
   }
}
