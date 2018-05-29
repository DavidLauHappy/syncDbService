package http;

public class TaskAddAction extends ApiAction{
	private String resultJson;
	private String InParameters;
	public TaskAddAction(String parameters){
		this.InParameters=parameters;
	}
	
	@Override
	public String getResult() {
		return resultJson;
	}

	@Override
	public boolean execute() {
		
		return false;
	}
}
