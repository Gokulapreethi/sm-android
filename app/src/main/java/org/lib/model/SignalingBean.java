package org.lib.model;

import java.io.Serializable;

/**
 * This bean class is used to set and get data's Related to Signaling. This
 * class contains information like
 * SessionId,SignalId,From,To,LocalIp,Port,audiossrc
 * ,videossrc,PublicIp,ToLocalIp,ToPublicIp,CallType,Result
 * Type,Gmember,PunchingMode
 * ,BuddyConnectIp,BuddyConnectPort,Message,ConferenceMember
 * ,gmrsip,cbserver1ip,cbserver2ip
 * Router,SignalingPort,ToSignalPort,SignalPort,rdpUsername,rdpPassword,rdpPort.
 * 
 * 
 */
@SuppressWarnings("serial")
public class SignalingBean implements Serializable, Cloneable {

	private String sessionid = null;
	private String signalid = null;
	private String from = null;
	private String to = null;
	private String localip = null;
	private String port = null;
	private String audiossrc = null;
	private String videossrc = null;
	private String publicip = null;
	private String tolocalip = null;
	private String topublicip = null;
	private String callType = null;
	private String result = null;
	private String type = null;
	private String gmember = null;
	private String userId = null;
	private String netWork = null;
	private String deviceOs = null;
	private String callDuration = null;
	private String host;
	private String participants;
	private String joincall;
	private String videopromote = "no";
	private String runningcallstate;
	private String callstatus;
	private String chatid;



	private String host_name;
	private String participant_name;

	public String getNetWork() {
		return netWork;
	}

