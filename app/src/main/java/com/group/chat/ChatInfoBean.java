package com.group.chat;

/**
 * Created  on 15-04-2016.
 */
public class ChatInfoBean {
    String name;
    String date;
    String status;
    String sid;
    String sentstatustime;
    String deliverstatustime;

    public String getSentstatustime() {
        return sentstatustime;
    }

    public void setSentstatustime(String sentstatustime) {
        this.sentstatustime = sentstatustime;
    }

    public String getDeliverstatustime() {
        return deliverstatustime;
    }

    public void setDeliverstatustime(String deliverstatustime) {
        this.deliverstatustime = deliverstatustime;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

}
