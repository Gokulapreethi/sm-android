package org.lib.model;

import android.content.Context;

public class ShareSendBean {

	private String strFileId;
	private String status;

	private String filePath;
	private String userName;
	private Context ctxt;
	private String componenttype;
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFileId(String strFileId) {
		this.strFileId = strFileId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileId() {
		return this.strFileId;
	}

	public String getStatus() {
		return this.status;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getUserName() {
		return this.userName;
	}
	
	public void setContext(Context con)
	{
		this.ctxt=con;
	}
	
	public Context getContext()
	{
		return this.ctxt;
	}
	
	public void setcomponenttype(String type)
	{
		this.componenttype=type;
	}
	public String getcomponentType()
	{
		return this.componenttype;
	}
}
