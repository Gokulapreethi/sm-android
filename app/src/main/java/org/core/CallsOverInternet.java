package org.core;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import org.audio.AudioPlayer;
import org.lib.model.SignalingBean;
import org.lib.xml.XmlComposer;
import org.net.rtp.MediaFrameListener;
import org.net.rtp.RtpEngine;
import org.net.rtp.SymmetricNatListener;
import org.util.AudioCodecFormat;
import org.util.Queue;
import org.util.Utility;

import com.cg.commonclass.CallDispatcher;

import android.util.Log;
import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;

/**
 * 
 * This HOLE_PUNCHING_STATE enum defines constants for relay join and udp hole
 * punch.
 * 
 */
enum HOLE_PUNCHING_STATE {
	HOLE_PUNCHING_IDLE, HOLE_PUNCHING_SERVER1_WAIT_RESPONSE, HOLE_PUNCHING_SERVER2_WAIT_RESPONSE, HOLE_PUNCHING_PORT_SCANNING, HOLE_PUNCHING_SUCCESS_MEDIA_JOIN, HOLE_PUNCHING_SUCCESS_JOIN, DEFAULT
};

/**
 * This class manages the communication session between two end points.<br/>
 * This class also contains UDP Hole Punching and stun to traverse devices
 * behind firewall and NAT All the media stream will be callbacked to this class
 * to notifyMediaFrame where streams are identified.<br/>
 * Whenever P2P failed relay module will be triggered by this class to achieve
 * media through relay
 * 
 */
