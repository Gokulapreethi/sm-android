package org.core;

import java.util.TimerTask;

import org.lib.model.SignalingBean;

import android.util.Log;

public class CallRingTimer extends TimerTask{

	private SignalingBean sb=null;
	private ProprietarySignalling proprietarySignalling;
	
	/**
	 * Set ProprietarySignalling to send message
	 * @param proprietarySignalling reference
	 */
	public void setProprietarySignalling(ProprietarySignalling proprietarySignalling) {
		this.proprietarySignalling = proprietarySignalling;
		Log.d("SIGNAL", "CallRingTimer");
	}
	
	/**
	 * used to set Signaling information.
	 * @param sb Signaling information.
	 */
	public void setSignalingBean(SignalingBean sb){
		this.sb=sb;
	}
	
	
	public SignalingBean getSignalingBean(){
		return this.sb;
	}
	/**
	 * Send bye signal to Request user and also to UI.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		sb.setType("3");
		proprietarySignalling.sendMessage(sb);
		Log.d("SIGNAL", "sending from CallRingTimer");
		//Type 10 Unable to Reach Buddy.
		sb.setType("10");
		proprietarySignalling.dispatchCallEvents(sb);
		this.cancel();
		
	}

}
