package com.bean;

import java.io.Serializable;

public class SpecialMessageBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String subcategory;
	private String members;
	private String remindertime;
	private String parentId;
	private boolean isReplyBack;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getRemindertime() {
		return remindertime;
	}

	public void setRemindertime(String remindertime) {
		this.remindertime = remindertime;
	}

	public boolean isReplyBack() {
		return isReplyBack;
	}

	public void setReplyBack(boolean isReplyBack) {
		this.isReplyBack = isReplyBack;
	}
}
