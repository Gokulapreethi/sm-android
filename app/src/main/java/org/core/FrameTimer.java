package org.core;

import java.util.TimerTask;

import android.util.Log;

/**
 * 
 * This class  checks and make sure receives  packet continuously. For every 10 seconds interval checks previous received packet time and current packet time for different values.  
 * If no packet movement exists event after 3 attempt then timer will send bye to UI and  also to target endpoint.     
 */
public class FrameTimer extends TimerTask {

	private CallsOverInternet callObj=null;
//private long packetTime;
private long previousPacketTime=0;
private int count=0;


/** 
 * Overloaded class constructor.Pass the call session obj instance. 	
 * @param callsOverInternet call session obj.
 */
public FrameTimer(CallsOverInternet callsOverInternet) {
	Log.d("FTR", "Created Frame Timer...");
		this.callObj=callsOverInternet;
	}




//	/** not used **/
//	public void setPacketTime(long packetTime) {
//		this.packetTime = packetTime;
//	}




	@Override
	public void run() {
		
		//System.out.println("####### FRAME TIMER ELAPSED :");
		
		
		if(previousPacketTime==callObj.getCurrentPacketTime()){
			//System.out.println("####### FRAME TIMER ELAPSED :"+count);
			count++;
			Log.d("FT", "####### 1 FRAME TIMER ELAPSED :"+count);
			callObj.sendMediaNotification();
			
		}else{
			previousPacketTime=callObj.getCurrentPacketTime();
			count=0;
			Log.d("FT", "####### 2 FRAME TIMER ELAPSED :"+count);
		}
		
		if(count==4){
			Log.d("h_bye", "in engine  3");
			Log.d("FT", "####### 3 FRAME TIMER ELAPSED :"+count);
			//System.out.println("###### FRAME TIMER COUNT "+count);
			callObj.sendBye();
			this.cancel();
		}

	}

}


