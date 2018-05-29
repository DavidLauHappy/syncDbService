package func;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import svc.UniExApi;

import com.db.DBConnectionManager;
import com.rsc.Context;
import com.utils.DateUtil;
import com.utils.FileUtils;
import com.utils.SqlServerUtil;

import bean.FileItem;

//需求条目导出处理
public class BackLogExportAction implements FileFunction {
	@Override
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="处理成功";
		//获取数据库中需求状态数据
		StringBuffer sbFileContent=new StringBuffer();
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql="select ID,STATUS,CUR_USER,SYNC_FLAG,IS_DELAY,ONLINE_DATE from BACKLOG "+
							 "where CONVERT(varchar(10),MDF_TIME)='@workDate'";
			String date=file.getWorkDate();
			String workdate=DateUtil.getDateFromDateStr(date);
			sql=sql.replace("@workDate", workdate);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					String id=dataLine.get("ID");
					String status=dataLine.get("STATUS");
					String userID=dataLine.get("CUR_USER");
					String flag=dataLine.get("SYNC_FLAG");
					String isDelay=dataLine.get("IS_DELAY");
					String onlineDate=dataLine.get("ONLINE_DATE");
					String line=id+","+status+","+userID+","+flag+","+isDelay+","+onlineDate+","+workdate+"\r\n";
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
