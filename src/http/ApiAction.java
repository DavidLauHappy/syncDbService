package http;

public abstract class ApiAction {
		//接口执行结果的返回json
		public abstract String getResult();
		//接口执行
		public abstract boolean execute();
}
