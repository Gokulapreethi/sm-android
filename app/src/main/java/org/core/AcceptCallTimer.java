package org.core;

import java.util.TimerTask;

import org.lib.model.SignalingBean;

import android.util.Log;

public class AcceptCallTimer extends TimerTask{

	private SignalingBean sb=null;
	private ProprietarySignalling proprietarySignalling;
	private CallsOverInternet callObj=null;

	public void setProprietarySignalling(ProprietarySignalling proprietarySignalling) {
		this.proprietarySignalling = proprietarySignalling;
	}


	public void setSignalingBean(SignalingBean sb){
		this.sb=sb;
	}
	public void setCallsession(CallsOverInternet callObj)
	{
		Log.d("SIGNAL", "Initiated Acceptcall Timer");
		this.callObj=callObj;
	}
	
	/**
	 * Send bye signal to Request user and also to UI.
	 */
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.d("SIGNAL", "SENDING Type 3from AcceptcallTimer");
		sb.setType("3");
		proprietarySignalling.sendMessage(sb);
//		String from=sb.getFrom();
//		String to=sb.getTo();
//		sb.setFrom(to);
//		sb.setTo(from);
		//proprietarySignalling.dispatchCallEvents(sb);
		callObj.sendAcceptTimerBye();
		
		this.cancel();

	}

}
