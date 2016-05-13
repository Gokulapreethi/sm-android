package org.core;

import java.util.TimerTask;

import org.lib.model.SignalingBean;

import android.util.Log;
/**
 * This class extends TimerTask. This class is used for set maximum time duration of outgoing call Request
 * Maximum 60secounds.
 * 
 *
 */
public class CallTimerTask extends TimerTask{

	private SignalingBean sb=null;
	private ProprietarySignalling proprietarySignalling;
	private boolean mbvalue=false;

	/**
	 * Set ProprietarySignalling to send message
	 * @param proprietarySignalling reference
	 */
	public void setProprietarySignalling(ProprietarySignalling proprietarySignalling) {
		this.proprietarySignalling = proprietarySignalling;
		Log.d("SIGNAL", "CallTimerTask");
	}

	public void setBool(boolean mbvalue){
		this.mbvalue=mbvalue;
	}

	/**
	 * used to set Signaling information.
	 * @param sb Signaling information.
	 */
	public void setSignalingBean(SignalingBean sb){
		this.sb=sb;
	}
	/**
	 * Send bye signal to Request user and also to UI.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.d("SIGNAL", "sending From callTimer task");
		if(!mbvalue){
			Log.d("SIGNAL", "sending From callTimer task on if");
			sb.setType("3");
			proprietarySignalling.sendMessage(sb);
			sb.setType("8");
			proprietarySignalling.dispatchCallEvents(sb);
		}
		else{
			Log.d("SIGNAL", "sending From callTimer task on else");
			sb.setType("3");
			proprietarySignalling.sendMessage(sb);
			proprietarySignalling.dispatchCallEvents(sb);
		}
		this.cancel();

	}

}
