package svc;

import java.lang.reflect.Method;

import bean.FileItem;
import bean.Task;

import com.rsc.Context;
import com.utils.Logger;
import com.utils.StringUtils;


/**
 * @author David
 *使用安渡加载新oa文件
 * 循环加载文件
 * 挨个文件处理，调度对应的处理函数
 * 切换工作日期
 * 切换服务状态
 */
public class FilesInService {
		//工作日期，理论上工作日期应该比系统日期晚一天
		
		public static FilesInService getInstance(){
			if(uniqueInstance==null)
				uniqueInstance=new FilesInService();
			return uniqueInstance;
		}
		
		//设置导入数据服务的工作日期
		public void setWorkDate(String workDate) {
			if(StringUtils.isNullOrEmpty(this.workDate)||!this.workDate.equals(workDate)){
				this.workDate = workDate;
				//发生日切时，才重新初始化任务状态
				for(Task task:Context.FilesIn){
					task.setStatus("0");
				}
				this.alreadyRunFlag=false;
			}
		}
		
		public void run(){
			 boolean flag=true;
			if(!this.alreadyRunFlag){
					for(Task task:Context.FilesIn){
						if("0".equalsIgnoreCase(task.getStatus())){
							FileItem file=new FileItem();
							String fileName=task.getName();
							fileName=fileName.replace("#workdate#", this.workDate);
							file.setName(fileName);
							file.setDesc(task.getDesc());
							file.setPath(task.getPath());
							file.setWorkDate(this.workDate);
							try {
								Class clazz=Class.forName(task.getClassID());
								Object obj = clazz.newInstance();
								Method m = clazz.getMethod("fileProcess", FileItem.class);
								Object ret = m.invoke(obj, file);		
								Logger.logService(serviceName, "调用["+task.getClassID()+".fileProcess()导出文件["+fileName+file.getDesc()+"]返回结果："+ret.toString());
								String result=ret.toString();
								String code=result.split("|")[0];
								if("0".equalsIgnoreCase(code)){
									task.setStatus("1");//当日处理完成
								}else{
									task.setStatus("0");//当日处理失败，仍然为待处理
									flag=false;
								}
							} catch (Exception e) {
								task.setStatus("0");//当日处理失败，仍然为待处理
								Logger.logService(serviceName, task.getClassID()+"不存在(请检查配置文件start.xml和func目录)，加载一次："+e.toString());
								flag=false;
							}
						}
					}
					if(flag){
						this.alreadyRunFlag=true;
					}else{
						this.alreadyRunFlag=false;
					}
			}
		}
		
		private String  workDate;
		private static  FilesInService uniqueInstance;
		private static final String serviceName="DataImportService";
		private boolean alreadyRunFlag=false;
		
}
