package bean;

import model.BackLogModel;

public class BackLog {
	private String id;
	private String suser;
	private String dept;
	private String sdate;
	private String iuser;
	private String name;
	private String background;
	private String rdesc;
	private String rdate;
	private String reason;
	private String cuser;
	private String auser;
	private String comment;
	private String rscr;
	private String rtype;
	private String rclass;
	private String status;
	private String curUser;
	private String mdfTime;
	private String syncFlag;
	private String link;
	private String scheID;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSuser() {
		return suser;
	}
	public void setSuser(String suer) {
		this.suser = suer;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getIuser() {
		return iuser;
	}
	public void setIuser(String iuser) {
		this.iuser = iuser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getRdesc() {
		return rdesc;
	}
	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCuser() {
		return cuser;
	}
	public void setCuser(String cuser) {
		this.cuser = cuser;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRscr() {
		return rscr;
	}
	public void setRscr(String rscr) {
		this.rscr = rscr;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getRclass() {
		return rclass;
	}
	public void setRclass(String rclass) {
		this.rclass = rclass;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurUser() {
		return curUser;
	}
	public void setCurUser(String curUser) {
		this.curUser = curUser;
	}
	public String getMdfTime() {
		return mdfTime;
	}
	public void setMdfTime(String mdfTime) {
		this.mdfTime = mdfTime;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getScheID() {
		return scheID;
	}
	public void setScheID(String scheID) {
		this.scheID = scheID;
	}
	public String getAuser() {
		return auser;
	}
	public void setAuser(String auser) {
		this.auser = auser;
	}
	
		public void addReqAttach(String fileID,String md5,String remotePath,String fileName,String fileTime,boolean upload,String mdfUser){
		BackLogModel.addReqAttach(this.id, fileID, md5, remotePath, fileName, fileTime, upload, mdfUser);
	}
	
	public void addToDb(){
		BackLogModel.add(this);
	}
}
