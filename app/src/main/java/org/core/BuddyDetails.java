package org.core;
/**
 * This bean class is used to get and set data's related to Buddy Details. This class contains information 
 * like ConnectIp,ConnectPort,AudioMediaid and VideoMediaid.
 * 
 *
 */
public class BuddyDetails {

	private String connectip=null;
	private int connectport;
	private long audiomediaid;
	private long videomediaid;
	public String getConnectip() {
		return connectip;
	}
	public void setConnectip(String connectip) {
		this.connectip = connectip;
	}
	public int getConnectport() {
		return connectport;
	}
	public void setConnectport(int connectport) {
		this.connectport = connectport;
	}
	public long getAudiomediaid() {
		return audiomediaid;
	}
	public void setAudiomediaid(long audiomediaid) {
		this.audiomediaid = audiomediaid;
	}
	public long getVideomediaid() {
		return videomediaid;
	}
	public void setVideomediaid(long videomediaid) {
		this.videomediaid = videomediaid;
	}
	
	
	
	
}
