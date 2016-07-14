package com.bean;

import java.io.Serializable;

public class GroupTempBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String groupId;
	private boolean isGroup;
	private String buddy;
	private String nickname;
	private String buddyStatus;
	private String open;

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBuddyStatus() {
		return buddyStatus;
	}

	public void setBuddyStatus(String buddyStatus) {
		this.buddyStatus = buddyStatus;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}
}
