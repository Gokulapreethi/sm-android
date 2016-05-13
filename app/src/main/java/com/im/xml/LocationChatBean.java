package com.im.xml;

public class LocationChatBean {

	String user, file, buddy, time, latitude, longitude, address;

	int type;

	public void setUserName(String username) {
		this.user = username;
	}

	public void setFilePath(String file) {
		this.file = file;
	}

	public void setBuddyName(String buddy) {
		this.buddy = buddy;
	}

	public void setChatTime(String time) {
		this.time = time;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Who is the user
	 * 
	 * @return
	 */
	public String getUserName() {
		return this.user;
	}

	/**
	 * Path of the resource file
	 * 
	 * @return
	 */
	public String getFilePath() {
		return this.file;
	}

	/**
	 * Name or names of the buddies chat with the user
	 * 
	 * @return
	 */
	public String getBuddyName() {
		return this.buddy;
	}

	/**
	 * Chat time and date
	 * 
	 * @return
	 */
	public String getChatTime() {
		return this.time;
	}

	/**
	 * Get the Type is it a sender or receiver sender=1 ,receiver=2
	 * 
	 * @return
	 */
	public int getType() {
		return this.type;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public String getAddress() {
		return this.address;
	}

}
