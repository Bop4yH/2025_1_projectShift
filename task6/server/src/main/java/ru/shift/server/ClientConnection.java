package ru.shift.server;

import java.io.IOException;
import java.net.Socket;
import ru.shift.common.TcpConnection;

public class ClientConnection extends TcpConnection {

   private String name;

   public ClientConnection(Socket socket) throws IOException {
      super(socket);
   }

   public boolean isRegistered() {
      return name != null;
   }

   public String getName() {
      return name;
   }

   public void setName(String n) {
      this.name = n;
   }
}
