package org.core;

import org.net.rtp.RtpPacket;
import org.util.Queue;


public class VideoSenderThread extends Thread{


	private boolean running=false;
	private Queue videoRtpQueue=null;
	private CommunicationEngine engine=null; 

	public VideoSenderThread(Queue videoRtpQueue,CommunicationEngine engine) {
	this.videoRtpQueue=videoRtpQueue;
	this.engine=engine;
	
	}
	
	public void run(){
		running=true;
		while(running){
			try{
				RtpPacket rtpPacket= (RtpPacket) videoRtpQueue.getMsg();
				if(rtpPacket!=null){
					engine.send(rtpPacket);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}



	public void stopVideoSender(){
		running=false;
	}

}
