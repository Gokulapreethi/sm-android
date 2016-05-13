package com.cg.timer;

import java.util.ArrayList;

public class ReminderRetrieve {
	boolean isResponseType;
	ArrayList<Integer> alList;

	public void setResponseType(boolean status) {
		isResponseType = status;
	}

	public void setReminderList(ArrayList<Integer> al) {
		alList = al;
	}

	public boolean getResponseType() {
		return isResponseType;
	}

	public ArrayList<Integer> getReminderList() {
		return alList;
	}
}
