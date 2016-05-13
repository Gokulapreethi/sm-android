package com.cg.quickaction;

public class BroadcastQuickAction extends QuickAction {

	public String[] getSubTypes(){
		return QuickActionType.BCASTSUBTYPESTEXT;
	}
	
	public  String getSubTypeDesc(){
		return QuickActionType.BCASTSUBTYPESTEXT[super.getActionSubType()];
	}
	
	public BroadcastQuickAction(){
		super();
		super.setActionType(2);
	}
}
