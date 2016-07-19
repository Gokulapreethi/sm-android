package org.lib.model;

import java.io.Serializable;

/**
 * Created by preethi on 01-Mar-16.
 */
public class TaskDetailsBean implements Serializable {
    private String groupid;
    private String taskId;
    private String creatorName;
    private String taskdesc;
    private String patientname;
    private String duedate;
    private String duetime;
    private String setreminder;
    private String timetoremind;
    private String assignedMembers;
    private String patientid;
    private String header;
    private String taskstatus;

    public String getHeadercode() {
        return headercode;
    }

    public void setHeadercode(String headercode) {
        this.headercode = headercode;
    }

    private String headercode;

    public String getCrtDuetime() {
        return crtDuetime;
    }

    public void setCrtDuetime(String crtDuetime) {
        this.crtDuetime = crtDuetime;
    }

    private String crtDuetime;
    public String getGroupid() {
        return groupid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getTaskdesc() {
        return taskdesc;
    }

    public String getDuedate() {
        return duedate;
    }

    public String getDuetime() {
        return duetime;
    }

    public String getTimetoremind() {
        return timetoremind;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public void setTaskdesc(String taskdesc) {
        this.taskdesc = taskdesc;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public void setDuetime(String duetime) {
        this.duetime = duetime;
    }

    public String getSetreminder() {
        return setreminder;
    }

    public void setSetreminder(String setreminder) {
        this.setreminder = setreminder;
    }

    public void setTimetoremind(String timetoremind) {
        this.timetoremind = timetoremind;
    }
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {

        this.taskId = taskId;
    }

    public String getAssignedMembers() {
        return assignedMembers;
    }

    public void setAssignedMembers(String assignedMembers) {
        this.assignedMembers = assignedMembers;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(String taskstatus) {
        this.taskstatus = taskstatus;
    }
}
