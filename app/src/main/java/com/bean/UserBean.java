package com.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserBean implements Serializable {
	private String ownerName;
	private String flag;
	private String buddyName;
	private boolean selected;
	private boolean owner = false;
	private boolean allowChecking = true;
	private Boolean isTitle;
	private String header;
	private String status;
	private String occupation;
	private Boolean invite=false;
	private String groupid;
	private String profilePic;
	private String admin;
	private String role;
	private String groupname;
	private String firstname;

	public boolean isAllowChecking() {
		return allowChecking;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setAllowChecking(boolean allowChecking) {
		this.allowChecking = allowChecking;
	}

	public String getBuddyName() {
		return buddyName;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setBuddyName(String buddyName) {
		this.buddyName = buddyName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Boolean getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(Boolean isTitle) {
		this.isTitle = isTitle;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Boolean getInvite() {
		return invite;
	}

	public void setInvite(Boolean invite) {
		this.invite = invite;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
}
