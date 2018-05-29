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
		String outMsg="����ɹ�";
		//��ȡ���ݿ����û�������������
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
