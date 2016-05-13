package com.cg.forms;



public class accessSyncBean {

	private String name;
	private String access;
	private String syncs;
	private int fid;
	private String query=null;

	private boolean selected;
	private boolean rulebased;

	public int getID() {
		return fid;
	}

	public void setId(int id) {
		this.fid = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setsync(String sync) {
		this.syncs = sync;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isRulebased() {
		return rulebased;
	}

	public void setRulebased(boolean rulebased) {
		this.rulebased = rulebased;
	}
	

}