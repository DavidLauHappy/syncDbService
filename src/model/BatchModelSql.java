package model;

public class BatchModelSql {
	
		public static String getInsert(String scheID,String expireDate,String crtDate,String crtTime,String dataFlag){
			String sql="insert into BATCH(SCHE_ID,EXPIRE_DATE,CRT_USER,CRT_TIME,DATA_FLAG) "+
							"values('@SCHE_ID','@EXPIRE_DATE','@CRT_USER','@CRT_TIME','@DATA_FLAG')";
			sql=sql.replace("@SCHE_ID", scheID);
			sql=sql.replace("@EXPIRE_DATE", expireDate);
			sql=sql.replace("@CRT_USER", crtDate);
			sql=sql.replace("@CRT_TIME", crtTime);
			sql=sql.replace("@DATA_FLAG", dataFlag);
			return sql;
		}
		
		public static String getDataFlag(){
			String sql="select FLAG from DATA_FLAG where ID='@ID'";
			sql=sql.replace("@ID", "BATCH");//使用表名
			return sql;
		}
		
		public static String getDataFlagSet(String dataFlag){
			String sql="update DATA_FLAG set FLAG='@FLAG' where ID='@ID'";
			sql=sql.replace("@ID", "BATCH");//使用表名
			sql=sql.replace("@FLAG", dataFlag);
			return sql;
		}
		
		public static String getDelete(String schID,String flag){
			String sql="delete from  BATCH where DATA_FLAG='@DATA_FLAG' and SCHE_ID='@SCHE_ID'";
			sql=sql.replace("@SCHE_ID", schID);
			sql=sql.replace("@DATA_FLAG", flag);
			return sql;
		}
		
}
