package org.core;

import java.util.HashMap;
import java.util.Timer;

import org.net.rtp.MediaFrameListener;
import org.net.rtp.RtpEngine;


/**
 * 
 * When ever P2P communication failed to achieve ,the last option to achieve media establishment is through this relay module only. 
 * In which after a series of signalling either party will  send or receive media through relay server.  
 *
 */
public class RelayClien implements MediaFrameListener {
/**
 * This RtpEngine class provides actual end to end network media transport. 
 * This class uses UDP and RTP protocol to deliver packets across internet.
 */
	private RtpEngine rtpEngine=null;
	/**
	 * Allow Media if allowMedia is true else False.
	 */
	private boolean allowMedia=false;
	/**
	 * Set Relay Joined state.
	 */
	private boolean relayJoined=false;
	/**
	 * Relay Server Ip Initially null.
	 */
	private String relayServerIp=null;
	/**
	 * Relay Server Port Initially 0.
	 */
	private int relayPort;
	/**
	 * HashMap to store and Retrieve Timers.
	 */
	private HashMap<String,Timer> timerTable=new HashMap<String,Timer>();
	/**
	 * HashMap to store and Retrieve Ssrc.
	 */
	private HashMap<Long,String> ssrcTable=new HashMap<Long,String>();
	/**
	 * Store and Retrieve CallsoverInternet for a particular Buddy.
	 */
	private HashMap<String,CallsOverInternet> callSessionTable=null;
	
	/**
	 * Session Id of the session.
	 */
	private String sessionid;
	
	/**
	 * Sending Audio Ssrc.
	 */
	private long sendAudiossrc;
	/**
	 * Sending Video Ssrc.
	 */
	private long sendVideossrc;
	
	
	/** check whether allow media or not**/
	public boolean isAllowMedia() {
		return allowMedia;
	}



	/** set allow media or not **/
	public void setAllowMedia(boolean allowVideo) {
		this.allowMedia = allowVideo;
	}


/**
 * Used to Initialize the Constructor. This can also used to Initialize RTPEngine.
 * @param relayServerIp Server Ip.
 * @param relayPort Server Port.
 */
	public RelayClien(String relayServerIp,int relayPort){
		this.relayServerIp=relayServerIp;
		this.relayPort=relayPort;
		

		if(rtpEngine==null){
			try{
				rtpEngine=new RtpEngine(0);
				rtpEngine.registerFrameListener(this);
				rtpEngine.addRemoteEndpoint(CallsOverInternet.audiossrc,relayServerIp, this.relayPort);
				rtpEngine.addRemoteEndpoint(CallsOverInternet.videossrc,relayServerIp, this.relayPort);
				rtpEngine.start();

			}catch(Exception e){
				e.printStackTrace();	
			}
		}	
	}


	/**
	 * Send Media Request for 3 times .
	 * @param data
	 */
	public void sendMediaRequestStop(byte[] data){
		
		for(int i=0;i<3;i++){
		rtpEngine.addRemoteEndpoint(12345,relayServerIp,relayPort);
		rtpEngine.sendData(12345,relayServerIp,data);
        rtpEngine.removeRemoteEndpoint(12345,relayServerIp);
		}
	}
	
//	/** not used **/
//	public void setSendAudiossrc(long sendAudiossrc) {
//		this.sendAudiossrc = sendAudiossrc;
//	}
//
//
//	/** not used **/
//	public void setSendVideossrc(long sendVideossrc) {
//		this.sendVideossrc = sendVideossrc;
//	}
//
//
//	/** not used **/
//	public long getSendAudiossrc() {
//		return sendAudiossrc;
//	}
//
//
//	/** not used **/
//	public long getSendVideossrc() {
//		return sendVideossrc;
//	}



	/**
	 * Receiver details such as ssrc and their respective call session obj
	 * @param ssrc stream ssrc
	 * @param key  call session object of respective stream
	 */
	public void addReceiverDetails(Long ssrc,String key){
		ssrcTable.put(ssrc,key);
	}


	/**
	 * set a reference to callsesiontable
	 * @param callSessionTable
	 */
	public void setCallSessionTable(
			HashMap<String, CallsOverInternet> callSessionTable) {
		this.callSessionTable = callSessionTable;
	}



	/**
	 * Current active sessionid
	 * @param sessionid  current sessionid
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}



	/** 
	 * Call this method to join relay when failed to receive or requested to send media through relay server.This method 
	 * will compose a join message
	 * @param sessionid valid and current active session id
	 * @param loginuser current logined user
	 * @param buddyInterested target buddy
	 * @param signal boolean  value which  determine whether relay join message should request a media or just simply  join. 
	 */
	public void sendRelayJoinMessage(String sessionid,String loginuser,String buddyInterested,boolean signal){
		byte[] data=null;
		String msgid=null;
		
		if(relayJoined){
			
			msgid="0002";
			//data=(msgid+","+sessionid+","+loginuser).getBytes();
			if(signal){
				data=(msgid+","+sessionid+","+loginuser+","+buddyInterested).getBytes();	
			}else{
				data=(msgid+","+sessionid+","+loginuser).getBytes();
			}
			
		}else{
			msgid="0000";
			
			
				data=(msgid+","+sessionid+","+loginuser+","+CallsOverInternet.audiossrc+"'"+CallsOverInternet.videossrc+","+"30").getBytes();	
			
			relayJoined=true;
		}

//		RelayTimer relayTimer=new RelayTimer(rtpEngine,relayServerIp,relayPort);
//		relayTimer.setData(data);
//
//		Timer timer=new Timer();
//		timer.schedule(relayTimer,0,2000);
//		timerTable.put(msgid+sessionid,timer);
		
		
	}




/**
 * Used to get the RTPEngine Reference.
 * @return RTPEngine
 */
	public RtpEngine getRtpEngine() {
		return rtpEngine;
	}





	@Override
	public void notifyError(String error) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when ever a full frame received. The full Frame is then decoded and send to UI for Visualization. used to notify the 
	 * mediaFrame to the UI.
	 */

	@Override
	public void notifyMediaFrame(byte[] data, long sscr) {
		// TODO Auto-generated method stub

		if(ssrcTable.containsKey(sscr)){
//			System.out.println("Receiving through relay server...... SSCR"+sscr);
			CallsOverInternet overInternet= callSessionTable.get(ssrcTable.get(sscr));
			overInternet.notifyMediaFrame(data, sscr);
		}else{
//			System.out.println("########### GOT RELAY RESPONSE #####");
			
			String str=new String(data);
//			System.out.println("#### "+str);
			String[] result=null;
			if(str.contains("0000")||str.contains("0002")&& str.contains(sessionid)){
				result=str.split(",");
				if(result.length>1){
					if(timerTable.containsKey(result[0]+result[1])){
						Timer timer=timerTable.get(result[0]+result[1]);
						timer.cancel();
						timerTable.remove(result[0]+result[1]);
					}
				}
			}
		}
	}



	@Override
	public void notifyPacketRequestMessage(long arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}


}
