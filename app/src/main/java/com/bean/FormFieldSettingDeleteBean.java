package com.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FormFieldSettingDeleteBean implements Serializable {
	private String userId;
	private String formId;
	private ArrayList<String[]> buddiesList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public ArrayList<String[]> getBuddiesList() {
		return buddiesList;
	}

	public void setBuddiesList(ArrayList<String[]> accessList) {
		this.buddiesList = accessList;
	}
}
