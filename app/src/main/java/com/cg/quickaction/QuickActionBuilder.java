package com.cg.quickaction;

import java.util.ArrayList;

import android.util.Log;
import android.util.SparseArray;

public class QuickActionBuilder {

	private User user = null;

	private ArrayList<ContactLogicbean> quickActionList = new ArrayList<ContactLogicbean>();
	private SparseArray<QuickAction> quickActionMap = new SparseArray<QuickAction>();

	// private HashMap<Integer,QuickAction> quickActionMap= new
	// HashMap<Integer,QuickAction>();
	// private static ArrayList<QuickActionBuilder> instances = new
	// ArrayList<QuickActionBuilder>();

	private static QuickActionBuilder instance = null;

	public User getUser() {
		return user;
	}

	public boolean getQAListFromDataStore() {

		return true;

	}

	public QuickAction getQuickAction(ContactLogicbean bean) {
		// TODO
		int id = bean.getId();
		return quickActionMap.get(id);
	}

	public void setQuickAction(int id, QuickAction action) {
		quickActionMap.put(id, action);
	}

	public static ContactLogicbean getSecLogicbean(String user,
			String actionCode, int ID) {
		ContactLogicbean bean = new ContactLogicbean();
		bean.setAction(actionCode);
		bean.setId(ID);
		QuickAction action = getQuickAction(actionCode);

		if (action == null) {

			return null;
		}

		action.setId(ID);
		getInstance(user).setQuickAction(ID, action);

		return bean;
	}

	public static ContactLogicbean getSecLogicbean(String user,
			String actionCode) {
		ContactLogicbean bean = new ContactLogicbean();
		bean.setAction(actionCode);
		QuickAction action = getQuickAction(actionCode);
		if (action == null) {

			return null;
		}

		getInstance(user).setQuickAction(bean.getId(), action);

		return bean;
	}

	public static QuickAction getQuickAction(String actionCode) {
		int quickActionType = -1;
		for (int i = 0; i < QuickActionType.QuickActionAllTypesCode.length; i++) {
			if (QuickActionType.QuickActionAllTypesCode[i].equals(actionCode)) {
				quickActionType = i + 1;
				break;
			}

		}
		QuickAction action = null;
		switch (quickActionType) {

		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			action = new CallQuickAction();
			action.setActionDBCode(quickActionType - 1);

			break;
		case 6:
		case 7:
			action = new BroadcastQuickAction();
			action.setActionDBCode(quickActionType - 1);
			break;
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			action = new ShareQuickAction();
			action.setActionDBCode(quickActionType - 1);
			break;
		case 13:
			action = new  ReportQuickAction();
			action.setActionDBCode(quickActionType - 1);
			break;

		}
		if (action != null)
			action.setActionDBCode(quickActionType - 1);
		if (action == null)
			Log.e("quickaction", "getQuickAction() action obj  is null");

		return action;
	}

	public static QuickActionBuilder getInstance(String userID) {

		if (instance == null) {
			instance = new QuickActionBuilder(new User(userID));

		}
		return instance;

	}

	private QuickActionBuilder(User UserID) {

		this.user = UserID;

	}

	public ArrayList<ContactLogicbean> getQuickActionList() {

		return quickActionList;
	}

	private void setQuickActionList(ArrayList<ContactLogicbean> list) {
		quickActionList = list;
	}

	public boolean isNoQuickAction() {
		return (quickActionList == null);
	}

	public void addQuickAction(ContactLogicbean bean) {
		if (quickActionList == null) {
			quickActionList = new ArrayList<ContactLogicbean>();

		}

		quickActionList.add(bean);

		if (quickActionMap.get(bean.getId()) == null) {
			QuickAction action = this.getQuickAction(bean);
			this.setQuickAction(bean.getId(), action);
		}

	}

	public void removeQuickAction(ContactLogicbean bean) {

	}

}
