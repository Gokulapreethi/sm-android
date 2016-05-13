package com.cg.files;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;
import org.lib.model.CallHistoryBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.ShareReminder;
import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.account.buddyView1;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.TextNoteDatas;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.forms.Alert;
import com.cg.ftpprocessor.FTPBean;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.BackgroundNotification;
import com.cg.instancemessage.IMNotifier;
import com.cg.quickaction.ContactLogics;
import com.cg.timer.ReminderService;
import com.crashlytics.android.Crashlytics;
import com.exchanges.ExchangesActivity;
import com.util.SingleInstance;
import com.util.VideoPlayer;
//import com.cg.account.R.drawable;

/**
 * This screen will populate all the event entries made by the user, it may be
 * notes or call history. From here user can create notes and they can make the
 * calls too
 * 
 * 
 * 
 */
public class CompleteListView extends FragmentActivity implements
		SlideMenuInterface.OnSlideMenuItemClickListener, IMNotifier {

	private FrameLayout historyContainer;

	private ViewStub viewStub;

	public String strCompleteListQuery = null;
	private static Context context;
	private int GET_RESOURCE;
	private final int FROM_GALLERY = 1;

	private Handler handler = new Handler();

	private final int FROM_PHOTO = 2;
	private final int FROM_VIDEO = 3;

	private Handler MenuHandler;
	public boolean isrejected = false;
	private int noScrHeight;
	private static int noScrWidth;
	public LinearLayout footer = null;
	public BeatListAdapter adapter;
	private final static int SIGNIN = 17;
	private final static int CONTACTSTITLE = 0;

	public Button IMRequest;
	private Button btnReceiveCall;
	public CallDispatcher callDisp;
	private Handler handleInComingAlert = new Handler();
	private AlertDialog alert;

	private Button btn_notification = null;

	public EditText filterEditText = null;

	private AlertDialog confirmation = null;

	private List<CompleteListBean> tempHistoryList = null;

	private boolean iscomponentOpend = false;

	public int component_index;

	private ProgressDialog progDailog = null;
	private SlideMenu slidemenu;
	public String owner;

	private ListView historyListView;

	public static List<CompleteListBean> historyList = null;
	private KeyguardManager keyguardManager;
	private KeyguardLock lock;
	protected PowerManager.WakeLock mWakeLock;
	public Button btn_edit = null;
	private Button btn_selectAll = null;
	private Button btn_deleteAll = null;
	private Button btn_shareAll = null;
	public boolean select = false;

	private Timer timer;

	private Timer timerQA;

	private MyTimerTask myTimerTask;

	private MyTimerTaskQA myTimerTaskQA;

	public static TextNoteDatas textnotes = null;

	private boolean isfromSplash = false;

	private AlertDialog alertDelay = null;

	private String autoPlayType = null;

	byte b = 10;
	byte c = 10;

	private int getlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			context = this;
			WebServiceReferences.contextTable.put("MAIN", this);

			SingleInstance.getChatProcesser();
			SingleInstance.getChatHistoryWriter();

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			noScrHeight = displaymetrics.heightPixels;
			noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;

			if (callDisp != null) {
				CallDispatcher.isConnected = callDisp.isOnLine(this);
			}
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

			WebServiceReferences.callDispatch.put("calldispatch", callDisp);
			adapter = new BeatListAdapter(this, true);
			if (WebServiceReferences.contextTable.containsKey("clist")) {
				WebServiceReferences.contextTable.remove("clist");
				WebServiceReferences.contextTable.put("clist", this);
			} else {
				WebServiceReferences.contextTable.put("clist", this);
			}
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			CallDispatcher.dateFormat = sp.getString("dateformate",
					"MM-dd-yyyy");

			/** GetDisply Screen Height and Width */
			isfromSplash = getIntent().getBooleanExtra("splash", false);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			btn_notification = (Button) findViewById(R.id.notification);
			btn_edit = (Button) findViewById(R.id.edit);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			btn_edit.setLayoutParams(params);
			btn_edit.setTextSize(15);
			btn_edit.setTypeface(Typeface.DEFAULT_BOLD);
			btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
			btn_selectAll = (Button) findViewById(R.id.btn_addcontact);
			btn_selectAll
					.setBackgroundResource(R.drawable.ic_action_select_all);
			btn_notification.setVisibility(View.GONE);
			btn_edit.setTag("edit");
			btn_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (btn_edit.getTag().toString()
								.equalsIgnoreCase("edit")) {
							btn_edit.setTag("unedit");
							select = true;
							if (adapter.getCount() > 0) {
								btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.update));
								btn_selectAll.setVisibility(View.VISIBLE);
								footer.setVisibility(View.VISIBLE);
							} else {
								Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.no_files_toedit), 1)
										.show();
							}
						} else {
							select = false;
							adapter.selectAllNotes(false);
							btn_edit.setTag("edit");
							btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
							btn_selectAll.setVisibility(View.GONE);
							footer.setVisibility(View.GONE);

						}
						adapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			IMRequest = (Button) findViewById(R.id.im);
			IMRequest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callDisp.openReceivedIm(v, context);
				}
			});
			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

			btnReceiveCall = (Button) findViewById(R.id.btncomp);
			btnReceiveCall
					.setBackgroundResource(R.drawable.toolbar_buttons_plus);

			btnReceiveCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (btn_edit.getText().toString()
								.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(R.string.edit))) {
							String username = null;

							if (CallDispatcher.LoginUser != null) {

								Intent intentComponent = new Intent(context,
										ComponentCreator.class);
								Bundle bndl = new Bundle();
								bndl.putString("type", "note");
								bndl.putBoolean("action", true);
								intentComponent.putExtras(bndl);
								startActivity(intentComponent);
							} else if (getUsernameFromPreferences().trim()
									.length() != 0) {
								Intent intentComponent = new Intent(context,
										ComponentCreator.class);
								Bundle bndl = new Bundle();
								bndl.putString("type", "note");
								bndl.putBoolean("action", true);
								intentComponent.putExtras(bndl);
								startActivity(intentComponent);
							} else {
								ShowError(SingleInstance.mainContext.getResources().getString(R.string.notes),SingleInstance.mainContext.getResources().getString(R.string.signin_before_createnote));
							}

						} else {
							ShowToast(SingleInstance.mainContext.getResources().getString(R.string.you_edit_mode));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			final Button btnSett = (Button) findViewById(R.id.settings);

			btnSett.setBackgroundResource(R.drawable.icon_sidemenu);

			btnSett.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					slidemenu.show();

				}
			});

			btn_selectAll.setTag("unselectall");
			btn_selectAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (historyList.size() > 0) {
							if (v.getTag().toString()
									.equalsIgnoreCase("unselectall")) {
								adapter.selectAllNotes(true);
								v.setTag("selectall");
								// adapter.deleteNoteMap = adapter.selectAllMap;
								adapter.loadList();
							} else {

								adapter.deleteNoteMap = adapter.selectAllMap;
								adapter.selectAllNotes(false);
								v.setTag("unselectall");
							}

							adapter.notifyDataSetChanged();

						} else {
							ShowToast(SingleInstance.mainContext.getResources().getString(R.string.no_notes_to_delete));

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						else
							e.printStackTrace();
					}
				}
			});

			if (CallDispatcher.LoginUser != null) {
				owner = CallDispatcher.LoginUser;
			} else {
				owner = getUsernameFromPreferences();
			}
			Log.i("files", "owner :: " + owner);
			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
					+ owner + "'";

			historyList = callDisp.getdbHeler(context)
					.getCompleteListProperties(strCompleteListQuery);
			ShowList();
			if (historyList.size() > 0) {
				btn_edit.setVisibility(View.VISIBLE);
				filterEditText.setVisibility(View.VISIBLE);
			} else {
				btn_selectAll.setVisibility(View.GONE);
				footer.setVisibility(View.GONE);
				filterEditText.setVisibility(View.GONE);
			}
			storeInLocalFolder();
			if (isfromSplash) {
				Intent loginintent = new Intent(context, buddyView1.class);
				loginintent.putExtra("isFirstlogin", true);
				startActivity(loginintent);
				isfromSplash = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

		}

	}

	/**
	 * When the user received the share reminder request from the buddies
	 * changed the buddy icon color to get the attention of the user
	 * 
	 * @param sr
	 */

	public void showShareRemainderRequest() {
		try {
			CallDispatcher.reminderArrives = true;

			for (int i = 0; i < WebServiceReferences.shareRemainderArray.size(); i++) {
				ShareReminder sr = WebServiceReferences.shareRemainderArray
						.get(i);
				TimeZone tz = Calendar.getInstance().getTimeZone();
				TimeZone z = TimeZone.getTimeZone(sr.getRemindertz());

				if ((sr.getRemindertz().trim().length() != 0)
						&& sr.getReminderdatetime().trim().length() != 0) {
					try {
						String strTime = convTimeZone(sr.getReminderdatetime(),
								z.getDisplayName(), tz.getID());
						Log.d("thread", "remainder tz ondd " + strTime);

						sr.setReminderdatetime(strTime);
						Log.i("thread", "After Converting : " + strTime);
					} catch (Exception e1) {
						Log.e("thread", "EXc:" + e1.getMessage());
						Log.i("callhistory", "" + e1.getMessage());
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e1.getMessage(), e1);

					}

				} else if (sr.getReminderdatetime().trim().length() != 0) {
					try {

						String dateString1 = sr.getReminderdatetime();
						Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm")
								.parse(dateString1);
						String dateString2 = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm").format(date);
						sr.setReminderdatetime(dateString2);
					} catch (Exception e) {
						Log.i("callhistory", "" + e.getMessage());
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);

					}
				}
				if (sr.getStatus().equalsIgnoreCase("new")) {

					if (!sr.getDownloadType().equals("2")) {
						callDisp.downloadOfflineresponse(sr.getFileLocation(),
								null, "files", sr);

					} else {
						createDBentryforStreaming(sr);
					}
				}
			}

			WebServiceReferences.shareRemainderArray.clear();
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	private void createDBentryforStreaming(final ShareReminder share) {

		try {
			if (callDisp.getToneEnabled()) {
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(this);
				String file = pref.getString("sharetone", null);
				if (file != null) {
					callDisp.playShareTone(file);
				}
			}

			if (callDisp.isApplicationInBackground(this)) {

				if (share.getType().equalsIgnoreCase("video")) {
					if (callDisp.notifier == null)
						callDisp.notifier = new BackgroundNotification(this);
					callDisp.notifier.ShowNotification(share.getFrom()
							+ " Shared Video note with you", context
							.getResources().getString(R.string.app_name),
							"share");

				}
			}

			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String url = preferences.getString("url", null);

			String loginIP = url.substring(url.indexOf("://") + 3);
			Log.d("logip", "login ip" + loginIP);
			if (loginIP.contains(":")) {
				loginIP = loginIP.substring(0, loginIP.indexOf(":"));
				Log.d("logip", "login ip" + loginIP);
				loginIP = loginIP.trim();
			} else if (loginIP.contains("/")) {
				loginIP = loginIP.substring(0, loginIP.indexOf("/"));
				Log.d("logip", "login ip" + loginIP);
				loginIP = loginIP.trim();
			}

			String cmpurl = "http://" + loginIP + "/uploads/" + share.getFrom()
					+ "/" + share.getFileLocation();

			ContentValues cv = new ContentValues();
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
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						else
							e.printStackTrace();
					}
				} else {
					vanishValue = share.getVanishValue();
				}
				vanishMode = share.getVanishMode();
			}

			cv.put("componenttype", share.getType());
			cv.put("componentpath", cmpurl);
			cv.put("ftppath", "");
			cv.put("componentname", "Video");
			cv.put("fromuser", share.getFrom());
			cv.put("comment", share.getShareComment());
			cv.put("reminderstatus", 0);
			cv.put("owner", CallDispatcher.LoginUser);
			cv.put("vanishmode", vanishMode);
			cv.put("vanishvalue", vanishValue);
			cv.put("receiveddateandtime", getNoteCreateTimeForFiles());
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
			int id = callDisp.getdbHeler(context).insertComponent(cv);

			final Components cmptPro = new Components();
			cmptPro.setComponentId(id);
			cmptPro.setcomponentType("video");
			cmptPro.setContentPath(cmpurl);
			cmptPro.setContentName("Video");
			cmptPro.setComment(share.getShareComment());
			cmptPro.setDateAndTime(getNoteCreateTimeForFiles());
			cmptPro.setfromuser(share.getFrom());
			cmptPro.setRemDateAndTime(share.getReminderdatetime());
			cmptPro.SetReminderStatus(0);
			cmptPro.setViewMode(0);
			cmptPro.setRemDateAndTime(share.getReminderdatetime());
			cmptPro.setresponseTpe(share.getReminderResponseType());
			cmptPro.setVanishMode(share.getVanishMode());
			cmptPro.setVanishValue(share.getVanishValue());

			if (callDisp.getdbHeler(context).insertComponent(cv) > 0) {

				if (CallDispatcher.LoginUser != null && callDisp.isConnected) {
					KeepAliveBean aliveBean = callDisp.getKeepAliveBean(
							share.getId(), "accepted");
					aliveBean.setKey("0");
					WebServiceReferences.webServiceClient.heartBeat(aliveBean);

				}

				WebServiceReferences.pendingShare -= 1;
				if (share.getReminderStatus().equalsIgnoreCase("true")) {
					String strDateTime = share.getReminderdatetime();
					String strQuery = "update component set reminderdateandtime='"
							+ strDateTime
							+ "',reminderstatus=1 where componentid="
							+ cmptPro.getComponentId();

					if (callDisp.getdbHeler(context).ExecuteQuery(strQuery)) {

						Intent reminderIntent = new Intent(context,
								ReminderService.class);
						startService(reminderIntent);
					}
				}

				handler.post(new Runnable() {

					@Override
					public void run() {

						if (callDisp.getdbHeler(context).isRecordExists(
								"select * from component where componentid="
										+ cmptPro.getComponentId() + "")) {
							notifyFileDownloaded(cmptPro, share.getType(),
									cmptPro.getDateAndTime());
						}
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Pass the username and password to the CallDispatcher's SignIn method
	 * 
	 * @param user
	 * @param password
	 */
	public void setLogin(final String user, final String password) {
		Log.i("call", "Comes to setLogin");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					String privateIp = callDisp.wifiEngine
							.getLocalInetaddress();
					callDisp.setLocalipaddsress(privateIp);
//					callDisp.SignIn(user, password);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (AppReference.isWriteInFile)
						AppReference.logger.error(e.getMessage(), e);
					else
						e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * To start the web service
	 * 
	 * @param url
	 * @param port
	 */
	public void startWebService(String url, String port) {
		try {
			callDisp.startWebService(url, port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void ShowList() {

		try {
			setContentView(R.layout.history_container);
			slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
			final ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
			callDisp.composeList(datas);

			slidemenu.init(CompleteListView.this, datas, CompleteListView.this,
					100);
			if (slidemenu.isMenuShowing()) {
				slidemenu.hide();
			}
			historyContainer = (FrameLayout) findViewById(R.id.historyContainerLayout);
			filterEditText = (EditText) findViewById(R.id.filter_text);
			filterEditText.setSingleLine();
			filterEditText.setFocusable(true);
			footer = (LinearLayout) findViewById(R.id.footer);
			btn_deleteAll = (Button) findViewById(R.id.deleteAll);
			btn_shareAll = (Button) findViewById(R.id.shareAll);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(filterEditText.getWindowToken(), 0);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

			filterEditText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(final CharSequence s, int start,
						int before, int count) {

					if (filterEditText.getText().toString().trim().length() == 0) {
						if (tempHistoryList != null) {
							tempHistoryList.clear();
							tempHistoryList = null;
						}
					}

					handleInComingAlert.post(new Runnable() {

						@Override
						public void run() {
							try {
								historyContainer.removeAllViews();
								tempHistoryList = new ArrayList<CompleteListBean>();
								if (historyList != null) {
									tempHistoryList.addAll(historyList);
								} else {
									historyList = callDisp.getdbHeler(context)
											.getCompleteListProperties(
													strCompleteListQuery);
									tempHistoryList.addAll(historyList);
								}

								for (int idx = 0; idx < historyList.size(); idx++) {
									CompleteListBean obj = historyList.get(idx);
									String data = "";

									data = obj.getContentName();
									if (!data.matches("(?i).*" + s.toString()
											+ ".*")) {
										tempHistoryList.remove(obj);
									} else {
									}
								}

								viewStub = new ViewStub(CompleteListView.this,
										R.layout.history_schedule);

								viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
									public void onInflate(ViewStub stub,
											View inflated) {

										setUIElements(inflated, tempHistoryList);
									}
								});
								historyContainer.addView(viewStub);
								viewStub.inflate();

							} catch (Exception e) {
								Log.i("callhistory", "" + "" + e.getMessage());
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);

							}
						}
					});

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});

			btn_deleteAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (adapter.getNotesForDelete().size() > 0) {
							AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
									context);
							deleteConfirmation.setTitle(SingleInstance.mainContext.getResources().getString(R.string.delete_confirm));
							deleteConfirmation
									.setMessage(SingleInstance.mainContext.getResources().getString(R.string.notes_to_delete));
							deleteConfirmation.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Set mapSet = (Set) adapter
													.getNotesForDelete()
													.entrySet();

											Iterator mapIterator = mapSet
													.iterator();
											while (mapIterator.hasNext()) {
												Map.Entry mapEntry = (Map.Entry) mapIterator
														.next();
												Integer keyValue = (Integer) mapEntry
														.getKey();
												CompleteListBean clBean = (CompleteListBean) mapEntry
														.getValue();
												Log.i("files",
														""
																+ clBean.getContentpath());
												try {

													Log.e("list",
															"Remove Idx:"
																	+ clBean.getcomponentType());
													Log.e("list",
															"component type:"
																	+ clBean.getcomponentType());

													String strDeleteQry = "delete from component where componentid="
															+ clBean.getComponentId();

													if (callDisp
															.getdbHeler(context)
															.ExecuteQuery(
																	strDeleteQry)) {
														if (clBean
																.getContentpath() != null
																&& clBean
																		.getContentpath() != "") {
															if (!clBean
																	.getcomponentType()
																	.equalsIgnoreCase(
																			"video")) {

																File f = new File(
																		clBean.getContentpath());
																if (f.exists()) {
																	f.delete();
																}

															} else if (clBean
																	.getcomponentType()
																	.equalsIgnoreCase(
																			"video")) {

																File f = new File(
																		clBean.getContentpath()
																				+ ".mp4");
																if (f.exists()) {
																	f.delete();
																}
																File f1 = new File(
																		clBean.getContentpath()
																				+ ".jpg");
																Log.e("ll",
																		">>>>>>>>>>>>>>>>>>>>"
																				+ f1.toString());
																if (f1.exists()) {
																	f1.delete();
																}

															}
														}

														historyScheduleData
																.clear();
														strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
																+ owner + "'";
														historyList = callDisp
																.getdbHeler(
																		context)
																.getCompleteListProperties(
																		strCompleteListQuery);
														historyScheduleData
																.addAll(historyList);
														historyList.clear();
														historyList
																.remove(keyValue);
														historyListView
																.setAdapter(adapter);
														adapter.notifyDataSetChanged();
														UpdateList();

														if (adapter.getCount() > 0) {
															btn_selectAll
																	.setVisibility(View.GONE);
															btn_edit.setVisibility(View.VISIBLE);
															filterEditText
																	.setVisibility(View.VISIBLE);
															footer.setVisibility(View.GONE);
														} else
															filterEditText
																	.setVisibility(View.GONE);
													}

												} catch (Exception e) {
													Log.i("delete",
															""
																	+ ""
																	+ e.getMessage());

													e.printStackTrace();
												}
											}
											btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
											adapter.getNotesForDelete().clear();
											adapter.notifyDataSetChanged();
										}

									});
							deleteConfirmation.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											adapter.getNotesForDelete().clear();
											adapter.notifyDataSetChanged();

											if (adapter.getCount() > 0) {
												btn_selectAll
														.setVisibility(View.GONE);
												btn_edit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
												filterEditText
														.setVisibility(View.VISIBLE);
												btn_edit.setVisibility(View.VISIBLE);
												footer.setVisibility(View.GONE);
											} else
												filterEditText
														.setVisibility(View.GONE);
										}
									});

							deleteConfirmation.show();
							select = false;

						} else {
							ShowToast(SingleInstance.mainContext.getResources().getString(R.string.select_delete_confirm));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btn_shareAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (adapter.getNotesForDelete().size() > 0) {
							if (callDisp.getmyBuddys().length > 0) {
								callDisp.send_multiple = true;
								Set mapSet = (Set) adapter.getNotesForDelete()
										.entrySet();
								Iterator mapIterator = mapSet.iterator();
								while (mapIterator.hasNext()) {
									Map.Entry mapEntry = (Map.Entry) mapIterator
											.next();
									Integer keyValue = (Integer) mapEntry
											.getKey();
									CompleteListBean clBean = (CompleteListBean) mapEntry
											.getValue();
									callDisp.multiple_componentlist.add(clBean);
								}
								Intent intent = new Intent(context,
										sendershare.class);
								startActivity(intent);
								if (footer.getVisibility() == View.VISIBLE
										|| btn_selectAll.getVisibility() == View.VISIBLE) {
									footer.setVisibility(View.GONE);
									btn_selectAll.setVisibility(View.GONE);
									btn_edit.setText("Edit");
								}
								select = false;
								adapter.notifyDataSetChanged();
							} else
								ShowToast("Nod buddies to share");
						} else {
							ShowToast(SingleInstance.mainContext.getResources().getString(R.string.you_don_have_buddies));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			setViewStub();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void setViewStub() {

		try {
			viewStub = new ViewStub(CompleteListView.this,
					R.layout.history_schedule);
			viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
				public void onInflate(ViewStub stub, View inflated) {

					setUIElements(inflated, historyList);
				}
			});
			historyContainer.addView(viewStub);
			viewStub.inflate();
			refreshList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public void refreshList() {
		try {
			if (CallDispatcher.LoginUser != null)

				owner = CallDispatcher.LoginUser;
			else
				owner = getUsernameFromPreferences();

			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
					+ owner + "'";
			if (historyList != null) {
				historyList.clear();
				historyList = callDisp.getdbHeler(context)
						.getCompleteListProperties(strCompleteListQuery);
				historyScheduleData.clear();
				if (historyList != null) {
					historyScheduleData.addAll(historyList);
				}
				adapter.notifyDataSetChanged();
			} else {
				historyList = callDisp.getdbHeler(context)
						.getCompleteListProperties(strCompleteListQuery);
				historyScheduleData.clear();
				if (historyList != null) {
					historyScheduleData.addAll(historyList);
				}
				adapter.notifyDataSetChanged();
			}

			if (historyScheduleData.size() > 0) {
				btn_edit.setVisibility(View.VISIBLE);
				filterEditText.setVisibility(View.VISIBLE);
			}
			btn_selectAll.setVisibility(View.GONE);
			footer.setVisibility(View.GONE);
			btn_edit.setText("Edit");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	LinearLayout llayCmptListContainer;

	public View selectedItem;

	public void updateLayoutParams(int size) {

		try {
			if (llayCmptListContainer != null && historyListView != null) {

				if (size == 0) {
					LinearLayout.LayoutParams llayListParms = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT);
					historyListView.setLayoutParams(llayListParms);
					if (selectedItem != null) {
						selectedItem.setBackgroundColor(Color.WHITE);
					}
				} else {
					LinearLayout.LayoutParams llayListParms = new LinearLayout.LayoutParams(
							(int) (noScrWidth / 2),
							LinearLayout.LayoutParams.FILL_PARENT);
					historyListView.setLayoutParams(llayListParms);
					if (selectedItem != null) {
						selectedItem.setBackgroundColor(Color.WHITE);
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

	/**
	 * Create the ListView for the respective list of data
	 * 
	 * @param v
	 * @param historyLists
	 */
	private void setUIElements(View v, List<CompleteListBean> historyLists) {

		try {
			Log.d("thread", "came to setuielement....." + v);
			if (v != null) {
				try {
					historyScheduleData.clear();
					Log.d("thread", "..... came to setui element");
					Log.d("thread", "..... came to setui element hscedule"
							+ historyScheduleData);
					Log.d("thread", "..... came to setui element list"
							+ historyLists);
					historyScheduleData.addAll(historyLists);
					llayCmptListContainer = (LinearLayout) findViewById(R.id.complete_List_container);
					llayCmptListContainer.removeView(historyListView);
					llayCmptListContainer.setWeightSum(10f);
					historyListView = new ListView(context);
					historyListView.setCacheColorHint(Color.TRANSPARENT);
					historyListView
							.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

					LinearLayout.LayoutParams llayListParms = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT);
					historyListView.setAdapter(adapter);
					if (historyLists.size() > 0) {
						btn_edit.setVisibility(View.VISIBLE);
						filterEditText.setVisibility(View.VISIBLE);
						llayCmptListContainer.addView(historyListView,
								llayListParms);
					} else {
						btn_edit.setVisibility(View.GONE);
						footer.setVisibility(View.GONE);
						if (filterEditText.getText().toString().length() > 0) {
							filterEditText.setVisibility(View.VISIBLE);
						} else {
							filterEditText.setVisibility(View.GONE);
						}
						btn_selectAll.setVisibility(View.GONE);

						LinearLayout textlayout = new LinearLayout(context);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						params.gravity = Gravity.CENTER;
						textlayout.setLayoutParams(params);
						TextView text = new TextView(context);
						LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						text.setText(SingleInstance.mainContext.getResources().getString(R.string.no_files)

								+ "Press '+' To Create Different Kinds Of Files");
						text.setTextSize(15);
						text.setGravity(Gravity.CENTER_HORIZONTAL);
						text.setLayoutParams(params2);
						text.setVisibility(View.VISIBLE);
						text.setTextColor(R.color.black);
						textlayout.addView(text);
						textlayout.setGravity(Gravity.CENTER);
						if (filterEditText.getEditableText() != null
								&& filterEditText.getText().toString().length() > 0) {
							textlayout.setVisibility(View.INVISIBLE);
						} else
							textlayout.setVisibility(View.VISIBLE);
						if (filterEditText.getEditableText() != null
								&& filterEditText.getEditableText().toString()
										.length() > 0) {
							textlayout.setVisibility(View.INVISIBLE);
						} else
							textlayout.setVisibility(View.VISIBLE);
						llayCmptListContainer
								.addView(textlayout, llayListParms);
					}

					if (btn_edit.getTag() != null) {
						if (btn_edit.getTag().toString()
								.equalsIgnoreCase("unedit")) {
							btn_edit.setTag("edit");
							btn_edit.setText("Edit");
						}
					}
					adapter.selectAllNotes(false);
					adapter.notifyDataSetChanged();

					historyListView
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									try {
										getlist = arg2;
										viewComponent(arg1, arg2);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							});
					historyListView
							.setOnItemLongClickListener(new OnItemLongClickListener() {

								@Override
								public boolean onItemLongClick(
										AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
									// TODO Auto-generated method stub
									try {
										deleteNote(arg2, context);
										return true;
									} catch (Exception e) {
										return false;
									}
								}

							});

				} catch (Exception e) {
					if (AppReference.isWriteInFile)
						AppReference.logger.error(e.getMessage(), e);
					else
						e.printStackTrace();
					Log.d("thread", "came to exception....." + e.toString());
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

	/**
	 * Creation of adapter to populate the list view
	 * 
	 *
	 */
	public static class BeatListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ArrayList<Object> viewList = new ArrayList<Object>();

		public HashMap<Integer, CompleteListBean> selectAllMap = new HashMap<Integer, CompleteListBean>();

		public HashMap<Integer, CompleteListBean> deleteNoteMap = new HashMap<Integer, CompleteListBean>();

		public HashMap<Integer, String[]> getCompMap = new HashMap<Integer, String[]>();

		public HashMap<Integer, Integer> getCompIds = new HashMap<Integer, Integer>();

		public TextView textView = null;

		public boolean listCheck = false;

		public boolean editCheck = false;

		boolean values = false;

		public BeatListAdapter(Context context, boolean value) {

			mInflater = LayoutInflater.from(context);
			values = value;
		}

		public int getCount() {
			return historyScheduleData.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public Object getSeletor(int pos) {
			if (viewList.size() < pos)
				return null;
			else
				return this.viewList.get(pos);
		}

		public void loadList() {
			try {
				for (int i = 0; i < historyScheduleData.size(); i++) {
					deleteNoteMap.put(i, historyScheduleData.get(i));
				}

				Log.i("delete", "-----------" + historyScheduleData.size()
						+ ", delete : " + deleteNoteMap.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public HashMap<Integer, CompleteListBean> getNotesForDelete() {
			return deleteNoteMap;
		}

		public void deleteComp(int cmpId) {
			try {
				for (int i = 0; i < historyScheduleData.size(); i++) {
					try {
						CompleteListBean cBean = historyScheduleData.get(i);
						if (cBean.getComponentId() == cmpId) {
							historyScheduleData.remove(compIds().get(cmpId));
							historyList.remove(i);
							historyScheduleData.clear();
							historyScheduleData.addAll(historyList);
						}

					} catch (Exception e) {
						// TODO: handle exception
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public boolean ContainsComp(int cmpId) {
			try {
				for (int i = 0; i < historyScheduleData.size(); i++) {

					CompleteListBean cBean = historyScheduleData.get(i);
					if (cBean.getComponentId() == cmpId) {

						return true;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);

			}
			return false;
		}

		public void changeReadStatus(View view) {
			ImageView read = (ImageView) view.findViewById(R.id.cr_icon);
			read.setVisibility(view.INVISIBLE);
		}

		public HashMap<Integer, CompleteListBean> getTotalList() {
			return selectAllMap;
		}

		public HashMap<Integer, String[]> getCompDetails() {
			return getCompMap;
		}

		public void selectAllNotes(boolean check) {
			listCheck = check;
		}

		public void editedCheckBox(boolean cb) {
			editCheck = cb;
		}

		public TextView getTextView() {
			return textView;
		}

		public HashMap<Integer, Integer> compIds() {
			return getCompIds;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			boolean isnull = false;
			try {
				if (convertView == null) {
					isnull = true;
					convertView = mInflater.inflate(R.layout.history_list_view,
							null);
					holder = new ViewHolder();
					holder.checkbox = (CheckBox) convertView
							.findViewById(R.id.noteCheck);
					holder.tvContent = (TextView) convertView
							.findViewById(R.id.historytext);
					holder.tvContent.setWidth((int) (noScrWidth / 2) + 10);
					holder.tvcomment = (TextView) convertView
							.findViewById(R.id.historytext1);
					holder.tvcomment.setWidth((int) (noScrWidth / 2) + 10);
					holder.ivNoteRead = (ImageView) convertView
							.findViewById(R.id.cr_icon);
					holder.ivNoteRead.setVisibility(View.INVISIBLE);
					holder.ivNoteRead.setBackgroundResource(R.drawable.read);
					holder.ivNoteType = (ImageView) convertView
							.findViewById(R.id.cl_icon);
					holder.tvDate = (TextView) convertView
							.findViewById(R.id.txt_date);
					holder.expireTime = (TextView) convertView
							.findViewById(R.id.expire_time);
					holder.checkboxLayout = (LinearLayout) convertView
							.findViewById(R.id.check_container);
					holder.text_container = (LinearLayout) convertView
							.findViewById(R.id.text_container);
					convertView.setTag(holder);
					holder.listContainer = (RelativeLayout) convertView
							.findViewById(R.id.list_container);

				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				CompleteListBean clTemp = null;
				if (values) {
					clTemp = historyScheduleData.get(position);
				} else {
					if (WebServiceReferences.contextTable
							.containsKey("buddynote")) {
						historyScheduleData = BuddyNoteList.historyScheduleData;
						clTemp = historyScheduleData.get(position);
					}
				}
				final CompleteListBean compBean = clTemp;
				Log.i("dateformat", "Format" + CallDispatcher.dateFormat);
				SimpleDateFormat df = new SimpleDateFormat(
						CallDispatcher.dateFormat + " hh:mm aaa");
				SimpleDateFormat df2 = new SimpleDateFormat(
						CallDispatcher.dateFormat);
				String[] receivedTimes = clTemp.getDateAndTime().split(" ");
				Date receivedDate = null;
				if (receivedTimes[0].contains("/")
						&& CallDispatcher.dateFormat.contains("-")) {
					SimpleDateFormat userDateFormat = new SimpleDateFormat(
							"MM/dd/yyyy");
					SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
							CallDispatcher.dateFormat);
					Log.i("dateformat", "received in condition "
							+ receivedTimes[0]);

					Date date = userDateFormat.parse(receivedTimes[0]);
					String convertedDate = dateFormatNeeded.format(date);
					receivedDate = dateFormatNeeded.parse(convertedDate);
				} else if (receivedTimes[0].contains("-")
						&& CallDispatcher.dateFormat.contains("/")) {
					SimpleDateFormat userDateFormat = new SimpleDateFormat(
							"dd-MM-yyyy");
					SimpleDateFormat dateFormatNeeded = new SimpleDateFormat(
							CallDispatcher.dateFormat);
					Log.i("dateformat", "received in condition "
							+ receivedTimes[0]);
					Date date = userDateFormat.parse(receivedTimes[0]);
					String convertedDate = dateFormatNeeded.format(date);
					receivedDate = df2.parse(convertedDate);
				} else {
					receivedDate = df2.parse(receivedTimes[0]);
				}
				Calendar cal = Calendar.getInstance();
				String[] todayDate = df.format(cal.getTime()).split(" ");
				Date today = df2.parse(todayDate[0]);
				String[] yesterdayDate = getYesterdayDateString(df).split(" ");
				Date yesterday = df2.parse(yesterdayDate[0]);
				Log.i("dateformat", "today :: " + today);
				Log.i("dateformat", "yesterday :: " + yesterday);

				if (receivedDate.compareTo(today) == 0) {
					holder.tvDate.setText(receivedTimes[1]);
				} else if (receivedDate.compareTo(yesterday) == 0) {
					holder.tvDate.setText("Yesterday");
				} else {
					holder.tvDate.setText(time(df));

				}

				if (clTemp.getVanishMode() != null
						&& (clTemp.getVanishMode().length() > 0 && !clTemp
								.getVanishMode().equalsIgnoreCase("null"))) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy hh:mm aa");
					DateFormatSymbols dfSym = new DateFormatSymbols();
					dfSym.setAmPmStrings(new String[] { "am", "pm" });
					dateFormat.setDateFormatSymbols(dfSym);
					String currnetDate = dateFormat.format(new Date());
					Date newDate = dateFormat.parse(currnetDate);
					String dateString = clTemp.getVanishValue();
					Date getDate = dateFormat.parse(dateString);
					Log.i("files", "current date :: " + currnetDate);
					Log.i("files", "vanish date :: " + dateString);
					if (compareDatesByDateMethods(dateFormat, getDate, newDate,
							"files")) {
						String strDeleteQry = "delete from component where componentid="
								+ clTemp.getComponentId();
						((CompleteListView) WebServiceReferences.contextTable
								.get("MAIN")).callDisp.getdbHeler(context)
								.ExecuteQuery(strDeleteQry);
						historyScheduleData.remove(position);
						notifyDataSetChanged();
					} else
						holder.expireTime.setText("Expires At : "
								+ clTemp.getVanishValue());
				}
				getCompIds.put(clTemp.getComponentId(), position);
				holder.checkbox.setChecked(false);
				holder.checkbox.setContentDescription("" + clTemp);
				holder.checkbox.setTag(position);
				if (listCheck) {
					selectAllMap.put(position, clTemp);
					String[] comp = new String[2];
					comp[0] = clTemp.getContentpath();
					comp[1] = clTemp.getcomponentType();
					getCompMap.put(position, comp);
					holder.checkbox.setChecked(true);
				} else {
					if (selectAllMap.size() > 0) {
						selectAllMap.clear();
					}
					if (getCompMap.size() > 0) {
						getCompMap.clear();
					}
					holder.checkbox.setChecked(false);
				}
				if (editCheck) {
					holder.checkbox.setChecked(false);
					holder.checkbox.setVisibility(View.GONE);
				}

				if (clTemp.getFromUser().equals("")) {
					holder.tvContent.setText(clTemp.getContentName());
					textView = holder.tvContent;
					holder.tvcomment.setVisibility(View.GONE);
				} else {

					Log.d("callc", "call history " + clTemp.getcomponentType());
					if (clTemp.getcomponentType().equals("note")
							|| clTemp.getcomponentType().equals("audio")
							|| clTemp.getcomponentType().equals("video")
							|| clTemp.getcomponentType().equals("photo")
							|| clTemp.getcomponentType().equals("sketch")
							|| clTemp.getcomponentType().equals("document")) {

						holder.tvContent.setText(clTemp.getContentName() + "\n"
								+ "From: " + clTemp.getFromUser());
						holder.tvcomment.setVisibility(View.GONE);

					}

					else {
						holder.tvContent.setText(clTemp.getContentName());
					}
				}
				Log.d("callc",
						"call history comp type " + clTemp.getcomponentType());

				Log.d("message", "is null" + isnull);
				if ((clTemp.getViewmode() == 0)
						&& (!clTemp.getcomponentType().equalsIgnoreCase("call") && (!clTemp
								.getcomponentType().equalsIgnoreCase("im")))) {
					holder.ivNoteRead.setVisibility(View.VISIBLE);
					holder.ivNoteRead.setBackgroundResource(R.drawable.read);
				} else {
					holder.ivNoteRead.setVisibility(View.INVISIBLE);
				}

				if (clTemp.getcomponentType().trim().equals("note")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.textnotesnew);
				} else if (clTemp.getcomponentType().trim().equals("audio")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.audionotesnew);
				} else if (clTemp.getcomponentType().trim().equals("photo")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.photonotesnew);
				} else if (clTemp.getcomponentType().trim().equals("video")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.videonotesnew);
				} else if (clTemp.getcomponentType().trim().equals("call")) {
					holder.ivNoteType.setBackgroundResource(R.drawable.callnew);

				} else if (clTemp.getcomponentType().trim().equals("IM")) {
					holder.ivNoteType.setBackgroundResource(R.drawable.callnew);
				} else if (clTemp.getcomponentType().trim()
						.equalsIgnoreCase("sketch")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.handpencil);
				} else if (clTemp.getcomponentType().trim()
						.equalsIgnoreCase("document")) {
					holder.ivNoteType
							.setBackgroundResource(R.drawable.textnotesnew);
					String[] name = clTemp.getContentpath().split("\\.");
					String extn = name[1];
					if (clTemp.getFromUser().equals("")) {
						holder.tvContent.setText(extn + " "
								+ clTemp.getContentName());
					} else {
						holder.tvContent.setText(extn + " "
								+ clTemp.getContentName() + "\n" + "From: "
								+ clTemp.getFromUser());
					}
				}

				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					if (((CompleteListView) WebServiceReferences.contextTable
							.get("MAIN")).select) {
						holder.checkbox.setVisibility(View.VISIBLE);
					} else {
						holder.checkbox.setVisibility(View.GONE);
					}
				}

				holder.checkbox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								if (isChecked) {
									deleteNoteMap.put(position, compBean);

								} else {

									if (deleteNoteMap.containsKey(position)) {
										deleteNoteMap.remove(position);

									}
									buttonView.setChecked(false);
								}

							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
			if (isnull) {
				viewList.add(0, convertView);
			}

			return convertView;
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

		static class ViewHolder {
			CheckBox checkbox;
			TextView tvcomment;
			TextView tvContent;
			ImageView ivNoteRead;
			ImageView ivNoteType;
			ImageView navigator;
			TextView tvDate;
			TextView expireTime;
			LinearLayout checkboxLayout;
			LinearLayout text_container;
			RelativeLayout listContainer;

		}

		private String time(SimpleDateFormat dateFormat) {
			try {
				Calendar cal = Calendar.getInstance();
				String[] date = dateFormat.format(cal.getTime()).split(" ");
				return date[0];
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
				return null;
			}
		}

		public void getPosition(int compId) {

		}

		private String getYesterdayDateString(SimpleDateFormat dateFormat) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -1);
				Log.i("dateformat",
						"yesterday format change :: "
								+ dateFormat.format(cal.getTime()));
				return dateFormat.format(cal.getTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
				return null;
			}
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

	}

	public View getview() {
		return this.selectedItem;
	}

	public int getindex() {
		return this.component_index;
	}

	public static Vector<CompleteListBean> historyScheduleData = new Vector<CompleteListBean>();

	/**
	 * Show Toast message .
	 * 
	 * @param string
	 * @param i
	 */
	public void ShowToast(String message) {

		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show the on create option menu if user logged in .
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu m_menu = menu;

		if (CallDispatcher.LoginUser != null) {
			m_menu.add(Menu.NONE, 1, 0, "Signout");
		}
		if (CallDispatcher.LoginUser != null) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			if (sp.getBoolean("autologin", false)) {
				m_menu.add(Menu.NONE, 2, 0, "Remove Autologin");
			}
		}
		if (filterEditText.getText().toString().trim().length() != 0) {
			if (tempHistoryList != null) {
				if (tempHistoryList.size() != 0) {
					m_menu.add(Menu.NONE, 3, 0, "Edit");
				}
			}
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Menu m_menu = menu;
		m_menu.clear();
		if (CallDispatcher.LoginUser != null) {
			m_menu.add(Menu.NONE, 1, 0, "Signout");
		}
		if (CallDispatcher.LoginUser != null) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(context);
			if (sp.getBoolean("autologin", false)) {
				m_menu.add(Menu.NONE, 2, 0, "Remove AutoSignin");
			}
		}

		if (filterEditText.getText().toString().trim().length() != 0) {
			if (tempHistoryList != null) {
				if (tempHistoryList.size() != 0) {
					m_menu.add(Menu.NONE, 3, 0, "Edit");

				}
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * If note creation menu item selected make the action for the respective
	 * menu selection
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("action", item.getItemId());
		msg.obj = bundle;
		MenuHandler.sendMessage(msg);
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			switch (GET_RESOURCE) {
			case FROM_GALLERY:
				if (resultCode == Activity.RESULT_CANCELED) {

				} else {
					Uri selectedImageUri = data.getData();
					String str = callDisp.getRealPathFromURI(selectedImageUri);
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("action", 5);
					bundle.putString("img_path", str);
					msg.obj = bundle;
					MenuHandler.sendMessage(msg);
				}
				break;
			case FROM_PHOTO:

				if (resultCode == 10) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("action", 6);
					bundle.putString("img_path", "photo");
					msg.obj = bundle;
					MenuHandler.sendMessage(msg);
				}

				break;
			case FROM_VIDEO:
				Log.i("lg", "onActivity result");
				Log.i("lg", "onActivity request code" + requestCode);
				Log.i("lg", "onActivity result code" + resultCode);
				Log.i("lg", "VIDEO_CAPTURE");
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("action", 4);
				bundle.putString("video_path", "video");
				msg.obj = bundle;
				MenuHandler.sendMessage(msg);
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	/**
	 * Crate thumbnail image file from the video
	 * 
	 * @param strThumbPath
	 * @return
	 */

	public boolean CreateVideoThumbnail(String strThumbPath) {
		try {
			System.out.println("create thumbnail");
			System.out.println("Thumb path" + strThumbPath);

			Log.d("lg", "......create video file");

			MediaPlayer m = new MediaPlayer();
			m.setDataSource(strThumbPath + ".mp4");
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
					strThumbPath + ".mp4",
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
				File fl = new File(strThumbPath + ".mp4");
				if (fl.exists()) {

					Bitmap bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.broken);
					bmp = ResizeVideoImage(bmp);
					Log.i("myLog", "Image Height :" + bmp.getHeight());
					Log.i("myLog", "Image Width  :" + bmp.getWidth());
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

		} catch (OutOfMemoryError e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			return false;
		} catch (FileNotFoundException ex) {
			Log.e("Exc", "FileNotFoundException : " + ex);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(ex.getMessage(), ex);

			return false;
		} catch (IOException ioe) {
			Log.e("Exc", "IOException : " + ioe);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(ioe.getMessage(), ioe);
			else
				return (createbrokenThump(strThumbPath));
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			Log.e("Exc", "Exception : " + e);
			return false;
		}
		return false;

	}

	@SuppressWarnings("finally")
	private boolean createbrokenThump(String strThumbPath) {
		boolean status = false;
		try {

			File fl = new File(strThumbPath + ".mp4");
			if (fl.exists()) {

				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.broken);
				bmp = ResizeVideoImage(bmp);
				Bitmap thumb1 = combineImages(bmp, "00:00:00");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				thumb1.compress(CompressFormat.JPEG, 75, bos);
				FileOutputStream fos = new FileOutputStream(strThumbPath
						+ ".jpg");
				bos.writeTo(fos);
				bos.close();
				fos.close();
				status = true;
				bmp.recycle();
				thumb1.recycle();
			} else {
				status = false;
			}
		} catch (FileNotFoundException ex) {
			Log.e("Exc", "FileNotFoundException : " + ex);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(ex.getMessage(), ex);

			status = false;
		} catch (IOException ioe) {
			Log.e("Exc", "IOException : " + ioe);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(ioe.getMessage(), ioe);

			status = false;
		} catch (Exception e) {
			Log.e("Exc", "Exception : " + e);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			status = false;
		} finally {
			return (status);
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
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}
	}

	private Bitmap combineImages(Bitmap c, String time) {
		try {
			Bitmap cs = null;

			Bitmap bitmapPlay = BitmapFactory.decodeResource(getResources(),
					R.drawable.vplay1);
			int width, height = 0;
			width = c.getWidth();
			height = c.getHeight();
			cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas comboImage = new Canvas(cs);
			comboImage.drawBitmap(c, 0f, 0f, null);
			comboImage.drawBitmap(bitmapPlay,
					(c.getWidth() / 2 - bitmapPlay.getWidth() / 2),
					(c.getHeight() / 2 - bitmapPlay.getHeight() / 2), null);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setTypeface(Typeface.SERIF);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(30);
			comboImage.drawText(time, (c.getWidth() / 4), (c.getHeight() / 2),
					paint);

			return cs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}
	}

	public void UpdateList() {
		try {

			if (CallDispatcher.LoginUser != null)
				owner = CallDispatcher.LoginUser;
			else
				owner = " ";

			strCompleteListQuery = "select componentid,componenttype,componentpath,ftppath,componentname,fromuser,comment,reminderstatus,owner,vanishmode,vanishvalue,receiveddateandtime,reminderdateandtime,viewmode,reminderzone,reminderresponsetype from component where componenttype!='IM' and componenttype!='call' and owner='"
					+ owner + "'";

			Log.d("file", "came to update list");
			historyContainer.removeAllViews();
			historyList = callDisp.getdbHeler(context)
					.getCompleteListProperties(strCompleteListQuery);
			final List<CompleteListBean> tempHistoryList = new ArrayList<CompleteListBean>();
			tempHistoryList.addAll(historyList);
			viewStub = new ViewStub(CompleteListView.this,
					R.layout.history_schedule);
			viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
				public void onInflate(ViewStub stub, View inflated) {

					setUIElements(inflated, tempHistoryList);
				}
			});
			historyContainer.addView(viewStub);
			viewStub.inflate();
			if (filterEditText != null) {
				if (filterEditText.getText().toString().trim().length() != 0) {
					filterEditText.setText("");
				}
			}
			if (iscomponentOpend) {
				openComponent();
				iscomponentOpend = false;

			}
			if (adapter != null)
				adapter.notifyDataSetChanged();

		} catch (Exception e) {
			Log.e("frg", "Exc :" + e);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	/**
	 * When user clicked the back button redirect the current screen into screen
	 * which is coming from which activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)) {
					AlertDialog.Builder buider = new AlertDialog.Builder(
							context);
					buider.setMessage(
							SingleInstance.mainContext.getResources().getString(R.string.app_background)
)
							.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes)
,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											// finish();
											moveTaskToBack(true);
											// return true;

										}
									})
							.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no)
,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
										}
									});
					AlertDialog alert = buider.create();
					alert.show();
				}
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return false;
		}
	}

	/**
	 * Show the list of onLine buddies
	 * 
	 * @param callType
	 */

	public void show() {
		try {
			final CharSequence[] choiceList = callDisp.getOnlineBuddys();
			if (choiceList != null && choiceList.length > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.create();
				builder.setTitle("Add People");
				for (int i = 0; i < choiceList.length; i++)
					// Log.i("buddy", "Name :" + choiceList[i]);

					builder.setItems(choiceList,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									BuddyInformationBean bean = null;
//									bean = WebServiceReferences.buddyList
//											.get(choiceList[which].toString());

									BuddyInformationBean oldBuddyBean = null;
//									= WebServiceReferences.buddyList
//											.get(bean.getName());

									if (oldBuddyBean != null) {
										bean.setLatitude(oldBuddyBean
												.getLatitude());
										bean.setLongitude(oldBuddyBean
												.getLongitude());
										Log.d("UMBX", "new location lat "
												+ bean.getLatitude());
										Log.d("UMBX", "new location long "
												+ bean.getLongitude());
									}

									Toast.makeText(
											context,
											"Select " + choiceList[which]
													+ "lat+"
													+ bean.getLatitude()
													+ "lon"
													+ bean.getLongitude()
													+ bean.getFriendsName(),
											Toast.LENGTH_SHORT).show();

								}
							});
				alert = builder.create();
				alert.show();
			} else {
				Toast.makeText(context, "No Online Users", 2000).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * When Activity is resumed play the stopped video or audio notes if it
	 * paused on the activity onPause state
	 */

	@Override
	protected void onResume() {
		try {
			super.onResume();
			// if (adapter.getNotesForDelete().size() > 0)
			// Log.i("sharall","--------"+adapter.getNotesForDelete().size());
			// adapter.getNotesForDelete().clear();

			// if (adapter.selectAllMap.size() > 0)
			// Log.i("sharall","----##----"+adapter.selectAllMap.size());
			// adapter.selectAllMap.clear();

			if (!btn_edit.getText().toString().equalsIgnoreCase("Update")) {
				UpdateList();
			}
			historyListView.setSelection(getlist);
			if (WebServiceReferences.Imcollection.size() == 0)
				IMRequest.setVisibility(View.GONE);
			else
				IMRequest.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("file", "exception ===> " + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	/**
	 * Notification from the DownloadShareRemainder about the file download
	 */
	public void notifyFileDownloaded(Components cmp, String type, String key) {
		try {

			CompleteListBean clBean = new CompleteListBean();
			clBean.setComponentId(cmp.getComponentId());
			clBean.setcomponentType(cmp.getcomponentType());
			clBean.setContent(cmp.getComment());
			clBean.setContentPath(cmp.getContentPath());
			clBean.setDateAndTime(cmp.getDateAndTime());
			clBean.setContentName(cmp.getContentName());
			clBean.setFromUser(cmp.getfromuser());
			clBean.setTouser(cmp.gettoUser());
			clBean.setReminderTime(cmp.getRemDateAndTime());
			clBean.setViewMode(cmp.getViewMode());
			clBean.setResponseType(cmp.getreminderresponsetype());
			clBean.setVanishMode(cmp.getVanishMode());
			if (clBean.getVanishMode() != null) {
				if (clBean.getVanishMode().equalsIgnoreCase("elapse")) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:MM");
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.HOUR,
							Integer.valueOf(cmp.getVanishValue()));
					clBean.setVanishValue(dateFormat.format(cal.getTime()));
				} else {
					clBean.setVanishValue(cmp.getVanishValue());
				}
			}
			historyList.add(0, clBean);
			historyScheduleData.clear();
			historyScheduleData.addAll(historyList);
			component_index += 1;
			UpdateList();
			if (historyList.size() > 0) {
				btn_edit.setVisibility(View.VISIBLE);
				filterEditText.setVisibility(View.VISIBLE);
			} else {
				btn_edit.setVisibility(View.GONE);
				footer.setVisibility(View.GONE);
				if (filterEditText.getText().toString().length() > 0) {
					filterEditText.setVisibility(View.VISIBLE);
				} else {
					filterEditText.setVisibility(View.GONE);
				}
				btn_selectAll.setVisibility(View.GONE);
				LinearLayout textlayout = new LinearLayout(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.CENTER;
				textlayout.setLayoutParams(params);
				TextView text = new TextView(context);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				text.setText(SingleInstance.mainContext.getResources().getString(R.string.no_files)

						+ " Press '+' To Create Different Kinds Of Files");
				text.setTextSize(15);
				text.setGravity(Gravity.CENTER_HORIZONTAL);
				text.setLayoutParams(params2);
				text.setVisibility(View.VISIBLE);
				text.setTextColor(R.color.black);
				textlayout.addView(text);
				textlayout.setGravity(Gravity.CENTER);
				if (filterEditText.getEditableText() != null
						&& filterEditText.getText().toString().length() > 0) {
					textlayout.setVisibility(View.INVISIBLE);
				} else
					textlayout.setVisibility(View.VISIBLE);

			}

			if (btn_edit.getTag() != null) {
				if (btn_edit.getTag().toString().equalsIgnoreCase("unedit")) {
					btn_edit.setTag("edit");
					btn_edit.setText("Edit");
				}
			}
			Toast.makeText(
					SipNotificationListener.getCurrentContext(),
					clBean.getFromUser() + " shared "
							+ clBean.getcomponentType() + " file with you", 1)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// added
	}

	public String getDeviceId() {
		try {
			String deviceId = null;
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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

	private void cancelDialog() {
		try {
			if (progDailog != null) {
				progDailog.dismiss();
				progDailog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyResponse(String title, String Message) {
		try {
			cancelDialog();
			ShowError(title, Message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ShowError(String Title, String Message) {

		try {
			confirmation = new AlertDialog.Builder(this).create();
			confirmation.setTitle(Title);
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
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {

		try {
			// TODO Auto-generated method stub
			if (adapter.getNotesForDelete().size() > 0) {
				adapter.getNotesForDelete().clear();
			}
			if (adapter.selectAllMap.size() > 0) {
				adapter.selectAllMap.clear();
			}
			System.gc();
			super.onDestroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeSenderPaging() {
		try {
			Bundle bndl = new Bundle();
			bndl.putString("Hangup", "Hangup");
			Message msg = new Message();
			msg.obj = bndl;
			handleInComingAlert.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void wakeupClock() {
		try {
			keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
			lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
			lock.disableKeyguard();

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
					"My Tag");
			this.mWakeLock.acquire();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void releaseClock() {
		try {
			this.mWakeLock.release();
			lock.reenableKeyguard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyadapter() {
		try {
			Log.e("lggg", "&&&&&&&&&&&&&&&&&&&&&&& " + adapter);

			historyScheduleData.clear();
			historyScheduleData.addAll(historyList);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public CompleteListBean putDBEntry(int option, String ComponentPath,
			String time, String title) {
		try {
			String heading = title;
			if (heading.contains("'")) {
				heading = heading.replace("'", "''");
			}
			ContentValues cv = new ContentValues();
			CompleteListBean clBean = new CompleteListBean();
			String componentType = "";
			String caption = title;

			if (option == 1) {
				componentType = "note";
				caption = heading;
			} else if (option == 2 || option == 3)
				componentType = "photo";
			else if (option == 4)
				componentType = "audio";
			else if (option == 5)
				componentType = "video";
			else if (option == 6)
				componentType = "sketch";
			else if (option == 7)
				componentType = "document";

			cv.put("componenttype", componentType);
			cv.put("componentpath", ComponentPath);
			cv.put("ftppath", "");
			cv.put("componentname", caption);
			cv.put("fromuser", "");
			cv.put("comment", "");
			cv.put("reminderstatus", 0);
			cv.put("owner", CallDispatcher.LoginUser);
			cv.put("vanishmode", "");
			cv.put("vanishvalue", "");
			cv.put("receiveddateandtime", getNoteCreateTimeForFiles());
			cv.put("reminderdateandtime", "");
			cv.put("viewmode", 1);
			cv.put("reminderzone", "");
			cv.put("reminderresponsetype", "");
			int id = callDisp.getdbHeler(context).insertComponent(cv);
			clBean.setComponentId(id);
			clBean.setcomponentType(componentType);
			clBean.setContentPath(ComponentPath);
			clBean.setContentName(title);
			clBean.setDateAndTime(time);
			clBean.setIsresponsed("0");
			callDisp.cmp = clBean;
			historyList.add(0, clBean);
			historyScheduleData.clear();
			historyScheduleData.addAll(historyList);
			adapter.notifyDataSetChanged();
			component_index += 1;
			return clBean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}

	}

	private String convTimeZone(String time, String sourceTZ, String destTZ)
			throws Exception {

		try {
			if (time.trim().contains("-")) {
				time = time.trim().replace("-", "/");
				String[] formate = time.split(" ");
				if (formate != null) {
					if (formate.length > 1) {
						String[] ch = formate[0].split("/");
						if (ch != null) {
							if (ch.length == 3) {
								time = ch[2] + "/" + ch[1] + "/" + ch[0] + " "
										+ formate[1];
							}
						}
					}
				}

			}

			String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
			// String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

			time = time.trim();

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
			Log.d("date",
					"#################### cond satisfied" + sdf.parse(time));

			Date specifiedTime = null;
			try {
				if (sourceTZ != null)
					sdf.setTimeZone(TimeZone.getTimeZone(sourceTZ));
				else
					sdf.setTimeZone(TimeZone.getDefault());

				specifiedTime = sdf.parse(time);

			} catch (Exception e1) {
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e1.getMessage(), e1);
				else
					e1.printStackTrace();
			}

			// switch timezone
			if (destTZ != null)
				sdf.setTimeZone(TimeZone.getTimeZone(destTZ));
			else
				sdf.setTimeZone(TimeZone.getDefault()); // default to server's
			// timezone

			return changetoOurFormat(sdf.format(specifiedTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}
	}

	private String changetoOurFormat(String strDate) {

		try {
			// create SimpleDateFormat object with source string date format
			SimpleDateFormat sdfSource = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm");

			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");

			// parse the date into another format
			strDate = sdfDestination.format(date);

			// System.out
			// .println("Date is converted from dd/MM/yyyy HH:mm format to yyyy-MM-dd HH:mm");
			// System.out.println("Converted date is : " + strDate);

		} catch (ParseException pe) {
			// System.out.println("Parse Exception : " + pe);
			if (AppReference.isWriteInFile)
				AppReference.logger.error(pe.getMessage(), pe);

		}
		return strDate;

	}

	public void notifyFileDownloaded(String Response, final FTPBean bean) {

		try {
			Log.d("FTP", "notifyFileDownloaded");
			if (Response.equalsIgnoreCase("true")) {

				if (callDisp.getToneEnabled()) {

					if (callDisp.getSettings() != null
							&& callDisp.getSettings().getShareToveValue1()
									.trim().length() > 0) {
						String file = callDisp.getSettings()
								.getShareToveValue1();
						if (file != null) {
							callDisp.playShareTone(file);
						}
					}
				}

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
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);

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
				cv.put("receiveddateandtime", getNoteCreateTimeForFiles());
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
				cmptPro.setDateAndTime(getNoteCreateTimeForFiles());
				cmptPro.SetReminderStatus(0);
				cmptPro.setViewMode(0);
				cmptPro.setfromuser(share.getFrom());
				cmptPro.setRemDateAndTime(share.getReminderdatetime());
				cmptPro.setresponseTpe(share.getReminderResponseType());
				cmptPro.setVanishMode(share.getVanishMode());
				cmptPro.setVanishValue(share.getVanishValue());

				if (share.getType().equalsIgnoreCase("audio")
						|| share.getType().equalsIgnoreCase("video")) {
					SharedPreferences preferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					WebServiceReferences.isAutoPlay = preferences.getBoolean(
							"autoplay", false);
					if (WebServiceReferences.isAutoPlay) {
						if (cmptPro.getRemDateAndTime() == null
								|| cmptPro.getRemDateAndTime().trim().length() == 0) {

							Components cmpts = (Components) cmptPro.clone();

							if (cmpts != null)
								WebServiceReferences.llAutoPlayContent
										.add(cmpts);

							if (WebServiceReferences.contextTable
									.containsKey("a_play")) {
								// initAutoPlayNotification(WebServiceReferences.isAutoPlay);
							} else {
								autoPlayType = cmpts.getcomponentType();
								checkAutoPlayerStatus();

							}
						}

					}
				}

				if (callDisp.getdbHeler(context).insertComponent(cv) > 0) {
					int id = callDisp.getdbHeler(context).getMaxIdToSet();
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
						if (callDisp.getdbHeler(context).ExecuteQuery(strQuery)) {
							Intent reminderIntent = new Intent(context,
									ReminderService.class);
							startService(reminderIntent);
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
						CreateVideoThumbnail(thumb);

					}

					handler.post(new Runnable() {

						@Override
						public void run() {
							Log.d("thread", "downloadd");
							if (callDisp.getdbHeler(context).isRecordExists(
									"select * from component where componentid="
											+ cmptPro.getComponentId() + "")) {
								notifyFileDownloaded(cmptPro, share.getType(),
										cmptPro.getDateAndTime());
							}

						}
					});

				}

				if (WebServiceReferences.pendingShare == 0) {
				}
				adapter.selectAllNotes(false);
				// if (btn_edit.getVisibility() == View.GONE) {
				// btn_edit.setVisibility(View.VISIBLE);
				// }
				// if (filterEditText.getVisibility() == View.GONE) {
				// filterEditText.setVisibility(View.VISIBLE);
				// }
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public static void checkDir() {
		try {
			File folder = new File("/sdcard/COMMedia/");
			if (!folder.exists()) {
				folder.mkdir();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public static String getNoteCreateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mma");
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

	public static String getNoteCreateTimeForFiles() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					CallDispatcher.dateFormat + " hh:mm aaa");
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

	public void updateComponent(String buddy_name, final String filepath,
			String fileid, String ftppath, String dtype, String fileType) {
		try {

			String qry = "update component set viewmode=2,ftppath='" + ftppath
					+ "' where componentid='" + fileid + "'";

			Log.d("qry", "updated succesfully..." + qry);
			if (callDisp.getdbHeler(this).ExecuteQuery(qry)) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i("thread", "################## uploadfilecontains"
								+ callDisp.uploadData.contains(filepath));
						if (callDisp.uploadData.contains(filepath)) {
							callDisp.uploadData.remove(filepath);
						}

					}
				});

				if (callDisp
						.getdbHeler(context)
						.getshareComponent(
								"select * from sharecomponent where componentid = '"
										+ fileid + "'").size() > 0) {
					qry = "update sharecomponent set ftppath ='" + ftppath
							+ "' where componentid='" + fileid + "'";
					callDisp.getdbHeler(context).ExecuteQuery(qry);
					Log.d("qry", "updated succesfully...");
				} else {
					ContentValues cv = new ContentValues();
					cv.put("componentpath", filepath);
					cv.put("componenttype", fileType);
					cv.put("fromuser", CallDispatcher.LoginUser);
					cv.put("touser", buddy_name);
					cv.put("ftppath", ftppath);
					cv.put("sharestatus", 0);
					callDisp.getdbHeler(context).insertShareComponent(cv);
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

	public static boolean CheckReminderIsValid(String strDate) {
		try {
			SimpleDateFormat formatter;
			if (strDate.contains("-"))
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			else {
				formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
				DateFormatSymbols dfSym = new DateFormatSymbols();
				dfSym.setAmPmStrings(new String[] { "am", "pm" });
				formatter.setDateFormatSymbols(dfSym);
			}
			Calendar cal = Calendar.getInstance();
			String strD2 = formatter.format(cal.getTime());
			Date currentDateTime = null;
			try {
				currentDateTime = (Date) formatter.parse(strD2);
				Log.e("rem", "currentDateTime:" + currentDateTime.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
			Date CheckDate = null;
			try {
				CheckDate = (Date) formatter.parse(strDate);
				Log.e("rem", "CheckDate:" + CheckDate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}
			if (CheckDate.getTime() <= currentDateTime.getTime())
				return false;
			else
				return true;

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return false;
		}
	}

	public HashMap<String, CompleteListBean> getSearchItems() {
		HashMap<String, CompleteListBean> beanholder = new HashMap<String, CompleteListBean>();
		try {
			if (tempHistoryList.size() != 0) {
				for (CompleteListBean listbean : tempHistoryList) {
					beanholder.put(Integer.toString(listbean.getComponentId()),
							listbean);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return beanholder;
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

	private void openComponent() {

		try {

			component_index += 1;
			Log.i("list", "Selected :" + component_index);
			CompleteListBean clBean = historyScheduleData.get(component_index);
			String strQuery = "select * from component where componentid="
					+ clBean.getComponentId();
			Components obj = callDisp.getdbHeler(context)
					.getComponent(strQuery);
			clBean.setContentPath(obj.getContentPath());
			clBean.setContentName(obj.getContentName());
			clBean.setIsresponsed(Integer.toString(obj.getViewMode()));
			clBean.setReminderTime(obj.getRemDateAndTime());

			if (obj != null && obj.getcomponentType() != null) {
				Log.i("list", "Selected :1");

				long freemem = callDisp.getExternalMemorySize();
				if (freemem > 0 && freemem >= 5210) {
					File fl = null;
					if (!clBean.getcomponentType().equalsIgnoreCase("video")) {
						fl = new File(clBean.getContentpath());
					} else {
						fl = new File(clBean.getContentpath() + ".mp4");
					}
					if (fl.exists()) {
						if (clBean.getViewmode() == 0) {
							String strUpdateNote = "update component set viewmode='"
									+ 1
									+ "' where componentid="
									+ clBean.getComponentId();
							if (callDisp.getdbHeler(context).ExecuteQuery(
									strUpdateNote)) {
								clBean.setViewMode(1);
								UpdateList();
							}
						}

					} else {
						ShowError("Compoent Error",
								"Sorry Can not open this component,because the file not exist.");
					}

				} else {
					if (freemem == -1) {
						ShowToast("Can not access SD card");
					} else {
						ShowToast("Insuficient Memory");
					}
				}
			}

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			Log.i("list", "&&&&&&&&&&&&&&&" + e.toString());

		}

	}

	private String getUsernameFromPreferences() {
		try {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			String username = sharedPreferences.getString("uname", null);
			try {
				if (username == null)
					username = "";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (AppReference.isWriteInFile)
					AppReference.logger.error(e.getMessage(), e);
				else
					e.printStackTrace();
			}

			return username;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
			return null;
		}

	}

	public void notifyCallHistoryToServer(String from, String[] to,
			String callType, String sessinId, String startTime[],
			String endTime[], String[] autoCall) {
		try {

			ArrayList<CallHistoryBean> arrayCallHistory = new ArrayList<CallHistoryBean>();

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
				chb.setLoginUserName(callDisp.LoginUser);
				arrayCallHistory.add(chb);

			}

			// callDisp.callHistoryMap.put(sessinId, arrayCallHistory);
		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			Log.i("callhistory", "" + "" + e.getMessage());
		}

	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

			Log.i("callhistory", "" + e.getMessage());
		}
	}

	public void changemenuitems() {

		try {
			Log.i("welcome",
					"enable user changemenuitems() called---->complete list view");

			Slidebean user = new Slidebean();
			if (CallDispatcher.LoginUser != null) {
				user.setTitle(CallDispatcher.LoginUser);
			} else {
				user.setTitle("");
			}
			user.setId(CONTACTSTITLE);
			slidemenu.setBean(0, user);

			Slidebean sm16 = new Slidebean();
			sm16.setTitle("Signout");
			sm16.setId(SIGNIN);
			slidemenu.setBean(19, sm16);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public void viewComponent(View arg1, int arg2) {
		try {
			selectedItem = arg1;

			CompleteListBean clBean = historyScheduleData.get(arg2);

			String strQuery = "select * from component where componentid="
					+ clBean.getComponentId();
			Components obj = callDisp.getdbHeler(context)
					.getComponent(strQuery);
			clBean.setContentPath(obj.getContentPath());
			clBean.setContentName(obj.getContentName());
			clBean.setIsresponsed(Integer.toString(obj.getViewMode()));
			clBean.setReminderTime(obj.getRemDateAndTime());
			clBean.setContent(obj.getComment());

			if (obj != null && obj.getcomponentType() != null) {
				long freemem = callDisp.getExternalMemorySize();
				if (freemem > 0 && freemem >= 5210) {

					File fl = null;
					fl = new File(clBean.getContentpath());
					if (fl.exists()) {

						if (clBean.getViewmode() == 0) {
							String strUpdateNote = "update component set viewmode='"
									+ 1
									+ "' where componentid="
									+ clBean.getComponentId();
							Log.d("qry", strUpdateNote);

							if (callDisp.getdbHeler(context).ExecuteQuery(
									strUpdateNote)) {
								clBean.setViewMode(1);
								adapter.changeReadStatus(selectedItem);
							}
						}

						Intent intentComponent = new Intent(context,
								ComponentCreator.class);
						Bundle bndl = new Bundle();
						bndl.putString("type", clBean.getcomponentType());
						bndl.putBoolean("action", false);
						bndl.putInt("position", arg2);
						intentComponent.putExtras(bndl);
						callDisp.cmp = clBean;
						startActivity(intentComponent);

					} else {
						if (clBean.getcomponentType().equalsIgnoreCase("video")) {

							if (clBean.getViewmode() == 0) {
								String strUpdateNote = "update component set viewmode='"
										+ 1
										+ "' where componentid="
										+ clBean.getComponentId();
								Log.d("qry", strUpdateNote);

								if (callDisp.getdbHeler(context).ExecuteQuery(
										strUpdateNote)) {
									clBean.setViewMode(1);
									adapter.changeReadStatus(selectedItem);
								}
							}

							Log.d("lg",
									"vvvvvvvvvvvvv" + clBean.getcomponentType());

							Intent intentComponent = new Intent(context,
									ComponentCreator.class);
							Bundle bndl = new Bundle();
							bndl.putString("type", clBean.getcomponentType());
							bndl.putBoolean("action", false);
							bndl.putInt("position", arg2);
							intentComponent.putExtras(bndl);
							callDisp.cmp = clBean;
							startActivity(intentComponent);
							// }

						} else {
							ShowError("Component Error",
									"Sorry Can not open this component,because the file not exist.");
						}
					}

				} else {
					if (freemem == -1) {
						ShowToast("Can not access SD card");
					} else {
						ShowToast("Insuficient Memory");
					}
				}
			}

		} catch (Exception e) {
			Log.i("callhistory", "" + e.getMessage());
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);

		}
	}

	public void deleteNote(final int arg02, final Context context) {
		try {
			final CompleteListBean clBean = historyScheduleData.get(arg02);
			String message = null;
			message = "Are you sure to delete this'" + clBean.getContentName()
					+ "'?";

			AlertDialog.Builder deleteConfirmation = new AlertDialog.Builder(
					context);
			deleteConfirmation.setTitle("Delete Confirmation");
			deleteConfirmation.setMessage(message);
			deleteConfirmation.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {

								String strDeleteQry = "delete from component where componentid="
										+ clBean.getComponentId();

								if (callDisp.getdbHeler(context).ExecuteQuery(
										strDeleteQry)) {
									ShowToast("Selected files are deleted");
									if (clBean.getContentpath() != null
											&& clBean.getContentpath() != "") {
										if (!clBean.getcomponentType()
												.equalsIgnoreCase("video")) {

											File f = new File(clBean
													.getContentpath());
											if (f.exists()) {
												f.delete();
											}

										} else if (clBean.getcomponentType()
												.equalsIgnoreCase("video")) {

											File f = new File(clBean
													.getContentpath() + ".mp4");
											if (f.exists()) {
												f.delete();
											}
											File f1 = new File(clBean
													.getContentpath() + ".jpg");
											if (f1.exists()) {
												f1.delete();
											}

										}
									}
									historyList.remove(arg02);
									historyScheduleData.clear();
									historyScheduleData.addAll(historyList);
									if (!btn_edit.getText().toString()
											.equalsIgnoreCase("update")) {
										UpdateList();
									} else {
										adapter.notifyDataSetChanged();
										if (adapter.getCount() == 0) {
											btn_selectAll
													.setVisibility(View.GONE);
											btn_edit.setVisibility(View.GONE);
											filterEditText
													.setVisibility(View.GONE);
											footer.setVisibility(View.GONE);
											llayCmptListContainer
													.setVisibility(View.VISIBLE);
										} else {
											btn_selectAll
													.setVisibility(View.VISIBLE);
											btn_edit.setVisibility(View.VISIBLE);

										}

									}
									btn_edit.setText("Edit");
								}

							} catch (Exception e) {
								Log.i("callhistory", "" + e.getMessage());
								if (AppReference.isWriteInFile)
									AppReference.logger.error(e.getMessage(), e);

							}

							if (WebServiceReferences.contextTable
									.containsKey("Component")) {
								((ComponentCreator) WebServiceReferences.contextTable
										.get("Component")).finish();
							}

						}
					});
			deleteConfirmation.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			deleteConfirmation.show();

			// showSingleSelectBuddy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public CompleteListBean viewNotes(int position) {
		CompleteListBean clBean = historyScheduleData.get(position);
		try {
			if (clBean != null) {
				callDisp.cmp = clBean;
				String strQuery = "select * from component where componentid="
						+ clBean.getComponentId();
				Components obj = callDisp.getdbHeler(context).getComponent(
						strQuery);
				clBean.setContentPath(obj.getContentPath());
				clBean.setContentName(obj.getContentName());
				clBean.setIsresponsed(Integer.toString(obj.getViewMode()));
				clBean.setReminderTime(obj.getRemDateAndTime());

				if (obj != null && obj.getcomponentType() != null) {
					long freemem = callDisp.getExternalMemorySize();
					if (freemem > 0 && freemem >= 5210) {
						File fl = null;
						if (!clBean.getcomponentType()
								.equalsIgnoreCase("video")) {
							fl = new File(clBean.getContentpath());
						} else {
							fl = new File(clBean.getContentpath() + ".mp4");
						}
						if (fl.exists()) {

							if (clBean.getViewmode() == 0) {
								String strUpdateNote = "update component set viewmode='"
										+ 1
										+ "' where componentid="
										+ clBean.getComponentId();

								if (callDisp.getdbHeler(context).ExecuteQuery(
										strUpdateNote)) {
									clBean.setViewMode(1);
									adapter.changeReadStatus(selectedItem);
								}
							}
						}
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
		return clBean;
	}

	public boolean checkPosition(int position) {
		boolean isPoistion = false;
		try {
			if (historyScheduleData.size() - 1 == position) {
				isPoistion = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}
		return isPoistion;
	}

	public void storeInLocalFolder() {
		try {
			Intent intent = getIntent();

			InputStream is = null;

			FileOutputStream os = null;

			String fullPath = null;

			try {
				String action = intent.getAction();

				if (!Intent.ACTION_VIEW.equals(action))
					return;

				Uri uri = intent.getData();
				String scheme = uri.getScheme();
				String name = null;

				if (scheme.equals("file")) {
					List<String> pathSegments = uri.getPathSegments();

					if (pathSegments.size() > 0)
						name = pathSegments.get(pathSegments.size() - 1);
					Log.i("POIS", " file name ===>" + name);

				}

				else if (scheme.equals("content")) {
					Cursor cursor = getContentResolver()
							.query(uri,
									new String[] { MediaStore.MediaColumns.DISPLAY_NAME },
									null, null, null);

					cursor.moveToFirst();

					int nameIndex = cursor
							.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
					if (nameIndex >= 0)
						name = cursor.getString(nameIndex);
					Log.i("POIS", " content name ===>" + name);

				}

				else
					return;

				if (name == null)
					return;

				int n = name.lastIndexOf(".");
				String fileName, fileExt;

				if (n == -1)
					return;

				else {
					fileName = name.substring(0, n);
					fileExt = name.substring(n);
					if (!fileExt.equals(".docx") && !fileExt.equals(".pdf")
							&& !fileExt.equals("doc") && !fileExt.equals("xls")
							&& !fileExt.equals("xlsx"))

						return;

				}

				fullPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + fileName + fileExt;
				Log.i("POIS", "FULL PATH===>" + fullPath);
				is = getContentResolver().openInputStream(uri);
				os = new FileOutputStream(fullPath);

				byte[] buffer = new byte[4096];
				int count;

				while ((count = is.read(buffer)) > 0)
					os.write(buffer, 0, count);

				os.close();
				is.close();

			}

			catch (Exception e) {
				if (is != null) {
					try {
						is.close();
					}

					catch (Exception e1) {

						if (AppReference.isWriteInFile)
							AppReference.logger.error(e1.getMessage(), e1);

					}
				}

				if (os != null) {
					try {
						os.close();
					}

					catch (Exception e1) {
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e1.getMessage(), e1);

					}
				}

				if (fullPath != null) {
					File f = new File(fullPath);

					f.delete();
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

	public void saveFileFromGallery(String fullPath, String fileName,
			String ftpPath, String comment, String reminderdate,
			String reminderzone, String reminderresponsetype, int reminderstatus) {
		try {
			if (WebServiceReferences.contextTable.containsKey("Component")) {
				((ComponentCreator) WebServiceReferences.contextTable
						.get("Component")).finish();
			}
			putDBEntry(7, fullPath, getNoteCreateTimeForFiles(),
					fileName.trim());
			// viewComponent(historyListView.getChildAt(0), 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public void openFilesinExternalApp(String filename) {

		try {
			// TODO Auto-generated method stub

			String[] name = filename.split("\\.");

			String extn = name[1];

		
			// if (extn.equals("pdf") || extn.equals("doc") ||
			// extn.equals("docs")
			// || extn.equals("docx") || extn.equals("odt")
			// || extn.equals("xls") || extn.equals("xlsx")
			// || extn.equals("ppt") || extn.equals("pptx")
			// || extn.equalsIgnoreCase("php") || extn.equals("psd")) {
			File files = null;
			if (!filename.contains(""
					+ Environment.getExternalStorageDirectory())) {
				files = new File(Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + filename);
			} else {
				files = new File(filename);
			}

			if (files.exists()) {
				Uri path = Uri.fromFile(files);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(path, "*/*");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				try {
					context.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(context,
							"No Application Available to View this file",
							Toast.LENGTH_SHORT).show();
				}
			}
			// }
			/***
			 * Ends here
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			else
				e.printStackTrace();
		}

	}

	public void notifyProfilepictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					if (slidemenu != null) {
						if (slidemenu.isMenuShowing())
							slidemenu.refreshItem();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm aa");
			final String strDate = simpleDateFormat.format(calendar.getTime());

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						Log.i("file delte", "===> " + strDate);
						if (adapter != null)
							adapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						else
							e.printStackTrace();
					}
				}
			});
		}

	}

	class MyTimerTaskQA extends TimerTask {

		@Override
		public void run() {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm aa");
			final String strDate = simpleDateFormat.format(calendar.getTime());

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						Log.i("QAA", "===>  timer " + strDate);
						setQuickActionAlert();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if (AppReference.isWriteInFile)
							AppReference.logger.error(e.getMessage(), e);
						else
							e.printStackTrace();
					}
				}
			});
		}

	}

	public static String setLength2(int data) {
		String strData = Integer.toString(data);
		if (strData.length() == 1) {
			strData = "0" + strData;
		}
		return strData;
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

				String strDate = year + "-" + setLength2(month) + "-"
						+ setLength2(date) + " " + setLength2(hour) + ":"
						+ setLength2(minute);
				Boolean success = callDisp.getdbHeler(context).gettime(strDate,
						SipNotificationListener.getCurrentContext());

				if (success) {
					Log.i("name", "inside");
					Alert.writeBoolean(getApplicationContext(), Alert.satus,
							true);

					Intent i = new Intent(CompleteListView.this,
							ContactLogics.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					getApplication().startActivity(i);

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

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.USERPROFILE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.NOTES:

			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		case WebServiceReferences.EXCHANGES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			break;
		default:
			break;
		}
	}

	public void notifyLoginResponse(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					Crashlytics.setUserName("User Name :"
							+ CallDispatcher.LoginUser);
					callDisp.cancelDialog();
					if (obj instanceof String) {
						String result = (String) obj;
						if (result.equalsIgnoreCase("Succesfully logged in")) {
							SharedPreferences preferences = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());
							if (preferences != null) {
								Editor editor = preferences.edit();
								editor.putString("uname",
										CallDispatcher.LoginUser);
								editor.putString("pword",
										CallDispatcher.Password);
								editor.commit();
							}
							preferences = null;
							callDisp.setUserSettings();
							// callDisp.loadSIPLibrary(context);
							// callDisp.registermyAccount();
							if (WebServiceReferences.type != 2) {
								Intent intent = new Intent(context,
										ExchangesActivity.class);
								intent.putExtra("isLaunch", true);
								startActivity(intent);
							}

						} else
							ShowError("Signin", (String) obj);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (AppReference.isWriteInFile)
						AppReference.logger.error(e.getMessage(), e);
					else
						e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					IMRequest.setVisibility(View.VISIBLE);
					IMRequest.setEnabled(true);

					IMRequest
							.setBackgroundResource(R.drawable.small_blue_balloon);

					if (!callDisp.getdbHeler(context)
							.userChatting(sb.getFrom())) {
						callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
								CallDispatcher.LoginUser, 1,
								CallDispatcher.LoginUser);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (AppReference.isWriteInFile)
						AppReference.logger.error(e.getMessage(), e);
					else
						e.printStackTrace();
				}

			}
		});

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
						SipNotificationListener.getCurrentContext());
				buider.setMessage(
						"Auto play will begin in 5 seconds. Click on Stop to prevent automatic playback now.")
						.setPositiveButton("Play",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										handler.removeCallbacks(delayProcessrunnable);
										openAutoPlayScreen();

									}
								})
						.setNegativeButton("Stop",
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
				intent.putExtra("createOrOpen", SingleInstance.mainContext.getResources().getString(R.string.open));
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

	// Its only for profile files upload success reponse
	

}
