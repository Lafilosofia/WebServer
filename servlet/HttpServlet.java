package src.com.ykt.webServer.servlet;

import java.io.File;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

/**
 * ����Servlet�ĸ���,�涨������Servlet��Ӧ�þ߱��Ĺ���
 * @author HP
 *
 */
public abstract class HttpServlet {
	//�÷�������������ҵ���߼�
	public abstract void service(HttpRequest request,HttpResponse response);
	//�÷�������������ת��ҵ��
	public void forward(String url,HttpRequest request,HttpResponse response){
		File file = new File("webapps/" + url);
		response.setEntity(file);
	}
	
}
