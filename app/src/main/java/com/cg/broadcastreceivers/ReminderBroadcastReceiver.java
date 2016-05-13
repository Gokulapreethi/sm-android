package com.cg.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cg.account.SplashScreen;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

/**
 * When any telephony calls received by the device ReminderBroadcastReceiver
 * triggered to notify the event to the ui.After Receiving the notification if
 * any call from our application is on progress we won't send the call data to
 * the receiver and we won't play the data's send by our buddies.When telephony
 * call is disconnected we will start the send and receive the call data to the
 * user
 * 
 * 
 */
public class ReminderBroadcastReceiver extends BroadcastReceiver {
	PhoneStateChanged phonestatechanged = null;

	/**
	 * This method is called when the BroadcastReceiver is receiving an Intent
	 * broadcast (Telephony call received time).
	 */
	@Override
	public void onReceive(Context context, Intent arg1) {
		String action = arg1.getAction();
		try {
			if (action != null) {
				if (action
						.equalsIgnoreCase("android.intent.action.PHONE_STATE")
						&& action != null) {
					if (arg1.getStringExtra(TelephonyManager.EXTRA_STATE)
							.equals(TelephonyManager.EXTRA_STATE_RINGING)
							&& arg1 != null) {
					} else if (arg1
							.getStringExtra(TelephonyManager.EXTRA_STATE)
							.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)
							&& arg1 != null) {

						if (WebServiceReferences.callDispatch
								.containsKey("calldisp")) {
							CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
									.get("calldisp");
							callDisp.notifyGSMCallAcceted();
						}

					} else if (arg1
							.getStringExtra(TelephonyManager.EXTRA_STATE)
							.equals(TelephonyManager.EXTRA_STATE_IDLE)
							&& arg1 != null) {
						if (WebServiceReferences.callDispatch
								.containsKey("calldispatch")) {
							CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
									.get("calldispatch");
							callDisp.GSMCallisAccepted = false;
						}
					}
				}

				else {
					Log.i("startup123", "after switching on");
					// Intent start = new Intent(context, SplashScreen.class);
					// start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// context.startActivity(start);

					/**
					 * Commented, To make app not to start after device
					 * has been switched ON
					 */
					// Intent start = new Intent(context, LoginService.class);
					// context.startService(start);
					/**
					 * Ends
					 */

					// SharedPreferences preferences = PreferenceManager
					// .getDefaultSharedPreferences(context);
					// boolean autologinstate = preferences.getBoolean(
					// "autologin", false);
					// boolean rememberPass = preferences.getBoolean("remember",
					// false);
					// final String username = preferences
					// .getString("uname", null);
					// String password = preferences.getString("pword", null);
					// LoginPageFragment loginPageFragment = LoginPageFragment
					// .newInstance(context);
					// if (autologinstate || rememberPass) {
					// loginPageFragment.setSilentLogin(true);
					// loginPageFragment.silentLogin(username, password);
					// }
					
					
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * Initialize the PhoneStateChanged Interface to notify the phone state to
	 * the ui
	 *
	 * @param phonestatechanged
	 */
	public void setIncomingCallBack(PhoneStateChanged phonestatechanged) {
		this.phonestatechanged = phonestatechanged;

	}
}
