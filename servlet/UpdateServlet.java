package src.com.ykt.webServer.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;



/**
 * �޸��û�����
 * @author 86180
 *
 */
public class UpdateServlet extends HttpServlet {
	/*
	 * �޸��û���������
	 * 1.��ȡ�û��޸����������
	 * 2.�����ж��û����˺ź���������ȷ��,������ʾ��Ӧ�Ĵ���ҳ��
	 * 3.Ȼ�����޸�����
	 * 4.�޸������Ӧһ���޸ĳɹ�ҳ��
	 */
	public void service(HttpRequest request, HttpResponse response) {
		//1
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		String newPwd = request.getParameter("newPassword");
		String confirmPwd = request.getParameter("confirmPassword");
		
		//2
		try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");) {
			boolean nameFlag = false;//�û���������
			boolean pwdFlag = false;//�û��������
			byte[] data = new byte[32];//�洢����
			for(int i = 0;i < raf.length() / 100;i ++){
				//����ָ��λ��
				raf.seek(i * 100);
				
				//��ȡ�û���
				raf.read(data);
				String username = new String(data, "UTF-8").trim();
				if(username.equals(name)){//�ж��û����Ƿ����
					nameFlag = true;
					
					//��ȡ�û�����
					raf.read(data);
					String password = new String(data, "UTF-8").trim();
					if(password.equals(pwd)){//�ж��û������Ƿ���ȷ
						pwdFlag = true;
						
						//3
						if(newPwd.equals(confirmPwd)){//�ж��������ȷ�������Ƿ�һ��
							//Ϊ���޸�����,��������ָ��λ��
							raf.seek(i * 100 + 32);
							byte[] d = newPwd.getBytes("UTF-8");
							d=Arrays.copyOf(d, 32);
							raf.write(d);
							//System.out.println("�޸�����ɹ�!");
							//4
							//ת��
							//forward("update_success.html", request, response);
							
							//�ض���
							response.sendRedirect("update_success.html");
						}else{//�����������ȷ�����벻һ��,��ת����ʾҳ��
							//4
							forward("updateError.html", request, response);
						}
						
					}
				}
			}
			
			//4
			if(!nameFlag){//�û���������
				//ת��
				//forward("nameNotExists.html", request, response);
				
				//�ض���
				response.sendRedirect("nameNotExists.html");
				return ;
			}
			if(!pwdFlag){//�û��������
				forward("pwdError.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
