package com.cg.files;

import java.io.Serializable;

/**
 * Bean of component . It's instance has the property of the component
 * 
 * 
 * 
 */
public class Components implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private int noComponentId = 0;
	private String strcomponentType = "";
	private int noVTabId = 0, noHTabId = 0;
			
	private String strPropertyId = "";
	private String strComment = "", strContentPath = "", strContentName = "",
			strContentThumbnail = "", strDateAndTime = "", strRemDateTime = "";
	private int noStatus = 0;
	private String fromuser = "", touser = "", remindertz = "";
	private int noViewMode = 0;
	private String remiderresponsetype="";
	private String noLeft,noTop,noWidth,noHeight;
	private int readFlag;
	private String vanishMode;
	private String vanishValue;
	private String sharestatus;
	private String ftpPath;
	private String parent_id = null;
	private String direction = null;
	private String bsstatus = null;
	private String bstype = null;
	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
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

	public int getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

	/**
	 * To set Component ID
	 * 
	 * @param ComponentId
	 */
	public void setComponentId(int ComponentId) {
		noComponentId = ComponentId;
	}

	/**
	 * To set the componentType
	 * 
	 * @param componentType
	 */
	public void setcomponentType(String componentType) {
		strcomponentType = componentType;
	}

	/**
	 * To set the vertical tab id
	 * 
	 * @param noVTId
	 */
	public void setVTabId(int noVTId) {
		noVTabId = noVTId;
	}

	/**
	 * To set the date id
	 * 
	 * @param noHTId
	 */
	public void setHTabId(int noHTId) {
		noHTabId = noHTId;
	}

	/**
	 * To set the X position of the note on the screen
	 * 
	 * @param noX
	 */
	public void setLeft(String noX) {
		noLeft = noX;
	}

	/**
	 * To set the Y position of the note on the screen
	 * 
	 * @param noY
	 */
	public void setTop(String noY) {
		noTop = noY;
	}

	/**
	 * To set the width of the component
	 * 
	 * @param noWith
	 */
	public void setWidth(String noWith) {
		noWidth = noWith;
	}

	/**
	 * To set the Height of the component
	 * 
	 * @param noHigt
	 */
	public void setHeight(String noHigt) {
		noHeight = noHigt;

	}

	/**
	 * To set the property id of the component
	 * 
	 * @param PropertyId
	 */
	public void setPropertyId(String PropertyId) {
		strPropertyId = PropertyId;
	}

	/**
	 * To set the Content of the component
	 * 
	 * @param Content
	 */
	public void setComment(String Comment) {
		strComment = Comment;
	}

	/**
	 * To set the Location of the content file
	 * 
	 * @param ContentPath
	 */
	public void setContentPath(String ContentPath) {
		strContentPath = ContentPath;
	}

	/**
	 * To set the name of the component
	 * 
	 * @param ContentName
	 */
	public void setContentName(String ContentName) {
		strContentName = ContentName;
	}

	/**
	 * To set the Thumbnail path of the component
	 * 
	 * @param ContentThumbnail
	 */
	public void setContentThumbnail(String ContentThumbnail) {
		strContentThumbnail = ContentThumbnail;
	}

	/**
	 * To set the note creation time
	 * 
	 * @param DateAndTime
	 */
	public void setDateAndTime(String DateAndTime) {
		strDateAndTime = DateAndTime;
	}

	/**
	 * To set the reminder date and time of the component
	 * 
	 * @param RemDateAndTime
	 */
	public void setRemDateAndTime(String RemDateAndTime) {
		strRemDateTime = RemDateAndTime;
	}

	/**
	 * To set the status of the reminder if it is fired or not
	 * 
	 * @param Status
	 */
	public void SetReminderStatus(int Status) {
		noStatus = Status;

	}

	/**
	 * To set the View mode which is in minimized or maximized
	 * 
	 * @param mode
	 */
	public void setViewMode(int mode) {
		noViewMode = mode;
	}

	/**
	 * To set the call from the which user
	 * 
	 * @param fuser
	 */
	public void setfromuser(String fuser) {
		fromuser = fuser;
	}

	/**
	 * To set the call from which user
	 * 
	 * @param tuser
	 */
	public void settoUser(String tuser) {
		touser = tuser;
	}

	/**
	 * set the Reminder timeZone
	 * 
	 * @param remString
	 */
	public void setreminderTz(String remString) {
		remindertz = remString;
	}

	/**
	 * To get ComponentID
	 * 
	 * @return
	 */
	public int getComponentId() {
		return noComponentId;
	}

	/**
	 * To get the type of the Component
	 * 
	 * @return
	 */
	public String getcomponentType() {
		return strcomponentType;
	}

	/**
	 * To get the id of the vertical Tab
	 * 
	 * @return
	 */
	public int getVTabId() {
		return noVTabId;
	}

	/**
	 * To get the HTab id of the component
	 * 
	 * @return
	 */
	public int getHTabId() {
		return noHTabId;
	}

	/**
	 * To get the x position of the component on the screen
	 * 
	 * @return
	 */
	public String getLeft() {
		return noLeft;
	}

	/**
	 * To get the y position of the component on the screen
	 * 
	 * @return
	 */
	public String getTop() {
		return noTop;
	}

	/**
	 * To get the Width of the component
	 * 
	 * @return
	 */
	public String getWidth() {
		return noWidth;
	}

	/**
	 * To get the Height of the component
	 * 
	 * @return
	 */
	public String getHeight() {
		return noHeight;
	}

	/**
	 * To get the property id of the component
	 * 
	 * @return
	 */
	public String getPropertyId() {
		return strPropertyId;
	}

	/**
	 * To get the Content of the component
	 * 
	 * @return
	 */
	public String getComment() {
		return strComment;
	}

	/**
	 * To get the content path of the component
	 * 
	 * @return
	 */
	public String getContentPath() {
		return strContentPath;
	}

	/**
	 * To get the name of the component
	 * 
	 * @return
	 */
	public String getContentName() {
		return strContentName;
	}

	/**
	 * To get the thumbnail path of the component
	 * 
	 * @return
	 */
	public String getContentThumbnail() {
		return strContentThumbnail;
	}

	/**
	 * To get the creation time of the component
	 * 
	 * @return
	 */
	public String getDateAndTime() {
		return strDateAndTime;
	}

	/**
	 * To get the reminder time of the component
	 * 
	 * @return
	 */
	public String getRemDateAndTime() {
		return strRemDateTime;
	}

	/**
	 * To get the reminder status of the component
	 * 
	 * @return
	 */
	public int getReminderStatus() {
		return noStatus;
	}

	/**
	 * To get the view mode of the component which is in minimized or maximized
	 * 
	 * @return
	 */
	public int getViewMode() {
		return noViewMode;
	}

	/**
	 * To get the get call from which user
	 * 
	 * @return
	 */
	public String getfromuser() {
		return fromuser;
	}

	/**
	 * To get the call to which user
	 * 
	 * @return
	 */
	public String gettoUser() {
		return touser;
	}

	/**
	 * To get the reminder timezone
	 * 
	 * @return
	 */
	public String getreminderTz() {
		return remindertz;
	}
	
	public void setresponseTpe(String type)
	{
		this.remiderresponsetype=type;
	}
	
	public String getreminderresponsetype()
	{
		return this.remiderresponsetype;
	}
	public Object clone() {
		try {
			Components cloned = (Components) super.clone();

			return cloned;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public String getSharestatus() {
		return sharestatus;
	}

	public void setSharestatus(String sharestatus) {
		this.sharestatus = sharestatus;
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
}
