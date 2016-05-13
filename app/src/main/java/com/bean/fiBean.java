package com.bean;

import java.util.ArrayList;
import java.util.List;

public class fiBean {
	private String permission_name;
	private String messageTitle;
	private String type;
	private int spinnerposition;
	private String arralist;
	private String typespace;
	private String fieldValue;
	private String validation;
	private List<String> spinnerArray = new ArrayList<String>();
	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getTypespace() {
		return typespace;
	}

	public void setTypespace(String typespace) {
		this.typespace = typespace;
	}

	public String getArralist() {
		return arralist;
	}

	public void setArralist(String arralist) {
		this.arralist = arralist;
	}

	public List<String> getSpinnerArray() {
		return spinnerArray;
	}

	public void setSpinnerArray(List<String> spinnerArray) {
		this.spinnerArray = spinnerArray;
	}

	public int getSpinnerposition() {
		return spinnerposition;
	}

	public void setSpinnerposition(int spinnerposition) {
		this.spinnerposition = spinnerposition;
	}
}
