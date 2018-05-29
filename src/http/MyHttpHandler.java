package http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyHttpHandler implements HttpHandler {

	@Override
	public void handle(final HttpExchange exchange) throws IOException {
		  new Thread(new Runnable(){
			   public void run(){
				   try{
					   String requestMethod = exchange.getRequestMethod();
					   //post��get��֧��
					   if("GET".equalsIgnoreCase(requestMethod)){
						    String uri=exchange.getRequestURI().getPath();
						    uri=uri.replace(SimpleHTTPServer.startPath, "");
						    uri=uri.replace("/", "");
						    String queryParameter=exchange.getRequestURI().getQuery();
						    ApiAction action=LocalAPIFactory.createAction(uri, queryParameter);
						    action.execute();
						    String response=action.getResult();
						    byte[] responseContent=response.getBytes("UTF-8");
						    //������Ӧͷ��
						    Headers responseHeaders = exchange.getResponseHeaders();  
				            responseHeaders.set("Content-Type", "text/plain");  
				           //������Ӧcode�����ݳ���
				            exchange.sendResponseHeaders(200, responseContent.length);  
				            OutputStream responseBody  = exchange.getResponseBody();
				            //������Ӧ����
					        responseBody.write(responseContent);
					        responseBody.close();
					   }else{
						   
					   }
					
					/* //���get��ʽ�Ĳ���
					 String queryString=exchange.getRequestURI().getQuery();
					 Map<String,String> queryParameters = DataUtils.getData(queryString);
					 //���post��ʽ�Ĳ���
					 String postString = IOUtils.toString(exchange.getRequestBody());
					 Map<String,String> postParameters = DataUtils.getData(postString);*/
					 
	
			        
				   }catch(Exception e){
					   
				   }
			   }
		  }).start();
	}
	

}
