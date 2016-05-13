package org.core;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Timer;

import org.lib.model.SignalingBean;
import org.lib.xml.XmlComposer;
import org.lib.xml.XmlParser;
import org.net.rtp.MediaFrameListener;
import org.net.rtp.RtpEngine;
import org.net.rtp.RtpPacket;
import org.net.udp.UDPDataListener;
import org.net.udp.UDPEngine;
import org.util.Utility;

import android.util.Log;

enum HOLE_PUNCHING_STATE1 {
	HOLE_PUNCHING_IDLE, HOLE_PUNCHING_SERVER1_WAIT_RESPONSE, HOLE_PUNCHING_SERVER2_WAIT_RESPONSE, HOLE_PUNCHING_PORT_SCANNING, HOLE_PUNCHING_SUCCESS, FETCHEXTERNALIP
};

public class CallSession implements MediaFrameListener, UDPDataListener {

	private SignalingBean sb = null;
	private RtpEngine rtpEngine = null;
	private UDPEngine udpEngine = null;
	private Utility utility = new Utility();
	private HOLE_PUNCHING_STATE1 holePunchState = HOLE_PUNCHING_STATE1.HOLE_PUNCHING_IDLE;
	private HolePunchRetransmissionTimer retransmission = null;
	private Timer timer = null;
	public HashMap<String, TimerBean> sessionIdMap = null;
	private Queue messageQueue = null;
	private XmlComposer xmlComposer = null;
	private XmlParser xmlParser = null;
	private String serverLocation = null;
	private int rtpEnginePort;
	private String receiverip = null;
	private long audiossrc;
	private long videossrc;
	private HashMap<String, BuddyDetails> sessionMap = new HashMap<String, BuddyDetails>();
	private CommunicationListener commListener = null;
	private MediaFrameListener frameListener;
	private String cbserver1 = null;
	private String cbserver2 = null;
	private String publicip = null;
	private String router = null;

	public String getCbserver1() {
		return cbserver1;
	}

	public void setCbserver1(String cbserver1) {
		this.cbserver1 = cbserver1;
	}

	public String getCbserver2() {
		return cbserver2;
	}

