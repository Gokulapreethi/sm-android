package com.cg.quickaction;

import java.util.ArrayList;

import com.cg.forms.ReservedWords;




public class User {

	private String UserID = null;
	private int status = ReservedWords.STATUS_AVAILABLE;
	private ArrayList<Buddy> buddies = new ArrayList<Buddy>();

	public String getStatus() {
		
		return ReservedWords.STATUS[status];
	}

	public String[] getBuddiesList()
	{
		String[] budList = new String[buddies.size()];
		for (int i=0; i<buddies.size(); i++){
			budList[i] = buddies.get(i).getUserID();
		}
		return budList;
	}
	
	public String[] getActiveBuddiesList()
	{
		String[] budList = new String[buddies.size()];
		for (int i=0; i<buddies.size(); i++){
			budList[i] = buddies.get(i).getUserID();
		}
		return budList;
	}
	
	
	public boolean addBuddy(Buddy user){
		buddies.add(user);
		return true;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserID() {
		return UserID;
	}

	private void setUserID(String userID) {
		UserID = userID;
		
	}
	
	public User()
	{
		
	}
	
	public User(String userID){
		this.setUserID(userID);		
	}

	
	
}
