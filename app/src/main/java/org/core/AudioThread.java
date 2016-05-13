package org.core;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
/**
 * Used to send Audio data. This class sends the Audio data which was stored in the Queue.
 * This audio data are later send using RTPEngine.
 *
 */
public class AudioThread implements Runnable{

	private ArrayList<String> mediaKey;
	private HashMap<String, CallsOverInternet> callTable;
	private Queue audioFrameQueue;
	private boolean running=true;
/**
 * Constructor used to Initialize a AudioThread.
 * @param mediaKey Unique key for Media.
 * @param callTable CallTable Contains Call Details.
 * @param audioFrameQueue Queue contains Audio Packets. 
 */
	public AudioThread(ArrayList<String> mediaKey,HashMap<String, CallsOverInternet> callTable,Queue audioFrameQueue) {
		this.mediaKey=mediaKey;
		this.callTable=callTable;
		this.audioFrameQueue=audioFrameQueue;
	}
	/**
	 * USed to send the AudioData from the Audio Queue using RTPEngine.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (running) 
		{
			try{

				if(audioFrameQueue!=null){
					byte aDataFrame[] = (byte[]) audioFrameQueue.getMsg();
					for(int i=0;i<mediaKey.size();i++){
						String key =mediaKey.get(i);
						CallsOverInternet callObj=callTable.get(key);
						Log.d("Close", "close the call on audio thread");
						//System.out.println("callObj :"+callObj);
						if(callObj!=null){
						//System.out.println("Audio :"+callObj.getAudiossrc()+" IP "+callObj.getBuddyConnectip());
							callObj.getRtpEngine().sendData(callObj.getAudiossrc(),callObj.getBuddyConnectip(), aDataFrame);
							
							
						}
					}


				}





			}catch(Exception e){
				Log.d("Close", "close the call audiothread exception");
				e.printStackTrace();
			}

		}
	}
	/**
	 * Used To Stop Audio Thread.
	 */
	public void stopAudioThread(){
		running=false;
	}
	


}
