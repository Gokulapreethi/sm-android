package com.thread;

import com.thread.SipCommunicator.sip_operation_types;

public class CommunicationBean {
	
	private sip_operation_types operation_types;
	
	private int acc_id;
	
	private String user_name;
	
	private String password;
	
	private String realm;
	
	private String registrar;
	
	private int call_id;
	
	private String sip_proxy;
	
	private boolean mic_mute;
	
	private String tonumber;
	
	private String file_path;
	
	private String mode;

	private String sip_endpoint;

	private String record_path;

	private Object rec_oject;
	
	private int rec_port;

	private String xml;
	
	private Boolean flag;
	
	private String SendingStatusid;
	
	
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public void setAcc_id(int a_id)
	{
		this.acc_id=a_id;
	}
	
	public int getAcc_id()
	{
		return this.acc_id;
	}
	
	public void setUser_name(String name)
	{
		this.user_name=name;
	}
	
	public String getUsername()
	{
		return this.user_name;
	}
	
	public void setPassword(String pwd)
	{
		this.password=pwd;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setRealm(String rlm)
	{
		this.realm=rlm;
	}
	
	public String getRealm()
	{
		return this.realm;
	}
	
	public void setRegistrar(String reg)
	{
		this.registrar=reg;
	}
	
	public String getRegistrar()
	{
		return this.registrar;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}


	public void setOperationType(sip_operation_types type)
	{
		this.operation_types=type;
	}
	
	public sip_operation_types getoperationType()
	{
		return this.operation_types;
	}
	
	public void setCall_id(int cl_id)
	{
		this.call_id=cl_id;
	}
	
	public int getCall_id()
	{
		return this.call_id;
	}
	
	public void setsipProxy(String proxy)
	{
		this.sip_proxy=proxy;
	}
	
	public String getSipProxy()
	{
		return this.sip_proxy;
	}
	
	public void setFlag(boolean res)
	{
		this.mic_mute=res;
	}
	
	public boolean getFlag()
	{
		return this.mic_mute;
	}
	
	public void setTonumber(String to_no)
	{
		this.tonumber=to_no;
	}
	
	public String getTonumber()
	{
		return this.tonumber;
	}
	
	public String getMode() {
		return this.mode;
	}

	public void setSipEndpoint(String sipid) {
		this.sip_endpoint = sipid;
	}

	public String getSipEndpoint() {
		return this.sip_endpoint;
	}

	public void setFilePath(String path) {
		this.record_path = path;
	}

	public String getFilepath() {
		return this.record_path;
	}

	public void setRecordObject(Object obj) {
		this.rec_oject = obj;
	}

	public Object getRecordObject() {
		return this.rec_oject;
	}
	
	public void setRecordPort(int port)
	{
		this.rec_port=port;
	}
	
	public int getRecordPort()
	{
		return this.rec_port;
	}

	

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getSendingStatusid() {
		return SendingStatusid;
	}

	public void setSendingStatusid(String sendingStatusid) {
		SendingStatusid = sendingStatusid;
	}
}
