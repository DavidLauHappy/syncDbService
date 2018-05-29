package http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.utils.DataUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import bean.Task;

public class TaskStatusAction extends ApiAction {

	private String resultJson;
	private String InParameters;
	public TaskStatusAction(String parameters){
		this.InParameters=parameters;
	}
	
	@Override
	public String getResult() {
		return resultJson;
	}

	@Override
	public boolean execute() {
		try{
			Map<String,String> parameter=DataUtils.getData(this.InParameters);
			if(parameter!=null){
				for(String key:parameter.keySet()){
					System.out.println(key+"->"+parameter.get(key));
				}
			}
			List<Task> resultList=new ArrayList<Task>();
			Task taskA=new Task();
			taskA.setId("1");
			taskA.setName("name1");
			taskA.setStatus("running");
			resultList.add(taskA);
			Task taskB=new Task();
			taskB.setId("2");
			taskB.setName("name2");
			taskB.setStatus("over");
			resultList.add(taskB);
			
			JSONArray  json=JSONArray.fromObject(resultList);
			this.resultJson=json.toString();
		}catch(Exception e){
			this.resultJson=e.toString();
			e.printStackTrace();
		}finally{
			  return true;
		}
	}

}
