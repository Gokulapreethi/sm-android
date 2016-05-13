package org.core;

import org.net.udp.UDPEngine;

public class PortRefreshDatas {
	
	private String mkeyZero=null;
	private String mKeyOne=null;
	private ProprietarySignalling pSignalling=null;
	private UDPEngine udpEngine=null;
	private int count=0;
	
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getMkeyZero() {
		return mkeyZero;
	}
	public void setMkeyZero(String mkeyZero) {
		this.mkeyZero = mkeyZero;
	}
	public String getmKeyOne() {
		return mKeyOne;
	}
	public void setmKeyOne(String mKeyOne) {
		this.mKeyOne = mKeyOne;
	}
	public ProprietarySignalling getpSignalling() {
		return pSignalling;
	}
	public void setpSignalling(ProprietarySignalling pSignalling) {
		this.pSignalling = pSignalling;
	}
	public UDPEngine getUdpEngine() {
		return udpEngine;
	}
	public void setUdpEngine(UDPEngine udpEngine) {
		this.udpEngine = udpEngine;
	}
	
	
	

}
