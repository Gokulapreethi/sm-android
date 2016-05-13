package org.lib.webservice;

import java.util.HashMap;

/**
 * This is a bean class used for set and get value related to Webservice.
 * 
 * 
 */
public class Servicebean {

	private EnumWebServiceMethods serviceMethods;

	private Object obj;

	private String[] option;

	private String key;

	private boolean isUpdateavailable = false;

	private Object updateObject;

	private HashMap<String, String> property_map;

	private String wsmethodname;

	private Object callBack;

	private Object extraDatas;

	public Object getCallBack() {
		return callBack;
	}

	public void setCallBack(Object callBack) {
		this.callBack = callBack;
	}

	public String[] getOption() {
		return option;
	}

	public void setOption(String[] option) {
		this.option = option;
	}

	public EnumWebServiceMethods getServiceMethods() {
		return serviceMethods;
	}

	public void setServiceMethods(EnumWebServiceMethods serviceMethods) {
		this.serviceMethods = serviceMethods;
	}

	public Object getObj() {
		return this.obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void setKey(String value) {
		this.key = value;
	}

	public String getKey() {
		return this.key;
	}

	public void setIsUpdateavailable(boolean flag) {
		this.isUpdateavailable = flag;
	}

	public boolean getIsupdateAvailable() {
		return this.isUpdateavailable;
	}

	public void setUpdateObject(Object obj) {
		this.updateObject = obj;
	}

	public Object getUpdateObject() {
		return this.updateObject;
	}

	

	/**
	 * @return the property_map
	 */
	public HashMap<String, String> getProperty_map() {
		return property_map;
	}

	/**
	 * @param property_map
	 *            the property_map to set
	 */
	public void setProperty_map(HashMap<String, String> property_map) {
		this.property_map = property_map;
	}

	/**
	 * @return the wsmethodname
	 */
	public String getWsmethodname() {
		return wsmethodname;
	}

	/**
	 * @param wsmethodname
	 *            the wsmethodname to set
	 */
	public void setWsmethodname(String wsmethodname) {
		this.wsmethodname = wsmethodname;
	}

	public Object getExtraDatas() {
		return extraDatas;
	}

	public void setExtraDatas(Object extraDatas) {
		this.extraDatas = extraDatas;
	}

}
