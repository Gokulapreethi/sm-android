package org.lib.model;

import java.util.ArrayList;

import com.bean.FormFieldSettingDeleteBean;

public class UdpMessageBean {
	String type = null;
	String sid = null;
	String eid = null;
	String text = null;
	ArrayList<Object> list = new ArrayList<Object>();
	ArrayList<Object> forms_list;
	private String[] config_responselist;

	private Object deletfrmbn = null;
	private Object editfrmbean = null;
	private Object settingfrmbean = null;
	private boolean resetProfile = false;

	private String[] callrresponseupadte;

	private String[] permission_details;

	private String[] profile_details;

	private String[] reset_account;
	
	private String[] groupChatSettings;

	private Object responseObject;

	private String[] formFieldSettings;

	private FormFieldSettingDeleteBean fSettingDeleteBean;

	private String[] editForm;

	// String name=null;
	// String status=null;
	// String localipaddress=null;
	// String externalipaddress=null;
	// String signalingport=null;

	BuddyInformationBean bib = null;

	public String[] getEditForm() {
		return editForm;
	}

	public void setEditForm(String[] editForm) {
		this.editForm = editForm;
	}

	VirtualBuddyBean vbb = null;

	public String[] getProfile_details() {
		return profile_details;
	}

	public void setProfile_details(String[] profile_details) {
		this.profile_details = profile_details;
	}

	public VirtualBuddyBean getVirtualBuddyBean() {
		return vbb;
	}

	public boolean isResetProfile() {
		return resetProfile;
	}

	public void setResetProfile(boolean resetProfile) {
		this.resetProfile = resetProfile;
	}

	public void setVirtualBuddyBean(VirtualBuddyBean vbb) {
		this.vbb = vbb;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setBuddyInformation(BuddyInformationBean bib) {

		this.bib = bib;
	}

	public String[] getReset_account() {
		return reset_account;
	}

	public void setReset_account(String[] reset_account) {
		this.reset_account = reset_account;
	}

	public BuddyInformationBean getBuddyInformation() {
		return bib;
	}

	public ArrayList<Object> getList() {
		return this.list;
	}

	public void setlist(ArrayList<Object> list1) {
		this.list = list1;
	}

	public ArrayList<Object> getFromsList() {
		return this.forms_list;
	}

	public void setFormslist(ArrayList<Object> list1) {
		this.forms_list = list1;
	}

	public void setSettingList(Object obj) {
		this.settingfrmbean = obj;
	}

	public Object getSettingobj() {
		return this.settingfrmbean;
	}

	public void setDeleteObject(Object obj) {
		this.deletfrmbn = obj;
	}

	public Object getDeleteObject() {
		return this.deletfrmbn;
	}

	public void setEditObject(Object obj) {
		this.editfrmbean = obj;
	}

	public Object getEditObject() {
		return this.editfrmbean;
	}

	public String[] getConfig_responselist() {
		return config_responselist;
	}

	public FormFieldSettingDeleteBean getfSettingDeleteBean() {
		return fSettingDeleteBean;
	}

	public void setfSettingDeleteBean(
			FormFieldSettingDeleteBean fSettingDeleteBean) {
		this.fSettingDeleteBean = fSettingDeleteBean;
	}

	public String[] getFormFieldSettings() {
		return formFieldSettings;
	}

	public void setFormFieldSettings(String[] formFieldSettings) {
		this.formFieldSettings = formFieldSettings;
	}

	public void setConfig_responselist(String[] config_responselist) {
		this.config_responselist = config_responselist;
	}

	public Object getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public String[] getCallrresponseupadte() {
		return callrresponseupadte;
	}

	public void setCallrresponseupadte(String[] callrresponseupadte) {
		this.callrresponseupadte = callrresponseupadte;
	}

	public String[] getPermission_details() {
		return permission_details;
	}

	public void setPermission_details(String[] permission_details) {
		this.permission_details = permission_details;
	}

	public String[] getGroupChatSettings() {
		return groupChatSettings;
	}

	public void setGroupChatSettings(String[] groupChatSettings) {
		this.groupChatSettings = groupChatSettings;
	}

}
