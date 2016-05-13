package com.cg.commonclass;

/**
 * 
 * This bean instance is used to maintain the name,status and checked details
 * 
 * 
 * 
 */
public class Databean {

	private String name;

	private String Status;

	public boolean selected;

	private String instantmessage;

	private String distance;

	private String address;

	private String profile_picurepath;

	/**
	 * To get the Name of the Buddy
	 * 
	 * @return
	 */
	public String getname() {
		return name;
	}

	/**
	 * To set the Name of the Buddy
	 * 
	 * @param orderName
	 */
	public void setname(String orderName) {
		this.name = orderName;
	}

	/**
	 * To get the status of the buddy
	 * 
	 * @return
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * To set the status of the buddy
	 * 
	 * @param orderStatus
	 */
	public void setStatus(String orderStatus) {
		this.Status = orderStatus;
	}

	/**
	 * To set the Selection status
	 * 
	 * @param selection
	 */
	public void setSelected(boolean selection) {
		this.selected = selection;
	}

	/**
	 * To get the selection status
	 * 
	 * @return
	 */
	public boolean getSelection() {
		return this.selected;
	}

	public String getInstantmessage() {
		return instantmessage;
	}

	public void setInstantmessage(String instantmessage) {
		this.instantmessage = instantmessage;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the profile_picurepath
	 */
	public String getProfile_picurepath() {
		return profile_picurepath;
	}

	/**
	 * @param profile_picurepath
	 *            the profile_picurepath to set
	 */
	public void setProfile_picurepath(String profile_picurepath) {
		this.profile_picurepath = profile_picurepath;
	}

}