package com.cg.quickaction;

import com.cg.forms.ReservedWords;

public class Buddy extends User {

	private int requestStatus = ReservedWords.BUDDY_REQUEST_PENDING;

	public Buddy(String UserID) {
		super(UserID);
	}

	public int getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(int requestStatus) {
		this.requestStatus = requestStatus;
	}

}
