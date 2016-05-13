package com.bean;

import java.io.Serializable;
import java.util.Vector;

public class FormFieldBean implements Serializable {
	private String formId;
	private String fieldName;
	private String attributeId;
	private Vector<DefaultPermission> defaultPermissionList;
	private Vector<IndividualPermission> individualPermissionList;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public Vector<DefaultPermission> getDefaultPermissionList() {
		return defaultPermissionList;
	}

	public void setDefaultPermissionList(
			Vector<DefaultPermission> defaultPermissionList) {
		this.defaultPermissionList = defaultPermissionList;
	}

	public Vector<IndividualPermission> getIndividualPermissionList() {
		return individualPermissionList;
	}

	public void setIndividualPermissionList(
			Vector<IndividualPermission> individualPermissionList) {
		this.individualPermissionList = individualPermissionList;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
}
