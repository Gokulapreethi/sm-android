package com.cg.forms;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.lib.model.ShareSendBean;

import android.app.AlertDialog;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cg.commonclass.CallDispatcher;
import com.cg.files.Components;

/**
 * To maintain the list of static variable which are used in the overall
 * application
 * 
 *
 * 
 */
public class ValueHandler {

	public static int tempIMCount = 0;
	public static boolean isImageRotated = false;
	public static int noSpinnerChangeCount = 0;
	public static boolean isRecordinginProgress = false;
	public static boolean isAutoPlay = false;
	public static LinkedList<Components> llAutoPlayContent = new LinkedList<Components>();
	public static HashMap<String, ShareSendBean> hsShareSendItems = new HashMap<String, ShareSendBean>();
	public static String imgPath = "";
	public static boolean isReminderOpened = false;	
	public static HashMap<String, ProgressBar> hsIMProgress = new HashMap<String, ProgressBar>();
	public static HashMap<String, ImageView> hsIMImageView = new HashMap<String, ImageView>();
	public static HashMap<Integer, Object> hsIMResendObj = new HashMap<Integer, Object>();
	public static String strTotData = "";
	public static boolean checkActivity = false;
	public static HashMap<Integer, String> hsActiveActivity = new HashMap<Integer, String>();
	public static HashMap<String, CallDispatcher> hsCallDispatcherObjs = new HashMap<String, CallDispatcher>();
	public static int buddyStatus = 0;
	public static CallDispatcher callObj = null;
	public static boolean isLogOn = false;
	public static AlertDialog callAlert;
	public static boolean isBuddyRequestComes = false;
	public static boolean isShareWindowOpened = false;

	/**
	 * This HashMap contains all the available GMT Times and its required ID
	 */
	public static HashMap<String, String> htTimeZone = new HashMap<String, String>();
	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";
	public static ArrayList<String> strALSearchItem = new ArrayList<String>();
//	public static ArrayList<Object> timeZoneList = prePareList();

