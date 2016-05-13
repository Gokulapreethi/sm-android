package org.lib.model;

public class UpdateInstructor {
	
	private String type;
	
	private String message;
	
	private String url;
	
	
	public void setType(String type)
	{
		this.type=type;
	}
	public String  getType()
	{
		return this.type;
	}
	
	public void setMessage(String msg)
	{
		this.message=msg;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	public String getUrl()
	{
		return this.url;
	}
     

}
