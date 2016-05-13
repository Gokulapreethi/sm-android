package org.core;

import java.util.TimerTask;

import android.util.Log;

/**
 * 
 * This class  checks and make sure receives  packet continuously. For every 10 seconds interval checks previous received packet time and current packet time for different values.  
 * If no packet movement exists event after 3 attempt then timer will send bye to UI and  also to target endpoint.     
 */
public class RelaySwitchTimer extends TimerTask {

	private CallsOverInternet callObj=null;



/** 
 * Overloaded class constructor.Pass the call session obj instance. 	
 * @param callsOverInternet call session obj.
 */
public RelaySwitchTimer(CallsOverInternet callsOverInternet) {
	Log.d("FTR", "Created Frame Timer...");
		this.callObj=callsOverInternet;
	}

	@Override
	public void run() {
		
		//System.out.println("####### FRAME TIMER ELAPSED :");
		Log.d("FTR", "public void run() {...");
		
			callObj.relayRequestOnMediaFailure();
			this.cancel();
		
	}

}


