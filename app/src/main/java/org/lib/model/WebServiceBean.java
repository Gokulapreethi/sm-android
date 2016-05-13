package org.lib.model;
/**
 * This bean class is used to  set and get datas related to WebService response. This class contains information 
 * like Result and Text.
 * 
 *
 */
public class WebServiceBean {
	 public String result = null;
     public String text = null;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
     
     
     

}
