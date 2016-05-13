package com.bean;

import java.io.Serializable;

public class GroupTempBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String groupId;
	private boolean isGroup;
	private String buddy;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getBuddy() {
		return buddy;
	}

	public void setBuddy(String buddy) {
		this.buddy = buddy;
	}

}
