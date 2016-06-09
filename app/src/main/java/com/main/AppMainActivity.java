/**
 * 
 */
package com.main;

import org.core.ProprietarySignalling;
import org.lib.PatientDetailsBean;
import org.lib.model.FileDetailsBean;
import org.lib.model.GroupMemberBean;
import org.lib.model.PatientCommentsBean;
import org.lib.model.RoleAccessBean;
import org.lib.model.RoleCommentsViewBean;
import org.lib.model.RoleEditRndFormBean;
import org.lib.model.RolePatientManagementBean;
import org.lib.model.RoleTaskMgtBean;
import org.lib.model.PatientDescriptionBean;
import org.lib.model.TaskDetailsBean;
import org.lib.model.UdpMessageBean;
import org.tcp.TCPEngine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Fingerprint.MainActivity;
import com.base.android.pjsua.PjsuaInterface;
import com.base.sipclientwrapper.SipClientWrapper;
import com.bean.BuddyPermission;
import com.bean.ConnectionBrokerServerBean;
import com.bean.DefaultPermission;
import com.bean.FormFieldBean;
import com.bean.GroupChatBean;
import com.bean.GroupChatPermissionBean;
import com.bean.IndividualPermission;
import com.bean.ProfileBean;
import com.bean.SpecialMessageBean;
import com.bean.UploadDownloadStatusBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.account.ChangePassword;
import com.cg.account.FindPeople;
import com.cg.account.MyAccountActivity;
import com.cg.account.PinAndTouchId;
import com.cg.account.PinSecurity;
import com.cg.account.SecurityQuestions;
import com.cg.account.forgotPassword;
import com.cg.avatar.AvatarActivity;
import com.cg.callservices.AudioCallScreen;
import com.cg.callservices.AudioPagingSRWindow;
import com.cg.callservices.CallConnectingScreen;
import com.cg.callservices.SipCallConnectingScreen;
import com.cg.callservices.VideoCallScreen;
import com.cg.callservices.VideoPagingSRWindow;
import com.cg.callservices.inCommingCallAlert;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.Logger;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.TestHTML5WebView;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.files.Components;
import com.cg.forms.Alert;
import com.cg.forms.AppsView;
import com.cg.forms.FormRecordsCreators;
import com.cg.forms.FormViewer;
import com.cg.forms.InputsFields;
import com.cg.ftpprocessor.FTPBean;
import com.cg.ftpprocessor.FTPNotifier;
import com.cg.ftpprocessor.FTPQueue;
import com.cg.hostedconf.AppReference;
import com.cg.profiles.ViewProfiles;
import com.cg.rounding.NotificationReceiver;
import com.cg.rounding.RoundNewPatientActivity;
import com.cg.rounding.RoundingEditActivity;
import com.cg.rounding.RoundingFragment;
import com.cg.rounding.RoundingGroupActivity;
import com.cg.rounding.TaskCreationActivity;
import com.cg.settings.UserSettingsBean;
import com.cg.snazmed.R;
import com.cg.snazmed.R.drawable;
import com.cg.timer.FileBroadCastReceiver;
import com.cg.timer.HeartbeatTimer;
import com.cg.timer.MissedDownloadReceiver;
import com.cg.timer.ReminderService;
import com.crypto.AESFileCrypto;
import com.ftp.ChatFTPBean;
import com.ftp.FTPPoolManager;
import com.group.GroupActivity;
import com.group.GroupAdapter1;
import com.group.GroupRequestFragment;
import com.group.chat.ForwardUserSelect;
import com.group.chat.GroupChatActivity;
import com.group.chat.GroupChatBroadCastReceiver;
import com.image.utils.ImageLoader;
import com.process.BGProcessor;
import com.process.MemoryProcessor;
import com.screensharing.ScreenSharingFragment;
import com.thread.CommunicationBean;
import com.thread.SipCommunicator;
import com.thread.SipCommunicator.sip_operation_types;
import com.thread.SipQueue;
import com.util.MyExceptionHandler;
import com.util.SingleInstance;
import com.util.Utils;
import com.util.VideoPlayer;

import org.core.CommunicationEngine;
import org.lib.model.AppVersionUpdateBean;
import org.lib.model.BuddyInformationBean;
import org.lib.model.ChattemplateModifieddate;
import org.lib.model.ConnectionBrokerBean;
import org.lib.model.FieldTemplateBean;
import org.lib.model.FormAttributeBean;
import org.lib.model.FormRecordsbean;
import org.lib.model.FormSettingBean;
import org.lib.model.FormsBean;
import org.lib.model.FormsListBean;
import org.lib.model.Formsinfocontainer;
import org.lib.model.GroupBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.ShareReminder;
import org.lib.model.WebServiceBean;
import org.lib.model.chattemplatebean;
import org.lib.webservice.Servicebean;
import org.util.Utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class AppMainActivity extends FragmentActivity implements PjsuaInterface,TCPEngine.TCPListener{

	private MediaPlayer iplayer;
    public static boolean isLogin;
	public static Context inActivity;
    public static  String relam = "";
    public static  String registrar = "";
    public static  String proxy = "122.165.92.35";
	private String[] lvMenuItems;
	public static Context searchContext;
	private TypedArray menuIcons;
	Handler handler = new Handler();
	private CallDispatcher callDisp;
	public ProgressDialog progress;
	public ProgressDialog formprogress;
	Button patchbutton;
	Button filepatchbtn;
	private ImageLoader imageLoader;
	private String profileImagePath = "";
	public boolean isFormLoaded = false;
	public boolean isInternetEnabled = true;
	public HeartbeatTimer HBT = null;
	// private ArrayList<String> tempMsgCount = new ArrayList<String>();
	private AlertDialog alertDelay = null;
	public MediaPlayer player = null;
	private String autoPlayType = null;
	private LinearLayout footer_lay = null;
    public static boolean isPinEnable = false;
	// Menu button
    ImageView btMenu;
    public static String ip="";
    public static TCPEngine tcpEngine;
    public static int port=0;

	// Title according to fragment
	TextView tvTitle;

	public static CommunicationEngine commEngine = null;

	public ConnectionBrokerServerBean cBean = null;

	private Vector<FieldTemplateBean> OtherDetails = new Vector<FieldTemplateBean>();

	private FTPNotifier ftpNotifier;

	private ImageView iv_LoginLogout;

	private ImageView profileImage;

	public TextView tv_namestatus;

	private AlertDialog alert = null;

	Context context = null;
	public long mLastClickTime = 0;
	private Timer timer;

	private Timer timerQA;

	private MyTimerTask myTimerTask;
	
	private AlertDialog.Builder missedCallAlert;

	private MyTimerTaskQA myTimerTaskQA;

	private SharedPreferences preferences = null;

	private KeyguardManager keyguardManager;

	private KeyguardLock lock;

	protected PowerManager.WakeLock mWakeLock;

	private ArrayList<Object> formsConfig = new ArrayList<Object>();

	public Button imView;

	public ArrayList<String[]> profileDetails = new ArrayList<String[]>();

	private MediaPlayer mediaPlayer;

	public String formnames = null;

	public static boolean DBInsideApp = false;

	private boolean shareScreen = false;
	private LinearLayout recentLay;
	public String[] profession=null;

	private String buddyName = "";
	TextView recenttv, tvcontacts, tvfiles, tvmore,appstv;
	private volatile SipCommunicator sipCommunicator;

	public static SipClientWrapper sipClientWrapper = new SipClientWrapper();

	private String toUserName;

	public static HashMap<String,ChatFTPBean> imFiles = new HashMap<>();

	Dialog incomingCall_alertDialog=null;
    public  String fileLoc=null;
	public static String connectedbuddies=null;
	public static String pinNo=null;
	public  String[] questions=null;
	public String seccreatedDate="";
	public int count=0;
	public String EidforGroup;
	public static Chronometer ctimer,cvtimer;

	public String activityOnTop;
	public boolean openPinActivity=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			MultiDex.install(this);
			super.onCreate(savedInstanceState);

			// Inflate the mainLayout

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			Log.i("AAAA","App Main "+Build.VERSION.SDK_INT);
//			if (Build.VERSION.SDK_INT >= 21) {
//				Log.i("AAAA","App Main 21");
//				Window window = getWindow();
//				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//				window.setStatusBarColor(getResources().getColor(R.color.white));
//			}

			AppReference.mainContext = this;
			SipQueue sipQueue = new SipQueue();

			sipCommunicator = new SipCommunicator(sipQueue);
			AppReference.sip_communicator = sipCommunicator;
			AppReference.sipQueue = sipQueue;
			sipCommunicator.setRnning(true);
			sipCommunicator.start();

			CommunicationBean bean = new CommunicationBean();
			bean.setOperationType(sip_operation_types.LOAD_LIBS);
			sipQueue.addMsg(bean);

//			mainLayout = (MainLayout) this.getLayoutInflater().inflate(
//					R.layout.activity_main, null);
//			setContentView(mainLayout);
            setContentView(R.layout.activity_main);

            btMenu = (ImageView) findViewById(R.id.side_menu);
            final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            final LinearLayout menu_side = (LinearLayout) findViewById(R.id.menu_side);
			ctimer=(Chronometer)findViewById(R.id.audio_timer);
			cvtimer=(Chronometer)findViewById(R.id.video_timer);
			ctimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
				@Override
				public void onChronometerTick(Chronometer arg0) {

					CharSequence text = ctimer.getText();
						ctimer.setText(text);

				}
			});
			cvtimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
				@Override
				public void onChronometerTick(Chronometer arg0) {

					CharSequence text = cvtimer.getText();
					cvtimer.setText(text);

				}
			});
            btMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(menu_side);
                }
            });
			Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(
					this, AppMainActivity.class));
			SingleInstance.mainContext = this;
			context = this;
			if(SingleInstance.mainContext.getResources()
							.getString(R.string.screenshot).equalsIgnoreCase(SingleInstance.mainContext.getResources()
									.getString(R.string.yes))){
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
			}
			Locale locale = null;
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(SingleInstance.mainContext);
			String locale_string = sharedPreferences.getString("locale",
					SingleInstance.mainContext.getResources()
							.getString(R.string.english_langugage));
			if(SingleInstance.Localefromdevice)
			{
				SingleInstance.Localefromdevice=false;
			}
			else{
				locale_string=Locale.getDefault().getLanguage();
			}
			Log.i("localeservice", "inside app main oncreate : "+locale_string);
			if (locale_string.equalsIgnoreCase("english")||locale_string.contains("en")) {
				locale = new Locale("en");
			} else {
				locale = new Locale("zh");
			}


			Locale.setDefault(locale);
			Configuration config = SingleInstance.mainContext
					.getResources().getConfiguration();
			config.locale = locale;
			SingleInstance.mainContext.getResources()
					.updateConfiguration(
							config,
							SingleInstance.mainContext
									.getResources()
									.getDisplayMetrics());

			onConfigurationChanged(config);
			
			Log.i("logservice", "inside app main oncreate");
			try {
				String dir_path = Environment.getExternalStorageDirectory()
						+ "/COMMedia";
				File directory = new File(dir_path);
				if (!directory.exists())
					directory.mkdir();
				String callrecording_path = dir_path + "/CallRecording";
				File dir = new File(callrecording_path);
				if (!dir.exists()) {
					dir.mkdir();
				}
				if (AppReference.isLogEnabled && AppReference.isWriteInFile) {
					String log_path = dir_path + "/log.txt";
					File logFile = new File(log_path);
					if (!logFile.exists())
						logFile.createNewFile();

					String logfilepath = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/log.txt";
					Logger logger = new Logger("File", logfilepath, true, true,
							true, true, true);
					AppReference.logger = logger;
					SingleInstance.printLog("App Main",
							"================================================",
							"DEBUG", null);
					SingleInstance.printLog("App Main", " App Version : "
							+ SingleInstance.mainContext.getResources()
									.getString(R.string.app_version), "DEBUG",
							null);
				}

				directory = null;
				dir_path = null;
			} catch (Exception e) {
				SingleInstance.printLog(null, e.getMessage(), null, e);
			}

			// Init menu
			progress = new ProgressDialog(context);
			lvMenuItems = getResources().getStringArray(R.array.menu_items);
			menuIcons = getResources().obtainTypedArray(R.array.icons);
            //Menu onClick process @author : Narayanan


			imView = (Button) findViewById(R.id.im_view);
			imageLoader = new ImageLoader(getApplicationContext());
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			if (timer == null)
				timer = new Timer();

			if (myTimerTask == null)
				myTimerTask = new MyTimerTask();

			timer.schedule(myTimerTask, 0, 1000 * 30);

			if (timerQA == null)
				timerQA = new Timer();

			if (myTimerTaskQA == null)
				myTimerTaskQA = new MyTimerTaskQA();

			timerQA.schedule(myTimerTaskQA, 0, 1000 * 60);

			SingleInstance.getChatProcesser();
			SingleInstance.getChatHistoryWriter();

//			for (int i = 0; i < lvMenuItems.length; i++) {
//				RowItem items = new RowItem(lvMenuItems[i],
//						menuIcons.getResourceId(i, -1));
//			}
//
//			menuIcons.recycle();
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(this);
			// Get menu button
