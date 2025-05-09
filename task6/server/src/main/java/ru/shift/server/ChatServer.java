package ru.shift.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
         InputProcessor inputProcessor = new InputProcessor(clientManager);

         ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
         executor.scheduleWithFixedDelay(inputProcessor, 0, 100, TimeUnit.MILLISECONDS);

         Thread acceptThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
               clientManager.acceptNewClient(serverSocket);
            }
         }, "client-accept-thread");

         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down server...");
            executor.shutdownNow();
            acceptThread.interrupt();
            try {
               serverSocket.close();
               log.info("Server socket closed.");
            } catch (IOException e) {
               log.warn("Failed to close server socket: {}", e.getMessage(), e);
            }
         }));

         acceptThread.start();
         acceptThread.join();
         log.info("Main thread has exited. Server is fully shut down.");

      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         log.info("Server shutdown requested (thread interrupted).");
      } catch (IOException e) {
         log.error("Server IO error: {}", e.getMessage(), e);
      }
   }
}

