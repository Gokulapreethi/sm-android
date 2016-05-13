package com.cg.broadcastreceivers;

/**
 * When device received any telephony calls at that time
 * {@link ReminderBroadcastReceiver} notify to our ui via PhoneStateChanged
 * interface
 * 
 * 
 * 
 */
public interface PhoneStateChanged {

	public void incomingCall();

	public void GSMCallAccepted(boolean falg);

}
