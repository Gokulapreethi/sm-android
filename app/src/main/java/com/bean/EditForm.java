package com.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class EditForm implements Serializable {


	private static final long serialVersionUID = 1L;
	private String mode;
	private String text;
	private HashMap<String, EditFormBean> eList;

	public HashMap<String, EditFormBean> geteList() {
		return eList;
	}

	public void seteList(HashMap<String, EditFormBean> eList) {
		this.eList = eList;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
