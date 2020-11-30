package src.com.ykt.webServer.servlet;
/**
 * 用户登录ye'wu
 */
import java.io.RandomAccessFile;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

public class LoginServlet extends HttpServlet {

	public void service(HttpRequest request, HttpResponse response) {
		/*
		 * 登录业务流程
		 * 1.通过request获取用户登录的信息
		 * 2.将获取的登录信息和user.dat文件中存储信息校验,
		 * 如果账号不存在则响应一个账号不存在页面,密码不对响应一个密码错误页面
		 * 如果登录成功,响应登录成功页面
		 * 3.设置response对象给客户端响应登陆成功页面
		 */
		//1
		String name=request.getParameter("username");
		String pwd=request.getParameter("password");
		//System.out.println("username:"+username+",pwd:"+pwd);
		
		//2
		try (RandomAccessFile raf=new RandomAccessFile("user.dat","r");) {
			boolean nameFlag=false;//账号不存在
			boolean pwdFlag=false;//密码错误
			for(int i=0;i<raf.length()/100;i++){
				//从user.dat文件中提取数据
				byte[] data=new byte[32];
				//设置指针位置
				raf.seek(i*100);
				//从user.dat中获取用户名
				raf.read(data);
				String userName=new String(data,"UTF-8").trim();
				if(userName.equals(name)){//判断用户名是否存在
					nameFlag=true;
					
					//从user.dat中获取密码
					raf.read(data);
					String password=new String(data,"UTF-8").trim();
					if(password.equals(pwd)){//判断密码是否正确
						pwdFlag=true;
						//登陆成功
						//3
						/*File file=new File("webapps/login_success.html");
						response.setEntity(file);*/
						forward("login_success.html", request, response);
					}
					
				}
			}
			
			//3
			//判断用户名和密码是否正确
			if(!nameFlag){//用户名不存在
				/*File file=new File("webapps/nameNotExists.html");
				response.setEntity(file);*/
				forward("nameNotExists.html", request, response);
				return ;
			}
			
			if(!pwdFlag){//用户密码错误
				/*File file=new File("webapps/pwdError.html");
				response.setEntity(file);*/
				forward("pwdError.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}