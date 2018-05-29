package func;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import svc.UniExApi;

import com.db.DBConnectionManager;
import com.rsc.Context;
import com.utils.FileUtils;
import com.utils.SqlServerUtil;

import bean.FileItem;

public class UserDoneTaskExportAction implements FileFunction{
	@Override
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="处理成功";
		//获取数据库中用户完成任务的数量
				StringBuffer sbFileContent=new StringBuffer();
				Connection conn = null;
				try{
					conn=DBConnectionManager.getInstance().getConnection();
					String sql="select USERS.USER_NAME+' '+TASK_DEF.OWNER USER_ID,COUNT(*) DONE_STAK,CONVERT(varchar(100),GETDATE()-1,23) DATA_DATE"+ 
									" FROM TASK_DEF,USERS WHERE TASK_DEF.OWNER=USERS.USER_ID AND  TASK_DEF.STATUS NOT IN('1','2') "+
									"GROUP BY OWNER,USERS.USER_NAME";
					List result=SqlServerUtil.executeQuery(sql, conn);
					if(result!=null&&result.size()>0){
						for(int w=0;w<result.size();w++){
							Map dataLine=(Map)result.get(w);
							String id=(String)dataLine.get("USER_ID");
							String dtaskCnt=dataLine.get("DONE_STAK")+"";
							String dataDate=(String)dataLine.get("DATA_DATE");
							String line=id+","+dtaskCnt+","+dataDate+"\r\n";
							sbFileContent.append(line);
						}
					}
				}catch(Exception e){
					outCode="-1";
					outMsg="获取数据库数据发生异常："+e.toString();
				}finally{
					DBConnectionManager.getInstance().freeConnction(conn);
				}
				//写入共享目录文件
				String content=sbFileContent.toString();
				String filePath=Context.outShareFloder+File.separator+file.getName();
				filePath=FileUtils.formatPath(filePath);
				boolean makeRet=FileUtils.writeFile(filePath,content);
				if(makeRet){
					//文件写入共享目录成功，调用安渡接口上传文件
					filePath=FileUtils.formatPath(filePath);
					boolean uploadRet=UniExApi.uplaodFile(filePath);
					if(!uploadRet){
						outCode="-1";
						outMsg="文件"+filePath+"上传安渡服务器发生异常";
					}
				}else{
					outCode="-1";
					outMsg="生成数据文件"+filePath+"发生异常";
				}
		
		return outCode+"|"+outMsg;
	}
}
