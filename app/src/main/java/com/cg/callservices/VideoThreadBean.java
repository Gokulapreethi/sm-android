package com.cg.callservices;

/**
 * Created by Amuthan on 26/04/2016.
 */
public class VideoThreadBean {

    private byte[] data;
    private int window;
    private int videoSssrc;
    private String member_name;
    private boolean videoDisabled;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public int getVideoSssrc() {
        return videoSssrc;
    }

    public void setVideoSssrc(int videoSssrc) {
        this.videoSssrc = videoSssrc;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public boolean isVideoDisabled() {
        return videoDisabled;
    }

    public void setVideoDisabled(boolean videoDisabled) {
        this.videoDisabled = videoDisabled;
    }
}
