package org.lib.model;

import java.util.ArrayList;

/**
 * This bean class is used to set and get data's Related to BuddyInformation.
 * This class contains information like BuddyName,Status,Local
 * IpAddress,ExternalIpAddress,Signaling Port,Email Id,Type.
 * 
 * 
 */

public class BuddyInformationBean {
	private String sepration;
	private String name = null;
	private String status = null;
	private String localipaddress = null;
	private String externalipaddress = null;
	private String emailid = null;
	private String type = null;
	private String signalingPort = null;
	private byte[] photo = null;
	private String fb_id = null;
	private String fb_name = null;
	private String latitude = null;
	private String longitude = null;
	private String distance;
    private String header = "NULL";

	// 17.12.14
	private String mode;
	private String instantmessage;

	// private String vUserName=null;
	private String serviceavailable = null;
	private String owner = null;
	private String vUserId = null;
	private String action = null;
	private String friendsName = null;
	private ArrayList<String[]> profileModifiedDate = null;
	private int messageCount;
	private String lastMessage = "";
	private String occupation = "";
	private boolean isTitle =false;
	private String nickname=null;
	private String firstname=null;
	private String lastname=null;

	private String profile_picpath;
    private boolean selected;
	private String role;

	public ArrayList<String[]> getProfileModifiedDate() {
		return profileModifiedDate;
	}

	public void setProfileModifiedDate(ArrayList<String[]> profileModifiedDate) {
		this.profileModifiedDate = profileModifiedDate;
	}

	private boolean islocationBasedServiceDone = false;

	public boolean isIslocationBasedServiceDone() {
		return islocationBasedServiceDone;
	}

	public void setIslocationBasedServiceDone(boolean islocationBasedServiceDone) {
		this.islocationBasedServiceDone = islocationBasedServiceDone;
	}

	public String getFriendsName() {
		return friendsName;
	}

	public void setFriendsName(String friendsName) {
		this.friendsName = friendsName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	// public void setVirtualUserName(String name){
	// this.vUserName=name;
	// }
	public void setServiceavailable(String service) {
		this.serviceavailable = service;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setVirtualUserId(String id) {
		this.vUserId = id;
	}

	// public String getVirtualUserName(){
	// return this.vUserName;
	// }
	public String getServiceavailable() {
		return this.serviceavailable;
	}

	public String getOwner() {
		return this.owner;
	}

	public String getVirtualUserId() {
		return this.vUserId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setFb_id(String fb_Id1) {
		this.fb_id = fb_Id1;
	}

	public String getFbID() {
		return this.fb_id;
	}

	public String getSignalingPort() {
		return signalingPort;
	}

	public void setSignalingPort(String signalingPort) {
		this.signalingPort = signalingPort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocalipaddress() {
		return localipaddress;
	}

	public void setLocalipaddress(String localipaddress) {
		this.localipaddress = localipaddress;
	}

	public String getExternalipaddress() {
		return externalipaddress;
	}

	public void setExternalipaddress(String externalipaddress) {
		this.externalipaddress = externalipaddress;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public void setfbName(String name) {
		this.fb_name = name;
	}

	public String getFBname() {
		return this.fb_name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the profile_picpath
	 */
	public String getProfile_picpath() {
		return profile_picpath;
	}

	/**
	 * @param profile_picpath
	 *            the profile_picpath to set
	 */
	public void setProfile_picpath(String profile_picpath) {
		this.profile_picpath = profile_picpath;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getInstantmessage() {
		return instantmessage;
	}

	public void setInstantmessage(String instantmessage) {
		this.instantmessage = instantmessage;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getSeparation() {
		return sepration;
	}

	public void setSeparation(String separation) {
		this.sepration = separation;
	}

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

    public String toString(){
        return " ==== " + name +
                " ==== " + emailid +
                " ==== " + nickname +
                " ==== " + status +
                " ==== " + localipaddress +
                " ==== " + externalipaddress +
                " ==== " + profile_picpath +
                " ==== " + mode +
                " ==== " + signalingPort +
                " ==== " + latitude +
                " ==== " + longitude +
                " ==== " + distance +
                " ==== " + occupation +
                " ==== " + firstname +
                " ==== " + lastname;
    }
}
