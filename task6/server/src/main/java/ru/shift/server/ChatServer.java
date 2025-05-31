package ru.shift.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.config.Config;
import ru.shift.config.ServerConfig;


public class ChatServer {

   private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

   public static void main(String[] args) {
      ServerConfig config = Config.loadConfig("config.properties")
          .orElseThrow(() -> new IllegalStateException("Can't load config"));
      final int PORT = config.port();

      try (ServerSocket serverSocket = new ServerSocket(PORT)) {
         log.info("Server started on port {}", PORT);

         ClientManager clientManager = new ClientManager();
         ExecutorService pool = Executors.newCachedThreadPool();
         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pool.shutdownNow();
            try {
               serverSocket.close();
               log.info("Server socket closed.");
            } catch (IOException e) {
               log.warn("Failed to close server socket: {}", e.getMessage(), e);
            }
         }));

         while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            ClientConnection client = clientManager.createClient(socket);
            pool.submit(new ClientHandler(client, clientManager));
         }

      } catch (IOException e) {
         log.error("Server IO error: {}", e.getMessage(), e);
      }
   }
}

