package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.db.DBConnectionManager;
import com.utils.Logger;
import com.utils.SqlServerUtil;
import com.utils.StringUtils;

import bean.TaskDef;

public class TaskDefModel {
	
		public static int getAdd(TaskDef data){
			Connection conn = null;
			int count=0;
			String  sql="";
			try{
				//取任务ID序列
				String no="";
				 sql=TaskDefModelSql.getNewID();
				conn=DBConnectionManager.getInstance().getConnection();
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
					Map dataLine=(Map)result.get(0);
					no=dataLine.get("ID")+"";
				}
				no="TASK"+StringUtils.leftpad(no,8,"0");
				data.setId(no);
				sql=TaskDefModelSql.getAdd(no, data.getTname(), data.getReqId(), data.getStatus(), data.getArrangeDate(),data.getOverdate(), data.getRdate(), data.getOwner(), data.getScheId(), data.getCrtUser(), data.getCrtTime(), data.getSycnFlag(),data.getOwnerApp());
				count=SqlServerUtil.executeUpdate(sql, conn);
			}catch(Exception e){
				Logger.error(sql+"异常:"+e.toString());
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return count;
		}
		
	
		public static String getUserApp(String userID){
			Connection conn = null;
			String retApp="";
			String  sql="";
			try{
				conn=DBConnectionManager.getInstance().getConnection();
				 sql="select APPS from USERS where USER_ID='@USER_ID'";
				sql=sql.replace("@USER_ID", userID);
				List result=SqlServerUtil.executeQuery(sql, conn);
				if(result!=null&&result.size()>0){
						Map dataLine=(Map)result.get(0);
						retApp=(String)dataLine.get("APPS");
				}
			}catch(Exception e){
				Logger.error(sql+"异常:"+e.toString());
			}finally{
				DBConnectionManager.getInstance().freeConnction(conn);
			}
			return retApp;
		}
}
