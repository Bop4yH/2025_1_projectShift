package ru.shift.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

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
