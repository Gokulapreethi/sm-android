package org.lib.model;

public class CallHistoryBean {
	
	private String from=null;
	private String to=null;
	private String sessionId=null;
	private String type=null;
	private String stime=null;
	private String etime=null;
	private String loginUserName=null;
	private String networkState=null;
	private String autoCall=null;
	
	
	
	public String getAutoCall() {
		return autoCall;
	}
	public void setAutoCall(String autoCall) {
		this.autoCall = autoCall;
	}
	public String getNetworkState() {
		return networkState;
	}
	public void setNetworkState(String networkState) {
		this.networkState = networkState;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}


}
