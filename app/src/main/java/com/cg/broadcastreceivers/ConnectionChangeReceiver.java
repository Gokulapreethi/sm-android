 package com.cg.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		try {
			
			CallDispatcher calldisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
			Log.d("BL", "Notify Connection Changed..." + calldisp);

			ConnectivityManager _connec = (ConnectivityManager) context
					.getApplicationContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			Log.d("LMCM", "Conection object is ----->" + _connec);
			if (_connec != null) {
				NetworkInfo wifi_info = _connec
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo mobile_info = _connec
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobile_info != null && wifi_info != null) {

					if ((_connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.getState() == NetworkInfo.State.CONNECTED)
							|| (_connec.getNetworkInfo(
									ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)) {

						if (calldisp != null) {
							calldisp.networkStateChanged(true);
						}
						Log.d("BL",
								"Device is connected to the network. Online mode is available.");
					} else if (_connec.getNetworkInfo(
							ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED
							|| _connec.getNetworkInfo(
									ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED) {
						if (calldisp != null) {
							calldisp.networkStateChanged(false);
						}
						Log.d("BL",
								"Device is NOT connected to the network. Offline mode.");
					}

				}
				if (mobile_info == null && wifi_info != null) {
					if (wifi_info.getState() == NetworkInfo.State.CONNECTED) {
						if (calldisp != null) {
							calldisp.networkStateChanged(true);
						}
					} else if (wifi_info.getState() == NetworkInfo.State.DISCONNECTED) {
						if (calldisp != null) {
							calldisp.networkStateChanged(false);
						}
					}
				} else if (wifi_info == null && mobile_info != null) {
					if (mobile_info.getState() == NetworkInfo.State.CONNECTED) {
						if (calldisp != null) {
							calldisp.networkStateChanged(true);
						}
					} else if (mobile_info.getState() == NetworkInfo.State.DISCONNECTED) {
						if (calldisp != null) {
							calldisp.networkStateChanged(false);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
