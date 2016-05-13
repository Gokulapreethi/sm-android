package com.cg.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * Instance of this bean used to have the TimeZone details
 * 
 * 
 * 
 */
public class TimeZoneBean {

	private String strId;
	private String strName;

	private ArrayList<String> strALShort = new ArrayList<String>();
	private ArrayList<String> strALID = new ArrayList<String>();
	private ArrayList<String> strALCountry = new ArrayList<String>();

	/**
	 * To set the TimeZone name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.strName = name;
	}

	/**
	 * To set the timezone id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.strId = id;
	}

	public String getGMT() {
		return strName;
	}

	/**
	 * To get the list cities with the same time zone which is separated by
	 * comma
	 * 
	 * @return
	 */
	public String getNameList() {
		String str = "";

		TimeZone tz = TimeZone.getTimeZone(strId);
		String[] strArMatchIds = TimeZone.getAvailableIDs(tz.getRawOffset());

		for (int i = 0; i < strArMatchIds.length; i++) {
			TimeZone tzTemp = TimeZone.getTimeZone(strArMatchIds[i]);
			String strTemp = tzTemp.getID();		
			if (strTemp.contains("/")) {
				strTemp = strTemp.substring(strTemp.indexOf("/") + 1);
				str = str + strTemp + ",";
			} else {
				str = str + strTemp + ",";
			}
		}

		return str;
	}

	/**
	 * Initialize Time zone data
	 */
	public void initTZData() {
		String str = "";

		TimeZone tz = TimeZone.getTimeZone(strId);

		ArrayList<String> alCity = new ArrayList<String>();

		String[] strArMatchIds = TimeZone.getAvailableIDs(tz.getRawOffset());

		for (int i = 0; i < strArMatchIds.length; i++) {
			TimeZone tzTemp = TimeZone.getTimeZone(strArMatchIds[i]);
			strALShort.add(tzTemp.getDisplayName(false, TimeZone.SHORT));
			strALID.add(tzTemp.getID());
			String strTemp = tzTemp.getID();
			if (strTemp.contains("/")) {
				strTemp = strTemp.substring(strTemp.lastIndexOf("/") + 1);
				strALCountry.add(strTemp);
			}

		}
		Set hashSet = new HashSet(strALShort);
		String[] strAR = (String[]) hashSet.toArray(new String[hashSet.size()]);
		strALShort = new ArrayList<String>(Arrays.asList(strAR));

		hashSet = new HashSet(strALID);
		strAR = (String[]) hashSet.toArray(new String[hashSet.size()]);
		strALID = new ArrayList<String>(Arrays.asList(strAR));

		hashSet = new HashSet(strALCountry);
		strAR = (String[]) hashSet.toArray(new String[hashSet.size()]);
		strALCountry = new ArrayList<String>(Arrays.asList(strAR));

	}

	/**
	 * To get the short name of the city
	 * 
	 * @return
	 */
	public ArrayList<String> getShortNameList() {
		return strALShort;
	}

	/**
	 * To get the time zone city name list
	 * 
	 * @return
	 */
	public ArrayList<String> getCityNameList() {

		if (strALCountry == null)
			initTZData();
		return strALCountry;
	}

	/**
	 * To get the time zone id list
	 * 
	 * @return
	 */
	public ArrayList<String> getIDList() {
		return strALID;
	}

}
