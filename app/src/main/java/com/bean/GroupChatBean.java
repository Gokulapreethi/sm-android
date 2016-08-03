package com.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class GroupChatBean implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String mimetype;
	private String result;
	private String type;
	private String category;
	private String subCategory;
	private String sessionid;
	private String from;
	private String to;
	private String signalid;
	private String senttime;
	private String senttimez;
	private String message;
	private String mediaName;
	private String mediaType;
	private String mediaUrl;
	private String sid;
	private String groupId = "";
	private String ftpUsername = "";
	private String ftpPassword = "";
	private String username = "";
	private Bitmap imageBitmap;
	private String pSingnalId = "";
	private String privateMembers;
	private String reminderTime;
	private String parentId = "";
    private String reply = "";
    private String confirm = "";
	private String filetitle="";
	private String comment="";
	private boolean playing;
	private boolean isJoin;
	private String senderWithdraw;
	private String messagestatus;
	private String dateandtime;

	public String getDateandtime() {
		return dateandtime;
	}

	public void setDateandtime(String dateandtime) {
		this.dateandtime = dateandtime;
	}

	public String getMessagestatus() {
		return messagestatus;
	}

	public void setMessagestatus(String messagestatus) {
		this.messagestatus = messagestatus;
	}




	public boolean isforward() {
		return isforward;
	}

	public void setIsforward(boolean isforward) {
		this.isforward = isforward;
	}

	private boolean isforward;

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private boolean select=false;

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    private boolean forward=false;

    public String getWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(String withdrawn) {
        this.withdrawn = withdrawn;
    }

    private String withdrawn = "";

    public boolean isWithdraw() {
        return withdraw;
    }

    public void setWithdraw(boolean withdraw) {
        this.withdraw = withdraw;
    }

    private boolean withdraw = false;

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    private boolean typing = false;

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    private String sent = "";

    public String getUnview() {
        return unview;
    }

    public void setUnview(String unview) {
        this.unview = unview;
    }

    private String unview = "";

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    private String urgent = "";

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    private String replied = "";
	private int progress = 100;
	private int status;
	private int unreadStatus;
    private int thumb=0;

	public String getCstatus() {
		return cstatus;
	}

	public void setCstatus(String cstatus) {
		this.cstatus = cstatus;
	}

	private String cstatus;


	public String getReadstatustime() {
		return readstatustime;
	}

	public void setReadstatustime(String readstatustime) {
		this.readstatustime = readstatustime;
	}

	private String readstatustime;

	public String getDeliverstatustime() {
		return deliverstatustime;
	}

	public void setDeliverstatustime(String deliverstatustime) {
		this.deliverstatustime = deliverstatustime;
	}

	private String deliverstatustime;

	public String getSentstatustime() {
		return sentstatustime;
	}

	public void setSentstatustime(String sentstatustime) {
		this.sentstatustime = sentstatustime;
	}

	private String sentstatustime;

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	public String getpSingnalId() {
		return pSingnalId;
	}

	public void setpSingnalId(String pSingnalId) {
		this.pSingnalId = pSingnalId;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public int getUnreadStatus() {
		return unreadStatus;
	}

	public void setUnreadStatus(int unreadStatus) {
		this.unreadStatus = unreadStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSignalid() {
		return signalid;
	}

	public void setSignalid(String signalid) {
		this.signalid = signalid;
	}

	public String getSenttime() {
		return senttime;
	}

	public void setSenttime(String senttime) {
		this.senttime = senttime;
	}

	public String getSenttimez() {
		return senttimez;
	}

	public void setSenttimez(String senttimez) {
		this.senttimez = senttimez;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getPrivateMembers() {
		return privateMembers;
	}

	public void setPrivateMembers(String privateMembers) {
		this.privateMembers = privateMembers;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFiletitle() {
		return filetitle;
	}

	public void setFiletitle(String filetitle) {
		this.filetitle = filetitle;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public GroupChatBean clone() {
		// TODO Auto-generated method stub
		try {
			return (GroupChatBean) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

	public boolean isJoin() {
		return isJoin;
	}

	public void setIsJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}

	public String getSenderWithdraw() {
		return senderWithdraw;
	}

	public void setSenderWithdraw(String senderWithdraw) {
		this.senderWithdraw = senderWithdraw;
	}
}
