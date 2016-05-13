                package org.core;

import java.util.Timer;
/**
 * This bean class is used to set and get data's Related to RetransmissionTimer. This class contains information like
 * callStatus,timer,message,sendCount,retransmissionCount.
 * 
 *
 */
public class TimerBean {

	/**
	 * This enum is used to set flags for CallStatus.
	 */
	private CallStatus callStatus=null;
	
	private CallStatus1 callStatus1=null;
	
	public CallStatus1 getCallStatus1() {
		return callStatus1;
	}
	public void setCallStatus1(CallStatus1 callStatus1) {
		this.callStatus1 = callStatus1;
	}
	/**
	 * Timers schedule one-shot or recurring tasks for execution. Prefer ScheduledThreadPoolExecutor for new code. 
    * Each timer has one thread on which tasks are executed sequentially. When this thread is busy running a task, runnable tasks may be subject to delays.
	 */
	private Timer timer=null;
	/**
	 * Message to send.
	 */
	private byte[] message;
	/**
	 * Sending count.
	 */
	private int sendCount;
	/**
	 * Retransmission Count.
	 */
	private int retransmissionCount;
	
	public CallStatus getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public byte[] getMessage() {
		return message;
	}
	public void setMessage(byte[] message) {
		this.message = message;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public int getRetransmissionCount() {
		return retransmissionCount;
	}
	public void setRetransmissionCount(int retransmissionCount) {
		this.retransmissionCount = retransmissionCount;
	}
	
	
	
	
}
