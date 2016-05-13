package com.cg.commonclass;

import org.util.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

import com.util.SingleInstance;

public class SipNotificationListener {

	private AlertDialog incomingCall_alertDialog = null;

	public boolean isrejected = false;

	private Handler handler = null;

	private CallDispatcher callDisp;

	private Utility utility;

	static Context context = null;

	public SipNotificationListener() {
		this.handler = new Handler();

		this.callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldispatch");

		this.utility = new Utility();

	}

	public void setcallBack() {
	}

	public static Context getCurrentContext() {
		Context context = null;

		if (WebServiceReferences.contextTable.containsKey("block_list")) {
			context = WebServiceReferences.contextTable.get("block_list");
		} else if (WebServiceReferences.contextTable
				.containsKey("utilitybuyer")) {
			context = WebServiceReferences.contextTable.get("utilitybuyer");
		} else if (WebServiceReferences.contextTable
				.containsKey("utilityseller")) {
			context = WebServiceReferences.contextTable.get("utilityseller");
		} else if (WebServiceReferences.contextTable
				.containsKey("utilityprovider")) {
			context = WebServiceReferences.contextTable.get("utilityprovider");
		} else if (WebServiceReferences.contextTable
				.containsKey("utilityneeder")) {
			context = WebServiceReferences.contextTable.get("utilityneeder");
		} 
//		else if (WebServiceReferences.contextTable.containsKey("utility")) {
//			context = WebServiceReferences.contextTable.get("utility");
//		} 
		else {
			context = SingleInstance.mainContext;
		}

		return context;
		// if (WebServiceReferences.contextTable.containsKey("buddyView1"))
		// context = WebServiceReferences.contextTable.get("buddyView1");
		// else if (WebServiceReferences.contextTable.containsKey("auto_play"))
		// {
		// context = WebServiceReferences.contextTable.get("auto_play");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("reminderactivity")) {
		// context = WebServiceReferences.contextTable.get("reminderactivity");
		// } else if (WebServiceReferences.contextTable.containsKey("imtabs")) {
		// if (WebServiceReferences.contextTable.containsKey("notepicker")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("Component")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("sendershare")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("sharebudies"))
		// context = WebServiceReferences.contextTable
		// .get("sharebudies");
		// else
		// context = WebServiceReferences.contextTable
		// .get("sendershare");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("Component");
		// }
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("pickerviewer"))
		// context = WebServiceReferences.contextTable
		// .get("pickerviewer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("notepicker");
		// }
		// } else {
		// if (WebServiceReferences.contextTable.containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else
		// context = WebServiceReferences.contextTable.get("imtabs");
		// }
		// } else if (WebServiceReferences.contextTable.containsKey("appsview"))
		// {
		// if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("frmreccreator")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("special"))
		// context = WebServiceReferences.contextTable
		// .get("special");
		// else
		// context = WebServiceReferences.contextTable
		// .get("frmreccreator");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("frmviewer");
		// } else
		// context = WebServiceReferences.contextTable.get("appsview");
		// } else if
		// (WebServiceReferences.contextTable.containsKey("Component")) {
		// if (WebServiceReferences.contextTable.containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("sendershare")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("sharebudies"))
		// context = WebServiceReferences.contextTable
		// .get("sharebudies");
		// else
		// context = WebServiceReferences.contextTable
		// .get("sendershare");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("Component");
		// }
		// } else if (WebServiceReferences.contextTable.containsKey(""))
		// {
		// if (WebServiceReferences.contextTable.containsKey("notepicker")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("Component")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("sendershare")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("sharebudies"))
		// context = WebServiceReferences.contextTable
		// .get("sharebudies");
		// else
		// context = WebServiceReferences.contextTable
		// .get("sendershare");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("Component");
		// }
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("pickerviewer"))
		// context = WebServiceReferences.contextTable
		// .get("pickerviewer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("notepicker");
		// }
		// } else if (WebServiceReferences.contextTable
		// .containsKey("QueryActivity")) {
		// if (WebServiceReferences.contextTable.containsKey("qaggregate"))
		// context = WebServiceReferences.contextTable
		// .get("qaggregate");
		// else
		// context = WebServiceReferences.contextTable
		// .get("QueryActivity");
		// } else
		// context = WebServiceReferences.contextTable.get("SecContatct");
		// } else if (WebServiceReferences.contextTable.containsKey("menupage"))
		// {
		// if (WebServiceReferences.contextTable.containsKey("BuddySettings")) {
		// context = WebServiceReferences.contextTable
		// .get("BuddySettings");
		// } else if (WebServiceReferences.contextTable.containsKey("locbusy"))
		// {
		// context = WebServiceReferences.contextTable.get("locbusy");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("ChangePassword")) {
		// context = WebServiceReferences.contextTable
		// .get("ChangePassword");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("IpSettings")) {
		// context = WebServiceReferences.contextTable.get("IpSettings");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("sharesettings"))
		// context = WebServiceReferences.contextTable
		// .get("sharesettings");
		// else if (WebServiceReferences.contextTable.containsKey("about"))
		// context = WebServiceReferences.contextTable.get("about");
		// else
		// context = WebServiceReferences.contextTable.get("menupage");
		//
		// } else if (WebServiceReferences.contextTable
		// .containsKey("formactivity")) {
		// if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("frmreccreator")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("special"))
		// context = WebServiceReferences.contextTable
		// .get("special");
		// else
		// context = WebServiceReferences.contextTable
		// .get("frmreccreator");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("frmviewer");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("frmcreator")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("cmpinstruction")) {
		//
		// if (WebServiceReferences.contextTable
		// .containsKey("textinputactivity"))
		// context = WebServiceReferences.contextTable
		// .get("textinputactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("formdec"))
		// context = WebServiceReferences.contextTable
		// .get("formdec");
		// else if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("cmpinstruction");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("frmcreator");
		// } else
		// context = WebServiceReferences.contextTable.get("formactivity");
		//
		// } else if (WebServiceReferences.contextTable
		// .containsKey("formsettings")) {
		// if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("frmreccreator")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("special"))
		// context = WebServiceReferences.contextTable
		// .get("special");
		// else if (WebServiceReferences.contextTable
		// .containsKey("formpermissionviewer"))
		// context = WebServiceReferences.contextTable
		// .get("formpermissionviewer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("frmreccreator");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("frmviewer");
		// } else
		// context = WebServiceReferences.contextTable.get("formsettings");
		// } else if
		// (WebServiceReferences.contextTable.containsKey("creategroup")) {
		// context = WebServiceReferences.contextTable.get("creategroup");
		// } else if
		// (WebServiceReferences.contextTable.containsKey("buddiesList")) {
		// if (WebServiceReferences.contextTable.containsKey("findpeoples"))
		// context = WebServiceReferences.contextTable.get("findpeoples");
		// else if (WebServiceReferences.contextTable
		// .containsKey("answeringmachine"))
		// context = WebServiceReferences.contextTable
		// .get("answeringmachine");
		// else if (WebServiceReferences.contextTable.containsKey("imtabs")) {
		// if (WebServiceReferences.contextTable.containsKey("notepicker")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("Component")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("sendershare")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("sharebudies"))
		// context = WebServiceReferences.contextTable
		// .get("sharebudies");
		// else
		// context = WebServiceReferences.contextTable
		// .get("sendershare");
		// } else
		// context = WebServiceReferences.contextTable
		// .get("Component");
		// }
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("pickerviewer"))
		// context = WebServiceReferences.contextTable
		// .get("pickerviewer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("notepicker");
		// }
		// } else {
		// if (WebServiceReferences.contextTable
		// .containsKey("handsketch")) {
		// if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else
		// context = WebServiceReferences.contextTable
		// .get("handsketch");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("zoomactivity"))
		// context = WebServiceReferences.contextTable
		// .get("zoomactivity");
		// else if (WebServiceReferences.contextTable
		// .containsKey("videoplayer"))
		// context = WebServiceReferences.contextTable
		// .get("videoplayer");
		// else
		// context = WebServiceReferences.contextTable
		// .get("imtabs");
		// }
		// } else
		// context = WebServiceReferences.contextTable.get("buddiesList");
		// } else if (WebServiceReferences.contextTable
		
		// else if
		// (WebServiceReferences.contextTable.containsKey("viewprofile"))
		// context = WebServiceReferences.contextTable.get("viewprofile");
		// else if (WebServiceReferences.contextTable.containsKey("utility")) {
		// if (WebServiceReferences.contextTable.containsKey("block_list"))
		// context = WebServiceReferences.contextTable.get("block_list");
		// else if (WebServiceReferences.contextTable
		// .containsKey("utilitybuyer"))
		// context = WebServiceReferences.contextTable.get("utilitybuyer");
		// else if (WebServiceReferences.contextTable
		// .containsKey("utilityseller"))
		// context = WebServiceReferences.contextTable
		// .get("utilityseller");
		// else if (WebServiceReferences.contextTable
		// .containsKey("utilityprovider"))
		// context = WebServiceReferences.contextTable
		// .get("utilityprovider");
		// else if (WebServiceReferences.contextTable
		// .containsKey("utilityneeder"))
		// context = WebServiceReferences.contextTable
		// .get("utilityneeder");
		// else
		// context = WebServiceReferences.contextTable.get("utility");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("sipmainactivity"))
		// context = WebServiceReferences.contextTable.get("sipmainactivity");
		// else if
		// (WebServiceReferences.contextTable.containsKey("userprofile")) {
		// context = WebServiceReferences.contextTable.get("userprofile");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("inviteprofile")) {
		// context = WebServiceReferences.contextTable.get("inviteprofile");
		// } else if (WebServiceReferences.contextTable
		// .containsKey("busyresponse")) {
		// context = WebServiceReferences.contextTable.get("busyresponse");
		// } else if (WebServiceReferences.contextTable.containsKey("clone"))
		// context = WebServiceReferences.contextTable.get("clone");
		// else {
		// context = WebServiceReferences.contextTable.get("MAIN");
		// }
		// return context;
		//
	}

	// private void ShowAlert(int callid, String remoteInfo, Context context) {
	// try {
	// Intent intent = new Intent(context,
	// SipIncomingCallAlertActivity.class);
	// intent.putExtra("remoteInfo", remoteInfo);
	// intent.putExtra("callid", callid);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	// context.startActivity(intent);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public void onMessageReceived(int call_id, String from, String to,
			String contact, String mime_type, String text) {
		// TODO Auto-generated method stub

	}

	public void on_referMessageReceived(int call_id, String remoteinfo) {
		// TODO Auto-generated method stub

	}

	public void on_call_media(int callid) {
		// TODO Auto-generated method stub

	}

	public void notifyMediaState(int call_id, String touser_no, boolean status) {
		// TODO Auto-generated method stub

	}

}
