package main.client;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    private int port;
    //

    @Override
    public void run() {
    Socket socket = new Socket("127.0.0.1",8081);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println("");
        out.close();

    }
    public void sendFile(String inputFilePath) {
        int bytes = 0;
        FileInputStream fileInputStream = new FileInputStream();
    }
}
