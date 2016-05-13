package org.core;

import java.net.InetAddress;
import java.util.TimerTask;

import org.net.udp.UDPEngine;
/**
 * This class extends TimerTask. This class is used to Retransmit the Proprietary Signaling of InstantMessage.
 * 
 *
 */
public class ProprietaryRetransmissionIm extends TimerTask {
	/**
	 *This class is used to send and Receive DatagramPacket using Using UDPServer(DatagramSocket). 
	 */
	private UDPEngine udpEngine;
	/**
	 * Used to set the Maximum ReTransmission Count. Initially this can be Zero.
	 */
	private int retransmissionCount;
	/**
	 * Number of counts to send. 
	 */
	private int count;
	/**
	 * Used to Initialize the message to Send.
	 */
	private byte[] message;
	/**
	 * Ip address to connect.
	 * 
	 */
	private String connectip=null;
	/**
	 * Port to connect.
	 */
	private int connectport;
	/**
	 * Router Ip Address.
	 */
	private String routerip=null;
	/** 
	 * Router Port.
	 */
	private int routerPort;

	/**
	 * Constructor to initialize values 
	 * @param retransmissionCount
	 * @param udpEngine udpEngine UDPEngine to send Signal.
	 * @param message message Signal message.
	 */
	public ProprietaryRetransmissionIm(int retransmissionCount,UDPEngine udpEngine,byte[] message) {
		this.udpEngine=udpEngine;
		this.message=message;
	}



	/**
	 * Sets the ServerIp to send Signal. 
	 * @param routerip ServerIp
	 */
	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}



	/**
	 * Sets the ServerPort to send Signal. 
	 * @param routerPort ServerPort
	 */
	public void setRouterPort(int routerPort) {
		this.routerPort = routerPort;
	}



	/**
	 * Get the Destination IpAddress.
	 * @return DestinationIp
	 */
	public String getConnectip() {
		return connectip;
	}



	/**
	 * Sets the destinationIp 
	 * @param connectip DestinationIp
	 */
	public void setConnectip(String connectip) {
		this.connectip = connectip;
	}



	/**
	 * Get the Destination Port.
	 * @return DestinationPort
	 */
	public int getConnectport() {
		return connectport;
	}



	/**
	 * Set the Destination Port.
	 * @param connectport Destination Port
	 */
	public void setConnectport(int connectport) {
		this.connectport = connectport;
	}



	/**
	 * Used to Retransmit the InstantMessage on local.If it get failure Retransmit the InstantMessage to Server. 
	 */
	@Override
	public void run() {
		try{
			//System.out.println("Count :"+count);
			if (count == 3) {
				
				
				
				if(connectip.equals(routerip)){
					//System.out.println("Count reached maximum level retransimission stopped");
					udpEngine.sendUDP(message,InetAddress.getByName(connectip),connectport);
					this.cancel();
				}
				else{
					//System.out.println("******************************************************************************************");
					//System.out.println("Since unable to connect buddy in loccal network trying to connect through server........");
					
					//System.out.println("routerip :"+routerip);
					//System.out.println("routerPort :"+routerPort);
					
					connectip=routerip;
					connectport=routerPort;
					count=0;
				}
				
				

			}else{
				//System.out.println("*********************************************");
				//System.out.println("Retramission Timer Code Executed Count="+count);
				//System.out.println(new String(message));
				udpEngine.sendUDP(message,InetAddress.getByName(connectip),connectport);
				//System.out.println("*********************************************");
				count++;
			}
		}catch(Exception e){
			this.cancel();
			e.printStackTrace();	
		}

	}




}

