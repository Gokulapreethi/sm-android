package com.cg.quickaction;

public class ReportQuickAction extends QuickAction {
	
	public String[] getSubTypes(){
		return QuickActionType.REPORTSUBTYPESTEXT;
	}
	
	public  String getSubTypeDesc(){
		return QuickActionType.REPORTSUBTYPESTEXT[super.getActionSubType()];
	}
	
	public ReportQuickAction(){
		super();
		super.setActionType(4);
	}

}
