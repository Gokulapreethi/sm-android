package org.core;

import java.net.InetAddress;
import java.util.TimerTask;

import org.lib.model.SignalingBean;
import org.net.udp.UDPEngine;

import android.util.Log;

/**
 * This class extends TimerTask. This class is used to Retransmit the
 * Proprietary Signaling.
 * 
 * 
 */
class ProprietaryRetransmission extends TimerTask {

	/**
	 * This class is used to send and Receive DatagramPacket using Using
	 * UDPServer(DatagramSocket).
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
	private String connectip = null;
	/**
	 * Port to connect.
	 */
	private int connectport;
	/**
	 * Router Ip Address.
	 */
	private String routerip = null;
	/**
	 * Router Port.
	 */
	private int routerPort;
	/**
	 * Unique Signal Id for the Particular Signal.
	 */
	private String signalid;

	private SignalingBean sb = null;
	private String singalMessage;

	/**
	 * Constructor to initialize values
	 * 
	 * @param retransmissionCount
	 *            maximum Retransmission count.
	 * @param udpEngine
	 *            UDPEngine to send Signal.
	 * @param message
	 *            Signal message.
	 * @param sb
	 *            signaling informations.
	 */
	public ProprietaryRetransmission(int retransmissionCount,
			UDPEngine udpEngine, byte[] message, SignalingBean sb) {
		this.udpEngine = udpEngine;
		this.message = message;
		this.sb = sb;
	}

	/**
	 * Sets the ServerIp to send Signal.
	 * 
	 * @param routerip
	 *            ServerIp
	 */
	public void setRouterip(String routerip) {
		this.routerip = routerip;
	}

	/**
	 * Sets the ServerPort to send Signal.
	 * 
	 * @param routerPort
	 *            ServerPort
	 */
	public void setRouterPort(int routerPort) {
		this.routerPort = routerPort;
	}

	/**
	 * Get the Destination IpAddress.
	 * 
	 * @return DestinationIp
	 */
	public String getConnectip() {
		return connectip;
	}

	/**
	 * Sets the destinationIp
	 * 
	 * @param connectip
	 *            DestinationIp
	 */
	public void setConnectip(String connectip) {
		this.connectip = connectip;
	}

	/**
	 * Get the Destination Port.
	 * 
	 * @return DestinationPort
	 */
	public int getConnectport() {
		return connectport;
	}

	/**
	 * Set the Destination Port.
	 * 
	 * @param connectport
	 *            Destination Port
	 */
	public void setConnectport(int connectport) {
		this.connectport = connectport;
	}

	/**
	 * Used to Retransmit the Signal on local.If it get failure Retransmit the
	 * Signal to Server.
	 */
	@Override
	public void run() {
		try {
			// System.out.println("Count :"+count);
			if (count == 3) {

				if (connectip.equals(routerip)) {
					// System.out.println("Count reached maximum level retransimission stopped");

					try {

						if (sb.getType().equals("3")
								|| sb.getType().equals("1")
								&& sb.getResult().equals("1")) {
							if (ProprietarySignalling.messageSessionTable
									.containsKey(sb.getSignalid())) {
								ProprietarySignalling.messageSessionTable
										.remove(sb.getSignalid());
								Log.d("SIGNALID", "REMOVED signalid "
										+ signalid);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					this.cancel();
				} else {
					// System.out.println("******************************************************************************************");
					// System.out.println("Since unable to connect buddy in loccal network trying to connect through server........");

					// System.out.println("routerip :"+routerip);
					// System.out.println("routerPort :"+routerPort);

					connectip = routerip;
					connectport = routerPort;
					count = 0;
				}

			} else {
				// System.out.println("*********************************************");
				// System.out.println("Retramission Timer Code Executed Count="+count);
				// System.out.println(new String(message));

			
				
				Log.d("UDP_SIGNAL", "Signal Sent-1  IP : " + connectip
						+ ", Port : " + connectport +", Message : "+singalMessage);
				
				
				
				udpEngine.sendUDP(message, InetAddress.getByName(connectip),
						connectport);
				// System.out.println("*********************************************");
				count++;
			}
		} catch (Exception e) {
			this.cancel();
			e.printStackTrace();
		}

	}

	public void setOrginalSignal(String xml) {
		this.singalMessage = xml;
		
	}

}
