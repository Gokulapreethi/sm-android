package org.lib.model;

import java.io.Serializable;

/**
 * Created by preethi on 02-Mar-16.
 */
public class RoleAccessBean implements Serializable{
    private String groupid;
    private String groupowner;
    private String groupmember;
    private String roleid;
    private String role;
    private String patientmanagement;
    private String taskmanagement;
    private String editroundingform;
    private String commentsview;

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setGroupowner(String groupowner) {
        this.groupowner = groupowner;
    }

    public void setGroupmember(String groupmember) {
        this.groupmember = groupmember;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPatientmanagement(String patientmanagement) {
        this.patientmanagement = patientmanagement;
    }

    public void setTaskmanagement(String taskmanagement) {
        this.taskmanagement = taskmanagement;
    }

    public void setEditroundingform(String editroundingform) {
        this.editroundingform = editroundingform;
    }

    public void setCommentsview(String commentsview) {
        this.commentsview = commentsview;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getGroupowner() {
        return groupowner;
    }

    public String getGroupmember() {
        return groupmember;
    }

    public String getRoleid() {
        return roleid;
    }

    public String getRole() {
        return role;
    }

    public String getPatientmanagement() {
        return patientmanagement;
    }

    public String getTaskmanagement() {
        return taskmanagement;
    }

    public String getEditroundingform() {
        return editroundingform;
    }

    public String getCommentsview() {
        return commentsview;
    }
}
