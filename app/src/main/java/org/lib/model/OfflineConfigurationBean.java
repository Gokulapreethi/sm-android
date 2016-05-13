package org.lib.model;

import java.util.ArrayList;

public class OfflineConfigurationBean {
	private String userId;
	ArrayList<OfflineConfigResponseBean> list;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ArrayList<OfflineConfigResponseBean> getList() {
		return list;
	}

	public void setList(ArrayList<OfflineConfigResponseBean> list) {
		this.list = list;
	}
}
