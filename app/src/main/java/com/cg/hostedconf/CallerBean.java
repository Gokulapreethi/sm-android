package com.cg.hostedconf;

import java.io.Serializable;

public class CallerBean implements Serializable {

	String userName = null;
	String toNnumber = null;

	int call_id;
	String presence = null;
	int hold;
	int mute;

	private int media_count = 0;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToNnumber() {
		return toNnumber;
	}

	public void setToNnumber(String toNnumber) {
		this.toNnumber = toNnumber;
	}

	public int getCall_id() {
		return call_id;
	}

	public void setCall_id(int call_id) {
		this.call_id = call_id;
	}

	public String getPresence() {
		return presence;
	}

	public void setPresense(String presence) {
		this.presence = presence;
	}

	public int getHold() {
		return hold;
	}

	public void setHold(int hold) {
		this.hold = hold;
	}

	public int getMute() {
		return mute;
	}

	public void setMute(int mute) {
		this.mute = mute;
	}

	public void setMediaCount(int count) {
		this.media_count = count;
	}

	public int getMediaCount() {
		return this.media_count;
	}

}
