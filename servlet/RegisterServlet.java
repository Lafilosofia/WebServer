package src.com.ykt.webServer.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;



/**
 * ����ע��ҵ��
 * @author HP
 *
 */
public class RegisterServlet extends HttpServlet{
	
	public void service(HttpRequest request, HttpResponse response){
		/*
		 * ע��ҵ�������
		 * 1.ͨ��request��ȡ�û�ע�����Ϣ
		 * 2.����Ϣд���ļ�user.dat��
		 * 3.����reponse������ͻ�����Ӧע����
		 */
		//   1
		String username = request.getParameter("username");
		String pwd = request.getParameter("passwrod");
		String nick = request.getParameter("nick");
		String age = request.getParameter("age");
		//��ageת��Integer����   
		Integer useAge = Integer.parseInt(age);
		System.out.println("���������:"+username+","+pwd+","+nick+","+age);
		
		// 2
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");) {
			/*
			 *ÿ����¼ռ��100���ֽ�,�����û���,����,�ǳƸ�ռ32���ֽ�,
			 *int����ageռ4���ֽ� 
			 */
			//����ָ��λ��
			raf.seek(raf.length());
			//д�û���
			byte[] data=username.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д����
			data=pwd.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д�ǳ�
			data=nick.getBytes("UTF-8");
			data=Arrays.copyOf(data, 32);
			raf.write(data);
			
			//д����
			raf.writeInt(useAge);
			
			// 3.ע��ɹ���Ӧһ��ע��ɹ�ҳ��
			//ת��
//			File file=new  File("webapps/reg_success.html");
//			response.setEntity(file);	
			forward("reg_success.html",request, response);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}