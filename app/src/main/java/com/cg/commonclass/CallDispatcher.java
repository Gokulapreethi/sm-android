package com.cg.commonclass;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ConnectionBrokerServerBean;
import com.bean.FormFieldSettingDeleteBean;
import com.bean.GroupChatBean;
import com.bean.ProfileBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.account.ShareByProfile;
import com.cg.account.buddiesList;
import com.cg.account.buddyView1;
import com.cg.account.forgotPassword;
import com.cg.account.getnewpassword;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.avatar.AvatarActivity;
import com.cg.avatar.CloneActivity;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.AudioPagingSRWindow;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.inCommingCallAlert;
import com.cg.callservices.VideoCallScreen;
import com.cg.callservices.VideoPagingSRWindow;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.TestHTML5WebView;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.files.ComponentCreator;
import com.cg.files.Components;
import com.cg.files.sendershare;
import com.cg.forms.AccessAndSync;
import com.cg.forms.Alert;
import com.cg.forms.AppsView;
import com.cg.forms.FormDescription;
import com.cg.forms.FormPermissionViewer;
import com.cg.forms.FormRecordsCreators;
import com.cg.forms.FormSettings;
import com.cg.forms.FormViewer;
import com.cg.forms.InputsFields;
import com.cg.forms.QueryBuilder;
import com.cg.forms.SQLParser;
import com.cg.ftpprocessor.FTPBean;
import com.cg.ftpprocessor.FTPNotifier;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.ActionItem1;
import com.cg.instancemessage.BackgroundNotification;
import com.cg.instancemessage.IMNotifier;
import com.cg.instancemessage.QuickAction1;
import com.cg.locations.GPSLocationChanges;
import com.cg.permissions.PermissionsActivity;
import com.cg.profiles.BusCard;
import com.cg.profiles.ViewProfiles;
import com.cg.quickaction.ContactLogics;
import com.cg.quickaction.QuickActionSelectcalls;
import com.cg.rounding.NotificationReceiver;
import com.cg.services.PlayerService;
import com.cg.settings.MenuPage;
import com.cg.settings.UserSettingsBean;
import com.cg.snazmed.R;
import com.cg.timer.FileDownloader;
import com.cg.timer.ReminderDetail;
import com.cg.timer.ReminderService;
import com.cg.utilities.Blocked_list;
import com.cg.utilities.DistanceComparator;
import com.cg.utilities.UtilitiyActivity;
import com.cg.utilities.UtilityBuyer;
import com.cg.utilities.UtilitySeller;
import com.cg.utilities.UtilityServiceNeeder;
import com.cg.utilities.UtilityServiceProvider;
import com.chat.ChatActivity;
import com.chat.ChatBean;
import com.crypto.AESFileCrypto;
import com.exchanges.ExchangesActivity;
import com.google.android.maps.GeoPoint;
import com.group.GroupActivity;
import com.group.ViewGroups;
import com.group.chat.GroupChatActivity;
import com.im.xml.TextChatBean;
import com.im.xml.XMLComposer;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.AvatarFragment;
import com.main.ContactsFragment;
import com.main.CreateProfileFragment;
import com.main.ExchangesFragment;
import com.main.FilesFragment;
import com.main.FormsFragment;
import com.main.LoginPageFragment;
import com.main.QuickActionFragment;
import com.main.Registration;
import com.main.SettingsFragment;
import com.main.ViewProfileFragment;
import com.screensharing.ScreenSharingFragment;
import com.util.SingleInstance;

import org.audio.AudioProperties;
import org.audio.AudioRecorderStateListener;
import org.core.CallSessionListener;
import org.core.VideoCallback;
import org.lib.model.BuddyInformationBean;
import org.lib.model.CallHistoryBean;
import org.lib.model.ConnectionBrokerBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.FindPeopleBean;
import org.lib.model.FormRecordsbean;
import org.lib.model.FormSettingBean;
import org.lib.model.FormsBean;
import org.lib.model.FormsListBean;
import org.lib.model.Formsinfocontainer;
import org.lib.model.FtpDetails;
import org.lib.model.GroupBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PermissionBean;
import org.lib.model.ProfileTemplateBean;
import org.lib.model.RecordTransactionBean;
import org.lib.model.ShareReminder;
import org.lib.model.ShareSendBean;
import org.lib.model.SiginBean;
import org.lib.model.SignalingBean;
import org.lib.model.UdpMessageBean;
import org.lib.model.UtilityBean;
import org.lib.model.WebServiceBean;
import org.lib.webservice.EnumWebServiceMethods;
import org.lib.webservice.Servicebean;
import org.lib.webservice.WebServiceCallback;
import org.lib.webservice.WebServiceClient;
import org.net.rtp.RtpPacket;
import org.util.Queue;
import org.util.Utility;
import org.wifi.NetworkBroadcastReceiver;
import org.wifi.NetworkListener;
import org.wifi.WifiEngine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest1;

/**
 * This class is used to send and receive the signals which is controlled by the
 * Engines. For the every send and receive process it has to notify the
 * 
 * 
 * respective user interface screen
 * 
 * 
 * 
 */

public class CallDispatcher implements WebServiceCallback, CallSessionListener,
		VideoCallback, NetworkListener, AudioRecorderStateListener,
		SlideMenuInterface.OnSlideMenuItemClickListener {
	public enum PUBLIC_IP_STATE {
		PUBLIC_IP_IDLE, PUBLIC_IP_SUCCESS_LOGIN, PUBLIC_IP_KEEPALIVE_KEY_0, PUBLIC_IP_KEEPALIVE_KEY_1, PUBLIC_IP_KEEPALIVE_KEY_1_ON_KEY_1_RESPONSE, PUBLIC_IP_GET_AND_SET, PUBLIC_IP_KEEPALIVE_KEY_1_ON_NETWORKCHANGE, PUBLIC_IP_KEEPALIVE_KEY_1_ON_KEY_0_RESPONSE, PUBLIC_IP_SILENT_SIGNIN, DEFAULT
	};

	final private static String TAG = "CallDisp";

	private Context context;

	public static boolean isWifiClosed = false;

	public static boolean issecMadeConference = false;

	private String formname = null;

	public static Queue callQueue = new Queue();

	public String callToBeForward = null;

	public static int action = 0;

	public static int forward = 0;

	private SharedPreferences p;

	// private booleLoaded = false;

	// private boolean isFormsLoaded = false;

	public static int formMenu;

	public static double latitude = 0;

	public static double longitude = 0;

	public boolean isGenContent = false;

	public static String actionMode = null;

	public static String timeMode = "Once";

	public static String triggerQuery = "";

	public static double latLongValue = 0;

	public double latConfigure = 0;

	public double longConfigure = 0;

	public String location_Service = null;

	private String[] offlinedbnames = null;

	private String[] offlineFormDeletenames = null;

	// public static int withProfile = 1;

	public Button btn_respond = null;

	private String formnameoffline = null;

	private String offline_id = null;

	public int serviceType = 5;

	public String imagepath = null;

	public boolean isfirstLogin = false;

	public static String currentIMSession;

	private String currnetLat = null;

	private String currentLong = null;

	public PowerManager.WakeLock wl = null;

	private ArrayList<String> field = null;

	private ArrayList<String> field_list = null;

	public static boolean getValidation = false;

	public static ArrayList<String> buddiesConferenceList = new ArrayList<String>();

	public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	public boolean send_multiple = false;

	public boolean send_files_multiple = false;

	public static String buddyName = null;

	public static boolean clearAll = false;

	public  static String chatId;

	public static String checkMyLocation = null;

	public static HashMap<Integer, ArrayList<String>> uploadDatasList = new HashMap<Integer, ArrayList<String>>();

	public String url = "http://www.youtube.com/watch?v=6j_avKdWiEk";

	public static String LoginUser = null;
	/**
	 * Logged in user's password
	 */
	public static String Password = null;
	/**
	 * Reference to know the mute state
	 */
	public static boolean mute = false;

	/**
	 * By this instance we can make,accept,reject the call and etc..
	 */
	// public static CommunicationEngine commEngine = null;

	private static String localipaddsress;

	private static String publicipaddress;
	public static boolean isCallignored=false;

	private String cbserver1 = null;

	private String cbserver2 = null;

	private String freeswitch = null;

	private String FTPpassword = null;

	private String callinitiator = null;

	private String FTPUsername = null;

	private int cbPort1;

	private int cbPort2;

	private boolean isSilentLoginCalled = false;

	private AppMainActivity appMainActivity;

	/**
	 * used for Audio,Video Unicast.
	 */
	public static ArrayList<String> pagingMembers = new ArrayList<String>();
	/**
	 * used for Maintaining Audio,Video BroadCasting request members.
	 */
	public static ArrayList<String> BroadcastingRequestMembers = new ArrayList<String>();

	public static ArrayList<String> columnNames = new ArrayList<String>();

	public static ArrayList<String> columnTypes = new ArrayList<String>();

	public static String numberMode = "";

	/**
	 * Used for maintaining Broadcast senders Information.
	 */
	public static HashMap<String, SignalingBean> BroadCastSenders = new HashMap<String, SignalingBean>();

	private String relay = null;

	private String router = null;
	/**
	 * This will notice if the call accept reject alert is opened or not
	 */
	public static boolean isCallAcceptRejectOpened = false;

	public static String myStatus = "1";

	public static String onlineStatus = "Online";

	public static boolean reminderArrives;

	public WifiEngine wifiEngine;

	private SharedPreferences sharedPreferences = null;

	public String ipaddress = null;

	private HashMap<String, FindPeopleBean> findPeopleList = new HashMap<String, FindPeopleBean>();

	private Handler handlerForCall = null;

	private Handler testhandler = null;

	/**
	 * Used to store conference members on Call services.
	 */
	public static ArrayList<String> members = new ArrayList<String>();
	/**
	 * Adapter to show Buddies List on Listview.
	 */
	// public static ArrayList showBuddies = null;

	public static SignalingBean sb = null;

	// private MediaPlayer player = null;

	public static String currentSessionid = null;

	public static boolean dialChecker = false;

	public static boolean isIncomingCall = false;

	public static boolean isIncomingAlert = false;

	public static boolean openSenderScreen = false;

	public static boolean isAudioCallWindowOpened = false;

	public static boolean isAudioPlayed = false;

	public static boolean isFromCallDisp = false;
	/**
	 * used to store and retrive conference members.
	 */
	public static ArrayList<String> conferenceMembers = new ArrayList<String>();

	public static HashMap<String, SignalingBean> conferenceMember_Details = new HashMap<String, SignalingBean>();
	/**
	 * Check video call full screen state.
	 */
	public static boolean videoScreenVisibleState = false;
	/**
	 * Store Buddy information on Call services to send hangup signal.
	 */
	public static HashMap<String, SignalingBean> buddySignall = new HashMap<String, SignalingBean>();

	public static Handler audioCallHandler = new Handler();

	public static boolean isCallInProgress = false;

	public static boolean isfirstlaunch = false;

	private Handler netWorkStatusHandler;


	private WifiManager wifiManager = null;

	private static String ssid = null;

	public String strTempUser;

	public String strTempPassword;

	public static boolean isConnected = false;

	// public boolean isInternetEnabled = true;

	public String returnDetails[] = new String[3];

	public static String callType = null;

	public static Drawable d = null;

	private NetworkBroadcastReceiver broadcastReceiver = null;

	public PUBLIC_IP_STATE publicipstate = PUBLIC_IP_STATE.PUBLIC_IP_IDLE;

	public boolean isKeepAliveSendAfter6Sec = false;

	private DiscoveryInfo di = null;

	private AudioManager audioManager;

	private ArrayList<Object> settingsConfig = new ArrayList<Object>();

	private ArrayList<Object> formattConfig = new ArrayList<Object>();

	private String prevsignal = "";

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	public static ArrayList<String> conConference = new ArrayList<String>();

	public static HashMap<String, SignalingBean> contConferencemembers = new HashMap<String, SignalingBean>();

	public static int networkState = 2;

	public boolean isCalledAudiocallScreen = false;

	public static SignalingBean notify_sb = null;

	public boolean Sipcallisconnected = false;

	public static boolean GSMCallisAccepted = false;

	public boolean SIPCallisAccepted = false;

	public String sessId = null;

	public boolean isHangUpReceived = false;

	public ArrayList<String> alConferenceRequest = new ArrayList<String>();

	private ArrayList<String> field_values = null;

	public static ProgressDialog pdialog = null;

	public static ProgressDialog dialog = null;

	public static HashMap<String, String> conferenceMembersTime = new HashMap<String, String>();

	public static ArrayList<String[]> offlineinsert = new ArrayList<String[]>();

	public static ArrayList<String[]> offlineedit = new ArrayList<String[]>();

	public static ArrayList<String[]> offlinedelete = new ArrayList<String[]>();

	public ArrayList<String> blocked_buddies = new ArrayList<String>();

	public static boolean istoneEnabled = false;

	public HashMap<String, Bitmap> bitmap_table = new HashMap<String, Bitmap>();

	public BackgroundNotification notifier = null;
	private Handler mHandler;

	/**
	 * When the user make a call from the conference screen add that name as a
	 * key and SignalingBean as vale in to this HashMap.If the receiver accept
	 * or reject the call remove the buddy's reference from this HashMap. When
	 * the user going to hang up the call send the hang up signal to that
	 * buddies.
	 */
	public static HashMap<String, SignalingBean> hsAddedBuddyNameFromConferenceCall = new HashMap<String, SignalingBean>();

	public static HashMap<String, SignalingBean> conferenceRequest = new HashMap<String, SignalingBean>();

	public boolean isLocationServiceEnabled = false;

	public ArrayList<String> uploadData = null;

	public boolean loginLogoutStatus = false;

	public final static HashMap<String, String> message_map = new HashMap<String, String>();

	public static String dateFormat = "yyyy-MM-dd";

	private boolean call_heartbeat = false;

	// public static HashMap<Integer, String> multimedia_pathmap = new
	// HashMap<Integer, String>();

	// public static HashMap<Integer,String> uploading_map = new
	// HashMap<Integer,String>();

	// private ArrayList<String[]> profileDetails = new ArrayList<String[]>();

	public HashMap<String, String> multimediaFieldsValues = new HashMap<String, String>();

	public static boolean profileRequested = false;

	public static HashMap<Integer, PendingIntent> quickActionMap = new HashMap<Integer, PendingIntent>();

	// public ArrayList<Databean> mainbuddylist = new ArrayList<Databean>();
	//
	// public ArrayList<Databean> pendinglist = new ArrayList<Databean>();

	// public Vector<GroupBean> groupList = new Vector<GroupBean>();
	//
	// public Vector<GroupBean> ownerGroupList = new Vector<GroupBean>();
	//
	// public Vector<GroupBean> buddyGroupList = new Vector<GroupBean>();

	// public static CountryArrayAdapter adapterToShow;

	// public static GroupAdapter groupAdapter;

	// public static CountryArrayAdapter pendingToShow;

	private ProgressDialog progressDialog = null;
	private Handler noanswerhandler = new Handler();

	// public HeartbeatTimer HBT = null;

	public boolean isAnotherUserLogedIn = false;
	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

	private DBAccess dbhelper = null;

	private FTPNotifier ftpNotifier;

	private int noScrHeight;

	private int noScrWidth;

	public String profilePicturepath = "";

	private Timer download_timer;

	private FileDownloader fileDownloader;

	public static boolean isCallInitiate = false;

	public static AudioProperties audioProperties = null;

	public CompleteListBean cmp;

	private AlertDialog mdialog = null;

	public boolean isAudioPaused = true;

	public ArrayList<String> accepted_users = new ArrayList<String>();

	public ArrayList<CompleteListBean> multiple_componentlist = new ArrayList<CompleteListBean>();

	final MediaPlayer mPlayer = new MediaPlayer();
	int mPlayingPosition = 0;

	private Dialog history_dialog;

	public static ArrayList<InputsFields> inputFieldList = new ArrayList<InputsFields>();

	private UserSettingsBean settings = null;
	private MediaPlayer iplayer;

	private XMLComposer myXMLWriter = null;

	private IMNotifier imNotifier = null;

	public GPSLocationChanges gpsloc = null;

	public static boolean fromMultimediaUtils = false;

	public static boolean handsketch_edit = false;

	public ArrayList<String[]> attribute_list = new ArrayList<String[]>();

	public ArrayList<String> con = new ArrayList<String>();

	public ArrayList<String> contype = new ArrayList<String>();

	public ArrayList<String[]> rec_list = new ArrayList<String[]>();

	// Optimization block

	private static CallDispatcher callDispatcher;

	//

	public Runnable hb_runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				isKeepAliveSendAfter6Sec = true;
				KeepAliveBean aliveBean = getKeepAliveBean();
				aliveBean.setKey("1");

				if (LoginUser != null) {
					WebServiceReferences.webServiceClient.heartBeat(aliveBean);
					String[] user = { LoginUser, "", "" };
//					WebServiceReferences.webServiceClient.getFormTemplate(user,
//							context);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	public static CallDispatcher getCallDispatcher(Context context) {
		if (callDispatcher == null) {
			SingleInstance.printLog(TAG, "CallDispatcher is null. Going init",
					null, null);
			callDispatcher = new CallDispatcher(context);
		}
		return callDispatcher;
	}

	/**
	 * At the time of instance creation pass the context of the activity
	 * 
	 * @param contexts
	 *            - To which screen we are going to notify about the signals
	 */
	public CallDispatcher(final Context contexts) {
		this.context = SingleInstance.mainContext;
		appMainActivity = (AppMainActivity) SingleInstance.mainContext;
		handlerForCall = new Handler();
		netWorkStatusHandler = new Handler();
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		String url = preferences.getString("url", null);
		String port = preferences.getString("port", null);

		String settingslat = preferences.getString("latitude", null);
		String settingslong = preferences.getString("longitude", null);
		int x = 0;
		x = preferences.getInt("type", 5);

		if (settingslat != null && settingslong != null) {
			latConfigure = Double.parseDouble(settingslat);
			longConfigure = Double.parseDouble(settingslong);
			location_Service = preferences.getString("locservice", "");
			serviceType = x;
		}
		settingslat = null;
		settingslong = null;

		if (url == null) {

			Editor editor = preferences.edit();
			editor.putString("url",
					(String) context.getString(R.string.service_url));
			editor.putString("port", "80");
			editor.commit();

			changeSettings((String) context.getString(R.string.service_url),
					"80");
		} else
			changeSettings(url, port);

		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		broadcastReceiver = new NetworkBroadcastReceiver();
		broadcastReceiver.setNetworkListener(this);

		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

		context.getApplicationContext().registerReceiver(broadcastReceiver,
				intentFilter);
		NetworkBroadcastReceiver.isRegistered = true;
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if(wifiInfo.getSSID()!=null) {
			ssid = wifiInfo.getSSID();
		}
		uploadData = new ArrayList<String>();
		WebServiceReferences.callDispatch.put("calldispatcher", context);
		WebServiceReferences.callDispatch.put("calldisp", this);
		preferences = null;
		initCall();
		getdbHeler(context);
		gpsloc = new GPSLocationChanges(context, 1000, 9);
		gpsloc.Start();
		restoreState();

		try {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			SingleInstance.mainContext.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			noScrHeight = displaymetrics.heightPixels;
			noScrWidth = displaymetrics.widthPixels;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Bitmap setProfilePicture(String imageName, int resource_id) {
		try {
			Log.d("profile", "came to setProfilePicture " + imageName);
			if (imageName != null && imageName.trim().length() != 0) {
				if (bitmap_table.containsKey(imageName)) {
					return bitmap_table.get(imageName);
				} else {
					String path = containsLocalPath(imageName);
					File file = new File(path);
					if (file.exists()) {
						Bitmap bitmap = ResizeImage(path);
						if (bitmap != null) {
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							bitmap_table.put(imageName, bitmap);
							return bitmap;
						}
					}
				}
			}
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return BitmapFactory
				.decodeResource(context.getResources(), resource_id);
	}

	/*
	 * public Bitmap setProfilePicture(String imageName, int resource_id) {
	 * Bitmap bitmap = null; try { Log.d("profile", "came to setProfilePicture "
	 * + imageName); if (imageName != null && imageName.trim().length() != 0) {
	 * if (bitmap_table.containsKey(imageName)) { bitmap =
	 * bitmap_table.get(imageName); } else { String path =
	 * Environment.getExternalStorageDirectory() + "/COMMedia/" + imageName;
	 * File file = new File(path); if (!file.exists()) {
	 * 
	 * if (!bitmap_table.containsKey("default")) { bitmap =
	 * BitmapFactory.decodeResource( context.getResources(),
	 * R.drawable.icon_buddy_aoffline); bitmap_table.put("default", bitmap); }
	 * else bitmap = bitmap_table.get("default");
	 * 
	 * } else { bitmap = ResizeImage(path); if (bitmap != null) { bitmap =
	 * Bitmap.createScaledBitmap(bitmap, 200, 150, false);
	 * bitmap_table.put(imageName, bitmap); } else { if
	 * (!bitmap_table.containsKey("default")) { bitmap =
	 * BitmapFactory.decodeResource( context.getResources(),
	 * R.drawable.icon_buddy_aoffline); bitmap_table.put("default", bitmap); }
	 * else bitmap = bitmap_table.get("default"); }
	 * 
	 * }
	 * 
	 * } } else { if (!bitmap_table.containsKey("default")) { bitmap =
	 * BitmapFactory.decodeResource( context.getResources(),
	 * R.drawable.icon_buddy_aoffline); bitmap_table.put("default", bitmap); }
	 * else bitmap = bitmap_table.get("default"); }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return bitmap; }
	 */

	public void composeList(ArrayList<Slidebean> datas) {
		try {
			Log.i("calldisp", "===> inside composeList");

			Slidebean contacts = new Slidebean();
			contacts.setTitle("Contacts");
			contacts.setId(WebServiceReferences.CONTACTS);
			datas.add(contacts);

			Slidebean exchanges = new Slidebean();
			exchanges.setTitle("Exchanges");
			exchanges.setId(WebServiceReferences.EXCHANGES);
			datas.add(exchanges);

			if (!SingleInstance.isMenuHide) {
				Slidebean profile = new Slidebean();
				profile.setTitle(SingleInstance.mainContext.getResources().getString(R.string.my_profile));
				profile.setId(WebServiceReferences.USERPROFILE);
				datas.add(profile);

				Slidebean utility = new Slidebean();
				utility.setTitle("Utilities");
				utility.setId(WebServiceReferences.UTILITY);
				datas.add(utility);

				Slidebean apps = new Slidebean();
				apps.setTitle("Apps");
				apps.setId(WebServiceReferences.APPS);
				datas.add(apps);

				Slidebean clone = new Slidebean();
				clone.setTitle("Avatar");
				clone.setId(WebServiceReferences.CLONE);
				datas.add(clone);

				Slidebean qa = new Slidebean();
				qa.setTitle("Quick Action");
				qa.setId(WebServiceReferences.QUICK_ACTION);
				datas.add(qa);
			}
			Slidebean notes = new Slidebean();
			notes.setTitle("SNAZBOX FILES");
			notes.setId(WebServiceReferences.NOTES);
			datas.add(notes);

			Slidebean settings = new Slidebean();
			settings.setTitle("Settings");
			settings.setId(WebServiceReferences.SETTINGS);
			datas.add(settings);

			Slidebean forms = new Slidebean();
			forms.setTitle("Forms");
			forms.setId(WebServiceReferences.FORMS);
			datas.add(forms);

			Slidebean feedback = new Slidebean();
			feedback.setTitle(SingleInstance.mainContext.getResources().getString(R.string.feed_back)
);
			feedback.setId(WebServiceReferences.FEEDBACK);
			datas.add(feedback);

		} catch (Exception e) {
			Log.e("calldisp composeList", "===> " + e.getMessage());
			e.printStackTrace();
		}
	}

	public KeepAliveBean getKeepAliveBeanValidate() {
		KeepAliveBean aliveBean = new KeepAliveBean();
		try {
			aliveBean.setName(LoginUser);
			aliveBean.setPassword("password");
			aliveBean.setLocalipaddress(getLocalipaddsress());

			if (getPublicipaddress() == null) {
				setPublicipaddress(getPublicIpFromURL());
				String privateIp = wifiEngine.getLocalInetaddress();
				setLocalipaddsress(privateIp);
				aliveBean.setLocalipaddress(privateIp);
			}

			if (getPublicipaddress() == null) {
				aliveBean.setExternalipaddress(getLocalipaddsress());
			} else {
				aliveBean.setExternalipaddress(getPublicipaddress());
			}
			aliveBean.setStatus(myStatus);
			aliveBean.setaver(context.getResources().getString(
					R.string.app_version));
			aliveBean.setavdate(context.getResources().getString(
					R.string.app_date));
			aliveBean.setdtype("ANDROID");
			aliveBean.setLat(Double.toString(latitude));
			aliveBean.setLon(Double.toString(longitude));

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (AppMainActivity.commEngine != null) {
			AppMainActivity.commEngine
					.setPublicInetaddress(getPublicipaddress());
			AppMainActivity.commEngine
					.setLocalInetaddress(getLocalipaddsress());
		}
		return aliveBean;
	}

	/**
	 * If web service is running we have to stop the service and add that
	 * details into the preference then start the web service
	 * 
	 * @param strURL
	 *            - Web service connection url
	 * @param strPort
	 *            - port number
	 */
	private void changeSettings(String strURL, String strPort) {

		try {
			if (WebServiceReferences.running) {
				WebServiceReferences.webServiceClient.stop();
				WebServiceReferences.running = false;
				WebServiceReferences.webServiceClient = null;
				startWebService(strURL, strPort);
			} else
				startWebService(strURL, strPort);

		} catch (Exception e) {
			Log.d("settings", "catch" + e);

		}

	}

	/**
	 * To initialize the context of the respective ui to notify the Engine
	 * actions
	 * 
	 * @param contexts
	 */
	public void setContext(Context contexts) {
		this.context = contexts;
		initCall();
	}

	/**
	 * To initialize the global object which are used to maintain the signals
	 * and handle the signals
	 * 
	 */
	public void initCall() {

		try {
			// showBuddies = new ArrayList();
			// mainbuddylist = new ArrayList<Databean>();
			// pendinglist = new ArrayList<Databean>();

			// adapterToShow = new CountryArrayAdapter(context, R.layout.relate,
			// mainbuddylist);
			// pendingToShow = new CountryArrayAdapter(context, R.layout.relate,
			// pendinglist);

			wifiEngine = new WifiEngine(this.context);
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			ipaddress = sharedPreferences.getString("url", null);
			String loginIP = null;

			if (ipaddress != null) {
				loginIP = ipaddress.substring(ipaddress.indexOf("://") + 3);
				loginIP = loginIP.substring(0, loginIP.indexOf(":"));
				loginIP = loginIP.trim();
			}

			final String ServerIp = loginIP;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String privateIp = wifiEngine.getLocalInetaddress();
						setLocalipaddsress(privateIp);
						String publicIp = wifiEngine.getPublicInetaddress(
								ServerIp, 5000);
						if (publicIp == null) {
							if (ServerIp != null) {
								getPublicIpAgain(ServerIp);
							}
							publicIp = privateIp;
						}
						Log.d("PIP", "set from loginscreen call initcall");
						setPublicipaddress(publicIp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();
			testhandler = new Handler() {
				public void handleMessage(Message result) {
					try {
						super.handleMessage(result);
						Bundle bund = (Bundle) result.obj;
						String buddy = bund.getString("buddy");
						if (buddy.equals("hangupfullscreen")) {
							// getBuddyList();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Store the web service url and prot number in the preferences
	 * 
	 * @param url
	 *            - Web service connection url
	 * @param port
	 *            -port nummber
	 */
	public void startWebService(String url, String port) {
		try {
			Log.i("login123","inside startwebservice");

			intializeWebService(url, port);
		} catch (Exception e) {
			Log.e("calldisp", "startWebService :" + e.getMessage());
		}

	}

	/**
	 * To initialize the WebServiceClient object and start the web service
	 * client process
	 * 
	 * @param url
	 *            - web service connection url
	 * @param port
	 *            - port number
	 */

	public void intializeWebService(String url, String port1) {
		try {
			Log.i("login123","inside intializeWebService");

			if (url != null) {
				if (url.trim().length() != 0) {
					WebServiceReferences.webServiceClient = new WebServiceClient();

					WebServiceReferences.webServiceClient
							.setWebServiceCallback(this);

					WebServiceReferences.webServiceClient
							.setmNamespace("http://ltws.com/");
					WebServiceReferences.webServiceClient.setmWsdlURL(url
							.trim() + "?wsdl");
					String loginIP = url.substring(url.indexOf("://") + 3);

					loginIP = loginIP.substring(0, loginIP.indexOf(":"));

					loginIP = loginIP.trim();

					String urlPort = url.substring(url.indexOf("://") + 3);

					urlPort = urlPort.substring(urlPort.indexOf(":") + 1);

					urlPort = urlPort.substring(0, urlPort.indexOf("/"));
					WebServiceReferences.webServiceClient.start(loginIP,
							Integer.parseInt(urlPort));
					WebServiceReferences.running = true;

				}

			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Check Settings", Toast.LENGTH_SHORT);
		}

	}

	/**
	 * Here we are going to logout the user and clear the object which is have
	 * the details about the logged in user.
	 */

	// public void logout(boolean status) throws Exception {
	// try {
	//
	// Log.d("webservice", "Login status : " + CallDispatcher.LoginUser);
	//
	// if (CallDispatcher.LoginUser != null) {
	// String username = CallDispatcher.LoginUser;
	// CallDispatcher.LoginUser = null;
	// if (status) {
	// Log.d("Signin", "Inside if in logout");
	// WebServiceReferences.webServiceClient.siginout(username);
	// } else {
	// cancelDialog();
	// dissableUserSettings();
	// Log.e("Signin", "Inside else in logout");
	// if (gpsloc != null) {
	// gpsloc.Stop();
	// }
	// WebServiceReferences.missedcallCount.clear();
	// destroySIPStack();
	// profilePicturepath = "";
	// if (isIncomingCall) {
	// isIncomingCall = false;
	// }
	// if (ftpNotifier != null)
	// ftpNotifier.shutDowntaskmanager();
	// ftpNotifier = null;
	// cancelDownloadSchedule();
	// stopRingTone();
	// // if (WebServiceReferences.buddyList != null) {
	// // if (WebServiceReferences.buddyList.size() > 0) {
	// // WebServiceReferences.buddyList.clear();
	// // }
	// // }
	// // if (mainbuddylist != null)
	// // mainbuddylist.clear();
	// // adapterToShow.notifyDataSetChanged();
	// // if (pendinglist != null)
	// // pendinglist.clear();
	// Log.e("lgg", "going to clear tempbuddy list......");
	//
	// // if (WebServiceReferences.tempBuddyList != null) {
	// // if (WebServiceReferences.tempBuddyList.size() > 0) {
	// // WebServiceReferences.tempBuddyList.clear();
	// // }
	// // }
	// if (!isKeepAliveSendAfter6Sec) {
	// handlerForCall.removeCallbacks(hb_runnable);
	// isKeepAliveSendAfter6Sec = false;
	// }
	// WebServiceReferences.webServiceClient.stop();
	// WebServiceReferences.running = false;
	// if (AppMainActivity.commEngine != null) {
	// AppMainActivity.commEngine.stop();
	// }
	// AppMainActivity.commEngine = null;
	// LoginUser = null;
	// ipaddress = null;
	// // mainbuddylist.clear();
	// members.clear();
	// CallDispatcher.reminderArrives = false;
	// isKeepAliveSendAfter6Sec = false;
	// conferenceMembers.clear();
	// bitmap_table.clear();
	//
	// if (appMainActivity.HBT != null) {
	// appMainActivity.stopHeartBeatTimer();
	// }
	// // if (!WebServiceReferences.contextTable
	// // .containsKey("buddyView1")) {
	// // Intent logiIntent = new Intent(
	// // SipNotificationListener.getCurrentContext(),
	// // buddyView1.class);
	// // SipNotificationListener.getCurrentContext()
	// // .startActivity(logiIntent);
	// AppMainActivity appMainActivity = (AppMainActivity)
	// SingleInstance.contextTable
	// .get("MAIN");
	// LoginPageFragment loginPageFragment = LoginPageFragment
	// .newInstance(context);
	// FragmentManager fragmentManager = appMainActivity
	// .getSupportFragmentManager();
	// FragmentTransaction fragmentTransaction = fragmentManager
	// .beginTransaction();
	// fragmentTransaction.replace(
	// R.id.activity_main_content_fragment,
	// loginPageFragment, "loginfragment");
	// fragmentTransaction.commit();
	// // }
	// }
	//
	// if (WebServiceReferences.contextTable
	// .containsKey("formactivity")) {
	//
	// WebServiceReferences.contextTable.remove("formactivity");
	// }
	//
	// } else {
	//
	// Log.w("webservice", "Else LogOut Options");
	//
	// }
	//
	// } catch (Exception e) {
	//
	// Log.i("signout", e.getMessage());
	// }
	// }

	/**
	 * To get the public ip address of the login user.
	 * 
	 * @param serverIp
	 *            - ip address which is parsed from the web service connection
	 *            url
	 */
	private void getPublicIpAgain(String serverIp) {
		try {
			Log.d("PIP", "set from loginscreen call again");
			for (int i = 0; i < 5; i++) {
				String publicIp = wifiEngine.getPublicInetaddress(serverIp,
						getcbPort1());
				if (publicIp != null) {
					setPublicipaddress(publicIp);
					if (AppMainActivity.commEngine != null) {
						AppMainActivity.commEngine
								.setPublicInetaddress(publicIp);
					}
					break;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To sign in the buddy. At the time of sign in initialize the
	 * CommunicationEngine instance.
	 * 
	 * @param user
	 *            - user id of the Buddy
	 * @param password
	 *            - password of the Buddy
	 */
	// public void SignIn(final String user, final String password) {
	// try {
	// Log.d("PIP", "Befor call signin");
	// if (WebServiceReferences.running) {
	// isSilentLoginCalled = false;
	// Log.i("welcome", "calldispatcher  WebServiceReferences.running");
	// String text1 = user;
	// String text2 = password;
	//
	// strTempUser = user;
	// strTempPassword = password;
	//
	// commEngine = new CommunicationEngine(
	// EnumSignallingType.PROPRIETARY);
	// commEngine.setLocalInetaddress(getLocalipaddsress());
	// commEngine.setPublicInetaddress(getPublicipaddress());
	//
	// PackageManager pm = context.getPackageManager();
	// boolean hasFrontCamera = pm
	// .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
	// Log.d("CAM", "has front camera " + hasFrontCamera);
	// if (Integer.parseInt(Build.VERSION.SDK) > 8) {
	// WebServiceReferences.CAMERA_ID = 1;
	// if (!hasFrontCamera) {
	// WebServiceReferences.CAMERA_ID = 0;
	// }
	//
	// } else {
	//
	// WebServiceReferences.CAMERA_ID = 2;
	//
	// }
	// commEngine.setVideoParameters(WebServiceReferences.VIDEO_WIDTH,
	// WebServiceReferences.VIDEO_HEIGHT,
	// WebServiceReferences.CAMERA_ID);
	// commEngine.start(3, 3000);
	// commEngine.setCallSessionListener(CallDispatcher.this,
	// CallDispatcher.this);
	// SiginBean siginBean = new SiginBean();
	// siginBean.setName(text1);
	//
	// siginBean.setPassword(text2);
	// siginBean.setMac(getDeviceId());
	// siginBean.setLocalladdress(getLocalipaddsress());
	//
	// if (getPublicipaddress() == null) {
	// siginBean.setExternalipaddress(getLocalipaddsress());
	// } else {
	// siginBean.setExternalipaddress(getPublicipaddress());
	// }
	//
	// siginBean.setSignalingPort(Integer.toString(commEngine
	// .getSignalingPort()));
	// siginBean.setPstatus("1");
	// getMobileDetails();
	// siginBean.setDtype(returnDetails[0]);
	// siginBean.setDos(returnDetails[2]);
	//
	// siginBean.setLatitude(Double.toString(latitude));
	//
	// siginBean.setLongitude(Double.toString(longitude));
	//
	// siginBean.setAver(context.getResources().getString(
	// R.string.app_version));
	// siginBean.setReleaseVersion(context
	// .getString(R.string.app_date));
	// // siginBean.setReleaseVersion("2014-12-10 00:00:00");
	// siginBean.setDtype("Android");
	// siginBean.setDeviceType("ANDROID");
	// if (mainbuddylist != null)
	// mainbuddylist.clear();
	// WebServiceReferences.webServiceClient.sigin(siginBean);
	//
	// } else {
	// SharedPreferences preferences = PreferenceManager
	// .getDefaultSharedPreferences(context);
	// String ipaddress = preferences.getString("ipaddress", null);
	// String port = preferences.getString("port", null);
	// String namespace = preferences.getString("namespace", null);
	// if (ipaddress != null && port != null && namespace != null) {
	// } else {
	// handlerForCall.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// Toast.makeText(context, "Please check settings",
	// 3000).show();
	// }
	// });
	// }
	//
	// }
	// Log.d("PIP", "Befor call signin");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * create the thread which is used to start the heartBeat
	 */
	public void startKeepalive() {

	}

	/**
	 * To create and return the KeepAliveBean object for the login user.
	 * 
	 * @return KeepAliveBean
	 */
	public KeepAliveBean getKeepAliveBean() {
		KeepAliveBean aliveBean = new KeepAliveBean();
		try {
			aliveBean.setName(LoginUser);
			aliveBean.setPassword("password");
			aliveBean.setLocalipaddress(getLocalipaddsress());
			if (getPublicipaddress() == null) {
				aliveBean.setExternalipaddress(getLocalipaddsress());
			} else {
				aliveBean.setExternalipaddress(getPublicipaddress());
			}
			aliveBean.setStatus(myStatus);

			aliveBean.setaver(context.getResources().getString(
					R.string.app_version));
			aliveBean.setavdate(context.getResources().getString(
					R.string.app_date));
			aliveBean.setLat(Double.toString(latitude));
			aliveBean.setLon(Double.toString(longitude));
			aliveBean.setdtype("ANDROID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aliveBean;

	}

	/**
	 * To get the list of user who are all the logged in user's buddies with
	 * <b>online state and alphabetical order</b> and <b>off line state with
	 * alphabetical order</b>
	 */
	// public ArrayList<Databean> getBuddyList() {
	// try {
	// Set<String> set = WebServiceReferences.buddyList.keySet();
	//
	// String buddy = null;
	//
	// Iterator<String> iterator = set.iterator();
	//
	// ArrayList<Databean> buddies_list = new ArrayList<Databean>();
	//
	// ArrayList<Databean> tempList = new ArrayList<Databean>();
	//
	// String pendingnames = null;
	//
	// while (iterator.hasNext()) {
	// buddy = iterator.next();
	// String[] buddies = null;
	//
	// if (buddy.contains(",")) {
	// buddies = buddy.split(",");
	// pendingnames = buddy;
	// } else {
	// buddies = new String[1];
	// buddies[0] = buddy;
	// }
	// for (int i = 0; i < buddies.length; i++) {
	// BuddyInformationBean bib = WebServiceReferences.buddyList
	// .get(buddies[i]);
	// if (bib != null) {
	// if (bib.getName().equalsIgnoreCase(LoginUser)) {
	// myStatus = bib.getStatus();
	// changeMyOnlineStatus();
	// } else {
	// int idx = getDataBean(bib.getName());
	// Databean databean;
	// if (idx == -1) {
	// databean = new Databean();
	// databean.setname(buddy);
	// if (bib.getStatus().equals("0")) {
	// bib.setStatus("Offline");
	// } else if (bib.getStatus().equals("1")) {
	// bib.setStatus("Online");
	//
	// } else if (bib.getStatus().equals("2")) {
	//
	// bib.setStatus("Airport");
	// } else if (bib.getStatus().equals("3")) {
	// bib.setStatus("Away");
	//
	// } else if (bib.getStatus().equals("4")) {
	// bib.setStatus("Stealth");
	// }
	// databean.setStatus(bib.getStatus());
	// databean.setDistance(bib.getDistance());
	// bib.getStatus();
	// String message = message_map.get(buddy);
	// if (message != null)
	// databean.setInstantmessage(message);
	// else
	// databean.setInstantmessage("");
	// databean.setStatus(bib.getStatus());
	//
	// buddies_list.add(databean);
	// tempList.add(databean);
	// } else {
	// databean = (Databean) adapterToShow
	// .getItem(idx);
	// databean.setname(buddy);
	// if (!bib.getStatus()
	// .equalsIgnoreCase("pending")) {
	// if (bib.getStatus().equals("0")) {
	// bib.setStatus("Offline");
	//
	// } else if (bib.getStatus().equals("1")) {
	// bib.setStatus("Online");
	//
	// } else if (bib.getStatus().equals("2")) {
	//
	// bib.setStatus("Airport");
	// } else if (bib.getStatus().equals("3")) {
	// bib.setStatus("Away");
	//
	// } else if (bib.getStatus().equals("4")) {
	// bib.setStatus("Stealth");
	// }
	//
	// databean.setStatus(bib.getStatus());
	// databean.setDistance(bib.getDistance());
	// bib.getStatus();
	// String message = message_map.get(buddy);
	// if (message != null)
	// databean.setInstantmessage(message);
	// else
	// databean.setInstantmessage("");
	//
	// Log.d("sss", "Changed:" + buddy);
	// buddies_list.add(databean);
	// tempList.add(databean);
	//
	// } else {
	// databean.setStatus("pending");
	// for (Databean dBean : buddies_list) {
	// for (Databean dbean : tempList) {
	// if (!dBean.getname().equals(
	// dbean.getname())) {
	// if (!databean
	// .getname()
	// .equalsIgnoreCase(
	// CallDispatcher.LoginUser)) {
	// buddies_list.add(databean);
	// }
	// }
	// }
	// }
	//
	// }
	//
	// }
	//
	// }
	// }
	// }
	//
	// }
	// if (pendingnames != null)
	// WebServiceReferences.buddyList.remove(pendingnames);
	//
	// if (mainbuddylist == null)
	// mainbuddylist = new ArrayList<Databean>();
	//
	// mainbuddylist.clear();
	// mainbuddylist.addAll(buddies_list);
	// if (pendinglist != null) {
	// for (Databean databean2 : pendinglist) {
	//
	// Log.d("123456", "Name : " + databean2.getname());
	// if (!WebServiceReferences.buddyList.containsKey(databean2
	// .getname()))
	// mainbuddylist.add(databean2);
	// }
	// }
	//
	// buddies_list = null;
	// CallDispatcher.adapterToShow.notifyDataSetChanged();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // TODO: handle exception
	// } finally {
	// new SortBuddies().execute("");
	// }
	// return mainbuddylist;
	// }
	//
	// private class SortBuddies extends AsyncTask<String, String, String> {
	//
	// @Override
	// protected String doInBackground(String... params) {
	// try {
	// // TODO Auto-generated method stub
	// comparewithStateanddistance();
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// try {
	// // TODO Auto-generated method stub
	// getReqBuddy();
	// // adapterToShow.notifyDataSetChanged();
	// super.onPostExecute(result);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// }
	//
	// }

	// public void comparewithStateanddistance() {
	// try {
	//
	// ArrayList<Databean> online_users = new ArrayList<Databean>();
	// ArrayList<Databean> away_users = new ArrayList<Databean>();
	// ArrayList<Databean> airpot_users = new ArrayList<Databean>();
	// ArrayList<Databean> offline_usesrs = new ArrayList<Databean>();
	// ArrayList<Databean> pending_users = new ArrayList<Databean>();
	// ArrayList<Databean> main_list = new ArrayList<Databean>();
	// if (mainbuddylist != null) {
	// main_list.addAll(mainbuddylist);
	//
	// for (Databean databean : main_list) {
	// if (databean.getStatus() != null) {
	// String profilePic = getdbHeler(context).getProfilePic(
	// databean.getname());
	// if (databean.getProfile_picurepath() != null) {
	// if (!databean.getProfile_picurepath().equals(
	// profilePic)) {
	// if (bitmap_table.containsKey(databean
	// .getProfile_picurepath())) {
	// bitmap_table.remove(databean
	// .getProfile_picurepath());
	// }
	// }
	// }
	// if (WebServiceReferences.buddyList.containsKey(databean
	// .getname())) {
	// BuddyInformationBean bean = WebServiceReferences.buddyList
	// .get(databean.getname());
	// if (bean != null)
	// bean.setProfile_picpath(profilePic);
	// }
	// databean.setProfile_picurepath(profilePic);
	//
	// if (databean.getStatus().equalsIgnoreCase("Online"))
	// online_users.add(databean);
	// else if (databean.getStatus().equalsIgnoreCase(
	// "Airport"))
	// airpot_users.add(databean);
	// else if (databean.getStatus().equalsIgnoreCase("Away"))
	// away_users.add(databean);
	// else if (databean.getStatus().equalsIgnoreCase(
	// "pending"))
	// pending_users.add(databean);
	// else if (databean.getStatus().equalsIgnoreCase(
	// "Offline")
	// || databean.getStatus().equalsIgnoreCase(
	// "Stealth"))
	// offline_usesrs.add(databean);
	// }
	// }
	// }
	// main_list.clear();
	// if (online_users.size() > 0) {
	// Collections.sort(online_users, new BuddyComparator());
	// main_list.addAll(online_users);
	// }
	// if (away_users.size() > 0) {
	// Collections.sort(away_users, new BuddyComparator());
	// main_list.addAll(away_users);
	// }
	// if (airpot_users.size() > 0) {
	// Collections.sort(airpot_users, new BuddyComparator());
	// main_list.addAll(airpot_users);
	// }
	//
	// if (offline_usesrs.size() > 0) {
	// Collections.sort(offline_usesrs, new BuddyComparator());
	// main_list.addAll(offline_usesrs);
	// }
	// if (pending_users.size() > 0) {
	// Collections.sort(pending_users, new BuddyComparator());
	// main_list.addAll(pending_users);
	// if (pendinglist != null) {
	// pendinglist.clear();
	// pendinglist.addAll(pending_users);
	// }
	// }
	// online_users = null;
	// away_users = null;
	// airpot_users = null;
	// offline_usesrs = null;
	// pending_users = null;
	// if (mainbuddylist != null) {
	// mainbuddylist.clear();
	// mainbuddylist.addAll(main_list);
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// public void showPendingBuddies(final BuddyInformationBean buddies) {
	// if (pendinglist == null)
	// pendinglist = new ArrayList<Databean>();
	// pendinglist.clear();
	// handlerForCall.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// Log.d("lg", ">>>>>>>>>>>>>>>>>>> pendng methos called");
	// String[] pending = buddies.getName().split(",");
	// for (int i = 0; i < pending.length; i++) {
	// Databean databean = new Databean();
	// Log.d("lg", ">>>>>>>>>>>>>>>>>>> pendng methos called"
	// + buddies.getName());
	// databean.setname(pending[i]);
	// databean.setStatus(buddies.getStatus());
	// databean.setDistance(buddies.getDistance());
	// Log.d("stattus", buddies.getName());
	// Log.d("stattus", buddies.getName());
	// buddies.getStatus();
	// pendinglist.add(databean);
	// }
	//
	// }
	// });
	//
	// }

	/**
	 * Check the buddy name is exist in the adapter. If that's not available
	 * return -1 else return the last index value of the adapter
	 * 
	 * @param name
	 *            - Buddy name to be check in the adapter list
	 * @return if buddy is not available in the adapter return -1
	 */
	// public int getDataBean(String name) {
	//
	// Log.e("sss", "***************");
	// Log.e("sss", "adapterToShow Size:" + adapterToShow.getCount());
	// Log.e("sss", "***************");
	// int idx = -1;
	//
	// for (int i = 0; i < adapterToShow.getCount(); i++) {
	// if (idx == -1) {
	// Databean db = (Databean) adapterToShow.getItem(i);
	// if (db.getname().trim().equals(name.trim()))
	// idx = i;
	// }
	// }
	// return idx;
	// }

	// public void getReqBuddy() {
	//
	// try {
	// // System.out.println("requestbuddies1");
	// Set<String> set = WebServiceReferences.reqbuddyList.keySet();
	//
	// String buddy = null;
	//
	// Iterator<String> iterator = set.iterator();
	//
	// while (iterator.hasNext()) {
	//
	// buddy = iterator.next();
	//
	// BuddyInformationBean bib = WebServiceReferences.reqbuddyList
	// .get(buddy);
	//
	// if (!bib.getName().equals(LoginUser)) {
	//
	// Log.e("br", "Request Received!");
	//
	// try {
	// final String name = bib.getName().toString();
	// if (!showBuddies.contains(name)) {
	// if (!WebServiceReferences.buddyList
	// .containsKey(name)) {
	// showBuddies.add(name);
	//
	// ContactsFragment contactsFragment = ContactsFragment
	// .getInstance(SingleInstance.mainContext);
	// contactsFragment.updateRequestedBuddy();
	//
	// handlerForCall.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// Toast.makeText(
	// context,
	// name
	// + " sent buddy request to you",
	// Toast.LENGTH_LONG).show();
	// }
	// });
	//
	// }
	//
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// }

	/**
	 * This will return the array with the details about the <br>
	 * mobile {<br>
	 * "Model",<br>
	 * "API Level",<br>
	 * "sdk version"<br>
	 * 
	 * @return String[]
	 */
	public String[] getMobileDetails() {

		try {
			Log.d("DET", "on getdetails ");
			String model = Build.MODEL;
			int vers = Build.VERSION.SDK_INT;
			//
			String version = null;
			int ver = Build.VERSION.SDK_INT;
			switch (ver) {
			case 1:
				version = "1.0";
				break;
			case 2:
				version = "1.1";
				break;
			case 3:
				version = "1.5";
				break;
			case 4:
				version = "1.6";
				break;
			case 5:
				version = "2.0";
				break;
			case 6:
				version = "2.0.1";
				break;
			case 7:
				version = "2.1.x";
				break;
			case 8:
				version = "2.2.x";
				break;
			case 9:
				version = "2.3 - 2.3.2";
				break;
			case 10:
				version = "2.3.3 - 2.3.4";
				break;
			case 11:
				version = "3.0.x";
				break;
			case 12:
				version = "3.1.x";
				break;
			case 13:
				version = "3.2";
				break;
			case 14:
				version = "4.0 - 4.0.2";
				break;
			case 15:
				version = "4.0.3 - 4.0.4";
				break;
			case 16:
				version = "4.1";
				break;
			case 17:
				version = "4.2";
				break;
			case 18:
				version = "4.3";
				break;
			case 19:
				version = "4.4";
				break;
			case 20:
				version = "4.4W";
				break;
			case 21:
				version = "5.0";
				break;
			case 22:
				version = "5.1";
				break;
			case 23:
				version = "6.0";
				break;
			default:
				break;
			}

			Log.d("DET", "getdetails " + model + "," + Integer.toString(vers)
					+ "," + version);
			returnDetails[0] = model;
			returnDetails[1] = Integer.toString(vers);
			returnDetails[2] = version;
			//
			return returnDetails;
		} catch (Exception e) {
			returnDetails[0] = "Model   :";
			returnDetails[1] = "Api Level:";
			returnDetails[2] = "Version  :";
			return returnDetails;
			// TODO: handle exception
		}
	}

	/**
	 * To set the local ip address
	 * 
	 * @param localipaddsress
	 */
	public void setLocalipaddsress(String localipaddsress) {
		final String ip = localipaddsress;
		CallDispatcher.localipaddsress = localipaddsress;
	}

	/**
	 * To get the local ip address
	 * 
	 * @return
	 */
	public static String getLocalipaddsress() {
		return localipaddsress;
	}

	/**
	 * To set the public ip address
	 * 
	 * @param publicipaddress
	 */
	public void setPublicipaddress(String publicipaddress) {

		Log.d("PIP", "set PIP" + publicipaddress);
		CallDispatcher.publicipaddress = publicipaddress;
	}

	/**
	 * To get the public ip address
	 * 
	 * @return
	 */
	public static String getPublicipaddress() {
		return publicipaddress;
		// return null;
	}

	/**
	 * To set the cb server details
	 * 
	 * @param cbserver1
	 */
	public void setCbserver1(String cbserver1) {
		this.cbserver1 = cbserver1;
	}

	/**
	 * To get the cb server1 detail
	 * 
	 * @return
	 */
	public String getCbserver1() {
		return cbserver1;
	}

	/**
	 * To set cbserver2 detail
	 * 
	 * @param cbserver2
	 */
	public void setCbserver2(String cbserver2) {
		this.cbserver2 = cbserver2;
	}

	/**
	 * To get cbserver2 detail
	 * 
	 * @return
	 */
	public String getCbserver2() {
		return cbserver2;
	}

	/**
	 * To set router detail
	 * 
	 * @param router
	 */
	public void setRouter(String router) {
		this.router = router;
	}

	/**
	 * To get router detail
	 * 
	 * @return
	 */
	public String getRouter() {
		return router;
	}

	/**
	 * To set relay detail
	 * 
	 * @param relay
	 */
	public void setRelay(String relay) {
		this.relay = relay;
	}

	/**
	 * To get relay detail
	 * 
	 * @return
	 */
	public String getRelay() {
		return relay;
	}

	public void setcbPort1(int port1) {
		this.cbPort1 = port1;
	}

	public int getcbPort1() {
		return this.cbPort1;
	}

	public void setcbPort2(int port2) {
		this.cbPort2 = port2;
	}

	public int getcbPort2() {
		return this.cbPort2;
	}

	public void setFS(String f_switch) {
		this.freeswitch = f_switch;
	}

	public String getFS() {
		return this.freeswitch;
	}

	public String getFTPpassword() {
		return FTPpassword;
	}

	public void setFTPpassword(String fTPpassword) {
		FTPpassword = fTPpassword;
	}

	public String getFTPUsername() {
		return FTPUsername;
	}

	public void setFTPUsername(String fTPUsername) {
		FTPUsername = fTPUsername;
	}

	/**
	 * From the CallSessionListener we can get the call related update from the
	 * Engine. For example incoming, missed call, call accept response, etc..
	 */
	@Override
	public void notifyProprietyResponse(final Object obj) {
		if (obj instanceof UdpMessageBean)
			updateBuddyStatus((UdpMessageBean) obj);
		else if (obj instanceof SignalingBean) {
			try {

				final SignalingBean sb = (SignalingBean) obj;
				final String from = sb.getFrom();
				final String to = sb.getTo();
				Log.i("calltest",
						"signBean.getBs_parentid() :" + sb.getBs_parentid());

				if (sb.getType().equals("heartbeat")) {
					try {
						KeepAliveBean aliveBean = getKeepAliveBean();
						aliveBean.setKey("1");
						Log.i("KA123",
								"sendKeyOneMessage:  calling HB webservice");
						WebServiceReferences.webServiceClient
								.heartBeat(aliveBean);
						String[] user = { LoginUser, "", "" };
//						WebServiceReferences.webServiceClient.getFormTemplate(
//								user, context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile) {
							try {
								AppReference.logger.error(
										"KA123 calling hearbeat", e);

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}

				}

				if (sb.getType().equals("0")) {

					Log.i("AudioCall", " isCallInitiate :" + isCallInitiate + " GSMCallisAccepted :" + GSMCallisAccepted + " connection : " + SingleInstance.instanceTable
							.containsKey("connection"));
					Log.i("AudioCall", "	sb.getSessionid() : " + sb.getSessionid() + " currentSessionid : " + currentSessionid + " isCallignored : "+isCallignored);

					if(isCallignored) {
						rejectInComingCall(sb);
					} else {
						// Incoming call
						if (isCallInitiate
								|| GSMCallisAccepted
								|| SingleInstance.instanceTable
								.containsKey("connection")) {
							// rejectInComingCall(sb);
							if (currentSessionid != null
									&& currentSessionid.equals(sb.getSessionid())) {

								if (!alConferenceRequest.contains(from)) {
									alConferenceRequest.add(from);
								}
								sb.setFrom(to);
								sb.setTo(from);
								sb.setType("1");
								sb.setResult("0");
								AppMainActivity.commEngine.acceptCall(sb);

								String buddy = getUser(sb.getFrom(), sb.getTo());

								if (!conferenceMembers.contains(buddy)) {
									conferenceMembers.add(0, buddy);
									buddySignall.put(buddy, sb);
									// VideoCallScreen acalObj = (VideoCallScreen)
									// objCallScreen;
								}
							} else {
								rejectInComingCall(sb);
								handlerForCall.post(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(context,
												sb.getFrom() + " call rejected.", 3000)
												.show();
									}
								});

							}
						} else {
							final Object objCallScreen = SingleInstance.instanceTable
									.get("callscreen");
							if (objCallScreen == null) {
								Log.i("AudioCall", "callscreen object null");
								if (currentSessionid == null) {

									String old_sessionid = null;
									if (sb.getJoincall() != null && sb.getJoincall().equalsIgnoreCase("yes")) {
										rejectInComingCall(sb);
//									String query = "select * from recordtransactiondetails ORDER BY id DESC";
//									ArrayList<RecordTransactionBean> mlist = DBAccess.getdbHeler().getcallhistorydetails(query);
//									for (RecordTransactionBean transactionBean : mlist) {
//										if (transactionBean.getSessionid().equalsIgnoreCase(sb.getSessionid())) {
//											old_sessionid = transactionBean.getSessionid();
//											break;
//										}
//									}
									} else if (old_sessionid == null) {
										currentSessionid = sb.getSessionid();
										isCallInitiate = true;
										String buddy = getUser(sb.getFrom(), sb.getTo());

										if (!conferenceMembers.contains(sb.getFrom())) {
											CallDispatcher.conferenceMembers.add(sb
													.getFrom());
											buddySignall.put(buddy, sb);
										}
										if (!WebServiceReferences.contextTable
												.containsKey("sicallalert")
												&& LoginUser != null) {

											Log.i("calltest",
													"signBean.getBs_parentid() :"
															+ sb.getBs_parentid());
											// if (sb.getBs_parentid() != null) {
											sb.setStartTime(getCurrentDateandTime());
											// DBAccess.getdbHeler()
											// .saveOrUpdateRecordtransactiondetails(
											// sb);
											// }

											// if (isAutoAcceptEnabled(LoginUser,
											// buddy)) {
											// intiateAutoAccept(sb, buddy);
											// } else {
											// notifyIncomingCall(sb);
											// }
											notifyIncomingCall(sb);
										} else {

										}
									} else {
										Log.i("Join", "reject Call");
										rejectInComingCall(sb);
									}
								} else {
									if (sb.getCallType().equalsIgnoreCase("AP")
											|| sb.getCallType().equalsIgnoreCase(
											"VP")) {
										rejectInComingCall(sb);
									} else {
										sb.setFrom(to);
										sb.setTo(from);
										sb.setType("1");
										sb.setResult("0");
										AppMainActivity.commEngine.acceptCall(sb);
										final String buddy = getUser(sb.getFrom(),
												sb.getTo());
										if (!conferenceMembers.contains(buddy)) {
											conferenceMembers.add(0, buddy);
											buddySignall.put(buddy, sb);
										}
										callQueue.addMsg(buddy);
									}
								}

							} else if (objCallScreen instanceof AudioCallScreen) {
								Log.i("AudioCall", "callscreen object AudioCallScreen");
								if (currentSessionid != null
										&& currentSessionid.equals(sb
										.getSessionid())) {
									if (!alConferenceRequest.contains(from))
										alConferenceRequest.add(from);

									sb.setFrom(to);
									sb.setTo(from);
									sb.setType("1");
									sb.setResult("0");
									AppMainActivity.commEngine.acceptCall(sb);

									String buddy = getUser(sb.getFrom(), sb.getTo());

									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										buddySignall.put(buddy, sb);
										AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
										acalObj.UpdateConferenceMembers(buddy, true);
									}
								} else
									rejectInComingCall(sb);

							} else if (objCallScreen instanceof VideoCallScreen) {
								Log.i("AudioCall", "callscreen object VideoCallScreen");
								if (currentSessionid != null
										&& currentSessionid.equals(sb
										.getSessionid())) {

									if (!alConferenceRequest.contains(from)) {
										alConferenceRequest.add(from);
									}
									sb.setFrom(to);
									sb.setTo(from);
									sb.setType("1");
									sb.setResult("0");
									AppMainActivity.commEngine.acceptCall(sb);

									String buddy = getUser(sb.getFrom(), sb.getTo());

									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										buddySignall.put(buddy, sb);
										VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
										acalObj.UpdateConferenceMembers(buddy, true);
									}
								} else {
									rejectInComingCall(sb);
								}

							} else if (objCallScreen instanceof AudioPagingSRWindow) {
								if (currentSessionid != null
										&& currentSessionid.equals(sb
										.getSessionid())) {

								} else {
									rejectInComingCall(sb);
								}

							} else if (objCallScreen instanceof VideoPagingSRWindow) {

								if (currentSessionid != null
										&& currentSessionid.equals(sb
										.getSessionid())) {

								} else {
									rejectInComingCall(sb);
								}

							}

						}
					}

				} else if (sb.getType().equals("15")) {
					handlerForCall.post(new Runnable() {

						@Override
						public void run() {
							try {
								Toast.makeText(
										context,
										"Unable to Receive Media from "
												+ sb.getFrom(), 3000).show();
								final Object objCallScreen = SingleInstance.instanceTable
										.get("callscreen");
								if (objCallScreen != null) {
									if (objCallScreen instanceof VideoCallScreen) {
										VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
										acalObj.notifyMediaFailure(sb.getFrom());
									} else if (objCallScreen instanceof VideoPagingSRWindow) {
										VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
										acalObj.notifyMediaFailure(sb.getFrom());
									}
								}

							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					});

				}

				else if (sb.getType().equals("3")) {
					// Hang up
					Log.i("callscreenfinish", "calldispatcher type=3");
					// if (CallDispatcher.sb.getBs_parentid() != null) {
					SingleInstance.mainContext.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ImageView min_outcall = (ImageView) SingleInstance.mainContext.findViewById(R.id.min_outcall);
							min_outcall.setVisibility(View.GONE);
							ImageView min_incall = (ImageView) SingleInstance.mainContext.findViewById(R.id.min_incall);
							min_incall.setVisibility(View.GONE);
							RelativeLayout audio_minimize = (RelativeLayout) SingleInstance.mainContext.findViewById(R.id.audio_minimize);
							audio_minimize.setVisibility(View.GONE);
							RelativeLayout video_minimize = (RelativeLayout) SingleInstance.mainContext.findViewById(R.id.video_minimize);
							video_minimize.setVisibility(View.GONE);
						}
					});



					Object objCallScreen = SingleInstance.instanceTable
							.get("callscreen");
					if (objCallScreen == null) {
						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
						CallDispatcher.sb.setCallstatus("missedcall");
					} else {
						CallDispatcher.sb.setCallstatus("callattended");
					}

					if(CallDispatcher.sb.getStartTime() == null){
						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
					}

					CallDispatcher.sb.setEndTime(getCurrentDateandTime());

					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(
											CallDispatcher.sb.getStartTime(),
											CallDispatcher.sb.getEndTime()));
					Log.d("Test", "TimeDuration inside callDispatcher" + CallDispatcher.sb.getStartTime() + "" + CallDispatcher.sb.getEndTime());

//For Callhistory host and participant name entry
					//Start
					CallDispatcher.sb.setHost_name(CallDispatcher.sb.getHost());
					String participant=null;
					if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
						for(String name:CallDispatcher.conferenceMembers){
							if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())){
								if(participant==null){
									participant=name;
								}else{
									participant=participant+","+name;
								}

							}
						}
					}
					if(participant!=null){
						CallDispatcher.sb.setParticipant_name(participant);
					}
					//end

//					DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//					DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
//							CallDispatcher.sb);
					// }
					if (conferenceMembers.size() == 1) {
						isCallInitiate=false;
						Log.i("callscreenfinish","conferenceMembers.size()==1 name-->"+conferenceMembers.get(0));
						Log.i("callscreenfinish", "sb.name-->" + sb.getFrom() + " sb.getTo() :" + sb.getTo());

						DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
						DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
								CallDispatcher.sb);
						SingleInstance.mainContext.notifyUI();

						if(conferenceMembers.get(0) != null && conferenceMembers.get(0).equalsIgnoreCase(sb.getFrom())) {

							if (SingleInstance.instanceTable.containsKey("alertscreen")) {
								inCommingCallAlert inCommingcallAlert = (inCommingCallAlert)SingleInstance.instanceTable.get("alertscreen");
								if(inCommingcallAlert != null) {
									inCommingcallAlert.removeInstance();
								}
							}

							FragmentManager fm =
									AppReference.mainContext.getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							ContactsFragment contactsFragment = ContactsFragment
									.getInstance(context);
							ft.replace(R.id.activity_main_content_fragment,
									contactsFragment);
							ft.commitAllowingStateLoss();

							if (!isCallignored) {
								handlerForCall.post(new Runnable() {
									@Override
									public void run() {
										showCallHistory(CallDispatcher.sb.getSessionid(), CallDispatcher.sb.getCallType());
									}
								});
							} else {
								isCallignored =false;
							}
						}

						if(conferenceMembers.get(0) != null && sb.getFrom() != null && conferenceMembers.get(0).equalsIgnoreCase(sb.getFrom()) ) {
							if (isCallignored) {
								isCallignored = false;
							}
						} else if(conferenceMembers.get(0) != null && sb.getTo() != null && conferenceMembers.get(0).equalsIgnoreCase(sb.getTo())) {
							if (isCallignored) {
								isCallignored = false;
							}
						}
					}
					if (SingleInstance.instanceTable
							.containsKey("callscreen")
							|| SingleInstance.instanceTable
									.containsKey("connection")
							|| isIncomingAlert) {
						Object objCallScreen1 = SingleInstance.instanceTable
								.get("callscreen");

						stopRingTone();

						// new implementation.....
						if (conferenceRequest.containsKey(sb.getFrom())) {
							conferenceRequest.remove(sb.getFrom());
						}

						if (sb.getResult() != null) {
							if (SingleInstance.instanceTable
									.containsKey("connection")
									&& LoginUser.equals(callinitiator)) {

								if (sb.getResult().equals("7"))
									getofflineCallResponse(sb.getFrom(),
											"Missed Call");
								else if (sb.getResult().equals("4"))
									getofflineCallResponse(sb.getFrom(),
											"Call Disconnected");
								else if (sb.getResult().equals("2"))
									getofflineCallResponse(sb.getFrom(),
											"Unable To Connect");
								else if (sb.getResult().equals("5"))
									getofflineCallResponse(sb.getFrom(),
											"Offline");

							}
						}

						boolean isaccepted = false;
						for (String username : accepted_users) {
							if (username.equals(sb.getFrom())) {
								isaccepted = true;
								accepted_users.remove(username);
								break;
							}
						}
						if (!isaccepted) {
							handlerForCall.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (LoginUser.equals(callinitiator)) {
										getofflineCallResponse(sb.getFrom(),
												"No Answer");


//										NoAnswer(sb.getFrom());
										closeDialWindow(
												"No Answer from "
														+ sb.getFrom(), "0",
												"3");
									}

									// have to call no Answer avatar here
								}
							});

						}

						sessId = sb.getSessionid();
						if (objCallScreen1 == null) {
							if (conferenceMembers.size() == 1) {
								closeDialWindow(
										"Call Disconnected with "
												+ sb.getFrom(), "0", "3");
								isHangUpReceived = true;
								stopRingTone();
								String buddy = getUser(sb.getFrom(), sb.getTo());
								if (isIncomingAlert) {
									Log.e("callInfo",
											"Comes to isIncomingAlert if");
									conferenceMembers.clear();
									buddySignall.clear();
									currentSessionid = null;
									isCallInitiate = false;
									if (!SingleInstance.instanceTable
											.containsKey("connection")) {
										if (WebServiceReferences.missedcallCount
												.containsKey(sb.getFrom())) {
											int count = WebServiceReferences.missedcallCount
													.get(sb.getFrom());
											count = count + 1;
											WebServiceReferences.missedcallCount
													.put(sb.getFrom(), count);
										} else
											WebServiceReferences.missedcallCount
													.put(sb.getFrom(), 1);

//										DBAccess.getdbHeler()
//												.saveOrUpdateRecordtransactiondetails(
//														sb);
										if(!isCallignored) {
//											isCallignored=true;
											CallDispatcher.sb.setStartTime(getCurrentDateandTime());
											handlerForCall.post(new Runnable() {
												@Override
												public void run() {
													showCallHistory(CallDispatcher.sb.getSessionid(), CallDispatcher.sb.getCallType());
												}
											});
											showMissedcallAlert(
													sb,
													SipNotificationListener
															.getCurrentContext(),
													WebServiceReferences.missedcallCount
															.get(sb.getFrom()));
										}
									}
									isIncomingCall = false;
								}
							} else if (conferenceMembers.size() == 0) {
								//
								closeDialWindow(
										"Call Disconnected from "
												+ sb.getFrom(), "0", "3");
								isHangUpReceived = true;
								stopRingTone();
								String buddy = getUser(sb.getFrom(), sb.getTo());
								conferenceMembers.clear();
								buddySignall.clear();
								currentSessionid = null;
								isIncomingCall = false;
								if (isCallInitiate) {
									notifyCallHistoryToServer(sb.getTo(),
											sb.getFrom(),
											missedCallType(sb.getCallType()),
											sb.getSessionid(),
											getCurrentDateTime(),
											getCurrentDateTime());
								}

								isCallInitiate = false;

							}

						} else if (objCallScreen instanceof AudioCallScreen) {
							AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
							if (sb.isPacketFailure()) {
								acalObj.failedUser = sb.getFrom();
							}

							acalObj.UpdateConferenceMembers(from, false);
						} else if (objCallScreen instanceof VideoCallScreen) {
							VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
							if (sb.isPacketFailure()) {
								acalObj.failedUser = sb.getFrom();
							}
							acalObj.UpdateConferenceMembers(from, false);
						} else if (objCallScreen instanceof VideoPagingSRWindow) {
							VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
							acalObj.UpdateConferenceMembers(from, false);
						} else if (objCallScreen instanceof AudioPagingSRWindow) {
							AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
							acalObj.UpdateConferenceMembers(from, false);
						}


						try {

							final String signalId = sb.getSignalid();
							netWorkStatusHandler.postDelayed(new Runnable() {

								@Override
								public void run() {
									try {
										if (AppMainActivity.commEngine != null) {
											AppMainActivity.commEngine
													.removeAcceptCallTimer(signalId);
										}
									} catch (Exception e) {
										Log.d("EXCEPTION",
												"Exception on remove accept call Timer");
									}

								}
							}, 1000);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						if (sb.isPacketFailure()) {
							handlerForCall.post(new Runnable() {

								@Override
								public void run() {
									try {

										Toast.makeText(context, sb.getFrom() + " Call Dropped",
												3000).show();
									} catch (Exception e) {
										// TODO: handle exception
									}

								}
							});

							handlerForCall.postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									getofflineCallResponse(sb.getFrom(),
											"Call Dropped");
								}
							}, 1000);

						}

					}
				} else if (sb.getType().equals("1")
						&& sb.getResult().equals("0")) {
					// accept call
					handlerForCall.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Object objCallScreen = SingleInstance.instanceTable
										.get("callscreen");
								if (conferenceRequest.containsKey(sb.getFrom())) {
									conferenceRequest.remove(sb.getFrom());
								}

								accepted_users.add(sb.getFrom());

								if (objCallScreen == null) {
									stopRingTone();
								}
								handlerForCall.post(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (SingleInstance.instanceTable
												.containsKey("connection"))
											((CallConnectingScreen) SingleInstance.instanceTable
													.get("connection"))
													.notifyState("Connecting");
									}
								});

								if (objCallScreen == null) {
									if (sb.getCallType().equals("AC")) {
										isCalledAudiocallScreen = true;
										isHangUpReceived = false;
//										Intent i = new Intent(context
//												.getApplicationContext(),
//												AudioCallScreen.class);
//										Bundle bundle = new Bundle();
//										bundle.putSerializable("signal", sb);
//										i.putExtra("signal", bundle);
//										i.putExtra("buddy", from);
//										i.putExtra("receive", "false");
//										context.startActivity(i);

										if (SingleInstance.contextTable.containsKey("groupchat"))
										{
										GroupChatActivity groupChatActivity =(GroupChatActivity)SingleInstance.contextTable.get("groupchat");
											groupChatActivity.finish();
										}

										if (WebServiceReferences.contextTable.containsKey("ordermenuactivity")) {
											CallHistoryActivity callHistoryActivity = (CallHistoryActivity) WebServiceReferences.contextTable.get("ordermenuactivity");
											callHistoryActivity.finish();
										}

										AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
												.get("MAIN");
										appMainActivity.closingActivity();

										if (SingleInstance.instanceTable.containsKey("connection")) {
											CallConnectingScreen connectingScreen = (CallConnectingScreen)SingleInstance.instanceTable.get("connection");
											connectingScreen.removeInstance();
										}

										if (SingleInstance.instanceTable.containsKey("alertscreen")) {
											inCommingCallAlert inCommingcallAlert = (inCommingCallAlert)SingleInstance.instanceTable.get("alertscreen");
											inCommingcallAlert.removeInstance();
										}

										CallDispatcher.conferenceMember_Details = new HashMap<String,SignalingBean>();
										sb.setRunningcallstate("Connected");
										CallDispatcher.conferenceMember_Details.put(from, (SignalingBean) sb.clone());

										FragmentManager fm =
												AppReference.mainContext.getSupportFragmentManager();
										FragmentTransaction ft = fm.beginTransaction();
										AudioCallScreen  audioCallScreen = AudioCallScreen
												.getInstance(context);
										Bundle bundle = new Bundle();
										bundle.putSerializable("signal", sb);
//										i.putExtra("signal", bundle);
										bundle.putString("buddy", from);
										bundle.putString("receive", "false");
										bundle.putString("host",CallDispatcher.LoginUser);
										audioCallScreen.setArguments(bundle);
										ft.replace(R.id.activity_main_content_fragment,
												audioCallScreen);
										ft.commitAllowingStateLoss();
									} else if (sb.getCallType().equals("VBC")
											|| sb.getCallType().equals("VP")) {
										isHangUpReceived = false;
										Intent i = new Intent(context
												.getApplicationContext(),
												VideoPagingSRWindow.class);
										Bundle bundle = new Bundle();
										bundle.putString("mode", "1");// Sender
										bundle.putString("sessionid",
												sb.getSessionid());
										bundle.putString("calltype",
												sb.getCallType());
										bundle.putString("receive", "false");

										i.putExtras(bundle);
										context.startActivity(i);
										Log.e("test",
												"Comes to Accept call Open new Video broad cast sender window");
									} else if (sb.getCallType().equals("VC")) {
										isHangUpReceived = false;
//										Intent i = new Intent(context
//												.getApplicationContext(),
//												VideoCallScreen.class);
//										Bundle bundle = new Bundle();
//										bundle.putString("sessionid",
//												sb.getSessionid());
//										bundle.putString("buddyName", from);
//										bundle.putString("receive", "false");
//										i.putExtras(bundle);
//										i.putExtra("buddy", from);
//										context.startActivity(i);

										AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
												.get("MAIN");
										appMainActivity.closingActivity();
										if (SingleInstance.contextTable.containsKey("groupchat"))
										{
											GroupChatActivity groupChatActivity =(GroupChatActivity)SingleInstance.contextTable.get("groupchat");
											groupChatActivity.finish();
										}

										if (SingleInstance.instanceTable.containsKey("connection")) {
											CallConnectingScreen connectingScreen = (CallConnectingScreen)SingleInstance.instanceTable.get("connection");
											connectingScreen.removeInstance();
										}

										if (SingleInstance.instanceTable.containsKey("alertscreen")) {
											inCommingCallAlert inCommingcallAlert = (inCommingCallAlert)SingleInstance.instanceTable.get("alertscreen");
											inCommingcallAlert.removeInstance();
										}

										CallDispatcher.conferenceMember_Details = new HashMap<String,SignalingBean>();
										sb.setRunningcallstate("Connected");
										CallDispatcher.conferenceMember_Details.put(from, (SignalingBean) sb.clone());

										FragmentManager fm =
												AppReference.mainContext.getSupportFragmentManager();
										FragmentTransaction ft = fm.beginTransaction();
										VideoCallScreen videoCallScreen = VideoCallScreen
												.getInstance(context);
										Bundle bundle = new Bundle();
										bundle.putString("sessionid",
												sb.getSessionid());
										bundle.putString("buddyName", from);
										bundle.putString("receive", "false");
										bundle.putString("host",CallDispatcher.LoginUser);
//										i.putExtras(bundle);
									//	bundle.putString("buddy", from);
										videoCallScreen.setArguments(bundle);
										ft.replace(R.id.activity_main_content_fragment,
												videoCallScreen);
										ft.commitAllowingStateLoss();
									}

									else if (sb.getCallType().equals("ABC")
											|| sb.getCallType().equals("AP")) {
										isHangUpReceived = false;
										Intent i = new Intent(context
												.getApplicationContext(),
												AudioPagingSRWindow.class);
										Bundle bundle = new Bundle();
										bundle.putString("mode", "1");// Sender
										bundle.putString("sessionid",
												sb.getSessionid());
										bundle.putString("calltype",
												sb.getCallType());
										bundle.putString("receive", "false");
										i.putExtra("buddy", from);

										i.putExtras(bundle);
										context.startActivity(i);
									} else if (sb.getCallType().equals("SS")) {
										isHangUpReceived = false;
										ScreenSharingFragment ssFragment = ScreenSharingFragment
												.newInstance(context);
										ssFragment.startProjection();

										Log.i("ssharing123",
												"sender view commited");

									}
									String buddy = getUser(sb.getFrom(),
											sb.getTo());
									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(buddy);
										CallDispatcher.conferenceMembersTime
												.put(buddy,
														getCurrentDateTime());
									}
									buddySignall.put(buddy, sb);
									stopRingTone();
									sb.setType("2");
									sb.setFrom(to);
									sb.setTo(from);
									AppMainActivity.commEngine.acceptCall(sb);
								} else if (objCallScreen instanceof AudioCallScreen) {
									stopRingTone();
									String buddy = getUser(sb.getFrom(),
											sb.getTo());

									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										CallDispatcher.conferenceMembersTime
												.put(buddy,
														getCurrentDateTime());
										buddySignall.put(buddy, sb);
										AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
										acalObj.UpdateConferenceMembers(buddy,
												true);
										stopRingTone();
										sb.setType("2");
										sb.setFrom(to);
										sb.setTo(from);

										sb.setRunningcallstate("Connected");
										CallDispatcher.conferenceMember_Details.put(from, (SignalingBean) sb.clone());

										AppMainActivity.commEngine
												.acceptCall(sb);
										Log.e("test",
												"Comes to Accept call Open new audio call window 3");
									}

								} else if (objCallScreen instanceof VideoCallScreen) {
									stopRingTone();
									String buddy = getUser(sb.getFrom(),
											sb.getTo());

									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										CallDispatcher.conferenceMembersTime
												.put(buddy,
														getCurrentDateTime());
									}
									buddySignall.put(buddy, sb);
									VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
									acalObj.UpdateConferenceMembers(buddy, true);
									// acalObj.updateBuddyNames(sb.getFrom().toString());

									stopRingTone();
									sb.setType("2");
									sb.setFrom(to);
									sb.setTo(from);

									sb.setRunningcallstate("Connected");
									CallDispatcher.conferenceMember_Details.put(from, (SignalingBean) sb.clone());

									AppMainActivity.commEngine.acceptCall(sb);
									Log.e("test",
											"Comes to Accept call Open new audio call window 3");

									Log.v("test",
											"sending video calll Type ;;;;;;;;;;;;;;;;;");

								} else if (objCallScreen instanceof VideoPagingSRWindow) {
									stopRingTone();
									String buddy = getUser(sb.getFrom(),
											sb.getTo());
									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										CallDispatcher.conferenceMembersTime
												.put(buddy,
														getCurrentDateTime());
										buddySignall.put(buddy, sb);
										VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
										acalObj.updateHistoryPeople(sb
												.getFrom());
										stopRingTone();
										sb.setType("2");
										sb.setFrom(to);
										sb.setTo(from);
										AppMainActivity.commEngine
												.acceptCall(sb);
										Log.e("test",
												"Comes to Accept call Open new Video broad cast call window 3");
									}

								}

								else if (objCallScreen instanceof AudioPagingSRWindow) {
									stopRingTone();

									String buddy = getUser(sb.getFrom(),
											sb.getTo());

									if (!conferenceMembers.contains(buddy)) {
										conferenceMembers.add(0, buddy);
										CallDispatcher.conferenceMembersTime
												.put(buddy,
														getCurrentDateTime());
										buddySignall.put(buddy, sb);
										AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
										acalObj.UpdateConferenceMembers(buddy,
												true);
										stopRingTone();
										sb.setType("2");
										sb.setFrom(to);
										sb.setTo(from);
										AppMainActivity.commEngine
												.acceptCall(sb);
										Log.e("test",
												"Comes to Accept call Open new audio call window 3");
									}

								}

							} catch (Exception e) {

								e.printStackTrace();
							}
						}
					});

				} else if (sb.getType().equals("1")
						&& sb.getResult().equals("1")) {
					Log.i("callscreenfinish","calldispatcher type=1 and result=1");
					stopRingTone();
					CallDispatcher.sb.setStartTime(getCurrentDateandTime());
					handlerForCall.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getofflineCallResponse(sb.getFrom(),
									"Call Rejected");
						}
					});
					// Call rejected response
					isCallInitiate = false;
					closeDialWindow("Call rejected " + sb.getFrom(), "1", "1");
					notifyCallHistoryToServer(sb.getTo(), sb.getFrom(),
							discardCallType(sb.getCallType()),
							sb.getSessionid(), getCurrentDateTime(),
							getCurrentDateTime());

					// if (CallDispatcher.sb.getBs_parentid() != null) {
					CallDispatcher.sb.setEndTime(getCurrentDateandTime());
					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(
											CallDispatcher.sb.getStartTime(),
											CallDispatcher.sb.getEndTime()));
					CallDispatcher.sb.setCallstatus("callattended");

					//For Callhistory host and participant name entry
					//Start
					CallDispatcher.sb.setHost_name(CallDispatcher.sb.getHost());
					String participant=null;
					if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
						for(String name:CallDispatcher.conferenceMembers){
							if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())){
								if(participant==null){
									participant=name;
								}else{
									participant=participant+","+name;
								}

							}
						}
					}
					if(participant!=null){
						CallDispatcher.sb.setParticipant_name(participant);
					}
					//end

					DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
					DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
							CallDispatcher.sb);
					// }
					// DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
					// CallDispatcher.sb);
					handlerForCall.post(new Runnable() {
						@Override
						public void run() {
							showCallHistory(CallDispatcher.sb.getSessionid(),CallDispatcher.sb.getCallType());
						}
					});

					if (conferenceRequest.containsKey(sb.getFrom())) {
						conferenceRequest.remove(sb.getFrom());
					}
					if (conferenceRequest.size() == 0
							&& conferenceMembers.size() == 0) {
						Object objCallScreen =SingleInstance.instanceTable
								.get("callscreen");

						if (objCallScreen != null) {
							if (objCallScreen instanceof AudioCallScreen) {
								AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
								acalObj.receiveHangUpx(sb);
							} else if (objCallScreen instanceof VideoCallScreen) {
								VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
								acalObj.receiveHAngUpx(sb);

							} else if (objCallScreen instanceof AudioPagingSRWindow) {
								AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();
							} else if (objCallScreen instanceof VideoPagingSRWindow) {
								VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();

							}

						}
					}
						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");
							Log.d("ZZZ", "----->callscreenactivity destroy objCallScreen<-----");
					Log.i("call", " sb.getFrom() : " + sb.getFrom());
					Log.i("call","start "+AppMainActivity.connectedbuddies);
					if(AppMainActivity.connectedbuddies != null) {
						String[] totUsers=null;
						if(AppMainActivity.connectedbuddies.contains(",")) {
							totUsers = AppMainActivity.connectedbuddies.split(",");
						} else {
							totUsers = new String[1];
							totUsers[0] = AppMainActivity.connectedbuddies;
						}
						AppMainActivity.connectedbuddies = null;
						for (String touser : totUsers) {
							if (!touser.trim().equalsIgnoreCase(sb.getFrom())) {
								if (AppMainActivity.connectedbuddies == null) {
									AppMainActivity.connectedbuddies = touser;
								} else {
									AppMainActivity.connectedbuddies = AppMainActivity.connectedbuddies + "," + touser;
								}
							}
						}
					}
					Log.i("call","end "+AppMainActivity.connectedbuddies);
					if (AppMainActivity.connectedbuddies == null) {
						if(conferenceMembers != null) {
							Log.i("callscreenfinish", "conferenceMembers size" + conferenceMembers.size());

							if (conferenceMembers.size() == 1) {
								isCallInitiate = false;
								Log.i("callscreenfinish", "1 conferenceMembers.size()==1 name-->" + conferenceMembers.get(0));
								Log.i("callscreenfinish", "1 sb.name-->" + sb.getFrom());
								if (conferenceMembers.get(0).equalsIgnoreCase(sb.getFrom())) {
									Log.i("callscreenfinish", "1.1 sb.name-->" + sb.getFrom());

									if (SingleInstance.instanceTable.containsKey("alertscreen")) {
										inCommingCallAlert inCommingcallAlert = (inCommingCallAlert) SingleInstance.instanceTable.get("alertscreen");
										if (inCommingcallAlert != null) {
											inCommingcallAlert.removeInstance();
										}
									}

									FragmentManager fm =
											AppReference.mainContext.getSupportFragmentManager();
									FragmentTransaction ft = fm.beginTransaction();
									ContactsFragment contactsFragment = ContactsFragment
											.getInstance(context);
									ft.replace(R.id.activity_main_content_fragment,
											contactsFragment);
									ft.commitAllowingStateLoss();
								}
							} else if (conferenceMembers.size() == 0) {

							}
						}
					}


				} else if (sb.getType().equals("1")
						&& sb.getResult().equals("2")) {

					Object objCallScreen = SingleInstance.instanceTable
							.get("callscreen");
					if (objCallScreen == null) {
						startCallRingTone();
					}
					handlerForCall.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (SingleInstance.instanceTable
									.containsKey("connection"))
								((CallConnectingScreen) SingleInstance.instanceTable
										.get("connection"))
										.notifyState("Ringing");
						}
					});

				}

				else if (sb.getType().equals("2")) {
					Log.d("test",
							"type 2 conf size " + conferenceMembers.size());
					if (SingleInstance.instanceTable
							.containsKey("connection")) {
						((CallConnectingScreen) SingleInstance.instanceTable
								.get("connection")).notifyType2Received();
					} else {
						String buddyx = getUser(sb.getFrom(), sb.getTo());
						CallDispatcher.conferenceMembersTime.put(buddyx,
								getCurrentDateTime());

						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");
						if (objCallScreen != null
								&& objCallScreen instanceof VideoCallScreen) {
							VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;

							acalObj.updateBuddyNames(sb.getFrom());

						} else if (objCallScreen != null
								&& objCallScreen instanceof AudioCallScreen)
							((AudioCallScreen) objCallScreen)
									.notifyType2Received();
						else if (objCallScreen != null
								&& objCallScreen instanceof AudioPagingSRWindow)
							((AudioPagingSRWindow) objCallScreen)
									.notifyType2Received();
						else
							notifyType2Received();

						if (objCallScreen != null) {
							if (conferenceMembers.size() < 2) {
								updatecallDuration();
							}
						}
					}
					// bun.putString("callAcceptResponse", null);
				} else if (sb.getType().equals("8")) {
					CallDispatcher.sb.setStartTime(getCurrentDateandTime());

					stopRingTone();
					handlerForCall.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getofflineCallResponse(sb.getTo(), "No Answer");
						}
					});
					CallDispatcher.sb.setEndTime(getCurrentDateandTime());
					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(
											CallDispatcher.sb.getStartTime(),
											CallDispatcher.sb.getEndTime()));

//For Callhistory host and participant name entry
					//Start
					CallDispatcher.sb.setHost_name(CallDispatcher.sb.getHost());
					String participant=null;
					if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
						for(String name:CallDispatcher.conferenceMembers){
							if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())){
								if(participant==null){
									participant=name;
								}else{
									participant=participant+","+name;
								}

							}
						}
					}
					if(participant!=null){
						CallDispatcher.sb.setParticipant_name(participant);
					}
					//end

					CallDispatcher.sb.setCallstatus("callattended");
					DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
					DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
							CallDispatcher.sb);
					NoAnswer(sb.getTo(),sb.getCallType());

//					closeDialWindow("No Answer from " + sb.getTo(), "", "");
					accepted_users.add(sb.getTo());
					if (isCallInitiate) {
						notifyCallHistoryToServer(sb.getFrom(), sb.getTo(),
								missedCallType(sb.getCallType()),
								sb.getSessionid(), getCurrentDateTime(),
								getCurrentDateTime());
					}
					isCallInitiate = false;

					// used to show no answer...

					if (conferenceRequest.containsKey(sb.getFrom())) {
						conferenceRequest.remove(sb.getFrom());
					}
					if (conferenceRequest.size() == 0
							&& conferenceMembers.size() == 0) {
						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

								// Toast.makeText(context,
								// sb.getFrom()+" is Busy",
								// Toast.LENGTH_LONG).show();

							}
						});
						if (objCallScreen != null) {
							if (objCallScreen instanceof AudioCallScreen) {
								AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
								acalObj.receiveHangUpx(sb);
							} else if (objCallScreen instanceof VideoCallScreen) {
								VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
								acalObj.receiveHAngUpx(sb);

							} else if (objCallScreen instanceof AudioPagingSRWindow) {
								AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();
							} else if (objCallScreen instanceof VideoPagingSRWindow) {
								VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();

							}

						}
					}

				}

				else if (sb.getType().equals("10")) {
					// bun.putString("callNoAnswer", null);
					isCallInitiate = false;
					// Log.e("test", "Unable to connect");
					if (sb.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)) {
						closeDialWindow("Unable to connect " + sb.getTo(), "",
								"");
					} else {
						closeDialWindow("Unable to connect " + sb.getFrom(),
								"", "");
					}
					notifyCallHistoryToServer(sb.getFrom(), sb.getTo(),
							failedCallType(sb.getCallType()),
							sb.getSessionid(), getCurrentDateTime(),
							getCurrentDateTime());

					if (conferenceRequest.containsKey(sb.getFrom())) {
						conferenceRequest.remove(sb.getFrom());
					}
					if (conferenceRequest.size() == 0
							&& conferenceMembers.size() == 0) {
						Object objCallScreen = SingleInstance.instanceTable
								.get("callscreen");
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								getofflineCallResponse(sb.getTo(),
										"Unable To Connect");
								// Toast.makeText(context,
								// sb.getFrom()+" is Busy",
								// Toast.LENGTH_LONG).show();
							}
						});
						if (objCallScreen != null) {
							if (objCallScreen instanceof AudioCallScreen) {
								AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
								acalObj.receiveHangUpx(sb);
							} else if (objCallScreen instanceof VideoCallScreen) {
								VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
								acalObj.receiveHAngUpx(sb);

							} else if (objCallScreen instanceof AudioPagingSRWindow) {
								AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();
							} else if (objCallScreen instanceof VideoPagingSRWindow) {
								VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
								acalObj.receivedHangUpx();

							}

						}
					}

				}

				else if (sb.getType().equals("7")) {
					String values[] = sb.getConferencemember().split(",");
					// System.out.println("values.length ##############3 "+values.length);
					if (values.length > 2) {
						// bean.setFrom(loginUsername);
						// bean.setTo(values[0]);
						if (!alConferenceRequest.contains(values[0])
								&& !conferenceMembers.contains(values[0])) {
							// Log.d("WIFI", "e7 "+values[0]);
							alConferenceRequest.add(values[0]);
						}

					}
				}
				if (sb.getType().equals("12")) {
					if ((sb.getCallType().equals("MTP"))
							|| (sb.getCallType().equals("MPP"))
							|| (sb.getCallType().equals("MAP"))
							|| (sb.getCallType().equals("MVP"))
							|| (sb.getCallType().equals("MHP"))) {

						Log.d("IM", "Response Received");
						Log.d("up_prog", " ********* notify on UI  ********* :"

						+ sb.getSignalid());
						if (WebServiceReferences.hsIMProgress.containsKey(sb
								.getSignalid().trim())) {
							Log.d("up_prog",
									" ~~~~~~~~~~~ Key Matched  ~~~~~~~~~~~ ");

							ProgressBar prgs = WebServiceReferences.hsIMProgress
									.get(sb.getSignalid());
							prgs.post(new Runnable() {

								@Override
								public void run() {
									WebServiceReferences.hsIMProgress.get(
											sb.getSignalid()).setVisibility(
											View.INVISIBLE);

								}
							});
							final ImageView img = WebServiceReferences.hsIMImageView
									.get(sb.getSignalid());
							String status = null;
							for (BuddyInformationBean bean : ContactsFragment
									.getBuddyList()) {
								if (!bean.isTitle()) {
									if (bean.getName().equalsIgnoreCase(
											CallDispatcher.buddyName)) {
										status = bean.getStatus();
									}
								}
							}
							if (status != null
									&& status.equalsIgnoreCase("Online")
									|| status.equalsIgnoreCase("away")
									|| status.equalsIgnoreCase("stealth")) {
								if (img != null) {
									img.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub
											WebServiceReferences.hsIMImageView
													.get(sb.getSignalid())
													.setImageResource(
															R.drawable.chatsent);
											WebServiceReferences.hsIMImageView
													.get(sb.getSignalid())
													.setContentDescription(
															"true");

										}
									});
								} else {

								}
							} else {
								if (img != null) {
									img.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub
											WebServiceReferences.hsIMImageView
													.get(sb.getSignalid())
													.setImageResource(
															R.drawable.chatsending);
											WebServiceReferences.hsIMImageView
													.get(sb.getSignalid())
													.setContentDescription(
															"false");

										}
									});
								} else {
								}
							}

						}
						if (!prevsignal.equals(sb.getSignalid())) {
							if (xmlmap.containsKey(sb.getFrom())) {
								XMLComposer writter = (XMLComposer) xmlmap
										.get(sb.getFrom());
								if (sb.getCallType().equals("MTP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "txt");
								} else if (sb.getCallType().equals("MPP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "img");
								} else if (sb.getCallType().equals("MAP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "audio");
								} else if (sb.getCallType().equals("MVP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "video");
								} else if (sb.getCallType().equals("MHP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "img");
								}
							} else {

								XMLComposer writter = new XMLComposer(
										"/sdcard/COMMedia/" + sb.getSessionid());
								if (sb.getCallType().equals("MTP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "txt");
								} else if (sb.getCallType().equals("MPP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "img");
								} else if (sb.getCallType().equals("MAP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "audio");
								} else if (sb.getCallType().equals("MVP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "video");
								} else if (sb.getCallType().equals("MHP")) {
									writter.updateFlag(sb.getSignalid().trim(),
											"1", "img");
								}
							}
							prevsignal = sb.getSignalid();
						}
					} else {

					}
				}

				if (sb.getType().equals("11")) {

					if (sb.getisRoboKey() != null) {
						if (sb.getisRoboKey().trim().length() == 0) {
							sb.setisrobokey(null);
						}
					}

					if ((sb.getCallType().equals("MTP"))) {

						if (notifier == null) {
							notifier = new BackgroundNotification(context);
						}

						// imNotifier = (IMNotifier)
						// WebServiceReferences.contextTable
						// .get("IM");

						if (sb.getisRoboKey() == null) {
							message_map.put(sb.getFrom(), sb.getMessage());
							ChatBean chatBean = getChatBean(sb);
							chatBean.setUsername(CallDispatcher.LoginUser
									+ sb.getFrom());
							SingleInstance.getChatHistoryWriter().getQueue()
									.addObject(chatBean);

							Vector<GroupChatBean> chatList = SingleInstance.chatHistory
									.get(chatBean.getFromUser());
							// if (chatList != null)
							// chatList.add(chatBean);
							// else {
							// chatList = new Vector<G>();
							// chatList.add(chatBean);
							// SingleInstance.chatHistory.put(
							// chatBean.getFromUser(), chatList);
							// }

							ChatActivity chat = (ChatActivity) SingleInstance.contextTable
									.get("chatactivity");
							if (chat != null)
								chat.updateUI(chatBean);

							else {
								if (WebServiceReferences.Imcollection
										.containsKey(sb.getSessionid())) {
									ArrayList<SignalingBean> al = WebServiceReferences.Imcollection
											.get(sb.getSessionid());
									al.add(sb);

									if (isApplicationInBackground(context)) {

										if (this.notifier != null) {
											if (WebServiceReferences.Imcollection
													.size() == 1) {
												this.notifier.ShowNotification(
														sb.getMessage(),
														sb.getFrom() + ":"
																+ al.size(),
														"im");
											} else {
												this.notifier.ShowNotification(
														sb.getMessage(),
														"Conversation :"
																+ al.size(),
														"im");
											}

										}
									}

								} else {
									if (!WebServiceReferences.activeSession
											.containsKey(sb.getFrom())) {
										WebServiceReferences.activeSession
												.put(sb.getFrom(),
														sb.getSessionid());

										ArrayList<SignalingBean> al = new ArrayList<SignalingBean>();
										al.add(sb);
										WebServiceReferences.Imcollection.put(
												sb.getSessionid(), al);
									} else {
										String session = WebServiceReferences.activeSession
												.get(sb.getFrom());
										if (WebServiceReferences.Imcollection
												.containsKey(session)) {
											ArrayList<SignalingBean> lst = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
													.get(session);
											lst.add(sb);

										} else {
											WebServiceReferences.activeSession
													.remove(sb.getFrom());
											WebServiceReferences.activeSession
													.put(sb.getFrom(),
															sb.getSessionid());

											ArrayList<SignalingBean> al = new ArrayList<SignalingBean>();
											al.add(sb);
											WebServiceReferences.Imcollection
													.put(sb.getSessionid(), al);
										}
									}
									CompleteListView mInstance = (CompleteListView) WebServiceReferences.contextTable
											.get("MAIN");
									GenerateimView(mInstance.IMRequest,
											sb.getFrom(), sb.getSessionid(),
											false, "", mInstance.owner);

								}

								if (imNotifier != null) {
									imNotifier.notifyReceivedIM(sb);
								}
							}
							handlerForCall.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									ContactsFragment.getContactAdapter()
											.notifyDataSetChanged();
									ExchangesFragment exchanges = ExchangesFragment
											.newInstance(context);
									if (exchanges != null)
										exchanges.notifyGroupList();
									imTone();
								}
							});

						}

						if (isApplicationInBackground(context)) {
							if (sb.getisRoboKey() == null) {
								WebServiceReferences.tempIMCount += 1;
								if (CallDispatcher.this.notifier != null) {
									if ((WebServiceReferences.Imcollection
											.size() == 1)
											|| (WebServiceReferences.Imcollection
													.size() == 0)) {
										CallDispatcher.this.notifier
												.ShowNotification(
														sb.getMessage(),
														sb.getFrom()
																+ ":"
																+ WebServiceReferences.tempIMCount,
														"im");
									} else {
										CallDispatcher.this.notifier
												.ShowNotification(
														sb.getMessage(),
														"Conversation :"
																+ WebServiceReferences.tempIMCount,
														"im");
									}

								}
							}

						} else {
							Log.d("MSG",
									"!!!!!!!!!!!! app background else part......");
						}

					} else {
						AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
								.get("MAIN");
						FTPBean bean = new FTPBean();
						bean.setFtp_username(sb.getFtpUser());
						bean.setFtp_password(sb.getFtppassword());
						bean.setServer_port(40400);
						bean.setOperation_type(2);
						bean.setComment(sb.getSessionid());
						bean.setServer_ip(appMainActivity.cBean.getRouter()
								.split(":")[0]);
						bean.setFile_path(sb.getFilePath());
						bean.setReq_object(sb);
						bean.setRequest_from(sb.getCallType());
						appMainActivity.getFtpNotifier()
								.addTasktoExecutor(bean);
					}
				}

				

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}
	private void NoAnswer(final String Username, final String Calltype){


		noanswerhandler.post(new Runnable() {

			@Override
			public void run() {

				Context context = null;
				if (SingleInstance.contextTable.get("groupchat") != null) {
					context = SingleInstance.contextTable.get("groupchat");
				} else {
					context = SingleInstance.mainContext;
				}
				final Dialog dialog = new Dialog(context);
				final ImageLoader imageLoader = new ImageLoader(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.no_answer_screen);
				dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
				dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
				dialog.show();
				TextView do_lay = (TextView) dialog.findViewById(R.id.do_lay);
				TextView username = (TextView)dialog.findViewById(R.id.username);
				ImageView profile_pic = (ImageView)dialog. findViewById(R.id.riv1);
				TextView send_messagelay = (TextView)dialog.findViewById(R.id.send_messagelay);
				TextView callback_messagelay = (TextView)dialog.findViewById(R.id.callback_messagelay);

				callback_messagelay.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(Calltype.equalsIgnoreCase("AC")){
							Log.d("string", "Calltype"+Calltype);
							MakeCall(1, Username, SingleInstance.mainContext);
						}
						else if(Calltype.equalsIgnoreCase("VC")) {
							MakeCall(2, Username,
									SingleInstance.mainContext);
						}
						dialog.dismiss();

					}
				});

				send_messagelay.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {


					}
				});


				ProfileBean bean = DBAccess.getdbHeler().getProfileDetails(Username);
				if(bean.getFirstname()!= null && bean.getLastname()!= null){
					username.setText(bean.getFirstname()+ " "+bean.getLastname());

				}
				String Profilephoto = bean.getPhoto();
				if(Profilephoto!= null&& Profilephoto.length() > 0){
					String pic_Path = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/COMMedia/" + bean.getPhoto();
					File pic = new File(pic_Path);
					if (pic.exists()) {
						imageLoader.DisplayImage(pic_Path, profile_pic , R.drawable.img_user);
					}

				}




				do_lay.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
	}

	private ChatBean getChatBean(SignalingBean sb) {
		// TODO Auto-generated method stub
		ChatBean chatBean = new ChatBean();
		chatBean.setFromUser(sb.getFrom());
		chatBean.setToUser(sb.getTo());
		chatBean.setSignalId(sb.getSignalid());
		chatBean.setSessionId(sb.getSessionid());
		chatBean.setTimeDate(getCurrentDateandTime());
		chatBean.setMessageType(sb.getCallType());
		chatBean.setSender(false);
		if (!sb.getCallType().equalsIgnoreCase("MTP")) {
			chatBean.setMessage(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/COMMedia/" + sb.getFilePath());
			if (sb.getCallType().equalsIgnoreCase("MPP")
					|| sb.getCallType().equalsIgnoreCase("MHP")) {
				// Bitmap bitMap = ResizeImage(Environment
				// .getExternalStorageDirectory().getAbsolutePath()
				// + "/COMMedia/" + sb.getFilePath());
				// chatBean.setProfilePic(bitMap);
			}
		} else {
			chatBean.setMessage(sb.getMessage());
		}

		return chatBean;
	}

	private void closeDialWindow(String strMsg, String type, String result) {
		try {
			if (dialChecker) {
				showBusyMessage(strMsg);
				Log.i("calltest", "closeDialWindow--dialChecker");

			} else {
				if (type.equals("1") && result.equals("1")) {
					Log.i("calltest", "closeDialWindow" + strMsg);
					showBusyMessage(strMsg);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void showBusyMessage(final String ErrMsg) {
		try {
			netWorkStatusHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

//					showAlert("", ErrMsg);
					Log.i("calltest", "showBusyMessage" +SingleInstance.instanceTable
							.containsKey("connection") +SingleInstance.instanceTable.containsKey("callscreen")+issecMadeConference);
					if (SingleInstance.instanceTable
							.containsKey("connection")) {
						if (!SingleInstance.instanceTable.containsKey("callscreen")) {
							if (!issecMadeConference) {
								((CallConnectingScreen) SingleInstance.instanceTable
										.get("connection")).finishConnectingScreen();
							}
//							else if (issecMadeConference
//									&& CallDispatcher.contConferencemembers
//									.size() == 0) {
//								if(CallDispatcher.sb != null && CallDispatcher.sb.getParticipants() != null) {
//									String mam_bers = CallDispatcher.sb.getParticipants();
//									String[] mam_bers_array = mam_bers.split("'");
//									if (mam_bers_array.length == 1 && !SingleInstance.instanceTable.containsKey("callscreen")) {
//										((CallConnectingScreen) SingleInstance.instanceTable
//												.get("connection")).finishConnectingScreen();
//									}
//								}
//							}
						}
					} else if (issecMadeConference
								&& CallDispatcher.contConferencemembers
										.size() == 0) {
						if (!SingleInstance.instanceTable.containsKey("callscreen")) {
							((CallConnectingScreen) SingleInstance.instanceTable
									.get("connection")).finishConnectingScreen();
						}
					}

					// ShowToast("User Busy", 1);

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// private void notifySigninError(final String title, final String message,
	// final Context context) {
	// handlerForCall.post(new Runnable() {
	//
	// @SuppressWarnings("deprecation")
	// @Override
	// public void run() {
	// try {
	// // TODO Auto-generated method stub
	//
	// Object objCallScreen = WebServiceReferences.contextTable
	// .get("callscreen");
	//
	// if (objCallScreen != null) {
	//
	// if (objCallScreen instanceof AudioCallScreen) {
	// AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
	// acalObj.notifyGSMCallAccepted();
	// } else if (objCallScreen instanceof VideoCallScreen) {
	//
	// VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
	// acalObj.notifyGSMCallAccepted();
	//
	// } else if (objCallScreen instanceof AudioPagingSRWindow) {
	// AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
	// acalObj.notifyGSMCallAccepted();
	//
	// }
	//
	// else if (objCallScreen instanceof VideoPagingSRWindow) {
	// VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
	// acalObj.notifyGSMCallAccepted();
	//
	// }
	// }
	// if (WebServiceReferences.contextTable
	// .containsKey("connection"))
	// ((CallConnectingScreen) WebServiceReferences.contextTable
	// .get("connection")).HangupCall();
	//
	// AlertDialog confirmation = new AlertDialog.Builder(context)
	// .create();
	// confirmation.setTitle(title);
	// confirmation.setMessage(message);
	// confirmation.setCancelable(false);
	// confirmation.setButton("OK",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// try {
	// // TODO Auto-generated method stub
	// appMainActivity.logout(false);
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// confirmation.show();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

	public void updateBuddyStatus(final UdpMessageBean message) {
		try {

			if (message.getText() != null) {

				netWorkStatusHandler.post(new Runnable() {
					@Override
					public void run() {
						try {
							if (message
									.getText()
									.equalsIgnoreCase(
											"Some other user with same user name has logged in")) {
//                                TCPEngine.getTCPClient().stopClient();
								isAnotherUserLogedIn = true;
								SingleInstance.mainContext.notifySigninError(
										"Duplicate Login", message.getText());
							}
							else if (message
									.getText().trim()
									.equalsIgnoreCase(
											"Session Invalid")) {
								isAnotherUserLogedIn = true;
								showToast(SingleInstance.mainContext,"Session Invalid:Time out");

								SingleInstance.mainContext.notifySigninError(
										"Timed Out", message.getText());
							}
							else {
								if (AppReference.isWriteInFile)
									AppReference.logger
											.debug("LOGOUT REASON :: "
													+ message.getText());
								// showAlert("LOGOUT",
								// "Reason : " + message.getText());
								// isAnotherUserLogedIn = true;
								// appMainActivity.logout(false);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});

			} else {

				final BuddyInformationBean bib = message.getBuddyInformation();

				if (bib != null) {

					if (latConfigure != 0 && longConfigure != 0
							&& serviceType != 5 && isLocationServiceEnabled) {

						if (bib.getLatitude() != null
								&& bib.getLongitude() != null) {

							if (bib.getLatitude().trim().length() != 0
									&& bib.getLongitude().trim().length() != 0) {

								String settingsLat = String.format("%.2f",
										latConfigure);
								String settingsLong = String.format("%.2f",
										longConfigure);

								String chklat = settingsLat.substring(0,
										settingsLat.indexOf(".") + 2);
								Log.d("UMBX", "OnLOgin " + chklat);
								String chklong = settingsLong.substring(0,
										settingsLong.indexOf(".") + 2);
								Log.d("UMBX", "OnLOgin " + chklong);

								double receivedlatvalueindouble = Double
										.parseDouble(bib.getLatitude());
								double receivedlongvalueindouble = Double
										.parseDouble(bib.getLongitude());

								final String recLat = String.format("%.2f",
										receivedlatvalueindouble);
								final String recLong = String.format("%.2f",
										receivedlongvalueindouble);

								final String recchaklat = recLat.substring(0,
										recLat.indexOf(".") + 2);
								Log.d("UMBX", "OnLOgin " + recchaklat);
								final String recchklong = recLong.substring(0,
										recLong.indexOf(".") + 2);

								if (chklat.equals(recchaklat)
										&& chklong.equals(recchklong)) {
									Log.d("UMBX", "received if");
									handlerForCall.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub

											try {
												if (serviceType != 2) {
													if (currentSessionid == null) {

														BuddyInformationBean oldBuddyBean = null;

														for (BuddyInformationBean temp : ContactsFragment
																.getBuddyList()) {
															if (temp.getName()
																	.equalsIgnoreCase(
																			bib.getName())) {
																oldBuddyBean = temp;
																break;
															}
														}

														Log.d("UMBX",
																"received  to make service"
																		+ oldBuddyBean);
														if (oldBuddyBean != null
																&& oldBuddyBean
																		.isIslocationBasedServiceDone()) {
															// used to calculate
															// old lat long....

															//

															Log.d("UMBX",
																	"old lat"
																			+ oldBuddyBean
																					.getLatitude());
															Log.d("UMBX",
																	"old long..."
																			+ oldBuddyBean
																					.getLongitude());
															double oldlatvalueindouble = Double
																	.parseDouble(oldBuddyBean
																			.getLatitude());
															double oldlongvalueindouble = Double
																	.parseDouble(oldBuddyBean
																			.getLongitude());

															String oldrecLat = String
																	.format("%.2f",
																			oldlatvalueindouble);
															String oldrecLong = String
																	.format("%.2f",
																			oldlongvalueindouble);
															//
															String recchaklatold = oldrecLat
																	.substring(
																			0,
																			oldrecLat
																					.indexOf(".") + 2);
															Log.d("UMBX",
																	"OnLOgin "
																			+ recchaklatold);
															String recchklongold = oldrecLong
																	.substring(
																			0,
																			oldrecLong
																					.indexOf(".") + 2);
															Log.d("UMBX",
																	"OnLOgin "
																			+ recchklongold);

															if (!recchaklat
																	.equals(recchaklatold)
																	&& !recchklong
																			.equals(recchaklatold)) {
																Log.d("UMBX",
																		"different");
																Toast.makeText(
																		context,
																		"Make call",
																		2000)
																		.show();
																bib.setIslocationBasedServiceDone(true);
																LocationMadeService(
																		bib.getName(),
																		"");
															} else {
																bib.setIslocationBasedServiceDone(true);
																Log.d("UMBX",
																		"same");
																Toast.makeText(
																		context,
																		"Already processed",
																		2000)
																		.show();
															}
														} else {
															Log.d("UMBX",
																	"normal");
															Toast.makeText(
																	context,
																	"Make call",
																	2000)
																	.show();
															bib.setIslocationBasedServiceDone(true);

															LocationMadeService(
																	bib.getName(),
																	"");
														}

													}
												} else {
													if (imagepath != null) {
														Toast.makeText(
																context,
																"sending notes",
																2000).show();

														sendNotesOnLatLong(bib
																.getName());
													}
												}
											} catch (Exception e) {
												// TODO: handle exception
												e.printStackTrace();
											}

										}
										// }
										// cat
									});

								} else {
									Log.d("UMBX", "received else");
								}
							} else {
								BuddyInformationBean oldBuddyBean = null;
								for (BuddyInformationBean temp : ContactsFragment
										.getBuddyList()) {
									if (!temp.isTitle()) {
										if (temp.getName().equalsIgnoreCase(
												bib.getName())) {
											oldBuddyBean = temp;
											break;
										}
									}
								}

								if (oldBuddyBean != null) {
									Log.d("UMBX",
											"new location service came to else part "
													+ oldBuddyBean
															.isIslocationBasedServiceDone());

									bib.setIslocationBasedServiceDone(oldBuddyBean
											.isIslocationBasedServiceDone());
									bib.setLatitude(oldBuddyBean.getLatitude());
									bib.setLongitude(oldBuddyBean
											.getLongitude());
									Log.d("UMBX",
											"new location lat "
													+ bib.getLatitude());
									Log.d("UMBX",
											"new location long "
													+ bib.getLongitude());
								}
							}
						} else {

							// used to set lat long as old .....

							BuddyInformationBean oldBuddyBean = null;
							for (BuddyInformationBean temp : ContactsFragment
									.getBuddyList()) {
								if (!temp.isTitle()) {
									if (temp.getName().equalsIgnoreCase(
											bib.getName())) {
										oldBuddyBean = temp;
										break;
									}
								}
							}

							if (oldBuddyBean != null) {
								Log.d("UMBX",
										"new location service "
												+ oldBuddyBean
														.isIslocationBasedServiceDone());

								bib.setIslocationBasedServiceDone(oldBuddyBean
										.isIslocationBasedServiceDone());
								bib.setLatitude(oldBuddyBean.getLatitude());
								bib.setLongitude(oldBuddyBean.getLongitude());
								Log.d("UMBX",
										"new location lat " + bib.getLatitude());
								Log.d("UMBX",
										"new location long "
												+ bib.getLongitude());
							}
						}
					}

					Log.d("UMB",
							"Received UdpMessageBean reached "
									+ bib.getStatus());
					Log.d("UMB", "Received UdpMessageBean reached======== "
							+ bib.getName());
					try {
						GroupChatActivity gca = (GroupChatActivity) SingleInstance.contextTable
								.get("groupchat");
						gca.updateBuddy(bib.getName(), bib.getStatus());
					}catch(Exception e){
						Log.i("DCBA", "Received NO Class found. reached======== "
								+ bib.getName() + "status " + bib.getStatus());
					}

					if (bib.getStatus().equals("0")
							|| bib.getStatus().equalsIgnoreCase("Online")
							|| bib.getStatus().equals("1")
							|| bib.getStatus().equals("2")
							|| bib.getStatus().equals("3")
							|| bib.getStatus().equals("4")) {

						for (BuddyInformationBean buddyInformationBean : ContactsFragment
								.getBuddyList()) {
							if (!buddyInformationBean.isTitle()) {
								if (buddyInformationBean.getName().equals(
										bib.getName())) {
									Log.i("contacts123", bib.getName() + "-"
											+ bib.getStatus());
									if (buddyInformationBean.getStatus()
											.equalsIgnoreCase("pending")) {
										if (!bib.getStatus().equalsIgnoreCase(
												"delete")) {
											showToast(
													SingleInstance.mainContext,
													buddyInformationBean
															.getName()
															+ " accepts your request");
											WebServiceReferences.webServiceClient.GetAllProfile(CallDispatcher.LoginUser,                                                                          buddyInformationBean.getName(),SingleInstance.mainContext);
										}
									}
									if (bib.getStatus().equalsIgnoreCase("0")) {
										buddyInformationBean
												.setStatus("Offline");
									} else if (bib.getStatus()
											.equalsIgnoreCase("1")) {
										buddyInformationBean
												.setStatus("Online");
									} else if (bib.getStatus()
											.equalsIgnoreCase("2")) {
										buddyInformationBean
												.setStatus("Airport");
									} else if (bib.getStatus()
											.equalsIgnoreCase("3")) {
										buddyInformationBean.setStatus("Away");
									} else if (bib.getStatus()
											.equalsIgnoreCase("4")) {
										buddyInformationBean
												.setStatus("Stealth");
									} else {
										Log.i("contacts123",
												"UNKNOWN : " + bib.getName()
														+ "-" + bib.getStatus());
									}
									break;
								}
							}
						}
						

						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								ContactsFragment.getInstance(context)
										.SortList();
							}
						});

						if (WebServiceReferences.contextTable
								.containsKey("utilitybuyer")) {
							UtilityBuyer buyer = (UtilityBuyer) WebServiceReferences.contextTable
									.get("utilitybuyer");
							buyer.updateBuddyStatus(bib.getName(),
									bib.getStatus());
						}
						if (WebServiceReferences.contextTable
								.containsKey("utilityseller")) {
							UtilitySeller seller = (UtilitySeller) WebServiceReferences.contextTable
									.get("utilityseller");
							seller.updateBuddyStatus(bib.getName(),
									bib.getStatus());
						}
						if (WebServiceReferences.contextTable
								.containsKey("utilityneeder")) {
							UtilityServiceNeeder needer = (UtilityServiceNeeder) WebServiceReferences.contextTable
									.get("utilityneeder");
							needer.updateBuddyStatus(bib.getName(),
									bib.getStatus());
						}
						if (WebServiceReferences.contextTable
								.containsKey("utilityprovider")) {
							UtilityServiceProvider provider = (UtilityServiceProvider) WebServiceReferences.contextTable
									.get("utilityprovider");
							provider.updateBuddyStatus(bib.getName(),
									bib.getStatus());
						}
						//

						// Log.d("UMB", "Received UdpMessageBean reached name"
						// + bib.getName());
						// Log.d("UMB", "Received UdpMessageBean reached status"
						// + bib.getStatus());
						// // if(bib.getLocalipaddress()!=null)
						//
						// if (WebServiceReferences.buddyList.containsKey(bib
						// .getName())) {
						// Log.d("UMB", "Received UdpMessageBean reached 1");
						//
						// Log.d("UMBX",
						// "Going to update in Buddy List..........................");
						// if (bib.getLocalipaddress() != null) {
						// Log.d("UMB",
						// "Received UdpMessageBean reached 1a");
						// Log.d("LR", "Buddy list called 7");
						//
						// Log.d("UMBX", "Replacedddd       ....");
						// BuddyInformationBean bean = (BuddyInformationBean)
						// WebServiceReferences.buddyList
						// .get(bib.getName());
						// bib.setIslocationBasedServiceDone(bean
						// .isIslocationBasedServiceDone());
						// WebServiceReferences.buddyList.put(
						// bib.getName(), bib);
						//
						// } else {
						// Log.d("UMB",
						// "Received UdpMessageBean reached 1b");
						//
						// Log.d("UMBX", "Only change Status....");
						// BuddyInformationBean changeInformation =
						// WebServiceReferences.buddyList
						// .get(bib.getName());
						//
						// Log.d("UMBX",
						// "Only change loc services"
						// + changeInformation
						// .isIslocationBasedServiceDone());
						// changeInformation.setStatus(bib.getStatus());
						//
						// }
						// } else {
						// Log.d("UMB", "Received UdpMessageBean reached 2");
						// Log.d("LR", "Buddy list called 6");
						// WebServiceReferences.buddyList.put(bib.getName(),
						// bib);
						// }
						//
						// handlerForCall.post(new Runnable() {
						//
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// Log.e("sig", "call from UDp");
						// Log.d("FUDP",
						// "FAce book UDP Name " + bib.getName());
						// Log.d("FUDP",
						// "FAce book UDP Id" + bib.getFbID());
						// try {
						// int idx = getDataBean(bib.getName());
						// if (idx != -1) {
						// Log.d("FUDP", "on if");
						// getBuddyList();
						//
						// }
						//
						// else {
						// Log.d("FUDP", "on else");
						// new SortBuddies().execute("");
						// }
						//
						// } catch (Exception e) {
						// // TODO: handle exception
						// }
						//
						// }
						// });
					}
					if (bib.getStatus().equals("new")) {
						Log.d("UMB",
								"Received UdpMessageBean reached on new request "
										+ bib.getStatus());
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									Log.d("thread",
											"New Request from UDP Request Buddyy");

									// if (!WebServiceReferences.buddyList
									// .containsKey(bib.getName().trim())
									// && !WebServiceReferences.reqbuddyList
									// .containsKey(bib.getName())) {
									// WebServiceReferences.reqbuddyList.put(
									// bib.getName(), bib);
									// getReqBuddy();
									// }
									if (isApplicationInBackground(SingleInstance.mainContext)) {
										SingleInstance.buddyRequestMessageList
												.add(bib.getName()
														+ " sent request to you");
									} else {
										showToast(
												SingleInstance.mainContext,
												bib.getName()
														+ " sent request to you");
									}
									SingleInstance.mainContext
											.updateBuddyInformation(bib);
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						});
					}
					if (bib.getStatus().equalsIgnoreCase("delete")) {

						Log.d("thread", "Received Udp for delete ");
						Log.d("thread", "Before Updated on delete");

						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								DBAccess.getdbHeler().deleteProfile(
										bib.getName());
								DBAccess.getdbHeler()
										.deleteGroupChatEntryLocally(
												bib.getName(),
												CallDispatcher.LoginUser);
								DBAccess.getdbHeler().deleteIndividualChat(
										bib.getName());
								// pendingToShow.deleteUser(bib.getName());
								// pendingToShow.notifyDataSetChanged();
								// for (Object name : showBuddies) {
								// if (((String) name).equals(bib.getName())) {
								// showBuddies.remove(name);
								// break;
								// }
								// }
								if (SingleInstance.groupChatHistory
										.containsKey(bib.getName())) {
									SingleInstance.groupChatHistory.remove(bib
											.getName());
								}
								SingleInstance.mainContext
										.updateBuddyInformation(bib);

							}
						});

						// if (WebServiceReferences.reqbuddyList.containsKey(bib
						// .getName())) {
						// WebServiceReferences.reqbuddyList.remove(bib
						// .getName());
						//
						// }

						// handlerForCall.post(new Runnable() {
						// @Override
						// public void run() {
						// // TODO Auto-generated method stub
						// Log.d("thread", "called buddy list  ");
						// // getBuddyList();
						// // WebServiceReferences.buddyList.clear();
						// try {
						// isConnected = true;
						// adapterToShow.deleteUser(bib.getName());
						// new SortBuddies().execute("");
						//
						// } catch (Exception e) {
						// // TODO: handle exception
						// }
						//
						// }
						// });

					}

				} else if (message.getPermission_details() != null) {
				
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					String[] details = new String[3];
					details[0] = message.getPermission_details()[1];
					details[1] = message.getPermission_details()[0];
					String dateTime = getdbHeler(context)
							.getMaxDateofPermission(details[1]);
					if (dateTime == null) {
						dateTime = "";
					}
					details[2] = dateTime;
					WebServiceReferences.webServiceClient
							.getAllPermission(details);

				} else if(message.getReset_account()!=null) {
					WebServiceReferences.webServiceClient.GetAllProfile(CallDispatcher.LoginUser,
							message.getReset_account()[0], SingleInstance.mainContext);
				}
				else if (message.getProfile_details() != null) {
					
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					if (message.getProfile_details()[2].length() == 0) {
						Vector<String> multimediaFields = getdbHeler(context)
								.getMultimediaFields(
										"SELECT fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
												+ message.getProfile_details()[0]
												+ "'");
						if (multimediaFields != null
								&& multimediaFields.size() > 0) {
							for (String fieldName : multimediaFields) {
								File file = new File(Environment
										.getExternalStorageDirectory()
										.getAbsoluteFile()
										+ "/COMMedia/" + fieldName);
								if (file.exists())
									file.delete();

								if (bitmap_table.containsKey(file.getName())) {
									Bitmap bmp = bitmap_table.remove(file
											.getName());
									if (bmp != null)
										if (!bmp.isRecycled())
											bmp.recycle();
								}

							}

							getdbHeler(context).deleteFieldValues(
									"DELETE from profilefieldvalues WHERE userid="
											+ "\""
											+ message.getProfile_details()[0]
													.toString().trim() + "\"");
						}
					} else {
						String modified_date = getdbHeler(context)
								.getModifiedDate(
										"select max(modifieddate) from profilefieldvalues where userid='"
												+ message.getProfile_details()[0]
												+ "'");
						if (modified_date == null)
							modified_date = "";
						else if (modified_date.trim().length() == 0)
							modified_date = "";

						if (message.getProfile_details()[2] != null) {
							if (!message.getProfile_details()[2]
									.equals(modified_date)) {
								String[] profileDetails = new String[3];
								profileDetails[0] = message
										.getProfile_details()[0];
								profileDetails[1] = "5";
								profileDetails[2] = modified_date;
								WebServiceReferences.webServiceClient
										.getStandardProfilefieldvalues(profileDetails);
							}
						}

					}
				} else if (message.getResponseObject() != null) {
					if (SingleInstance.contextTable.containsKey("MAIN")) {
						((AppMainActivity) SingleInstance.contextTable
								.get("MAIN")).notifyUDPupdates(message
								.getResponseObject());
					} else {
						showToast(SipNotificationListener.getCurrentContext(),
								"Main Activity is NULL");
					}
				}

				else if (message.getCallrresponseupadte() != null) {
					String[] details = (String[]) message
							.getCallrresponseupadte();
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					String[] params = new String[3];
					params[0] = CallDispatcher.LoginUser;
					params[1] = details[0];
					params[2] = details[1];
					WebServiceReferences.webServiceClient
							.GetConfigurationResponDetails(params);

				}

				else if (message.getFromsList() != null) {
					if (message.getFromsList().size() > 0) {
						settingsConfig.addAll(message.getFromsList());
						if (message.getSettingobj() != null)
							settingsConfig.add(message.getSettingobj());
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {

								processUdpForms();
							}
						});

					}
				}

				else if (message.getDeleteObject() != null) {
					if (message.getDeleteObject() instanceof FormsBean) {
						FormsBean bean = (FormsBean) message.getDeleteObject();
						if (bean.isDeletedForm()) {

							handleDeleteForms(bean);
						} else if (bean.isDeletedRecord()) {

							handleDeleteFormsRecords(bean);
						} else if (bean.isEditRecord()) {

							WebServiceReferences.webServiceClient.Getcontent(
									CallDispatcher.LoginUser, bean.getFormId(),
									bean.getFormtime(), context);
						}
					}

				} else if (message.getEditObject() != null) {
					if (message.getEditObject() instanceof FormsBean) {
						FormsBean bean = (FormsBean) message.getEditObject();
						if (bean.isEditRecord()) {

							handleDataForms(bean);
						}
					}
				} else if (message.getConfig_responselist() != null) {
					final String[] configresponse_list = (String[]) message
							.getConfig_responselist();
					if (configresponse_list[0] != null) {
						getdbHeler(context).deleteAllshares(
								"DELETE from formslookup WHERE owner=" + "\""
										+ configresponse_list[1].trim() + "\"");
						getdbHeler(context).deleteAllshares(
								"DELETE from formsettings WHERE formowenerid="
										+ "\"" + configresponse_list[1].trim()
										+ "\"");
						getdbHeler(context).deleteAllshares(
								"DELETE from profilefieldvalues WHERE userid="
										+ "\"" + configresponse_list[1].trim()
										+ "\"");
						getdbHeler(context).deleteAllshares(
								"DELETE from offlinecallpendingclones WHERE fromuser="
										+ "\"" + configresponse_list[1].trim()
										+ "\"");
						Set<String> sessionIdList = getdbHeler(context)
								.getSessionId(configresponse_list[1].trim());
						if (sessionIdList != null && sessionIdList.size() > 0) {
							for (String sessionId : sessionIdList) {
								// getdbHeler(context).deleteAllshares(
								// "DELETE from MMChat WHERE sessionid='"
								// + sessionId + "'");
								DBAccess.getdbHeler().deleteAllshares(
										"delete from chat where groupid='"
												+ configresponse_list[1].trim()
												+ "' and username ='"
												+ CallDispatcher.LoginUser
												+ "'");
								File file = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/" + sessionId);
								if (file.exists()) {
									try {
										file.delete();
									} catch (Exception e) {
										Log.e("imscreen",
												"===> exception "
														+ e.getMessage());
										e.printStackTrace();
									}
								}
								if (WebServiceReferences.Imcollection
										.containsKey(sessionId)) {
									WebServiceReferences.Imcollection
											.remove(sessionId);
								}
								if (message_map
										.containsKey(configresponse_list[1])) {
									message_map.remove(configresponse_list[1]);
								}
								handlerForCall.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										ContactsFragment.getContactAdapter()
												.notifyDataSetChanged();
								
										ExchangesFragment exchanges = ExchangesFragment
												.newInstance(context);
										if (exchanges.adapter != null)
											exchanges.adapter
													.notifyDataSetChanged();
									}
								});

							}
						}
						getdbHeler(context).deleteAllshares(
								"DELETE from component WHERE fromuser=" + "\""
										+ configresponse_list[1].trim()
										+ "\" and owner='"
										+ CallDispatcher.LoginUser + "'");

						handlerForCall.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (SingleInstance.contextTable
										.containsKey("forms")) {
									FormsFragment quickActionFragment = FormsFragment
											.newInstance(context);

									quickActionFragment.populateLists();

								}

								if (WebServiceReferences.contextTable
										.containsKey("formsettings"))
									((FormSettings) WebServiceReferences.contextTable
											.get("formsettings"))
											.populateLists();

								if (WebServiceReferences.contextTable
										.containsKey("clone"))
									(AvatarFragment.newInstance(context))
											.loadConfigs();

								(FilesFragment.newInstance(context))
										.filesListRefresh();
                                SingleInstance.mainContext.notifyUI();
								// if (WebServiceReferences.contextTable
								// .containsKey("imscreen")) {
								// InstantMessageScreen imScreen =
								// (InstantMessageScreen)
								// WebServiceReferences.contextTable
								// .get("imscreen");
								// if (imScreen.buddy != null
								// && imScreen.buddy
								// .equalsIgnoreCase(configresponse_list[1])) {
								// imScreen.clearAll();
								// }
								//
								// }
							}
						});

					}
				} else if (message.getGroupChatSettings() != null) {
					String[] result = (String[]) message.getGroupChatSettings();
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					String[] params = new String[3];
					params[0] = result[0];
					params[1] = CallDispatcher.LoginUser;
					params[2] = result[2];
					WebServiceReferences.webServiceClient
							.getGroupChatSettings(params);

				} else if (message.getFormFieldSettings() != null) {
					String[] result = message.getFormFieldSettings();
					String[] formSettingValue = DBAccess.getdbHeler()
							.getColumnofSync(result[1]);
					Log.i("formfield123",
							"udp signal get form field settings : " + result[0]
									+ " , " + result[1]);
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					if (formSettingValue != null) {
						WebServiceReferences.webServiceClient.Getformsettings(
								formSettingValue[0], CallDispatcher.LoginUser,
								context);
					}
				} else if (message.getfSettingDeleteBean() != null) {
					FormFieldSettingDeleteBean ffDBean = message
							.getfSettingDeleteBean();
					if (!WebServiceReferences.running) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						String url = preferences.getString("url", null);
						String port = preferences.getString("port", null);
						startWebService(url, port);
					}
					if (ffDBean != null) {
						Log.i("formfield123", "udp signal delete formid : "
								+ ffDBean.getFormId());
						ArrayList<String[]> buddiesAccessList = ffDBean
								.getBuddiesList();
						if (buddiesAccessList != null) {
							for (String[] buddyAccess : buddiesAccessList) {
								Log.i("formfield123",
										"udp signal delete formid : "
												+ buddyAccess[0] + " , "
												+ buddyAccess[1]);
								DBAccess.getdbHeler()
										.deleteFormFieldBuddyAccess(
												ffDBean.getFormId(),
												buddyAccess[0], buddyAccess[1]);
							}
						}
					}
				} else {
					final ArrayList<Object> list = message.getList();

					for (final Object lst : list) {
						if (lst instanceof ShareReminder) {
							ShareReminder sr = (ShareReminder) lst;
							if (sr.getStatus().equals("new")) {
								WebServiceReferences.shareRemainderArray
										.add(sr);
							}
							// if (listenerclass instanceof CompleteListView) {
							// CompleteListView complete = (CompleteListView)
							// listenerclass;
							// complete.showShareRemainderRequest();
							// }
							handlerForCall.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									(FilesFragment.newInstance(context))
											.showShareRemainderRequest();
								}
							});

						}
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean isApplicationInBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		Log.d("BACK", "isApplicationSentToBackground method");
		if (!tasks.isEmpty()) {
			Log.d("BACK", "not Empty");
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				Log.d("BACK", "going to return true........");
				return true;
			}
		}

		return false;
	}

	private String missedCallType(String calltype) {
		if (calltype.equalsIgnoreCase("AC")) {
			return "111";
		} else if (calltype.equalsIgnoreCase("AP")) {
			return "112";
		} else if (calltype.equalsIgnoreCase("ABC")) {
			return "113";
		} else if (calltype.equalsIgnoreCase("VC")) {
			return "211";
		} else if (calltype.equalsIgnoreCase("VP")) {
			return "212";
		} else if (calltype.equalsIgnoreCase("VBC")
				|| calltype.equalsIgnoreCase("SS")) {
			return "213";
		}

		return "Missed call";

	}

	private String times() {
		String delegate = "MM/dd/yyyy hh:mm:ss aaa";
		return (String) DateFormat.format(delegate, Calendar.getInstance()
				.getTime());
	}

	public String time() {

		String delegate = "hh:mm aaa";
		return (String) DateFormat.format(delegate, Calendar.getInstance()
				.getTime());
	}

	/**
	 * This will return the list of buddies who are all available in online
	 * state and them details to the PagingBuddySelectionScreen
	 */
	// public ArrayList<Databean> getOnLineBuddiesP() {
	//
	// Set set = WebServiceReferences.buddyList.keySet();
	// ArrayList<Databean> buddies = new ArrayList<Databean>();
	// Log.d("ON", "online buddies " + WebServiceReferences.buddyList.size());
	//
	// Iterator<String> itr = set.iterator();
	// while (itr.hasNext()) {
	// String buddy = itr.next();
	// BuddyInformationBean bean = WebServiceReferences.buddyList
	// .get(buddy);
	//
	// Log.d("ON", "online buddies " + bean.getStatus());
	// Log.d("ON", "name " + bean.getName());
	// // System.out.println("buddy not offline...");
	// if (bean.getStatus().startsWith("Onli")) {
	//
	// if (!bean.getName().equals(LoginUser)) {
	// Databean db = new Databean();
	// db.setname(bean.getName());
	// db.setStatus(bean.getStatus());
	// buddies.add(db);
	// } else {
	// // System.out.println("Buddy  contains");
	// }
	// }
	//
	// }
	//
	// return buddies;
	//
	// }

	public String getCurrentDateTime() {
		try {
			Date curDate = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
			Log.d("Test",
					"Current DateandTime caldispatcher" + sdf.format(curDate));

			return sdf.format(curDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * This method is updated from the WebServiceCallback interface. It will
	 * update the details about subscribe,sign in, sign out, etc..
	 */
	@Override
	public void notifyWebServiceResponse(final Servicebean servicebean) {
		Log.i("calldisp", "notifyWebServicecalResponse Response comes here");
		try {

			switch (servicebean.getServiceMethods()) {
			case SUBSCRIBE:
				Log.i("register", "555555555555555555555");
				if (WebServiceReferences.contextTable
						.containsKey("registration")) {
					Log.i("register", "55");
					((Registration) WebServiceReferences.contextTable
							.get("registration"))
							.notifyRegistrationResponse(servicebean.getObj());
				}

				break;

			case SIGNIN:
				//
				// if (!isSilentLoginCalled) {
				//
				// if (mainbuddylist != null) {
				// mainbuddylist.clear();
				// }
				//
				// if (servicebean.getObj() instanceof WebServiceBean) {
				// // if (commEngine != null)
				// // commEngine.stop();
				//
				// // if (WebServiceReferences.contextTable
				// // .containsKey("loginpage"))
				// // ((LoginPageFragment)
				// // WebServiceReferences.contextTable
				// // .get("loginpage"))
				// // .notifyLoginResponse(((WebServiceBean) servicebean
				// // .getObj()).getText());
				// // LoginUser = null;
				// } else if (servicebean.getObj() instanceof ArrayList) {
				//
				// LoginUser = strTempUser;
				// Password = strTempPassword;
				// isKeepAliveSendAfter6Sec = false;
				// handlerForCall.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// cancelDialog();
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// if (LoginUser != null) {
				// publicipstate = PUBLIC_IP_STATE.PUBLIC_IP_SUCCESS_LOGIN;
				// getpublicIpFromUdpStunHttp();
				// if (getPublicipaddress() == null) {
				// try {
				// logout(true);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }
				// }
				//
				// }
				// }).start();
				//
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				//
				// }
				// }, 2500);
				//
				// handlerForCall.postDelayed(hb_runnable, 6000);
				// handlerForCall.post(new Runnable() {
				// //
				// @Override
				// public void run() {
				// try {
				// if (gpsloc != null) {
				// if (!gpsloc.isStarted)
				// gpsloc.Start();
				// }
				// }
				//
				// catch (Exception e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }
				// //
				// }
				// });
				//
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// // if (commEngine != null)
				// // commEngine.initCodec();
				// }
				// }).start();
				//
				// if (ftpNotifier == null)
				// ftpNotifier = new FTPNotifier();
				//
				// WebServiceReferences.reqbuddyList.clear();
				// WebServiceReferences.buddyList.clear();
				// showBuddies.clear();
				// mainbuddylist.clear();
				// // profileDetails.clear();
				// ArrayList list = (ArrayList) servicebean.getObj();
				// for (int i = 0; i < list.size(); i++) {
				// final Object obj = list.get(i);
				// if (obj instanceof AppVersionUpdateBean) {
				// AppVersionUpdateBean appBean = (AppVersionUpdateBean) list
				// .get(i);
				// if (appBean.getType() == 0) {
				// Toast.makeText(
				// context,
				// "New version is available for download",
				// 1).show();
				// WebServiceReferences.type = appBean
				// .getType();
				// } else if (appBean.getType() == 1) {
				// appLogout(appBean.getMessage(),
				// appBean.getUrl(),
				// appBean.getType(),
				// SipNotificationListener
				// .getCurrentContext());
				// WebServiceReferences.type = appBean
				// .getType();
				// } else if (appBean.getType() == 2) {
				// appLogout(appBean.getMessage(),
				// appBean.getUrl(),
				// appBean.getType(),
				// SipNotificationListener
				// .getCurrentContext());
				// WebServiceReferences.type = appBean
				// .getType();
				//
				// }
				// } else if (obj instanceof BuddyInformationBean) {
				// BuddyInformationBean bib = (BuddyInformationBean) list
				// .get(i);
				// if (bib.getStatus().equalsIgnoreCase("new")) {
				// WebServiceReferences.reqbuddyList.put(
				// bib.getName(), bib);
				// } else if (bib.getStatus().equalsIgnoreCase(
				// "pending")) {
				// showPendingBuddies(bib);
				// WebServiceReferences.buddyList.put(
				// bib.getName(), bib);
				// } else {
				// Log.d("NAME", "name" + bib.getName());
				// WebServiceReferences.buddyList.put(
				// bib.getName(), bib);
				// }
				// }
				//
				// else if (obj instanceof ConnectionBrokerBean) {
				// ConnectionBrokerBean cbb = (ConnectionBrokerBean) list
				// .get(i);
				// Log.e("sig",
				// "Login OK Comes to Array List and ConnectionBrokerBean");
				// setCbserver1(cbb.getCbserver1());
				// setCbserver2(cbb.getCbserver2());
				// Log.e("rou",
				// "cbb.getRouter() IP:" + cbb.getRouter());
				// setRouter(cbb.getRouter());
				// setRelay(cbb.getRelayServer());
				// setcbPort1(cbb.getaa1Port());
				// setcbPort2(cbb.getaa2Port());
				// setFS(cbb.getFS());
				// Log.i("sip", "cbb.getFS() IP:" + cbb.getFS());
				// // if (commEngine != null) {
				// //
				// // if (cbb.getaa1Port() != 0
				// // && cbb.getaa2Port() != 0)
				// // commEngine.setConnectionBrokerDetails(
				// // getCbserver1(), getCbserver2(),
				// // getcbPort1(), getcbPort2());
				// //
				// // String routerDetails[] = getRouter().split(
				// // ":");
				// // String relayDetails[] = getRelay().split(
				// // ":");
				// // commEngine.startSignalingAgent(LoginUser,
				// // routerDetails[0],
				// // Integer.parseInt(routerDetails[1]),
				// // relayDetails[0],
				// // Integer.parseInt(relayDetails[1]));
				// //
				// // }
				//
				// } else if (obj instanceof String[]) {
				// String[] buddy_profile = (String[]) obj;
				// // profileDetails.add(buddy_profile);
				// } else if (obj instanceof ShareReminder) {
				//
				// testhandler.post(new Runnable() {
				// @Override
				// public void run() {
				// ShareReminder sr = (ShareReminder) obj;
				// if (sr.getStatus().equalsIgnoreCase(
				// "new")) {
				// Log.e("share",
				// "getReminderdatetime:"
				// + sr.getReminderdatetime());
				// Log.e("share",
				// "share Id :" + sr.getId());
				// Log.e("share", "getRemindertz:"
				// + sr.getRemindertz());
				// if (sr.getStatus().equals("new")) {
				// WebServiceReferences.shareRemainderArray
				// .add(sr);
				// }
				//
				// }
				// }
				// });
				// }
				// }
				//
				// if (WebServiceReferences.shareRemainderArray.size() > 0) {
				// if (WebServiceReferences.contextTable
				// .containsKey("MAIN")) {
				// CompleteListView complete = (CompleteListView)
				// WebServiceReferences.contextTable
				// .get("MAIN");
				// complete.showShareRemainderRequest();
				// }
				// }
				//
				// /* For getting Profile template info */
				//
				// String modifiedDate = getdbHeler(context)
				// .getModifiedDate(
				// "select profiletimestamp from profiletemplate where profileid=5");
				// if (modifiedDate == null) {
				// modifiedDate = "";
				// } else if (modifiedDate.trim().length() == 0) {
				// modifiedDate = "";
				// }
				// WebServiceReferences.webServiceClient
				// .getProfileTemplate(modifiedDate);
				// scheduleFileDownloader();
				//
				// /* For getting Avatar configs */
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// String dateTime = getdbHeler(context)
				// .getMaxDateofOfflineConfig(
				// CallDispatcher.LoginUser);
				//
				// if (dateTime == null) {
				// dateTime = "\"\"";
				// }
				//
				// String params[] = new String[2];
				// params[0] = CallDispatcher.LoginUser;
				// params[1] = dateTime;
				// if (WebServiceReferences.running) {
				// WebServiceReferences.webServiceClient
				// .GetMyConfigurationDetails(params);
				// }
				// }
				// });
				//
				// // handlerForCall.post(new Runnable() {
				// //
				// // @Override
				// // public void run() {
				// // // TODO Auto-generated method stub
				// // WebServiceReferences.webServiceClient
				// // .getGroupAndMembers(LoginUser, "");
				// // WebServiceReferences.webServiceClient
				// // .getParticipateGroups(LoginUser, "");
				// // }
				// // });
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// if (WebServiceReferences.running) {
				// WebServiceReferences.webServiceClient
				// .getBlockedBuddyList(CallDispatcher.LoginUser);
				//
				// Vector<UtilityBean> utility_list = getdbHeler(
				// context).SelectUtilityRecords(
				// "select * from utility where userid='"
				// + CallDispatcher.LoginUser
				// + "'");
				// // For temp, server will not consider
				// // utilityName, So hardcoded -
				
				// String utilityName = "buysell";
				// WebServiceReferences.webServiceClient
				// .syncUtilityItems(
				// CallDispatcher.LoginUser,
				// utilityName, utility_list);
				// utilityName = "serviceneededprovider";
				// WebServiceReferences.webServiceClient
				// .syncUtilityItems(
				// CallDispatcher.LoginUser,
				// utilityName, utility_list);
				//
				// }
				// }
				// }).start();
				// if (WebServiceReferences.type != 2) {
				// if (WebServiceReferences.contextTable
				// .containsKey("buddyView1"))
				// ((buddyView1) WebServiceReferences.contextTable
				// .get("buddyView1"))
				// .notifyLoginResponse("Succesfully logged in");
				// else if (WebServiceReferences.contextTable
				// .containsKey("MAIN"))
				// ((CompleteListView) WebServiceReferences.contextTable
				// .get("MAIN"))
				// .notifyLoginResponse("Succesfully logged in");
				// }
				// Message messagex = new Message();
				// Bundle bundle = new Bundle();
				// bundle.putString("buddy", "hangupfullscreen");
				// messagex.obj = bundle;
				// testhandler.sendMessage(messagex);
				//
				// handlerForCall.postDelayed(new Runnable() {
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// if (latConfigure != 0 && longConfigure != 0
				// && serviceType != 5) {
				//
				// if (LoginUser != null) {
				// if (isLocationServiceEnabled)
				// doLocationMadeService();
				// }
				//
				// }
				//
				// }
				// }, 2500);
				//
				// }
				//
				// } else {
				// // Processing silent Login....
				// if (mainbuddylist != null) {
				// mainbuddylist.clear();
				// }
				//
				// if (servicebean.getObj() instanceof WebServiceBean) {
				// if (WebServiceReferences.contextTable
				// .containsKey("buddyView1"))
				// ((buddyView1) WebServiceReferences.contextTable
				// .get("buddyView1"))
				// .notifyLoginResponse(((WebServiceBean) servicebean
				// .getObj()).getText());
				//
				// } else if (servicebean.getObj() instanceof ArrayList) {
				// if (WebServiceReferences.type != 2) {
				// WebServiceReferences.buddyList.clear();
				// mainbuddylist.clear();
				// WebServiceReferences.reqbuddyList.clear();
				// myStatus = "1";
				// changeMyOnlineStatus();
				// isConnected = true;
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// LoginTaskSilent login = new LoginTaskSilent();
				// login.execute(servicebean, servicebean,
				// servicebean);
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				//
				// }
				// });
				// }
				// }
				//
				// }

				break;

			case SIGNOUT:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						cancelDialog();
						if (gpsloc != null) {
							gpsloc.Stop();
						}

						dissableUserSettings();
						if (servicebean.getObj() instanceof WebServiceBean) {
							WebServiceBean server_response = (WebServiceBean) servicebean
									.getObj();
							Context cntxt;
							destroySIPStack();
							myStatus = "0";
							onlineStatus = "";
							// if (WebServiceReferences.contextTable
							// .containsKey("buddiesList"))
							// cntxt = WebServiceReferences.contextTable
							// .get("buddiesList");
							// else
							// cntxt = context;
							cntxt = SingleInstance.mainContext;

							profilePicturepath = "";
							WebServiceReferences.missedcallCount.clear();

							if (server_response.getText() != null
									&& server_response.getText()
											.equalsIgnoreCase(
													"you are logged out")) {

								Log.e("lgg", "logout......");

								if (isIncomingCall) {
									isIncomingCall = false;
								}

								if (ftpNotifier != null)
									ftpNotifier.shutDowntaskmanager();
								ftpNotifier = null;
								cancelDownloadSchedule();
								stopRingTone();

								// if (WebServiceReferences.buddyList != null) {
								// if (WebServiceReferences.buddyList.size() >
								// 0) {
								// WebServiceReferences.buddyList.clear();
								// }
								// }
								// if (mainbuddylist != null)
								// mainbuddylist.clear();
								// adapterToShow.notifyDataSetChanged();
								//
								// if (pendinglist != null)
								// pendinglist.clear();

								// pendingToShow.notifyDataSetChanged();
								Log.e("lgg",
										"going to clear tempbuddy list......");

								// if (WebServiceReferences.tempBuddyList !=
								// null) {
								// if (WebServiceReferences.tempBuddyList
								// .size() > 0) {
								// WebServiceReferences.tempBuddyList
								// .clear();
								// }
								// }
								Log.d("QAA", "Size==>"
										+ CallDispatcher.message_map.size());

								Log.e("lgg", "going to call signout......");
								Log.e("lgg", "logout......"
										+ isKeepAliveSendAfter6Sec);

								if (!isKeepAliveSendAfter6Sec) {
									handlerForCall.removeCallbacks(hb_runnable);
									isKeepAliveSendAfter6Sec = false;
								}
								WebServiceReferences.webServiceClient.stop();
								WebServiceReferences.running = false;

								if (AppMainActivity.commEngine != null) {
									AppMainActivity.commEngine.stop();
								}
								AppMainActivity.commEngine = null;

								LoginUser = null;
								ipaddress = null;
								// mainbuddylist.clear();
								members.clear();
								CallDispatcher.reminderArrives = false;
								isKeepAliveSendAfter6Sec = false;
								conferenceMembers.clear();
								bitmap_table.clear();

								if (appMainActivity.HBT != null) {
									appMainActivity.stopHeartBeatTimer();
								}

								if (!WebServiceReferences.contextTable
										.containsKey("buddyView1")) {
									Intent logiIntent = new Intent(cntxt,
											buddyView1.class);
									cntxt.startActivity(logiIntent);
								}
								if (WebServiceReferences.contextTable
										.containsKey("formactivity")) {

									WebServiceReferences.contextTable
											.remove("formactivity");

								}

//								Toast.makeText(cntxt,
//										server_response.getText(),
//										Toast.LENGTH_LONG).show();

							} else {
								Toast.makeText(cntxt,
										server_response.getText(),
										Toast.LENGTH_LONG).show();
							}
						}
					}
				});

				break;
			case HEARTBEAT:

				if (servicebean.getKey().equals("1")) {

					try {
						// WebServiceReferences.tempBuddyList.clear();
						// WebServiceReferences.tempBuddyList
						// .putAll(ContactsFragment.buddyList);
						// WebServiceReferences.buddyList.clear();
						WebServiceReferences.reqbuddyList.clear();
						isConnected = true;

						if (appMainActivity.HBT != null) {

							appMainActivity.stopHeartBeatTimer();

							new Thread(new Runnable() {

								@Override
								public void run() {

									publicipstate = PUBLIC_IP_STATE.PUBLIC_IP_SILENT_SIGNIN;
									getpublicIpFromUdpStunHttp();
								}
							}).start();

						}

						if (servicebean.getObj() instanceof ArrayList) {

							if (CallDispatcher.LoginUser != null) {
								Log.e("Bud",
										"Comes Here To UPdate the State of the Buddy:"
												+ CallDispatcher.LoginUser);
							}
							ArrayList list = (ArrayList) servicebean.getObj();
							Log.i("b", "list size inside HEARTBEAT responce"
									+ list.size());
							WebServiceReferences.reqbuddyList.clear();

							for (int i = 0; i < list.size(); i++) {

								final Object obj = list.get(i);
								if (obj instanceof BuddyInformationBean) {
									BuddyInformationBean bib = (BuddyInformationBean) list
											.get(i);

									for (BuddyInformationBean biBean : ContactsFragment
											.getBuddyList()) {
										if (!biBean.isTitle()) {
											if (biBean.getName()
													.equalsIgnoreCase(
															bib.getName())) {
												biBean = bib;
												biBean.setStatus(ContactsFragment
														.getStatusString(bib
																.getStatus()));
											}
										}
										// else {
										// ContactsFragment.buddyList.add(bib);
										// }

									}
//									handlerForCall.post(new Runnable() {
//
//										@Override
//										public void run() {
//											try {
//
//												ContactsFragment
//														.getInstance(
//																SingleInstance.mainContext)
//														.SortList();
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//
//										}
//									});

									/*
									 * 
									 * if (bib.getType().equals("N")) {
									 * 
									 * WebServiceReferences.buddyList.put(bib
									 * .getName().trim(), bib);
									 * 
									 * if (WebServiceReferences.tempBuddyList
									 * .containsKey(bib.getName())) {
									 * BuddyInformationBean oldBib =
									 * WebServiceReferences.tempBuddyList
									 * .get(bib.getName()); Log.d("UMBX",
									 * "contains GetKey One......................****"
									 * + oldBib.isIslocationBasedServiceDone());
									 * if (oldBib
									 * .isIslocationBasedServiceDone()) {
									 * bib.setIslocationBasedServiceDone(true);
									 * 
									 * bib.setLatitude(oldBib .getLatitude());
									 * bib.setLongitude(oldBib .getLongitude());
									 * 
									 * }
									 * 
									 * }
									 * 
									 * } else if (bib.getType().equals("R")) {
									 * 
									 * WebServiceReferences.reqbuddyList.put(
									 * bib.getName(), bib); } else if
									 * (bib.getStatus() != null) { if
									 * (bib.getStatus().equalsIgnoreCase(
									 * "pending"))
									 * WebServiceReferences.buddyList.put(
									 * bib.getName(), bib); }
									 */

								} else if (obj instanceof FtpDetails) {
									handlerForCall.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub
											setFTPUsername(((FtpDetails) obj)
													.getFtpusername());
											setFTPpassword(((FtpDetails) obj)
													.getFtppasssword());
											Editor editor = sharedPreferences
													.edit();
											editor.putString("ftpusername",
													((FtpDetails) obj)
															.getFtpusername());
											editor.putString("ftppassword",
													((FtpDetails) obj)
															.getFtppasssword());
											editor.commit();
											editor = null;
											if (context instanceof AppMainActivity) {
												if (!((AppMainActivity) context).isFormLoaded) {

													((AppMainActivity) context)
															.process();

												}

											}
											Log.i("FORMOPT", "Inside Process");

										}
									});
								}

							}
							// getBuddyList();

							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("buddy", "hangupfullscreen");
							message.obj = bundle;
							try {
								testhandler.sendMessage(message);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile) {
							try {
								AppReference.logger.error(
										"KA123 key1 webservice response", e);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						e.printStackTrace();
					}

				} else if (servicebean.getKey().equals("0")) {

					if (appMainActivity.HBT != null) {

						try {
							setPublicipaddress(null);
							appMainActivity.stopHeartBeatTimer();
							new Thread(new Runnable() {

								@Override
								public void run() {

									publicipstate = PUBLIC_IP_STATE.PUBLIC_IP_SILENT_SIGNIN;
									getpublicIpFromUdpStunHttp();
								}
							}).start();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							if (AppReference.isWriteInFile) {
								try {
									AppReference.logger
											.error("KA123 key0 webservice response",
													e);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							e.printStackTrace();
						}

					}

				}

				break;

			case DELETEPEOPLE:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof WebServiceBean) {
							final WebServiceBean bean = (WebServiceBean) servicebean
									.getObj();
							ContactsFragment.getInstance(
									SingleInstance.mainContext)
									.notifyBuddyDeleted(bean.getText());
						
						}

					}
				});
				break;

			case ADDPEOPLE:
				//
				// Log.d("thread", "##############" + servicebean.getObj());
				// if (servicebean.getObj() instanceof WebServiceBean) {
				// testhandler.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// cancelDialog();
				// String text = ((WebServiceBean) servicebean
				// .getObj()).getText();
				// Toast.makeText(context, text, 3000).show();
				// }
				// });
				//
				// } else if (servicebean.getObj() instanceof ArrayList) {
				//
				// Log.d("thread", "Addpeople conditin satisfied arraylist");
				// //
				// ArrayList<BuddyInformationBean> list =
				// (ArrayList<BuddyInformationBean>) servicebean
				// .getObj();
				//
				// for (int i = 0; i < list.size(); i++) {
				//
				// final Object obj = list.get(i);
				// if (obj instanceof BuddyInformationBean) {
				//
				// final BuddyInformationBean bib = (BuddyInformationBean) obj;
				//
				// bib.setStatus("pending");
				// if (!WebServiceReferences.buddyList.containsKey(bib
				// .getName())) {
				// WebServiceReferences.buddyList.put(
				// bib.getName(), bib);
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// adapterToShow.notifyDataSetChanged();
				// }
				// });
				//
				// } else {
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// if (WebServiceReferences.buddyList
				// .get(bib.getName()).getStatus()
				// .equalsIgnoreCase("online")) {
				// // pendingToShow.deleteUser(bib
				// // .getName());
				// // pendingToShow
				// // .notifyDataSetChanged();
				// }
				// }
				// });
				//
				// }
				//
				// // for (Object name : showBuddies) {
				// // if (((String) name).equals(bib.getName())) {
				// // showBuddies.remove(name);
				// // break;
				// // }
				// // }
				// if (WebServiceReferences.reqbuddyList
				// .containsKey(bib.getName().trim())) {
				// WebServiceReferences.reqbuddyList.remove(bib
				// .getName().trim());
				// }
				//
				// if (WebServiceReferences.contextTable
				// .containsKey("buddiesList"))
				// ContactsFragment.getInstance(
				// SingleInstance.mainContext)
				// .notifyBuddyrequestDeleted(
				// bib.getName());
				//
				// } else if (obj instanceof WebServiceBean) {
				// WebServiceBean service_bean = (WebServiceBean) obj;
				// Toast.makeText(context, service_bean.getText(),
				// 3000).show();
				// }
				// }
				//
				// getBuddyList();
				// Toast.makeText(context, "Invitation Sent Successfully",
				// 3000).show();
				//
				// }

				break;

			case FINDPEOPLE:

				if (servicebean.getObj() instanceof WebServiceBean) {
					if (WebServiceReferences.contextTable
							.containsKey("findpeoples")) {

//						FindPeople bList = (FindPeople) WebServiceReferences.contextTable
//                                .get("findpeoples");
//                        bList.notifyFindPeople(null, false);

					}

				} else if (servicebean.getObj() instanceof ArrayList) {
					Log.d("findpeople", "findpeopleelseif");
					Log.d("find", "on calldisp findpeopleelseif");
					ArrayList list = (ArrayList) servicebean.getObj();

					HashMap<String, FindPeopleBean> fPeople = new HashMap<String, FindPeopleBean>();
					for (int i = 0; i < list.size(); i++) {
						FindPeopleBean bib = (FindPeopleBean) list.get(i);
						findPeopleList.put(bib.getName(), bib);
						fPeople.put(bib.getName(), bib);
					}

					if (WebServiceReferences.contextTable
							.containsKey("findpeoples")) {
//						Log.d("find", "on calldisp findpeoples.......");
//						FindPeople bList = (FindPeople) WebServiceReferences.contextTable
//								.get("findpeoples");
//						bList.notifyPeople(getPeopleList(), fPeople, true);
//						bList.notifyFindPeople(fPeople, true);

					}
				}

				findPeopleList.clear();

				break;

			case ACCEPT:

				// if (servicebean.getObj() instanceof ArrayList) {
				// ArrayList<BuddyInformationBean> arrBib =
				// (ArrayList<BuddyInformationBean>) servicebean
				// .getObj();
				// final BuddyInformationBean bib = arrBib.get(0);
				// if (bib != null) {
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// Toast.makeText(
				// context,
				// bib.getName()
				// + " added succesfully",
				// Toast.LENGTH_LONG).show();
				// if (WebServiceReferences.reqbuddyList
				// .containsKey(bib.getName()))
				// WebServiceReferences.reqbuddyList
				// .remove(bib.getName());
				//
				// if (bib.getStatus().equals("0")) {
				// bib.setStatus("Offline");
				// } else if (bib.getStatus().equals("1")) {
				// bib.setStatus("Online");
				//
				// } else if (bib.getStatus().equals("2")) {
				//
				// bib.setStatus("Airport");
				// } else if (bib.getStatus().equals("3")) {
				// bib.setStatus("Away");
				//
				// } else if (bib.getStatus().equals("4")) {
				// bib.setStatus("Stealth");
				// }
				// WebServiceReferences.buddyList.put(
				// bib.getName(), bib);
				// getBuddyList();
				// CallDispatcher.adapterToShow
				// .notifyDataSetChanged();
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				//
				// }
				// });
				// }
				//
				// }

				break;
			case REJECT:

				// if (servicebean.getObj() instanceof WebServiceBean) {
				// final WebServiceBean bean = (WebServiceBean) servicebean
				// .getObj();
				//
				// if (bean != null) {
				//
				// if (bean.getResult().equals("1")) {
				//
				// WebServiceReferences.reqbuddyList.remove(bean
				// .getText());
				// CallDispatcher.showBuddies.remove(bean.getText());
				//
				// handlerForCall.post(new Runnable() {
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// // TODO Auto-generated method stub
				// Toast.makeText(context,
				// "You Rejected " + bean.getText(),
				// Toast.LENGTH_LONG).show();
				// }
				// });
				//
				// }
				// }
				// }
				break;

			case GETPROFILETEMPLATE:
				// handlerForCall.post(new Runnable() {
				// @Override
				// public void run() {
				// try {
				// // TODO Auto-generated method stub
				// if (servicebean.getObj() instanceof ArrayList) {
				// ArrayList<Object> response = (ArrayList<Object>) servicebean
				// .getObj();
				// String profileId = null;
				// for (Object object : response) {
				// if (object instanceof String)
				// Log.d("Profile",
				// "GETPROFILETEMPLATE response ---->"
				// + object);
				//
				// if (object instanceof ArrayList) {
				// ArrayList<ProfileTemplateBean> bean_list =
				// (ArrayList<ProfileTemplateBean>) object;
				// for (ProfileTemplateBean pBean : bean_list) {
				// ContentValues cv = new ContentValues();
				// cv.put("profileid",
				// pBean.getProfileId());
				// cv.put("profilename",
				// pBean.getProfileName());
				// cv.put("profiletimestamp",
				// pBean.getModifiedDate());
				//
				// profileId = pBean.getProfileId();
				//
				// if (!getdbHeler(context)
				// .isRecordExists(
				// "select * from profiletemplate where profileid="
				// + pBean.getProfileId())) {
				// int id = getdbHeler(context)
				// .insertProfileTemplate(
				// cv);
				// Log.i("clone", "---->id" + id);
				// } else {
				// cv.remove("profileid");
				// getdbHeler(context)
				// .updateProfileTemplate(
				// cv,
				// "profileid="
				// + pBean.getProfileId());
				// }
				//
				// }
				// }
				// if (object instanceof Vector) {
				// Vector<FieldTemplateBean> field_list =
				// (Vector<FieldTemplateBean>) object;
				// for (FieldTemplateBean bean : field_list) {
				// ContentValues cv = new ContentValues();
				// if (profileId != null) {
				// cv.put("profileid", profileId);
				// }
				// cv.put("groupname",
				// bean.getGroupName());
				// cv.put("fieldid", bean.getFieldId());
				// cv.put("fieldname",
				// bean.getFieldName());
				// cv.put("fieldtype",
				// bean.getFieldType());
				// cv.put("createddate",
				// bean.getCreatedDate());
				// cv.put("modifieddate",
				// bean.getModifiedDate());
				// if (!getdbHeler(context)
				// .isRecordExists(
				// "select * from fieldtemplate where fieldid="
				// + bean.getFieldId())) {
				// int id = getdbHeler(context)
				// .insertFieldTemplate(cv);
				// Log.i("clone", "---->id" + id);
				// } else {
				// cv.remove("fieldid");
				// getdbHeler(context)
				// .updateFieldTemplate(
				// cv,
				// "fieldid="
				// + bean.getFieldId());
				// }
				// ContentValues cv2 = new ContentValues();
				// cv2.put("groupid",
				// bean.getGroupId());
				// cv2.put("groupname",
				// bean.getGroupName());
				// if obj(!getdbHeler(context)
				// .isRecordExists(
				// "select * from profilegroup where groupid="
				// + bean.getGroupId()
				// + " and groupname='"
				// + bean.getGroupName()
				// + "'")) {
				// int id = getdbHeler(context)
				// .insertProfileGroup(cv2);
				// Log.i("clone", "---->id" + id);
				// }
				// }
				// }
				//
				// }
				//
				// if (appMainActivity.profileDetails != null) {
				// for (String[] buddyprofile_info :
				// appMainActivity.profileDetails) {
				// if (buddyprofile_info != null) {
				// BuddyInformationBean bib = null;
				// for (BuddyInformationBean temp : ContactsFragment
				// .getBuddyList()) {
				// if (temp.getName()
				// .equalsIgnoreCase(
				// buddyprofile_info[0])) {
				// bib = temp;
				// break;
				// }
				// }
				//
				// if (bib != null
				// && !bib.getStatus()
				// .equalsIgnoreCase(
				// "pending")) {
				// if (buddyprofile_info.length > 0) {
				// if (buddyprofile_info[2]
				// .length() == 0) {
				// Vector<String> multimediaFields = getdbHeler(
				// context)
				// .getMultimediaFields(
				// "SELECT fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
				// + buddyprofile_info[0]
				// + "'");
				// if (multimediaFields != null
				// && multimediaFields
				// .size() > 0) {
				// for (String fieldName : multimediaFields) {
				// File file = new File(
				// Environment
				// .getExternalStorageDirectory()
				// .getAbsoluteFile()
				// + "/COMMedia/"
				// + fieldName);
				// if (file.exists())
				// file.delete();
				// }
				// }
				// getdbHeler(context)
				// .deleteFieldValues(
				// "DELETE from profilefieldvalues WHERE userid="
				// + "\""
				// + buddyprofile_info[0]
				// .toString()
				// .trim()
				// + "\"");
				//
				// } else {
				// String modified_date = getdbHeler(
				// context)
				// .getModifiedDate(
				// "select max(modifieddate) from profilefieldvalues where userid='"
				// + buddyprofile_info[0]
				// + "'");
				// if (modified_date == null)
				// modified_date = "";
				// else if (modified_date
				// .trim()
				// .length() == 0)
				// modified_date = "";
				//
				// if (buddyprofile_info[2] != null) {
				// if (!buddyprofile_info[2]
				// .equals(modified_date)) {
				// String[] profileDetails = new String[3];
				// profileDetails[0] = buddyprofile_info[0];
				// profileDetails[1] = "5";
				// profileDetails[2] = modified_date;
				// WebServiceReferences.webServiceClient
				// .getStandardProfilefieldvalues(profileDetails);
				// }
				// }
				// }
				// }
				// }
				// }
				// }
				//
				// appMainActivity.profileDetails.clear();
				//
				// }
				//
				// if (SingleInstance.instanceTable
				// .containsKey("createprofile")) {
				// cancelDialog();
			
				//
				// CreateProfileFragment pFragment = (CreateProfileFragment)
				// SingleInstance.instanceTable
				// .get("createprofile");
				// pFragment.getProfileFields();
				// if (pFragment.profileFieldList.size() > 0) {
				// pFragment.clearAllViews();
				// for (FieldTemplateBean fieldtemplate :
				// pFragment.profileFieldList) {
				// pFragment.inflateFields(0,
				// fieldtemplate);
				// }
				// }
				// }
				//
				// } else {
				// if (servicebean.getObj() instanceof WebServiceBean) {
				// if (((WebServiceBean) servicebean.getObj())
				// .getText() != null
				// && ((WebServiceBean) servicebean
				// .getObj())
				// .getText()
				// .equalsIgnoreCase(
				// "No updates in profile template")) {
				//
				// if (appMainActivity.profileDetails != null) {
				// for (String[] buddyprofile_info :
				// appMainActivity.profileDetails) {
				// if (buddyprofile_info != null) {
				// BuddyInformationBean bib = null;
				// for (BuddyInformationBean temp : ContactsFragment
				// .getBuddyList()) {
				// if (temp.getName()
				// .equalsIgnoreCase(
				// buddyprofile_info[0])) {
				// bib = temp;
				// break;
				// }
				// }
				// if (bib != null
				// && !bib.getStatus()
				// .equalsIgnoreCase(
				// "pending")) {
				// if (buddyprofile_info.length > 0) {
				// if (buddyprofile_info[2]
				// .length() == 0) {
				// Vector<String> multimediaFields = getdbHeler(
				// context)
				// .getMultimediaFields(
				// "SELECT fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
				// + buddyprofile_info[0]
				// + "'");
				// if (multimediaFields != null
				// && multimediaFields
				// .size() > 0) {
				// for (String fieldName : multimediaFields) {
				// File file = new File(
				// Environment
				// .getExternalStorageDirectory()
				// .getAbsoluteFile()
				// + "/COMMedia/"
				// + fieldName);
				// if (file.exists())
				// file.delete();
				// }
				// }
				// getdbHeler(
				// context)
				// .deleteFieldValues(
				// "DELETE from profilefieldvalues WHERE userid="
				// + "\""
				// + buddyprofile_info[0]
				// .toString()
				// .trim()
				// + "\"");
				//
				// } else {
				// String modified_date = getdbHeler(
				// context)
				// .getModifiedDate(
				// "select max(modifieddate) from profilefieldvalues where userid='"
				// + buddyprofile_info[0]
				// + "'");
				// if (modified_date == null)
				// modified_date = "";
				// else if (modified_date
				// .trim()
				// .length() == 0)
				// modified_date = "";
				//
				// if (buddyprofile_info[2] != null) {
				// if (!buddyprofile_info[2]
				// .equals(modified_date)) {
				// String[] profileDetails = new String[3];
				// profileDetails[0] = buddyprofile_info[0];
				// profileDetails[1] = "5";
				// profileDetails[2] = modified_date;
				// WebServiceReferences.webServiceClient
				// .getStandardProfilefieldvalues(profileDetails);
				// }
				// }
				// }
				// }
				// }
				// }
				// }
				//
				// appMainActivity.profileDetails
				// .clear();
				// }
				//
				// }
				// }
				// Log.d("Profile",
				// "GETPROFILETEMPLATE result--->"
				// + ((WebServiceBean) servicebean
				// .getObj()).getText());
				// }
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// }
				// });
				break;

			case GETPROFILEFIELDVALUES:

				// if (servicebean.getObj() instanceof ArrayList) {
				// ArrayList<Object> resut = (ArrayList<Object>) servicebean
				// .getObj();
				// String[] profile_info = new String[4];
				// profile_info = (String[]) resut.get(0);
				// String profileOwner = profile_info[1];
				// if (resut.size() > 1) {
				// ArrayList<FieldTemplateBean> filed_values =
				// (ArrayList<FieldTemplateBean>) resut
				// .get(1);
				// for (FieldTemplateBean fieldTemplateBean : filed_values) {
				//
				// ContentValues cv = new ContentValues();
				// if (fieldTemplateBean.getFiledvalue() != null
				// && !fieldTemplateBean.getFiledvalue()
				// .equalsIgnoreCase("null")
				// && fieldTemplateBean.getFieldId() != null) {
				//
				// cv.put("fieldid",
				// fieldTemplateBean.getFieldId());
				// cv.put("modifieddate", fieldTemplateBean
				// .getField_modifieddate());
				// cv.put("createddate", fieldTemplateBean
				// .getField_createddate());
				// cv.put("userid", profileOwner);
				//
				// cv.put("fieldvalue",
				// fieldTemplateBean.getFiledvalue());
				//
				// if (multimediaFieldsValues.size() > 0)
				// multimediaFieldsValues.clear();
				//
				// multimediaFieldsValues = getdbHeler(context)
				// .getMultimediaFieldValues(
				// "SELECT fieldid,fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
				// + profileOwner + "'");
				// Log.i("profile", "====> get profile multimedia");
				// if (multimediaFieldsValues != null
				// && multimediaFieldsValues.size() > 0) {
				// Log.i("profile",
				// "====> inside get profile multimedia");
				// if (multimediaFieldsValues
				// .containsKey(fieldTemplateBean
				// .getFieldId())) {
				// String fieldValue = multimediaFieldsValues
				// .get(fieldTemplateBean
				// .getFieldId());
				// if (!fieldValue
				// .equalsIgnoreCase(fieldTemplateBean
				// .getFiledvalue())) {
				// Log.i("profile",
				// "====> inside get profile multimedia file exists");
				// File file = new File(
				// Environment
				// .getExternalStorageDirectory()
				// .getAbsolutePath()
				// + "/COMMedia/"
				// + fieldValue);
				// if (file.exists()) {
				// Log.i("profile",
				// "====> inside get profile multimedia file delete");
				// file.delete();
				// }
				// }
				// }
				// }
				//
				// String profile_multimedia = Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + fieldTemplateBean.getFiledvalue();
				// File msg_file = new File(profile_multimedia);
				// String f_type = getdbHeler(context)
				// .getFieldType(
				// fieldTemplateBean.getFieldId());
				//
				// if (msg_file.exists())
				// cv.put("status", 2);
				// else {
				//
				// if (f_type != null
				// && f_type.equalsIgnoreCase("photo")
				// || f_type.equalsIgnoreCase("video")
				// || f_type.equalsIgnoreCase("audio")
				// || f_type
				// .equalsIgnoreCase("multimedia"))
				// cv.put("status", 0);
				// else
				// cv.put("status", 2);
				//
				// }
				// boolean isUpdated = false;
				//
				// if (!getdbHeler(context).isRecordExists(
				// "select * from profilefieldvalues where fieldid="
				// + fieldTemplateBean
				// .getFieldId()
				// + " and userid='"
				// + profileOwner + "'")) {
				//
				// getdbHeler(context)
				// .insertProfileFieldValues(cv);
				//
				// } else {
				// cv.remove("fieldid");
				// getdbHeler(context)
				// .updateProfileFieldValues(
				// cv,
				// "fieldid="
				// + fieldTemplateBean
				// .getFieldId()
				// + " and userid='"
				// + profileOwner
				// + "'");
				//
				// }
				//
				// if (fieldTemplateBean.getFieldId().equals("3"))
				// isUpdated = true;
				//
			
				//
				// if (!msg_file.exists()) {
				//
				// if (f_type != null) {
				// if (f_type.equalsIgnoreCase("photo")
				// || f_type
				// .equalsIgnoreCase("video")
				// || f_type
				// .equalsIgnoreCase("audio")
				// || f_type
				// .equalsIgnoreCase("multimedia")) {
				// Log.d("profile",
				// "Field value is :"
				// + fieldTemplateBean
				// .getFiledvalue());
				// if (fieldTemplateBean.getFieldId()
				// .equals("3")) {
				//
				// for (BuddyInformationBean temp : ContactsFragment
				// .getBuddyList()) {
				// if (temp.getName()
				// .equalsIgnoreCase(
				// fieldTemplateBean
				// .getUsername())) {
				//
				// temp.setProfile_picpath(fieldTemplateBean
				// .getFiledvalue());
				// break;
				// }
				// }
				//
				// }
				// downloadOfflineresponse(
				// fieldTemplateBean
				// .getFiledvalue(),
				// fieldTemplateBean
				// .getFieldId()
				// + ","
				// + profileOwner
				// + "," + isUpdated,
				// "profile field", null);
				// }
				//
				// }
				// } else {
				// // comparewithStateanddistance();
				// profilePictureDownloaded();
				// }
				//
				// msg_file = null;
				// }
				//
				// }
				// }
				//
				// } else if (servicebean.getObj() instanceof WebServiceBean) {
				// Log.d("Profile", "Error message is --->"
				// + ((WebServiceBean) servicebean.getObj()).getText());
				// }

				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// cancelDialog();
				// if (SingleInstance.contextTable
				// .containsKey("chatactivity")) {
				// ((ChatActivity) SingleInstance.contextTable
				// .get("chatactivity")).notifyViewProfile();
				// } else if (WebServiceReferences.contextTable
				// .containsKey("buddiesList")) {
				// (ContactsFragment.getInstance(context))
				// .notifyViewProfile(true);
				// }
				// }
				// });

				break;

			case SHAREBYPROFILE:
				if (WebServiceReferences.contextTable
						.containsKey("sharebyprofile")) {
					ShareByProfile frmrec_creator = (ShareByProfile) WebServiceReferences.contextTable
							.get("sharebyprofile");
					frmrec_creator.notifyWebServiceResponse(servicebean
							.getObj());
				}

				break;

			case SETSTANDARDPROFILEFIELDVALUES:
				handlerForCall.post(new Runnable() {
					@Override
					public void run() {
						try {
							// TODO Auto-generated method stub
							cancelDialog();
							if (servicebean.getObj() instanceof ArrayList) {
								ArrayList<Object> response = (ArrayList<Object>) servicebean
										.getObj();
								String createdDate = "";
								String modifiedDate = "";
								for (Object object : response) {
									if (object instanceof ArrayList) {
										ArrayList<ProfileTemplateBean> bean_list = (ArrayList<ProfileTemplateBean>) object;
										for (ProfileTemplateBean pBean : bean_list) {
											ContentValues cv = new ContentValues();
											cv.put("profileid",
													pBean.getProfileId());
											cv.put("profilename", "MyProfile");
											cv.put("profiletimestamp",
													pBean.getModifiedDate());
											createdDate = pBean
													.getCreatedDate();
											modifiedDate = pBean
													.getModifiedDate();
											if (!getdbHeler(context)
													.isRecordExists(
															"select * from profiletemplate where profileid='"
																	+ pBean.getProfileId()
																	+ "'")) {
												int id = getdbHeler(context)
														.insertProfileTemplate(
																cv);

											} else {
												cv.remove("profileid");
												getdbHeler(context)
														.updateProfileTemplate(
																cv,
																"profileid='"
																		+ pBean.getProfileId()
																		+ "'");
											}

										}
									}
									if (object instanceof Vector) {
										Vector<FieldTemplateBean> field_list = (Vector<FieldTemplateBean>) object;
										for (FieldTemplateBean fieldTemplateBean : field_list) {
											if (fieldTemplateBean.getStatus()
													.equalsIgnoreCase("1")) {

												fieldTemplateBean
														.setCreatedDate(createdDate);
												fieldTemplateBean
														.setModifiedDate(modifiedDate);

												getdbHeler(context)
														.saveOrUpdateProfileField(
																fieldTemplateBean);

											}

										}
										
									}

								}

							} else if (servicebean.getObj() instanceof WebServiceBean) {

								if (WebServiceReferences.contextTable
										.containsKey("buscard"))
									((BusCard) WebServiceReferences.contextTable
											.get("buscard"))
											.showAlert(((WebServiceBean) servicebean
													.getObj()).getText());

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				break;
			case DELETEPROFILE:
				if (WebServiceReferences.contextTable
						.containsKey("buscard")) {
					BusCard bCard = (BusCard) WebServiceReferences.contextTable
							.get("buscard");
					bCard.notifyWebServiceDelete(servicebean.getObj());
				}
				// SingleInstance.mainContext.setProfilePic();

				if (SingleInstance.contextTable.containsKey("MAIN")) {
					AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
							.get("MAIN");
					appMainActivity.setProfilePic();

				}
				break;
			case FORGOTPASSWORD:
				Log.d("result", "" + servicebean.getServiceMethods().toString());

				final WebServiceBean webserviceBean = (WebServiceBean) servicebean
						.getObj();

				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							if (WebServiceReferences.contextTable
									.containsKey("forgotPassword")) {
								forgotPassword forget = (forgotPassword) WebServiceReferences.contextTable
										.get("forgotPassword");
								forget.getResponseFromServer(
										webserviceBean.getText());

							}
							if (WebServiceReferences.contextTable
									.containsKey("getnewpassword")) {
								getnewpassword gPassword = (getnewpassword) WebServiceReferences.contextTable
										.get("getnewpassword");
//								gPassword
//										.notifyWebserviceResponse(webserviceBean);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				break;

			case SECRETANSWER:
				final WebServiceBean webServiceBean = (WebServiceBean) servicebean
						.getObj();

				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							if (WebServiceReferences.contextTable
									.containsKey("getnewpassword")) {
								getnewpassword newp = (getnewpassword) WebServiceReferences.contextTable
										.get("getnewpassword");
//								newp.getSecretQuestionResponse(
//										webServiceBean.getText(),
//										webServiceBean.getResult());
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				});

				break;

			case SHARE_REMAINDER:

				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof ShareSendBean) {

							if (!SingleInstance.orderToastShow) {
								showToast(SipNotificationListener
										.getCurrentContext(),
										((ShareSendBean) servicebean.getObj())
												.getText());
							}

						}
					}
				});

				break;

			case GETSETUTILITY:
				handlerForCall.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						cancelDialog();
						if (WebServiceReferences.contextTable
								.containsKey("utilityseller")) {
							((UtilitySeller) WebServiceReferences.contextTable
									.get("utilityseller"))
									.notifywebserviceReponse(servicebean
											.getObj());
						} else if (WebServiceReferences.contextTable
								.containsKey("utilitybuyer")) {
							((UtilityBuyer) WebServiceReferences.contextTable
									.get("utilitybuyer"))
									.notifywebserviceReponse(servicebean
											.getObj());
						} else if (WebServiceReferences.contextTable
								.containsKey("utilityprovider")) {
							((UtilityServiceProvider) WebServiceReferences.contextTable
									.get("utilityprovider"))
									.notifywebserviceReponse(servicebean
											.getObj());
						} else if (WebServiceReferences.contextTable
								.containsKey("utilityneeder")) {
							((UtilityServiceNeeder) WebServiceReferences.contextTable
									.get("utilityneeder"))
									.notifywebserviceReponse(servicebean
											.getObj());
						}
					}
				});
				break;

			case BLOCKUNBLOCKBUDDY:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof String[]) {

							if (WebServiceReferences.contextTable
									.containsKey("block_list")) {
								((Blocked_list) WebServiceReferences.contextTable
										.get("block_list"))
										.notifyBuddyblockunblock(servicebean
												.getObj());
							}
							if (WebServiceReferences.contextTable
									.containsKey("utilityseller")) {
								((UtilitySeller) WebServiceReferences.contextTable
										.get("utilityseller"))
										.notifyBuddyblockunblock(servicebean
												.getObj());
							} else if (WebServiceReferences.contextTable
									.containsKey("utilitybuyer")) {
								((UtilityBuyer) WebServiceReferences.contextTable
										.get("utilitybuyer"))
										.notifyBuddyblockunblock(servicebean
												.getObj());
							} else if (WebServiceReferences.contextTable
									.containsKey("utilityprovider")) {
								((UtilityServiceProvider) WebServiceReferences.contextTable
										.get("utilityprovider"))
										.notifyBuddyblockunblock(servicebean
												.getObj());
							} else if (WebServiceReferences.contextTable
									.containsKey("utilityneeder")) {
								((UtilityServiceNeeder) WebServiceReferences.contextTable
										.get("utilityneeder"))
										.notifyBuddyblockunblock(servicebean
												.getObj());
							}
						}
					}
				});
				break;

			case OFFLINECONFIGURATION:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						(AvatarFragment.newInstance(context))
								.notifyWebresponse(servicebean.getObj());
					}
				});

				break;

			case OFFLINECALLRESPONSE:
				notifyOfflineCallResponse(servicebean.getObj());
				break;
			case GETMYCONFIGURATIONDETAILS:
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// if (servicebean.getObj() instanceof ArrayList) {
				// ArrayList<Object> getmyconfig_result = (ArrayList<Object>)
				// servicebean
				// .getObj();
				// for (Object object : getmyconfig_result) {
				// if (object instanceof String[]) {
				// String[] config_info = (String[]) object;
				// if (config_info[1] != null) {
				// if (config_info[1].trim().length() > 0) {
				// ArrayList<OfflineRequestConfigBean> config_list = getdbHeler(
				// context)
				// .getOfflineSettingDetails(
				// "where Id NOT IN("
				// + config_info[1]
				// + ")");
				// if (config_list != null) {
				// for (OfflineRequestConfigBean offlineRequestConfigBean :
				// config_list) {
				// if (offlineRequestConfigBean != null) {
				// if (offlineRequestConfigBean
				// .getMessagetype() != null) {
				// if (!offlineRequestConfigBean
				// .getMessagetype()
				// .equalsIgnoreCase(
				// "call forwarding")
				// && !offlineRequestConfigBean
				// .getMessagetype()
				// .equalsIgnoreCase(
				// "conference call")
				// && !offlineRequestConfigBean
				// .getMessagetype()
				// .equalsIgnoreCase(
				// "call forward to gsm")) {
				// String path = offlineRequestConfigBean
				// .getMessage();
				// File config_path = new File(
				// path);
				// if (config_path
				// .exists())
				// config_path
				// .delete();
				// }
				// }
				// getdbHeler(context)
				// .deleteOfflineCallSettingDetails(
				// offlineRequestConfigBean
				// .getId());
				// }
				// }
				// }
				// } else {
				// if (getmyconfig_result.get(0) instanceof String) {
				// String text = (String) getmyconfig_result
				// .get(0);
				// if (text.equalsIgnoreCase("No updates available")) {
				// getdbHeler(context)
				// .deleteOfflineCallSettingDetails(
				// null);
				// }
				// }
				//
				// }
				// }
				// } else if (object instanceof ArrayList) {
				// ArrayList<OfflineRequestConfigBean> bean_list =
				// (ArrayList<OfflineRequestConfigBean>) object;
				// for (OfflineRequestConfigBean result : bean_list) {
				// ContentValues cv = new ContentValues();
				// cv.put("Id", result.getId());
				// cv.put("userid",
				// CallDispatcher.LoginUser);
				// if (result.getBuddyId().equals(
				// CallDispatcher.LoginUser))
				// cv.put("buddyid", "default");
				// else
				// cv.put("buddyid",
				// result.getBuddyId());
				//
				// cv.put("message_title",
				// result.getMessageTitle());
				// cv.put("message_type",
				// result.getMessagetype());
				// cv.put("message", result.getMessage());
				//
				// cv.put("when_action", result.getWhen());
				//
				// cv.put("status", 0);
				//
				// String path = Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + result.getMessage();
				// File fle = new File(path);
				//
				// if (!fle.exists())
				// downloadOfflineresponse(
				// result.getMessage(),
				// result.getId(),
				// "offline settings", null);
				//
				// cv.put("response_type",
				// result.getResponseType());
				//
				// if (result.getUrl() == null)
				// cv.put("url", "''");
				// else
				// cv.put("url", result.getUrl());
				//
				// if (result.getRecordTime() == null)
				// cv.put("record_time",
				// result.getRecordTime());
				// else
				// cv.put("record_time",
				// result.getRecordTime());
				//
				// int row_id = getdbHeler(context)
				// .insertOfflineCallSettingDetails(
				// cv);
				// Log.d("Avataar",
				// "getmyconfigdetails insert response--->"
				// + row_id);
				// }
				// } else
				// Log.d("Avataar",
				// "Get My configuration Details response --->"
				// + object);
				//
				// }
				// } else {
				// Log.d("Avataar",
				// "Get My configuration Details response --->"
				// + ((WebServiceBean) servicebean
				// .getObj()).getText());
				// }
				// }
				// });
				// break;

			case RESPONSEFORCALLCONFIGURATION:
				// if (WebServiceReferences.contextTable.containsKey("clone"))
				// AvatarFragment.newInstance(context)
				// .NotifyresponsecallConfig(servicebean.getObj());
				// if (WebServiceReferences.contextTable
				// .containsKey("answeringmachine"))
				// ((AnsweringMachineActivity) WebServiceReferences.contextTable
				// .get("answeringmachine"))
				// .NotifyresponsecallConfig(servicebean.getObj());
				if (WebServiceReferences.contextTable.containsKey("avatarset")) {
					AvatarActivity.newInstance(context)
							.NotifyresponsecallConfig(servicebean.getObj());
				}
				if (WebServiceReferences.contextTable
						.containsKey("answeringmachine")) {
					((AnsweringMachineActivity) WebServiceReferences.contextTable
							.get("answeringmachine"))
							.NotifyresponsecallConfig(servicebean.getObj());
				}

				break;
			case GETCONFIGURATIONRESPONSEDETAILS:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof ArrayList) {
							ArrayList<Object> response = (ArrayList<Object>) servicebean
									.getObj();
							String buddyname = null;
							for (Object object : response) {

								if (object instanceof OfflineRequestConfigBean) {
									OfflineRequestConfigBean bean = (OfflineRequestConfigBean) object;
									buddyname = bean.getBuddyId();
									ContentValues cv = new ContentValues();
									cv.put("id", bean.getId());
									cv.put("userid", CallDispatcher.LoginUser);
									cv.put("buddyid", bean.getBuddyId());
									cv.put("configid", bean.getConfigId());
									cv.put("messagetitle",
											bean.getMessageTitle());
									cv.put("responsetype",
											bean.getResponseType());
									cv.put("response", bean.getResponse());
									cv.put("receivedtime", getCurrentDateTime());
									cv.put("status", 0);
									if (!getdbHeler(context).isRecordExists(
											"select * from offlinecallresponsedetails where id="
													+ bean.getId())) {
										int id = getdbHeler(context)
												.insertOfflineCallResponseDetails(
														cv);
										Log.i("clone", "---->id" + id);
									} else {
										cv.remove("id");
										getdbHeler(context)
												.updateOfflineCallResponseDetails(
														cv,
														"id=" + bean.getId());
									}

									String[] params = new String[3];
									params[0] = CallDispatcher.LoginUser;
									params[1] = bean.getBuddyId();
									params[2] = bean.getConfigId();
									WebServiceReferences.webServiceClient
											.DeleteMyResponseDetail(params);
									cancelDialog();

									String msg_path = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/" + bean.getMessage();
									File msg_file = new File(msg_path);
									if (!msg_file.exists())
										downloadOfflineresponse(
												bean.getMessage(),
												bean.getId(),
												"answering machine", null);

									String res_path = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/" + bean.getResponse();
									File res_file = new File(res_path);
									if (!res_file.exists()) {
										ContentValues cv_update = new ContentValues();
										cv_update.put("status", 0);
										getdbHeler(context)
												.updateOfflineCallResponseDetails(
														cv_update,
														"id=" + bean.getId());
										cv = null;
										downloadOfflineresponse(
												bean.getResponse(),
												bean.getId(),
												"offline response", null);
									}
								}

							}

							if (WebServiceReferences.contextTable
									.containsKey("clone")) {
								AvatarFragment avatarFragment = AvatarFragment
										.newInstance(context);
								avatarFragment.populateAllresponses();
							}
							if (WebServiceReferences.contextTable
									.containsKey("avatarset")) {
								AvatarActivity avatarActivity = AvatarActivity
										.newInstance(context);
								avatarActivity.populateAllresponses();
							}
							if (WebServiceReferences.contextTable
									.containsKey("answeringmachine"))
								((AnsweringMachineActivity) WebServiceReferences.contextTable
										.get("answeringmachine"))
										.populatePendingclones();
							if (buddyname != null) {
								showToast(context,
										"Received avatar response from "
												+ buddyname);
							}
						} else {
							// if (AppReference.sipnotifier != null) {
							// ShowError("GetConfigurationResponDetails",
							// ((WebServiceBean) servicebean.getObj())
							// .getText());
							// }
						}
					}
				});
				break;

			case GETBLOCKBUDDYLIST:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if (servicebean.getObj() instanceof ArrayList) {
							ArrayList<Object> blocked_list = (ArrayList<Object>) servicebean
									.getObj();
							blocked_buddies.clear();
							if (blocked_list != null) {
								for (Object object : blocked_list) {
									if (object instanceof String)
										blocked_buddies.add((String) object);
								}
							}

						}

					}
				});
				break;
			case SYNCUTILITYITEMS:
				// handlerForCall.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// if (servicebean.getObj() instanceof Syncutilitybean) {
				// Syncutilitybean sync_bean = (Syncutilitybean) servicebean
				// .getObj();
				// if (sync_bean.getMissing_utility() != null) {
				// for (UtilityBean bean : sync_bean
				// .getMissing_utility()) {
				// ContentValues cv = new ContentValues();
				// cv.put("userid", bean.getUsername());
				// cv.put("org_name", bean.getNameororg());
				// cv.put("product_name",
				// bean.getProduct_name());
				// cv.put("quantity", bean.getQty());
				// cv.put("price", bean.getPrice());
				// cv.put("video_file",
				// bean.getVideofilename());
				// cv.put("img_file", bean.getImag_filename());
				// cv.put("voice", bean.getAudiofilename());
				// cv.put("location", bean.getLocation());
				// cv.put("address", bean.getAddress());
				// cv.put("country", bean.getCountry());
				// cv.put("state", bean.getState());
				// cv.put("city", bean.getCityordist());
				// cv.put("pin", bean.getPin());
				// cv.put("email", bean.getEmail());
				// cv.put("c_no", bean.getC_no());
				// cv.put("entry_mode", bean.getMode());
				// cv.put("utility_name",
				// bean.getUtility_name());
				// cv.put("posted_date", bean.getPosted_date());
				// cv.put("id", bean.getId());
				// if (!getdbHeler(context).isRecordExists(
				// "select * from utility where id="
				// + bean.getId()))
				// getdbHeler(context).insertUtility(cv);
				// else
				// getdbHeler(context).UpdateUtility(cv,
				// bean.getPosted_date());
				// if (bean.getAudiofilename() != null) {
				// if (bean.getAudiofilename().trim()
				// .length() > 0) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + bean.getAudiofilename());
				// if (!fle.exists())
				// downloadOfflineresponse(
				// bean.getAudiofilename(),
				// null, "utilities", null);
				//
				// }
				// }
				// if (bean.getVideofilename() != null) {
				// if (bean.getVideofilename().trim()
				// .length() > 0) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + bean.getVideofilename());
				// if (!fle.exists())
				// downloadOfflineresponse(
				// bean.getVideofilename(),
				// null, "utilities", null);
				//
				// }
				// }
				// if (bean.getImag_filename() != null) {
				// if (bean.getImag_filename().trim()
				// .length() > 0) {
				// String[] file_names = bean
				// .getImag_filename().split(
				// ",");
				// for (String names : file_names) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + names);
				// if (!fle.exists())
				// downloadOfflineresponse(
				// names, null,
				// "utilities", null);
				// }
				//
				// }
				// }
				//
				// }
				//
				// }
				// if (sync_bean.getUpdated_utility() != null) {
				// for (UtilityBean bean : sync_bean
				// .getUpdated_utility()) {
				//
				// ContentValues cv = new ContentValues();
				// cv.put("userid", bean.getUsername());
				// cv.put("org_name", bean.getNameororg());
				// cv.put("product_name",
				// bean.getProduct_name());
				// cv.put("quantity", bean.getQty());
				// cv.put("price", bean.getPrice());
				// cv.put("video_file",
				// bean.getVideofilename());
				// cv.put("img_file", bean.getImag_filename());
				// cv.put("voice", bean.getAudiofilename());
				// cv.put("location", bean.getLocation());
				// cv.put("address", bean.getAddress());
				// cv.put("country", bean.getCountry());
				// cv.put("state", bean.getState());
				// cv.put("city", bean.getCityordist());
				// cv.put("pin", bean.getPin());
				// cv.put("email", bean.getEmail());
				// cv.put("c_no", bean.getC_no());
				// cv.put("entry_mode", bean.getMode());
				// cv.put("utility_name",
				// bean.getUtility_name());
				// cv.put("posted_date", bean.getPosted_date());
				// cv.put("id", bean.getId());
				// if (!getdbHeler(context).isRecordExists(
				// "select * from utility where id="
				// + bean.getId()))
				// getdbHeler(context).insertUtility(cv);
				// else
				// getdbHeler(context).UpdatesyncUtility(
				// cv, bean.getId());
				//
				// if (bean.getAudiofilename() != null) {
				// if (bean.getAudiofilename().trim()
				// .length() > 0) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + bean.getAudiofilename());
				// if (!fle.exists())
				// downloadOfflineresponse(
				// bean.getAudiofilename(),
				// null, "utilities", null);
				//
				// }
				// }
				// if (bean.getVideofilename() != null) {
				// if (bean.getVideofilename().trim()
				// .length() > 0) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + bean.getVideofilename());
				// if (!fle.exists())
				// downloadOfflineresponse(
				// bean.getVideofilename(),
				// null, "utilities", null);
				//
				// }
				// }
				// if (bean.getImag_filename() != null) {
				// if (bean.getImag_filename().trim()
				// .length() > 0) {
				// String[] file_names = bean
				// .getImag_filename().split(
				// ",");
				// for (String names : file_names) {
				// File fle = new File(
				// Environment
				// .getExternalStorageDirectory()
				// + "/COMMedia/"
				// + names);
				// if (!fle.exists())
				// downloadOfflineresponse(
				// names, null,
				// "utilities", null);
				//
				// }
				//
				// }
				// }
				//
				// }
				// }
				// if (sync_bean.getDeleted_utility() != null) {
				// for (UtilityBean utilityBean : sync_bean
				// .getDeleted_utility()) {
				// getdbHeler(context).deleteUtility(
				// Integer.toString(utilityBean
				// .getId()));
				// }
				// }
				// }
				// }
				// });
				break;
			case ADDFORMRECORDS:
				

				break;
			case GETFORMSTEMPLATE:
				Log.i("FORMSOPTMZ",
						"--------------GETFORMSTEMPLATE--------------");

				break;
			case GETFORMATTRIBUTE:

				break;

			case ACCESSFORM:

				if (WebServiceReferences.contextTable
						.containsKey("formpermission")) {

					AccessAndSync frmrec_creator = (AccessAndSync) WebServiceReferences.contextTable
							.get("formpermission");
					frmrec_creator.notifyWebServiceResponse(servicebean
							.getObj());
				} else if (WebServiceReferences.contextTable
						.containsKey("formpermissionviewer")) {

					FormPermissionViewer frmrec_creator = (FormPermissionViewer) WebServiceReferences.contextTable
							.get("formpermissionviewer");
					frmrec_creator.notifyWebServiceResponseupdate(servicebean
							.getObj());
				} else if (WebServiceReferences.contextTable
						.containsKey("SecContatct")) {

					ContactLogics frm_creator = (ContactLogics) WebServiceReferences.contextTable
							.get("SecContatct");
					frm_creator.notifyWebServiceResponseAccess(servicebean
							.getObj());
				}

				break;

			case DELETEACCESSFORM:
				if (WebServiceReferences.contextTable
						.containsKey("formpermissionviewer")) {
					FormPermissionViewer frmrec_creator = (FormPermissionViewer) WebServiceReferences.contextTable
							.get("formpermissionviewer");
					Log.i("formsettings", "" + servicebean.getObj().toString());

					if (servicebean.getObj() instanceof String[]) {
						String[] response = (String[]) servicebean.getObj();

						String fsid = response[0];

						String del_row = "delete from formsettings where settingid='"
								+ fsid + "'";
						getdbHeler(SipNotificationListener.getCurrentContext())
								.ExecuteQuery(del_row);
						frmrec_creator.refreshList();
						cancelDialog();

					} else if (servicebean.getObj() instanceof WebServiceBean) {
						showAlert("Response", servicebean.getObj().toString());
					}

				} else {
				}

				break;

			case UPDATEFORMRECORDS:
				if (WebServiceReferences.contextTable
						.containsKey("frmreccreator")) {
					FormRecordsCreators frmrec_creator = (FormRecordsCreators) WebServiceReferences.contextTable
							.get("frmreccreator");
					frmrec_creator.notifyWebServiceResponse1(servicebean
							.getObj());
				} else {
					handleDataForms(servicebean.getObj());

				}

				break;

			case CREATEFORM:

				Log.i("thread", "response to createforms");
				if (WebServiceReferences.contextTable.containsKey("formdesc")) {
					FormDescription frm_creator = (FormDescription) WebServiceReferences.contextTable
							.get("formdesc");
					frm_creator.notifyWebServiceResponse(servicebean.getObj());
				} else if (WebServiceReferences.contextTable
						.containsKey("SecContatct")) {
					ContactLogics frm_creator = (ContactLogics) WebServiceReferences.contextTable
							.get("SecContatct");
					frm_creator.notifyWebServiceResponse(servicebean.getObj());
				} else {
					Log.i("thread", "response to createforms elseeee....");
				}
				break;
			case GETFORMRECORDS:

				handlerForCall.post(new Runnable() {

					@Override
					public void run() {

						if (SingleInstance.contextTable.containsKey("forms")) {
							FormsFragment quickActionFragment = FormsFragment
									.newInstance(context);

							quickActionFragment
									.notifyWebServiceResponse(servicebean
											.getObj());
						} else {
							handleGetDataForms(servicebean.getObj());
						}
					}
				});

				break;

			case GETFORMSETTINGS:

				break;

			case DELETEFORM:

				if (SingleInstance.contextTable.containsKey("forms")) {
					FormsFragment quickActionFragment = FormsFragment
							.newInstance(context);

					quickActionFragment.notifyFormDeletionRespose(servicebean
							.getObj());
				} else if (WebServiceReferences.contextTable
						.containsKey("appsview")) {
					AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
							.get("appsview");
					frm_activity.refreshIcon();
				}

				else {
					handleDeleteForms(servicebean.getObj());
				}

				break;

			case DELETEFORMRECORD:
				if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
					FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
							.get("frmviewer");
					frm_creator.notifyFormDeleteRecords(servicebean.getObj());
				} else {
					handleDeleteFormsRecords(servicebean.getObj());
				}
				break;
			case CHANGEPASSWORD:
				final WebServiceBean sbean = (WebServiceBean) servicebean
						.getObj();
				Log.d("chk", "RESULT:" + sbean.getText());
				// handlerForCall.post(new Runnable() {
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// try {
				// if (WebServiceReferences.contextTable
				// .containsKey("ChangePassword")) {
				//
				// Toast.makeText(context, sbean.getText(),
				// Toast.LENGTH_SHORT).show();
				//
				// ChangePassword change = (ChangePassword)
				// WebServiceReferences.contextTable
				// .get("ChangePassword");
				//
				// change.Close(sbean.getText());
				//
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				//
				// }
				// });

			case SETPERMISSION:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (WebServiceReferences.contextTable
								.containsKey("permissionsactivity"))
							((PermissionsActivity) WebServiceReferences.contextTable
									.get("permissionsactivity"))
									.notifySetPermissionResponse(servicebean
											.getObj());
					}
				});
				break;

			case GETALLPERMISSION:
				handlerForCall.post(new Runnable() {
					public void run() {
						if (servicebean.getObj() instanceof ArrayList)
							notifyGetallPermission((ArrayList<PermissionBean>) servicebean
									.getObj());
					}
				});
				break;
			case SAVEMYLOCATION:
				if (servicebean.getObj() instanceof WebServiceBean) {

					testhandler.post(new Runnable() {

						@Override
						public void run() {

							if (call_heartbeat) {
								call_heartbeat = false;
								KeepAliveBean aliveBean = getKeepAliveBean();
								aliveBean.setKey("1");

								if (LoginUser != null) {

									WebServiceReferences.webServiceClient
											.heartBeat(aliveBean);
								}
							}
						}
					});

				}

				break;
			case DELETEALLSHARES:
				if (WebServiceReferences.contextTable
						.containsKey("buddiesList"))
					ContactsFragment.getInstance(context)
							.notifyDeleteallshareResponse(servicebean.getObj());
				else
					notifyDeleteallshareResponse(servicebean.getObj());
				break;

			case CREATEGROUP:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (WebServiceReferences.contextTable
								.containsKey("creategroup")) {
							((GroupActivity) WebServiceReferences.contextTable
									.get("creategroup"))
									.notifyCreateGroup(servicebean.getObj());
						}
						if (WebServiceReferences.contextTable
								.containsKey("exchanges")) {
							ExchangesFragment exchanges = ExchangesFragment
									.newInstance(context);
							exchanges.notifyGroupList();
						}
					}
				});
				break;
			// case EDITGROUP:
			// handlerForCall.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// if (WebServiceReferences.contextTable
			// .containsKey("creategroup")) {
			// ((GroupActivity) WebServiceReferences.contextTable
			// .get("creategroup"))
			// .notifyEditGroup(servicebean.getObj());
			// }
			// if (WebServiceReferences.contextTable
			// .containsKey("exchanges")) {
			// ((ExchangesActivity) WebServiceReferences.contextTable
			// .get("exchanges")).notifyGroupList(false);
			// }
			// }
			// });
			// break;
			case DELETEGROUP:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (WebServiceReferences.contextTable
								.containsKey("creategroup")) {
							((GroupActivity) WebServiceReferences.contextTable
									.get("creategroup"))
									.notifyDeleteGroup(servicebean.getObj());
						} else if (WebServiceReferences.contextTable
								.containsKey("buddiesList")) {
							ContactsFragment.getInstance(
									SingleInstance.mainContext)
									.notifyDeleteGroup(servicebean.getObj());
						}
						if (WebServiceReferences.contextTable
								.containsKey("exchanges")) {
							ExchangesFragment excFragment = ExchangesFragment
									.newInstance(context);
							excFragment.notifyGroupList();
						}
					}
				});
				break;
			// case GROUPMEMBERSADD:
			// handlerForCall.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// if (WebServiceReferences.contextTable
			// .containsKey("creategroup")) {
			// ((GroupActivity) WebServiceReferences.contextTable
			// .get("creategroup"))
			// .notifyAddGroupMembers(servicebean.getObj());
			// }
			// }
			// });
			// break;
			// case GROUPMEMBERSDELETE:
			// handlerForCall.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// if (WebServiceReferences.contextTable
			// .containsKey("creategroup")) {
			// ((GroupActivity) WebServiceReferences.contextTable
			// .get("creategroup"))
			// .notifyDeleteGroupMembers(servicebean
			// .getObj());
			// }
			// }
			// });
			// break;
			case LEAVEGROUP:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (WebServiceReferences.contextTable
								.containsKey("buddiesList")) {
							(ContactsFragment.getInstance(context))
									.notifyDeleteGroup(servicebean.getObj());
						}
						if (WebServiceReferences.contextTable
								.containsKey("creategroup")) {
							((GroupActivity) WebServiceReferences.contextTable
									.get("creategroup"))
									.notifyDeleteGroup(servicebean.getObj());
						}
						if (WebServiceReferences.contextTable
								.containsKey("viewgroup")) {
							((ViewGroups) WebServiceReferences.contextTable
									.get("viewgroup"))
									.notifyDeleteGroup(servicebean.getObj());

						}

					}
				});
				break;
			case GETGROUPANDMEMBERS:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof Vector) {
							@SuppressWarnings("unchecked")
							Vector<GroupBean> gBeanList = (Vector<GroupBean>) servicebean
									.getObj();
							for (GroupBean groupBean : gBeanList) {
								groupBean.setUserName(LoginUser);
								groupBean.setOwnerName(LoginUser);
								getdbHeler(context)
										.saveOrUpdateGroup(groupBean);
								getdbHeler(context).insertorUpdateGroupMembers(
										groupBean);

							}

							if (WebServiceReferences.contextTable
									.containsKey("exchanges")) {
								ExchangesFragment exchangesFragment = ExchangesFragment
										.newInstance(context);
								exchangesFragment.notifyGroupList();
							}
						}
					}
				});
				break;
			case GETPARTICIPATEGROUPS:
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (servicebean.getObj() instanceof Vector) {
							@SuppressWarnings("unchecked")
							Vector<GroupBean> gBeanList = (Vector<GroupBean>) servicebean
									.getObj();
							for (GroupBean groupBean : gBeanList) {
								groupBean.setUserName(LoginUser);
								groupBean.setActiveGroupMembers(groupBean
										.getGroupMembers());
								getdbHeler(context)
										.saveOrUpdateGroup(groupBean);
								getdbHeler(context).insertorUpdateGroupMembers(
										groupBean);
							}

							if (WebServiceReferences.contextTable
									.containsKey("exchanges")) {
								ExchangesFragment exchangesFragment = ExchangesFragment
										.newInstance(context);
								exchangesFragment.notifyGroupList();
							}
						}
					}
				});
				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("welcome", "exception" + e.toString());
		}
	}

	public void profilePictureDownloaded() {
		handlerForCall.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// comparewithStateanddistance();
				// adapterToShow.notifyDataSetChanged();
			}
		});
	}

	public void notifyFileDownloaded(final String info, final String req_from,
			final boolean download_status, final String filename) {
		testhandler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (req_from.equalsIgnoreCase("profile field")) {
						if (info != null) {
							String[] details = info.split(",");
							if (details[0] != null && details[1] != null) {
								ContentValues cv = new ContentValues();
								if (download_status)
									cv.put("status", 2);
								else
									cv.put("status", 0);

								DBAccess.getdbHeler(context)
										.updateProfileFieldValues(
												cv,
												"fieldid=" + details[0]
														+ " and userid='"
														+ details[1] + "'");
								if (details[0].equals("2")
										&& details[1]
												.equalsIgnoreCase(LoginUser)) {
									SingleInstance.mainContext.setProfilePic();
									if (SingleInstance.contextTable
											.containsKey("settings")) {
										SettingsFragment settingContext = SettingsFragment
												.newInstance(context);
										settingContext
												.notifyProfilePictureDownloaded();
									}
									ContactsFragment contactsContext = ContactsFragment
											.getInstance(SingleInstance.mainContext);
									contactsContext
											.notifyProfilePictureDownloaded();

								}
								if (SingleInstance.instanceTable
										.containsKey("createprofile")) {
									(CreateProfileFragment.newInstance(context))
											.notifyFileDownloaded(details[0],
													filename, details[1]);
								}
								if (SingleInstance.instanceTable
										.containsKey("viewprofile")) {
									(ViewProfileFragment.newInstance(context))
											.notifyfileDownloaded(details[0],
													details[1]);
								}

							}
						}

					} else if (req_from.equalsIgnoreCase("files")) {

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void downloadOfflineresponse(String path, String id, String from,
			Object obj) {
		try {
			Log.d("FTP", "Came to downloadofflinecall Response----->" + id);
			AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			FTPBean ftpBean = new FTPBean();
			if (CallDispatcher.LoginUser != null) {
				if (from.equalsIgnoreCase("profile field")) {
					if (id != null) {
						String[] details = id.split(",");
						if (details[0] != null && details[1] != null) {
							ContentValues cv = new ContentValues();
							cv.put("status", 1);
							DBAccess.getdbHeler(context)
									.updateProfileFieldValues(
											cv,
											"fieldid=" + details[0]
													+ " and userid='"
													+ details[1] + "'");
						}
					}
					ftpBean.setReq_object(id);
				} else if (from.equalsIgnoreCase("files")) {
					if (obj != null) {
						if (obj instanceof ShareReminder) {

							ftpBean.setShare((ShareReminder) obj);
							ftpBean.setReq_object((ShareReminder) obj);

						}
					}
				} else if (from.equalsIgnoreCase("answering machine")) {
					Log.i("avatar123", "inside avatar download 2");
					ContentValues cv = new ContentValues();
					cv.put("status", 1);
					DBAccess.getdbHeler(context)
							.updateOfflineCallPendingClones(cv, "id=" + id);
					cv = null;
					ftpBean.setReq_object(id);
				} else if (from.equalsIgnoreCase("offline response")) {
					Log.i("avatar_test", "downloadOfflineresponse");
					ContentValues cv = new ContentValues();
					cv.put("status", 1);
					DBAccess.getdbHeler(context)
							.updateOfflineCallResponseDetails(cv, "id=" + id);
					cv = null;
					ftpBean.setReq_object(id);
				}

				if (path != null && path.trim().length() > 0
						&& !path.equalsIgnoreCase("null")) {
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					if (obj instanceof ShareReminder) {
						ftpBean.setFtp_username(((ShareReminder) obj).getFrom());
						ftpBean.setFtp_password(((ShareReminder) obj)
								.getFtpPassword());
					} else {
						ftpBean.setFtp_username("ftpadmin");
						ftpBean.setFtp_password("ftppassword");
					}
					Log.i("avatar123", "inside avatar download 2 filename : "
							+ path);
					ftpBean.setFile_path(path);
					ftpBean.setOperation_type(2);
					ftpBean.setRequest_from(from);
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FTPNotifier getFtpNotifier() {
		return ftpNotifier;
	}

	public boolean checkSettings() {
		boolean returnValue = true;
		try {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String url = preferences.getString("url", null);

			String loginIP = url.substring(url.indexOf("://") + 3);
			Log.d("logip", "login ip" + loginIP);
			loginIP = loginIP.substring(0, loginIP.indexOf(":"));
			Log.d("logip", "login ip" + loginIP);
			loginIP = loginIP.trim();
			String urlPort = url.substring(url.indexOf("://") + 3);

			urlPort = urlPort.substring(urlPort.indexOf(":") + 1);

			urlPort = urlPort.substring(0, urlPort.indexOf("/"));

			if (urlPort.length() < 1 || loginIP.length() < 8) {
				return false;
			}

		} catch (Exception e) {
			returnValue = false;
			Log.d("error", "" + e);
		}

		return returnValue;
	}

	/**
	 * This method is updated by the WebServiceCallback interface when the
	 * connection or other problem occurred
	 */
	int count = 0;

	@Override
	public void notifyError(final String errorMsg,
			EnumWebServiceMethods methodType) {
		// TODO Auto-generated method stub
		switch (methodType) {
		case SUBSCRIBE:
			if (WebServiceReferences.contextTable.containsKey("buddyView1"))
				((buddyView1) WebServiceReferences.contextTable
						.get("buddyView1"))
						.notifyRegistrationResponse(errorMsg);
			break;
		case SIGNIN:
			if (WebServiceReferences.contextTable.containsKey("buddyView1"))
				((buddyView1) WebServiceReferences.contextTable
						.get("buddyView1")).notifyLoginResponse(errorMsg);
			break;
		case SIGNOUT:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});

			break;
		case HEARTBEAT:

			break;

		case GETPROFILETEMPLATE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case GETPROFILEFIELDVALUES:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case ACCEPT:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case REJECT:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case ADDPEOPLE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case DELETEPEOPLE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case OFFLINECALLRESPONSE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case OFFLINECONFIGURATION:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case DELETEMYRESPONSEDETAILS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case SETSTANDARDPROFILEFIELDVALUES:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case GETSETUTILITY:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case SYNCUTILITYITEMS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case DELETEACCESSFORM:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case ADDFORMRECORDS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case GETFORMSTEMPLATE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case GETFORMATTRIBUTE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;

		case ACCESSFORM:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case DELETEFORMRECORD:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case DELETEFORM:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case CREATEFORM:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						cancelDialog();
						showToast(context, errorMsg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			break;
		case UPDATEFORMRECORDS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case GETFORMSETTINGS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case GETFORMRECORDS:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case DELETEPROFILE:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		case CREATEGROUP:
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancelDialog();
					showToast(context, errorMsg);
				}
			});
			break;
		default:
			break;
		}
	}

	/**
	 * To hang up the specified user's call
	 * 
	 * @param buddyName
	 *            - User name to hang up call
	 * @param calltype
	 *            - type of the call
	 */
	public void hangupAP(String buddyName, String calltype) {
		Log.d("MAP", "hangupcall on calldisp");
		SignalingBean sb1 = CallDispatcher.sb;

		if (sb1 != null) {
			Log.d("MAP", "hangupcall on calldisp notnull");
			sb1.setFrom(LoginUser);
			sb1.setTo(buddyName);
			sb1.setType("3");
			sb1.setCallType(calltype);
			AppMainActivity.commEngine.hangupCall(sb1);
		}
	}

	/**
	 * List of user names return by the engine for the respective search key
	 */
	public String[] getPeopleList() {
		String strArray[] = new String[findPeopleList.size()];
		try {
			Set<String> set = findPeopleList.keySet();

			int i = 0;
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {

				strArray[i] = iterator.next();
				i++;

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strArray;
	}

	/**
	 * To add the buddy into the conference call
	 * 
	 * @param selectedBuddy
	 *            - whom to add into the conference
	 * @param calltype
	 *            - type of the call
	 */
	public void callconfernce(String selectedBuddy, String calltype) {

		Log.e("test", "In callconfernce SessionId:" + sb.getSessionid());

		BuddyInformationBean bib = null;
		for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
			if (!temp.isTitle()) {
				if (temp.getName().equalsIgnoreCase(selectedBuddy)) {
					bib = temp;
					break;
				}
			}
		}
		// bib = WebServiceReferences.buddyList.get(selectedBuddy);
		SignalingBean sbConf = new SignalingBean();
		sbConf.setFrom(LoginUser);
		sbConf.setTo(selectedBuddy);
		sbConf.setType("0");
		sbConf.setSessionid(sb.getSessionid());
		sbConf.setCallType(calltype);
		sbConf.setToSignalPort(bib.getSignalingPort());
		sbConf.setResult("0");
		sbConf.setTopublicip(bib.getExternalipaddress());
		sbConf.setTolocalip(bib.getLocalipaddress());
		sbConf.setToSignalPort(bib.getSignalingPort());

		if (calltype.equals("VC")) {
			CallDispatcher.hsAddedBuddyNameFromConferenceCall.put(
					selectedBuddy, sbConf);
		} else if (calltype.equals("AC")) {
			CallDispatcher.hsAddedBuddyNameFromConferenceCall.put(
					selectedBuddy, sbConf);
		}
		AppMainActivity.commEngine.makeConferenceCall(sbConf);
	}

	public ArrayList<String> calculateNearestLocations(String name) {
		Log.i("thread", "......... came to loc calculation NAME:" + name);
		ArrayList<String> buddy_names = new ArrayList<String>();
		BuddyInformationBean bean = null;
		for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
			if (!temp.isTitle()) {
				if (temp.getName().equalsIgnoreCase(name)) {
					bean = temp;
					break;
				}
			}
		}

		Log.i("thread", "......... buddy lat" + bean.getLatitude());
		Log.i("thread", "......... buddy long" + bean.getLongitude());
		if (bean != null && bean.getLatitude() != null
				&& bean.getLongitude() != null) {
			if (bean.getLatitude().trim().length() != 0
					&& bean.getLongitude().trim().length() != 0) {
				Double ownlat = Double.parseDouble(bean.getLatitude());
				Double ownlong = Double.parseDouble(bean.getLongitude());
				String[] online_buddys = getOnlineBuddys();
				ArrayList<Double> dist_list = new ArrayList<Double>();
				HashMap<Double, String> hb_list = new HashMap<Double, String>();

				for (int i = 0; i < online_buddys.length; i++) {
					if (!online_buddys[i].toString().trim()
							.equalsIgnoreCase(name.trim())) {

						BuddyInformationBean online_bean = null;

						for (BuddyInformationBean temp : ContactsFragment
								.getBuddyList()) {
							if (!temp.isTitle()) {
								if (temp.getName().equalsIgnoreCase(
										online_buddys[i])) {
									online_bean = temp;
									break;
								}
							}
						}

						if (online_bean.getLatitude().trim().length() != 0
								&& online_bean.getLongitude().trim().length() != 0) {
							Double blat = Double.parseDouble(online_bean
									.getLatitude());
							Double blong = Double.parseDouble(online_bean
									.getLongitude());
							if (blat != 0 && blong != 0) {
								double theta = ownlong - blong;
								double dist = Math.sin(deg2rad(ownlat))
										* Math.sin(deg2rad(blat))
										+ Math.cos(deg2rad(ownlat))
										* Math.cos(deg2rad(blat))
										* Math.cos(deg2rad(theta));
								dist = Math.acos(dist);
								dist = rad2deg(dist);
								dist = dist * 60; // 60 nautical miles per
													// degree of seperation
								dist = dist * 1852; // 1852 meters per nautical
													// mile
								Log.i("thread", "......... buddy distance"
										+ dist);
								hb_list.put(dist, online_buddys[i]);
								dist_list.add(dist);
							}
						}
					}
				}

				Collections.sort(dist_list);
				if (dist_list.size() > 3) {
					for (int j = 0; j < 3; j++) {
						Log.i("thread",
								"......... buddy distance after sorting"
										+ dist_list.get(j));
						buddy_names.add(hb_list.get(dist_list.get(j)));
					}
				} else {
					for (int j = 0; j < dist_list.size(); j++) {
						Log.i("thread",
								"......... buddy distance after sorting"
										+ dist_list.get(j));
						buddy_names.add(hb_list.get(dist_list.get(j)));
					}
				}

			}
		}

		return buddy_names;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private static final double EARTH_RADIUS = 6371;
	public static final double kms1 = 0.001;

	public static double distance(GeoPoint startP, GeoPoint endP) {

		double lat1 = startP.getLatitudeE6() / 1E6;
		double lat2 = endP.getLatitudeE6() / 1E6;
		double lon1 = startP.getLongitudeE6() / 1E6;
		double lon2 = endP.getLongitudeE6() / 1E6;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c * kms1;
	}

	public String[] getOnlineBuddys() {

		List<String> buddies = new ArrayList<String>();
		for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
//			if (!bib.isTitle()) {
				if (bib.getStatus().equalsIgnoreCase("online")) {
					if (!bib.getName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						buddies.add(bib.getName());
					}
//				}
			}
		}

		// Set set = WebServiceReferences.buddyList.keySet();
		//
		// Log.e("loop", "enter loop");
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		//
		// BuddyInformationBean bean = null;
		// for (BuddyInformationBean temp : ContactsFragment.buddyList) {
		// if(temp.getName().equalsIgnoreCase(buddy))
		// {
		// bean = temp;
		// break;
		// }
		// }
		//
		// if (bean != null && bean.getStatus().startsWith("Onli")) {
		// Log.i("thread",
		// "$$$$$$$$$$$$$$$$$$$$"
		// + conferenceMembers.contains(buddy));
		// if (!conferenceMembers.contains(buddy)) {
		// if (!buddy.equalsIgnoreCase(LoginUser)) {
		// buddies.add(buddy);
		// }
		// } else {
		// Log.e("buddy", "Else Part Name:" + buddy);
		// }
		// }
		//
		// }
		String arr[] = buddies.toArray(new String[buddies.size()]);
		return arr;

	}

	public ArrayList<String> getOnlineBuddysOnly() {

		ArrayList<String> buddies = new ArrayList<String>();
		buddies.clear();
		if (!SingleInstance.isbcontacts) {

			for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
				if (!bib.isTitle()) {
					if (bib.getStatus().equalsIgnoreCase("online")) {
						if (!bib.getName().equalsIgnoreCase(
								CallDispatcher.LoginUser)) {
							buddies.add(bib.getName());
						}
					}
				}
			}
		} else {}

		return buddies;
		//
		// Set set = WebServiceReferences.buddyList.keySet();
		// ArrayList<String> buddies = new ArrayList<String>();
		// Log.e("loop", "enter loop");
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		// if (bean.getStatus().startsWith("Onli")) {
		// Log.i("thread",
		// "$$$$$$$$$$$$$$$$$$$$"
		// + conferenceMembers.contains(buddy));
		// if (!conferenceMembers.contains(buddy)) {
		// if (!buddy.equalsIgnoreCase(LoginUser)) {
		// buddies.add(buddy);
		// }
		//
		// } else {
		//
		// }
		//
		// } else {
		// Log.e("buddy", "Else Part Name:" + buddy);
		// }
		//
		// }
		// return buddies;
	}

	/**
	 * To get the buddies who are all in online
	 * 
	 * @return
	 */
	public String[] getOnlinePagingBuddys() {

		ArrayList<String> buddies = new ArrayList<String>();
		for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
			if (!bib.isTitle()) {
				if (!bib.getStatus().equalsIgnoreCase("Offline")) {
					if (!bib.getName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						if (!pagingMembers.contains(bib.getName())) {

							buddies.add(bib.getName());
						}
					}
				}
			}
		}

		// Set set = WebServiceReferences.buddyList.keySet();
		// List<String> buddies = new ArrayList<String>();
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		//
		// Log.e("buddy", "Name:" + bean.getLocalipaddress());
		// if ((!bean.getStatus().equals("Offline"))
		// && (!bean.getName().equalsIgnoreCase(
		// CallDispatcher.LoginUser))) {
		//
		// if (!pagingMembers.contains(buddy)) {
		//
		// buddies.add(buddy);
		// } else {
		//
		// }
		// } else {
		// Log.e("buddy", "Else Part Name:" + buddy);
		// }
		//
		// }

		String arr[] = buddies.toArray(new String[buddies.size()]);
		return arr;
	}

	public String[] getmyBuddys() {

		ArrayList<String> buddies = new ArrayList<String>();
		for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
			if (!bib.isTitle()) {
				if (!bib.getStatus().equalsIgnoreCase("Pending")
						&& !bib.getStatus().equalsIgnoreCase("new")) {
					if (!bib.getName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						buddies.add(bib.getName());
					}
				}
			}
		}

		Log.e("123", "%%%%%%%%%%% came to getmyBuddies");
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies"
		// + WebServiceReferences.buddyList.size());
		// Set set = WebServiceReferences.buddyList.keySet();
		// List<String> buddies = new ArrayList<String>();
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// if (buddy != null) {
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies" + buddy);
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		// if (!bean.getStatus().toString().equalsIgnoreCase("Pending")
		// && !bean.getName().equalsIgnoreCase(
		// CallDispatcher.LoginUser)) {
		// buddies.add(buddy);
		// }
		//
		// }
		//
		// }
		String arr[] = buddies.toArray(new String[buddies.size()]);
		return arr;

	}

	public String[] getmyBuddysForSpecialQA() {

		ArrayList<String> buddies = new ArrayList<String>();
		for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
			if (!bib.isTitle()) {
				if (!bib.getStatus().equalsIgnoreCase("Pending")
						&& !bib.getStatus().equalsIgnoreCase("Offline")) {
					if (!bib.getName().equalsIgnoreCase(
							CallDispatcher.LoginUser)) {
						buddies.add(bib.getName());
					}
				}
			}
		}

		Log.e("123", "%%%%%%%%%%% came to getmyBuddies");
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies"
		// + WebServiceReferences.buddyList.size());
		// Set set = WebServiceReferences.buddyList.keySet();
		// List<String> buddies = new ArrayList<String>();
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// if (buddy != null) {
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies" + buddy);
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		// Log.e("123", "Name: " + buddy + "LOGIN " + LoginUser);
		// // if (!buddy.trim().equalsIgnoreCase(LoginUser.trim())) {
		// Log.e("123", "Nameeeeeeeeee22222222:" + buddy + "LOGIN "
		// + LoginUser);
		// if (!bean.getStatus().toString().equalsIgnoreCase("Pending")
		// && !bean.getStatus().equalsIgnoreCase("Offline")
		// && !bean.getName().equalsIgnoreCase(
		// CallDispatcher.LoginUser)) {
		// buddies.add(buddy);
		// }
		//
		// }
		//
		// }
		String arr[] = buddies.toArray(new String[buddies.size()]);
		return arr;

	}

	public String[] getmyBuddysForAccess() {

		Log.e("123", "%%%%%%%%%%% came to getmyBuddies");
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies"
		// + WebServiceReferences.buddyList.size());
		// Set set = WebServiceReferences.buddyList.keySet();
		// List<String> buddies = new ArrayList<String>();
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// if (buddy != null) {
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies" + buddy);
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		// Log.e("123", "Name: " + buddy + "LOGIN " + LoginUser);
		// // if (!buddy.trim().equalsIgnoreCase(LoginUser.trim())) {
		// Log.e("123", "Nameeeeeeeeee22222222:" + buddy + "LOGIN "
		// + LoginUser);
		// if (!bean.getStatus().toString().equalsIgnoreCase("Pending")) {
		// buddies.add(buddy);
		// }
		//
		// }
		//
		// }
		// String arr[] = buddies.toArray(new String[buddies.size()]);
		return getmyBuddys();

	}

	public String[] getmyBuddysQA() {
		Log.e("123", "%%%%%%%%%%% came to getmyBuddies");
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies"
		// + WebServiceReferences.buddyList.size());
		// Set set = WebServiceReferences.buddyList.keySet();
		// List<String> buddies = new ArrayList<String>();
		//
		// Iterator<String> itr = set.iterator();
		// while (itr.hasNext()) {
		// String buddy = itr.next();
		// if (buddy != null) {
		// Log.e("123", "%%%%%%%%%%% came to getmyBuddies" + buddy);
		// BuddyInformationBean bean = WebServiceReferences.buddyList
		// .get(buddy);
		// Log.e("123", "Name: " + buddy + "LOGIN " + LoginUser);
		// if (!buddy.trim().equalsIgnoreCase(LoginUser.trim())
		// && !bean.getStatus().equalsIgnoreCase("pending")) {
		// Log.e("123", "Nameeeeeeeeee22222222:" + buddy + "LOGIN "
		// + LoginUser);
		// buddies.add(buddy);
		// }
		// }
		//
		// }
		// String arr[] = buddies.toArray(new String[buddies.size()]);
		return getmyBuddys();

	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}

	// public String[] getbuddieswithself() {
	// Set set = WebServiceReferences.buddyList.keySet();
	// List<String> buddies = new ArrayList<String>();
	// buddies.add("");
	// Iterator<String> itr = set.iterator();
	// while (itr.hasNext()) {
	// String buddy = itr.next();
	//
	// Log.e("123", "Name: " + buddy + "LOGIN " + LoginUser);
	//
	// Log.e("123", "Nameeeeeeeeee22222222:" + buddy + "LOGIN "
	// + LoginUser);
	// buddies.add(buddy);
	//
	// }
	// String arr[] = buddies.toArray(new String[buddies.size()]);
	// return arr;
	//
	// }

	/**
	 * If LogingUser is match with from return the to value else return from
	 * 
	 * @param from
	 * @param to
	 * @return String
	 */
	public static String getUser(String from, String to) {
		try {
			String user = null;
			System.out.println("From :" + from + " To :" + to
					+ " loginUser.toLowerCase() :" + LoginUser);
			if (LoginUser.toLowerCase().equals(from)) {
				user = to;
			} else {
				user = from;
			}
			return user;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Start the media player with ring tone when buddy made the call to other
	 * user.When the receiving end accept the call we have to stop the player
	 */
	public void startRingTone() {

		try {
			Log.d("RI", "start rin");
			Log.i("ring123", "start ringtone");
			if (SingleInstance.mainContext.player == null) {
				Log.i("ring123", "start ringtone player = null");
				SingleInstance.mainContext.player = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd("Ring.mp3");
				SingleInstance.mainContext.player.setDataSource(
						descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				SingleInstance.mainContext.player.setVolume(100, 100);
				SingleInstance.mainContext.player
						.setAudioStreamType(AudioManager.STREAM_MUSIC);
				descriptor.close();
				SingleInstance.mainContext.player.setLooping(true);
				SingleInstance.mainContext.player.prepare();
				SingleInstance.mainContext.player.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Start the media player with ring tone when incoming call is coming from
	 * the user.When the user accept the call we have to stop the incoming call
	 * sound
	 */
	public void startCallRingTone() {
		try {
			Log.d("RING", "on startCallRingTone()");
			Log.i("ring123", "start call ringtone");
			if (SingleInstance.mainContext.player == null) {
				Log.i("ring123", "start call ringtone player = null");
				Log.d("RING", "Started Player caller Ring Tone");
				SingleInstance.mainContext.player = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd("NFCW.mp3");
				SingleInstance.mainContext.player.setDataSource(
						descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				SingleInstance.mainContext.player.setLooping(true);
				SingleInstance.mainContext.player.prepare();
				SingleInstance.mainContext.player.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("RING", "on startCallRingTone()" + e.toString());
		}
	}

	/**
	 * To stop the playing ring tone
	 */
	public void stopRingTone() {
		try {
			if (SingleInstance.mainContext.player != null) {

				if (SingleInstance.mainContext.player.isPlaying()) {
					SingleInstance.mainContext.player.stop();
					SingleInstance.mainContext.player.release();

				}
				SingleInstance.mainContext.player = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * To Hang up the call
	 */
	public void callHangUp() {
		try {
			sb.setResult("1");
			sb.setType("3");

			Log.d("nll", "############################ sb..." + sb);
			Log.d("nll", "############################"
					+ AppMainActivity.commEngine);
			AppMainActivity.commEngine.hangupCall(sb);
			if (SingleInstance.mainContext.player != null) {
				if (SingleInstance.mainContext.player.isPlaying()) {
					stopRingTone();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void callHangUp1(SignalingBean sbx) {
		try {
			sbx.setResult("1");
			sbx.setType("3");

			Log.d("nll", "############################ sb..." + sb);
			Log.d("nll", "############################"
					+ AppMainActivity.commEngine);
			AppMainActivity.commEngine.hangupCall(sbx);
			if (SingleInstance.mainContext.player != null) {
				if (SingleInstance.mainContext.player.isPlaying()) {
					stopRingTone();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnectUsers() {
		try {
			Set<String> set = CallDispatcher.contConferencemembers
					.keySet();

			String Key = null;
			Iterator<String> iterator = set.iterator();

			while (iterator.hasNext()) {

				Key = iterator.next();

				SignalingBean sbx = CallDispatcher.contConferencemembers
						.get(Key);

				callHangUp1(sbx);

			}

		} catch (Exception e) {
			Log.d("stattus", "completed  vcvcv " + e);
		}
	}

	public void callHangupFromScreen(SignalingBean sb) {

		sb.setResult("0");
		sb.setType("3");
		if (sb.getSessionid() == null)
			sb.setSessionid(CallDispatcher.sb.getSessionid());
		if (sb.getSignalid() == null)
			sb.setSignalid(CallDispatcher.sb.getSignalid());
		AppMainActivity.commEngine.hangupCall(sb);
		if (SingleInstance.mainContext.player != null) {
			if (SingleInstance.mainContext.player.isPlaying()) {
				stopRingTone();
			}
		}

	}

	/**
	 * If user hang up the call send bye signal to all users who are all
	 * connected in the conference
	 */
	public void hangUpCall() {

		try {

			if (SingleInstance.mainContext.player != null) {
				if (SingleInstance.mainContext.player.isPlaying()) {
					stopRingTone();
				}
			}
			Message message = new Message();
			Bundle bunn = new Bundle();
			bunn.putString("leave", "leave");
			message.obj = bunn;
			audioCallHandler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * When call is in progress if the user try to close the application send
	 * bye signal to the users who are all connected with him. It may be a one
	 * to one or conference call
	 */
	public void directAudioHangUp() {
		if (conferenceMembers.size() > 0) {

			if (SingleInstance.mainContext.player != null) {
				if (SingleInstance.mainContext.player.isPlaying()) {
					stopRingTone();
				}
			}
			for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {

				Log.e("AC", "Login User:" + LoginUser);
				String buddyName = conferenceMembers.get(i);
				Log.e("AC", "Going to Hangup :" + buddyName);

				SignalingBean sb1 = buddySignall.get(buddyName);

				if (sb1 != null) {
					Log.i("AC", "SignalingBean Not Null");
					sb1.setFrom(CallDispatcher.LoginUser);
					sb1.setTo(buddyName);
					sb1.setType("3");
					sb1.setCallType("AC");
					AppMainActivity.commEngine.hangupCall(sb1);
				} else {
					Log.e("AC", "SignalingBean is Null");
				}

			}
		}
	}

	/**
	 * To mute and unmute the speaker with respect to the speaker value
	 * 
	 * @param speaker
	 */
	public void audioSpeakerMute(boolean speaker) {
		Message message = new Message();
		Bundle bunn = new Bundle();
		bunn.putString("spemute", "spemute");
		bunn.putBoolean("speaker", speaker);
		message.obj = bunn;
		audioCallHandler.sendMessage(message);
	}

	/**
	 * To mute or unmute the speaker when video call is in progress
	 * 
	 * @param speaker
	 */
	public static void videoSpeakerMute(boolean speaker) {

	}

	/**
	 *
	 */
	@Override
	public void notifyDecodedVideoCallback(byte[] data, long ssrc) {
		Log.e("video", "Comes to Video call Back");
	}

	/**
	 * When Network state is changed here we can get the update from the api
	 */
	@Override
	public void notifyNetworkStateChanged(NetworkInfo networkInfo) {
		// TODO Auto-generated method stub
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		Log.i("SSID", "Network State Changed ");
		Log.i("SSID", "Saved SSID:" + ssid);
		Log.i("SSID", "New SSID:" + wifiInfo.getSSID());
		Log.i("SSID", "network info isconnected:" + networkInfo.isConnected());

		if (ssid != null && wifiInfo != null) {
			if (!ssid.equals(wifiInfo.getSSID()) && networkInfo.isConnected()) {
				ssid = wifiInfo.getSSID();
				// LoginPageFragment loginPageFragment = LoginPageFragment
				// .newInstance(SingleInstance.mainContext);
				// loginPageFragment.backgroundLogin();

			}
		}

	}

//	public String getPublicIpFromURL() {
//
//		ipaddress = (String) this.context.getString(R.string.service_url);
//		String loginIP = null;
//
//		Log.d("logip1", "login ip a" + ipaddress);
//		loginIP = ipaddress.substring(ipaddress.indexOf("://") + 3);
//		Log.d("thread", "login ip b" + loginIP);
//		loginIP = loginIP.substring(0, loginIP.indexOf(":"));
//		Log.d("thread", "login ip c" + loginIP);
//		loginIP = loginIP.trim();
//
//		final String ServerIp = loginIP;
//
//		Log.d("logip1", "ip address " + ServerIp);
//
//		try {
//
//			URL url = new URL("http://" + ServerIp + "/NOTESServices/whoami");
//			URLConnection con = url.openConnection();
//			con.setConnectTimeout(getcbPort1());
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					con.getInputStream()));
//			String publicIp = in.readLine();
//			Log.d("logip1", "ip address " + publicIp);
//			Log.d("PIP", "set from Calldispatcher " + publicIp);
//			return publicIp;
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.d("logip1", "ip address " + e.toString());
//			return null;
//		}
//	}
	private String getPublicIpFromURL() {
		try {
			String[] tmp = ipaddress.split("/");
			String https_url = tmp[0] + "//" + tmp[2] + "/" + tmp[3]
					+ "/whoami";

			URL url = new URL(https_url);
			// HttpsURLConnection con =
			// (HttpsURLConnection)url.openConnection();

			// Create a context that doesn't check certificates.
			SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			TrustManager[] trust_mgr = get_trust_mgr();
			ssl_ctx.init(null, // key manager
					trust_mgr, // trust manager
					new SecureRandom()); // random number generator
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx
					.getSocketFactory());

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			// Guard against "bad hostname" errors during handshake.
			con.setHostnameVerifier(new HostnameVerifier() {
				
				@Override
				public boolean verify(String host, SSLSession sess) {
					// TODO Auto-generated method stub
					if (host.equals("localhost"))
						return true;
					else
						return true;
				}
			});

			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			String input, response = "";

			while ((input = br.readLine()) != null) {
				response = response + input;
			}
			br.close();
					Log.d("public_ip", "Public IP : " + response);

			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}


private TrustManager[] get_trust_mgr() {
		TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String t) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String t) {
			}
		} };
		return certs;
}
	@Override
	public void notifyResolution(int w, int h) {
		// TODO Auto-generated method stub
	}

	@Override
	public void notifyRecorderFailed(boolean isRecorderFailed) {
		// TODO Auto-generated method stub

	}

	public String getpublicIpFromUdpStunHttp() {

		Log.d("call", "Exsisting Ip " + getPublicipaddress());
		String publicIpAddresss = null;
		Log.d("call", "getpublicIpFromUdpStunHttp() " + parseAndGetServerIp());
		publicIpAddresss = getPublicIpFromUdp(parseAndGetServerIp(),
				getcbPort1(), 1);
		Log.d("call", "getpublicIp  " + publicIpAddresss);
		if (publicIpAddresss == null) {

			publicIpAddresss = getPublicIpFromStun();
			Log.d("call", "getpublicIpFromStun()" + publicIpAddresss);
		}
		if (publicIpAddresss == null) {
			publicIpAddresss = getPublicIpFromURL();
			Log.d("call", "getpublicIpFromURL()" + publicIpAddresss);
		}
		ProcessPublicIpState(publicIpAddresss);

		return publicIpAddresss;

	}

	String parseAndGetServerIp() {

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String url = preferences.getString("url", null);
		if (url != null) {
			if (url.trim().length() > 5) {
				ipaddress = url;
			} else {
				ipaddress = (String) this.context
						.getString(R.string.service_url);
			}
		} else {
			ipaddress = (String) this.context.getString(R.string.service_url);
		}

		String loginIP = null;
		// if (ipaddress != null) {
		Log.d("call", "login ip a" + ipaddress);
		loginIP = ipaddress.substring(ipaddress.indexOf("://") + 3);
		Log.d("call", "login ip b" + loginIP);
		loginIP = loginIP.substring(0, loginIP.indexOf(":"));
		Log.d("call", "login ip c" + loginIP);
		loginIP = loginIP.trim();
		// } else {
		// Log.d("logip", "login ip null");
		// }

		final String ServerIp = loginIP;

		return ServerIp;

	}

	void ProcessPublicIpState(String obtainPublicIp) {
		Log.d("SERVERIP",
				"ProcessPublicIpState(String obtainPublicIp) ------------------->"
						+ obtainPublicIp);
		try {
			if (wifiEngine != null) {
				String privateIp = wifiEngine.getLocalInetaddress();
				setLocalipaddsress(privateIp);
				if (AppMainActivity.commEngine != null) {
					AppMainActivity.commEngine.setLocalInetaddress(privateIp);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (publicipstate) {
		case PUBLIC_IP_SUCCESS_LOGIN:
			Log.d("SERVERIP", "PUBLIC_IP_SUCCESS_LOGIN"
					+ isKeepAliveSendAfter6Sec);
			try {
				if (!isKeepAliveSendAfter6Sec) {
					setPublicipaddress(obtainPublicIp);
					if (AppMainActivity.commEngine != null) {
						AppMainActivity.commEngine
								.setPublicInetaddress(obtainPublicIp);
					}
				} else {

					setPublicipaddress(obtainPublicIp);
					if (AppMainActivity.commEngine != null) {
						AppMainActivity.commEngine
								.setPublicInetaddress(obtainPublicIp);
					}
					KeepAliveBean aliveBean = getKeepAliveBean();
					aliveBean.setKey("1");

					if (LoginUser != null) {
						Log.i("b",
								"***************process public ip state method***********************");
						WebServiceReferences.webServiceClient
								.heartBeat(aliveBean);
						String[] user = { LoginUser, "", "" };
//						WebServiceReferences.webServiceClient.getFormTemplate(
//								user, context);
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			break;

		case PUBLIC_IP_KEEPALIVE_KEY_1_ON_NETWORKCHANGE:

			setPublicipaddress(obtainPublicIp);

			// if (commEngine != null) {
			// commEngine.setPublicInetaddress(obtainPublicIp);
			// }
			KeepAliveBean aliveBean = getKeepAliveBeanValidate();
			if (aliveBean != null) {
				aliveBean.setKey("1");
				WebServiceReferences.webServiceClient.heartBeat(aliveBean);
				String[] user = { LoginUser, "", "" };
//				WebServiceReferences.webServiceClient.getFormTemplate(user,
//						context);
			}

			break;

		case PUBLIC_IP_GET_AND_SET:
			setPublicipaddress(obtainPublicIp);

			if (AppMainActivity.commEngine != null) {
				AppMainActivity.commEngine.setPublicInetaddress(obtainPublicIp);
			}

			break;

		case PUBLIC_IP_KEEPALIVE_KEY_1_ON_KEY_1_RESPONSE:
			setPublicipaddress(obtainPublicIp);

			if (AppMainActivity.commEngine != null) {
				AppMainActivity.commEngine.setPublicInetaddress(obtainPublicIp);
			}
			try {
				KeepAliveBean aliveBean1 = getKeepAliveBeanValidate();
				if (aliveBean1 != null) {
					aliveBean1.setKey("1");
					WebServiceReferences.webServiceClient.heartBeat(aliveBean1);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			break;

		case PUBLIC_IP_KEEPALIVE_KEY_1_ON_KEY_0_RESPONSE:
			setPublicipaddress(obtainPublicIp);

			if (AppMainActivity.commEngine != null) {
				AppMainActivity.commEngine.setPublicInetaddress(obtainPublicIp);
			}
			try {
				KeepAliveBean aliveBean1 = getKeepAliveBeanValidate();
				if (aliveBean1 != null) {
					aliveBean1.setKey("1");
					WebServiceReferences.webServiceClient.heartBeat(aliveBean1);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		case PUBLIC_IP_SILENT_SIGNIN:

			setPublicipaddress(obtainPublicIp);

			if (AppMainActivity.commEngine != null) {
				AppMainActivity.commEngine.setPublicInetaddress(obtainPublicIp);
			}
			try {

				silentSignIn(LoginUser);
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		default:
			break;
		}
	}

	public String getPublicIpFromUdp(String serverIp, int serverPort, int retry) {
		Log.d("UDP", "sendUdpMessage(String serverIp,int serverPort)");
		DatagramSocket s;
		for (int x = 0; x < retry; x++) {
			try {
				s = new DatagramSocket();
				byte buffer[] = "what is my public ip".getBytes();
				RtpPacket rtppSend = new RtpPacket(buffer, buffer.length);
				DatagramPacket datagramSend = new DatagramPacket(new byte[1], 1);
				datagramSend.setData(rtppSend.packet);
				datagramSend.setLength(rtppSend.packet_len);
				datagramSend.setAddress(InetAddress.getByName(serverIp));
				datagramSend.setPort(serverPort);
				Log.d("UDP", "for(int x=0;x<retry;x++)");
				s.send(datagramSend); // send to the server

				s.setSoTimeout(1000); // set the timeout in millisecounds.
				Log.d("UDP", "setSoTimeout");

				while (true) { // recieve data until timeout

					try {
						DatagramPacket datagramReceive = new DatagramPacket(
								new byte[1], 1);
						// DatagramPacket datagram=new
						// DatagramPacket(rtpp.packet,rtpp.packet.length); // ch
						buffer = new byte[1024 + 12];
						RtpPacket rtpPacketReceive = new RtpPacket(buffer,
								buffer.length);
						datagramReceive.setData(rtpPacketReceive.packet);
						datagramReceive
								.setLength(rtpPacketReceive.packet.length);
						// socket.receive(datagram);
						Log.d("UDP", "ready to receive  while(true){");
						s.receive(datagramReceive);
						byte data[] = new byte[rtpPacketReceive.getLength() - 12];
						for (int i = 0; i < data.length; i++) {
							data[i] = buffer[i + 12];
						}
						String result = new String(data);
						Log.d("UDP", "received" + result);
						System.out.println("Public IP Result :" + result);

						if (result.contains(":")) {
							String publicInetAddres = result.split(":")[0];
							System.out.println("Public IP Result :"
									+ publicInetAddres);
							Log.d("UDP", "received Public ip Address."
									+ publicInetAddres);
							return publicInetAddres;
						}
						// break;

						// s.receive(dp);
						// String rcvd = "rcvd from " + dp.getAddress() + ", " +
						// dp.getPort() + ": "+ new String(dp.getData(), 0,
						// dp.getLength());
						// System.out.println(rcvd);
					} catch (SocketTimeoutException e) {
						// timeout exception.
						Log.d("UDP", "1 Exception " + e);
						System.out.println("Timeout reached!!! " + e);
						s.close();
						break;
					}
				}

			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
				Log.d("UDP", "2 Exception " + e1);
				System.out.println("Socket closed " + e1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("UDP", "3 Exception " + e);
				e.printStackTrace();
			}
		}
		Log.d("UDP", "sendUdpMessage(String serverIp,int serverPort) Finished");
		return null;
	}

	public String getPublicIpFromStun() {
		Log.d("SERVERIP", "getPublicIpFromStun()");
		di = null;
		try {

			Log.d("SERVERIP", "getPublicIpFromStun() on thread");

			Log.d("SERVERIP", "Thread synchronized");
			try {
				// Thread.sleep(5000);
				InetAddress inet = InetAddress.getByName(getLocalipaddsress());
				DiscoveryTest1 test = new DiscoveryTest1(inet, getCbserver1(),
						3478);
				di = test.test();
			} catch (Exception e) {
				// TODO: handle exception
			}

			Log.d("SERVERIP", "Stun waiting");

			Log.d("SERVERIP", "Stun Going to Return");
			if (di == null) {
				return null;
			} else {
				Log.d("SERVERIP", "Stun Going to Return" + di.getPublicIP());
				Log.d("SERVERIP", "Stun Going to Return"
						+ di.getPublicIP().toString());

				String receivedPIP = di.getPublicIP().toString();
				receivedPIP = receivedPIP.substring(1, receivedPIP.length());
				Log.d("SERVERIP", "Stun Going to " + receivedPIP + "ended");
				return receivedPIP;

			}

		} catch (Exception e) {

		}
		return null;
	}

	public KeepAliveBean getKeepAliveBean(String id, String action) {
		Log.d("Share", "Came to getKeepAliveBean -->" + id + " --->" + action);
		KeepAliveBean aliveBean = new KeepAliveBean();
		try {
			aliveBean.setName(CallDispatcher.LoginUser);
			aliveBean.setPassword("password");
			aliveBean.setLocalipaddress(getLocalipaddsress());
			if (getPublicipaddress() == null) {
				aliveBean.setExternalipaddress(getLocalipaddsress());
			} else {
				aliveBean.setExternalipaddress(getPublicipaddress());
			}
			aliveBean.setStatus(CallDispatcher.myStatus);
			aliveBean.setShareReminderAction(action);
			aliveBean.setLat(Double.toString(latitude));
			aliveBean.setLon(Double.toString(longitude));
			aliveBean.setId(id);
			aliveBean.setaver(context.getResources().getString(
					R.string.app_version));
			aliveBean.setavdate(context.getResources().getString(
					R.string.app_date));
			aliveBean.setdtype("ANDROID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return aliveBean;

	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	public void setcurrentlatLong(String lat, String clong) {

		this.currnetLat = lat;
		this.currentLong = clong;
	}

	public String[] getcurrentLocation() {
		String[] locdtails = null;
		if (currnetLat != null && currentLong != null) {
			if (!currnetLat.equalsIgnoreCase("null")
					&& !currentLong.equalsIgnoreCase("null")) {
				Log.i("thread", "###############" + currnetLat);
				Log.i("thread", "###############" + currentLong);
				locdtails = new String[2];
				locdtails[0] = currnetLat;
				locdtails[1] = currentLong;
			}
		}

		return locdtails;
	}

	public String[] getBuddyLocation(String location) {
		String locs[] = location.split(",");
		if (locs[0].contains("Latitude:")) {
			locs[0] = locs[0].replace("Latitude:", "");
		}
		if (locs[1].contains("Longitude:")) {
			locs[1] = locs[1].replace("Longitude:", "");
		}
		return locs;
	}

	public void setToneEnabled(boolean val) {
		istoneEnabled = val;
	}

	public boolean getToneEnabled() {
		return istoneEnabled;
	}

	public void showprogress(final ProgressDialog psdialog, Context context) {

		try {
			handlerForCall.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						progressDialog = psdialog;
						if (progressDialog != null) {
							progressDialog.setCancelable(true);
							progressDialog.setMessage("Progress ...");
							progressDialog
									.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progressDialog.setProgress(0);
							progressDialog.setMax(100);
							progressDialog.show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isProgressDialogShowing() {
		try {
			if (progressDialog != null) {
				return progressDialog.isShowing();
			} else
				return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return false;
		}
	}

	public void cancelDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

	}

	public void cancelDialog(ProgressDialog progressDialog) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	class ValueComparator implements Comparator<String> {

		Map<String, Double> base;

		public ValueComparator(Map<String, Double> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}

	public ArrayAdapter<String> menusAdapter(String menusArray[],
			final Context context) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, menusArray) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// setting the ID and text for every items in the list

				String text = getItem(position);

				// visual settings for the list item
				TextView listItem = new TextView(context);

				listItem.setText(text);
				listItem.setTag(position);
				listItem.setTextSize(18);
				listItem.setPadding(10, 10, 10, 10);
				listItem.setTextColor(Color.WHITE);

				return listItem;
			}
		};

		return adapter;
	}

	public static String setDateFormat(String date) {
		String newDate = null;
		if (dateFormat.equalsIgnoreCase("dd-MM-yyyy")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
			newDate = sdf.format(date);
			Log.i("login", "new date=====> " + newDate);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("mm-dd-yyyy");
			newDate = sdf.format(date);
			Log.i("login", "new date else=====> " + newDate);
		}
		return newDate;
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 50;
		int targetHeight = 50;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	private void silentSignIn(final String user) {

		isSilentLoginCalled = true;

		try {
			if (WebServiceReferences.running) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {

							String text1 = user;
							String text2 = CallDispatcher.Password;
							strTempUser = user;
							strTempPassword = text2;
							AppMainActivity.commEngine
									.setLocalInetaddress(getLocalipaddsress());
							AppMainActivity.commEngine
									.setPublicInetaddress(getPublicipaddress());
							PackageManager pm = context.getPackageManager();
							boolean hasFrontCamera = pm
									.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
							if (Integer.parseInt(Build.VERSION.SDK) > 8) {
								WebServiceReferences.CAMERA_ID = 1;
								if (!hasFrontCamera) {
									WebServiceReferences.CAMERA_ID = 0;
								} else if (Camera.getNumberOfCameras() == 1) {
									WebServiceReferences.CAMERA_ID = 0;
								}

							} else {
								WebServiceReferences.CAMERA_ID = 2;

							}

							SiginBean siginBean = new SiginBean();
							text1 = text1.toLowerCase();
							siginBean.setDeviceType("Android");

							siginBean.setName(text1);
							siginBean.setPassword(text2);
							siginBean.setMac(getDeviceId());

							siginBean.setLocalladdress(getLocalipaddsress());
							siginBean
									.setExternalipaddress(getPublicipaddress());
							siginBean.setSignalingPort(Integer
									.toString(AppMainActivity.commEngine
											.getSignalingPort()));
							siginBean.setPstatus("1");
							siginBean.setDtype(returnDetails[0]);
							siginBean.setDos(returnDetails[2]);
							siginBean.setAver(context.getResources().getString(
									R.string.app_version));

							WebServiceReferences.webServiceClient
									.UsersignIn(siginBean);

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}).start();

			} else {
				try {

					SharedPreferences preferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					String ipaddress = preferences.getString("ipaddress", null);
					String port = preferences.getString("port", null);
					String namespace = preferences.getString("namespace", null);
					if (ipaddress != null && port != null && namespace != null) {

					} else {
						Toast.makeText(context, "Please check settings", 3000)
								.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private class LoginTaskSilent extends
			AsyncTask<Servicebean, Servicebean, Servicebean> {
		Servicebean ser = null;

		protected void onPreExecute() {
		}

		@Override
		protected Servicebean doInBackground(Servicebean... params) {
			ser = params[0];
			BackGroundSilent(ser);
			return null;
		}

		protected void onProgressUpdate(Servicebean... progress) {
		}

		protected void onPostExecute(Servicebean result) {
			postExecuteSilent(ser);

		}

	}

	void postExecuteSilent(Servicebean ser) {
		try {
			if (ftpNotifier == null)
				ftpNotifier = new FTPNotifier();

			// getBuddyList();
			// adapterToShow.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void BackGroundSilent(Servicebean servicebean) {
		ContactsFragment.getBuddyList().clear();
		WebServiceReferences.reqbuddyList.clear();
		try {
			LoginUser = strTempUser;
			LoginUser = LoginUser.toLowerCase();
			Password = strTempPassword;

			ArrayList list = (ArrayList) servicebean.getObj();
			for (int i = 0; i < list.size(); i++) {
				final Object obj = list.get(i);
				if (obj instanceof BuddyInformationBean) {

					BuddyInformationBean bib = (BuddyInformationBean) list
							.get(i);
					if (!bib.getStatus().equals("new")) {
						synchronized (ContactsFragment.getBuddyList()) {
							ContactsFragment.getBuddyList().add(bib);

						}
						// WebServiceReferences.buddyList.put(bib.getName(),
						// bib);
					} else if (bib.getStatus().equalsIgnoreCase("pending")) {
						// showPendingBuddies(bib);

					} else {
						WebServiceReferences.reqbuddyList.put(bib.getName(),
								bib);
					}
				} else if (obj instanceof ConnectionBrokerBean) {
					ConnectionBrokerBean cbb = (ConnectionBrokerBean) list
							.get(i);

					setCbserver1(cbb.getCbserver1());
					setCbserver2(cbb.getCbserver2());
					setcbPort1(cbb.getaa1Port());
					setcbPort2(cbb.getaa2Port());

					setRouter(cbb.getRouter());
					setRelay(cbb.getRelayServer());

					if (AppMainActivity.commEngine != null) {
						AppMainActivity.commEngine.setConnectionBrokerDetails(
								getCbserver1(), getCbserver2(), getcbPort1(),
								getcbPort2());

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void notifytype2Sent() {
		// TODO Auto-generated method stub
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("AAAA", "notifytype2Sent  ==> " );
				CallDispatcher.sb.setStartTime(getCurrentDateandTime());
				Object objCallScreen = SingleInstance.instanceTable
						.get("callscreen");
				if (objCallScreen != null) {
					if (objCallScreen instanceof AudioCallScreen)
						((AudioCallScreen) SingleInstance.instanceTable
								.get("callscreen")).notifyType2Received();
					if (objCallScreen instanceof AudioPagingSRWindow)
						((AudioPagingSRWindow) SingleInstance.instanceTable
								.get("callscreen")).notifyType2Received();

				} else {
					handlerForCall.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Object objCallScreen = SingleInstance.instanceTable
									.get("callscreen");
							if (objCallScreen != null) {
								if (objCallScreen instanceof AudioCallScreen)
									((AudioCallScreen) SingleInstance.instanceTable
											.get("callscreen"))
											.notifyType2Received();
								if (objCallScreen instanceof AudioPagingSRWindow)
									((AudioPagingSRWindow) SingleInstance.instanceTable
											.get("callscreen"))
											.notifyType2Received();

							}
						}
					}, 1500);
				}
			}
		});
	}

	@Override
	public void notifySIPResponse(Object obj) {
		// TODO Auto-generated method stub

	}

	public static String getDeviceId() {
		try {
			String deviceId = null;
			TelephonyManager telephonyManager = (TelephonyManager) SingleInstance.mainContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephonyManager.getDeviceId();
			if (deviceId == null) {
				deviceId = "Unknown";
			}
			return deviceId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void whenCallHangedUp() {
		try {
			if (isAudioPaused) {
				audioManager.abandonAudioFocus(null);
				isAudioPaused = false;
			}
		} catch (Exception e) {
		}

	}

	public void networkStateChanged(final boolean isConnected) {
		Log.d("BL", "called network statechanged");
		try {
			setPublicipaddress(null);
			CallDispatcher.isConnected = isConnected;
			notifyConnectivityChanged(isConnected);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void notifyConnectivityChanged(final boolean flag) {
		netWorkStatusHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!isConnected && LoginUser != null) {
					isWifiClosed = true;
					cancelDownloadSchedule();
					Log.i("wifi123", "Wifi closed : " + isWifiClosed);
					SingleInstance.mainContext
							.ShowError("The Network Has Changed");

					SingleInstance.printLog(null, "The Network Has Changed" , "INFO", null);
					new AppMainActivity().setfooterVisiblity(false);
					SingleInstance.mainContext.chageMyStatus();

					Object objCallScreen = SingleInstance.instanceTable
							.get("callscreen");

					if (objCallScreen != null) {

						if (objCallScreen instanceof AudioCallScreen) {
							AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
							acalObj.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");
						} else if (objCallScreen instanceof VideoCallScreen) {

							VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
							acalObj.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");

						} else if (objCallScreen instanceof AudioPagingSRWindow) {
							AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
							acalObj.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");

						}

						else if (objCallScreen instanceof VideoPagingSRWindow) {
							VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
							acalObj.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");

						} else if (SingleInstance.instanceTable
								.containsKey("screenshare")) {
							ScreenSharingFragment ssFragment = (ScreenSharingFragment) SingleInstance.instanceTable
									.get("screenshare");
							ssFragment
									.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");

						}

					}
					if (WebServiceReferences.contextTable
							.containsKey("frmviewer")) {
						FormViewer view1 = (FormViewer) WebServiceReferences.contextTable
								.get("frmviewer");
						view1.cancelDialog(false);
					} else if (WebServiceReferences.contextTable
							.containsKey("frmreccreator")) {
						FormRecordsCreators view1 = (FormRecordsCreators) WebServiceReferences.contextTable
								.get("frmreccreator");
						view1.cancelDialog(false);
					} else if (WebServiceReferences.contextTable
							.containsKey("formpermissionviewer")) {
						FormPermissionViewer frm_creator = (FormPermissionViewer) WebServiceReferences.contextTable
								.get("formpermissionviewer");

						frm_creator.cancelDialog(false);

					} else if (WebServiceReferences.contextTable
							.containsKey("formpermission")) {
						AccessAndSync view1 = (AccessAndSync) WebServiceReferences.contextTable
								.get("formpermission");
						view1.cancelDialog(false);
					} else if (WebServiceReferences.contextTable
							.containsKey("formdesc")) {
						FormDescription view1 = (FormDescription) WebServiceReferences.contextTable
								.get("formdesc");
						view1.cancelDialog(false);
					}

					else if (SingleInstance.contextTable.containsKey("forms")) {
						FormsFragment quickActionFragment = FormsFragment
								.newInstance(context);

						quickActionFragment.cancelDialog();
						quickActionFragment.showToast();
					}
					if (SingleInstance.instanceTable
							.containsKey("connection"))
						((CallConnectingScreen) SingleInstance.instanceTable
								.get("connection"))
								.showWifiStateChangedAlert("Internet Connection lost,can not continue this call");
					// if (WebServiceReferences.contextTable
					// .containsKey("sipcallscreen")) {
					// ((CallScreenActivity) WebServiceReferences.contextTable
					// .get("sipcallscreen"))
					// .ForceHangup("Internet Connection lost ,you can not continue this call");
					// } else
					// if (WebServiceReferences.contextTable
					// .containsKey("icommingsipcall")) {
					// ((IncomingCallActivity) WebServiceReferences.contextTable
					// .get("icommingsipcall"))
					// .ForceHangup("Internet Connection lost ,you can not continue this call");
					// } else if (WebServiceReferences.contextTable
					// .containsKey("sicallalert")) {
					// ((SipIncomingCallAlertActivity)
					// WebServiceReferences.contextTable
					// .get("sicallalert")).finish();
					// stopRingTone();
					// }

					new Thread(new Runnable() {

						@Override
						public void run() {
							uploadData.clear();
							updateBuddysToOffline();
							appMainActivity.startHeartBeatTimer();
						}
					}).start();

				} else if (isConnected) {

					isWifiClosed = false;
					Log.d("droid123", "came to else");
					Log.i("wifi123", "Wifi closed : " + isWifiClosed);
					// scheduleFileDownloader();
					// new Thread(new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// if (appMainActivity.HBT == null) {
					// try {
					//
					// publicipstate =
					// PUBLIC_IP_STATE.PUBLIC_IP_KEEPALIVE_KEY_1_ON_NETWORKCHANGE;
					// getpublicIpFromUdpStunHttp();
					// } catch (Exception e) {
					// // TODO: handle exception
					// }
					// } else {
					// // Used to find and set Public
					// // Ip----------Newly Added....
					// publicipstate = PUBLIC_IP_STATE.PUBLIC_IP_GET_AND_SET;
					// getpublicIpFromUdpStunHttp();
					// }
					//
					// }
					// }).start();

					LoginPageFragment loginPageFragment = LoginPageFragment
							.newInstance(SingleInstance.mainContext);
					loginPageFragment.backgroundLogin();

				}
				// setPublicipaddress(null);
			}
		});
	}

	public boolean isOnLine(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public void showKeyDownAlert(final Context mcontext) {

		AlertDialog.Builder buider = new AlertDialog.Builder(mcontext);
		buider.setMessage(
				"Are you sure, You want to Send this application to Background ?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) mcontext).moveTaskToBack(true);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		AlertDialog alert = buider.create();
		alert.show();

	}

	void updateBuddysToOffline() {

		try {

			for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
				if (!bib.isTitle()) {
					if (!bib.getStatus().equalsIgnoreCase("Pending")) {
						bib.setStatus("Offline");
					}
				}
			}

			if (ContactsFragment.getContactAdapter() != null
					) {
				handlerForCall.post(new Runnable() {

					@Override
					public void run() {
						try {
							ContactsFragment.getContactAdapter()
									.notifyDataSetChanged();
							
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

			}

			// Set<String> set = WebServiceReferences.buddyList.keySet();
			//
			// String buddy = null;
			// Iterator<String> iterator = set.iterator();
			//
			// while (iterator.hasNext()) {
			//
			// buddy = iterator.next();
			//
			// BuddyInformationBean bib = WebServiceReferences.buddyList
			// .get(buddy);
			// if (!bib.getStatus().equalsIgnoreCase("pending")) {
			// bib.setStatus("Offline");
			// }
			//
			// // int idx = getDataBean(bib.getName());
			// Databean databean;
			//
			// // if (idx == -1) {
			// //
			// // } else {
			// //
			// // // databean = (Databean) adapterToShow.getItem(idx);
			// // databean.setname(buddy);
			// // databean.setStatus(bib.getStatus());
			// //
			// // }
			//
			// }
			// audioCallHandler.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// // new SortBuddies().execute("");
			// }
			// });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// void startHeartBeatTimer() {
	//
	// Log.i("b", "startHeartBeatTimer()***********");
	//
	// try {
	// isInternetEnabled = false;
	// if (HBT == null) {
	// HBT = new HeartbeatTimer();
	// HBT.setReference(this);
	// Timer tm = new Timer();
	// tm.schedule(HBT, 0, 3000);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	//
	// public void stopHeartBeatTimer() {
	// isInternetEnabled = true;
	// if (HBT != null) {
	// HBT.cancel();
	// HBT = null;
	// }
	// }

	public DBAccess getdbHeler(Context context) {
		if (dbhelper == null) {
			dbhelper = new DBAccess(context);
			dbhelper.openDatabase();
			dbhelper.insertCloneMasterDatas();
			dbhelper.insertRecordsonMasterTable();
		}

		return dbhelper;
	}

	public String getFileName() {
		String strFilename = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			strFilename = dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strFilename;
	}

	public long getExternalMemorySize() {
		try {
			if (externalMemoryAvailable()) {
				File path = Environment.getExternalStorageDirectory();
				Log.d("Im", path.toString());
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				return (availableBlocks * blockSize);

			} else {
				return -1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

	public int isMemoryavailable(long val) {
		int result = 0;
		try {

			if (getExternalMemorySize() > val) {
				result = 1;
			} else {
				if (!externalMemoryAvailable()) {
					result = 2;
				} else {
					result = 0;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private boolean externalMemoryAvailable() {
		try {
			return android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Bitmap ResizeImage(String filePath) {
		try {
			int targetWidth = noScrWidth;// (int) (noScrWidth / 1.2);
			int targetHeight = noScrHeight;// (int) (noScrHeight / 1.2);

			Bitmap bitMapImage = null;
			Options options = new Options();
			options.inJustDecodeBounds = true;
//			Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
			Bitmap bmp = AESFileCrypto.decryptBitmap(filePath);
			if (bmp != null)
				bmp.recycle();
			double sampleSize = 0;
			Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
					.abs(options.outWidth - targetWidth);

			if (options.outHeight * options.outWidth * 2 >= 1638) {

				sampleSize = scaleByHeight ? options.outHeight / targetHeight
						: options.outWidth / targetWidth;
				sampleSize = (int) Math.pow(2d,
						Math.floor(Math.log(sampleSize) / Math.log(2d)));
			}

			options.inJustDecodeBounds = false;
			options.inTempStorage = new byte[128];
			while (true) {
				try {
					if (bitMapImage != null)
						bitMapImage.recycle();
					System.gc();
					options.inSampleSize = (int) sampleSize;
					bitMapImage = BitmapFactory.decodeFile(filePath, options);
					break;
				} catch (Exception ex) {
					try {
						sampleSize = sampleSize * 2;
					} catch (Exception ex1) {

					}
				}
			}
			return bitMapImage;
		} catch (OutOfMemoryError e) {
			Log.e("bitmap", "===> " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public Bitmap ResizeImage(String file, int isize) {
		Bitmap b = null;
		try {
			Bitmap mBitmap;
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeFile(file, opts);
			if (bmp != null)
				bmp.recycle();
			opts.inJustDecodeBounds = false;
			if (opts.outWidth == opts.outHeight) {
				mBitmap = BitmapFactory.decodeFile(file, opts);
				b = Bitmap.createScaledBitmap(mBitmap, isize, isize, true);
			} else {
				if (opts.outHeight == isize) {
					mBitmap = BitmapFactory.decodeFile(file, opts);
					b = Bitmap.createScaledBitmap(mBitmap, opts.outWidth,
							opts.outHeight, true);
				} else {
					double ratio;
					if (opts.outHeight < opts.outWidth) {
						ratio = (double) opts.outWidth
								/ (double) opts.outHeight;
						mBitmap = BitmapFactory.decodeFile(file, opts);
						b = Bitmap.createScaledBitmap(mBitmap,
								(int) (isize * ratio), isize, true);
					} else {
						ratio = (double) opts.outWidth
								/ (double) opts.outHeight;
						mBitmap = BitmapFactory.decodeFile(file, opts);
						b = Bitmap.createScaledBitmap(mBitmap,
								(int) Math.round(isize / ratio), isize, true);
					}
				}

			}
			System.gc();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception

			Log.e("BLOB", "BITMAP error" + "" + "" + e.getMessage());
		}

		return b;

	}

	/**
	 * @return the noScrHeight
	 */
	public int getNoScrHeight() {
		return noScrHeight;
	}

	/**
	 * @param noScrHeight
	 *            the noScrHeight to set
	 */
	public void setNoScrHeight(int noScrHeight) {
		this.noScrHeight = noScrHeight;
	}

	/**
	 * @return the noScrWidth
	 */
	public int getNoScrWidth() {
		return noScrWidth;
	}

	/**
	 * @param noScrWidth
	 *            the noScrWidth to set
	 */
	public void setNoScrWidth(int noScrWidth) {
		this.noScrWidth = noScrWidth;
	}

	public Bitmap getmyProfilePicture(String imageName) {
		try {

			Log.d("profile", "rofile path----->" + profilePicturepath);
			if (imageName == null || imageName.trim().length() == 0)
				imageName = getdbHeler(context).getProfilePic(LoginUser);

			if (imageName != null && imageName.trim().length() > 0) {
				Bitmap bitmap = setProfilePicture(imageName,
						R.drawable.icon_buddy_aoffline);
				return bitmap;
			}

			// else {
			//
			// Log.d("profile", "came to else " + profilePicturepath);
			// if (bitmap_table.containsKey("default"))
			// profile_bitmap = bitmap_table.get("default");
			// else {
			// profile_bitmap = BitmapFactory.decodeResource(
			// context.getResources(),
			// R.drawable.icon_buddy_aoffline);
			// bitmap_table.put("default", profile_bitmap);
			// }
			// }

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

		return BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon_buddy_aoffline);
	}

	public void changeMyOnlineStatus() {

		if (myStatus.equals("0"))
			onlineStatus = "Offline";

		if (myStatus.equals("1"))
			onlineStatus = "Online";

		if (myStatus.equals("2"))
			onlineStatus = "Busy";

		if (myStatus.equals("3"))
			onlineStatus = "Away";

		if (myStatus.equals("4"))
			onlineStatus = "Stealth";

	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			Log.i("profile", "===> inside getRealPathFromURI");
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(contentUri,
					proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public void showToast(Context context1, final String message) {
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				try {
					Toast.makeText(SingleInstance.mainContext, message,
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			if (CallDispatcher.LoginUser != null) {
				Intent intent = new Intent(context, buddiesList.class);
				context.startActivity(intent);
			} else
				showToast(context, "Kindly Login");
			break;
		case WebServiceReferences.USERPROFILE:
			if (CallDispatcher.LoginUser != null) {
				if (getdbHeler(context).isRecordExists(
						"select * from profilefieldvalues where userid='"
								+ CallDispatcher.LoginUser + "'")) {
					Intent intent = new Intent(context, ViewProfiles.class);
					intent.putExtra("buddyname", CallDispatcher.LoginUser);
					intent.putExtra("fromactivity", "buscard");
					context.startActivity(intent);
				} else {
					Intent intent = new Intent(context, BusCard.class);
					context.startActivity(intent);
				}
			} else
				showToast(context, "Kindly Login");
			break;
		case WebServiceReferences.UTILITY:
			if (CallDispatcher.LoginUser != null) {
				Intent intent = new Intent(context, UtilitiyActivity.class);
				context.startActivity(intent);
			} else
				showToast(context, "Kindly Login");
			break;
		case WebServiceReferences.NOTES:
			if (!WebServiceReferences.contextTable.containsKey("MAIN")) {
				Intent intent = new Intent(context, CompleteListView.class);
				context.startActivity(intent);
			}
			break;
		case WebServiceReferences.APPS:
			Intent appsIntent = new Intent(context, AppsView.class);
			context.startActivity(appsIntent);
			break;
		case WebServiceReferences.CLONE:
			if (CallDispatcher.LoginUser != null) {
				Intent intentqa = new Intent(context, CloneActivity.class);
				context.startActivity(intentqa);
			} else
				showToast(context, "Kindly Login");
			break;
		case WebServiceReferences.SETTINGS:
			Intent mIntent = new Intent(context, MenuPage.class);
			context.startActivity(mIntent);
			break;

		case WebServiceReferences.QUICK_ACTION:
			if (CallDispatcher.LoginUser != null) {
				Intent intentqa = new Intent(context, ContactLogics.class);
				context.startActivity(intentqa);
			} else
				showToast(context, "Kindly Login");

			break;
		case WebServiceReferences.FORMS:

			if (AppReference.isFormloaded) {

				FormsFragment viewProfileFragment = FormsFragment
						.newInstance(context);
				AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
						.get("MAIN");
				FragmentManager fragmentManager = appMainActivity
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment,
						viewProfileFragment);
				fragmentTransaction.commit();
			} else {
				if (CallDispatcher.LoginUser != null) {
					showprogress();
				}

			}
			break;
		case WebServiceReferences.FEEDBACK:
			if (CallDispatcher.LoginUser != null) {
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/COMMedia/log.txt");
				if (file.exists()) {
					String content = "Username : " + CallDispatcher.LoginUser
							+ "\n OS Details : Android \n Version : "
							+ android.os.Build.VERSION.RELEASE + " \n Make : "
							+ android.os.Build.BRAND + "\n Model : "
							+ android.os.Build.MODEL;
					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"Alpha Log");
					emailIntent.putExtra(
							android.content.Intent.EXTRA_STREAM,
							Uri.parse("file:// "
									+ Environment.getExternalStorageDirectory()
									+ "/COMMedia/log.txt"));
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							content);
					SipNotificationListener.getCurrentContext().startActivity(
							Intent.createChooser(emailIntent, "Send mail..."));
				} else
					showToast(SipNotificationListener.getCurrentContext(),
							"Sorry, no log file to send");
			} else
				showToast(SipNotificationListener.getCurrentContext(),
						"Please login to send feedback");
			break;
		case WebServiceReferences.EXCHANGES:
			if (CallDispatcher.LoginUser != null) {
				Intent exchanges = new Intent(context, ExchangesActivity.class);
				context.startActivity(exchanges);
			} else
				showToast(SipNotificationListener.getCurrentContext(),
						"Please login to send feedback");
			break;
		default:
			break;
		}
	}

	public Context getContext() {
		return context;
	}

	private void scheduleFileDownloader() {
		if (fileDownloader == null && download_timer == null) {
			fileDownloader = new FileDownloader();
			download_timer = new Timer();
			download_timer.schedule(fileDownloader, 1000 * 60 * 5,
					1000 * 60 * 5);
		}
	}

	public void cancelDownloadSchedule() {
		if (download_timer != null)
			download_timer.cancel();

		download_timer = null;
		fileDownloader = null;
	}

	public void notifyGSMCallAcceted() {
		isCallInitiate = false;
		GSMCallisAccepted = true;
		if (WebServiceReferences.contextTable.containsKey("multimediautils"))
			((MultimediaUtils) WebServiceReferences.contextTable
					.get("multimediautils")).notifyGSMCallAccepted();
		// if (WebServiceReferences.contextTable.containsKey("videoplayer"))
		// ((VideoPlayer) WebServiceReferences.contextTable.get("videoplayer"))
		// .notifyGSMCallAccepted();
		Object objCallScreen = SingleInstance.instanceTable
				.get("callscreen");

		if (objCallScreen != null) {

			if (objCallScreen instanceof AudioCallScreen) {
				AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
				acalObj.notifyGSMCallAccepted();
			} else if (objCallScreen instanceof VideoCallScreen) {

				VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
				acalObj.notifyGSMCallAccepted();

			} else if (objCallScreen instanceof AudioPagingSRWindow) {
				AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
				acalObj.notifyGSMCallAccepted();

			}

			else if (objCallScreen instanceof VideoPagingSRWindow) {
				VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
				acalObj.notifyGSMCallAccepted();

			}
		}
		if (SingleInstance.instanceTable.containsKey("connection"))
			((CallConnectingScreen) SingleInstance.instanceTable
					.get("connection")).HangupCall();

		// if (WebServiceReferences.contextTable.containsKey("sipcallscreen")) {
		// ((CallScreenActivity) WebServiceReferences.contextTable
		// .get("sipcallscreen"))
		// .ForceHangup("Internet Connection lost ,you can not continue this call");
		// } else

		// if (WebServiceReferences.contextTable
		// .containsKey("icommingsipcall")) {
		// ((IncomingCallActivity) WebServiceReferences.contextTable
		// .get("icommingsipcall"))
		// .ForceHangup("Internet Connection lost ,you can not continue this call");
		// } else if
		// (WebServiceReferences.contextTable.containsKey("sicallalert")) {
		// ((SipIncomingCallAlertActivity) WebServiceReferences.contextTable
		// .get("sicallalert")).finish();
		// stopRingTone();
		// }
	}

	public void sendReminder(String filename, String buddyName, String type,
			String reminderStatus, String reminderDatetime,
			String responsetype, String remindertz, String filePath,
			String fileid, boolean hastoupdate, boolean hastoStream,
			String comment, String vanishMode, String vanishValue,
			CompleteListBean cmpBean) {

		Utility utility = new Utility();
		ShareSendBean sbean = new ShareSendBean();

		sbean.setUserName(buddyName);
		if (hastoupdate) {
			sbean.setFileId(Long.toString(utility.getRandomMediaID()));
		} else {
			if (fileid != null) {
				sbean.setFileId(fileid);
			}
		}
		sbean.setFilePath(filePath);
		if (type.equalsIgnoreCase("note")) {
			type = "notes";
		} else if (type.equalsIgnoreCase("photo")) {
			type = "photonotes";
		}
		sbean.setcomponenttype(type);
		ShareReminder reminder = new ShareReminder();

		if (hastoupdate) {
			if (WebServiceReferences.contextTable.containsKey("MAIN")) {
				CompleteListView list = (CompleteListView) WebServiceReferences.contextTable
						.get("MAIN");
				if (type.equalsIgnoreCase("video")) {
					if (hastoStream)
						list.updateComponent(buddyName, filePath.substring(0,
								filePath.trim().length() - 4), sbean
								.getFileId(), filename, "2", type);
					else
						list.updateComponent(buddyName, filePath.substring(0,
								filePath.trim().length() - 4), sbean
								.getFileId(), filename, "1", type);

				} else {
					list.updateComponent(buddyName, filePath,
							sbean.getFileId(), filename, "1", type);
				}
			}
		}

		reminder.setFrom(CallDispatcher.LoginUser);
		reminder.setTo(buddyName);
		reminder.setType(type);
		reminder.setReceiverStatus("manual");
		reminder.setFileLocation(filename);
		reminder.setRemindertz(remindertz);
		reminder.setReminderdatetime(reminderDatetime);
		reminder.setFileid(sbean.getFileId());
		Log.d("ftp", "webserc" + reminderStatus);
		reminder.setReminderStatus(reminderStatus);
		reminder.setReminderResponseType(responsetype);
		reminder.setIsbusyResponse("0");
		if (cmpBean != null && cmpBean.getParent_id() != null) {
			reminder.setBsstatus("Received");
			reminder.setBstype("Order");
			reminder.setDirection("In");
			reminder.setParent_id(cmpBean.getParent_id());
			ContentValues cv = new ContentValues();
			if (cmpBean.getBstype() != null) {
				cv.put("bscategory", cmpBean.getBstype());
			}
			if (cmpBean.getBsstatus() != null) {
				cv.put("bsstatus", cmpBean.getBsstatus());
			}
			if (cmpBean.getDirection() != null) {
				cv.put("bsdirection", "Out");
			}

			cv.put("parentid", cmpBean.getParent_id());
			cv.put("componenttype", cmpBean.getcomponentType());
			cv.put("componentpath", cmpBean.getContentpath());
			cv.put("ftppath", cmpBean.getFtpPath());
			cv.put("componentname", cmpBean.getContentName());
			cv.put("fromuser", CallDispatcher.LoginUser);
			cv.put("comment", cmpBean.getComment());
			cv.put("owner", CallDispatcher.LoginUser);
			cv.put("touser", buddyName);

			if (cmpBean.getVanishMode() != null)
				cv.put("vanishmode", cmpBean.getVanishMode());
			if (cmpBean.getVanishValue() != null)
				cv.put("vanishvalue", cmpBean.getVanishValue());
			if (cmpBean.getDateAndTime() != null)
				cv.put("receiveddateandtime", cmpBean.getDateAndTime());
			if (cmpBean.getReminderTime() != null)
				cv.put("reminderdateandtime", cmpBean.getReminderTime());
			cv.put("reminderstatus", 0);
			cv.put("viewmode", 1);
			if (cmpBean.getReminderZone() != null)
				cv.put("reminderzone", cmpBean.getReminderZone());
			if (cmpBean.getReminderResponseType() != null)
				cv.put("reminderresponsetype",
						cmpBean.getReminderResponseType());

			if (DBAccess.getdbHeler().isRecordExists(
					"select * from component where componentid='"
							+ cmpBean.getComponentId() + "'")) {

				DBAccess.getdbHeler().updateComponent(cv,
						"componentid='" + cmpBean.getComponentId() + "'");
			} else {
				DBAccess.getdbHeler().insertComponent(cv);
			}

		}
		if (vanishMode != null) {
			reminder.setVanishMode(vanishMode);
		}
		if (vanishValue != null) {
			reminder.setVanishValue(vanishValue);
		}
		if (comment != null) {
			reminder.setComment(comment);
		}
		if (hastoStream) {
			reminder.setDownloadType("2");

		} else {
			reminder.setDownloadType("2");
		}

		Log.d("ftp", "webserc" + reminder.getReminderResponseType());
		Log.d("ftp", "webserc");
		if (CallDispatcher.LoginUser != null) {
			WebServiceReferences.webServiceClient.ShareFiles(reminder);
		}
	}

	/*
	 * To send file to buddies (file sharing)
	 */
	public void sendshare(boolean flag, String username, String password,
			String buddieslist, ArrayList<String> SendbuddyList,
			ArrayList<String> uploadDatas, String componenttype,
			String comments, String filename, String from, CheckBox by_time,
			Spinner time_spinner, TextView ttl_result, TextView ttl_value,
			TextView time_input, boolean stream_enabled,
			CompleteListBean cmpBean) {
		AppMainActivity appMain = ((AppMainActivity) SingleInstance.contextTable
				.get("MAIN"));
		try {
			ConnectionBrokerServerBean cBean = appMain.cBean;
			FTPBean ftpBean = new FTPBean();
			Log.i("files", "router :: " + cBean.getRouter());
			ftpBean.setServer_ip(cBean.getRouter().split(":")[0]);
			ftpBean.setServer_port(40400);
			ftpBean.setFtp_username(username);
			ftpBean.setFtp_password(password);
			ftpBean.setOperation_type(1);
			ArrayList<String> b_list = new ArrayList<String>();
			b_list.addAll(SendbuddyList);
			ArrayList<String> u_list = new ArrayList<String>();
			u_list.addAll(uploadDatas);
			ftpBean.setDatas(u_list);
			ftpBean.setBuddiesList(b_list);
			ftpBean.setNoteType(componenttype);
			ftpBean.setComment(comments);
			if (ftpBean.getNoteType().equalsIgnoreCase("video")) {
				if (!filename.endsWith(".mp4"))
					filename = filename + ".mp4";
			}
			ftpBean.setStream_enabled(stream_enabled);
			ftpBean.setFile_path(filename);
			uploadData.add(filename);
			if (from.equalsIgnoreCase("sendFiles")) {
				if (by_time.isChecked()) {
					if (time_spinner.getSelectedItem().toString()
							.equalsIgnoreCase("Hour")
							|| time_input.getText().toString().length() > 0) {
						ftpBean.setVanish_mode("elapse");
						ttl_result.setText("");
						ttl_value.setText(time_input.getText().toString());
						ftpBean.setVanish_value(ttl_value.getText().toString());
						ttl_result.setText(SingleInstance.mainContext.getResources().getString(R.string.file_will_be_deleted_after)

								+ time_input.getText().toString()
								+ SingleInstance.mainContext.getResources().getString(R.string.hr_from_the_time_of_file_received)
);

					}

				} else if (ttl_value.getText().toString().length() > 0) {
					ftpBean.setVanish_mode("TTL");
					ftpBean.setVanish_value(ttl_value.getText().toString());
				}
			}
			ftpBean.setRequest_from("filessharing");
			ftpBean.setcBean(cmpBean);
			if (appMain.getFtpNotifier() != null)
				appMain.getFtpNotifier().addTasktoExecutor(ftpBean);

			if (WebServiceReferences.contextTable.containsKey("sendershare")) {
				((sendershare) WebServiceReferences.contextTable
						.get("sendershare")).finish();
			}
			if (WebServiceReferences.contextTable.containsKey("Component")) {
				if (flag)
					((ComponentCreator) WebServiceReferences.contextTable
							.get("Component")).finish();
			}
			send_multiple = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * To upload files using webservice 
	 */
	public void uploadFile(String username, String password,String componenttype,
			String filename, String contents,String componentpath,Object context1)
	{
        Log.i("FileUpload", "Inside CallDisp_UploadFile---> " +componentpath);

        String[] temp = new String[7];
		temp[0]=username;
		temp[1]=password;
		temp[2]=componenttype;
		temp[3]=filename;
		temp[4]=contents;
		File file = new File(componentpath);
		long length = (int) file.length();
		length = length/1024;
		temp[5]="file";
		temp[6]= String.valueOf(length);
		Log.i("FileUpload", "Inside CallDisp_UploadFile---> " +temp[6]);
		WebServiceReferences.webServiceClient.FileUpload(temp,context1);
		FileDetailsBean fBean=new FileDetailsBean();
		fBean.setFilename(filename);
		fBean.setFiletype(componenttype);
		fBean.setFilecontent(contents);
		fBean.setServicetype("Upload");
		SingleInstance.fileDetailsBean=fBean;
	}
	public void downloadFile(String username, String password,
							 String filename)
	{
		String[] dtemp = new String[3];
        String[] fname=filename.split(",");
        if(fname.length>0)
        {
            for(int i=0;i<fname.length;i++) {
                dtemp[0] = username;
                dtemp[1] = password;
                dtemp[2] = fname[i];
                Log.d("XP WSD", "User Name " + username + " Password " + password + " File Name " + filename);
                WebServiceReferences.webServiceClient.FileDownload(dtemp);
            }
        }else{
            dtemp[0]=username;
            dtemp[1]=password;
            dtemp[2]=filename;
            Log.d("XP WSD", "User Name "+username+" Password "+password+" File Name "+filename);
            WebServiceReferences.webServiceClient.FileDownload(dtemp);
        }


	}

	/*
	 * To upload files using webservice 
	 */

	public void showAlert(final String title, final String message) {
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					Context context = null;
					if (SingleInstance.contextTable.get("groupchat") != null) {
						context = SingleInstance.contextTable.get("groupchat");
					} else {
						context = SingleInstance.mainContext;
					}
					final AlertDialog myAlertDialog = new AlertDialog.Builder(
							context).create();
					myAlertDialog.setTitle(title);
					myAlertDialog.setMessage(message);
					myAlertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									myAlertDialog.dismiss();
								}
							});
					myAlertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void compareutilityresponse(final ArrayList<UtilityBean> list,
			final Context context, final LinearLayout lay) {
		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ArrayList<UtilityBean> nilldistancelist = new ArrayList<UtilityBean>();
					ArrayList<UtilityBean> distancem_list = new ArrayList<UtilityBean>();
					ArrayList<UtilityBean> distancekm_list = new ArrayList<UtilityBean>();
					for (UtilityBean utilityBean : list) {
						if (utilityBean.getDistance().equals("nil")
								|| utilityBean.getDistance().equals("0.0")
								|| utilityBean.getDistance().equalsIgnoreCase(
										"disabled"))
							nilldistancelist.add(utilityBean);
						else {
							String distance = String.format("%.3f", Double
									.parseDouble(utilityBean.getDistance()));
							if (distance.startsWith("0")) {
								double dist = Double.parseDouble(distance) * 1000;
								utilityBean.setDistance(Integer
										.toString((int) dist) + "m");
								distancem_list.add(utilityBean);
							} else {
								utilityBean.setDistance(distance + "km");
								distancekm_list.add(utilityBean);
							}
						}
					}
					if (distancem_list.size() > 0)
						Collections.sort(distancem_list,
								new DistanceComparator());
					if (distancekm_list.size() > 0)
						Collections.sort(distancekm_list,
								new DistanceComparator());
					if (nilldistancelist.size() > 0)
						Collections.sort(nilldistancelist,
								new BuddyComparator());

					list.clear();
					list.addAll(distancem_list);
					list.addAll(distancekm_list);
					list.addAll(nilldistancelist);
					handlerForCall.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (context instanceof UtilityBuyer)
								((UtilityBuyer) context).notifyResultSorted(
										list, lay);
							else if (context instanceof UtilitySeller)
								((UtilitySeller) context).notifyResultSorted(
										list, lay);
							else if (context instanceof UtilityServiceNeeder)
								((UtilityServiceNeeder) context)
										.notifyResultSorted(list, lay);
							else if (context instanceof UtilityServiceProvider)
								((UtilityServiceProvider) context)
										.notifyResultSorted(list, lay);

						}
					});

				}
			}).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int connectivityType() {
		try {

			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			boolean isWifi = manager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			if (isWifi) {
				return 2;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return 3;
	}

	public void startPlayer(Context contect) {
		try {

			Intent i = new Intent(contect, PlayerService.class);
			i.putExtra(PlayerService.EXTRA_PLAYLIST, "main");
			i.putExtra(PlayerService.EXTRA_SHUFFLE, true);
			contect.startService(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyCallHistoryToServer(String from, String to,
			String callType, String sessinId, String startTime, String endTime) {

		try {
			ArrayList<CallHistoryBean> arrayCallHistory = new ArrayList<CallHistoryBean>();

			CallHistoryBean chb = new CallHistoryBean();
			chb.setFrom(from);
			chb.setTo(to);
			chb.setSessionId(sessinId);
			chb.setStime(startTime);
			chb.setEtime(endTime);
			chb.setType(callType);
			chb.setNetworkState("2");
			chb.setLoginUserName(LoginUser);
			chb.setAutoCall("0");
			arrayCallHistory.add(chb);
			// WebServiceReferences.webServiceClient.callHistory(arrayCallHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void notifyCallHistoryToServerfromcallscreen(String from,
			String[] to, String callType, String sessinId, String startTime[],
			String endTime[], String[] autoCall) {
		try {

			ArrayList<CallHistoryBean> arrayCallHistory = new ArrayList<CallHistoryBean>();
			Log.d("Test", "@@@@_________@@@NotifyCallHistory@@From@@" + from
					+ " @@To@@" + to + " @@Calltype@@ " + callType
					+ " @@SessinId@@ " + sessinId + "StartTime" + startTime
					+ "Endtime" + endTime + " @@autocall@@ " + autoCall);
			for (int i = 0; i < to.length; i++) {

				CallHistoryBean chb = new CallHistoryBean();
				chb.setFrom(from);
				chb.setTo(to[i]);
				chb.setSessionId(sessinId);
				chb.setStime(startTime[i]);
				chb.setEtime(endTime[0]);
				chb.setType(callType);
				chb.setAutoCall(autoCall[i]);

				chb.setNetworkState(Integer
						.toString(CallDispatcher.networkState));
				chb.setLoginUserName(LoginUser);
				arrayCallHistory.add(chb);

			}

			// WebServiceReferences.webServiceClient.callHistory(arrayCallHistory);
		} catch (Exception e) {
			Log.i("callhistory", "" + "" + e.getMessage());
		}

	}

	public SignalingBean callconfernceUpdate(String selectedBuddy,
			String calltype, String strSessionId) {

		SignalingBean sbConf = new SignalingBean();
		try {

			BuddyInformationBean bib = null;

			for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
				if (!temp.isTitle()) {
					if (temp.getName().equalsIgnoreCase(selectedBuddy)) {
						bib = temp;
						break;
					}
				}
			}

			sbConf.setFrom(LoginUser);
			sbConf.setTo(selectedBuddy);
			sbConf.setType("0");
			sbConf.setSessionid(strSessionId);
			sbConf.setCallType(calltype);
			sbConf.setToSignalPort(bib.getSignalingPort());
			sbConf.setResult("0");
			sbConf.setTopublicip(bib.getExternalipaddress());
			sbConf.setTolocalip(bib.getLocalipaddress());
			sbConf.setToSignalPort(bib.getSignalingPort());
			sbConf.setChatid(CallDispatcher.LoginUser);
			sbConf.setHost(CallDispatcher.LoginUser);
			String participant=null;
			for(String temp:CallDispatcher.conferenceMembers){
				if(participant==null)
					participant=temp;
				else
					participant=participant+","+temp;
			}
			sbConf.setParticipants(participant+","+selectedBuddy);
			AppMainActivity.commEngine.makeConferenceCall(sbConf);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return sbConf;

	}

	public void rejectInComingCall(SignalingBean signBean) {
		try {
			String from = signBean.getFrom();
			String to = signBean.getTo();

			// BuddyInformationBean bib =
			// WebServiceReferences.buddyList.get(from);

			BuddyInformationBean bib = null;

			for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
				if (!temp.isTitle()) {
					if (temp.getName().equalsIgnoreCase(from)) {
						bib = temp;
						break;
					}
				}
			}

			signBean.setTopublicip(bib.getExternalipaddress());
			signBean.setTolocalip(bib.getLocalipaddress());
			signBean.setLocalip(getLocalipaddsress());
			signBean.setPublicip(getPublicipaddress());
			signBean.setToSignalPort(bib.getSignalingPort());
			signBean.setFrom(to);
			signBean.setTo(from);
			signBean.setType("1");
			signBean.setResult("1");
			AppMainActivity.commEngine.rejectCall(signBean);
			notifyCallHistoryToServer(signBean.getTo(), signBean.getTo(),
					discardCallType(signBean.getCallType()),
					signBean.getSessionid(), getCurrentDateTime(),
					getCurrentDateTime());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private String discardCallType(String calltype) {
		try {
			if (calltype.equalsIgnoreCase("AC")) {
				return "131";
			} else if (calltype.equalsIgnoreCase("AP")) {
				return "132";
			} else if (calltype.equalsIgnoreCase("ABC")) {
				return "133";
			} else if (calltype.equalsIgnoreCase("VC")) {
				return "231";
			} else if (calltype.equalsIgnoreCase("VP")) {
				return "232";
			} else if (calltype.equalsIgnoreCase("VBC")
					|| calltype.equalsIgnoreCase("SS")) {
				return "233";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Missed call";

	}

	public void notifyIncomingCall(final SignalingBean signBean) {

		try {

			testhandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i("call123", "incoming call1");
					try {
						if (!SingleInstance.mainContext.isAutoAcceptEnabled(
								CallDispatcher.LoginUser, CallDispatcher
										.getUser(signBean.getFrom(),
												signBean.getTo()))) {
							startRingTone();
						}
						whenCallInitiated();
						CallDispatcher.isCallAcceptRejectOpened = true;
						isIncomingAlert = true;
						// Context context = SipNotificationListener
						// .getCurrentContext();
						Intent intentmain = null;
						if (isApplicationInBackground(context)) {
							intentmain = new Intent(context,
									AppMainActivity.class);
							intentmain
									.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							intentmain
									.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							intentmain
									.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
							intentmain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

							context.getApplicationContext().startActivity(
									intentmain);
						}
						Log.i("call123", "incoming call2");
						if (signBean.getCallType().equalsIgnoreCase("AC")
								|| signBean.getCallType()
										.equalsIgnoreCase("VC")
								|| signBean.getCallType().equalsIgnoreCase(
										"ABC")
								|| signBean.getCallType().equalsIgnoreCase(
										"VBC")
								|| signBean.getCallType()
										.equalsIgnoreCase("AP")
								|| signBean.getCallType()
										.equalsIgnoreCase("VP")
								|| signBean.getCallType()
										.equalsIgnoreCase("SS")) {

							PowerManager pm = (PowerManager) context
									.getSystemService(Context.POWER_SERVICE);
							wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
									| PowerManager.ACQUIRE_CAUSES_WAKEUP,
									"inCommingCallAlert");
							wl.acquire();

							// Log.i("calltest",
							// "signBean.getBs_parentid() :"+signBean.getBs_parentid());
							// if(signBean.getBs_parentid()!=null){
							// signBean.setStartTime(getCurrentDateandTime());
							// DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
							// signBean);
							// }
							//
							Intent intent = new Intent(SingleInstance.mainContext, NotificationReceiver.class);
							PendingIntent pIntent = PendingIntent.getActivity(SingleInstance.mainContext, (int) System.currentTimeMillis(), intent, 0);
							Notification n  = new Notification.Builder(SingleInstance.mainContext)
									.setContentTitle(" Call from " + signBean.getFrom())
									.setSmallIcon(R.drawable.logo_snazmed)
									.setContentIntent(pIntent)
									.setAutoCancel(true).build();


							NotificationManager notificationManager =
									(NotificationManager) SingleInstance.mainContext.getSystemService(SingleInstance.mainContext.NOTIFICATION_SERVICE);

							notificationManager.notify(0, n);
							AppMainActivity appMainActivity=(AppMainActivity) SingleInstance.contextTable
									.get("MAIN");
							appMainActivity.closingActivity();

							checkandcloseDialog();

							FragmentManager fm =
									AppReference.mainContext.getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							inCommingCallAlert incommingCallAlert = inCommingCallAlert
									.getInstance(context);
							Bundle bundle = new Bundle();
							bundle.putSerializable("bean", signBean);
							incommingCallAlert.setArguments(bundle);
							ft.replace(R.id.activity_main_content_fragment, incommingCallAlert);
							ft.commitAllowingStateLoss();

//							Intent intent = new Intent(context,
//									inCommingCallAlert.class);
//							intent.putExtra("bean", signBean);
//							context.startActivity(intent);
							Log.i("call123", "incoming call3");
						}
						Log.i("call123", "incoming call");
					} catch (Exception e) {
						Log.e("thread",
								"Wxception in listall notification"
										+ e.toString());
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			testhandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (SingleInstance.contextTable
							.containsKey("IncomingCallAlert")) {

						isCallInitiate = false;
						isIncomingCall = false;
						isIncomingAlert = false;

						rejectInComingCall(signBean);
						stopRingTone();
						currentSessionid = null;
//						inCommingCallAlert ICA = (inCommingCallAlert) SingleInstance.contextTable
//								.get("IncomingCallAlert");
//						ICA.finish();
						FragmentManager fm =
								AppReference.mainContext.getSupportFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						ContactsFragment contactsFragment = ContactsFragment
								.getInstance(context);
						ft.replace(R.id.activity_main_content_fragment,
								contactsFragment);
						ft.commitAllowingStateLoss();
						CallDispatcher.conferenceMembers.clear();
						CallDispatcher.buddySignall.clear();
					}

				}
			});

		} catch (Exception e) {
			// Log.d("REJ", "whgw " + e.toString());
		}
	}

	public void whenCallInitiated() {
		try {
			audioManager.requestAudioFocus(null,
					AudioManager.STREAM_VOICE_CALL,
					AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
							| AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
			isAudioPaused = true;

		} catch (Exception e) {
		}

	}

	public void showMissedcallAlert(final SignalingBean sbean,
			final Context context, final int count) {
		// TODO Auto-generated method stub
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (SingleInstance.instanceTable
							.containsKey("alertscreen")) {
//						inCommingCallAlert alert = (inCommingCallAlert) SingleInstance.contextTable
//								.get("alertscreen");
//						alert.finishactivity();
						FragmentManager fm =
								AppReference.mainContext.getSupportFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						ContactsFragment contactsFragment = ContactsFragment
								.getInstance(context);
						ft.replace(R.id.activity_main_content_fragment,
								contactsFragment);
						ft.commitAllowingStateLoss();

					}
					ProfileBean bean = DBAccess.getdbHeler().getProfileDetails(sbean.getFrom());
					String fullname=bean.getFirstname()+" "+bean.getLastname();
					if (mdialog != null) {

						if (mdialog.isShowing())
							mdialog.cancel();

						mdialog = new AlertDialog.Builder(context).create();

						mdialog.setMessage("(" + Integer.toString(count) + ")"
								+ " missed call from " + fullname);
						mdialog.setCancelable(true);
						mdialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (WebServiceReferences.missedcallCount
												.containsKey(sbean.getFrom()))
											WebServiceReferences.missedcallCount
													.remove(sbean.getFrom());
										isCallignored = false;
										mdialog.cancel();
										mdialog = null;
									}
								});

						mdialog.show();
					} else if (mdialog == null) {
						mdialog = new AlertDialog.Builder(context).create();

						mdialog.setMessage("(" + Integer.toString(count) + ")"
								+ "missed call from " +fullname );
						mdialog.setCancelable(true);
						mdialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										isCallignored = false;
										mdialog = null;
									}
								});

						mdialog.show();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});
	}

	public void ShowError(String Title, String Message, Context context) {
		try {
			AlertDialog confirmation = new AlertDialog.Builder(context)
					.create();

			confirmation.setMessage(Message);
			confirmation.setCancelable(true);
			confirmation.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});

			confirmation.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updatecallDuration() {
		try {
			Object objCallScreen = SingleInstance.instanceTable
					.get("callscreen");
			if (objCallScreen instanceof AudioCallScreen) {
				AudioCallScreen acalObj = (AudioCallScreen) objCallScreen;
				acalObj.resetCallDuration();
			} else if (objCallScreen instanceof VideoCallScreen) {
				VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;
				acalObj.resetCallDuration();

			} else if (objCallScreen instanceof AudioPagingSRWindow) {
				AudioPagingSRWindow acalObj = (AudioPagingSRWindow) objCallScreen;
				acalObj.resetCallDuration();
			} else if (objCallScreen instanceof VideoPagingSRWindow) {
				VideoPagingSRWindow acalObj = (VideoPagingSRWindow) objCallScreen;
				acalObj.resetCallDuration();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String failedCallType(String calltype) {
		try {
			if (calltype.equalsIgnoreCase("AC")) {
				return "121";
			} else if (calltype.equalsIgnoreCase("AP")) {
				return "122";
			} else if (calltype.equalsIgnoreCase("ABC")) {
				return "123";
			} else if (calltype.equalsIgnoreCase("VC")) {
				return "221";
			} else if (calltype.equalsIgnoreCase("VP")) {
				return "222";
			} else if (calltype.equalsIgnoreCase("VBC")
					|| callType.equalsIgnoreCase("SS")) {
				return "223";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Missed call";

	}

	public String diffBetweenTwoTimes(String time1, String time2) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yy hh:mm:ss a");
			Date date1 = null;
            try {
                date1 = formatter.parse(time1);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
            Date date2 = null;
            try {
                date2 = formatter.parse(time2);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
            if (date1 != null && date2 != null) {
				long diff = date2.getTime() - date1.getTime();

				int seconds = (int) (diff / 1000) % 60;
				int minutes = (int) ((diff / (1000 * 60)) % 60);
				int hours = (int) ((diff / (1000 * 60 * 60)) % 24);

				return checkLength(hours) + ":" + checkLength(minutes) + ":"
						+ checkLength(seconds);
			} else {
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	private String checkLength(int no) {
		String str = Integer.toString(no);
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}

	public void notifyType2Received() {
		handlerForCall.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("AAAA", "notifyType2Received  ==> " );
				if (SingleInstance.instanceTable.containsKey("connection")) {
					Log.d("AAAA", "notifyType2Received ifpart ==> " );
					((CallConnectingScreen) SingleInstance.instanceTable
							.get("connection")).notifyType2Received();
				} else {
					Log.d("AAAA", "notifyType2Received elsepart ==> " );
					String buddyx = getUser(sb.getFrom(), sb.getTo());

					CallDispatcher.conferenceMembersTime.put(buddyx,
							getCurrentDateTime());
					Object objCallScreen = SingleInstance.instanceTable
							.get("callscreen");
					if (objCallScreen != null
							&& objCallScreen instanceof VideoCallScreen) {
						VideoCallScreen acalObj = (VideoCallScreen) objCallScreen;

						acalObj.updateBuddyNames(sb.getFrom());

					} else if (objCallScreen != null
							&& objCallScreen instanceof AudioCallScreen)
						((AudioCallScreen) objCallScreen).notifyType2Received();
					else if (objCallScreen != null
							&& objCallScreen instanceof AudioPagingSRWindow)
						((AudioPagingSRWindow) objCallScreen)
								.notifyType2Received();
				}
				Log.i("Test", "CALL NOTIFICATION>>>>>"+getCurrentDateandTime());
				CallDispatcher.sb.setStartTime(getCurrentDateandTime());
			}
		}, 1500);
	}

	public boolean Checkbetweendateandtime(String pickdate, String startdate,
			String enddate) {

		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Date currentDateTime = null;
			try {
				currentDateTime = (Date) formatter.parse(pickdate);
				Log.e("welcome", "Pick Between Date is Printing---->"
						+ currentDateTime.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Date StartDate = null;
			try {
				StartDate = (Date) formatter.parse(startdate);
				Log.e("welcome",
						"Default Between Starting Date is Printing--->"
								+ StartDate.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Date EndDate = null;
			try {
				EndDate = (Date) formatter.parse(enddate);
				Log.e("welcome", "Default Between Date is Printing--->"
						+ EndDate.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (currentDateTime.getTime() > StartDate.getTime()
					&& currentDateTime.getTime() < EndDate.getTime()) {
				return true;
			}

			else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean CheckGreaterDateandTime(String pickdate, String gdate) {

		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Date currentDateTime = null;
			try {
				currentDateTime = (Date) formatter.parse(pickdate);
				Log.e("welcome", "Pick Greater Date is Printing---->"
						+ currentDateTime.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Date GreaterDate = null;
			try {
				GreaterDate = (Date) formatter.parse(gdate);
				Log.e("welcome", "Default Greater Date is Printing--->"
						+ GreaterDate.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (currentDateTime.getTime() <= GreaterDate.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean CheckLessDateandTime(String pickdate, String ldate) {
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Date currentDateTime = null;
			try {
				currentDateTime = (Date) formatter.parse(pickdate);
				Log.e("welcome", "Pick Lesser Date is Printing---->"
						+ currentDateTime.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			Date LessDate = null;
			try {
				LessDate = (Date) formatter.parse(ldate);
				Log.e("welcome", "Default Lesser Date is Printing--->"
						+ LessDate.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (LessDate.getTime() <= currentDateTime.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Bitmap setIconImage(Bitmap formIcon, String name) {

		Log.i("FORMSOPTMZ", "Inside setIcon Image===>" + name);

		if (name.length() > 0) {
			File extStore = Environment.getExternalStorageDirectory();
			if (!name.startsWith("FormIcon")) {
				if (!name.startsWith("MPD")) {
					name = "MPD_" + name;
				}
			}

			File myFile = new File(extStore.getAbsolutePath() + "/COMMedia/"
					+ name);
			Log.i("IOS", "????????????? setIconImage" + name);

			if (!myFile.exists()) {
				Log.i("IOS", "????????????? File not exists" + name);
				formIcon = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.form_icon);
				downloadOfflineresponse(name, "", "forms", "");

			} else {
				Log.i("IOS", "????????????? File exists" + name);
				String blob_path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/COMMedia/" + name;
				if (name.contains("MPD_")) {
					Log.i("IOS", "????????????? contains MPD exists" + name);

					formIcon = ResizeImage(blob_path);
					if (formIcon != null)
						formIcon = Bitmap.createScaledBitmap(formIcon, 200,
								150, false);

				}

				else if (name.contains("FormIcon_")) {
					Log.i("IOS", "????????????? contains MPD exists" + name);

					formIcon = ResizeImage(blob_path);
					Log.i("welcome", "Resize image path=" + blob_path);

					if (formIcon != null)
						formIcon = Bitmap.createScaledBitmap(formIcon, 200,
								150, false);
					else {
						formIcon = BitmapFactory.decodeResource(
								context.getResources(), R.drawable.form_icon);
					}
				}
			}
		} else {
			formIcon = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.form_icon);
		}

		return formIcon;

	}

	private void handleDataForms(Object obj) {
		try {
			HashMap<String, String> dtype = new HashMap<String, String>();
			if (obj instanceof FormsBean) {
				Log.i("frmsdb", "inside handle get content");
				dtype.clear();
				final FormsBean bean = (FormsBean) obj;
				String[] fields1 = bean.getFormFields().split(",");
				ArrayList<String> field_list = new ArrayList<String>();

				for (int i = 0; i < fields1.length - 1; i++) {
					String split_string[] = fields1[i].split(":");
					field_list.add(split_string[0]);
					if (split_string[1].equalsIgnoreCase("VARCHAR(45)"))
						dtype.put(split_string[0], "nvarchar(20)");
					else if (split_string[1].equalsIgnoreCase("int(10)"))
						dtype.put(split_string[0], "INTEGER");
					else if (split_string[1].equalsIgnoreCase("LONGBLOB"))
						dtype.put(split_string[0], "BLOB");
				}
				if (getdbHeler(context).isFormtableExists(
						bean.getFormName() + "_" + bean.getFormId())) {

					Log.i("forms",
							"insideeeeeeeeeeeeeee  formmmmmm existsssssss");
					String tid_split[] = fields1[fields1.length - 1].toString()
							.split(":");
					field_list.add(tid_split[0]);
					if (bean.getFormRecords() != null) {
						for (int rec = 0; rec < bean.getFormRecords().size(); rec++) {
							String[] field_values = bean.getFormRecords().get(
									rec);
							ContentValues cv = new ContentValues();

							for (int idx = 0; idx < field_values.length; idx++) {
								Log.i("thread",
										"Field name" + field_list.get(idx));
								Log.i("thread", "Field value"
										+ field_values[idx]);

								String columnname = "[" + field_list.get(idx)
										+ "]";
								if (field_list.get(idx).equalsIgnoreCase(
										"tableid")) {
									cv.put(columnname, field_values[idx]);
								} else {
									if (dtype.get(field_list.get(idx))
											.equalsIgnoreCase("BLOB"))
										cv.put(field_list.get(idx),
												decodeBase64(field_values[idx]));
									else if (dtype.get(field_list.get(idx))
											.equalsIgnoreCase("INTEGER"))
										cv.put(columnname, Integer
												.parseInt(field_values[idx]));
									else if (dtype.get(field_list.get(idx))
											.equalsIgnoreCase("nvarchar(20)"))
										cv.put(columnname, field_values[idx]);
								}
							}
							cv.put("[status]", "1");

							getdbHeler(context).update(
									"[" + bean.getFormName() + "_"
											+ bean.getFormId() + "]", cv,
									bean.getEditRecordId());

						}

						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Log.i("thread",
										"Activity existance/////////"
												+ WebServiceReferences.contextTable
														.containsKey("formactivity"));
								if (SingleInstance.contextTable
										.containsKey("forms")) {
									FormsFragment quickActionFragment = FormsFragment
											.newInstance(context);

									quickActionFragment.populateLists();

								}
								if (WebServiceReferences.contextTable
										.containsKey("frmviewer")) {
									FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
											.get("frmviewer");
									if (frm_creator.isShowingCurrentForm(bean
											.getFormId())) {
										frm_creator.refreshList();
									}
								}
							}
						});

					}

				} else {
					Log.i("forms", "insideeeeeeeeeeeeeee  elsesssssss");

					field_list.add("status");
					dtype.put("status", "nvarchar(20)");
					field = new ArrayList<String>();

					for (int i = 0; i < field_list.size(); i++) {
						String sub = "[" + field_list.get(i) + "]";
						Log.i("name", sub);

						field.add(sub);
					}
					String tbl = "[" + bean.getFormName() + "_"
							+ bean.getFormId() + "]";
					if (getdbHeler(context).createFormTable(field, tbl, dtype)) {

						String tid_split[] = fields1[fields1.length - 1]
								.toString().split(":");
						String status = "1";

						field_list.add(tid_split[0]);
						if (bean.getFormRecords() != null) {
							for (int rec = 0; rec < bean.getFormRecords()
									.size(); rec++) {
								String[] field_values = bean.getFormRecords()
										.get(rec);
								ContentValues cv = new ContentValues();

								for (int idx = 0; idx < field_values.length; idx++) {
									Log.i("thread",
											"Field name" + field_list.get(idx));
									Log.i("thread", "Field value"
											+ field_values[idx]);
									String columnname = "["
											+ field_list.get(idx) + "]";

									if (field_list.get(idx).equalsIgnoreCase(
											"tableid")) {
										cv.put(columnname, field_values[idx]);
									} else {
										if (dtype.get(field_list.get(idx))
												.equalsIgnoreCase("BLOB")) {
											cv.put(field_list.get(idx),
													decodeBase64(field_values[idx]));
										} else if (dtype.get(
												field_list.get(idx))
												.equalsIgnoreCase("INTEGER")) {
											cv.put(columnname,
													Integer.parseInt(field_values[idx]));
										} else if (dtype.get(
												field_list.get(idx)).contains(
												"2")) {
											cv.put(columnname,
													field_values[idx]);
										}
									}
								}

								cv.put("[status]", status);
								if (getdbHeler(context).insertForRecords(
										bean.getFormName() + "_"
												+ bean.getFormId(), cv)) {

								}
							}

							handlerForCall.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Log.i("thread",
											"Activity existance/////////"
													+ WebServiceReferences.contextTable
															.containsKey("formactivity"));
									if (SingleInstance.contextTable
											.containsKey("forms")) {
										FormsFragment quickActionFragment = FormsFragment
												.newInstance(context);

										quickActionFragment.populateLists();

									}
									if (WebServiceReferences.contextTable
											.containsKey("frmviewer")) {
										FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
												.get("frmviewer");
										if (frm_creator
												.isShowingCurrentForm(bean
														.getFormId())) {
											frm_creator.refreshList();
										}
									}
								}
							});

						}

					}
				}

			} else if (obj instanceof WebServiceBean) {
				Log.e("thread", "getdata at background came to else part");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void handleGetDataForms(Object obj) {

		HashMap<String, String> dtype = new HashMap<String, String>();

		String tableID = null;
		if (obj instanceof Formsinfocontainer) {
			dtype.clear();
			final Formsinfocontainer bean = (Formsinfocontainer) obj;
			String[] fields1 = bean.getForm_fields().split(",");
			field = new ArrayList<String>();
			ArrayList<String> field_list = new ArrayList<String>();
			for (int i = 0; i < fields1.length - 1; i++) {
				String split_string[] = fields1[i].split(":");
				String sub = "[" + split_string[0] + "]";
				field_list.add(split_string[0]);
				field.add(sub);
				Log.i("new", "sub" + sub);

				if (split_string[1].contains("4")) {

					dtype.put(split_string[0], "nvarchar(20)");

				} else if (split_string[1].contains("1")) {

					dtype.put(split_string[0], "INTEGER");
				} else if (split_string[1].equalsIgnoreCase("LONGBLOB")) {
					dtype.put(split_string[0], "BLOB");
				} else {

					dtype.put(split_string[0], "nvarchar(20)");
				}

			}

			field_list.add("status");
			dtype.put("status", "nvarchar(20)");
			field.add("[status]");
			Log.i("new", "recordate " + "recorddate");
			Log.i("new", "status" + "status");

			String tbl = "[" + bean.getForm_name() + "_" + bean.getForm_id()
					+ "]";

			if (getdbHeler(context).isFormtableExists(
					bean.getForm_name() + "_" + bean.getForm_id())) {

				String tid_split[] = fields1[fields1.length - 1].toString()
						.split(":");
				field_list.remove("status");
				field_list.add(tid_split[0]);

				if (bean.getRec_list() != null) {

					for (int rec = 0; rec < bean.getRec_list().size(); rec++) {

						FormRecordsbean field_values = bean.getRec_list().get(
								rec);

						HashMap<String, String> RecordInfo = field_values
								.getRecords_info();
						ContentValues cv = new ContentValues();

						if (field_values.getIsDeleted_record()) {

							if (getdbHeler(context).isRecordExists(
									"select * from " + tbl + "  where tableid="
											+ RecordInfo.get("tableid"))) {
								String query = "delete from" + tbl
										+ " where tableid='"
										+ RecordInfo.get("tableid") + "'";

								if (getdbHeler(context).ExecuteQuery(query)) {

								}
							}

						} else {

							for (int i = 0; i < RecordInfo.size(); i++) {

								String columnane = "[" + field_list.get(i)
										+ "]";

								if (field_list.get(i).equalsIgnoreCase(
										"tableid")) {
									tableID = RecordInfo.get(field_list.get(i));
									cv.put(columnane,
											RecordInfo.get(field_list.get(i)));
								} else {

									if (dtype.get(field_list.get(i))
											.equalsIgnoreCase("BLOB")) {
										cv.put(columnane,
												decodeBase64(RecordInfo
														.get(field_list.get(i))));
									} else if (dtype.get(field_list.get(i))
											.equalsIgnoreCase("INTEGER")) {

										if (RecordInfo.get(field_list.get(i)) != null) {

											if (RecordInfo.get(
													field_list.get(i)).length() > 0) {
												cv.put(columnane,
														Integer.parseInt(RecordInfo.get(field_list
																.get(i))));

											}
										} else {

											cv.put(columnane, 0);
										}

									} else if (dtype.get(field_list.get(i))
											.contains("2")) {
										if (RecordInfo.get(field_list.get(i)) != null) {

											if (RecordInfo.get(
													field_list.get(i))
													.contains("MPD_")
													|| RecordInfo.get(
															field_list.get(i))
															.contains("MAD_")
													|| RecordInfo.get(
															field_list.get(i))
															.contains("MVD_")) {

												File extStore = Environment
														.getExternalStorageDirectory();
												File myFile = new File(
														extStore.getAbsolutePath()
																+ "/COMMedia/"
																+ RecordInfo
																		.get(field_list
																				.get(i)));

												if (!myFile.exists()) {
													downloadOfflineresponse(
															RecordInfo.get(field_list
																	.get(i)),
															"", "forms", "");

												}
												cv.put(columnane, RecordInfo
														.get(field_list.get(i)));

											} else {
												cv.put(columnane, RecordInfo
														.get(field_list.get(i)));

											}
										} else {

											cv.put(columnane, "");

										}

									}

								}
							}

							cv.put("[status]", "1");

							if (getdbHeler(context).record(
									bean.getForm_name() + "_"
											+ bean.getForm_id(), tableID)) {

								getdbHeler(context).update(
										"[" + bean.getForm_name() + "_"
												+ bean.getForm_id() + "]", cv,
										tableID);

							} else {

								getdbHeler(context).insertForRecords(
										bean.getForm_name() + "_"
												+ bean.getForm_id(), cv);

							}

						}
					}

				}

			}

			else {

				if (getdbHeler(context).createFormTable(field, tbl, dtype)) {
					String tid_split[] = fields1[fields1.length - 1].toString()
							.split(":");
					field_list.remove("status");
					field_list.add(tid_split[0]);

					if (bean.getRec_list() != null) {

						for (int rec = 0; rec < bean.getRec_list().size(); rec++) {

							FormRecordsbean recordsBean = bean.getRec_list()
									.get(rec);
							HashMap<String, String> field_values = recordsBean
									.getRecords_info();

							ContentValues cv = new ContentValues();
							if (recordsBean.getIsDeleted_record()) {

								if (getdbHeler(context).isRecordExists(
										"select * from " + tbl
												+ "  where tableid="
												+ field_values.get("tableid"))) {
									String query = "delete from" + tbl
											+ " where tableid='"
											+ field_values.get("tableid") + "'";

									if (getdbHeler(context).ExecuteQuery(query)) {

									}
								}

							} else {
								for (int i = 0; i < field_values.size(); i++) {
									String columnane = "[" + field_list.get(i)
											+ "]";

									if (field_list.get(i).equalsIgnoreCase(
											"tableid")) {
										tableID = field_values.get(field_list
												.get(i));
										cv.put(columnane, field_values
												.get(field_list.get(i)));
									} else {

										if (dtype.get(field_list.get(i))
												.equalsIgnoreCase("BLOB")) {
											cv.put(columnane,
													decodeBase64(field_values
															.get(field_list
																	.get(i))));
										} else if (dtype.get(field_list.get(i))
												.equalsIgnoreCase("INTEGER")) {

											if (field_values.get(field_list
													.get(i)) != null) {

												if (field_values.get(
														field_list.get(i))
														.length() > 0) {
													cv.put(columnane,
															Integer.parseInt(field_values
																	.get(field_list
																			.get(i))));

												}
											} else {

												cv.put(columnane, 0);
											}

										} else if (dtype.get(field_list.get(i))
												.contains("2")) {

											if (field_values.get(field_list
													.get(i)) != null) {

												if (field_values.get(
														field_list.get(i))
														.contains("MPD_")
														|| field_values
																.get(field_list
																		.get(i))
																.contains(
																		"MAD_")
														|| field_values
																.get(field_list
																		.get(i))
																.contains(
																		"MVD_")) {

													File extStore = Environment
															.getExternalStorageDirectory();
													File myFile = new File(
															extStore.getAbsolutePath()
																	+ "/COMMedia/"
																	+ field_values
																			.get(field_list
																					.get(i)));

													if (!myFile.exists()) {
														downloadOfflineresponse(
																field_values
																		.get(field_list
																				.get(i)),
																"", "forms", "");

													}
													cv.put(columnane,
															field_values
																	.get(field_list
																			.get(i)));

												} else {
													cv.put(columnane,
															field_values
																	.get(field_list
																			.get(i)));

												}
											} else {

												cv.put(columnane, "");

											}

										}

									}
								}

								cv.put("[status]", "1");

								getdbHeler(context).insertForRecords(
										bean.getForm_name() + "_"
												+ bean.getForm_id(), cv);
							}
						}

					}

				}
			}

			if (WebServiceReferences.contextTable.containsKey("frmviewer")) {
				FormViewer formviewer = (FormViewer) WebServiceReferences.contextTable
						.get("frmviewer");
				if (formviewer.isShowingCurrentForm(bean.getForm_id())) {
					formviewer.refreshList();
				}

			}

		} else if (obj instanceof WebServiceBean) {
		}
	}

	private void handleDeleteForms(Object obj) {
		if (obj instanceof FormsBean) {
			FormsBean bean = (FormsBean) obj;
			if (getdbHeler(context).isFormExists(bean.getFormId(),
					bean.getUserName())) {
				String del_row = "delete from formslookup where tableid='"
						+ bean.getFormId() + "' and owner='"
						+ bean.getUserName() + "'";

				if (getdbHeler(context).ExecuteQuery(del_row)) {
					if (getdbHeler(context).isFormtableExists(
							bean.getFormName() + "_" + bean.getFormId())) {
						String del_tbl = "DROP TABLE IF EXISTS'"
								+ bean.getFormName() + "_" + bean.getFormId()
								+ "'";

						String del_forminfo = "delete from forminfo where tablename='["
								+ bean.getFormName()
								+ "_"
								+ bean.getFormId()
								+ "]'";

						getdbHeler(context).ExecuteQuery(del_forminfo);

						if (getdbHeler(context).ExecuteQuery(del_tbl)) {
							handlerForCall.post(new Runnable() {

								@Override
								public void run() {

									if (SingleInstance.contextTable
											.containsKey("forms")) {
										FormsFragment quickActionFragment = FormsFragment
												.newInstance(context);

										quickActionFragment
												.populateListAfterDelete();

									}

									if (WebServiceReferences.contextTable
											.containsKey("appsview")) {
										AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
												.get("appsview");
										frm_activity.refreshIcon();
									}
									if (WebServiceReferences.contextTable
											.containsKey("frmviewer")) {

										FormViewer frm_activity = (FormViewer) WebServiceReferences.contextTable
												.get("frmviewer");
										frm_activity.finish();

									}
									if (WebServiceReferences.contextTable
											.containsKey("frmreccreator")) {

										FormRecordsCreators frm_activity = (FormRecordsCreators) WebServiceReferences.contextTable
												.get("frmreccreator");
										frm_activity.finish();
									}

								}
							});
						}

						String del = "delete from forminfo where tablename='["
								+ bean.getFormName() + "_" + bean.getFormId()
								+ "]'";
						getdbHeler(context).ExecuteQuery(del);

						String del_settings = "delete from formsettings where formid='"
								+ bean.getFormId() + "'";
						getdbHeler(context).ExecuteQuery(del_settings);

					} else {

					}
				}
			}
		} else if (obj instanceof WebServiceBean) {
			WebServiceBean bn = (WebServiceBean) obj;
			Log.i("thread", "delete form result" + bn.getText());
		}
	}

	private void handleDeleteFormsRecords(Object obj) {

		if (obj instanceof FormsBean) {
			final FormsBean bean = (FormsBean) obj;

			if (getdbHeler(context).isFormtableExists(
					bean.getFormName() + "_" + bean.getFormId())) {
				String del_row = "delete from " + "[" + bean.getFormName()
						+ "_" + bean.getFormId() + "]" + " where tableid='"
						+ bean.getDeletedRecordId() + "'";

				if (getdbHeler(context).ExecuteQuery(del_row)) {

					handlerForCall.post(new Runnable() {

						@Override
						public void run() {

							String strUpdateCount = "update formslookup set rowcount='"
									+ bean.getcount()
									+ "',status='"
									+ "1"
									+ "' where tableid='"
									+ bean.getFormId()
									+ "'";

							getdbHeler(context).ExecuteQuery(strUpdateCount);
							if (SingleInstance.contextTable
									.containsKey("forms")) {
								FormsFragment quickActionFragment = FormsFragment
										.newInstance(context);

								quickActionFragment.populateLists();

							}
							if (WebServiceReferences.contextTable
									.containsKey("frmviewer")) {
								FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
										.get("frmviewer");

								frm_creator.refreshList();

							}

							if (WebServiceReferences.contextTable
									.containsKey("appsview")) {
								AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
										.get("appsview");
								frm_activity.refreshIcon();

							}
						}
					});

				}
			}

		} else if (obj instanceof WebServiceBean) {
			WebServiceBean bn = (WebServiceBean) obj;
		}
	}

	public byte[] decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		Bitmap bmp = BitmapFactory.decodeByteArray(decodedByte, 0,
				decodedByte.length);

		Bitmap immagex = bmp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		return b;

	}

	private void processUdpForms() {

		for (int i = 0; i < settingsConfig.size(); i++) {

			if (settingsConfig.get(i) instanceof FormsListBean) {

				final FormsListBean bean = (FormsListBean) settingsConfig
						.get(i);

				SingleInstance.mainContext.formnames = bean.getForm_name();

				if (bean.getMode() != null
						&& bean.getMode().equalsIgnoreCase("yes")) {
					// if (bean.getAttribute().equalsIgnoreCase("yes")) {
					formname = bean.getForm_name() + "_" + bean.getFormId();
					WebServiceReferences.webServiceClient.GetAttributecontent(
							bean.getForm_owner(), bean.getFormId(),
							SingleInstance.mainContext, "no");

					// }

					if (!getdbHeler(context).isFormExists(bean.getFormId(),
							bean.getForm_owner())) {

						String formicon;
						String formdescription;

						if (bean.getFormicon().length() == 0) {
							formicon = "";
						} else {
							formicon = bean.getFormicon();
							File extStore = Environment
									.getExternalStorageDirectory();
							File myFile = new File(extStore.getAbsolutePath()
									+ "/COMMedia/" + formicon);

							if (!myFile.exists()) {
								downloadOfflineresponse(formicon, "", "forms",
										"");
							}
						}

						if (bean.getFormdescription().length() == 0) {
							formdescription = "";
						} else {

							formdescription = bean.getFormdescription();

						}

						String insertQuery = "insert into formslookup(owner,tablename,tableid,rowcount,formtime,status,Formiconname,Formdescription)"
								+ "values('"
								+ bean.getForm_owner()
								+ "','"
								+ bean.getForm_name()
								+ "','"
								+ bean.getFormId()
								+ "','"
								+ bean.getNoofRows()
								+ "','"
								+ bean.getFormtime()
								+ "','"
								+ "1"
								+ "','"
								+ formicon
								+ "','"
								+ formdescription
								+ "')";

						if (getdbHeler(context).ExecuteQuery(insertQuery)) {

							if (SingleInstance.contextTable
									.containsKey("forms")) {
								FormsFragment quickActionFragment = FormsFragment
										.newInstance(context);

								quickActionFragment.populateLists();

							}
							if (WebServiceReferences.contextTable
									.containsKey("frmviewer")) {
								Log.i("FORMOPT", "udp FORM VIEWER TRUE===>");

								FormViewer frm_activity = (FormViewer) WebServiceReferences.contextTable
										.get("frmviewer");
								frm_activity.showConfirmation(
										CallDispatcher.LoginUser,
										bean.getFormId(), bean.getFormtime(),
										false);

							}

							else {

								WebServiceReferences.webServiceClient
										.Getcontent(CallDispatcher.LoginUser,
												bean.getFormId(), "", context);

							}
							if (WebServiceReferences.contextTable
									.containsKey("appsview")) {

								AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
										.get("appsview");
								frm_activity.refreshIcon();

							}

						}
					} else {

						if (getdbHeler(context).isFormtableExists(
								bean.getForm_name() + "_" + bean.getFormId())) {

							String recordtime = getdbHeler(context)
									.getRecordtime(bean.getFormId());

							if (!recordtime.equals(bean.getFormtime())) {

								String strUpdateCount = "update formslookup set rowcount='"
										+ bean.getNoofRows()
										+ "',formtime='"
										+ bean.getFormtime()
										+ "' where tableid='"
										+ bean.getFormId() + "'";

								if (getdbHeler(context).ExecuteQuery(
										strUpdateCount)) {

									if (WebServiceReferences.contextTable
											.containsKey("appsview")) {
										AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
												.get("appsview");
										frm_activity.refreshIcon();

									}

									if (SingleInstance.contextTable
											.containsKey("forms")) {
										FormsFragment quickActionFragment = FormsFragment
												.newInstance(context);

										quickActionFragment.populateLists();

									}

									if (WebServiceReferences.contextTable
											.containsKey("frmviewer")) {

										FormViewer frm_activity = (FormViewer) WebServiceReferences.contextTable
												.get("frmviewer");

										frm_activity.showConfirmation(
												CallDispatcher.LoginUser,
												bean.getFormId(),
												bean.getFormtime(), false);

									} else {
										WebServiceReferences.webServiceClient
												.Getcontent(
														CallDispatcher.LoginUser,
														bean.getFormId(),
														bean.getFormtime(),
														context);

									}

								} else {
									WebServiceReferences.webServiceClient
											.Getcontent(
													CallDispatcher.LoginUser,
													bean.getFormId(),
													bean.getFormtime(), context);

								}

							}
						} else {
							String strUpdateCount = "update formslookup set rowcount='"
									+ bean.getNoofRows()
									+ "' where tableid='"
									+ bean.getFormId() + "'";
							getdbHeler(context).ExecuteQuery(strUpdateCount);

							handlerForCall.post(new Runnable() {

								@Override
								public void run() {

									if (SingleInstance.contextTable
											.containsKey("forms")) {
										FormsFragment quickActionFragment = FormsFragment
												.newInstance(context);

										quickActionFragment.populateLists();

									}
									if (WebServiceReferences.contextTable
											.containsKey("appsview")) {

										AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
												.get("appsview");
										frm_activity.refreshIcon();

									}

									FormViewer formviewer = (FormViewer) WebServiceReferences.contextTable
											.get("frmviewer");
									if (formviewer.isShowingCurrentForm(bean
											.getFormId())) {
										formviewer.showConfirmation(
												CallDispatcher.LoginUser,
												bean.getFormId(),
												bean.getFormtime(), false);
									} else {

										WebServiceReferences.webServiceClient
												.Getcontent(
														CallDispatcher.LoginUser,
														bean.getFormId(),
														bean.getFormtime(),
														SingleInstance.mainContext);

									}
								}
							});

						}

					}

					if (!bean.getForm_owner().equalsIgnoreCase(
							CallDispatcher.LoginUser)
							&& !WebServiceReferences.buddies_forms
									.contains(bean.getForm_owner())) {
						WebServiceReferences.buddies_forms.add(bean
								.getForm_owner());
					}
				}
			}
			if (settingsConfig.get(i) instanceof FormSettingBean) {
				FormSettingBean settingBean = (FormSettingBean) settingsConfig
						.get(i);
				if (settingBean.getmode().equals("new")) {

					WebServiceReferences.webServiceClient.Getformsettings(
							settingBean.getFsId(), CallDispatcher.LoginUser,
							context);

					showToast(
							SingleInstance.mainContext,
							"You have received a form from "
									+ settingBean.getbuddy());

				} else if (settingBean.getmode().equals("edit")) {

					WebServiceReferences.webServiceClient.Getformsettings(
							settingBean.getFsId(), CallDispatcher.LoginUser,
							context);

				} else if (settingBean.getmode().equals("delete")) {

					String del_row = "delete from formsettings where settingid='"
							+ settingBean.getFsId() + "'";

					String[] formDetails = { settingBean.getbuddy(),
							settingBean.getFormid() };
					if (getdbHeler(context).ExecuteQuery(del_row)) {
						if (formDetails != null) {

							String del_tbl = "DROP TABLE IF EXISTS'"
									+ SingleInstance.mainContext.formnames
									+ "_" + settingBean.getFormid() + "'";
							getdbHeler(context).ExecuteQuery(del_tbl);
							String del_rows = "delete from formslookup where tableid='"
									+ settingBean.getFormid()
									+ "' and owner='"
									+ settingBean.getbuddy() + "'";
							getdbHeler(context).ExecuteQuery(del_rows);
							String del_forminfo = "delete from forminfo where tablename='["
									+ SingleInstance.mainContext.formnames
									+ "_" + settingBean.getFormid() + "]'";
							getdbHeler(context).ExecuteQuery(del_forminfo);
							if (SingleInstance.contextTable
									.containsKey("forms")) {
								FormsFragment quickActionFragment = FormsFragment
										.newInstance(context);

								quickActionFragment.populateLists();

							}

							if (WebServiceReferences.contextTable
									.containsKey("appsview")) {
								AppsView frm_activity = (AppsView) WebServiceReferences.contextTable
										.get("appsview");
								frm_activity.refreshIcon();
							}
							if (WebServiceReferences.contextTable
									.containsKey("frmviewer")) {

								FormViewer frm_activity = (FormViewer) WebServiceReferences.contextTable
										.get("frmviewer");
								frm_activity.finish();

							}
							if (WebServiceReferences.contextTable
									.containsKey("frmreccreator")) {

								FormRecordsCreators frm_activity = (FormRecordsCreators) WebServiceReferences.contextTable
										.get("frmreccreator");
								frm_activity.finish();
							}

						}

					}

				}

			}

		}
		settingsConfig.clear();

	}

	// private void offlineProcess() {
	//
	// Log.i("off", "INSIDE OFFLINE PROCESS");
	//
	// offlinedbnames = getdbHeler(context).getDBNamesOFFline();
	// offlineFormDeletenames = getdbHeler(context).getDeleteDBNamesOFFline();
	// if (offlineFormDeletenames.length != 0) {
	//
	// for (int i = 0; i < offlineFormDeletenames.length; i++) {
	// String formname = offlineFormDeletenames[i];
	// String[] dabname = formname.split("_");
	// String id = dabname[1];
	// String[] params = { CallDispatcher.LoginUser, id };
	// WebServiceReferences.webServiceClient.deleteForm(params);
	// }
	//
	// }
	// if (offlinedbnames.length != 0) {
	// Log.i("off", "INSIDE OFFLINE PROCESS FOR lengthhh"
	// + offlinedbnames.length);
	//
	// for (int i = 0; i < offlinedbnames.length; i++) {
	// Log.i("off", "INSIDE OFFLINE PROCESS FOR LOOOOOP"
	// + offlinedbnames[i]);
	//
	// formnameoffline = offlinedbnames[i];
	// Log.i("off", "INSIDE OFFLINE PROCESS FOR namessss"
	// + formnameoffline);
	//
	// String[] dabname = formnameoffline.split("_");
	// offline_id = dabname[1];
	// field_list = getdbHeler(context).getColumnNamesoffline(
	// "[" + offlinedbnames[i] + "]");
	//
	// offlinedelete = getdbHeler(context).getRecordsoflinedelete(
	// "[" + offlinedbnames[i] + "]");
	// deletewebservices(offlinedelete, offline_id);
	//
	// offlineedit = getdbHeler(context).getRecordsoflineupdate(
	// "[" + offlinedbnames[i] + "]");
	//
	// updatewebservices(offlineedit, offline_id);
	//
	// offlineinsert = getdbHeler(context).getRecordsoflineinsert(
	// "[" + offlinedbnames[i] + "]", "0");
	// insertwebservices(offlineinsert, offline_id);
	//
	// }
	// }
	//
	// }

	private void insertwebservices(ArrayList<String[]> offlineinsert,
			String offline_id) {
		// TODO Auto-generated method stub

		Log.i("new", "field list size" + field_list.size());
		Log.i("new", "field values size" + field_list.size());

		if (offlineinsert.size() > 0) {

			for (int i = 0; i < offlineinsert.size(); i++) {
				List<String> stringList = new ArrayList<String>(
						Arrays.asList(offlineinsert.get(i)));
				field_values = (ArrayList<String>) stringList;
				Log.i("off", "field sizeeeee" + field_list.size());
				Log.i("off", "field values sizeeeee" + field_values.size());

				WebServiceReferences.webServiceClient.addFormRecords(
						CallDispatcher.LoginUser, offline_id,
						field_list.toArray(new String[field_list.size()]),
						field_values.toArray(new String[field_values.size()]),
						context);

			}
		}
	}

	private void updatewebservices(ArrayList<String[]> offlineinsert,
			String offline_id) {
		// TODO Auto-generated method stub
		if (offlineinsert.size() > 0) {

			for (int i = 0; i < offlineinsert.size(); i++) {
				String[] jj = offlineinsert.get(i);
				List<String> stringList = new ArrayList<String>(
						Arrays.asList(offlineinsert.get(i)));
				field_values = (ArrayList<String>) stringList;
				field_values.remove(0);
				Log.i("off", "firld values sizeeee" + field_values.size());
				Log.i("off", " field_list sizeeee" + field_list.size());

				WebServiceReferences.webServiceClient.updateFormRecords(
						offline_id, CallDispatcher.LoginUser, jj[0],
						field_list.toArray(new String[field_list.size()]),
						field_values.toArray(new String[field_values.size()]),
						context);

			}
		}
	}

	private void deletewebservices(ArrayList<String[]> offlineinsert,
			String formid) {

		if (offlineinsert.size() > 0) {

			for (int i = 0; i < offlineinsert.size(); i++) {
				String[] args = offlineinsert.get(i);

				String[] params = { CallDispatcher.LoginUser, formid, args[1] };
				Log.i("off", "paramssssss--->" + CallDispatcher.LoginUser
						+ args[1] + args[0]);
				WebServiceReferences.webServiceClient.deleteFormRecords(params,
						context);

			}
		}

	}

	public void setSettings(UserSettingsBean bean) {
		this.settings = bean;

	}

	public UserSettingsBean getSettings() {
		return this.settings;
	}

	public void uploadofflineResponse(String path, boolean pendingClone,
			int position, String from) {
		// TODO Auto-generated method stub

		Log.i("Instruction", "Path====>" + path);
		Log.i("onresult123", "uploadofflineResponse");
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");
		if (path != null) {
			if (CallDispatcher.LoginUser != null) {
				FTPBean ftpBean = new FTPBean();
				ftpBean.setServer_ip(appMainActivity.cBean.getRouter().split(
						":")[0]);
				ftpBean.setServer_port(40400);
				ftpBean.setFtp_username("ftpadmin");
				ftpBean.setFtp_password("ftppassword");
				if (path.contains("MVD_")) {
					if (path.contains(".mp4"))
						ftpBean.setFile_path(path);
					else
						ftpBean.setFile_path(path + ".mp4");
				} else if (path.contains("MAD_"))
					ftpBean.setFile_path(path);
				else
					ftpBean.setFile_path(path);
				ftpBean.setOperation_type(1);
				ftpBean.setReq_object(position);
				ftpBean.setRequest_from(from);
				if (appMainActivity.getFtpNotifier() != null)
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				Log.i("onresult123", "send for upload");
			}
		}
	}

	// public ArrayList<String> getMyBuddies() {
	// try {
	// Set set = WebServiceReferences.buddyList.keySet();
	// ArrayList<String> buddies = new ArrayList<String>();
	// Iterator<String> itr = set.iterator();
	// buddies.add("All Users");
	// while (itr.hasNext()) {
	// String buddy = itr.next();
	// BuddyInformationBean bean = WebServiceReferences.buddyList
	// .get(buddy);
	// Log.i("clone", "Name: " + buddy + "LOGIN " + LoginUser);
	// if (!bean.getName().equalsIgnoreCase(CallDispatcher.LoginUser)) {
	// buddies.add(buddy);
	// }
	// }
	//
	// return buddies;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// }
	//
	// }

	public void ConMadeConference(String calltype, Context context) {

		if (calltype.equalsIgnoreCase("HC")) {
			calltype = "ACF";
		}

		CallDispatcher.issecMadeConference = true;

		if (calltype.equalsIgnoreCase("ACF") || calltype.equalsIgnoreCase("AC")) {
			calltype = "AC";
		} else if (calltype.equalsIgnoreCase("VCF")
				|| calltype.equalsIgnoreCase("VC")) {
			calltype = "VC";
		} else if (calltype.equalsIgnoreCase("ABC")) {
			calltype = "ABC";

		} else if (calltype.equalsIgnoreCase("VBC")) {
			calltype = "VBC";

		} else if (calltype.equalsIgnoreCase("SS")) {
			calltype = "SS";

		}
		if (CallDispatcher.conConference.size() != 0) {

			String total_participants = null;
			for(String par_name : CallDispatcher.conConference) {
				if(par_name != null && par_name.length() > 0) {
					if(total_participants == null){
						total_participants = par_name;
					} else {
						total_participants = total_participants+","+par_name;
					}
				}
			}

			String username = CallDispatcher.conConference.get(0);
			CallDispatcher.conConference.remove(0);

			BuddyInformationBean bib1 = null;
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(username)) {
							bib1 = temp;
							break;
						}
					}
				}
			//} 
			Log.i("call123", "Bib : " + bib1);
			Log.d("Test", "audioconf check Bib :"+bib1);

			if (bib1 != null) {
				Log.d("Test", "audioconf Bib notnull :"+bib1);

				CallDispatcher.sb = new SignalingBean();
				Log.e("ACal", "user (Who is log on) "
						+ CallDispatcher.LoginUser);
				// sb=new SignalingBean();
				Log.i("AAAA","Call dispatcher  ConMadeConference "+username);
				CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
				CallDispatcher.sb.setTo(username);

				CallDispatcher.sb.setType("0");
				CallDispatcher.sb.setToSignalPort(bib1.getSignalingPort());
				CallDispatcher.sb.setResult("0");
				CallDispatcher.sb.setTopublicip(bib1.getExternalipaddress());
				CallDispatcher.sb.setTolocalip(bib1.getLocalipaddress());
				CallDispatcher.sb.setToSignalPort(bib1.getSignalingPort());
				CallDispatcher.sb.setCallType(calltype);
				if(calltype.equalsIgnoreCase("VC") || calltype.equalsIgnoreCase("AC")) {
					CallDispatcher.sb.setHost(CallDispatcher.LoginUser);
					CallDispatcher.sb.setParticipants(total_participants);
				}
				CallDispatcher.sb.setChatid(CallDispatcher.chatId);
				CallDispatcher.dialChecker = true;
				CallDispatcher.isCallInitiate = true;
				SignalingBean sb = (SignalingBean) CallDispatcher.sb.clone();
				AppMainActivity.commEngine.makeCall(CallDispatcher.sb);

				String sessionIdGenerated = CallDispatcher.sb.getSessionid();

				SignalingBean toSave = (SignalingBean) CallDispatcher.sb
						.clone();
				CallDispatcher.contConferencemembers.put(username, toSave);

				ShowConnectionScreen(sb, LoginUser, context, true);

				for (int i = 0; i < CallDispatcher.conConference.size(); i++) {

					String name = CallDispatcher.conConference.get(i);

					Log.i("call123", "Name : " + name);

					for (BuddyInformationBean bib : ContactsFragment
							.getBuddyList()) {
						if (!bib.isTitle()) {
							if (bib.getName().equalsIgnoreCase(name)) {

								Log.i("call123", "Name : " + name + " status :"
										+ bib.getStatus());
								CallDispatcher.sb = new SignalingBean();
								Log.e("ACal", "user (Who is log on) "
										+ CallDispatcher.LoginUser);
								// sb=new SignalingBean();
								CallDispatcher.sb
										.setFrom(CallDispatcher.LoginUser);

								CallDispatcher.sb.setTo(name);

								CallDispatcher.sb.setType("0");
								CallDispatcher.sb.setToSignalPort(bib
										.getSignalingPort());
								CallDispatcher.sb.setResult("0");
								CallDispatcher.sb.setTopublicip(bib
										.getExternalipaddress());
								CallDispatcher.sb.setTolocalip(bib
										.getLocalipaddress());
								CallDispatcher.sb.setToSignalPort(bib
										.getSignalingPort());
								CallDispatcher.sb.setCallType(calltype);

								CallDispatcher.sb
										.setSessionid(sessionIdGenerated);

								if(calltype.equalsIgnoreCase("VC") || calltype.equalsIgnoreCase("AC")) {
									CallDispatcher.sb.setHost(CallDispatcher.LoginUser);
									CallDispatcher.sb.setParticipants(total_participants);
								}
								CallDispatcher.sb.setChatid(CallDispatcher.chatId);
								// SignalingBean sb1 = (SignalingBean)
								// CallDispatcher.sb.clone();
								AppMainActivity.commEngine
										.makeContactConference(CallDispatcher.sb);
								SignalingBean toSave1 = (SignalingBean) CallDispatcher.sb
										.clone();
								CallDispatcher.contConferencemembers.put(
										name, toSave1);
								if (calltype.equalsIgnoreCase("VC")) {
									CallDispatcher.hsAddedBuddyNameFromConferenceCall
											.put(name, toSave1);
								} else if (calltype.equalsIgnoreCase("AC")) {
									CallDispatcher.hsAddedBuddyNameFromConferenceCall
											.put(name, toSave1);
								}
								break;
							}
						}
					}

				}

				CallDispatcher.conConference.clear();

			}
		}

	}

	public void ShowConnectionScreen(SignalingBean sbean, String username,
			Context context, boolean isconf) {
		try {
			if (!SingleInstance.instanceTable.containsKey("connection")) {
				appMainActivity.closingActivity();
				FragmentManager fm =
						AppReference.mainContext.getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("name", username);
				bundle.putString("type", sbean.getCallType());
				bundle.putBoolean("status", false);
				bundle.putSerializable("bean", sbean);
				bundle.putBoolean("bconf", isconf);
				FragmentTransaction ft = fm.beginTransaction();
				CallConnectingScreen callConnectingScreen = CallConnectingScreen
						.getInstance(context);
				callConnectingScreen.setArguments(bundle);
				ft.replace(R.id.activity_main_content_fragment,
						callConnectingScreen);
				ft.commitAllowingStateLoss();
//				Intent intent = new Intent(context, CallConnectingScreen.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("name", username);
//				bundle.putString("type", sbean.getCallType());
//				bundle.putBoolean("status", false);
//				bundle.putSerializable("bean", sbean);
//				bundle.putBoolean("bconf", isconf);
//				intent.putExtras(bundle);
//				context.startActivity(intent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyOfflineCallResponse(final Object obj) {
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cancelDialog();
				ContactsFragment.getInstance(context).cancelDialog();
				if (obj instanceof ArrayList) {
					Log.i("avatar123", "inside avatar download 1");
					ArrayList<Object> callresponse_list = (ArrayList<Object>) obj;
					if (callresponse_list.size() == 3) {
						String user_id = null;
						String buddy_id = null;
						if (callresponse_list.get(0) instanceof String)
							user_id = (String) callresponse_list.get(0);
						if (callresponse_list.get(1) instanceof String)
							buddy_id = (String) callresponse_list.get(1);

						if (callresponse_list.get(2) instanceof ArrayList) {
							ArrayList<OfflineRequestConfigBean> config_list = (ArrayList<OfflineRequestConfigBean>) callresponse_list
									.get(2);

							if (config_list != null) {
								for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {
									ContentValues cv = new ContentValues();
									cv.put("config_id",
											offlineRequestConfigBean
													.getConfig_id());
									cv.put("fromuser", buddy_id);
									cv.put("messagetitle",
											offlineRequestConfigBean
													.getMessageTitle());
									cv.put("messagetype",
											offlineRequestConfigBean
													.getMessagetype());
									cv.put("message", offlineRequestConfigBean
											.getMessage());
									cv.put("responsetype",
											offlineRequestConfigBean
													.getResponseType());
									cv.put("response", "''");
									cv.put("url",
											offlineRequestConfigBean.getUrl());
									cv.put("receivedtime", getCurrentDateTime());
									cv.put("sendstatus", "0");
									cv.put("username", CallDispatcher.LoginUser);

									getdbHeler(context)
											.insertOfflinePendingClones(cv);

									downloadOfflineresponse(
											offlineRequestConfigBean
													.getMessage(),
											offlineRequestConfigBean
													.getConfig_id(),
											"answering machine", null);

								}
								if (!SingleInstance.instanceTable
										.containsKey("callscreen")) {
									Intent intent = new Intent(context,
											AnsweringMachineActivity.class);
									intent.putExtra("buddy", buddy_id);
									intent.putExtra(
											"avatarlist",
											(ArrayList<OfflineRequestConfigBean>) callresponse_list
													.get(2));
									SingleInstance.mainContext
											.startActivity(intent);
								}
							}
						}

					}
				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					showAlert("Warning !", service_bean.getText());
					cancelDialog();
				}

			}
		});
	}

	private void getofflineCallResponse(String selectedBuddy, String reason) {
		try {
			Object objCallScreen = SingleInstance.instanceTable
					.get("callscreen");
			Log.d("Avataar", "----->getofflineCallResponse<-----"
					+ objCallScreen + selectedBuddy + LoginUser);
			if (objCallScreen == null) {

//				progressDialog = new ProgressDialog(SingleInstance.mainContext);
//				showprogress(progressDialog, SingleInstance.mainContext);
				if (WebServiceReferences.running) {
					String[] details = new String[3];
					details[0] = CallDispatcher.LoginUser;
					details[1] = selectedBuddy;
					details[2] = getdbHeler(SingleInstance.mainContext)
							.getwheninfo(
									"select cid from clonemaster where cdescription=\""
											+ reason + "\"");
//					WebServiceReferences.webServiceClient
//							.OfflineCallResponse(details);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void doAction() {

		CallDispatcher.conConference.clear();
		CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldispatch");
		String from = Alert.readString(context, Alert.fromuser, null);
		String to = Alert.readString(context, Alert.touser, null);
		Log.i("name", "to" + to);

		String ftppath = Alert.readString(context, Alert.ftppath, null);
		Log.i("name", "ftppath" + ftppath);

		String content = Alert.readString(context, Alert.content, null);
		Log.i("name", "content" + content);

		String type = Alert.readString(context, Alert.type, null);
		Log.i("name", "typessss" + type);

		if (type.equalsIgnoreCase("sp")) {
			sendquickaction(from, to, ftppath, content, type);

		} else if (type.equalsIgnoreCase("st")) {
			sendquickaction(from, to, ftppath, content, type);

		} else if (type.equalsIgnoreCase("sa")) {
			sendquickaction(from, to, ftppath, content, type);

		} else if (type.equalsIgnoreCase("sv")) {
			sendquickaction(from, to, ftppath, content, type);

		} else if (type.equalsIgnoreCase("SHS")) {
			sendquickaction(from, to, ftppath, content, type);

		} else if (type.equalsIgnoreCase("AC")) {
			callDisp.MakeCall(1, to,
					SipNotificationListener.getCurrentContext());
		} else if (type.equalsIgnoreCase("VC")) {
			callDisp.MakeCall(2, to,
					SipNotificationListener.getCurrentContext());
		} else if (type.equalsIgnoreCase("AbC")) {
			String[] str = to.split(",");
			String offlinenames = null;

			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				//
				// }

			}

			callDisp.ConMadeConference("ABC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}

		} else if (type.equalsIgnoreCase("VBC")) {

			String[] str = to.split(",");
			String offlinenames = null;

			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("VBC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		} else if (type.equalsIgnoreCase("ACF")) {

			String[] str = to.split(",");
			String offlinenames = null;

			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}
				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("AC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		} else if (type.equalsIgnoreCase("VCF")) {

			String[] str = to.split(",");
			String offlinenames = null;

			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}
				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			ConMadeConference("VC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		} else if (type.equalsIgnoreCase("SS")) {

			String[] str = to.split(",");
			String offlinenames = null;

			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			callDisp.ConMadeConference("SS",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void doAction(String label, String from, String to, String ftppath,
			String content, String action, String condition) {
		Alert.writeString(context, Alert.ToUsers, to);
		condition = condition.replace("&", "'");
		Log.i("BL", "condtion" + condition);
		String resultexists = null;
		String message = null;
		ArrayList<String> colNames = new ArrayList<String>();
		ArrayList<String> colTypes = new ArrayList<String>();

		con.clear();
		contype.clear();
		attribute_list.clear();

		String tablename = null;
		Log.i("Action", action);
		try {
			resultexists = getdbHeler(context).isQueryContainResult(condition,
					CallDispatcher.LoginUser);
			Log.i("name", "result" + resultexists);
			Log.i("thread", "99999999999---->" + resultexists);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = "Invalid Query" + e.getCause();

			e.printStackTrace();
		}

		colNames.clear();
		colTypes.clear();

		rec_list = getdbHeler(context).isQueryContainResultss(condition, "");

		if (condition.equals("") || resultexists.equalsIgnoreCase("true")
				|| resultexists.equals("")) {

			Log.i("name", "conditioninside");

			CallDispatcher.conConference.clear();
			CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
			if (action.equalsIgnoreCase("sp")) {

				Log.i("name", "photo");
				sendquickaction(from, to, ftppath, content, action);

			}

			else if (action.equalsIgnoreCase("st")) {
				sendquickaction(from, to, ftppath, content, action);
			} else if (action.equalsIgnoreCase("Show Results Form")) {
				// CallDispatcher.showprogress(CallDispatcher.pdialog, context);

				QueryBuilder queryBuilder = new QueryBuilder();
				Log.i("xml", "conditions---->" + condition);
				// String[] colStrArr = queryBuilder
				// .getSelectColsFromSQL(condition);

				ArrayList<String> columns = new ArrayList<String>();

				SQLParser parser = new SQLParser(condition);
				String[] forms = parser.getForms();
				if (forms != null && forms.length > 0) {
					forms[0] = forms[0].replace("[", "").replace("]", "");
					columns = callDisp.getdbHeler(context).getColumnNames(
							forms[0]);
				}

				if (columns != null && columns.size() > 0) {
					columns.remove("uuid");
					columns.remove("euuid");
					columns.remove("uudate");
					columns.remove("recorddate");
					columns.remove("status");
				}

				String[] columArray = columns
						.toArray(new String[columns.size()]);

				for (int i = 0; i < columArray.length; i++) {

					Log.i("thread",
							"--------->"
									+ columArray[i].replace("[", "").replace(
											"]", ""));
					colNames.add(columArray[i].replace("[", "")
							.replace("]", ""));
					colTypes.add("nvarchar(20)");
				}

				Log.i("IMP", "inside show result forms");
				Log.i("IMP",
						"table nameeeeee"
								+ Alert.readString(context,
										Alert.QuickActionquery, null));
				con = colNames;
				contype = colTypes;

				Log.i("BL", "activity.columnName  --->" + con.toString());
				Log.i("BL", "activity.columnType  --->" + contype.toString());

				con.add("uuid");
				con.add("euuid");
				con.add("uudate");
				contype.add("nvarchar(20)");
				contype.add("nvarchar(20)");
				contype.add("nvarchar(20)");

				String[] columnnames = con.toArray(new String[con.size()]);
				String[] columntypes = contype.toArray(new String[contype
						.size()]);
				for (int i = 0; i < columnnames.length; i++) {
					Log.i("IMP", "FOR LOOP BEFORE------------->"
							+ columnnames[i]);
				}
				tablename = label + getCurrentDateTime();
				Log.i("test", "table name" + tablename);
				String[] params = new String[4];
				params[0] = CallDispatcher.LoginUser.trim();
				params[1] = tablename;
				params[2] = "";
				params[3] = "";

				Log.i("IMP", "lenfgth LOOP" + columnnames.length);

				HashMap<String, String> field_dtypes = new HashMap<String, String>();

				field_dtypes = getdbHeler(context).getColumnTypesforminfo("");
				Log.i("IMP", "LENGTHHHHHHHHH" + field_dtypes.size());

				Log.i("IMP", "columntypes length---->" + columnnames.length);
				Log.i("IMP", "conType---->" + contype.size());

				for (int i = 0; i < columnnames.length - 3; i++) {
					Log.i("IMP", "FOR LOOP insideeeee" + columnnames[i]);
					int hyphenIndx = columnnames[i].indexOf("-");
					String lkupName = columnnames[i].substring(hyphenIndx + 1,
							columnnames[i].length());

					Log.i("IMP", "FOR LOOP BEFORE" + lkupName);
					if (lkupName.contains("uuid") || lkupName.contains("euuid")
							|| lkupName.contains("uudate")) {

						String[] attributes = { lkupName, "Free Text", "", "",
								"", "" };
						attribute_list.add(attributes);

					} else {
						Log.i("qa", "context" + context + "lkupName "
								+ lkupName + "field_dtypes " + field_dtypes);
						String[] attributes = getdbHeler(context)
								.getRecordsofforminfotable1(lkupName,
										field_dtypes);

						Log.i("IMP", "FOR LOOP attributes.length"
								+ attributes.length);
						for (int k = 0; k < attributes.length; k++) {
							Log.i("IMP", "FOR LOOP ATTRIBUTES" + attributes[k]);

						}

						if (attributes[1].equalsIgnoreCase("numeric")) {
							columntypes[i] = "int(10)";
							contype.add(i, "int(10)");

							Log.i("IMP", "columntypes---->" + columntypes[i]);

						}
						attribute_list.add(attributes);

					}

				}

				Log.i("IMP", "LENGTHHHHHHHHH" + attribute_list.size());
				progressDialog = new ProgressDialog(
						SipNotificationListener.getCurrentContext());
				showprogress(progressDialog,
						SipNotificationListener.getCurrentContext());

				QuickActionFragment actionFragment = QuickActionFragment
						.newInstance(context);
				Log.i("qa", "context" + actionFragment.getContext());
				WebServiceReferences.webServiceClient.CreateFormAttribute(
						params, columnnames, columntypes, attribute_list,
						actionFragment.getContext());

				rec_list = getdbHeler(context).isQueryContainResultss(
						condition, "");

				for (int i = 0; i < rec_list.size(); i++) {

					Log.i("ne",
							"rec list values $$$$$$$$$$ *" + rec_list.get(i));
					String[] ff = rec_list.get(i);
					for (int j = 0; j < ff.length; j++) {
						Log.i("ne", "rec list values $$$$$$$$$$ *" + ff[j]);

					}

				}
			} else if (action.equalsIgnoreCase("sa")) {
				sendquickaction(from, to, ftppath, content, action);
			} else if (action.equalsIgnoreCase("sv")) {
				sendquickaction(from, to, ftppath, content, action);
			} else if (action.equalsIgnoreCase("SHS")) {
				sendquickaction(from, to, ftppath, content, action);
			} else if (action.equalsIgnoreCase("AC")) {
				Log.i("Action", action);
				if (!SingleInstance.instanceTable
						.containsKey("callscreen")
						&& !SingleInstance.instanceTable
								.containsKey("alertscreen")
						&& !WebServiceReferences.contextTable
								.containsKey("sicallalert")
						&& !WebServiceReferences.contextTable
								.containsKey("sipcallscreen"))
					callDisp.MakeCall(1, to,
							SipNotificationListener.getCurrentContext());
			} else if (action.equalsIgnoreCase("VC")) {
				if (!SingleInstance.instanceTable
						.containsKey("callscreen")
						&& !WebServiceReferences.contextTable
								.containsKey("alertscreen")
						&& !WebServiceReferences.contextTable
								.containsKey("sicallalert")
						&& !WebServiceReferences.contextTable
								.containsKey("sipcallscreen"))
					callDisp.MakeCall(2, to,
							SipNotificationListener.getCurrentContext());
			} else if (action.equalsIgnoreCase("ABC")) {
				requestAudioBroadCast(to);
			} else if (action.equalsIgnoreCase("VBC")) {
				requestVideoBroadCast(to);
			} else if (action.equalsIgnoreCase("ACF")) {
				requestAudioConference(to);
			} else if (action.equalsIgnoreCase("VCF")) {
				if (!SingleInstance.instanceTable
						.containsKey("callscreen")
						&& !SingleInstance.instanceTable
								.containsKey("alertscreen")
						&& !WebServiceReferences.contextTable
								.containsKey("sicallalert")
						&& !WebServiceReferences.contextTable
								.containsKey("sipcallscreen")) {
					String[] str = to.split(",");
					String offlinenames = null;
					// ArrayList< String> datas=new ArrayList<String>();
					for (int i = 0; i < str.length; i++) {

						for (BuddyInformationBean bib : ContactsFragment
								.getBuddyList()) {
							if (!bib.isTitle()) {
								if (bib.getName().equalsIgnoreCase(str[i])) {
									if (bib.getStatus().equalsIgnoreCase(
											"online")) {

										CallDispatcher.conConference
												.add(str[i]);
									} else {
										if (offlinenames == null) {
											offlinenames = str[i];
										} else {
											offlinenames = offlinenames + ","
													+ str[i];
										}

									}
									break;
								}
							}
						}

						// BuddyInformationBean bib = WebServiceReferences.q
						// .get(str[i]);
						// if (bib.getStatus().startsWith("Onli")) {
						// } else {
						// if (offlinenames == null) {
						// offlinenames = str[i];
						// } else {
						// offlinenames = offlinenames + "," + str[i];
						// }
						// }

					}
					callDisp.ConMadeConference("VC",
							SipNotificationListener.getCurrentContext());
					if (offlinenames != null) {
						Toast.makeText(context,
								offlinenames + " is on Offline",
								Toast.LENGTH_LONG).show();
					}
				}
			} else if (action.equalsIgnoreCase("HC")) {
				requestAudioConference(to);
				// if (!WebServiceReferences.contextTable
				// .containsKey("callscreen")
				// && !WebServiceReferences.contextTable
				// .containsKey("alertscreen")
				// && !WebServiceReferences.contextTable
				// .containsKey("sicallalert")
				// && !WebServiceReferences.contextTable
				// .containsKey("sipcallscreen")) {
				// if (AppReference.isRegistered
				// && AppReference.sip_accountID != -1) {
				//
				// String[] str = to.split(",");
				// String offlinenames = null;
				// ArrayList<Databean> buddies = new ArrayList<Databean>();
				// for (int i = 0; i < str.length; i++) {
				//
				//
				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// if (!bib.getName().equals(
				// CallDispatcher.LoginUser)) {
				// Databean db = new Databean();
				// db.setname(bib.getName());
				// db.setStatus(bib.getStatus());
				// buddies.add(db);
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + ","
				// + str[i];
				// }
				// }
				// }
				// }
				// ArrayList<CallerBean> process_members = new
				// ArrayList<CallerBean>();
				// if (process_members.size() > 0) {
				// process_members.clear();
				// }
				// if (buddies != null && buddies.size() > 0) {
				// for (Databean dBean : buddies) {
				// CallerBean callerBean = new CallerBean();
				// callerBean.setUserName(dBean.getname());
				// callerBean.setToNnumber(dBean.getname());
				// callerBean.setPresense("Connecting");
				// callerBean.setHold(0);
				// callerBean.setMute(0);
				// callerBean.setCall_id(-1);
				// process_members.add(callerBean);
				// }
				// }
				//
				// if (process_members.size() == 0) {
				// Toast.makeText(
				// SipNotificationListener.getCurrentContext(),
				// "No online buddies", Toast.LENGTH_LONG)
				// .show();
				// } else {
				// // Log.i("sip", "sip account ID"
				// // + AppReference.sip_accountID);
				// // Intent intent = new Intent(context,
				// // CallScreenActivity.class);
				// // intent.putExtra("call_list", process_members);
				// // SipNotificationListener.getCurrentContext()
				// // .startActivity(intent);
				// }
				// } else
				// Toast.makeText(
				// SipNotificationListener.getCurrentContext(),
				// "SIP not Registered. Try again!",
				// Toast.LENGTH_SHORT).show();
				//
				// }
			}

		} else {
			if (resultexists.equalsIgnoreCase("false")) {
				message = "There is no Record exists";
				showAlert("Response", message);

			} else if (resultexists.equalsIgnoreCase("exception")) {
				message = "Invalid Query";
				showAlert("Response", message);

			}

		}
		if (WebServiceReferences.contextTable
				.containsKey("QuickActionSelectcalls")) {
			((QuickActionSelectcalls) WebServiceReferences.contextTable
					.get("QuickActionSelectcalls")).finish();
		}

	}

	public void requestAudioConference(String to) {
		Log.d("Test","audioconf inside requestAudioConference calldisp");

		if (!SingleInstance.instanceTable.containsKey("callscreen")
				&& !SingleInstance.instanceTable
						.containsKey("alertscreen")
				&& !WebServiceReferences.contextTable
						.containsKey("sicallalert")
				&& !WebServiceReferences.contextTable
						.containsKey("sipcallscreen")) {
			String[] str = to.split(",");
			String offlinenames = null;
			String online = null;

			Log.i("call123", "Members :" + to);
			Log.d("Test", "audioconf Members :" + to);


			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {
				Log.d("Test", "audioconf str length :" +str.length);

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					Log.d("Test", "audioconf inside bib");

					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {

							Log.i("call123",
									bib.getName() + "-" + bib.getStatus());
							Log.i("Test","Status"+
									bib.getName() + "-=============" + bib.getStatus());
							if (bib.getStatus().equalsIgnoreCase("online")) {
								Log.d("Test", "audioconf status isOnline :");

								CallDispatcher.conConference.add(str[i]);
								if (online == null) {
									online = str[i];
								} else {
									online = online + "," + str[i];
								}
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			ConMadeConference("AC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is Offline", 2).show();
			}
			if (online != null) {
					Log.d("AAAA", "notifyType2Received ifpart ==> ");
				if(SingleInstance.instanceTable
						.containsKey("connection")) {
					CallConnectingScreen connectingScreen = (CallConnectingScreen) SingleInstance.instanceTable
							.get("connection");
					if(connectingScreen != null) {
						connectingScreen.setTitle(online);
					}
				}
			}
		} else {
			Log.i("AudioCall", "Call Screen :" + SingleInstance.instanceTable.containsKey("callscreen") +
					" Alert Screen :" + SingleInstance.instanceTable
					.containsKey("alertscreen") + " Sip Call Alert :" +
					WebServiceReferences.contextTable
							.containsKey("sicallalert") + " Sip Call Screen :" +
					WebServiceReferences.contextTable
							.containsKey("sipcallscreen"));
		}

	}

	public void requestVideoConference(String to) {

		if (!SingleInstance.instanceTable.containsKey("callscreen")
				&& !SingleInstance.instanceTable
						.containsKey("alertscreen")
				&& !WebServiceReferences.contextTable
						.containsKey("sicallalert")
				&& !WebServiceReferences.contextTable
						.containsKey("sipcallscreen")) {
			String[] str = to.split(",");
			String offlinenames = null;

			Log.i("call123", "Members :" + to);

			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {

							Log.i("call123",
									bib.getName() + "-" + bib.getStatus());
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			ConMadeConference("VC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is Offline",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	public void requestVideoBroadCast(String to) {
		if (!SingleInstance.instanceTable.containsKey("callscreen")
				&& !SingleInstance.instanceTable
						.containsKey("alertscreen")
				&& !WebServiceReferences.contextTable
						.containsKey("sicallalert")
				&& !WebServiceReferences.contextTable
						.containsKey("sipcallscreen")) {
			String value = to + ",";
			String[] str = value.split(",");
			String offlinenames = null;
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				// }

			}
			ConMadeConference("VBC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void MakeCall(int operation, String username, Context context) {
		try {
			BuddyInformationBean bib = null;

			for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
				if (!temp.isTitle()) {
					if (temp.getName().equalsIgnoreCase(username)) {
						bib = temp;
						break;
					}
				}
			}

			

			// bib = WebServiceReferences.buddyList.get(username);
			if (bib != null) {
				CallDispatcher.sb = new SignalingBean();
				CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
				CallDispatcher.sb.setTo(username);
				CallDispatcher.sb.setType("0");
				CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
				CallDispatcher.sb.setResult("0");
				CallDispatcher.sb.setTopublicip(bib.getExternalipaddress());
				CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
				CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
				CallDispatcher.sb.setChatid(CallDispatcher.LoginUser);
				CallDispatcher.sb.setHost(CallDispatcher.LoginUser);
				CallDispatcher.sb.setParticipants(username);
				switch (operation) {
				case 1:
					CallDispatcher.sb.setCallType("AC");
					break;
				case 2:
					CallDispatcher.sb.setCallType("VC");
					break;
				case 3:
					CallDispatcher.sb.setCallType("AP");
					break;
				case 4:
					CallDispatcher.sb.setCallType("VP");
					break;
				case 5:
					CallDispatcher.sb.setCallType("ABC");
					break;
				case 6:
					CallDispatcher.sb.setCallType("VBC");
					break;
				case 7:
					CallDispatcher.sb.setCallType("SS");
				default:
					break;
				}

				if (CallDispatcher.sb.getCallType().equals("AC")
						|| CallDispatcher.sb.getCallType().equals("VC")) {
					if (SingleInstance.ContactSharng
							|| SingleInstance.parentId != null) {
						CallDispatcher.sb.setBs_calltype("1"); // "1" in "2" out
						CallDispatcher.sb.setBs_callCategory("1"); // "1" order,
																	// "2"
																	// feedback,
																	// "3"
																	// service
						CallDispatcher.sb.setBs_callstatus(""); // "1" received,
																// "2" accept,
																// "3" reject
																// "4"
																// delivered,
						// "5" invoice
						if (SingleInstance.parentId != null) {

							CallDispatcher.sb
									.setBs_parentid(SingleInstance.parentId);

						} else {
							CallDispatcher.sb.setBs_parentid(Utility
									.getSessionID());
						}
//						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
						SignalingBean temp = (SignalingBean) CallDispatcher.sb
								.clone();
						temp.setBs_calltype("2");
//						DBAccess.getdbHeler()
//								.saveOrUpdateRecordtransactiondetails(
//										CallDispatcher.sb);
						SingleInstance.ContactSharng = false;
						
					} else {
//						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
//						DBAccess.getdbHeler()
//								.saveOrUpdateRecordtransactiondetails(
//										CallDispatcher.sb);
					}
				} else {
//					CallDispatcher.sb.setStartTime(getCurrentDateandTime());
					
				}
				CallDispatcher.dialChecker = true;
				SignalingBean vbcsb = (SignalingBean) CallDispatcher.sb.clone();
				ShowConnectionScreen(vbcsb, username, context, false);
				CallDispatcher.isCallInitiate = true;
				SingleInstance.parentId = null;
				AppMainActivity.commEngine.makeCall(CallDispatcher.sb);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void MakeCallFromCallHistory(int operation, String username, Context context, RecordTransactionBean transaction_Bean, int con_scr_opened, String promote_feature) {
		try {
			BuddyInformationBean bib = null;

			for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
				if (!temp.isTitle()) {
					if (temp.getName().equalsIgnoreCase(username)) {
						bib = temp;
						break;
					}
				}
			}

			// bib = WebServiceReferences.buddyList.get(username);
			if (bib != null) {

				SignalingBean pefore_promote = null;
				String previous_start_time = null;
				if(promote_feature != null && promote_feature.equalsIgnoreCase("promote")) {
					previous_start_time = sb.getStartTime();
					pefore_promote = (SignalingBean) sb.clone();
				}

				CallDispatcher.sb = new SignalingBean();
				CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
				CallDispatcher.sb.setTo(username);
				CallDispatcher.sb.setType("0");
				CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
				CallDispatcher.sb.setResult("0");
				CallDispatcher.sb.setTopublicip(bib.getExternalipaddress());
				CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
				CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
				CallDispatcher.sb.setSessionid(transaction_Bean.getSessionid());
				CallDispatcher.sb.setHost(transaction_Bean.getHost());
				CallDispatcher.sb.setParticipants(transaction_Bean.getParticipants());

				if(transaction_Bean.getChatid()!=null)
					CallDispatcher.sb.setChatid(transaction_Bean.getChatid());

				if(promote_feature.equalsIgnoreCase("promote")) {
					CallDispatcher.sb.setVideopromote("yes");
					CallDispatcher.sb.setStartTime(previous_start_time);
				} else if(promote_feature.equalsIgnoreCase("disablevideo")){
					CallDispatcher.sb.setVideoStoped(transaction_Bean.getDisableVideo());
				} else {
					CallDispatcher.sb.setVideopromote("no");
					CallDispatcher.sb.setJoincall("yes");
				}

				switch (operation) {
					case 1:
						CallDispatcher.sb.setCallType("AC");
						break;
					case 2:
						CallDispatcher.sb.setCallType("VC");
						break;
					case 3:
						CallDispatcher.sb.setCallType("AP");
						break;
					case 4:
						CallDispatcher.sb.setCallType("VP");
						break;
					case 5:
						CallDispatcher.sb.setCallType("ABC");
						break;
					case 6:
						CallDispatcher.sb.setCallType("VBC");
						break;
					case 7:
						CallDispatcher.sb.setCallType("SS");
					default:
						break;
				}

				if (CallDispatcher.sb.getCallType().equals("AC")
						|| CallDispatcher.sb.getCallType().equals("VC")) {
					if (SingleInstance.ContactSharng
							|| SingleInstance.parentId != null) {
						CallDispatcher.sb.setBs_calltype("1"); // "1" in "2" out
						CallDispatcher.sb.setBs_callCategory("1"); // "1" order,
						// "2"
						// feedback,
						// "3"
						// service
						CallDispatcher.sb.setBs_callstatus(""); // "1" received,
						// "2" accept,
						// "3" reject
						// "4"
						// delivered,
						// "5" invoice
						if (SingleInstance.parentId != null) {

							CallDispatcher.sb
									.setBs_parentid(SingleInstance.parentId);

						} else {
							CallDispatcher.sb.setBs_parentid(Utility
									.getSessionID());
						}
//						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
						SignalingBean temp = (SignalingBean) CallDispatcher.sb
								.clone();
						temp.setBs_calltype("2");
//						DBAccess.getdbHeler()
//								.saveOrUpdateRecordtransactiondetails(
//										CallDispatcher.sb);
						SingleInstance.ContactSharng = false;

					} else {
//						CallDispatcher.sb.setStartTime(getCurrentDateandTime());
//						DBAccess.getdbHeler()
//								.saveOrUpdateRecordtransactiondetails(
//										CallDispatcher.sb);
					}
				} else {
//					CallDispatcher.sb.setStartTime(getCurrentDateandTime());

				}
				CallDispatcher.dialChecker = true;
				SignalingBean vbcsb = (SignalingBean) CallDispatcher.sb.clone();
				if(con_scr_opened == 0) {
					ShowConnectionScreen(vbcsb, username, context, false);
				}
				CallDispatcher.isCallInitiate = true;
				SingleInstance.parentId = null;
				if(promote_feature.equalsIgnoreCase("promote") || promote_feature.equalsIgnoreCase("disablevideo")){
					AppMainActivity.commEngine.makeCallPromotion(CallDispatcher.sb);
				} else {
					AppMainActivity.commEngine.makeCall(CallDispatcher.sb);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendquickaction(String from, String to, String ftppath,
			String content, String type) {
		String[] tousers = to.split(",");

		for (int i = 0; i < tousers.length; i++) {
			ShareReminder reminder = new ShareReminder();
			Utility utility = new Utility();
			reminder.setFrom(CallDispatcher.LoginUser);
			reminder.setTo(tousers[i]);
			if (type.equalsIgnoreCase("ST")) {
				reminder.setType("notes");
			} else if (type.equalsIgnoreCase("SP")) {
				reminder.setType("photonotes");
			} else if (type.equalsIgnoreCase("SA")) {
				reminder.setType("audio");
			} else if (type.equalsIgnoreCase("SV")) {
				reminder.setType("video");
			} else if (type.equalsIgnoreCase("SHS")) {
				reminder.setType("handsketch");
			}
			reminder.setReceiverStatus("manual");
			reminder.setFileLocation(ftppath);
			reminder.setIsbusyResponse("1");
			reminder.setRemindertz("");
			reminder.setReminderdatetime("");
			reminder.setFileid(Long.toString(utility.getRandomMediaID()));
			Log.d("ftp", "webserc" + "");
			reminder.setReminderStatus("false");
			reminder.setReminderResponseType("");
			reminder.setDownloadType("1");
			reminder.setComment(content);

			Log.d("ftp", "webserc" + reminder.getReminderResponseType());
			Log.d("ftp", "webserc");
			WebServiceReferences.webServiceClient.ShareFiles(reminder);
			Log.d("ftp", "webserc send");

		}
	}

	private void setQuickActionAlert(Context context) {
		Log.i("Test", "inside CalenderAlert");

		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int minute = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		String strDate = year + "-" + WebServiceReferences.setLength2(month)
				+ "-" + WebServiceReferences.setLength2(date) + " "
				+ WebServiceReferences.setLength2(hour) + ":"
				+ WebServiceReferences.setLength2(minute);
		Boolean success = getdbHeler(context).gettime(strDate, context);

		if (success) {
			Log.i("name", "inside CalenderAlert");
			Alert.writeBoolean(context, Alert.satus, true);
			Intent i = new Intent(context, ContactLogics.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		}
	}

	public void getTTLList() {
		try {
			Vector<ReminderDetail> ttlList = getdbHeler(context).getTTL();
			if (ttlList != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"dd/MM/yyyy hh:mm aa");
				DateFormatSymbols dfSym = new DateFormatSymbols();
				dfSym.setAmPmStrings(new String[] { "am", "pm" });
				dateFormat.setDateFormatSymbols(dfSym);
				String currnetDate = dateFormat.format(new Date());
				Date newDate = dateFormat.parse(currnetDate);
				for (ReminderDetail reminder : ttlList) {
					if (reminder.getVanishMode() != null) {
						if (reminder.getVanishMode().equalsIgnoreCase("ttl")) {
							Log.i("reminder",
									"====> " + reminder.getVanishValue());
							String dateString = reminder.getVanishValue();
							Date getDate = dateFormat.parse(dateString);
							if (compareDatesByDateMethods(dateFormat, getDate,
									newDate, "files")) {
								String strDeleteQry = "delete from component where componentid="
										+ reminder.getCompId();
								if (getdbHeler(context).ExecuteQuery(
										strDeleteQry)) {
									if (WebServiceReferences.contextTable
											.containsKey("MAIN")) {
										((CompleteListView) WebServiceReferences.contextTable
												.get("MAIN")).UpdateList();
									}
								}

							}
						} else if (reminder.getVanishMode().equalsIgnoreCase(
								"elapse")) {
							String dateString = reminder.getVanishValue();
							Date getDate = dateFormat.parse(dateString);
							if (compareDatesByDateMethods(dateFormat, getDate,
									newDate, "files")) {
								String strDeleteQry = "delete from component where componentid="
										+ reminder.getCompId();
								if (getdbHeler(context).ExecuteQuery(
										strDeleteQry)) {
									if (WebServiceReferences.contextTable
											.containsKey("MAIN")) {
										((CompleteListView) WebServiceReferences.contextTable
												.get("MAIN")).UpdateList();
									}
								}

							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean compareDatesByDateMethods(SimpleDateFormat df, Date oldDate,
			Date newDate, String from) {
		boolean isequalGreat = false;
		try {
			// how to check if two dates are equals in java
			if (from.equalsIgnoreCase("files")) {
				if (oldDate.before(newDate)) {
					isequalGreat = true;
				}
			}
			if (oldDate.equals(newDate)) {
				isequalGreat = true;
			}

			return isequalGreat;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm aa");
			final String strDate = simpleDateFormat.format(calendar.getTime());

			try {
				Log.i("file delte", "===> " + strDate);
				getTTLList();
				setQuickActionAlert(SipNotificationListener.getCurrentContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public String getNoteCreateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mma");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void notifyGetallPermission(
			ArrayList<PermissionBean> permission_list) {
		for (PermissionBean permission_bean : permission_list) {
			ContentValues cv = new ContentValues();
			Log.d("permissions", "bean--->" + permission_bean);
			if (permission_bean != null) {
				cv.put("userid", permission_bean.getUserId());
				cv.put("buddyid", permission_bean.getBuddyId());
				cv.put("audiocall",
						Integer.parseInt(permission_bean.getAudio_call()));
				cv.put("videocall",
						Integer.parseInt(permission_bean.getVideo_call()));
				cv.put("audiobroadcast",
						Integer.parseInt(permission_bean.getABC()));
				cv.put("videobroadcast",
						Integer.parseInt(permission_bean.getVBC()));
				cv.put("audiounicast",
						Integer.parseInt(permission_bean.getAUC()));
				cv.put("videounicast",
						Integer.parseInt(permission_bean.getVUC()));
				cv.put("mmchat", Integer.parseInt(permission_bean.getMMchat()));
				cv.put("textmessage",
						Integer.parseInt(permission_bean.getTextMessage()));
				cv.put("audiomessage",
						Integer.parseInt(permission_bean.getAudioMessage()));
				cv.put("videomessage",
						Integer.parseInt(permission_bean.getVideoMessage()));
				cv.put("photomessage",
						Integer.parseInt(permission_bean.getPhotoMessage()));
				cv.put("sketchmessage",
						Integer.parseInt(permission_bean.getSketchMessage()));
				cv.put("shareprofile",
						Integer.parseInt(permission_bean.getShareProfile()));
				cv.put("viewprofile",
						Integer.parseInt(permission_bean.getViewProfile()));
				cv.put("answeringmachine",
						Integer.parseInt(permission_bean.getAvtaar()));
				cv.put("formshare",
						Integer.parseInt(permission_bean.getFormshare()));
				cv.put("hostedconf",
						Integer.parseInt(permission_bean.getHostedconf()));
				cv.put("record_time", permission_bean.getRecordTime());
				if (!getdbHeler(context).isRecordExists(
						"select * from setpermission where userid='"
								+ permission_bean.getUserId()
								+ "' and buddyid='"
								+ permission_bean.getBuddyId() + "'")) {
					int row_count = getdbHeler(context).insertPermission(cv);
					Log.d("permissions", "inserted Row id is--->" + row_count);
				} else {
					int row_count = getdbHeler(context).updatePermission(
							cv,
							"userid='" + permission_bean.getUserId()
									+ "' and buddyid='"
									+ permission_bean.getBuddyId() + "'");
					Log.d("permissions", "updated Row id is--->" + row_count);
				}

			}

		}
	}

	/**
	 * Crate thumbnail image file from the video
	 * 
	 * @param strThumbPath
	 * @return
	 */

	// public boolean CreateVideoThumbnail(String strThumbPath) {
	// try {
	// System.out.println("create thumbnail");
	// System.out.println("Thumb path" + strThumbPath);
	//
	// Log.d("lg", "......create video file");
	//
	// MediaPlayer m = new MediaPlayer();
	// m.setDataSource(strThumbPath + ".mp4");
	// m.prepare();
	//
	// long milliseconds = (long) m.getDuration();
	//
	// String seconds = WebServiceReferences.setLength2((int) (Math
	// .round((double) milliseconds / 1000) % 60));
	// String minutes = WebServiceReferences
	// .setLength2((int) ((milliseconds / (1000 * 60)) % 60));
	// String hours = WebServiceReferences
	// .setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
	//
	// String asText = hours + ":" + minutes + ":" + seconds;
	// Bitmap thumbImage = ThumbnailUtils.createVideoThumbnail(
	// strThumbPath + ".mp4",
	// MediaStore.Images.Thumbnails.MINI_KIND);
	// if (thumbImage != null) {
	// thumbImage = ResizeVideoImage(thumbImage);
	// Bitmap thumb = combineImages(thumbImage, asText);
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// thumb.compress(CompressFormat.JPEG, 75, bos);
	// FileOutputStream fos = new FileOutputStream(strThumbPath
	// + ".jpg");
	// bos.writeTo(fos);
	// bos.close();
	// fos.close();
	// thumbImage.recycle();
	// thumb.recycle();
	// return true;
	// } else {
	// File fl = new File(strThumbPath + ".mp4");
	// if (fl.exists()) {
	//
	// Bitmap bmp = BitmapFactory.decodeResource(
	// SipNotificationListener.getCurrentContext()
	// .getResources(), drawable.broken);
	// bmp = ResizeVideoImage(bmp);
	// Log.i("myLog", "Image Height :" + bmp.getHeight());
	// Log.i("myLog", "Image Width  :" + bmp.getWidth());
	// Bitmap thumb1 = combineImages(bmp, asText);
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// thumb1.compress(CompressFormat.JPEG, 75, bos);
	// FileOutputStream fos = new FileOutputStream(strThumbPath
	// + ".jpg");
	// bos.writeTo(fos);
	// bos.close();
	// fos.close();
	// bmp.recycle();
	// thumb1.recycle();
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// } catch (FileNotFoundException ex) {
	// Log.e("Exc", "FileNotFoundException : " + ex);
	// return false;
	// } catch (IOException ioe) {
	// Log.e("Exc", "IOException : " + ioe);
	// return (createbrokenThump(strThumbPath));
	// } catch (Exception e) {
	// Log.e("Exc", "Exception : " + e);
	// return false;
	// }
	//
	// }

	@SuppressWarnings("finally")
	// private boolean createbrokenThump(String strThumbPath) {
	// boolean status = false;
	// try {
	//
	// File fl = new File(strThumbPath + ".mp4");
	// if (fl.exists()) {
	//
	// Bitmap bmp = BitmapFactory.decodeResource(
	// SipNotificationListener.getCurrentContext()
	// .getResources(), drawable.broken);
	// bmp = ResizeVideoImage(bmp);
	// Bitmap thumb1 = combineImages(bmp, "00:00:00");
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// thumb1.compress(CompressFormat.JPEG, 75, bos);
	// FileOutputStream fos = new FileOutputStream(strThumbPath
	// + ".jpg");
	// bos.writeTo(fos);
	// bos.close();
	// fos.close();
	// status = true;
	// bmp.recycle();
	// thumb1.recycle();
	// } else {
	// status = false;
	// }
	// } catch (FileNotFoundException ex) {
	// Log.e("Exc", "FileNotFoundException : " + ex);
	// status = false;
	// } catch (IOException ioe) {
	// Log.e("Exc", "IOException : " + ioe);
	// status = false;
	// } catch (Exception e) {
	// Log.e("Exc", "Exception : " + e);
	// status = false;
	// } finally {
	// return (status);
	// }
	// }
	// private Bitmap ResizeVideoImage(Bitmap bitmap) {
	//
	// int outWidth = bitmap.getWidth();
	// int outHeight = bitmap.getHeight();
	//
	// if (outWidth == outHeight) {
	// bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
	// } else {
	//
	// if (outHeight == 200) {
	// bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight,
	// true);
	// } else {
	// double ratio;
	// if (outHeight < outWidth) {
	// ratio = (double) outWidth / (double) outHeight;
	//
	// bitmap = Bitmap.createScaledBitmap(bitmap,
	// (int) (200 * ratio), 200, true);
	// } else {
	// ratio = (double) outWidth / (double) outHeight;
	//
	// bitmap = Bitmap.createScaledBitmap(bitmap,
	// (int) Math.round(200 / ratio), 200, true);
	// }
	//
	// }
	//
	// }
	// return bitmap;
	// }
	//
	// private Bitmap combineImages(Bitmap c, String time) {
	// Bitmap cs = null;
	//
	// Bitmap bitmapPlay = BitmapFactory.decodeResource(
	// SipNotificationListener.getCurrentContext().getResources(),
	// R.drawable.vplay1);
	// int width, height = 0;
	// width = c.getWidth();
	// height = c.getHeight();
	// cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	// Canvas comboImage = new Canvas(cs);
	// comboImage.drawBitmap(c, 0f, 0f, null);
	// comboImage.drawBitmap(bitmapPlay,
	// (c.getWidth() / 2 - bitmapPlay.getWidth() / 2),
	// (c.getHeight() / 2 - bitmapPlay.getHeight() / 2), null);
	// Paint paint = new Paint();
	// paint.setColor(Color.RED);
	// paint.setTypeface(Typeface.SERIF);
	// paint.setTypeface(Typeface.DEFAULT_BOLD);
	// paint.setTextSize(30);
	// comboImage.drawText(time, (c.getWidth() / 4), (c.getHeight() / 2),
	// paint);
	//
	// return cs;
	// }
	public void playShareTone(String flname) {
		try {

			Log.d("RING", "on IM Tone()");
			if ((iplayer == null) && flname != null) {
				Log.d("RING", "Started Player caller Ring Tone");
				iplayer = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd(flname);
				iplayer.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				iplayer.setLooping(false);
				iplayer.prepare();
				iplayer.start();
				iplayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							iplayer.release();
							iplayer = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("RING", "on startCallRingTone()" + e.toString());
		}

	}

	public void notifyFileDownloaded(String Response, final FTPBean bean) {

		try {
			if (Response.equalsIgnoreCase("true")) {
				final ShareReminder share = (ShareReminder) bean
						.getReq_object();
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + bean.getFile_path();

				String comment;
				if (share.getShareComment() != null) {
					comment = share.getShareComment();
				} else {
					comment = "";
				}

				String vanishMode = null;
				String vanishValue = null;
				if (share.getVanishMode() != null) {
					if (share.getVanishMode().equalsIgnoreCase("elapse")) {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"dd/MM/yyyy hh:mm aa");
							DateFormatSymbols dfSym = new DateFormatSymbols();
							dfSym.setAmPmStrings(new String[] { "am", "pm" });
							dateFormat.setDateFormatSymbols(dfSym);
							Calendar cal = Calendar.getInstance();
							String currnetDate = dateFormat.format(new Date());
							Date newDate = dateFormat.parse(currnetDate);
							cal.setTime(newDate);
							cal.add(Calendar.HOUR,
									Integer.valueOf(share.getVanishValue()));
							vanishValue = dateFormat.format(cal.getTime());
						} catch (Exception e) {
							Log.e("files", "===> " + e.getMessage());
							e.printStackTrace();
						}
					} else {
						vanishValue = share.getVanishValue();
					}
					vanishMode = share.getVanishMode();
				}
				ContentValues cv = new ContentValues();
				String componentType = "";
				String title = "";
				String readString = "";

				if (share.getType().equalsIgnoreCase("notes")) {
					componentType = "note";
					try {
						File f = new File(filepath);
						FileInputStream fileIS = new FileInputStream(f);
						BufferedReader buf = new BufferedReader(
								new InputStreamReader(fileIS));
						readString = buf.readLine();
						if (readString.trim().length() > 12) {
							readString = readString.substring(0, 11);
						}
						Log.i("Notes", "=====>" + readString);
						String heading = readString;
						if (heading.contains("'")) {
							heading = heading.replace("'", "");
						}
						title = heading;
					}

					catch (Exception e) {
						Log.d("thread", "catch" + e);
					}
					if (CompleteListView.textnotes == null)
						CompleteListView.textnotes = new TextNoteDatas();

				} else if (share.getType().equalsIgnoreCase("audio")) {
					componentType = "audio";
					title = "Audio";

				} else if (share.getType().equalsIgnoreCase("video")) {
					componentType = "video";
					title = "Video";
				} else if (share.getType().equalsIgnoreCase("photonotes")) {
					componentType = "photo";
					title = "Photo";
				} else if (share.getType().equalsIgnoreCase("handsketch")) {
					componentType = "sketch";
					title = "Hand Sketch";
				} else if (share.getType().equalsIgnoreCase("document")) {
					String[] name = bean.getFile_path().split("\\.");
					if (name[0].contains(Environment
							.getExternalStorageDirectory() + "/COMMedia/")) {
						name[0] = name[0].replace(
								Environment.getExternalStorageDirectory()
										+ "/COMMedia/", "");
					}
					String extn = name[1];
					componentType = "document";
					title = name[0];
				}
				cv.put("componenttype", componentType);
				cv.put("componentpath", filepath);
				cv.put("ftppath", bean.getFile_path());
				cv.put("componentname", title);
				cv.put("fromuser", share.getFrom());
				cv.put("comment", comment);
				cv.put("owner", CallDispatcher.LoginUser);
				cv.put("vanishmode", vanishMode);
				cv.put("vanishvalue", vanishValue);
				cv.put("receiveddateandtime", getNoteCreateTime());
				cv.put("reminderdateandtime", share.getReminderdatetime());
				cv.put("viewmode", 0);
				cv.put("reminderresponsetype", "");

				if (share.getReminderdatetime() != null
						&& share.getReminderdatetime().length() > 0)
					cv.put("reminderstatus", 1);
				else
					cv.put("reminderstatus", 0);

				if (share.getRemindertz() != null)
					cv.put("reminderzone", share.getRemindertz());
				else
					cv.put("reminderzone", "");

				final Components cmptPro = new Components();
				cmptPro.setcomponentType(componentType);
				cmptPro.setComment(comment);
				cmptPro.setContentPath(filepath);
				cmptPro.setContentName(title);
				cmptPro.setContentThumbnail("");
				cmptPro.setDateAndTime(getNoteCreateTime());
				cmptPro.SetReminderStatus(0);
				cmptPro.setViewMode(0);
				cmptPro.setfromuser(share.getFrom());
				cmptPro.setRemDateAndTime(share.getReminderdatetime());
				cmptPro.setresponseTpe(share.getReminderResponseType());
				cmptPro.setVanishMode(share.getVanishMode());
				cmptPro.setVanishValue(share.getVanishValue());

				if (getdbHeler(context).insertComponent(cv) > 0) {
					int id = getdbHeler(context).getMaxIdToSet();
					cmptPro.setComponentId(id);
					WebServiceReferences.pendingShare -= 1;
					Log.d("thread", "c " + share.getReminderStatus());
					if (share.getReminderStatus().equalsIgnoreCase("true")) {
						Log.d("thread", "status true");
						String strDateTime = share.getReminderdatetime();
						String strQuery = "update component set reminderdateandtime='"
								+ strDateTime
								+ "',reminderstatus=1 where componentid="
								+ cmptPro.getComponentId();
						Log.d("thread", "status true " + strQuery);
						if (getdbHeler(context).ExecuteQuery(strQuery)) {
							Intent reminderIntent = new Intent(context,
									ReminderService.class);
							SipNotificationListener.getCurrentContext()
									.startService(reminderIntent);
							Log.d("thread", strQuery);
						}

					} else {
						Log.d("thread", "reminder status false");
					}

					if (cmptPro.getcomponentType() != null
							&& cmptPro.getcomponentType().equalsIgnoreCase(
									"video")) {
						String thumb = cmptPro.getContentPath().substring(0,
								cmptPro.getContentPath().lastIndexOf('.'));
						// CreateVideoThumbnail(thumb);

					}
					if (WebServiceReferences.contextTable.containsKey("MAIN")) {
						final CompleteListView complete = (CompleteListView) WebServiceReferences.contextTable
								.get("MAIN");
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								Log.d("thread", "downloadd");
								if (getdbHeler(context)
										.isRecordExists(
												"select * from component where componentid="
														+ cmptPro
																.getComponentId()
														+ "")) {

									complete.notifyFileDownloaded(cmptPro,
											share.getType(),
											cmptPro.getDateAndTime());
								}

							}
						});
					}

				}

			
				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					CompleteListView list = (CompleteListView) WebServiceReferences.contextTable
							.get("MAIN");
					list.adapter.selectAllNotes(false);
					if (list.btn_edit.getVisibility() == View.GONE) {
						list.btn_edit.setVisibility(View.VISIBLE);
					}
					if (list.filterEditText.getVisibility() == View.GONE) {
						list.filterEditText.setVisibility(View.VISIBLE);
					}
					if (WebServiceReferences.pendingShare == 0) {
						list.finish();
					}
				}
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadSIPLibrary(Context Context) {

		if (AppReference.sipnotifier == null)
			AppReference.sipnotifier = new SipNotificationListener();
		/*
		 * SipQueue sipQueue = null; SipCommunicator sipCommunicator = null; if
		 * (AppReference.sipCommunicator == null) { sipQueue = new SipQueue();
		 * sipCommunicator = new SipCommunicator(sipQueue,
		 * AppReference.sipnotifier); sipCommunicator.setRnning(true);
		 * sipCommunicator.start();
		 * 
		 * AppReference.sipCommunicator = sipCommunicator; AppReference.sipQueue
		 * = sipQueue; AppReference.sipnotifier.setcallBack();
		 * 
		 * } else { sipQueue = AppReference.sipQueue; sipCommunicator =
		 * AppReference.sipCommunicator; sipCommunicator.setRnning(true);
		 * AppReference.sipnotifier.setcallBack(); }
		 * 
		 * CommunicationBean bean = new CommunicationBean();
		 * bean.setOperationType(sip_operation_types.LOAD_LIBS);
		 * sipQueue.addMsg(bean);
		 * 
		 * File fledir = Context.getDir("Media", Context.MODE_PRIVATE); File
		 * file = new File(fledir, "hold.wav"); if (!file.exists()) {
		 * Log.d("calltest", "file existes" + file.getPath());
		 * checkisholdmusicexists(context); }
		 * 
		 * CommunicationBean mbean = new CommunicationBean();
		 * mbean.setOperationType(sip_operation_types.CREATE_PLAYER);
		 * mbean.setFile_path(file.getPath()); sipQueue.addMsg(mbean);
		 */
	}

	public void checkisholdmusicexists(Context con) {
		AssetManager assetManager = con.getAssets();
		try {
			InputStream in = assetManager.open("hold.wav");
			File fledir = con.getDir("Media", Context.MODE_PRIVATE);
			File file = new File(fledir, "hold.wav");
			FileOutputStream fout = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				fout.write(buffer, 0, read);
			}
			in.close();
			in = null;
			fout.close();
			fout.flush();
			fout = null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void destroySIPStack() {
	}

	public void GenerateimView(final Button IMRequest, final String name,
			final String id, final boolean remove, final String Message,
			final String owner) {
		testhandler.post(new Runnable() {

			@Override
			public void run() {

				// update the view for notification
				Log.d("session", "generate im.................................");
				IMRequest.setVisibility(View.VISIBLE);
				IMRequest.setEnabled(true);

				IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!remove) {
					PutImEntry(id, name, CallDispatcher.LoginUser, 1, owner);
				}

			}
		});
	}

	public void WriteReceivedHistory(SignalingBean sb) {
		if (sb.getMessage() != null) {
			if (!sb.getMessage().equals("")) {
				TextChatBean txtbean = new TextChatBean();
				txtbean.setBuddyName(sb.getFrom());
				txtbean.setChatTime(times());
				txtbean.setColor("0.000000,1.000000,0.000000,0.000000");
				txtbean.setFace("Helvetica");
				txtbean.setMessage(sb.getMessage());
				txtbean.setSize("12.000000");
				txtbean.setStyle("Normal");
				txtbean.setType(2);
				txtbean.setFlag(1);
				txtbean.setIsRobo(true);
				txtbean.setSignalId(sb.getSignalid());
				txtbean.setUserName(CallDispatcher.LoginUser);
				appendChatDatatoXML(txtbean);

			}
		}

	}

	private void appendChatDatatoXML(Object chatBean) {
		Log.i("INSTANT", "################ appendChatDatatoXML");

		try {
			Log.d("XML", "################ method called");
			myXMLWriter.addChildNode(chatBean);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("X", e.getLocalizedMessage());
		}
	}

	public void PutImEntry(final String session, final String fromUser,
			final String touser, final int viewmode, final String owner) {
		testhandler.post(new Runnable() {

			@Override
			public void run() {
				String buddy = getUser(fromUser, touser);

				ContentValues cv = new ContentValues();
				cv.put("componenttype", "IM");
				cv.put("componentname", "MultiMedia Chat");
				cv.put("componentpath", session);
				cv.put("fromuser", fromUser);
				cv.put("ftppath", touser);
				cv.put("owner", owner);
				cv.put("viewmode", "1");
				cv.put("reminderstatus", "0");
				cv.put("receiveddateandtime", getCurrentDateTime());
				cv.put("comment", buddy);
				cv.put("bscategory", "");
				cv.put("bsstatus", "");
				cv.put("bsdirection", "");
				cv.put("parentid", "");
				getdbHeler(context).insertComponent(cv);

			}
		});
	}

	public void imTone() {
		try {
			Log.d("RING", "on IM Tone()");
			if (iplayer == null) {
				Log.d("RING", "Started Player caller Ring Tone");
				iplayer = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd("im.mp3");
				iplayer.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				iplayer.setLooping(false);
				iplayer.prepare();
				iplayer.start();
				iplayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							iplayer.release();
							iplayer = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("RING", "on startCallRingTone()" + e.toString());
		}
	}

	public void notifyDownloadtoIMScreen(FTPBean bean, boolean downloadResult) {
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");
		SignalingBean sb = (SignalingBean) bean.getReq_object();
		if (sb != null) {
			if (sb.getCallType().equals("MPP")
					|| sb.getCallType().equals("MAP")
					|| sb.getCallType().equals("MVP")
					|| sb.getCallType().equals("MHP")) {
				sb.setftpObj(bean);
				sb.setIsdownloadSuccess(downloadResult);

				if (downloadResult) {
					if (sb.getisRobo() != null) {
						if (!sb.getisRobo().equalsIgnoreCase("yes")) {
							FTPBean fbean = new FTPBean();
							fbean.setServer_ip(appMainActivity.cBean
									.getRouter().split(":")[0]);
							fbean.setServer_port(40400);
							fbean.setFtp_username(bean.getFtp_username());
							fbean.setFtp_password(bean.getFtp_password());
							fbean.setFilename(bean.getFilename());
							fbean.setOperation_type(3);
							appMainActivity.getFtpNotifier().addTasktoExecutor(
									fbean);
						}
					}
				}

				// imNotifier = (IMNotifier) WebServiceReferences.contextTable
				// .get("IM");
				if (notifier == null) {
					notifier = new BackgroundNotification(context);
				}
				Log.d("IM", "Message Received");
				if ((sb.getCallType().equals("MTP"))
						|| (sb.getCallType().equals("MPP"))
						|| (sb.getCallType().equals("MVP"))
						|| (sb.getCallType().equals("MAP"))
						|| (sb.getCallType().equals("MHP")))
					Log.d("im", "im msg");
				Log.d("im", "mysession id show " + sb.getSessionid());
				Log.d("see", "calldispatcher  " + sb.getSessionid());
				Log.d("MSG", "!!!!!!!!!!!!" + sb.getMessage());

				if (sb.getCallType().equals("MPP"))
					message_map.put(sb.getFrom(), "Photo Message");
				else if (sb.getCallType().equals("MVP"))
					message_map.put(sb.getFrom(), "Video Message");
				else if (sb.getCallType().equals("MAP"))
					message_map.put(sb.getFrom(), "Audio Message");
				else if (sb.getCallType().equals("MHP"))
					message_map.put(sb.getFrom(), "Handsketch Message");
				ChatBean chatBean = getChatBean(sb);
				chatBean.setUsername(CallDispatcher.LoginUser + sb.getFrom());
				SingleInstance.getChatHistoryWriter().getQueue()
						.addObject(chatBean);

				// Vector<ChatBean> chatList = SingleInstance.chatHistory
				// .get(chatBean.getFromUser());
				// if (chatList != null)
				// chatList.add(chatBean);
				// else {
				// chatList = new Vector<ChatBean>();
				// chatList.add(chatBean);
				// SingleInstance.chatHistory.put(chatBean.getFromUser(),
				// chatList);
				// }
				ChatActivity chat = (ChatActivity) SingleInstance.contextTable
						.get("chatactivity");
				if (chat != null)
					chat.updateUI(chatBean);

				else {
					if (WebServiceReferences.Imcollection.containsKey(sb
							.getSessionid())) {
						ArrayList<SignalingBean> al = WebServiceReferences.Imcollection
								.get(sb.getSessionid());
						al.add(sb);

						if (isApplicationInBackground(context)) {

							if (this.notifier != null) {
								if (WebServiceReferences.Imcollection.size() == 1) {
									this.notifier.ShowNotification(
											sb.getMessage(), sb.getFrom() + ":"
													+ al.size(), "im");
								} else {
									this.notifier.ShowNotification(
											sb.getMessage(), "Conversation :"
													+ al.size(), "im");
								}

							}
						}

					} else {
						if (!WebServiceReferences.activeSession.containsKey(sb
								.getFrom())) {
							WebServiceReferences.activeSession.put(
									sb.getFrom(), sb.getSessionid());

							ArrayList<SignalingBean> al = new ArrayList<SignalingBean>();
							al.add(sb);
							WebServiceReferences.Imcollection.put(
									sb.getSessionid(), al);
						} else {
							String session = WebServiceReferences.activeSession
									.get(sb.getFrom());
							if (WebServiceReferences.Imcollection
									.containsKey(session)) {
								ArrayList<SignalingBean> lst = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
										.get(session);
								lst.add(sb);

							} else {
								WebServiceReferences.activeSession.remove(sb
										.getFrom());
								WebServiceReferences.activeSession.put(
										sb.getFrom(), sb.getSessionid());

								ArrayList<SignalingBean> al = new ArrayList<SignalingBean>();
								al.add(sb);
								WebServiceReferences.Imcollection.put(
										sb.getSessionid(), al);
							}
						}
						AppMainActivity appActivity = (AppMainActivity) WebServiceReferences.contextTable
								.get("MAIN");
						GenerateimView(appActivity.imView, sb.getFrom(),
								sb.getSessionid(), false, "",
								CallDispatcher.LoginUser);
						imTone();
						handlerForCall.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ContactsFragment.getContactAdapter()
										.notifyDataSetChanged();
								
								ExchangesFragment exchanges = ExchangesFragment
										.newInstance(context);
								if (exchanges != null)
									exchanges.adapter.notifyDataSetChanged();

							}
						});

					}

					if (imNotifier != null) {
						Log.d("cam", "imNotifier not null");
						imNotifier.notifyReceivedIM(sb);

					} else {
						Log.d("cam", "IMNotifier is null");
					}
				}
			}
		}
	}

	public Bitmap Resizeresource(int resource_id, Context context) {
		try {
			int targetWidth = noScrWidth;// (int) (noScrWidth / 1.2);
			int targetHeight = noScrHeight;// (int) (noScrHeight / 1.2);

			Bitmap bitMapImage = null;
			Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
					resource_id, options);
			if (bmp != null)
				bmp.recycle();
			double sampleSize = 0;
			Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
					.abs(options.outWidth - targetWidth);

			if (options.outHeight * options.outWidth * 2 >= 1638) {

				sampleSize = scaleByHeight ? options.outHeight / targetHeight
						: options.outWidth / targetWidth;
				sampleSize = (int) Math.pow(2d,
						Math.floor(Math.log(sampleSize) / Math.log(2d)));
			}

			options.inJustDecodeBounds = false;
			options.inTempStorage = new byte[128];
			while (true) {
				try {
					if (bitMapImage != null)
						bitMapImage.recycle();
					System.gc();
					options.inSampleSize = (int) sampleSize;
					bitMapImage = BitmapFactory.decodeResource(
							context.getResources(), resource_id, options);
					break;
				} catch (Exception ex) {
					try {
						sampleSize = sampleSize * 2;
					} catch (Exception ex1) {

					}
				}
			}
			return bitMapImage;
		} catch (OutOfMemoryError e) {
			Log.e("bitmap", "===> " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public void openReceivedIm(View v, Context context) {

		Log.d("imclick", "imarray button====================="
				+ WebServiceReferences.Imcollection.size());
		if (WebServiceReferences.Imcollection.size() > 0) {
			ArrayList<String> al = new ArrayList<String>();
			HashMap<String, String> hsIMS = new HashMap<String, String>();

			int[] ix = new int[WebServiceReferences.Imcollection.size()];
			Iterator myVeryOwnIterator = WebServiceReferences.Imcollection
					.keySet().iterator();
			int i = 0;
			while (myVeryOwnIterator.hasNext()) {
				String key = (String) myVeryOwnIterator.next();
				ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
						.get(key);
				ix[i] = value.size();
				SignalingBean sx = value.get(0);
				String srtNames = sx.getFrom();
				Log.i("PROFILELOGS", "type===>" + sx.getCallType());
				Log.i("PROFILELOGS", "message===>" + sx.getMessage());

				al.add(srtNames);
				hsIMS.put(srtNames, sx.getSessionid());
				i += 1;
			}
			ShowImMembers(al, v, ix, hsIMS, context);

		} else {
			v.setVisibility(View.GONE);
			Toast.makeText(context, "No IM", Toast.LENGTH_SHORT).show();
		}
		Log.d("imclick", "imarray button finished");
	}

	public void ShowImMembers(final ArrayList al, final View v, final int[] ix,
			final HashMap<String, String> hs, final Context context) {

		try {
			final QuickAction1 quickAction = new QuickAction1(context);
			for (int i = 0; i < al.size(); i++) {
				ActionItem1 dashboard = new ActionItem1();
				// dashboard.setTitle("My name is Test");
				String size = Integer.toString(ix[i]);
				dashboard.setTitle((String) al.get(i) + "   " + size);
				// dashboard.
				dashboard.setIcon(context.getResources().getDrawable(
						R.drawable.kontak));
				quickAction.addActionItem(dashboard);

			}
			quickAction
					.setOnActionItemClickListener(new QuickAction1.OnActionItemClickListener() {
						@Override
						public void onItemClick(int pos) {

							try {
								Log.e("IM", "Postition :" + pos);
								Log.e("IM", "NAME :" + al.get(pos));

								Log.e("IM", "Session :" + hs.get(al.get(pos)));
								String name = (String) al.get(pos);
								String session = (String) hs.get(al.get(pos));

								showActiveSession(session, name, context);
								al.remove(pos);
								if (al.size() <= 0) {
									v.setVisibility(View.INVISIBLE);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});

			quickAction.show(v);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showActiveSession(String sessionId, String selectedBuddy,
			Context cntxt) {
		try {

			// if
			// (WebServiceReferences.instantMessageScreen.containsKey(sessionId))
			// {
			// InstantMessageScreen imscreen = (InstantMessageScreen)
			// WebServiceReferences.instantMessageScreen
			// .get(session);
			// imscreen.finish();
			// }

			BuddyInformationBean bean = null;

			for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
				if (!bib.isTitle()) {
					if (bib.getName().equalsIgnoreCase(selectedBuddy)) {
						bean = bib;
						break;
					}
				}
			}

			String state = bean.getStatus();
			// SignalingBean bean = new SignalingBean();
			// bean.setSessionid(session);
			// bean.setFrom(CallDispatcher.LoginUser);
			// bean.setTo(selectedBuddy);
			// bean.setConferencemember("");
			// bean.setMessage("");
			// bean.setCallType("MSG");
			Intent intent = new Intent(context, ChatActivity.class);
			// intent.putExtra("sb", bean);
			// intent.putExtra("fromto", true);
			intent.putExtra("buddy", selectedBuddy);
			intent.putExtra("status", state);
			intent.putExtra("sessionid", sessionId);
			cntxt.startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendLocationtoServer(double lat, double longi) {// if(CallDispatcher.latitude!=lat&&CallDispatcher.longitude!=longi)

		if (latitude == 0.0 && longitude == 0.0)
			call_heartbeat = true;

		CallDispatcher.latitude = lat;
		CallDispatcher.longitude = longi;
		String latt = Double.toString(lat);
		String longg = Double.toString(longi);

		try {
			if (LoginUser != null) {
				String[] locationDetails = new String[5];
				locationDetails[0] = LoginUser;
				locationDetails[1] = latt;
				locationDetails[2] = longg;
				locationDetails[3] = "";
				locationDetails[4] = "0";
//				WebServiceReferences.webServiceClient
//						.saveMyLocation(locationDetails);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void LocationMadeService(String username, String calltype) {
		if (location_Service.equalsIgnoreCase("video unicast")) {
			calltype = "VP";
		} else if (location_Service.equalsIgnoreCase("audio unicast")) {
			calltype = "AP";
		} else if (location_Service.equalsIgnoreCase("Video Call")) {
			calltype = "VC";
		} else if (location_Service.equalsIgnoreCase("Audio Call")) {
			calltype = "AC";
		}

		BuddyInformationBean bib = null;

		for (BuddyInformationBean temp : ContactsFragment.getBuddyList()) {
			if (!temp.isTitle()) {
				if (temp.getName().equalsIgnoreCase(username)) {
					bib = temp;
					break;
				}
			}
		}
		if (bib != null) {
			// BuddyInformationBean bib = WebServiceReferences.buddyList
			// .get(username);
			CallDispatcher.sb = new SignalingBean();
			Log.e("ACal", "user (Who is log on) " + CallDispatcher.LoginUser);

			CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);

			CallDispatcher.sb.setTo(username);

			CallDispatcher.sb.setType("0");
			CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
			CallDispatcher.sb.setResult("0");
			CallDispatcher.sb.setTopublicip(bib.getExternalipaddress());
			CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
			CallDispatcher.sb.setToSignalPort(bib.getSignalingPort());
			CallDispatcher.sb.setCallType(calltype);

			CallDispatcher.dialChecker = true;
			CallDispatcher.isCallInitiate = true;
			SignalingBean sb = (SignalingBean) CallDispatcher.sb.clone();
			AppMainActivity.commEngine.makeCall(sb);
			bib.setIslocationBasedServiceDone(true);
			ShowConnectionScreen(sb, bib.getName(),
					SipNotificationListener.getCurrentContext(), false);

		}
	}

	void sendNotesOnLatLong(String to) {
		ShareReminder reminder = new ShareReminder();
		Utility utility = new Utility();
		reminder.setFrom(CallDispatcher.LoginUser);
		reminder.setTo(to);
		reminder.setType("photonotes");
		reminder.setReceiverStatus("manual");
		File path = new File(imagepath);
		String file = path.getName();
		reminder.setFileLocation(file);
		reminder.setRemindertz("");
		reminder.setRemindertz("auto");
		reminder.setReminderdatetime("");
		reminder.setFileid(Long.toString(utility.getRandomMediaID()));

		reminder.setReminderStatus("false");
		Log.d("ftp", "webserc");
		WebServiceReferences.webServiceClient.ShareFiles(reminder);
		Log.d("ftp", "webserc send");
	}

	// void doLocationMadeService() {
	// Log.d("UMBX", "OnLOgin doLocationMadeService()");
	// try {
	//
	// Set<String> set = WebServiceReferences.buddyList.keySet();
	//
	// String buddy = null;
	//
	// Iterator<String> iterator = set.iterator();
	//
	// while (iterator.hasNext()) {
	// // System.out.println("main screen alert");
	// buddy = iterator.next();
	// // System.out.println("main screen alertee");
	// BuddyInformationBean bib = null;
	//
	//
	// // BuddyInformationBean bean = null;
	//
	// for (BuddyInformationBean temp : ContactsFragment.buddyList) {
	// if (temp.getName().equalsIgnoreCase(buddy)) {
	// bib = temp;
	// break;
	// }
	// }
	//
	// Log.d("stattus",
	// "user " + bib.getName() + " state " + bib.getStatus());
	// // System.out.println("xxxxxxxxxxxx");
	// // String v="asdf";
	// Log.d("UMBX", "OnLOgin doLocationMadeService()" + bib.getName());
	// if (!bib.getName().equalsIgnoreCase(LoginUser)) {
	// Log.d("UMBX", "OnLOgin buddy " + bib.getName());
	// Log.d("UMBX", "OnLOgin status " + bib.getStatus());
	// Log.d("UMBX", "OnLOgin location " + bib.getLatitude() + " "
	// + bib.getLongitude());
	// if (bib.getStatus().equals("1")
	// || bib.getStatus().equalsIgnoreCase("online")) {
	// if (bib.getLatitude() != null
	// && bib.getLongitude() != null) {
	// if (bib.getLatitude().trim().length() != 0
	// && bib.getLongitude().trim().length() != 0) {
	// //
	// Log.d("UMBX", "OnLOgin " + bib.getLatitude()
	// + " " + bib.getLongitude());
	// try {
	// // used for two digit latLong....
	// // double configu
	// String settingsLat = String.format("%.2f",
	// latConfigure);
	// Log.d("UMBX", "sett " + settingsLat);
	// String settingsLong = String.format("%.2f",
	// longConfigure);
	// Log.d("UMBX", "sett " + settingsLong);
	// String chklat = settingsLat.substring(0,
	// settingsLat.indexOf(".") + 2);
	// Log.d("UMBX", "OnLOgin " + chklat);
	// String chklong = settingsLong.substring(0,
	// settingsLong.indexOf(".") + 2);
	// Log.d("UMBX", "OnLOgin " + chklong);
	//
	// double receivedlatvalueindouble = Double
	// .parseDouble(bib.getLatitude());
	//
	// double receivedlongvalueindouble = Double
	// .parseDouble(bib.getLongitude());
	//
	// String recLat = String.format("%.2f",
	// receivedlatvalueindouble);
	// Log.d("UMBX", "sett " + recLat);
	//
	// String recLong = String.format("%.2f",
	// receivedlongvalueindouble);
	// Log.d("UMBX", "sett " + recLong);
	//
	// String recchaklat = recLat.substring(0,
	// recLat.indexOf(".") + 2);
	// Log.d("UMBX", "OnLOgin " + recchaklat);
	// String recchklong = recLong.substring(0,
	// recLong.indexOf(".") + 2);
	// Log.d("UMBX", "OnLOgin " + recchklong);
	//
	// // String current_lat=
	// // Double.toString(latConfigure);
	// //
	// // String
	// // current_long=Double.toString(longConfigure);
	//
	// Log.d("UMBX", "received " + recLat + " "
	// + recLong + " " + settingsLat + " "
	// + settingsLat);
	// if (chklat.equals(recchaklat)
	// && chklong.equals(recchklong)) {
	// // final boolean isMakeCall=false;
	//
	// Log.d("UMBX", "received if");
	// // handlerForCall.post(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // TODO Auto-generated method stub
	// if (serviceType != 2) {
	// if (currentSessionid == null) {
	//
	// Toast.makeText(context,
	// "Make call", 2000)
	// .show();
	// Log.d("UMBX",
	// "calling "
	// + bib.getName());
	// bib.setIslocationBasedServiceDone(true);
	//
	// LocationMadeService(
	// bib.getName(), "");
	// break;
	//
	// }
	// } else {
	// if (imagepath != null) {
	// Toast.makeText(context,
	// "sending notes", 2000)
	// .show();
	//
	// sendNotesOnLatLong(bib
	// .getName());
	// }
	// }
	//
	// } else {
	// Log.d("UMBX", "received else");
	// }
	//
	// //
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.d("UMBX", "OnLOgin exception " + e);
	// }
	// }
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// Log.d("UMBX", "Processed");
	//
	// } catch (Exception e) {
	// Log.d("UMBX", "exception" + e.toString());
	// e.printStackTrace();
	//
	// }
	//
	// }

	public void notifyDeleteallshareResponse(final Object obj) {
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cancelDialog();
				if (obj instanceof String[]) {
					String[] delete_response = (String[]) obj;
					if (delete_response[2] != null) {
						getdbHeler(context).deleteAllshares(
								"DELETE from formsettings WHERE buddyid="
										+ "\"" + delete_response[2].trim()
										+ "\"");
						getdbHeler(context).deleteAllshares(
								"DELETE from profilefieldvalues WHERE userid="
										+ "\"" + delete_response[2].trim()
										+ "\"");
						getdbHeler(context).deleteAllshares(
								"DELETE from offlinecallsettingdetails WHERE buddyid="
										+ "\"" + delete_response[2].trim()
										+ "\"");
						Toast.makeText(context, "Deleted Succesfully",
								Toast.LENGTH_SHORT).show();
					}
				} else if (obj instanceof WebServiceBean) {

					Toast.makeText(context, ((WebServiceBean) obj).getText(),
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	public void setUserSettings() {

		settings = getdbHeler(SipNotificationListener.getCurrentContext())
				.getUserSettings();
		if (settings != null) {
			if (settings.getLocationserviceEnabled() != null
					&& settings.getLocationserviceEnabled().equals("1"))
				isLocationServiceEnabled = true;
			else
				isLocationServiceEnabled = false;

			if (isLocationServiceEnabled) {
				if (settings.getLocationValue1() != null
						&& settings.getLocationValue2() != null
						&& settings.getLocationValue1().trim().length() > 0
						&& settings.getLocationValue2().trim().length() > 0) {
					latConfigure = Double.parseDouble(settings
							.getLocationValue1());
					longConfigure = Double.parseDouble(settings
							.getLocationValue2());
				}

				if (settings.getLocationService() != null
						&& settings.getLocationService().trim().length() > 0) {
					location_Service = getdbHeler(
							SipNotificationListener.getCurrentContext())
							.getServiceName(settings.getLocationService());
				}
				serviceType = 0;

			}

			if (settings.getAutoPlayService() != null
					&& settings.getAutoPlayService().equals("1"))
				WebServiceReferences.isAutoplay = true;
			else
				WebServiceReferences.isAutoplay = false;

			if (settings.getShareToneSevice() != null
					&& settings.getShareToneSevice().equals("1"))
				setToneEnabled(true);
			else
				setToneEnabled(false);

		}

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(SipNotificationListener
						.getCurrentContext().getApplicationContext());
		WebServiceReferences.globaldateformat = pref.getString("dateformate",
				"MM-dd-yyyy");
		pref = null;

	}

	public void dissableUserSettings() {
		WebServiceReferences.isAutoplay = false;
		setToneEnabled(false);
		isLocationServiceEnabled = false;
		latConfigure = 0;
		longConfigure = 0;
		location_Service = "";
		serviceType = 5;
		setSettings(null);
		CallDispatcher.action = 0;
		WebServiceReferences.globaldateformat = "MM-dd-yyyy";
	}

	// For image Rotation
	public Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}

	public void changemyPictureOrientation(Bitmap img, String ComponentPath) {
		try {
			img = Bitmap.createScaledBitmap(img, 100, 75, false);
			ExifInterface ei = new ExifInterface(ComponentPath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				Log.i("camera", "Orientation===>ORIENTATION_ROTATE_90");

				Bitmap bmp90 = RotateBitmap(img, 90);
				FileOutputStream fileOutputStream90 = new FileOutputStream(
						ComponentPath);
				bmp90.compress(CompressFormat.JPEG, 75, fileOutputStream90);
				fileOutputStream90.flush();
				fileOutputStream90.close();
				fileOutputStream90 = null;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				Log.i("camera", "Orientation===>ORIENTATION_ROTATE_180");

				Bitmap bmp270 = RotateBitmap(img, 270);
				FileOutputStream fileOutputStream270 = new FileOutputStream(
						ComponentPath);
				bmp270.compress(CompressFormat.JPEG, 75, fileOutputStream270);
				fileOutputStream270.flush();
				fileOutputStream270.close();
				fileOutputStream270 = null;

				break;
			case ExifInterface.ORIENTATION_ROTATE_180:

				Bitmap bmp = RotateBitmap(img, 180);
				FileOutputStream fileOutputStream = new FileOutputStream(
						ComponentPath);
				bmp.compress(CompressFormat.JPEG, 75, fileOutputStream);

				break;
			case ExifInterface.ORIENTATION_NORMAL:

				break;
			//
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void appLogout(String message, String url, int type, Context context) {
		cancelDialog();
		showAppLogoutAlert("Response", message, url, type, context);
	}

	public void showAppLogoutAlert(final String title, final String message,
			final String url, final int type, final Context context) {
		// TODO Auto-generated method stub
		Log.i("appver", "dialog title :: " + title);
		Log.i("appver", "dialog message :: " + message);
		Log.i("appver", "dialog type :: " + type);
		handlerForCall.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("appver", "Before create dialog");
				Log.i("appver", "dialog title :: " + title);
				Log.i("appver", "dialog message :: " + message);
				Log.i("appver", "dialog type :: " + type);
				final AlertDialog myAlertDialog = new AlertDialog.Builder(
						context).create();
				Log.i("appver", "After create dialog");
				Log.i("appver", "dialog title :: " + title);
				Log.i("appver", "dialog message :: " + message);
				Log.i("appver", "dialog type :: " + type);
				myAlertDialog.setTitle(title);
				myAlertDialog.setMessage(message);
				myAlertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								if (type == 2) {
									try {
										Intent i = new Intent(context,
												TestHTML5WebView.class);
										Bundle bundle = new Bundle();
										bundle.putString("url", url);
										i.putExtras(bundle);
										context.startActivity(i);
										p = PreferenceManager
												.getDefaultSharedPreferences(context);
										boolean autologinstate = p.getBoolean(
												"autologin", false);
										// logout(autologinstate);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								myAlertDialog.dismiss();
							}
						});
				myAlertDialog.show();
			}
		});

	}

	public void restoreState() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isloggedin = preferences.getBoolean("islogin", false);
		if (isloggedin) {
			String username = preferences.getString("uname", "");
			String password = preferences.getString("pword", "");
			if (username.trim().length() > 0 && password.trim().length() > 0) {
				CallDispatcher.LoginUser = username;
				CallDispatcher.Password = password;
				silentSignIn(username);
			}
		}
		preferences = null;

	}

	private void viewProfile(String buddy) {
		ViewProfileFragment viewProfileFragment = ViewProfileFragment
				.newInstance(context);
		// Bundle bundle = new Bundle();
		// bundle.putString("buddyname", buddy);
		// viewProfileFragment.setArguments(bundle);
		viewProfileFragment.setBuddyName(buddy);
		FragmentManager fragmentManager = ((AppMainActivity) SingleInstance.contextTable
				.get("MAIN")).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.activity_main_content_fragment,
				viewProfileFragment);
		fragmentTransaction.commit();

	}

	public void doViewProfile(String buddy) {

		try {
			ArrayList<String> profileList = getdbHeler(context).getProfile(
					buddy);
			try {
				if (AppReference.isWriteInFile)
					AppReference.logger.error("size of arrayList--->"
							+ profileList.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				// Intent intent = new Intent(context, ViewProfiles.class);
				// intent.putExtra("buddyname", buddy);
				// SipNotificationListener.getCurrentContext().startActivity(
				// intent);
				viewProfile(buddy);

			} else {

				Log.i("profile", "VIEW PROFILE------>" + buddy
						+ "---->GetProfileDetails");
				CallDispatcher.pdialog = new ProgressDialog(context);
				showprogress(CallDispatcher.pdialog, context);
				CallDispatcher.isFromCallDisp = false;
				String modifiedDate = getdbHeler(context).getModifiedDate(
						"select max(modifieddate) from profilefieldvalues where userid='"
								+ buddy + "'");
				if (modifiedDate == null) {
					modifiedDate = "";
				} else if (modifiedDate.trim().length() == 0) {
					modifiedDate = "";
				}
				String[] profileDetails = new String[3];
				profileDetails[0] = buddy;
				profileDetails[1] = "5";
				profileDetails[2] = modifiedDate;
				// view = 1;
				// isProfileRequested = true;
				// WebServiceReferences.webServiceClient
				// .getStandardProfilefieldvalues(profileDetails);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public static String containsLocalPath(String fileName) {
		if (fileName.contains(Environment.getExternalStorageDirectory()
				+ "/COMMedia/"))
			return fileName;
		else
			return Environment.getExternalStorageDirectory() + "/COMMedia/"
					+ fileName;
	}

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();
	}

	public String getCurrentDateandTime() {
		try {
			// Calendar c = Calendar.getInstance();
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss aa");
			// String strDate = sdf.format(c.getTime());

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public static String loadCurrentStatus() {
        String status = null;
        try {

            if (CallDispatcher.myStatus.equals("1")) {
                status = SingleInstance.mainContext.getString(R.string.online);
            } else if (CallDispatcher.myStatus.equals("3")) {
                status = SingleInstance.mainContext.getString(R.string.away);
            } else if (CallDispatcher.myStatus.equals("4")) {
                status = SingleInstance.mainContext.getString(R.string.stealth);
            } else if (CallDispatcher.myStatus.equals("0")) {
                status = SingleInstance.mainContext.getString(R.string.offline);
            } else if (CallDispatcher.myStatus.equals("2")) {
                status = SingleInstance.mainContext.getString(R.string.busy);
            }

            if (status == null)
                status = "";

        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
        return status;
    }

	public void requestAudioBroadCast(String to) {

		if (!SingleInstance.instanceTable.containsKey("callscreen")
				&& !SingleInstance.instanceTable
						.containsKey("alertscreen")
				&& !WebServiceReferences.contextTable
						.containsKey("sicallalert")
				&& !WebServiceReferences.contextTable
						.containsKey("sipcallscreen")) {
			String value = to + ",";

			String[] str = value.split(",");
			String offlinenames = null;
			// ArrayList< String> datas=new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {

				for (BuddyInformationBean bib : ContactsFragment.getBuddyList()) {
					if (!bib.isTitle()) {
						if (bib.getName().equalsIgnoreCase(str[i])) {
							if (bib.getStatus().equalsIgnoreCase("online")) {

								CallDispatcher.conConference.add(str[i]);
							} else {
								if (offlinenames == null) {
									offlinenames = str[i];
								} else {
									offlinenames = offlinenames + "," + str[i];
								}

							}
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				// if (bib.getStatus().startsWith("Onli")) {
				// } else {
				// if (offlinenames == null) {
				// offlinenames = str[i];
				// } else {
				// offlinenames = offlinenames + "," + str[i];
				// }
				//
				// }

			}

			ConMadeConference("ABC",
					SipNotificationListener.getCurrentContext());
			if (offlinenames != null) {
				Toast.makeText(context, offlinenames + " is on Offline",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	public void showCallHistory(final String strSessionId, final String callType)
	{
		try {
			checkandcloseDialog();
			Log.i("AudioCall","came to showCallHistory");
			final Dialog dialog = new Dialog(SingleInstance.mainContext);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.call_record_dialog);
			dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
			dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
			dialog.show();
			history_dialog = dialog;
			mHandler=new Handler();
			Button save = (Button) dialog.findViewById(R.id.save);
			Button delete = (Button) dialog.findViewById(R.id.delete);
			final ImageView play_button = (ImageView) dialog.findViewById(R.id.play_button);
			final SeekBar seekBar1 = (SeekBar) dialog.findViewById(R.id.seekBar1);
			final TextView txt_time= (TextView)dialog.findViewById(R.id.txt_time);
			if(CallDispatcher.sb.getCallDuration() != null) {
				txt_time.setText(CallDispatcher.sb.getCallDuration());
			}
			Log.i("AudioCall","came to showCallHistory 1");
			play_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String file = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/CallRecording/"
							+ strSessionId + ".wav";
					Log.d("Stringpath", "mediapath--->" + file);
					File newfile=new File(file);

					if (mPlayer.isPlaying()) {
						mPlayer.pause();
						play_button.setBackgroundResource(R.drawable.play);
					} else {
						play_button.setBackgroundResource(R.drawable.audiopause);
						if(newfile.exists())
						playAudio(file, 0);

					}
					if(newfile.exists()) {

						int position = 0;
						if (position == mPlayingPosition) {
							mProgressUpdater.mBarToUpdate = seekBar1;
							mProgressUpdater.tvToUpdate = txt_time;
							mHandler.postDelayed(mProgressUpdater, 100);
						} else {

							try {
								Log.d("Stringpath", "mediapath--->");
								seekBar1.setProgress(0);
								MediaMetadataRetriever mmr = new MediaMetadataRetriever();
								mmr.setDataSource(file);
								String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
								mmr.release();
								String min, sec;
								min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
								sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
								if (Integer.parseInt(min) < 10) {
									min = 0 + String.valueOf(min);
								}
								if (Integer.parseInt(sec) < 10) {
									sec = 0 + String.valueOf(sec);
								}
								txt_time.setText(min + ":" + sec);
//                            audio_tv.setText(duration);
							} catch (Exception e) {
								e.printStackTrace();
							}

							seekBar1.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
							seekBar1.setProgress(0);
							if (mProgressUpdater.mBarToUpdate == seekBar1) {
								//this progress would be updated, but this is the wrong position
								mProgressUpdater.mBarToUpdate = null;
							}
						}
					}
				}

			});


			save.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Intent intentComponent = new Intent(context,
							CallHistoryActivity.class);
					intentComponent.putExtra("buddyname",
							CallDispatcher.sb.getFrom());
					intentComponent.putExtra("individual", true);
					if(callType.equalsIgnoreCase("VC"))
					intentComponent.putExtra("audiocall",false);
					intentComponent.putExtra("isDelete", false);
					intentComponent.putExtra("sessionid",
							CallDispatcher.sb.getSessionid());
					context.startActivity(intentComponent);
					mPlayer.stop();
				}
			});
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Intent intentComponent = new Intent(context,
							CallHistoryActivity.class);
					intentComponent.putExtra("buddyname",
							CallDispatcher.sb.getFrom());
					intentComponent.putExtra("isDelete", true);
					if(callType.equalsIgnoreCase("VC"))
						intentComponent.putExtra("audiocall",false);
					intentComponent.putExtra("individual", true);
					intentComponent.putExtra("sessionid",
							CallDispatcher.sb.getSessionid());
					context.startActivity(intentComponent);
					mPlayer.stop();
				}
			});
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	public void checkandcloseDialog() {
		Log.i("AudioCall","came to checkandcloseDialog");
		if(history_dialog != null) {
			if(history_dialog.isShowing()){
				history_dialog.dismiss();
			}
		}
	}

	private class PlaybackUpdater implements Runnable {
		public SeekBar mBarToUpdate = null;
		public TextView tvToUpdate = null;

		@Override
		public void run() {
			if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
				Log.d("Mposition","seekbar---->");
				double tElapsed = mPlayer.getCurrentPosition();
				int fTime = mPlayer.getDuration();
				double timeRemaining = fTime - tElapsed;
				double sTime = mPlayer.getCurrentPosition();

				String min, sec;
				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
				if (Integer.parseInt(min) < 10) {
					min = 0 + String.valueOf(min);
				}
				if (Integer.parseInt(sec) < 10) {
					sec = 0 + String.valueOf(sec);
				}
				tvToUpdate.setText(min + ":" + sec);
				mBarToUpdate.setProgress((100 * mPlayer.getCurrentPosition() / mPlayer.getDuration()));
//                tvToUpdate.setText(String.format("%d:%d ",TimeUnit.MILLISECONDS.toMinutes((long) fTime),TimeUnit.MILLISECONDS.toSeconds((long) fTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) fTime))));
				mHandler.postDelayed(this, 500);

			} else {
				//not playing so stop updating
			}
		}
	}

	private void stopPlayback() {
		mPlayingPosition = 0;
		mProgressUpdater.mBarToUpdate = null;
		mProgressUpdater.tvToUpdate = null;
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.stop();
	}

	public void playAudio(String fname,  int position) {
		try {
			mPlayer.reset();
			mPlayer.setDataSource(fname);
			mPlayer.prepare();
			mPlayer.start();
			mPlayingPosition = position;

			mHandler.postDelayed(mProgressUpdater, 500);

		} catch (IOException e) {

			e.printStackTrace();
			stopPlayback();
		}
	}

}
