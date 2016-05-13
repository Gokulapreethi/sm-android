package org.lib.model;

import java.util.ArrayList;

public class Syncutilitybean {

	private ArrayList<UtilityBean> missing_utility;

	private ArrayList<UtilityBean> updated_utility;

	private ArrayList<UtilityBean> deleted_utility;

	public ArrayList<UtilityBean> getMissing_utility() {
		return missing_utility;
	}

	public void setMissing_utility(ArrayList<UtilityBean> missing_utility) {
		this.missing_utility = missing_utility;
	}

	public ArrayList<UtilityBean> getUpdated_utility() {
		return updated_utility;
	}

	public void setUpdated_utility(ArrayList<UtilityBean> updated_utility) {
		this.updated_utility = updated_utility;
	}

	public ArrayList<UtilityBean> getDeleted_utility() {
		return deleted_utility;
	}

	public void setDeleted_utility(ArrayList<UtilityBean> deleted_utility) {
		this.deleted_utility = deleted_utility;
	}

}
