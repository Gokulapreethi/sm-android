package org.lib.model;

import java.util.ArrayList;
import java.util.HashMap;

public class FormsBean {
	String form_name;
	String form_id;
	String rec_id;
	String form_fields;
	ArrayList<String[]> form_records;
	ArrayList<HashMap<String, String>> records;

	String User_name;
	String form_types;
	String deleted_username;
	String form_time;
	String flag_records;
	String row_count;

	boolean isDeleted_information = false;
	boolean isdeleteRecordInformation = false;
	boolean iseditRecordInformation = false;

	public ArrayList<HashMap<String, String>> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<HashMap<String, String>> records) {
		this.records = records;
	}

	public void setFormName(String name) {
		this.form_name = name;
	}

	public String getFormtime() {
		return this.form_time;
	}

	public void setCount(String count) {
		this.row_count = count;
	}

	public String getcount() {
		return this.row_count;
	}

	public void setFormTime(String time) {
		this.form_time = time;
	}

	public String getFormName() {
		return this.form_name;
	}

	public void setFormId(String id) {
		this.form_id = id;
	}

	public String getFormId() {
		return this.form_id;
	}

	public void setFlag(String flag) {
		this.flag_records = flag;
	}

	public String getFlag() {
		return this.flag_records;
	}

	public void setFromFields(String fields) {
		this.form_fields = fields;
	}

	public String getFormFields() {
		return this.form_fields;
	}

	public void setFormRecords(ArrayList<String[]> recs) {
		this.form_records = recs;
	}

	public ArrayList<String[]> getFormRecords() {
		return this.form_records;
	}

	public void setuserName(String user) {
		this.User_name = user;
	}

	public String getUserName() {
		return this.User_name;
	}

	public void setFormTypes(String types) {
		this.form_types = types;
	}

	public String gtFormTypes() {
		return this.form_types;
	}

	public void setDeletedUserName(String name) {
		this.deleted_username = name;
	}

	public String getDeletedUserName() {
		return this.deleted_username;
	}

	public void setIsDeletedForm(boolean res) {
		this.isDeleted_information = res;
	}

	public boolean isDeletedForm() {
		return this.isDeleted_information;
	}

	public void setIsDeletedRecord(boolean res) {
		this.isdeleteRecordInformation = res;
	}

	public boolean isEditRecord() {
		return this.iseditRecordInformation;
	}

	public void setIsEditRecord(boolean res) {
		this.iseditRecordInformation = res;
	}

	public boolean isDeletedRecord() {
		return this.isdeleteRecordInformation;
	}

	public String getDeletedRecordId() {
		return this.rec_id;
	}

	public void setDeletedrecordId(String id) {
		this.rec_id = id;
	}

	public String getEditRecordId() {
		return this.rec_id;
	}

	public void setEditrecordId(String id) {
		this.rec_id = id;
	}

}
