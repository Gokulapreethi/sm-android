package org.lib.model;

public class FormSettingBean {
	
	String fsid;
	String buddy;
	String permissionid;
	String syncid;
	String syncquery;
	String createddate;
	String modifieddate;
	String mode;
	String formsettingtime;
	String formid;
	String iscontainAttribute;
	String formname;


	/**
	 * @return the formid
	 */
	public String getFormid() {
		return formid;
	}
	/**
	 * @param formid the formid to set
	 */
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public void setFsid(String id)
	{
		this.fsid=id;
	}
	public String getFsId()
	{
		return this.fsid;
	}
	public void setBuddy(String name)
	{
		this.buddy=name;
	}
	public String getbuddy()
	{
		return this.buddy;
	}
	public void setPermissionid(String pid)
	{
		this.permissionid=pid;
	}
	public String getPermisionid()
	{
		return this.permissionid;
	}
	public void setsyncid(String sid)
	{
		this.syncid=sid;
	}
	public String getsyncid()
	{
		return this.syncid;
	}
	public void setSyncquery(String query)
	{
		this.syncquery=query;
	}
	public String getSyncquery()
	{
		return this.syncquery;
	}
	
	public void setcreateddate(String cddate)
	{
		this.createddate=cddate;
	}
	public String getcreateddate()
	{
		return this.createddate;
	}
	public void setmodifieddate(String mdate)
	{
		this.modifieddate=mdate;
	}
	public String getmodifieddate()
	{
		return this.modifieddate;
	}
	
	public void setmode(String md)
	{
		this.mode=md;
	}
	public String getmode()
	{
		return this.mode;
	}
	
	public void setformsetting(String frm)
	{
		this.formsettingtime=frm;
	}
	public String getformsetting()
	{
		return this.formsettingtime;
	}
	/**
	 * @return the iscontainAttribute
	 */
	public String getIscontainAttribute() {
		return iscontainAttribute;
	}
	/**
	 * @param iscontainAttribute the iscontainAttribute to set
	 */
	public void setIscontainAttribute(String iscontainAttribute) {
		this.iscontainAttribute = iscontainAttribute;
	}
	/**
	 * @return the formname
	 */
	public String getFormname() {
		return formname;
	}
	/**
	 * @param formname the formname to set
	 */
	public void setFormname(String formname) {
		this.formname = formname;
	}
	
}
