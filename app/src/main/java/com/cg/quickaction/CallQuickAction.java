package com.cg.quickaction;


public class CallQuickAction extends QuickAction {
	
	public CallQuickAction(){
		super();
		super.setActionType(1);
	}

	public String[] getSubTypes() {
		return QuickActionType.CALLSUBTYPESTEXT;
	}
	
	public  String getSubTypeDesc(){
		return QuickActionType.CALLSUBTYPESTEXT[super.getActionSubType()];
	}

	public void addContacts(Buddy contact) {

		/*
		 * public static int AUDIOCALL = 1; public static int VIDEOCALL = 2;
		 * public static int AUDIOCONF = 3; public static int VIDEOCONF = 4;
		 * public static int HOSTEDCONF = 5;
		 */

		switch (this.getActionSubType()) {

		case QuickActionType.AUDIOCALL:
			this.removeAllContacts();
			this.addContact(contact);
			break;
		case QuickActionType.VIDEOCALL:
			this.removeAllContacts();
			this.addContact(contact);
			break;
			
		case QuickActionType.AUDIOCONF:
			this.addContact(contact);
			break;
		
		case QuickActionType.VIDEOCONF:
			this.addContact(contact);
			break;
		
		case QuickActionType.HOSTEDCONF:
			this.addContact(contact);
			break;
		
		}

	}
	
	public void addContacts(String contact) {
		this.addContact(new Buddy(contact));
	}
	
	

}
