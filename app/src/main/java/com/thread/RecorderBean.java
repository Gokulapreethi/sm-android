package com.thread;

public class RecorderBean {

	private String remote_info;

	private int record_port;

	private int record_id;

	private String file_path;

	private boolean isRecordingStarted = false;

	private int call_id;

	public String getRemote_info() {
		return remote_info;
	}

	public void setRemote_info(String remote_info) {
		this.remote_info = remote_info;
	}

	public int getRecord_port() {
		return record_port;
	}

	public void setRecord_port(int record_port) {
		this.record_port = record_port;
	}

	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public void setRecordingStarted(boolean isstarted) {
		this.isRecordingStarted = isstarted;
	}

	public boolean isRecordingStarted() {
		return this.isRecordingStarted;
	}

	public void setCall_id(int callid) {
		this.call_id = callid;
	}

	public int getCall_id() {
		return this.call_id;
	}

}
