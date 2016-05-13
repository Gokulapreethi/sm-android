package com.bean;

import java.io.Serializable;

public class GroupChatPermissionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String userId;
	private String groupId;
	private String groupOwner;
	private String groupMember;
	private String audioConference;
	private String videoConference;
	private String audioBroadcast;
	private String videoBroadcast;
	private String textMessage;
	private String audioMessage;
	private String videoMessage;
	private String photoMessage;
	private String privateMessage;
	private String replyBackMessage;
	private String scheduleMessage;
	private String deadLineMessage;
	private String withDrawn;
	private String chat;
	private String type;
	private Object callBack;
	
	public Object getCallBack() {
		return callBack;
	}

	public void setCallBack(Object callBack) {
		this.callBack = callBack;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}

	public String getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(String groupMember) {
		this.groupMember = groupMember;
	}

	public String getAudioConference() {
		return audioConference;
	}

	public void setAudioConference(String audioConference) {
		this.audioConference = audioConference;
	}

	public String getVideoConference() {
		return videoConference;
	}

	public void setVideoConference(String videoConference) {
		this.videoConference = videoConference;
	}

	public String getAudioBroadcast() {
		return audioBroadcast;
	}

	public void setAudioBroadcast(String audioBroadcast) {
		this.audioBroadcast = audioBroadcast;
	}

	public String getVideoBroadcast() {
		return videoBroadcast;
	}

	public void setVideoBroadcast(String videoBroadcast) {
		this.videoBroadcast = videoBroadcast;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public String getAudioMessage() {
		return audioMessage;
	}

	public void setAudioMessage(String audioMessage) {
		this.audioMessage = audioMessage;
	}

	public String getVideoMessage() {
		return videoMessage;
	}

	public void setVideoMessage(String videoMessage) {
		this.videoMessage = videoMessage;
	}

	public String getPhotoMessage() {
		return photoMessage;
	}

	public void setPhotoMessage(String photoMessage) {
		this.photoMessage = photoMessage;
	}

	public String getPrivateMessage() {
		return privateMessage;
	}

	public void setPrivateMessage(String privateMessage) {
		this.privateMessage = privateMessage;
	}

	public String getReplyBackMessage() {
		return replyBackMessage;
	}

	public void setReplyBackMessage(String replyBackMessage) {
		this.replyBackMessage = replyBackMessage;
	}

	public String getScheduleMessage() {
		return scheduleMessage;
	}

	public void setScheduleMessage(String scheduleMessage) {
		this.scheduleMessage = scheduleMessage;
	}

	public String getDeadLineMessage() {
		return deadLineMessage;
	}

	public void setDeadLineMessage(String deadLineMessage) {
		this.deadLineMessage = deadLineMessage;
	}

	public String getWithDrawn() {
		return withDrawn;
	}

	public void setWithDrawn(String withDrawn) {
		this.withDrawn = withDrawn;
	}
	

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
