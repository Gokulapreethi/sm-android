package com.cg.forms;




public class SyncBean {

	private String name;
	private String access;
	private String syncs;
	private String query;

	private int fid;

	private boolean selected;

	public int getID() {
		return fid;
	}

	public void setId(int id) {
		this.fid = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String nam) {
		this.name = nam;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getaccess() {
		return access;
	}

	public void setAccess(String acc) {
		this.access = acc;
	}
	public String getsync() {
		return syncs;
	}
	public void setQuery(String sql) {
		this.query = sql;
	}
	public String getquery() {
		return query;
	}
	public void setsync(String sync) {
		this.syncs = sync;
	}
	

}
