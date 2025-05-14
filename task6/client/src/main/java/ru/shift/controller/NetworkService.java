package ru.shift.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.shift.common.Message;
import ru.shift.common.TcpConnection;
import ru.shift.data.ConnectionData;

public class NetworkService implements AutoCloseable {

   private final TcpConnection tcpConnection;
   private final ExecutorService pool = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "tcp-reader");
      t.setDaemon(true);
      return t;
   });

   public NetworkService(ConnectionData data) throws IOException {
      this.tcpConnection = new TcpConnection(new Socket(data.ip(), data.port()));
   }

   public void addListener(NetListener l) {
      tcpConnection.addListener(l);
   }

   public void removeListener(NetListener l) {
      tcpConnection.removeListener(l);
   }

   public void start() {
      pool.submit(tcpConnection::readLoop);
   }

   public void send(Message msg) throws IOException {
      tcpConnection.send(msg);
   }

   @Override
   public void close() {
      pool.shutdownNow();
      tcpConnection.close();
   }

   public interface NetListener extends TcpConnection.Listener {

   }
}
