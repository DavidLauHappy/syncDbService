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
				Logger.logService(serviceName, "������Ƕ��httpserver�����쳣:"+e.toString());
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
		   Logger.logService(serviceName, "��ʼ������Ƕ��httpserver����");
		   HttpServerProvider provider = HttpServerProvider.provider();  
		   //����8080�˿ڣ���ͬʱ����100������
			HttpServer server=provider.createHttpServer(new InetSocketAddress(8080), 100);
			server.createContext(startPath,new MyHttpHandler());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			Logger.logService(serviceName, "������Ƕ��httpserver�ɹ���ɡ���");
	}
	
}
