package org.lib.model;

import java.io.Serializable;

public class ShareResponseBean implements Serializable {

	String id;
	String status;

	public void setShareId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShareId() {
		return this.id;
	}

	public String getStatus() {
		return this.status;
	}
}
