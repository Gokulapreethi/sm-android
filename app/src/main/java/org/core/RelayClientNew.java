package org.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import org.net.rtp.MediaFrameListener;
import org.net.rtp.RtpEngineRelay;

import android.util.Log;

public class RelayClientNew implements MediaFrameListener {

	private RtpEngineRelay rtpEngineRelay = null;
	private String relayServerIp = null;
	private int relayPort;
	private boolean relayJoined = false;
	String buddy = null;
	String callType = null;
	HashMap<String, Timer> dummyPAcketTimersMap = new HashMap<String, Timer>();
	boolean isType6 = false;

	/**
	 * HashMap to store and Retrieve Ssrc.
	 */
	// private HashMap<Long,String> ssrcTable;
	private HashMap<Long, String> audiossrcTable;
	private HashMap<Long, String> videossrcTable;
	private HashMap<String, Timer> timerTable = new HashMap<String, Timer>();
	private HashMap<Long, String> ssrcTable = new HashMap<Long, String>();
	private CommunicationEngine commEngine = null;

	/**
	 * Store and Retrieve CallsoverInternet for a particular Buddy.
	 */
	private HashMap<String, CallsOverInternet> callSessionTable = null;

	/**
	 * Session Id of the session.
	 */
	private String sessionid;

	public RelayClientNew(String relayServerIp, int relayPort) {
		this.relayServerIp = relayServerIp;
		this.relayPort = relayPort;
		if (rtpEngineRelay == null) {
			try {

				// ssrcTable=new HashMap<Long,String>();
				audiossrcTable = new HashMap<Long, String>();
				videossrcTable = new HashMap<Long, String>();

				rtpEngineRelay = new RtpEngineRelay(0);
				rtpEngineRelay.registerFrameListener(this);
				rtpEngineRelay.addRemoteEndpoint(CallsOverInternet.audiossrc,
						relayServerIp, this.relayPort);
				rtpEngineRelay.addRemoteEndpoint(CallsOverInternet.videossrc,
						relayServerIp, this.relayPort);
				rtpEngineRelay.setSsrcTables(audiossrcTable, videossrcTable);
				rtpEngineRelay.setMyAudioSsrc(CallsOverInternet.audiossrc);
				rtpEngineRelay.setMyVideoSsrc(CallsOverInternet.videossrc);
				rtpEngineRelay.start();

				Log.d("call",new Date()+"created relayRtp");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setCommEngine(CommunicationEngine commEngine) {
		this.commEngine = commEngine;
		// rtpEngineRelay.setCommunicationEngine(commEngine);

	}

	public void setBuddyName(String buddyName) {
		rtpEngineRelay.setBuddy(buddyName);
	}

	// public void addReceiverDetails(Long ssrc,String key){
	// ssrcTable.put(ssrc,key);
	// }

	public void addAudioSsrc(Long ssrc, String key) {

		Log.d("call",new Date()+"ADD       audio ssrc " + ssrc);
		audiossrcTable.put(ssrc, key);
		ssrcTable.put(ssrc, key);
	}

	public void setCallType(String calltype) {
		this.callType = calltype;
	}

	public void addVideoSsrc(Long ssrc, String key) {
		Log.d("call",new Date()+"ADD       video ssrc " + ssrc);
		videossrcTable.put(ssrc, key);
		ssrcTable.put(ssrc, key);

		setVideoSsrc(ssrc);
	}

	public void addVideoSsrcAlone(Long ssrc, String key) {
		Log.d("call",new Date()+"ADD       video ssrc " + ssrc);
		videossrcTable.put(ssrc, key);
		ssrcTable.put(ssrc, key);
	}

	public void removeSSRC(Long audioSsrc, Long videoSsrc) {

		Log.d("call",new Date()+"REMOVE       video ssrc ");
		if (audiossrcTable.containsKey(audioSsrc)) {
			audiossrcTable.remove(audioSsrc);
			ssrcTable.remove(audioSsrc);
		}
		if (videossrcTable.containsKey(videoSsrc)) {
			videossrcTable.remove(videoSsrc);
			ssrcTable.remove(videoSsrc);
		}

	}

	public void setVideoSsrc(Long ssrc) {

		if (rtpEngineRelay.getBuddyVideoSsrc() == 0) {
			rtpEngineRelay.setBuddyVideoSsrc(ssrc);
		} else {

			Log.d("call",new Date()+"Reset Video SSRC " + ssrc);
			rtpEngineRelay.setBuddyVideoSsrc(ssrc);
			rtpEngineRelay.resetRetransmision(true);
			// s
		}
	}

	// public void setbuddyName(Long ssrc)
	// {
	// rtpEngineRelay.setBuddyVideoSsrc(ssrc) ;
	// }

	/**
	 * set a reference to callsesiontable
	 * 
	 * @param callSessionTable
	 */
	public void setCallSessionTable(
			HashMap<String, CallsOverInternet> callSessionTable) {
		this.callSessionTable = callSessionTable;
	}

	/**
	 * Current active sessionid
	 * 
	 * @param sessionid
	 *            current sessionid
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public RtpEngineRelay getRtpEngine() {
		return rtpEngineRelay;
	}

	/**
	 * Call this method to join relay when failed to receive or requested to
	 * send media through relay server.This method will compose a join message
	 * 
	 * @param sessionid
	 *            valid and current active session id
	 * @param loginuser
	 *            current logined user
	 * @param buddyInterested
	 *            target buddy
	 * @param signal
	 *            boolean value which determine whether relay join message
	 *            should request a media or just simply join.
	 */
	public void sendRelayJoinMessage(String sessionid, String loginuser,
			String buddyInterested) {
		isType6 = false;

		Log.d("call",new Date()+"@@@@@@@@@@@@@@@@" + "  sending Relay joined message  "
				+ "@@@@@@@@@@@@@@@@@");
		Log.d("SM", "calleeeeeeeeeeeeeeeeeeeeed");
		byte[] data = null;
		String msgid = null;
		if (buddy == null) {
			buddy = buddyInterested;
		}
		Log.d("SM", "sendRelayJoinMessage" + buddyInterested);
		Log.d("call",new Date()+"sendRelayJoinMessage " + relayJoined);
		if (relayJoined) {
			Log.d("SM", "if(relayJoined){ " + buddyInterested);

			// sendMediaRequestMessage(sessionid, loginuser, buddyInterested);
			sendRelayMediaReceiveMessage(sessionid, loginuser, buddyInterested);

		} else {

			msgid = "0000";

			data = (msgid + "," + sessionid + "," + loginuser + ","
					+ CallsOverInternet.audiossrc + ","
					+ CallsOverInternet.videossrc + "," + "60").getBytes();

			RelayTimer relayTimer = new RelayTimer(rtpEngineRelay,
					relayServerIp, relayPort, data);
			// relayTimer.setData(data);

			Log.d("MSG", "**************** sending Message=== "
					+ new String(data));
			Timer timer = new Timer();
			timer.schedule(relayTimer, 0, 2000);
			timerTable.put(msgid + sessionid, timer);
			Log.d("call",new Date()+"join" + new String(data));
			relayJoined = true;
		}

	}

	public void sendRelayJoinMessageOnType6(String sessionid, String loginuser,
			String buddyInterested) {
		// isType6=true;

		Log.d("call",new Date()+"@@@@@@@@@@@@@@@@"
				+ "  sending Relay joined messageOnType6  "
				+ "@@@@@@@@@@@@@@@@@");
		Log.d("SM", "calleeeeeeeeeeeeeeeeeeeeed");
		byte[] data = null;
		String msgid = null;

		if (buddy == null) {
			buddy = buddyInterested;
		}
		Log.d("call",new Date()+"sendRelayJoinMessage " + relayJoined);
		if (relayJoined) {

			// sendMediaRequestMessage(sessionid, loginuser, buddyInterested);
			// sendRelayAudioReceiveMessage(sessionid, loginuser,
			// buddyInterested);

		} else {

			msgid = "0000";

			data = (msgid + "," + sessionid + "," + loginuser + ","
					+ CallsOverInternet.audiossrc + ","
					+ CallsOverInternet.videossrc + "," + "60").getBytes();

			RelayTimer relayTimer = new RelayTimer(rtpEngineRelay,
					relayServerIp, relayPort, data);
			// relayTimer.setData(data);

			Timer timer = new Timer();
			timer.schedule(relayTimer, 0, 2000);
			timerTable.put(msgid + sessionid, timer);
			Log.d("call",new Date()+"join" + new String(data));
			Log.d("MSG", "**************** sending Message=== "
					+ new String(data));
			relayJoined = true;
			isType6 = true;
		}

	}

	public void sendMediaRequestMessage1(String sessionid, String loginuser,
			String buddyInterested) {

		Log.d("call",new Date()+"Media sess " + sessionid + " ," + loginuser + " , "
				+ buddyInterested);
		Log.d("call",new Date()+"call type" + callType);
		String audioRequestMembers = "";

		HashMap callTable = (HashMap) commEngine.getCallTable();

		Set set = callTable.keySet();

		Iterator<String> itr = set.iterator();

		while (itr.hasNext()) {

			Object obj = callTable.get(itr.next());

			if (obj instanceof CallsOverInternet) {
				CallsOverInternet callsOverInternet = (CallsOverInternet) obj;

				if (callsOverInternet.isJoinedRelay()) {

					audioRequestMembers = audioRequestMembers
							+ callsOverInternet.getBuddyName() + ",";

				}
			}
		}

		if (audioRequestMembers != null) {
			audioRequestMembers = audioRequestMembers.substring(0,
					audioRequestMembers.length() - 1);
			sendRelayAudioReceiveMessage(sessionid, loginuser,
					audioRequestMembers);

			if (audioRequestMembers.contains(buddyInterested)) {
				if (callType.equalsIgnoreCase("VC")
						|| callType.equalsIgnoreCase("VP")
						|| callType.equalsIgnoreCase("VBC")|| callType.equalsIgnoreCase("SS")) {
					sendRelayVideoReceiveMessage(sessionid, loginuser,
							buddyInterested);

				}
			}
		}

	}

	// public void sendVideoRequestMessage(String loginUser,String
	// sessionId,String buddy)
	// {
	// String msgId="";
	//
	// }

	public void sendVideoStopMessage(String loginUser, String sessionId,
			String buddy) {
		// if()
		// {

		String msgid = "0021";
		String msg = msgid + "," + sessionId + "," + loginUser + "," + buddy;
		Log.d("call",new Date()+" ((((((((((())))))))) VIDEO stop message" + msg);
		RelayTimer relayTimer = new RelayTimer(rtpEngineRelay, relayServerIp,
				relayPort, msg.getBytes());

		Timer timer = new Timer();
		timer.schedule(relayTimer, 0, 2000);
		timerTable.put(msgid + sessionid, timer);

	}

	public void sendRelayMediaReceiveMessage(String sessionid,
			String loginuser, String buddyInterested) {
		byte[] data = null;
		String msgid = null;

		Log.d("SM", "sendRelayMediaReceiveMessage " + buddyInterested);
		if (relayJoined) {

			msgid = "0030";
			data = (msgid + "," + sessionid + "," + loginuser + "," + buddyInterested)
					.getBytes();

			RelayTimer relayTimer = new RelayTimer(rtpEngineRelay,
					relayServerIp, relayPort, data);
			// relayTimer.setData(data);

			Timer timer = new Timer();
			timer.schedule(relayTimer, 0, 2000);
			timerTable.put(msgid + sessionid, timer);

		}

	}

	public void startDummyPacketTimer(String name, String sessionId,
			String buddy) {
		Log.d("TEST", "Came to startDummyPackettimer");
		Timer timer = new Timer();

		if (!dummyPAcketTimersMap.containsKey(buddy + sessionId))
			;
		{

			Log.d("TEST", "Came to startDummyPackettimer if");
			byte[] data = null;
			String msgid = "1111";
			data = (msgid + "," + sessionId + "," + name).getBytes();

			RelayDummyPacketTimer relayTimer = new RelayDummyPacketTimer(
					rtpEngineRelay, relayServerIp, relayPort, data);
			timer.schedule(relayTimer, 30000, 30000);

			dummyPAcketTimersMap.put(buddy + sessionId, timer);
		}
	}

	public void stopDummyPAcketTimer(String key) {

		Log.d("TEST", "Came to stop dummy packet timer ");
		if (dummyPAcketTimersMap.containsKey(key)) {
			Timer callTimer = dummyPAcketTimersMap.get(key);

			if (callTimer != null) {
				callTimer.cancel();
				Log.d("TIMERS", "CallTimer stop");

			}

			dummyPAcketTimersMap.remove(key);
		}

	}

	public void sendRelayAudioReceiveMessage(String sessionid,
			String loginuser, String buddyInterested) {
		byte[] data = null;
		String msgid = null;
		Log.d("call",new Date()+"join Audio message.........");
		Log.d("SM", "sendRelayJoinMessage Audio...........");
		if (relayJoined) {

			msgid = "0010";
			data = (msgid + "," + sessionid + "," + loginuser + "," + buddyInterested)
					.getBytes();

			Log.d("call",new Date()+"join Audio message" + new String(data));
			RelayTimer relayTimer = new RelayTimer(rtpEngineRelay,
					relayServerIp, relayPort, data);
			// relayTimer.setData(data);
			// Log.d("MSG","0010 sending Message=== "+msgid+sessionid);

			Timer timer = new Timer();
			timer.schedule(relayTimer, 0, 2000);
			timerTable.put(msgid + sessionid, timer);

		}

	}

	public void sendRelayVideoReceiveMessage(String sessionid,
			String loginuser, String buddyInterested) {
		byte[] data = null;
		String msgid = null;
		Log.d("call",new Date()+"join Audio message...........");
		if (relayJoined) {

			msgid = "0020";
			data = (msgid + "," + sessionid + "," + loginuser + "," + buddyInterested)
					.getBytes();

			Log.d("call",new Date()+"join video" + new String(data));
			RelayTimer relayTimer = new RelayTimer(rtpEngineRelay,
					relayServerIp, relayPort, data);

			// Log.d("MSG","0020 sending Message=== "+msgid+sessionid);

			Timer timer = new Timer();
			timer.schedule(relayTimer, 0, 2000);
			timerTable.put(msgid + sessionid, timer);

		}

	}

	void sendReceiveAndSignal(String[] str) {
		if (!isType6) {
			sendRelayJoinMessage(str[1], str[2], buddy);
		} else {
			sendRelayAudioReceiveMessage(str[1], str[2], buddy);
		}

	}

	public void sendSessionClosedMessage(String msg) {
		for (int i = 0; i < 2; i++) {
			byte[] data = msg.getBytes();
			rtpEngineRelay.addRemoteEndpoint(12345, relayServerIp, relayPort);
			rtpEngineRelay.sendData(12345, relayServerIp, data);

			rtpEngineRelay.removeRemoteEndpoint(12345, relayServerIp);
		}

	}

	@Override
	public void notifyMediaFrame(byte[] data, long sscr) {

		Log.d("call",new Date()+"notifyMediaFrame(byte[] data, long sscr) " + sscr);
		try {

			if (ssrcTable.containsKey(sscr)) {

				// Log.d("call","on relay new ssrc datas "+sscr
				// +" ,key---> "+ssrcTable.get(sscr));
				// System.out.println("Receiving through relay server...... SSCR"+sscr);
				CallsOverInternet overInternet = callSessionTable.get(ssrcTable
						.get(sscr));
				overInternet.notifyMediaFrame(data, sscr);
				// Log.d("call",new Date()+"Relay media Receive Notification if");
			} else if (data == null) {

			}

			else {
				// System.out.println("########### GOT RELAY RESPONSE #####");

				String str = new String(data);
				// Log.d("MSG","Receiving Message=== ");
				// Log.d("call",
				// "########## Relay media Receive Notification else ########################"+str);
				// Log.d("SM",
				// "########## Relay media Receive Notification else ########################"+str);
				// System.out.println("#### "+str);
				String[] result = null;
				if (str.contains("0000") || str.contains("0030")
						|| str.contains("0010") || str.contains("0020")
						&& str.contains(sessionid)) {
					result = str.split(",");
					if (result.length > 1) {
						Log.d("MSG", "key--------------> " + result[0]
								+ result[1]);
						Log.d("call",
								"Receiving Message===================== " + str);
						if (timerTable.containsKey(result[0] + result[1])) {
							Log.d("MSG", "contains key");
							Timer timer = timerTable.get(result[0] + result[1]);
							timer.cancel();
							timerTable.remove(result[0] + result[1]);

							if (str.contains("0000")) {

								Log.d("SM", "Received @$%$%#@#$%@$ MSG " + str);
								// Log.d("SM","Received @$%$%#@#$%@$ MSG "+isType6);

								// if(!isType6)
								// {
								//
								// sendReceiveAndSignal(result);
								//
								// }
								// else {
								sendReceiveAndSignal(result);
								// }
							}

						}
					}
				}
			}
		} catch (Exception e) {
			Log.d("call",new Date()+"Exception on Relay new " + e);
			e.printStackTrace();
		}

	}

	@Override
	public void notifyError(String error) {
		// TODO Auto-generated method stub

	}

	public void stop() {

		// boolean isType6=false;

		audiossrcTable = null;
		videossrcTable = null;
		timerTable = null;
		ssrcTable = null;

		callSessionTable = null;

		if (dummyPAcketTimersMap != null) {
			Set set = dummyPAcketTimersMap.keySet();

			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {

				String Key = itr.next();
				Object obj = dummyPAcketTimersMap.get(Key);

				if (obj instanceof Timer) {
					Timer callTimer = (Timer) obj;

					if (callTimer != null) {
						callTimer.cancel();
						Log.d("TIMERS", "CallTimer stop");
						// System.out.println("### CALL TIMER CANCELD FOR SIGNAL : "
						// + signalid);
					}

				}
				dummyPAcketTimersMap.remove(Key);
			}

		}

	}

	@Override
	public void notifyPacketRequestMessage(long arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		Log.d("REQUESTP", "Came to Notify Packet receive message"+commEngine);
		if (commEngine != null) {
			commEngine.sendType4(arg0, arg1, arg2);
		}
	}

}
