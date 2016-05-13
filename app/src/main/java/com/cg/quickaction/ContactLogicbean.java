package com.cg.quickaction;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ContactLogicbean implements Parcelable {

	private boolean isDirty = false;
	private boolean isNew = false;
	private boolean markDelete = false;
	private String user = "";
	private String status = "1";
	private int id = 0;
	private String schedule;
	private String buddyUser;
	private String modeTime;
	private String freqMode;
	private String ftpPath;
	private String editconditon;
	private String editlable;
	private String edittime;
	private String editToUser;
	private String runMode;
	private boolean isRunNow;
	private boolean isEdit;
	private boolean issave;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRunNow() {
		return isRunNow;
	}

	public void setRunNow(boolean isRunNow) {
		this.isRunNow = isRunNow;
	}

	public QuickAction getQuickAction() {

		QuickActionBuilder builder = QuickActionBuilder.getInstance(user);
		QuickAction actionObj = builder.getQuickAction(this);
		if (actionObj == null) {
			return null;
		}

		actionObj.setOwner(user);
		return actionObj;
	}

	public String getDesc() {
		return this.getQuickAction().getDesc();
	}

	public void setDesc(String desc) {
		this.getQuickAction().setDesc(desc);
	}

	public int describeContents() {
		return 0;
	}

	// write your object's data to the passed-in Parcel
	public void writeToParcel(Parcel out, int flags) {
		out.writeByte((byte) (isDirty ? 1 : 0));
		out.writeByte((byte) (isNew ? 1 : 0));
		out.writeByte((byte) (markDelete ? 1 : 0));
		out.writeString(user);
		out.writeInt(id);
		out.writeString(schedule);
		out.writeString(buddyUser);
		out.writeString(modeTime);
		out.writeString(freqMode);
		out.writeString(ftpPath);
		out.writeString(editlable);
		out.writeString(editconditon);
		out.writeString(edittime);
		out.writeString(editToUser);
		out.writeString(runMode);

	}

	public ContactLogicbean(Parcel in) {
		isDirty = (in.readByte() == 1 ? true : false);
		isNew = (in.readByte() == 1 ? true : false);
		markDelete = (in.readByte() == 1 ? true : false);
		user = in.readString();
		id = in.readInt();
		schedule = in.readString();
		buddyUser = in.readString();
		modeTime = in.readString();
		freqMode = in.readString();
		ftpPath = in.readString();
		editlable = in.readString();
		editconditon = in.readString();
		edittime = in.readString();
		editToUser = in.readString();
		runMode = in.readString();

	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<ContactLogicbean> CREATOR = new Parcelable.Creator<ContactLogicbean>() {
		public ContactLogicbean createFromParcel(Parcel in) {
			return new ContactLogicbean(in);
		}

		public ContactLogicbean[] newArray(int size) {
			return new ContactLogicbean[size];
		}
	};

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public ContactLogicbean() {

	}

	public ContactLogicbean(String action) {
		QuickActionBuilder.getQuickAction(action);
	}

	public ContactLogicbean(String User, String action) {
		this(action);
		user = User;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {

		if (getQuickAction() == null) {
			Log.e("quickaction", this.getClass().getName()
					+ " quick action is null");
			return "";
		}
		return getQuickAction().getName();
	}

	public void setLabel(String label) {
		this.setDirty(false);
		if (getQuickAction() == null)
			Log.e("banudebug", this.getClass().getName()
					+ " quick action is null");
		getQuickAction().setName(label);
	}

	public String getCondition() {

		if (getQuickAction().getTrigger() != null)
			return getQuickAction().getTrigger().getSQLQuery();
		else
			return "";

	}

	public void setCondition(String condition) {

		if (condition == null)
			return;
		try {
			if (condition.trim().length() > 0) {
				this.setDirty(false);
				getQuickAction().getTrigger().setSQLQuery(condition);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTimemode(int timemode) {
		this.setDirty(false);
		getQuickAction().getScheduler().setRepeatType(timemode);
	}

	public void setTimemode(String time) {
		this.setDirty(false);
		// Keep Day as default
		int repeatType = 3;
		try {
			repeatType = Integer.parseInt(time);
		} catch (NumberFormatException e) {
			// Keep Day as default
			repeatType = 3;
		}

		getQuickAction().getScheduler().setRepeatType(repeatType);
	}

	public String gettimeMode() {
		return getQuickAction().getScheduler().getRepeatType() + "";
	}

	public Calendar getTimeCalendar() {
		return getQuickAction().getScheduler().getSchedule();
	}

	public String getTime() {
		// DateFormat dateFormat = null;
		// String format = Settings.System.getString(
		// WebServiceReferences.contextTable.get("MAIN")
		// .getContentResolver(), Settings.System.DATE_FORMAT);
		// if (TextUtils.isEmpty(format)) {
		// dateFormat = android.text.format.DateFormat
		// .getDateFormat(SplashScreen.context);
		// } else {
		// dateFormat = new SimpleDateFormat(format);
		// }
		// String result = dateFormat.format(getQuickAction().getScheduler()
		// .getSchedule().getTime());
		//
		// return result;

		return getQuickAction().getScheduler().getSchedule().getTime()
				.toString();

	}

	public String getFrequncy() {
		return getQuickAction().getScheduler().getRepeatSchedule() + "";

	}

	public void setFrequncy(String freq) {
		this.setDirty(false);
		int repeatType = 1;
		try {
			repeatType = Integer.parseInt(freq);
		} catch (NumberFormatException e) {
			// Keep Day as default
			repeatType = 1;
		}

		switch (getQuickAction().getScheduler().getRepeatType()) {
		case 0: // year
			getQuickAction().getScheduler().setRepeatSchedule(repeatType, 0, 0,
					0, 0, 0);
			break;
		case 1: // month
			getQuickAction().getScheduler().setRepeatSchedule(0, repeatType, 0,
					0, 0, 0);
			break;
		case 2: // week
			getQuickAction().getScheduler().setRepeatSchedule(0, 0,
					repeatType * 7, 0, 0, 0);
			break;
		case 3: // day
			getQuickAction().getScheduler().setRepeatSchedule(0, 0, repeatType,
					0, 0, 0);
			break;
		case 4: // hour
			getQuickAction().getScheduler().setRepeatSchedule(0, 0, 0,
					repeatType, 0, 0);
			break;
		case 5: // minute
			getQuickAction().getScheduler().setRepeatSchedule(0, 0, 0, 0,
					repeatType, 0);
			break;

		case 6: // second
			getQuickAction().getScheduler().setRepeatSchedule(0, 0, 0, 0,
					repeatType, 0);
			break;

		}

	}

	public boolean isAnyBuddies() {
		return getQuickAction().isAnyBuddies();
	}

	public String getAction() {
		return QuickActionType.QuickActionAllTypesCode[getQuickAction()
				.getActionDBCode()];

	}

	public boolean isIssave() {
		return issave;
	}

	public void setIssave(boolean issave) {
		this.issave = issave;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public void setAction(String action) {
		int id = this.getId();
		if (id != 0) {

			QuickActionBuilder builder = QuickActionBuilder.getInstance(user);
			builder.setQuickAction(id,
					QuickActionBuilder.getQuickAction(action));

		}

		else {

		}
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	public String getEditToUser() {
		return editToUser;
	}

	public void setEditToUser(String editToUser) {
		this.editToUser = editToUser;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getActionTitle() {

		return QuickActionType.QuickActionAllTypesText[this.getQuickAction()
				.getActionDBCode()];
	}

	public String[] getBuddies() {
		return getQuickAction().getContactList();
	}

	public void addBuddies(String buddy) {
		this.setDirty(false);
		getQuickAction().addContact(buddy);
	}

	public void setBuddies(String buddy) {
		this.setDirty(false);
		getQuickAction().addContact(buddy);
	}

	public String getModeTime() {
		return modeTime;
	}

	public void setModeTime(String modeTime) {
		this.modeTime = modeTime;
	}

	public String getTo() {
		return this.gettouser();
	}

	public void setTo(String to) {
		this.setTouser(to);
	}

	public void removeBuddy(String buddy) {
		this.setDirty(false);
		getQuickAction().removeContact(buddy);
	}

	public void removeAllBuddy() {
		this.setDirty(false);
		getQuickAction().removeAllContacts();
	}

	public String getfromuser() {
		return user;
	}

	public void setFromuser(String fromuser) {

		user = fromuser;
	}

	public String gettouser() {
		return this.getBuddies()[0];
	}

	public void setTouser(String touser) {
		this.setDirty(false);
		getQuickAction().addContact(touser);
	}

	// public String getftppath() {
	// try {
	// ShareQuickAction shareAction = (ShareQuickAction) getQuickAction();
	// return shareAction.getFTPPath();
	// } catch (ClassCastException e) {
	// return "";
	// }
	//
	// }
	//
	// public void setFtppath(String ftppath) {
	// try {
	//
	// ShareQuickAction shareAction = (ShareQuickAction) getQuickAction();
	// shareAction.setFTPPath(ftppath);
	// this.setDirty(false);
	// } catch (ClassCastException e) {
	//
	// }
	//
	// }

	public String getcontent() {
		try {
			ShareQuickAction shareAction = (ShareQuickAction) getQuickAction();
			return shareAction.getText();
		} catch (ClassCastException e) {
			return "";
		}

	}

	public void setcontent(String content) {
		try {
			ShareQuickAction shareAction = (ShareQuickAction) getQuickAction();
			shareAction.setText(content);
			this.setDirty(false);
		} catch (ClassCastException e) {

		}
	}

	public String gettype() {
		return QuickActionType.ACTIONTYPES[getQuickAction().getActionType()];
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	public boolean isMarkDelete() {
		return markDelete;
	}

	public void setMarkDelete(boolean markDelete) {
		this.markDelete = markDelete;
	}

	public String getFreqMode() {
		return freqMode;
	}

	public void setFreqMode(String freqMode) {
		this.freqMode = freqMode;
	}

	public String getBuddyUser() {
		return buddyUser;
	}

	public void setBuddyUser(String buddyUser) {
		this.buddyUser = buddyUser;
	}

	public String getEditlable() {
		return editlable;
	}

	public void setEditlable(String editlable) {
		this.editlable = editlable;
	}

	public String getEditconditon() {
		return editconditon;
	}

	public void setEditconditon(String editconditon) {
		this.editconditon = editconditon;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

}
