package org.lib.model;

/**
 * Created by balamurugan on 2/28/2016.
 */
public class chattemplatebean {


    String templetid;
    String templetmessage;
    boolean selected=false;
    String editvalue;

    public String getEditvalue() {return editvalue;}

    public void setEditvalue(String editvalue) {this.editvalue = editvalue;}

    public String getTempletid() {
        return templetid;
    }

    public void setTempletid(String templetid) {
        this.templetid = templetid;
    }

    public String getTempletmessage() {
        return templetmessage;
    }

    public void setTempletmessage(String templetmessage) {
        this.templetmessage = templetmessage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
