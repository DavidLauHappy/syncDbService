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

//需求条目文件从新oa导入进来并写入到数据库表BACKLOG
public class BackLogImportAction implements FileFunction {

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
				 char  newLine=(char)14;
				 char spliter=(char)15;
				 for(String line:dataLines){
					 outMsg="处理文件["+fileName+"]记录行:"+line+"内容完成！";
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
								background=background.replace(String.valueOf(newLine),"\r\n" );//char(14) 回车替换 换行
								background=background.replace(String.valueOf(spliter),",");//char(15) 替换逗号
							}else{
								background="";
							}
						 	baklog.setBackground(background);
						 	String rdesc=datas[7];
						 	rdesc=rdesc.replace(String.valueOf(newLine),"\r\n" );//char(14) 回车替换 换行
						 	rdesc=rdesc.replace(String.valueOf(spliter),",");//char(15) 替换逗号
						 	baklog.setRdesc(rdesc);
						 	String rdate=datas[8];
						 	rdate=rdate.substring(0, 10);
						 	baklog.setRdate(rdate);
						 	String reason=datas[9];
						 	if(!StringUtils.isNullOrEmpty(reason)){
						 		reason=reason.replace(String.valueOf(newLine),"\r\n" );//char(14) 回车替换 换行
						 		reason=reason.replace(String.valueOf(spliter),",");//char(15) 替换逗号
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
						 		comment=comment.replace(String.valueOf(newLine),"\r\n" );//char(14) 回车替换 换行
						 		comment=comment.replace(String.valueOf(spliter),",");//char(15) 替换逗号
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
						 	String attacname=datas[18];//附近名称可能要带上日期或需求编号这样的名字
						 	String scheID=datas[19];
						 	baklog.setScheID(scheID);
						 	String syncFlag=datas[20];
						 	baklog.setSyncFlag(syncFlag);
						 	String timeString=datas[21];//新OA不能拿到附近的实际时间只能用上传的操作时间
						 	if(!StringUtils.isNullOrEmpty(attacname)){
						 		//下载需求附件
						 		outMsg="处理文件["+fileName+"]记录行:"+line+"内容开始下载需求:"+id+"的附件："+attacname;
						 		downloadRet=UniExApi.downloadFile(attacname);
								if(!downloadRet){
									//附近下载异常，跳过这条记录
									outMsg="处理文件["+fileName+"]记录行:"+line+"内容下载需求:"+id+"的附件："+attacname+"失败详情见接口日志";
									continue;
								}else{
									//增加附近上传ftp服务器，记录附件表记录
									boolean upRet=true;
									boolean needUpload=false;
									RFile rfile=new RFile();
									File afile=new File(Paths.getInstance().getWorkDir()+attacname);
									String location=id;//上传到需求编号对应的目录
									String md5=FileUtils.getMd5ByFile(afile);
									long time=DateUtil.getTimeLongFromString(timeString);
									afile.setLastModified(time);
									rfile.setMd5(md5);
									if(!FileUtils.fileExist(md5)){
										rfile.setRpath(location);
										outMsg="处理文件["+fileName+"]记录行:"+line+"内容下载需求:"+id+"的附件："+attacname+"开始上传文档服务器";
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
						 //新增需求条目  插入数据库
						baklog.addToDb();	
					 }
				 }
			}
		}
		return outCode+"|"+outMsg;
	}

}
