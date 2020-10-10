package http;

import com.sun.xml.internal.ws.api.server.WSEndpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

/**
 * http请求
 * 该类的每一个实例用于表示响应给客户端的每一条数据
 */
public class HttpResponse {
    private Socket socket;
    private OutputStream out;
    private File entity;
    /*响应头相关的信息定义:
     * key:响应头的名字
     * value:响应头对应的值*/
    private Map<String,String> headers = new HashMap<String,String>();
    //状态行相关信息定义
    //状态码
    private int statusCode = 200;
    //状态描述
    private String statusReason = "OK";
    //用户信息字节数
    private byte[] data;

    public HttpResponse(Socket socket){
        try{
            this.socket = socket;
            this.out = this.socket.getOutputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将当前响应内容以http响应的格式回复给客户端,需要按照http响应格式来发送
    public void flush(){
        //1.状态行
        sendStatusLine();
        //2.响应头
        sendHeader();
        //3.响应正文
        sendContent();
    }

    //响应状态行
    private void sendStatusLine(){
        String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
        println(line);
    }

    //响应响应头
    private void sendHeader(){
        /*
         * Content-Type: text/html(CRLF)
         * Content-Length: 1235(CRLF)
         * (CRLF)
         */
        //遍历headers这个Map,将所有的响应头发送
        String type  = "Content-Type:text/html";
        println(type);
        String length = "Content-Length:" + entity.length();
        println(length);
        println("");    //单独发送一个""空字符串表示响应头结尾
        Set<Map.Entry<String,String>> entry = headers.entrySet();
        for (Map.Entry<String,String> e:entry){
            String key = e.getKey();
            String value = e.getValue();
            println(key + ":" + value);
        }
        println("");    //单独发送一个""空字符串表示响应头结尾
    }

    //响应正文
    private void sendContent(){
        if (entity != null){
            //jdk1.7版本以上利用以下方式可以自动关闭流
            try(FileInputStream fis = new FileInputStream(entity);){
                byte[] bytes = new byte[1024 * 10];
                int len = -1;
                while((len = fis.read(bytes)) != -1) out.write(bytes,0,len);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            //响应所有用户数据(服务端自己写的前端相关代码和动态数据)
            try{
                out.write(data);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void println(String str){
        try{
            out.write(str.getBytes("ISO8859-1"));
            out.write(13);  //CR回车
            out.write(10);  //LF换行
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //设置实体文件
    public void setEntity(File entity){
        String entityName = entity.getName();
        String regex = "^.+\\.[a-zA-Z0-9] + $";
        if (entityName.matches(regex)){
            //获取文件后缀名
            String[] str = entityName.split("\\.");
            String ext = str[1];    //文件后缀
            //例如:后缀key(ext)value(ext所对应的那个类型)
            headers.put("Content-Type",HttpContext.getMimeType(ext));
            headers.put("Content-Length",entity.length() + "");
        }
        this.entity = entity;
    }

    //设置状态码
    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;   //状态码
        this.statusReason = HttpContext.getStatusReason(statusCode);    //状态码对应的状态描述
    }

    /* 描述重定向和转发的区别
     * 重定向是两次请求(服务端用了两次request接收,两次response响应)
     * 重定向后地址发生变化为第二次请求的页面
     * 转发之后地址栏不发生变化(服务端用了一次request请求,一次response响应),请求
     * 还是第一次的地址*/

    //重定向
    public void sendResirect(String url){
        setStatusCode(302); //设置重定向状态码和状态描述
        this.headers.put("Location",url);
    }

    //给响应头添加数据
    public void putHeaders(String key,String value){
        headers.put(key,value);
    }

    //设置data
    public void setData(byte[] data){
        this.data = data;
    }
}
