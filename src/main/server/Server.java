package main.server;

import main.common.ConnectionService;
import main.common.Message;

import java.io.File;
import java.net.ServerSocket;
import java.util.ArrayList;


import java.io.IOException;

public class Server extends Thread{
    private ArrayList <ConnectionService> clientList;
    private int port;

    public Server(int port,ArrayList clientList) {
        this.port = port;
        this.clientList = clientList;

    }
    public void sendToall(Message message) throws IOException {
        for(ConnectionService client : clientList)
            client.writeMessage(message);
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Сервер запущен");
            while (true) {
                // throws IOException
                try (ConnectionService service = new ConnectionService(serverSocket.accept())){
                    clientList.add(service);
                    Message message = service.readMessage();
                    File file = new File(message.getText());
                    if(file.isFile()){
                        Message message2 = new Message("Файл загружен");
                        sendToall(message2);sendToall(message2);

                    }
                    else {

                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Ошибка подключение клиента");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // скорее всего порт уже занят
            System.out.println("Ошибка создания serverSocket.");
        }
    }

}



