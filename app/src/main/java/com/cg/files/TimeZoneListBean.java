package com.cg.files;

import java.io.Serializable;

/**
 * Instance of the TimeZoneListBean is used to maintain the details about the
 * time zone
 * 
 * 
 * 
 */
public class TimeZoneListBean implements Serializable {

	private String strId;
	private String strName;
	private int offsetTime;

	/**
	 * To set the City name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.strName = name;
	}

	/**
	 * To set the Time Zone id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.strId = id;
	}

	/**
	 * To set the offset time
	 * 
	 * @param offset
	 */
	public void setOffSetTime(int offset) {
		this.offsetTime = offset;
	}

	/**
	 * To get the city name
	 * 
	 * @return
	 */
	public String getName() {
		return strName;
	}

	/**
	 * To get the time zone id
	 * 
	 * @return
	 */
	public String getId() {
		return strId;
	}

	/**
	 * To get the offset time
	 * 
	 * @return
	 */
	public int getOffsetTime() {
		return offsetTime;
	}

}
