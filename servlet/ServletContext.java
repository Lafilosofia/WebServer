package src.com.ykt.webServer.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServletContext {
	/*
	 * Servletӳ����Ϣ
	 * key:url
	 * value:url��Ӧ��Servlet����
	 */
	private static Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}

	/**��ʼ��Servletӳ����Ϣ*/
	private static void initServletMapping() {
		//����servlets.xml������SERVLET_MAPPING���ϵ���
		//1.����SAXReaderʵ��
		SAXReader reader = new SAXReader();
		
		try {
			//2.��ȡDocumentʵ��
			File file = new File("conf/servlets.xml");
			Document doc = reader.read(file);
			
			//3.��ȡ����ǩ
			Element root = doc.getRootElement();
			
			//4.��ȡ����ǩ�µ��ӱ�ǩ
			List<Element> elements = root.elements("servlet");
			
			//5.�𼶻�ȡ�ӱ�ǩ�е�����
			elements.forEach((e)->{
				String key = e.elementText("url");
				String value = e.elementText("className");
				
				//ΪMAP�����������
				SERVLET_MAPPING.put(key, value);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**��������url�Ƿ��ӦServlet����*/
	public static boolean hasServlet(String url){
		//������url����Ƿ���Ϊkey����SERVLET_MAPPING������
		boolean flag = SERVLET_MAPPING.containsKey(url);
		return flag;
	}
	
	/**���ݸ�����url��ȡ��Ӧ��servlet����*/
	public static String getServletByUrl(String url){
		String value = SERVLET_MAPPING.get(url);
		return value;
	}
	
	public static void main(String[] args) {
		//����SERVLET_MAPPING�����е���������
		//SERVLET_MAPPING.forEach((k,v)->System.out.println(k+":"+v));
		
		/*Set<Entry<String,String>> entry=SERVLET_MAPPING.entrySet();
		for(Entry<String,String> e:entry){
			String key=e.getKey();
			String value=e.getValue();
			System.out.println(key+":"+value);
		}*/
		
		//System.out.println(hasServlet("/regServlet"));
		//System.out.println(getServletByUrl("/regServlet"));
		
	}
}
