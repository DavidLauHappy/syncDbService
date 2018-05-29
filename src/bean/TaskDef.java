package bean;

import model.TaskDefModel;

//任务定义
public class TaskDef {
	private String id;
	private String tname;
	private String reqId;
	private String status;
	private String arrangeDate;
	private String overdate;
	private String rdate;
	private String owner;
	private String ownerApp;
	private String scheId;
	private String crtUser;
	private String crtTime;
	private String sycnFlag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getArrangeDate() {
		return arrangeDate;
	}
	public void setArrangeDate(String arrangeDate) {
		this.arrangeDate = arrangeDate;
	}
	public String getOverdate() {
		return overdate;
	}
	public void setOverdate(String overdate) {
		this.overdate = overdate;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
		//同时设置用户的id作为任务的应用
		this.ownerApp=TaskDefModel.getUserApp(this.owner);
	}
	public String getScheId() {
		return scheId;
	}
	public void setScheId(String scheId) {
		this.scheId = scheId;
	}
	public String getCrtUser() {
		return crtUser;
	}
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	public String getCrtTime() {
		return crtTime;
	}
	public void setCrtTime(String crtTime) {
		this.crtTime = crtTime;
	}
	public String getSycnFlag() {
		return sycnFlag;
	}
	public void setSycnFlag(String sycnFlag) {
		this.sycnFlag = sycnFlag;
	}
	
	
	public String getOwnerApp() {
		return ownerApp;
	}
	public void addDb(){
		TaskDefModel.getAdd(this);
	}
		
}
