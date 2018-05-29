package model;

public class BackLogModelSql {
	
		//新增需求附件
		public static String getReqAttachInsert(String reqID,String fileID,String fileName,String fileTime,String md5,String mdfUser){
			String sql="insert into REQ_FILES(REQ_ID,FILE_ID,FILE_NAME,FILE_TIME,MD5,MDF_USER,MDF_TIME) "+
							"values('@REQ_ID','@FILE_ID','@FILE_NAME','@FILE_TIME','@MD5','@MDF_USER',@MDF_TIME)";
			sql=sql.replace("@REQ_ID", reqID);
			sql=sql.replace("@FILE_ID", fileID);
			sql=sql.replace("@FILE_NAME", fileName);
			sql=sql.replace("@FILE_TIME", fileTime);
			sql=sql.replace("@MD5", md5);
			sql=sql.replace("@MDF_USER", mdfUser);
			sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
		
		//新增文件硬地址
		public static String getAttachHD(String md5,String location,String mdfUser){
			String sql="insert into  FILES(MD5,LOCATION,CRT_USER, CRT_TIME) "+
					 "values('@MD5','@LOCATION','@CRT_USER',@CRT_TIME)";
				sql=sql.replace("@LOCATION", location);
				sql=sql.replace("@MD5", md5);
				sql=sql.replace("@CRT_USER", mdfUser);
				sql=sql.replace("@CRT_TIME", "CONVERT(varchar(100),GETDATE(),120)");
				return sql;
		}
		
		//新增需求条目
		public static String getInsert(String id,String suser,String dept,String sdate,String isuer,String name,String background,String rdesc,String rdate,String reason,String cuser,String auser,String comment,String rsrc,String rtype,String rclass,String status,String curUser,String syncFlag,String link,String scheID){
			String sql="insert into BACKLOG(ID,SUSER,DEPT,SDATE,IUSER,NAME,BACKGROUND,RDESC,RDATE,REASON,CUSER,AUSER,COMMENT,RSRC,RTYPE,RCLASS,STATUS,CUR_USER,MDF_TIME,SYNC_FLAG,LINK,SCHE_ID,IS_DELAY) "+
							"values('@ID','@SUSER','@DEPT','@SDATE','@IUSER','@NAME','@BACKGROUND','@RDESC','@RDATE','@REASON','@CUSER','@AUSER','@COMMENT','@RSRC','@RTYPE','@RCLASS','@STATUS','@CUR_USER',@MDF_TIME,'@SYNC_FLAG','@LINK','@SCHE_ID','0')";
			sql=sql.replace("@ID", id);
			sql=sql.replace("@SUSER", suser);
			sql=sql.replace("@DEPT", dept);
			sql=sql.replace("@SDATE", sdate);
			sql=sql.replace("@IUSER", isuer);
			sql=sql.replace("@NAME", name);
			sql=sql.replace("@BACKGROUND", background);
			sql=sql.replace("@RDESC", rdesc);
			sql=sql.replace("@RDATE", rdate);
			sql=sql.replace("@REASON", reason);
			sql=sql.replace("@CUSER", cuser);
			sql=sql.replace("@AUSER", auser);
			sql=sql.replace("@COMMENT", comment);
			sql=sql.replace("@RSRC", rsrc);
			sql=sql.replace("@RTYPE", rtype);
			sql=sql.replace("@RCLASS", rclass);
			sql=sql.replace("@STATUS", status);
			sql=sql.replace("@CUR_USER", curUser);
			sql=sql.replace("@SYNC_FLAG", syncFlag);
			sql=sql.replace("@LINK", link);
			sql=sql.replace("@SCHE_ID", scheID);
			sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
			return sql;
		}
}
