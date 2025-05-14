package ru.shift.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

public class TcpConnection implements AutoCloseable {

   private final Socket socket;
   private final BufferedReader in;
   private final PrintWriter out;

   private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
   private final Object sendLock = new Object();

   public TcpConnection(Socket socket) throws IOException {
      this.socket = socket;
      this.in = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new PrintWriter(
          new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
   }

   public void readLoop() {
      try (socket; in; out) {
         String line;
         while ((line = in.readLine()) != null) {
            for (Listener l : listeners) {
               l.onRawMessage(line);
            }
         }
         for (Listener l : listeners) {
            l.onError(null);
         }
      } catch (IOException e) {
         for (Listener l : listeners) {
            l.onError(e);
         }
      }
   }

   public void send(Message msg) throws IOException {
      String json = MessageUtils.mapper().writeValueAsString(msg);
      synchronized (sendLock) {
         out.println(json);
      }
   }

   public void addListener(Listener l) {
      listeners.add(l);
   }

   public void removeListener(Listener l) {
      listeners.remove(l);
   }

   @Override
   public void close() {
      try {
         socket.close();
      } catch (IOException ignore) {
      }
   }

   public interface Listener {

      void onRawMessage(String json);

      void onError(Throwable t);
   }
}
