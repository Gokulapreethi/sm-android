package com.bean;

import java.io.Serializable;

public class EditFormBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String tablename;
	private String columnname;
	private String entrymode;
	private String validata;
	private String defaultvalue;
	private String instruction;
	private String errortip;
	private String attributeid;
	private String formid;
	private String datatype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getEntrymode() {
		return entrymode;
	}

	public void setEntrymode(String entrymode) {
		this.entrymode = entrymode;
	}

	public String getValidata() {
		return validata;
	}

	public void setValidata(String validata) {
		this.validata = validata;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getErrortip() {
		return errortip;
	}

	public void setErrortip(String errortip) {
		this.errortip = errortip;
	}

	public String getAttributeid() {
		return attributeid;
	}

	public void setAttributeid(String attributeid) {
		this.attributeid = attributeid;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

}
