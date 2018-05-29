package bean;

import java.sql.Connection;
import java.util.UUID;


import com.db.DBConnectionManager;
import com.utils.SqlServerUtil;

public class Msg {
	 public enum Status{Unread,Read;}
	 
	  public static int add(String userID,String text){
		  Connection conn = null;
			int count=0;
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				String sql="insert into MSGS(USER_ID,MSG_ID,MSG,CRT_USER,CRT_TIME,STATUS) "+
								"values('@USER_ID','@MSG_ID','@MSG','@CRT_USER',@CRT_TIME,'@STATUS')";
				sql=sql.replace("@USER_ID", userID);
				sql=sql.replace("@MSG_ID", UUID.randomUUID().toString());
				sql=sql.replace("@MSG", text);
				sql=sql.replace("@CRT_USER", "06000");
				sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				sql=sql.replace("@STATUS", Status.Unread.ordinal()+"");
				count=SqlServerUtil.executeUpdate(sql, conn);
			}catch(Exception e){
				
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
	  }
}
