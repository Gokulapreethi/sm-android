package org.lib.model;


public class FileDetailsBean {

    private String filename;
    private String filetype;
    private String filecontent;
    private String servicetype;
    private String username;
    private String branchtype;
    private String tetxfiles;
    private String imagefiles;
    private String audiofiles;
    private String videofiles;
    private String otherfiles;
    private String totalsize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(String filecontent) {
        this.filecontent = filecontent;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getAudiofiles() {
        return audiofiles;
    }

    public void setAudiofiles(String audiofiles) {
        this.audiofiles = audiofiles;
    }

    public String getVideofiles() {
        return videofiles;
    }

    public void setVideofiles(String videofiles) {
        this.videofiles = videofiles;
    }

    public String getOtherfiles() {
        return otherfiles;
    }

    public void setOtherfiles(String otherfiles) {
        this.otherfiles = otherfiles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBranchtype() {
        return branchtype;
    }

    public void setBranchtype(String branchtype) {
        this.branchtype = branchtype;
    }

    public String getTetxfiles() {
        return tetxfiles;
    }

    public void setTetxfiles(String tetxfiles) {
        this.tetxfiles = tetxfiles;
    }

    public String getImagefiles() {
        return imagefiles;
    }

    public void setImagefiles(String imagefiles) {
        this.imagefiles = imagefiles;
    }

    public String getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(String totalsize) {
        this.totalsize = totalsize;
    }
}
