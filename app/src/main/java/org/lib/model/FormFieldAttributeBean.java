package org.lib.model;

import java.io.Serializable;

public class FormFieldAttributeBean implements Serializable {
	private String attributeId;
	private String defaultPermission;

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
}
