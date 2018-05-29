package http;

public class LocalAPIFactory {
		public static ApiAction createAction(String type,String parameter){
				ApiAction action=null;
				if(Api001.equalsIgnoreCase(type)){
					action=new TaskStatusAction(parameter);
				}
				else if(Api002.equalsIgnoreCase(type)){
					action=new TaskAddAction(parameter);
				}else{
					System.err.println("不支持的接口:"+type);
				}
				return action;
		}
		
		
		public static final String Api001="taskStatus";
		public static final String Api002="taskAdd";
}
