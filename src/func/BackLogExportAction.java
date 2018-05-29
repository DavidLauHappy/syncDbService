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

//������Ŀ��������
public class BackLogExportAction implements FileFunction {
	@Override
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="����ɹ�";
		//��ȡ���ݿ�������״̬����
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
			outMsg="��ȡ���ݿ����ݷ����쳣��"+e.toString();
		}finally{
			DBConnectionManager.getInstance().freeConnction(conn);
		}
		//д�빲��Ŀ¼�ļ�
		String content=sbFileContent.toString();
		String filePath=Context.outShareFloder+File.separator+file.getName();
		filePath=FileUtils.formatPath(filePath);
		boolean makeRet=FileUtils.writeFile(filePath,content);
		if(makeRet){
			//�ļ�д�빲��Ŀ¼�ɹ������ð��ɽӿ��ϴ��ļ�
			filePath=FileUtils.formatPath(filePath);
			boolean uploadRet=UniExApi.uplaodFile(filePath);
			if(!uploadRet){
				outCode="-1";
				outMsg="�ļ�"+filePath+"�ϴ����ɷ����������쳣";
			}
		}else{
			outCode="-1";
			outMsg="���������ļ�"+filePath+"�����쳣";
		}
		return outCode+"|"+outMsg;
	}

}
