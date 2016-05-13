package org.core;

import java.util.TimerTask;
/**
 * This class extends TimerTask. This class is used to Join Relay if no media packet is Received even after 30 secounds.
 * 
 *
 */
public class MediaTimer extends TimerTask{

	public RelayInterface relayInterface=null;
	/**
	 * Used to Register the class for Notification.
	 * @param relayInterface implemented class
	 */
	public void setRelayInterface(RelayInterface relayInterface) {
		this.relayInterface = relayInterface;
	}
//Used to join Relay.
	/**
	 * Didn't receive media packets even after 30 seconds  then  Join on Relay. 
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("#####Didn't receive media packets even after 30 seconds MediaReceiveTimer Elapsed..");
		relayInterface.JoinRelay();
		this.cancel();
	}

}
