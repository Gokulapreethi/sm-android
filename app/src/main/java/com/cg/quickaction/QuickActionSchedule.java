package com.cg.quickaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QuickActionSchedule {

	private boolean manaul = false;
	private boolean repeat = false;
	
	private Calendar schedule = Calendar.getInstance();
	private Calendar currSchedule = null;

	public Calendar getCurrSchedule() {
		if (currSchedule == null)
			return schedule;
		return currSchedule;
	}

	public void setCurrSchedule(Calendar currSchedule) {

		this.currSchedule = currSchedule;
	}

	private int repeatSchedule = 0;

	private int repeatType = 0;

	public int getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(int repeatType) {
		this.repeat = true;
		this.repeatType = repeatType;
	}

	public void setRepeatTypeVal(int repeatTypeParam, int repeatScheduleParam) {
		this.repeat = true;
		this.repeatSchedule = repeatScheduleParam;
		this.repeatType = repeatTypeParam;
	}

	public int getRepeatSchedule() {
		return repeatSchedule;
	}

	public void setRepeatSchedule(int repeatSchedule) {
		this.repeat = true;
		this.repeatSchedule = repeatSchedule;
	}

	public Calendar getSchedule() {
		return schedule;
	}

	public void setSchedule(Calendar schedule) {
		this.schedule = schedule;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public boolean isManaul() {
		return manaul;
	}

	public void setManaul(boolean manaul) {
		this.manaul = manaul;
	}

	public void setSchedule(int year, int month, int date, int hourOfDay,
			int minute, int second) {
		schedule.set(year, month, date, hourOfDay, minute, second);
	}

	public void setRepeatSchedule(int year, int month, int date, int hourOfDay,
			int minute, int second) {
		if (year != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_YEAR);
			this.setRepeatSchedule(year);
		}

		if (month != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_MONTH);
			this.setRepeatSchedule(month);
		}

		if (date != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_DAY);
			this.setRepeatSchedule(date);
		}

		if (hourOfDay != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_HOUR);
			this.setRepeatSchedule(hourOfDay);
		}

		if (minute != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_MIN);
			this.setRepeatSchedule(minute);
		}

		if (second != 0) {
			this.setRepeat(true);
			this.setRepeatType(QuickActionType.REPEAT_SEC);
			this.setRepeatSchedule(second);
		}

	}

	public Calendar getNextSchedule() {

		switch (this.getRepeatType()) {
		case QuickActionType.REPEAT_YEAR:
			this.getCurrSchedule().add(Calendar.YEAR, this.getRepeatSchedule());
			break;

		case QuickActionType.REPEAT_MONTH:
			this.getCurrSchedule().add(Calendar.MONTH, this.getRepeatSchedule());
			break;

		case QuickActionType.REPEAT_DAY:
			this.getCurrSchedule().add(Calendar.DATE, this.getRepeatSchedule());
			break;

		case QuickActionType.REPEAT_HOUR:
			this.getCurrSchedule().add(Calendar.HOUR_OF_DAY, this.getRepeatSchedule());
			break;

		case QuickActionType.REPEAT_MIN:
			this.getCurrSchedule().add(Calendar.MINUTE, this.getRepeatSchedule());
			break;

		case QuickActionType.REPEAT_SEC:
			this.getCurrSchedule().add(Calendar.SECOND, this.getRepeatSchedule());
			break;

		}
		return this.getCurrSchedule();
	}

	public Calendar getNextSchedule(Calendar cal) {
		currSchedule = cal;
		
		
		return this.getNextSchedule();
	}

	public static void main(String[] arg) {
		System.out.println(Calendar.getInstance().get(Calendar.YEAR));
		System.out.println(Calendar.getInstance().get(Calendar.MONTH));
		System.out.println(Calendar.getInstance().get(Calendar.DATE));

		QuickActionSchedule sc = new QuickActionSchedule();
		sc.setSchedule(Calendar.getInstance());
		sc.setRepeatSchedule(0, 0, 0, 0, 0, 1);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Calendar c = sc.getNextSchedule();

		for (int i = 0; i < 10; i++) {
			c = sc.getNextSchedule(c);
			System.out.println(format1.format(c.getTime()));

		}
	}
	
	public static QuickActionSchedule getManualSchedule(){
		QuickActionSchedule schedule = new QuickActionSchedule();
		schedule.setManaul(true);
		return schedule;
	}
	
	public static QuickActionSchedule getScheduler(Calendar cal){
		QuickActionSchedule schedule = new QuickActionSchedule();
		schedule.setManaul(false);
		schedule.setSchedule(cal);
		return schedule;
	}
	
	public static QuickActionSchedule getScheduler(int year, int month, int date, int hourOfDay,
			int minute, int second){
		QuickActionSchedule schedule = new QuickActionSchedule();
		schedule.setManaul(false);
		schedule.setSchedule(year,month, date, hourOfDay, minute, second);
		return schedule;
	}

}
