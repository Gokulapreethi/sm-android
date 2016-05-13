package org.lib.model;

/**
 * This bean class is used to set and get datas Related to KeepAlive(HeartBeat).
 * This class contains information like UserName,Local
 * IpAddress,Password,External IpAddress,Type,Status,Id,share Remainder Action.
 * 
 * 
 */
public class KeepAliveBean {
	private String name = null;

	private String localipaddress = null;

	private String password = null;

	private String externalipaddress = null;

	private String type = null;

	private String status = null;

	private String id = null;

	private String shareReminderAction = null;

	private String key = "0";

	private String aver;

	private String avdate;

	private String dtype;

	private String lat;

	private String lon;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalipaddress() {
		return localipaddress;
	}

	public void setLocalipaddress(String localipaddress) {
		this.localipaddress = localipaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExternalipaddress() {
		return externalipaddress;
	}

	public void setExternalipaddress(String externalipaddress) {
		this.externalipaddress = externalipaddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShareReminderAction() {
		return shareReminderAction;
	}

	public void setShareReminderAction(String shareReminderAction) {
		this.shareReminderAction = shareReminderAction;
	}

	public void setaver(String ver) {
		this.aver = ver;
	}

	public String getAver() {
		return this.aver;
	}

	public void setavdate(String dt) {
		this.avdate = dt;
	}

	public String getavdate() {
		return this.avdate;
	}

	public void setdtype(String type) {
		this.dtype = type;
	}

	public String getDtype() {
		return this.dtype;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

}
