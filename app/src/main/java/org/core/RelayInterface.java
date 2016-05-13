package org.core;




public interface RelayInterface {
	
	/**
	 * Called to join with relay server
	 */
	public void sendMediaJoinMessage();
	
	/**
	 * Called when ever MediaTimer elapsed  after 25 seconds 
	 */
	public void JoinRelay();
	 
}
