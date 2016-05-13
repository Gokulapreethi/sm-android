package com.cg.timer;

import java.io.Serializable;

/**
 * Instance of this bean is used to maintain the attribute of the Components
 * which are need for the reminder notification
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class ReminderDetail implements Serializable {

	String strType, strContent, strPath,label,conditon,type,vanishMode,vanishValue;
	
	int noComponentID,labelid,compId;

	public int getCompId() {
		return compId;
	}
	public void setCompId(int compId) {
		this.compId = compId;
	}
	/**
	 * To set the Component type
	 * 
	 * @param strRemType
	 */
	public void setRemType(String strRemType) {
		strType = strRemType;
	}
	public String getVanishMode() {
		return vanishMode;
	}

	public void setVanishMode(String vanishMode) {
		this.vanishMode = vanishMode;
	}

	public String getVanishValue() {
		return vanishValue;
	}

	public void setVanishValue(String vanishValue) {
		this.vanishValue = vanishValue;
	}
	/**
	 * To set the Reminder content
	 * 
	 * @param strRemContent
	 */
	public void setRemContent(String strRemContent) {
		strContent = strRemContent;
	}

	/**
	 * To set the reminder file path
	 * 
	 * @param strRemPath
	 */
	public void setRemPath(String strRemPath) {
		strPath = strRemPath;
	}

	/**
	 * To set the component ID
	 * 
	 * @param ComponentID
	 */
	public void setComponentID(int ComponentID) {
		noComponentID = ComponentID;
	}
	public void setLabelID(int Labelid) {
		labelid = Labelid;
	}
	/**
	 * To get the Note(Component) type
	 * 
	 * @return String - Reminder type
	 */
	public String getRemType() {
		return strType;
	}

	/**
	 * To get the reminder content
	 * 
	 * @return String - reminder content
	 */
	public String getRemContent() {
		return strContent;
	}

	/**
	 * To get the components file path
	 * 
	 * @return String - file path of the component
	 */
	public String getRemPath() {
		return strPath;
	}

	/**
	 * To get the component id
	 * 
	 * @return int component id
	 */
	public int getComponentID() {
		return noComponentID;
	}
	public int getLabelID() {
		return labelid;
	}
	
	
	public String getlabel() {
		return label;
	}
	public void setlabel(String Label) {
		label = Label;
	}
	
	public String gettype() {
		return type;
	}
	public void settype(String Type) {
		label = Type;
	}
	
}
