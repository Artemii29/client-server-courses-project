package main.client;

import main.common.ConnectionService;
import main.common.Message;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

// При создании клиента в конструктор передается
// экземпляр InetSocketAddress,
// который хранит IP сервера и порт.
public class Client implements Runnable {
    private InetSocketAddress remoteAddress;

    public Client(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    // 2.1. запрашивает текст сообщения (запрос)
// у пользователя
// 2.2. устанавливает соединение с сервером
// 2.3. создает экземпляр сообщения
// 2.4. отправляет сообщение на сервер
// 2.5. получает ответ
// 2.6. выводит полученный ответ в консоль
// 2.7. закрывает соединение с сервером
// И так до тех пор, пока пользователь не введет '/exit'
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите текст или /exit");
            String text = scanner.nextLine();
            if (text.equals("/exit")) break;


            try (Socket socket = new Socket()){
                socket.connect(remoteAddress);
                try (ConnectionService service = new ConnectionService(socket)){
                    service.writeMessage(new Message(text));
                    Message message = service.readMessage();
                    System.out.println(message.getText());
                } catch (IOException e){
                    System.out.println("Сервер перестал отвечать");
                }
            } catch (IOException e){
                System.out.println("Сервер не доступен");
            }
        }
    }

}