	public void setNetWork(String netWork) {
		this.netWork = netWork;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	private String punchingmode = null;
	private String videoStoped;
	private String buddyConnectip = null;
	private String buddyConnectport = null;
	private String message = null;
	private String conferencemember = "";
	private String gmrsip = null;
	private String cbserver1ip = null;
	private String cbserver2ip = null;
	private String router = null;
	private String signalingPort = null;
	private String toSignalPort = null;
	private String signalport = null;

	// for share screen
	private String rdpUsername = null;
	private String rdpPassword = null;
	private String rdpPort = null;

	private boolean isPacketFailure = false;

	private String filePath = null;
	private long storageWarningLevel;
	private String FtpUser = null;
	private String FtpPassword = null;
	private String fileid = null;
	private String url = null;
	// private SignalingBean
	private String[] forwardingDetails = null;
	private String callSubType = null;

	private String seqNo = null;
	private String ssrc = null;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private String isrobo = null;
	private String isroboreq = null;
	private Object ftpObj = null;
	private boolean isDownloadSuccess = false;

	private Object requestSource;

	public String getBs_calltype() {
		return bs_calltype;
	}

	public void setBs_calltype(String bs_calltype) {
		this.bs_calltype = bs_calltype;
	}

	public String getBs_callCategory() {
		return bs_callCategory;
	}

	public void setBs_callCategory(String bs_callCategory) {
		this.bs_callCategory = bs_callCategory;
	}

	public String getBs_callstatus() {
		return bs_callstatus;
	}

	public void setBs_callstatus(String bs_callstatus) {
		this.bs_callstatus = bs_callstatus;
	}

	public String getBs_parentid() {
		return bs_parentid;
	}

	public void setBs_parentid(String bs_parentid) {
		this.bs_parentid = bs_parentid;
	}

	// for call history bs_calltype="1" bs_callCategory="1" bs_callstatus =""
	private String bs_calltype=null;
	private String bs_callCategory=null;
	private String bs_callstatus=null;
	private String bs_parentid=null;
	private String startTime=null;
	private String endTime=null;
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Object getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Object requestSource) {
		this.requestSource = requestSource;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getSsrc() {
		return ssrc;
	}

	public void setSsrc(String ssrc) {
		this.ssrc = ssrc;
	}

	public String getCallSubType() {
		return callSubType;
	}

	public void setCallSubType(String callSubType) {
		this.callSubType = callSubType;
	}

	public String[] getForwardingDetails() {
		return forwardingDetails;
	}

	public void setForwardingDetails(String[] forwardingDetails) {
		this.forwardingDetails = forwardingDetails;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getStorageWarningLevel() {
		return storageWarningLevel;
	}

	public void setStorageWarningLevel(long storageWarningLevel) {
		this.storageWarningLevel = storageWarningLevel;
	}

	public boolean isPacketFailure() {
		return isPacketFailure;
	}

	public void setPacketFailure(boolean isPacketFailure) {
		this.isPacketFailure = isPacketFailure;
	}

	public String getGmember() {
		return gmember;
	}

	public void setGmember(String gmember) {
		this.gmember = gmember;
	}

	public String getSignalport() {
		return signalport;
	}

	public void setSignalport(String signalport) {
		this.signalport = signalport;
	}

	public String getToSignalPort() {
		return toSignalPort;
	}

	public void setToSignalPort(String toSignalPort) {
		this.toSignalPort = toSignalPort;
	}

	public String getSignalingPort() {
		return signalingPort;
	}

	public void setSignalingPort(String signalingPort) {
		this.signalingPort = signalingPort;
	}

	public String getSignalid() {
		return signalid;
	}

	public String getFtpUser() {
		return this.FtpUser;
	}

	public String getFtppassword() {
		return this.FtpPassword;
	}

	public void setSignalid(String signalid) {
		this.signalid = signalid;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public String getBuddyConnectip() {
		return buddyConnectip;
	}

	public void setBuddyConnectip(String buddyConnectip) {
		this.buddyConnectip = buddyConnectip;
	}

	public String getBuddyConnectport() {
		return buddyConnectport;
	}

	public void setBuddyConnectport(String buddyConnectport) {
		this.buddyConnectport = buddyConnectport;
	}

	public String getGmrsip() {
		return gmrsip;
	}

	public void setGmrsip(String gmrsip) {
		this.gmrsip = gmrsip;
	}

	public String getCbserver1ip() {
		return cbserver1ip;
	}

	public void setCbserver1ip(String cbserver1ip) {
		this.cbserver1ip = cbserver1ip;
	}

	public String getCbserver2ip() {
		return cbserver2ip;
	}

	public void setCbserver2ip(String cbserver2ip) {
		this.cbserver2ip = cbserver2ip;
	}

	public String getConferencemember() {
		return conferencemember;
	}

	public void setConferencemember(String conferencemember) {
		this.conferencemember = conferencemember;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRdpUsername(String rdpUsername) {
		this.rdpUsername = rdpUsername;
	}

	public void setRdpPassword(String rdpPassword) {
		this.rdpPassword = rdpPassword;
	}

	public void setRdpPort(String rdpPort) {
		this.rdpPort = rdpPort;
	}

	public void setFtpUser(String user) {
		this.FtpUser = user;
	}

	public void setFtppassword(String pass) {
		this.FtpPassword = pass;
	}

	public void setFileId(String id) {
		this.fileid = id;
	}

	public String getPunchingmode() {
		return punchingmode;
	}

	public void setPunchingmode(String punchingmode) {
		this.punchingmode = punchingmode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getLocalip() {
		return localip;
	}

	public void setLocalip(String localip) {
		this.localip = localip;
	}

	public String getAudiossrc() {
		return audiossrc;
	}

	public void setAudiossrc(String audiossrc) {
		this.audiossrc = audiossrc;
	}

	public String getVideossrc() {
		return videossrc;
	}

	public void setVideossrc(String videossrc) {
		this.videossrc = videossrc;
	}

	public String getPublicip() {
		return publicip;
	}

	public void setPublicip(String publicip) {
		this.publicip = publicip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTolocalip() {
		return tolocalip;
	}

	public void setTolocalip(String tolocalip) {
		this.tolocalip = tolocalip;
	}

	public String getTopublicip() {
		return topublicip;
	}

	public void setTopublicip(String topublicip) {
		this.topublicip = topublicip;
	}

	public String getRdpUsername() {
		return rdpUsername;
	}

	public String getRdpPassword() {
		return rdpPassword;
	}

	public String getRdpPort() {
		return rdpPort;
	}

	public void setisRobo(String robo) {
		this.isrobo = robo;
	}

	public String getisRobo() {
		return this.isrobo;
	}

	public void setftpObj(Object obj) {
		this.ftpObj = obj;
	}

	public Object getFtpObj() {
		return this.ftpObj;
	}

	public void setIsdownloadSuccess(boolean res) {
		this.isDownloadSuccess = res;
	}

	public boolean getIsDownloadSuccess() {
		return this.isDownloadSuccess;
	}

	public void setisrobokey(String val) {
		this.isroboreq = val;
	}

	public String getisRoboKey() {
		return this.isroboreq;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getJoincall() {
		return joincall;
	}

	public void setJoincall(String joincall) {
		this.joincall = joincall;
	}

	public String getVideopromote() {
		return videopromote;
	}

	public void setVideopromote(String videopromote) {
		this.videopromote = videopromote;
	}

	public String getVideoStoped() {
		return videoStoped;
	}

	public void setVideoStoped(String videoStoped) {
		this.videoStoped = videoStoped;
	}

	public String getRunningcallstate() {
		return runningcallstate;
	}

	public void setRunningcallstate(String runningcallstate) {
		this.runningcallstate = runningcallstate;
	}

	public String getCallstatus() {
		return callstatus;
	}

	public void setCallstatus(String callstatus) {
		this.callstatus = callstatus;
	}

	public String getChatid() {
		return chatid;
	}

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}

	public Object clone() {
		try {
			SignalingBean cloned = (SignalingBean) super.clone();

			return cloned;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public String getHost_name() {
		return host_name;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public String getParticipant_name() {
		return participant_name;
	}

	public void setParticipant_name(String participant_name) {
		this.participant_name = participant_name;
	}

}
