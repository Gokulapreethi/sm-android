package org.core;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.audio.AudioRecorder;
import org.audio.AudioRecorderListener;
import org.audio.AudioRecorderStateListener;
import org.codec.audio.Speex;
import org.lib.model.SignalingBean;
import org.net.rtp.MediaFrameListener;
import org.net.rtp.RtpEngine;
import org.net.rtp.RtpPacket;
import org.net.rtp.Store;
import org.util.AudioCodecFormat;
import org.util.GraphicsUtil;
import org.util.Queue;
import org.util.Utility;
import org.video.EnumVideoCodec;
import org.video.Preview;
import org.video.PreviewFrameSink;
import org.video.VideoCodec;
import org.video.VideoFrameCallback;
import org.video.VideoFrameRenderer;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.cg.callservices.VideoCallScreen;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;

/**
 * 
 * Communication Engine is the core class which provides various functionality
 * to establish,make and control session This class initialize the video and
 * audio codec's,start audio recording,received video and audio callback.
 * 
 * 
 */
public class CommunicationEngine implements AudioRecorderListener,
		VideoFrameCallback {
	private boolean screenSharing = false;
	int mPreviewWidth;
	int mPreviewHeight;

	private int videoSqno;
	private boolean mDecodeFrame;
	private String deviceId = null;

	private String currentSession = null;

	Queue audioPlayedQueue = new Queue();
	/**
	 * Width of the Preview to capture Video Data.
	 */
	private int width = 176;
	/**
	 * Height of the Preview to capture Video Data.
	 */
	private int height = 144;
	/**
	 * This enum Defines the communication Protocol constant.
	 */
	private EnumCommunicationProtocol communicationProtocol = null;

	private CommunicationListener communicationListener = null;
	private CallSession callSession = null;
	/**
	 * Interface to global information about an application environment. This is
	 * an abstract class whose implementation is provided by the Android system.
	 * It allows access to application-specific resources and classes, as well
	 * as up-calls for application-level operations such as launching
	 * activities, broadcasting and receiving intents, etc.
	 */
	private Context context;
	/**
	 * Used to maintain the Conference Call, information.
	 */
	private HashMap<String, ArrayList<SignalingBean>> conferenceTable = new HashMap<String, ArrayList<SignalingBean>>();
	/**
	 * used to maintain Signal On Time to close the IncomingCall.
	 */
	public HashMap<String, String> maintainSignallingOnTime = new HashMap<String, String>();
	/**
	 * Time of Last Received Frame.
	 */
	private long lastFrametime;
	/**
	 * A listener that can be used to notify media frame.
	 */
	private MediaFrameListener frameListener;
	/**
	 * Interface used to notify the ProprietyResponse and SIPResponse.
	 */
	private CallSessionListener callSessionListener = null;
	/**
	 * Interface used to notify the ProprietyResponse and SIPResponse.
	 */
	private EnumSignallingType enumSignallingType = null;
	/**
	 * CbServer1 Ip.Initially null.
	 */
	private String cbserver1 = null;
	/**
	 * CbServer2 Ip.Initially null.
	 */
	private String cbserver2 = null;
	/**
	 * cbServer1 Port.
	 */
	private int cbport1 = 0;
	/**
	 * cbServer2 Port.
	 */
	private int cbport2 = 0;
	/**
	 * Router IP Address.
	 */
	private String routerip = null;
	/**
	 * Relay Server Port.
	 */
	private int relayServerPort = 0;
	/**
	 * Relay Server IP Address.
	 */
	private String relayServerip = null;
	/**
	 * Router Port
	 */
	private int routerPort = 0;
	/**
	 * Used to send and Receive UDP signal. This class implement UDPDataListener
	 * which is used to receive UDP Signal.
	 */
	private ProprietarySignalling proprietarySignalling = null;
	/**
	 * Login Username.
	 */
	private String loginUsername = null;
	/**
	 * Used to maintain the Call Details.
	 */
	private HashMap callTable = new HashMap();
	/**
	 * Used to maintain conference Type7 Type0 Race CAse
	 */
	public HashMap<String, String> confRequestTable = new HashMap<String, String>();

	/**
	 * 
	 * Used to MAintain the InstantMessage.
	 */
	public HashMap imHashmap = new HashMap<String, InstantMessage>();
	/**
	 * Used to generate Random Id for Application like SessionId,SignalId etc.
	 */
	private Utility utility = new Utility();
	/**
	 * This class manages the communication session between two end points. This
	 * class also contains UDP Hole Punching and stun to traverse devices behind
	 * firewall and NAT All the media stream will be callbacked to this class to
	 * notifyMediaFrame where streams are identified. Whenever P2P failed relay
	 * module will be triggered by this class to achieve media through relay
	 */
	private CallsOverInternet callsOverInternet = null;
	/**
	 * Used to Initialize Audio Recorder. Using AudioRecorder we can Record
	 * Audio from the MIC.
	 */
	private AudioRecorder audioRecorder = null;
	/**
	 * This VideoCodec class provides video codec functionalities. Supports two
	 * codecs H264 and VP8.Also contains pixel format conversion methods.
	 */
	private VideoCodec videoCodec = null;
	/**
	 * Queue is used to insert and Retrieve Object. It is used as
	 * FIFO(First-In-First-Out) logic. java.util.LinkedList is used here for
	 * insert and Retrieve operations. This can be used to maintain Audio Data.
	 */
	private Queue audioFrameQueue = new Queue();
	/**
	 * Queue is used to insert and Retrieve Object. It is used as
	 * FIFO(First-In-First-Out) logic. java.util.LinkedList is used here for
	 * insert and Retrieve operations. This can be used to maintain Video Data.
	 */
	private Queue videoFrameQueue = new Queue();
	/**
	 * Used to Maintain Media key for Call Services.
	 */
	private ArrayList<String> mediaKey = new ArrayList<String>();
	/**
	 * Used to send Audio data. This class sends the Audio data which was stored
	 * in the Queue. This audio data are later send using RTPEngine.
	 */
	private AudioThread audioThread = null;
	private int signalingPort = 0;
	/**
	 * Used to Capture Video Frame From the Camera.
	 */
	private Preview preview = null;
	public boolean video_preview = true;
	/**
	 * Check whether the Video is Encode or not.
	 */
	private boolean encodeVideo = false;
	byte encoded_data[];
	private int key_frame = 0;
	/**
	 * Used to Draw the Captured Video Frame using GLSurfacce View.
	 */
	private GLSurfaceView glSurfaceView = null;
	private ByteBuffer frameBuffer = null;
	/**
	 * Used to convert the Input RGB data in to Bitmap using OpenGL. The Bitmap
	 * image is then draw to the GlSurface View.
	 */
	private VideoFrameRenderer frameRenderer = null;
	private byte[] frame = null;
	/**
	 * This class is used to represent a preview frame size and opengl mode that
	 * can be used by a frame renderer .
	 */
	private PreviewFrameSink frameSink = null;
	/**
	 * Public Ip Address.
	 */
	private String publicInetaddress = null;
	/**
	 * Local IpAddress.
	 */
	private String localInetaddress = null;
	private long decodessrc;

	public long getDecodessrc() {
		return decodessrc;
	}

	public void setDecodessrc(long decodessrc) {
		this.decodessrc = decodessrc;
	}

	/**
	 * Mic Mute State.
	 */
	private boolean micMute = false;
	/**
	 * Interface used to notify incoming video data.
	 */
	private VideoCallback videoCallback = null;
	private Thread thread = null;
	/**
	 * Queue is used to insert and Retrieve Object. It is used as
	 * FIFO(First-In-First-Out) logic. java.util.LinkedList is used here for
	 * insert and Retrieve operations.
	 */
	private Queue videoQueue = new Queue();

	private AudioRecorderStateListener audioRecordStateListener = null;
	public static boolean relayJoined = false;
	/** < RelayJoined Status */
	public static boolean relayMediaJoined = false;
	/** < RelayMediaJoined Status */
	/**
	 * This class extends Thread. This class is used to Process the incoming
	 * video data from the Queue and Notify it to Decode.
	 */
	private VideoThread videoThread = null;
	private VideoSenderThread videoSendThread = null;
	private Queue videoRtpQueue = new Queue();
	// private SenderThread senderThread=null;
	private int decoderIndex = -1;
	/**
	 * Camera Id to select Camera.(Front or Back).
	 */
	private int camaera_id = 1;
	private HashMap<Integer, Boolean> decoderStatus = new HashMap<Integer, Boolean>();
	/**
	 * Speex class provides native method call to speex functionalities. By
	 * default quality parameter is 4.The implementation for the native methods
	 * will be searched inside the libspeex.Current speex lib supports only
	 * narrowband 8khz sampling rate
	 */
	private Speex speex = null;
	/**
	 * Queue is used to insert and Retrieve Object. It is used as
	 * FIFO(First-In-First-Out) logic. java.util.LinkedList is used here for
	 * insert and Retrieve operations.
	 */
	private Queue sendQueue = new Queue();
	private boolean mSend = false;
	// private int mSend=0;
	// For Testing
	private long t1 = 0;
	private long t2 = 0;
	public int frameCount;
	/** < FrameCount. */
	private Timer frameChkTimer = null;

	public static HashMap<String, String> callState = new HashMap<String, String>();

	/**
	 * Used to get the Decoder Index Value.
	 * 
	 * @return Index Value
	 */
	public int getDecoderIndex() {
		return decoderIndex++;
	}

	/**
	 * constructor used to Initialize communicationEngine and Set the reference
	 * of CallSessionListener.
	 * 
	 * @param callSessionListener
	 *            Received message.
	 * @param enumSignallingType
	 *            SignalingType.
	 */
	public CommunicationEngine(CallSessionListener callSessionListener,
			EnumSignallingType enumSignallingType) {
		this.callSessionListener = callSessionListener;
		this.enumSignallingType = enumSignallingType;

	}

	/**
	 * constructor used to Initialize communicationEngine and Set the reference
	 * of EnumSignallingType.
	 * 
	 * @param enumSignallingType
	 *            SignalingType.
	 */
	public CommunicationEngine(EnumSignallingType enumSignallingType) {
		this.enumSignallingType = enumSignallingType;
	}

	/**
	 * Used to Register the Class for Notification.
	 * 
	 * @param callSessionListener
	 *            Received message
	 */
	public void setCallSessionListener(CallSessionListener callSessionListener,
			AudioRecorderStateListener audiorecordstateListener) {
		this.callSessionListener = callSessionListener;
		this.audioRecordStateListener = audiorecordstateListener;
		if (proprietarySignalling != null) {
			proprietarySignalling.setCallSessionListener(callSessionListener);
		}

	}

	public void notifyType2Sent() {
		if (callSessionListener != null)
			callSessionListener.notifytype2Sent();
	}

	/**
	 * Used to get the Public IpAddress.
	 * 
	 * @return Public IpAddress.
	 */
	public String getPublicInetaddress() {
		return publicInetaddress;
	}

	/**
	 * Used to set the Public IpAddress.
	 * 
	 * @param publicInetaddress
	 *            Public IpAddress.
	 */
	public void setPublicInetaddress(String publicInetaddress) {
		this.publicInetaddress = publicInetaddress;
		Log.d("COMM", "My Public IP Address" + localInetaddress);
	}

	/**
	 * Used to get the Local IpAddress.
	 * 
	 * @return Local IpAddress
	 */
	public String getLocalInetaddress() {
		return localInetaddress;
	}

	/**
	 * Used to set the Video Queue Reference.
	 * 
	 * @return contains Video data.
	 */
	public Queue getVideoQueue() {
		return videoQueue;
	}

	/**
	 * Used to get the Signaling Port.
	 * 
	 * @return Port for Signaling.
	 */
	public int getSignalingPort() {
		return signalingPort;
	}

	/**
	 * Used to Set the Signaling Port.
	 * 
	 * @param signalingPort
	 *            Port for Signaling.
	 */
	public void setSignalingPort(int signalingPort) {
		this.signalingPort = signalingPort;
	}

	/**
	 * Used to set the Local IpAddress.
	 * 
	 * @param localInetaddress
	 *            Local IpAddress.
	 */
	public void setLocalInetaddress(String localInetaddress) {
		this.localInetaddress = localInetaddress;
		Log.d("COMM", "My Local IP Address" + localInetaddress);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void stopPreview() {
		if (preview != null) {
			preview.stopPreview();
			preview = null;
		}
	}

	// public CallRecorderListener getCallRecorderlistener() {
	// return callRecorderlistener;
	// }
	//
	//
	//
	//
	// public void setCallRecorderlistener(CallRecorderListener
	// callRecorderlistener) {
	// this.callRecorderlistener = callRecorderlistener;
	// }

	/**
	 * This method is used to Initialize ProprietarySignalling and also to
	 * Initialize UDPEngine. ProprietarySignalling is used to send and Receive
	 * UDP signal. This class implement UDPDataListener which is used to receive
	 * UDP Signal.
	 * 
	 * @param retranmissionCount
	 *            Maximum count for Retransmission.
	 * @param retransmissionInterval
	 *            TimeDuration.
	 */
	public void start(int retranmissionCount, int retransmissionInterval) {
		try {

			if (proprietarySignalling == null) {
				proprietarySignalling = new ProprietarySignalling();
				proprietarySignalling.initialize(retranmissionCount,
						retransmissionInterval, callSessionListener, this,
						loginUsername);

				signalingPort = proprietarySignalling.startUDPEngine();
				while (signalingPort == -1) {
					signalingPort = proprietarySignalling.startUDPEngine();
					// System.out.println("########### signalingPort :"+signalingPort);
				}
				// System.out.println("########### signalingPort :"+signalingPort);
				proprietarySignalling.setCallTable(callTable);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProprietarySignalling getProprietarySignalling() {
		try {
			if (proprietarySignalling == null) {
				start(3, 3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proprietarySignalling;
	}

	/**
	 * Initialize the Decoder.
	 */
	public void initializeDecoder() {
		for (int i = 0; i < 6; i++) {
			decoderStatus.put(i, false);
		}
	}

	/**
	 * Set the connectionBroker Server details.
	 * 
	 * @param cbserver1
	 *            cbServer1 IPAddress
	 * @param cbserver2
	 *            cbServer2 IPAddress.
	 * @param cbport1
	 *            cbServer1 Port.
	 * @param cbport2
	 *            cbServer2 Port.
	 */
	public void setConnectionBrokerDetails(String cbserver1, String cbserver2,
			int cbport1, int cbport2) {
		// Log.d("PORT", "cb1 "+cbport1+" cb2 "+cbport2);
		this.cbserver1 = cbserver1;
		this.cbserver2 = cbserver2;
		this.cbport1 = cbport1;
		this.cbport2 = cbport2;
	}

	/**
	 * Used to get the CallTable Reference.
	 * 
	 * @return callTable.
	 */
	public HashMap getCallTable() {
		return callTable;
	}

	/**
	 * Used to get the Registered class for Notification.
	 * 
	 * @return VideoCallback
	 */
	public VideoCallback getVideoCallback() {
		return videoCallback;
	}

	/**
	 * Used to reset VpxDecoderIndex Value.
	 * 
	 * @param idx
	 *            Index.
	 */
	public void resetVpxDecoderIndex(int idx) {

		if (decoderStatus.containsKey(idx)) {
			decoderStatus.put(idx, false);
		}
	}

	/**
	 * Used to set VpxDecoderIndex Value.
	 * 
	 * @param idx
	 *            Index
	 */
	public void setVpxDecoderIndex(int idx) {
		if (decoderStatus.containsKey(idx)) {
			decoderStatus.put(idx, true);
		}
	}

	/**
	 * used to Initialize the Decoder.
	 */
	public void resetVpxDecoderIndex() {

		initializeDecoder();
	}

	/**
	 * Used to get the Free Decoder Index value.
	 * 
	 * @return Index.
	 */
	public int getVpxFreeDecoderIndex() {

		int idx = -1;
		for (int i = 0; i < decoderStatus.size(); i++) {
			// System.out.println();
			if (!decoderStatus.get(i)) {
				idx = i;
				break;
			}
		}
		if (idx == -1) {
			idx = (int) utility.getRandomMediaID();
		}
		return idx;
	}

	/**
	 * Used to get the Login UserName.
	 * 
	 * @return UserName.
	 */
	public String getLoginUsername() {
		return loginUsername;
	}

	/**
	 * Used to Register VideoCallback for Notification.
	 * 
	 * @param videoCallback
	 */
	public void setVideoCallback(VideoCallback videoCallback) {
		this.videoCallback = videoCallback;
	}

	/**
	 * Used to Show the Selected Buddy Video on Screen. This is used for video
	 * Conference to swith Buddy Video.
	 * 
	 * @param buddy
	 *            Buddy with Call.
	 * @param sessionid
	 *            UniqueId for service.
	 */
	public void showBuddyVideo(String buddy, String sessionid) {
		// System.out.println("buddy :"+buddy);
		// System.out.println("sessionid :"+sessionid);
		// System.out.println("callTable :"+callTable);

		CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
				.get(sessionid + buddy);
		if (callsOverInternet != null) {
			decodessrc = callsOverInternet.getBuddyVideoSSRC();
			Log.d("decodessrc", " " + decodessrc);
			Log.d("ShowBuddyVideo", "decode SSrc " + decodessrc);
			// if()
			// {
			//
			// }

		} else {
			Log.d("ShowBuddyVideo", "CAllsOverInternet Not found");

		}
		// System.out.println("showBuddyVideo :"+decodessrc);
	}

	/**
	 * Used to get the conferenceTable. The conference table contains the
	 * information about the Conference Call.
	 * 
	 * @return conferenceTable
	 */
	public HashMap<String, ArrayList<SignalingBean>> getConferenceTable() {
		return conferenceTable;
	}

	/**
	 * Get the AudioSsrc of the Service.
	 * 
	 * @return Unique identity for Audio.
	 */
	public long getAudiossrc() {
		return callSession.getAudiossrc();
	}

	/**
	 * Get the VideoSsrc of the service.
	 * 
	 * @return Unique identity for Video.
	 */
	public long getVideossrc() {
		return callSession.getVideossrc();
	}

	/**
	 * Used to get the MediaFrameListener implemented class reference.
	 * 
	 * @return MediaFrameListener implemented class Reference.
	 */
	public MediaFrameListener getFrameListener() {
		return frameListener;
	}

	/**
	 * Used to Register the class for Media Frame Notification.
	 * 
	 * @param frameListener
	 *            implemented class Reference.
	 */
	public void setFrameListener(MediaFrameListener frameListener) {
		callSession.setFrameListener(frameListener);
	}

	/**
	 * Constructor to Initialize.
	 * 
	 * @param context
	 *            Activity Reference
	 */
	public CommunicationEngine(Context context) {
		this.context = context;
		callSession = new CallSession();

	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean ismDecodeFrame() {
		return mDecodeFrame;
	}

	public void setmDecodeFrame(boolean mDecodeFrame) {
		this.mDecodeFrame = mDecodeFrame;
	}

	/**
	 * Used to get BuddyMedia Details.
	 * 
	 * @param buddy
	 *            BuddyName
	 * @return
	 */
	public BuddyDetails getBuddyMediaDetails(String buddy) {
		return callSession.getBuddyMediaDetails(buddy);
	}

	/**
	 * Used to Start SignalingAgent to send signaling on every 2Secounds.
	 * 
	 * @param username
	 *            Login UserName
	 * @param routerip
	 *            Server IpAddress
	 * @param routerPort
	 *            Server IpAddress
	 * @param relayServerip
	 *            RelayServer IpAddress.
	 * @param relayServerPort
	 *            RelatServer Port.
	 */
	public void startSignalingAgent(String username, String routerip,
			int routerPort, String relayServerip, int relayServerPort) {
		this.relayServerPort = relayServerPort;
		this.relayServerip = relayServerip;
		this.loginUsername = username;

		if (proprietarySignalling != null) {
			SignalingBean sb = new SignalingBean();
			sb.setFrom(username);

			// sb.setTo(username);
			/*
			 * sb.setLocalip(localInetaddress);
			 * sb.setPublicip(publicInetaddress);
			 */
			proprietarySignalling.setRouterip(routerip);
			proprietarySignalling.setRouteport(routerPort);

			proprietarySignalling.startPortRefereshTimer(username);
		}

	}

	/**
	 * Used To send data to particular Buddy.
	 * 
	 * @param buddy
	 *            BuddyName
	 * @param Buddy
	 *            Ssrc.
	 * @param data
	 *            Data to send
	 */
	public void send(String buddy, String ssrc, byte[] data) {
		callSession.send(buddy, ssrc, data);
	}

	/**
	 * Used to Make Call services like AudioCall,VideoCall etc.It also used to
	 * set the necessary details for the Signaling.
	 * 
	 * @param sb
	 *            Signaling Information.
	 */
	public void makeCall(SignalingBean sb) {

		System.out.println("###### MAKE CALL ######");
		if (proprietarySignalling != null) {

			sb.setFrom(sb.getFrom().toLowerCase());

			if (!sb.getType().equals("9")) {
				if(sb.getType().equals("0") && (sb.getCallType().equalsIgnoreCase("VC") || sb.getCallType().equalsIgnoreCase("AC"))
						&& sb.getSessionid() != null && sb.getSessionid().length() >0) {
					Log.i("NOTES","Session id :" +sb.getSessionid());
				} else {
					if (!sb.getCallType().equals("ABC")
							&& !sb.getCallType().equals("VBC")
							&& !sb.getCallType().equals("SS")) {
						sb.setSessionid(utility.getSessionID(getDeviceId()));
					}
				}
			}
			sb.setSignalid(Long.toString(utility.getRandomMediaID()));
			sb.setPublicip(publicInetaddress);
			sb.setLocalip(localInetaddress);
			Log.d("MC",
					"ToUser " + sb.getTo() + ", TR " + sb.getType()
							+ sb.getResult() + " ,Signalid " + sb.getSignalid());
			// New Implementation added on June12 for Deny call or conference
			// request for More than one Time...
			if (sb.getType().equals("0")) {
				
				sb.setCallSubType(getCallSubtype(sb.getCallType()));

				if (!confRequestTable.containsKey(sb.getTo())) {
					confRequestTable.put(sb.getTo(), "");
					proprietarySignalling.sendMessage(sb);
					proprietarySignalling.startRingTimer(sb);
				}
			} else {
				proprietarySignalling.sendMessage(sb);
				if (!sb.getType().equals("9")) {

					proprietarySignalling.startRingTimer(sb);
				}

			}

			//
			// proprietarySignalling.sendMessage(sb);
			// if(!sb.getType().equals("9")){
			// //proprietarySignalling.startCallTimer(sb);
			// proprietarySignalling.startRingTimer(sb);
			// }

			if (sb.getType().equals("9")) {

				Log.d("999",
						"sending type 9 from doaddconference "
								+ sb.getGmember() + " TO " + sb.getTo());
				CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
						.get(sb.getSessionid() + sb.getGmember());
				if (callsOverInternet != null) {
					decodessrc = callsOverInternet.getBuddyVideoSSRC();
					Log.d("999", "Decode ssrc" + decodessrc);
				}

				if (sb.getTo().equals(sb.getGmember())) {
					try {
						Log.d("RESET",
								"True" + " sb.getGmember() " + sb.getGmember()
										+ " To " + sb.getTo());

						CallsOverInternet c1 = (CallsOverInternet) callTable
								.get(sb.getSessionid() + sb.getTo());
						if (c1 != null) {
							c1.getRtpEngine().resetRetransmision(true);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					Log.d("RESET", "False");
					try {

						CallsOverInternet c1 = (CallsOverInternet) callTable
								.get(sb.getSessionid() + sb.getTo());
						if (c1 != null) {
							c1.getRtpEngine().resetRetransmision(false);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					// engine.resetRetransmision(false);
				}

				// CallsOverInternet obj=(CallsOverInternet)
				// callTable.get(sb.getSessionid()+sb.getTo());

				if (sb.getTo().equals(sb.getGmember())) {
					CallsOverInternet obj = (CallsOverInternet) callTable
							.get(sb.getSessionid() + sb.getTo());

					if (obj != null) {

						if (obj.isJoinedRelay()) {

							RelayClientNew relayNew = (RelayClientNew) callTable
									.get(sb.getSessionid());
							if (relayNew != null) {
								relayNew.setVideoSsrc(decodessrc);
								relayNew.setBuddyName(obj.getBuddyName());
								// relayNew.sendMediaRequestMessage(callsOverInternet.getSessionid(),
								// getLoginUsername(),
								// callsOverInternet.getBuddyName());
								Log.d("999", "relay client matches");
								relayNew.sendRelayVideoReceiveMessage(
										obj.getSessionid(), getLoginUsername(),
										obj.getBuddyName());

							}
						}

					}
				} else {
					CallsOverInternet obj = (CallsOverInternet) callTable
							.get(sb.getSessionid() + sb.getTo());
					if (obj != null) {

						if (obj.isJoinedRelay()) {

							RelayClientNew relayNew = (RelayClientNew) callTable
									.get(sb.getSessionid());
							if (relayNew != null) {
								relayNew.setVideoSsrc(decodessrc);
								 relayNew.setBuddyName(obj.getBuddyName());
								Log.d("999", "relay client does not matches");
//								relayNew.sendVideoStopMessage(
//										getLoginUsername(), obj.getSessionid(),
//										obj.getBuddyName());
								relayNew.sendRelayVideoReceiveMessage(
										 obj.getSessionid(),getLoginUsername(),
										obj.getBuddyName());

							}
						}

					}

				}

				// }

			}
		}
	}

	public void turnOnOffVideo(SignalingBean sb){
		if (proprietarySignalling != null) {

			sb.setFrom(sb.getFrom().toLowerCase());

			sb.setSignalid(Long.toString(utility.getRandomMediaID()));
			sb.setPublicip(publicInetaddress);
			sb.setLocalip(localInetaddress);
			Log.d("OnOff",
					"ToUser " + sb.getTo() + ", TR " + sb.getType()
							+ sb.getResult() + " ,Signalid " + sb.getSignalid());

				proprietarySignalling.sendMessage(sb);
				if (!sb.getType().equals("9")) {

					proprietarySignalling.startRingTimer(sb);
				}

			if (sb.getType().equals("9")) {

				Log.d("OnOff",
						"sending type 9 from doaddconference "
								+ sb.getGmember() + " TO " + sb.getTo());
				CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
						.get(sb.getSessionid() + sb.getGmember());
				if (callsOverInternet != null) {
					decodessrc = callsOverInternet.getBuddyVideoSSRC();
					Log.d("OnOff", "Decode ssrc" + decodessrc);
				}

				if (sb.getTo().equals(sb.getGmember())) {
					try {
						Log.d("OnOff",
								"True" + " sb.getGmember() " + sb.getGmember()
										+ " To " + sb.getTo());

						CallsOverInternet c1 = (CallsOverInternet) callTable
								.get(sb.getSessionid() + sb.getTo());
						if (c1 != null) {
							c1.getRtpEngine().resetRetransmision(true);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {

					try {

						CallsOverInternet c1 = (CallsOverInternet) callTable
								.get(sb.getSessionid() + sb.getTo());
						if (c1 != null) {
							c1.getRtpEngine().resetRetransmision(false);
//							c1.getRtpEngine().resetRetransmision(true);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					// engine.resetRetransmision(false);
				}

				// CallsOverInternet obj=(CallsOverInternet)
				// callTable.get(sb.getSessionid()+sb.getTo());

				if (sb.getTo().equals(sb.getGmember())) {
					CallsOverInternet obj = (CallsOverInternet) callTable
							.get(sb.getSessionid() + sb.getTo());
					Log.d("OnOff", "sb.getTo().equals(sb.getGmember())" );
					if (obj != null) {
						Log.d("OnOff", "obj != null" );
						if (obj.isJoinedRelay()) {
							Log.d("OnOff", "obj.isJoinedRelay()" );
							RelayClientNew relayNew = (RelayClientNew) callTable
									.get(sb.getSessionid());
							if (relayNew != null) {
								Log.d("OnOff", "relayNew != null" );
								relayNew.setVideoSsrc(decodessrc);
								relayNew.setBuddyName(obj.getBuddyName());
								relayNew.sendRelayVideoReceiveMessage(
										obj.getSessionid(), getLoginUsername(),
										obj.getBuddyName());

							}
						}

					}
				} else {
					CallsOverInternet obj = (CallsOverInternet) callTable
							.get(sb.getSessionid() + sb.getTo());
					if (obj != null) {

						if (obj.isJoinedRelay()) {

							RelayClientNew relayNew = (RelayClientNew) callTable
									.get(sb.getSessionid());
							if (relayNew != null) {
								relayNew.setVideoSsrc(decodessrc);
								relayNew.setBuddyName(obj.getBuddyName());
								Log.d("OnOff", "relay client does not matches");
								relayNew.sendVideoStopMessage(
										getLoginUsername(), obj.getSessionid(),
										obj.getBuddyName());

							}
						}

					}

				}

				// }

			}
		}
	}

	public void makeContactConference(SignalingBean sb) {
		if (proprietarySignalling != null) {

			sb.setFrom(sb.getFrom().toLowerCase());

			sb.setSignalid(Long.toString(utility.getRandomMediaID()));
			sb.setPublicip(publicInetaddress);
			sb.setLocalip(localInetaddress);
			Log.d("MC",
					"ToUser " + sb.getTo() + ", TR " + sb.getType()
							+ sb.getResult() + " ,Signalid " + sb.getSignalid()
							+ " session " + sb.getSessionid());

			if (sb.getType().equals("0")) {

				Log.i("call123", "Name : " + sb.getTo());
				sb.setCallSubType(getCallSubtype(sb.getCallType()));

				if (!confRequestTable.containsKey(sb.getTo())) {
					confRequestTable.put(sb.getTo(), "");
					proprietarySignalling.sendMessage(sb);
					proprietarySignalling.startRingTimer(sb);

				}
			} else {
				proprietarySignalling.sendMessage(sb);
				if (!sb.getType().equals("9")) {

					proprietarySignalling.startRingTimer(sb);
				}

			}
		}

	}

	private String getCallSubtype(String callType) {
		if (callType.equalsIgnoreCase("AC")) {
			if (callTable.size() > 0) {

				return "5";
			}
			return "1";

		} else if (callType.equalsIgnoreCase("VC")) {
			if (callTable.size() > 0) {
				return "6";
			}

			return "2";
		} else if (callType.equalsIgnoreCase("AP")) {
			return "3";
		} else if (callType.equalsIgnoreCase("VP")) {
			return "4";
		} else if (callType.equalsIgnoreCase("ABC")) {
			return "7";
		} else if (callType.equalsIgnoreCase("VBC")) {
			return "8";
		} else if (callType.equalsIgnoreCase("SS")) {
			return "9";
		}

		return "";
	}

	// private boolean returnConference()
	// {
	// int i=0;
	// if(callTable.size()>0){
	//
	// Set set=callTable.keySet();
	//
	// Iterator<String> itr=set.iterator();
	// while(itr.hasNext()){
	// Object obj=callTable.get(itr.next());
	//
	// if(obj instanceof CallsOverInternet){
	//
	// i+=1;
	// }
	//
	//
	// else if(obj instanceof RelayClientNew){
	//
	// }
	//
	// }
	// }
	//
	// if()
	// {
	//
	// }
	//
	// //return "";
	// }

	/**
	 * Used to HangUp the Call like AudioCall,VideoCall etc.
	 * 
	 * @param sb
	 *            Signaling Information.
	 */
	public void hangupCall(SignalingBean sb) {

		try {
			stopRecording();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("call", new Date() + "close the call 000");
		if (proprietarySignalling != null) {
			// System.out.println("###################3 HANGUP CALL ########################3"+"$$$$$ "+sb.getTolocalip()+" $$$$$ "+sb.getTopublicip()+" ## "+sb.getFrom()+" ## "+sb.getTo()+" ## ");
			sb.setFrom(sb.getFrom().toLowerCase());

			/*
			 * //newly added String tolocalip=sb.getLocalip(); String
			 * topublicip=sb.getPublicip();
			 * 
			 * sb.setTolocalip(tolocalip); sb.setTopublicip(topublicip);
			 * sb.setToSignalPort(sb.getSignalport());
			 */

			if (sb.getTolocalip() == null || sb.getTopublicip() == null) {
				String tolocalip = sb.getLocalip();
				String topublicip = sb.getPublicip();

				sb.setTolocalip(tolocalip);
				sb.setTopublicip(topublicip);
				sb.setToSignalPort(sb.getSignalport());
			}

			sb.setPublicip(publicInetaddress);
			sb.setLocalip(localInetaddress);

			proprietarySignalling.stopCallTimer(sb.getSignalid());
			proprietarySignalling.sendMessage(sb);
			// stopRelayClient(sb.getSessionid());

			removeEntryFromConfRequestTable(sb.getTo());

			if (!sb.getCallType().toLowerCase().equals("ss")) {
				Log.d("Close", "close the call 000");
				clean();
			}

		}

	}

	/**
	 * Used to Stop the Relay Client. Also used to stop the Appropriate
	 * RTPEngine.
	 * 
	 * @param sessionid
	 *            UniqueId for a Session.
	 */
	public void stopRelayClient(String sessionid) {
		try {
			if (callTable.containsKey(sessionid)) {
				Object obj = callTable.get(sessionid);
				if (obj instanceof RelayClientNew) {
					RelayClientNew relayClient = (RelayClientNew) callTable
							.get(sessionid);
					relayClient.getRtpEngine().stopRTPEngine();
					relayClient = null;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Used to Clean all the Resources related to the Buddy.
	 * 
	 * @param key
	 *            UniqueId
	 * @param sessionid
	 *            UniqueId for a Session.
	 * @param from
	 *            From user
	 */
	public void cleanBuddyResources(String key, String sessionid, String from) {
		Log.d("call",
				"cleanBuddyResources ********************************************************************** ");
		if (callTable.size() > 0) {

			// System.out.println(" callTable "+callTable+" key :"+key);
			Object obj = (CallsOverInternet) callTable.get(key);
			if (obj != null && obj instanceof CallsOverInternet) {
				CallsOverInternet callsOverInternet = (CallsOverInternet) obj;

				callsOverInternet.getRtpEngine().stopRTPEngine();
				callsOverInternet.setRelayMediaJoinRunning(false);
				callsOverInternet.setRelayRunning(false);
				callsOverInternet.stopAudioPlayer();

				if (callsOverInternet.isJoinedRelay()) {
					String data = "0001," + callsOverInternet.getSessionid()
							+ loginUsername;
					RtpEngine rtpEngine = callsOverInternet.getRtpEngine();
					// rtpEngine.addRemoteEndpoint(12345,relayServerip,relayServerPort);
					// rtpEngine.sendData(12345,relayServerip, data.getBytes());
					// System.out.println("############################################# Relay leave message sent"+new
					// String(data));
					// rtpEngine.removeRemoteEndpoint(12345,relayServerip);

				}
			}
			// else if(obj instanceof RelayClient){
			// RelayClient relayClient=(RelayClient)obj;
			// relayClient.getRtpEngine().stopRTPEngine();
			// }
			else if (obj instanceof RelayClientNew) {

				RelayClientNew relayClient = (RelayClientNew) obj;
				relayClient.getRtpEngine().stopRTPEngine();

			}

			// System.out.println("getConferenceTable :"+getConferenceTable());
			ArrayList<SignalingBean> list = getConferenceTable().get(sessionid);

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					SignalingBean sg = list.get(i);
					// System.out.println("### BYE ###");
					// System.out.println("sg.getTo() : "+sg.getTo()+" From() : "+from);
					if (sg.getTo().equals(from)) {
						list.remove(i);
					}
				}
				if (list.size() == 0) {
					stopRelayClient(key);
					micMute(false);
					stopAudioCapture();
					CommunicationEngine.relayJoined = false;
				}
			}
			removeMediaKey(sessionid, from);

		}
	}

	// added on march 14
	/**
	 * used to Initialize VideoCodec and also used to Start VideoThread to
	 * Process Video Data.
	 */
	public void initCodec() {
		Log.d("thread", "Going to Thread started ");
		initializeVideoCodec();
		Log.d("thread", "Thread started initvideocodec");
		speex = Speex.getInstance();
		Log.d("thread", "Thread started speex");
		Log.d("VIDEO", "Init codec ");
		videoThread = new VideoThread(videoQueue);
		videoThread.setCommunicationEngine(this);
		videoThread.setPriority(10);
		videoThread.start();

		videoSendThread = new VideoSenderThread(videoRtpQueue, this);
		videoSendThread.start();

		initializeDecoder();

	}

	/**
	 * Used to Clean all the Resources Related to the Service.
	 */
	public void clean() {
		try {
			Log.d("SM",
					"^^^^^^^^^^^^^^^^  Relay media Receive Notification else ########################");
			Log.d("call",
					"Clean ********************************************************************** ");
			stopAudioCapture();
			callState.clear();
			confRequestTable.clear();
			audioPlayedQueue.clear();
			Store.packetStoreForSend.clear();
			if (callTable.size() > 0) {

				Set set = callTable.keySet();

				Iterator<String> itr = set.iterator();
				while (itr.hasNext()) {
					Object obj = callTable.get(itr.next());

					if (obj instanceof CallsOverInternet) {
						CallsOverInternet callsOverInternet = (CallsOverInternet) obj;

						callsOverInternet.getRtpEngine().stopRTPEngine();
						callsOverInternet.setRelayMediaJoinRunning(false);
						callsOverInternet.setRelayRunning(false);
						callsOverInternet.stopAudioPlayer();

						if (callsOverInternet.getFrameTimer() != null) {
							callsOverInternet.getFrameTimer().cancel();
						}
						if (callsOverInternet.isJoinedRelay()) {
							// String
							// data="0001,"+callsOverInternet.getSessionid()+loginUsername;
							// RtpEngine
							// rtpEngine=callsOverInternet.getRtpEngine();
							// rtpEngine.addRemoteEndpoint(12345,relayServerip,relayServerPort);
							// rtpEngine.sendData(12345,relayServerip,
							// data.getBytes());
							// //System.out.println("############################################# Relay leave message sent"+new
							// String(data));
							// rtpEngine.removeRemoteEndpoint(12345,relayServerip);

						}
					}

					// else if(obj instanceof RelayClient){
					// RelayClient relayClient=(RelayClient)obj;
					// String
					// sessionClosed="0001,"+currentSession+","+getLoginUsername();
					// //
					// relayClient.getRtpEngine().addRemoteEndpoint(12345,relayServerip,relayServerPort);
					// //
					// relayClient.getRtpEngine().sendData(12345,relayServerip,
					// sessionClosed.getBytes());
					// //
					// // relayClient.getRtpEngine().removeRemoteEndpoint(12345,
					// relayServerip);
					//
					//
					//
					// relayClient.getRtpEngine().stopRTPEngine();
					// }
					else if (obj instanceof RelayClientNew) {
						RelayClientNew relayClient = (RelayClientNew) obj;
						String sessionClosed = "0001," + currentSession + ","
								+ getLoginUsername();
						relayClient.sendSessionClosedMessage(sessionClosed);
						relayClient.getRtpEngine().stopRTPEngine();
						relayClient.stop();
						relayClient = null;
					}

				}
			}

			// Used to cancel Timers
			if (proprietarySignalling.callTimer.size() > 0) {
				//
				try {
					Set<String> set = proprietarySignalling.callTimer.keySet();

					String Key = null;
					Iterator<String> iterator = set.iterator();

					while (iterator.hasNext()) {
						// System.out.println("main screen alert");
						Key = iterator.next();

						Timer callTimer = proprietarySignalling.callTimer
								.get(Key);

						if (callTimer != null) {
							callTimer.cancel();
							Log.d("TIMERS", "CallTimer stop");
							// System.out.println("### CALL TIMER CANCELD FOR SIGNAL : "
							// + signalid);
						}
						proprietarySignalling.callTimer.remove(Key);

					}

				} catch (Exception e) {
					Log.d("stattus", "completed  vcvcv " + e);
				}

				//
			}

			//

			Log.d("call",
					"Clean ********************************************************************** ");

			callTable.clear();
			conferenceTable.clear();
			mediaKey.clear();
			CommunicationEngine.relayJoined = false;
			resetVpxDecoderIndex();
			videoSqno = 0;
		} catch (Exception e) {
			Log.d("call", new Date() + "close the call 09090909" + e.toString());
			e.printStackTrace();
		}
	}

	public void resetAEC() {
		try {
			Log.d("RESETAEC", "Session Count " + callTable.size());
			if (callTable.size() == 1) {
				Set set = callTable.keySet();

				Iterator<String> itr = set.iterator();
				while (itr.hasNext()) {
					Object obj = callTable.get(itr.next());
					if (obj instanceof CallsOverInternet) {
						CallsOverInternet callsOverInternet = (CallsOverInternet) obj;
						if (callsOverInternet.getAudioPlayer() != null) {
							Log.d("RESETAEC", "AEC made true ");
							callsOverInternet.getAudioPlayer().setmDuplex(true);
						}

					}
				}

			} else {
				Set set = callTable.keySet();

				Iterator<String> itr = set.iterator();
				while (itr.hasNext()) {
					Object obj = callTable.get(itr.next());
					if (obj instanceof CallsOverInternet) {
						CallsOverInternet callsOverInternet = (CallsOverInternet) obj;
						if (callsOverInternet.getAudioPlayer() != null) {
							Log.d("RESETAEC", "AEC made false ");
							callsOverInternet.getAudioPlayer()
									.setmDuplex(false);
						}

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove media key from the ArrayList.
	 * 
	 * @param sessionid
	 *            Unique SessionId
	 * @param buddy
	 *            BuddyName
	 */
	public void removeMediaKey(String sessionid, String buddy) {
		mediaKey.remove(sessionid + buddy);
	}

	/**
	 * This method is Used to Accept or Reject the ScreenShare Request.
	 * 
	 * @param sb
	 *            signaling Information.
	 */
	public void acceptRejectshareScreen(SignalingBean sb) {
		if (proprietarySignalling != null) {
			String sessionid = sb.getSessionid();
			String from = sb.getFrom();
			String to = sb.getTo();

			String tolocalip = sb.getLocalip();
			String topublicip = sb.getPublicip();
			sb.setFrom(sb.getFrom().toLowerCase());
			sb.setTolocalip(tolocalip);
			sb.setTopublicip(topublicip);
			sb.setToSignalPort(sb.getSignalport());

			sb.setPublicip(publicInetaddress);
			sb.setLocalip(localInetaddress);

			proprietarySignalling.sendMessage(sb);
		}
	}

	public ArrayList<String> getUndeliveredIMSignalId(String strSessionId,
			String strTo) {

		ArrayList<String> alSignalIds = new ArrayList<String>();
		try {
			InstantMessage instantMessage = (InstantMessage) imHashmap
					.get(strSessionId + strTo);
			ArrayList<SignalingBean> alMsgList = instantMessage
					.getMessageList();
			for (int i = 0; i < alMsgList.size(); i++) {
				alSignalIds.add(alMsgList.get(i).getSignalid());
			}
		} catch (Exception e) {

		}
		return alSignalIds;

	}

	public HashMap<String, ArrayList<String>> getUndeliveredIMDetails() {

		HashMap<String, ArrayList<String>> hsUDIM = new HashMap<String, ArrayList<String>>();
		try {

			Set set = imHashmap.entrySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
				final Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				InstantMessage instantMessage = (InstantMessage) imHashmap
						.get(key);
				ArrayList<SignalingBean> alMsgList = instantMessage
						.getMessageList();
				if (alMsgList.size() > 0) {
					String keyValue = null;
					ArrayList<String> strAl = new ArrayList<String>();
					for (int idx = 0; idx < alMsgList.size(); idx++) {
						if (keyValue == null)
							keyValue = alMsgList.get(idx).getSessionid();

						strAl.add(alMsgList.get(idx).getSignalid());
					}
					if (keyValue != null && strAl.size() != 0) {
						hsUDIM.put(keyValue, strAl);
					}
				}

			}

		} catch (Exception e) {

		}
		return hsUDIM;

	}

	/**
	 * Used to Reject The Incoming Service.
	 * 
	 * @param sb
	 *            signaling Information.
	 */
	public void rejectCall(SignalingBean sb) {
		if (proprietarySignalling != null) {

			proprietarySignalling.stopCallTimer(sb.getSignalid());

			proprietarySignalling.sendMessage(sb);
		}
	}

	/**
	 * Used to Make the Conference AudioCall or VideoCall.
	 * 
	 * @param sb
	 *            signaling Information.
	 */
	public void makeConferenceCall(SignalingBean sb) {
		sb.setFrom(sb.getFrom().toLowerCase());
		sb.setSignalid(Long.toString(utility.getRandomMediaID()));

		sb.setPublicip(publicInetaddress);
		sb.setLocalip(localInetaddress);
		sb.setCallSubType(getCallSubtype(sb.getCallType()));

		switch (enumSignallingType) {

		case PROPRIETARY:
			if (proprietarySignalling != null) {

				Log.d("1211",
						"From " + sb.getFrom() + "  to " + sb.getTo()
								+ " Type " + sb.getType() + " signalid "
								+ sb.getSignalid());
				proprietarySignalling.sendMessage(sb);
			}

			break;
		}

	}

	// public void makeConferenceCallRequest(SignalingBean sb){
	// sb.setFrom(sb.getFrom().toLowerCase());
	// sb.setSignalid(Long.toString(utility.getRandomMediaID()));
	//
	// sb.setPublicip(publicInetaddress);
	// sb.setLocalip(localInetaddress);
	//
	//
	// switch (enumSignallingType) {
	//
	// case PROPRIETARY:
	// if(proprietarySignalling!=null){
	//
	// Log.d("1211",
	// "From "+sb.getFrom()+"  to "+sb.getTo()+" Type "+sb.getType()+" signalid "+sb.getSignalid());
	// proprietarySignalling.sendMessage(sb);
	// }
	//
	// break;
	// }
	//
	// }
	//

	/**
	 * used to get relay Server Port.
	 * 
	 * @return Relay Server Port.
	 */
	public int getRelayServerPort() {
		return relayServerPort;
	}

	/**
	 * used to get relay Server IpAddress
	 * 
	 * @return Relay Server IpAddress.
	 */
	public String getRelayServerip() {
		return relayServerip;
	}

	/**
	 * Used to Accept the Service like AudioCall,VideoCall,InstantMessage etc.
	 * 
	 * @param sb
	 *            signaling Information.
	 */
	public void acceptCall(SignalingBean sb) {

		// Log.d("test", "accept call from UI alert");
		// Log.d("TIMERXX","Before call Accept Timer started 15secs  "+sb.getSignalid());

		String sessionid = sb.getSessionid();
		String from = sb.getFrom();
		String to = sb.getTo();

		String tolocalip = sb.getLocalip();
		String topublicip = sb.getPublicip();

		sb.setFrom(sb.getFrom().toLowerCase());
		sb.setTolocalip(tolocalip);
		sb.setTopublicip(topublicip);
		sb.setToSignalPort(sb.getSignalport());

		sb.setPublicip(publicInetaddress);
		sb.setLocalip(localInetaddress);

		// used to check and establish Relay connection...
		boolean isRelay = false;

		Log.i("call", "Checking call subtype.........." + sb.getCallSubType());
		if (sb.getCallSubType() == null) {
			isRelay = false;
		} else {
			isRelay = isRelay(sb);
			Log.i("call", "Checking call subtype null .........." + isRelay);
		}
		Log.i("call", "Checking call subtype sb type .........." + sb.getType());
		// boolean isRelay=true;
		// Log.d("TYPE6", "isRelay "+isRelay);
		// Log.d("SM","accept call  "+isRelay);
		// Log.d("SM","accept call user "+sb.getTo());

		// System.out.println("My Public ip:"+sb.getPublicip()+" To Public up :"+sb.getTopublicip()+" To localip :"+sb.getTolocalip());
		// System.out.println();
		// Log.d("test", "accept call from UI alert 2");

		if (sb.getType().equals("1") && sb.getResult().equals("0")) {
			proprietarySignalling.stopCallTimer(sb.getSignalid());
			// proprietarySignalling.startAcceptTimer(sb);
		}
		// Log.d("test", "accept call from UI alert 3");

		switch (enumSignallingType) {

		case PROPRIETARY:
			if (proprietarySignalling != null) {

				Log.i("call",
						"came to accept call"
								+ callTable.containsKey(sb.getSessionid()
										+ sb.getTo()));
				if (!callTable.containsKey(sb.getSessionid() + sb.getTo())) {

					this.currentSession = sb.getSessionid();
					// Log.d("TIMERXX","Before  33333 call Accept Timer started 15secs  "+sb.getSignalid());

					// Log.d("test", "accept call from UI alert 4");
					// Log.e("test",
					// "Engine  Comes to Accept call Inside of accept call");
					CallsOverInternet callsOverInternet = new CallsOverInternet(
							sb, proprietarySignalling, this, cbserver1,
							cbserver2, cbport1, cbport2, routerip,
							relayServerPort, relayServerip);

					if (sb.getType().equals("1") && sb.getResult().equals("0")) {
						// proprietarySignalling.stopCallTimer(sb.getSignalid());
						// proprietarySignalling.startAcceptTimer(sb);
						// changed for Relay.....

						proprietarySignalling.startAcceptTimer(sb,
								callsOverInternet);

					}

					//
					//

					if (sb.getCallSubType() == null) {
						callsOverInternet.setOldVersion(true);
					} else {
						callsOverInternet.setOldVersion(false);
					}

					callsOverInternet.setVideoQueue(videoQueue);
					callTable.put(sb.getSessionid() + sb.getTo(),
							callsOverInternet);

					if(sb.getVideossrc() != null) {
						Log.i("NotesVideo", "on CommunucationEngine : " + sb.getTo() + "  :  " + Integer.parseInt(sb.getVideossrc()));

						if(!WebServiceReferences.videoSSRC_total_list.contains(Integer.parseInt(sb.getVideossrc()))){
//							WebServiceReferences.videoSSRC_total_list.add(Integer.parseInt(sb.getVideossrc()));
						}

						if (!WebServiceReferences.videoSSRC_total.containsKey(Integer.parseInt(sb.getVideossrc()))) {
							Log.i("NotesVideo", "inside if");
							WebServiceReferences.videoSSRC_total.put(Integer.parseInt(sb.getVideossrc()), sb.getTo());
							Log.i("NotesVideo", "videoSSRC size : " + WebServiceReferences.videoSSRC_total.size());
						}
					}

					mediaKey.add(sb.getSessionid() + sb.getTo());
					conferenceMembers(sb);
					if (sb.getType().equals("1")) {
						// Log.d("test", "accept call from UI alert 5");

						callsOverInternet.sendRequestToServerOne(isRelay);

					}

					else if (sb.getType().equals("2")) {
						// new implementation for signalling time maintainance
						// Log.d("MSOT",
						// "check and allow on before send Type 2 "+maintainSignallingOnTime.containsKey(sb.getSessionid()+sb.getTo()));
						// Log.d("SM","Type2 ");
						Log.i("call", "came to sg gettype==2 ..........");
						Log.i("call",
								"came to sg gettype==2 .........."
										+ maintainSignallingOnTime
												.containsKey(sb.getSessionid()
														+ sb.getTo()));
						if (maintainSignallingOnTime.containsKey(sb
								.getSessionid() + sb.getTo())) {

							// if(sb.getCallSubType()==null)
							// {
							// callsOverInternet.setOldVersion(true) ;
							// }
							// else
							// {
							// callsOverInternet.setOldVersion(false) ;
							// }

							if (!isRelay) {
								RtpEngine engine = callsOverInternet
										.getRtpEngine();
								// Log.d("REP", "from comm1");
								Log.i("call",
										"going to call remote addremoteendpoint1 .........."
												+ sb.getBuddyConnectip()
												+ "......"
												+ sb.getBuddyConnectport());
								engine.addRemoteEndpoint(callsOverInternet
										.getAudiossrc(),
										sb.getBuddyConnectip(), Integer
												.parseInt(sb
														.getBuddyConnectport()));
								// Log.d("REP", "from comm1");
								callsOverInternet.setBuddyConnectip(sb
										.getBuddyConnectip());
								callsOverInternet.setBuddyConnectPort(Integer
										.parseInt(sb.getBuddyConnectport()));
								callsOverInternet.setBuddyAudioSSRC(Integer
										.parseInt(sb.getAudiossrc()));
								callsOverInternet.setBuddyVideoSSRC(Integer
										.parseInt(sb.getVideossrc()));
								// decodessrc=Integer.parseInt(sb.getVideossrc());

								// used to set audioSsrc and Video Ssrc on Rtp
								// engine...
								engine.setBuddyAudioSsrc(Integer.parseInt(sb
										.getAudiossrc()));
								engine.setBuddyVideoSsrc(Integer.parseInt(sb
										.getVideossrc()));

								engine.setMyVideoSsrc(callsOverInternet
										.getVideossrc());
								engine.setMyAudioSsrc(callsOverInternet
										.getAudiossrc());

								if (sb.getCallType().equals("VC")
										|| sb.getCallType().equals("VBC")
										|| sb.getCallType().equals("VP")
										|| sb.getCallType().equals("SS")) {
									// Log.d("REP", "from comm2");
									Log.i("call",
											"going to call remote addremoteendpoint .........."
													+ sb.getBuddyConnectip()
													+ "......"
													+ sb.getBuddyConnectport());
									engine.addRemoteEndpoint(callsOverInternet
											.getVideossrc(), sb
											.getBuddyConnectip(), Integer
											.parseInt(sb.getBuddyConnectport()));
									// showBuddyVideo(buddy, sessionid)
									// Log.d("REP", "from comm2");
									decodessrc = Integer.parseInt(sb
											.getVideossrc());

								}

								if (sb.getPunchingmode().equals("0")) {

								} else if (sb.getPunchingmode().equals("3")) {
									// Tring to do port logic prediction.....
									callsOverInternet.setSymmetric(true);
									// callsOverInternet.se
									engine.setSymmetric(true);
									engine.setHaveToSetPort(true);
									// s
									// Log.d("PORTSCAN",
									// "processing port scanning... ******************************");

									doPortScanning(callsOverInternet,
											callsOverInternet
													.getBuddyConnectip(),
											callsOverInternet
													.getBuddyConnectPort());

								}

								else {

									// do port prediction logic here
								}
								callsOverInternet
										.sendRequestToServerOne(isRelay);
							} else {
								// Log.d("SM","Type2 Relay");
								// used to Initiate Relay code.....
								// joinRelaynew(sb.getSessionid(), sb.getTo(),
								// Long.valueOf(sb.getAudiossrc()),
								// Long.valueOf(sb.getVideossrc()),
								// sb.getSessionid()+sb.getTo(),sb.getCallType(),
								// true);

								callsOverInternet.setBuddyConnectip(sb
										.getBuddyConnectip());
								callsOverInternet.setBuddyConnectPort(Integer
										.parseInt(sb.getBuddyConnectport()));
								callsOverInternet.setBuddyAudioSSRC(Integer
										.parseInt(sb.getAudiossrc()));
								callsOverInternet.setBuddyVideoSSRC(Integer
										.parseInt(sb.getVideossrc()));
								callsOverInternet.setJoinedRelay(true);
								// Log.d("SIGNAL",
								// "on Relayyyyyyyyyyyyyyyy ****************");
								// decodessrc=Integer.parseInt(sb.getVideossrc());

								// used to set audioSsrc and Video Ssrc on Rtp
								// engine...

								if (sb.getCallType().equals("VC")
										|| sb.getCallType().equals("VBC")
										|| sb.getCallType().equals("VP")
										|| sb.getCallType().equals("SS")) {
									// engine.addRemoteEndpoint(callsOverInternet.getVideossrc(),sb.getBuddyConnectip(),Integer.parseInt(sb.getBuddyConnectport()));
									// showBuddyVideo(buddy, sessionid)
									decodessrc = Integer.parseInt(sb
											.getVideossrc());

								}

								// callsOverInternet.sendSignalAfterJoin();
								callsOverInternet
										.sendRequestToServerOne(isRelay);
							}

							// Added on may25 Frame Timer
							if (!sb.getCallType().equals("AP")
									&& !sb.getCallType().equals("VP")
									&& !sb.getCallType().equals("ABC")
									&& !sb.getCallType().equals("VBC")
									&& !sb.getCallType().equals("SS")) {
								// callsOverInternet.stopMediaReceiveTimer();
								callsOverInternet.startTimer();
							}
							//

						}

					}
					if (sb.getType().equals("2")) {

						Log.i("call",
								"Checking call gettype2 call doadd conf .........."
										+ isRelay);

						// send

						doAddConference(sb.getTo(), sb.getTolocalip(),
								sb.getTopublicip(), sb.getSessionid(),
								sb.getToSignalPort());

						// send
						if (callTable.containsKey(sb.getSessionid())) {
							Object object = callTable.get(sb.getSessionid());
							if (object instanceof RelayClientNew) {
								// Log.d("TYPE6", "sending Type6");
								// Log.d("SM"," Type6");
								sendType6ForExsistingUser();
							}
						}

						// Implementation of Relay Server MediaTimer.....

						// String callType=sb.getCallType();
						// if(callType.equalsIgnoreCase("AP")||callType.equalsIgnoreCase("VP")||callType.equalsIgnoreCase("VBC")||callType.equalsIgnoreCase("ABC"))
						// {
						//
						// if(callTable.containsKey(sb.getSessionid()))
						// {
						// Object object=callTable.get(sb.getSessionid());
						// if(object instanceof RelayClientNew)
						// {
						// if(callsOverInternet.isBuddyInNat()&&callsOverInternet.isJoinedRelay())
						// {
						// RelayClientNew relay=(RelayClientNew)object;
						// relay.startDummyPacketTimer(getLoginUsername(),
						// sb.getSessionid(), sb.getTo());
						//
						// }
						//
						// }
						// }
						// }

					}

				}

			}

			break;

		default:
			break;
		}
	}

	public void startRelayMediaTimer() {

	}

	public void doPortScanning(CallsOverInternet calls, String ipAddress,
			int port) {
		try {
			// Log.d("PORTSCAN", "processing port scanning..."+port);
			for (int i = port - 500; i < port + 500; i++) {
				// Log.d("PORTSCAN", "proceesingg "+i);
				calls.getRtpEngine().sendDummyPackets(ipAddress, port);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void sendType6ForExsistingUserAndcurrentUser(
			CallsOverInternet callsOver) {

		try {
			// Switch the MediaFailureUser to Relay...

			if (!callsOver.isJoinedRelay() && !callsOver.isOldVersion()) {

				callsOver.JoinRelayNew(true);
				callsOver.setAllowAudio(false);
				callsOver.setAllowMedia(false);
				callsOver.sendRelayJoinSignalling();
				// joinRelayOnTypeSix(callsOverInternet.getSessionid(),
				// callsOverInternet.getBuddyName(),
				// Long.valueOf(callsOverInternet.getBuddyAudioSSRC()),
				// Long.valueOf(callsOverInternet.getBuddyVideoSSRC()),
				// callsOverInternet.getSessionid()+callsOverInternet.getBuddyName(),callsOverInternet.getSignallingBean().getCallType());
				callsOver.setJoinedRelay(true);

				// Also send Type 9 for receive Video.....

			}

			// Used to call exsisting NAT P to P user for Relay...
			if (callTable.size() > 0) {

				Set set = callTable.keySet();

				Iterator<String> itr = set.iterator();
				while (itr.hasNext()) {
					Object obj = callTable.get(itr.next());

					if (obj instanceof CallsOverInternet) {
						CallsOverInternet callsOverInternet = (CallsOverInternet) obj;

						if (!callsOverInternet.isJoinedRelay()
								&& !callsOverInternet.isOldVersion()) {

							// Log.d("TYPE6",
							// "sending Type6 for "+callsOverInternet.getBuddyName());
							// Log.d("SM",
							// "sending Type6 for "+callsOverInternet.getBuddyName());
							callsOverInternet.sendRelayJoinSignalling();
							callsOverInternet.setAllowAudio(false);
							callsOverInternet.setAllowMedia(false);
							// Log.d("SM"," joinRelayOnTypeSix");
							joinRelayOnTypeSix(
									callsOverInternet.getSessionid(),
									callsOverInternet.getBuddyName(),
									Long.valueOf(callsOverInternet
											.getBuddyAudioSSRC()),
									Long.valueOf(callsOverInternet
											.getBuddyVideoSSRC()),
									callsOverInternet.getSessionid()
											+ callsOverInternet.getBuddyName(),
									callsOverInternet.getSignallingBean()
											.getCallType());
							callsOverInternet.setJoinedRelay(true);

						}

					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendType4(long ssrc, int seqNo, String buddy) {

		// used to send request for Rtp PAcket................
		SignalingBean sb = new SignalingBean();
		sb.setFrom(getLoginUsername());
		sb.setTo(buddy);
		sb.setLocalip(getLocalInetaddress());
		sb.setPublicip(getPublicInetaddress());
		sb.setSsrc(Long.toString(ssrc));
		sb.setSeqNo(Integer.toString(seqNo));
		sb.setSignalid(Long.toString(utility.getRandomMediaID()));
		sb.setSessionid(currentSession);

		sb.setType("4");
		sb.setResult("0");
		proprietarySignalling.sendRtpRequestMessage(sb);

	}

	private void sendType6ForExsistingUser() {
		// Used to call exsisting NAT P to P user for Relay...
		if (callTable.size() > 0) {

			Set set = callTable.keySet();

			Iterator<String> itr = set.iterator();
			while (itr.hasNext()) {
				Object obj = callTable.get(itr.next());

				if (obj instanceof CallsOverInternet) {
					CallsOverInternet callsOverInternet = (CallsOverInternet) obj;

					if (callsOverInternet.isBuddyInNat()
							&& !callsOverInternet.isJoinedRelay()
							&& !callsOverInternet.isOldVersion()) {

						// Log.d("TYPE6",
						// "sending Type6 for "+callsOverInternet.getBuddyName());
						// Log.d("SM",
						// "sending Type6 for "+callsOverInternet.getBuddyName());
						callsOverInternet.sendRelayJoinSignalling();
						callsOverInternet.setAllowAudio(false);
						callsOverInternet.setAllowMedia(false);
						// Log.d("SM"," joinRelayOnTypeSix");
						joinRelayOnTypeSix(callsOverInternet.getSessionid(),
								callsOverInternet.getBuddyName(),
								Long.valueOf(callsOverInternet
										.getBuddyAudioSSRC()),
								Long.valueOf(callsOverInternet
										.getBuddyVideoSSRC()),
								callsOverInternet.getSessionid()
										+ callsOverInternet.getBuddyName(),
								callsOverInternet.getSignallingBean()
										.getCallType());
						callsOverInternet.setJoinedRelay(true);

					} else if (!callsOverInternet.isJoinedRelay()
							&& !callsOverInternet.isOldVersion()) {
						callsOverInternet.sendRelayJoinSignalling();
						callsOverInternet.setAllowAudio(false);
						callsOverInternet.setAllowMedia(false);
						// Log.d("SM"," joinRelayOnTypeSix");
						joinRelayOnTypeSix(callsOverInternet.getSessionid(),
								callsOverInternet.getBuddyName(),
								Long.valueOf(callsOverInternet
										.getBuddyAudioSSRC()),
								Long.valueOf(callsOverInternet
										.getBuddyVideoSSRC()),
								callsOverInternet.getSessionid()
										+ callsOverInternet.getBuddyName(),
								callsOverInternet.getSignallingBean()
										.getCallType());
						callsOverInternet.setJoinedRelay(true);

					}

				}

			}
		}
	}

	public boolean isRelay(SignalingBean sb) {

		// if(sb.)
		// {
		//
		// }

		try {

			// Log.d("SIGNAL", "isRelay(SignalingBean sb)");
			// Log.d("SIGNAL", "isRelay(SignalingBean sb)"+sb.getCallSubType());
			if (sb.getBuddyConnectip() != null
					&& (sb.getBuddyConnectip().equals(relayServerip))) {
				return true;
			}

			else if (sb.getCallSubType().equals("5")
					|| sb.getCallSubType().equals("6")
					|| sb.getCallSubType().equals("7")
					|| sb.getCallSubType().equals("8")) {
				if (isNat(sb)) {
					return true;
				} else if (sb.getCallSubType().equals("5")
						|| sb.getCallSubType().equals("6")
						|| sb.getCallSubType().equals("7")
						|| sb.getCallSubType().equals("8")) {
					return true;
				}
				// return
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean isNat(SignalingBean sb) {
		if (!sb.getPublicip().equals(sb.getTopublicip())) {
			if (!sb.getLocalip().equals(sb.getPublicip())) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Used to check and remove Accept Call Timer on Race case from UI on
	 * Receiving type3...
	 */
	public void removeAcceptCallTimer(String signalId) {
		try {
			Log.d("TIMERXX", "Trying To remove2222222222222 " + signalId);
			if (proprietarySignalling != null) {

				proprietarySignalling.stopCallTimer(signalId);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Used to get cbServer1 IpAddress.
	 * 
	 * @return cbServer2 IpAddress
	 */
	public String getCbserver1() {
		return cbserver1;
	}

	/**
	 * Used to get cbServer2 IpAddress.
	 * 
	 * @return cbServer2 IpAddress
	 */
	public String getCbserver2() {
		return cbserver2;
	}

	/**
	 * Used to get cbServer1 Port.
	 * 
	 * @return cbServer1 Port.
	 */
	public int getCbport1() {
		return cbport1;
	}

	/**
	 * Used to get cbServer2 Port.
	 * 
	 * @return cbServer2 Port.
	 */
	public int getCbport2() {
		return cbport2;
	}

	/**
	 * Used to Start a New Conference Call Service
	 * 
	 * @param sb
	 *            Signaling information.
	 */
	public void startNewConferenceCall(SignalingBean sb) {
		Log.d("MC",
				" on startNewConferenceCall ToUser " + sb.getTo() + ", TR "
						+ sb.getType() + sb.getResult() + " ,Signalid "
						+ sb.getSignalid());
		Log.d("1211",
				" on startNewConferenceCall ToUser " + sb.getTo()
						+ ", from user " + sb.getFrom() + " TR " + sb.getType()
						+ sb.getResult() + " ,Signalid " + sb.getSignalid());
		sb.setFrom(sb.getFrom().toLowerCase());
		// System.out.println("startNewConferenceCall : "+sb.getTo());

		// System.out.println("Call Table "+callTable);
		if (callTable.containsKey(sb.getSessionid() + sb.getFrom())) {
			SignalingBean bean = new SignalingBean();

			// System.out.println("sb :"+sb);
			// System.out.println("sb.getConferencemember() :"+sb.getConferencemember());

			String values[] = sb.getConferencemember().split(",");
			// System.out.println("values.length ##############3 "+values.length);
			if (values.length > 2) {
				bean.setFrom(loginUsername);
				bean.setTo(values[0]);

				// System.out.println("During Conference............."+sb.getSessionid()+bean.getTo());
				// System.out.println("callTable :"+callTable);

				if (!callTable.containsKey(sb.getSessionid() + bean.getTo())) {

					bean.setSessionid(sb.getSessionid());
					bean.setCallType(sb.getCallType());
					bean.setToSignalPort(values[3]);
					bean.setType("0");
					bean.setSignalport(values[3]);
					bean.setTolocalip(values[1]);
					bean.setTopublicip(values[2]);
					Log.d("MC",
							" on startNewConferenceCall type0 ToUser "
									+ bean.getTo() + ", TR " + bean.getType()
									+ bean.getResult() + " ,Signalid "
									+ bean.getSignalid());
					if (!confRequestTable.containsKey(bean.getTo())) {
						Log.d("MC", " conf request table Not contains ");
						confRequestTable.put(bean.getTo(), "");
						makeConferenceCall(bean);
					} else {
						Log.d("MC",
								" on startNewConferenceCall type0 ToUser Already make conference Request");
					}
					// System.out.println("############## ########## makeConferenceCall(bean) ######### ");

				}

			}

		} else {
			// System.out.println("conference bean not valid .........");
		}
	}

	public void removeEntryFromConfRequestTable(String name) {
		if (confRequestTable.containsKey(name)) {
			confRequestTable.remove(name);
		} else {
			Log.d("REFC", "Not contains key");
		}

	}

	/**
	 * This method is used to Check add add the Buddy Details in Conference
	 * table.
	 * 
	 * @param sb
	 *            Signaling information.
	 */
	public void conferenceMembers(SignalingBean sb) {

		// System.out.println("######## conferenceMembers");
		// System.out.println("conferenceTable : "+conferenceTable);
		// System.out.println("sb.getSessionid(): "+sb.getSessionid());
		// System.out.println("From :"+sb.getFrom());
		// System.out.println("To :"+sb.getTo());
		Log.d("call",
				new Date() + "conference table entry " + sb.getSessionid());
		Log.d("call",
				"conference table entry "
						+ conferenceTable.containsKey(sb.getSessionid()));

		if (conferenceTable.containsKey(sb.getSessionid())) {
			ArrayList<SignalingBean> arrayList = conferenceTable.get(sb
					.getSessionid());
			arrayList.add(sb);
			// System.out.println("###Add New Member to confeence :"+sb.getTo());
			// System.out.println("size :"+arrayList.size());

		} else {

			ArrayList<SignalingBean> arrayList = new ArrayList<SignalingBean>();
			arrayList.add(sb);
			conferenceTable.put(sb.getSessionid(), arrayList);

			// System.out.println("################## Add New Member to confeence :"+sb.getTo());

		}
	}

	/**
	 * This method is used to Start AudioCapture by Starting AudioRecorder.
	 */
	public void startAudioCapture(String filePath, long warningLevel) {

		Log.i("call", "going to start audio capturing");
		Log.i("call", "going to start audio capturing" + audioRecorder);
		if (audioRecorder == null) {
			// String user = username + "_" + System.currentTimeMillis();
			audioRecorder = new AudioRecorder(0, audioPlayedQueue, filePath);
			audioRecorder.setAudioRecorderListener(this);
			audioRecorder
					.setAudioRecorderStateListener(audioRecordStateListener);
			audioRecorder.setAudioCodecFormat(AudioCodecFormat.EAudioSpeex);
			audioRecorder.Start();

			// Log.d("call",new Date()+"Start Audio Captureeeeeeee");
			/*
			 * audioThread=new AudioThread(mediaKey, callTable,
			 * audioFrameQueue); Thread thread=new Thread(audioThread);
			 * thread.start();
			 */

		}
	}

	public Queue getAudioPlayedQueue() {
		return audioPlayedQueue;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * This method is used to Initialize VideoCodec like VP8 or H264.
	 */
	public void initializeVideoCodec() {
		if (videoCodec == null) {
			Log.d("THREAD", "Video codec not null");
			videoCodec = new VideoCodec(EnumVideoCodec.H264);
			if (videoCodec.getEnumVideoCodec() == EnumVideoCodec.H264) {
				Log.d("THREAD", "Video code ffmpeggggg");
				videoCodec.InitializeFFmpeg(width, height, 256000, 0, 75, 25);
				Log.d("THREAD", "Video code ffmpeggggggzzzzzzzzzzzzzzzz");
				Log.d("CODEC", "FFMPEG InitializeFFmpeg ");
			} else if (videoCodec.getEnumVideoCodec() == EnumVideoCodec.VP8) {
				Log.d("THREAD", "Video code vpx");
				videoCodec.initializeVPXCodec(width, height, 1, 128);
				Log.d("CODEC", "FFMPEG initializeVPXCodec");
			}
			Log.d("THREAD", "Video code ffmpeg finished");
			encodeVideo = true;
			// System.out.println("####################################### ### ##"+encodeVideo);
		}

	}

	/**
	 * This method is used to Set VideoParameters.
	 * 
	 * @param width
	 *            width of Video
	 * @param height
	 *            height of Video
	 * @param camaera_id
	 *            CameraId
	 */
	public void setVideoParameters(int width, int height, int camaera_id) {
		this.width = width;
		this.height = height;
		this.camaera_id = camaera_id;
		mPreviewWidth = width;
		mPreviewHeight = height;
	}

	/**
	 * Used to get the Video Preview of Camera. Class Constructor get the
	 * surfaceholder to access and control over the surface and register a
	 * callback interface for this holder.
	 * 
	 * @param context
	 *            . Information about the Application.
	 * @return Camera Preview.
	 */
	public Preview getVideoPreview(Context context) {
		try {

			// this.width=width;
			// this.height=height;
			preview = new Preview(context);
			preview.setCameraid(this.camaera_id);
			/* Height and width commented for NOTE III fix */
			// preview.setWidth(width);
			// preview.setHeight(height);
			preview.setVideoFrameCallback(this);

			return preview;

		} catch (Exception e) {
			return null;
			// TODO: handle exception
		}
	}

	/**
	 * This method is used to make InstantMessage.
	 * 
	 * @param sb
	 *            Signaling Information.
	 * @return SignalId.
	 */
	public String makeIM(SignalingBean sb) {

		if (sb.getSessionid() == null) {
			sb.setSessionid(utility.getSessionID(getDeviceId()));
		}
		/* sb.setSignalid(Long.toString(utility.getRandomMediaID())); */
		sb.setPublicip(publicInetaddress);
		sb.setLocalip(localInetaddress);

		if (sb.getConferencemember() == null) {
			sb.setConferencemember("");
		}

		if (imHashmap.containsKey(sb.getSessionid() + sb.getTo())) {

			InstantMessage instantMessage = (InstantMessage) imHashmap.get(sb
					.getSessionid() + sb.getTo());
			instantMessage.addMessage(sb);
		} else {

			InstantMessage instantMessage = new InstantMessage(
					proprietarySignalling);
			instantMessage.addMessage(sb);
			imHashmap.put(sb.getSessionid() + sb.getTo(), instantMessage);

		}

		// System.out.println("Signalid:"+sb.getSignalid());
		return sb.getSignalid();

	}

	/**
	 * Used to get the Hashmap of InstantMessage reference.
	 * 
	 * @return
	 */
	public HashMap getImHashmap() {
		return imHashmap;
	}

	/**
	 * Used to accept and Add the Conference call Request.
	 * 
	 * @param toname
	 *            BuddyName
	 * @param localip
	 *            Local IpAddress
	 * @param publicip
	 *            Public IpAddress
	 * @param sessionid
	 *            Unique Id of the session.
	 * @param signalingport
	 *            Signal Port.
	 */
	private void doAddConference(String toname, String localip,
			String publicip, String sessionid, String signalingport) {
		// System.out.println("####################### doAddConference "+ toname
		// );
		Log.i("call",
				"conftable .........." + conferenceTable.containsKey(sessionid));
		if (conferenceTable.containsKey(sessionid)) {
			Log.d("call", new Date() + "sending type7 " + sessionid);
			ArrayList<SignalingBean> arrayList = (ArrayList<SignalingBean>) conferenceTable
					.get(sessionid);
			// System.out.println("**********maintanceConference count " +
			// arrayList.size() );
			Log.d("call", new Date() + "sending type744444 " + arrayList.size());
			if (arrayList.size() > 1) {
				SignalingBean new_user_sb = null;
				for (int i = 0; i < arrayList.size(); i++) {
					Log.d("call", new Date() + "sending type7 of to name");
					SignalingBean sb = (SignalingBean) arrayList.get(i);
					// System.out.println("sb.getTo():"+sb.getTo());
					// System.out.println("toname :"+toname);
					Log.i("Join"," sb.getTo() : "+sb.getTo()+" toname :"+toname);

					if (!sb.getTo().equals(toname)) {

						sb.setPunchingmode("0");
						sb.setBuddyConnectip("0");
						sb.setBuddyConnectport("0");

						sb.setConferencemember(toname + "," + localip + ","
								+ publicip + "," + signalingport);
						sb.setType("7");
						Log.d("call", new Date() + "sended type7 of to name");
						// System.out.println("################################## MAKE TYPE 7");
						if (sb.getCallType().equals("AC")
								|| sb.getCallType().equals("VC")) {
							makeConferenceCall(sb);
						}

						if (sb.getCallType().equals("VC")) {

							Log.d("999", "sending type 9 from doaddconference "
									+ toname);
							sb.setType("9");
							sb.setGmember(sb.getTo());
							makeCall(sb);
						}

					} else {
						new_user_sb = sb;
						// System.out.println("********** new user only allowed here"+toname);

					}
				}

//				if(new_user_sb!= null) {
//					for (int i = 0; i < arrayList.size(); i++) {
//						Log.d("call", new Date() + "sending type7 of to name 1");
//						SignalingBean sb = (SignalingBean) arrayList.get(i);
//						// System.out.println("sb.getTo():"+sb.getTo());
//						// System.out.println("toname :"+toname);
//						Log.i("Join", " sb.getTo() : " + sb.getTo() + " toname :" + toname + " new_user_sb : " + new_user_sb.getTo());
//
//						if (!sb.getTo().equals(toname)) {
//
//							new_user_sb.setPunchingmode("0");
//							new_user_sb.setBuddyConnectip("0");
//							new_user_sb.setBuddyConnectport("0");
//
//							new_user_sb.setConferencemember(sb.getTo() + "," + localip + ","
//									+ publicip + "," + signalingport);
//							new_user_sb.setType("7");
//							Log.d("call", new Date() + "sended type7 of to name");
//							// System.out.println("################################## MAKE TYPE 7");
//							if (new_user_sb.getCallType().equals("AC")
//									|| new_user_sb.getCallType().equals("VC")) {
//								makeConferenceCall(new_user_sb);
//							}
//
//							if (new_user_sb.getCallType().equals("VC")) {
//
//								Log.d("999", "sending type 9 from doaddconference "
//										+ toname);
//								new_user_sb.setType("9");
//								new_user_sb.setGmember(new_user_sb.getTo());
//								makeCall(new_user_sb);
//							}
//
//						} else {
//							// System.out.println("********** new user only allowed here"+toname);
//
//						}
//					}
//				}
			}
		} else {
			Log.d("testx", " not sending type7");
		}
	}

	/**
	 * Used to get the GLSurfaceView.It is used to draw the image on the
	 * Surface.
	 * 
	 * @param context
	 *            Application Information.
	 * @return Drawing Surface.
	 */
	public GLSurfaceView getGLSurfaceView(Context context) {
		glSurfaceView = new GLSurfaceView(context);
		frameRenderer = new VideoFrameRenderer();
		glSurfaceView.setRenderer(frameRenderer);
		frameSink = frameRenderer;
		frame = new byte[width * height * 3];
		frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		return glSurfaceView;
	}

	/**
	 * Used to decode the Video Frame and Notify it to UI .The Decoder Used is
	 * H264 or VP8.
	 * 
	 * @param frame
	 *            VideoFrame.
	 * @param ssrc
	 *            VideoSsrc.
	 * @param decIndex
	 *            DecoderIndex.
	 */
	public synchronized void decodeVideoFrame(byte[] frame, long ssrc, int decIndex) {

		// System.out.println("decodessrc : "+decodessrc+" ssrc: "+ssrc);
		Log.d("VIDEOP", "" + "decodessrc : " + decodessrc + " ssrc: " + ssrc);
		byte[] yuvdata = null;
		byte[] rgbdata = null;
		try {
				if(WebServiceReferences.videoSSRC_total_list != null ) {

					if (WebServiceReferences.videoSSRC_total_list.contains((int) (long) ssrc)) {

					} else {
						WebServiceReferences.videoSSRC_total_list.add((int) (long) ssrc);
//						if(WebServiceReferences.videoSSRC_total.containsKey((int) (long) ssrc)){
//
//						}
					}
				}
				if (frame != null) {
					Log.d("VIDEOP12345", "frame != null");
					// System.out.println("decodessrc :"+decodessrc+" videocodec :"+videoCodec);
					if (videoCodec != null && mDecodeFrame) {
						System.out.println("decodessrc :" + decodessrc
								+ " videocodec :" + videoCodec);
//				if (decodessrc == ssrc) {

						int result = 0;

						yuvdata = new byte[mPreviewWidth * mPreviewHeight
								+ (mPreviewWidth * mPreviewHeight) / 2];
						rgbdata = new byte[mPreviewWidth * mPreviewHeight * 3];
						EnumVideoCodec enumVideoCodec = videoCodec
								.getEnumVideoCodec();
//					if (WebServiceReferences.videoSSRC_total_list.size() > 0 && WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 0) {
						switch (enumVideoCodec) {
							case H264:
								Log.i("amu2", "H264");
								if (WebServiceReferences.videoSSRC_total_list.size() > 0 ) {
								result = videoCodec.DecodeH264(frame, yuvdata,WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) );
								}
								break;
							case VP8:
								Log.i("amu2", "VP8");
								result = videoCodec.decodeVPX(frame, yuvdata,
										frame.length, decIndex);
								break;
							default:
								break;
						}
//					}
						// System.out.println("RESULT :"+result);

						if (result > 3) {


							if (videoCallback != null) {
								// System.out.println("Engine Comm rgb");
								Log.d("VDO", "going to notify decoded video frame");
								videoCodec.convertYUV4202RGB24(yuvdata, rgbdata,
										mPreviewWidth, mPreviewHeight);
								videoCallback.notifyDecodedVideoCallback(rgbdata,
										ssrc);

							}
						}

						rgbdata = null;
						yuvdata = null;
						frame = null;

			/*	} else {

					int result = 0;
					yuvdata = new byte[mPreviewWidth * mPreviewHeight
							+ (mPreviewWidth * mPreviewHeight) / 2];
					rgbdata = new byte[mPreviewWidth * mPreviewHeight * 3];
					EnumVideoCodec enumVideoCodec = videoCodec
							.getEnumVideoCodec();
					switch (enumVideoCodec) {
						case H264:
							result = videoCodec.DecodeH264(frame, yuvdata);
							break;
						case VP8:
							result = videoCodec.decodeVPX(frame, yuvdata,
									frame.length, decIndex);
							break;
						default:
							break;
					}
					// System.out.println("RESULT :"+result);

					if (result > 3) {
						videoCodec.convertYUV4202RGB24(yuvdata, rgbdata,
								mPreviewWidth, mPreviewHeight);
						if (videoCallback != null) {
							// System.out.println("Engine Comm rgb");
							Log.d("VDO", "going to notify decoded video frame");
							videoCallback.notifyDecodedVideoCallback2(rgbdata,
									ssrc);
						}
					}
				}*/
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		rgbdata = null;
		yuvdata = null;
		frame = null;

	}

	public class VideoBean
	{
		private long ssrc;
		private byte[] frame;
		private VideoCallback videoCallback;
		private int width;
		private int height;
		private VideoCodec videoCodec;

		public long getSsrc() {
			return ssrc;
		}

		public void setSsrc(long ssrc) {
			this.ssrc = ssrc;
		}

		public byte[] getFrame() {
			return frame;
		}

		public void setFrame(byte[] frame) {
			this.frame = frame;
		}

		public VideoCallback getVideoCallback() {
			return videoCallback;
		}

		public void setVideoCallback(VideoCallback videoCallback) {
			this.videoCallback = videoCallback;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public VideoCodec getVideoCodec() {
			return videoCodec;
		}

		public void setVideoCodec(VideoCodec videoCodec) {
			this.videoCodec = videoCodec;
		}
	}


	/**
	 * Used to Stop AudioCapture from AudioRecorder.Also stop the AudioRecorder.
	 */
	public void stopAudioCapture() {
		if (audioRecorder != null) {
			if (CallDispatcher.sb.getCallType().equalsIgnoreCase("ABC")
					|| CallDispatcher.sb.getCallType().equalsIgnoreCase("VBC")
					|| CallDispatcher.sb.getCallType().equalsIgnoreCase("AP")
					|| CallDispatcher.sb.getCallType().equalsIgnoreCase("VP")) {
				audioRecorder.Stop(CallDispatcher.sb.getSignalid());
			} else {
				audioRecorder.Stop(CallDispatcher.sb.getFrom() + "_"
						+ CallDispatcher.sb.getSessionid());
			}
			audioRecorder = null;
		}
		if (audioThread != null) {
			audioThread.stopAudioThread();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					// AudioMixing audioMixing = AudioMixing.getInstance();
					//
					// audioMixing.mixFiles(
					// CallDispatcher.sb.getFrom() + "_"
					// + CallDispatcher.sb.getSessionid(),
					// CallDispatcher.sb.getTo() + "_"
					// + CallDispatcher.sb.getSessionid(),
					// CallDispatcher.sb.getSessionid());
					if (!CallDispatcher.sb.getCallType()
							.equalsIgnoreCase("ABC")
							|| !CallDispatcher.sb.getCallType()
									.equalsIgnoreCase("VBC")
							|| !CallDispatcher.sb.getCallType()
									.equalsIgnoreCase("AP")
							|| !CallDispatcher.sb.getCallType()
									.equalsIgnoreCase("VP")) {
						mixFiles();
					}
//					if (CallDispatcher.sb.getCallType().equalsIgnoreCase("ABC")
//							|| CallDispatcher.sb.getCallType()
//									.equalsIgnoreCase("VBC")
//							|| CallDispatcher.sb.getCallType()
//									.equalsIgnoreCase("AP")
//							|| CallDispatcher.sb.getCallType()
//									.equalsIgnoreCase("VP")) {
//						recordAudioVideoBroadcast(CallDispatcher.sb
//								.getSignalid());
//					}
					Log.i("CallRecorder123", "Recording Done");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	// =======================================================

	// =============================================================

	/**
	 * used to stop VideoCapture .
	 */
	public void stopVideoCapture() {
		if (videoCodec != null) {

			if (videoCodec.getEnumVideoCodec() == EnumVideoCodec.VP8) {
				videoCodec.exitVPXCodec();
			} else if (videoCodec.getEnumVideoCodec() == EnumVideoCodec.H264) {
				videoCodec.ExitFFmpeg();
			}

			videoCodec = null;
		}
		encodeVideo = false;

	}

	public void foo() {

	}

	/**
	 * Used to Set the MIC Properties.(MUTE,UNMUTE).
	 * 
	 * @param mute
	 *            Status.
	 */
	public void micMute(boolean mute) {
		micMute = mute;

		if (audioRecorder != null) {
			audioRecorder.setMicMute(mute);
		}

		// AudioProperties audioProperties=new AudioProperties(context);
		// audioProperties.setMicrophoneMute(mute);
	}

	/**
	 * used to Mute and UnMute Speaker Properties.
	 * 
	 * @param buddy
	 *            Buddy Name
	 * @param session
	 *            Unique Session Id.
	 * @param mute
	 *            Speaker Mute
	 */
	public void speakerMute(String buddy, String session, boolean mute) {
		try {
			CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
					.get(session + buddy);
			callsOverInternet.setSpeakerMute(mute);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to stop UDPEngine and Clean the Unwanted Resources.
	 */
	// Make the Reference of proprietarySignalling as Null...
	public void stop() {
		System.out.println("### 1 stop proprietarySignalling :"
				+ proprietarySignalling);

		if (proprietarySignalling != null) {
			proprietarySignalling.stopPortRefereshTimer();
			proprietarySignalling.stopUDPEngine();
			clean();
		}

		stopVideoCapture();

		if (speex != null) {
			speex.closeSpeex();
		}

		if (videoThread != null) {
			videoThread.stopVideoThread();
		}

		if (videoSendThread != null) {
			videoSendThread.stopVideoSender();
		}

	}

	/**
	 * This method is used to get the Audio data and send it using RTPEngine.
	 * Using RTPEngine we can send the Data.
	 */
	@Override
	public void notifyAudioData(byte[] audiodata) {

		// Log.d("call",new Date()+"Audio......");
		for (int i = 0; i < mediaKey.size(); i++) {
			String key = mediaKey.get(i);

			Object obj = callTable.get(key);
			if (obj instanceof CallsOverInternet) {
				// Log.d("call",
				// "created notify data Instance of calls overner Audio");

				CallsOverInternet callObj = (CallsOverInternet) obj;
				// System.out.println("callObj :"+callObj);
				if (callObj != null) {

					// Log.d("ismute","isallow audio "+callObj.isAllowAudio());
					// if(callObj.isAllowMedia()){ old code
					if (callObj.isAllowAudio() && !callObj.isJoinedRelay()) {// new
																				// code
																				// Nov
																				// 11
																				// 2011

						// System.out.println("[P2P] Audio :"+callObj.getAudiossrc()+" IP "+callObj.getBuddyConnectip()+"    ## Allow "+callObj.isAllowMedia());
						callObj.getRtpEngine().sendData(callObj.getAudiossrc(),
								callObj.getBuddyConnectip(), audiodata);

					}
				}
			}

			else if (obj instanceof RelayClientNew) {
				// Log.d("call",new Date()+"Sending Audio......RelayClientNew");
				RelayClientNew relayClient = (RelayClientNew) obj;
				relayClient.getRtpEngine().sendData(
						CallsOverInternet.audiossrc, relayServerip, audiodata);
			}

		}

	}

	/**
	 * This method is used to Set relay client to Allow Media.
	 * 
	 * @param value
	 *            status.
	 * @param sessionid
	 *            Unique SessionId.
	 */
	// public void setRelayClientallow(boolean value,String sessionid){
	// if(callTable.containsKey(sessionid)){
	// RelayClient relayClient=(RelayClient) callTable.get(sessionid);
	// relayClient.setAllowMedia(value);
	// }
	// }
	/**
	 * Used to Join Relay using relayClient.
	 * 
	 * @param sessionid
	 *            Unique sessionId
	 * @param buddy
	 *            Buddy name
	 * @param audiossrc
	 *            AudioSsrc
	 * @param videossrc
	 *            VideoSsrc
	 * @param key
	 *            Key
	 * @param signal
	 *            SignalType.
	 */
	// public void joinRelay(String sessionid,String buddy,Long audiossrc,long
	// videossrc,String key,boolean signal){
	//
	// if(callTable.containsKey(sessionid)){
	// RelayClient relayClient=(RelayClient) callTable.get(sessionid);
	// relayClient.addReceiverDetails(audiossrc, key);
	// relayClient.addReceiverDetails(videossrc, key);
	// relayClient.sendRelayJoinMessage(sessionid,loginUsername, buddy,signal);
	// if(signal){
	// sendTypeSixSignal(key);
	// }
	// }else{
	//
	// RelayClient relayClient=new RelayClient(relayServerip,relayServerPort);
	// relayClient.setSessionid(sessionid);
	// relayClient.addReceiverDetails(audiossrc, key);
	// relayClient.addReceiverDetails(videossrc, key);
	// relayClient.setCallSessionTable(callTable);
	// callTable.put(sessionid,relayClient);
	// mediaKey.add(sessionid);
	// relayClient.sendRelayJoinMessage(sessionid,loginUsername, buddy,signal);
	// if(signal){
	// sendTypeSixSignal(key);
	// }
	//
	// }
	//
	//
	//
	// }

	public void joinRelaynew(String sessionid, String buddy, Long audiossrc,
			long videossrc, String key, String calltype, boolean isSsrc) {

		Log.d("SM", " joinRelaynew(;" + buddy);

		if (callTable.containsKey(sessionid)) {

			RelayClientNew relayClient = (RelayClientNew) callTable
					.get(sessionid);

			if (isSsrc) {
				Log.d("call", new Date() + "set SSRC " + audiossrc + ","
						+ videossrc);
				relayClient.addAudioSsrc(audiossrc, key);
				relayClient.addVideoSsrc(videossrc, key);
			}

			Log.d("call", new Date() + "sendRelayJoinMessage from comm engine");
			relayClient.setBuddyName(buddy);
			relayClient.sendRelayJoinMessage(sessionid, loginUsername, buddy);

		} else {

			CommunicationEngine.relayJoined = true;

			RelayClientNew relayClient = new RelayClientNew(relayServerip,
					relayServerPort);
			relayClient.setCommEngine(this);
			relayClient.setCallType(calltype);
			relayClient.setSessionid(sessionid);
			if (isSsrc) {
				relayClient.addAudioSsrc(audiossrc, key);
				relayClient.addVideoSsrc(videossrc, key);
			}
			Log.d("call", new Date() + "sendRelayJoinMessage from comm engine");
			relayClient.sendRelayJoinMessage(sessionid, loginUsername, buddy);
			relayClient.setBuddyName(buddy);
			relayClient.setCallSessionTable(callTable);
			callTable.put(sessionid, relayClient);
			mediaKey.add(sessionid);

		}

	}

	public void joinRelayOnTypeSix(String sessionid, String buddy,
			Long audiossrc, long videossrc, String key, String calltype) {

		Log.d("call", new Date() + "joinRelayOnTypeSix");

		if (callTable.containsKey(sessionid)) {
			Log.d("SM", "joinRelayOnTypeSix exsist " + buddy);

			RelayClientNew relayClient = (RelayClientNew) callTable
					.get(sessionid);

			Log.d("call", new Date() + "set SSRC " + audiossrc + ","
					+ videossrc);
			relayClient.addAudioSsrc(audiossrc, key);
			// relayClient.addVideoSsrc(videossrc, key);

			relayClient.addVideoSsrcAlone(videossrc, key);
			Log.d("call", new Date() + "sendRelayJoinMessage from comm engine");
			// relayClient.setBuddyName(buddy);
			// relayClient.sendRelayJoinMessage(sessionid, loginUsername,
			// buddy);
			relayClient.sendRelayAudioReceiveMessage(sessionid, loginUsername,
					buddy);

		} else {

			Log.d("SM",
					"joinRelayOnTypeSix Notexsist   $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			CommunicationEngine.relayJoined = true;

			RelayClientNew relayClient = new RelayClientNew(relayServerip,
					relayServerPort);
			relayClient.setCommEngine(this);
			relayClient.setCallType(calltype);
			relayClient.setSessionid(sessionid);
			relayClient.addAudioSsrc(audiossrc, key);
			relayClient.addVideoSsrcAlone(videossrc, key);
			Log.d("call", new Date() + "sendRelayJoinMessage from comm engine");
			// relayClient.sendRelayJoinMessage(sessionid, loginUsername,
			// buddy);
			relayClient.sendRelayJoinMessageOnType6(sessionid, loginUsername,
					buddy);
			// relayClient.setBuddyName(buddy);
			relayClient.setCallSessionTable(callTable);
			callTable.put(sessionid, relayClient);
			mediaKey.add(sessionid);

		}

	}

	/**
	 * Used to send Type6 signal for Relay request.
	 * 
	 * @param key
	 *            ID
	 */
	public void sendTypeSixSignal(String key) {

		CallsOverInternet callsOverInternet = (CallsOverInternet) callTable
				.get(key);
		if (callsOverInternet != null) {
			callsOverInternet.sendMediaJoinRequestToEndUser();
		} else {
			// System.out.println("callsOverInternet is null");
		}
	}

	/**
	 * This method is used to shoe the FrameCount.
	 */
	public void showFramecount() {
		Log.d("COUNT", "" + frameCount + " " + System.currentTimeMillis());
		frameCount = 0;

	}

	public void encodeAndSend(byte[] arg0) {

	}

	/**
	 * This method is used to get the Video data and send it using RTPEngine.
	 * Using RTPEngine we can send the Videoframe.
	 */
	@Override
	public void notifyVideoFrame(byte[] arg0) {
		if (!screenSharing) {
			try {
				// Log.d("call",new Date()+"Video......");
				if (videoCodec != null && video_preview) {
					if (encodeVideo) {
						byte[] yuvdata = new byte[width * height
								+ (width * height) / 2];
						byte[] coded_frame = new byte[1];
						EnumVideoCodec enumVideoCodec = videoCodec
								.getEnumVideoCodec();
						switch (enumVideoCodec) {
						case H264:
							int outW = 176;
							int outH = 144;
							byte[] rgbdata = new byte[width * height * 3];
							byte[] outRGB = new byte[outW * outH * 3];
							byte[] yuv420p = null;
							byte[] h264data = new byte[115200];
							if (mPreviewWidth == width
									&& mPreviewHeight == height) {
								yuv420p = new byte[(width * height * 3) / 2];
								videoCodec.yuv420spToYuv420(arg0, yuv420p,
										width, height);
							} else {
								videoCodec.yuv420sp2rgb(arg0, width, height,
										256, rgbdata);
								Log.d("SCALE", "Width 1 " + width
										+ " Height 1 " + height);
								videoCodec.Scale(width, height, rgbdata, outW,
										outH, outRGB);
								yuv420p = new byte[(outW * outH * 3) / 2];
								videoCodec.convertRGB2YUV420(outRGB, yuv420p,
										outW, outH);
							}

							int size = videoCodec.EncodeH264(yuv420p, h264data,
									null, coded_frame);
							encoded_data = new byte[size];
							if (size > 0) {
								key_frame = coded_frame[0];
								for (int i = 0; i < size; i++) {
									encoded_data[i] = h264data[i];
								}
								mSend = true;
							}
							h264data = null;
							yuv420p = null;
							break;
						case VP8:
							byte[] vpxdata = new byte[256 * 1024];
							byte[] rgbdata1 = new byte[width * height * 3];
							videoCodec.yuv420sp2rgb(arg0, width, height, 256,
									rgbdata1);
							videoCodec.convertRGB2YUV420(rgbdata1, yuvdata,
									width, height);
							int result = videoCodec.encodeVPX(width, height,
									yuvdata, vpxdata);
							if (result > 3) {
								encoded_data = new byte[result];
								for (int i = 0; i < result; i++) {
									encoded_data[i] = vpxdata[i];
								}
								mSend = true;
							}
							vpxdata = null;
							rgbdata1 = null;
							yuvdata = null;
							break;

						default:
							break;
						}

						if (encoded_data != null) {

							if (key_frame == 1) {

								System.out.println("EEE" + key_frame);
								// Log.d("FRAMEKEY","key frame "+key_frame);
								videoFragment(encoded_data, true);
							} else if (key_frame == 0) {
								videoFragment(encoded_data, false);
							}

						}

						/*
						 * if(encoded_data!=null){ for(int
						 * i=0;i<mediaKey.size();i++){ String key
						 * =mediaKey.get(i);
						 * 
						 * Object obj= callTable.get(key); if(obj instanceof
						 * CallsOverInternet){ CallsOverInternet
						 * callObj=(CallsOverInternet) obj;
						 * //System.out.println("callObj :"+callObj);
						 * if(callObj!=null){ if(callObj.isAllowMedia()){
						 * System.out.
						 * println("[P2P]Sending Video :"+callObj.getAudiossrc
						 * ()+" IP "+callObj.getBuddyConnectip());
						 * callObj.getRtpEngine
						 * ().sendData(callObj.getVideossrc(),callObj
						 * .getBuddyConnectip(), encoded_data); } } }else if(obj
						 * instanceof RelayClient){ RelayClient
						 * relayClient=(RelayClient)obj;
						 * if(relayClient.isAllowMedia()){
						 * relayClient.getRtpEngine()
						 * .sendData(CallsOverInternet.videossrc,relayServerip,
						 * encoded_data); //System.out.println(
						 * "[RELAY]Sending video through relay server...."); } }
						 * }
						 * 
						 * }
						 */

						encoded_data = null;

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void enable_disable_VideoPreview(boolean enable_video){
		this.video_preview = enable_video;
	}

	private byte[] data_tmp = null;

	public void videoFragment(byte[] data, boolean keyframe) {

		try {
			if (data != null) {

				if (data.length < 1024) {
					long timespan = new Date().getTime();
					ByteArrayOutputStream dataArray = new ByteArrayOutputStream();
					dataArray.write(new byte[12]);
					dataArray.write(data, 0, data.length);
					byte rtpadata[] = dataArray.toByteArray();
					RtpPacket rtpp = new RtpPacket(rtpadata, rtpadata.length);
					rtpp.setTimestamp(timespan);
					rtpp.setMarker(true);
					rtpp.setSequenceNumber(videoSqno);
					if (keyframe) {
						// Log.d("FRAMEKEY"," less 1024 key frame seq "+rtpp.getSequenceNumber());
						rtpp.setPayloadType(14);

					}

					videoSqno++;
					// send(rtpp);
					videoRtpQueue.addMsg(rtpp);
				} else {
					fragment(data, keyframe);
				}

			}
		} catch (Exception e) {
			Log.d("VIDEO", "On video fragment " + e);
			e.printStackTrace();
		}
	}

	private void fragment(byte[] capturedbytes, boolean keyframe) {
		try {
			final int CHUNK_SIZE = 1024;
			long timespan = new Date().getTime();
			int length = CHUNK_SIZE;
			int sendcount = 0;
			sendcount = capturedbytes.length;
			int j = 0;
			int kf = 0;
			for (int i = 0; i < capturedbytes.length; i += CHUNK_SIZE) {
				ByteArrayOutputStream outFrame = new ByteArrayOutputStream();
				int x = 0;
				j = j + 1;
				if (sendcount >= CHUNK_SIZE) {
					outFrame.write(new byte[12]);
					outFrame.write(capturedbytes, i, CHUNK_SIZE);
					sendcount = sendcount - CHUNK_SIZE;

				} else {
					outFrame.write(new byte[12]);
					outFrame.write(capturedbytes, i, sendcount);
					x = 1;

				}

				RtpPacket videortppPacket = null;// new RtpPacket();

				byte outArray[] = outFrame.toByteArray();
				videortppPacket = new RtpPacket(outArray, outArray.length);
				if (x == 1) {
					videortppPacket.setMarker(true);
					x = 0;

				} else {

					videortppPacket.setMarker(false);

					System.out.println("KeyFrame " + keyframe);
					if (kf == 0) {
						// Log.d("FRAMEKEY","first packet  key frame seq "+videortppPacket.getSequenceNumber());
						if (keyframe) {
							videortppPacket.setPayloadType(14);
							kf = 1;
						}
					}

				}

				videortppPacket.setTimestamp(timespan);
				videortppPacket.setSequenceNumber(videoSqno);
				videoSqno++;

				// send(videortppPacket);
				videoRtpQueue.addMsg(videortppPacket);

				outFrame.close();
				outFrame = null;
				outArray = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(RtpPacket rtpPacket) {
		try {

			// Log.d("call",new Date()+" commu Video send");

			if (rtpPacket.getSequenceNumber() > 255) {
				Integer key = rtpPacket.getSequenceNumber()
						- Store.packetStoreForSend.size();
				Store.packetStoreForSend.remove(key);
				Store.packetStoreForSend.put(rtpPacket.getSequenceNumber(),
						rtpPacket);
			} else {
				Store.packetStoreForSend.put(rtpPacket.getSequenceNumber(),
						rtpPacket);
			}

			if (rtpPacket.getPayloadType() == 14) {
				// System.out.println("Sending payload type "+rtpPacket.getPayloadType());
			}
			// Log.d("call",new Date()+" size "+mediaKey.size());
			for (int i = 0; i < mediaKey.size(); i++) {
				String key = mediaKey.get(i);

				Object obj = callTable.get(key);
				if (obj instanceof CallsOverInternet) {

					CallsOverInternet callObj = (CallsOverInternet) obj;
					if (callObj != null) {

						if (callObj.isAllowMedia() && !callObj.isJoinedRelay()) {

							rtpPacket.setSscr(callObj.getVideossrc());
							System.out.println("Sending connectip "
									+ callObj.getBuddyConnectip());
							callObj.getRtpEngine().setRemoteAddress(
									InetAddress.getByName(callObj
											.getBuddyConnectip()));
							callObj.getRtpEngine().setRemotePort(
									callObj.getBuddyConnectPort());

							if (callObj.getBuddyConnectip() != null) {
								// Log.d("call",
								// " send from coom engine callsessionnnn Video "+rtpPacket.getSscr());
								callObj.getRtpEngine().send(rtpPacket);
							}

						}
					}

				}

				// else if(obj instanceof RelayClient){
				// RelayClient relayClient=(RelayClient)obj;
				// if(relayClient.isAllowMedia()){
				// relayClient.getRtpEngine().setRemoteAddress(InetAddress.getByName(relayServerip));
				// relayClient.getRtpEngine().setRemotePort(relayServerPort);
				// relayClient.getRtpEngine().send(rtpPacket);
				//
				// }
				// }

				else if (obj instanceof RelayClientNew) {

					RelayClientNew relayClient = (RelayClientNew) obj;

					// Log.d("call",
					// " send from coom engine Video relay ip "+relayServerip);
					relayClient.getRtpEngine().setRemoteAddress(
							InetAddress.getByName(relayServerip));
					relayClient.getRtpEngine().setRemotePort(relayServerPort);
					rtpPacket.setSscr(CallsOverInternet.videossrc);
					relayClient.getRtpEngine().send(rtpPacket);
					// Log.d("call",
					// " send from coom engine Video "+rtpPacket.getSscr());

					// }
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void notifyVideoFrame_Sharing(byte[] arg0) {

		try {
			// Log.d("call",new Date()+"Video......");

			if (videoCodec != null) {
				Log.i("SImage", "inside notifyVideoFrame_Sharing ");
				if (encodeVideo) {
					byte[] yuvdata = new byte[width * height + (width * height)
							/ 2];
					byte[] coded_frame = new byte[1];
					EnumVideoCodec enumVideoCodec = videoCodec
							.getEnumVideoCodec();
					switch (enumVideoCodec) {
					case H264:
						int outW = 176;
						int outH = 144;
						byte[] rgbdata = new byte[width * height * 3];
						byte[] outRGB = new byte[outW * outH * 3];
						byte[] yuv420p = null;
						byte[] h264data = new byte[115200];
						if (mPreviewWidth == width && mPreviewHeight == height) {
							yuv420p = new byte[(width * height * 3) / 2];
							videoCodec.yuv420spToYuv420(arg0, yuv420p, width,
									height);
						} else {
							videoCodec.yuv420sp2rgb(arg0, width, height, 256,
									rgbdata);
							Log.d("SCALE", "Width 1 " + width + " Height 1 "
									+ height);
							videoCodec.Scale(width, height, rgbdata, outW,
									outH, outRGB);
							yuv420p = new byte[(outW * outH * 3) / 2];
							videoCodec.convertRGB2YUV420(outRGB, yuv420p, outW,
									outH);
						}

						int size = videoCodec.EncodeH264(yuv420p, h264data,
								null, coded_frame);
						encoded_data = new byte[size];
						if (size > 0) {
							key_frame = coded_frame[0];
							for (int i = 0; i < size; i++) {
								encoded_data[i] = h264data[i];
							}
							mSend = true;
						}
						h264data = null;
						yuv420p = null;
						break;
					case VP8:
						byte[] vpxdata = new byte[256 * 1024];
						byte[] rgbdata1 = new byte[width * height * 3];
						videoCodec.yuv420sp2rgb(arg0, width, height, 256,
								rgbdata1);
						videoCodec.convertRGB2YUV420(rgbdata1, yuvdata, width,
								height);
						int result = videoCodec.encodeVPX(width, height,
								yuvdata, vpxdata);
						if (result > 3) {
							encoded_data = new byte[result];
							for (int i = 0; i < result; i++) {
								encoded_data[i] = vpxdata[i];
							}
							mSend = true;
						}
						vpxdata = null;
						rgbdata1 = null;
						yuvdata = null;
						break;

					default:
						break;
					}

					if (encoded_data != null) {

						if (key_frame == 1) {

							System.out.println("EEE" + key_frame);
							// Log.d("FRAMEKEY","key frame "+key_frame);
							videoFragment(encoded_data, true);
						} else if (key_frame == 0) {
							videoFragment(encoded_data, false);
						}

					}

					/*
					 * if(encoded_data!=null){ for(int
					 * i=0;i<mediaKey.size();i++){ String key =mediaKey.get(i);
					 * 
					 * Object obj= callTable.get(key); if(obj instanceof
					 * CallsOverInternet){ CallsOverInternet
					 * callObj=(CallsOverInternet) obj;
					 * //System.out.println("callObj :"+callObj);
					 * if(callObj!=null){ if(callObj.isAllowMedia()){
					 * System.out.
					 * println("[P2P]Sending Video :"+callObj.getAudiossrc
					 * ()+" IP "+callObj.getBuddyConnectip());
					 * callObj.getRtpEngine
					 * ().sendData(callObj.getVideossrc(),callObj
					 * .getBuddyConnectip(), encoded_data); } } }else if(obj
					 * instanceof RelayClient){ RelayClient
					 * relayClient=(RelayClient)obj;
					 * if(relayClient.isAllowMedia()){
					 * relayClient.getRtpEngine()
					 * .sendData(CallsOverInternet.videossrc,relayServerip,
					 * encoded_data); //System.out.println(
					 * "[RELAY]Sending video through relay server...."); } } }
					 * 
					 * }
					 */

					encoded_data = null;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void notifyResolution(int w, int h) {
		if (width != w && height != h) {
			width = w;
			height = h;
			videoCallback.notifyResolution(w, h);

		}

	}

	private void stopRecording() {
		// if (callRecorder != null) {
		// Log.i("CallRecorder123", "Stop Recording");
		// callRecorder.stop();
		// callRecorder.release();
		// }
	}

	public void setScreenSharing(boolean status) {
		screenSharing = status;
	}

	private void mixFiles() {
		try {

			InputStream is1 = new FileInputStream(new File(
					Environment.getExternalStorageDirectory()
							+ "/COMMedia/CallRecording/"
							+ CallDispatcher.sb.getFrom() + "_"
							+ CallDispatcher.sb.getSessionid() + ".wav"));
			InputStream is2 = new FileInputStream(new File(
					Environment.getExternalStorageDirectory()
							+ "/COMMedia/CallRecording/"
							+ CallDispatcher.sb.getTo() + "_"
							+ CallDispatcher.sb.getSessionid() + ".wav"));
			// InputStream is3 = new FileInputStream(new File(
			// Environment.getExternalStorageDirectory(),
			// "/Android/data/blablabla3.mp3"));

			// text.setText("haciendo shorts");

			byte[] music1Array1 = convertStreamToByteArray(is1);
			byte[] music2Array2 = convertStreamToByteArray(is2);

			byte[] music1Array = new byte[music1Array1.length - 44];
			byte[] music2Array = new byte[music2Array2.length - 44];

			for (int i = 44; i < music1Array1.length; i++) {
				music1Array[i - 44] = music1Array1[i];
			}

			for (int i = 44; i < music2Array2.length; i++) {
				music2Array[i - 44] = music2Array2[i];
			}

			int music1ArrayLength = music1Array.length;
			int music2ArrayLength = music2Array.length;

			byte[] finalMusic1 = null;
			byte[] finalMusic2 = null;

			if (music1ArrayLength > music2ArrayLength) {
				finalMusic1 = music1Array;
				finalMusic2 = new byte[music1ArrayLength];
				for (int i = 0; i < music2Array.length; i++) {
					finalMusic2[i] = (byte) music2Array[i];
				}
				for (int i = music2ArrayLength; i < music1ArrayLength; i++) {
					finalMusic2[i] = (byte) 0xff;
				}
			} else if (music2ArrayLength > music1ArrayLength) {

				finalMusic2 = music2Array;
				finalMusic1 = new byte[music2ArrayLength];
				for (int i = 0; i < music1Array.length; i++) {
					finalMusic1[i] = (byte) music1Array[i];
				}

				for (int i = music1ArrayLength; i < music2ArrayLength; i++) {
					finalMusic1[i] = (byte) 0xff;
				}
			}
			byte[] outputByte = new byte[finalMusic1.length];

			for (int i = 0; i < finalMusic1.length; i++) {
				outputByte[i] = (byte) (finalMusic1[i] + finalMusic2[i]);
			}

			String fileName = "" + CallDispatcher.sb.getSessionid();

			DataOutputStream amplifyOutputStream = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(
							Environment.getExternalStorageDirectory()
									+ "/COMMedia/CallRecording/"
									+ fileName.trim() + ".wav")));

			amplifyOutputStream.write(outputByte);
			amplifyOutputStream.close();

			// start

			long size = 0;
			try {
				FileInputStream fileSize = new FileInputStream(
						Environment.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/" + fileName.trim()
								+ ".wav");
				size = fileSize.getChannel().size();
				fileSize.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final int RECORDER_BPP = 16;
			int RECORDER_SAMPLERATE = 8000;

			long datasize = size + 36;
			long byteRate = (RECORDER_BPP * RECORDER_SAMPLERATE) / 8;
			long longSampleRate = RECORDER_SAMPLERATE;
			byte[] header = new byte[44];

			header[0] = 'R'; // RIFF/WAVE header
			header[1] = 'I';
			header[2] = 'F';
			header[3] = 'F';
			header[4] = (byte) (datasize & 0xff);
			header[5] = (byte) ((datasize >> 8) & 0xff);
			header[6] = (byte) ((datasize >> 16) & 0xff);
			header[7] = (byte) ((datasize >> 24) & 0xff);
			header[8] = 'W';
			header[9] = 'A';
			header[10] = 'V';
			header[11] = 'E';
			header[12] = 'f'; // 'fmt ' chunk
			header[13] = 'm';
			header[14] = 't';
			header[15] = ' ';
			header[16] = 16; // 4 bytes: size of 'fmt ' chunk
			header[17] = 0;
			header[18] = 0;
			header[19] = 0;
			header[20] = 1; // format = 1
			header[21] = 0;
			header[22] = (byte) 1;
			header[23] = 0;
			header[24] = (byte) (longSampleRate & 0xff);
			header[25] = (byte) ((longSampleRate >> 8) & 0xff);
			header[26] = (byte) ((longSampleRate >> 16) & 0xff);
			header[27] = (byte) ((longSampleRate >> 24) & 0xff);
			header[28] = (byte) (byteRate & 0xff);
			header[29] = (byte) ((byteRate >> 8) & 0xff);
			header[30] = (byte) ((byteRate >> 16) & 0xff);
			header[31] = (byte) ((byteRate >> 24) & 0xff);
			header[32] = (byte) ((RECORDER_BPP) / 8); // block align
			header[33] = 0;
			header[34] = RECORDER_BPP; // bits per sample
			header[35] = 0;
			header[36] = 'd';
			header[37] = 'a';
			header[38] = 't';
			header[39] = 'a';
			header[40] = (byte) (size & 0xff);
			header[41] = (byte) ((size >> 8) & 0xff);
			header[42] = (byte) ((size >> 16) & 0xff);
			header[43] = (byte) ((size >> 24) & 0xff);
			// out.write(header, 0, 44);

			try {
				RandomAccessFile rFile = new RandomAccessFile(
						Environment.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/" + fileName.trim()
								+ ".wav", "rw");
				rFile.seek(0);
				rFile.write(header);
				rFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// end

			// play(output);
			// escribir(output);
		} catch (NotFoundException e) {
			// toast("notf");
			e.printStackTrace();
		} catch (IOException e) {
			// toast("ioex");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// =======================================================

	public byte[] convertStreamToByteArray(InputStream is) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buff = new byte[10240];
		int i = Integer.MAX_VALUE;
		while ((i = is.read(buff, 0, buff.length)) > 0) {
			baos.write(buff, 0, i);
		}

		return baos.toByteArray(); // be sure to close InputStream in calling
									// function

	}

	private void recordAudioVideoBroadcast(String fileName) {
		try {
			DataOutputStream amplifyOutputStream = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(
							Environment.getExternalStorageDirectory()
									+ "/COMMedia/CallRecording/"
									+ fileName.trim() + ".wav")));
			// InputStream is1 = new FileInputStream(new File(
			// Environment.getExternalStorageDirectory()
			// + "/COMMedia/CallRecording/"
			// + CallDispatcher.sb.getFrom() + "_"
			// + CallDispatcher.sb.getSessionid() + ".wav"));
			// InputStream is2 = new FileInputStream(new File(
			// Environment.getExternalStorageDirectory()
			// + "/COMMedia/CallRecording/"
			// + CallDispatcher.sb.getTo() + "_"
			// + CallDispatcher.sb.getSessionid() + ".wav"));
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/COMMedia/CallRecording/" + CallDispatcher.sb.getFrom()
					+ "_" + CallDispatcher.sb.getSignalid() + ".wav");
			InputStream is = null;
			if (file.exists()) {
				is = new FileInputStream(new File(
						Environment.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/"
								+ CallDispatcher.sb.getFrom() + "_"
								+ CallDispatcher.sb.getSignalid() + ".wav"));
			}
			file = new File(Environment.getExternalStorageDirectory()
					+ "/COMMedia/CallRecording/" + CallDispatcher.sb.getTo()
					+ "_" + CallDispatcher.sb.getSignalid() + ".wav");
			if (file.exists()) {
				is = new FileInputStream(new File(
						Environment.getExternalStorageDirectory()
								+ "/COMMedia/CallRecording/"
								+ CallDispatcher.sb.getTo() + "_"
								+ CallDispatcher.sb.getSignalid() + ".wav"));
			}
			byte outputByte[] = convertStreamToByteArray(is);
			amplifyOutputStream.write(outputByte);
			amplifyOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
