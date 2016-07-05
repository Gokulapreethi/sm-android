package com.cg.quickaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.forms.Alert;
import com.util.SingleInstance;

public class QuickActionBroadCastReceiver extends BroadcastReceiver {

	private String ftpPath = "";
	private String fromUser = "";
	private String toUser = "";
	private String content = "";
	private String type = "";
	private String query = "";
	private int id;
	private CallDispatcher callDisp = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// TODO Auto-generated method stub
			Log.i("BroadCast","inside QuickActionBroadCastReceiver");
			String userName = intent.getExtras().getString("fromuser");
			if (CallDispatcher.LoginUser != null && userName != null
					&& CallDispatcher.LoginUser.equalsIgnoreCase(userName)) {
				Log.i("QAA", "===> " + intent.getExtras().getString("fromuser"));
				Log.i("QAA", "===> " + intent.getExtras().getString("ftppath"));
				Log.i("QAA", "===> " + intent.getExtras().getString("touser"));
				Log.i("QAA", "===> " + intent.getExtras().getString("content"));
				Log.i("QAA", "===> " + intent.getExtras().getString("type"));
				Log.i("QAA", "===> " + intent.getExtras().getString("query"));
				Log.i("QAA", "===> " + intent.getExtras().getInt("id", 0));
				ftpPath = intent.getExtras().getString("ftppath");
				fromUser = intent.getExtras().getString("fromuser");
				toUser = intent.getExtras().getString("touser");
				content = intent.getExtras().getString("content");
				type = intent.getExtras().getString("type");
				query = intent.getExtras().getString("query");
				id = intent.getExtras().getInt("id", 0);
				if (id > 0) {
					Alert.writeString(context, Alert.ftppath, ftpPath);
					Alert.writeString(context, Alert.fromuser, fromUser);
					Alert.writeString(context, Alert.touser, toUser);
					Alert.writeString(context, Alert.content, content);
					Alert.writeString(context, Alert.type, type);
					Alert.writeString(context, Alert.query, query);
					Alert.writeBoolean(context, Alert.satus, true);
					if (type.equalsIgnoreCase("AC")
							|| type.equalsIgnoreCase("VC")
							|| type.equalsIgnoreCase("ABC")
							|| type.equalsIgnoreCase("VBC")
							|| type.equalsIgnoreCase("ACF")
							|| type.equalsIgnoreCase("HC")) {
						if (!SingleInstance.instanceTable
								.containsKey("callscreen")
								&& !WebServiceReferences.contextTable
										.containsKey("alertscreen")
								&& !WebServiceReferences.contextTable
										.containsKey("sicallalert")
								&& !WebServiceReferences.contextTable
										.containsKey("sipcallscreen")) {
							if (WebServiceReferences.callDispatch
									.containsKey("calldisp"))
								callDisp = (CallDispatcher) WebServiceReferences.callDispatch
										.get("calldisp");
							else
								callDisp = new CallDispatcher(context);

							callDisp.doAction(content, fromUser, toUser,
									ftpPath, content, type, query);
						}
					} else {
						if (WebServiceReferences.callDispatch
								.containsKey("calldisp"))
							callDisp = (CallDispatcher) WebServiceReferences.callDispatch
									.get("calldisp");
						else
							callDisp = new CallDispatcher(context);

						callDisp.doAction(content, fromUser, toUser, ftpPath,
								content, type, query);

					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
