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
			//ɾ����¼
			String deleter=BatchModelSql.getDelete(data.getSchID(), data.getDataFlag());
			count=SqlServerUtil.executeUpdate(deleter, conn);
			//������¼
			String sql=BatchModelSql.getInsert(data.getSchID(), data.getExpireDate(), data.getCrtUser(), data.getCrtTime(), data.getDataFlag());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			Logger.error("��������["+data.getSchID()+"]�����쳣��"+e.toString());
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
			Logger.error("���µ�ǰ������ͬ����ǩ�쳣��"+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	//ȡ��ǰ������ͬ����ǩ
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
			Logger.error("ȡ��ǰ������ͬ����ǩ�쳣��"+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return result;
	}
	
}
