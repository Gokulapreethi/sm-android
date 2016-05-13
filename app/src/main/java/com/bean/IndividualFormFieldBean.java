package com.bean;

import java.io.Serializable;

public class IndividualFormFieldBean implements Serializable {
	private String attributeId;
	private String permission;

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}
