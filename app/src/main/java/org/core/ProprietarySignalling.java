package org.core;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.lib.model.SignalingBean;
import org.lib.model.UdpMessageBean;
import org.lib.xml.XmlComposer;
import org.lib.xml.XmlParser;
import org.net.rtp.RtpEngine;
import org.net.rtp.RtpPacket;
import org.net.udp.UDPDataListener;
import org.net.udp.UDPEngine;
import org.tcp.TCPEngine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.bean.GroupChatBean;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.callservices.VideoThreadBean;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.timer.KeepAliveReceiver;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.util.SingleInstance;

/**
 * This class is used to send and Receive UDP signal. This class implement
 * UDPDataListener which is used to receive UDP Signal.
 * 
 * 
 */
public class ProprietarySignalling implements UDPDataListener {

	/**
	 * This class is used to send and Receive DatagramPacket using Using
	 * UDPServer(DatagramSocket). Initially this can be null.
	 */
	private HashMap<String, UdpMessageBean> udpMap = new HashMap<String, UdpMessageBean>();

	private UDPEngine udpEngine = null;

	Timer portRefereshTimer = null;
	PortRefresher portRefereshreceiver = null;

	private static int PORT_REFERSH_INTERVAL = 15000;
	Timer mTimer = null;
	/**
	 * 
	 */

	public static String seceretKey = "comcryptkey00000";
	/**
	 * Used to save the Timerbean related to SignalId.
	 */
	public static HashMap messageSessionTable = new HashMap();
	/**
	 * Used to compose the XML for sending Signal.
	 */
	private XmlComposer xmlComposer = new XmlComposer();
	/**
	 * Retransmission count of the Signal. Initially Zero.
	 */
	private int retransmissionCount = 0;
	/**
	 * Retransmission Time Interval .
	 */
	private int retransmissionInterval = 0;
	/**
	 * Used to save the Timerbean related to SessionId.
	 */
	public static HashMap<String, TimerBean> sessionIdMap = new HashMap<String, TimerBean>();
	/** < Used to maintain sessionId */
	/**
	 * Used to Parse the Received XML Signal.
	 */
	private XmlParser xmlParser = new XmlParser();
	private CallSessionListener callSessionListener = null;
	private int mInterval = 15000;
	/**
	 * Communication Engine is the core class which provides various
	 * functionality to establish,make and control session. This class
	 * initialize the video and audio codec's,start audio recording,received
	 * video and audio callback. Initially this can be null.
	 */
	private CommunicationEngine communicationEngine = null;
	private HashMap callTable = null;
	/**
	 * Used to maintain the Timer in accordance with the SignalId.
	 */
	public HashMap<String, Timer> callTimer = new HashMap<String, Timer>();
	/**
	 * Router Ip address.
	 */
	private String routerip = null;
	/**
	 * Router Port Number.
	 */
	private int routeport;
	private int listnerPort;
	private Thread thread = null;
	private String xml = null;
	private boolean running = true;
	private String loginUser = null;
	int portno = 0;

	/**
	 * used to Initialize ProprietarySignalling.
	 * 
	 * @param retxCount
	 *            retransmission count
	 * @param timerInterval
	 *            timer interval
	 * @param callSessionListener
	 *            callsessionListener reference.
	 * @param commEngine
	 *            Communication Engine Reference.
	 * @param uname
	 *            userName.
	 */
	public void initialize(int retxCount, int timerInterval,
			CallSessionListener callSessionListener,
			CommunicationEngine commEngine, String uname) {
		this.loginUser = uname;
		this.callSessionListener = callSessionListener;
		this.communicationEngine = commEngine;
	}

	public CallSessionListener getCallSessionListener() {
		return callSessionListener;
	}

	/**
	 * used to register the class for Notify data.
	 * 
	 * @param callSessionListener
	 *            implemented class
	 */
	public void setCallSessionListener(CallSessionListener callSessionListener) {
		this.callSessionListener = callSessionListener;
	}

	/**
	 * Used to start CallTimer for Outgoing call request.maximum 60 secound's.
	 * We can also put the Timer on CallTimer hashmap in accordance with the
	 * Signal Id.
	 * 
	 * @param sb
	 *            Signaling information.
	 */
	public void startCallTimer(SignalingBean sb, boolean value) {
		Timer timer = new Timer();
		CallTimerTask callTimerTask = new CallTimerTask();
		callTimerTask.setBool(value);
		callTimerTask.setProprietarySignalling(this);
		callTimerTask.setSignalingBean(sb);
		// timer.schedule(callTimerTask, 60000, 60000);
		timer.schedule(callTimerTask, 30000, 30000);
		Log.d("TIMERS", "Call Timer started 30secs " + sb.getSignalid());
		callTimer.put(sb.getSignalid(), timer);
		Log.d("TIMERS", "callTimer " + callTimer);
		Log.d("TIMERXX",
				"start callTimer Timer started 15secs ******* "
						+ sb.getSignalid());
	}

	public void startRingTimer(SignalingBean sb) {
		Timer ringerTimer = new Timer();
		CallRingTimer callRingTimer = new CallRingTimer();
		callRingTimer.setProprietarySignalling(this);
		callRingTimer.setSignalingBean(sb);
		ringerTimer.schedule(callRingTimer, 15000);
		callTimer.put(sb.getSignalid(), ringerTimer);
		Log.d("TIMERS", "Ringer Timer started 15secs");
		Log.d("TIMERXX",
				"stert RingTimer Timer started 15secs ******* "
						+ sb.getSignalid());
	}

	public void startAcceptTimer(SignalingBean sb, CallsOverInternet callObj) {
		Timer acceptTimer = new Timer();
		AcceptCallTimer callRingTimer = new AcceptCallTimer();
		callRingTimer.setProprietarySignalling(this);
		callRingTimer.setSignalingBean(sb);
		callRingTimer.setCallsession(callObj);
		acceptTimer.schedule(callRingTimer, 30000);
		Log.d("TIMERXX",
				"Accept Timer started 15secs *******@@@@@ " + sb.getSignalid());
		callTimer.put(sb.getSignalid(), acceptTimer);
		Log.d("TIMERS", "Accept Timer started 15secs  " + sb.getSignalid());
		Log.d("TIMERXX",
				"Start Accept Timer started 15secs ******* " + sb.getSignalid());
	}

	/**
	 * Start UDPEngine to send and Receive UDPSignal. This can also used to
	 * Initialize PortNumber.
	 * 
	 * @return Status 1 for success -1 for Failure.
	 */
	public int startUDPEngine() {
		try {
			if (udpEngine == null) {

				udpEngine = new UDPEngine();
				udpEngine.setUdpListener(this);
				udpEngine.startUDPListener();
				portno = udpEngine.getBindPort();

				// System.out
				// .println("############################################# UDP Engine started..............."
				// + portno);

			}
		} catch (Exception e) {
			e.printStackTrace();
			udpEngine = null;
			portno = -1;

		}
		return portno;

	}

	public UDPEngine getUDPEngine() {
		if (udpEngine == null) {
			startUDPEngine();
		}
		return udpEngine;
	}

	/**
	 * Stop UDPEngine.Results in stopped of DatagramSocket.
	 */
	public void stopUDPEngine() {
		// System.out.println("### stop udpEngine called :");
		if (udpEngine != null) {
			Log.e("buddyx", "Name: stopeeeeed Udpppppppp");
			udpEngine.stopUDPListener();
		}
		running = false;

	}

	/**
	 * used to get callTable
	 * 
	 * @return callTable reference
	 */
	public HashMap getCallTable() {
		return callTable;
	}

	/**
	 * Set the CallTable Reference.
	 * 
	 * @param callTable
	 *            CallTable Reference
	 */
	public void setCallTable(HashMap callTable) {
		this.callTable = callTable;
	}

	/**
	 * used to get callStatus. The call Status will be CALL_INVITE, CALL_ACCEPT,
	 * CALL_RESPONSE, CALL_BYE etc.
	 * 
	 * @param type
	 *            callType
	 * @return CallStatus
	 */
	private CallStatus checkTypeOfCall(String type, String result) {

		CallStatus callStatus = null;
		if (type.equals("0")) {
			callStatus = CallStatus.CALL_INVITE;
		} else if (type.equals("1")) {
			if (result.equals("2")) {
				callStatus = CallStatus.CALL_RINGING;
			} else {
				callStatus = CallStatus.CALL_ACCEPT;

			}

			// Ed
		} else if (type.equals("2")) {
			callStatus = CallStatus.CALL_RESPONSE;
		} else if (type.equals("3")) {
			callStatus = CallStatus.CALL_BYE;
		} else if (type.equals("4")) {
			callStatus = CallStatus.CALL_END;
		} else if (type.equals("12")) {
			callStatus = callStatus.CALL_ACCEPT;

		}

		return callStatus;
	}

	/**
	 * Used to Change the CallStatus on the TimerBean.
	 * 
	 * @param timerBean
	 * @param type
	 */
	private void changeCallStatus(TimerBean timerBean, String type,
			String result) {
		if (type.equals("0")) {
			timerBean.setCallStatus(CallStatus.CALL_RESPONSE);
		} else if (type.equals("1")) {

			if (result.equals("2")) {
				Log.d("DEBUG",
						"Changing Call Status " + timerBean.getCallStatus());
				timerBean.setCallStatus(CallStatus.CALL_ACCEPT);
				Log.d("DEBUG",
						"Changed Call Status " + timerBean.getCallStatus());

			} else {
				timerBean.setCallStatus(CallStatus.CALL_BYE);
			}
		} else if (type.equals("2")) {
			timerBean.setCallStatus(CallStatus.CALL_BYE);
		} else if (type.equals("3")) {
			timerBean.setCallStatus(CallStatus.CALL_END);
		} else if (type.equals("4")) {
			timerBean.setCallStatus(CallStatus.CALL_ClOSED);
		}
	}

	/**
	 * Notify ProprietyResponse to User Interface.
	 * 
	 * @param sb
	 *            signaling information
	 */
	public void notifyUI(SignalingBean sb) {

		if (callSessionListener != null) {
			callSessionListener.notifyProprietyResponse(sb);
		}
	}

	// String connectipOnSendMessage = null;
	// int connectportOnSendMessage = 0;

