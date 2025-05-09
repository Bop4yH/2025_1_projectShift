package ru.shift.server;

import java.util.ArrayList;
import java.util.List;

public class InputProcessor implements Runnable {

   private final ClientManager clientManager;

   public InputProcessor(ClientManager clientManager) {
      this.clientManager = clientManager;
   }

   @Override
   public void run() {
      List<ClientConnection> toRemove = new ArrayList<>();
      for (ClientConnection client : clientManager.allClients()) {
         try {
            clientManager.processClientInput(client, toRemove);
         } catch (Exception e) {
            // логи внутри clientManager
            toRemove.add(client);
         }
      }
      if (!toRemove.isEmpty()) {
         clientManager.allClients().removeAll(toRemove);
         clientManager.broadcastUsers();
      }
   }
}
