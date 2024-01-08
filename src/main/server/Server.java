package main.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        public static void receiveFile(String filename) throws Exception{
            FileOutputStream fileOutputStream
                    = new FileOutputStream(filename);
            int bytes = 0;
            FileInputStream fileInputStream = new FileInputStream(filename);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            long size = dataInputStream.readLong();
            byte[] buffer = new byte[4 * 1024];
            while (size > 0
                    && (bytes = dataInputStream.read(
                    buffer, 0,
                    (int)Math.min(buffer.length, size)))
                    != -1) {
                // Here we write the file using write method
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
        }
    }


}
