package com.cg.files;

import java.io.Serializable;

/**
 * This instance is used to maintain the every note created by the user. Those
 * entries will populated in the Complete list view screen
 * 
 * 
 * 
 */
public class CompleteListBean implements Serializable {

	private int noComponentId;
	private String strContent;
	private String strDateAndTime;
	private String strcomponentType;
	private String strContentName;
	private String strContentPath;

	private String fromuser = "";
	private String touser = "";
	private String responseType = "";
	private String reminderTime = "";
	private String isResponded = "";
	private String ftpPath = "";
	private int viewmode = 0;
	private String vanishMode;
	private String vanishValue;
	private String comment = "";
	private String owner = "";
	private String reminderZone = "";
	private String propertyId;
	private String streamThumb = "";
	private boolean selected;
	private String parent_id = null;
	private String direction = null;
	private String bsstatus = null;
	private String bstype = null;
	private String fromName;
	private String toName;
	private String sessionid;
	private String parent_idcall=null;
	private String type;
	private String startTime;
	private String endTime;
	private String userId;
	private String network;
	private String deviceOS;
	private String calltype;
	private String ac;
	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getCalltype() {
		return calltype;
	}

	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getStreamThumb() {
		return streamThumb;
	}

	public void setStreamThumb(String streamThumb) {
		this.streamThumb = streamThumb;
	}

	public String getVanishMode() {
		return vanishMode;
	}

	public void setVanishMode(String vanishMode) {
		this.vanishMode = vanishMode;
	}

	public String getVanishValue() {
		return vanishValue;
	}

	public void setVanishValue(String vanishValue) {
		this.vanishValue = vanishValue;
	}

	/**
	 * To set component id
	 * 
	 * @param ComponentId
	 */

	public void setComponentId(int ComponentId) {
		noComponentId = ComponentId;
	}

	/**
	 * To set the type of the component
	 * 
	 * @param componentType
	 */
	public void setcomponentType(String componentType) {
		strcomponentType = componentType;
	}

	/**
	 * To set the content of the component
	 * 
	 * @param Content
	 */
	public void setContent(String Content) {
		strContent = Content;
	}

	/**
	 * To set the content name of the component
	 * 
	 * @param Content
	 */
	public void setContentName(String Content) {
		strContentName = Content;
	}

	/**
	 * To set the location of the file which is exist in the device
	 * 
	 * @param strPath
	 */
	public void setContentPath(String strPath) {

		strContentPath = strPath;
	}

	public void setFromUser(String name) {
		this.fromuser = name;
	}

	public void setTouser(String name) {
		this.touser = name;
	}

	public void setResponseType(String type) {
		this.responseType = type;
	}

	public void setReminderTime(String time) {
		this.reminderTime = time;
	}

	/**
	 * To get the text content
	 * 
	 * @return
	 */
	public String getContentpath() {
		return strContentPath;
	}

	/**
	 * To set the date and Time of note createion
	 * 
	 * @param DateAndTime
	 */
	public void setDateAndTime(String DateAndTime) {
		strDateAndTime = DateAndTime;
	}

	/**
	 * To get the component id
	 * 
	 * @return
	 */
	public int getComponentId() {
		return noComponentId;
	}

	/**
	 * To get the component type
	 * 
	 * @return
	 */
	public String getcomponentType() {
		return strcomponentType;
	}

	/**
	 * To get the content of the component
	 * 
	 * @return
	 */
	public String getContent() {
		return strContent;
	}

	/**
	 * To get the ContentName
	 * 
	 * @return
	 */
	public String getContentName() {
		return strContentName;
	}

	/**
	 * To get the date and Time of component creation
	 * 
	 * @return
	 */
	public String getDateAndTime() {
		return strDateAndTime;
	}

	public String getFromUser() {
		return this.fromuser;
	}

	public String getTouser() {
		return this.touser;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getReminderZone() {
		return reminderZone;
	}

	public void setReminderZone(String reminderZone) {
		this.reminderZone = reminderZone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReminderResponseType() {
		return this.responseType;
	}

	public String getReminderTime() {
		return this.reminderTime;
	}

	public void setIsresponsed(String val) {
		this.isResponded = val;
	}

	public String getIsresponsed() {
		return this.isResponded;
	}

	public String getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(String id) {
		this.propertyId = id;
	}

	public void setViewMode(int mode) {
		this.viewmode = mode;
	}

	public int getViewmode() {
		return this.viewmode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBsstatus() {
		return bsstatus;
	}

	public void setBsstatus(String bsstatus) {
		this.bsstatus = bsstatus;
	}

	public String getBstype() {
		return bstype;
	}

	public void setBstype(String bstype) {
		this.bstype = bstype;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParent_idcall() {
		return parent_idcall;
	}

	public void setParent_idcall(String parent_idcall) {
		this.parent_idcall = parent_idcall;
	}

}
