package com.db;

import java.sql.Connection;

import com.rsc.Context;

public class DBConnectionManager {
	public static DBConnectionManager getInstance(){
   	 	if(unique_instance==null)
   	 	unique_instance=new DBConnectionManager();
   	 	return unique_instance;
    }
	
	public Connection getConnection(){
		if(pool!=null)
			return pool.getConnection();
		return null;
	}
	
	public void freeConnction(Connection con){
		if(pool!=null)
			pool.freeConnection(con);
	}
	
	public void release(){
		if(pool!=null)
			pool.release();
	}
     private static DBConnectionManager unique_instance;
     private DBConnectionPool pool;
     private DBConnectionManager(){
    	 pool=new DBConnectionPool(Context.ServerDbMinConns);
     }
     
}
