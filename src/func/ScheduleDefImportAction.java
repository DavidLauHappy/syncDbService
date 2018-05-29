package func;

import java.io.File;
import java.util.List;

import model.BatchModel;

import svc.UniExApi;

import com.utils.FileUtils;
import com.utils.Paths;

import bean.Batch;
import bean.FileItem;

/**
 * @author David
 * ���ζ����ļ�����
 */
public class ScheduleDefImportAction  implements FileFunction {
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
				 boolean updateFlag=false;
				 String curFlag="";
					//��ȡcsv�ļ�
				 for(String line:dataLines){
					 outMsg="�����ļ�["+fileName+"]��¼��:"+line+"������ɣ�";
					 String[] datas=line.split(",");
					 if(datas.length==4){
						    String scheID=datas[0];
						    String expDate=datas[1];
						    expDate=expDate.substring(0, 10);
						    String crtUser=datas[2];
						    String crtTime=datas[3];
						    //���ݸ���
						    Batch batch=new Batch();
						    batch.setSchID(scheID);
						    batch.setExpireDate(expDate);
						    batch.setCrtTime(crtTime);
						    batch.setCrtUser(crtUser);
						    curFlag=BatchModel.getDataFlag();
						    if("A".equalsIgnoreCase(curFlag)){
						    	curFlag="B";
						    }else{
						    	curFlag="A";
						    }
						   batch.setDataFlag(curFlag) ;
						   batch.sync();
						   updateFlag=true;
					 }
				 }
				 //ͬ����ɺ󡢸������ݱ�ǩ
				 if(updateFlag){
					 BatchModel.updateDataFlag(curFlag);
				 }
			}
		}
		
		return outCode+"|"+outMsg;
	}
}
