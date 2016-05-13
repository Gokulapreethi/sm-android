package org.lib.model;

import java.io.Serializable;

public class GroupBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String groupId = "";
	private String groupName;
	private String createdDate;
	private String modifiedDate;
	private String activeGroupMembers;
	private String inActiveGroupMembers;
	private String deleteGroupMembers;
	private String groupMembers;
	private String nonGroupMembers;
	private String newGroupMembers;
	private String ownerName;
	private String groupStatus;
	private String unableToAdd;
	private String result;
	private String mode;
	private String method;
	private String userName;
	private String lastMsg;
	private Object callback;
	private String category;
	private String groupProfilePic;
	private String recentDate="";
	private int messageCount;
	private String type="";
	private String groupdescription;
	private String groupIcon;
	private String status;
	private String inviteMembers;
	private String grouptype;
	private String adminMember;
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(String groupMembers) {
		this.groupMembers = groupMembers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUnableToAdd() {
		return unableToAdd;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setUnableToAdd(String unableToAdd) {
		this.unableToAdd = unableToAdd;
	}

	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActiveGroupMembers() {
		return activeGroupMembers;
	}

	public void setActiveGroupMembers(String activeGroupMembers) {
		this.activeGroupMembers = activeGroupMembers;
	}

	public String getInActiveGroupMembers() {
		return inActiveGroupMembers;
	}

	public void setInActiveGroupMembers(String inActiveGroupMembers) {
		this.inActiveGroupMembers = inActiveGroupMembers;
	}

	public String getDeleteGroupMembers() {
		return deleteGroupMembers;
	}

	public void setDeleteGroupMembers(String deleteGroupMembers) {
		this.deleteGroupMembers = deleteGroupMembers;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getNonGroupMembers() {
		return nonGroupMembers;
	}

	public void setNonGroupMembers(String nonGroupMembers) {
		this.nonGroupMembers = nonGroupMembers;
	}

	public String getNewGroupMembers() {
		return newGroupMembers;
	}

	public void setNewGroupMembers(String newGroupMembers) {
		this.newGroupMembers = newGroupMembers;
	}

	public Object getCallback() {
		return callback;
	}

	public void setCallback(Object callback) {
		this.callback = callback;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGroupProfilePic() {
		return groupProfilePic;
	}

	public void setGroupProfilePic(String groupProfilePic) {
		this.groupProfilePic = groupProfilePic;
	}

	public String getRecentDate() {
		return recentDate;
	}

	public void setRecentDate(String recentDate) {
		this.recentDate = recentDate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Group Id=" + groupId + ", Group Name =" + groupName;
	}

	public String getGroupdescription() {
		return groupdescription;
	}

	public void setGroupdescription(String groupdescription) {
		this.groupdescription = groupdescription;
	}

	public String getGroupIcon() {
		return groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInviteMembers() {
		return inviteMembers;
	}

	public void setInviteMembers(String inviteMembers) {
		this.inviteMembers = inviteMembers;
	}

	public String getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}

	public String getAdminMember() {
		return adminMember;
	}

	public void setAdminMember(String adminMember) {
		this.adminMember = adminMember;
	}
}