	/**
	 * To get the current date
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * format the date string into the date format <b>dd-MMM-yy</b>
	 * 
	 * @param strDate
	 * @return
	 */
	public static String formatDate(String strDate) {
		try {
			Date curDate = new Date(strDate);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			return sdf.format(curDate);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Check the length of the integer if it is one make that length to two
	 * 
	 * @param no
	 * @return
	 */
	public static String setLength2(int data) {
		String strData = Integer.toString(data);
		if (strData.length() == 1) {
			strData = "0" + strData;
		}
		return strData;

	}

	/**
	 * To get the current date and time and return that as a string. Date format
	 * - <b>d-MMM-yy hh:mm:ss a</b>
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		return sdf.format(curDate);
	}

	/**
	 * To prepare the Timezone list
	 * 
	 * @return
	 */
//	private static ArrayList<Object> prePareList() {
//
//		if (timeZoneList != null) {
//			return timeZoneList;
//		} else {
//			String[] zoneIds = TimeZone.getAvailableIDs();
//			int tzcount = 0;
//
//			for (int i = 0; i < zoneIds.length; i++) {
//				TimeZone tz = TimeZone.getTimeZone(zoneIds[i]);
//				// String temp = tz.getDisplayName(false, TimeZone.SHORT);
//
//				if (tz.getID().matches(TIMEZONE_ID_PREFIXES)) {
//
//					int rawOffset = tz.getRawOffset();
//					int hour = rawOffset / (60 * 60 * 1000);
//					int min = Math.abs(rawOffset / (60 * 1000)) % 60;
//
//					String strHour = null;
//					if (hour >= 0) {
//						strHour = (hour < 10 ? "0" : "") + hour;
//					} else if (hour > -10) {
//						strHour = (hour < 0 && hour > -10 ? "-0" : "")
//								+ (-1 * hour);
//					} else {
//						strHour = "" + hour;
//					}
//
//					strHour = (hour >= 0 ? "+" : "") + strHour;
//
//					String strMin = (min < 10 ? "0" : "") + min;
//
//					String strKey = "GMT" + strHour + ":" + strMin;
//
//					if (!htTimeZone.containsKey(strKey)) {
//						tzcount++;
//						htTimeZone.put(strKey, tz.getID());
//						// System.out.println("XX" + tzcount + " Time Zone : "
//						// + strKey + "ID: " + tz.getID());
//					}
//				}
//
//			}
//
//			ValueComparator bvc = new ValueComparator(htTimeZone);
//
//			TreeMap<String, String> sorted_map = new TreeMap(bvc);
//
//			sorted_map.putAll(htTimeZone);
//
//			// Log.e("tz", "Size:" + sorted_map.size());
//
//			timeZoneList = new ArrayList<Object>();
//
//			Set set = sorted_map.entrySet();
//
//			Iterator i = set.iterator();
//
//			while (i.hasNext()) {
//
//				Map.Entry me = (Map.Entry) i.next();
//				// System.out.print(me.getKey() + ": ");
//
//				TimeZoneBean tzb = new TimeZoneBean();
//				tzb.setId((String) me.getValue());
//				tzb.setName((String) me.getKey());
//
//				tzb.initTZData();
//
//				ArrayList<String> alCN = tzb.getCityNameList();
//				ArrayList<String> alID = tzb.getIDList();
//				ArrayList<String> alSN = tzb.getShortNameList();
//
//				for (int id1 = 0; id1 < alCN.size(); id1++)
//					strALSearchItem.add(alCN.get(id1));
//
//				for (int id1 = 0; id1 < alID.size(); id1++)
//					strALSearchItem.add(alID.get(id1));
//
//				for (int id1 = 0; id1 < alSN.size(); id1++)
//					strALSearchItem.add(alSN.get(id1));
//
//				timeZoneList.add(tzb);
//
//			}
//			return timeZoneList;
//		}
//
//	}

	/**
	 * To get the difference between two date time format
	 * 
	 * Date format is <b>dd-MMM-yy hh:mm:ss a</b>
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static String diffBetweenTwoTimes(String time1, String time2) {

		DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		Date date1 = null;
		try {
			date1 = formatter.parse(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = formatter.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = date2.getTime() - date1.getTime();

		int seconds = (int) (diff / 1000) % 60;
		int minutes = (int) ((diff / (1000 * 60)) % 60);
		int hours = (int) ((diff / (1000 * 60 * 60)) % 24);

		return checkLength(hours) + ":" + checkLength(minutes) + ":"
				+ checkLength(seconds);

	}

	/**
	 * Check the length of the integer if it is one make that length to two
	 * 
	 * @param no
	 * @return
	 */
	private static String checkLength(int no) {
		String str = Integer.toString(no);
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * This method will convert <b>yyyy-MM-dd HH:mm</b> date to <b>dd-MMM-yy
	 * hh:mm a</b> <b>Ex:2011-1-19 00:48</b> date to <b>19-Jan-11 12:48 AM</b> <br>
	 * 
	 * @param strDate
	 * @return
	 */
	static String changeReminderFormat(String strDate) {

		// System.out.println(strDate);
		try {
			// create SimpleDateFormat object with source string date format
			SimpleDateFormat sdfSource = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");

			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat(
					"dd-MMM-yy hh:mm a");

			// parse the date into another format
			strDate = sdfDestination.format(date);

			// System.out.println("Converted date is : " + strDate);

		} catch (ParseException pe) {
			// System.out.println("Parse Exception : " + pe);
		} catch (NullPointerException e) {

		}
		return strDate;

	}

	public static String diffBetweenTwoTimes1(String time1, String time2) {

		// DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");

		// DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = formatter.parse(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = formatter.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = date2.getTime() - date1.getTime();

		int seconds = (int) (diff / 1000) % 60;
		int minutes = (int) ((diff / (1000 * 60)) % 60);
		int hours = (int) ((diff / (1000 * 60 * 60)) % 24);

		return checkLength(hours) + ":" + checkLength(minutes) + ":"
				+ checkLength(seconds);

	}
}
