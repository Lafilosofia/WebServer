package src.com.ykt.webServer.servlet;

import java.io.File;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

/**
 * 所有Servlet的父类,规定了所有Servlet都应该具备的功能
 * @author HP
 *
 */
public abstract class HttpServlet {
	//该方法是用来处理业务逻辑
	public abstract void service(HttpRequest request,HttpResponse response);
	//该方法是用来处理转发业务
	public void forward(String url,HttpRequest request,HttpResponse response){
		File file = new File("webapps/" + url);
		response.setEntity(file);
	}
	
}
