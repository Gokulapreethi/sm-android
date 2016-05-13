package com.cg.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.util.SingleInstance;

public class MissedDownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// TODO Auto-generated method stub
			if (CallDispatcher.LoginUser != null) {
				Log.i("missed123", "inside receiver");
				Log.i("missed123", "Millis : "  + System.currentTimeMillis());
				SingleInstance.mainContext
						.missedDownloads(CallDispatcher.LoginUser);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
