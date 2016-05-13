package com.im.xml;

public class AudioChatBean {

	String user, file="", buddy, time,old_file="",signalid;
	int type,id,flag;
	private boolean isRobo=false,isimported=false;
	
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
	
	public void setIsRobo(boolean robo)
	{
		this.isRobo=robo;
	}
	
	public boolean getIsRobo()
	{
		return this.isRobo;
	}

	public void setisImported(boolean res)
	{
		this.isimported=res;
	}
	public boolean getisImported()
	{
		return this.isimported;
	}
	public void setOldFileName(String filename)
	{
		this.old_file=filename;
	}
	public String getOldFileName()
	{
		return this.old_file;
	}

	public void setId(int val)
	{
		this.id=val;
	}
	public int getId()
	{
		return this.id;
	}
	public void setFlag(int val)
	{
		this.flag=val;
	}
	public int getFlag()
	{
		return this.flag;
	}
	public void setSignalId(String val)
	{
		this.signalid=val;
	}
	public String getSignalId()
	{
		return this.signalid;
	}
}
