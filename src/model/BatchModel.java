package model;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import bean.BackLog;
import bean.Batch;

import com.db.DBConnectionManager;
import com.utils.Logger;
import com.utils.SqlServerUtil;

public class BatchModel {

	public static int add(Batch data){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			//删除记录
			String deleter=BatchModelSql.getDelete(data.getSchID(), data.getDataFlag());
			count=SqlServerUtil.executeUpdate(deleter, conn);
			//新增记录
			String sql=BatchModelSql.getInsert(data.getSchID(), data.getExpireDate(), data.getCrtUser(), data.getCrtTime(), data.getDataFlag());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			Logger.error("需求批次["+data.getSchID()+"]新增异常："+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	
	public static int updateDataFlag(String flag){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BatchModelSql.getDataFlagSet(flag);
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			Logger.error("更新当前的数据同步标签异常："+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//取当前的数据同步标签
	public static String getDataFlag(){
		String result="";
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BatchModelSql.getDataFlag();
			List results=SqlServerUtil.executeQuery(sql, conn);
			if(results!=null&&results.size()>0){
				Map<String,String> dataLine=(Map)results.get(0);
				result=dataLine.get("FLAG");
			}
		}catch(Exception e){
			Logger.error("取当前的数据同步标签异常："+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return result;
	}
	
}
