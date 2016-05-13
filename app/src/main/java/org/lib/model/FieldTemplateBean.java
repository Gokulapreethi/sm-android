package org.lib.model;



public class FieldTemplateBean {

	private String groupName;

	private String groupId;

	private String fieldId;

	private String fieldName;

	private String fieldType;

	private String createdDate;

	private String modifiedDate;

	private String status="0";

	private String filedvalue;

	private String username;

	private String field_modifieddate;

	private String field_createddate;

	private int uploaddownloadStatus = 0;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFiledvalue() {
		return filedvalue;
	}

	public void setFiledvalue(String filedvalue) {
		this.filedvalue = filedvalue;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getField_modifieddate() {
		return field_modifieddate;
	}

	public void setField_modifieddate(String field_modifieddate) {
		this.field_modifieddate = field_modifieddate;
	}

	public String getField_createddate() {
		return field_createddate;
	}

	public void setField_createddate(String field_createddate) {
		this.field_createddate = field_createddate;
	}

	/**
	 * @return the uploaddownloadStatus
	 */
	public int getUploaddownloadStatus() {
		return uploaddownloadStatus;
	}

	/**
	 * @param uploaddownloadStatus
	 *            the uploaddownloadStatus to set
	 */
	public void setUploaddownloadStatus(int uploaddownloadStatus) {
		this.uploaddownloadStatus = uploaddownloadStatus;
	}

}
