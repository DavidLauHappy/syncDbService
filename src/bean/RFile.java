package bean;


/**
 * @author David
 * 文档服务器上的远程文件
 */
public class RFile {
	   private String fileName;
	   private String rpath;
	   private String fileID;
	   private String md5;
	   private String localPath="";
	   private String fileTime;
	   
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRpath() {
		return rpath;
	}
	public void setRpath(String rpath) {
		this.rpath = rpath;
	}
	public String getFileID() {
		return fileID;
	}
	public void setFileID(String fileID) {
		this.fileID = fileID;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getFileTime() {
		return fileTime;
	}
	public void setFileTime(String fileTime) {
		this.fileTime = fileTime;
	}
	   
}
