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
					//读取csv文件
				 for(String line:dataLines){
					 outMsg="处理文件["+fileName+"]记录行:"+line+"内容完成！";
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
						  //写入到数据库
						  outMsg="处理文件["+fileName+"]记录行:"+line+"开始写入数据库！";
						  task.addDb();
						  //生成开发提醒
						  outMsg="处理文件["+fileName+"]记录行:"+line+"开始生成任务提醒！";
						  String msg="您有新的任务["+name+"("+reqID+")]需要处理！";
						  Msg.add(owner, msg);
					 }
				 }
			}
		}
		return outCode+"|"+outMsg;
	}

}
