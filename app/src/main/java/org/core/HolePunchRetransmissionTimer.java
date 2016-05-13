package org.core;

import java.util.TimerTask;

import org.net.rtp.RtpEngine;


/**
 * 
 * This class retransmit the  message to  respective remote end point for every 2 seconds and maximum of 3 attempt.. 
 *
 */
public class HolePunchRetransmissionTimer extends TimerTask{

	private RtpEngine rtpEngine=null;
	private String ipaddress=null;
	private CallsOverInternet callsOverInternet=null;
	private byte[] buffer=null;
	private	int count=0;
	private long ssrc;
	
	


	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * Overloaded Class Constructor
	 * set required information for retransmission
	 * @param callsOverInternet reference to call session obj
	 * @param ssrc message ssrc 
	 * @param ipaddress target ipaddress
	 * @param buffer message
	 */
	public HolePunchRetransmissionTimer(CallsOverInternet callsOverInternet,long ssrc,String ipaddress,byte[] buffer) {
		this.callsOverInternet=callsOverInternet;
		this.ipaddress=ipaddress;
		this.buffer=buffer;
		this.ssrc=ssrc;
	}

	/**
	 * set rtpengine reference
	 * @param rtpEngine rtpengine reference
	 */
	public void setRtpEngine(RtpEngine rtpEngine) {
		this.rtpEngine = rtpEngine;
	}
	
	/**
	 * set maximum  attempt
	 * @param count maximim count value
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * Used to send Data Using RTPEngine for maximum count of 3. 
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(rtpEngine!=null){
			try {

				rtpEngine.sendData(ssrc,ipaddress,buffer);
				//System.out.println("Retransmitting packet................................."+rtpEngine.getRemoteAddress());
				if(count==3){
					this.cancel();
					//System.out.println("Retransmitting Count Limit Reached ................................."+rtpEngine.getRemoteAddress());
					callsOverInternet.stopTimer();
					callsOverInternet.gotPortIpAddressFromServer2(null);
				}
				count++;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		
	}
	
	
	

}
