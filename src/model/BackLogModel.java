package model;

import java.sql.Connection;

import bean.BackLog;

import com.db.DBConnectionManager;
import com.utils.Logger;
import com.utils.SqlServerUtil;

public class BackLogModel {
	
	
	public static int addReqAttach(String reqID,String fileID,String md5,String remotePath,String fileName,String fileTime,boolean upload,String mdfUser){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BackLogModelSql.getReqAttachInsert(reqID, fileID, fileName, fileTime, md5, mdfUser);
			count=SqlServerUtil.executeUpdate(sql, conn);
			if(count>0&&upload){
				sql=BackLogModelSql.getAttachHD(md5, remotePath, mdfUser);
				count=SqlServerUtil.executeUpdate(sql, conn);
			}
		}catch(Exception e){
			Logger.error("需求条目["+reqID+"]新增附件["+fileName+"]异常："+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
	
	public static int add(BackLog data){
		Connection conn = null;
		int count=0;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql=BackLogModelSql.getInsert(data.getId(), data.getSuser(), data.getDept(),data.getSdate(), data.getIuser(), data.getName(), data.getBackground(), data.getRdesc(), data.getRdate(), data.getReason(), data.getCuser(), data.getAuser(), data.getComment(),data.getRscr(), data.getRtype(), data.getRclass(), data.getStatus(), data.getCurUser(), data.getSyncFlag(),data.getLink(), data.getScheID());
			count=SqlServerUtil.executeUpdate(sql, conn);
		}catch(Exception e){
			Logger.error("需求条目["+data.getId()+"]新增异常："+e.toString());
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		return count;
	}
}
