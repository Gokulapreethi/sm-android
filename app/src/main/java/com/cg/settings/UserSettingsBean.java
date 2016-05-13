package com.cg.settings;

import com.main.SettingsFragment;

public class UserSettingsBean {

	private String autoplay_service = "";

	private String sharetone_value1 = "";
	
	public static String sharetone = "";

	private String sharetone_service = "";

	private String locationsett_service = "";

	private String location_value1 = "";

	private String location_value2 = "";

	private String location_service = "";

	private String action_value1 = "";

	private String action_value2 = "";

	private String action_service = "";

	private String geo_remoteadd = "";

	private String ftp_address = "";

	public void setAutoplayService(String serv) {
		this.autoplay_service = serv;
	}

	public String getAutoPlayService() {
		return this.autoplay_service;
	}

	public void setShareToneValue1(String share_val) {
		this.sharetone_value1 = share_val;
		this.sharetone = SettingsFragment.tonename;

	}

	public String getShareToveValue1() {
		return this.sharetone_value1;
	}

	public void setSharetoneService(String serv) {
		this.sharetone_service = serv;
	}

	public String getShareToneSevice() {
		return this.sharetone_service;
	}

	public void setLocationservEnabled(String val) {
		this.locationsett_service = val;
	}

	public String getLocationserviceEnabled() {
		return this.locationsett_service;
	}

	public void setLcationValue1(String val1) {
		this.location_value1 = val1;
	}

	public String getLocationValue1() {
		return this.location_value1;
	}

	public void setLocationValue2(String val2) {
		this.location_value2 = val2;
	}

	public String getLocationValue2() {
		return this.location_value2;
	}

	public void setLocationService(String loc_serv) {
		this.location_service = loc_serv;
	}

	public String getLocationService() {
		return this.location_service;
	}

	public void setActionValue1(String action1) {
		this.action_value1 = action1;
	}

	public String getAction1() {
		return this.action_value1;
	}

	public void setActionvalue2(String action2) {
		this.action_value2 = action2;
	}

	public String getactionValue2() {
		return this.action_value2;
	}

	public void setActionServices(String action_serv) {
		this.action_service = action_serv;
	}

	public String getActionSevices() {
		return this.action_service;
	}

	public void setgeoRemoteAddree(String add) {
		this.geo_remoteadd = add;
	}

	public String getRemoteaddress() {
		return this.geo_remoteadd;
	}

	public void setFtpAddress(String ftpadd) {
		this.ftp_address = ftpadd;
	}

	public String getFtpAddress() {
		return this.ftp_address;
	}

}
