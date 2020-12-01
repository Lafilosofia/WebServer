package src.com.ykt.webServer.servlet;

import java.io.RandomAccessFile;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

/**
 * 查询用户所有的数据并响应到浏览器
 * @author HP
 *
 */
public class SelectUserAllServlet extends HttpServlet {

	public void service(HttpRequest request, HttpResponse response) {
		/*
		 * 1.首先查询出所有的用户数据
		 * 2.编写前端相关代码,把查询出来的所有用户数据动态的放在页面中
		 */
		StringBuilder builder=new StringBuilder();
		builder.append("<html>");
		builder.append("<head>");
		builder.append("<meta charset='UTF-8'>");
		builder.append("<title>用户列表</title>");
		builder.append("</head>");
		builder.append("<body style='background:orange '>");
		builder.append("<div align='center'>");
		builder.append("<h1>用户列表</h1>");
		builder.append("<table border='1'>");
		
		builder.append("<tr>");
		builder.append("<th>ID</th>");
		builder.append("<th>姓名</th>");
		builder.append("<th>密码</th>");
		builder.append("<th>昵称</th>");
		builder.append("<th>年龄</th>");
		builder.append("</tr>");
		
		//动态显示所有用户信息
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "r")){
			byte[] bytes=new byte[32];
			for(int i=1;i<=raf.length()/100;i++){
				//读取名字
				raf.read(bytes);
				String userName=new String(bytes, "UTF-8").trim();
				
				//读取密码
				raf.read(bytes);
				String userPwd=new String(bytes, "UTF-8").trim();
				
				//读取昵称
				raf.read(bytes);
				String userNick=new String(bytes, "UTF-8").trim();
				
				//读取年龄
				int age=raf.readInt();
				
				builder.append("<tr>");
				builder.append("<td>"+i+"</td>");
				builder.append("<td>"+userName+"</td>");
				builder.append("<td>"+userPwd+"</td>");
				builder.append("<td>"+userNick+"</td>");
				builder.append("<td>"+age+"</td>");
				builder.append("</tr>");
			}
			
			builder.append("</table>");
			builder.append("<a href='myweb/web/index.html'>返回首页</a>");
			builder.append("</div>");
			builder.append("</body>");
			builder.append("</html>");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String str=builder.toString();
			byte[] data=str.getBytes("UTF-8");
			
			//设置响应头
			response.putHeaders("Content-Type", "text/html");
			response.putHeaders("Content-Length", data.length+"");
			
			//设置响应正文
			response.setData(data);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
