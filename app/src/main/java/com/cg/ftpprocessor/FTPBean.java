package com.cg.ftpprocessor;

import java.util.ArrayList;

import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.ShareReminder;

import com.cg.files.CompleteListBean;
import com.cg.quickaction.ContactLogicbean;

public class FTPBean {

	private String ftp_username;

	private String ftp_password;

	private String server_ip;

	private int server_port;

	private int operation_type;

	private String request_from;

	private String filename;

	private String file_path;

	private Object req_object;

	private ArrayList<String> datas;

	private String vanish_mode;

	private String vanish_value;

	private int compId;

	private String noteType;

	private String comment;

	private ArrayList<String> buddiesList;

	private ContactLogicbean beanObj;

	private boolean runNow;

	private boolean stream_enabled;

	private ArrayList<OfflineRequestConfigBean> defalut_lis = new ArrayList<OfflineRequestConfigBean>();

	private ArrayList<OfflineRequestConfigBean> buddy_list = new ArrayList<OfflineRequestConfigBean>();

	private String mode;

	private boolean showInNotificationBar = false;
	
	private int progress;
	
	private CompleteListBean cBean= new CompleteListBean();

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public ArrayList<String> getBuddiesList() {
		return buddiesList;
	}

	public void setBuddiesList(ArrayList<String> buddiesList) {
		this.buddiesList = buddiesList;
	}

	private ShareReminder share;

	public ShareReminder getShare() {
		return share;
	}

	public void setShare(ShareReminder share) {
		this.share = share;
	}

	/**
	 * @return the ftp_username
	 */
	public String getFtp_username() {
		return ftp_username;
	}

	/**
	 * @param ftp_username
	 *            the ftp_username to set
	 */
	public void setFtp_username(String ftp_username) {
		this.ftp_username = ftp_username;
	}

	/**
	 * @return the ftp_password
	 */
	public String getFtp_password() {
		return ftp_password;
	}

	/**
	 * @param ftp_password
	 *            the ftp_password to set
	 */
	public void setFtp_password(String ftp_password) {
		this.ftp_password = ftp_password;
	}

	/**
	 * @return the server_ip
	 */
	public String getServer_ip() {
		return server_ip;
	}

	/**
	 * @param server_ip
	 *            the server_ip to set
	 */
	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	/**
	 * @return the server_port
	 */
	public int getServer_port() {
		return server_port;
	}

	/**
	 * @param server_port
	 *            the server_port to set
	 */
	public void setServer_port(int server_port) {
		this.server_port = server_port;
	}

	/**
	 * @return the operation_type
	 */
	public int getOperation_type() {
		return operation_type;
	}

	/**
	 * @param operation_type
	 *            the operation_type to set
	 */
	public void setOperation_type(int operation_type) {
		this.operation_type = operation_type;
	}

	/**
	 * @return the request_from
	 */
	public String getRequest_from() {
		return request_from;
	}

	/**
	 * @param request_from
	 *            the request_from to set
	 */
	public void setRequest_from(String request_from) {
		this.request_from = request_from;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the file_path
	 */
	public String getFile_path() {
		return file_path;
	}

	/**
	 * @param file_path
	 *            the file_path to set
	 */
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	/**
	 * @return the req_object
	 */
	public Object getReq_object() {
		return req_object;
	}

	/**
	 * @param req_object
	 *            the req_object to set
	 */
	public void setReq_object(Object req_object) {
		this.req_object = req_object;
	}

	/**
	 * 
	 * @return vanish_mode
	 */
	public String getVanish_mode() {
		return vanish_mode;
	}

	/**
	 * 
	 * @param vanish_mode
	 *            vanish mode to set
	 */
	public void setVanish_mode(String vanish_mode) {
		this.vanish_mode = vanish_mode;
	}

	public String getVanish_value() {
		return vanish_value;
	}

	public void setVanish_value(String vanish_value) {
		this.vanish_value = vanish_value;
	}


	public CompleteListBean getcBean() {
		return cBean;
	}

	public void setcBean(CompleteListBean cBean) {
		this.cBean = cBean;
	}

	public ArrayList<String> getDatas() {
		return datas;
	}

	public void setDatas(ArrayList<String> datas) {
		this.datas = datas;
	}

	public ArrayList<OfflineRequestConfigBean> getDefalut_lis() {
		return defalut_lis;
	}

	public void setDefalut_lis(ArrayList<OfflineRequestConfigBean> defalut_lis) {
		this.defalut_lis = defalut_lis;
	}

	public ArrayList<OfflineRequestConfigBean> getBuddy_list() {
		return buddy_list;
	}

	public void setBuddy_list(ArrayList<OfflineRequestConfigBean> buddy_list) {
		this.buddy_list = buddy_list;
	}

	public boolean isStream_enabled() {
		return stream_enabled;
	}

	public void setStream_enabled(boolean stream_enabled) {
		this.stream_enabled = stream_enabled;
	}

	public ContactLogicbean getBeanObj() {
		return beanObj;
	}

	public void setBeanObj(ContactLogicbean beanObj) {
		this.beanObj = beanObj;
	}

	public int getCompId() {
		return compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRunNow() {
		return runNow;
	}

	public void setRunNow(boolean runNow) {
		this.runNow = runNow;
	}

	public boolean isShowInNotificationBar() {
		return showInNotificationBar;
	}

	public void setShowInNotificationBar(boolean showInNotificationBar) {
		this.showInNotificationBar = showInNotificationBar;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