public class CallsOverInternet implements MediaFrameListener, RelayInterface,
		SymmetricNatListener {
	/**
	 * CbServer1 Ip.Initially null.
	 */
	private String cbserver1 = null;
	/**
	 * CbServer2 Ip.Initially null.
	 */
	private String cbserver2 = null;

	private boolean isBuddyInNat = false;

	HashMap<String, Timer> mapType13Timer = new HashMap<String, Timer>();

	HashMap<String, Type13Timer> mapType13TimerToReset = new HashMap<String, Type13Timer>();
	SignalingBean sbType13 = null;

	private boolean isSymmetric = false;

	// private boolean haveToSetPort=false;
	//
	//
	//
	//
	//
	//
	// public boolean isHaveToSetPort() {
	// return haveToSetPort;
	// }
	//
	// public void setHaveToSetPort(boolean haveToSetPort) {
	// this.haveToSetPort = haveToSetPort;
	// }

	public boolean isSymmetric() {
		return isSymmetric;
	}

	public void setSymmetric(boolean isSymmetric) {
		this.isSymmetric = isSymmetric;
	}

	/**
	 * cbServer1 Port.
	 */
	private int cbport1 = 0;
	/**
	 * cbServer2 Port.
	 */
	private int cbport2 = 0;
	/**
	 * This class is used to send and Receive UDP signal. This class implement
	 * UDPDataListener which is used to receive UDP Signal.
	 */
	private ProprietarySignalling proprietarySignalling = null;
	/**
	 * Communication Engine is the core class which provides various
	 * functionality to establish,make and control session .This class
	 * initialize the video and audio codec's,start audio recording,received
	 * video and audio callback.
	 */
	private CommunicationEngine communicationEngine = null;
	/** Initially holePunchstate will be set to HOLE_PUNCHING_IDLE state **/
	private HOLE_PUNCHING_STATE holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_IDLE;
	/**
	 * This bean class is used to set and get data's Related to Signaling.
	 */
	private SignalingBean sb = null;
	/**
	 * This RtpEngine class provides actual end to end network media transport.
	 * This class uses UDP and RTP protocol to deliver packets across internet.
	 */
	private RtpEngine rtpEngine = null;
	/**
	 * Destination RTPPort.
	 */
	private int rtpPort;
	/**
	 * Used to generate Random Id for Application like SessionId,SignalId etc.
	 */
	private Utility utility = new Utility();
	/**
	 * Timers schedule one-shot or recurring tasks for execution. Prefer
	 * ScheduledThreadPoolExecutor for new code. Each timer has one thread on
	 * which tasks are executed sequentially. When this thread is busy running a
	 * task, runnable tasks may be subject to delays. One-shot are scheduled to
	 * run at an absolute time or after a relative delay.
	 */
	private Timer timer = null;
	/**
	 * Used to Check the Receive dMedia Packet.
	 */
	private Timer packetTimer = null;
	/**
	 * This class retransmit the message to respective remote end point for
	 * every 2 seconds and maximum of 3 attempt..
	 */
	private HolePunchRetransmissionTimer retransmission = null;
	/**
	 * Router Ip Address.
	 */
	private String routerip = null;
	private String key = null;
	/**
	 * Check whether the Media Packet is sending ThroughRelay.
	 */
	private boolean sendingMediaToRelay = false;
	public static long audiossrc;
	/** < AudioScrc key used for signaling. */
	public static long videossrc;
	/** < VideoSsrc key used for signaling. */

	/**
	 * Connect Port of Buddy.
	 */
	private int buddyConnectPort;
	/**
	 * Audio Ssrc of Buddy.
	 */
	private int buddyAudioSSRC;
	/**
	 * Video Ssrc of Buddy.
	 */
	private int buddyVideoSSRC;
	/**
	 * Relay Server Port.
	 */
	private int relayServerPort = 0;
	/**
	 * Decoder Index.
	 */
	private int decIndex;
	/**
	 * Check whether the Media Packet is Receiving ThroughRelay.
	 */
	private boolean receiveMediaRelay = false;

	/**
	 * Check Call Type like AC,VC etc.
	 */
	private String callType = null;
	/**
	 * Connect IPAddress of Buddy.
	 */
	private String buddyConnectip = null;

	/**
	 * Audio Player decodes and play a audio streams. Audio Player settings
	 * would be mono_channel configuration,PCM 16 bit encoding and sample rate
	 * in 8KHZ Audio player operates under streaming mode.Supports two codec
	 * speex and G711 for decoding the streams. For speex parallel decoding
	 * slots can be used.
	 */
	private AudioPlayer audioPlayer = null;
	/**
	 * Used to store and retrieve Audio Data. Queue is used to insert and
	 * Retrieve Object. It is used as FIFO(First-In-First-Out) logic.
	 * java.util.LinkedList is used here for insert and Retrieve operations.
	 */
	private Queue playerQueue = null;
	/**
	 * Used to store and retrieve Video Data. Queue is used to insert and
	 * Retrieve Object. It is used as FIFO(First-In-First-Out) logic.
	 * java.util.LinkedList is used here for insert and Retrieve operations.
	 */
	private Queue videoQueue = null;
	private Timer mediaReceiveTimer = null;

	/**
	 * Relay Server IP Address.
	 */
	private String relayServerip = null;
	private VideoThread videoThread = null;
	private Thread relayMessageThread = null;
	private Thread relayMediaJoinThread = null;
	/**
	 * Used to check Relay is running or not.
	 */
	private boolean relayRunning = true;
	/**
	 * Used to check RelayMediaJoin is running or not.
	 */
	private boolean relayMediaJoinRunning = true;
	/**
	 * Used to check Relay is Joined or not.
	 */
	private boolean joinedRelay = false;
	/**
	 * Used to check Speaker Mute State.
	 */
	private boolean speakerMute = false;
	/**
	 * Used to Check Whether Media is Allow or Not.
	 */
	private boolean allowMedia = true;
	/**
	 * Used to check Media allow state.
	 * 
	 */
	private boolean allowAudio = true;// newly added
	/**
	 * Used to check Packet Receive state.
	 */
	private boolean packetReceive = false;
	/**
	 * used to maintain Session Id.
	 */
	private String sessionid = null;
	private FrameTimer frameTimer = null;
	Timer relayTimer = null;
	RelaySwitchTimer relaySwitch = null;

	private long currentPacketTime;
	private boolean isOldVersion = false;

	/**
	 * Used to Ping STUN for NAT.
	 */
	private DiscoveryInfo di = null;

	static {
		Utility util = new Utility();
		audiossrc = util.getRandomMediaID();
		videossrc = util.getRandomMediaID();
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	public CallsOverInternet() {

	}

	public boolean isBuddyInNat() {
		return isBuddyInNat;
	}

	public void setBuddyInNat(boolean isBuddyInNat) {
		this.isBuddyInNat = isBuddyInNat;
	}

	public SignalingBean getSignallingBean() {
		return this.sb;
	}

	public boolean isOldVersion() {
		return isOldVersion;
	}

	public void setOldVersion(boolean isOldVersion) {
		this.isOldVersion = isOldVersion;
	}

	/**
	 * Overloaded class constructor CallsOverInternet initialized when both
	 * party agree to have a call session.The constructor will assign the
	 * reference to the last sent signalingbean obj,proprietarySignalling
	 * obj,communication engine and other server network details.
	 * 
	 * @param sb
	 *            reference to last sent signaling bean
	 * @param proprietarySignalling
	 *            reference to proprietarySignalling
	 * @param communicationEngine
	 *            reference to communicationEngine
	 * @param cbserver1
	 *            ipaddress of address agent server1
	 * @param cbserver2
	 *            ipaddress of address agent server2
	 * @param cbport1
	 *            port of address agent server1
	 * @param cbport2
	 *            port of address agent server2
	 * @param routerip
	 *            ipaddress of router to which signaling has to be sent
	 * @param relayPort
	 *            relay server port
	 * @param relayServerip
	 *            relay server ipaddress
	 */
	public CallsOverInternet(SignalingBean sb,
			ProprietarySignalling proprietarySignalling,
			CommunicationEngine communicationEngine, String cbserver1,
			String cbserver2, int cbport1, int cbport2, String routerip,
			int relayPort, String relayServerip) {
		// Log.d("FTR","Callsession constructor....");
		this.cbport1 = cbport1;
		this.cbport2 = cbport2;
		this.cbserver1 = cbserver1;
		this.cbserver2 = cbserver2;
		this.communicationEngine = communicationEngine;
		this.proprietarySignalling = proprietarySignalling;
		this.sb = sb;
		this.sessionid = sb.getSessionid();
		this.relayServerPort = relayPort;
		this.relayServerip = relayServerip;

		Log.i("call123", "############# public ip" + sb.getPublicip());
		Log.i("call123", "############# local ip" + sb.getLocalip());

		this.routerip = routerip;
		decIndex = communicationEngine.getVpxFreeDecoderIndex();
		communicationEngine.setVpxDecoderIndex(decIndex);
		// System.out.println("########## decIndex :"+decIndex);

		key = sb.getSessionid() + sb.getTo();

		if (rtpEngine == null) {
			try {
				rtpPort = utility.getPortNo();
				Log.e("RELAY", "rtpport " + rtpPort);
				if (onDifferentNetwork()) {
					try {
						stunReply();
					} catch (Exception e) {
						Log.i("call", "Calls over net exception" + e.toString());
						rtpPort = utility.getPortNo();
					}
				}
				rtpEngine = new RtpEngine(rtpPort);
				rtpEngine.registerFrameListener(this);
				rtpEngine.registerSymNatListener(this);
				rtpEngine.start();

			} catch (Exception e) {
				Log.i("call", "Calls over net exception111" + e.toString());
				e.printStackTrace();
			}
		}

	}

	/**
	 * Used to ping STUN for getting public port on NAT.
	 * 
	 * @return
	 * @throws Exception
	 */
	private String stunReply() throws Exception {

		Log.d("call", new Date() + "TRying for Stun");
		Log.d("call", new Date() + "TRying for Stun" + sb.getLocalip());
		String localIp = InetAddress.getLocalHost().getHostAddress();
		// System.out.println(localIp);

		InetAddress inet = InetAddress.getByName(sb.getLocalip());
		// int porttx=utility.getPortNo();
		Log.d("call", new Date() + "TRying for Stun" + rtpPort);
		Log.d("call", new Date() + "TRying for Stun" + inet);

		DiscoveryTest test = new DiscoveryTest(inet, rtpPort, cbserver1, 3478);
		Log.d("call", new Date() + "TRying for Stun  " + test);
		di = test.test();
		Log.d("call", new Date() + "after TRying for Stun");
		Log.d("call", new Date() + "TRying for Stun  " + di);
		// di.getPublicIP();
		Log.e("NAT", "Is full cone" + di.isFullCone());
		Log.e("NAT", "Is Open Access" + di.isOpenAccess());
		Log.e("NAT", "Is isPortRestrictedCone" + di.isPortRestrictedCone());
		Log.e("NAT", "Is Restricted cone" + di.isRestrictedCone());
		Log.e("NAT", "Is isSymmetric" + di.isSymmetric());
		Log.e("NAT", "Is isSymmetricUDPFirewall" + di.isSymmetricUDPFirewall());
		// System.out.println("The Result is "+test.test());
		// System.out.println(test.test());

		return "";
	}

	/**
	 * Checks whether a device is in same or different network.
	 * 
	 * @return if same network returns true or else false
	 */
	private boolean onDifferentNetwork() {
		if (sb.getPublicip() == null) {
			// Log.d("test",
			// "@@@@@@@@@@@@@@@@@@@@ on different network in constructor &&&&&&&&&&&&&&&&&");
			sb.setPublicip(sb.getLocalip());
		}

		if (!sb.getPublicip().equals(sb.getTopublicip())) {
			// if (!sb.getLocalip().equals("0.0.0.0")) {
			if (!sb.getLocalip().equals(sb.getPublicip())) {
				Log.d("call123", "different network true");
				return true;
			}
			Log.d("call123", "different network true 1");
			return true;
			// }
		}

		Log.d("call123", "different network false");

		return false;
	}

	/**
	 * Checks whether to allow audio or not
	 * 
	 * @return true if allow audio; false if otherwise
	 */
	public boolean isAllowAudio() {
		return allowAudio;
	}

	public void setAllowAudio(boolean allowAudio) {
		this.allowAudio = allowAudio;
	}

	/**
	 * send bye signal to respective remote endpoint and also callback to UI.
	 * This bye signal informs the Buddy to Close him from his Service list.
	 */
	public void sendBye() {
		// Log.d("h_bye", "in engine ");
		sb.setType("3");
		proprietarySignalling.sendMessage(sb);

		String from = sb.getFrom();
		String to = sb.getTo();
		SignalingBean sb1 = new SignalingBean();
		sb1.setFrom(to);
		sb1.setTo(from);
		sb1.setPacketFailure(true);
		sb1.setSessionid(sb.getSessionid());
		sb1.setSignalid(sb.getSignalid());
		sb1.setCallType(sb.getCallType());
		sb1.setType(sb.getType());

		proprietarySignalling.notifyUI(sb1);

		// System.out.println("sb.getSessionid()+sb.getTo() :"+sb.getSessionid()+sb.getFrom());
		communicationEngine.cleanBuddyResources(
				sb1.getSessionid() + sb1.getFrom(), sb1.getSessionid(),
				sb1.getFrom());
	}

	public void sendMediaNotification() {

		String from = sb.getFrom();
		String to = sb.getTo();
		SignalingBean sb1 = new SignalingBean();
		sb1.setFrom(to);
		sb1.setTo(from);
		// sb1.setPacketFailure(true);
		sb1.setSessionid(sb.getSessionid());
		sb1.setSignalid(sb.getSignalid());
		sb1.setCallType(sb.getCallType());
		sb1.setType("15");
		proprietarySignalling.notifyUI(sb1);

	}

	public void sendAcceptTimerBye() {
		// Log.d("h_bye", "in engine sendAcceptTimerBye()");
		String from = sb.getFrom();
		String to = sb.getTo();
		SignalingBean sb1 = new SignalingBean();
		sb1.setFrom(to);
		sb1.setTo(from);
		sb1.setPacketFailure(true);
		sb1.setSessionid(sb.getSessionid());
		sb1.setSignalid(sb.getSignalid());
		sb1.setCallType(sb.getCallType());
		sb1.setType("3");

		proprietarySignalling.notifyUI(sb1);

		// System.out.println("sb.getSessionid()+sb.getTo() :"+sb.getSessionid()+sb.getFrom());
		communicationEngine.cleanBuddyResources(
				sb1.getSessionid() + sb1.getFrom(), sb1.getSessionid(),
				sb1.getFrom());

	}

	/**
	 * Checks whether media is sending through relay
	 * 
	 * @return true if media is being sending through relay server; false if
	 *         otherwise
	 */
	public boolean isSendingMediaToRelay() {
		return sendingMediaToRelay;
	}

	public void setSendingMediaToRelay(boolean sendingMediaToRelay) {
		this.sendingMediaToRelay = sendingMediaToRelay;
	}

	/**
	 * Checks whether media is receiving through relay
	 * 
	 * @return true if media is receiving through relay server; false if
	 *         otherwise
	 */
	public boolean isReceiveMediaRelay() {
		return receiveMediaRelay;
	}

	/** This method used to make sure that media is receiving through relay **/
	public void setReceiveMediaRelay(boolean receiveMediaRelay) {
		this.receiveMediaRelay = receiveMediaRelay;
	}

	/**
	 * Decoder index used by this call session obj
	 * 
	 * @return decoder index
	 */
	public int getDecIndex() {
		return decIndex;
	}

	/**
	 * a queue which store the incoming video frames.
	 * 
	 * @param videoQueue
	 *            reference to videoQueue
	 */
	public void setVideoQueue(Queue videoQueue) {
		this.videoQueue = videoQueue;
	}

	// /** not used **/
	// public VideoThread getVideoThread() {
	// return videoThread;
	// }

	/**
	 * Checks whether to allow media.
	 * 
	 * @return true if media; false if otherwise
	 */
	public boolean isAllowMedia() {
		return allowMedia;
	}

	/**
	 * Sets the allowmedia to on or off. This can be used to control allowing
	 * media or not
	 * 
	 * @param allowmedia
	 *            set true to allow; false to off
	 */
	public void setAllowMedia(boolean allowMedia) {
		this.allowMedia = allowMedia;
	}

	/**
	 * Sessionid of active call session
	 * 
	 * @return call session id
	 */
	public String getSessionid() {
		return sb.getSessionid();
	}

	public String getBuddyName() {
		return sb.getTo();
	}

	// /** not used **/
	// public boolean isRelayRunning() {
	// return relayRunning;
	// }

	/**
	 * on set true relay is running; false relay is not running
	 * 
	 */
	public void setRelayRunning(boolean relayRunning) {
		this.relayRunning = relayRunning;
	}

	// /** not used **/
	// public boolean isRelayMediaJoinRunning() {
	// return relayMediaJoinRunning;
	// }

	/** on set true relay joined for media is running; false not running **/
	public void setRelayMediaJoinRunning(boolean relayMediaJoinRunning) {
		this.relayMediaJoinRunning = relayMediaJoinRunning;
	}

	/**
	 * Checks whether the speaker mute is on or off.
	 * 
	 * @return true if speaker mute is on, false if it's off
	 */
	public boolean isSpeakerMute() {
		return speakerMute;
	}

	/**
	 * Sets the speaker mute on or off.
	 * 
	 * @param speakerMute
	 *            on set true to mute the speaker; false to turn mute off
	 */
	public void setSpeakerMute(boolean speakerMute) {
		this.speakerMute = speakerMute;
	}

	/**
	 * buddy's audio ssrc
	 * 
	 * @return ssrc
	 */
	public int getBuddyAudioSSRC() {
		return buddyAudioSSRC;
	}

	/** set's buddy audio ssrc **/
	public void setBuddyAudioSSRC(int buddyAudioSSRC) {
		this.buddyAudioSSRC = buddyAudioSSRC;
	}

	/**
	 * buddy's video ssrc
	 * 
	 * @return
	 */
	public int getBuddyVideoSSRC() {
		return buddyVideoSSRC;
	}

	/** set's buddy video ssrc **/
	public void setBuddyVideoSSRC(int buddyVideoSSRC) {
		this.buddyVideoSSRC = buddyVideoSSRC;
	}

	/** return's ipaddress of buddy **/
	public String getBuddyConnectip() {
		return buddyConnectip;
	}

	/** set's ipaddress of buddy **/
	public void setBuddyConnectip(String buddyConnectip) {
		this.buddyConnectip = buddyConnectip;
	}

	/** return's buddy connect port **/
	public int getBuddyConnectPort() {
		return buddyConnectPort;
	}

	/** set buddy connect port **/
	public void setBuddyConnectPort(int buddyConnectPort) {
		this.buddyConnectPort = buddyConnectPort;
	}

	/**
	 * Used to get the RTPEngine Reference to send and Receive RTPPacket. This
	 * RtpEngine class provides actual end to end network media transport. This
	 * class uses UDP and RTP protocol to deliver packets across internet.
	 * 
	 * @return RTPEngine
	 */
	public RtpEngine getRtpEngine() {
		return rtpEngine;
	}

	/**
	 * * Used to set the RTPEngine Reference to send and Receive RTPPacket. This
	 * RtpEngine class provides actual end to end network media transport. This
	 * class uses UDP and RTP protocol to deliver packets across internet.
	 * 
	 * @param rtpEngine
	 */
	public void setRtpEngine(RtpEngine rtpEngine) {
		this.rtpEngine = rtpEngine;
	}

	/** get call session audio ssrc **/
	public long getAudiossrc() {
		return audiossrc;
	}

	/** set call session audio ssrc **/
	public void setAudiossrc(long audiossrc) {
		this.audiossrc = audiossrc;
	}

	/** get call session video ssrc **/
	public long getVideossrc() {
		return videossrc;
	}

	/** set call session video ssrc **/
	public void setVideossrc(long videossrc) {
		this.videossrc = videossrc;
	}

	/**
	 * This method used to determine whether a call is local or nat. If nat then
	 * udp hole punch called else continue a normal signaling flow.
	 */
	public void sendRequestToServerOne(boolean isRelay) {

		// Log.d("call",new Date()+"isRelay "+isRelay);
		// Log.d("call",new Date()+"Type "+sb.getType());
		try {

			Log.i("call", "came to sendreq1...." + isRelay);

			if (!isRelay) {

				// Log.d("test", "accept call from UI alert 7");

				// System.out.println("My Localip ip:"+sb.getLocalip()+"My Public ip:"+sb.getPublicip()+" To Public up :"+sb.getTopublicip()+" To localip :"+sb.getTolocalip());
				// Log.d("call","My Localip ip:"+sb.getLocalip()+"My Public ip:"+sb.getPublicip()+" To Public up :"+sb.getTopublicip()+" To localip :"+sb.getTolocalip());
				Log.i("call",
						"came to sendreq1 public ip...." + sb.getPublicip());
				if (sb.getPublicip() == null) {
					sb.setPublicip(sb.getLocalip());
				}

				Log.i("call",
						"came to sendreq1 public to ip...."
								+ sb.getTopublicip());
				if (!sb.getPublicip().equals(sb.getTopublicip())) {
					Log.i("call",
							"came to sendreq1 publicl ocal ip...."
									+ sb.getLocalip());
					if (!sb.getLocalip().equals(sb.getPublicip())) {

						// Newly Implemented To check whether the two users are
						// in local with Different Public Ip....

						sendType13Request(sb);

						// comment
						// if(di!=null&&di.getPublicPort()!=0&&!di.isSymmetric())
						// {
						//
						// String receivedPIP=di.getPublicIP().toString();
						// receivedPIP=receivedPIP.substring(1,
						// receivedPIP.length());
						//
						//
						// sb.setBuddyConnectip(receivedPIP);
						// sb.setBuddyConnectport(Integer.toString(di.getPublicPort()));
						// gotPortIpAddressFromServer2(receivedPIP);
						// //sb.setBuddyLocalPort(Integer.toString(rtpPort));
						//
						// setBuddyInNat(true);
						// }
						// else {
						//
						// holePunchstate=HOLE_PUNCHING_STATE.HOLE_PUNCHING_SERVER1_WAIT_RESPONSE;
						// pingServer1();
						// setBuddyInNat(true);
						// }

						// ended........

					} else {

						Log.i("call", "came to sendreq1 else part....");
						sb.setBuddyConnectip(sb.getLocalip());
						sb.setBuddyConnectport(Integer.toString(rtpPort));
						sb.setPunchingmode("0");

						// System.out.println("##### Check this "+sb.getBuddyConnectip()
						// +" : "+sb.getBuddyConnectport()+" : "+sb.getLocalip());
						gotPortIpAddressFromServer2(sb.getLocalip());
						setBuddyInNat(false);
						// System.out.println("LOCAL CALL");
						// holePunchstate =
						// HOLE_PUNCHING_STATE.HOLE_PUNCHING_SUCCESS_MEDIA_JOIN;
					}

				} else {

					// System.out.println("##### Check this "+sb.getBuddyConnectip()
					// +" : "+sb.getBuddyConnectport()+" : "+sb.getLocalip());
					sb.setBuddyConnectip(sb.getLocalip());
					sb.setBuddyConnectport(Integer.toString(rtpPort));
					sb.setPunchingmode("0");
					gotPortIpAddressFromServer2(sb.getLocalip());
					setBuddyInNat(false);
					// holePunchstate =
					// HOLE_PUNCHING_STATE.HOLE_PUNCHING_SUCCESS_MEDIA_JOIN;
					// System.out.println("LOCAL CALL");
				}

			} else {
				// Confirm It as Relay...........

				// Log.d("SM","setBuddyInNat(true);");

				// setBuddyInNat(true);
				// if(sb.getType().equals("1"))
				// {
				// JoinRelayNew(false);
				// }
				// else if(sb.getType().equals("2"))
				// {
				// JoinRelayNew(true);
				// }
				// sendSignalAfterJoin();
				// sendType13OnConferenceConnect(sb);

				type13ExpirationOnRelay1();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// void sendRelayJoinRequest()
	// {
	// sb.setBuddyConnectip(relayServerip);
	// sb.setBuddyConnectport(Integer.toString(relayServerPort));
	// //sb.setPunchingmode("0");
	// gotPortIpAddressFromServer2(relayServerip);
	// }

	public void type13ExpirationOnRelay1() {
		// Log.d("Type13", "Type13 Expiresssssssss on relayyyyyyyyyyy");

		try {
			setBuddyInNat(isNat(sb));
			if (sb.getType().equals("1")) {
				JoinRelayNew(false);
			} else if (sb.getType().equals("2")) {
				JoinRelayNew(true);
			}
			sendSignalAfterJoin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isNat(SignalingBean sb) {
		try {
			if (!sb.getPublicip().equals(sb.getTopublicip())) {
				if (!sb.getLocalip().equals(sb.getPublicip())) {

					return true;
				}
			}

			return false;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Whether a call is local or nat ,this method will be called finally to
	 * fill the required network details that are necessary to establish a
	 * session
	 * 
	 * @param connectip
	 *            ipaddress
	 */
	public void gotPortIpAddressFromServer2(String connectip) {
		// Log.d("udp",
		// "sending message from accept call on gotPortIpAddressFromServer2" );

		Log.i("call", "came to getport ip address form server2" + connectip);
		if (connectip == null) {
			Log.i("call", "came server2 sb type" + sb.getType());
			if (sb.getType().equals("1")) {
				JoinRelayNew(false);
			} else if (sb.getType().equals("2")) {
				Log.i("call", "came to going to join relay");
				JoinRelayNew(true);
			}
			sendSignalAfterJoin();
			return;
		}

		if (sb.getType().equals("2"))
			communicationEngine.notifyType2Sent();

		Log.i("call", "came to after return");
		// connectip=null;
		// System.out.println("################### gotPortIpAddressFromServer2 ################### connectip :"+connectip);
		sb.setAudiossrc(Long.toString(audiossrc));
		sb.setVideossrc(Long.toString(videossrc));

		if (sb.getBuddyConnectport() == null) {
			sb.setBuddyConnectport(Integer.toString(rtpPort));
		}

		if (connectip == null) {
			sb.setBuddyConnectip(communicationEngine.getLocalInetaddress());
			sb.setBuddyConnectip(relayServerip);
			sb.setBuddyConnectport(Integer.toString(relayServerPort));

			proprietarySignalling.sendMessage(sb);
			// sendRelayJoinMessage("0000", sb.getTo());

			// call relay join processssssssss...

		} else {
			proprietarySignalling.sendMessage(sb);
		}
		// if(sb.getBuddyConnectport()==null){
		// sb.setBuddyConnectport(Integer.toString(rtpPort));
		// }
		// Log.d("test", "Test gotIpAddress  from UI alert  attempt stun");
		// proprietarySignalling.sendMessage(sb);
		// check for paging
		// condition for recorder....
		// Log.d("SDPP", "callfrom calls over "+sb.getCallType());
		if (sb.getCallType().equals("MAP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("MVP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("AP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("VP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("ABC") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "audioBroadcasting "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("VBC") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "VideoBroadcasting "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("SS") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "VideoBroadcasting "+sb.getCallType()+","+sb.getType());
		}

		else {
			if (!sb.getCallType().equals("MTP")
					&& !sb.getCallType().equals("MPP")) {
				if (sb.getCallType().equals("ABC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VBC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"AP")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VP")) {
					communicationEngine.startAudioCapture(sb.getSignalid(),
							sb.getStorageWarningLevel());
				} else {
					communicationEngine.startAudioCapture(sb.getFrom() + "_"
							+ sb.getSessionid(), sb.getStorageWarningLevel());
				}
			}
		}

		// condition for player
		// Log.d("AP", "before check type2 "+sb.getType()+","+sb.getCallType());
		if (sb.getType().equals("2")) {
			// Log.d("AP", "ontype2");
			if (!sb.getCallType().equals("MAP")
					&& !sb.getCallType().equals("MVP")
					&& !sb.getCallType().equals("AP")
					&& !sb.getCallType().equals("VP")
					&& !sb.getCallType().equals("ABC")
					&& !sb.getCallType().equals("VBC")
					&& !sb.getCallType().equals("SS")) {
				// Log.d("AP", "calling audio player");
				if (!sb.getCallType().equals("MTP")
						&& !sb.getCallType().equals("MPP")) {
					startAudioPlayer(true);
					communicationEngine.resetAEC();
				}
			}

		} else {
			if (!sb.getCallType().equals("MTP")
					&& !sb.getCallType().equals("MPP")) {

				if (!sb.getCallType().equals("AP")
						&& !sb.getCallType().equals("VP")
						&& !sb.getCallType().equals("ABC")
						&& !sb.getCallType().equals("VBC")
						&& !sb.getCallType().equals("SS")) {
					startAudioPlayer(true);
					communicationEngine.resetAEC();
				} else {
					startAudioPlayer(false);
				}

			}
		}

		// Log.d("AP", "fghdb "+sb.getType()+","+sb.getCallType());
		checkMediaReceiveTimer();

		if (CommunicationEngine.callState.containsKey(sb.getSignalid())
				&& sb.getType().equals("1")) {
			String str = CommunicationEngine.callState.get(sb.getSignalid());
			// Log.d("test","str "+str);
			if (str.equals("3")) {
				if (communicationEngine.getConferenceTable().size() == 1) {
					communicationEngine.clean();
					communicationEngine.stopPreview();
				}
			}
		}

	}

	public void sendType13Request(SignalingBean sbType13) {
		Log.i("call", "############### came to sendtype13req"
				+ isValidLocalIp(sbType13));
		if (isValidLocalIp(sbType13)) {
			// Log.d("Type13", "sending   sendType13Request");
			try {
				// RelayTimer relayTimer=new
				// RelayTimer(rtpEngineRelay,relayServerIp,relayPort,data);
				this.sbType13 = sbType13;
				// Compose Type 13 and send Locallyy...
				SignalingBean sid = (SignalingBean) sbType13.clone();
				sid.setSignalid(Long.toString(utility.getRandomMediaID()));
				// sid.setSessionid(Long.toString(utility.getRandomMediaID()));

				XmlComposer xmlComposer = new XmlComposer();
				String xmlType13 = xmlComposer.composeType13Xml(sid);
				String finalXml = xmlComposer.getMessagewithRoot(xmlType13,
						"0", "13");

				// Log.d("Type13", "sending   sendType13Request "+finalXml);
				// Log.d("SIGNAL", "sending   sendType13Request "+finalXml);
				// s

				TimerBean tb = new TimerBean();
				tb.setCallStatus1(CallStatus1.CALL_INT1);
				ProprietarySignalling.messageSessionTable.put(
						sid.getSignalid(), tb);

				byte[] data = AESCrypto.encrypt(
						ProprietarySignalling.seceretKey, finalXml.getBytes());
				// proprietarySignalling

				Type13Timer timerCall = new Type13Timer(proprietarySignalling,
						data, sbType13, this);

				Timer timer = new Timer();
				timer.schedule(timerCall, 0, 700);
				// timerTable.put(msgid+sessionid,timer);

				mapType13Timer.put(sb.getTo() + sb.getSessionid(), timer);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else

		{
			type13Expiration();
		}

	}

	private void sendType13OnConferenceConnect(SignalingBean sbType13) {
		if (isValidLocalIp(sbType13)) {

			// Log.d("Type13", "sending   sendType13Request");
			try {
				// RelayTimer relayTimer=new
				// RelayTimer(rtpEngineRelay,relayServerIp,relayPort,data);
				this.sbType13 = sbType13;
				// Compose Type 13 and send Locallyy...

				SignalingBean sid = (SignalingBean) sbType13.clone();
				sid.setSignalid(Long.toString(utility.getRandomMediaID()));
				// sid.setSessionid(Long.toString(utility.getRandomMediaID()));

				XmlComposer xmlComposer = new XmlComposer();
				String xmlType13 = xmlComposer.composeType13Xml(sid);
				String finalXml = xmlComposer.getMessagewithRoot(xmlType13,
						"0", "13");

				// Log.d("Type13", "sending   sendType13Request "+finalXml);
				// Log.d("SIGNAL", "sending   sendType13Request "+finalXml);
				// s

				TimerBean tb = new TimerBean();
				tb.setCallStatus1(CallStatus1.CALL_INT1);
				ProprietarySignalling.messageSessionTable.put(
						sid.getSignalid(), tb);

				byte[] data = AESCrypto.encrypt(
						ProprietarySignalling.seceretKey, finalXml.getBytes());

				// proprietarySignalling

				Type13Timer timerCall = new Type13Timer(proprietarySignalling,
						data, sbType13, this);
				timerCall.setRelayConfirmation(true);

				Timer timer = new Timer();
				timer.schedule(timerCall, 0, 700);
				// timerTable.put(msgid+sessionid,timer);

				mapType13Timer.put(sb.getTo() + sb.getSessionid(), timer);

				mapType13TimerToReset.put(sb.getTo() + sb.getSessionid(),
						timerCall);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else

		{
			type13ExpirationOnRelay();
		}

	}

	private boolean isValidLocalIp(SignalingBean sbeann) {
		Log.i("call", "@@@@@@@@@@@@@@@@@@@@ came to local ip validation "
				+ sbeann.getLocalip());
		if (sbeann.getLocalip() == null) {
			return false;
		} else if (sb.getLocalip().equals("")) {
			return false;
		} else if (sb.getLocalip().equals("0.0.0.0")) {
			return false;
		}

		return true;

	}

	public void type13Expiration() {
		Log.i("call", "@@@@@@@@@@@ came to type13 expiration");
		// Log.d("Type13", "Type13 Expireeeeeeee");
		// Buddy is Really on NAT
		// if(di!=null)
		// {
		// Log.d("test", "type13Expiration()  NotNull");
		// Log.d("test", "type13Expiration()  sym "+di.isSymmetric());
		// Log.d("test", "type13Expiration()  pubip "+di.getPublicIP());
		// Log.d("test", "type13Expiration()  port "+di.getPublicPort());
		// }
		// else {
		// Log.d("test", "type13Expiration()  Null");
		// }
		// Log.d("test", "type13Expiration()  "+di);

		// di=null;
		Log.d("call", new Date() + "Attempting Stun");

		Log.d("call", new Date() + "Attempting Stun -----> " + di);

		if (di != null && di.getPublicPort() != 0 && !di.isSymmetric()) {
			// Log.d("test", "Attempting Stun");
			String receivedPIP = di.getPublicIP().toString();
			Log.i("call", "!!!!!!!!!!!!!!!" + receivedPIP);
			receivedPIP = receivedPIP.substring(1, receivedPIP.length());
			sb.setBuddyConnectip(receivedPIP);
			sb.setBuddyConnectport(Integer.toString(di.getPublicPort()));
			sb.setPunchingmode("0");
			gotPortIpAddressFromServer2(receivedPIP);

			// sb.setBuddyLocalPort(Integer.toString(rtpPort));

			setBuddyInNat(true);
		} else {

			Log.d("call", new Date() + "came to else part.....");

			holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_SERVER1_WAIT_RESPONSE;
			pingServer1();
			setBuddyInNat(true);
		}
	}

	public void type13ExpirationOnRelay() {
		// Log.d("Type13", "Type13 Expiresssssssss on relayyyyyyyyyyy");

		try {
			setBuddyInNat(true);
			if (sb.getType().equals("1")) {
				JoinRelayNew(false);
			} else if (sb.getType().equals("2")) {
				JoinRelayNew(true);
			}
			sendSignalAfterJoin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveType14(String key) {

		// Log.d("SIGNAL", "receiveType14(String key) ");
		// Log.d("Type13",
		// "receiveType14(String key)   ****************************");
		if (mapType13Timer != null && mapType13Timer.containsKey(key)) {
			Timer timer = mapType13Timer.get(key);
			timer.cancel();
		}

		// added at last
		if (mapType13TimerToReset != null
				&& mapType13TimerToReset.containsKey(key)) {

			Type13Timer timer = mapType13TimerToReset.get(key);
			// Log.d("SIGNAL",
			// "^^^^^^^^^^^^^^^^^containsssssssssssss "+timer.getRelayConfirmation());
			if (timer.getRelayConfirmation()) {
				setJoinedRelay(false);
				try {
					// if(sb.getCallType().equalsIgnoreCase("ABC")||sb.getCallType().equalsIgnoreCase("VBC"))
					// {
					// // Log.d("REP", "from calsssssssssssssss %%%%%%%%%%%%");
					// getRtpEngine().addRemoteEndpoint(getAudiossrc(),sb.getBuddyConnectip(),Integer.parseInt(sb.getBuddyConnectport()));
					// }
					if (sb.getType().equals("2")) {
						// Log.d("R14", "Receive type14 on relay join");
						getRtpEngine().addRemoteEndpoint(getAudiossrc(),
								sb.getBuddyConnectip(),
								Integer.parseInt(sb.getBuddyConnectport()));

						// last changed...
						getRtpEngine().setBuddyAudioSsrc(
								Integer.parseInt(sb.getAudiossrc()));
						getRtpEngine().setBuddyVideoSsrc(
								Integer.parseInt(sb.getVideossrc()));

						getRtpEngine().setMyVideoSsrc(getVideossrc());
						getRtpEngine().setMyAudioSsrc(getAudiossrc());

						if (sb.getCallType().equals("VC")
								|| sb.getCallType().equals("VBC")
								|| sb.getCallType().equals("VP")
								|| sb.getCallType().equals("SS")) {
							// Log.d("REP", "from comm2");
							getRtpEngine().addRemoteEndpoint(getVideossrc(),
									sb.getBuddyConnectip(),
									Integer.parseInt(sb.getBuddyConnectport()));
							// showBuddyVideo(buddy, sessionid)
							// Log.d("REP", "from comm2");

						}

						//

					}

				}

				catch (Exception e) {
					// TODO: handle exception
				}

				// Log.d("SIGNAL",
				// " ^^^^^^^^^^^^^^^^^^^^^^^^^ Removedddddddddddddddd ");
			}

		}

		// setJoinedRelay(false);

		try {
			// Buddy is in Local with Different Public Ip for Both Users....
			sb.setBuddyConnectip(sb.getLocalip());
			sb.setBuddyConnectport(Integer.toString(rtpPort));
			sb.setPunchingmode("0");
			gotPortIpAddressFromServer2(sb.getLocalip());
			setBuddyInNat(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendSignalAfterJoin() {
		Log.i("call", "came to going after relay join");
		// System.out.println("################### gotPortIpAddressFromServer2 ################### connectip :"+connectip);
		sb.setAudiossrc(Long.toString(audiossrc));
		sb.setVideossrc(Long.toString(videossrc));

		if (sb.getBuddyConnectport() == null) {
			sb.setBuddyConnectport(Integer.toString(rtpPort));
		}

		sb.setBuddyConnectip(relayServerip);
		sb.setBuddyConnectport(Integer.toString(relayServerPort));

		proprietarySignalling.sendMessage(sb);

		if (sb.getType().equals("2"))
			communicationEngine.notifyType2Sent();

		// Log.d("test", "Test gotIpAddress  from UI alert  attempt stun");
		// proprietarySignalling.sendMessage(sb);
		// check for paging
		// condition for recorder....
		// Log.d("SDPP", "callfrom calls over "+sb.getCallType());
		if (sb.getCallType().equals("MAP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("MVP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("AP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("VP") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP", "audiopaging "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("ABC") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "audioBroadcasting "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("VBC") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "VideoBroadcasting "+sb.getCallType()+","+sb.getType());
		} else if (sb.getCallType().equals("SS") && sb.getType().equals("1")) {
			// communicationEngine.sendDuumyPackets();
			// Log.d("AP",
			// "VideoBroadcasting "+sb.getCallType()+","+sb.getType());
		}

		else {
			if (!sb.getCallType().equals("MTP")
					&& !sb.getCallType().equals("MPP")) {
				if (sb.getCallType().equalsIgnoreCase("ABC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VBC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"AP")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VP")) {
					communicationEngine.startAudioCapture(sb.getSignalid(),
							sb.getStorageWarningLevel());
				} else {
					communicationEngine.startAudioCapture(sb.getFrom() + "_"
							+ sb.getSessionid(), sb.getStorageWarningLevel());
				}
			}
		}

		// condition for player
		// Log.d("AP", "before check type2 "+sb.getType()+","+sb.getCallType());
		if (sb.getType().equals("2")) {
			// Log.d("AP", "ontype2");
			if (!sb.getCallType().equals("MAP")
					&& !sb.getCallType().equals("MVP")
					&& !sb.getCallType().equals("AP")
					&& !sb.getCallType().equals("VP")
					&& !sb.getCallType().equals("ABC")
					&& !sb.getCallType().equals("VBC")
					&& !sb.getCallType().equals("SS")) {
				// Log.d("AP", "calling audio player");
				if (!sb.getCallType().equals("MTP")
						&& !sb.getCallType().equals("MPP")) {
					startAudioPlayer(true);
					communicationEngine.resetAEC();
				}
			}

		} else {
			if (!sb.getCallType().equals("MTP")
					&& !sb.getCallType().equals("MPP")) {

				if (!sb.getCallType().equals("AP")
						&& !sb.getCallType().equals("VP")
						&& !sb.getCallType().equals("ABC")
						&& !sb.getCallType().equals("VBC")
						&& !sb.getCallType().equals("SS")) {
					startAudioPlayer(true);
					communicationEngine.resetAEC();
				} else {
					startAudioPlayer(false);
				}

			}
		}

		// Log.d("AP", "fghdb "+sb.getType()+","+sb.getCallType());
		checkMediaReceiveTimer();

		if (CommunicationEngine.callState.containsKey(sb.getSignalid())
				&& sb.getType().equals("1")) {
			String str = CommunicationEngine.callState.get(sb.getSignalid());
			// Log.d("test","str "+str);
			if (str.equals("3")) {
				if (communicationEngine.getConferenceTable().size() == 1) {
					communicationEngine.clean();
					communicationEngine.stopPreview();
				}
			}
		}
	}

	void requestRelayServer() {
		// Used to send Relay Ip and Port on Signalling.....
		// s

	}

	// void sendRelayJoinMessage(final String msgid,final String
	// interestedBuddy){
	//
	// if(relayMessageThread==null){
	// relayMessageThread=new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// while(relayRunning){
	// holePunchstate=HOLE_PUNCHING_STATE.HOLE_PUNCHING_SUCCESS_JOIN;
	//
	// composeAndSendRelayJoinMessage(relayServerip, relayServerPort,
	// interestedBuddy, msgid);
	// Thread.sleep(2000);
	// }
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// });
	// relayMessageThread.start();
	// //System.out.println("Relay message thread started.....................!#####################");
	// }
	// }
	//
	//
	// void composeAndSendRelayJoinMessage(String relayServerIp,int
	// relayServerPort,String buddy,String messageId)
	// {
	// byte
	// data[]=(messageId+","+sb.getSessionid()+","+communicationEngine.getLoginUsername()+","+CallsOverInternet.audiossrc+","+CallsOverInternet.videossrc+","+"30").getBytes();
	// rtpEngine.addRemoteEndpoint(12345, relayServerIp, (relayServerPort));
	// rtpEngine.sendData(12345, relayServerIp, data);
	// rtpEngine.removeRemoteEndpoint(12345, relayServerIp);
	// }

	/**
	 * Call checkMediaReceiverTimer method to schedule a timer for 25 seconds
	 * and reset's holePunchstate to DEFAULT. This timer wil be stopped as soon
	 * as any media packet's received.If timer elapsed then relay module
	 * triggered.
	 */
	public void checkMediaReceiveTimer() {
		if (mediaReceiveTimer == null) {
			mediaReceiveTimer = new Timer();
			MediaTimer mediaTimer = new MediaTimer();
			/*
			 * mediaTimer.setRelayInterface(this);
			 * mediaReceiveTimer.schedule(mediaTimer,25000,25000);
			 */
			holePunchstate = HOLE_PUNCHING_STATE.DEFAULT;

			// System.out.println("################################## MediaReceiveTimer started......");
		}
	}

	/**
	 * This mehtod will start a new audio player for this session.This player
	 * will use speex codec.Respective decoder index should be passed to player
	 */
	public void startAudioPlayer(boolean mDuplex) {

		playerQueue = new Queue();
		if (CallDispatcher.sb.getCallType().equalsIgnoreCase("ABC")
				|| CallDispatcher.sb.getCallType().equalsIgnoreCase("VBC")
				|| CallDispatcher.sb.getCallType().equalsIgnoreCase("AP")
				|| CallDispatcher.sb.getCallType().equalsIgnoreCase("VP")) {
			audioPlayer = new AudioPlayer(playerQueue, 4096,
					communicationEngine.getAudioPlayedQueue(),
					CallDispatcher.sb.getSignalid());
		} else {
			audioPlayer = new AudioPlayer(playerQueue, 4096,
					communicationEngine.getAudioPlayedQueue(),
					CallDispatcher.sb.getTo() + "_"
							+ CallDispatcher.sb.getSessionid());
		}
		audioPlayer.setAudioCodecFormat(AudioCodecFormat.EAudioSpeex);
		audioPlayer.setIndex(decIndex);
		audioPlayer.setmDuplex(mDuplex);
		audioPlayer.Start();
	}

	/**
	 * stop's the audio player
	 * 
	 */
	public void stopAudioPlayer() {
		try {
			if (audioPlayer != null) {
				if (CallDispatcher.sb.getCallType().equalsIgnoreCase("ABC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VBC")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"AP")
						|| CallDispatcher.sb.getCallType().equalsIgnoreCase(
								"VP")) {
					audioPlayer.Stop(CallDispatcher.sb.getSignalid());
				} else {
					audioPlayer.Stop(CallDispatcher.sb.getTo() + "_"
							+ CallDispatcher.sb.getSessionid());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * This method is used to ping address agent server1.Enum holePunchstate
	 * will be set to HOLE_PUNCHING_SERVER1_WAIT_RESPONSE.
	 * 
	 * 
	 */
	public void pingServer1() {
		try {
			Log.d("call", new Date() + "came to ping server1");
			// Log.d("call","################## Pinging Server1......");
			// System.out.println("################## Pinging Server1......");
			holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_SERVER1_WAIT_RESPONSE;
			rtpEngine.setRemoteAddress(InetAddress.getByName(cbserver1));
			rtpEngine.setRemotePort(cbport1);
			// Log.d("REP", "from calls 1");
			rtpEngine.addRemoteEndpoint(1235, (cbserver1), cbport1);
			rtpEngine.sendData(1235, cbserver1,
					"get my nat port from server1".getBytes());
			timer = new Timer();
			retransmission = new HolePunchRetransmissionTimer(this, 1235,
					cbserver1, "get my nat port from server1".getBytes());
			retransmission.setRtpEngine(rtpEngine);
			timer.schedule(retransmission, 2000, 2000);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * This method is used to ping address agent server2.Enum holePunchstate
	 * will be set to HOLE_PUNCHING_SERVER2_WAIT_RESPONSE.
	 * 
	 * @param data
	 *            result obtained from pingserver1
	 */
	public void pingServer2(String data) {
		try {

			Log.d("call", new Date() + "came to ping server2");
			// Log.d("call","Server 1 Response"+data);
			// System.out.println("################# Pinging Server2......"+cbserver2+" port :"+cbport2);
			holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_SERVER2_WAIT_RESPONSE;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(new byte[12]);
			out.write(data.getBytes());
			byte buffer[] = out.toByteArray();

			rtpEngine.setRemoteAddress(InetAddress.getByName(cbserver2));
			rtpEngine.setRemotePort(cbport2);

			// Log.d("REP", "from calls 2");
			rtpEngine.addRemoteEndpoint(1236, (cbserver2), cbport2);
			rtpEngine.sendData(1236, cbserver2, buffer);
			timer = new Timer();
			retransmission = new HolePunchRetransmissionTimer(this, 1236,
					cbserver2, buffer);
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

	/** Stop the timer used by pingserver1 and pingserver2 **/
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * This method has been isolated, a dedicated rtp socket will take care of
	 * relay related functionality
	 **/
	public void joinRequest(String ipaddress, int port, String buddyInterested,
			String msgid) {
		// System.out.println("CommunicationEngine.relayJoined :"+CommunicationEngine.relayJoined+" msgid:"+msgid);
		byte data[] = (msgid + "," + sb.getSessionid() + ","
				+ communicationEngine.getLoginUsername() + "," + buddyInterested)
				.getBytes();
		// Log.d("REP", "from calls 3");
		rtpEngine.addRemoteEndpoint(12345, ipaddress, (port));
		rtpEngine.sendData(12345, ipaddress, data);

		// System.out.println("############################################# joinRequest sent"+new
		// String(data));
		rtpEngine.removeRemoteEndpoint(12345, ipaddress);
	}

	/**
	 * This method has been isolated,a dedicated rtp socket will take care of
	 * relay related functionality
	 **/
	public void joinMessageToServer(final String msgid,
			final String interestedBuddy) {

		if (relayMessageThread == null) {
			relayMessageThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (relayRunning) {
							holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_SUCCESS_JOIN;
							// System.out.println("############################# Relay Join message thread code executed....");
							if (joinedRelay) {
								joinRequest(relayServerip, relayServerPort,
										interestedBuddy, "0002");
							} else {
								joinRequest(relayServerip, relayServerPort,
										interestedBuddy, msgid);
							}

							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			relayMessageThread.start();
			// System.out.println("Relay message thread started.....................!#####################");
		}
	}

	/**
	 * This method has been isolated ,a dedicated rtp socket will take care of
	 * relay related functionality
	 **/
	public void joinRelayServerMedia(final String buddyInterested,
			final String msgid) {

		if (relayMediaJoinThread == null) {
			relayMediaJoinThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (relayMediaJoinRunning) {
							holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_SUCCESS_MEDIA_JOIN;
							// System.out.println("Relay message thread code executed....");
							joinRequest(relayServerip, relayServerPort,
									buddyInterested, msgid);
							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			relayMediaJoinThread.start();
		}
	}

	/**
	 * When start receiving media packets this method will be called and
	 * remove's media receiver timer and start's the packet timer
	 */

	public void stopMediaReceiveTimer() {
		// Log.d("FT", "######## Started frame timer");

		if (mediaReceiveTimer != null) {
			mediaReceiveTimer.cancel();
			holePunchstate = HOLE_PUNCHING_STATE.DEFAULT;
			mediaReceiveTimer = null;
			// System.out.println("######################### MediaReceiveTimer has been canceld ############");
		}

	}

	// may 26
	public void startTimer() {
		// Log.d("FTR", "Created Frame Timer...from Call over");
		if (packetTimer == null) {
			// Log.d("FTR",
			// "Created Frame Timer...from Call over$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

			// newly implemented for Relay switching....
			relayTimer = new Timer();
			relaySwitch = new RelaySwitchTimer(this);
			relayTimer.schedule(relaySwitch, 6000);

			packetTimer = new Timer();
			frameTimer = new FrameTimer(this);
			packetTimer.schedule(frameTimer, 15000, 15000);

		}

	}

	public boolean isPacketReceive() {
		return packetReceive;
	}

	public FrameTimer getFrameTimer() {
		return frameTimer;
	}

	public long getCurrentPacketTime() {
		return currentPacketTime;
	}

	@Override
	public void notifyMediaFrame(byte[] data, long ssrc) {

		Log.d("VDO", "Came to notifyMedia frame at calls over internet");
		currentPacketTime = System.currentTimeMillis();
		// Log.d("VDO","on calls over ssrc "+ssrc+" buddyVideoSSRC "+buddyVideoSSRC+" buddyAudioSSRC :"+buddyAudioSSRC);
		// Log.d("call","on calls over ssrc "+ssrc+" buddyVideoSSRC "+buddyVideoSSRC+" buddyAudioSSRC :"+buddyAudioSSRC+"REceived ssrc "+ssrc+" name "+getBuddyName());
		if (data != null) {
			String str = new String(data);

			if (str.contains("0000,") || str.contains("0002,")) {
				System.out.println("#################### Relay response Data :"
						+ str);
				// System.out.println("Holepunchstate :@@@@@@@@@@@@@@@ "+holePunchstate);
			}
			str = null;

			// Used to cancel RelaySwitchTimer....
			if (relayTimer != null) {
				if (ssrc == buddyAudioSSRC || ssrc == buddyVideoSSRC) {
					relayTimer.cancel();
					relayTimer = null;

				}

			}

			if (ssrc == buddyAudioSSRC) {
				packetReceive = true;
				stopMediaReceiveTimer();

				if (playerQueue != null) {

					if (!speakerMute) {
						// System.out.println("########## playerQueue size: "+playerQueue.getSize());
						if (playerQueue.getSize() < 5) {
							// System.out.println("########## playerQueue : ");
							playerQueue.addMsg(data);
						}

					}

				}
			} else if (ssrc == buddyVideoSSRC) {
				packetReceive = true;
				stopMediaReceiveTimer();

				// System.out.println("buddyVideoSSRC $$$");
				// System.out.println("videoQueue size :"+videoQueue.getSize());
				// Log.d("BEFOREDECODE",""+videoQueue.getSize());

				// if(videoQueue.getSize()<20){

				VideoBean bean = new VideoBean();
				bean.setData(data);
				bean.setSsrc(ssrc);
				bean.setDecoderIndex(decIndex);
				videoQueue.addMsg(bean);

				/*
				 * } else {
				 * System.out.println("Queue Full Frame Skipped "+ssrc); }
				 */
				// communicationEngine.decodeVideoFrame(data, ssrc);
			} else {

				switch (holePunchstate) {
				case HOLE_PUNCHING_SERVER1_WAIT_RESPONSE:

					stopTimer();
					pingServer2(new String(data).toString());
					break;

				case HOLE_PUNCHING_SERVER2_WAIT_RESPONSE:
					String response = new String(data).toString();
					// System.out.println("################### Server2 Response :"+response);
					// Log.d("call","Server 2 Response"+response);

					if (response != null && response.length() > 0
							&& response.indexOf("#") > 0) {
						stopTimer();
						String result[] = response.split("#");
						// Log.d("UHP", "Result "+response);
						// Log.d("UHP", "Result0 "+result[0]);
						// Log.d("UHP", "Result1 "+result[1]);
						// Log.d("UHP", "Result2 "+result[2]);

						holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_IDLE;
						if (result.length > 2) {

							sb.setBuddyConnectip(result[0]);
							sb.setBuddyConnectport(result[1]);
							sb.setPunchingmode(result[2]);
							// result[2]="3";

							if (!result[2].trim().equals("3")) {
								// Log.d("UHP", "Result not 3 -->"+result[2]);

								gotPortIpAddressFromServer2(result[0]);
							} else {
								// Log.d("UHP", "Result3    -->"+result[2]);

								gotPortIpAddressFromServer2(result[0]);
							}

						}

					}

					break;
				case HOLE_PUNCHING_SUCCESS_MEDIA_JOIN:

					relayMediaJoinRunning = false;
					relayMediaJoinThread = null;

					String result = new String(data);

					if (result != null && result.length() > 0) {

						if (result.contains(",")) {

							if (CommunicationEngine.relayJoined) {
								setAllowMedia(false);
							} else {

								String values[] = result.split(",");
								setJoinedRelay(true);

								if (values[0].equals("0000")
										|| values[0].equals("0002")
										&& values[2].trim()
												.equals(sb.getFrom())) {
									rtpEngine
											.removeRemoteEndpoint(
													getAudiossrc(),
													getBuddyConnectip());
									if (sb.getCallType().equals("VC")) {
										rtpEngine.removeRemoteEndpoint(
												getVideossrc(),
												getBuddyConnectip());
									}

									// System.out.println("############# relayServerip:"+relayServerip);
									// System.out.println("############# relayServerPort:"+relayServerPort);
									setBuddyConnectip(relayServerip);
									// Log.d("REP", "from calls 4");
									rtpEngine.addRemoteEndpoint(getVideossrc(),
											relayServerip, relayServerPort);
									rtpEngine.addRemoteEndpoint(getAudiossrc(),
											relayServerip, relayServerPort);
								}

							}

						}
					}
					holePunchstate = HOLE_PUNCHING_STATE.DEFAULT;

					break;

				case HOLE_PUNCHING_SUCCESS_JOIN:
					relayRunning = false;
					relayMessageThread = null;

					// System.out.println("HOLE_PUNCHING_SUCCESS_JOIN >>>>>>>>>> "+new
					// String(data));
					String result1 = new String(data);

					if (result1 != null && result1.length() > 0) {

						String values[] = result1.split(",");

						setJoinedRelay(true);
						sendMediaJoinRequestToEndUser();
						// }
					}
					holePunchstate = HOLE_PUNCHING_STATE.HOLE_PUNCHING_PORT_SCANNING;
					break;

				default:

					break;
				}
			}
		} else {
			videoQueue.clear();
		}

	}

	public boolean isJoinedRelay() {
		return joinedRelay;
	}

	public void setJoinedRelay(boolean joinedRelay) {
		this.joinedRelay = joinedRelay;
	}

	/**
	 * After joining with relay server send's type six signal to inform the
	 * remote end point join relay server to send media through relay server
	 * 
	 */
	public void sendMediaJoinRequestToEndUser() {
		if (sb != null) {
			sb.setSignalid(Long.toString(utility.getRandomMediaID()));
			sb.setType("6");
			proprietarySignalling.sendMessage(sb);
		}
	}

	public void sendRelayJoinSignalling() {
		if (sb != null) {
			sb.setSignalid(Long.toString(utility.getRandomMediaID()));
			sb.setType("6");

			proprietarySignalling.sendMessage(sb);
		}
	}

	/**
	 * Used to Send Relay Request on Media Failure...
	 */
	public void relayRequestOnMediaFailure() {
		// Used to send media request to My buddy...
		communicationEngine.sendType6ForExsistingUserAndcurrentUser(this);

	}

	// /** This method will not be callbacked.A dedicated rtp socket will take
	// care of relay related functionality **/
	// @Override
	public void sendMediaJoinMessage() {
		// TODO Auto-generated method stub
		// System.out.println("##########################   sendMediaJoinMessage() "+(CommunicationEngine.relayJoined));
		// stopMediaReceiveTimer();
		//
		// if(joinedRelay){
		// joinMessageToServer("0002",sb.getTo());
		// }else{
		// joinMessageToServer("0000",sb.getTo());
		// }
	}

	@Override
	public void JoinRelay() {
		// if(communicationEngine!=null){
		// //System.out.println("############## Joining Relay #############");
		// //
		// communicationEngine.joinRelaynew(sessionid,sb.getTo(),Long.valueOf(buddyAudioSSRC),Long.valueOf(buddyVideoSSRC),key,true);
		// setReceiveMediaRelay(true);
		// }
	}

	public void JoinRelayNew(boolean ssrcState) {
		if (communicationEngine != null) {
			// System.out.println("############## Joining Relay #############");
			communicationEngine.joinRelaynew(sessionid, sb.getTo(),
					Long.valueOf(buddyAudioSSRC), Long.valueOf(buddyVideoSSRC),
					key, sb.getCallType(), ssrcState);
			setJoinedRelay(true);
			// setReceiveMediaRelay(true);
		}
	}

	@Override
	public void notifyMediaAddress(String ipAddress, int Port) {

		try {
			setBuddyConnectPort(Port);
			// Log.d("REP", "from calls 5");
			Log.d("call", new Date() + "---- going to set videoSSRC ---"
					+ getVideossrc());
			getRtpEngine().addRemoteEndpoint(getVideossrc(),
					getBuddyConnectip(), Port);
			Log.d("call", new Date() + "---- going to set audioSSRC ---"
					+ getAudiossrc());
			getRtpEngine().addRemoteEndpoint(getAudiossrc(),
					getBuddyConnectip(), Port);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyPacketRequestMessage(long arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		Log.d("PACKETREQUEST", "Came to notifyPacketRequestMessage");
	}

}
