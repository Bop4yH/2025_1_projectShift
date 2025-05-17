package ru.shift.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpConnection implements AutoCloseable {

   private static final Logger log = LoggerFactory.getLogger(TcpConnection.class);
   private final Socket socket;
   private final BufferedReader in;
   private final BufferedWriter out;

   private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
   private final Object sendLock = new Object();

   public TcpConnection(Socket socket) throws IOException {
      this.socket = socket;
      this.in = new BufferedReader(
          new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
      this.out = new BufferedWriter(
          new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
   }

   public void readLoop() {
      try {
         String line;
         while ((line = in.readLine()) != null) {
            for (Listener l : listeners) {
               l.onRawMessage(line);
            }
         }
         for (Listener l : listeners) {
            l.onDisconnect();
         }
      } catch (IOException e) {
         for (Listener l : listeners) {
            l.onError(e);
         }
      }
   }

   public void send(Message msg) throws IOException {
      String json = MessageUtils.serialize(msg);
      synchronized (sendLock) {
         out.write(json + "\n");
         out.flush();
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
         out.close();
         in.close();
      } catch (IOException e) {
         log.error("Failed to close connection", e);
      }
   }

   public interface Listener {

      void onRawMessage(String json);

      void onError(Throwable t);

      void onDisconnect();
   }
}
