package http;

import javax.swing.text.Document;
import javax.swing.text.Element;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

/**
 * http协议相关内容定义
 */
public class HttpContext {
    /*介质类型映射
      key 资源后缀
      value Content-Type中对应的值*/
    private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
    /*状态码与状态描述映射
      key 状态码
      value 对应的状态描述
     */
    private static Map<String,String > STATUS_CODE_REASON_MAPPING = new HashMap<String, String>();
        static {
            //初始化
            initMimeMapping();
            initStatusCodeReasonMapping();
        }

    //初始化介质类型映射
    private static void initMimeMapping(){
            MIME_MAPPING.put("html","text/html");
            MIME_MAPPING.put("png","image/png");
            MIME_MAPPING.put("css","text/css");
            MIME_MAPPING.put("js","application/javascript");
            MIME_MAPPING.put("gif","image/gif");
            MIME_MAPPING.put("jpg","image/jpg");

            //1.创建SAXReader实例
        SAXReader reader = new SAXReader();
        try{
            //2.获取Doument实例
            File file = new File("conf/web.xml");
            Document doc = reader.read(file);
            //3.获取跟标签<web-app>
            Element root = doc.getRootElements();
            //4.获取跟标签<web-app>下指定的子标签<mime-mapping>
            List<Element> element = root.elements("mime-mapping");
            //5.逐级获取子标签
            element.forEach((e)->{
                //获取标签<mime-mapping>下的子标签的文本
                //1.获取<extension>标签中的文本
                String key = e.elementText("extension");
                //2.获取<mime-type>标签中的文本
                String value = e.elementText("mime-type");
                MIME_MAPPING.put(key,value);
            });
            System.out.println("MAP集合的大小:" + MIME_MAPPING.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //初始化状态码与状态描述的映射
    private static void initStatusCodeReasonMapping(){
            STATUS_CODE_REASON_MAPPING.put(200,"OK");
            STATUS_CODE_REASON_MAPPING.put(201,"Created");
            STATUS_CODE_REASON_MAPPING.put(202,"Accepted");
            STATUS_CODE_REASON_MAPPING.put(204,"No Content");
            STATUS_CODE_REASON_MAPPING.put(301,"Moved Permanently");
            STATUS_CODE_REASON_MAPPING.put(302,"Moved Temporarily");
            STATUS_CODE_REASON_MAPPING.put(304."Not Modified");
            STATUS_CODE_REASON_MAPPING.put(400,"Bad Request");
            STATUS_CODE_REASON_MAPPING.put(401,"Unauthorized");
            STATUS_CODE_REASON_MAPPING.put(403,"Forbidden");
            STATUS_CODE_REASON_MAPPING.put(404,"Not Found");
            STATUS_CODE_REASON_MAPPING.put(500,"Internal Server Error");
            STATUS_CODE_REASON_MAPPING.put(501,"Not Implemented");
            STATUS_CODE_REASON_MAPPING.put(502,"Bad Gateway");
            STATUS_CODE_REASON_MAPPING.put(503,"Service Unavailable");
    }
    //根据给定的资源后缀名获取对应的Content-Type的值
    public static String getMimeType(String key) {
        String value = MIME_MAPPING.get(key);
        return value;
    }
    //根据给定的状态码获取对应的状态描述
    public static String getStatusReason(int code){
            String reason = STATUS_CODE_REASON_MAPPING.get(code);
            return reason;
    }

    public static void main(String[] args) {
        String v = HttpContext.getMimeType("css");
        System.out.println(v);
        System.out.println(getStatusReason(404));
    }

}
