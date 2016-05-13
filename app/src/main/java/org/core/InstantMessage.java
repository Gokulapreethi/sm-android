package org.core;

import java.util.ArrayList;

import org.lib.model.SignalingBean;
/**
 * This class is used to send InstantMessage. The InstantMessages are added in the Queue.
 * It send the next message only after Receive Acknowledgement of First Message. 
 * 
 *
 */
public class InstantMessage {

	private ArrayList<SignalingBean> messageList=null;
	private ProprietarySignalling signalling=null;

/**
 *  Constructor to initialize and set ProprietarySignalling reference.
 * @param signalling ProprietarySignalling reference
 */
	public InstantMessage(ProprietarySignalling signalling) {
		this.signalling=signalling;
		messageList=new ArrayList<SignalingBean>();
	}
/**
 * Used to add message to the Queue.
 * @param sb Message
 */
	public void addMessage(SignalingBean sb){
		messageList.add(sb);
//		if(messageList.size()==1){
//			signalling.sendMessage(sb);
//		}
		sendMesage();
//		else{
//			
//			
//		}
	}
	
	
	public ArrayList<SignalingBean> getMessageList() {
		return messageList;

	}
	
		
/**
 * Used to remove message from the Queue.
 */
	public void removeMessage(){
		if(messageList.size()>0){
			messageList.remove(0);
		}
	}
/**
 * Send message if there is no Acknowledgement is pending.
 */
	public void sendMesage(){
		if(messageList.size()>=1){
           signalling.sendMessage(messageList.get(0));
		}
	}

	/**
	 *  used to check and send Message.
	 *  
	 * @param status Result
	 */
	public void notifyResponse(int status){
		if(status==0){
		removeMessage();	
		}
		sendMesage();
	}
}
