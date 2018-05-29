package func;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import svc.UniExApi;
import bean.FileItem;

import com.db.DBConnectionManager;
import com.rsc.Context;
import com.utils.DateUtil;
import com.utils.FileUtils;
import com.utils.SqlServerUtil;
import com.utils.StringUtils;

public class DelayDetailExportAction implements FileFunction {
	@Override
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="����ɹ�";
		//��ȡ���ݿ�������״̬����
		StringBuffer sbFileContent=new StringBuffer();
		Connection conn = null;
		try{
			conn=DBConnectionManager.getInstance().getConnection();
			String sql="select REQ_ID,LOG_ID,USERS.USER_NAME+' '+ DELAY_DETAIL.APPLY_USER APPLY_USER, APPLY_TIME,AUSERS.USER_NAME+' '+APPR_USER APPR_USER,"+
										"DSTATUS,REASON,DCOMMENT,DTIME,PDATE,DDATE from DELAY_DETAIL,USERS,USERS AUSERS"+
										" WHERE DELAY_DETAIL.APPLY_USER=USERS.USER_ID"+
										   " and DELAY_DETAIL.APPR_USER=AUSERS.USER_ID"+
										   " and CONVERT(varchar(10),MTIME)='@workDate'";
			String date=file.getWorkDate();
			String workdate=DateUtil.getDateFromDateStr(date);
			sql=sql.replace("@workDate", workdate);
			List result=SqlServerUtil.executeQuery(sql, conn);
			if(result!=null&&result.size()>0){
				 char  newLine=(char)14;
				 char spliter=(char)15;
				for(int w=0;w<result.size();w++){
					Map<String,String> dataLine=(Map)result.get(w);
					String id=dataLine.get("REQ_ID");
					String logID=dataLine.get("LOG_ID");
					String applyUserID=dataLine.get("APPLY_USER");
					String applyTime=dataLine.get("APPLY_TIME");
					String apprUser=dataLine.get("APPR_USER");
					String status=dataLine.get("DSTATUS");
					String reason=dataLine.get("REASON");
					if(!StringUtils.isNullOrEmpty(reason)){
						reason=reason.replace("\r\n", String.valueOf(newLine));//char(14) �س��滻 ����
						reason=reason.replace(",", String.valueOf(spliter));//char(15) �滻����
						reason=reason.replace("��", String.valueOf(spliter));//char(15) �滻����
					}else{
						reason="";
					}
					String comment=dataLine.get("DCOMMENT");
					if(!StringUtils.isNullOrEmpty(reason)){
						comment=comment.replace("\r\n", String.valueOf(newLine));//char(14) �س��滻 ����
						comment=comment.replace(",", String.valueOf(spliter));//char(15) �滻����
						comment=comment.replace("��", String.valueOf(spliter));//char(15) �滻����
					}else{
						comment="";
					}
					String processTime=dataLine.get("DTIME");
					String pdate=dataLine.get("PDATE");
					String dDate=dataLine.get("DDATE");
					String line=id+","+logID+","+applyUserID+","+applyTime+","+apprUser+","+status+","+reason+","+comment+","+processTime+","+pdate+","+dDate+","+workdate+ "\r\n";
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
