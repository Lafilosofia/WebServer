package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private ServerSocket server;
    private ExecutorService threadPoll;
    //初始化服务端
    public WebServer(){
        //tomacat(服务器)默认服务器端口号是8088
        try{
            this.server = new ServerSocket(8088);
            threadPoll = Executors.newFixedThreadPool(30);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            while (true){
                System.out.println("等待客户端连接...");
                Socket socket = server.accept();
                ClientHandler client = new ClientHandler(socket);
                Thread t = new Thread(client);
                t.start();
                threadPoll.execute(client);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer web = new WebServer();
        web.start();
    }
}
