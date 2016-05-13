package org.core;

/**
 * Interface used to notify incoming video data.
 * 
 *
 */
public interface VideoCallback {
	/**
	 * used to Notify the Decoded Video Data.
	 * @param data VideoData
	 * @param ssrc VideoSsrc
	 */
public void notifyDecodedVideoCallback(byte data[],long ssrc);
public void notifyResolution(int w,int h);
}
