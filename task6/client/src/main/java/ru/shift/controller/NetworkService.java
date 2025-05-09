package ru.shift.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.common.Message;
import ru.shift.common.MessageUtils;
import ru.shift.data.ConnectionData;

public class NetworkService implements AutoCloseable {

   private static final Logger log = LoggerFactory.getLogger(NetworkService.class);
   private final ConnectionData data;
   private final ExecutorService readerPool =
       Executors.newSingleThreadExecutor(r -> {
          Thread t = new Thread(r, "tcp-reader");
          t.setDaemon(true);
          return t;
       });
   private final List<NetListener> listeners = new CopyOnWriteArrayList<>();

   private Socket socket;
   private PrintWriter out;

   public NetworkService(ConnectionData data) {
      this.data = data;
   }

   public static void testConnection(String ip, int port) throws IOException {
      try (Socket ignored = new Socket(ip, port)) {
      }
   }

   public void start() {
      readerPool.submit(this::readLoop);
   }

   private void readLoop() {
      try (Socket s = new Socket(data.ip(), data.port());
          BufferedReader in = new BufferedReader(
              new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
          PrintWriter pw = new PrintWriter(
              new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

         socket = s;
         out = pw;

         String line;
         while ((line = in.readLine()) != null) {
            for (NetListener l : listeners) {
               l.onRawMessage(line);
            }
         }
      } catch (IOException e) {
         log.warn("tcp reader exited", e);
         for (NetListener l : listeners) {
            l.onError(e);
         }
      }
   }

   public synchronized void send(Message msg) throws IOException {
      if (out == null) {
         throw new IllegalStateException("Not connected yet");
      }
      out.println(MessageUtils.mapper().writeValueAsString(msg));
   }

   public void addListener(NetListener l) {
      listeners.add(l);
   }

   public void removeListener(NetListener l) {
      listeners.remove(l);
   }

   @Override
   public void close() {
      readerPool.shutdownNow();
      if (out != null) {
         out.close();
      }
      if (socket != null && !socket.isClosed()) {
         try {
            socket.close();
         } catch (IOException e) {
            log.debug("Socket close error", e);
         }
      }
   }

   public interface NetListener {

      void onRawMessage(String json);

      void onError(Throwable t);
   }
}
