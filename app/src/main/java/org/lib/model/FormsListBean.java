package org.lib.model;

public class FormsListBean {
	
	String form_id;
	String form_name;
	String form_owner;
	String no_rows;
	String form_time;
	String ftpusername;
	String ftppassword;
	String containsattributes;
	String formicon;
	String formdescription;
	String mode;


	public void setForm_id(String id)
	{
		this.form_id=id;
	}
	public String getFormId()
	{
		return this.form_id;
	}
	public void setForm_name(String name)
	{
		this.form_name=name;
	}
	public String getForm_name()
	{
		return this.form_name;
	}
	
	public void setAttribute(String name)
	{
		this.containsattributes=name;
	}
	public String getAttribute()
	{
		return this.containsattributes;
	}
	
	
	
	public void setForm_ownser(String owner)
	{
		this.form_owner=owner;
	}
	public String getForm_owner()
	{
		return this.form_owner;
	}
	public void setnumberof_rows(String count)
	{
		this.no_rows=count;
	}
	public String getNoofRows()
	{
		return this.no_rows;
	}
	public void setForm_time(String time)
	{
		this.form_time=time;
	}
	public String getFormtime()
	{
		return this.form_time;
	}
	
	public void setftpusername(String ftpuser)
	{
		this.ftpusername=ftpuser;
	}
	public String getFtpuser()
	{
		return this.ftpusername;
	}
	public void setftppassword(String ftppass)
	{
		this.ftppassword=ftppass;
	}
	public String getFtppasssword()
	{
		return this.ftppassword;
	}
	public String getFormicon() {
		return formicon;
	}
	public void setFormicon(String formicon) {
		this.formicon = formicon;
	}
	public String getFormdescription() {
		return formdescription;
	}
	public void setFormdescription(String formdescription) {
		this.formdescription = formdescription;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}
