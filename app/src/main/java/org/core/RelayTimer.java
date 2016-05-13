package org.core;

import java.util.TimerTask;

import org.net.rtp.RtpEngineRelay;

import android.util.Log;

/**
 * 
 * This class extends TimerTask. This RelayTimer retransmit the relay join message to server for every 2 seconds maximum 3 attempts 
 *
 */

public class RelayTimer extends TimerTask{

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
	public RelayTimer(RtpEngineRelay rtpEngine,String ip,int port,byte[] data) {
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

		if(count==3){
			this.cancel();
		}else{
			
			Log.d("SM","called from Timerrrrrrrrrr "+new String(data));
			Log.d("call"," ????????????????? Timer sending Message=== "+new String(data));
			if(data==null)
			{
				Log.d("SM","NULLLLLLLLLLLLLLLLLL");	
			}
			else {
				Log.d("SM","NOT NULLLLLLLLLLLLLLLLLL");	
			}
		//	Log.d("call","%%%%%%%%%%%%%%%%"+relayip+","+relayPort+"%%%%%%%%%%%%%%%%%%%%%%%%%%%"+new String(data));
//			rtpEngine.addRemoteEndpoint(12345,relayip,relayPort);
//			rtpEngine.sendData(12345,relayip, data);
//
//			rtpEngine.removeRemoteEndpoint(12345, relayip);
			rtpEngine.sendSpecialMessage(data, relayip, relayPort, 12345);
			
			count++;

		}
	}


}
