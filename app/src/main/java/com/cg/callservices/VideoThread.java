package com.cg.callservices;

import org.util.Queue;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This thread is used to retrieve the video data from the {@link Queue} and
 * send that to the handler to update the video frames
 * 
 * @author
 * 
 */
public class VideoThread extends Thread {

	private Handler handler = null;
	private Queue videoQueue = null;
	private boolean running = true;

	/**
	 * Constructor method of the VideoThread. Here we are going to initialize
	 * the {@link Queue}instance
	 * 
	 * @param videoQueue
	 */
	public VideoThread(Queue videoQueue) {
		this.videoQueue = videoQueue;
	}

	/**
	 * To return the handler instance
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * To initialize the Handler instance
	 * 
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * To retrieve the data from the Queue and send that to the handler to
	 * update the video frames
	 */
	public void run() {

		try {
			while (running) {
				// System.out.println("videoQueue :" + videoQueue.getSize());

				byte data[] = (byte[]) videoQueue.getMsg();
				// System.out.println(data.length);
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putByteArray("img", data);
				msg.obj = bundle;
				handler.sendMessage(msg);
				msg = null;
				bundle = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To stop the video thread running
	 */
	public void stopVideoThread() {
		running = false;
	}

}
