package org.lib.model;

import java.util.ArrayList;

public class Formsinfocontainer {

	private String form_id;

	private String form_name;

	private String form_fields;

	private ArrayList<FormRecordsbean> rec_list;

	/**
	 * @return the form_id
	 */
	public String getForm_id() {
		return form_id;
	}

	/**
	 * @param form_id
	 *            the form_id to set
	 */
	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	/**
	 * @return the form_name
	 */
	public String getForm_name() {
		return form_name;
	}

	/**
	 * @param form_name
	 *            the form_name to set
	 */
	public void setForm_name(String form_name) {
		this.form_name = form_name;
	}

	/**
	 * @return the form_fields
	 */
	public String getForm_fields() {
		return form_fields;
	}

	/**
	 * @param form_fields
	 *            the form_fields to set
	 */
	public void setForm_fields(String form_fields) {
		this.form_fields = form_fields;
	}

	/**
	 * @return the rec_list
	 */
	public ArrayList<FormRecordsbean> getRec_list() {
		return rec_list;
	}

	/**
	 * @param rec_list
	 *            the rec_list to set
	 */
	public void setRec_list(ArrayList<FormRecordsbean> rec_list) {
		this.rec_list = rec_list;
	}

}
