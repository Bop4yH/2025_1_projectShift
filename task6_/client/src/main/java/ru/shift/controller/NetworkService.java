package ru.shift.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shift.Message;
import ru.shift.data.ConnectionData;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkService implements AutoCloseable {
    public interface NetworkListener {
        void onRawMessage(String json);
        void onError(Throwable t);
    }

    private final ConnectionData data;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<NetworkListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean running;
    private Thread readerThread;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public NetworkService(ConnectionData data) { this.data = data; }

    public void start() {
        running = true;
        readerThread = new Thread(this::run);
        readerThread.start();
    }

    private void run() {
        try (Socket s = new Socket(data.ip(), data.port());
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter pw = new PrintWriter(
                     new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

            socket = s; in = br; out = pw;

            String line;
            while (running && (line = br.readLine()) != null) {
                for (NetworkListener l : listeners) l.onRawMessage(line);
            }
        } catch (IOException e) {
            for (NetworkListener l : listeners) l.onError(e);
        }
    }

    /* thread-safe write */
    public synchronized void send(Message msg) throws IOException {
        String json = mapper.writeValueAsString(msg);
        out.println(json);
    }

    public void addListener(NetworkListener l) { listeners.add(l); }
    public void removeListener(NetworkListener l){ listeners.remove(l); }

    @Override
    public void close() throws Exception {
        running = false;
        if (socket != null) socket.close();
    }
}
