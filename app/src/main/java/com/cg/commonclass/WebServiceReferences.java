package com.cg.commonclass;

import android.R.string;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cg.callservices.VideoThreadBean;
import com.cg.files.Components;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.MMBean;

import org.lib.model.BuddyInformationBean;
import org.lib.model.ShareReminder;
import org.lib.model.SignalingBean;
import org.lib.webservice.WebServiceClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * To maintain the static references which is used for our application call
 * service network related activity
 * 

 * 
 */
public class WebServiceReferences {

	public static HashMap<String, Context> contextTable = new HashMap<String, Context>();

	public static HashMap<String, Object> callDispatch = new HashMap<String, Object>();

//	public static HashMap<String, BuddyInformationBean> buddyList = new HashMap<String, BuddyInformationBean>();

//	public static HashMap<String, BuddyInformationBean> tempBuddyList = new HashMap<String, BuddyInformationBean>();

	public static HashMap<String, BuddyInformationBean> reqbuddyList = new HashMap<String, BuddyInformationBean>();

	// Video call related params
	public static int CAMERA_ID = 2;
	/**
	 * width of the video frame size
	 */
	public final static int VIDEO_WIDTH = 176;
//	public final static int VIDEO_WIDTH = 360;
	/**
	 * Height of the video frame
	 */
	public final static int VIDEO_HEIGHT = 144;
//	public final static int VIDEO_HEIGHT = 480;

	// For web services

	public static boolean running = false;

	public static WebServiceClient webServiceClient = null;

	// For Menu

	public final static int CONTACTS = 1;

	public final static int USERPROFILE = 2;

	public final static int UTILITY = 3;

	public final static int NOTES = 4;

	public final static int APPS = 5;

	public final static int CLONE = 6;

	public final static int SETTINGS = 7;

	public final static int QUICK_ACTION = 8;

	public final static int FORMS = 9;

	public final static int FEEDBACK = 10;

	public final static int EXCHANGES = 11;

	// For Common GUI

	public static boolean isRecordinginProgress = false;

	public static String setLength2(int data) {
		String strData = Integer.toString(data);
		if (strData.length() == 1) {
			strData = "0" + strData;
		}
		return strData;

	}

	// For Image Pinch
	//
	// public static boolean isImageRotated = false;
	//
	// public static String imgPath = null;

	// For pending share

	public static int pendingShare = 0;

	// For sharing

	public static ArrayList<ShareReminder> shareRemainderArray = new ArrayList<ShareReminder>();

	public static LinkedList<Object> reminderQueue = new LinkedList<Object>();

	public static HashMap<String, Object> reminderMap = new HashMap<String, Object>();

	public static boolean isReminderOpened = false;

	public static HashMap<String, Integer> missedcallCount = new HashMap<String, Integer>();

	public static Set<String> buddies_forms = new HashSet<String>();

	// For IM

	public static HashMap<String, Context> instantMessageScreen = new HashMap<String, Context>();

	public static HashMap<String, String> activeSession = new HashMap<String, String>();

	public static HashMap<String, ArrayList<SignalingBean>> Imcollection = new HashMap<String, ArrayList<SignalingBean>>();

	public static HashMap<String, LinearLayout> imViews = new HashMap<String, LinearLayout>();

	public static HashMap<string, string> sessionMaintanance = new HashMap<string, string>();

	public static String SelectedBuddy;

	public static String session;

	public static HashMap<String, MMBean> hsMPPBean = new HashMap<String, MMBean>();

	public static HashMap<String, ProgressBar> hsIMProgress = new HashMap<String, ProgressBar>();

	public static HashMap<String, ImageView> hsIMImageView = new HashMap<String, ImageView>();

	public static HashMap<Integer, Object> hsIMResendObj = new HashMap<Integer, Object>();

	// For BG notification

	public static boolean isnotified = false;

	public static int tempIMCount = 0;

	public static int Imcont = 0;

	// For Settings

	public static boolean isAutoplay = false;

	public static String globaldateformat = "";

	public static int type = 3;

	public static boolean isAutoPlay = false;

	public static LinkedList<Components> llAutoPlayContent = new LinkedList<Components>();


	public static LinkedHashMap<Integer, VideoThreadBean> videoSSRC_total = new LinkedHashMap<Integer, VideoThreadBean>();

	public static ArrayList<Integer> videoSSRC_total_list = new ArrayList<>();

	public static ArrayList<Integer> removed_videoSSRC_list = new ArrayList<>();

	public static String getNoteCreateTimeForFiles() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					CallDispatcher.dateFormat + " hh:mm:ss a");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}
	}
}
