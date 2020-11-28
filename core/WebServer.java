package src.com.ykt.webServer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	private ServerSocket server;
	
	//初始化服务端
	public WebServer(){
		//tomcat(服务器)默认服务端口号是8088
		try {
			server=new ServerSocket(8088);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			while(true){
				Socket socket=server.accept();
				ClientHandler client=new ClientHandler(socket);
				Thread t=new Thread(client);
				t.start();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer web=new WebServer();
		web.start();
	}
}
