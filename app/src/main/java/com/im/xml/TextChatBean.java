package com.im.xml;

public class TextChatBean {

	String user = "", msg = "", buddy = "", time = "", size, color, face,
			style, fileName = "", old_file = "", signalid = "";
	int type, id, flag;
	private boolean isRobo = false, isimported = false;

	private boolean sent = false;

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public void setUserName(String username) {
		this.user = username;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}

	public void setBuddyName(String buddy) {
		this.buddy = buddy;
	}

	public void setChatTime(String time) {
		this.time = time;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setType(int type) {
		this.type = type;
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
	 * Get the chat message
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.msg;
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
	 * Size of the font
	 * 
	 * @return
	 */
	public String getSize() {
		return this.size;
	}

	/**
	 * color of the font
	 * 
	 * @return
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Font face of the font
	 * 
	 * @return
	 */
	public String getFace() {
		return this.face;
	}

	/**
	 * Style of the font
	 * 
	 * @return
	 */
	public String getStyle() {
		return this.style;
	}

	/**
	 * Get the Type is it a sender or receiver sender=1 ,receiver=2
	 * 
	 * @return
	 */
	public int getType() {
		return this.type;
	}

	public void setIsRobo(boolean robo) {
		this.isRobo = robo;
	}

	public boolean getIsRobo() {
		return this.isRobo;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setisImported(boolean res) {
		this.isimported = res;
	}

	public boolean getisImported() {
		return this.isimported;
	}

	public void setOldFileName(String fname) {
		this.old_file = fname;
	}

	public String getOldFileName() {
		return this.old_file;
	}

	public void setId(int val) {
		this.id = val;
	}

	public int getId() {
		return this.id;
	}

	public void setFlag(int val) {
		this.flag = val;
	}

	public int getFlag() {
		return this.flag;
	}

	public void setSignalId(String val) {
		this.signalid = val;
	}

	public String getSignalId() {
		return this.signalid;
	}
}
