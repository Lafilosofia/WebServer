package src.com.ykt.webServer.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.EmptyStackException;



import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;
import src.com.ykt.webServer.servlet.LoginServlet;
import src.com.ykt.webServer.servlet.RegisterServlet;
import src.com.ykt.webServer.servlet.UpdateServlet;

/**
 * �߳�������,���ڴ���ĳ���ͻ��˵�����������Ӧ
 * @author 86180
 *
 */
public class ClientHandler implements Runnable {
		private Socket socket;
		public ClientHandler(Socket socket){
			this.socket=socket;
		}
		
		public void run() {
			try {
				//����ͻ�����������
				HttpRequest request=new HttpRequest(socket);
//				//��ȡ�����ַ
				String uri=request.getRequestURI();
				System.out.println(uri);
				//��Ӧ�ͻ�������
				HttpResponse response=new HttpResponse(socket);
//				
				File file=new File("webapps"+uri);
				if("/regServlet".equals(uri)){//������û�ע����Ҫ�����û�����
					//����ע������
					RegisterServlet reg=new RegisterServlet();
					reg.service(request, response);
				}else if("/loginServlet".equals(uri)){//������û���¼��Ҫ�����û���¼����
					//�����¼����
					LoginServlet login=new LoginServlet();
					login.service(request,response);
					
				}else if("/updateServlet".equals(uri)){	
					UpdateServlet update=new UpdateServlet();
					update.service(request,response);
					
				}else{//�ж��ļ��Ƿ����
					if(file.exists()){
						response.setEntity(file);
					}else{
						//ҲҪ��Ӧһ��"����ҳ��",
						File f=new File("webapps/error.html");
						response.setStatusCode(404);//��Ӧ404����
						response.setEntity(f);
					}
				}
				response.flush();
			}catch(EmptyStackException e){
				System.out.println("������");
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
}