	public void setCbserver2(String cbserver2) {
		this.cbserver2 = cbserver2;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public BuddyDetails getBuddyMediaDetails(String buddy) {
		return sessionMap.get(buddy);
	}

	public CallSession() {

		messageQueue = new Queue();
		xmlComposer = new XmlComposer();
		xmlParser = new XmlParser();
		sessionIdMap = new HashMap<String, TimerBean>();

	}

	public long getAudiossrc() {
		return audiossrc;
	}

	public MediaFrameListener getFrameListener() {
		return frameListener;
	}

	public void setFrameListener(MediaFrameListener frameListener) {
		this.frameListener = frameListener;
	}

	public long getVideossrc() {
		return videossrc;
	}

	// public void registerSignalingAgent(SignalingBean sb){
	// try{
	//
	// String xml= xmlComposer.composeSignalXML(sb);
	// xml=xmlComposer.getMessagewithRoot(xml,"0","0");
	// udpEngine.sendUDP(xml.getBytes());
	// //System.out.println("*******************************************");
	// //System.out.println("### Register to SignalingAgent sent...."+xml);
	// //System.out.println("*******************************************");
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// }

	public CommunicationListener getCommListener() {
		return commListener;
	}

	public void stopRtpEngine() {
		if (rtpEngine != null) {
			rtpEngine.stopRTPEngine();
		}
	}

	public void stopUdpEngine() {
		if (udpEngine != null) {
			udpEngine.stopUDPListener();
		}
	}

	public void setCommListener(CommunicationListener commListener) {
		this.commListener = commListener;
	}

	public void initializeRTPEngine() {
		try {
			rtpEnginePort = utility.getPortNo();
			rtpEngine = new RtpEngine(rtpEnginePort);
			rtpEngine.registerFrameListener(this);
			rtpEngine.start();
			// System.out.println("### initializeRTPEngine");
			// getPublicipaddress();

			// System.out.println("### RTP Engine started.........");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getPublicipaddress() {
		try {

			// System.out.println("### get Public ip ####");
			// byte buffer[]=new byte[1024];
			byte buffer[] = "what is my public ip".getBytes();
			RtpPacket rtpPacket = new RtpPacket(buffer, buffer.length);
			rtpEngine.setRemoteAddress(InetAddress.getByName(router));
			rtpEngine.setRemotePort(5000);

			rtpEngine.addRemoteEndpoint(1234, (router), 6000);
			rtpEngine
					.sendData(1234, router, "what is my public ip?".getBytes());
			rtpEngine.send(rtpPacket);
			holePunchState = HOLE_PUNCHING_STATE1.FETCHEXTERNALIP;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeUDPEngine() {
		try {
			// int dynamicPort=utility.getPortNo();
			udpEngine = new UDPEngine(0);

			// System.out.println("Router :"+router);
			udpEngine.setRemoteAddress(InetAddress.getByName(router));
			udpEngine.setRemotePort(50000);
			udpEngine.setUdpListener(this);
			udpEngine.startUDPListener();
			// System.out.println("### UDP Engine started.........");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startCallSession(SignalingBean sbn) {
		try {
			this.sb = sbn;
			// sb.setPublicip(publicip);

			// System.out.println("Type :"+sb.getType()+" Result :"+sb.getResult());

			if (sb.getType().equals("0") && sb.getResult().equals("0")) {// Invite
																			// Request
				String xml = xmlComposer.composeInviteXML(sb);
				udpEngine.sendUDP(xml.getBytes());
				sendRequest(xml, 3, 3000, sb.getSessionid(), sb.getType());
				// System.out.println("### Message Sent...."+xml);
			} else if (sb.getType().equals("3")) {
				String xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "1", "3");
				udpEngine.sendUDP(xml.getBytes());
				sendRequest(xml, 3, 3000, sb.getSessionid(), sb.getType());
				// System.out.println("### Hangup Sent...."+xml);
			}

			else if ((sb.getType().equals("1") && sb.getResult().equals("0"))
					|| (sb.getType().equals("2") && sb.getResult().equals("0"))) {// Invite
																					// Response

				if (sb.getResult().equals("0")) {

					Log.d("signal12345", "while sending");
					receiverip = sb.getTolocalip() + "," + sb.getTopublicip();
					sb.setAudiossrc(Long.toString(utility.getRandomMediaID()));
					sb.setVideossrc(Long.toString(utility.getRandomMediaID()));

					audiossrc = Long.parseLong(sb.getAudiossrc());
					videossrc = Long.parseLong(sb.getVideossrc());

					if (sb.getType().equals("2")) {
						// System.out.println("********************* Adding remote endpoint......"+videossrc);

						// System.out.println("sessionMap :"+sessionMap+" From :"+sb.getFrom()+" To :"+sb.getTo());
						BuddyDetails buddy = sessionMap.get(sb.getTo());
						rtpEngine.addRemoteEndpoint(videossrc,
								buddy.getConnectip(), (buddy.getConnectport()));
						rtpEngine.addRemoteEndpoint(audiossrc,
								buddy.getConnectip(), (buddy.getConnectport()));
					}

					boolean chkNat = checkNat();
					// System.out.println("chkNat :"+chkNat);
					if (chkNat) {
						Log.d("signal12345", "while sending------ its NAT");
						// System.out.println("### NAT CALL");
						pingServer1(null, 0);
					} else {
						// System.out.println("### LOCAL CALL");

						Log.d("signal12345", "while sending------ its not NAT");
						gotNatDetailsFromServer2(sb.getLocalip(),
								Integer.toString(rtpEnginePort), "0", chkNat);
					}
				}
			} else if (sb.getResult().equals("1") && sb.getType().equals("1")) {
				String xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "1", "1");
				udpEngine.sendUDP(xml.getBytes());
				sendRequest(xml, 3, 3000, sb.getSessionid(), sb.getType());
				// System.out.println("Sending REJECT....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pingServer1(String serverAddress, int port) {
		try {

			// System.out.println("### Pinging Server1......");
			holePunchState = HOLE_PUNCHING_STATE1.HOLE_PUNCHING_SERVER1_WAIT_RESPONSE;
			rtpEngine.setRemoteAddress(InetAddress.getByName(cbserver1));
			rtpEngine.setRemotePort(5000);

			rtpEngine.addRemoteEndpoint(1235, (cbserver1), 5000);
			rtpEngine.sendData(1235, cbserver1,
					"get my nat port from server1".getBytes());

			timer = new Timer();
			// retransmission=new
			// HolePunchRetransmissionTimer(this,1235,cbserver1,"get my nat port from server1".getBytes());
			retransmission.setRtpEngine(rtpEngine);
			timer.schedule(retransmission, 2000, 2000);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void pingServer2(String data) {
		try {

			System.out.println("### Pinging Server2......" + cbserver2);
			holePunchState = HOLE_PUNCHING_STATE1.HOLE_PUNCHING_SERVER2_WAIT_RESPONSE;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(new byte[12]);
			out.write(data.getBytes());
			byte buffer[] = out.toByteArray();

			rtpEngine.setRemoteAddress(InetAddress.getByName(cbserver2));
			rtpEngine.setRemotePort(6000);

			rtpEngine.addRemoteEndpoint(1236, (cbserver2), 6000);
			rtpEngine.sendData(1236, cbserver2, buffer);

			timer = new Timer();
			// retransmission=new
			// HolePunchRetransmissionTimer(this,1236,cbserver2,buffer);
			retransmission.setRtpEngine(rtpEngine);
			timer.schedule(retransmission, 2000, 2000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void notifyError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMediaFrame(byte[] data, long sscr) {
		// System.out.println("########## ########## ########### ##########3 ###        #####>>>");
		// System.out.println("Call Session notify Media Frame....");
		Log.d("VDO", "Came to call session notify media frame");
		switch (holePunchState) {

		case HOLE_PUNCHING_SERVER1_WAIT_RESPONSE:
			// System.out.println(" ### Server1 Response :"+new
			// String(data).toString());
			if (timer != null) {
				timer.cancel();
				System.out.println("### Ping server1 Timer stopped");
			}
			pingServer2(new String(data).toString());

			break;
		case HOLE_PUNCHING_SERVER2_WAIT_RESPONSE:
			String response = new String(data).toString();
			// System.out.println("### Server2 Response :"+response);

			if (response != null && response.length() > 0
					&& response.indexOf("#") > 0) {
				if (timer != null) {
					timer.cancel();
					// System.out.println("### Ping server2 Timer stopped");
				}
				String result[] = response.split("#");
				holePunchState = HOLE_PUNCHING_STATE1.HOLE_PUNCHING_IDLE;
				gotNatDetailsFromServer2(result[0], result[1], (result[2]),
						true);
			}

			break;
		case FETCHEXTERNALIP:
			// System.out.println("#### PUBLIC IP   :"+new String(data));
			holePunchState = HOLE_PUNCHING_STATE1.HOLE_PUNCHING_IDLE;
			String result = new String(data);
			if (result.contains(":")) {
				publicip = result.split(":")[0];
			}
			rtpEngine.removeRemoteEndpoint(1234, router);

			break;

		default:
			// System.out.println("frameListener.notifyMediaFrame(data, sscr) frameListener :"+frameListener);
			Log.d("VDO", "Default case..");
			if (frameListener != null) {
				frameListener.notifyMediaFrame(data, sscr);
			}

			break;
		}

	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	public void gotNatDetailsFromServer2(String ipaddress, String port,
			String punchingMode, boolean chkNat) {
		try {
			sb.setPort(port);
			sb.setBuddyConnectip(ipaddress);
			sb.setBuddyConnectport(port);
			sb.setPunchingmode(punchingMode);

			String xml = xmlComposer.composeSignalXML(sb);
			// System.out.println("Type :"+sb.getType());
			if (sb.getType().equals("1")) {// sending response
				xml = xmlComposer.getMessagewithRoot(xml, "0", "1");
			}
			if (sb.getType().equals("2")) {// sending reply msg to response
				xml = xmlComposer.getMessagewithRoot(xml, "0", "2");
			}

			System.out.println(udpEngine.getRemotePort() + "  "
					+ udpEngine.getRemoteAddress());
			System.out.println("************************************");
			System.out.println("Send XML :" + xml);
			udpEngine.sendUDP(xml.getBytes());
			System.out.println("************************************");

			sendRequest(xml, 3, 3000, sb.getSessionid(), sb.getType());

			/*
			 * sessionMap.put(sb.getTo()+sb.getAudiossrc(),receiverip);
			 * sessionMap.put(sb.getTo()+sb.getVideossrc(),receiverip);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendRequest(String xml, int count, int interval,
			String sessionId, String type) {

		TimerBean timerBean = null;
		if (sessionIdMap.containsKey(sessionId)) {
			timerBean = sessionIdMap.get(sessionId);
		} else {
			timerBean = new TimerBean();
			sessionIdMap.put(sessionId, timerBean);
		}

		if (type.equals("0")) {
			timerBean.setCallStatus(CallStatus.CALL_ACCEPT);
		}

		Timer timer = new Timer();
		RetranmisionTimer timerTask = new RetranmisionTimer(xml, count,
				udpEngine, sessionId, sessionIdMap);
		timer.schedule(timerTask, interval, interval);
		timerBean.setTimer(timer);
		sessionIdMap.put(sessionId, timerBean);

	}

	public void send(String buddy, String ssrc, byte data[]) {

		// System.out.println("buddy+ssrc :"+buddy+ssrc);
		// System.out.println("sessionMap :"+sessionMap);
		BuddyDetails details = sessionMap.get(buddy);
		if (details != null) {
			String ip = details.getConnectip();
			rtpEngine.sendData(Long.parseLong(ssrc), ip, data);
		}
	}

	public boolean checkNat() {

		boolean chknat = false;
		/*
		 * System.out.println("sb.getPublicip() :"+sb.getPublicip());
		 * System.out.println("sb.getLocalip() :"+sb.getLocalip());
		 * System.out.println("serverLocation :"+serverLocation);
		 * System.out.println("sb.getTopublicip() :"+sb.getTopublicip());
		 */

		if (sb.getPublicip().equals(sb.getLocalip())) {
			serverLocation = "local";
		} else {
			serverLocation = "public";
		}
		// System.out.println("serverLocation   ## :"+serverLocation);

		if (serverLocation.equals("local")) {
			chknat = false;
		} else if (serverLocation.equals("public")) {

			if (sb.getPublicip().equals(sb.getTopublicip())) {
				chknat = false;
			} else {
				chknat = true;
			}

		}

		return chknat;

	}

	public String createResponse() {
		//
		return null;
	}

	private CallStatus checkTypeOfCall(String type) {

		CallStatus callStatus = null;
		if (type.equals("0")) {
			callStatus = CallStatus.CALL_INVITE;
		} else if (type.equals("1")) {
			callStatus = CallStatus.CALL_ACCEPT;
		} else if (type.equals("2")) {
			callStatus = CallStatus.CALL_RESPONSE;
		} else if (type.equals("3")) {
			callStatus = CallStatus.CALL_BYE;
		} else if (type.equals("4")) {
			callStatus = CallStatus.CALL_END;
		}
		return callStatus;
	}

	private void changeCallStatus(TimerBean timerBean, String type) {
		if (type.equals("0")) {
			timerBean.setCallStatus(CallStatus.CALL_RESPONSE);
		} else if (type.equals("1")) {
			timerBean.setCallStatus(CallStatus.CALL_BYE);
		} else if (type.equals("2")) {
			timerBean.setCallStatus(CallStatus.CALL_BYE);
		} else if (type.equals("3")) {
			timerBean.setCallStatus(CallStatus.CALL_END);
		} else if (type.equals("4")) {
			timerBean.setCallStatus(CallStatus.CALL_ClOSED);
		}
	}

	@Override
	public void notifyUDPPackets(byte[] data, String sourceIp, int sourcePort) {
		// TODO Auto-generated method stub
		// System.out.println("\n****************************************");
		// System.out.println("notifyUDP Data :\n"+new String(data));
		// System.out.println("\n****************************************");

		SignalingBean sb = xmlParser.parseSignal(new String(data));
		boolean resultValue = true;
		if (sessionIdMap.containsKey(sb.getSessionid())) {
			TimerBean timerBean = sessionIdMap.get(sb.getSessionid());

			if (timerBean.getCallStatus().equals(checkTypeOfCall(sb.getType()))
					|| checkTypeOfCall(sb.getType())
							.equals(CallStatus.CALL_BYE)
					|| checkTypeOfCall(sb.getType())
							.equals(CallStatus.CALL_END)) {

				if (timerBean.getTimer() != null) {
					timerBean.getTimer().cancel();
				}
				changeCallStatus(timerBean, sb.getType());

			} else {
				resultValue = false;
			}

		} else {

			TimerBean timerBean = new TimerBean();
			timerBean.setCallStatus(CallStatus.CALL_RESPONSE);
			sessionIdMap.put(sb.getSessionid(), timerBean);

		}

		if (resultValue) {

			// System.out.println("Type :"+sb.getType()+" Result :"+sb.getResult());
			if (sb.getType().equals("0") && sb.getResult().equals("0")) {// Invite
																			// Request

				// System.out.println("commListener :"+commListener);
				if (commListener != null) {
					commListener.notifyInvite(sb);
				}

			} else if (sb.getType().equals("1") && sb.getResult().equals("0")) {
				// System.out.println("notify Accept.....");
				// rtpEngine.addRemoteEndpoint(audiossrc,sb.getBuddyConnectip(),Integer.parseInt(sb.getBuddyConnectport()));

				if (sb.getCallType().equals("VC")) {
					// System.out.println("********************* Adding remote endpoint......"+videossrc);
					// rtpEngine.addRemoteEndpoint(videossrc,sb.getBuddyConnectip(),Integer.parseInt(sb.getBuddyConnectport()));
				}

				BuddyDetails buddyDetails = new BuddyDetails();
				buddyDetails.setAudiomediaid(Long.parseLong(sb.getAudiossrc()));
				buddyDetails.setVideomediaid(Long.parseLong(sb.getVideossrc()));
				buddyDetails.setConnectip(sb.getBuddyConnectip());
				buddyDetails.setConnectport(Integer.parseInt(sb
						.getBuddyConnectport()));

				sessionMap.put(sb.getFrom(), buddyDetails);
				if (commListener != null) {
					commListener.notifyAccept(sb);

				}
			} else if (sb.getType().equals("1") && sb.getResult().equals("1")) {

				if (commListener != null) {
					commListener.notifyReject(sb);

				}
			} else if (sb.getType().equals("3")) {

				// System.out.println("commListener :"+commListener);
				if (commListener != null) {
					commListener.notifyBye(sb);

				}
			}

			else if (sb.getType().equals("2") && sb.getResult().equals("0")) {
				// System.out.println("notify AcceptResponse.....");

				rtpEngine.addRemoteEndpoint(audiossrc, sb.getBuddyConnectip(),
						Integer.parseInt(sb.getBuddyConnectport()));
				if (sb.getCallType().equals("VC")) {
					rtpEngine.addRemoteEndpoint(videossrc,
							sb.getBuddyConnectip(),
							Integer.parseInt(sb.getBuddyConnectport()));
				}

				// System.out.println("[1] "+sb.getBuddyConnectip()+" [2] "+sb.getBuddyConnectport());
				BuddyDetails buddyDetails = new BuddyDetails();
				buddyDetails.setAudiomediaid(Long.parseLong(sb.getAudiossrc()));
				buddyDetails.setVideomediaid(Long.parseLong(sb.getVideossrc()));
				buddyDetails.setConnectip(sb.getBuddyConnectip());
				buddyDetails.setConnectport(Integer.parseInt(sb
						.getBuddyConnectport()));

				sessionMap.put(sb.getFrom(), buddyDetails);
				if (commListener != null) {
					commListener.notifyAcceptResponse(sb);
				}

			}

		}

	}

	@Override
	public void notifyPacketRequestMessage(long arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}
}
