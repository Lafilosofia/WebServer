package src.com.ykt.webServer.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Http��Ӧ
 * �����ÿһ��ʵ�����ڱ�ʾ��Ӧ���ͻ��˵�ÿһ������
 * @author ����
 *
 */
public class HttpResponse {
	private Socket socket;
	private OutputStream out;
	private File entity;

	/*
	 * ��Ӧͷ��ص���Ϣ����:
	 * key:��Ӧͷ������
	 * value:��Ӧͷ��Ӧ��ֵ
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	/*
	 * ״̬�������Ϣ����
	 */
	//״̬��
	private int statusCode = 200;
	//״̬����
	private String statusReason = "OK";
	//�û���Ϣ�ֽ���
	private byte[] data;
	public HttpResponse(Socket socket){
		try {
			this.socket = socket;
			this.out=this.socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	//����ǰ��Ӧ������HTTP��Ӧ�ĸ�ʽ�ظ����ͻ�����Ҫ����
	//HTTP��Ӧ��ʽ������
	public void flush(){
		//1.״̬��
		sendStatusLine();
		
		//2.��Ӧͷ
		sendHeader();
		
		//3.��Ӧ����   
		sendContent();
	}
	
	//��Ӧ״̬��
	private void sendStatusLine() {
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
			println(line);
	}
	
	//��Ӧ ��Ӧͷ
	private void sendHeader(){
			//����headers���Map��,���е���Ӧͷ����
//			String type="Content-Type: text/html";
//			println(type);
//			String length="Content-Length: "+entity.length();
//			println(length);
//			println("");//��������һ��""��ʾ��Ӧͷ��β
			Set<Entry<String,String>> entry =  headers.entrySet();
			for(Entry<String,String> e:entry){
				String key = e.getKey();
				String value=e.getValue();
				println(key + ": " + value);
			}
			println("");//��������һ��""��ʾ��Ӧͷ��β
	}			
	
	//��Ӧ����
	private void sendContent(){
		//JDK1.7�汾�����������·�ʽ�����Զ��ر���
		try(FileInputStream fis = new FileInputStream(entity);){
			byte[] bytes = new byte[1024*10];
			int len = -1;
			while((len = fis.read(bytes)) != -1){
				out.write(bytes,0,len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private  void println(String str){
		try {
			out.write(str.getBytes("ISO8859-1"));
			out.write(13);//CR �س�
			out.write(10);//LF ����
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	//����ʵ���ļ�
	public void setEntity(File entity) {
		String entityName = entity.getName();  //index162_.html
		String regex = ".+\\.[a-zA-Z0-9]+";
		if(entityName.matches(regex)){
			//��ȡ�ļ��ĺ�׺��
			String[] str = entityName.split("\\.");
			String ext = str[1];//�ļ���׺
		//	����:��׺:key(ext)   value(ext����Ӧ���Ǹ�����)
			headers.put("Content-Type",HttpContext.getMimeType(ext));
			headers.put("Content-Length", entity.length() + "");
		}
		this.entity = entity;
	}

	//����״̬��
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;//״̬��
		this.statusReason = HttpContext.getStatusReason(statusCode);//״̬���Ӧ��״̬����
	}

	/*
	 * �����ض����ת��������
	 * �ض�������������(�������������request����,����response��Ӧ)
	 * �ض�����ַ�����仯Ϊ�ڶ��������ҳ��
	 * 
	 * ת��֮���ַ���������仯(���������һ��request����,һ��response��Ӧ),����
	 * ���ǵ�һ�εĵ�ַ
	 */
	
	/**�ض���*/
	public void sendRedirect(String url){
		setStatusCode(302);//�����ض���״̬���״̬����
		this.headers.put("Location", url);
	}
	
	/**����Ӧͷ�������*/
	public void putHeaders(String key,String value){
		headers.put(key, value);
	}

	/**����data*/
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}

