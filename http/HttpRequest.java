package src.com.ykt.webServer.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
/**
 * Http����
 * �����ÿһ��ʵ�����ڱ�ʾ�ͻ��˷��͹�������������
 * @author ����
 *
 */
public class HttpRequest {
	private Socket socket;
	
	/**������*/
	private InputStream in;
	
	/**����ʽ*/
	private String method;
	
	/**������Դ·��*/
	private String url;
	
	/**������ʹ�õ�Э��汾*/
	private String  protocol;
	
	/**������Դ·��url��"?"���������*/
	private String requestURI;
	
	/**������Դ·��url��"?"���ұ�����*/
	private String quertString;
	
	/**�洢�������*/
	Map<String,String> parameter = new HashMap<String,String>();
	
	/**�洢��Ϣͷ*/
	Map<String,String> headers = new HashMap<String,String>();
	
	public HttpRequest(Socket socket){//HttpRequest��ʼ��
		try {
			this.socket = socket;
			//ͨ��socket��ȡ������
			this.in = socket.getInputStream();
			/*
			 * ����ͻ�����������
			 * 1.����������
			 * 2.������Ϣͷ
			 * 3.������Ϣ����(post���������Ϣ����)
			 */
			// 1.����������
				parseRequestLine();
				
			// 2.������Ϣͷ
				parseHeaders();
				
			// 3.������Ϣ����
				parseContent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//������Ϣ����
	private void parseContent() {
		/*
		 * ��һ�����������Ϣ����ʱ,������Ϣͷ�г���Content-Length˵������,�Լ�
		 * Content-Type˵��������������,����Ϣͷ�в���������������˵��û����Ϣ����.
		 */
		if(this.headers.containsKey("Content-Length")){
			//��ȡheaders��Content-Length��ֵ
			String length = headers.get("Content-Length");
			//��lengthת��Ϊint����
			int len = Integer.parseInt(length);
			//��ȡheaders��Content-Type��ֵ
			String typeValue = headers.get("Content-Type");
			//�ж��Ƿ�Ϊpost�ύ��form������
			if("application/x-www-form-urlencoded".equals(typeValue)){
				byte[] data = new byte[len];
				try {
					in.read(data);
					//��ȡ��Ϣ��������(�������)
					String str = new String(data,"ISO8859-1");
					parseParamters(str);//�����������
					//System.out.println("��ȡ��Ϣ���ĵ�����(�������)��:"+str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	//������Ϣͷ
	private void parseHeaders() {
		/*
		 * ѭ����ȡÿһ��(������Ϣͷ),����ȡ�������ַ����ǿ��ַ���ʱ,
		 * ˵��������ȡ����CRLF,��ô�Ϳ���ֹͣ��ȡ��Ϣͷ����.
		 * 
		 * ÿ��ȡһ����Ϣͷʱ,����Ϣͷ��������Ϊkey,��Ϣͷ��ֵ��Ϊvalue����
		 * ��headers���Map������,���������Ϣͷ����.
		 */
		while(true){
			String line = readLine();
			if(line.equals("")){//����һ��Ϊ���ַ���,����Ϣͷ����
				break;
			}
			String[] arr = line.split(":\\s");
			String key = arr[0];
			String value = arr[1];	
			headers.put(key, value);
		}
		//������Ϣͷ������
		Set<Entry<String,String>> entry = headers.entrySet();
		for(Entry<String,String> e:entry){
			System.out.println(e.getKey() + ": " + e.getValue());
			
		}
		System.out.println("������Ϣͷ���!");
	}






	//����������
	private void parseRequestLine(){
		/*
		 * 1.ͨ����������ȡһ���ַ���,�൱�ڶ�ȡ������������;
		 * 2.���տո���������,���Եõ���Ӧ������������
		 * 3.�ֱ�method,url,protocol���ô���Ӧ��������������еĽ�������
		 */
		String line = readLine();
		String[] data = line.split("\\s");
		//����������쳣����
		if(data.length < 3){
			//System.out.println("������!");
			throw new EmptyRequestException();
			
		}
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		
		//System.out.println("����ʽ:"+method+";������Դ·��:"+url+";����http�汾:"+protocol);
		//��������·����Դurl
		parseURL();
		
	}
	
	//����·����Դurl
	/*
	 * �����������������,���������߲�������,��������,����Ҫ����"?"�Ȳ��url
	 * ,Ȼ��"?"����������õ�requestURI��,��"?"�ұߵ��������õ�quertString��,
	 * ����һ���Բ������ֽ���,��ÿ�����������������뵽������
	 */
	private void parseURL(){
		
		try {
			/*
			 * ��url����,����url���ڴ���������������ISO8859-1�ַ�����֧�ֵ��ַ�ʱ,�ᱻ
			 * ����������е���Щ�ַ���"XX%"����ʽת�����,����,Ҫ��url�е�����"XX%"
			 * ���ݽ��н���
			 */
			//System.out.println("����֮ǰ��url:"+url);
			//this.url=URLDecoder.decode(url,"UTF-8");
			//System.out.println("����֮���url:"+url);
			
			if(this.url.contains("?")){//�жϴ�url���Ƿ����"? "
				String[] arrURL = url.split("\\?");
				this.requestURI = arrURL[0];
				if(arrURL.length>1){
					this.quertString = arrURL[1];
					//System.out.println("?�ұߵ�������:"+quertString);
					//����quertString
						parseParamters(quertString);
				}else{
					this.quertString = "";//����Ϊ���ַ���
				}
				
			}else{
				this.requestURI = url;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//����quertString(����"?"����Ĳ���,����:username=ceshi&password=123456)
	private void parseParamters(String quertString){
		//����UTF-8��ʽ��"XX%"����ʽ����
		try {
			quertString = URLDecoder.decode(quertString,"UTF-8");
			String[] parmters = quertString.split("&");
			for(int i = 0;i < parmters.length;i ++){
				String[] s = parmters[i].split("=");
				if(s.length > 1){
					String key = s[0];
					String value = s[1];
					parameter.put(key,value);
				}else{
					//����:username=    ���ǿ�������keyΪusername,valueΪ""
					parameter.put(s[0], "");
				}
				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//���Բ���������
		/*Set<Entry<String,String>> entry=parameter.entrySet();
		for(Entry<String,String> e:entry){
			String key=e.getKey();
			String value=e.getValue();
			System.out.println(key+":"+value);
		}*/
		
	}
	
	
	
	
	
	
	//��ȡ������
	private String readLine(){
		try {
			/*
			 * ��ȡһ���ַ���,��CRLF��βΪһ��
			 * ˳���in�ж�ȡÿ���ַ�,�����Ӷ�ȡ��CR,LFʱֹͣ����֮ǰ
			 * ��ȡ���ַ�ת��Ϊ�ַ���
			 */
			StringBuilder builder = new StringBuilder();
			char c1 = 'a';//��ʾ�ϴζ�ȡ�����ַ�
			char c2 = 'a';//��ʾ��ǰ(����)��ȡ���ַ�
			int d = -1;
			while((d = in.read()) != -1){
					c2 = (char)d;
					if(c1 == 13 && c2 == 10){
						break;
					}
					builder.append(c2);
					c1 = c2;
			}
//			CR:�س���,��Ӧ����:13
//			LF:���з�,��Ӧ����:10 
//			���ַ��������ʣ��һ��CR(�س���,Ҳ���ǿո��),������Ҫ����ո�trim()
			return builder.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//��ȡ�����ַ
	public String getRequestURI() {
		return requestURI;
	}

	
	
	//��ȡ�������ֶ�Ӧ�Ĳ���
	public String getParameter(String name) {//?username=admin&password=12345
		String value = this.parameter.get(name);
		return value;
	}	
	
}