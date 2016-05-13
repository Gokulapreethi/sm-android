package org.lib.webservice;

/**
 * This interface is used to notify the WebService Response message like
 * SIGNIN,SUBSCRIBE etc. This is also used to Notify Error(failure).
 * 
 * 
 */
public interface WebServiceCallback {
	/**
	 * Notify the WebService Responses like SIGNIN,SUBSCRIBE etc.
	 * 
	 * @param servicebean
	 *            instance used to get and set value.
	 */
	public void notifyWebServiceResponse(Servicebean servicebean);

	/**
	 * Notify the Error Responses for WebService Call.(Due to Network
	 * Failure,Server Failure etc..)
	 * 
	 * @param errorMsg
	 *            Show the Error cause.
	 */
	public void notifyError(String errorMsg, EnumWebServiceMethods methodType);
}
