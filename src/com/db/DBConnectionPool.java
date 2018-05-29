package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import com.rsc.Context;
import com.rsc.SecurityCenter;

public class DBConnectionPool {
	public DBConnectionPool(int min){
		this.minConn=min;
		this.createConnection();
	}
	
	public synchronized Connection getConnection(){
		Connection con=null;
		if(this.freePool.size()>0){
			con=this.freePool.firstElement();
			this.freePool.removeElementAt(0);
			try{
				if(con.isClosed()){//池里面的连接被数据库服务器关闭
					con=getConnection();//在池里面找一个可用的
				}
			}catch(Exception e){
				con=getConnection();//在池里面找一个可用的
			}
		}else{
			con=this.newSqlServerConnection();
		}
		return con;
	}
	
	public synchronized void freeConnection(Connection con){
		if(this.freePool!=null){
			this.freePool.addElement(con);
		}
	}
	
	
	public void release(){
		for(int w=0;w<this.freePool.size();w++){
			try{
				Connection con=this.freePool.get(w);
				if(con!=null)
					con.close();
			}catch(SQLException e){
				
			}
		}
		this.freePool.removeAllElements();
	}
	
	private int minConn;
	private Vector<Connection> freePool=new Vector<Connection>();
	private void createConnection(){
		for(int w=0;w<this.minConn;w++){
			Connection con=this.newSqlServerConnection();
			freePool.add(con);
		}
	}
	
	private Connection newSqlServerConnection(){
		try{
			String DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  
			String url="jdbc:sqlserver://"+Context.SqlServerIp+":"+Context.ServerSqlPort+";DatabaseName="+Context.ServerDbName;
			String passwd=SecurityCenter.getInstance().decrypt(Context.ServerDbPasswd, Context.EncryptKey);
			Class.forName(DriverName);  
			Connection con = DriverManager.getConnection(url, Context.ServerDbUser, passwd); 
			 return con;
		}catch (ClassNotFoundException e) {
			throw new RuntimeException("无法找到指定类:"+e.toString());
		}
		catch(SQLException e){
			throw new RuntimeException("与数据库连接失败:"+e.toString());
		}
	}
}
