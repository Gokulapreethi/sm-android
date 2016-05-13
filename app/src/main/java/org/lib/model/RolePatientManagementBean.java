package org.lib.model;

import java.io.Serializable;

/**
 * Created by preethi on 03-Mar-16.
 */
public class RolePatientManagementBean implements Serializable {
    private String groupid;
    private String groupowner;
    private String groupmember;
    private String roleid;
    private String prole;
    private String add;
    private String modify;
    private String delete;
    private String discharge;
    private String role;

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

    public void setProle(String prole) {
        this.prole = prole;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setDischarge(String discharge) {
        this.discharge = discharge;
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

    public String getProle() {
        return prole;
    }

    public String getAdd() {
        return add;
    }

    public String getModify() {
        return modify;
    }

    public String getDelete() {
        return delete;
    }

    public String getDischarge() {
        return discharge;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
