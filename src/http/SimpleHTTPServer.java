package http;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import com.utils.Logger;

public class SimpleHTTPServer {
	
	public static SimpleHTTPServer getInstance(){
		if(httpServer==null)
			httpServer=new SimpleHTTPServer();
		return httpServer;
	}
	
	public void run(){
		if(!this.alreadyRunFlag){
			try {
				this.alreadyRunFlag=true;
				this.init();
			} catch (Exception e) {
				Logger.logService(serviceName, "启动内嵌的httpserver发生异常:"+e.toString());
			}
		}
	}
	
	private static SimpleHTTPServer httpServer;
	private boolean alreadyRunFlag=false;
	private SimpleHTTPServer(){
		this.alreadyRunFlag=false;
	}
	public static String startPath="/openAPI";
	public static final String serviceName="http";
	public void init() throws Exception{
		   Logger.logService(serviceName, "开始启动内嵌的httpserver……");
		   HttpServerProvider provider = HttpServerProvider.provider();  
		   //监听8080端口，能同时接受100个请求
			HttpServer server=provider.createHttpServer(new InetSocketAddress(8080), 100);
			server.createContext(startPath,new MyHttpHandler());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			Logger.logService(serviceName, "启动内嵌的httpserver成功完成……");
	}
	
}
