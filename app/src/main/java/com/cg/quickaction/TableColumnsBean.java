package com.cg.quickaction;

import java.io.Serializable;

public class TableColumnsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	private String columnName;
	private String columnType;
}
