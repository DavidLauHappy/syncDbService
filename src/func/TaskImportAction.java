package func;

import java.io.File;
import java.util.List;

import svc.UniExApi;

import com.utils.FileUtils;
import com.utils.Paths;

import bean.FileItem;
import bean.Msg;
import bean.TaskDef;

public class TaskImportAction implements FileFunction {

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
				 for(String line:dataLines){
					 outMsg="�����ļ�["+fileName+"]��¼��:"+line+"������ɣ�";
					 String[] datas=line.split(",");
					 if(datas.length==11){
						 TaskDef task=new TaskDef();
						  /*String id=datas[0];
						  task.setId(id);*/
						  String name=datas[0];
						  task.setTname(name);
						  String reqID=datas[1];
						  task.setReqId(reqID);
						  String status=datas[2];
						  task.setStatus(status);
						  String arrageDate=datas[3];
						  arrageDate=arrageDate.substring(0, 10);
						  task.setArrangeDate(arrageDate);
						  String overdate=datas[4];
						  overdate=overdate.substring(0, 10);
						  task.setOverdate(overdate);
						  String rdate=datas[5];
						  rdate=rdate.substring(0, 10);
						  task.setRdate(rdate);
						  String owner=datas[6];
						  task.setOwner(owner);
						  String scheId=datas[7];
						  task.setScheId(scheId);
						  String crtUser=datas[8];
						  task.setCrtUser(crtUser);
						  String crtTime=datas[9];
						  task.setCrtTime(crtTime);
						  String sycnFlag=datas[10];
						  task.setSycnFlag(sycnFlag);
						  //д�뵽���ݿ�
						  outMsg="�����ļ�["+fileName+"]��¼��:"+line+"��ʼд�����ݿ⣡";
						  task.addDb();
						  //���ɿ�������
						  outMsg="�����ļ�["+fileName+"]��¼��:"+line+"��ʼ�����������ѣ�";
						  String msg="�����µ�����["+name+"("+reqID+")]��Ҫ����";
						  Msg.add(owner, msg);
					 }
				 }
			}
		}
		return outCode+"|"+outMsg;
	}

}