//			btMenu = (Button) findViewById(R.id.side_menu);
//			btMenu.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//						return;
//					}
//					mLastClickTime = SystemClock.elapsedRealtime();
//					if (SingleInstance.fromGroupChat) {
//						GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
//								.get("groupchat");
//						if (gChat != null) {
//							GroupTempBean gTemp = gChat.setAndGetGroupDetails();
//							if (gTemp != null) {
//								gChat.finish();
//								Intent intent = new Intent(context,
//										GroupChatActivity.class);
//								if (gTemp.isGroup()) {
//									intent.putExtra("groupid",
//											gTemp.getGroupId());
//									intent.putExtra("isGroup", true);
//								} else {
//									intent.putExtra("groupid",
//											gTemp.getGroupId());
//									intent.putExtra("isGroup", false);
//									intent.putExtra("buddy", gTemp.getBuddy());
//								}
//								intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//								overridePendingTransition(0, 0);
//								startActivity(intent);
//							}
//						}
//						btMenu.setBackgroundResource(R.drawable.icon_sidemenu);
//						SingleInstance.fromGroupChat = false;
//					} else {
//						toggleMenu(v);
//					}
//				}
//			});

			imView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callDisp.openReceivedIm(v, context);
				}
			});

			// Get title textview
			tvTitle = (TextView) findViewById(R.id.activity_main_content_title);
			changeLoginButton();
			SingleInstance.contextTable.put("MAIN", this);

			// Add FragmentMain as the initial fragment
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			// SettingsFragment fragment = SettingsFragment.newInstance(this);
			LoginPageFragment fragment = LoginPageFragment.newInstance(this);
			boolean isSilent = getIntent()
					.getBooleanExtra("silentlogin", false);
			boolean autoLogin = preferences.getBoolean("autologin", false);
			boolean rememberPass = preferences.getBoolean("remember", false);
			if (isSilent) {
				if (autoLogin && rememberPass) {
					overridePendingTransition(0, 0);
					fragment.setSilentLogin(true);
					String username = preferences.getString("uname", null);
					String password = preferences.getString("pword", null);
					fragment.silentLogin(username, password);
					ContactsFragment contactsFragment = ContactsFragment
							.getInstance(context);
					ft.add(R.id.activity_main_content_fragment,
							contactsFragment, "loginfragment");
					ft.commit();
				} else {
					ft.add(R.id.activity_main_content_fragment, fragment,
							"loginfragment");
					ft.commit();
				}
				moveTaskToBack(true);
			} else {
				ft.add(R.id.activity_main_content_fragment, fragment,
						"loginfragment");
				ft.commit();
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			e.printStackTrace();
		}

	}

	public void setfooterVisiblity(boolean flag) {
//		if (!flag)
//			footer_lay.setVisibility(View.GONE);
//		else
//			footer_lay.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	public void toggleMenu(View v) {
		try {
//			mainLayout.toggleMenu();
			hideKeyboard();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ScreenSharingFragment screenSharingFragment = ScreenSharingFragment
				.newInstance(context);
		if (screenSharingFragment.background) {
			shareScreen = true;
			screenSharingFragment.background = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        inActivity = this;
		if (SingleInstance.buddyRequestMessageList.size() > 0) {
			for (String message : SingleInstance.buddyRequestMessageList) {
				showToast(message);
			}
			SingleInstance.buddyRequestMessageList.clear();
		}
		ScreenSharingFragment screenSharingFragment = ScreenSharingFragment
				.newInstance(context);
		if (shareScreen) {
			screenSharingFragment.background = true;
			shareScreen = false;
		}
		cancelDialogOnResume();
		if(isPinEnable) {
			if (openPinActivity) {
				openPinActivity=false;
				Intent i = new Intent(AppMainActivity.this, MainActivity.class);
				startActivity(i);
			} else {
				count=0;
				registerBroadcastReceiver();
			}
		}


	}

	public void hideKeyboard() {
		try {
//			InputMethodManager imm = (InputMethodManager) this
//					.getSystemService(Service.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

			// imm.showSoftInput(ed, 0);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	// @Override
	// public void onBackPressed() {
	// if (mainLayout.isMenuShown()) {
	// mainLayout.toggleMenu();
	// }
	// else {
	// super.onBackPressed();
	// }
	// }

	public void loginResponse(Object object) {

		try {
			initializeCommunicationEngine();
			profileDetails.clear();
			if (ftpNotifier == null)
				ftpNotifier = new FTPNotifier();
			if (object instanceof ArrayList) {
				ArrayList list = (ArrayList) object;
				for (int i = 0; i < list.size(); i++) {
					final Object obj = list.get(i);
					if (obj instanceof AppVersionUpdateBean) {
						AppVersionUpdateBean appBean = (AppVersionUpdateBean) list
								.get(i);
						if (appBean.getType() == 0) {
							Toast.makeText(getApplicationContext(),
									"New version is available for download", 1)
									.show();
							WebServiceReferences.type = appBean.getType();
						} else if (appBean.getType() == 1) {
							appLogout(appBean.getMessage(), appBean.getUrl(),
									appBean.getType(), getApplicationContext());
							WebServiceReferences.type = appBean.getType();
						} else if (appBean.getType() == 2) {
							appLogout(appBean.getMessage(), appBean.getUrl(),
									appBean.getType(), getApplicationContext());
							WebServiceReferences.type = appBean.getType();

						}
					} else if (obj instanceof ConnectionBrokerBean) {
						ConnectionBrokerBean cbb = (ConnectionBrokerBean) list
								.get(i);

						SingleInstance
								.printLog(
										"Signin",
										"Login OK Comes to Array List and ConnectionBrokerBean",
										"INFO", null);

						cBean = new ConnectionBrokerServerBean();
						cBean.setConnectionBrokerServer1(cbb.getCbserver1());
						cBean.setConnectionBrokerServer2(cbb.getCbserver2());

						cBean.setRouter(cbb.getRouter());
						cBean.setRelay(cbb.getRelayServer());
						cBean.setCbPort1(cbb.getaa1Port());
						cBean.setCbPort1(cbb.getaa2Port());
						cBean.setFreeSwitch(cbb.getFS());

						if (commEngine != null) {

							if (cbb.getaa1Port() != 0 && cbb.getaa2Port() != 0)
								// commEngine.setConnectionBrokerDetails(getCbserver1(),
								// getCbserver2(), getcbPort1(), getcbPort2());
								commEngine.setConnectionBrokerDetails(
										cbb.getCbserver1(), cbb.getCbserver2(),
										cbb.getaa1Port(), cbb.getaa2Port());

							String routerDetails[] = cbb.getRouter().split(":");
							String relayDetails[] = cbb.getRelayServer().split(
									":");
							commEngine.startSignalingAgent(
									CallDispatcher.LoginUser, routerDetails[0],
									Integer.parseInt(routerDetails[1]),
									relayDetails[0],
									Integer.parseInt(relayDetails[1]));

						}

					} else if (obj instanceof String[]) {
						String[] buddy_profile = (String[]) obj;
						profileDetails.add(buddy_profile);
					} else if (obj instanceof ShareReminder) {
						ShareReminder sr = (ShareReminder) obj;
						if (sr.getStatus().equalsIgnoreCase("new")) {

							String message = "getReminderdatetime : "
									+ sr.getReminderdatetime() + ", id : "
									+ sr.getId() + ", getRemindertz : "
									+ sr.getRemindertz();
							SingleInstance.printLog("Signin", message, "INFO",
									null);
							if (sr.getStatus().equals("new")) {
								WebServiceReferences.shareRemainderArray
										.add(sr);
							}

						}

					}
				}

			}
			Log.i("share123", "shared file size : "
					+ WebServiceReferences.shareRemainderArray.size());
			if (WebServiceReferences.shareRemainderArray.size() > 0) {
				FilesFragment filesFragment = FilesFragment
						.newInstance(getApplicationContext());
				filesFragment.showShareRemainderRequest();

			}
            if(WebServiceReferences.webServiceClient!=null){
				Log.i("chattemplate", "Appmain loginrespponse WebServiceReferences.webServiceClient!=null");
				String modifieddatetime=null;
                if(DBAccess.getdbHeler(SingleInstance.mainContext).getChatTemplateModifieddatetime()!=null){
					modifieddatetime=DBAccess.getdbHeler(SingleInstance.mainContext).getChatTemplateModifieddatetime();
				}else{
					modifieddatetime="\"\"";
				}
				WebServiceReferences.webServiceClient.getChatTemplate(modifieddatetime);
			}
			checkChatHistory();
			setUnReadNotesSize();
			setProfilePic();
			changeLoginButton();
			chageMyStatus();
			callingHeartBeart();
            tcpEngine = new TCPEngine(CallDispatcher.LoginUser, ip, port, this);
            tcpEngine.start();
//			BGProcessor.getInstance().getProfileTemplate();
//			BGProcessor.getInstance().getSyncUtility();
			BGProcessor.getInstance().getMyConfigurationDetails();
//			BGProcessor.getInstance().getMyGroupChatSettings(
//					CallDispatcher.LoginUser);
			// missedDownloads(CallDispatcher.LoginUser);
			triggerMissedDownloads();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void checkChatHistory() {
		String oldUsername = preferences.getString("uname", null);
		if (oldUsername != null
				&& oldUsername.equalsIgnoreCase(CallDispatcher.LoginUser)) {
			if (SingleInstance.groupChatHistory != null
					&& SingleInstance.groupChatHistory.size() > 0) {
				SingleInstance.groupChatHistory.clear();
			}
		}
	}

	private void setUnReadNotesSize() {
		// TODO Auto-generated method stub

		DBAccess.getdbHeler().getUnreadnotesSize(CallDispatcher.LoginUser);

	}

	public FTPNotifier getFtpNotifier() {
		return ftpNotifier;
	}

	private void initializeCommunicationEngine() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (commEngine != null)
						commEngine.initCodec();
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}
		}).start();
	}

	private void appLogout(String message, String url, int type, Context context) {
		showAppLogoutAlert("Response", message, url, type, context);
	}

	public void showAppLogoutAlert(final String title, final String message,
			final String url, final int type, final Context context) {

		String msg = "Title : " + title + ", Message : " + message + ", Type :"
				+ type;
		SingleInstance.printLog("Dialog", msg, "INFO", null);

		handler.post(new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                try {

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
                                    if (type == 2) {
                                        try {
                                            Intent i = new Intent(context,
                                                    TestHTML5WebView.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("url", url);
                                            i.putExtras(bundle);
                                            context.startActivity(i);
                                            // p = PreferenceManager
                                            // .getDefaultSharedPreferences(context);
                                            // boolean autologinstate =
                                            // p.getBoolean(
                                            // "autologin", false);
                                            // logout(autologinstate);

                                        } catch (Exception e) {
                                            SingleInstance.printLog(null,
                                                    e.getMessage(), null, e);
                                        }
                                    }
                                    myAlertDialog.dismiss();
                                }
                            });
                    myAlertDialog.show();
                } catch (Exception e) {
                    SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }
        });

	}

	/**
	 * Notify to UI
	 */

	public void notifyUploadStatus(String filename, Object req_object,
			boolean uploadStatus) {

		try {
			if (req_object != null && uploadStatus) {
				String[] params = new String[2];
				if (CallDispatcher.LoginUser != null)
					params[0] = CallDispatcher.LoginUser;
				else {
					SharedPreferences preferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					params[0] = preferences.getString("uname", null);
				}
				if (req_object instanceof FieldTemplateBean) {
					FieldTemplateBean fieldTemplate = (FieldTemplateBean) req_object;
					DBAccess.getdbHeler(getApplicationContext())
							.saveOrUpdateProfileField(fieldTemplate);

					params[1] = "5";
					fieldTemplate.setFiledvalue(filename);
					WebServiceReferences.webServiceClient
							.updateProfileFieldValueForMultimedia(params,
									fieldTemplate);
				} else if (req_object instanceof OfflineRequestConfigBean) {
					OfflineRequestConfigBean offlineRequestConfigBean = (OfflineRequestConfigBean) req_object;
					offlineRequestConfigBean.setMode("edit");
					offlineRequestConfigBean.setMessage(filename);
					// AvatarFragment avatarFragment = AvatarFragment
					// .newInstance(context);

					AvatarActivity avatarActivity = AvatarActivity
							.newInstance(context);
					WebServiceReferences.webServiceClient.offlineconfiguration(
							CallDispatcher.LoginUser,
							offlineRequestConfigBean.getDefalut_list(),
							offlineRequestConfigBean.getBuddy_list(),
							avatarActivity);

				}
			} else {
				if (req_object instanceof FieldTemplateBean) {
					FieldTemplateBean fieldTemplate = (FieldTemplateBean) req_object;
					if (fieldTemplate != null)
						showToast("Profile field upload failed for "
								+ fieldTemplate.getFieldName());
				} else if (req_object instanceof OfflineRequestConfigBean) {
					OfflineRequestConfigBean offlineRequestConfigBean = (OfflineRequestConfigBean) req_object;
					if (offlineRequestConfigBean != null)
						showToast("Avatar upload failed for "
								+ offlineRequestConfigBean.getMessageTitle());
				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void showToast(final String message) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public void notifyUDPupdates(Object responseObject) {

		try {

			final GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");

			SingleInstance.printLog("UDP_NOTIFICATION",
					"Received UDP Notification", "INFO", null);

			if (responseObject instanceof GroupBean) {
				processGroupChanges((GroupBean) responseObject);
			}else if(responseObject instanceof String[]){
				String[] result=(String[])responseObject;
					WebServiceReferences.webServiceClient.GetGroupDetails(CallDispatcher.LoginUser,
							result[0],SingleInstance.mainContext);
			} else if (responseObject instanceof GroupChatBean) {
				final GroupChatBean groupChatBean = (GroupChatBean) responseObject;

				if (groupChatBean.getType().equals("100")) {
					if (groupChatBean.getSenttime() == null
							|| groupChatBean.getSenttime().length() == 0) {
						groupChatBean.setSenttime(getCurrentDateandTime());
					}
					if (groupChatBean.getMimetype().equals("text")
							|| groupChatBean.getMimetype().equals("location")) {
						processGroupChatChanges(groupChatBean);

					} else {
						groupChatBean.setStatus(0);
						processGroupChatChanges(groupChatBean.clone());
						SingleInstance.printLog("UDP_NOTIFICATION",
								"Received Group Chat FTP Notification : "
										+ groupChatBean.getMediaName(), "INFO",
								null);

						String fileName = groupChatBean.getMediaName();
						groupChatBean
								.setMediaName(Utils
										.getFilePathString(groupChatBean
												.getMediaName()));
						downloadGroupChatFile(groupChatBean, fileName);

						/*
						 * FTPBean bean = new FTPBean();
						 * bean.setFtp_username(groupChatBean.getFtpUsername());
						 * bean.setFtp_password(groupChatBean.getFtpPassword());
						 * bean.setServer_port(40400);
						 * bean.setOperation_type(2);
						 * bean.setComment(groupChatBean.getSessionid());
						 * bean.setServer_ip
						 * (callDisp.getRouter().split(":")[0]);
						 * bean.setFile_path(groupChatBean.getMediaName());
						 * bean.setReq_object(groupChatBean);
						 * bean.setRequest_from("100");
						 * callDisp.getFtpNotifier().addTasktoExecutor(bean);
						 */

					}
				} else {
					processGroupChatChanges(groupChatBean);
				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void downloadGroupChatFile(GroupChatBean groupChatBean,
			String fileName) {

		ChatFTPBean chatFTPBean = new ChatFTPBean();
		if (cBean != null) {
			chatFTPBean.setServerIp(cBean.getRouter().split(":")[0]);
			chatFTPBean.setServerPort(40400);
			chatFTPBean.setUsername(groupChatBean.getFtpUsername());
			chatFTPBean.setPassword(groupChatBean.getFtpPassword());
			chatFTPBean.setInputFile(fileName);
			chatFTPBean.setOutputFile(groupChatBean.getMediaName());
			chatFTPBean.setOperation("DOWNLOAD");
			chatFTPBean.setSourceObject(groupChatBean);
			chatFTPBean.setCallback(AppMainActivity.this);
			insertOrUpdateUploadOrDownload(chatFTPBean, "0", "groupchat");
			FTPPoolManager.processRequest(chatFTPBean);

		}
	}

	public void processGroupChatChanges(GroupChatBean gcBean) {

		try {

			SingleInstance.printLog("UDP_NOTIFICATION",
                    "Received UDP Notification - 1" + gcBean.getMediaName(),
                    "INFO", null);

			if (gcBean.getGroupId() == null) {
				gcBean.setGroupId(gcBean.getTo());
			}
			if (gcBean.getSubCategory() != null
					&& (gcBean.getSubCategory().equalsIgnoreCase("gs") || gcBean
							.getSubCategory().equalsIgnoreCase("gd"))) {
				Log.i("gchat123", "parentid inside processGroupChatChanges : "
						+ gcBean.getParentId());
				saveAndSettingScheduler(gcBean);
			} else {
				saveAndNotifyGroupChatUI(gcBean);
			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	private void processGroupChanges(final GroupBean object) {
		try {
			GroupActivity.getAllGroups();
			String method = object.getMethod();
			String groupNewMember = object.getNonGroupMembers();
			Log.d("Test", "Appmain$$$$ getNonGroupMembers  @@@ "
					+ groupNewMember);

			object.setUserName(CallDispatcher.LoginUser);

			Log.i("AAAA", "processGroupChanges EID = " + EidforGroup);
			if(EidforGroup!=null && EidforGroup.equalsIgnoreCase("30"))
				object.setGrouptype("Rounding");
			else
				object.setGrouptype("group");

			if (method.equalsIgnoreCase("ModifyGroup")
					|| method.equalsIgnoreCase("LeaveGroup")) {

				if (object.getDeleteGroupMembers() != null
						&& object.getDeleteGroupMembers().contains(
								CallDispatcher.LoginUser)) {
					deleteGroupAndNotify(object);
					showToast("You have been removed from "
							+ object.getGroupName());
				} else {
					if (object.getCreatedDate() == null) {
						object.setCreatedDate(getCurrentDateTime());
					}
					if (object.getModifiedDate() == null) {
						object.setModifiedDate(getCurrentDateTime());
					}
					DBAccess.getdbHeler(getApplicationContext())
							.saveOrUpdateGroup(object);
					updateGroupAndNotify(object);
					if (object.getGroupMembers() != null
							&& object.getGroupMembers().length() > 0) {
						object.setActiveGroupMembers(object.getGroupMembers());
						DBAccess.getdbHeler(getApplicationContext())
								.insertorUpdateGroupMembers(object);
					}
					GroupBean gBean = DBAccess.getdbHeler(
							getApplicationContext()).getGroupAndMembers(
							"select * from groupdetails where groupid="
									+ object.getGroupId());

					String deleteMembers = object.getDeleteGroupMembers();
					String activeMembers = "";
					if (deleteMembers != null && deleteMembers.length() > 0
							&& gBean != null) {
						String[] tmp = gBean.getActiveGroupMembers().split(",");
						for (String member : tmp) {
							if (!deleteMembers.contains(member))
								activeMembers += member + ",";
							else {
								DBAccess.getdbHeler(getApplicationContext())
										.deleteGroupChatEntryLeaveGroup(
												object.getGroupId(),
												CallDispatcher.LoginUser,
												member);

							}
						}
						if (activeMembers != null && activeMembers.length() > 0)
							activeMembers = activeMembers.substring(0,
									activeMembers.length() - 1);

						ContentValues cv = new ContentValues();
						cv.put("active_members", activeMembers);
						DBAccess.getdbHeler(getApplicationContext())
								.updateGroupMembers(cv,
										"groupid=" + gBean.getGroupId());
					}
					if (method.equalsIgnoreCase("ModifyGroup")) {
						WebServiceReferences.webServiceClient.GetGroupDetails(CallDispatcher.LoginUser,
								object.getGroupId(),SingleInstance.mainContext);
						GroupBean oldBean = null;
						if(EidforGroup!=null && EidforGroup.equalsIgnoreCase("30")){
							for (GroupBean gBean1 : RoundingGroupActivity.RoundingList) {
								if (gBean1.getGroupId().equals(object.getGroupId())) {
									gBean1.setActiveGroupMembers(object
											.getGroupMembers());
									Log.d("Test",
											"Appmain$$$$ RoundingGroupMembers 1453 @@@ "
													+ object.getGroupMembers());

									gBean1.setGroupName(object.getGroupName());
									oldBean = gBean;
									break;
								}
							}
							if (oldBean != null) {
								for (GroupBean gBean1 : RoundingGroupActivity.RoundingList) {
									if (gBean1.getGroupId().equals(
											object.getGroupId())) {
										gBean1.setActiveGroupMembers(object
												.getGroupMembers());
										Log.d("Test",
												"Appmain$$$$ GroupMembers 1463 @@@ "
														+ object.getGroupMembers());
										gBean1.setGroupName(object.getGroupName());
										break;
									}
								}
							} else {
								RoundingGroupActivity.RoundingList.add(object);
							}
						}else {
							for (GroupBean gBean1 : GroupActivity.groupList) {
								if (gBean1.getGroupId().equals(object.getGroupId())) {
									gBean1.setActiveGroupMembers(object
											.getGroupMembers());
									Log.d("Test",
											"Appmain$$$$ GroupMembers 1453 @@@ "
													+ object.getGroupMembers());

									gBean1.setGroupName(object.getGroupName());
									oldBean = gBean;
									break;
								}
							}
							if (oldBean != null) {
								for (GroupBean gBean1 : GroupActivity.groupList) {
									if (gBean1.getGroupId().equals(
											object.getGroupId())) {
										gBean1.setActiveGroupMembers(object
												.getGroupMembers());
										Log.d("Test",
												"Appmain$$$$ GroupMembers 1463 @@@ "
														+ object.getGroupMembers());
										gBean1.setGroupName(object.getGroupName());
										break;
									}
								}
							} else {
								GroupActivity.groupList.add(object);
							}
						}
						if (object.getDeleteGroupMembers() != null
								&& object.getDeleteGroupMembers().length() > 0) {
							showToast(object.getDeleteGroupMembers()
									+ " has removed you from the  "
									+ object.getGroupName() + " Group");
						} else {
							Log.d("Test",
									"Appmain$$$$ getActiveGroupMembers 1484 @@@ "
											+ object.getActiveGroupMembers()
													.length());
							showToast(object.getOwnerName()
									+ " have added you in the "
									+ object.getGroupName() + " Group");
						}
					} else {
						showToast(object.getDeleteGroupMembers()
								+ " left from the " + object.getGroupName()
								+ " Group");
					}

					refreshGroupList();

					ExchangesFragment exchanges = ExchangesFragment
							.newInstance(getApplicationContext());
					if (exchanges != null) {
						exchanges.notifyGroupList();
					}
				}

			} else if (method.equalsIgnoreCase("GroupDelete")) {

				deleteGroupAndNotify(object);

				showToast(object.getOwnerName() + " has deleted the "
						+ object.getGroupName());
				ExchangesFragment exchanges = ExchangesFragment
						.newInstance(getApplicationContext());
				if (exchanges != null) {
					exchanges.notifyGroupList();
				}

			}
			// else if (method.equalsIgnoreCase("LeaveGroup")) {
			// if (object.getDeleteGroupMembers() != null
			// && object.getDeleteGroupMembers().contains(
			// CallDispatcher.LoginUser)) {
			// deleteGroupAndNotify(object);
			// }
			// }
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void updateGroupAndNotify(GroupBean groupBean) {
		try {Log.i("AAAA", "updateGroupAndNotify  " );
			for (GroupBean gBean : GroupActivity.groupList) {
				if (gBean.getGroupId().equals(groupBean.getGroupId())) {
					Log.i("AAAA", "updateGroupAndNotify EID = " + EidforGroup);
					gBean.setGroupName(groupBean.getGroupName());
					break;
				}
			}
			Log.i("AAAA", "updateGroupAndNotify  groupList " +GroupActivity.groupList);
			Log.i("AAAA", "updateGroupAndNotify  RoundingList " +RoundingGroupActivity.RoundingList);
			handler.post(new Runnable() {
				@Override
				public void run() {

					refreshGroupList();
				}
			});
			ExchangesFragment exchanges = ExchangesFragment
					.newInstance(getApplicationContext());
			if (exchanges != null) {
				exchanges.notifyGroupList();
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void deleteGroupAndNotify(GroupBean groupBean) {
		try {
			DBAccess.getdbHeler().deleteGroupAndMembers(groupBean.getGroupId());
			GroupBean deleteGroup = null;
			for (GroupBean gBean : GroupActivity.groupList) {
				if (gBean.getGroupId().equals(groupBean.getGroupId())) {
					deleteGroup = gBean;
					break;
				}
			}
			if(EidforGroup!=null && EidforGroup.equalsIgnoreCase("30")){
				GroupBean deleteGroupBean = null;
				for (GroupBean gBean : RoundingGroupActivity.RoundingList) {
					if (gBean.getGroupId().equals(groupBean.getGroupId())) {
						deleteGroupBean = gBean;
						break;
					}
				}
				if (deleteGroupBean != null) {
					RoundingGroupActivity.RoundingList.remove(deleteGroupBean);
				}
			}

			if (groupBean.getMethod().equalsIgnoreCase("GroupDelete")
					|| groupBean.getMethod().equalsIgnoreCase("ModifyGroup")) {
				DBAccess.getdbHeler(getApplicationContext())
						.deleteGroupChatEntryLocally(groupBean.getGroupId(),
								CallDispatcher.LoginUser);
			}

			if (deleteGroup != null) {
				GroupActivity.groupList.remove(deleteGroup);
			}

			refreshGroupList();

			ExchangesFragment exchanges = ExchangesFragment
					.newInstance(getApplicationContext());
			if (exchanges != null) {
				exchanges.notifyGroupList();
			}
			GroupChatActivity gChat = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			if (gChat != null) {
				if (gChat.groupId.equalsIgnoreCase(deleteGroup.getGroupId())) {
					ShowGroupDeleteAlert("Alert",
                            "You have been removed from the group", gChat);
				}
			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void notifyChatFTPStatus(ChatFTPBean ftpBean, boolean status) {
		try {
			GroupChatBean gcBean = (GroupChatBean) ftpBean.getSourceObject();
			if (gcBean != null) {
				if (ftpBean.getOperation().equalsIgnoreCase("UPLOAD")) {
					if (status) {
						gcBean.setStatus(2);
						SingleInstance.getGroupChatProcesser().getQueue()
								.addObject(gcBean);
						DBAccess.getdbHeler().deleteUploadDownloadStatus(
								ftpBean.getInputFile());

						updateUploadDownloadUI(gcBean, 2);
					} else {
						gcBean.setStatus(1);
						updateUploadDownloadUI(gcBean, 1);
					}
					DBAccess.getdbHeler().updateGroupChat(gcBean.getStatus(),
                            gcBean.getSignalid());
				} else if (ftpBean.getOperation().equalsIgnoreCase("DOWNLOAD")) {
					if (status) {
						DBAccess.getdbHeler().deleteUploadDownloadStatus(
								ftpBean.getInputFile());
						gcBean.setStatus(2);
						updateUploadDownloadUI(gcBean, 2);
						DBAccess.getdbHeler().updateGroupChat(2,
								gcBean.getSignalid());
					} else {
						gcBean.setStatus(1);
						updateUploadDownloadUI(gcBean, 1);
						DBAccess.getdbHeler().updateGroupChat(1,
								gcBean.getSignalid());
					}

				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void notifyScheduleAndDeadLineMsg(GroupChatBean gcBean,
			String parentId) {
		if (gcBean.getSubCategory().equalsIgnoreCase("gs")) {
			gcBean.setSenttime(getCurrentDateandTime());
			saveAndNotifyGroupChatUI(gcBean);
		} else if (gcBean.getSubCategory().equalsIgnoreCase("gd")) {
			GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			GroupBean gBean = DBAccess.getdbHeler(getApplicationContext())
					.getGroup(
							"select * from grouplist where groupid='"
									+ gcBean.getGroupId() + "'");

			if (gcActivity == null) {
				alertDeadLineMessage(gcBean, gBean);
			} else {
				boolean isOpen = false;
				if (gcActivity.isGroup|| gcActivity.isRounding
						&& gcActivity.groupId.equalsIgnoreCase(gcBean
								.getGroupId())) {
					isOpen = true;
				}
				if (!gcActivity.isGroup&& !gcActivity.isRounding
						&& gcActivity.buddy.equalsIgnoreCase(gcBean
								.getGroupId().replace(CallDispatcher.LoginUser,
										""))) {
					isOpen = true;
				}
				if (isOpen) {
					String[] privateMembers = gcBean.getPrivateMembers().split(
							",");
					for (String tmp : privateMembers) {
						if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							gcBean.setParentId(parentId);
							gcActivity.deadLineReplyDialog(gcBean);
							break;
						}
					}
				} else {
					gcBean.setParentId(parentId);
					alertDeadLineMessage(gcBean, gBean);
				}
			}
		}
		ExchangesFragment exchanges = ExchangesFragment
				.newInstance(getApplicationContext());
		if (exchanges != null) {
			exchanges.notifyUI();

		}
	}

	private void alertDeadLineMessage(GroupChatBean gcBean, GroupBean gBean) {
		if (gcBean.getPrivateMembers() != null
				&& gcBean.getPrivateMembers().length() > 0) {
			String[] privateMembers = gcBean.getPrivateMembers().split(",");
			for (String tmp : privateMembers) {
				if (CallDispatcher.LoginUser != null) {
					if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
						Intent intent = new Intent(getApplicationContext(),
								GroupChatActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("deadLine", true);
						intent.putExtra("gcBean", gcBean);
						intent.putExtra("groupBean", gBean);
						if (gcBean.getCategory().equalsIgnoreCase("G")) {
							intent.putExtra("isGroup", true);
							intent.putExtra("groupid", gBean.getGroupId());
						} else {
							intent.putExtra("isGroup", false);
							intent.putExtra("buddy", gcBean.getFrom());
						}
						getApplication().startActivity(intent);
						// deadLineReplyDialog(gcBean, gBean);
						break;
					}
				}
			}
		}
	}

	private void saveAndSettingScheduler(GroupChatBean gcBean) {
		try {
			if (!gcBean.getFrom().equalsIgnoreCase(CallDispatcher.LoginUser)
					&& gcBean.getReminderTime() != null) {
				int row = 0;
				if (gcBean.getSubCategory().equalsIgnoreCase("gs"))
					row = DBAccess.getdbHeler(getApplicationContext())
							.insertorUpdateScheduleMsg(gcBean);
				else if (gcBean.getSubCategory().equalsIgnoreCase("gd"))
					saveAndNotifyGroupChatUI(gcBean);
				if (gcBean.getPrivateMembers() != null
						&& gcBean.getPrivateMembers().length() > 0) {
					String[] privateMembers = gcBean.getPrivateMembers().split(
							",");
					for (String tmp : privateMembers) {
						if (tmp.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							AlarmManager alarmMgr = (AlarmManager) getApplicationContext()
									.getSystemService(Context.ALARM_SERVICE);
							Intent intent = new Intent(getApplicationContext(),
									GroupChatBroadCastReceiver.class);
							Log.i("gchat123",
									"parentid inside save and setting scheduler : "
											+ gcBean.getParentId());
							intent.putExtra("groupchat", gcBean);
							intent.putExtra("parent_id", gcBean.getParentId());
							PendingIntent alarmIntent = PendingIntent
									.getBroadcast(getApplicationContext(),gcBean.getParentId().hashCode(),
											intent, 0);
							SingleInstance.scheduledMsg.put(
									gcBean.getSignalid(), alarmIntent);
							SimpleDateFormat outPutFormatter = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm");
							Date date = null;
							try {
								date = outPutFormatter.parse(gcBean
										.getReminderTime());
								alarmMgr.set(AlarmManager.RTC_WAKEUP,
										date.getTime(), alarmIntent);
							} catch (Exception e) {
								e.printStackTrace();
								if (AppReference.isWriteInFile)
									AppReference.logger
											.error(e.getMessage(), e);
								else
									e.printStackTrace();

							}
							break;

						} else {
							if (gcBean.getSubCategory().equalsIgnoreCase("GD")) {
								notifyScheduleAndDeadLineMsg(gcBean,
										gcBean.getParentId());
							}
						}
					}
				}

				/**
				 * Due to IOS compatibility commenting this line and replacing
				 * the below date format
				 */
				// SimpleDateFormat outPutFormatter = new SimpleDateFormat(
				// "dd/MM/yyyy hh:mm");
				// DateFormatSymbols dfSym = new DateFormatSymbols();
				// dfSym.setAmPmStrings(new String[] { "am", "pm" });
				// outPutFormatter.setDateFormatSymbols(dfSym);
				/**
				 * Ends here
				 */

			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void saveAndNotifyGroupChatUI(GroupChatBean gcBean) {

		try {

			GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			/*
			 * Due to IOS client
			 */
			if (gcBean.getSenttime() == null
					|| gcBean.getSenttime().equalsIgnoreCase("(null)")) {
				gcBean.setSenttime(getCurrentDateandTime());
			}
			if (gcBean.getSenttimez() == null
					|| gcBean.getSenttimez().equalsIgnoreCase("(null)")) {
				gcBean.setSenttimez(getCurrentDateandTime());
			}
			/**
			 * Ends here
			 */
			if (gcBean.getCategory().equalsIgnoreCase("I")) {
				gcBean.setSessionid(gcBean.getFrom());
				gcBean.setGroupId(gcBean.getFrom());
			} else {
				gcBean.setGroupId(gcBean.getTo());
			}
			SingleInstance.printLog("GROUP123",
					"history writer :  " + gcBean.getGroupId(), "INFO", null);
			boolean isOpen = false;
			if (groupChatActivity != null) {
				if (groupChatActivity.isGroup || groupChatActivity.isRounding
						&& groupChatActivity.groupId
								.equals(gcBean.getGroupId())) {
					isOpen = true;
				}
				if (!groupChatActivity.isGroup&& !groupChatActivity.isRounding
						&& groupChatActivity.buddy.equals(gcBean.getGroupId()
								.replace(CallDispatcher.LoginUser, ""))) {
					isOpen = true;
				}
			}
			if (isOpen) {
				Log.i("groupchat123",
						"Received buddy and open buddy page are same");
			} else {
				if (gcBean.getSubCategory() != null
						&& (gcBean.getSubCategory().equalsIgnoreCase("GD") || gcBean
								.getSubCategory().equalsIgnoreCase("GDI"))) {
					int count = 1;
					if (SingleInstance.deadLineMsgCount.containsKey(gcBean
							.getGroupId())) {
						count = SingleInstance.deadLineMsgCount.get(gcBean
								.getGroupId());
						count = count + 1;
						SingleInstance.deadLineMsgCount.remove(gcBean
								.getGroupId());
						SingleInstance.deadLineMsgCount.put(
								gcBean.getGroupId(), count);
					} else {
						count = 1;
						SingleInstance.deadLineMsgCount.put(
								gcBean.getGroupId(), count);
					}
				}
				if (gcBean.getCategory().equalsIgnoreCase("G")) {

					if (gcBean.getType().equalsIgnoreCase("100")) {
						int count = 1;
						if (SingleInstance.unreadCount.containsKey(gcBean
								.getGroupId())) {
							count = SingleInstance.unreadCount.get(gcBean
									.getGroupId());
							count = count + 1;
							SingleInstance.unreadCount.remove(gcBean
									.getGroupId());
							SingleInstance.unreadCount.put(gcBean.getGroupId(),
									count);
							gcBean.setUnreadStatus(0);
						} else {
							count = 1;
							SingleInstance.unreadCount.put(gcBean.getGroupId(),
									count);
							gcBean.setUnreadStatus(0);
						}
						for (GroupBean bBean : ContactsFragment.getGroupList()) {
								if (bBean.getGroupId().equalsIgnoreCase(
										gcBean.getGroupId())) {
									int unreadCount = DBAccess
											.getdbHeler()
											.getUnreadMsgCountById(
													gcBean.getGroupId(),
													CallDispatcher.LoginUser);
									int totalUnreadCount = unreadCount + 1;
									bBean.setMessageCount(totalUnreadCount);
									Log.i("AAAA", "APP MAIN INSIDE GROUP:"+bBean.getMessageCount());
									break;
								}

						}
						for (GroupBean bBean : ContactsFragment.getBuddyGroupList()) {
							if (bBean.getGroupId().equalsIgnoreCase(
									gcBean.getGroupId())) {
								int unreadCount = DBAccess
										.getdbHeler()
										.getUnreadMsgCountById(
												gcBean.getGroupId(),
												CallDispatcher.LoginUser);
								int totalUnreadCount = unreadCount + 1;
								bBean.setMessageCount(totalUnreadCount);
								Log.i("AAAA", "APP MAIN INSIDE GROUP:"+bBean.getMessageCount());
								break;
							}

						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ContactsFragment.getGroupAdapter()
										.notifyDataSetChanged();
							}
						});
					}
				} else {
					int count = 0;
					String buddyName = gcBean.getGroupId().replace(
							CallDispatcher.LoginUser, "");
					if (groupChatActivity != null
							&& groupChatActivity.buddy != null
							&& groupChatActivity.buddy
									.equalsIgnoreCase(buddyName)) {
					} else {
						if (gcBean.getType().equalsIgnoreCase("100")) {
							if (SingleInstance.individualMsgUnreadCount
									.containsKey(buddyName)) {
								count = SingleInstance.individualMsgUnreadCount
										.get(buddyName);
								count = count + 1;
								SingleInstance.individualMsgUnreadCount
										.remove(buddyName);
								SingleInstance.individualMsgUnreadCount.put(
										buddyName, count);
								gcBean.setUnreadStatus(0);
							} else {
								count = 1;
								SingleInstance.individualMsgUnreadCount.put(
										buddyName, count);
								gcBean.setUnreadStatus(0);
							}
							String buddynametonotifyUI = null;
							int MsgCount = 0;
							for (BuddyInformationBean bBean : ContactsFragment
									.getBuddyList()) {
								if (!bBean.isTitle()) {
									if (bBean.getName().equalsIgnoreCase(
											buddyName)) {
										buddynametonotifyUI=buddyName;
										int unreadCount = DBAccess
												.getdbHeler()
												.getUnreadMsgCountById(
														buddyName,
														CallDispatcher.LoginUser);
										int totalUnreadCount = unreadCount + 1;
										MsgCount=totalUnreadCount;
										bBean.setMessageCount(totalUnreadCount);
										break;
									}
								}
							}
//							showPopup();
							Intent intent = new Intent(this, NotificationReceiver.class);
							PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
							Notification n  = new Notification.Builder(this)
									.setContentTitle("New Message from " + buddynametonotifyUI)
									.setContentText("Unread Count "+MsgCount)
									.setSmallIcon(drawable.logo_snazmed)
									.setContentIntent(pIntent)
									.setAutoCancel(true).build();


							NotificationManager notificationManager =
									(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

							notificationManager.notify(0, n);


							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									ContactsFragment.getContactAdapter()
											.notifyDataSetChanged();
								}
							});

						}
					}
				}
			}
			SingleInstance.getGroupChatHistoryWriter().getQueue()
					.addObject(gcBean);

			boolean isExists = false;
			if (SingleInstance.contextTable.get("groupchat") != null) {
				if (groupChatActivity.isGroup|| groupChatActivity.isRounding) {
					if (groupChatActivity.groupId.equalsIgnoreCase(gcBean
							.getGroupId())) {
						Vector<GroupChatBean> chatList = groupChatActivity.chatList;
						if (chatList != null) {
							if (chatList.size() > 0) {
								for (GroupChatBean groupChatBean : chatList) {
									if (groupChatBean.getSignalid()
											.equalsIgnoreCase(
													gcBean.getSignalid())) {
										isExists = true;
									}
								}

							}
						}
						if (!isExists) {
							if (groupChatActivity.isGroup|| groupChatActivity.isRounding) {
								if (SingleInstance.unreadCount
										.containsKey(gcBean.getGroupId())) {
									SingleInstance.unreadCount.remove(gcBean
											.getGroupId());
								}
							} else {
								String buddyName = gcBean.getGroupId().replace(
										CallDispatcher.LoginUser, "");
								if (SingleInstance.unreadCount
										.containsKey(buddyName)) {
									SingleInstance.unreadCount
											.remove(buddyName);
								}
							}
							groupChatActivity.notifyUI(gcBean);
						}
					} else {
						saveChatInBackGround(gcBean);
					}
				} else {
					if (groupChatActivity.buddy.equalsIgnoreCase(gcBean
							.getGroupId())) {
						((GroupChatActivity) SingleInstance.contextTable
								.get("groupchat")).notifyUI(gcBean);
					} else {
						saveChatInBackGround(gcBean);
					}

				}
			} else {
				saveChatInBackGround(gcBean);
			}
			if (!isExists && gcBean.getType().equalsIgnoreCase("100")) {
				imTone();
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
                    notifyUI();
				}
			});
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void saveChatInBackGround(GroupChatBean gcBean) {
		if (SingleInstance.groupChatHistory.get(gcBean.getGroupId()) != null) {
			Vector<GroupChatBean> chatList = SingleInstance.groupChatHistory
					.get(gcBean.getGroupId());
			if (gcBean.getType().equals("100")) {
				if (gcBean.getSubCategory() != null
						&& gcBean.getSubCategory().equalsIgnoreCase("gdi")) {
					if (gcBean.getParentId() != null
							&& gcBean.getParentId().length() > 0
							&& !gcBean.getParentId().equalsIgnoreCase("null")) {
						for (int i = 0; i < chatList.size(); i++) {
							GroupChatBean gcBean1 = chatList.get(i);
							final int pos = i;
							if (gcBean1 != null
									&& gcBean1.getParentId() != null
									&& gcBean1.getParentId().equals(
											gcBean.getParentId())) {
								String msg = gcBean1.getMessage()
										+ "\nStatus : " + gcBean.getMessage();
								gcBean1.setMessage(msg);
								break;
							}

						}

					} else {
						chatList.add(gcBean);
					}
				} else {
					chatList.add(gcBean);
				}
			} else if (gcBean.getType().equals("104")) {
				for (int i = 0; i < chatList.size(); i++) {
					GroupChatBean gChat = chatList.get(i);
					if (gChat != null
							&& gChat.getSignalid().equals(
									gcBean.getpSingnalId())) {
						chatList.remove(i);
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								SingleInstance.mainContext.notifyUI();
							}
						});
                        if (gcBean.getCategory().equalsIgnoreCase("I")) {
							for (BuddyInformationBean bBean : ContactsFragment
									.getBuddyList()) {
								if (!bBean.isTitle()) {
									bBean.setMessageCount(DBAccess.getdbHeler()
											.getUnreadMsgCountById(
													bBean.getName(),
													CallDispatcher.LoginUser));
								}
							}
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									ContactsFragment.getContactAdapter()
											.notifyDataSetChanged();

								}
							});

						}else if(gcBean.getCategory().equalsIgnoreCase("G"))
						{
							Log.i("AAAA","APP MAIN INSIDE GROUP");
							for (GroupBean bBean : ContactsFragment.getGroupList()) {
									bBean.setMessageCount(DBAccess.getdbHeler()
											.getUnreadMsgCountById(
													bBean.getGroupId(),
													CallDispatcher.LoginUser));

							}
							for (GroupBean bBean : ContactsFragment.getBuddyGroupList()) {
								bBean.setMessageCount(DBAccess.getdbHeler()
										.getUnreadMsgCountById(
												bBean.getGroupId(),
												CallDispatcher.LoginUser));

							}
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									ContactsFragment.getGroupAdapter()
											.notifyDataSetChanged();

								}
							});
						}
						final ExchangesFragment exchanges = ExchangesFragment
								.newInstance(context);
						if (exchanges != null) {
							for (GroupBean gBean : exchanges.exchangesList) {
								gBean.setMessageCount(DBAccess.getdbHeler()
										.getUnreadMsgCountById(
												gBean.getGroupId(),
												CallDispatcher.LoginUser));
							}
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (exchanges.adapter != null) {
										exchanges.adapter
												.notifyDataSetChanged();
									}
								}
							});
						}

						break;
					}
				}
			}
		} else {
			if (gcBean.getType().equals("100")) {
				Vector<GroupChatBean> chatlist = new Vector<GroupChatBean>();
				chatlist.add(gcBean);
				SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
						chatlist);
			}
		}

	}

	public String getCurrentDateandTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
		return "";
	}

	public String getCurrentDateTime() {
		try {
			Date curDate = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

			return sdf.format(curDate);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
		return "";
	}

	public String getCurrentDateTime24Format() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return sdf.format(curDate);
	}

	/**
	 * Logout Scenario
	 * 
	 * @param status
	 * @throws Exception
	 */

	public void logout(boolean status) throws Exception {
		try {
			Log.d("Test", "Inside LogoutAppmain");
			isPinEnable=false;
			if (CallDispatcher.LoginUser != null) {
				if(!CallDispatcher.isCallInitiate) {
					AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
							.get("MAIN");
					appMainActivity.showprogress();
					CommunicationBean bean = new CommunicationBean();
//                bean.setOperationType(SipCommunicator.sip_operation_types.SIGNOUT_ACCOUNT);
//                AppReference.sipQueue.addMsg(bean);
					Log.d("droid123", "SF SIP Signout");
					if (AppReference.isWriteInFile)
						AppReference.logger.debug("LOGOUT : Inside LOGOUT TRUE");
					stopHeartBeatTimer();
					String username = CallDispatcher.LoginUser;
					CallDispatcher.LoginUser = null;
					changeLoginButton();
					isLogin = false;
					ChatFTPBean chatFTPBean = new ChatFTPBean();
					chatFTPBean.setOperation("CANCELNOTIFY");
					FTPPoolManager.processRequest(chatFTPBean);
					FTPQueue obj = new FTPQueue();
					if (obj.getSize() > 0) {
						Log.d("ABCD", "Queuw LogoutAppmain" + obj.getSize());
						obj.makeEmptyQueue();
						NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						notifManager.cancelAll();
					}
					if (status) {
						SingleInstance.printLog("Signin", "SINOUT Respone send",
								"INFO", null);
						WebServiceReferences.webServiceClient.siginout(username);
					} else {

						if (AppReference.isWriteInFile)
							AppReference.logger
									.debug("LOGOUT : Inside LOGOUT FALSE");
						cancelDialog();
						callDisp.dissableUserSettings();
						SingleInstance.printLog("Signin", "Inside else in logout",
								"INFO", null);
						if (callDisp.gpsloc != null) {
							callDisp.gpsloc.Stop();
						}

//					if (WebServiceReferences.contextTable
//							.containsKey("alertscreen")) {
//						((inCommingCallAlert) SingleInstance.contextTable
//								.get("alertscreen")).finish();
//					}
						WebServiceReferences.missedcallCount.clear();
						callDisp.destroySIPStack();
						callDisp.profilePicturepath = "";
						if (CallDispatcher.isIncomingCall) {
							CallDispatcher.isIncomingCall = false;
						}
						if (ftpNotifier != null)
							ftpNotifier.shutDowntaskmanager();
						ftpNotifier = null;

						callDisp.cancelDownloadSchedule();
						callDisp.stopRingTone();
						// if (WebServiceReferences.buddyList != null) {
						// if (WebServiceReferences.buddyList.size() > 0) {
						// WebServiceReferences.buddyList.clear();
						// }
						// }
						// if (callDisp.mainbuddylist != null)
						// callDisp.mainbuddylist.clear();
						// CallDispatcher.adapterToShow.notifyDataSetChanged();
						// if (callDisp.pendinglist != null)
						// callDisp.pendinglist.clear();

						// if (WebServiceReferences.tempBuddyList != null) {
						// if (WebServiceReferences.tempBuddyList.size() > 0) {
						// WebServiceReferences.tempBuddyList.clear();
						// }
						// }
						if (!callDisp.isKeepAliveSendAfter6Sec) {
							handler.removeCallbacks(callDisp.hb_runnable);
							callDisp.isKeepAliveSendAfter6Sec = false;
						}
						WebServiceReferences.webServiceClient.stop();
						WebServiceReferences.running = false;
						if (AppMainActivity.commEngine != null) {
							AppMainActivity.commEngine.stop();
						}
						AppMainActivity.commEngine = null;
						CallDispatcher.LoginUser = null;
						callDisp.ipaddress = null;
						// callDisp.mainbuddylist.clear();
						CallDispatcher.members.clear();
						CallDispatcher.reminderArrives = false;
						callDisp.isKeepAliveSendAfter6Sec = false;
						CallDispatcher.conferenceMembers.clear();
						callDisp.bitmap_table.clear();

						if (HBT != null) {
							stopHeartBeatTimer();
						}
						// removeMemory();
						LoginPageFragment loginPageFragment = LoginPageFragment
								.newInstance(context);
						loginPageFragment.setSilentLogin(false);
						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						fragmentTransaction.replace(
								R.id.activity_main_content_fragment,
								loginPageFragment, "loginfragment");
						fragmentTransaction.commit();
						removeFragments(ContactsFragment.getInstance(context));

						removeFragments(ExchangesFragment.newInstance(context));
						removeFragments(CreateProfileFragment.newInstance(context));
						removeFragments(ViewProfileFragment.newInstance(context));
						removeFragments(UtilityFragment.newInstance(context));
						removeFragments(FilesFragment.newInstance(context));
						removeFragments(AppsViewFragment.newInstance(context));
						removeFragments(AvatarFragment.newInstance(context));
						removeFragments(QuickActionFragment.newInstance(context));
						removeFragments(FormsFragment.newInstance(context));
						removeFragments(MyAccountFragment.newInstance(context));
						removeFragments(SearchPeopleFragment.newInstance(context));
						removeFragments(DashBoardFragment.newInstance(context));
						removeFragments(CallHistoryFragment.newInstance(context));
						removeFragments(CalendarFragment.newInstance(context));
						removeFragments(InviteUserFragment.newInstance(context));
						removeFragments(FindPeople.newInstance(context));
						removeFragments(SecurityQuestions.newInstance(context));
						removeFragments(RequestFragment.newInstance(context));
						removeFragments(PinAndTouchId.newInstance(context));
						removeFragments(ChangePassword.newInstance(context));
					}
					FTPPoolManager.closeChatFTP();
//				imageLoader.DisplayImage("", profileImage,
//						R.drawable.icon_buddy_aoffline);
				}else
				showToast("Please try..Call is in progress");

			} else {

				SingleInstance.printLog("webservice", "Else LogOut Options",
						"INFO", null);

			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

//	private void removeMemory() {
//		// CallDispatcher.adapterToShow = null;
//		GroupActivity.groupAdapter = null;
//		// callDisp.mainbuddylist.clear();
//		// callDisp.mainbuddylist = null;
//		ExchangesFragment.newInstance(context).exchangesList.clear();
//		ExchangesFragment.newInstance(context).exchangesList = null;
//		chageMyStatus();
//		removeFragments(ContactsFragment.getInstance(context));
//
//		removeFragments(ExchangesFragment.newInstance(context));
//		removeFragments(CreateProfileFragment.newInstance(context));
//		removeFragments(ViewProfileFragment.newInstance(context));
//		removeFragments(UtilityFragment.newInstance(context));
//		removeFragments(FilesFragment.newInstance(context));
//		removeFragments(AppsViewFragment.newInstance(context));
//		removeFragments(AvatarFragment.newInstance(context));
//		removeFragments(QuickActionFragment.newInstance(context));
//		removeFragments(FormsFragment.newInstance(context));
//	}

	public void removeFragments(Fragment fragment) {
		try {
			if (fragment != null) {
				clearFragmentMemory(fragment);
				getSupportFragmentManager().beginTransaction().remove(fragment)
						.commitAllowingStateLoss();
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	private void clearFragmentMemory(Fragment fragment) {
		try {
			View view = null;
			if (fragment instanceof ContactsFragment) {
				view = ((ContactsFragment) fragment).getParentView();

				((ContactsFragment) fragment)._rootView = null;
			}

			if (fragment instanceof ExchangesFragment) {
				view = ((ExchangesFragment) fragment).getParentView();
				((ExchangesFragment) fragment)._rootView = null;
			}
			if (fragment instanceof CreateProfileFragment) {
				view = ((CreateProfileFragment) fragment).getParentView();
				((CreateProfileFragment) fragment).view = null;
			}
			if (fragment instanceof ViewProfileFragment) {
				view = ((ViewProfileFragment) fragment).getParentView();
				((ViewProfileFragment) fragment).view = null;
			}
			if (fragment instanceof UtilityFragment) {
				view = ((UtilityFragment) fragment).getParentView();
				((UtilityFragment) fragment).view = null;
			}
			if (fragment instanceof FilesFragment) {
				view = ((FilesFragment) fragment).getParentView();
				((FilesFragment) fragment).view = null;
			}
			if (fragment instanceof AppsViewFragment) {
				view = ((AppsViewFragment) fragment).getParentView();
				((AppsViewFragment) fragment).view = null;
			}
			if (fragment instanceof SettingsFragment) {
				view = ((SettingsFragment) fragment).getParentView();
				((SettingsFragment) fragment).view = null;
			}
			if (fragment instanceof QuickActionFragment) {
				view = ((QuickActionFragment) fragment).getParentView();
				((QuickActionFragment) fragment).view = null;
			}
			if (fragment instanceof FormsFragment) {
				view = ((FormsFragment) fragment).getParentView();
				((FormsFragment) fragment).view = null;
			}
			if (fragment instanceof CallHistoryFragment) {
				view = ((CallHistoryFragment) fragment).getParentView();
				((CallHistoryFragment) fragment)._rootView = null;
			}
			if (fragment instanceof MyAccountFragment) {
				view = ((MyAccountFragment) fragment).getParentView();
				((MyAccountFragment) fragment).view = null;
			}
			if (fragment instanceof DashBoardFragment) {
				view = ((DashBoardFragment) fragment).getParentView();
				((DashBoardFragment) fragment).view = null;
			}
			if (fragment instanceof SearchPeopleFragment) {
				view = ((SearchPeopleFragment) fragment).getParentView();
				((SearchPeopleFragment) fragment)._rootView = null;
			}
			if (fragment instanceof CalendarFragment) {
				view = ((CalendarFragment) fragment).getParentView();
				((CalendarFragment) fragment).view = null;
			}
			if (fragment instanceof InviteUserFragment) {
				view = ((InviteUserFragment) fragment).getParentView();
				((InviteUserFragment) fragment).view = null;
			}
			if (fragment instanceof FindPeople) {
				view = ((FindPeople) fragment).getParentView();
				((FindPeople) fragment)._rootView = null;
			}
			if (fragment instanceof RequestFragment) {
				view = ((RequestFragment) fragment).getParentView();
				((RequestFragment) fragment)._rootView = null;
			}
			if (fragment instanceof SecurityQuestions) {
				view = ((SecurityQuestions) fragment).getParentView();
				((SecurityQuestions) fragment).view = null;
			}
			if (fragment instanceof PinAndTouchId) {
				view = ((PinAndTouchId) fragment).getParentView();
				((PinAndTouchId) fragment)._rootView = null;
			}
			if (fragment instanceof ChangePassword) {
				view = ((ChangePassword) fragment).getParentView();
				((ChangePassword) fragment)._rootView = null;
			}
			if (view != null) {
				MemoryProcessor.getInstance().unbindDrawables(view);
				view = null;
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void showprogress() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (progress == null) {
						progress = new ProgressDialog(context);
						if (progress != null) {
							progress.setCancelable(false);
							progress.setMessage("Logout in Progress ...");
							progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							progress.setProgress(0);
							progress.setMax(100);
							progress.show();
						}
					}

				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}

		});

	}

	public void showformprogress() {

		try {
			if (formprogress == null) {
				formprogress = new ProgressDialog(context);
				if (formprogress != null) {
					formprogress.setCancelable(true);
					formprogress.setMessage("Progress ...");
					formprogress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					formprogress.setProgress(0);
					formprogress.setMax(100);
					formprogress.show();
				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void callingHeartBeart() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				try {

					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								if (CallDispatcher.LoginUser != null) {
									callDisp.publicipstate = CallDispatcher.PUBLIC_IP_STATE.PUBLIC_IP_SUCCESS_LOGIN;
									callDisp.getpublicIpFromUdpStunHttp();
									if (CallDispatcher.getPublicipaddress() == null) {
										try {
											if (AppReference.isWriteInFile)
												AppReference.logger
														.debug("LOGOUT : FROM callingHeartBeart()");
											logout(true);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).start();

				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}

			}
		}, 2500);

		handler.postDelayed(callDisp.hb_runnable, 6000);

		handler.post(new Runnable() {
			//
			@Override
			public void run() {
				try {
					if (callDisp.gpsloc != null) {
						if (!callDisp.gpsloc.isStarted)
							callDisp.gpsloc.Start();
					}
				}

				catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
				//
			}
		});
	}

	public void cancelDialog() {
		try {
			if (progress != null) {
				progress.dismiss();
				progress = null;
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void cancelDialogOnResume() {
		try {
			if (progress != null) {
				progress.dismiss();
				progress = null;
			}
			if (formprogress != null) {
				formprogress.dismiss();
				formprogress = null;

			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void cancelformDialog() {
		try {
			if (isFormLoaded) {
				if (formprogress != null) {
					formprogress.dismiss();
					formprogress = null;

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

				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void chageMyStatus() {
		try {
            notifyUI();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public String loadCurrentStatus() {
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

    int temp = 10;
    protected void ShowView() {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.status_selection);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT

                , WindowManager.LayoutParams.MATCH_PARENT);
        final TextView online = (TextView) dialog.findViewById(R.id.st_online);
        final TextView busy = (TextView) dialog.findViewById(R.id.st_busy);
        final TextView offline = (TextView) dialog.findViewById(R.id.st_offline);
        final TextView invisible = (TextView) dialog.findViewById(R.id.st_invisible);
        Button savepswd = (Button) dialog.findViewById(R.id.savepswd);
		String status_1 = loadCurrentStatus();
		if(status_1.equalsIgnoreCase("online")){
            online.setBackgroundColor(getResources().getColor(R.color.green));
            online.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
		}else if(status_1.equalsIgnoreCase("busy")){
            busy.setBackgroundColor(getResources().getColor(R.color.yellow));
            busy.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
		}else if(status_1.equalsIgnoreCase("offline")){
            offline.setBackgroundColor(getResources().getColor(R.color.pink));
            offline.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
		}else{
            invisible.setBackgroundColor(getResources().getColor(R.color.ash));
            invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
		}
        online.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setBackgroundColor(getResources().getColor(R.color.green));
                online.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
                busy.setBackgroundColor(getResources().getColor(R.color.black2));
                busy.setCompoundDrawablesWithIntrinsicBounds(drawable.busy_icon, 0, 0, 0);
                offline.setBackgroundColor(getResources().getColor(R.color.black2));
                offline.setCompoundDrawablesWithIntrinsicBounds(drawable.offline_icon, 0, 0, 0);
                invisible.setBackgroundColor(getResources().getColor(R.color.black2));
                invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.invisibleicon, 0, 0, 0);
                temp = 1;
            }
        });
        busy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setBackgroundColor(getResources().getColor(R.color.black2));
                online.setCompoundDrawablesWithIntrinsicBounds(drawable.online_icon, 0, 0, 0);
                busy.setBackgroundColor(getResources().getColor(R.color.yellow));
                busy.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
                offline.setBackgroundColor(getResources().getColor(R.color.black2));
                offline.setCompoundDrawablesWithIntrinsicBounds(drawable.offline_icon, 0, 0, 0);
                invisible.setBackgroundColor(getResources().getColor(R.color.black2));
                invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.invisibleicon, 0, 0, 0);
                temp = 2;
            }
        });
        offline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setBackgroundColor(getResources().getColor(R.color.black2));
                online.setCompoundDrawablesWithIntrinsicBounds(drawable.online_icon, 0, 0, 0);
                busy.setBackgroundColor(getResources().getColor(R.color.black2));
                busy.setCompoundDrawablesWithIntrinsicBounds(drawable.busy_icon, 0, 0, 0);
                offline.setBackgroundColor(getResources().getColor(R.color.pink));
                offline.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
                invisible.setBackgroundColor(getResources().getColor(R.color.black2));
                invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.invisibleicon, 0, 0, 0);
                temp = 0;
            }
        });
        invisible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                online.setBackgroundColor(getResources().getColor(R.color.black2));
                online.setCompoundDrawablesWithIntrinsicBounds(drawable.online_icon, 0, 0, 0);
                busy.setBackgroundColor(getResources().getColor(R.color.black2));
                busy.setCompoundDrawablesWithIntrinsicBounds(drawable.busy_icon, 0, 0, 0);
                offline.setBackgroundColor(getResources().getColor(R.color.black2));
                offline.setCompoundDrawablesWithIntrinsicBounds(drawable.offline_icon, 0, 0, 0);
                invisible.setBackgroundColor(getResources().getColor(R.color.ash));
                invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.navigation_check, 0, 0, 0);
                temp = 3;
            }
        });
        savepswd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp!=10) {
                    changeFieldType(temp);
                    temp = 10;
                }
                online.setBackgroundColor(getResources().getColor(R.color.black2));
                online.setCompoundDrawablesWithIntrinsicBounds(drawable.online_icon, 0, 0, 0);
                busy.setBackgroundColor(getResources().getColor(R.color.black2));
                busy.setCompoundDrawablesWithIntrinsicBounds(drawable.busy_icon, 0, 0, 0);
                offline.setBackgroundColor(getResources().getColor(R.color.black2));
                offline.setCompoundDrawablesWithIntrinsicBounds(drawable.offline_icon, 0, 0, 0);
                invisible.setBackgroundColor(getResources().getColor(R.color.black2));
                invisible.setCompoundDrawablesWithIntrinsicBounds(drawable.invisibleicon, 0, 0, 0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void changeFieldType(int status) {
        if (CallDispatcher.isConnected) {
            CallDispatcher.myStatus = Integer.toString(status);
            CallDispatcher callDisp = null;
            if (callDisp == null) {
                callDisp = new CallDispatcher(SingleInstance.mainContext);
            }
            KeepAliveBean aliveBean = callDisp.getKeepAliveBean();
            aliveBean.setKey("0");

            if (!WebServiceReferences.running) {
                callDisp.startWebService(
                        SingleInstance.mainContext.getResources().getString(R.string.service_url), "80");
            }
            WebServiceReferences.webServiceClient.heartBeat(aliveBean);
        } else {
            CallDispatcher.myStatus = "0";
        }
        AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                .get("MAIN");
        appMainActivity.chageMyStatus();
    }
	public void notifyWebServiceResponse(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
                    AppMainActivity.tcpEngine.stopClient();
					LoginPageFragment loginPageFragment = LoginPageFragment
							.newInstance(context);
					loginPageFragment.setSilentLogin(false);
					loginPageFragment.clearTextFeilds();
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment,
							loginPageFragment, "loginfragment");
					fragmentTransaction.commit();
					if (callDisp.gpsloc != null) {
						callDisp.gpsloc.Stop();
					}
					callDisp.dissableUserSettings();
					if (obj instanceof Servicebean) {
						Servicebean servicebean = (Servicebean) obj;
						if (servicebean.getObj() instanceof WebServiceBean) {
							WebServiceBean server_response = (WebServiceBean) servicebean
									.getObj();
							// Context cntxt;
							callDisp.destroySIPStack();
							CallDispatcher.myStatus = "0";
							callDisp.onlineStatus = "";
							// if (WebServiceReferences.contextTable
							// .containsKey("buddiesList"))
							// cntxt = WebServiceReferences.contextTable
							// .get("buddiesList");
							callDisp.profilePicturepath = "";
							WebServiceReferences.missedcallCount.clear();

							if (server_response.getText() != null
									&& server_response.getText()
											.equalsIgnoreCase(
													"you are logged out")) {

								if (CallDispatcher.isIncomingCall) {
									CallDispatcher.isIncomingCall = false;
								}

								if (ftpNotifier != null)
									ftpNotifier.shutDowntaskmanager();
								ftpNotifier = null;
								callDisp.cancelDownloadSchedule();
								callDisp.stopRingTone();

								// if (WebServiceReferences.buddyList != null) {
								// if (WebServiceReferences.buddyList.size() >
								// 0) {
								// WebServiceReferences.buddyList.clear();
								// }
								// }
								// if (callDisp.mainbuddylist != null)
								// callDisp.mainbuddylist.clear();
								// CallDispatcher.adapterToShow
								// .notifyDataSetChanged();
								//
								// if (callDisp.pendinglist != null)
								// callDisp.pendinglist.clear();

								// pendingToShow.notifyDataSetChanged();

								// if (WebServiceReferences.tempBuddyList !=
								// null) {
								// if (WebServiceReferences.tempBuddyList
								// .size() > 0) {
								// WebServiceReferences.tempBuddyList
								// .clear();
								// }
								// }

								SingleInstance.printLog("Signin",
										"going to call signout......", "INFO",
										null);
								SingleInstance
										.printLog(
												"Signin",
												"logout......"
														+ callDisp.isKeepAliveSendAfter6Sec,
												"INFO", null);

								if (!callDisp.isKeepAliveSendAfter6Sec) {
									handler.removeCallbacks(callDisp.hb_runnable);
									callDisp.isKeepAliveSendAfter6Sec = false;
								}
								WebServiceReferences.webServiceClient.stop();
								WebServiceReferences.running = false;

								if (commEngine != null) {
									commEngine.stop();
								}
								commEngine = null;

								CallDispatcher.LoginUser = null;
//								tv_namestatus.setText("");
								callDisp.ipaddress = null;
								// callDisp.mainbuddylist.clear();
								CallDispatcher.members.clear();
								CallDispatcher.reminderArrives = false;
								callDisp.isKeepAliveSendAfter6Sec = false;
								CallDispatcher.conferenceMembers.clear();
								callDisp.bitmap_table.clear();

								if (HBT != null) {
									stopHeartBeatTimer();
								}
								SingleInstance.printLog("Signin",
										"web service response", "INFO", null);

								// removeMemory();
								removeFragments(ContactsFragment
										.getInstance(context));

								removeFragments(ExchangesFragment
										.newInstance(context));
								removeFragments(CreateProfileFragment
										.newInstance(context));
								removeFragments(ViewProfileFragment
										.newInstance(context));
								removeFragments(UtilityFragment
										.newInstance(context));
								removeFragments(FilesFragment
										.newInstance(context));
								removeFragments(AppsViewFragment
										.newInstance(context));
								removeFragments(AvatarFragment
										.newInstance(context));
								removeFragments(QuickActionFragment
										.newInstance(context));
								removeFragments(FormsFragment
										.newInstance(context));
								removeFragments(DashBoardFragment
										.newInstance(context));
								removeFragments(MyAccountFragment
										.newInstance(context));
								removeFragments(SearchPeopleFragment
										.newInstance(context));
								removeFragments(CalendarFragment
										.newInstance(context));
								removeFragments(InviteUserFragment
										.newInstance(context));
								removeFragments(FindPeople
										.newInstance(context));
								removeFragments(RequestFragment
										.newInstance(context));
								removeFragments(CallHistoryFragment
										.newInstance(context));
								removeFragments(SecurityQuestions
										.newInstance(context));
								removeFragments(PinAndTouchId
										.newInstance(context));
								removeFragments(ChangePassword
										.newInstance(context));

								// }
								if (WebServiceReferences.contextTable
										.containsKey("formactivity")) {

									WebServiceReferences.contextTable
											.remove("formactivity");

								}
								progress.dismiss();

								Toast.makeText(SingleInstance.mainContext,
										server_response.getText(),
										Toast.LENGTH_LONG).show();

							} else {
								Toast.makeText(SingleInstance.mainContext,
										server_response.getText(),
										Toast.LENGTH_LONG).show();
							}
						}
					} else if (obj instanceof String) {
						String message = (String) obj;
						Toast.makeText(context, message, Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				} finally {
					cancelDialog();
				}
			}
		});

	}

    class MyTimerTask extends TimerTask {

		@Override
		public void run() {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {

						if (FilesFragment.newInstance(context).filesAdapter != null)
							FilesFragment.newInstance(context).filesAdapter
									.notifyDataSetChanged();
					} catch (Exception e) {
						SingleInstance.printLog(null, e.getMessage(), null, e);
					}
				}
			});
		}

	}

	class MyTimerTaskQA extends TimerTask {

		@Override
		public void run() {
			try {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							setQuickActionAlert();
						} catch (Exception e) {
							SingleInstance.printLog(null, e.getMessage(), null,
									e);
						}
					}
				});
			} catch (Exception e) {
				SingleInstance.printLog(null, e.getMessage(), null, e);
			}
		}

	}

	private void setQuickActionAlert() {
		try {
			if (CallDispatcher.LoginUser != null) {
				Calendar calendar = new GregorianCalendar();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int date = calendar.get(Calendar.DAY_OF_MONTH);
				int minute = calendar.get(Calendar.MINUTE);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				String strDate = year + "-"
						+ WebServiceReferences.setLength2(month) + "-"
						+ WebServiceReferences.setLength2(date) + " "
						+ WebServiceReferences.setLength2(hour) + ":"
						+ WebServiceReferences.setLength2(minute);
				Boolean success = DBAccess.getdbHeler().gettime(strDate,
                        SipNotificationListener.getCurrentContext());

				if (success) {
					Alert.writeBoolean(getApplicationContext(), Alert.satus,
							true);
					QuickActionFragment quickActionFragment = QuickActionFragment
							.newInstance(context);
					quickActionFragment.showAlert();
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment,
							quickActionFragment);
					fragmentTransaction.commit();
				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				AlertDialog.Builder buider = new AlertDialog.Builder(context);
				buider.setMessage(
						SingleInstance.mainContext.getResources().getString(
								R.string.app_background))
						.setPositiveButton(
								context.getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											// TODO Auto-generated method stub
											// finish();
											moveTaskToBack(true);
											// return true;
										} catch (Exception e) {
											SingleInstance.printLog(null,
													e.getMessage(), null, e);
										}

									}
								})
						.setNegativeButton(
								context.getResources().getString(R.string.no),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
				AlertDialog alert = buider.create();
				alert.show();
			}

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			return false;
		}
	}

	public void closeSenderPaging() {
		try {
			Bundle bndl = new Bundle();
			bndl.putString("Hangup", "Hangup");
			Message msg = new Message();
			msg.obj = bndl;
			handler.sendMessage(msg);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void wakeupClock() {
		try {
			keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
			// lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
			lock.disableKeyguard();
			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
					"My Tag");
			this.mWakeLock.acquire();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void releaseClock() {
		try {
			this.mWakeLock.release();
			lock.reenableKeyguard();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void notifyFileDownloaded(String Response, final FTPBean bean) {

		try {
			SingleInstance
					.printLog("FTP", "notifyFileDownloaded", "INFO", null);
			if (CallDispatcher.LoginUser != null) {
				if (Response.equalsIgnoreCase("true")) {
					SharedPreferences prefer = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					boolean tone = prefer.getBoolean("tone", false);
					if (CallDispatcher.istoneEnabled || tone) {

						// if (callDisp.getSettings() != null
						// && callDisp.getSettings().getShareToveValue1()
						// .trim().length() > 0)
						if (UserSettingsBean.sharetone.length() > 0) {
							String file = UserSettingsBean.sharetone;
							if (file != null) {
								callDisp.playShareTone(file);
							}
						}
					}

					final ShareReminder share = (ShareReminder) bean
							.getReq_object();

					Log.d("sharereminder", "from :" + share.getFrom());
					Log.d("sharereminder", "to :" + share.getTo());
					Log.d("sharereminder", "id :" + share.getId());
					Log.d("sharereminder", "fileid :" + share.getFileid());
					Log.d("sharereminder", "filelocation :" + share.getFileLocation());
					Log.d("sharereminder", "remainder status:" + share.getReminderStatus());
					Log.d("sharereminder", "remainderdate :" + share.getReminderdatetime());
					Log.d("sharereminder", "remainder response :" + share.getReminderResponseType());

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
								String currnetDate = dateFormat
										.format(new Date());
								Date newDate = dateFormat.parse(currnetDate);
								cal.setTime(newDate);
								cal.add(Calendar.HOUR,
										Integer.valueOf(share.getVanishValue()));
								vanishValue = dateFormat.format(cal.getTime());
							} catch (Exception e) {
								SingleInstance.printLog(null, e.getMessage(),
										null, e);
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
							File f = new File(AESFileCrypto.decryptFile(context,filepath));
							FileInputStream fileIS = new FileInputStream(f);
							BufferedReader buf = new BufferedReader(
									new InputStreamReader(fileIS));
							readString = buf.readLine();
							if (readString.trim().length() > 12) {
								readString = readString.substring(0, 11);
							}

							String heading = readString;
							if (heading.contains("'")) {
								heading = heading.replace("'", "");
							}
							title = heading;
						}

						catch (Exception e) {
							SingleInstance.printLog(null, e.getMessage(), null,
									e);

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
					cv.put("receiveddateandtime",
							WebServiceReferences.getNoteCreateTimeForFiles());
					cv.put("reminderdateandtime", share.getReminderdatetime());
					cv.put("viewmode", 0);
					cv.put("reminderresponsetype",
							share.getReminderResponseType());

					if (share.getReminderdatetime() != null
							&& share.getReminderdatetime().length() > 0)
						cv.put("reminderstatus", 1);
					else
						cv.put("reminderstatus", 0);

					if (share.getRemindertz() != null)
						cv.put("reminderzone", share.getRemindertz());
					else
						cv.put("reminderzone", "");
					if (share.getBstype() != null) {
						cv.put("bscategory", share.getBstype());
					} else {

						cv.put("bscategory", "");

					}
					if (share.getBsstatus() != null) {
						cv.put("bsstatus", share.getBsstatus());
					} else {

						cv.put("bsstatus", "");

					}
					if (share.getDirection() != null) {
						cv.put("bsdirection", share.getDirection());
					} else {

						cv.put("bsdirection", "");

					}
					if (share.getParent_id() != null) {
						cv.put("parentid", share.getParent_id());
					} else {

						cv.put("parentid", "");
					}
					final Components cmptPro = new Components();
					cmptPro.setcomponentType(componentType);
					cmptPro.setComment(comment);
					//old code
					//cmptPro.setContentPath(filepath);
					//After Changed webservice
					cmptPro.setContentPath(share.getFilepath());
					cmptPro.setContentName(title);
					cmptPro.setContentThumbnail("");
					cmptPro.setDateAndTime(WebServiceReferences
							.getNoteCreateTimeForFiles());
					cmptPro.SetReminderStatus(0);
					cmptPro.setViewMode(0);
					cmptPro.setfromuser(share.getFrom());
					cmptPro.setRemDateAndTime(share.getReminderdatetime());
					cmptPro.setresponseTpe(share.getReminderResponseType());
					cmptPro.setVanishMode(share.getVanishMode());
					cmptPro.setVanishValue(share.getVanishValue());
					cmptPro.setBstype(share.getBstype());
					cmptPro.setBsstatus(share.getBsstatus());
					cmptPro.setDirection(share.getDirection());
					cmptPro.setParent_id(share.getParent_id());
					if (share.getType().equalsIgnoreCase("audio")
							|| share.getType().equalsIgnoreCase("video")) {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						WebServiceReferences.isAutoPlay = preferences
								.getBoolean("autoplay", false);
						if (WebServiceReferences.isAutoPlay
								|| SettingsFragment.autoplaystr) {
							if (cmptPro.getRemDateAndTime() == null
									|| cmptPro.getRemDateAndTime().trim()
											.length() == 0) {

								Components cmpts = (Components) cmptPro.clone();

								if (cmpts != null)
									WebServiceReferences.llAutoPlayContent
											.add(cmpts);

								if (WebServiceReferences.contextTable
										.containsKey("a_play")) {
									// initAutoPlayNotification(WebServiceReferences.isAutoPlay);
								} else {

									autoPlayType = cmpts.getcomponentType();
									if (WebServiceReferences.contextTable
											.containsKey("customvideoplayer")
											|| WebServiceReferences.contextTable
													.containsKey("multimediautils")) {
									} else {
										checkAutoPlayerStatus();
									}

								}
							}

						}
					}

					if (DBAccess.getdbHeler().insertComponent(cv) > 0) {
						int id = DBAccess.getdbHeler().getMaxIdToSet();
						cmptPro.setComponentId(id);
						WebServiceReferences.pendingShare -= 1;
						SingleInstance
								.printLog("thread",
										"c " + share.getReminderStatus(),
										"DEBUG", null);

						if (share.getReminderStatus().equalsIgnoreCase("true")) {
							SingleInstance.printLog("thread", "status true",
									"DEBUG", null);

							String strDateTime = share.getReminderdatetime();
							String strQuery = "update component set reminderdateandtime='"
									+ strDateTime
									+ "',reminderstatus=1 where componentid="
									+ cmptPro.getComponentId();
							SingleInstance.printLog("thread", "status true"
									+ strQuery, "DEBUG", null);

							if (DBAccess.getdbHeler().ExecuteQuery(strQuery)) {
								Intent reminderIntent = new Intent(context,
										ReminderService.class);
								startService(reminderIntent);

							}

						} else {
							SingleInstance.printLog("thread",
									"reminder status false", "DEBUG", null);
						}

						if (cmptPro.getcomponentType() != null
								&& cmptPro.getcomponentType().equalsIgnoreCase(
										"video")) {
							String thumb = cmptPro.getContentPath().substring(
									0,
									cmptPro.getContentPath().lastIndexOf('.'));
							CreateVideoThumbnail(thumb);

						}

						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									if (DBAccess.getdbHeler().isRecordExists(
											"select * from component where componentid="
													+ cmptPro.getComponentId()
													+ "")) {
										notifyFileDownloaded(cmptPro,
												share.getType(),
												cmptPro.getDateAndTime());
									}
								} catch (Exception e) {
									SingleInstance.printLog(null,
											e.getMessage(), null, e);
								}

							}
						});

					}

					if (WebServiceReferences.pendingShare == 0) {
					}

				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void checkAutoPlayerStatus() {

		try {
			if (WebServiceReferences.llAutoPlayContent.size() > 0) {
				if (!WebServiceReferences.contextTable.containsKey("auto_play")) {
					boolean result = handler.postDelayed(delayProcessrunnable,
							5000);
					if (alertDelay == null) {
						showAutoplayAlert();

					} else if (!alertDelay.isShowing()) {
						alertDelay.show();

					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public void showAutoplayAlert() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				AlertDialog.Builder buider = new AlertDialog.Builder(
						SingleInstance.mainContext);
				buider.setMessage(
						context.getResources().getString(
								R.string.autoplay_dialog))
						.setPositiveButton(
								context.getResources().getString(R.string.play),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										handler.removeCallbacks(delayProcessrunnable);
										openAutoPlayScreen();

									}
								})
						.setNegativeButton(
								context.getResources().getString(R.string.stop),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										handler.removeCallbacks(delayProcessrunnable);
										dialog.cancel();
										alertDelay = null;
									}
								});

				alertDelay = buider.create();
				alertDelay.show();
			}
		});
	}

	final Runnable delayProcessrunnable = new Runnable() {
		public void run() {
			try {
				openAutoPlayScreen();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
		}
	};

	private void openAutoPlayScreen() {
		try {
			if (autoPlayType != null && autoPlayType.equalsIgnoreCase("audio")) {
				Components cmpts = WebServiceReferences.llAutoPlayContent
						.removeFirst();
				Intent intent = new Intent(context, MultimediaUtils.class);
				intent.putExtra("filePath", cmpts.getContentPath());
				intent.putExtra("requestCode", 2);
				intent.putExtra("action", "audio");
				intent.putExtra("createOrOpen", "open");
				startActivity(intent);
			} else if (autoPlayType != null
					&& autoPlayType.equalsIgnoreCase("video")) {
				Components cmpts = WebServiceReferences.llAutoPlayContent
						.removeFirst();
				Intent intentVPlayer = new Intent(context, VideoPlayer.class);
				// intentVPlayer.putExtra("File_Path", cmpts.getContentPath());
				// intentVPlayer.putExtra("Player_Type", "Video Player");
				// intentVPlayer.putExtra("time", 0);
				intentVPlayer.putExtra("video", cmpts.getContentPath());
				startActivity(intentVPlayer);
			}
			if (alertDelay != null) {
				if (alertDelay.isShowing()) {
					alertDelay.dismiss();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		autoPlayType = null;
		alertDelay = null;
	}

	public void notifyFileDownloaded(Components cmp, String type, String key) {
		try {

			CompleteListBean cBean = new CompleteListBean();
			cBean.setComponentId(cmp.getComponentId());
			cBean.setcomponentType(cmp.getcomponentType());
			cBean.setContent(cmp.getComment());
			cBean.setContentPath(cmp.getContentPath());
			cBean.setDateAndTime(cmp.getDateAndTime());
			cBean.setContentName(cmp.getContentName());
			cBean.setFromUser(cmp.getfromuser());
			cBean.setTouser(cmp.gettoUser());
			cBean.setReminderTime(cmp.getRemDateAndTime());
			cBean.setViewMode(cmp.getViewMode());
			cBean.setResponseType(cmp.getreminderresponsetype());
			cBean.setVanishMode(cmp.getVanishMode());
			if (cBean.getVanishMode() != null) {
				if (cBean.getVanishMode().equalsIgnoreCase("elapse")) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy hh:mm aa");
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.HOUR,
							Integer.valueOf(cmp.getVanishValue()));
					cBean.setVanishValue(dateFormat.format(cal.getTime()));
					try {
						Intent alarm = new Intent(context,
								FileBroadCastReceiver.class);
						alarm.putExtra("cBean", cBean);
						Log.i("alarm intent", "intent" + context);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(context, cBean.getComponentId(),
										alarm, 0);

						AlarmManager alarmManager = (AlarmManager) context
								.getSystemService(Context.ALARM_SERVICE);
						Log.i("12345", "alarmIntent" + cBean.getComponentId());
						alarmManager.set(AlarmManager.RTC_WAKEUP,
								cal.getTimeInMillis(), pendingIntent);
						Log.i("123456789", "alarmIntent11111111"
								+ pendingIntent);
						// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						// cal.curren tTimeMillis(), 1000, pendingIntent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (cBean.getVanishMode().equalsIgnoreCase("TTL")) {

					Calendar cal = Calendar.getInstance();
					Log.i("calender else", "ms" + cmp.getVanishValue());
					cBean.setVanishValue(cmp.getVanishValue());

					// long time=cal.getTimeInMillis();
					// String s1=cmp.getVanishValue();

					String input = cmp.getVanishValue();
					Log.i("calender date", "ms" + cmp.getVanishValue());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd/MM/yyyy hh:mm aa");
					Log.i("yyyy mmm ddd", " calender " + cmp.getVanishValue());
					Date date = sdf.parse(input);
					Log.i("Date parse", "date" + sdf.parse(input));
					Log.i("calender sample format", "ms" + date.getTime());
					long milliseconds = date.getTime();
					Log.i("milliseconds", "ms" + date.getTime());
					Date currentDate = new Date();
					currentDate.getTime();

					try {
						Log.i("try block", "try" + milliseconds);

						Intent alarm = new Intent(context,
								FileBroadCastReceiver.class);
						alarm.putExtra("cBean", cBean);
						// Bundle bundle = new Bundle();
						// bundle.putParcelableArray(com., value);

						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(context, cBean.getComponentId(),
										alarm, 0);
						AlarmManager alarmManager = (AlarmManager) context
								.getSystemService(Context.ALARM_SERVICE);
						Log.i("12345vanish",
								"alarmIntent" + cBean.getComponentId());
						alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds,
								pendingIntent);
						Log.i("123456789vanish", "alarmIntent11111111" + cBean);
						// alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						// cal.curren tTimeMillis(), 1000, pendingIntent);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (cBean.getParent_id() == null
					|| cBean.getParent_id().length() == 0) {
				FilesFragment filesFragment = FilesFragment
						.newInstance(context);
				SingleInstance.mainContext.notifyUI();
				filesFragment.notifyDownloadFile(cBean);
				String noteType = "";
				if (cBean.getcomponentType().equalsIgnoreCase("note")) {
					noteType = context.getResources().getString(
							R.string.text_share);
				} else if (cBean.getcomponentType().equalsIgnoreCase("audio")) {
					noteType = context.getResources().getString(
							R.string.audio_share);
				} else if (cBean.getcomponentType().equalsIgnoreCase("video")) {
					noteType = context.getResources().getString(
							R.string.video_share);
				} else if (cBean.getcomponentType().equalsIgnoreCase("sketch")) {
					noteType = context.getResources().getString(
							R.string.sketch_share);
				} else if (cBean.getcomponentType()
						.equalsIgnoreCase("document")) {
					noteType = context.getResources().getString(
							R.string.document_share);
				} else if (cBean.getcomponentType().equalsIgnoreCase("photo")) {
					noteType = context.getResources().getString(
							R.string.image_share);
				}
				Toast.makeText(context, cBean.getFromUser() + " " + noteType, 1)
						.show();
			}
			updateFileCount();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			e.printStackTrace();
		}

	}

	public void handleGetDataForms(Object obj) {

		try {
			HashMap<String, String> dtype = new HashMap<String, String>();

			String tableID = null;
			if (obj instanceof Formsinfocontainer) {
				dtype.clear();
				final Formsinfocontainer bean = (Formsinfocontainer) obj;
				String[] fields1 = bean.getForm_fields().split(",");
				ArrayList<String> field = new ArrayList<String>();
				ArrayList<String> field_list = new ArrayList<String>();
				for (int i = 0; i < fields1.length - 1; i++) {
					String split_string[] = fields1[i].split(":");
					String sub = "[" + split_string[0] + "]";
					field_list.add(split_string[0]);
					field.add(sub);
					SingleInstance.printLog("new", "sub" + sub, "DEBUG", null);

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

				String tbl = "[" + bean.getForm_name() + "_"
						+ bean.getForm_id() + "]";

				if (DBAccess.getdbHeler().isFormtableExists(
						bean.getForm_name() + "_" + bean.getForm_id())) {

					String tid_split[] = fields1[fields1.length - 1].toString()
							.split(":");
					field_list.remove("status");
					field_list.add(tid_split[0]);

					if (bean.getRec_list() != null) {

						for (int rec = 0; rec < bean.getRec_list().size(); rec++) {

							FormRecordsbean field_values = bean.getRec_list()
									.get(rec);

							HashMap<String, String> RecordInfo = field_values
									.getRecords_info();
							ContentValues cv = new ContentValues();

							if (field_values.getIsDeleted_record()) {

								if (DBAccess.getdbHeler().isRecordExists(
										"select * from " + tbl
												+ "  where tableid="
												+ RecordInfo.get("tableid"))) {
									String query = "delete from" + tbl
											+ " where tableid='"
											+ RecordInfo.get("tableid") + "'";

									if (DBAccess.getdbHeler().ExecuteQuery(
											query)) {

									}
								}

							} else {

								for (int i = 0; i < RecordInfo.size(); i++) {

									String columnane = "[" + field_list.get(i)
											+ "]";

									if (field_list.get(i).equalsIgnoreCase(
											"tableid")) {
										tableID = RecordInfo.get(field_list
												.get(i));
										cv.put(columnane, RecordInfo
												.get(field_list.get(i)));
									} else {

										if (dtype.get(field_list.get(i))
												.equalsIgnoreCase("BLOB")) {
											cv.put(columnane, callDisp
													.decodeBase64(RecordInfo
															.get(field_list
																	.get(i))));
										} else if (dtype.get(field_list.get(i))
												.equalsIgnoreCase("INTEGER")) {

											if (RecordInfo.get(field_list
													.get(i)) != null) {

												if (RecordInfo.get(
														field_list.get(i))
														.length() > 0) {
													cv.put(columnane,
															Integer.parseInt(RecordInfo
																	.get(field_list
																			.get(i))));

												}
											} else {

												cv.put(columnane, 0);
											}

										} else if (dtype.get(field_list.get(i))
												.contains("2")) {
											if (RecordInfo.get(field_list
													.get(i)) != null) {

												if (RecordInfo.get(
														field_list.get(i))
														.contains("MPD_")
														|| RecordInfo
																.get(field_list
																		.get(i))
																.contains(
																		"MAD_")
														|| RecordInfo
																.get(field_list
																		.get(i))
																.contains(
																		"MVD_")) {

													File extStore = Environment
															.getExternalStorageDirectory();
													File myFile = new File(
															extStore.getAbsolutePath()
																	+ "/COMMedia/"
																	+ RecordInfo
																			.get(field_list
																					.get(i)));

													Log.i("formsnew123",
															"object id : "
																	+ field_list
																			.get(i));

													if (!myFile.exists()) {
														callDisp.downloadOfflineresponse(
																RecordInfo
																		.get(field_list
																				.get(i)),
																"", "forms", "");
														SingleInstance.formMultimediaRecords
																.put(field_list
																		.get(i),
																		1);

													}
													cv.put(columnane,
															RecordInfo
																	.get(field_list
																			.get(i)));

												} else {
													cv.put(columnane,
															RecordInfo
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

								if (DBAccess.getdbHeler().record(
										bean.getForm_name() + "_"
												+ bean.getForm_id(), tableID)) {

									DBAccess.getdbHeler().update(
											"[" + bean.getForm_name() + "_"
													+ bean.getForm_id() + "]",
											cv, tableID);

								} else {

									DBAccess.getdbHeler().insertForRecords(
											bean.getForm_name() + "_"
													+ bean.getForm_id(), cv);

								}

							}
						}

					}

				}

				else {

					if (DBAccess.getdbHeler()
							.createFormTable(field, tbl, dtype)) {
						String tid_split[] = fields1[fields1.length - 1]
								.toString().split(":");
						field_list.remove("status");
						field_list.add(tid_split[0]);

						if (bean.getRec_list() != null) {

							for (int rec = 0; rec < bean.getRec_list().size(); rec++) {

								FormRecordsbean recordsBean = bean
										.getRec_list().get(rec);
								HashMap<String, String> field_values = recordsBean
										.getRecords_info();

								ContentValues cv = new ContentValues();
								if (recordsBean.getIsDeleted_record()) {

									if (DBAccess.getdbHeler().isRecordExists(
											"select * from "
													+ tbl
													+ "  where tableid="
													+ field_values
															.get("tableid"))) {
										String query = "delete from" + tbl
												+ " where tableid='"
												+ field_values.get("tableid")
												+ "'";

										if (DBAccess.getdbHeler().ExecuteQuery(
												query)) {

										}
									}

								} else {
									for (int i = 0; i < field_values.size(); i++) {
										String columnane = "["
												+ field_list.get(i) + "]";

										if (field_list.get(i).equalsIgnoreCase(
												"tableid")) {
											tableID = field_values
													.get(field_list.get(i));
											cv.put(columnane, field_values
													.get(field_list.get(i)));
										} else {

											if (dtype.get(field_list.get(i))
													.equalsIgnoreCase("BLOB")) {
												cv.put(columnane,
														callDisp.decodeBase64(field_values.get(field_list
																.get(i))));
											} else if (dtype
													.get(field_list.get(i))
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

											} else if (dtype.get(
													field_list.get(i))
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
															callDisp.downloadOfflineresponse(
																	field_values
																			.get(field_list
																					.get(i)),
																	"",
																	"forms", "");
															SingleInstance.formMultimediaRecords
																	.put(field_list
																			.get(i),
																			1);

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

									DBAccess.getdbHeler().insertForRecords(
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
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void handleDeleteForms(Object obj) {
		try {
			if (obj instanceof FormsBean) {
				FormsBean bean = (FormsBean) obj;
				if (DBAccess.getdbHeler().isFormExists(bean.getFormId(),
                        bean.getUserName())) {
					String del_row = "delete from formslookup where tableid='"
							+ bean.getFormId() + "' and owner='"
							+ bean.getUserName() + "'";

					if (DBAccess.getdbHeler().ExecuteQuery(del_row)) {
						if (DBAccess.getdbHeler().isFormtableExists(
								bean.getFormName() + "_" + bean.getFormId())) {
							String del_tbl = "DROP TABLE IF EXISTS'"
									+ bean.getFormName() + "_"
									+ bean.getFormId() + "'";

							String del_forminfo = "delete from forminfo where tablename='["
									+ bean.getFormName()
									+ "_"
									+ bean.getFormId() + "]'";

							DBAccess.getdbHeler().ExecuteQuery(del_forminfo);

							if (DBAccess.getdbHeler().ExecuteQuery(del_tbl)) {
								handler.post(new Runnable() {

									@Override
									public void run() {

										try {
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
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
							}

							String del = "delete from forminfo where tablename='["
									+ bean.getFormName()
									+ "_"
									+ bean.getFormId() + "]'";
							DBAccess.getdbHeler().ExecuteQuery(del);

							String del_settings = "delete from formsettings where formid='"
									+ bean.getFormId() + "'";
							DBAccess.getdbHeler().ExecuteQuery(del_settings);

						} else {

						}
					}
				}
			} else if (obj instanceof WebServiceBean) {
				WebServiceBean bn = (WebServiceBean) obj;
				SingleInstance.printLog("thread",
						"delete form result" + bn.getText(), "DEBUG", null);
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void handleGetDataFormsSettings(Object object) {
		try {
			// TODO Auto-generated method stub
			if (object instanceof Vector) {
				Vector<Object> resultObject = (Vector<Object>) object;
				for (int i = 0; i < resultObject.size(); i++) {
					if (resultObject.get(i) instanceof String[]) {
						String[] response = (String[]) resultObject.get(i);

						SingleInstance.printLog(
								"settings",
								"inside web CALL DISPATCHER form"
										+ response.toString(), "DEBUG", null);

						String buddy = response[0];
						String fsid = response[1];
						String formid = response[2];
						String createddate = response[3];
						String modifieddate = response[4];
						String syncquery = response[5];
						String permissionid = response[6];
						String syncid = response[7];
						String owner = response[8];

						owner = CallDispatcher.LoginUser;

						int count = DBAccess.getdbHeler().getreocrdcountUDP(
								formid, fsid, owner, buddy);

						if (count == 0) {

							try {
								String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
										+ "values('"
										+ fsid
										+ "','"
										+ owner
										+ "','"
										+ formid
										+ "','"
										+ buddy
										+ "','"
										+ permissionid
										+ "','"
										+ syncid
										+ "','"
										+ syncquery
										+ "','"
										+ createddate
										+ "','"
										+ modifieddate
										+ "')";

								DBAccess.getdbHeler()
										.ExecuteQuery(insertQuery1);
							} catch (Exception e) {

								e.printStackTrace();

							}

						} else {

							ContentValues cv = new ContentValues();

							cv.put("accesscode", permissionid);
							cv.put("synccode", syncid);
							cv.put("syncquery", syncquery);
							cv.put("datecreated", createddate);
							cv.put("datemodified", modifieddate);
							DBAccess.getdbHeler().updates(fsid, cv, formid);
						}

						if (WebServiceReferences.contextTable
								.containsKey("frmviewer")) {

							FormViewer frm_creator = (FormViewer) WebServiceReferences.contextTable
									.get("frmviewer");
							if (frm_creator.isShowingCurrentForm(formid)) {
								frm_creator.refreshList();
							}

						}
					} else if (resultObject.get(i) instanceof Vector) {

						Vector<Vector<FormFieldBean>> fieldAccessList = (Vector<Vector<FormFieldBean>>) resultObject
								.get(i);
						Log.i("formfield123", "parse result totallist size : "
								+ fieldAccessList.size());
						Vector<FormFieldBean> ownerList = fieldAccessList
								.get(0);
						Log.i("formfield123", "parse result ownerlist size : "
								+ ownerList.size());
						for (FormFieldBean formFieldBean : ownerList) {
							if (formFieldBean.getDefaultPermissionList() != null) {
								Vector<DefaultPermission> dList = formFieldBean
										.getDefaultPermissionList();
								for (DefaultPermission defaultPermission : dList) {
									DBAccess.getdbHeler()
											.saveOrUpdateOwnerFormField(
													formFieldBean.getFormId(),
													defaultPermission
															.getAttributeId(),
													defaultPermission
															.getDefaultPermission());
									Vector<BuddyPermission> bList = defaultPermission
											.getBuddyPermissionList();
									if (bList != null) {
										for (BuddyPermission buddyPermission : bList) {
											DBAccess.getdbHeler()
													.saveOrUpdateIndividualFormField(
															formFieldBean
																	.getFormId(),
															defaultPermission
																	.getAttributeId(),
															buddyPermission
																	.getBuddyName(),
															buddyPermission
																	.getBuddyAccess());
										}
									}

								}
							}

						}
						Vector<FormFieldBean> individualList = fieldAccessList
								.get(1);
						Log.i("formfield123",
								"parse result individuallist size : "
										+ individualList.size());
						for (FormFieldBean formFieldBean : individualList) {
							if (formFieldBean.getIndividualPermissionList() != null) {
								Vector<IndividualPermission> iBeanList = formFieldBean
										.getIndividualPermissionList();
								for (IndividualPermission iBean : iBeanList) {
									DBAccess.getdbHeler()
											.saveOrUpdateIndividualFormField(
													formFieldBean.getFormId(),
													iBean.getAttributeId(),
													iBean.getUserName(),
													iBean.getPermission());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			e.printStackTrace();
		}

	}

	public void handleDataForms(Object obj) {
		try {
			HashMap<String, String> dtype = new HashMap<String, String>();
			if (obj instanceof FormsBean) {
				SingleInstance.printLog("FORMS", "inside handle get content",
						"INFO", null);

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
				if (DBAccess.getdbHeler().isFormtableExists(
						bean.getFormName() + "_" + bean.getFormId())) {

					SingleInstance.printLog("FORMS",
							"insideeeeeeeeeeeeeee  formmmmmm existsssssss",
							"INFO", null);

					String tid_split[] = fields1[fields1.length - 1].toString()
							.split(":");
					field_list.add(tid_split[0]);
					if (bean.getFormRecords() != null) {
						for (int rec = 0; rec < bean.getFormRecords().size(); rec++) {
							String[] field_values = bean.getFormRecords().get(
									rec);
							ContentValues cv = new ContentValues();

							for (int idx = 0; idx < field_values.length; idx++) {

								SingleInstance.printLog("FORMS", "Field name"
										+ field_list.get(idx) + ", Value : "
										+ field_values[idx], "INFO", null);

								String columnname = "[" + field_list.get(idx)
										+ "]";
								if (field_list.get(idx).equalsIgnoreCase(
										"tableid")) {
									cv.put(columnname, field_values[idx]);
								} else {
									if (dtype.get(field_list.get(idx))
											.equalsIgnoreCase("BLOB"))
										cv.put(field_list.get(idx),
												callDisp.decodeBase64(field_values[idx]));
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

							DBAccess.getdbHeler().update(
									"[" + bean.getFormName() + "_"
											+ bean.getFormId() + "]", cv,
									bean.getEditRecordId());

						}

						handler.post(new Runnable() {

							@Override
							public void run() {
								try {

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
								} catch (Exception e) {
									SingleInstance.printLog(null,
											e.getMessage(), null, e);
								}
							}
						});

					}

				} else {
					SingleInstance.printLog("FORMS",
							"insideeeeeeeeeeeeeee  elsesssssss", "INFO", null);

					field_list.add("status");
					dtype.put("status", "nvarchar(20)");
					ArrayList<String> field = new ArrayList<String>();

					for (int i = 0; i < field_list.size(); i++) {
						String sub = "[" + field_list.get(i) + "]";
						SingleInstance.printLog("FORMS", sub, "INFO", null);
						field.add(sub);
					}
					String tbl = "[" + bean.getFormName() + "_"
							+ bean.getFormId() + "]";
					if (DBAccess.getdbHeler()
							.createFormTable(field, tbl, dtype)) {

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
									SingleInstance.printLog("FORMS",
											"Field name" + field_list.get(idx)
													+ ", Value : "
													+ field_values[idx],
											"INFO", null);

									String columnname = "["
											+ field_list.get(idx) + "]";

									if (field_list.get(idx).equalsIgnoreCase(
											"tableid")) {
										cv.put(columnname, field_values[idx]);
									} else {
										if (dtype.get(field_list.get(idx))
												.equalsIgnoreCase("BLOB")) {
											cv.put(field_list.get(idx),
													callDisp.decodeBase64(field_values[idx]));
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
								if (DBAccess.getdbHeler().insertForRecords(
										bean.getFormName() + "_"
												+ bean.getFormId(), cv)) {

								}
							}

							handler.post(new Runnable() {

								@Override
								public void run() {
									try {
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
									} catch (Exception e) {
										SingleInstance.printLog(null,
												e.getMessage(), null, e);
									}
								}
							});

						}

					}
				}

			} else if (obj instanceof WebServiceBean) {
				SingleInstance
						.printLog("webservice",
								"getdata at background came to else part",
								"INFO", null);
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void process() {
		try {
			isFormLoaded = true;
			WebServiceReferences.buddies_forms.clear();

			String form_id = "";

			for (int i = 0; i < formsConfig.size(); i++) {
				if (formsConfig.get(i) instanceof FormsListBean) {

					final FormsListBean bean = (FormsListBean) formsConfig
							.get(i);

					if (form_id.trim().length() == 0)
						form_id = bean.getFormId();
					else
						form_id = form_id + "," + bean.getFormId();

					Alert.writeString(context, Alert.form_owner,
							bean.getForm_owner());
					Alert.writeString(context, Alert.formid, bean.getFormId());
					if (bean.getFormId() != null) {

						if (!DBAccess.getdbHeler().isFormExists(
								bean.getFormId(), bean.getForm_owner())) {
							String status = "1";
							String formicon;
							String formdescription;

							if (bean.getFormicon().length() == 0) {
								formicon = "";
							} else {

								formicon = bean.getFormicon();
								File extStore = Environment
										.getExternalStorageDirectory();
								File myFile = new File(
										extStore.getAbsolutePath()
												+ "/COMMedia/" + formicon);

								if (!myFile.exists()) {
									callDisp.downloadOfflineresponse(formicon,
											"", "forms", "");

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
									+ status
									+ "','"
									+ formicon
									+ "','"
									+ formdescription + "')";
							if (DBAccess.getdbHeler().ExecuteQuery(insertQuery)) {
								handler.post(new Runnable() {

									@Override
									public void run() {

										try {
											if (SingleInstance.contextTable
													.containsKey("forms")) {
												FormsFragment quickActionFragment = FormsFragment
														.newInstance(context);

												quickActionFragment
														.populateLists();

											}

											WebServiceReferences.webServiceClient
													.Getcontent(
															CallDispatcher.LoginUser,
															bean.getFormId(),
															"", context);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
							}
						} else {
							if (DBAccess.getdbHeler().isFormExists(
									bean.getFormId(), bean.getForm_owner())) {
								String recordtime = DBAccess.getdbHeler()
										.getRecordtime(bean.getFormId());

								if (!recordtime.equals(bean.getFormtime())) {

									String strUpdateCount = "update formslookup set rowcount='"
											+ bean.getNoofRows()
											+ "',formtime='"
											+ bean.getFormtime()
											+ "' where tableid='"
											+ bean.getFormId() + "'";

									if (DBAccess.getdbHeler().ExecuteQuery(
											strUpdateCount)) {
										if (SingleInstance.contextTable
												.containsKey("forms")) {
											FormsFragment quickActionFragment = FormsFragment
													.newInstance(context);

											quickActionFragment.populateLists();

											if (WebServiceReferences.contextTable
													.containsKey("frmviewer")) {
												FormViewer frmviewer = (FormViewer) WebServiceReferences.contextTable
														.get("frmviewer");

												frmviewer
														.showConfirmation(
																CallDispatcher.LoginUser,
																bean.getFormId(),
																recordtime,
																false);

											}

											else {
												WebServiceReferences.webServiceClient
														.Getcontent(
																CallDispatcher.LoginUser,
																bean.getFormId(),
																recordtime,
																context);
											}

										} else {
											WebServiceReferences.webServiceClient
													.Getcontent(
															CallDispatcher.LoginUser,
															bean.getFormId(),
															recordtime, context);
										}
									}
								} else {

									if (SingleInstance.contextTable
											.containsKey("forms")) {
										FormsFragment quickActionFragment = FormsFragment
												.newInstance(context);

										quickActionFragment.populateLists();

									}
								}

							}

							else {

								handler.post(new Runnable() {

									@Override
									public void run() {

										try {
											if (SingleInstance.contextTable
													.containsKey("forms")) {
												FormsFragment quickActionFragment = FormsFragment
														.newInstance(context);

												quickActionFragment
														.populateLists();

											}

											FormViewer frm_activity = (FormViewer) WebServiceReferences.contextTable
													.get("frmviewer");
											if (frm_activity
													.isShowingCurrentForm(bean
															.getFormId())) {
												frm_activity
														.showConfirmation(
																CallDispatcher.LoginUser,
																bean.getFormId(),
																bean.getFormtime(),
																false);
											} else {

											}
										} catch (Exception e) {
											SingleInstance.printLog(null,
													e.getMessage(), null, e);
										}
									}
								});

							}

						}

					}
					if (CallDispatcher.LoginUser != null) {

						if (!bean.getForm_owner().equalsIgnoreCase(
								CallDispatcher.LoginUser)
								&& !WebServiceReferences.buddies_forms
										.contains(bean.getForm_owner())) {
							WebServiceReferences.buddies_forms.add(bean
									.getForm_owner());
						}
					}

				}
				if (formsConfig.get(i) instanceof FormSettingBean) {

					FormSettingBean bean = (FormSettingBean) formsConfig.get(i);
					String settingid = bean.getFsId();
					String buddyid = bean.getbuddy();
					String servermdyDate = bean.getmodifieddate();
					String ownerid = Alert.readString(context,
							Alert.form_owner, null);
					String formid = Alert.readString(context, Alert.formid,
							null);

					String message = settingid + "settingid," + buddyid
							+ "buddyid," + ownerid + "ownerid,formid" + formid;
					SingleInstance.printLog("FORMS", message, "INFO", null);

					if (!DBAccess.getdbHeler().isSettingFormExists(settingid)) {

						String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
								+ "values('"
								+ bean.getFsId()
								+ "','"
								+ Alert.readString(context, Alert.form_owner,
										null)
								+ "','"
								+ Alert.readString(context, Alert.formid, null)
								+ "','"
								+ bean.getbuddy()
								+ "','"
								+ bean.getPermisionid()
								+ "','"
								+ bean.getsyncid()
								+ "','"
								+ bean.getSyncquery()
								+ "','"
								+ bean.getcreateddate()
								+ "','"
								+ bean.getmodifieddate() + "')";

						if (DBAccess.getdbHeler().ExecuteQuery(insertQuery1)) {

						}
					} else {

						String formMofified = DBAccess.getdbHeler()
								.getSettingsModifiedtime(formid);
						if (!servermdyDate.equalsIgnoreCase(formMofified)) {

							String strUpdateCount = "update formsettings set settingid='"
									+ bean.getFsId()
									+ "',formowenerid='"
									+ Alert.readString(context,
											Alert.form_owner, null)
									+ "',formid='"
									+ Alert.readString(context, Alert.formid,
											null)
									+ "',buddyid='"
									+ bean.getbuddy()
									+ "',accesscode='"
									+ bean.getPermisionid()
									+ "',synccode='"
									+ bean.getsyncid()
									+ "',syncquery='"
									+ bean.getSyncquery()
									+ "',datecreated='"
									+ bean.getcreateddate()
									+ "',datemodified='"
									+ bean.getmodifieddate()
									+ "' where formid='"
									+ Alert.readString(context, Alert.formid,
											null) + "'";

							if (DBAccess.getdbHeler().ExecuteQuery(
									strUpdateCount)) {

							}

						}

					}

				}

				if (formsConfig.get(i) instanceof FormAttributeBean) {

					FormAttributeBean bean = (FormAttributeBean) formsConfig
							.get(i);
					String tbl_name = "[" + bean.getTableName() + "_"
							+ bean.getFormid() + "]";
					String col_name = "[" + bean.getFieldname() + "]";
					if (!DBAccess.getdbHeler().isAttributeFormExists(
							bean.getTableName() + "_" + bean.getFormid(),
							bean.getFieldname())) {
						DBAccess.getdbHeler().saveOrUpdateFormInfo(tbl_name,
								col_name, bean);

						String instruction = bean.getInstruction();

						SingleInstance.printLog("FORMS",
								"***************************lennn"
										+ instruction.length(), "INFO", null);

						if (instruction.length() != 0) {
							String[] instructions = instruction.split(",");

							for (int j = 0; j < instructions.length; j++) {
								File extStore = Environment
										.getExternalStorageDirectory();
								if (instructions[j].length() > 0) {
									File myFile = new File(
											extStore.getAbsolutePath()
													+ "/COMMedia/"
													+ instructions[j]);

									if (!myFile.exists()) {

										callDisp.downloadOfflineresponse(
												instructions[j], "", "forms",
												"");

									}
								}

							}

						}
					}
				}

			}
			if (form_id.trim().length() > 0) {

				ArrayList<String> deteteForms = DBAccess.getdbHeler()
						.LocalTableName(form_id.trim());

				String deleteQuery = "delete from formslookup where tableid not in ("
						+ form_id + ")";
				DBAccess.getdbHeler().ExecuteQuery(deleteQuery);
				form_id = null;

				if (deteteForms.size() > 0) {
					for (int j = 0; j < deteteForms.size(); j++) {
						if (deteteForms.get(j).length() > 1) {
							String[] Tablename = deteteForms.get(j).split("_");
							String del_tbl = "DROP TABLE IF EXISTS'"
									+ deteteForms.get(j) + "'";
							DBAccess.getdbHeler().ExecuteQuery(del_tbl);

							String del_settings = "delete from formsettings where formid='"
									+ Tablename[1] + "'";
							String del_forminfo = "delete from forminfo where tablename='["
									+ deteteForms.get(j) + "]'";

							DBAccess.getdbHeler().ExecuteQuery(del_settings);
							DBAccess.getdbHeler().ExecuteQuery(del_forminfo);
						}

					}
					if (SingleInstance.contextTable.containsKey("forms")) {
						FormsFragment quickActionFragment = FormsFragment
								.newInstance(context);

						quickActionFragment.populateLists();

					}

				}
				cancelformDialog();

			} else {

				ArrayList<String> deteteForms = DBAccess.getdbHeler()
						.LocalTableNameForNewUser();
				form_id = null;

				if (deteteForms.size() > 0) {
					for (int j = 0; j < deteteForms.size(); j++) {

						if (deteteForms.get(j).length() > 1) {

							String[] Tablename = deteteForms.get(j).split("_");
							String deleteQuery = "delete from formslookup where tableid='"
									+ Tablename[1] + "'";
							DBAccess.getdbHeler().ExecuteQuery(deleteQuery);
							String del_tbl = "DROP TABLE IF EXISTS'"
									+ deteteForms.get(j) + "'";
							DBAccess.getdbHeler().ExecuteQuery(del_tbl);

							String del_settings = "delete from formsettings where formid='"
									+ Tablename[1] + "'";
							String del_forminfo = "delete from forminfo where tablename='["
									+ deteteForms.get(j) + "]'";

							DBAccess.getdbHeler().ExecuteQuery(del_settings);
							DBAccess.getdbHeler().ExecuteQuery(del_forminfo);
						}

					}
					if (SingleInstance.contextTable.containsKey("forms")) {
						FormsFragment quickActionFragment = FormsFragment
								.newInstance(context);

						quickActionFragment.populateLists();

					}
				}
				cancelformDialog();

			}

		} catch (Exception e) {
			e.printStackTrace();
			SingleInstance.printLog(null, e.getMessage(), null, e);
		} finally {
			cancelformDialog();
		}

	}

	public void handleFormsTemplate(Object object) {

		try {
			if (object instanceof ArrayList) {

				ArrayList list = (ArrayList) object;

				formsConfig.clear();
				for (int i = 0; i < list.size(); i++) {

					final Object obj = list.get(i);

					if (obj instanceof FormsListBean) {

						formsConfig.add(obj);

					} else if (obj instanceof FormSettingBean) {

						formsConfig.add(obj);

					} else if (obj instanceof FormAttributeBean) {

						formsConfig.add(obj);
					}

				}

				if (formsConfig.size() > 0) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							try {
								process();
								WebServiceReferences.webServiceClient
										.Getformsettings("",
												CallDispatcher.LoginUser,
												context);
							} catch (Exception e) {
								SingleInstance.printLog(null, e.getMessage(),
										null, e);
							}

						}
					});
				} else {

				}
				// Message message = new Message();
				// Bundle bundle = new Bundle();
				// bundle.putString("buddy", "hangupfullscreen");
				// message.obj = bundle;
				// handler.sendMessage(message);

			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	public void handleGetDataAttributeForms(Object obj, String isEditForms) {
		try {
			if (obj instanceof ArrayList) {
				ArrayList response = (ArrayList) obj;

				for (int i = 0; i < response.size(); i++) {

					final Object obj1 = response.get(i);
					if (obj1 instanceof FormAttributeBean) {
						FormAttributeBean faBean = (FormAttributeBean) response
								.get(i);

						String tbl_name = "";

						if (formnames != null) {
							tbl_name = "[" + formnames + "_"
									+ faBean.getFormid() + "]";
						} else {
							tbl_name = DBAccess.getdbHeler().getFormName(
									faBean.getFormid());
							tbl_name = "[" + tbl_name + "_"
									+ faBean.getFormid() + "]";
						}
						String col_name = "[" + faBean.getFieldname() + "]";
						// String insertQueryinfotbl =
						// "insert into forminfo(tablename,column,entrymode,validdata,defaultvalue,instruction,errortip)"
						// + "values('"
						// + tbl_name
						// + "','"
						// + col_name
						// + "','"
						// + bib.getEntry()
						// + "','"
						// + bib.getDatavalidation()
						// + "','"
						// + bib.getDefaultvalue()
						// + "','"
						// + bib.getInstruction()
						// + "','"
						// + bib.getErrortip() + "')";

						String instruction = faBean.getInstruction();

						if (instruction.length() != 0) {
							String[] instructions = instruction.split(",");

							for (int j = 0; j < instructions.length; j++) {
								File extStore = Environment
										.getExternalStorageDirectory();

								if (instructions[j].length() > 0) {
									File myFile = new File(
											extStore.getAbsolutePath()
													+ "/COMMedia/"
													+ instructions[j]);

									if (!myFile.exists()) {
										callDisp.downloadOfflineresponse(
												instructions[j], "", "forms",
												"");

									}
								}

							}

						}

						if (isEditForms.equalsIgnoreCase("yes")) {
							String table = tbl_name.replace("[", "")
									.replace("]", "").trim();
							String column = col_name.replace("[", "")
									.replace("]", "").trim();
							Log.i("editforms123", "table : " + table
									+ " column : " + column);
							ArrayList<InputsFields> tempList = new ArrayList<InputsFields>();
							Vector<InputsFields> oldList = DBAccess
									.getdbHeler().getFormFields(table);
							ArrayList<InputsFields> list = new ArrayList<InputsFields>();
							if (oldList != null) {
								list.addAll(oldList);
							}
							for (InputsFields iFields : list) {
								if (iFields.getAttributeId().equals(
										faBean.getAttributeid())) {
									iFields.setFieldName(faBean.getFieldname());
								}
								tempList.add(iFields);
							}

							DBAccess.getdbHeler().updateTableColumn(table,
									tempList, list);
						}
						DBAccess.getdbHeler().saveOrUpdateFormInfo(tbl_name,
								col_name, faBean);
						// if (DBAccess.getdbHeler().ExecuteQuery(
						// insertQueryinfotbl)) {
						//
						// }

					}
				}

			} else if (obj instanceof WebServiceBean) {
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}

	}

	private void refreshGroupList() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					ContactsFragment contactsFragment = ContactsFragment
							.getInstance(getApplicationContext());
					contactsFragment.getList();
					RoundingFragment roundingFragment = RoundingFragment
							.newInstance(getApplicationContext());
					roundingFragment.getList();
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}
		});

	}

	public void notifySigninError(final String title, final String message) {
		handler.post(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub

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

					showprogress();
					logout(true);

					final AlertDialog confirmation = new AlertDialog.Builder(context)
							.create();
					confirmation.setTitle(title);
					confirmation.setMessage(message);
					confirmation.setCancelable(false);
					confirmation.setButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										// TODO Auto-generated method stub
										confirmation.dismiss();

									} catch (Exception e) {
										SingleInstance.printLog(null,
												e.getMessage(), null, e);
									}
								}
							});

					confirmation.show();
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}
		});

	}

	public void setProfilePic() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
                    ProfileBean bean=SingleInstance.myAccountBean;
                    ImageView user_image = (ImageView) findViewById(R.id.user_image);
                    if(bean.getPhoto()!=null){
                        String profilePic=bean.getPhoto();
                        Log.i("AAAA", "MYACCOUNT "+profilePic);
                        if (profilePic != null && profilePic.length() > 0) {
                            if (!profilePic.contains("COMMedia")) {
                                profilePic = Environment
                                        .getExternalStorageDirectory()
                                        + "/COMMedia/" + profilePic;
                            }
                            Log.i("AAAA","MYACCOUNT "+profilePic);
                            imageLoader.DisplayImage(profilePic, user_image,
                                    R.drawable.img_user);
                        }
                    }
//					String profilePic = DBAccess.getdbHeler(
//							getApplicationContext()).getProfilePic(
//							CallDispatcher.LoginUser);
//					SingleInstance.printLog("PROFILE", "profile image path : "
//							+ profilePic, "INFO", null);
//					if (profilePic != null && profilePic.length() > 0) {
//						if (!profilePic.contains("COMMedia"))
//							profilePic = Environment
//									.getExternalStorageDirectory()
//									+ "/COMMedia/" + profilePic;
//
//						SingleInstance.printLog("PROFILE",
//								"profile image path 2 : " + profilePic, "INFO",
//								null);
//                        ImageView user_image = (ImageView) findViewById(R.id.user_image);
//						imageLoader.DisplayImage(profilePic, user_image,
//								R.drawable.img_user);

//					} else {
//						imageLoader.DisplayImage(profilePic, profileImage,
//								R.drawable.img_user);
//					}
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}
		});

	}

	private boolean isLoggedIn() {
		try {
			if (CallDispatcher.LoginUser != null
					&& CallDispatcher.LoginUser.length() > 0
                    && CallDispatcher.Password != null
                    && CallDispatcher.Password.length() > 0) {
				return true;
			} else {
				LoginPageFragment loginPageFragment = LoginPageFragment
						.newInstance(context);
				loginPageFragment.setSilentLogin(false);
				FragmentManager fragmentManager = this
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment, loginPageFragment,
						"loginfragment");
				fragmentTransaction.commit();

			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
		return false;
	}

	public void ShowError(final String message) {
		try {
			logout(false);
			final Context context;
			GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");
			if (gcActivity != null) {
				context = gcActivity;
			} else {
				context = this.context;
			}
			// handler.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// try {
			// AlertDialog.Builder confirmation = new AlertDialog.Builder(
			// context);
			// confirmation.setTitle(Title);
			// confirmation.setMessage(Message);
			// confirmation.setCancelable(true);
			// confirmation.setPositiveButton("OK",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// try {
			// dialog.dismiss();
			// } catch (Exception e) {
			// SingleInstance.printLog(null,
			// e.getMessage(), null, e);
			// }
			//
			// }
			// });
			//
			// confirmation.show();
			//
			// } catch (Exception e) {
			// SingleInstance.printLog(null, e.getMessage(), null, e);
			// }
			// }
			// });
			showToast(message);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void ShowGroupDeleteAlert(final String Title, final String Message,
			final GroupChatActivity gChat) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						AlertDialog.Builder confirmation = new AlertDialog.Builder(
								gChat);
						confirmation.setTitle(Title);
						confirmation.setMessage(Message);
						confirmation.setCancelable(true);
						confirmation.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										try {
											gChat.finish();
											dialog.dismiss();
										} catch (Exception e) {
											SingleInstance.printLog(null,
													e.getMessage(), null, e);
										}

									}
								});

						confirmation.show();
					} catch (Exception e) {
						SingleInstance.printLog(null, e.getMessage(), null, e);
					}
				}
			});
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	private void imTone() {
		try {

			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
				AssetFileDescriptor descriptor;
				descriptor = context.getAssets().openFd("im.mp3");
				mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
						descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				mediaPlayer.setLooping(false);
				mediaPlayer.prepare();
				mediaPlayer.start();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						try {
							mediaPlayer.release();
							mediaPlayer = null;
						} catch (Exception e) {
							SingleInstance.printLog(null, e.getMessage(), null,
									e);
						}
					}
				});
			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void startHeartBeatTimer() {

		try {
			isInternetEnabled = false;
			if (HBT == null) {
				HBT = new HeartbeatTimer();
				HBT.setReference(callDisp);
				Timer tm = new Timer();
				tm.schedule(HBT, 0, 3000);
			}
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void stopHeartBeatTimer() {
		isInternetEnabled = true;
		if (HBT != null) {
			HBT.cancel();
			HBT = null;
		}
	}

	// public void notifyContactList() {
	// try {
	// ArrayList<Databean> contactList = (ContactsFragment
	// .newInstance(context)).contactList;
	// ArrayList<Databean> newContactList = new ArrayList<Databean>();
	// HashMap<String, Databean> contactHashMap = new HashMap<String,
	// Databean>();
	// for (Databean dBean : contactList) {
	// contactHashMap.put(dBean.getname(), dBean);
	// }
	// Set selectedSet = (Set) contactHashMap.entrySet();
	// Iterator mapIterator = selectedSet.iterator();
	// while (mapIterator.hasNext()) {
	// Map.Entry mapEntry = (Map.Entry) mapIterator.next();
	// Databean databean = (Databean) mapEntry.getValue();
	// newContactList.add(databean);
	// }
	// (ContactsFragment.newInstance(context)).contactList.clear();
	// (ContactsFragment.newInstance(context)).contactList
	// .addAll(newContactList);
	// CallDispatcher.adapterToShow = new CountryArrayAdapter(context,
	// R.layout.relate, newContactList);
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// CallDispatcher.adapterToShow.notifyDataSetChanged();
	// }
	// });
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	@SuppressWarnings("unchecked")
	public void updateBuddyInformation(BuddyInformationBean buddyInformationBean) {
		try {
            Log.d("AAA", "UpdateBuddyInformation Appmain ");
			/* getBuddyList() default size is 2 */
			if (ContactsFragment.getBuddyList().size() > 1) {
				Vector<BuddyInformationBean> tempList = new Vector<BuddyInformationBean>();
				tempList.clear();
				tempList = (Vector<BuddyInformationBean>) ContactsFragment
						.getBuddyList().clone();

				Vector<BuddyInformationBean> btempList = new Vector<BuddyInformationBean>();
				btempList.clear();

				for (BuddyInformationBean bInfo : btempList) {

					if (!bInfo.isTitle()) {
						if (buddyInformationBean.getStatus().equalsIgnoreCase(
								"delete")) {
							if (bInfo.getName().equalsIgnoreCase(
									buddyInformationBean.getName())) {

							}
						}
					}
				}

				for (BuddyInformationBean bInfo : tempList) {
					if (!bInfo.isTitle()) {
						if (buddyInformationBean.getStatus().equalsIgnoreCase(
								"delete")) {
							if (bInfo.getName().equalsIgnoreCase(
									buddyInformationBean.getName())) {
								if (bInfo.getStatus().equalsIgnoreCase(
										"pending")) {
									showToast(buddyInformationBean.getName()
											+ " reject's your request");
								} else if (bInfo.getStatus().equalsIgnoreCase(
										"new")) {
									showToast("Buddy request by "
											+ buddyInformationBean.getName()
											+ " has been deleted");
									if (WebServiceReferences.contextTable
											.containsKey("viewprofileactivity")) {
										ViewProfiles vProfiles = (ViewProfiles) WebServiceReferences.contextTable
												.get("viewprofileactivity");
										if (vProfiles.getBuddyname() != null
												&& vProfiles
														.getBuddyname()
														.equalsIgnoreCase(
																bInfo.getName())) {
											vProfiles.finish();
										}
									}
								} else {
									showToast("you have been removed from "
											+ buddyInformationBean.getName()
											+ " contact list");
									if (WebServiceReferences.contextTable
											.containsKey("viewprofileactivity")) {
										ViewProfiles vProfiles = (ViewProfiles) WebServiceReferences.contextTable
												.get("viewprofileactivity");
										if (vProfiles.getBuddyname() != null
												&& vProfiles
														.getBuddyname()
														.equalsIgnoreCase(
																bInfo.getName())) {
											vProfiles.finish();
										}
									}
								}
								synchronized (ContactsFragment.getBuddyList()) {
									ContactsFragment.getBuddyList().remove(
											bInfo);
									break;
								}
							}
						} else {
							if (!bInfo.getName().equalsIgnoreCase(
									buddyInformationBean.getName())) {
								synchronized (ContactsFragment.getBuddyList()) {
									buddyInformationBean.setTitle(false);
									ContactsFragment.getBuddyList().add(
											buddyInformationBean);

									break;

								}
							}
						}
					}
				}
			} else {
				synchronized (ContactsFragment.getBuddyList()) {
					buddyInformationBean.setTitle(false);
					ContactsFragment.getBuddyList().add(buddyInformationBean);

				}
			}
			ContactsFragment.getInstance(context).SortList();

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			e.printStackTrace();
		}

	}

	public void notifyBuddyRequestSent(final Object response) {
		try {
			buddyName = "";
			if (response instanceof Servicebean) {
				BuddyInformationBean tempBuddy = null;
				Servicebean servicebean = (Servicebean) response;
				if (servicebean.getObj() instanceof ArrayList) {
					ArrayList<BuddyInformationBean> list = (ArrayList<BuddyInformationBean>) servicebean
							.getObj();
					for (int i = 0; i < list.size(); i++) {
						final Object obj = list.get(i);
						if (obj instanceof BuddyInformationBean) {
							tempBuddy = ((BuddyInformationBean) obj);
							updateBuddyInformation((BuddyInformationBean) obj);
						} else if (obj instanceof WebServiceBean) {
							final WebServiceBean service_bean = (WebServiceBean) obj;
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(context,
											service_bean.getText(), 3000)
											.show();
								}
							});

						}
					}
					if (tempBuddy != null) {
						buddyName = tempBuddy.getName();
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources().getString(R.string.buddy_request_sent_successfully_to1)
											+ buddyName, 3000).show();
							ContactsFragment.getInstance(context).SortList();

						}
					});

				} else if (response instanceof WebServiceBean) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context,
									((WebServiceBean) response).getText(),
									Toast.LENGTH_LONG).show();
						}
					});

				} else if (response instanceof String) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(context, ((String) response),
									Toast.LENGTH_LONG).show();
						}
					});
				}
			}
			ContactsFragment.getInstance(context).SortList();
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void notifyMessageCount() {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					SingleInstance.mainContext.notifyUI();
					if (ContactsFragment.getContactAdapter() != null) {
						ContactsFragment.getContactAdapter()
								.notifyDataSetChanged();

					}
//					updateBatchCount();
				}
			});

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public boolean CreateVideoThumbnail(String strThumbPath) {
		try {
            String tempThumb = strThumbPath;
			MediaPlayer m = new MediaPlayer();
			m.setDataSource(AESFileCrypto.decryptFile(context, strThumbPath + ".mp4"));
			m.prepare();

			long milliseconds = (long) m.getDuration();

			String seconds = WebServiceReferences.setLength2((int) (Math
					.round((double) milliseconds / 1000) % 60));
			String minutes = WebServiceReferences
					.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
			String hours = WebServiceReferences
					.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));

			String asText = hours + ":" + minutes + ":" + seconds;
			Bitmap thumbImage = ThumbnailUtils.createVideoThumbnail(
                    AESFileCrypto.decryptFile(context, strThumbPath + ".mp4"),
					MediaStore.Images.Thumbnails.MINI_KIND);
			if (thumbImage != null) {
				thumbImage = ResizeVideoImage(thumbImage);
				Bitmap thumb = combineImages(thumbImage, asText);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				thumb.compress(CompressFormat.JPEG, 75, bos);
				FileOutputStream fos = new FileOutputStream(strThumbPath
						+ ".jpg");
				bos.writeTo(fos);
				bos.close();
				fos.close();
				thumbImage.recycle();
				thumb.recycle();
				return true;
			} else {
				File fl = new File(AESFileCrypto.decryptFile(context, strThumbPath + ".mp4"));
				if (fl.exists()) {

					Bitmap bmp = BitmapFactory.decodeResource(
							SipNotificationListener.getCurrentContext()
									.getResources(), drawable.broken);
					bmp = ResizeVideoImage(bmp);

					Bitmap thumb1 = combineImages(bmp, asText);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					thumb1.compress(CompressFormat.JPEG, 75, bos);
					FileOutputStream fos = new FileOutputStream(strThumbPath
							+ ".jpg");
					bos.writeTo(fos);
					bos.close();
					fos.close();
					bmp.recycle();
					thumb1.recycle();

					return true;
				} else {

					return false;
				}
			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
			return false;
		}

	}

	private Bitmap ResizeVideoImage(Bitmap bitmap) {

		try {
			int outWidth = bitmap.getWidth();
			int outHeight = bitmap.getHeight();

			if (outWidth == outHeight) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
			} else {

				if (outHeight == 200) {
					bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
							outHeight, true);
				} else {
					double ratio;
					if (outHeight < outWidth) {
						ratio = (double) outWidth / (double) outHeight;

						bitmap = Bitmap.createScaledBitmap(bitmap,
								(int) (200 * ratio), 200, true);
					} else {
						ratio = (double) outWidth / (double) outHeight;

						bitmap = Bitmap.createScaledBitmap(bitmap,
								(int) Math.round(200 / ratio), 200, true);
					}

				}

			}

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
		return bitmap;
	}

	private Bitmap combineImages(Bitmap c, String time) {
		Bitmap cs = null;
		try {

			Bitmap bitmapPlay = BitmapFactory.decodeResource(
					SipNotificationListener.getCurrentContext().getResources(),
					R.drawable.vplay1);
			int width, height = 0;
			width = c.getWidth();
			height = c.getHeight();
			cs = Bitmap.createBitmap(width, height + 50,
					Bitmap.Config.ARGB_8888);
			Canvas comboImage = new Canvas(cs);
			comboImage.drawColor(0xffffffff);
			comboImage.drawBitmap(c, 0f, 50f, null);
			comboImage
					.drawBitmap(
							bitmapPlay,
							(c.getWidth() / 2 - bitmapPlay.getWidth() / 2),
							(c.getHeight() / 2 - bitmapPlay.getHeight() / 2) + 50,
							null);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setTypeface(Typeface.SERIF);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(30);
			comboImage.drawText(time, (c.getWidth() / 4), 22, paint);

		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
		return cs;
	}

	public boolean isNetworkConnectionAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null)
			return false;
		State network = info.getState();
		return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
	}

	public void chatProgress(ChatFTPBean ftpBean, final int tmp) {
		final GroupChatBean gcBean = (GroupChatBean) ftpBean.getSourceObject();
		final GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
				.get("groupchat");
		Log.i("download123", "percent1 : " + tmp);
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (gcActivity != null) {
						if (gcActivity.chatList != null) {
							for (GroupChatBean chatBean : gcActivity.chatList) {
								if (chatBean.getSignalid().equalsIgnoreCase(
										gcBean.getSignalid())) {
									chatBean.setProgress(tmp);
									Log.i("download123", "percent2 : " + tmp);
									if (tmp == 100) {
										chatBean.setStatus(2);
									}
									gcActivity.adapter.notifyDataSetChanged();
								}
							}
						}
					}
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}

			}
		});

	}

	private void updateUploadDownloadUI(final GroupChatBean gcBean,
			final int status) {
		if (CallDispatcher.LoginUser != null) {
			final GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
					.get("groupchat");

			if (gcActivity != null) {
				handler.post(new Runnable() {

					@Override
					public void run() {

						try {
							for (int i = 0; i < gcActivity.chatList.size(); i++) {
								GroupChatBean chatBean = gcActivity.chatList
										.get(i);
								if (chatBean.getSignalid().equalsIgnoreCase(
										gcBean.getSignalid())) {
									Log.i("gchat1234", "signal id : "
											+ chatBean.getSignalid()
											+ "status : " + status);
									chatBean.setStatus(status);
									if (gcBean.getSubCategory() != null
											&& gcBean.getSubCategory().length() > 0
											&& gcBean.getSubCategory()
													.equalsIgnoreCase("GRB")) {
										gcActivity.lv.setSelection(i);
										break;
									}
								}

							}
							gcActivity.adapter.notifyDataSetChanged();
						} catch (Exception e) {
							SingleInstance.printLog(null, e.getMessage(), null,
									e);
						}

					}
				});

			} else {
				Log.i("group123",
						"upload status : groupid : " + gcBean.getGroupId());
				String groupOrBuddyName = "";
				if (gcBean.getCategory().equalsIgnoreCase("G")) {
					groupOrBuddyName = gcBean.getGroupId();
				} else {
					groupOrBuddyName = gcBean.getGroupId().replace(
							CallDispatcher.LoginUser, "");
				}
				Vector<GroupChatBean> gcList = SingleInstance.groupChatHistory
						.get(groupOrBuddyName);
				if (gcList != null) {
					for (int i = 0; i < gcList.size(); i++) {
						GroupChatBean groupChatBean = gcList.get(i);
						if (groupChatBean.getSignalid().equalsIgnoreCase(
								gcBean.getSignalid())) {
							Log.i("group123",
									"upload status : inside if condition : "
											+ status);
							groupChatBean.setStatus(status);
							gcList.remove(i);
							gcList.add(i, groupChatBean);
							SingleInstance.groupChatHistory
									.remove(groupOrBuddyName);
							SingleInstance.groupChatHistory.put(
									groupOrBuddyName, gcList);
							break;
						}
					}
				}

			}
		}
	}

	public void insertOrUpdateUploadOrDownload(ChatFTPBean chatFTPBean,
			String status, String modules) {
		try {
			UploadDownloadStatusBean uploadDownloadStatusBean = new UploadDownloadStatusBean();
			uploadDownloadStatusBean.setFilename(chatFTPBean.getInputFile());
			uploadDownloadStatusBean.setUsername(CallDispatcher.LoginUser);
			uploadDownloadStatusBean.setFtpusername(chatFTPBean.getUsername());
			uploadDownloadStatusBean.setFtppassword(chatFTPBean.getPassword());
			uploadDownloadStatusBean.setStatus(status);
			uploadDownloadStatusBean.setOperation(chatFTPBean.getOperation());
			uploadDownloadStatusBean.setModules(modules);
			if (modules.equalsIgnoreCase("groupchat")) {
				GroupChatBean gcBean = (GroupChatBean) chatFTPBean
						.getSourceObject();
				uploadDownloadStatusBean.setOthers(gcBean.getSignalid());
			}
			DBAccess.getdbHeler().insertOrUpdateUploadDownloadStatus(
					uploadDownloadStatusBean);
		} catch (Exception e) {
			SingleInstance.printLog(null, e.getMessage(), null, e);
		}
	}

	public void missedDownloads(String username) {
		try {
			Vector<UploadDownloadStatusBean> uploadDownloadList = DBAccess
					.getdbHeler().getUploadDownloadList(username);
			if (cBean != null) {
				Log.i("missed123", "inside if condition");
				for (UploadDownloadStatusBean uploadDownloadStatusBean : uploadDownloadList) {
					Log.i("missed123", "inside for loop filename : "
							+ uploadDownloadStatusBean.getFilename());
					ChatFTPBean chatFTPBean = new ChatFTPBean();
					chatFTPBean.setServerIp(cBean.getRouter().split(":")[0]);
					chatFTPBean.setServerPort(40400);
					chatFTPBean.setUsername(uploadDownloadStatusBean
							.getFtpusername());
					chatFTPBean.setPassword(uploadDownloadStatusBean
							.getFtppassword());
					chatFTPBean.setInputFile(uploadDownloadStatusBean
							.getFilename());
					chatFTPBean.setOutputFile(Utils
							.getFilePathString(uploadDownloadStatusBean
									.getFilename()));
					chatFTPBean.setOperation(uploadDownloadStatusBean
							.getOperation());
					GroupChatBean gcBean = null;
					if (uploadDownloadStatusBean.getModules().equalsIgnoreCase(
							"groupchat")) {
						Log.i("missed123",
								"inside for groupchat if condition signal id : "
										+ uploadDownloadStatusBean.getOthers());
						gcBean = DBAccess.getdbHeler().getGroupChatBean(
								uploadDownloadStatusBean.getOthers());
						chatFTPBean.setSourceObject(gcBean);
					}
					File file = new File(chatFTPBean.getOutputFile());
					if (file.exists()) {
						file.delete();
					}
					chatFTPBean.setCallback(AppMainActivity.this);
					if (gcBean != null) {
						insertOrUpdateUploadOrDownload(chatFTPBean, "0",
								"groupchat");
						Log.i("missed123", "before calling processrequest : "
								+ chatFTPBean.getInputFile());
						FTPPoolManager.processRequest(chatFTPBean);
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PendingIntent triggerMissedDownloads() {
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MissedDownloadReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 60 * 250 * 2, alarmIntent);
		return alarmIntent;
	}

	public void changeLoginButton() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					if (CallDispatcher.LoginUser != null) {
//						imageLoader.DisplayImage("", iv_LoginLogout,
//								R.drawable.logout_slide);
					} else {
//						imageLoader.DisplayImage("", iv_LoginLogout,
//								R.drawable.login_slide);
					}
				} catch (Exception e) {
					SingleInstance.printLog(null, e.getMessage(), null, e);
				}
			}
		});

	}

	public GroupChatPermissionBean getGroupChatPermission(GroupBean gBean) {
		try {
			GroupChatPermissionBean gcpBean = DBAccess.getdbHeler()
					.selectPermissionsForGroup(
							"select * from setgrouppermission where userid='"
									+ CallDispatcher.LoginUser
									+ "' and groupid='" + gBean.getGroupId()
									+ "' and groupmember='"
									+ CallDispatcher.LoginUser + "'",
							CallDispatcher.LoginUser, gBean.getGroupId(),
							gBean.getOwnerName(), CallDispatcher.LoginUser);
			return gcpBean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	// private void loadExchanges() {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// DBAccess.getdbHeler().deleteOldGroups();
	// WebServiceReferences.webServiceClient.getGroupAndMembers(
	// CallDispatcher.LoginUser, "",
	// SingleInstance.mainContext);
	// WebServiceReferences.webServiceClient.getParticipateGroups(
	// CallDispatcher.LoginUser, "",
	// SingleInstance.mainContext);
	// // loadOtherInfo();
	//
	// }
	// });
	// }

	public void notifyMyGroupList(Object obj) {

		try {
			// TODO Auto-generated method stub

			if (obj instanceof Vector) {
				Vector<GroupBean> gBeanList = (Vector<GroupBean>) obj;
				for (GroupBean groupBean : gBeanList) {
					groupBean.setUserName(CallDispatcher.LoginUser);
					DBAccess.getdbHeler(context).saveOrUpdateGroup(groupBean);
					DBAccess.getdbHeler(context).insertorUpdateGroupMembers(
							groupBean);
				}
                Log.d("AAA", "Inside NotifyGroupList ");

				GroupActivity.getAllGroups();
				ContactsFragment.getGroupList().clear();
				synchronized (ContactsFragment.getGroupList()) {
					ContactsFragment.getGroupList().addAll(GroupActivity.groupList);
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						GroupActivity.groupAdapter.notifyDataSetChanged();
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void notifyRoundingGroupList(Object obj) {

		try {
			// TODO Auto-generated method stub

			if (obj instanceof Vector) {
				Vector<GroupBean> gBeanList = (Vector<GroupBean>) obj;
				for (GroupBean groupBean : gBeanList) {
					groupBean.setUserName(CallDispatcher.LoginUser);
					DBAccess.getdbHeler(context).saveOrUpdateGroup(groupBean);
					DBAccess.getdbHeler(context).insertorUpdateGroupMembers(
							groupBean);
				}
				Log.d("AAA","Inside notifyRoundingGroupList ");

				RoundingGroupActivity.getallRoundingGroups();
				RoundingFragment.getRoundingList().clear();
				synchronized (RoundingFragment.getRoundingList()) {
					RoundingFragment.getRoundingList().addAll(RoundingGroupActivity.RoundingList);
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						RoundingFragment.newInstance(context).getList();
					}
				});
			}else if(obj instanceof WebServiceBean){
				RoundingFragment.isEmptyList=true;
				Log.d("AAA","Inside notifyRoundingGroupList webservice ");
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						RoundingFragment.newInstance(context).getList();
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyBuddyGroupList(Object obj) {

		try {
			// TODO Auto-generated method stub
			if (obj instanceof Vector) {
                Log.d("AAA","Inside notifyBuddyGroupList ");
				Vector<GroupBean> gBeanList = (Vector<GroupBean>) obj;
				for (GroupBean groupBean : gBeanList) {
					groupBean.setUserName(CallDispatcher.LoginUser);
					groupBean
							.setActiveGroupMembers(groupBean.getGroupMembers());
					DBAccess.getdbHeler(context).saveOrUpdateGroup(groupBean);
					DBAccess.getdbHeler(context).insertorUpdateGroupMembers(
							groupBean);
					// tempMsgCount.add(groupBean.getGroupId());
				}
			}
			GroupActivity.getAllGroups();
			ContactsFragment.getGroupList().clear();
			//	Collections.sort(GroupActivity.ownerGroupList, new GroupListComparator());
			synchronized (ContactsFragment.getGroupList()) {
				ContactsFragment.getGroupList().addAll(GroupActivity.groupList);
			}
//			ContactsFragment.getBuddyGroupList().clear();
		//	Collections.sort(GroupActivity.buddyGroupList, new GroupListComparator());
//			synchronized (ContactsFragment.getBuddyGroupList()) {
//				ContactsFragment.getBuddyGroupList().addAll(GroupActivity.buddyGroupList);
//			}
//			GroupActivity.groupAdapter2 = new GroupAdapter2(context,
//					R.layout.grouplist, GroupActivity.buddyGroupList);

			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
//					final ContactsFragment contactsFragment = ContactsFragment
//							.getInstance(context);
//					if (contactsFragment.isPendingshowing) {
//						contactsFragment.lv
//								.setAdapter(GroupActivity.groupAdapter);
						GroupActivity.groupAdapter.notifyDataSetChanged();
//					}
				}
			});
			// getUnreadCountGroupChatAfterLogin(tempMsgCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// private void getUnreadCountGroupChatAfterLogin(
	// ArrayList<String> tempMsgCount) {
	// Iterator<Map.Entry<String, Integer>> iter = SingleInstance.unreadCount
	// .entrySet().iterator();
	// while (iter.hasNext()) {
	// Map.Entry<String, Integer> entry = iter.next();
	// for (String temp : tempMsgCount) {
	// if (temp.equalsIgnoreCase(entry.getKey())) {
	// int value = entry.getValue();
	// SingleInstance.unreadCount.remove(temp);
	// SingleInstance.unreadCount.put(temp, value);
	// }
	// }
	// }
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// adapter.notifyDataSetChanged();
	// }
	// });
	//
	// }

	private void deadLineReplyDialog(final GroupChatBean gcBean1,
			final GroupBean gBean) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Context context = null;
				GroupChatActivity gcActivity = (GroupChatActivity) SingleInstance.contextTable
						.get("groupchat");
				if (gcActivity != null) {
					context = gcActivity;
				} else {
					context = SingleInstance.mainContext;
				}
				AlertDialog.Builder alert_builder = new AlertDialog.Builder(
						context);
				alert_builder.setTitle("DeadLine Message");
				CharSequence[] b_type = { "Done", "Not Done", "Cancel" };
				alert_builder.setSingleChoiceItems(b_type, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int pos) {
								// TODO Auto-generated method stub
								SpecialMessageBean spBean = new SpecialMessageBean();
								spBean.setSubcategory("GDI");
								spBean.setParentId(gcBean1.getParentId());
								spBean.setMembers(gcBean1.getFrom());
								if (pos == 0) {
									sendMsg(CallDispatcher.LoginUser
											+ " completed the task.", "",
											"text", spBean, gBean);
									dialog.dismiss();
								} else if (pos == 1) {
									sendMsg(CallDispatcher.LoginUser
											+ " not yet complete the task.",
											"", "text", spBean, gBean);
									dialog.dismiss();
								} else if (pos == 2) {
									sendMsg(CallDispatcher.LoginUser
											+ " cancel the task.", "", "text",
											spBean, gBean);
									dialog.dismiss();

								}
							}
						});
				alert_builder.show();
			}
		});

	}

	private void sendMsg(String message, String localpath, String type,
			SpecialMessageBean spBean, GroupBean groupBean) {

		try {
			if (CallDispatcher.LoginUser != null) {
				Log.i("group0123", "message1 : " + message);
				GroupChatBean gcBean = new GroupChatBean();
				gcBean.setFrom(CallDispatcher.LoginUser);
				gcBean.setType("100");
				gcBean.setMimetype(type);
				if (type.equals("text")) {
					gcBean.setMessage(message);
					Log.i("group0123", "message11 : " + gcBean.getMessage());
				}
				gcBean.setSignalid(Utility.getSessionID());
				gcBean.setSenttime(callDisp.getCurrentDateandTime());
				gcBean.setSenttimez("GMT");
				gcBean.setTo(groupBean.getGroupId());
				gcBean.setSessionid(groupBean.getGroupId());
				gcBean.setGroupId(groupBean.getGroupId());
				gcBean.setCategory("G");
				if (spBean != null) {
					if (spBean.getSubcategory() != null) {
						gcBean.setSubCategory(spBean.getSubcategory());
						if (spBean.getMembers() != null) {
							gcBean.setPrivateMembers(spBean.getMembers());
						}
						if (spBean.getSubcategory().equalsIgnoreCase("gs")) {
							gcBean.setReminderTime(spBean.getRemindertime());
						} else if (spBean.getSubcategory().equalsIgnoreCase(
								"gdi")
								|| spBean.getSubcategory().equalsIgnoreCase(
										"gd")
								|| (spBean.getSubcategory()
										.equalsIgnoreCase("grb"))) {
							gcBean.setReminderTime(spBean.getRemindertime());
							if (spBean.getParentId() != null) {
								gcBean.setParentId(spBean.getParentId());
							} else
								gcBean.setParentId(Utility.getSessionID());
						}
					}
				}
				if (type.equals("text")) {
					Log.i("group0123", "message2 : " + gcBean.getMessage());
					SingleInstance.getGroupChatProcesser().getQueue()
							.addObject(gcBean);
				}
				Vector<GroupChatBean> chatList = SingleInstance.groupChatHistory
						.get(gcBean.getGroupId());
				if (gcBean.getSubCategory() != null) {
					if (gcBean.getSubCategory().equalsIgnoreCase("gdi")) {
						if (gcBean.getParentId() != null
								&& gcBean.getParentId().length() > 0
								&& !gcBean.getParentId().equalsIgnoreCase(
										"null")) {
							for (int i = 0; i < chatList.size(); i++) {
								GroupChatBean gcBean1 = chatList.get(i);
								if (gcBean1 != null
										&& gcBean1.getParentId() != null
										&& gcBean1.getParentId().equals(
												gcBean.getParentId())) {
									String msg = gcBean1.getMessage()
											+ "\nStatus : "
											+ gcBean.getMessage();
									gcBean1.setMessage(msg);
									break;
								}
							}
						} else
							chatList.add(gcBean);
					} else {
						chatList.add(gcBean);
					}
				} else {
					chatList.add(gcBean);
				}
				if (SingleInstance.groupChatHistory.containsKey(gcBean
						.getGroupId())) {
					SingleInstance.groupChatHistory.remove(gcBean.getGroupId());
					SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
							chatList);
				} else {
					SingleInstance.groupChatHistory.put(gcBean.getGroupId(),
							chatList);
				}
			} else {
				showToast("Kindly login to send message");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

	}

	public void notifyViewProfile(Servicebean servicebean) {

		// TODO Auto-generated method stub
		if (servicebean.getObj() instanceof ArrayList) {
			Log.i("BackGround", "notifyProfileValues");

			ArrayList<Object> resut = (ArrayList<Object>) servicebean.getObj();
			String[] profile_info = new String[4];
			profile_info = (String[]) resut.get(0);
			String profileOwner = profile_info[1];
			if (resut.size() > 1) {
				ArrayList<FieldTemplateBean> filed_values = (ArrayList<FieldTemplateBean>) resut
						.get(1);
				for (FieldTemplateBean fieldTemplateBean : filed_values) {

					ContentValues cv = new ContentValues();
					if (fieldTemplateBean.getFiledvalue() != null
							&& !fieldTemplateBean.getFiledvalue()
									.equalsIgnoreCase("null")
							&& fieldTemplateBean.getFieldId() != null) {

						cv.put("fieldid", fieldTemplateBean.getFieldId());
						cv.put("modifieddate",
								fieldTemplateBean.getField_modifieddate());
						cv.put("createddate",
								fieldTemplateBean.getField_createddate());
						cv.put("userid", profileOwner);

						cv.put("fieldvalue", fieldTemplateBean.getFiledvalue());
						HashMap<String, String> multimediaFieldsValues = BGProcessor
								.getInstance().multimediaFieldsValues;
						if (multimediaFieldsValues.size() > 0)
							multimediaFieldsValues.clear();

						multimediaFieldsValues = DBAccess
								.getdbHeler(SingleInstance.mainContext)
								.getMultimediaFieldValues(
										"SELECT fieldid,fieldvalue FROM profilefieldvalues WHERE fieldid IN (SELECT fieldid FROM fieldtemplate WHERE fieldtype = 'Audio' OR fieldtype = 'Video' OR fieldtype = 'Photo') and userid = '"
												+ profileOwner + "'");
						Log.i("profile", "====> get profile multimedia");
						if (multimediaFieldsValues != null
								&& multimediaFieldsValues.size() > 0) {
							Log.i("profile",
									"====> inside get profile multimedia");
							if (multimediaFieldsValues
									.containsKey(fieldTemplateBean.getFieldId())) {
								String fieldValue = multimediaFieldsValues
										.get(fieldTemplateBean.getFieldId());
								if (!fieldValue
										.equalsIgnoreCase(fieldTemplateBean
												.getFiledvalue())) {
									Log.i("profile",
											"====> inside get profile multimedia file exists");
									File file = new File(Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/COMMedia/" + fieldValue);
									if (file.exists()) {
										Log.i("profile",
												"====> inside get profile multimedia file delete");
										file.delete();
									}
								}
							}
						}

						String profile_multimedia = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ fieldTemplateBean.getFiledvalue();
						File msg_file = new File(profile_multimedia);
						String f_type = DBAccess.getdbHeler(
								SingleInstance.mainContext).getFieldType(
								fieldTemplateBean.getFieldId());

						if (msg_file.exists())
							cv.put("status", 2);
						else {

							if (f_type != null
									&& f_type.equalsIgnoreCase("photo")
									|| f_type.equalsIgnoreCase("video")
									|| f_type.equalsIgnoreCase("audio")
									|| f_type.equalsIgnoreCase("multimedia"))
								cv.put("status", 0);
							else
								cv.put("status", 2);

						}
						boolean isUpdated = false;

						if (!DBAccess.getdbHeler(SingleInstance.mainContext)
								.isRecordExists(
										"select * from profilefieldvalues where fieldid="
												+ fieldTemplateBean
														.getFieldId()
												+ " and userid='"
												+ profileOwner + "'")) {

							DBAccess.getdbHeler(SingleInstance.mainContext)
									.insertProfileFieldValues(cv);

						} else {
							cv.remove("fieldid");
							DBAccess.getdbHeler(SingleInstance.mainContext)
									.updateProfileFieldValues(
											cv,
											"fieldid="
													+ fieldTemplateBean
															.getFieldId()
													+ " and userid='"
													+ profileOwner + "'");

						}

						if (fieldTemplateBean.getFieldId().equals("3"))
							isUpdated = true;

						if (!msg_file.exists()) {

							if (f_type != null) {
								if (f_type.equalsIgnoreCase("photo")
										|| f_type.equalsIgnoreCase("video")
										|| f_type.equalsIgnoreCase("audio")
										|| f_type
												.equalsIgnoreCase("multimedia")) {
									Log.d("profile", "Field value is :"
											+ fieldTemplateBean.getFiledvalue());
									if (fieldTemplateBean.getFieldId().equals(
											"3")) {

										for (BuddyInformationBean temp : ContactsFragment
												.getBuddyList()) {
											if (!temp.isTitle()) {
												if (temp.getName()
														.equalsIgnoreCase(
																fieldTemplateBean
																		.getUsername())) {

													temp.setProfile_picpath(fieldTemplateBean
															.getFiledvalue());
													break;
												}
											}
										}

									}
									BGProcessor
											.getInstance()
											.downloadOfflineresponse(
													fieldTemplateBean
															.getFiledvalue(),
													fieldTemplateBean
															.getFieldId()
															+ ","
															+ profileOwner
															+ "," + isUpdated,
													"profile field", null);
								}

							}
						} else {
							// comparewithStateanddistance();
							// profilePictureDownloaded();
						}

						msg_file = null;
					}

				}
			}

		} else if (servicebean.getObj() instanceof WebServiceBean) {
			Log.d("Profile", "Error message is --->"
					+ ((WebServiceBean) servicebean.getObj()).getText());
			ContactsFragment contactsFragment = (ContactsFragment) SingleInstance.instanceTable
					.get("contactspage");
			if (contactsFragment != null) {
				contactsFragment.cancelDialog();
			}
		}

		handler.post(new Runnable() {

			@Override
			public void run() {
				GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable
						.get("groupchat");
				if (groupChatActivity != null) {
					groupChatActivity.notifyViewProfile();
				}
				if (CallDispatcher.profileRequested = true) {
					ContactsFragment.getInstance(context).notifyViewProfile(
							true);
				}
			}
		});

	}

	public static String getFileName() {
		String strFilename = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
			Date date = new Date();
			strFilename = dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return strFilename;
	}

	public void notifyUI() {
		handler.post(new Runnable() {
			@Override
			public void run() {
                int dashCount = 0;
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadMsgCount(CallDispatcher.LoginUser);
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadFileCount(CallDispatcher.LoginUser);
                dashCount += DBAccess.getdbHeler(context)
                        .getUnreadCallCount(CallDispatcher.LoginUser);
                TextView dash_count = (TextView) findViewById(R.id.dash_count);
                if (dashCount > 0) {
                    dash_count.setText(Integer.toString(dashCount));
                    dash_count.setVisibility(View.VISIBLE);
                } else {
                    dash_count.setVisibility(View.GONE);
                }
				ProfileBean bean=SingleInstance.myAccountBean;
                TextView userName = (TextView) findViewById(R.id.userName);
				if(bean.getFirstname()!=null && bean.getLastname()!=null)
                	userName.setText(bean.getFirstname()+" "+bean.getLastname());
				else if(bean.getNickname()!=null)
					userName.setText(bean.getNickname());
				else
					userName.setText(CallDispatcher.LoginUser);
                String status_1 = loadCurrentStatus();
                TextView status = (TextView) findViewById(R.id.status);
                ImageView img_status = (ImageView) findViewById(R.id.img_status);
                if(status_1.equalsIgnoreCase("online")){
                    status.setText("Online");
                    img_status.setBackgroundResource(drawable.online_icon);
                }else if(status_1.equalsIgnoreCase("away")){
                    status.setText("Invisible");
                    img_status.setBackgroundResource(drawable.invisibleicon);
                }else if(status_1.equalsIgnoreCase("busy")){
                    status.setText("Busy");
                    img_status.setBackgroundResource(drawable.busy_icon);
                }else{
                    status.setText("Offline");
                    img_status.setBackgroundResource(drawable.offline_icon);
                }
                setProfilePic();
			}
		});
	}

	public void notifyGetGroupChatSettings(Object obj) {
		try {
			if (obj instanceof Vector) {
				Vector<GroupChatPermissionBean> gcpList = (Vector<GroupChatPermissionBean>) obj;
				for (GroupChatPermissionBean gcpBean : gcpList) {
					gcpBean.setUserId(CallDispatcher.LoginUser);
					DBAccess.getdbHeler().insertorUpdateGroupChatSettings(
							gcpBean);
				}
			} else if (obj instanceof WebServiceBean) {
				WebServiceBean server_response = (WebServiceBean) obj;
				if (server_response.getText() != null) {
					showToast(server_response.getText());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isAutoAcceptEnabled(String ownerName, String username) {
		String query = "select * from autoacceptcalls where owner='"
				+ ownerName + "' and username='" + username + "' and flag='1'";
		return DBAccess.getdbHeler().isRecordExists(query);
	}

	public void notifyQuickAction(Object obj) {
		// TODO Auto-generated method stub
		// if (SingleInstance.contextTable
		// .containsKey("qaction")) {
		QuickActionFragment settingContext = QuickActionFragment
				.newInstance(SingleInstance.mainContext);
		settingContext.notifyWebServiceResponse(obj);
		// }
		// ((QuickActionFragment) )
		// .notifyWebServiceResponse(mServicebean
		// .getObj());

	}

	public void notifyQuickActiondata(Object object) {
		// TODO Auto-generated method stub

		// if (SingleInstance.contextTable
		// .containsKey("qaction")) {
		QuickActionFragment settingContext = QuickActionFragment
				.newInstance(SingleInstance.mainContext);
		settingContext.notifyWebServiceResponse1(object);
		// }

	}

	public void resetMenuList(BuddyInformationBean bibBean) {
		try {
            Log.i("AAA", "resetMenuList AppMainActivity");

//			lvMenuItems = getResources().getStringArray(R.array.menu_items);
//			menuIcons = getResources().obtainTypedArray(R.array.icons);
//			List<String> templvList = Arrays.asList(lvMenuItems);
//			ArrayList<String> lvList = new ArrayList<String>(templvList);
//			for (int i = 0; i < lvMenuItems.length; i++) {
//				RowItem items = new RowItem(lvMenuItems[i],
//						menuIcons.getResourceId(i, -1));
//			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String[] removeItemFromListMenuArray(ArrayList<String> list,
			int position) {
        list.remove(position);
		String[] obj = null;
		obj = list.toArray(new String[list.size()]);
		return obj;
	}

	public String getCallDuration(String startTime, String endTime) {
        Log.i("callduration123", "duration : ");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
			Date d1 = null;
			Date d2 = null;
			d1 = sdf.parse(startTime);
			d2 = sdf.parse(endTime);
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			Log.i("callduration123",
					"duration : " + String.valueOf(diffHours) + ":"
							+ String.valueOf(diffMinutes) + ":"
							+ String.valueOf(diffSeconds));
			return String.valueOf(diffHours) + ":"
					+ String.valueOf(diffMinutes) + ":"
					+ String.valueOf(diffSeconds);
			// return "0:"
			// + String.valueOf(diffMinutes) + ":"
			// + String.valueOf(diffSeconds);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void ModuleNameChange() {
		handler.post(new Runnable() {

		@Override
		public void run() {
			recenttv.setText(getResources().getString(R.string.recents_app));
			tvfiles.setText(getResources().getString(R.string.file_app));
			tvmore.setText(getResources().getString(R.string.settings_app));
			tvcontacts.setText(getResources().getString(R.string.contacts_app));
			appstv.setText(getResources().getString(R.string.apps_av));
		}
	});
}
	private boolean compareDatesByDateMethods(SimpleDateFormat df,
			Date oldDate, Date newDate, String from) {
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
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return false;
		}

	}

	public void exchangeFragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			if (isLoggedIn()) {
				fragments = ExchangesFragment.newInstance(AppMainActivity.this);
				((ExchangesFragment) fragments).setLaunch(false);
			} else
				showToast("Kindly Login");
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("Conversation");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contactFragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			if (isLoggedIn()) {
				SingleInstance.isbcontacts = true;
				fragments = ContactsFragment.getInstance(AppMainActivity.this);
			} else
				showToast("Kindly Login");
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void appFragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			if (isLoggedIn())
				fragments = AppsViewFragment.newInstance(AppMainActivity.this);
			else

				showToast("Kindly Login");
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("Apps");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void settingFragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			fragments = SettingsFragment.newInstance(AppMainActivity.this);
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("Settings");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fileFragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			SingleInstance.myOrder = false;
			fragments = FilesFragment.newInstance(AppMainActivity.this);
			((FilesFragment) fragments).getUsername(CallDispatcher.LoginUser);
			((FilesFragment) fragments).setFromContacts(false);
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText(getResources().getString(R.string.files));
			}

			updateFileCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void avatarfragment() {
		try {
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			if (isLoggedIn()) {
				// fragment =
				// AvatarFragment.newInstance(AppMainActivity.this);
				fragments = AvatarActivity.newInstance(AppMainActivity.this);
			} else
				showToast("Kindly Login");
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("Answering Machine");
}
	} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void historyfragment() {
		try {
			Log.i("Test", "AppMainActivity ........");
			FragmentManager fm = AppMainActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment fragments = null;
			if (isLoggedIn()) {
				// fragment =
				// AvatarFragment.newInstance(AppMainActivity.this);
				fragments = CallHistoryFragment.newInstance(AppMainActivity.this);
			} else
				showToast("Kindly Login");
			if (fragments != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragments);
				ft.commit();

				// Set title accordingly
				tvTitle.setText("Call History");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLogWriterForCall(String message) {

		try {
			File logFile = new File(Environment.getExternalStorageDirectory()
					+ "/COMMedia/UDPlog.txt");
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileOutputStream fw = new FileOutputStream(logFile, true);
			PrintStream logWriter = new PrintStream(fw, true);
			logWriter.println(getCurrentDateTime() + " :: " + message);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public void showProfile()
	{
		FragmentManager fm = AppMainActivity.this
				.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragments = null;
		if (DBAccess.getdbHeler(context).isRecordExists(
				"select * from profilefieldvalues where userid='"
						+ CallDispatcher.LoginUser + "'")) {
			fragments = ViewProfileFragment
					.newInstance(AppMainActivity.this);
			// // Bundle bundle = new Bundle();
			// // bundle.putString("buddyname",
			// CallDispatcher.LoginUser);
			// fragment.setArguments(bundle);
			SingleInstance.profileView = true;
			((ViewProfileFragment) fragments)
					.setBuddyName(CallDispatcher.LoginUser);

			OtherDetails = callDisp.getdbHeler(context)
					.getProfileFieldsValues(
							CallDispatcher.LoginUser);

			((ViewProfileFragment) fragments).initView();

		} else {
			fragments = CreateProfileFragment
					.newInstance(AppMainActivity.this);
		}
		if (fragments != null) {
			// Replace current fragment by this new one
			ft.replace(R.id.activity_main_content_fragment, fragments);
			ft.commit();

			// Set title accordingly

		}
	}

	public void killApp(int pid) {
		Toast.makeText(context, "Cannot Access", Toast.LENGTH_SHORT).show();
		// android.os.Process.killProcess(pid);
		Log.i("locker123", "killApp inside appmain_________# ");
		Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
		startHomescreen.addCategory(Intent.CATEGORY_HOME);
		startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// startHomescreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(startHomescreen);
	}
	public void feedback()
	{
		if (isLoggedIn()) {
			File file = new File(
					Environment.getExternalStorageDirectory()
							+ "/COMMedia/log.txt");
			if (file.exists()) {
				String content = "Username : "
						+ CallDispatcher.LoginUser
						+ "\n OS Details : Android \n Version : "
						+ android.os.Build.VERSION.RELEASE
						+ " \n Make : "
						+ android.os.Build.BRAND
						+ "\n Model : "
						+ android.os.Build.MODEL
						+ "\n Application Release Version : "
						+ SingleInstance.mainContext.getResources().getString(
								R.string.app_version);
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND_MULTIPLE);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						context.getResources().getString(R.string.app_name)
								+ " Log");
				ArrayList<Uri> uris = new ArrayList<Uri>();
				String[] filePaths = new String[] {Environment.getExternalStorageDirectory()
						+ "/COMMedia/log.txt", Environment.getExternalStorageDirectory()
						+ "/COMMedia/UDPlog.txt"};
				for (String file1 : filePaths)
				{
					File fileIn = new File(file1);
					Uri u = Uri.fromFile(fileIn);
					uris.add(u);
				}
				emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
				startActivity(Intent.createChooser(emailIntent, "Send mail"));
//				emailIntent.putExtra(
//						android.content.Intent.EXTRA_STREAM,
//						Uri.parse("file:// "
//								+ Environment.getExternalStorageDirectory()
//								+ "/COMMedia/log.txt"));
//				emailIntent
//						.putExtra(android.content.Intent.EXTRA_TEXT, content);
//				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			} else
				showToast("Sorry, no log file to send");
		} else
			showToast("Please login to send feedback");
	}

	public void initialize() {
		Log.i("locker123", "killApp inside initialize appmain_________# ");

		// Start receiver with the name StartupReceiver_Manual_Start
		// Check AndroidManifest.xml file
		getBaseContext().getApplicationContext().sendBroadcast(
				new Intent("StartupReceiver_Manual_Start"));

	}

	public void updateBatchCount() {
		// patchbutton.setText(DBAccess.getdbHeler(AppMainActivity.this)
		// .getUnreadMsgCount(CallDispatcher.LoginUser));

		Log.d("XYZ",
				"Unread Count : "
						+ DBAccess.getdbHeler(AppMainActivity.this)
								.getUnreadMsgCount(CallDispatcher.LoginUser));
		int count = DBAccess.getdbHeler(AppMainActivity.this)
				.getUnreadMsgCount(CallDispatcher.LoginUser);

//		if (count > 0) {
//			patchbutton.setText("" + count);
//
//			patchbutton.setVisibility(View.VISIBLE);
//
//		} else {
//
//			patchbutton.setVisibility(View.GONE);
//
//		}
//
//		patchbutton.setText(""
//				+ DBAccess.getdbHeler(AppMainActivity.this).getUnreadMsgCount(
//						CallDispatcher.LoginUser));

	}

	public void updateFileCount() {
		Log.d("XYZ",
				"Unread Count : "
						+ DBAccess.getdbHeler(AppMainActivity.this)
								.getUnreadnotesSize(CallDispatcher.LoginUser));

		int count = DBAccess.getdbHeler(AppMainActivity.this)
				.getUnreadnotesSize(CallDispatcher.LoginUser);

//		if (count > 0) {
//			filepatchbtn.setText("" + count);
//
//			filepatchbtn.setVisibility(View.VISIBLE);
//
//		} else {
//
//			filepatchbtn.setVisibility(View.GONE);
//		}
//
//		filepatchbtn.setText(""
//				+ DBAccess.getdbHeler(AppMainActivity.this).getUnreadnotesSize(
//						CallDispatcher.LoginUser));
	}

    @Override
	public void didReceiveOnCallState(String remoteinfo, String sessionid,
			int call_id, int call_status) {
		// TODO Auto-generated method stub
		Log.i("incoming", "Appmain didReceiveOnCallState : call_id :" + call_id);
		if (SingleInstance.contextTable.containsKey("sipcallscreen")) {
			SipCallConnectingScreen callConnectingScreen = (SipCallConnectingScreen) SingleInstance.contextTable
					.get("sipcallscreen");
			callConnectingScreen.didReceiveOnCallState(remoteinfo, sessionid,
					call_id, call_status);
		}
	}

	@Override
	public void didReceiveCallConnected(String remoteinfo,
			String call_sessionid, int call_id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didReceiveCallDisconnected(String remoteinfo, String sessionid,
			int call_id, int call_status) {
		// TODO Auto-generated method stub
		Log.i("incoming", "Appmain didReceiveCallDisconnected");
		if (incomingCall_alertDialog != null) {
			if (incomingCall_alertDialog.isShowing()) {
				// CommunicationBean bean = new CommunicationBean();
				// bean.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
				// bean.setCall_id(call_id);
				// AppReferences.sipQueue.addMsg(bean);
				callDisp.stopRingTone();
				incomingCall_alertDialog.dismiss();
			}
		}
		if (SingleInstance.contextTable.containsKey("sipcallscreen")) {
			SipCallConnectingScreen callConnectingScreen = (SipCallConnectingScreen) SingleInstance.contextTable
					.get("sipcallscreen");
			callConnectingScreen.didReceiveCallDisconnected(remoteinfo,
					sessionid, call_id, call_status);
		}
	}

	@Override
	public void notifyRegistration(int acc_id, boolean isStatus,
			boolean isUpdate, int errCode, String errReason, String SipReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didReceiveIncomingCall(final int callid, final String remoteinfo) {
		// TODO Auto-generated method stub

		Log.i("incoming", "AppmainActivity  - = - didReceiveIncomingCall");
		Log.i("incoming", "callid :" + callid + " remoteinfo :" + remoteinfo);
		
		
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
                Log.d("files",inActivity.getClass().toString());
                if(SingleInstance.inCall) {
                    CommunicationBean bean = new CommunicationBean();
                    bean.setCall_id(callid);
                    bean.setOperationType(sip_operation_types.REJECT_CALL);
                    AppReference.sipQueue.addMsg(bean);
                }else {

                    showAlert(remoteinfo, callid, inActivity);
                }
			}
		});

	}

	@Override
	public void on_call_media(int call_id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didReceiveCallReplace(int callid, String sipinfo,
			String fromuser, String rhvalue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didReceiveIncomingJoinCall(int callid, String remoteinfo,
			String sipinfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageReceived(int call_id, String from, String to,
			String contact, String mime_type, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void on_referMessageReceived(int call_id, String remoteinfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMediaState(int call_id, String touser_no, boolean status) {
		// TODO Auto-generated method stub

	}

	public void showAlert(String remoteinfo, final int call_id,
			final Context ctxt) {

		try {
			callDisp.startRingTone();
			toUserName = remoteinfo.substring(remoteinfo.indexOf(":") + 1,
					remoteinfo.indexOf("@"));

			Intent intentmain = new Intent(this, AppMainActivity.class);
			intentmain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			intentmain.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			intentmain.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			intentmain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intentmain);

			KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
			if (keyguardManager.inKeyguardRestrictedInputMode()) {
				// it is locked
				keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
				lock = keyguardManager
						.newKeyguardLock("com.main;");
				lock.disableKeyguard();
			}

			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, "inCommingCallAlert");

			mWakeLock.acquire();

			AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

			am.setStreamVolume(AudioManager.STREAM_MUSIC,
					am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
			
			// Missed Call Alert
			
			AlertDialog.Builder a = new AlertDialog.Builder(ctxt);
			a.setTitle("Missed call alert");
			a.setMessage("You missed a call from "+toUserName);
			a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			missedCallAlert = a;
			
			final Dialog dialog1 = new Dialog(ctxt);
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.setContentView(R.layout.callcustomdialog);
			dialog1.setCancelable(false);
			TextView accept = (TextView) dialog1.findViewById(R.id.tv_accept);
			TextView decline = (TextView) dialog1.findViewById(R.id.tv_cancel);
			TextView tview = (TextView) dialog1.findViewById(R.id.call_alert1);
			tview.setText("" + "\n" + toUserName);
			

			Message msg = new Message();
			Bundle bndl = new Bundle();
			bndl.putInt("callid", call_id);
			msg.setData(bndl);

			ringback_timer.sendMessageDelayed(msg, 30000);

			accept.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("incoming", "accept clicked :  call_id :" + call_id);
					callDisp.stopRingTone();	
					CommunicationBean bean = new CommunicationBean();
					bean.setCall_id(call_id);
					bean.setSendingStatusid("200");
					bean.setOperationType(sip_operation_types.ANSWER_CALL);
					AppReference.sipQueue.addMsg(bean);
					dialog1.dismiss();

				}

			});

			decline.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("incoming", "reject clicked : call_id " + call_id);
					callDisp.stopRingTone();
					CommunicationBean bean = new CommunicationBean();
					bean.setCall_id(call_id);
					bean.setSendingStatusid("486");
					bean.setOperationType(sip_operation_types.REJECT_CALL);
					AppReference.sipQueue.addMsg(bean);
					dialog1.dismiss();
				}
			});
			incomingCall_alertDialog = dialog1;
			dialog1.show();

		} catch (Exception ex) {
			//incomingCall_alertDialog.dismiss();
			ex.printStackTrace();
		}

	}

	public void notifyAcceptReject(int call_id) {
        Log.i("incoming", "notifyAcceptReject");
		Intent callscreen = new Intent(context, SipCallConnectingScreen.class);
		callscreen.putExtra("host", false);
		String name=toUserName.replace("_","@");
		callscreen.putExtra("fromname", name);
		callscreen.putExtra("callid", call_id);
		startActivity(callscreen);

	}

	Handler ringback_timer = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bndl = msg.getData();
			int call_id = bndl.getInt("callid");
			if (incomingCall_alertDialog != null) {
				if (incomingCall_alertDialog.isShowing()) {
					CommunicationBean bean = new CommunicationBean();
					bean.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
					bean.setCall_id(call_id);
					AppReference.sipQueue.addMsg(bean);
					incomingCall_alertDialog.dismiss();
					callDisp.stopRingTone();
					// Missed Call Alert... Alpha SIP
					missedCallAlert.show();
				}
			}
			msg = null;
			bndl = null;
		}

	};

    public void notifySharedFiles(Object obj)
    {
		Log.i("filename", "NOTIFY RESULT IN APPMAINACTIVITY" );
//		filesFragment.deleteFiles();
        if (obj instanceof ArrayList) {
            final ArrayList list = (ArrayList) obj;

            for (final Object lst : list) {
                if (lst instanceof ShareReminder) {
                    ShareReminder sr = (ShareReminder) lst;
                    if (sr.getStatus().equals("new")) {
                        WebServiceReferences.shareRemainderArray
                                .add(sr);
                    }

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Log.i("filename", "shared file size : "
                                    + WebServiceReferences.shareRemainderArray.size());
                            FilesFragment filesFragment = FilesFragment
                                    .newInstance(getApplicationContext());
                            filesFragment.showShareRemainderRequest();
                        }
                    });

                }
            }

        }
    }

	public void notifyGetProfessions(Object obj) {
		try {
			int i = 0;
			Log.i("Test", "WEBSERVICE RESPONSE RESULT: "+(String[])obj);
			if (obj instanceof String[]) {
				String[] temp = (String[]) obj;
												
				profession = Arrays.copyOf(temp, temp.length);																	
					Log.i("Test", "RESULT String" + temp);
			}							
			Log.i("Test", "WEBSERVICE RESPONSE RESULT: "+profession);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    private class BGNotifyGroupLoadWebservice extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean  doInBackground(Object... params) {

            Log.d("AAA", "Appmain notify GroupLoad DoinBAck");

            try {

                // TODO Auto-generated method stub
                    Object obj=params[1];
                if (obj instanceof Vector) {
                    Vector<GroupBean> gBeanList = (Vector<GroupBean>) obj;
                    for (GroupBean groupBean : gBeanList) {
                        groupBean.setUserName(CallDispatcher.LoginUser);
                        groupBean.setOwnerName(CallDispatcher.LoginUser);
                        DBAccess.getdbHeler(context).saveOrUpdateGroup(groupBean);
                        DBAccess.getdbHeler(context).insertorUpdateGroupMembers(
                                groupBean);
                    }
                    Log.d("AAA","Inside NotifyGroupList ");

                    GroupActivity.getAllGroups();
                    GroupActivity.groupAdapter1 = new GroupAdapter1(context,
                            R.layout.grouplist, GroupActivity.groupList);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            GroupActivity.groupAdapter1.notifyDataSetChanged();
                        }
                    });
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }
        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }
        protected void onPostExecute(boolean result) {
            super.onPostExecute(result);
        }
    }
	public void notifyGetAllProfile(Object obj) {
		try {

			if (obj instanceof Vector) {
				Vector<ProfileBean> gcpList = (Vector<ProfileBean>) obj;
				Log.i("AAAA","notifyGetAllProfile "+gcpList.size());
				for(ProfileBean pBean: gcpList) {
					if(pBean.getUsername().equals(CallDispatcher.LoginUser)) {
						SingleInstance.myAccountBean = pBean;
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								MyAccountFragment.newInstance(context).loadFields();
							}
						});
					}
					for(BuddyInformationBean bib:ContactsFragment.getBuddyList()){
						if(bib.getName().equalsIgnoreCase(pBean.getUsername()))
							bib.setProfile_picpath(pBean.getPhoto());
					}
					DBAccess.getdbHeler().insertorupdateProfileDetails(pBean);
					if(!(pBean.getPhoto().equals(null) || pBean.getPhoto().equals(""))) {
						String[] param = new String[3];
						param[0] = CallDispatcher.LoginUser;
						param[1] = CallDispatcher.Password;
						param[2] = pBean.getPhoto();
						WebServiceReferences.webServiceClient.FileDownload(param);
					}
				}
				notifyUI();
			} else if (obj instanceof WebServiceBean) {
				WebServiceBean server_response = (WebServiceBean) obj;
				if (server_response.getText() != null) {
					showToast(server_response.getText());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Bitmap ShrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}
	public void notifyFiledetails(String[] filedetails) {

		try {
			Log.i("check","Inside notifyfiledetails appmain");


			for (int i = 0; i < filedetails.length; i++) {
				Log.d("filevalue123", "data" + filedetails[i]);
			}

			ChatFTPBean chatFTPBean = imFiles.remove(filedetails[0]);
			if(chatFTPBean!=null){
				String directory_path = Environment
						.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/COMMedia/";
				Log.d("decode", "decode : " + directory_path);
				File directory = new File(directory_path);
				if (!directory.exists())
					directory.mkdir();
				directory = null;
				String file_name = directory_path + filedetails[0];
				Log.d("decode12", "decode12 : " + file_name);
				if (file_name.trim().toLowerCase().endsWith("jpg")) {
					Log.d("decode12", "decode12img1 : " + directory_path);
					byte[] imageAsBytes = Base64.decode(filedetails[1], 0);
					File img_file = new File(file_name);
					img_file.createNewFile();
					FileOutputStream image_writter = new FileOutputStream(
							img_file);
					image_writter.write(imageAsBytes);
					image_writter.flush();
					image_writter.close();

					Bitmap bmp = AppMainActivity.this.ShrinkBitmap(
							file_name, 150, 150);
					image_writter = new FileOutputStream(img_file);
					bmp.compress(CompressFormat.JPEG, 75, image_writter);
					bmp.recycle();
					bmp = null;
					image_writter.flush();
					image_writter.close();
					Log.d("decode12", "decode12img1a : " + directory_path);
					// img_file = null;
					if (img_file.exists()) {
						Log.d("decode12", "decode12img1a1 : " + directory_path);
						notifyChatFTPStatus(chatFTPBean, true);
						Log.d("decode12", "decode12img1b : " + directory_path);
					}
				} else {
					if (decodeAudioVideoToBase64(file_name, filedetails[1])) {
						notifyChatFTPStatus(chatFTPBean,true);
					}
				}
				showToast("file downloaded successfully");

			}
			else{
				FTPBean ftpBean = new FTPBean();
				ShareReminder shareReminder=new ShareReminder();
				shareReminder = AppReference.filedownload.get(filedetails[0]);
				Log.d("false123", "false : " + getFileName());


				String directory_path = Environment
						.getExternalStorageDirectory()
						.getAbsolutePath()
						+ "/COMMedia/";
				Log.d("decode", "decode : "+directory_path );
				File directory = new File(directory_path);
				if (!directory.exists())
					directory.mkdir();
				directory = null;
				String file_name = directory_path + filedetails[0];
				Log.d("decode12", "decode12 : " + file_name);
				if(file_name!=null) {
					if (file_name.split(".", 3).equals("jpg")) {
						byte[] imageAsBytes = Base64.decode(filedetails[1], 0);
						File img_file = new File(file_name);
						img_file.createNewFile();
						FileOutputStream image_writter = new FileOutputStream(
								img_file);
						image_writter.write(imageAsBytes);
						image_writter.flush();
						image_writter.close();

						Bitmap bmp = AppMainActivity.this.ShrinkBitmap(
								file_name, 150, 150);
						image_writter = new FileOutputStream(img_file);
						bmp.compress(CompressFormat.JPEG, 75, image_writter);
						bmp.recycle();
						bmp = null;
						image_writter.flush();
						image_writter.close();

						// img_file = null;
						if (img_file.exists()) {
							shareReminder.setFilepath(file_name);
							ftpBean.setReq_object(shareReminder);
							ftpBean.setFile_path(filedetails[0]);
							notifyFileDownloaded("true", ftpBean);
						}
					} else {
						if (decodeAudioVideoToBase64(file_name, filedetails[1])) {
							shareReminder.setFilepath(file_name);
							ftpBean.setReq_object(shareReminder);
							ftpBean.setFile_path(filedetails[0]);
							notifyFileDownloaded("true", ftpBean);
						}
					}
					showToast("file downloaded successfully");
				}

			}
			ContactsFragment.getInstance(context).SortList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public void notifyMySecretQuestion( Object obj)
	{
		cancelDialog();
		if (obj instanceof String[]) {
			String[] result = (String[]) obj;
			Log.i("AAAA", "notifyMySecretQuestion value "+result[0]);
			questions = Arrays.copyOf(result, result.length);
			Intent i = new Intent(context, forgotPassword.class);
			startActivity(i);

		}
	}
	public void showDialog() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					progress = new ProgressDialog(SingleInstance.mainContext);
					if (progress != null) {
						progress.setCancelable(false);
						progress.setMessage("Progress ...");
						progress
								.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progress.setProgress(0);
						progress.setMax(100);
						progress.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}
	public void notifyGetSecurityQuestions(Object obj)
	{
		if(obj instanceof ArrayList){
			ArrayList<String[]> questionsList = (ArrayList<String[]>) obj;
			for(String[] questions:questionsList){
				DBAccess.getdbHeler().insertorUpdateSecurityQuestions(questions);
			}
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("AAAA", "onStop ");
		isApplicationBroughtToBackground();
//		if(isPinEnable) {
//			if (isApplicationBroughtToBackground()) {
//				Intent i = new Intent(AppMainActivity.this, MainActivity.class);
//				startActivity(i);
//			} else {
//				count=0;
//				registerBroadcastReceiver();
//			}
//		}


	}
	public void isApplicationBroughtToBackground() {
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//		if (!tasks.isEmpty()) {
//			ComponentName topActivity = tasks.get(0).topActivity;
//			if (!topActivity.getPackageName().equals(context.getPackageName())) {
//				return true;
//			}
//		}
//		return false;
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
		ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
		activityOnTop = ar.topActivity.toString();
		Log.i("pin", "activityOnTop " + activityOnTop);
		Log.i("pin", "activityOnTop " + context.getPackageName());
		if (!activityOnTop.contains(context.getPackageName())) {
			openPinActivity=true;
		}else {
			openPinActivity = false;
		}

	}
	public void registerBroadcastReceiver() {
		final IntentFilter theFilter = new IntentFilter();
		theFilter.addAction(Intent.ACTION_SCREEN_ON);
		theFilter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver screenOnOffReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String strAction = intent.getAction();
				KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				if (strAction.equals(Intent.ACTION_SCREEN_OFF) || strAction.equals(Intent.ACTION_SCREEN_ON))
				{
					if( myKM.inKeyguardRestrictedInputMode())
					{
						if(count==0) {
							Intent i = new Intent(AppMainActivity.this, PinSecurity.class);
							startActivity(i);
							count++;
						}
					}
				}
			}
		};

		getApplicationContext().registerReceiver(screenOnOffReceiver, theFilter);
	}


    public static void onDrawerItemSelected(int position) {

        try {
            AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
                    .get("MAIN");
            Context context = SingleInstance.mainContext;
            FragmentManager fm = appMainActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment = null;
            appMainActivity.setProfilePic();
            if (appMainActivity.isLoggedIn()) {
                switch(position){
                    case 1:
                        fragment = DashBoardFragment.newInstance(context);
                        break;
                    case 2:
                        fragment = ContactsFragment.getInstance(context);
                        break;
                    case 3:
                        SingleInstance.myOrder = false;
                        fragment = FilesFragment.newInstance(context);
                        ((FilesFragment) fragment).getUsername(CallDispatcher.LoginUser);
                        ((FilesFragment) fragment).setFromContacts(false);
                        break;
                    case 4:
                        fragment = CalendarFragment.newInstance(context);
                        break;
                    case 5:
                        fragment = MyAccountFragment.newInstance(context);
                        break;
                    case 6:
                        fragment = SettingsFragment.newInstance(context);
                        break;
                    case 7:
                        appMainActivity.logout(true);
                        break;
					case 8:
						fragment= RoundingFragment.newInstance(context);
						break;
					case 9:
						fragment = InviteUserFragment.newInstance(context);
                }
            } else
                appMainActivity.showToast("Kindly Login");

            if (fragment != null) {
                ft.replace(R.id.activity_main_content_fragment, fragment);
                ft.commit();
            }

            appMainActivity.close_drawer();

            // Hide menu anyway
//            mainLayout.toggleMenu();
        } catch (Exception e) {
            SingleInstance.printLog(null, e.getMessage(), null, e);
        }
    }

    private void close_drawer(){
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        final LinearLayout menu_side = (LinearLayout) findViewById(R.id.menu_side);
        mDrawerLayout.closeDrawer(menu_side);
    }
	private boolean decodeAudioVideoToBase64(String path, String content){
		File file=new File(path);
		try {
			file.createNewFile();
			Log.d("false123", "false1 : " + getFileName());
			byte[] Bytearray= Base64.decode(content, 100);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(Bytearray);
			fos.close();
			Log.d("false123", "false2 : " + getFileName());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
@Override
    public void tcpMessageReceived(String message) {
        try {
            ProprietarySignalling prop = commEngine.getProprietarySignalling();
            prop.notifyUDPPackets(message.getBytes(), "Via TCP", port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
	public void notifyAcceptRejectGroup(Object obj)
	{
		if (obj instanceof String[]) {
			final String[] result = (String[]) obj;
			if (result[0].equalsIgnoreCase("You have Accepted the group")) {
				SingleInstance.mainContext.showToast(result[0]);
				ContentValues cv = new ContentValues();
				cv.put("invitemembers", CallDispatcher.LoginUser);
				DBAccess.getdbHeler(getApplicationContext())
						.updateGroupMembers(cv, "groupid=" + result[1]);
				Log.i("AAAA", "App main ACCEPT GROUP ");
			} else {
				SingleInstance.mainContext.showToast(result[0]);
				callDisp.getdbHeler(context).deleteGroupAndMembers(result[1]);
				Log.i("AAAA", "App main REJECT GROUP ");
				for (GroupBean gBean : GroupActivity.groupList) {
					if (gBean.getGroupId().equals(result[1])) {
						GroupActivity.groupList.remove(gBean);
						break;
					}
				}
			}
		}
		GroupRequestFragment fragment= GroupRequestFragment.newInstance(SingleInstance.mainContext);
		boolean isFromRounding=fragment.getRequestFrom();
		fragment.cancelDialog();
		if(isFromRounding){
			RoundingFragment roundingFragment = RoundingFragment.newInstance(context);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, roundingFragment)
					.commitAllowingStateLoss();
			roundingFragment.getList();
		}else {
			ContactsFragment contactsFragment = ContactsFragment.getInstance(context);
			FragmentManager fragmentManager = SingleInstance.mainContext
					.getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(
					R.id.activity_main_content_fragment, contactsFragment)
					.commitAllowingStateLoss();
			contactsFragment.getList();
		}

	}
	public void notifygroupDetails(Object obj){
		Log.i("AAAA","notifygroupDetails");
		if(obj instanceof GroupBean){
			GroupBean bean=(GroupBean)obj;
			ContentValues cv = new ContentValues();
			cv.put("active_members", bean.getActiveGroupMembers());
			cv.put("invitemembers", bean.getInviteMembers());
			DBAccess.getdbHeler(getApplicationContext())
					.updateGroupMembers(cv, "groupid=" + bean.getGroupId());
			ContactsFragment.getInstance(SingleInstance.mainContext).getList();
			RoundingFragment.newInstance(SingleInstance.mainContext).getList();
		}
//		else if(obj instanceof String[]){
//			String[] result=(String[])obj;
//			if(result[2].equalsIgnoreCase("0")){
//			}else{
//				ContentValues cv = new ContentValues();
//				cv.put("invitemembers", CallDispatcher.LoginUser);
//				DBAccess.getdbHeler(getApplicationContext())
//						.updateGroupMembers(cv, "groupid=" + result[1]);
//				Log.i("AAAA", "notifygroupDetails accept");
//			}
//		}
	}

    public  void notifySentreceived(GroupChatBean gcBean)
    {
        final GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable
                .get("groupchat");
        if(groupChatActivity.chatList!=null) {
            for (int i = 0; i < groupChatActivity.chatList.size(); i++) {
                GroupChatBean gcBean1 = groupChatActivity.chatList
                        .get(i);
                if (gcBean.getSignalid().equals(gcBean1.getSignalid())) {
                    if (gcBean1.getSent() != null && !gcBean1.getSent().equals("3")) {
						gcBean1.setSent(gcBean.getSent());
					}
						String  mem="";
						DBAccess.getdbHeler(getApplicationContext()).ChatInfo(gcBean.getFrom(), gcBean.getSignalid(),gcBean.getSent(),gcBean.getSenttimez());
                        if (SingleInstance.groupChatHistory.containsKey(gcBean1.getGroupId())) {
                            SingleInstance.groupChatHistory.remove(gcBean1.getGroupId());
                            SingleInstance.groupChatHistory.put(gcBean1.getGroupId(), groupChatActivity.chatList);
                        }
//                    }
                }

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    groupChatActivity.adapter.notifyDataSetChanged();
                }
            });
        }

    }


    public  void notifytextTyping(final GroupChatBean gcBean)
    {
        final GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable
                .get("groupchat");
        if(groupChatActivity!=null) {
            for (int i = 0; i < groupChatActivity.chatList.size(); i++) {
                GroupChatBean gcBean1 = groupChatActivity.chatList
                        .get(i);
                if (groupChatActivity.buddy != null && gcBean.getFrom().equals(groupChatActivity.buddy)) {
//                gcBean1.setTyping(true);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (gcBean.getResult().equals("0")) {
                                groupChatActivity.typingstatus.setVisibility(View.VISIBLE);
                            } else {
                                groupChatActivity.typingstatus.setVisibility(View.GONE);
                            }
                        }
                    });

                }

            }
        }



//        groupChatActivity.chatList;
//        if (groupChatActivity.buddy.equalsIgnoreCase(gcBean
//                .getGroupId())) {
//            ((GroupChatActivity) SingleInstance.contextTable
//                    .get("groupchat")).notifyUI(gcBean);
//        } else {
//            saveChatInBackGround(gcBean);
//        }

    }
    public void ReadMessageAck(UdpMessageBean message) {
        try {
            ProprietarySignalling prop = commEngine.getProprietarySignalling();
            prop.sendAcknowledgement(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
		public void notifyChatTemplate(boolean response,ArrayList<Object> object){
		Log.i("chattemplate","Appmain notifychatTemplate");
		if(response && object!=null){
			if (object instanceof ArrayList) {
				ArrayList list = (ArrayList) object;
				boolean db_delete=false;
				for (int i = 0; i < list.size(); i++) {
					final Object obj = list.get(i);
					if (obj instanceof ChattemplateModifieddate) {
						ChattemplateModifieddate chattemplateModifieddate = (ChattemplateModifieddate) list
								.get(i);
						if(DBAccess.getdbHeler(SingleInstance.mainContext).getChatTemplateModifieddatetime()!=null){
							Log.i("chattemplate", "Appmain notifychatTemplate  modified DB!=null");
							if(chattemplateModifieddate.getModifieddatetime().equalsIgnoreCase(DBAccess.getdbHeler(SingleInstance.mainContext)
									.getChatTemplateModifieddatetime())){
								Log.i("chattemplate","Appmain notifychatTemplate  modified DB!=null date same");
							}else {
								boolean db_deleteModidate = DBAccess.getdbHeler(SingleInstance.mainContext).chatTemplateModifiedDateDelete();
								boolean db_chatTemplate = DBAccess.getdbHeler(SingleInstance.mainContext).chatTemplateDelete();
								if(db_deleteModidate) {
									Log.i("chattemplate","Appmain notifychatTemplate  modified DB!=null delete and new save");
									db_delete=true;
									DBAccess.getdbHeler(SingleInstance.mainContext).insertChatTemplateModifieddate(chattemplateModifieddate);
								}
							}
						}else{
							Log.i("chattemplate","Appmain notifychatTemplate  modified DB==null");
							db_delete=true;
							DBAccess.getdbHeler(SingleInstance.mainContext).insertChatTemplateModifieddate(chattemplateModifieddate);
						}
					} else if (obj instanceof chattemplatebean) {
						chattemplatebean chattemplatebean = (chattemplatebean) list
								.get(i);
						if(db_delete) {
							Log.i("chattemplate","Appmain notifychatTemplate  chat template entry");
							DBAccess.getdbHeler(SingleInstance.mainContext).insertChatTemplate(chattemplatebean);
						}
					}
				}
			}
		}else{
			Log.i("chattemplate","nagative response came");
		}

	}
	public void notifyGetPatientRecords(Object obj){
		if(obj instanceof Vector){
			Log.i("patientdetails","notifyGetPatientRecords");
			Vector<PatientDetailsBean> pBeanList=(Vector<PatientDetailsBean>)obj;
			for(PatientDetailsBean pBean:pBeanList){
				DBAccess.getdbHeler().insertorUpdatePatientDetails(pBean);
			}

		}
	}
	public void notifyGetTaskRecords(Object obj){
		if(obj instanceof Vector){
			Log.i("patientdetails","notifyGetTaskRecords");
			Vector<TaskDetailsBean> pBeanList=(Vector<TaskDetailsBean>)obj;
			for(TaskDetailsBean pBean:pBeanList){
				DBAccess.getdbHeler().insertorUpdatTaskDetails(pBean);
			}

		}
	}
	public void notifyGetPatientDescription(Object obj){
		if(obj instanceof Vector){
			Log.i("patientdetails","notifyGetPatientDescription");
			Vector<PatientDescriptionBean> pBeanList=(Vector<PatientDescriptionBean>)obj;
			for(PatientDescriptionBean pBean:pBeanList){
				DBAccess.getdbHeler().insertorUpdatePatientDescriptions(pBean);
			}

		}
	}
	public void notifyGetPatientComments(Object obj){
		if(obj instanceof Vector){
			Log.i("patientdetails","notifyGetPatientComments");
			Vector<PatientCommentsBean> pBeanList=(Vector<PatientCommentsBean>)obj;
			for(PatientCommentsBean pBean:pBeanList){
				DBAccess.getdbHeler().insertorUpdatePatientComments(pBean);
			}

		}
	}
	public void notifyroleAccess(Object object) {
		Log.i("sss", "notifyroleAccess");
		if (object instanceof Vector) {
			Vector<Object> resultObject = (Vector<Object>) object;
			for (int i = 0; i < resultObject.size(); i++) {
				if (resultObject.get(i) instanceof RoleAccessBean) {
					RoleAccessBean beanList = (RoleAccessBean)resultObject.get(i);
						DBAccess.getdbHeler().insertorupdateRoleAccess(beanList);
				}
				if (resultObject.get(i) instanceof RolePatientManagementBean) {
					RolePatientManagementBean beanList = (RolePatientManagementBean)resultObject.get(i);
					DBAccess.getdbHeler().insertorupdateRolepatientAccess(beanList);
				}
				if (resultObject.get(i) instanceof RoleEditRndFormBean) {
					RoleEditRndFormBean beanList = (RoleEditRndFormBean)resultObject.get(i);
					DBAccess.getdbHeler().insertorupdateRoleEditRoundingFormAccess(beanList);
				}
				if (resultObject.get(i) instanceof RoleTaskMgtBean) {
					RoleTaskMgtBean beanList = (RoleTaskMgtBean)resultObject.get(i);
					DBAccess.getdbHeler().insertorupdateRoleTransactionAccess(beanList);
				}
				if (resultObject.get(i) instanceof RoleCommentsViewBean) {
					RoleCommentsViewBean beanList = (RoleCommentsViewBean)resultObject.get(i);
					DBAccess.getdbHeler().insertorupdateRoleCommentsViewAccess(beanList);
				}
			}
		}
	}
	public void notifyStatesWebServiceResponse(Object obj) {
		if (obj instanceof ArrayList) {
			ArrayList<String[]> statesList = (ArrayList<String[]>) obj;
			Log.i("AAAA", "notifyStatesWebServiceResponse " + statesList.size());
			for (String[] state : statesList) {
				DBAccess.getdbHeler().insertorUpdateStateDetails(state[0],state[1]);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}
	public void notifyMedicalSocietiesWebServiceResponse(Object obj) {
		if (obj instanceof ArrayList) {
			ArrayList<String[]> medicalSocietiesList = (ArrayList<String[]>) obj;
			Log.i("AAAA", "notifyMedicalSocietiesWebServiceResponse " + medicalSocietiesList.size());
			for (String[] medicalSociety : medicalSocietiesList) {
				DBAccess.getdbHeler().insertorUpdateMedicalSocieties(medicalSociety[1], medicalSociety[0]);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}
	public void notifyHospitalDetails(Object obj) {
		if (obj instanceof ArrayList) {
			ArrayList<String> hospitalDetails = (ArrayList<String>) obj;
			Log.i("AAAA", "notifyHospitalDetails " + hospitalDetails.size());
			for (String hospitaldetails : hospitalDetails) {
				DBAccess.getdbHeler().insertorUpdateHospitalDetails(hospitaldetails);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}
	public void notifySpecialiesRresponse(Object obj) {
		if (obj instanceof ArrayList) {
			ArrayList<String[]> specialitList = (ArrayList<String[]>) obj;
			Log.i("AAAA", "notifySpecialiesRresponse " + specialitList.size());
			for (String[] specialities : specialitList) {
				DBAccess.getdbHeler().insertorUpdateSpecialityDetails(specialities[0], specialities[1]);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}
	public void notifyMedicalSchools(Object obj) {
		if (obj instanceof ArrayList) {
			ArrayList<String> schoolsList = (ArrayList<String>) obj;
			Log.i("AAAA", "notifyMedicalSchools " + schoolsList.size());
			for (String medicalSchools : schoolsList) {
				DBAccess.getdbHeler().insertorUpdateMedicalSchools(medicalSchools);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}
	public void notifyGetFileDetails(Object obj){
		if(obj instanceof Vector){
			Vector<FileDetailsBean>  fBeanList=(Vector<FileDetailsBean> )obj;
			SingleInstance.fileDetails.clear();
			SingleInstance.fileDetails=fBeanList;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					DashBoardFragment.newInstance(SingleInstance.mainContext).showMemoryControl();
				}
			});
		}
	}
	public void notifyGetMemberRights(Object obj) {
		if (obj instanceof Vector) {
			Vector<GroupMemberBean> membersList = (Vector<GroupMemberBean>) obj;
			for (GroupMemberBean bean : membersList) {
				DBAccess.getdbHeler().insertorUpdateMemberDetails(bean);
			}
		} else if (obj instanceof WebServiceBean) {
			showToast(((WebServiceBean) obj).getText());
		}
	}

	public void closingActivity(){

		if (SingleInstance.contextTable.containsKey("groupchat"))
		{
			GroupChatActivity groupChatActivity =(GroupChatActivity)SingleInstance.contextTable.get("groupchat");
			groupChatActivity.finish();
		}

		if (WebServiceReferences.contextTable.containsKey("ordermenuactivity")) {
			CallHistoryActivity callHistoryActivity = (CallHistoryActivity) WebServiceReferences.contextTable.get("ordermenuactivity");
			callHistoryActivity.finish();
		}

		if (WebServiceReferences.contextTable.containsKey("myaccountactivity")) {

			MyAccountActivity myaccount_activity = (MyAccountActivity) WebServiceReferences.contextTable.get("myaccountactivity");
			myaccount_activity.finish();
		}
		if (WebServiceReferences.contextTable.containsKey("roundnewpatient")) {

			RoundNewPatientActivity roundnewpatient = (RoundNewPatientActivity) WebServiceReferences.contextTable.get("roundnewpatient");
			roundnewpatient.finish();

		}
		if (WebServiceReferences.contextTable.containsKey("roundingEdit")) {

			RoundingEditActivity roundingEdit = (RoundingEditActivity) WebServiceReferences.contextTable.get("roundingEdit");
			roundingEdit.finish();
		}
		if (WebServiceReferences.contextTable.containsKey("roundingGroup")) {

			RoundingGroupActivity roundingGroup = (RoundingGroupActivity) WebServiceReferences.contextTable.get("roundingGroup");
			roundingGroup.finish();
		}

		if (WebServiceReferences.contextTable.containsKey("taskcreation")) {

			TaskCreationActivity taskcreation = (TaskCreationActivity) WebServiceReferences.contextTable.get("taskcreation");
			taskcreation.finish();
		}
		if (WebServiceReferences.contextTable.containsKey("forwarduser")) {

			ForwardUserSelect forwarduser = (ForwardUserSelect) WebServiceReferences.contextTable.get("forwarduser");
			forwarduser.finish();
		}
		if (WebServiceReferences.contextTable.containsKey("groupactivity")) {

			GroupActivity groupactivity = (GroupActivity) WebServiceReferences.contextTable.get("groupactivity");
			groupactivity.finish();
		}






	}
}
