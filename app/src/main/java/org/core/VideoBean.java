package org.core;
/**
 * This bean class is used to set and get data's related to Video. This class contains information like 
 * Video data,Ssrc,decoderIndex. 
 * 
 *
 */
public class VideoBean {
	private byte[] data=null;
	private long ssrc;
	private int decoderIndex;
	
	
	public int getDecoderIndex() {
		return decoderIndex;
	}
	public void setDecoderIndex(int decoderIndex) {
		this.decoderIndex = decoderIndex;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public long getSsrc() {
		return ssrc;
	}
	public void setSsrc(long ssrc) {
		this.ssrc = ssrc;
	}
	

}
