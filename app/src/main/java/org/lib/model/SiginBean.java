package org.lib.model;

/**
 * This bean class is used to set and get data's Related to SignIn. This class contains information 
 * like UserName,Password,Local Address,ExternalIpAddress,Signaling Port,MAC address.
 * 
 *
 */
public class SiginBean {
	
	private String name=null;
	private String password=null;
	private String localladdress;
	private String externalipaddress=null;
	private String mac=null;
	private String Pstatus=null;
	private String signalingPort=null;
	private String dtype=null;
	private String dos=null;
	private String aver=null;
	private String latitude=null;
	private String longitude=null;
	private String avdate=null;
	
	private String devicetype;
	
	
	private Object callBack;
	
	
	
	
	
	public Object getCallBack() {
		return callBack;
	}
	public void setCallBack(Object callBack) {
		this.callBack = callBack;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getDos() {
		return dos;
	}
	public void setDos(String dos) {
		this.dos = dos;
	}
	public String getAver() {
		return aver;
	}
	public void setAver(String aver) {
		this.aver = aver;
	}
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLocalladdress() {
		return localladdress;
	}
	public void setLocalladdress(String localladdress) {
		this.localladdress = localladdress;
	}
	public String getExternalipaddress() {
		return externalipaddress;
	}
	public void setExternalipaddress(String externalipaddress) {
		this.externalipaddress = externalipaddress;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPstatus() {
		return Pstatus;
	}
	public void setPstatus(String pstatus) {
		Pstatus = pstatus;
	}
	public String getSignalingPort() {
		return signalingPort;
	}
	public void setSignalingPort(String signalingPort) {
		this.signalingPort = signalingPort;
	}
	
	public void setReleaseVersion(String dt)
	{
		this.avdate=dt;
	}
	
	public String getReleaseVersion()
	{
		return this.avdate;
	}
		
	public void setDeviceType(String type)
	{
		this.devicetype=type;
	}
	
	public String getdevcetype()
	{
		return this.devicetype;
	}

}
