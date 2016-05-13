package org.core;

import java.util.TimerTask;

import org.net.rtp.RtpEngineRelay;

import android.util.Log;

/**
 * 
 * This class extends TimerTask. This RelayTimer retransmit the relay join message to server for every 2 seconds maximum 3 attempts 
 *
 */

public class RelayDummyPacketTimer extends TimerTask{

/**
 * Data to send.
 */
	private byte data[]=null;
	/**
	 * RTPEngine to send and Receive RTPPacket.
	 */
	private RtpEngineRelay rtpEngine=null;
	private int count=0;
	/**
	 * RelayIp to send RTPPacket.
	 */
	private String relayip;
	/**
	 * Relay Port to send RTPPacket.
	 */
	private int relayPort;

/**
 * Construct new RelayTimer.
 * @param rtpEngine RtpEngine
 * @param ip Destination Ip.
 * @param port Destination Port.
 */
	public RelayDummyPacketTimer(RtpEngineRelay rtpEngine,String ip,int port,byte[] data) {
		this.rtpEngine=rtpEngine; 
		this.relayip=ip;
		this.relayPort=port;
		this.data=data;
	}

/**
 * Set the Media data to send.
 * @param data Mediadata.
 */
//	public void setData(byte[] data){
//		this.data=data;
//	}


/**
 * Request the Buddy to Join on Relay server to transfer Media.
 */
	@Override
	public void run() {

//		if(count==3){
//			this.cancel();
//		}else{
			
			Log.d("SM","called from Timerrrrrrrrrr "+new String(data));
			Log.d("TEST"," ????????????????? Timer sending Message=== "+new String(data));
			if(data==null)
			{
				Log.d("SM","NULLLLLLLLLLLLLLLLLL");	
			}
			else {
				Log.d("SM","NOT NULLLLLLLLLLLLLLLLLL");	
			}
	
			rtpEngine.sendSpecialMessage(data, relayip, relayPort, 12345);
			
			count++;

		//}
	}


}
