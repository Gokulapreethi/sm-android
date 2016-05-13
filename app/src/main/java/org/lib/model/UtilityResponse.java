package org.lib.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UtilityResponse implements Serializable{

	private String utility_id;

	private String noofresults;

	private String posted_date;

	private String result;

	private String message;

	private String editedutility_id;

	private String deletedutility_id;

	private ArrayList<UtilityBean> result_list;
	
	private String productName;

	public String getUtility_id() {
		return utility_id;
	}

	public void setUtility_id(String utility_id) {
		this.utility_id = utility_id;
	}

	public String getNoofresults() {
		return noofresults;
	}

	public void setNoofresults(String noofresults) {
		this.noofresults = noofresults;
	}

	public String getPosted_date() {
		return posted_date;
	}

	public void setPosted_date(String posted_date) {
		this.posted_date = posted_date;
	}

	public ArrayList<UtilityBean> getResult_list() {
		return result_list;
	}

	public void setResult_list(ArrayList<UtilityBean> result_list) {
		this.result_list = result_list;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEditedutility_id() {
		return editedutility_id;
	}

	public void setEditedutility_id(String editedutility_id) {
		this.editedutility_id = editedutility_id;
	}

	public String getDeletedutility_id() {
		return deletedutility_id;
	}

	public void setDeletedutility_id(String deletedutility_id) {
		this.deletedutility_id = deletedutility_id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
