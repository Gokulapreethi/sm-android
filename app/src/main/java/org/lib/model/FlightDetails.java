package org.lib.model;

import java.io.Serializable;
/**
 * This bean class is used to set and get data's Related to FlightDetails. This class contains information 
 * like FlightNumber,Source,Destination,Status and Delayed by Time.
 * 
 *
 */
@SuppressWarnings("serial")
public class FlightDetails implements Serializable {
	private String flightNumber=null;
	private String source=null;
	private String destination=null;
	private String status=null;
	private String delayedBy=null;
	
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDelayedBy() {
		return delayedBy;
	}
	public void setDelayedBy(String delayedBy) {
		this.delayedBy = delayedBy;
	}



	

}
