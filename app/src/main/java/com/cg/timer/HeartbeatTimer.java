package com.cg.timer;

import java.util.TimerTask;

import org.core.ProprietarySignalling;
import org.lib.model.KeepAliveBean;

import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

public class HeartbeatTimer extends TimerTask {
	CallDispatcher callDisp = null;

	public void setProprietarySignalling(
			ProprietarySignalling proprietarySignalling) {
		Log.d("HB", "Timer started");

	}

	/**
	 * used to set Signaling information.
	 * 
	 * @param sb
	 *            Signaling information.
	 */
	public void setReference(CallDispatcher calldisp) {
		Log.d("HB", "Timer started");
		this.callDisp = calldisp;
	}

	/**
	 * Send bye signal to Request user and also to UI.
	 */
	@Override
	public void run() {

		if (WebServiceReferences.running) {
			try {

				KeepAliveBean aliveBean = callDisp.getKeepAliveBean();

				WebServiceReferences.webServiceClient.heartBeat(aliveBean);
				Log.d("HB", "Call KeepAliveBean From HeartBeatTimer...");

			} catch (Exception e) {
				Log.d("HB", "Thread Excep");

				Log.e("call", "Thread InterruptedException");
				e.printStackTrace();
			}
		}

		else {
			Log.d("HB", "cancel processingccc");
			this.cancel();

		}

	}

}
