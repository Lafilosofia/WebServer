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
 * Http请求
 * 该类的每一个实例用于表示客户端发送过来的请求内容
 * @author 刘坤
 *
 */
public class HttpRequest {
	private Socket socket;
	
	/**输入流*/
	private InputStream in;
	
	/**请求方式*/
	private String method;
	
	/**请求资源路径*/
	private String url;
	
	/**请求所使用的协议版本*/
	private String  protocol;
	
	/**请求资源路径url中"?"的左边内容*/
	private String requestURI;
	
	/**请求资源路径url中"?"的右边内容*/
	private String quertString;
	
	/**存储请求参数*/
	Map<String,String> parameter = new HashMap<String,String>();
	
	/**存储消息头*/
	Map<String,String> headers = new HashMap<String,String>();
	
	public HttpRequest(Socket socket){//HttpRequest初始化
		try {
			this.socket = socket;
			//通过socket获取输入流
			this.in = socket.getInputStream();
			/*
			 * 处理客户端请求数据
			 * 1.解析请求行
			 * 2.解析消息头
			 * 3.解析消息正文(post请求才有消息正文)
			 */
			// 1.解析请求行
				parseRequestLine();
				
			// 2.解析消息头
				parseHeaders();
				
			// 3.解析消息正文
				parseContent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//解析消息正文
	private void parseContent() {
		/*
		 * 当一个请求包含消息正文时,会在消息头中出现Content-Length说明长度,以及
		 * Content-Type说明参数内容类型,若消息头中不含有这两项属性说明没有消息正文.
		 */
		if(this.headers.containsKey("Content-Length")){
			//获取headers中Content-Length的值
			String length = headers.get("Content-Length");
			//将length转换为int类型
			int len = Integer.parseInt(length);
			//获取headers中Content-Type的值
			String typeValue = headers.get("Content-Type");
			//判断是否为post提交的form表单数据
			if("application/x-www-form-urlencoded".equals(typeValue)){
				byte[] data = new byte[len];
				try {
					in.read(data);
					//获取消息正文内容(请求参数)
					String str = new String(data,"ISO8859-1");
					parseParamters(str);//解析请求参数
					//System.out.println("获取消息正文的内容(请求参数)是:"+str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	//解析消息头
	private void parseHeaders() {
		/*
		 * 循环读取每一行(若干消息头),当读取的这行字符串是空字符串时,
		 * 说明单独读取到了CRLF,那么就可以停止读取消息头操作.
		 * 
		 * 每读取一个消息头时,将消息头的名字作为key,消息头的值作为value存入
		 * 到headers这个Map集合中,最终完成消息头工作.
		 */
		while(true){
			String line = readLine();
			if(line.equals("")){//读到一行为空字符串,则消息头读完
				break;
			}
			String[] arr = line.split(":\\s");
			String key = arr[0];
			String value = arr[1];	
			headers.put(key, value);
		}
		//测试消息头中数据
		Set<Entry<String,String>> entry = headers.entrySet();
		for(Entry<String,String> e:entry){
			System.out.println(e.getKey() + ": " + e.getValue());
			
		}
		System.out.println("解析消息头完毕!");
	}






	//解析请求行
	private void parseRequestLine(){
		/*
		 * 1.通过输入流读取一行字符串,相当于读取了请求行内容;
		 * 2.按照空格拆分请求行,可以得到对应的三部分内容
		 * 3.分别将method,url,protocol设置带对应的属性完成请求行的解析工作
		 */
		String line = readLine();
		String[] data = line.split("\\s");
		//解决空请求异常处理
		if(data.length < 3){
			//System.out.println("空请求!");
			throw new EmptyRequestException();
			
		}
		this.method = data[0];
		this.url = data[1];
		this.protocol = data[2];
		
		//System.out.println("请求方式:"+method+";请求资源路径:"+url+";请求http版本:"+protocol);
		//继续解析路径资源url
		parseURL();
		
	}
	
	//解析路径资源url
	/*
	 * 由于请求有两种情况,带参数或者不带参数,若带参数,则需要按照"?"先拆分url
	 * ,然后将"?"左边内容设置到requestURI中,将"?"右边的内容设置到quertString中,
	 * 并进一步对参数部分解析,将每个参数解析出来存入到集合中
	 */
	private void parseURL(){
		
		try {
			/*
			 * 将url解码,由于url中在传递像中文这样非ISO8859-1字符集所支持的字符时,会被
			 * 浏览器将其中的这些字符以"XX%"的形式转码后发送,所以,要对url中的所有"XX%"
			 * 内容进行解码
			 */
			//System.out.println("解码之前的url:"+url);
			//this.url=URLDecoder.decode(url,"UTF-8");
			//System.out.println("解码之后的url:"+url);
			
			if(this.url.contains("?")){//判断此url中是否包含"? "
				String[] arrURL = url.split("\\?");
				this.requestURI = arrURL[0];
				if(arrURL.length>1){
					this.quertString = arrURL[1];
					//System.out.println("?右边的内容是:"+quertString);
					//解析quertString
						parseParamters(quertString);
				}else{
					this.quertString = "";//参数为空字符串
				}
				
			}else{
				this.requestURI = url;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//解析quertString(解析"?"后面的参数,例如:username=ceshi&password=123456)
	private void parseParamters(String quertString){
		//按照UTF-8形式将"XX%"的形式解码
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
					//例如:username=    我们可以设置key为username,value为""
					parameter.put(s[0], "");
				}
				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//测试参数的数据
		/*Set<Entry<String,String>> entry=parameter.entrySet();
		for(Entry<String,String> e:entry){
			String key=e.getKey();
			String value=e.getValue();
			System.out.println(key+":"+value);
		}*/
		
	}
	
	
	
	
	
	
	//读取请求行
	private String readLine(){
		try {
			/*
			 * 读取一行字符串,以CRLF结尾为一行
			 * 顺序从in中读取每个字符,当连接读取了CR,LF时停止并将之前
			 * 读取的字符转换为字符串
			 */
			StringBuilder builder = new StringBuilder();
			char c1 = 'a';//表示上次读取到的字符
			char c2 = 'a';//表示当前(本次)读取的字符
			int d = -1;
			while((d = in.read()) != -1){
					c2 = (char)d;
					if(c1 == 13 && c2 == 10){
						break;
					}
					builder.append(c2);
					c1 = c2;
			}
//			CR:回车符,对应编码:13
//			LF:换行符,对应编码:10 
//			在字符串中最后剩余一个CR(回车符,也就是空格符),所以需要清除空格trim()
			return builder.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//获取请求地址
	public String getRequestURI() {
		return requestURI;
	}

	
	
	//获取给定名字对应的参数
	public String getParameter(String name) {//?username=admin&password=12345
		String value = this.parameter.get(name);
		return value;
	}	
	
}