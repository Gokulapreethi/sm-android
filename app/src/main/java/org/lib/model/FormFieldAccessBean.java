package org.lib.model;

import java.io.Serializable;

public class FormFieldAccessBean implements Serializable {
	private String accessibleBuddy;
	private String permissionId;

	public String getAccessibleBuddy() {
		return accessibleBuddy;
	}

	public void setAccessibleBuddy(String accessibleBuddy) {
		this.accessibleBuddy = accessibleBuddy;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
}
