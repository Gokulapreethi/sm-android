package com.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bean.GroupChatBean;
import com.bean.ProfileBean;
import com.cg.commonclass.Logger;
import com.cg.commonclass.SipNotificationListener;
import com.cg.hostedconf.AppReference;
import com.chat.ChatHistoryWriter;
import com.chat.ChatProcesser;
import com.group.chat.GroupChatHistoryWriter;
import com.group.chat.GroupChatProcesser;
import com.main.AppMainActivity;
import com.main.SettingsFragment;

import org.lib.model.BuddyInformationBean;
import org.lib.model.FileDetailsBean;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class SingleInstance {

	private static ChatProcesser chatProcesser;
	private static ChatHistoryWriter chatHistoryWriter;

	public static AppMainActivity mainContext;
	public static SettingsFragment settingContext;
	private static GroupChatProcesser groupChatProcesser;

	public static ConcurrentHashMap<String, Context> contextTable = new ConcurrentHashMap<String, Context>();
	public static ConcurrentHashMap<String, Vector<GroupChatBean>> chatHistory = new ConcurrentHashMap<String, Vector<GroupChatBean>>();

	public static String currentGroupId;
	public static ConcurrentHashMap<String, Vector<GroupChatBean>> groupChatHistory = new ConcurrentHashMap<String, Vector<GroupChatBean>>();
	private static GroupChatHistoryWriter groupChatHistoryWriter;
	public static boolean isMenuHide = false;
	public static boolean notePicker = false;
	public static HashMap<String, Integer> unreadCount = new HashMap<String, Integer>();
	public static HashMap<String, Integer> individualMsgUnreadCount = new HashMap<String, Integer>();
	public static HashMap<String, PendingIntent> scheduledMsg = new HashMap<String, PendingIntent>();
	public static HashMap<String, Integer> deadLineMsgCount = new HashMap<String, Integer>();
	public static HashMap<String, Vector<GroupChatBean>> deadListMsg = new HashMap<String, Vector<GroupChatBean>>();
	public static ConcurrentHashMap<String, Object> instanceTable = new ConcurrentHashMap<String, Object>();
	public static HashSet<Integer> notificationBarIDMap = new HashSet<Integer>();
	public static HashMap<String, Integer> formMultimediaRecords = new HashMap<String, Integer>();
	public static boolean manualLogout = false;
	private static Logger logger;
	public static boolean crashlyticsLog = true;
	public static boolean quickViewShow = false;
	public static boolean profileView = false;
	public static boolean nameSatatus = false;
	public static boolean audioComponent = false;
	public static boolean gridViewShow = false;
	public static boolean ContactSharng = false;
	public static boolean myOrder = false;
	public static boolean isbcontacts = false;
	public static boolean orderToastShow = false;
	public static String parentId = null;
	public static boolean isAudioPlaying=false;
	public static boolean showsecContacts = true;
	public static HashMap<String, String> callRecording = new HashMap<String, String>();
	public static boolean fromGroupChat = false;
	public static Vector<String> buddyRequestMessageList = new Vector<String>();
    public static boolean inCall=false;
	public static boolean Localefromdevice =  false;
	public static ProfileBean myAccountBean=new ProfileBean();
    public static ArrayList<BuddyInformationBean> searchedResult=new ArrayList<BuddyInformationBean>();
	public static FileDetailsBean fileDetailsBean=new FileDetailsBean();
	public static Vector<FileDetailsBean> fileDetails=new Vector<FileDetailsBean>();
//	public static HashMap<String,Object> current_open_activity_detail = new HashMap<String,Object>();
	public static HashMap<Context,HashMap<String,Object>> current_open_activity_detail = new HashMap<Context,HashMap<String,Object>>();
	public static String build_version;

	public static ChatProcesser getChatProcesser() {
		if (chatProcesser == null) {
			chatProcesser = new ChatProcesser();
			chatProcesser.setRunning(true);
			chatProcesser.start();
		}
		return chatProcesser;
	}

	public static ChatHistoryWriter getChatHistoryWriter() {
		if (chatHistoryWriter == null) {
			chatHistoryWriter = new ChatHistoryWriter();
			chatHistoryWriter.setRunning(true);
			chatHistoryWriter.start();
		}
		return chatHistoryWriter;
	}

	public static GroupChatProcesser getGroupChatProcesser() {
		if (groupChatProcesser == null) {
			groupChatProcesser = new GroupChatProcesser();
			groupChatProcesser.setRunning(true);
			groupChatProcesser.start();
		}
		return groupChatProcesser;
	}

	public static void setGroupChatProcesser(
			GroupChatProcesser groupChatProcesser) {
		SingleInstance.groupChatProcesser = groupChatProcesser;
	}

	public static GroupChatHistoryWriter getGroupChatHistoryWriter() {
		if (groupChatHistoryWriter == null) {
			groupChatHistoryWriter = new GroupChatHistoryWriter();
			groupChatHistoryWriter.setRunning(true);
			groupChatHistoryWriter.start();
		}
		return groupChatHistoryWriter;
	}

	public static void setAlarm(PendingIntent alarmIntent, GroupChatBean gcBean) {
		AlarmManager alarmMgr = (AlarmManager) SipNotificationListener
				.getCurrentContext().getSystemService(Context.ALARM_SERVICE);
		SimpleDateFormat outPutFormatter = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm aa");
		DateFormatSymbols dfSym = new DateFormatSymbols();
		dfSym.setAmPmStrings(new String[] { "am", "pm" });
		outPutFormatter.setDateFormatSymbols(dfSym);
		Date date = null;
		try {
			date = outPutFormatter.parse(gcBean.getReminderTime());
			alarmMgr.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();

		}
	}

	public static void printLog(String tag, String message, String type,
			Throwable e) {
		try {
			if (AppReference.isWriteInFile) {
				if (e == null) {
					getLogger().info(message);
				} else {
					getLogger().error(message, e);
				}
			} else {
				if (e == null) {
					if (type != null) {
						if (type.equalsIgnoreCase("DEBUG")) {
							Log.d(tag, message);
						} else if (type.equalsIgnoreCase("WARN")) {
							Log.w(tag, message);
						} else {
							Log.i(tag, message);
						}
					} else
						Log.i(tag, message);
				} else {
					e.printStackTrace();
				}
			}
		} catch (Exception e2) {
			if (e != null)
				e.printStackTrace();
			e2.printStackTrace();
		}
	}

	public static synchronized Logger getLogger() {

		if (logger == null) {
			try {
				String dir_path = Environment.getExternalStorageDirectory()
						+ "/COMMedia";
				File directory = new File(dir_path);
				if (!directory.exists())
					directory.mkdir();

				String log_path = dir_path + "/log.txt";
				File logFile = new File(log_path);
				if (!logFile.exists())
					logFile.createNewFile();

				String logfilepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/log.txt";
				logger = new Logger("File", logfilepath, true, true, true,
						true, true);
				AppReference.logger = logger;

				directory = null;
				dir_path = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return logger;
	}

}
