package com.cg.quickaction;

import org.lib.model.ShareReminder;
import org.util.Utility;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

public class ShareQuickAction extends QuickAction {
	
	private String text = null;
	private String FTPPath = null;
	
	public String getFTPPath() {
		return FTPPath;
	}

	public void setFTPPath(String fTPPath) {
		FTPPath = fTPPath;
	}

	private ShareReminder getShareReminderObj(boolean isManual, String user){
		ShareReminder reminder = new ShareReminder();
		reminder.setFrom(CallDispatcher.LoginUser);
		Utility utility = new Utility();
		if (isManual){
			reminder.setReminderStatus("false");
			reminder.setReminderResponseType("");
			reminder.setRemindertz("");
			reminder.setReminderdatetime("");
		}
		else
		{
			//TODO: get Scheduler details
		}

		//public static int TEXTSHARE = 8;
//		public static int AUDIOSHARE = 9;
//		public static int VIDEOSHARE = 10;
//		public static int IMAGESHARE = 11;
//		public static int SKETCHSHARE = 12;

		switch (this.getActionSubType()){
		case 8:
			reminder.setType("notes");
			reminder.setComment(this.getText());
			break;
		case 9:
			reminder.setType("audio");
			reminder.setComment(this.getText());
			reminder.setFileLocation(this.getFTPPath());
			reminder.setReceiverStatus("manual");
			reminder.setIsbusyResponse("1");
			reminder.setFileid(Long.toString(utility.getRandomMediaID()));
			break;
		case 10:
			reminder.setType("video");
			reminder.setComment(this.getText());
			reminder.setFileLocation(this.getFTPPath());
			reminder.setReceiverStatus("manual");
			reminder.setIsbusyResponse("1");
			reminder.setFileid(Long.toString(utility.getRandomMediaID()));
			break;
		case 11:
			reminder.setType("photonotes");
			reminder.setComment(this.getText());
			reminder.setFileLocation(this.getFTPPath());
			reminder.setReceiverStatus("manual");
			reminder.setIsbusyResponse("1");
			reminder.setFileid(Long.toString(utility.getRandomMediaID()));
			break;
		case 12:
			reminder.setType("handsketch");
			reminder.setComment(this.getText());
			reminder.setFileLocation(this.getFTPPath());
			reminder.setReceiverStatus("manual");
			reminder.setIsbusyResponse("1");
			reminder.setFileid(Long.toString(utility.getRandomMediaID()));
			break;

		}


		return reminder;
	}

	public void runNow()
	{
		String[] buddiesList = super.getContactList();
		for (int i = 0; i < buddiesList.length; i++) {

		ShareReminder runNowObj = this.getShareReminderObj(true,  buddiesList[i]);

		}

	}

	public boolean saveAndShare(){
		String[] buddiesList = super.getContactList();
		ShareReminder reminder = new ShareReminder();
		reminder.setFrom(CallDispatcher.LoginUser);
		Utility utility = new Utility();
		String supertype = this.getSubTypeDesc();
		String type = QuickActionType.ACTIONTYPES[this.getActionType()];




		for (int i = 0; i < buddiesList.length; i++) {

			reminder.setTo(buddiesList[i]);
			if (supertype.equalsIgnoreCase("ST")) {
				reminder.setType("notes");
			} else if (type.equalsIgnoreCase("SP")) {
				reminder.setType("photonotes");
			} else if (type.equalsIgnoreCase("SA")) {
				reminder.setType("audio");
			} else if (type.equalsIgnoreCase("SV")) {
				reminder.setType("video");
			}
			else if (type.equalsIgnoreCase("SHS")) {
				reminder.setType("handsketch");
			}
			reminder.setReceiverStatus("manual");
			reminder.setFileLocation(this.getFTPPath());
			reminder.setIsbusyResponse("1");
			reminder.setRemindertz("");
			reminder.setReminderdatetime("");
			reminder.setFileid(Long.toString(utility
					.getRandomMediaID()));
			reminder.setReminderStatus("false");
			reminder.setReminderResponseType("");
			reminder.setComment(this.getText());
			WebServiceReferences.webServiceClient
			.ShareFiles(reminder);




		}
		return true;
	}

	public ShareQuickAction(){
		super();
		super.setActionType(3);
	}
	
	public String[] getSubTypes(){
		return QuickActionType.SHARESUBTYPESTEXT;
	}
	
	public  String getSubTypeDesc(){
		return QuickActionType.SHARESUBTYPESTEXT[super.getActionSubType()];
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}
