package org.core;

import org.util.Queue;

import android.util.Log;

/**
 * This class extends Thread. This class is used to Process the incoming video
 * data from the Queue and Notify it to Decode.
 * 
 * 
 */
public class VideoThread extends Thread {

	/**
	 * Queue used to Retrieve the Video data
	 */
	private Queue videoQueue = null;
	/**
	 * This bean class is used to set and get datas related to WebService
	 * response. This class contains information like Result and Text.
	 */
	private CommunicationEngine communicationEngine = null;
	/**
	 * Used to maintain the VideoThread State.
	 */
	private boolean running = true;

	/**
	 * Assign the VideoQueue.The Video Queue which contains the Video data.
	 * 
	 * @param videoQueue
	 *            Queue holding incoming VideoData
	 */
	public VideoThread(Queue videoQueue) {
		this.videoQueue = videoQueue;
	}

	/**
	 * used to stop the VideoThread.
	 */
	public void stopVideoThread() {
		Log.d("VIDEO", "Stop video queue " + running);
		running = false;
	}

	/**
	 * Used to set communicationEngine Reference to Notify Video Data.
	 * 
	 * @param communicationEngine
	 *            CommunicationEngine reference
	 */
	public void setCommunicationEngine(CommunicationEngine communicationEngine) {
		this.communicationEngine = communicationEngine;
	}

	/**
	 * used to get the message from VideoQueue and Notify it to Communication
	 * Engine.
	 */
	public void run() {
		// Log.d("VIDEO", "Start video queue "+running);
		if (communicationEngine != null) {
			videoQueue = communicationEngine.getVideoQueue();
			// Log.d("VIDEO", "Start video queue not Null ");
		}
		while (running) {
			try {
				// Log.d("VIDEO", "Running video queue ");
				if (videoQueue != null) {
					// System.out.println("#############");
					VideoBean bean = (VideoBean) videoQueue.getMsg();
					if (communicationEngine != null) {
						// System.out.println("Index :"+bean.getDecoderIndex()+" SSRC :"+bean.getSsrc());

						communicationEngine.decodeVideoFrame(bean.getData(),
								bean.getSsrc(), bean.getDecoderIndex());
					}
					bean = null;
				} else {
					// Log.d("VIDEO", "Video Queue Null");
				}
			} catch (Exception e) {
				Log.d("VIDEO", "Video Queue x" + e);
				e.printStackTrace();
			}
		}

	}

}
