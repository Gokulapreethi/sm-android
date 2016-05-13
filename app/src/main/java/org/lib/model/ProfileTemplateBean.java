package org.lib.model;

public class ProfileTemplateBean {
	private String profileId;
	
	private String profileName;
	
	private String createdDate;
	
	private String modifiedDate;
	
	private boolean deleteProfile;
	

	public boolean isDeleteProfile() {
		return deleteProfile;
	}

	public void setDeleteProfile(boolean deleteProfile) {
		this.deleteProfile = deleteProfile;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
