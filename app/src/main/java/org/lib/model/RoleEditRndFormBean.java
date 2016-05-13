package org.lib.model;

import java.io.Serializable;

import javax.crypto.SecretKey;

/**
 * Created by preethi on 03-Mar-16.
 */
public class RoleEditRndFormBean implements Serializable {
    private String groupid;
    private String groupowner;
    private String groupmember;
    private String roleid;
    private String role;
    private String erole;
    private String status;
    private String diagnosis;
    private String testandvitals;
    private String hospitalcourse;
    private String notes;
    private String consults;

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

    public void setErole(String erole) {
        this.erole = erole;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTestandvitals(String testandvitals) {
        this.testandvitals = testandvitals;
    }

    public void setHospitalcourse(String hospitalcourse) {
        this.hospitalcourse = hospitalcourse;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setConsults(String consults) {
        this.consults = consults;
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

    public String getErole() {
        return erole;
    }

    public String getStatus() {
        return status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTestandvitals() {
        return testandvitals;
    }

    public String getHospitalcourse() {
        return hospitalcourse;
    }

    public String getNotes() {
        return notes;
    }

    public String getConsults() {
        return consults;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
