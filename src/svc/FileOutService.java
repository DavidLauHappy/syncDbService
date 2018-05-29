package svc;

import java.lang.reflect.Method;

import com.rsc.Context;
import com.utils.Logger;
import com.utils.StringUtils;

import bean.FileItem;
import bean.Task;

/**
 * @author David
 * �����ݿ⵼��csv�ļ������ð��ɴ��䵽����
 * 
 */
public class FileOutService {
	
	public static FileOutService getInstance(){
		if(uniqueInstance==null)
			uniqueInstance=new FileOutService();
		return uniqueInstance;
	}
	//���õ������ݷ���Ĺ�������
	public void setWorkDate(String workDate) {
		if(StringUtils.isNullOrEmpty(this.workDate)||!this.workDate.equals(workDate)){
			this.workDate = workDate;
			//��������ʱ�������³�ʼ������״̬
			for(Task task:Context.FilesOut){
				task.setStatus("0");
			}
			this.alreadyRunFlag=false;
		}
	}
	
	public void run(){
		boolean flag=true;
		if(!this.alreadyRunFlag){
			for(Task task:Context.FilesOut){
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
							Logger.logService(serviceName, "����["+task.getClassID()+".fileProcess()�����ļ�["+fileName+file.getDesc()+"]���ؽ����"+ret.toString());
							String result=ret.toString();
							String code=result.split("|")[0];
							if("0".equalsIgnoreCase(code)){
								task.setStatus("1");//���մ������
							}else{
								task.setStatus("0");//���մ���ʧ�ܣ���ȻΪ������
								flag=false;
							}
						} catch (Exception e) {
							task.setStatus("0");//���մ���ʧ�ܣ���ȻΪ������
							flag=false;
							Logger.logService(serviceName, task.getClassID()+"������(���������ļ�start.xml��funcĿ¼)������һ�Σ�"+e.toString());
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
	private static  FileOutService uniqueInstance;
	private static final String serviceName="DataExportService";
	private boolean alreadyRunFlag=false;
	
	
	
}
