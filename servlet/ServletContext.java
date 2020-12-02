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
	 * Servlet映射信息
	 * key:url
	 * value:url对应的Servlet名字
	 */
	private static Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}

	/**初始化Servlet映射信息*/
	private static void initServletMapping() {
		//解析servlets.xml并存入SERVLET_MAPPING集合当中
		//1.创建SAXReader实例
		SAXReader reader = new SAXReader();
		
		try {
			//2.获取Document实例
			File file = new File("conf/servlets.xml");
			Document doc = reader.read(file);
			
			//3.获取跟标签
			Element root = doc.getRootElement();
			
			//4.获取跟标签下的子标签
			List<Element> elements = root.elements("servlet");
			
			//5.逐级获取子标签中的内容
			elements.forEach((e)->{
				String key = e.elementText("url");
				String value = e.elementText("className");
				
				//为MAP集合添加数据
				SERVLET_MAPPING.put(key, value);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**检查给定的url是否对应Servlet处理*/
	public static boolean hasServlet(String url){
		//给定的url检查是否作为key存在SERVLET_MAPPING集合中
		boolean flag = SERVLET_MAPPING.containsKey(url);
		return flag;
	}
	
	/**根据给定的url获取对应的servlet名字*/
	public static String getServletByUrl(String url){
		String value = SERVLET_MAPPING.get(url);
		return value;
	}
	
	public static void main(String[] args) {
		//测试SERVLET_MAPPING集合中的所有数据
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
