package svc;

import java.lang.reflect.Method;

import bean.FileItem;
import bean.Task;

import com.rsc.Context;
import com.utils.Logger;
import com.utils.StringUtils;


/**
 * @author David
 *ʹ�ð��ɼ�����oa�ļ�
 * ѭ�������ļ�
 * �����ļ��������ȶ�Ӧ�Ĵ�����
 * �л���������
 * �л�����״̬
 */
public class FilesInService {
		//�������ڣ������Ϲ�������Ӧ�ñ�ϵͳ������һ��
		
		public static FilesInService getInstance(){
			if(uniqueInstance==null)
				uniqueInstance=new FilesInService();
			return uniqueInstance;
		}
		
		//���õ������ݷ���Ĺ�������
		public void setWorkDate(String workDate) {
			if(StringUtils.isNullOrEmpty(this.workDate)||!this.workDate.equals(workDate)){
				this.workDate = workDate;
				//��������ʱ�������³�ʼ������״̬
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
								Logger.logService(serviceName, task.getClassID()+"������(���������ļ�start.xml��funcĿ¼)������һ�Σ�"+e.toString());
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
