package com.bean;

import java.io.Serializable;

public class BuddyPermission implements Serializable {
	private String buddyName;
	private String buddyAccess;

	public String getBuddyName() {
		return buddyName;
	}

	public void setBuddyName(String buddyName) {
		this.buddyName = buddyName;
	}

	public String getBuddyAccess() {
		return buddyAccess;
	}

	public void setBuddyAccess(String buddyAccess) {
		this.buddyAccess = buddyAccess;
	}

}
