package com.bean;

import java.io.Serializable;
import java.util.Vector;

public class DefaultPermission implements Serializable {
	private String attributeId;
	private String defaultPermission;
	private Vector<BuddyPermission> buddyPermissionList;

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	public String getDefaultPermission() {
		return defaultPermission;
	}

	public void setDefaultPermission(String defaultPermission) {
		this.defaultPermission = defaultPermission;
	}

	public Vector<BuddyPermission> getBuddyPermissionList() {
		return buddyPermissionList;
	}

	public void setBuddyPermissionList(Vector<BuddyPermission> buddyPermissionList) {
		this.buddyPermissionList = buddyPermissionList;
	}
}
