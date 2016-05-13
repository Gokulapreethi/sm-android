package com.cg.instancemessage;

/**
 * Instance of this bean is used to maintain the attributes of the IM
 * 
 * 
 * 
 */
public class ImBeanUpdated {
	private String fromUser = null;

	private String ToUser = null;

	private String buddyName = null;

	private String messageText = null;

	private String sessionId = null;

	private int identifyFromTo;

	private String Time = null;

	/**
	 * To set the From User
	 * 
	 * @param fromuser
	 */
	public void putFromUser(String fromuser) {
		this.fromUser = fromuser;
	}

	/**
	 * To set the to user
	 * 
	 * @param touser
	 */
	public void putToUser(String touser) {
		this.ToUser = touser;
	}

	/**
	 * To set the Buddy Name
	 * 
	 * @param buddyname
	 */
	public void putbuddyName(String buddyname) {
		this.buddyName = buddyname;
	}

	/**
	 * To put the im message
	 * 
	 * @param messagetext
	 */
	public void putmessageText(String messagetext) {
		this.messageText = messagetext;
	}

	/**
	 * To put the session id
	 * 
	 * @param sessionid
	 */
	public void putsessionId(String sessionid) {
		this.sessionId = sessionid;
	}

	/**
	 * To set the token to identify the message by the sender or receiver
	 * 
	 * @param identifyfromto
	 */
	public void putidentifyFromTo(int identifyfromto) {
		this.identifyFromTo = identifyfromto;
	}

	/**
	 * to put the chat send / receive time
	 * 
	 * @param time
	 */
	public void putTime(String time) {
		this.Time = time;
	}

	/**
	 * To get the From user
	 * 
	 * @return String
	 */
	public String getFromUser() {
		return this.fromUser;
	}

	/**
	 * To get the to user
	 * 
	 * @return String
	 */
	public String getToUser() {
		return this.ToUser;
	}

	/**
	 * To get the Buddy name
	 * 
	 * @return String
	 */
	public String getbuddyName() {
		return this.buddyName;
	}

	/**
	 * To get the IM Message
	 * 
	 * @return String
	 */
	public String getmessageText() {
		return this.messageText;
	}

	/**
	 * To get the session Id
	 * 
	 * @return String
	 */
	public String getsessionId() {
		return this.sessionId;
	}

	/**
	 * To get the token to identify the message from sender or receiver
	 * 
	 * @return
	 */
	public int getidentifyFromTo() {
		return this.identifyFromTo;
	}

	/**
	 * To get the send / receive time of the message
	 * 
	 * @return
	 */
	public String getTime() {
		return this.Time;
	}

}
