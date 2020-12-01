package src.com.ykt.webServer.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;



/**
 * 处理注册业务
 * @author HP
 *
 */
public class RegisterServlet extends HttpServlet{
	
	public void service(HttpRequest request, HttpResponse response){
		/*
		 * 注册业务的流程
		 * 1.通过request获取用户注册的信息
		 * 2.将信息写入文件user.dat中
		 * 3.设置reponse对象给客户端响应注册结果
		 */
		//   1
		String username = request.getParameter("username");
		String pwd = request.getParameter("passwrod");
		String nick = request.getParameter("nick");
		String age = request.getParameter("age");
		//把age转换Integer类型   
		Integer useAge = Integer.parseInt(age);
		System.out.println("请求参数是:"+username+","+pwd+","+nick+","+age);
		
		// 2
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			/*
			 *每条记录占用100个字节,其中用户名,密码,昵称各占32个字节,
			 *int类型age占4个字节 
			 */
			//设置指针位置
			raf.seek(raf.length());
			//写用户名
			byte[] data=username.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//写密码
			data=pwd.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//写昵称
			data=nick.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//写年龄
			raf.writeInt(useAge);
			
			// 3.注册成功响应一个注册成功页面
			//转发
//			File file=new  File("webapps/reg_success.html");
//			response.setEntity(file);	
			forward("reg_success.html",request, response);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}