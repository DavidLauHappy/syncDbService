package func;

import bean.FileItem;

public interface FileFunction {
	//对接口文件的处理，包括文件导出与摆渡，摆渡文件获取与导入
	public String fileProcess(FileItem file);
}
