package org.core;

import java.util.HashMap;
import java.util.TimerTask;

import org.net.udp.UDPEngine;

/**
 *This class extends TimerTask. This class is used to Retransmit the UDP signaling.The Timer gets canceled if it get Response or Time Out.   
 * 
 *
 */
public class RetranmisionTimer extends TimerTask{
	/**
	 * sending count.
	 */
	private int count = 0;
	/**
	 * Message to send.
	 */
	private String message;
	/**
	 * Retransmission count for Signal.
	 */
	private int retransmissionCount;
	/**
	 *This class is used to send and Receive DatagramPacket using Using UDPServer(DatagramSocket).

	 */
	private UDPEngine engine;
	/**
	 * Unique Id for a session.
	 */
	private String sessionId;
	/**
	 * Used to store and Retrieve Session Id's.
	 */
	HashMap<String, TimerBean> sessionIdStack=null;
	private static String TAG="RetranmisionTimer";
/**
 * Constructor used to initialize Timer 
 * @param message Message to send.
 * @param count maximum ReSend count.
 * @param engine UDPEngine to send signal.
 * @param sessionId SessionId of the Signal.
 * @param sessionIdStack  Hashmap holding the Timerbean.
 */
	public RetranmisionTimer(String message,int count,UDPEngine engine,String sessionId,HashMap<String, TimerBean> sessionIdStack) {
		this.message=message;
		this.retransmissionCount=count;
		this.engine=engine;
		this.sessionId=sessionId;
		this.sessionIdStack=sessionIdStack;
	}

/**
 * Used to send the UDPData using UDPEngine. This can used to Retransmit the Signal. If it gets the Maximum level it gets cancel.
 */
	@Override

//if it reach maximum count it gets cancel.
	public void run() {

			
		

		try {

			engine.sendUDP(message.getBytes());
//			System.out.println("Retramission Timer Code Executed Count="+count);

			if (count == retransmissionCount) {
//				System.out.println("Retramission Timer stoped. Count :"+count);
				if(sessionIdStack.containsKey(sessionId)){
					TimerBean timerBean=sessionIdStack.get(sessionId);
					if(timerBean!=null){
						timerBean.getTimer().cancel();
					}
					sessionIdStack.remove(sessionId);
				}
				this.cancel();
			}
			else {
				count=count+1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			count++;
		}


	}



}
