/**
 * 
 */
package com.ws;

import java.util.HashMap;

import org.lib.webservice.EnumWebServiceMethods;


public class WSBean {

	private HashMap<String, String> property_map;

	private String wsMethodName;

	private Object callBack;

	private EnumWebServiceMethods serviceMethods;

	private Object resultObject;
	
	public String[] getOption() {
		return option;
	}

	public void setOption(String[] option) {
		this.option = option;
	}

	private String[] option;

	public HashMap<String, String> getProperty_map() {
		return property_map;
	}

	public void setProperty_map(HashMap<String, String> property_map) {
		this.property_map = property_map;
	}

	public String getWsMethodName() {
		return wsMethodName;
	}

	public void setWsMethodName(String wsMethodName) {
		this.wsMethodName = wsMethodName;
	}

	public Object getCallBack() {
		return callBack;
	}

	public void setCallBack(Object callBack) {
		this.callBack = callBack;
	}

	public EnumWebServiceMethods getServiceMethods() {
		return serviceMethods;
	}

	public void setServiceMethods(EnumWebServiceMethods serviceMethods) {
		this.serviceMethods = serviceMethods;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

}
