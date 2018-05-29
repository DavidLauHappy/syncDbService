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
 * 批次定义文件导入
 */
public class ScheduleDefImportAction  implements FileFunction {
	public String fileProcess(FileItem file) {
		String outCode="0";
		String outMsg="处理成功";
		String fileName=file.getName();
		//调用安渡接口下载csv文件到工作目录
		boolean downloadRet=UniExApi.downloadFile(fileName);
		if(!downloadRet){
			outCode="-1";
			outMsg="从安渡服务器下载文件"+fileName+"失败。";
		}
		else{
			//读取文件
			File dataFile=new File(Paths.getInstance().getWorkDir()+fileName);
			if(dataFile.exists()){
				 List<String> dataLines=FileUtils.getFileLines(dataFile.getAbsolutePath());
				 outMsg="读取文件["+fileName+"]内容完成！";
				 boolean updateFlag=false;
				 String curFlag="";
					//读取csv文件
				 for(String line:dataLines){
					 outMsg="处理文件["+fileName+"]记录行:"+line+"内容完成！";
					 String[] datas=line.split(",");
					 if(datas.length==4){
						    String scheID=datas[0];
						    String expDate=datas[1];
						    expDate=expDate.substring(0, 10);
						    String crtUser=datas[2];
						    String crtTime=datas[3];
						    //数据更新
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
				 //同步完成后、更新数据标签
				 if(updateFlag){
					 BatchModel.updateDataFlag(curFlag);
				 }
			}
		}
		
		return outCode+"|"+outMsg;
	}
}
