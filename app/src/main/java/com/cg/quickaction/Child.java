package com.cg.quickaction;

public class Child
{
	private String name;
	private String columnname;
	private String aggregation;
	private String columnType;
	private boolean checked;

	
	
	public boolean isChecked()
	{
		return checked;
	}
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getcolumname()
	{
		return columnname;
	}
	
	public void setcolumname(String text1)
	{
		this.columnname = text1;
	}
	
	public String getAggregation()
	{
		return aggregation;
	}
	
	public void setAggregation(String text2)
	{
		this.aggregation = text2;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
}
