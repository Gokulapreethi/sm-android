package org.lib.model;

import java.util.HashMap;

public class FormRecordsbean {

	private HashMap<String, String> records_info;

	private boolean isDeleted_record = false;

	/**
	 * @return the records_info
	 */
	public HashMap<String, String> getRecords_info() {
		return records_info;
	}

	/**
	 * @param records_info
	 *            the records_info to set
	 */
	public void setRecords_info(HashMap<String, String> records_info) {
		this.records_info = records_info;
	}

	/**
	 * @return the isDeleted_record
	 */
	public boolean getIsDeleted_record() {
		return isDeleted_record;
	}

	/**
	 * @param isDeleted_record
	 *            the isDeleted_record to set
	 */
	public void setIsDeleted_record(Boolean isDeleted_record) {
		this.isDeleted_record = isDeleted_record;
	}

}
