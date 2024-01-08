package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private int port;
    private static ConcurrentHashMap<Integer, ClientHandler> activeConnections = new ConcurrentHashMap<>();

    public Server(int port) {
        this.port = port;
        activeConnections = new ConcurrentHashMap<>();
    }

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.start();
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = socket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                activeConnections.put(clientSocket.getPort(), clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                int read = clientSocket.getInputStream().read();
                if (read == -1)
                    System.out.println("disconnected");
                else
                    System.out.println(read);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    activeConnections.remove(clientSocket.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendFile(String inputFilePath) {
    }
}
