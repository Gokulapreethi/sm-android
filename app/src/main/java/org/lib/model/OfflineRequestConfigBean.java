package org.lib.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.net.Uri;

public class OfflineRequestConfigBean implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String buddyId;

	private String messageTitle;

	private String messagetype;

	private String message;

	private String responseType;

	private String response;

	private String receivedTime;

	private String sendStatus;

	private String from;

	private String when;

	private String config_id;

	private String recordTime;

	private int status;

	private String id;

	private String mode;

	private String userId;

	private String url;

	private String configId;

	private ArrayList<OfflineRequestConfigBean> defalut_list = new ArrayList<OfflineRequestConfigBean>();

	private ArrayList<OfflineRequestConfigBean> buddy_list = new ArrayList<OfflineRequestConfigBean>();

	// Edit 05-04-15
	private String saveButtonTitle;
	private String whenreply;
	private int modeInt;
	private Integer ListPosition;
	private String position;
	private String calltype;
	private String callBuddies;
	private Uri uri;
	private boolean addNewList;
	private int requestCode;
	private int resultCode;
//	private int takeFlags;

	public Integer getListPosition() {
		return ListPosition;
	}

	public void setListPosition(Integer listPosition) {
		ListPosition = listPosition;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getCallBuddies() {
		return callBuddies;
	}

	public void setCallBuddies(String callBuddies) {
		this.callBuddies = callBuddies;
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public boolean isAddNewList() {
		return addNewList;
	}

	public void setAddNewList(boolean addNewList) {
		this.addNewList = addNewList;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

//	public int getTakeFlags() {
//		return takeFlags;
//	}
//
//	public void setTakeFlags(int takeFlags) {
//		this.takeFlags = takeFlags;
//	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBuddyId() {
		return buddyId;
	}

	public void setBuddyId(String buddyId) {
		this.buddyId = buddyId;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUrl() {
		return url;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public String getConfig_id() {
		return config_id;
	}

	public void setConfig_id(String config_id) {
		this.config_id = config_id;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<OfflineRequestConfigBean> getDefalut_list() {
		return defalut_list;
	}

	public void setDefalut_list(ArrayList<OfflineRequestConfigBean> defalut_list) {
		this.defalut_list = defalut_list;
	}

	public ArrayList<OfflineRequestConfigBean> getBuddy_list() {
		return buddy_list;
	}

	public void setBuddy_list(ArrayList<OfflineRequestConfigBean> buddy_list) {
		this.buddy_list = buddy_list;
	}

	public OfflineRequestConfigBean clone() throws CloneNotSupportedException {
		return (OfflineRequestConfigBean) super.clone();

	}

	public int getModeInt() {
		return modeInt;
	}

	public void setModeInt(int modeInt) {
		this.modeInt = modeInt;
	}

	public String getWhenreply() {
		return whenreply;
	}

	public void setWhenreply(String whenreply) {
		this.whenreply = whenreply;
	}

	public String getSaveButtonTitle() {
		return saveButtonTitle;
	}

	public void setSaveButtonTitle(String saveButtonTitle) {
		this.saveButtonTitle = saveButtonTitle;
	}

}
