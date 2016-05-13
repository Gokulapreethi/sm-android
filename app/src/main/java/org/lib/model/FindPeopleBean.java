package org.lib.model;
/**
 * This bean class is used to set and get data's Related to FindPeople(Search buddies). This class contains information 
 * like Name and EmailId.
 * 
 *
 */
public class FindPeopleBean {
    public String name=null;
    public String emailId = null;
    private String buddyType=null;
    private String id=null;
    private String owner=null;
	public boolean selected;
	private String role;
	private String photo=null;
	private String status=null;

    
    public String getBuddyType() {
		return buddyType;
	}
	public void setBuddyType(String buddyType) {
		this.buddyType = buddyType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
    

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
    
	/**
	 * To set the Selection status
	 * 
	 * @param selection
	 */
	public void setSelected(boolean selection) {
		this.selected = selection;
	}

	/**
	 * To get the selection status
	 * 
	 * @return
	 */
	public boolean getSelection() {
		return this.selected;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
