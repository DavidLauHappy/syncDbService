package com.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlServerUtil {
	
	public static final String serviceName="SqlServer";
	public static List executeQuery(String sql,Connection conn){
		Logger.logService(serviceName,"执行sql:"+sql);
		List result=new ArrayList();
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql);
			ResultSetMetaData rsmd=rs.getMetaData();
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				result.add(map);
			}	
		}catch(Exception e){
			Logger.logService(serviceName,"执行sql:"+sql+"异常:"+e.toString());
			Logger.error("执行sql:"+sql+"异常:"+e.toString());
			throw new RuntimeException("执行["+sql+"]异常："+e.toString());
		}finally{
			if(rs!=null){
				try{
					if (rs != null) rs.close();
					if (stmt != null) stmt.close();
				}catch(Exception e){
					Logger.logService(serviceName,"执行sql:"+sql+"回收资源异常常:"+e.toString());
					Logger.error("执行sql:"+sql+"回收资源异常常:"+e.toString());
					throw new RuntimeException("执行["+sql+"]回收资源异常："+e.toString());
				}
			}
		}
		return result;
	}
	
	
	public static int executeUpdate(String sql,Connection conn){
		Logger.logService(serviceName,"执行sql:"+sql);
		int result=0;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			stmt=conn.createStatement();
			result=stmt.executeUpdate(sql);
		}catch(Exception e){
			Logger.logService(serviceName,"执行sql:"+sql+"异常:"+e.toString());
			Logger.error("执行sql:"+sql+"异常:"+e.toString());
			throw new RuntimeException("执行["+sql+"]异常："+e.toString());
		}finally{
			if(rs!=null){
				try{
					if (rs != null) rs.close();
					if (stmt != null) stmt.close();
				}catch(Exception e){
					Logger.logService(serviceName,"执行sql:"+sql+"回收资源异常常:"+e.toString());
					Logger.error("执行sql:"+sql+"回收资源异常常:"+e.toString());
					throw new RuntimeException("执行["+sql+"]回收资源异常："+e.toString());
				}
			}
		}
		return result;
	}
	
	
}
