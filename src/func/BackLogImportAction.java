package func;

import java.io.File;
import java.util.List;
import java.util.UUID;

import svc.FtpFileService;
import svc.UniExApi;

import com.rsc.Context;
import com.utils.DateUtil;
import com.utils.FileUtils;
import com.utils.Paths;
import com.utils.StringUtils;

import bean.BackLog;
import bean.FileItem;
import bean.RFile;

//������Ŀ�ļ�����oa���������д�뵽���ݿ��BACKLOG
public class BackLogImportAction implements FileFunction {

	@Override
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="����ɹ�";
		String fileName=file.getName();
		//���ð��ɽӿ�����csv�ļ�������Ŀ¼
		boolean downloadRet=UniExApi.downloadFile(fileName);
		if(!downloadRet){
			outCode="-1";
			outMsg="�Ӱ��ɷ����������ļ�"+fileName+"ʧ�ܡ�";
		}
		else{
			//��ȡ�ļ�
			File dataFile=new File(Paths.getInstance().getWorkDir()+fileName);
			if(dataFile.exists()){
				 List<String> dataLines=FileUtils.getFileLines(dataFile.getAbsolutePath());
				 outMsg="��ȡ�ļ�["+fileName+"]������ɣ�";
					//��ȡcsv�ļ�
				 char  newLine=(char)14;
				 char spliter=(char)15;
				 for(String line:dataLines){
					 outMsg="�����ļ�["+fileName+"]��¼��:"+line+"������ɣ�";
					 String[] datas=line.split(",");
					 if(datas.length==22){
						 	boolean attachFlag=false;
						 	BackLog baklog=new BackLog();
						 	baklog.setCurUser("06000");
						 	String id=datas[0];
						 	baklog.setId(id);
						 	String suser=datas[1];
						 	baklog.setSuser(suser);
						 	String dept=datas[2];
						 	baklog.setDept(dept);
						 	String sdate=datas[3];
						 	sdate=sdate.substring(0, 10);
						 	baklog.setSdate(sdate);
						 	String iuser=datas[4];
						 	baklog.setIuser(iuser);
						 	String name=datas[5];
						 	baklog.setName(name);
						 	String background=datas[6];
							if(!StringUtils.isNullOrEmpty(background)){
								background=background.replace(String.valueOf(newLine),"\r\n" );//char(14) �س��滻 ����
								background=background.replace(String.valueOf(spliter),",");//char(15) �滻����
							}else{
								background="";
							}
						 	baklog.setBackground(background);
						 	String rdesc=datas[7];
						 	rdesc=rdesc.replace(String.valueOf(newLine),"\r\n" );//char(14) �س��滻 ����
						 	rdesc=rdesc.replace(String.valueOf(spliter),",");//char(15) �滻����
						 	baklog.setRdesc(rdesc);
						 	String rdate=datas[8];
						 	rdate=rdate.substring(0, 10);
						 	baklog.setRdate(rdate);
						 	String reason=datas[9];
						 	if(!StringUtils.isNullOrEmpty(reason)){
						 		reason=reason.replace(String.valueOf(newLine),"\r\n" );//char(14) �س��滻 ����
						 		reason=reason.replace(String.valueOf(spliter),",");//char(15) �滻����
						 	}else{
						 		reason="";
						 	}
						 	baklog.setReason(reason);
						 	String cuser=datas[10];
						 	baklog.setCuser(cuser);
						 	String auser=datas[11];
						 	baklog.setAuser(auser);
						 	String comment=datas[12];
						 	if(!StringUtils.isNullOrEmpty(comment)){
						 		comment=comment.replace(String.valueOf(newLine),"\r\n" );//char(14) �س��滻 ����
						 		comment=comment.replace(String.valueOf(spliter),",");//char(15) �滻����
						 	}else{
						 		comment="";
						 	}
						 	baklog.setComment(comment);
						 	String rsrc=datas[13];
						 	baklog.setRscr(rsrc);
						 	String rtype=datas[14];
						 	baklog.setRtype(rtype);
						 	String rclass=datas[15];
						 	baklog.setRclass(rclass);
						 	String status=datas[16];
						 	baklog.setStatus(status);
						 	String link=datas[17];
						 	baklog.setLink(link);
						 	String attacname=datas[18];//�������ƿ���Ҫ�������ڻ�����������������
						 	String scheID=datas[19];
						 	baklog.setScheID(scheID);
						 	String syncFlag=datas[20];
						 	baklog.setSyncFlag(syncFlag);
						 	String timeString=datas[21];//��OA�����õ�������ʵ��ʱ��ֻ�����ϴ��Ĳ���ʱ��
						 	if(!StringUtils.isNullOrEmpty(attacname)){
						 		//�������󸽼�
						 		outMsg="�����ļ�["+fileName+"]��¼��:"+line+"���ݿ�ʼ��������:"+id+"�ĸ�����"+attacname;
						 		downloadRet=UniExApi.downloadFile(attacname);
								if(!downloadRet){
									//���������쳣������������¼
									outMsg="�����ļ�["+fileName+"]��¼��:"+line+"������������:"+id+"�ĸ�����"+attacname+"ʧ��������ӿ���־";
									continue;
								}else{
									//���Ӹ����ϴ�ftp����������¼�������¼
									boolean upRet=true;
									boolean needUpload=false;
									RFile rfile=new RFile();
									File afile=new File(Paths.getInstance().getWorkDir()+attacname);
									String location=id;//�ϴ��������Ŷ�Ӧ��Ŀ¼
									String md5=FileUtils.getMd5ByFile(afile);
									long time=DateUtil.getTimeLongFromString(timeString);
									afile.setLastModified(time);
									rfile.setMd5(md5);
									if(!FileUtils.fileExist(md5)){
										rfile.setRpath(location);
										outMsg="�����ļ�["+fileName+"]��¼��:"+line+"������������:"+id+"�ĸ�����"+attacname+"��ʼ�ϴ��ĵ�������";
										 upRet=FtpFileService.getService().upLoad(Paths.getInstance().getWorkDir()+attacname, location);
										 needUpload=true;
									}else{
										upRet=true;
										 needUpload=false;
									}
									if(upRet){
										String fileID=UUID.randomUUID().toString();
										rfile.setFileID(fileID);
										baklog.addReqAttach(fileID, md5, location, attacname, timeString, needUpload,"06000");
									}
								}
						 	}
						 //����������Ŀ  �������ݿ�
						baklog.addToDb();	
					 }
				 }
			}
		}
		return outCode+"|"+outMsg;
	}

}
