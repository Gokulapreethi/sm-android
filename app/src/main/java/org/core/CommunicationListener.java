package org.core;

import org.lib.model.SignalingBean;
/**
 *Used to Notify the CallType.
 * 
 *
 */
public interface CommunicationListener {

	public void notifyInvite(SignalingBean sb);
	public void notifyAccept(SignalingBean sb);
	public void notifyAcceptResponse(SignalingBean sb);
	public void notifyReject(SignalingBean sb);
	public void notifyBye(SignalingBean sb);
	
}
