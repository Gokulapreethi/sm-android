package org.lib.model;

/**
 * This bean class is used to set and get data's Related to BuddyInformation.
 * This class contains information like BuddyName,Status,Local
 * IpAddress,ExternalIpAddress,Signaling Port,Email Id,Type.
 * 
 * 
 */

public class FormAttributeBean {

	private String formid = null;

	private String attributeid = null;

	private String fieldname = null;

	private String fieldtype = null;

	private String dataentrymode = null;

	private String datasource = null;

	private String datavalidation = null;

	private String defaultvalue = null;

	private String instruction = null;

	private String errortip = null;

	private String table_name;

	public void setTableName(String name) {
		this.table_name = name;
	}

	public String getTableName() {
		return this.table_name;
	}

	public String getFormid() {
		return formid;
	}

	public void setformid(String formsid) {
		this.formid = formsid;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setfieldname(String fieldnamee) {
		this.fieldname = fieldnamee;
	}

	public String getAttributeid() {
		return attributeid;
	}

	public void setAttributeid(String atid) {
		this.attributeid = atid;
	}

	public void setDataEntry(String entry) {
		this.dataentrymode = entry;
	}

	public String getEntry() {
		return dataentrymode;
	}

	public void setFieldtype(String ftype) {
		this.fieldtype = ftype;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setDataSource(String dsource) {
		this.datasource = dsource;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDataValidation(String datavali) {
		this.datavalidation = datavali;
	}

	public String getDatavalidation() {
		return datavalidation;
	}

	public void setDefaultvalue(String defaultval) {
		this.defaultvalue = defaultval;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setInstruction(String instruc) {
		this.instruction = instruc;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setErrortip(String error) {
		this.errortip = error;
	}

	public String getErrortip() {
		return errortip;
	}

}
