package src.com.ykt.webServer.http;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTPЭ��������ݶ���
 * @author 86180
 *
 */
public class HttpContext {
	/*
	 * ��������ӳ��
	 * key:    ��Դ��׺
	 * value:  Content-Type�ж�Ӧ��ֵ
	 */
	private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	/*
	 * ״̬����״̬����ӳ��
	 * key:״̬��
	 * value:��Ӧ��״̬����
	 */
	private static Map<Integer,String> STATUS_CODE_REASON_MAPPING = new HashMap<Integer,String>();
	static{
		//��ʼ��
		initMimeMapping();
		initStatusCodeReasonMapping();
	}
	
	//��ʼ����������ӳ��
	private static void initMimeMapping() {
		MIME_MAPPING.put("html", "text/html");
		MIME_MAPPING.put("png", "image/png");
		MIME_MAPPING.put("css", "text/css");
		MIME_MAPPING.put("js", "application/javascript");
		MIME_MAPPING.put("gif", "image/gif");
		MIME_MAPPING.put("jpg", "image/jpeg");
		
	}
	//��ʼ��״̬����״̬������ӳ��
	private static void initStatusCodeReasonMapping() {
		STATUS_CODE_REASON_MAPPING.put(200, "OK"); 
		STATUS_CODE_REASON_MAPPING.put(201, "Created");
		STATUS_CODE_REASON_MAPPING.put(202, "Accepted");  
		STATUS_CODE_REASON_MAPPING.put(204, "No Content");  
		STATUS_CODE_REASON_MAPPING.put(301, "Moved Permanently"); 
		STATUS_CODE_REASON_MAPPING.put(302, "Moved Temporarily");  
		STATUS_CODE_REASON_MAPPING.put(304, "Not Modified"); 
		STATUS_CODE_REASON_MAPPING.put(400, "Bad Request"); 
		STATUS_CODE_REASON_MAPPING.put(401, "Unauthorized"); 
		STATUS_CODE_REASON_MAPPING.put(403, "Forbidden");
		STATUS_CODE_REASON_MAPPING.put(404, "NO Found");
		STATUS_CODE_REASON_MAPPING.put(500, "Internal Server Error");
		STATUS_CODE_REASON_MAPPING.put(501, "Not Implemented");
		STATUS_CODE_REASON_MAPPING.put(502, "Bad Gateway"); 
		STATUS_CODE_REASON_MAPPING.put(503, "Service Unavailable");
	}

	//���ݸ�������Դ��׺����ȡ��Ӧ��Content-type��ֵ
	public static String getMimeType(String key){
		String value = MIME_MAPPING.get(key);
		return value;
		
	}
	
	//���ݸ�����״̬���ȡ��Ӧ��״̬����
	public static String getStatusReason(int code){
		String reason = STATUS_CODE_REASON_MAPPING.get(code);
		return reason;
	}
	
	
	public static void main(String[] args) {
			String v = HttpContext.getMimeType("js");
			System.out.println(v);
		//System.out.println(HttpContext.getStatusReason(404));
	}
}

