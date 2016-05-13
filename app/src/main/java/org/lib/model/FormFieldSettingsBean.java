package org.lib.model;

import java.io.Serializable;

public class FormFieldSettingsBean implements Serializable {
	private String formId;
	private String userId;
	private String mode;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
