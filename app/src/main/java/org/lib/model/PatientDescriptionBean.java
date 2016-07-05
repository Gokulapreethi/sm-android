package org.lib.model;

public class PatientDescriptionBean {

    private String patientid="";
    private String reportid="";
    private String reportcreator="";
    private String currentstatus="";
    private String diagnosis="";
    private String medications="";
    private String testandvitals="";
    private String hospitalcourse="";
    private String consults="";
    private String reportmodifier;
    private String groupid;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private String Date;

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getReportid() {
        return reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getReportcreator() {
        return reportcreator;
    }

    public void setReportcreator(String reportcreator) {
        this.reportcreator = reportcreator;
    }

    public String getCurrentstatus() {
        return currentstatus;
    }

    public void setCurrentstatus(String currentstatus) {
        this.currentstatus = currentstatus;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getTestandvitals() {
        return testandvitals;
    }

    public void setTestandvitals(String testandvitals) {
        this.testandvitals = testandvitals;
    }

    public String getHospitalcourse() {
        return hospitalcourse;
    }

    public void setHospitalcourse(String hospitalcourse) {
        this.hospitalcourse = hospitalcourse;
    }

    public String getConsults() {
        return consults;
    }

    public void setConsults(String consults) {
        this.consults = consults;
    }

    public String getReportmodifier() {
        return reportmodifier;
    }

    public void setReportmodifier(String reportmodifier) {
        this.reportmodifier = reportmodifier;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
