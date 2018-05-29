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
					   //post与get的支持
					   if("GET".equalsIgnoreCase(requestMethod)){
						    String uri=exchange.getRequestURI().getPath();
						    uri=uri.replace(SimpleHTTPServer.startPath, "");
						    uri=uri.replace("/", "");
						    String queryParameter=exchange.getRequestURI().getQuery();
						    ApiAction action=LocalAPIFactory.createAction(uri, queryParameter);
						    action.execute();
						    String response=action.getResult();
						    byte[] responseContent=response.getBytes("UTF-8");
						    //设置响应头部
						    Headers responseHeaders = exchange.getResponseHeaders();  
				            responseHeaders.set("Content-Type", "text/plain");  
				           //设置响应code和内容长度
				            exchange.sendResponseHeaders(200, responseContent.length);  
				            OutputStream responseBody  = exchange.getResponseBody();
				            //设置相应内容
					        responseBody.write(responseContent);
					        responseBody.close();
					   }else{
						   
					   }
					
					/* //获得get方式的参数
					 String queryString=exchange.getRequestURI().getQuery();
					 Map<String,String> queryParameters = DataUtils.getData(queryString);
					 //获得post方式的参数
					 String postString = IOUtils.toString(exchange.getRequestBody());
					 Map<String,String> postParameters = DataUtils.getData(postString);*/
					 
	
			        
				   }catch(Exception e){
					   
				   }
			   }
		  }).start();
	}
	

}
