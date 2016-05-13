package com.cg.quickaction;
import java.util.ArrayList;
import java.util.Arrays;
public abstract class QuickAction {
	
	private int id = 0;
	private String name = null;
	private String owner = null;
	private String title = null;
	private String desc = null;
	private ArrayList<Buddy> contacts = new ArrayList<Buddy>();
	private QuickActionSchedule scheduler = null;
	private int actionType = 0;
	private int actionSubType = 0;
	private TriggerQuickAction trigger = null;
	private int actionDBCode = 0;
	
	
	public int getActionDBCode() {
		
		return actionDBCode;
	}
	public void setActionDBCode(int actionDBCode) {
		this.actionDBCode = actionDBCode;
	}
	
	public boolean isAnyBuddies(){
		if (contacts.size()>0)
		
			return true;
		else
			return false;
	}
	
	public Buddy[] getContacts() {
		Buddy[] contactList = new Buddy[contacts.size()];
		contactList = contacts.toArray(contactList);
		return contactList;
	}
	
	public String[] getContactList(){
		String[] contactList = new String[contacts.size()];
		for (int i=0; i< contacts.size(); i++){
			contactList[i] = contacts.get(i).getUserID();
		}
		return contactList;
	}
	protected void setContacts(Buddy[] contacts) {
		this.contacts = new ArrayList<Buddy>(Arrays.asList(contacts)); 
	}
	protected void addContact(Buddy contact){
		for (int i =0; i < contacts.size(); i++){
			if (contacts.get(i).getUserID().equals(contact.getUserID())){
				return;
			}
		}
		this.contacts.add(contact);
	}
	protected void removeContact(Buddy contact){
		for (int i =0; i < contacts.size(); i++){
			if (contacts.get(i).getUserID().equals(contact.getUserID())){
				contacts.remove(i);
			}
		}
	}
	public void removeContact(String contact){
		this.removeContact(new Buddy(contact));
	}
	public void removeAllContacts(){
		contacts.clear();
	}
	public void addContact(String contact){
		Buddy buddy = new Buddy(contact);
		this.contacts.add(buddy);
	}
	public abstract String[] getSubTypes();
	public abstract String getSubTypeDesc();
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public QuickActionSchedule getScheduler() {
		if (this.scheduler == null){
			return QuickActionSchedule.getManualSchedule();
		}
		return scheduler;
	}
	public boolean isManual(){
		return this.getScheduler().isManaul();
	}
	public void setScheduler(QuickActionSchedule scheduler) {
		this.scheduler = scheduler;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getActionSubType() {
		return actionSubType;
	}
	public void setActionSubType(int actionSubType) {
		this.actionSubType = actionSubType;
	}
	public TriggerQuickAction getTrigger() {
		if (trigger == null)
			trigger = new TriggerQuickAction();
		return trigger;
	}
	public void setTrigger(TriggerQuickAction trigger) {
		this.trigger = trigger;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
