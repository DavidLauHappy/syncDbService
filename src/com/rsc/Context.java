package com.rsc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.utils.Paths;

import bean.Task;

public class Context {
	//���ݿ����ӳ���С������
	 public static int ServerDbMinConns=2;
	 //���ݿ�����
	 public static String SqlServerIp="";
     public static String ServerSqlPort="1433";
     public static String ServerDbName="";
     public static String ServerDbUser="sa";
     public static String ServerDbPasswd="";
     public static String outShareFloder="";
     public static String outShareUser="";
     //ftp��������Ϣ
     public static String FTPServerIp="";
     public static String FTPServerPort="";
     public static String ServerFTPUser="";
     public static String ServerFTPasswd="";
     
     //�̶���Կ
     public static String EncryptKey="Gorgeous";
     //��ʼ����Ŀ¼
     public static String StartPath="";
     //����������Ϣ
     public static String UniNXG_URL="";//���ɷ��ɵ��û���
     public static String UniNXG_ShareUser="";//��������ĶԷ����û���
     public static String apiKey="";	//���ɷ����ϵͳ��key
     //public static String securityKey="";
     public static String UniNXG_IP_OUT="";//���ɽ���������豸����ip
     public static String UniNXG_IP_IN="";//���ɽ���������豸����ip
     public static String UniNXG_DIR="";//���ɷ������ϵĹ���Ŀ¼��
     //�����ļ��б�
     public static List<Task> FilesIn=new ArrayList<Task>();
     //�����ļ��б�
     public static List<Task> FilesOut=new ArrayList<Task>();
     //��������
     public static String workDate="";
     //����ʱ��
     public static String startTime="";
     //HTTP����������ʶ
     public  static String httpServerEnable="No";
     //������ѯƵ��
     public static int sleepSecond=0;
     //�Ƿ��������
     public static boolean isRunable(){
    	 String lockFile=Paths.getInstance().getConfigPath()+"lock.ini";
    	 File file=new File(lockFile);
    	 if(file.exists()){
    		 return false;
    	 }else{
    		 return true;
    	 }
     }
}
