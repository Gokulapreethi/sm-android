package org.lib.model;

import java.io.Serializable;
/**
 * This bean class is used to set and get datas Related to ShareReminder. This class contains information 
 * like FromName,ToName,Type,ReminderStatus,FileLocation,reminder date time,remindertz,file id,id,ftpPassword,status,receiver status.
 * 
 *
 */
@SuppressWarnings("serial")
public class ShareReminder implements Serializable{

	private String from=null;
	private String to=null;
	private String type=null;
	private String reminderStatus=null;
	private String fileLocation=null;
	private String reminderdatetime="";
	private String remindertz=null;
	private String fileid=null;
	private String id=null;
	private String ftpPassword=null;
	private String status=null;
	private String receiverStatus=null;
	private String reminderResponseType="";
	private String isbusyResponse=null;
	private String downloadtype="";
	private String shareComment=null;
	private String vanishMode = null;
	private String vanishValue = null;
	private String parent_id = null;
	private String direction = null;
	private String bsstatus = null;
	private String bstype = null;

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilepath() {
		return filepath;
	}

	private String filepath=null;
	
	
	
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
	public String getReceiverStatus() {
		return receiverStatus;
	}
	public void setReceiverStatus(String receiverStatus) {
		this.receiverStatus = receiverStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFtpPassword() {
		return ftpPassword;
	}
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
		
	public String getReminderStatus() {
		return reminderStatus;
	}
	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	public String getReminderdatetime() {
		return reminderdatetime;
	}
	public void setReminderdatetime(String reminderdatetime) {
		this.reminderdatetime = reminderdatetime;
	}
	public String getRemindertz() {
		return remindertz;
	}
	public void setRemindertz(String remindertz) {
		this.remindertz = remindertz;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	
	public void setReminderResponseType(String rtype)
	{
		this.reminderResponseType=rtype;
	}
	
	public String getReminderResponseType()
	{
		return this.reminderResponseType;
	}
	
	public void setIsbusyResponse(String val)
	{
		this.isbusyResponse=val;
	}
	
	public String getisbusyResponse()
	{
		return this.isbusyResponse;
	}
	
	public String getDownloadType()
	{
		return this.downloadtype;
	}
	
	public void setDownloadType(String dtype)
	{
		this.downloadtype=dtype;
	}
	
	public void setComment(String comment)
	{
		this.shareComment=comment;
	}
	
	public String getShareComment()
	{
		return this.shareComment;
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
