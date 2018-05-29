package bean;

import model.BatchModel;

public class Batch {
		private String schID;
		private String expireDate;
		private String crtUser;
		private String crtTime;
		private String dataFlag;
		public String getSchID() {
			return schID;
		}
		public void setSchID(String schID) {
			this.schID = schID;
		}
		public String getExpireDate() {
			return expireDate;
		}
		public void setExpireDate(String expireDate) {
			this.expireDate = expireDate;
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
		public String getDataFlag() {
			return dataFlag;
		}
		public void setDataFlag(String dataFlag) {
			this.dataFlag = dataFlag;
		}
		public void setCrtTime(String crtTime) {
			this.crtTime = crtTime;
		}
		
		public void sync(){
			BatchModel.add(this);
		}
		
		
}
