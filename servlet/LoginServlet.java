package src.com.ykt.webServer.servlet;
/**
 * �û���¼ye'wu
 */
import java.io.RandomAccessFile;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

public class LoginServlet extends HttpServlet {

	public void service(HttpRequest request, HttpResponse response) {
		/*
		 * ��¼ҵ������
		 * 1.ͨ��request��ȡ�û���¼����Ϣ
		 * 2.����ȡ�ĵ�¼��Ϣ��user.dat�ļ��д洢��ϢУ��,
		 * ����˺Ų���������Ӧһ���˺Ų�����ҳ��,���벻����Ӧһ���������ҳ��
		 * �����¼�ɹ�,��Ӧ��¼�ɹ�ҳ��
		 * 3.����response������ͻ�����Ӧ��½�ɹ�ҳ��
		 */
		//1
		String name=request.getParameter("username");
		String pwd=request.getParameter("password");
		//System.out.println("username:"+username+",pwd:"+pwd);
		
		//2
		try (RandomAccessFile raf=new RandomAccessFile("user.dat","r");) {
			boolean nameFlag=false;//�˺Ų�����
			boolean pwdFlag=false;//�������
			for(int i=0;i<raf.length()/100;i++){
				//��user.dat�ļ�����ȡ����
				byte[] data=new byte[32];
				//����ָ��λ��
				raf.seek(i*100);
				//��user.dat�л�ȡ�û���
				raf.read(data);
				String userName=new String(data,"UTF-8").trim();
				if(userName.equals(name)){//�ж��û����Ƿ����
					nameFlag=true;
					
					//��user.dat�л�ȡ����
					raf.read(data);
					String password=new String(data,"UTF-8").trim();
					if(password.equals(pwd)){//�ж������Ƿ���ȷ
						pwdFlag=true;
						//��½�ɹ�
						//3
						/*File file=new File("webapps/login_success.html");
						response.setEntity(file);*/
						forward("login_success.html", request, response);
					}
					
				}
			}
			
			//3
			//�ж��û����������Ƿ���ȷ
			if(!nameFlag){//�û���������
				/*File file=new File("webapps/nameNotExists.html");
				response.setEntity(file);*/
				forward("nameNotExists.html", request, response);
				return ;
			}
			
			if(!pwdFlag){//�û��������
				/*File file=new File("webapps/pwdError.html");
				response.setEntity(file);*/
				forward("pwdError.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}