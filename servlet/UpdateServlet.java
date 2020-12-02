package src.com.ykt.webServer.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;



/**
 * 修改用户密码
 * @author 86180
 *
 */
public class UpdateServlet extends HttpServlet {
	/*
	 * 修改用户密码流程
	 * 1.获取用户修改密码的数据
	 * 2.首先判断用户的账号和密码是正确的,否则提示响应的错误页面
	 * 3.然后再修改密码
	 * 4.修改完后响应一个修改成功页面
	 */
	public void service(HttpRequest request, HttpResponse response) {
		//1
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		String newPwd = request.getParameter("newPassword");
		String confirmPwd = request.getParameter("confirmPassword");
		
		//2
		try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");) {
			boolean nameFlag = false;//用户名不存在
			boolean pwdFlag = false;//用户密码错误
			byte[] data = new byte[32];//存储数据
			for(int i = 0;i < raf.length() / 100;i ++){
				//设置指针位置
				raf.seek(i * 100);
				
				//获取用户名
				raf.read(data);
				String username = new String(data, "UTF-8").trim();
				if(username.equals(name)){//判断用户名是否存在
					nameFlag = true;
					
					//获取用户密码
					raf.read(data);
					String password = new String(data, "UTF-8").trim();
					if(password.equals(pwd)){//判断用户密码是否正确
						pwdFlag = true;
						
						//3
						if(newPwd.equals(confirmPwd)){//判断新密码和确认密码是否一样
							//为了修改密码,重新设置指针位置
							raf.seek(i * 100 + 32);
							byte[] d = newPwd.getBytes("UTF-8");
							d=Arrays.copyOf(d, 32);
							raf.write(d);
							//System.out.println("修改密码成功!");
							//4
							//转发
							//forward("update_success.html", request, response);
							
							//重定向
							response.sendRedirect("update_success.html");
						}else{//如果新密码与确认密码不一致,跳转到提示页面
							//4
							forward("updateError.html", request, response);
						}
						
					}
				}
			}
			
			//4
			if(!nameFlag){//用户名不存在
				//转发
				//forward("nameNotExists.html", request, response);
				
				//重定向
				response.sendRedirect("nameNotExists.html");
				return ;
			}
			if(!pwdFlag){//用户密码错误
				forward("pwdError.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
