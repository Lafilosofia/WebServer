package core;

import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import http.EmptyRequesException;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * 线程任务类,用于处理某个客户端的请求并予以响应
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            //处理客户端请求数据
            HttpRequest request = new HttpRequest(socket) ;
            //获取请求地址
            String uri = request.doGetRequestURI();
            System.out.println("请求地址:" + uri);
            //响应客户端数据
            HttpResponse response = new HttpResponse(socket);
            // ../表示当前目录的上一级目录
            File file = new File("webapps" + uri);
            //判断请求的uri在集合中时是否存在,进行数据处理
            if (ServletContext.hasServlet(uri)){
                String className = ServletContext.getServletByUrl(uri);
                //通过反射获取类
                Class cls = Class.forName(className);
                //动态创建对象
                HttpServlet servlet = (HttpServlet)cls.newInstance();
                servlet.service(request,response);
            //判断文件是否存在
            }else {
                if(file.exists()) response,setEntity(file);
                else{
                    //也要响应一个"错误页面"
                    File f = new File("webapps/error.html");
                    response.setStatusCode(404);    //响应错误404
                    response.setEntity(f);
                }
            }
            response.flush();
        }catch (EmptyRequesException e){
            System.out.println("空请求!");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