	/**
	 * This method is used to convert the Signaling bean in to appropriate XML
	 * data and send the message using UDP Engine.This method is also used to
	 * set the Timer for Signaling.
	 * 
	 * @param sb
	 *            signaling Information.
	 */
	public void sendMessage(SignalingBean sb) {

		Log.d("UDP_SIGNAL", "Type : " + sb.getType());

		try {
			// used for maintain accurate signal Time Mismatch
			if (sb.getType().equals("0")) {
				Log.d("MSOT1", "Inserted data on type 0 " + sb.getSessionid()
						+ " To " + sb.getTo());
				communicationEngine.maintainSignallingOnTime.put(
						sb.getSessionid() + sb.getTo(), "key");
			} else if (sb.getType().equals("3")) {
				if (communicationEngine.maintainSignallingOnTime.containsKey(sb
						.getSessionid() + sb.getTo())) {
					communicationEngine.maintainSignallingOnTime.remove(sb
							.getSessionid() + sb.getTo());
				}
			}

			// maintained

			// communicationEngine.maintainSignallingOnTime.
			TimerBean timerBean = null;
			String xml = null;
			// String connectip = null;
			// int connectport = 0;
			String connectipOnSendMessage = null;
			int connectportOnSendMessage = 0;
			sb.setSignalport(Integer.toString(portno));

			// if (sb.getPublicip() == null) {
			// sb.setPublicip(sb.getLocalip());
			// }
			// if (sb.getTopublicip() == null) {
			// sb.setTopublicip(sb.getTolocalip());
			// }
			// System.out
			// .println("###################3 HANGUP CALL ########################3"
			// + "$$$$$ "
			// + sb.getTolocalip()
			// + " $$$$$ "
			// + sb.getTopublicip() + "### ss" + sb.getSessionid());

			// System.out.println("messageSessionTable :"+messageSessionTable);
			if (messageSessionTable.containsKey(sb.getSignalid())) {
				timerBean = (TimerBean) messageSessionTable.get(sb
						.getSignalid());
				if (timerBean.getTimer() != null) {
					timerBean.getTimer().cancel();
				}
			} else {
				timerBean = new TimerBean();
				messageSessionTable.put(sb.getSignalid(), timerBean);
			}

			if (sb.getType().equals("0")) {
				xml = xmlComposer.composeInviteXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "0", sb.getType());

				timerBean.setCallStatus(CallStatus.CALL_RINGING);
				xmlComposer.composeInviteXML(sb);
				AppMainActivity.tcpEngine.sendMessage(xml);
				return;
			} else if (sb.getType().equals("11") || sb.getType().equals("12")) {

				xml = xmlComposer.composeInviteXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "0", sb.getType());
			} else if ((sb.getType().equals("1") && sb.getResult().equals("0"))
					|| sb.getType().equals("2") || sb.getType().equals("7")) {
				xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "0", sb.getType());
				Log.i("signal12345", " while reply type =1 r = 0");
				if ((sb.getType().equals("1") && sb.getResult().equals("0"))
						|| sb.getType().equals("2")) {
					AppMainActivity.tcpEngine.sendMessage(xml);
					return;
				}
			} else if ((sb.getType().equals("1") && sb.getResult().equals("-1"))) {
				xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "-1", sb.getType());
				AppMainActivity.tcpEngine.sendMessage(xml);
				return;
			} else if (sb.getType().equals("1") && sb.getResult().equals("1")) {
				xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "1", sb.getType());
				AppMainActivity.tcpEngine.sendMessage(xml);
				return;
			} else if (sb.getType().equals("1") && sb.getResult().equals("2")) {
				xml = xmlComposer.composeSignalXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "2", sb.getType());
				AppMainActivity.tcpEngine.sendMessage(xml);
				return;
			} else if (sb.getType().equals("3")) {
				xml = xmlComposer.composeInviteXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "0", "3");
				AppMainActivity.tcpEngine.sendMessage(xml);
				return;
			} else if (sb.getType().equals("6") || sb.getType().equals("9")) {
				xml = xmlComposer.composeInviteXML(sb);
				xml = xmlComposer.getMessagewithRoot(xml, "0", sb.getType());
			} else if (sb.getType().equals("100")) {
				GroupChatBean gcBean = (GroupChatBean) sb.getRequestSource();
				gcBean.setSessionid(sb.getSessionid());

                xml = xmlComposer.composeGroupChatXML(gcBean);
                //Narayanan Feb 15th Start
//                if(AppMainActivity.tcpEngine.sendMessage(xml)){
//                    GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
//                            .get("groupchat");
//                    gcActivity.updateThumbs(sb.getSignalid());
//                }
//                return;
                //Narayanan Feb 15th End
            } else if (sb.getType().equals("104")) {
                GroupChatBean gcBean = (GroupChatBean) sb.getRequestSource();
                gcBean.setSessionid(sb.getSessionid());

                xml = xmlComposer.composeGroupChatDeleteMsgXML(gcBean);
                //Narayanan Feb 15th Start
//                AppMainActivity.tcpEngine.sendMessage(xml);
//                return;
                //Narayanan Feb 15th End
            } else if (sb.getType().equals("102")) {
                xml = xmlComposer.composeGroupChat102XML((GroupChatBean) sb
                        .getRequestSource());
                //Narayanan Feb 15th Start
//                AppMainActivity.tcpEngine.sendMessage(xml);
//               return;
                //Narayanan Feb 15th End
            }

			AppReference.getLogger()
					.debug("UDP_SIGNAL" + "Send Signal :"
							+ new String(xml.getBytes()));
			SingleInstance.mainContext.setLogWriterForCall("Send Signal :"
					+ new String(xml.getBytes()));
			// Newly added for Encryption...
			/** encrypt message using AES **/
			Log.d("UDP_SIGNAL", "Send Signal :" + new String(xml.getBytes()));

			final byte[] msg = AESCrypto.encrypt(seceretKey, xml.getBytes());
			// byte[] msg=xml.getBytes();

			timerBean.setMessage(msg);
			timerBean.setSendCount(0);
			timerBean.setRetransmissionCount(retransmissionCount);

			if (sb.getTopublicip() != null
					&& (sb.getPublicip().equals(sb.getTopublicip()) || sb
							.getTolocalip().equals(sb.getTopublicip()))) {
				Log.d("UDP_SIGNAL", "Public Ip :" + sb.getPublicip()
						+ ", ToPublic Ip :" + sb.getTopublicip()
						+ ", ToLocalIP :" + sb.getTolocalip());
				connectipOnSendMessage = sb.getTolocalip();
				connectportOnSendMessage = Integer.parseInt(sb
						.getToSignalPort());

			} else {
				Log.d("UDP_SIGNAL", "from calls on NAT");
				connectipOnSendMessage = routerip;
				connectportOnSendMessage = routeport;
			}

			Log.d("UDP_SIGNAL", "CIP " + connectipOnSendMessage + " port"
					+ connectportOnSendMessage);
			Timer timer = new Timer();
			if (sb.getType().equals("11")) {
				ProprietaryRetransmissionIm retransmission = new ProprietaryRetransmissionIm(
						retransmissionCount, udpEngine, msg);
				retransmission.setConnectip(connectipOnSendMessage);
				retransmission.setConnectport(connectportOnSendMessage);
				retransmission.setRouterip(routerip);
				retransmission.setRouterPort(routeport);
				timerBean.setCallStatus(CallStatus.CALL_ACCEPT);
				// Timer timer = new Timer();
				timer.schedule(retransmission, 0, 3000);
			} else {
				ProprietaryRetransmission retransmission = new ProprietaryRetransmission(
						retransmissionCount, udpEngine, msg, sb);

				retransmission.setOrginalSignal(xml);
				retransmission.setConnectip(connectipOnSendMessage);
				retransmission.setConnectport(connectportOnSendMessage);
				retransmission.setRouterip(routerip);
				retransmission.setRouterPort(routeport);
				// timer.schedule(retransmission, 0, 300);
				timer.schedule(retransmission, 0, 3000);
				Log.i("CALLINFO",
						" RETRANSMISSION " + "IP : "
								+ retransmission.getConnectip() + "PORT : "
								+ retransmission.getConnectport());
			}

			timerBean.setTimer(timer);
			// System.out.println("***************************************");
			// System.out.println("Following XML sent");

			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			try {
				Log.i("CALLINFO", "Message : " + msg + "IP : "
						+ connectipOnSendMessage + "PORT : "
						+ connectportOnSendMessage);
				udpEngine.sendUDP(msg,
						InetAddress.getByName(connectipOnSendMessage),
						connectportOnSendMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// }
			// }).start();
			//
			// udpEngine.sendUDP(msg, InetAddress.getByName(connectip),
			// connectport);

			// System.out.println(xml);
			// System.out.println("***************************************");
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError o) {
			// TODO: handle exception
			o.printStackTrace();
			Toast.makeText(SingleInstance.mainContext, "Out of Memory Error",
					Toast.LENGTH_LONG).show();
		}

	}

	public void sendType13FromTimer(byte[] data, InetAddress address, int port) {
		try {
			udpEngine.sendUDP(data, address, port);
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void sendRtpRequestMessage(SignalingBean sbean) {
		String xmString = null;
		try {

			Log.d("REQUEST", "request from propSignalling ");
			xmString = xmlComposer.composeInviteXML(sbean);
			xmString = xmlComposer.getMessagewithRoot(xmString, "0",
					sbean.getType());

			final byte[] msg = AESCrypto.encrypt(seceretKey,
					xmString.getBytes());

			udpEngine.sendUDP(msg, InetAddress.getByName(routerip), routeport);
			Log.d("REQUEST", "request from propSignalling " + xmString);
		} catch (Exception e) {
			Log.d("REQUEST", "request from propSignalling " + e);
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to Cancel the Retransmission Timer alloted for the
	 * signaling message.
	 * 
	 * @param signalid
	 *            Unique Id for signal.
	 * @param type
	 *            Type
	 * @param sessionid
	 *            SessionId of the Signal.
	 * @param callType
	 *            CallType.S
	 * @param sb
	 *            Signaling Information.
	 * @return Result
	 */
	public boolean cancelRetransmission(String signalid, String type,
			String sessionid, String callType, SignalingBean sb) {

		Log.d("Type13", "cancelRetransmission         Type " + sb.getType());
		if (!sb.getType().equals("13") && !sb.getType().equals("14")) {
			if (!type.equals("4")) {

				Log.d("CHK", " cancelRetransmission Type " + type + " Result "
						+ sb.getResult() + " SessionID " + sessionid
						+ " callType " + callType);

				boolean resultValue = true;
				Log.d("CHK1",
						"From " + sb.getFrom() + "  to " + sb.getTo()
								+ " Type " + sb.getType() + " signalid "
								+ sb.getSignalid()+ " vidssrc : "+sb.getVideossrc());

				if(sb.getCallType()!=null && (sb.getCallType().equalsIgnoreCase("VC") || sb.getCallType().equalsIgnoreCase("AC"))&& sb.getVideossrc() != null && !sb.getVideossrc().equalsIgnoreCase("null") && sb.getVideossrc().length() > 0){

					if(!WebServiceReferences.videoSSRC_total_list.contains(Integer.parseInt(sb.getVideossrc()))){
//						WebServiceReferences.videoSSRC_total_list.add(Integer.parseInt(sb.getVideossrc()));
					}

					if(!WebServiceReferences.videoSSRC_total.containsKey(Integer.parseInt(sb.getVideossrc()))){
						VideoThreadBean videoThreadBean = new VideoThreadBean();
						videoThreadBean.setMember_name(sb.getFrom());
						videoThreadBean.setVideoDisabled(false);
						Log.i("VideoSSRC", "videoSSRC_total.put 2");
						WebServiceReferences.videoSSRC_total.put((Integer.parseInt(sb.getVideossrc())),videoThreadBean);
					}
				}

				if (messageSessionTable.containsKey(signalid)) {
					Log.d("CHK1", "Already contains " + sb.getFrom() + "  to "
							+ sb.getTo() + " Type " + sb.getType()
							+ " signalid " + sb.getSignalid());
					Log.d("CHK", "Signal ID already conatains " + type
							+ " Result " + sb.getResult());
					TimerBean timerBean = (TimerBean) messageSessionTable
							.get(signalid);

					Log.d("DEBUG",
							"Expected Call Status " + timerBean.getCallStatus());
					CallStatus receivedCallStatus = checkTypeOfCall(type,
							sb.getResult());
					Log.d("DEBUG", "Received Call Status " + receivedCallStatus);

					// used for Allow Accept when Ringer not comes
					if (receivedCallStatus == CallStatus.CALL_ACCEPT
							&& timerBean.getCallStatus() == CallStatus.CALL_RINGING) {
						timerBean.setCallStatus(CallStatus.CALL_ACCEPT);
					}

					//

					if (timerBean.getCallStatus() == receivedCallStatus
							|| (receivedCallStatus == (CallStatus.CALL_BYE) && timerBean
									.getCallStatus() == CallStatus.CALL_RESPONSE)
							|| (receivedCallStatus == (CallStatus.CALL_BYE) && timerBean
									.getCallStatus() == CallStatus.CALL_ACCEPT)) {
						Log.d("type12", "if... ");
						Log.d("DEBUG", "Approved - Current Call Status "
								+ timerBean.getCallStatus());
						if (timerBean.getTimer() != null) {
							timerBean.getTimer().cancel();
							System.out
									.println("Condition Matched  Timer canceld");

							if (type.equals("3")) {
								try {
									if (messageSessionTable
											.containsKey(signalid)) {
										ProprietarySignalling.messageSessionTable
												.remove(signalid);
										Log.d("SIGNALID",
												"NOTIFY UDP .REMOVED signalid "
														+ signalid);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}
						if (receivedCallStatus == (CallStatus.CALL_BYE)) {
							Log.i("calltest", "stop" + receivedCallStatus);

						}
						changeCallStatus(timerBean, type, sb.getResult());
						System.out
								.println("Call Status Changed Expecting next status "
										+ timerBean.getCallStatus());

						if (type.equals("12") || type.equals("101")) {
							// timerBean.getTimer().cancel();
							Log.d("key",
									"on receive " + sessionid + sb.getFrom());
							InstantMessage im = (InstantMessage) communicationEngine
									.getImHashmap().get(
											sessionid + sb.getFrom());
							if (im != null)
								im.notifyResponse(0);
							// return true;
						}
					} else {

						Log.d("TYPE101", "messageSessionTable 1: "
								+ messageSessionTable.size());
						if (type.equals("12") || type.equals("101")) {
							timerBean.getTimer().cancel();
							InstantMessage im = (InstantMessage) communicationEngine
									.getImHashmap().get(
											sessionid + sb.getFrom());
						}

						resultValue = false;
						System.out
								.println("Duplicate message ignored.....Type "
										+ sb.getType() + " Signal ID "
										+ sb.getSignalid());
					}

				} else {
					Log.d("TYPE101", "messageSessionTable 2: "
							+ messageSessionTable.size());
					if (type.equals("0") || type.equals("6")
							|| type.equals("5") || type.equals("7")
							|| type.equals("9") || type.equals("11")) {

						TimerBean timerBean = new TimerBean();
						timerBean.setCallStatus(CallStatus.CALL_RESPONSE);
						messageSessionTable.put(signalid, timerBean);
						// used to process Type 0 and Result 2
						if (type.equals("0") && sb.getResult().equals("0")) {
							if((sb.getVideopromote() != null && sb.getVideopromote().equalsIgnoreCase("yes"))
								||	(sb.getVideoStoped() != null && (sb.getVideoStoped().equalsIgnoreCase("yes") || sb.getVideoStoped().equalsIgnoreCase("no")) )){

							} else {
								sendAck(sb);
							}
						}

					} else {
						Log.d("type12", "final ");
						resultValue = false;
					}

				}

				return resultValue;
			} else {
				return true;
			}

		}
		// ////////////////////////////

		else {
			Log.d("Type13", "cancelretransmission " + sb.getSignalid());
			if (messageSessionTable.containsKey(signalid)) {
				Log.d("Type13", "contains " + sb.getSignalid());
				Log.d("Type13", "contains " + type);
				if (type.equals("14")) {
					//
					TimerBean tbReceive = (TimerBean) messageSessionTable
							.get(signalid);

					Log.d("Type13", "contains " + tbReceive.getCallStatus1());
					if (tbReceive.getCallStatus1() == CallStatus1.CALL_INT1) {
						// Receive Type 14 expected...
						tbReceive.setCallStatus1(CallStatus1.CALL_INT3);
						Log.d("Type13", "trueeeee " + sb.getSignalid());

						return true;
					} else {
						// Already receive Type 14 for this Signal Id........
						return false;
					}

				}

				return false;

			} else {
				if (type.equals("13")) {
					TimerBean timerBean = new TimerBean();

					timerBean.setCallStatus1(CallStatus1.CALL_INT2);
					// timerBean.setCallStatus(CallStatus.CALL_RESPONSE);
					messageSessionTable.put(signalid, timerBean);
					return true;
				}
				return false;
			}

			// return false;
		}

		// /////////////////////////
	}

	void sendAck(SignalingBean sbx) {
		// Send Ack for Type2

		SignalingBean sb = (SignalingBean) sbx.clone();

		String from = sb.getFrom();
		String to = sb.getTo();
		String fromLocalip = sb.getLocalip();
		String fromPublicIp = sb.getPublicip();

		sb.setFrom(to);
		sb.setTo(from);
		sb.setLocalip(communicationEngine.getLocalInetaddress());
		sb.setPublicip(communicationEngine.getPublicInetaddress());
		sb.setToSignalPort(sbx.getSignalport());
		sb.setTolocalip(fromLocalip);
		sb.setTopublicip(fromPublicIp);
		sb.setType("1");
		sb.setResult("2");

		// Used To send Ack For Ringing

		sendMessage(sb);

	}

	/**
	 * Get the Server IpAddress
	 * 
	 * @return Server IpAddress
	 */
	public String getRouterip() {
		return routerip;
	}

	/**
	 * Set the Server IpAddress
	 * 
	 * @param routerip
	 *            Server IpAddress
	 */
	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}

	/**
	 * Get the Server Port.
	 * 
	 * @return Server Port
	 */
	public int getRouteport() {
		return routeport;
	}

	/**
	 * Set the Server Port.
	 * 
	 * @param routeport
	 *            Server Port
	 */
	public void setRouteport(int routeport) {
		this.routeport = routeport;
	}

	public void startPortRefereshTimer(String username) {

		if (portRefereshTimer == null) {

			portRefereshTimer = new Timer();

			if (portRefereshreceiver == null) {
				portRefereshreceiver = new PortRefresher();
			}

			portRefereshreceiver.makeStart(communicationEngine.getContext());

			String mKeyZero = xmlComposer.getWellFormedXml(null, username,
					"20", "0");
			String mKeyOne = xmlComposer.getWellFormedXml(null, username, "20",
					"1");

            AlarmManager alarmManager = (AlarmManager) SingleInstance.mainContext
                    .getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(SingleInstance.mainContext,
                    KeepAliveReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    SingleInstance.mainContext, 222, intent, 0);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), PORT_REFERSH_INTERVAL,
					pendingIntent);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//				startRepeatingTask();
//
//			}

			// PortRefereshTask task = new PortRefereshTask(mKeyZero, mKeyOne,
			// udpEngine, this);
			// // task.
			// portRefereshTimer.schedule(task, 0, PORT_REFERSH_INTERVAL);

			Log.d("PORTREFERESH", "PortRefereshTask scgheduled interval "
					+ PORT_REFERSH_INTERVAL + " ms");

		}

	}

	public void stopPortRefereshTimer() {

		// new method start

		try {
			AlarmManager alarmManager = (AlarmManager) SingleInstance.mainContext
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(SingleInstance.mainContext,
					KeepAliveReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					SingleInstance.mainContext, 222, intent, 0);
			alarmManager.cancel(pendingIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// end
		stopRepeatingTask();
		if (portRefereshTimer != null) {

			portRefereshTimer.cancel();
			portRefereshreceiver.makeStop(communicationEngine.getContext());
			Log.d("PORTREFERESH", "PortRefereshTask stopped");
			portRefereshTimer = null;

		}
	}

	// public void startPortRefereshTimer(String username){
	// try{
	// if(portRefereshTimer==null){
	// portRefereshTimer=new PortRefresher();
	//
	// String mKeyZero=xmlComposer.getWellFormedXml(null,username, "20", "0");
	// String mKeyOne=xmlComposer.getWellFormedXml(null,username, "20", "1");
	//
	// ProprietarySignalling.test=" Modified";
	//
	// Log.d("PORTREFERESH","onreceive test on startporefresh  "+ProprietarySignalling.test);
	//
	// //pRefreshDatas=new PortRefreshDatas();
	// ApplicationExtension.pRefreshDatas.setmKeyOne(mKeyOne);
	// ApplicationExtension.pRefreshDatas.setpSignalling(this);
	// ApplicationExtension.pRefreshDatas.setUdpEngine(udpEngine);
	//
	//
	//
	//
	// Log.d("PORTREFERESH","keyzero "+mKeyZero);
	// Log.d("PORTREFERESH","keyone "+mKeyOne);
	//
	//
	// portRefereshTimer.makeStart(communicationEngine.getContext());
	// Log.d("PORTREFERESH","PortRefereshTask scgheduled interval "+PORT_REFERSH_INTERVAL+" ms");
	// }
	// }
	// catch (Exception e) {
	// Log.d("PORTREFERESH","Port "+e);
	// }
	//
	//
	//
	// }
	//
	// public void stopPortRefereshTimer(){
	// if(portRefereshTimer!=null){
	// portRefereshTimer.makeStop(communicationEngine.getContext());
	// Log.d("PORTREFERESH","PortRefereshTask stopped");
	// portRefereshTimer=null;
	// //pRefreshDatas=null;
	// }
	// }

	public void sendKeyOneMessage(final String mMessageKeyOne) {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			int count = 0;

			@Override
			public void run() {
				try {
					if (AppReference.isWriteInFile) {
						try {
							AppReference.getLogger().debug(
									"--------------------------");
							AppReference.getLogger().debug("Timer : " + count);
							AppReference.getLogger().debug(
									"--------------------------");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (count == 1) {
						Log.d("PORTREFERESH",
								" Key One message timer elapsed so requesting ui to send heartbeat request..");

						SignalingBean sb = new SignalingBean();
						sb.setType("heartbeat");
						sb.setCallType("");
						Log.i("KA123",
								"sendKeyOne : Message Key One message timer elapsed so requesting ui to send heartbeat request..");
						if (AppReference.isWriteInFile) {
							try {
								AppReference.getLogger().debug(
										"KA123" + String.valueOf(count));
								AppReference
										.getLogger()
										.debug("KA123 sendKeyOne : Message Key One message timer elapsed so requesting ui to send heartbeat request..");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						callSessionListener.notifyProprietyResponse(sb);

					} else {
						count++;
						byte[] data = AESCrypto.encrypt(
								ProprietarySignalling.seceretKey,
								mMessageKeyOne.getBytes());
						udpEngine.sendUDP(data,
								InetAddress.getByName(getRouterip()),
								getRouteport());
						// Log.i("KA123",
						// "sendKeyOneMessage:  Type 20 key 1 sent");
						// Log.d("PORTREFERESH", " Type 20 key 1 sent");
						if (AppReference.isWriteInFile) {
							try {
								AppReference
										.getLogger()
										.debug("KA123 sendKeyOneMessage:  Type 20 key 1 sent");
								AppReference.getLogger().debug(
										"KA123Type 20 key 1 sent");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// count++;
					}

				} catch (Exception e) {
					if (AppReference.isWriteInFile) {
						try {
							AppReference
									.getLogger()
									.error("KA123 ERROR in calling sendKeyOneMessage(final String mMessageKeyOne) method ",
											e);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					e.printStackTrace();
				}
			}
		}, 0, 3000);
	}

	public void stopKeyOneMessage() {
		if (AppReference.isWriteInFile) {
			try {
				AppReference.getLogger()
						.debug("KA123 Stopping key one message");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (mTimer != null) {
			mTimer.cancel();
			if (AppReference.isWriteInFile) {
				try {
					AppReference.getLogger().debug(
							"KA123 Stopped key one message");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Log.d("PORTREFERESH",
					"Got type20 response. Key One message timer canceld");
			mTimer = null;
		}
	}

	/**
	 * Used to cancel the Timer alloted for Call.
	 * 
	 * @param signalid
	 *            UniqueId for Services
	 */
	public void stopCallTimer(String signalid) {
		Log.d("TIMERS", "stop calltime " + callTimer + " signalid " + signalid);

		if (callTimer.containsKey(signalid)) {
			Log.d("TIMERXX", "Trying To check match 333333333333333333 "
					+ signalid);
			Timer timer = callTimer.get(signalid);
			if (timer != null) {
				timer.cancel();
				Log.d("TIMERXX", "Removed");
				Log.d("TIMERS", "CallTimer stop");
				// System.out.println("### CALL TIMER CANCELD FOR SIGNAL : "
				// + signalid);
			}
			callTimer.remove(signalid);
		}

	}

	/**
	 * Used to check and Notify the Incoming Signal message to the UI.
	 * 
	 * @param sb
	 *            Receiving Signal Information.
	 */
	public void dispatchCallEvents(Object sbx) {
		Log.d("udp", "dispatch call events started ");

		// System.out.println("callSessionListener :"+callSessionListener);
		if (sbx instanceof SignalingBean) {
			SignalingBean sb = (SignalingBean) sbx;

			if (callSessionListener != null) {

				// specially for receiver side
				if (sb.getType().equals("0")) {
					Log.d("MSOT",
							"Inserted data on type 0 " + sb.getSessionid()
									+ sb.getTo());
					communicationEngine.maintainSignallingOnTime.put(
							sb.getSessionid() + sb.getTo(), "key");
				}

				Log.d("DDD", "dispatchCallEvents Type " + sb.getType()
						+ " Result " + sb.getResult());

				if (checkandAllow(sb)) {
					Log.d("udp", "dispatch call events notify ");
					if (sb.getType().equals("0") && sb.getResult().equals("0")) {

						SignalingBean sbdata = (SignalingBean) sb.clone();

						String from = sb.getFrom();
						String to = sb.getTo();
						String fromLocalip = sb.getLocalip();
						String fromPublicIp = sb.getPublicip();

						sbdata.setFrom(to);
						sbdata.setTo(from);
						sbdata.setLocalip(communicationEngine
								.getLocalInetaddress());
						sbdata.setPublicip(communicationEngine
								.getPublicInetaddress());
						sbdata.setToSignalPort(sb.getSignalport());
						sbdata.setTolocalip(fromLocalip);
						sbdata.setTopublicip(fromPublicIp);
						sbdata.setSignalid(sb.getSignalid());

						startCallTimer(sbdata, true);

					} else if (sb.getType().equals("1")
							&& sb.getResult().equals("2")) {

						Log.d("121",
								"From " + sb.getFrom() + "  to " + sb.getTo()
										+ " Type " + sb.getType()
										+ " signalid " + sb.getSignalid());
						SignalingBean sbdata = (SignalingBean) sb.clone();

						String from = sb.getFrom();
						String to = sb.getTo();
						String fromLocalip = sb.getLocalip();
						String fromPublicIp = sb.getPublicip();

						sbdata.setFrom(to);
						sbdata.setTo(from);
						sbdata.setLocalip(communicationEngine
								.getLocalInetaddress());
						sbdata.setPublicip(communicationEngine
								.getPublicInetaddress());
						sbdata.setToSignalPort(sb.getSignalport());
						sbdata.setTolocalip(fromLocalip);
						sbdata.setTopublicip(fromPublicIp);
						sbdata.setSignalid(sb.getSignalid());
						stopCallTimer(sb.getSignalid());
						startCallTimer(sbdata, false);

					} else if (sb.getType().equals("1")
							&& (sb.getResult().equals("0") || sb.getResult()
									.equals("1"))) {

						Log.d("121",
								"www. From " + sb.getFrom() + "  to "
										+ sb.getTo() + " Type " + sb.getType()
										+ " signalid " + sb.getSignalid());

						Log.d("TIMERS", " Type " + sb.getType() + " Result "
								+ sb.getResult());
						stopCallTimer(sb.getSignalid());

					}

					Log.d("CHK", "notify UI on dispatch " + sb.getType()
							+ " Result " + sb.getResult());
					// Used to Remove Buddy from Conference Request...
					if ((sb.getType().equals("0") && sb.getResult().equals("1"))
							|| (sb.getType().equals("1") && sb.getResult()
									.equals("1"))
							|| (sb.getType().equals("3"))
							|| (sb.getType().equals("10"))
							|| (sb.getType().equals("8"))) {
						String key = null;
						if (sb.getType().equals("10")
								|| sb.getType().equals("8")) {
							key = sb.getTo();
						} else {
							key = sb.getFrom();
						}
						Log.d("REFC", "0n key " + key + " Type " + sb.getType());
						communicationEngine
								.removeEntryFromConfRequestTable(key);
					}
					callSessionListener.notifyProprietyResponse(sb);
				}

				Log.d("udp", "dispatch call events notify ");

			}

			if (sb.getType().equals("11")) {

				if (sb.getCallType().trim().equals("MTP")
						|| (sb.getCallType().trim().equals("MAP"))
						|| sb.getCallType().trim().equals("MVP")
						|| sb.getCallType().trim().equals("MPP")
						|| (sb.getCallType().equals("MHP"))) {
					// SignalingBean sbToSend=new SignalingBean();
					// sbToSend=sb;
					Log.d("REQUEST", "11       " + sb.getType());
					// Used to send pending message. While user goes offline and
					// come on Online and send the message.
					try {

						if (communicationEngine.imHashmap.containsKey(sb
								.getSessionid() + sb.getFrom())) {

							InstantMessage instantMessage = (InstantMessage) communicationEngine.imHashmap
									.get(sb.getSessionid() + sb.getFrom());
							if(instantMessage!=null)
							instantMessage.sendMesage();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					//

					// sb.setMessage("");
					Log.d("sebye", "from callsover send proprietary signal");
					SignalingBean sbToSend = (SignalingBean) sb.clone();

					//
					String from = sb.getFrom();
					String to = sb.getTo();
					sbToSend.setFrom(to);
					sbToSend.setTo(from);
					sbToSend.setType("12");
					sbToSend.setTolocalip(sb.getLocalip());
					sbToSend.setTopublicip(sb.getPublicip());
					sbToSend.setPublicip(communicationEngine
							.getPublicInetaddress());
					sbToSend.setLocalip(communicationEngine
							.getLocalInetaddress());
					sbToSend.setToSignalPort(sb.getSignalport());
					sbToSend.setSignalingPort(Integer.toString(portno));

					//

					sbToSend.setMessage("");
					Log.d("sebye", "type " + sb.getType());
					Log.d("sebye", "f " + sb.getCallType());
					Log.d("sebye", "from callsover send proprietary signal");
					Log.d("sebye", "from callsover send proprietary signal");
					sendMessage(sbToSend);
				}
			} else if (sb.getType().equals("4")) {
				Log.d("REQUEST", "44444444444444");
				Log.d("REQUEST", "dispatch callevents 4 ");
				if (communicationEngine.getCallTable().containsKey(
						sb.getSessionid())) {
					Log.d("REQUEST", "contains packet " + sb.getSeqNo());
					try {
						RelayClientNew relay = (RelayClientNew) communicationEngine
								.getCallTable().get(sb.getSessionid());
						RtpPacket rtppacket = relay.getRtpEngine()
								.getlostPacket(sb.getSeqNo());

						// String
						// str="1001,"+sb.getSessionid()+","+sb.getFrom()+"#";
						// byte[] specialMessage=str.getBytes();
						// byte[] video=rtppacket.getPacket();
						// Log.d("data", "length:"+rtppacket.packet.length);
						// Log.d("data", "length of packet:"+video.length);
						//
						//
						// byte[] finalbyte = new byte[specialMessage.length +
						// video.length];
						// System.arraycopy(specialMessage, 0, finalbyte, 0,
						// specialMessage.length);
						// System.arraycopy(video, 0, finalbyte,
						// specialMessage.length, video.length);
						//
						// rtppacket.packet=finalbyte;
						//
						// Log.d("REQUESTP",
						// "contains packet "+communicationEngine.getRelayServerip()+","+communicationEngine.getRelayServerPort()+","+str);
						// relay.getRtpEngine().send(rtppacket,
						// InetAddress.getByName(communicationEngine.getRelayServerip()),
						// communicationEngine.getRelayServerPort());

						String str = "1001," + sb.getSessionid() + ","
								+ sb.getFrom() + "#";
						// String str="";

						byte[] specialMessage = str.getBytes();

						Log.d("REQUEST", "qqq " + new String(specialMessage));

						// byte[] c = new byte[a.length + b.length];
						// System.arraycopy(a, 0, c, 0, a.length);
						// System.arraycopy(b, 0, c, a.length, b.length);

						byte[] videoOldwithHeader = rtppacket.getPacket();
						ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
						arrayStream.write(videoOldwithHeader, 12,
								rtppacket.getLength() - 12);
						// }catch(Exception e){
						// e.printStackTrace();
						// }
						// }

						byte video[] = arrayStream.toByteArray();

						byte[] finalbyte = new byte[specialMessage.length
								+ video.length];
						System.arraycopy(specialMessage, 0, finalbyte, 0,
								specialMessage.length);
						System.arraycopy(video, 0, finalbyte,
								specialMessage.length, video.length);

						// /////////////////////////////
						ByteArrayOutputStream dataArray = new ByteArrayOutputStream();
						dataArray.write(new byte[12]);
						dataArray.write(finalbyte, 0, finalbyte.length);
						Log.d("SIZE", "REQUEST packet seq no " + sb.getSeqNo());
						Log.d("SIZE", "original MSg " + rtppacket.getLength());
						Log.d("SIZE", "REQUEST packet " + finalbyte.length);
						byte rtpadata[] = dataArray.toByteArray();
						Log.d("SIZE", "REQUEST  packet size" + rtpadata.length);
						RtpPacket rtp = new RtpPacket(rtpadata, rtpadata.length);
						rtp.setMarker(rtppacket.hasMarker());
						rtp.setTimestamp(rtppacket.getTimestamp());
						rtp.setSequenceNumber(rtppacket.getSequenceNumber());
						rtp.setSscr(rtppacket.getSscr());
						rtp.setPayloadType(rtppacket.getPayloadType());
						// rtpp.setTimestamp(timespan);
						// rtpp.setMarker(true);
						// rtpp.setSequenceNumber(videoSqno);

						// ///////////////////////

						Log.d("TEST", "old bytes with header "
								+ videoOldwithHeader.length);
						Log.d("TEST", "old bytes without header "
								+ video.length);
						Log.d("TEST", "special message bytes without header "
								+ specialMessage.length);
						Log.d("TEST", "final byte special+video "
								+ finalbyte.length);
						Log.d("TEST", "final Byte with header "
								+ rtp.packet_len);

						// RtpPacket rtp=new RtpPacket(finalbyte,
						// finalbyte.length);
						// rtp.setMarker(rtppacket.hasMarker());
						// rtp.setTimestamp(rtppacket.getTimestamp());
						// rtp.setSequenceNumber(rtppacket.getSequenceNumber());
						// rtp.setSscr(rtppacket.getSscr());
						// rtp.setPayloadType(14);
						// rtp.set
						Log.d("SIZE",
								"packet store Final size()-----aaaaaaa-----> "
										+ rtppacket.getLength());
						Log.d("SIZE", "Final size()----bbbbb------> "
								+ rtp.packet.length);

						Log.d("data", "length:" + rtppacket.packet.length);
						Log.d("data", "length of packet:" + rtp.packet.length);

						relay.getRtpEngine().send(
								rtp,
								InetAddress.getByName(communicationEngine
										.getRelayServerip()),
								communicationEngine.getRelayServerPort());

						// for(int i=0;i<3;i++)
						// {
						// relay.getRtpEngine().send(rtppacket,
						// InetAddress.getByName(communicationEngine.getRelayServerip()),
						// communicationEngine.getRelayServerPort());
						// }

						// separateSpecialMessages(rtp,relay);

					} catch (Exception e) {
						Log.d("REQUEST", "contains  " + e);
						e.printStackTrace();
					}

				} else {
					Log.d("REQUESTP", "Not contains Packet ################# "
							+ sb.getSeqNo());
				}
			}

			//
			else if (sb.getType().equals("13")) {
				// Log.d("SIGNAL", "RECEIved type 13");
				// Log.d("SIGNAL", "RECEIved type 13");
				try {
					String from = sb.getFrom();
					String to = sb.getTo();

					sb.setFrom(to);
					sb.setTo(from);

					String xmlType13 = xmlComposer.composeType13Xml(sb);
					String finalXml = xmlComposer.getMessagewithRoot(xmlType13,
							"0", "14");

					// Log.d("SIGNAL", "Send type 14 "+finalXml);

					final byte[] msg = AESCrypto.encrypt(seceretKey,
							finalXml.getBytes());
					try {
						for (int i = 0; i < 3; i++) {
							udpEngine.sendUDP(msg,
									InetAddress.getByName(sb.getLocalip()),
									Integer.parseInt(sb.getSignalingPort()));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (sb.getType().equals("14")) {
				// Log.d("SIGNAL", "RECEIved type 14");
				String key = sb.getSessionid() + sb.getFrom();

				if (callTable.size() > 0) {

					// System.out.println(" callTable "+callTable+" key :"+key);
					Object obj = (CallsOverInternet) callTable.get(key);
					if (obj != null && obj instanceof CallsOverInternet) {
						CallsOverInternet callsOverInternet = (CallsOverInternet) obj;
						callsOverInternet.receiveType14(sb.getFrom()
								+ sb.getSessionid());

					}

				}

			}

			//

			else {
				Log.d("REQUEST", "elseeeeeeeeeee" + sb.getType());
			}
			Log.d("REQUEST", "end");

		} else if (sbx instanceof UdpMessageBean) {
			sendAcknowledgement((UdpMessageBean) sbx);
			Log.d("ALLOW", "on ");
			UdpMessageBean message = (UdpMessageBean) sbx;
			Log.d("ALLOW", "on " + message.getSid());
			if (callSessionListener != null) {

				if (udpMap.containsKey(message.getSid())) {
					Log.d("ALLOW", "on if " + message.getSid());
				} else {
					Log.d("ALLOW_Else", "on else " + message.getSid());
					udpMap.put(message.getSid(), message);
					Log.d("ALLOW", "on else put " + message.getSid());

					callSessionListener
							.notifyProprietyResponse((UdpMessageBean) sbx);
				}

			} else {
				Log.d("ALLOW", "callSessionListener is Null");
			}
		}

		Log.d("REQUEST", "Method end");
	}

	// void processType

	public void sendAcknowledgement(UdpMessageBean udpMessageBean) {

		String message = null;

		if (udpMessageBean.getType().equals("100")
				|| udpMessageBean.getType().equals("104")) {
			GroupChatBean gcBean = (GroupChatBean) udpMessageBean
					.getResponseObject();
			gcBean.setSid(udpMessageBean.getSid());
            message = xmlComposer.composeGroupChat102XML(gcBean);
            if(udpMessageBean.getType().equals("100"))
            {
                gcBean.setResult("2");
                message = xmlComposer.composeGroupChat101XML(gcBean);
            }
		}
        else if (udpMessageBean.getType().equals("101"))
        {
            GroupChatBean gcBean=(GroupChatBean) udpMessageBean
                    .getResponseObject();
            gcBean.setResult("3");
            message = xmlComposer.composeGroupChat101XML(gcBean);

        }
        else if (udpMessageBean.getType().equals("105"))
        {
            GroupChatBean gcBean=(GroupChatBean) udpMessageBean
                    .getResponseObject();
            gcBean.setResult(gcBean.getResult());
            message = xmlComposer.composeGroupChat105XML(gcBean);

        }else {
			message = xmlComposer.composeUdpAcknowledgement(udpMessageBean);
		}

		Log.d("SIGNAL", "Sending acknowledment ()() " + message);

		// byte[] portAlive=message.getBytes();
		byte[] portAlive = null;
		try {
			portAlive = AESCrypto.encrypt(ProprietarySignalling.seceretKey,
					message.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			udpEngine.sendUDP(portAlive, InetAddress.getByName(routerip),
					routeport);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	void separateSpecialMessages(RtpPacket rtpp, RelayClientNew relay) {

		Log.d("TEST", "Received " + rtpp.getLength());
		ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
		try {
			byte[] received = rtpp.packet;

			byte[] buffer = rtpp.getPacket();
			arrayStream.write(buffer, 0, rtpp.getLength());

		} catch (Exception e) {
			e.printStackTrace();
		}
		byte fullFrame[] = arrayStream.toByteArray();

		byte header[] = Arrays.copyOfRange(fullFrame, 0, 12);

		byte[] withoutHeader = Arrays.copyOfRange(fullFrame, 12,
				fullFrame.length);

		Log.d("TEST", "MSG fullframe " + fullFrame.length);
		Log.d("TEST", "MSG header " + header.length);
		Log.d("TEST", "MSG withoutheader " + withoutHeader.length);

		// Log.d("TEST", "MSG "+frame);
		String convertString = new String(withoutHeader);
		Log.d("TEST", "MSG " + convertString);

		String specialMessage = convertString.substring(0,
				convertString.indexOf("#") + 1);
		byte specialmessagebytes[] = specialMessage.getBytes();

		Log.d("TEST", "Special message " + specialMessage);

		Log.d("TEST", "Special message length " + specialmessagebytes.length);

		byte[] videoByte = Arrays.copyOfRange(withoutHeader,
				specialmessagebytes.length, withoutHeader.length);

		Log.d("TEST", "Svideo byte " + videoByte.length);

		byte[] finalbyte = new byte[12 + videoByte.length];
		System.arraycopy(header, 0, finalbyte, 0, header.length);
		System.arraycopy(videoByte, 0, finalbyte, header.length,
				videoByte.length);

		Log.d("TEST", "Special final sending byte length------------> "
				+ finalbyte.length);

		try {
			relay.getRtpEngine()
					.getRtpSocket()
					.sendByte(
							finalbyte,
							InetAddress.getByName(communicationEngine
									.getRelayServerip()),
							communicationEngine.getRelayServerPort());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String specialMessage=convertString.sub

		// return frame;
	}

	private void sendRelayDummyPackets(String callType, String sessionId,
			String buddy, CallsOverInternet callsOverInternet) {
		Log.d("TEST", "Came to sendrelat dummy packets");
		// String callType=sb.getCallType();
		if (callType.equalsIgnoreCase("AP") || callType.equalsIgnoreCase("VP")
				|| callType.equalsIgnoreCase("VBC")
				|| callType.equalsIgnoreCase("ABC")
				|| callType.equalsIgnoreCase("SS")) {

			if (callTable.containsKey(sessionId)) {
				Log.d("TEST", "Call table contains session ");
				Object object = callTable.get(sessionId);
				Log.d("TEST", "Is instance of relayclientnew ");
				if (object instanceof RelayClientNew) {
					Log.d("TEST", "Came to if");
					Log.d("TEST",
							"is buddy nat" + callsOverInternet.isBuddyInNat());
					Log.d("TEST",
							"IsJoinRelay" + callsOverInternet.isJoinedRelay());
					if (callsOverInternet.isBuddyInNat()
							&& callsOverInternet.isJoinedRelay()) {
						Log.d("TEST", "Came to if 1");
						RelayClientNew relay = (RelayClientNew) object;
						relay.startDummyPacketTimer(
								communicationEngine.getLoginUsername(),
								sessionId, buddy);

					} else {
						if (callType.equalsIgnoreCase("VBC")
								|| callType.equalsIgnoreCase("ABC")
								|| callType.equalsIgnoreCase("SS")
								&& callsOverInternet.isJoinedRelay()) {
							Log.d("TEST", "Came to if 2");
							RelayClientNew relay = (RelayClientNew) object;
							relay.startDummyPacketTimer(
									communicationEngine.getLoginUsername(),
									sessionId, buddy);
						}
					}

				}
			}
		}

	}

	/**
	 * Used to maintain Signal on appropriate Time.
	 * 
	 * @param sb
	 *            Received Signal information.
	 * @return Status
	 */
	private boolean checkandAllow(SignalingBean sb) {
		if (!sb.getType().equals("1")) {
			return true;
		} else if (communicationEngine.maintainSignallingOnTime.containsKey(sb
				.getSessionid() + sb.getFrom())) {
			// communicationEngine.maintainSignallingOnTime.remove(key)
			return true;
		} else {
			return false;
		}

		// return false;

	}

	/**
	 * Used to Notify the Incoming Signal Information to the UI.Also used for
	 * Initializing and Closes service related Information. Also used to Clear
	 * all the Resource related to Service.
	 */
	@Override
	public void notifyUDPPackets(byte[] data, String sourceIp, int sourcePort) {

        try {
            byte[] udpData = null;
            try {
                udpData = AESCrypto.decrypt(seceretKey, data);
            }
            //Narayanan 13th feb
//            catch (IllegalBlockSizeException e1) {
//                udpData = data;
//            }
            catch (Exception e1) {
                //Narayanan 13th feb
                udpData = data;
//                e1.printStackTrace();
            }
            AppReference.getLogger().debug(
                    "UDP_SIGNAL" + "Received Signal :" + new String(udpData));
            Log.d("UDP_SIGNAL", "Received SIGNAL : " + new String(udpData)
                    + "FROM IP : " + sourceIp + "FROM PORT : " + sourcePort);

			SingleInstance.mainContext.setLogWriterForCall("Received SIGNAL : "
					+ new String(udpData) + "FROM IP : " + sourceIp
					+ "FROM PORT : " + sourcePort);

			// if (AppReference.isWriteInFile)
			// AppReference.getLogger().debug(
			// "CALLINFO : " + "Received SIGNAL : "
			// + new String(udpData) + "FROM IP : " + sourceIp
			// + "FROM PORT : " + sourcePort);

			SignalingBean sb = null;
			String ReceivedData = new String(udpData);
			if (ReceivedData.contains("type=\"21\"")
					|| ReceivedData.contains("type=\"100\"")
					|| ReceivedData.contains("type=\"104\"")) {
				UdpMessageBean udpMessage = xmlParser
						.parseUdpMessageBean(ReceivedData);
				dispatchCallEvents(udpMessage);

			}

            else if (ReceivedData.contains("type=\"20\"")) {
				Log.d("PORTREFERESH", "Type 20 key 1 response");
				KeepAliveReceiver.isGotKeyOneResponse = true;
				// stopKeyOneMessage();
			} else {
				sb = xmlParser.parseSignal(new String(udpData));

				if (cancelRetransmission(sb.getSignalid(), sb.getType(),
						sb.getSessionid(), sb.getCallType(), sb)) {

					if(sb.getType().equals("0") && sb.getVideopromote() != null && sb.getVideopromote().equalsIgnoreCase("yes")){

						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");

						if (objCallScreen != null) {

							if (objCallScreen instanceof AudioCallScreen) {
								AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
								acalObj.notifyCallvIDEOPromotionRequest(sb);
							}
						}

						if (callTable.containsKey(sb.getSessionid()
								+ sb.getFrom())) {

							CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
									.get(sb.getSessionid() + sb.getFrom());
							RtpEngine engine = callsOverInternet
									.getRtpEngine();
							// Log.d("REP", "from prop1");
//							engine.addRemoteEndpoint(callsOverInternet
//											.getAudiossrc(),
//									sb.getBuddyConnectip(), Integer
//											.parseInt(sb
//													.getBuddyConnectport()));

//							if (sb.getCallType().equals("VC")) {
//								engine.addRemoteEndpoint(callsOverInternet
//										.getVideossrc(), sb
//										.getBuddyConnectip(), Integer
//										.parseInt(sb.getBuddyConnectport()));
//								// Log.d("REP", "from prop2");
//
//								communicationEngine.showBuddyVideo(
//										sb.getFrom(), sb.getSessionid());
//
//								engine.setBuddyVideoSsrc(Integer
//										.parseInt(sb.getVideossrc()));
//
//								engine.setMyVideoSsrc(callsOverInternet
//										.getVideossrc());
//
//							}
//							// communicationEngine.startAudioCapture();
//							if (!sb.getCallType().equals("ABC")
//									&& !sb.getCallType().equals("VBC")
//									&& !sb.getCallType().equals("VP")
//									&& !sb.getCallType().equals("AP")) {
//								if (!sb.getCallType().equals("MTP")
//										&& !sb.getCallType().equals("MPP")
//										&& !sb.getCallType().equals("MAP")
//										&& !sb.getCallType().equals("MVP")
//										&& !sb.getCallType().equals("MHP")) {
//									if (sb.getCallType().equals("ABC")
//											|| CallDispatcher.sb
//											.getCallType()
//											.equalsIgnoreCase("VBC")
//											|| CallDispatcher.sb
//											.getCallType()
//											.equalsIgnoreCase("AP")
//											|| CallDispatcher.sb
//											.getCallType()
//											.equalsIgnoreCase("VP")) {
//										communicationEngine
//												.startAudioCapture(
//														sb.getSignalid(),
//														sb.getStorageWarningLevel());
//									} else {
//										communicationEngine
//												.startAudioCapture(
//														sb.getFrom()
//																+ "_"
//																+ sb.getSessionid(),
//														sb.getStorageWarningLevel());
//									}
//								}
//							} else {
//								// Log.d("SDPP",
//								// "Started sending dummy packets");
//								engine.sendDummyPackets(
//										sb.getBuddyConnectip(),
//										callsOverInternet.getAudiossrc());
//							}
//							stopCallTimer(sb.getSignalid());
							callsOverInternet.startTimer();

							// Implemented for port acanning.....
//							if (sb.getPunchingmode().equals("0")) {
//
//							} else if (sb.getPunchingmode().equals("3")) {
//								// Tring to do port logic prediction.....
//								callsOverInternet.setSymmetric(true);
//								// callsOverInternet.se
//								engine.setSymmetric(true);
//								engine.setHaveToSetPort(true);
//								// s
//								// Log.d("PORTSCAN",
//								// "processing port scanning...&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//								communicationEngine.doPortScanning(
//										callsOverInternet,
//										callsOverInternet
//												.getBuddyConnectip(),
//										callsOverInternet
//												.getBuddyConnectPort());
//
//							}
							// /

						}

						return;
					} else if(sb.getType().equals("0") && sb.getVideoStoped() != null && (sb.getVideoStoped().equalsIgnoreCase("yes") || sb.getVideoStoped().equalsIgnoreCase("no"))){
						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");

						if (objCallScreen != null) {

							if (objCallScreen instanceof AudioCallScreen) {
								AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
								acalObj.notifyVideoStoped(sb);
							} else if (objCallScreen instanceof VideoCallScreen) {
								VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
								acalObj.notifyVideoStoped(sb);
							}
						}
						return;
					}

					if (sb.getType().equals("4")) {
						if (sb.getCallType() == null) {
							sb.setCallType("");
						}
						Log.d("REQUEST",
								"notify Udp packet from propSignalling "
										+ new String(udpData));
					}

                    if(sb.getType().equals("101")){
                        Log.d("abcdef","IN 101 signal "+sb.getSignalid());
                        GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
                                .get("groupchat");
						if(sb.getSignalid()!=null)
                        gcActivity.updateThumbs(sb.getSignalid());
                    }

					if (sb.getType().equals("13")) {

						String ip = sourceIp;
						String finalIp = ip.substring(1, ip.length());
						// Log.d("SIGNAL", "IPPPPPPFinal "+finalIp);
						sb.setLocalip(finalIp);
						sb.setSignalingPort(Integer.toString(sourcePort));

					}

					if (sb.getType().equals("13") || sb.getType().equals("14")) {
						if (sb.getCallType() == null) {
							// Log.d("SIGNAL", "Set call type ");
							sb.setCallType("");
						}
					}

					// System.out.println("DispatchCallEvents : For "+sb.getType()+" Signal "+sb.getSignalid());
					if (!sb.getType().equals("12")) {
						Log.d("udp", "nottype 12 udp packets ");
						dispatchCallEvents(sb);
					} else {
						// used to send notification of message delivered
						Log.d("type12",
								"Receive type 12 and call dispatch events");
						dispatchCallEvents(sb);
					}

					if (sb.getType().equals("2")) {

						Log.d("TEST", "Received signal " + new String(udpData));

						// System.out.println("callTable :"+callTable);
						// System.out.println("sb.getSessionid()+sb.getFrom() :"+sb.getSessionid()+sb.getFrom());

						if (!sb.getBuddyConnectip().equals(
								communicationEngine.getRelayServerip())
								&& !sb.getBuddyConnectport().equals(
										Integer.toString(communicationEngine
												.getRelayServerPort()))) {

							Log.d("TEST", "isRelay on receive false");
							if (callTable.containsKey(sb.getSessionid()
									+ sb.getFrom())) {

								CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
										.get(sb.getSessionid() + sb.getFrom());
								RtpEngine engine = callsOverInternet
										.getRtpEngine();
								// Log.d("REP", "from prop1");
								engine.addRemoteEndpoint(callsOverInternet
										.getAudiossrc(),
										sb.getBuddyConnectip(), Integer
												.parseInt(sb
														.getBuddyConnectport()));
								// Log.d("REP", "from prop1");
								callsOverInternet.setBuddyConnectip(sb
										.getBuddyConnectip());
								callsOverInternet.setBuddyConnectPort(Integer
										.parseInt(sb.getBuddyConnectport()));
								callsOverInternet.setBuddyAudioSSRC(Integer
										.parseInt(sb.getAudiossrc()));
								callsOverInternet.setBuddyVideoSSRC(Integer
										.parseInt(sb.getVideossrc()));
								// callsOverInternet.startAudioPlayer();

								engine.setBuddyAudioSsrc(Integer.parseInt(sb
										.getAudiossrc()));
								engine.setMyAudioSsrc(callsOverInternet
										.getAudiossrc());
								if (sb.getCallType().equals("VC")
										|| sb.getCallType().equals("VBC")
										|| sb.getCallType().equals("VP")
										|| sb.getCallType().equalsIgnoreCase(
												"SS")) {
									// Log.d("REP", "from prop2");
									engine.addRemoteEndpoint(callsOverInternet
											.getVideossrc(), sb
											.getBuddyConnectip(), Integer
											.parseInt(sb.getBuddyConnectport()));
									// Log.d("REP", "from prop2");

									communicationEngine.showBuddyVideo(
											sb.getFrom(), sb.getSessionid());

									engine.setBuddyVideoSsrc(Integer
											.parseInt(sb.getVideossrc()));

									engine.setMyVideoSsrc(callsOverInternet
											.getVideossrc());

								}
								// communicationEngine.startAudioCapture();
								if (!sb.getCallType().equals("ABC")
										&& !sb.getCallType().equals("VBC")
										&& !sb.getCallType().equals("VP")
										&& !sb.getCallType().equals("AP")) {
									if (!sb.getCallType().equals("MTP")
											&& !sb.getCallType().equals("MPP")
											&& !sb.getCallType().equals("MAP")
											&& !sb.getCallType().equals("MVP")
											&& !sb.getCallType().equals("MHP")) {
										if (sb.getCallType().equals("ABC")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("VBC")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("AP")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("VP")) {
											communicationEngine
													.startAudioCapture(
															sb.getSignalid(),
															sb.getStorageWarningLevel());
										} else {
											communicationEngine
													.startAudioCapture(
															sb.getFrom()
																	+ "_"
																	+ sb.getSessionid(),
															sb.getStorageWarningLevel());
										}
									}
								} else {
									// Log.d("SDPP",
									// "Started sending dummy packets");
									engine.sendDummyPackets(
											sb.getBuddyConnectip(),
											callsOverInternet.getAudiossrc());
								}
								stopCallTimer(sb.getSignalid());
								callsOverInternet.startTimer();

								// Implemented for port acanning.....
								if (sb.getPunchingmode().equals("0")) {

								} else if (sb.getPunchingmode().equals("3")) {
									// Tring to do port logic prediction.....
									callsOverInternet.setSymmetric(true);
									// callsOverInternet.se
									engine.setSymmetric(true);
									engine.setHaveToSetPort(true);
									// s
									// Log.d("PORTSCAN",
									// "processing port scanning...&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
									communicationEngine.doPortScanning(
											callsOverInternet,
											callsOverInternet
													.getBuddyConnectip(),
											callsOverInternet
													.getBuddyConnectPort());

								}
								// /

							}

						} else {
							Log.d("TEST", "isRelay on receive true");
							// To process Relay

							if (callTable.containsKey(sb.getSessionid()
									+ sb.getFrom())) {
								Log.d("TEST", "is call Table contains");
								// System.out.println("VVVVVVVVVVVVV 121"+sb.getBuddyConnectip());
								CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
										.get(sb.getSessionid() + sb.getFrom());

								callsOverInternet.setBuddyConnectip(sb
										.getBuddyConnectip());
								callsOverInternet.setBuddyConnectPort(Integer
										.parseInt(sb.getBuddyConnectport()));
								callsOverInternet.setBuddyAudioSSRC(Integer
										.parseInt(sb.getAudiossrc()));
								callsOverInternet.setBuddyVideoSSRC(Integer
										.parseInt(sb.getVideossrc()));
								// callsOverInternet.setJoinedRelay(true);
								communicationEngine.joinRelaynew(
										sb.getSessionid(), sb.getFrom(),
										Long.valueOf(sb.getAudiossrc()),
										Long.valueOf(sb.getVideossrc()),
										sb.getSessionid() + sb.getFrom(),
										sb.getCallType(), true);
								callsOverInternet.setJoinedRelay(true);
								if (sb.getCallType().equals("VC")
										|| sb.getCallType().equals("VBC")
										|| sb.getCallType().equals("VP")
										|| sb.getCallType().equalsIgnoreCase(
												"SS")) {
									// engine.addRemoteEndpoint(callsOverInternet
									// .getVideossrc(), sb.getBuddyConnectip(),
									// Integer.parseInt(sb.getBuddyConnectport()));

									communicationEngine.showBuddyVideo(
											sb.getFrom(), sb.getSessionid());

									RelayClientNew relayNew = (RelayClientNew) callTable
											.get(sb.getSessionid());
									if (relayNew != null) {
										relayNew.setVideoSsrc(Long.valueOf(sb
												.getVideossrc()));
										relayNew.setBuddyName(callsOverInternet
												.getBuddyName());

									}

								}
								// communicationEngine.startAudioCapture();
								if (!sb.getCallType().equals("ABC")
										&& !sb.getCallType().equals("VBC")
										&& !sb.getCallType().equals("VP")
										&& !sb.getCallType().equals("AP")
										&& !sb.getCallType().equalsIgnoreCase(
												"SS")) {
									if (!sb.getCallType().equals("MTP")
											&& !sb.getCallType().equals("MPP")
											&& !sb.getCallType().equals("MAP")
											&& !sb.getCallType().equals("MVP")
											&& !sb.getCallType().equals("MHP")) {
										if (sb.getCallType().equals("ABC")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("VBC")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("AP")
												|| CallDispatcher.sb
														.getCallType()
														.equalsIgnoreCase("VP")) {
											communicationEngine
													.startAudioCapture(
															sb.getSignalid(),
															sb.getStorageWarningLevel());
										} else {
											communicationEngine
													.startAudioCapture(
															sb.getFrom()
																	+ "_"
																	+ sb.getSessionid(),
															sb.getStorageWarningLevel());
										}
									}
								} else {
									// Log.d("SDPP",
									// "Started sending dummy packets");

								}
								stopCallTimer(sb.getSignalid());
								callsOverInternet.startTimer();

								sendRelayDummyPackets(sb.getCallType(),
										sb.getSessionid(), sb.getFrom(),
										callsOverInternet);

							} else
								Log.d("TEST", " call Table not contains");

						}

					} else if (sb.getType().equals("3")) {
						CommunicationEngine.callState.put(sb.getSignalid(),
								sb.getType());
						// used to Cancel Receiver TImer1 ....
						stopCallTimer(sb.getSignalid());

						try {

							if (communicationEngine.maintainSignallingOnTime
									.containsKey(sb.getSessionid()
											+ sb.getFrom())) {
								communicationEngine.maintainSignallingOnTime
										.remove(sb.getSessionid()
												+ sb.getFrom());
							}

							if (callTable.containsKey(sb.getSessionid()
									+ sb.getFrom())) {
								CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
										.get(sb.getSessionid() + sb.getFrom());
								callsOverInternet.getRtpEngine()
										.stopRTPEngine();
								callsOverInternet.stopAudioPlayer();
								callsOverInternet.setRelayRunning(false);
								callsOverInternet.setRelayRunning(false);
								// used to remove Audio SSRC and Video SSRC from
								// RelaClient.....
								// Newly added...
								if (callsOverInternet.isJoinedRelay()) {
									RelayClientNew relay = (RelayClientNew) communicationEngine
											.getCallTable().get(
													sb.getSessionid());
									relay.removeSSRC(Long
											.valueOf(callsOverInternet
													.getBuddyAudioSSRC()), Long
											.valueOf(callsOverInternet
													.getBuddyVideoSSRC()));
									relay.stopDummyPAcketTimer(sb.getFrom()
											+ sb.getSessionid());
								}

								communicationEngine
										.resetVpxDecoderIndex(callsOverInternet
												.getDecIndex());

								if (callsOverInternet.getFrameTimer() != null) {
									Log.d("FTR", "Key "
											+ "Cancel Associated Timers");

									callsOverInternet.getFrameTimer().cancel();
								}

								callTable.remove(sb.getSessionid()
										+ sb.getFrom());
								ArrayList<SignalingBean> list = communicationEngine
										.getConferenceTable().get(
												sb.getSessionid());

								Log.d("MSG", "before for " + list.size());
								for (int i = 0; i < list.size(); i++) {
									SignalingBean sg = list.get(i);

									if (sg.getTo().equals(sb.getFrom())) {
										list.remove(i);
										// System.out.println("sg.getTo() : " +
										// sg.getTo()
										// + " sb.getFrom() : " +
										// sb.getFrom()+","+list.size());
									}
								}
								communicationEngine.removeMediaKey(
										sb.getSessionid(), sb.getFrom());

								Log.d("call",
										new Date() + "after for " + list.size());
								if (list.size() == 0) {

									communicationEngine.micMute(false);
									System.out
											.println("Communication removemediakeu 3");
									communicationEngine.stopAudioCapture();
									System.out
											.println("Communication removemediakeu 4");
									communicationEngine.clean();
									System.out
											.println("Communication removemediakeu 5");
									// CommunicationEngine.relayJoined = false;
									System.out
											.println("Communication removemediakeu");
								} else {
									if (!sb.getCallType().equals("AP")
											&& !sb.getCallType().equals("VP")
											&& !sb.getCallType().equals("ABC")
											&& !sb.getCallType().equals("VBC")
											&& !sb.getCallType().equals("SS")) {
										communicationEngine.resetAEC();
									}
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					} else if (sb.getType().equals("7")) {
						Log.d("MC", " Type 7  Received...");
						// System.out
						// .println("########################## TYPE 7  RECEIVED #############################33");
						// Log.d("1211",
						// "From "+sb.getFrom()+"  to "+sb.getTo()+" Type "+sb.getType()+" signalid "+sb.getSignalid());
						communicationEngine.startNewConferenceCall(sb);

					} else if (sb.getType().equals("6")) { // Log.d("666",
															// "Received SSrc on Type 666 ");

						if (communicationEngine.getCallTable().containsKey(
								sb.getSessionid() + sb.getFrom())) {

							try {
								CallsOverInternet overInternet = (CallsOverInternet) communicationEngine
										.getCallTable().get(
												sb.getSessionid()
														+ sb.getFrom());
								// Log.d("SM",
								// "Receiving Type6 for "+overInternet.getBuddyName());
								// Log.d("666",
								// "Received SSrc on Type 666 111111111111111");
								overInternet.setAllowMedia(false);
								overInternet.setAllowAudio(false);
								communicationEngine.joinRelayOnTypeSix(sb
										.getSessionid(), sb.getFrom(), Long
										.valueOf(overInternet
												.getBuddyAudioSSRC()), Long
										.valueOf(overInternet
												.getBuddyVideoSSRC()),
										sb.getSessionid() + sb.getFrom(), sb
												.getCallType());
								overInternet.setJoinedRelay(true);

								if (sb.getCallType().equalsIgnoreCase("VC")
										|| sb.getCallType().equalsIgnoreCase(
												"VBC")
										|| sb.getCallType().equalsIgnoreCase(
												"VP")
										|| sb.getCallType().equals("SS")) {
									// used to send relay video Receive
									// message...
									long videoSSRC = communicationEngine
											.getDecodessrc();
									// sendVideoRequest(videoSSRC);
									// Log.d("666",
									// "Received SSrc on Type 666 "+videoSSRC);
									// Log.d("666",
									// "Stored SSrc on Type 666 "+overInternet.getVideossrc());
									// Log.d("666",
									// "Stored SSrc on Type 666 "+overInternet.getBuddyVideoSSRC());
									if (videoSSRC == overInternet
											.getBuddyVideoSSRC()) {
										// Log.d("666", "Matches ...");
										//
										RelayClientNew relayNew = (RelayClientNew) callTable
												.get(sb.getSessionid());
										if (relayNew != null) {
											relayNew.setVideoSsrc(videoSSRC);
											relayNew.setBuddyName(overInternet
													.getBuddyName());
											relayNew.sendRelayVideoReceiveMessage(
													overInternet.getSessionid(),
													communicationEngine
															.getLoginUsername(),
													overInternet.getBuddyName());

										}
										//

									}

								}

							} catch (Exception e) {
								// Log.d("SM",
								// "Receiving Type6 for "+e.toString());
								e.printStackTrace();
							}
							// Log.d("SM", "Received and Process Type6");

						}
					} else if (sb.getType().equals("9")) {
						// System.out
						// .println("############## TYPE 9 RECEIVED #############");

						// System.out.println("sb.getCallType() :" +
						// sb.getCallType());

						if (sb.getCallType().equals("VC")) {

							if (communicationEngine.getCallTable().containsKey(
									sb.getSessionid() + sb.getFrom())) {
								CallsOverInternet overInternet = (CallsOverInternet) communicationEngine
										.getCallTable().get(
												sb.getSessionid()
														+ sb.getFrom());
								// System.out.println("sb.getTo() :" +
								// sb.getTo());
								// System.out.println("loginUser :"
								// + communicationEngine.getLoginUsername());

								if (sb.getGmember().equals(
										communicationEngine.getLoginUsername())) {

									if (overInternet.isSendingMediaToRelay()) {

									} else {
										overInternet.setAllowMedia(true);

										overInternet.setAllowAudio(true);
									}

									// System.out.println("Video will be  allowed to "
									// + sb.getFrom());
								} else {
									overInternet.setAllowMedia(false);
									overInternet.setAllowAudio(true);
									// System.out.println("Video will not be  allowed to "
									// + sb.getFrom());
								}

							}
						}

					}
				}

				else if (sb.getType().equals("11")) {
					// Used to send Type 12 message when receive Type 11....No
					// restriction
					SignalingBean sbToSend = (SignalingBean) sb.clone();

					//
					String from = sb.getFrom();
					String to = sb.getTo();
					sbToSend.setFrom(to);
					sbToSend.setTo(from);
					sbToSend.setType("12");
					sbToSend.setTolocalip(sb.getLocalip());
					sbToSend.setTopublicip(sb.getPublicip());
					sbToSend.setPublicip(communicationEngine
							.getPublicInetaddress());
					sbToSend.setLocalip(communicationEngine
							.getLocalInetaddress());
					sbToSend.setToSignalPort(sb.getSignalport());
					sbToSend.setSignalingPort(Integer.toString(portno));
					sbToSend.setMessage("");

					sendMessage(sbToSend);

				}

				//
			}
            if (ReceivedData.contains("type=\"101\"")) {
                GroupChatBean gcBean = new GroupChatBean();
                UdpMessageBean udpMessage = xmlParser
                        .parseUdpMessageBean(ReceivedData);
                gcBean = (GroupChatBean) udpMessage.getResponseObject();
                if (ReceivedData.contains("result=\"2\"")) {
                    gcBean.setSent("2");
                    SingleInstance.mainContext.notifySentreceived(gcBean);

                } else if (ReceivedData.contains("result=\"3\"")) {
                    gcBean.setSent("3");
                    SingleInstance.mainContext.notifySentreceived(gcBean);
                }
            }
            if (ReceivedData.contains("type=\"105\"")) {
                if (ReceivedData.contains("result=\"0\"")) {
                    GroupChatBean gcBean = new GroupChatBean();
                    UdpMessageBean udpMessage = xmlParser
                            .parseUdpMessageBean(ReceivedData);
                    gcBean = (GroupChatBean) udpMessage.getResponseObject();
                    SingleInstance.mainContext.notifytextTyping(gcBean);
                }else
                    if (ReceivedData.contains("result=\"1\"")) {
                        GroupChatBean gcBean = new GroupChatBean();
                        UdpMessageBean udpMessage = xmlParser
                                .parseUdpMessageBean(ReceivedData);
                        gcBean = (GroupChatBean) udpMessage.getResponseObject();
                        SingleInstance.mainContext.notifytextTyping(gcBean);
                    }

            }

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("REQUEST", "Notify UDP packet" + e);
		}
	}

	Handler mHandler = new Handler();
	Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			Log.i("alarm123","COMES INTO HANDLER");
			Intent intent = new Intent(SingleInstance.mainContext,
					KeepAliveReceiver.class);
			SingleInstance.mainContext.startActivity(intent);
			mHandler.postDelayed(mStatusChecker, mInterval);
		}
	};
	void startRepeatingTask() {
		Log.i("alarm123","COMES INTO HANDLER startRepeatingTask ");
		mStatusChecker.run();
	}

	void stopRepeatingTask() {
		mHandler.removeCallbacks(mStatusChecker);
	}

	// Started

	// Closed

}
