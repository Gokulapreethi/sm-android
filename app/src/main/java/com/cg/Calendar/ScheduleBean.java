package com.cg.Calendar;

public class ScheduleBean {
    private String owner;
    private String title;
    private String startTime;
    private String endTime;
    private String sceduleDate;
    private String eventDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSceduleDate() {
        return sceduleDate;
    }

    public void setSceduleDate(String sceduleDate) {
        this.sceduleDate = sceduleDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String toString(){
        return "==" + owner+
                "==" + title+
                "==" + startTime+
                "==" + endTime +
                "==" + sceduleDate;
    }
}
