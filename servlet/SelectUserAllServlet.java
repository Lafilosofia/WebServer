package src.com.ykt.webServer.servlet;

import java.io.RandomAccessFile;

import src.com.ykt.webServer.http.HttpRequest;
import src.com.ykt.webServer.http.HttpResponse;

/**
 * ��ѯ�û����е����ݲ���Ӧ�������
 * @author HP
 *
 */
public class SelectUserAllServlet extends HttpServlet {

	public void service(HttpRequest request, HttpResponse response) {
		/*
		 * 1.���Ȳ�ѯ�����е��û�����
		 * 2.��дǰ����ش���,�Ѳ�ѯ�����������û����ݶ�̬�ķ���ҳ����
		 */
		StringBuilder builder=new StringBuilder();
		builder.append("<html>");
		builder.append("<head>");
		builder.append("<meta charset='UTF-8'>");
		builder.append("<title>�û��б�</title>");
		builder.append("</head>");
		builder.append("<body style='background:orange '>");
		builder.append("<div align='center'>");
		builder.append("<h1>�û��б�</h1>");
		builder.append("<table border='1'>");
		
		builder.append("<tr>");
		builder.append("<th>ID</th>");
		builder.append("<th>����</th>");
		builder.append("<th>����</th>");
		builder.append("<th>�ǳ�</th>");
		builder.append("<th>����</th>");
		builder.append("</tr>");
		
		//��̬��ʾ�����û���Ϣ
		try(RandomAccessFile raf=new RandomAccessFile("user.dat", "r")){
			byte[] bytes=new byte[32];
			for(int i=1;i<=raf.length()/100;i++){
				//��ȡ����
				raf.read(bytes);
				String userName=new String(bytes, "UTF-8").trim();
				
				//��ȡ����
				raf.read(bytes);
				String userPwd=new String(bytes, "UTF-8").trim();
				
				//��ȡ�ǳ�
				raf.read(bytes);
				String userNick=new String(bytes, "UTF-8").trim();
				
				//��ȡ����
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
			builder.append("<a href='myweb/web/index.html'>������ҳ</a>");
			builder.append("</div>");
			builder.append("</body>");
			builder.append("</html>");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			String str=builder.toString();
			byte[] data=str.getBytes("UTF-8");
			
			//������Ӧͷ
			response.putHeaders("Content-Type", "text/html");
			response.putHeaders("Content-Length", data.length+"");
			
			//������Ӧ����
			response.setData(data);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
