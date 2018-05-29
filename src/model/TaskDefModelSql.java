package model;

public class TaskDefModelSql {
	
		public static String getAdd(String id,String name,String reqID,String status,String arrageDate,String overDate,String rdate,String owner,String scheID,String crtUser,String crtTime,String sycFlag,String app){
			 String sql="insert into TASK_DEF(ID,TNAME,REQ_ID,STATUS,ARRANGE_DATE,OVERDATE,RDATE,OWNER,SCHE_ID,IS_DELAY,DELAY_CNT,RLS_CNT,RST_CNT,CRT_USER,CRT_TIME,MDF_USER,MDF_TIME,RST_APPRISE,CODE_APPRISE,APP,CUR_USERID,UPT_FLAG) "+
					 			"values('@ID','@TNAME','@REQ_ID','@STATUS','@ARRANGE_DATE','@OVERDATE','@RDATE','@OWNER','@SCHE_ID','@IS_DELAY','@DELAY_CNT','@RLS_CNT','@RST_CNT','@CRT_USER','@CRT_TIME','@MDF_USER',@MDF_TIME,'@RST_APPRISE','@CODE_APPRISE','@APP','@CUR_USERID','@UPT_FLAG')";
			 		//scheID.substring(0, scheID.indexOf("_"));			
			 		sql=sql.replace("@ID", id);
					sql=sql.replace("@TNAME", name);
					sql=sql.replace("@REQ_ID", reqID);
					sql=sql.replace("@STATUS", "2");//开发提交中
					sql=sql.replace("@ARRANGE_DATE", arrageDate);
					sql=sql.replace("@OVERDATE", overDate);
					sql=sql.replace("@RDATE", rdate);
					sql=sql.replace("@OWNER", owner);
					sql=sql.replace("@SCHE_ID", scheID);
					sql=sql.replace("@CRT_USER", crtUser);
					sql=sql.replace("@CRT_TIME", crtTime);
					sql=sql.replace("@ARRANGE_DATE", arrageDate);
					sql=sql.replace("@IS_DELAY", "0");
					sql=sql.replace("@DELAY_CNT", "0");
					sql=sql.replace("@RLS_CNT", "0");
					sql=sql.replace("@RST_CNT", "0");
					sql=sql.replace("@MDF_USER", crtUser);
					sql=sql.replace("@MDF_TIME", "CONVERT(varchar(100),GETDATE(),120)");
					sql=sql.replace("@RST_APPRISE", "0");
					sql=sql.replace("@CODE_APPRISE","0" );
					sql=sql.replace("@APP", app);
					sql=sql.replace("@CUR_USERID", owner);
					sql=sql.replace("@UPT_FLAG", sycFlag);	
			    return sql;
		}
		
		public static String getNewID(){
			String sql="SELECT NEXT VALUE FOR SEQ_TASK_ID as ID ";
	    	return sql;
		}
}
