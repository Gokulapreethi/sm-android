package org.core;

/**
 * Interface used to notify the ProprietyResponse and SIPResponse.
 * 
 * 
 */
public interface CallSessionListener {
	/**
	 * Used to Notify SIPResponse.
	 * 
	 * @param obj
	 */
	void notifySIPResponse(Object obj);

	/**
	 * Used to Notify ProprietyResponse.
	 * 
	 * @param obj
	 *            Object as Reference.
	 */
	void notifyProprietyResponse(Object obj);

	void notifytype2Sent();
	// void notifyUdpMessage();

}
