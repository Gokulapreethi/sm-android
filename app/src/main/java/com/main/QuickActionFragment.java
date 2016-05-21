package com.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.lib.model.FormAttributeBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.WebServiceBean;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bean.BuddyPermission;
import com.bean.DefaultPermission;
import com.bean.FormFieldBean;
import com.bean.IndividualPermission;
import com.cg.DB.DBAccess;
import com.cg.adapters.BusAdapter;
import com.cg.snazmed.R;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.forms.Alert;
import com.cg.quickaction.BuddyBean;
import com.cg.quickaction.ContactLogicbean;
import com.cg.quickaction.QuickActionBroadCastReceiver;
import com.cg.quickaction.QuickActionBuilder;
import com.cg.quickaction.QuickActionCallsSchedule;
import com.cg.quickaction.QuickActionSelectcalls;
import com.cg.quickaction.QuickActionSettingcalls;
import com.cg.quickaction.QuickActionTitlecalls;
import com.cg.quickaction.QuickActionType;
import com.cg.quickaction.ShareNotePicker;
import com.process.MemoryProcessor;
import com.util.SingleInstance;

public class QuickActionFragment extends Fragment implements
		OnCheckedChangeListener {

	LinearLayout llContainer = null;
	// Context context = null;
	Spinner actionSpinner = null;
	int selected = 0;
	String Times_mode = null;
	TextView frequency = null;
	// String attribute_tablename = null;
	private Handler wservice_handler = null;
	boolean isallselect = false;
	ImageView timemode = null;
	boolean isview = false;
	String formid, frequncy = null;
	private ProgressDialog progDailog = null;
	SharedPreferences p = null;
	// ArrayList<String> con = new ArrayList<String>();
	// ArrayList<String> contype = new ArrayList<String>();
	private ArrayList<String> field = new ArrayList<String>();
	Handler handler = new Handler();
	String[] list = null;
	String action = null;
	String actiontype = null;
	String To = null;
	String url = null;
	String Condition = null;
	String time, ftppath = null;
	private CompleteListBean cbean = null;
	HashMap<String, String> dtype = new HashMap<String, String>();
	String tablename = null;
	EditText edurl = null;
	ArrayList<ContactLogicbean> datasToLoad = new ArrayList<ContactLogicbean>();
	public ListView lv = null;
	int idToProcess = 0;
	int userid = 0;
	public BusAdapter adapterToShow = null;
	AlertDialog alert = null;
	// Button btn_notification = null;
	// Button IMRequest = null;
	// Button btn_cancel = null;
	Button btnReceiveCall = null;
	// Button btn_delete = null;
	// SegmentedRadioGroup group = null;
	CallDispatcher callDisp;
	// AlertDialog.Builder builder;
	private ArrayList<String> columnname = null;
	private ArrayList<String> columntype = null;
	String[] fields = null;
	String[] types = null;
	boolean isreject = false;
	String[] m_fields = null;
	String[] m_types = null;
	String c = null;
	private TextView tv_username = null;
	String[] records = null;
	String formname = null;
	// private TextView title = null;
	ArrayAdapter<String> adapter;
	List<String> menusList = new ArrayList<String>();
	String strDateTime, id = null;
	boolean entry = false;
	String type = null;
	String[] myServices = { "Share Photo", "Share Audio", "Share Text",
			"Share Video", "Share HandSketch", "Audio Call", "Video Call",
			"Audio Broadcast", "Video Broadcast", "Audio Conference",
			"Video Conference", "Show Results Form" };

	ArrayList<String> arrayList = new ArrayList<String>();;
	ArrayList<String> usersList = new ArrayList<String>();
	boolean ismultiview = false;
	String[] users = null;

	// private SlideMenu slidemenu;
	static int buddyStatus = 0;
	String owner;

	RelativeLayout buss_lay;
	Button addanaction;

	private ViewFlipper viewflipper, vf;
	LinearLayout call, menu, broadcast, share, report;
	RelativeLayout audiocall, videocall, audioconference, videoconference,
			hostedconference, rlay_title, llayout, audiobroadcast,
			videobroadcast, textnote, audionote, videonote, photonote,
			handsketch, generatereport;
	Button btn_cancelone, btn_next, btn_cancell, btn_can, btn_done, btn_add,
			btn_saving;
	ImageView calll, btn_back, bt_back, step2, step3, btn_sett;
	CheckBox checkbox;
	TextView username, note_date, selection, actiontitle, Schedule,
			scheduletask, scheduletaskone, audiocal, videocal, audioconferen,
			videoconferenc, hostedconferen, tv_calloptionname, tv_sharetext,
			tv_shareaudio, tv_videonote, tv_sharephoto, tv_sharehandsketch,
			tv_generatereport;
	Spinner spinner;
	EditText enterdate, et_description, et_descriptionone, et_title;
	ListView buddylist = null;
	String label, descrip, checkboxfield, spinnerfield, frequencyfield,
			datefield;
	String status = "1";
	QuickActionBuilder qabuilder = null;

	private static QuickActionFragment quickActionFragment;

	private static Context context;

	private static Context contextforms;

	public View view;

	private boolean fromMenu = false;
	private  Button plusBtn ;

	// private Button plus = null; // this button plus hide in this page,this
	// button create fragment xml

	public static QuickActionFragment newInstance(Context maincontext) {
		try {
			if (quickActionFragment == null) {
				context = maincontext;
				quickActionFragment = new QuickActionFragment();
			}

			return quickActionFragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return quickActionFragment;
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Button select = (Button) getActivity().findViewById(R.id.btn_brg);
		select.setVisibility(View.GONE);
		RelativeLayout mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
		mainHeader.setVisibility(View.VISIBLE);
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		
		title.setVisibility(View.VISIBLE);
		

		Button imVw = (Button) getActivity().findViewById(R.id.im_view);
		imVw.setVisibility(View.GONE);

		Button setting = (Button) getActivity().findViewById(R.id.btn_settings);
		setting.setVisibility(View.GONE);

		  plusBtn = (Button) getActivity().findViewById(R.id.add_group);
	//	plusBtn.setVisibility(View.VISIBLE);
		plusBtn.setBackgroundResource(R.drawable.toolbar_buttons_plus);
		plusBtn.setText("");

		if (plusBtn.getVisibility() == View.VISIBLE) {
			plusBtn.setVisibility(View.GONE);
		}

		plusBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								TextView title = (TextView) getActivity()
										.findViewById(
												R.id.activity_main_content_title);
								title.setText("Select Action");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					designSettingsScreen(null, 0, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		if (view == null) {
			view = inflater.inflate(R.layout.buslogics, null);
			try {
				getActivity().getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

				Button settings = (Button) getActivity().findViewById(
						R.id.btn_settings);
				settings.setText("");
				settings.setVisibility(View.GONE);

				// super.onCreate(savedInstanceState);
				qabuilder = QuickActionBuilder
						.getInstance(CallDispatcher.LoginUser);
				// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
				// setContentView(R.layout.custom_title1);
				// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				// R.layout.custom_title1);
				// context = this;
				// DisplayMetrics displaymetrics = new DisplayMetrics();
				// getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				// int noScrHeight = displaymetrics.heightPixels;
				// int noScrWidth = displaymetrics.widthPixels;

				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					callDisp = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					callDisp = new CallDispatcher(context);

				// callDisp.setNoScrHeight(noScrHeight);
				// callDisp.setNoScrWidth(noScrWidth);
				// displaymetrics = null;
				SingleInstance.contextTable.put("qaction", context);
				// title = (TextView) findViewById(R.id.note_date);
				// title.setText("Quick Action");
				menusList.add("Run");
				menusList.add("Edit");
				menusList.add("Delete");
				CallDispatcher.pdialog = new ProgressDialog(context);
				// btn_cancel = (Button) findViewById(R.id.settings);
				// btn_cancel.setBackgroundResource(R.drawable.icon_sidemenu);
				// btn_cancel.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// slidemenu.show();
				// }
				// });
				// btn_notification = (Button) findViewById(R.id.notification);
				// btn_notification.setVisibility(View.GONE);
				//
				// IMRequest = (Button) findViewById(R.id.im);
				// IMRequest.setVisibility(View.GONE);
				//
				// IMRequest.setBackgroundResource(R.drawable.one);

				// if (CallDispatcher.LoginUser == null) {
				// Intent notes = new Intent(context, CompleteListView.class);
				// startActivity(notes);
				// }

				// ShowList();

				// filter_text = (EditText) findViewById(R.id.filter_text);

				// btnReceiveCall = (Button) view.findViewById(R.id.btncomp);
				// btnReceiveCall.setBackgroundResource(R.drawable.icon_add_notes);
				//
				// btnReceiveCall.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				//
				// try {
				// designSettingsScreen(null, 0, 0);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// }
				// });

				Button selectall = (Button) getActivity().findViewById(
						R.id.btn_brg);
				selectall.setVisibility(View.GONE);

				
				addanaction = (Button) view.findViewById(R.id.addanaction);
				buss_lay = (RelativeLayout) view.findViewById(R.id.buss_lay);
				checkboxfield = getActivity().getIntent().getStringExtra(
						"checkboxfield");

				addanaction.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						buss_lay.setVisibility(View.GONE);
						designSettingsScreen(null, 0, 0);

					}
				});

				arrayList = new ArrayList<String>();
				usersList = new ArrayList<String>();

				if (Alert.readBoolean(context, Alert.satus, true)) {
					
					String condition = Alert.readString(context, Alert.query,
							null);
					if (condition != null) {
						condition = condition.replace("&", "'");
						String resultexists = callDisp.getdbHeler(context)
								.isQueryContainResult(condition,
										CallDispatcher.LoginUser);
						String message = null;
						if (condition.startsWith("No trigger configured")
								|| condition.length() == 0
								|| resultexists.equalsIgnoreCase("true")) {
							if (!fromMenu) {
								showAlert();
								Alert.writeBoolean(context, Alert.satus, false);
							}
						} else {
							if (resultexists.equalsIgnoreCase("false")) {
								message = "There is no Record exists";
								showAlertMessage(message);

							} else if (resultexists.contains("SQLException")) {
								message = "Invalid Query";
								showAlertMessage(message);

							}

						}

						/**
						 * Ends here
						 */
					}
				}
				llContainer = (LinearLayout) view.findViewById(R.id.footer);
				llContainer.setOrientation(LinearLayout.VERTICAL);
				// group = (SegmentedRadioGroup)
				// view.findViewById(R.id.segment_text);
				// group.setOnCheckedChangeListener(this);
				tv_username = (TextView) view.findViewById(R.id.buss_txtusr);
				tv_username.setTextColor(Color.BLACK);
				tv_username.setTypeface(null, Typeface.BOLD);
				tv_username.setText(CallDispatcher.LoginUser);
				tv_username.setTextSize(20);
				wservice_handler = new Handler();
				// if (AppReference.sip_accountID != -1
				// && !AppReference.call_mode.equalsIgnoreCase("twoway")) {
				//
				// CommunicationBean bean = new CommunicationBean();
				// bean.setOperationType(sip_operation_types.MODIFY_ACCOUNT);
				// bean.setRealm(callDisp.getFS());
				// bean.setSipEndpoint(AppReference.sip_registeredid);
				// bean.setMode("twoway");
				// AppReference.call_mode = "twoway";
				// if (AppReference.sipQueue != null)
				// AppReference.sipQueue.addMsg(bean);
				// }
				loadBussinesDatas();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		
		 title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText(getResources().getString(R.string.quickaction));

		return view;

	}

	public Context getContext() {
		return getActivity();

	}

	// protected void ShowList() {
	// try {
	// // TODO Auto-generated method stub
	// setContentView(R.layout.history_container);
	//
	// slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
	// ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
	// callDisp.composeList(datas);
	// slidemenu.init(QuickActionFragment.this, datas, QuickActionFragment.this,
	// 100);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public void showAlert() {

		try {
		
			Log.i("name", "typessss" + type);

			String from = Alert.readString(context, Alert.fromuser, null);
			String to = Alert.readString(context, Alert.touser, null);

			String ftppath2 = Alert.readString(context, Alert.ftppath, null);

			String content = Alert.readString(context, Alert.content, null);

			String type2 = Alert.readString(context, Alert.type, null);
			String qry = Alert.readString(context, Alert.query, null);

			callDisp.showToast(SipNotificationListener.getCurrentContext(),
					"Quick Action Executed");

			callDisp.doAction(content, from, to, ftppath2, content, type2, qry);
			// Setting Dialog Title
			// alertDialog.setTitle("Quick Action");
			//
			// // Setting Dialog Message
			// alertDialog.setMessage("Processing...");
			//
			// // Setting Icon to Dialog
			// alertDialog.setIcon(R.drawable.icon);
			//
			// // Setting OK Button
			// alertDialog.setButton("OK", new DialogInterface.OnClickListener()
			// {
			// public void onClick(DialogInterface dialog, int which) {
			// // Write your code here to execute after dialog closed
			// Toast.makeText(getApplicationContext(),
			// "You clicked on OK", Toast.LENGTH_SHORT).show();
			// }
			// });
			//
			// // Showing Alert Message
			// alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean islableChanged = false;
	private boolean istouserChanged = false;
	private boolean isConditionChanged = false;
	private boolean isUrlChanged = false;
	private boolean istimeChanged = false;

	private Button save = null;
	private Button cancel = null;
	private Button delete = null;

	void designSettingsScreen(final ContactLogicbean bean, final int mode,
			final int position) {
		SingleInstance.quickViewShow = false;
		Button plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.GONE);
		try {
			InputMethodManager inputManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO: handle exception
		}
		buss_lay.setVisibility(View.GONE);

		llContainer.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout rlsecControls = (RelativeLayout) inflater.inflate(
				R.layout.bus_settingss, null);

		llContainer.addView(rlsecControls);

		checkbox = (CheckBox) rlsecControls.findViewById(R.id.checkbox);
		calll = (ImageView) rlsecControls.findViewById(R.id.call);

		username = (TextView) rlsecControls.findViewById(R.id.username);
		call = (LinearLayout) rlsecControls.findViewById(R.id.lcall);
		broadcast = (LinearLayout) rlsecControls
				.findViewById(R.id.lbroadcast);
		share = (LinearLayout) rlsecControls.findViewById(R.id.lshare);
		report = (LinearLayout) rlsecControls.findViewById(R.id.lreport);

		viewflipper = (ViewFlipper) rlsecControls
				.findViewById(R.id.viewflipper);
		audiocall = (RelativeLayout) rlsecControls
				.findViewById(R.id.audiocall);
		videocall = (RelativeLayout) rlsecControls
				.findViewById(R.id.videocall);
		audioconference = (RelativeLayout) rlsecControls
				.findViewById(R.id.audioconference);
		videoconference = (RelativeLayout) rlsecControls
				.findViewById(R.id.videoconference);
		hostedconference = (RelativeLayout) rlsecControls
				.findViewById(R.id.hostedconference);
		audiobroadcast = (RelativeLayout) rlsecControls
				.findViewById(R.id.audiobroadcast);
		videobroadcast = (RelativeLayout) rlsecControls
				.findViewById(R.id.videobroadcast);
		generatereport = (RelativeLayout) rlsecControls
				.findViewById(R.id.generatereport);

		textnote = (RelativeLayout) rlsecControls
				.findViewById(R.id.sharetext);
		audionote = (RelativeLayout) rlsecControls
				.findViewById(R.id.shareaudio);
		videonote = (RelativeLayout) rlsecControls
				.findViewById(R.id.sharevideo);
		photonote = (RelativeLayout) rlsecControls
				.findViewById(R.id.sharephoto);
		handsketch = (RelativeLayout) rlsecControls
				.findViewById(R.id.sharehandsketch);

		et_description = (EditText) rlsecControls
				.findViewById(R.id.et_description);

		audiocal = (TextView) rlsecControls.findViewById(R.id.tv_audio);
		videocal = (TextView) rlsecControls.findViewById(R.id.tv_video);
		audioconferen = (TextView) rlsecControls
				.findViewById(R.id.tv_audioconferen);
		videoconferenc = (TextView) rlsecControls
				.findViewById(R.id.tv_videoconferen);
		hostedconferen = (TextView) rlsecControls
				.findViewById(R.id.tv_hostedconferen);
		menu = (LinearLayout) rlsecControls.findViewById(R.id.menu);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		call.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				viewflipper.setVisibility(View.VISIBLE);
				viewflipper.setDisplayedChild(0);
				viewflipper.showNext();

				return false;
			}
		});

		broadcast.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				viewflipper.setVisibility(View.VISIBLE);
				viewflipper.setDisplayedChild(1);
				viewflipper.showNext();
				return false;
			}
		});

		share.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				viewflipper.setVisibility(View.VISIBLE);
				viewflipper.setDisplayedChild(2);
				viewflipper.showNext();
				return false;

			}
		});
		report.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				viewflipper.setVisibility(View.VISIBLE);
				viewflipper.setDisplayedChild(3);
				viewflipper.showNext();
				return false;
			}
		});

		audiobroadcast.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				TextView tv_title = (TextView) v
						.findViewById(R.id.tv_audiobrca);
				String title = tv_title.getText().toString().trim();
				action = "ABC";
				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.AUDIOBCASTCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("title", title);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("editUser", false);
				startActivity(intent);

				Log.i("welcome", "Title-->" + title);
				action = "ABC";
				return false;

			}
		});

		videobroadcast.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				TextView tv_title = (TextView) v
						.findViewById(R.id.tv_videocast);
				String title = tv_title.getText().toString().trim();

				action = "VBC";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.VIDEOBCASTCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("title", title);
				intent.putExtra("editUser", false);
				startActivity(intent);

				return false;
			}
		});

		textnote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tv_sharetext = (TextView) v.findViewById(R.id.tv_sharetext);
				action = "ST";
				type = "note";
				String title = tv_sharetext.getText().toString().trim();
				Intent intent = new Intent(context, ShareNotePicker.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.TEXTSHARECODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				beanObj.setAction(action);
				beanObj.setEditlable(title);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("note", type);
				intent.putExtra("editFile", false);
				startActivity(intent);

			}
		});

		audionote.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				tv_shareaudio = (TextView) arg0
						.findViewById(R.id.tv_shareaudio);

				String title = tv_shareaudio.getText().toString().trim();
				action = "SA";
				type = "audio";

				Intent intent = new Intent(context, ShareNotePicker.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.AUDIOSHARECODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("note", type);
				intent.putExtra("title", title);
				intent.putExtra("action", action);
				intent.putExtra("editFile", false);
				startActivity(intent);

				return false;
			}
		});

		videonote.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				tv_videonote = (TextView) arg0.findViewById(R.id.tv_videonote);

				String title = tv_videonote.getText().toString().trim();
				action = "SV";
				type = "video";

				Intent intent = new Intent(context, ShareNotePicker.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.VIDEOSHARECODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("note", type);
				intent.putExtra("title", title);
				intent.putExtra("action", action);
				intent.putExtra("editFile", false);
				startActivity(intent);

				return false;
			}
		});

		photonote.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				tv_sharephoto = (TextView) arg0
						.findViewById(R.id.tv_sharephoto);

				String title = tv_sharephoto.getText().toString().trim();
				action = "SP";
				type = "photo";

				Intent intent = new Intent(context, ShareNotePicker.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.IMAGESHARECODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("note", type);
				intent.putExtra("title", title);
				intent.putExtra("action", action);
				intent.putExtra("editFile", false);
				startActivity(intent);

				return false;
			}
		});

		handsketch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				tv_sharehandsketch = (TextView) arg0
						.findViewById(R.id.tv_sharehandsketch);

				String title = tv_sharehandsketch.getText().toString().trim();
				action = "SHS";
				type = "handsketch";

				Intent intent = new Intent(context, ShareNotePicker.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.SKETCHSHARECODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("note", type);
				intent.putExtra("title", title);
				intent.putExtra("action", action);
				intent.putExtra("editFile", false);
				startActivity(intent);

				return false;
			}
		});
		generatereport.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				tv_generatereport = (TextView) v
						.findViewById(R.id.tv_generatereport);

				String title = tv_generatereport.getText().toString().trim();
				// boolean isValid =
				// getIntent().getExtras().getBoolean("isvalid");
				// Intent intent = new Intent(context,
				// QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.REPORTREPORTCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				beanObj.setAction("Show Results Form");

				Intent intent = new Intent(context,
						QuickActionSettingcalls.class);
				intent.putExtra("type", type);
				intent.putExtra("qabean", beanObj);

				startActivity(intent);

				// intent.putExtra("qabean", beanObj);
				// intent.putExtra("title", title);
				// intent.putExtra("editUser", false);
				// startActivity(intent);

				return false;
			}
		});

		audiocall.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				String title = audiocal.getText().toString().trim();
				action = "AC";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.AUDIOCALLCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("title", title);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("editUser", false);
				startActivity(intent);
				return false;
			}
		});

		videocall.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				String title = videocal.getText().toString().trim();
				action = "VC";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.VIDEOCALLCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("title", title);
				intent.putExtra("editUser", false);
				startActivity(intent);

				return false;
			}
		});

		audioconference.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				String title = audioconferen.getText().toString().trim();
				action = "ACF";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.AUDIOCONFCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("title", title);
				intent.putExtra("editUser", false);
				startActivity(intent);

				return false;
			}
		});
		videoconference.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				String title = videoconferenc.getText().toString().trim();
				action = "VCF";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.VIDEOCONFCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("title", title);
				intent.putExtra("editUser", false);
				startActivity(intent);

				return false;
			}
		});
		hostedconference.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				String title = hostedconferen.getText().toString().trim();
				action = "HC";

				Intent intent = new Intent(context,
						QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.HOSTEDCONFCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				intent.putExtra("qabean", beanObj);
				intent.putExtra("title", title);
				intent.putExtra("editUser", false);
				startActivity(intent);

				return false;
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == 100) && (resultCode == -10)) {
			try {
				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("filepath");
					edurl.setText(path);
					// component_id=bun.getString("id");
				}

			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		}

	}

	// public void changeCheckedBtn(int pos) {
	// if (pos == 0) {
	// if (group != null) {
	// RadioButton rbtn = (RadioButton) group
	// .findViewById(R.id.button_one);
	// rbtn.setChecked(true);
	// }
	// } else {
	// if (group != null) {
	// RadioButton rbtn = (RadioButton) group
	// .findViewById(R.id.button_two);
	// rbtn.setChecked(true);
	// }
	// }
	// }

	public void schedule_detail(String runoption) {
		try {
			checkboxfield = runoption;

			Log.i("welcome", "Checkbox field-->" + checkboxfield);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadBussinesDatas() {
		Log.i("quickshow", "loadbasicdata enter");
		if (SingleInstance.quickViewShow) {

			buss_lay.setVisibility(View.GONE);

			designSettingsScreen(null, 0, 0);

		}
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setText("Quick Action");
		plusBtn.setVisibility(View.VISIBLE);

		try {
			// InputMethodManager inputManager = (InputMethodManager)
			// getActivity()
			// .getSystemService(Context.INPUT_METHOD_SERVICE);
			//
			// inputManager.hideSoftInputFromWindow(getActivity()
			// .getCurrentFocus().getWindowToken(),
			// InputMethodManager.HIDE_NOT_ALWAYS);

			llContainer.removeAllViews();

			if (WebServiceReferences.contextTable
					.containsKey("QuickActionSelectcalls"))
				((QuickActionSelectcalls) WebServiceReferences.contextTable
						.get("QuickActionSelectcalls")).finish();

			ArrayList<ContactLogicbean> datasToLoad = callDisp.getdbHeler(
					context).getQucikActionList();
			Log.d("welcome",
					this.getClass().getName()
							+ " after calling DB to get data count "
							+ datasToLoad.size());
			adapterToShow = new BusAdapter(context, R.layout.relate,
					datasToLoad);
			lv = new ListView(context);
			lv.setBackgroundColor(Color.WHITE);
			lv.setCacheColorHint(Color.TRANSPARENT);
			lv.setAdapter(adapterToShow);
			lv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));

			// if (group != null) {
			// group.changeCheckedItem(0);
			// }
			if (datasToLoad.size() == 0) {
				buss_lay.setVisibility(View.VISIBLE);
			}

			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					int Id = adapterToShow.getId(position);
					userid = position;
					doDeleteQA(Id, context);
					return true;
				}
			});

			registerForContextMenu(lv);
			llContainer.addView(lv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		try {
			// TODO Auto-generated method stub

			Log.i("thread",
					"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ method called");
			if (checkedId == R.id.button_one) {
				// loadBussinesDatas();

			} else if (checkedId == R.id.button_two) {
				designSettingsScreen(null, 0, 0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public View getParentView() {
		return view;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					SingleInstance.contextTable.remove("qaction");
					MemoryProcessor.getInstance().unbindDrawables(view);
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void notifyWebServiceResponse1(final Object obj) {

		try {
			Log.i("test", "inside webresponce" + obj.toString());
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					callDisp.cancelDialog();
					if (obj instanceof String[]) {
						String[] response = (String[]) obj;
						String id = response[0];
						String tableid = response[3];

						String formtime = response[2];
						Log.i("records", "id values" + id);
						arrayList.clear();
						ContentValues values = new ContentValues();
						values.put("[tableid]", id);

						if (m_fields.length == list.length) {
							Log.i("ne",
									" equalllll  arraylist insertion lenth size"
											+ arrayList.size());

							for (int idx = 0; idx < m_fields.length; idx++) {
								String columnane = m_fields[idx];
								Log.i("QAA", "QAA 123 :: " + m_fields[idx]);
								values.put(columnane, list[idx]);

							}

							values.put("[recorddate]", formtime);
							values.put("[status]", "1");

							if (callDisp.getdbHeler(context).insertForRecords(
									formname, values)) {

								Log.i("test", "trueeeeeeee" + formname);

								String count = callDisp.getdbHeler(context)
										.getRecordCount("[" + formname + "]");
								Log.i("test", "trueeeeeeee" + count);

								// String strQuery =
								// "update formslookup set rowcount='"
								// + count
								// + "' ,formtime='"
								// + formtime
								// + "' ,status='"
								// + "1"
								// + "' where tableid=" + id;
								// callDisp.getdbHeler(context).ExecuteQuery(
								// strQuery);
								ContentValues cv = new ContentValues();
								cv.put("rowcount", count);
								cv.put("formtime", formtime);
								cv.put("status", 1);
								Log.i("test", "id :" + id);
								Log.i("test", "tableid :" + tableid);

								callDisp.getdbHeler(context)
										.updateFormsRecordCount(cv,
												"tableid='" + tableid + "'");

								Log.i("QAA", "record size :: "
										+ callDisp.rec_list.size());
								if (callDisp.rec_list.size() != 0) {
									callDisp.rec_list.remove(0);
									if (callDisp.rec_list.size() != 0)
										syncqueryMulticolumn(formid);
									else {
										callDisp.cancelDialog();
										showAlert("Do you want to see forms");
									}
								}

							} else {
								Log.i("test", "falseeeee");
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						Log.i("name", "details..." + service_bean.getText());
						// finish();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyWebServiceResponseAccess(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (obj instanceof String[]) {
						String[] response = (String[]) obj;
						Log.i("settings",
								"inside web responce form"
										+ response.toString());

						String buddy = response[0];
						String fsid = response[1];
						String formid = response[2];
						String formcreatedtinme = response[3];
						String editedtime = response[4];
						String mode = response[5];
						if (mode.equals("edit")) {

							ContentValues cv = new ContentValues();

							Log.i("settings", "inside if loop ");
							cv.put("settingid", fsid);
							cv.put("buddyid", buddy);
							cv.put("datecreated", formcreatedtinme);
							cv.put("datemodified", editedtime);

							if (callDisp.getdbHeler(context).updates(formid,
									cv, buddy)) {
								if (!Alert.readString(context, Alert.ToUsers,
										null).equals(null)
										|| !Alert.readString(context,
												Alert.ToUsers, null).equals("")) {

									if (usersList.size() != 0) {
										Log.i("records", "!!!!!!!!querryyyy"
												+ users[0]);
										callAccess(users[0]);
										usersList.remove(0);

									} else {

										showAlert("Do you want to see forms");

									}
								}
							}

						}

						else if (mode.equals("new"))

						{
							Log.i("settings", "inside else loop ");

							Log.i("test", "inside qurynull  loop ");

							String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
									+ "values('"
									+ fsid
									+ "','"
									+ CallDispatcher.LoginUser
									+ "','"
									+ formid
									+ "','"
									+ buddy
									+ "','"
									+ "A04"
									+ "','"
									+ "S02"
									+ "','"
									+ ""
									+ "','"
									+ formcreatedtinme
									+ "','"
									+ editedtime
									+ "')";
							Log.i("settings", "insertion query" + insertQuery1);

							Log.i("forms", "querryyyy" + insertQuery1);

							if (callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1)) {

								if (!Alert.readString(context, Alert.ToUsers,
										null).equals(null)
										|| !Alert.readString(context,
												Alert.ToUsers, null).equals("")) {

									if (usersList.size() != 0) {
										Log.i("ne", "inside  Rules settings ");

										Log.i("records", "!!!!!!!!querryyyy"
												+ users[0]);
										callAccess(users[0]);
										usersList.remove(0);

									} else {
										showAlert("Do you want to see forms");

									}
								}
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						// showToast(service_bean.getText());
						Log.i("a", service_bean.getText() + "errorr message");
						showAlert(service_bean.getText());
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void callAccess(String user) {
		try {
			// TODO Auto-generated method stub

			Log.i("ne",
					"*************inside user access quick action***************user"
							+ user);
			if (user != null && !user.isEmpty()) {

				WebServiceReferences.webServiceClient.SaveAccessForm(
						CallDispatcher.LoginUser, formid, user, "A04", "S02",
						"", "", "new", context, null);

			} else {
				showAlert("Do you want to see forms");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyWebServiceResponse(final Object obj) {
		try {
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (obj instanceof ArrayList) {
						@SuppressWarnings("unchecked")
						ArrayList<String[]> responses = (ArrayList<String[]>) obj;
						Log.i("name",
								"inside web responce form"
										+ responses.toString());
						String[] response = responses.get(0);
						Log.i("test",
								"inside web responce form"
										+ response.toString());

						String id = response[0];
						formid = id;
						String formtime = response[3];
						String fsid = response[4];
						tablename = response[1];
						Log.i("test", "responce" + id);
						Log.i("test", formtime + "form time");

						String insertQuery = "insert into formslookup(owner,tablename,tableid,rowcount,formtime,status)"
								+ "values('"
								+ CallDispatcher.LoginUser
								+ "','"
								+ tablename
								+ "','"
								+ id
								+ "','','"
								+ formtime
								+ "','" + "1" + "')";

						Log.i("test", "querryyyy" + insertQuery);

						// attribute_tablename = "[" + tablename + "_" + formid
						// +
						// "]";

						if (callDisp.getdbHeler(context).ExecuteQuery(
								insertQuery)) {
							Log.i("BL", "----->Form Look up added successfully");

							callDisp.con.add("recorddate");
							callDisp.contype.add("nvarchar(20)");
							callDisp.con.add("status");
							callDisp.contype.add("nvarchar(20)");

							ArrayList<String> type = new ArrayList<String>();
							for (int i = 0; i < callDisp.contype.size(); i++) {
								if (callDisp.contype.get(i).contains("2")) {

									type.add("nvarchar(20)");
									Log.i("a", "*****************" + types);

								} else if (callDisp.contype.get(i)
										.contains("1")) {

									type.add("INTEGER");

									Log.i("a", "*****************" + types);
								} else if (callDisp.contype.get(i).equals(
										"BLOB")) {

									type.add("nvarchar(20)");
								}

							}
							field = new ArrayList<String>();
							Log.i("test",
									"insideeeeeeeeeeeeeee  else formcreator");
							for (int i = 0; i < callDisp.con.size(); i++) {
								String obj = "[" + callDisp.con.get(i) + "]";
								Log.i("BL", obj);

								field.add(obj);
							}

							Log.i("name", "FORMS LOOKUP INSERTED SUCCESSFULLYY");

							String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
									+ "values('"
									+ fsid
									+ "','"
									+ CallDispatcher.LoginUser
									+ "','"
									+ id
									+ "','"
									+ CallDispatcher.LoginUser
									+ "','"
									+ "A04"
									+ "','"
									+ "S02"
									+ "','"
									+ ""
									+ "','"
									+ formtime
									+ "','"
									+ formtime
									+ "')";

							if (callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1)) {
								if (callDisp.getdbHeler(context)
										.createFormTableQA(field,
												tablename + "_" + id, type)) {
									Log.i("BL",
											"----->subtable created successfully");

									formname = tablename + "_" + id;
									String ss = null;
									String val = null;
									callDisp.con.remove("recorddate");
									callDisp.con.remove("status");

									for (int a = 0; a < callDisp.attribute_list
											.size(); a++) {
										String[] fieldvalue = new String[6];
										fieldvalue = callDisp.attribute_list
												.get(a);

										Log.i("welcome",
												"Column name for Database-->"
														+ callDisp.con.get(a));
										Log.i("welcome",
												"Entry Mode for Datatbase-->"
														+ fieldvalue[1]);
										Log.i("welcome", "Valid Data-->"
												+ fieldvalue[2]);
										Log.i("welcome", "Default Value-->"
												+ fieldvalue[3]);
										Log.i("welcome", "Instruction-->"
												+ fieldvalue[4]);
										Log.i("welcome", "Error Tips-->"
												+ fieldvalue[5]);

										Log.i("welcome",
												"Field Name[4] was printing-->"
														+ fieldvalue[4]
																.length());
										if (fieldvalue[4].length() != 0) {
											ss = fieldvalue[4].substring(0,
													fieldvalue[4].length() - 1);

											Log.i("i", "REMOVE" + ss);
										} else {
											ss = "";
										}

										Log.d("welcome",
												"Data Inserted to DB Successfully for new field");
										String tbl_name = "[" + formname + "]";
										String col_name = "["
												+ callDisp.con.get(a) + "]";
										FormAttributeBean faBean = new FormAttributeBean();
										faBean.setDataEntry(fieldvalue[1]);
										faBean.setDataValidation(fieldvalue[2]);
										faBean.setDefaultvalue(fieldvalue[3]);
										faBean.setInstruction(ss);
										faBean.setErrortip("");

										// String insertQueryinfotbl =
										// "insert into forminfo(tablename,column,entrymode,validdata,defaultvalue,instruction,errortip)"
										// + "values('"
										// + "["
										// + formname
										// + "]"
										// + "','"
										// + "["
										// + callDisp.con.get(a)
										// + "]"
										// + "','"
										// + fieldvalue[1]
										// + "','"
										// + fieldvalue[2]
										// + "','"
										// + fieldvalue[3]
										// + "','"
										// + ss
										// + "','" + "" + "')";

										// Log.i("name", "FORMS INFO CREATED  "
										// + insertQueryinfotbl);

										/**
										 * No use of the below condition
										 */
										if (DBAccess.getdbHeler()
												.saveOrUpdateFormInfo(tbl_name,
														col_name, faBean) > 0) {

											String[] useraattribute = null;

											for (int i = 1; i < responses
													.size(); i++) {
												Log.i("IMP",
														"responce----------->inside for loop"
																+ responses
																		.size());

												useraattribute = responses
														.get(i);

												Log.i("IMP",
														"responce----------->inside for loop"
																+ useraattribute.length);

												// String id = response[0];
												// String formname =
												// response[1];
												//
												// String columnname =
												// response[2];
												// String attributeid =
												// response[3];

												String id1 = useraattribute[0];

												String columnname = useraattribute[1];
												Log.i("QAA", "123QA columnname"
														+ useraattribute[1]);

												String attributeid = useraattribute[2];

												int value = Integer
														.parseInt(attributeid);

												ContentValues cv = new ContentValues();

												cv.put("[Id]", value);
												columnname = "[" + columnname
														+ "]";

												// if (callDisp
												// .getdbHeler(context)
												// .updatesAttribute(
												//
												// columnname,
												// cv,
												// "[" + formname
												// + "]")) {
												//
												// }
											}
										}
										/**
										 * Ends
										 * 
										 */

									}

									syncqueryMulticolumn(formid);

									Log.i("i",
											"this is inside else for syncmultiquery");
									// finish();

								}
							}

						}

					}

					else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;

						showAlert(service_bean.getText());
					}
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getNoteCreateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy");
		return sdf.format(curDate);
	}

	public static String getNoteCreateTimes() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		return sdf.format(curDate);
	}

	private void syncqueryMulticolumn(String id) {
		try {
			// TODO Auto-generated method stub

			ismultiview = true;
			if (callDisp.rec_list.size() != 0) {
				{
					records = callDisp.rec_list.get(0);
					arrayList.clear();
					field.remove("[status]");
					field.remove("[recorddate]");
					m_fields = field.toArray(new String[field.size()]);

					Log.i("records", "recc lenth" + records.length);
					Log.i("records", "col lenth" + m_fields.length);

					for (int i = 0; i < m_fields.length; i++) {
						Log.i("QAA", "QAA123 :: fields :: " + m_fields[i]);
					}
					for (int j = 0; j < records.length; j++) {
						Log.i("QAA", "QAA123 :: records :: " + records[j]);
					}

					if (m_fields.length == records.length) {
						for (int i = 0; i < m_fields.length; i++) {

							arrayList.add(records[i]);
							Log.i("ne", "fields:" + m_fields[i]);
							Log.i("ne", "records:" + records[i]);

						}

						// arrayList.add(CallDispatcher.LoginUser);
						// arrayList.add(CallDispatcher.LoginUser);
						// arrayList.add(getNoteCreateTimes());
					} else {

						for (int i = 0; i < m_fields.length; i++) {
							// Log.i("ne", "records:" + records[i]);
							// Log.i("ne", "records:" + records[i+1]);
							// Log.i("ne", "records:" + records[i+2]);
							//
							// arrayList.add(records[i + 2]);
							arrayList.add(records[i]);
							// Log.i("ne", "fields:" + m_fields[i]);
							// Log.i("ne", "records:" + records[i]);

						}
						arrayList.add(CallDispatcher.LoginUser);
						arrayList.add(CallDispatcher.LoginUser);
						arrayList.add(getNoteCreateTimes());
					}

					list = (String[]) arrayList.toArray(new String[arrayList
							.size()]);

					Log.i("ne", "recc lenth" + records.length);
					Log.i("ne", "col lenth" + list.length);

					Log.i("ne", "fiels values size" + m_fields.length);
					Log.i("ne", "list values size" + list.length);

					for (int i = 0; i < m_fields.length; i++) {

						Log.i("ne", "fiels values" + m_fields[i]);

					}
					for (int i = 0; i < list.length; i++) {
						Log.i("ne", "types values" + list[i]);

					}

					if (m_fields.length == list.length) {
						String[] column_field = m_fields;
						ArrayList<String> field_columns = new ArrayList<String>();
						if (column_field != null) {
							for (int i = 0; i < column_field.length; i++) {
								field_columns.add(column_field[i].replace("[",
										"").replace("]", ""));

							}

							WebServiceReferences.webServiceClient
									.addFormRecords(
											CallDispatcher.LoginUser,
											id,
											field_columns
													.toArray(new String[field_columns
															.size()]), list,
											context);
						}
					}

				}

			} else {

				if (!Alert.readString(context, Alert.ToUsers, null)
						.equals(null)
						|| !Alert.readString(context, Alert.ToUsers, null)
								.equals("")) {
					users = Alert.readString(context, Alert.ToUsers, null)
							.split(",");

					Log.i("ne", "*********" + users.toString());
					for (int i = 0; i < users.length; i++) {
						Log.i("ne", "*********inside loop" + users[i]);

						usersList.add(users[i]);

					}
				}

				if (usersList.size() != 0) {
					Log.i("ne", "inside  Rules settings ");

					Log.i("records", "!!!!!!!!querryyyy" + users[0]);
					callAccess(users[0]);
					usersList.remove(0);

				}
				// WebServiceReferences.webServiceClient.SaveAccessForm(CallDispatcher.LoginUser,formid,CallDispatcher.LoginUser,"A04","S02",
				// "", "","new");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void syncquery(String id) {
		try {
			// TODO Auto-generated method stub

			isview = true;
			if (callDisp.rec_list.size() != 0) {
				{
					records = callDisp.rec_list.get(0);
					arrayList.clear();

					Log.i("records", "recc lenth" + records.length);
					Log.i("records", "col lenth" + fields.length);

					for (int i = 0; i < fields.length - 3; i++) {

						arrayList.add(records[i]);
						Log.i("ne", "fields:" + fields[i]);
						Log.i("ne", "records:" + records[i]);

					}
					arrayList.add(CallDispatcher.LoginUser);
					arrayList.add(CallDispatcher.LoginUser);
					arrayList.add(getNoteCreateTimes());

					list = (String[]) arrayList.toArray(new String[arrayList
							.size()]);

					Log.i("ne", "recc lenth" + records.length);
					Log.i("ne", "col lenth" + list.length);

					Log.i("ne", "fiels values size" + fields.length);
					Log.i("ne", "list values size" + list.length);

					for (int i = 0; i < fields.length; i++) {

						Log.i("ne", "fiels values" + fields[i]);

					}
					for (int i = 0; i < list.length; i++) {
						Log.i("ne", "types values" + list[i]);

					}

					if (fields.length == list.length) {

						WebServiceReferences.webServiceClient.addFormRecords(
								CallDispatcher.LoginUser, id, fields, list,
								context);

					}

				}

			} else {

				if (!Alert.readString(context, Alert.ToUsers, null)
						.equals(null)
						|| !Alert.readString(context, Alert.ToUsers, null)
								.equals("")) {
					users = Alert.readString(context, Alert.ToUsers, null)
							.split(",");

					Log.i("ne", "*********" + users.toString());
					for (int i = 0; i < users.length; i++) {
						Log.i("ne", "*********inside loop" + users[i]);

						usersList.add(users[i]);

					}
				}
				WebServiceReferences.webServiceClient.SaveAccessForm(
						CallDispatcher.LoginUser, formid,
						CallDispatcher.LoginUser, "A04", "S02", "", "", "new",
						context, null);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyWebServiceResponseUDP(final Object object) {
		try {
			// TODO Auto-generated method stub
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (object instanceof Vector) {
						Vector<Object> resultObject = (Vector<Object>) object;
						for (int i = 0; i < resultObject.size(); i++) {
							if (resultObject.get(i) instanceof String[]) {
								String[] response = (String[]) resultObject
										.get(i);
								Log.i("settings",
										"inside FORM PERMISSIONVIEWER form"
												+ response.toString());

								String buddy = response[0];
								String fsid = response[1];
								String formid = response[2];
								String createddate = response[3];
								String modifieddate = response[4];
								String syncquery = response[5];
								String permissionid = response[6];
								String syncid = response[7];
								String owner = response[8];

								Log.i("settings", "$$$$$$$$$$$$fsid fsid"
										+ fsid);

								int count = callDisp.getdbHeler(context)
										.getreocrdcountUDP(formid, fsid, owner,
												buddy);

								Log.i("settings", "counttttttttttttttttt"
										+ count);
								if (count == 0) {
									Log.i("settings", "$$$$$$$$$$$$count zero ");
									if (owner != null) {
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
												+ syncquery
												+ "','"
												+ ""
												+ "','"
												+ createddate
												+ "','"
												+ modifieddate + "')";
										Log.i("settings", "insertion query"
												+ insertQuery1);

										Log.i("forms", "querryyyy"
												+ insertQuery1);

										callDisp.getdbHeler(context)
												.ExecuteQuery(insertQuery1);
									} else {

										String insertQuery1 = "insert into formsettings(settingid,formowenerid,formid,buddyid,accesscode,synccode,syncquery,datecreated,datemodified)"
												+ "values('"
												+ fsid
												+ "','"
												+ CallDispatcher.LoginUser
												+ "','"
												+ formid
												+ "','"
												+ buddy
												+ "','"
												+ permissionid
												+ "','"
												+ syncquery
												+ "','"
												+ ""
												+ "','"
												+ createddate
												+ "','" + modifieddate + "')";
										Log.i("settings", "insertion query"
												+ insertQuery1);

										Log.i("forms", "querryyyy"
												+ insertQuery1);

										callDisp.getdbHeler(context)
												.ExecuteQuery(insertQuery1);
									}

								} else {

									Log.i("settings", "$$$$$$$$$$$$update else"
											+ fsid);

									ContentValues cv = new ContentValues();

									Log.i("settings", "inside if loop ");

									cv.put("accesscode", permissionid);
									cv.put("synccode", syncid);
									cv.put("syncquery", syncquery);
									cv.put("datecreated", createddate);
									cv.put("datemodified", modifieddate);

									callDisp.getdbHeler(context).updates(fsid,
											cv, formid);

								}
							} else if (resultObject.get(i) instanceof Vector) {
								Vector<Vector<FormFieldBean>> fieldAccessList = (Vector<Vector<FormFieldBean>>) resultObject
										.get(i);
								Log.i("formfield123",
										"parse result totallist size : "
												+ fieldAccessList.size());
								Vector<FormFieldBean> ownerList = fieldAccessList
										.get(0);
								Log.i("formfield123",
										"parse result ownerlist size : "
												+ ownerList.size());
								for (FormFieldBean formFieldBean : ownerList) {
									if (formFieldBean
											.getDefaultPermissionList() != null) {
										Vector<DefaultPermission> dList = formFieldBean
												.getDefaultPermissionList();
										for (DefaultPermission defaultPermission : dList) {
											DBAccess.getdbHeler()
													.saveOrUpdateOwnerFormField(
															formFieldBean
																	.getFormId(),
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
									if (formFieldBean
											.getIndividualPermissionList() != null) {
										Vector<IndividualPermission> iBeanList = formFieldBean
												.getIndividualPermissionList();
										for (IndividualPermission iBean : iBeanList) {
											DBAccess.getdbHeler()
													.saveOrUpdateIndividualFormField(
															formFieldBean
																	.getFormId(),
															iBean.getAttributeId(),
															iBean.getUserName(),
															iBean.getPermission());
										}
									}
								}

							}
						}
					} else if (object instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) object;
						// showToast(service_bean.getText());

						showAlert(service_bean.getText());
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doDeleteQA(final int Id, final Context context) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			builder.setMessage("Are you sure you want to delete?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									String query = null;
									String runMode = callDisp.getdbHeler(
											context).getRunMode(Id);
									if (runMode.equalsIgnoreCase("RP")) {
										Intent intent = new Intent(
												context,
												QuickActionBroadCastReceiver.class);
										PendingIntent alarmIntent = PendingIntent
												.getBroadcast(context, Id,
														intent, 0);
										AlarmManager alarmMgr = (AlarmManager) context
												.getSystemService(Context.ALARM_SERVICE);
										alarmMgr.cancel(alarmIntent);
									}
									query = "delete from CustomAction where Id="
											+ Id;
									boolean delete = DBAccess.getdbHeler(
											context).ExecuteQuery(query);
									if (delete) {
										adapterToShow.deleteRecord(userid);
										handler.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												adapterToShow
														.notifyDataSetChanged();
											}
										});

										if (WebServiceReferences.contextTable
												.containsKey("QuickActionTitlecalls")) {
											((QuickActionTitlecalls) WebServiceReferences.contextTable
													.get("QuickActionTitlecalls"))
													.finish();
										}
										if (WebServiceReferences.contextTable
												.containsKey("QuickActionSelectcalls")) {
											((QuickActionSelectcalls) WebServiceReferences.contextTable
													.get("QuickActionSelectcalls"))
													.finish();
										}
									}
									loadBussinesDatas();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert1 = builder.create();
			alert1.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showalert() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(
					" Do u want to go back without saving your changes? ")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										// loadBussinesDatas();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			// filter_text.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<OfflineRequestConfigBean> config_list;

	public void notifyOfflineCallResponse(final Object obj) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					CallDispatcher.pdialog = new ProgressDialog(context);
					callDisp.cancelDialog();
					if (obj instanceof ArrayList) {
						ArrayList<Object> callresponse_list = (ArrayList<Object>) obj;
						if (callresponse_list.size() == 3) {
							String user_id = null;
							String buddy_id = null;
							if (callresponse_list.get(0) instanceof String)
								user_id = (String) callresponse_list.get(0);
							if (callresponse_list.get(1) instanceof String)
								buddy_id = (String) callresponse_list.get(1);

							if (callresponse_list.get(2) instanceof ArrayList) {
								if (config_list != null)
									config_list.clear();

								config_list = (ArrayList<OfflineRequestConfigBean>) callresponse_list
										.get(2);
							}

							if (config_list != null) {
								for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {
									ContentValues cv = new ContentValues();
									cv.put("config_id",
											offlineRequestConfigBean.getId());
									String to = Alert.readString(context,
											Alert.touser, null);
									cv.put("fromuser", buddy_id);
									cv.put("messagetitle",
											offlineRequestConfigBean
													.getMessageTitle());
									cv.put("messagetype",
											offlineRequestConfigBean
													.getMessagetype());
									cv.put("responsetype",
											offlineRequestConfigBean
													.getResponseType());
									cv.put("response", "''");
									cv.put("url",
											offlineRequestConfigBean.getUrl());
									cv.put("receivedtime", CompleteListView
											.getNoteCreateTime());
									cv.put("sendstatus", "0");
									cv.put("username", CallDispatcher.LoginUser);

									cv.put("message", offlineRequestConfigBean
											.getMessage());
									cv.put("status", 0);

									int id = callDisp.getdbHeler(context)
											.insertOfflinePendingClones(cv);
									offlineRequestConfigBean.setId(Integer
											.toString(id));
									offlineRequestConfigBean
											.setReceivedTime(CompleteListView
													.getNoteCreateTime());
									offlineRequestConfigBean.setStatus(0);

									if (offlineRequestConfigBean.getMessage() != null) {
										String message_path = Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/"
												+ offlineRequestConfigBean
														.getMessage();
										File message_file = new File(
												message_path);
										if (!message_file.exists()) {
											offlineRequestConfigBean
													.setStatus(1);
											callDisp.downloadOfflineresponse(
													offlineRequestConfigBean
															.getMessage(),
													offlineRequestConfigBean
															.getId(),
													"answering machine", null);
										}

										message_file = null;
										message_path = null;
									}
								}
							}
							if (!SingleInstance.instanceTable
									.containsKey("callscreen")) {
								Intent intent = new Intent(context,
										AnsweringMachineActivity.class);
								intent.putExtra("buddy", buddy_id);
								startActivity(intent);
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						showAlert(service_bean.getText());
						CallDispatcher.pdialog = new ProgressDialog(context);
						callDisp.cancelDialog();
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void downloadOfflineresponse(OfflineRequestConfigBean bean) {
	// try {
	// if (bean != null) {
	// if (CallDispatcher.LoginUser != null) {
	// ContentValues cv = new ContentValues();
	// cv.put("status", 1);
	// callDisp.getdbHeler(context)
	// .updateOfflineCallPendingClones(cv,
	// "id=" + bean.getId());
	// cv = null;
	// bean.setStatus(1);
	// FTPBean ftpBean = new FTPBean();
	// ftpBean.setServer_ip(callDisp.getRouter().split(":")[0]);
	// ftpBean.setServer_port(40400);
	// ftpBean.setFtp_username("ftpadmin");
	// ftpBean.setFtp_password("ftppassword");
	// ftpBean.setFile_path(bean.getMessage());
	// ftpBean.setOperation_type(2);
	// ftpBean.setReq_object(bean.getId());
	// ftpBean.setRequest_from("answering machine");
	// callDisp.getFtpNotifier().addTasktoExecutor(ftpBean);
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private void showAlertMessage(String message) {
		try {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
			myAlertDialog.setTitle("Response");
			myAlertDialog.setMessage(message);
			myAlertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// do something when the OK button is clicked

							arg0.cancel();

							// FormsActivity

							// frm_creator=(FormsActivity)WebServiceReferences.contextTable.get("formactivity");
							// String[] tb=tablename.split("_");
							// frm_creator.notifylistChanged(tb[0], tb[1],
							// CallDispatcher.LoginUser);

						}
					});
			myAlertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showAlert(String message) {
		try {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
			myAlertDialog.setTitle("Response");
			myAlertDialog.setMessage(message);
			myAlertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
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
					});
			myAlertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {
							// do something when the Cancel button is clicked

							dialog.cancel();
						}
					});
			myAlertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!SingleInstance.quickViewShow) {
			Button plusBtn = (Button) getActivity()
					.findViewById(R.id.add_group);
			//plusBtn.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}

	public ArrayList<BuddyBean> getBuddyBean() {
		CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		String[] choiceList = callDisp.getmyBuddysQA();

		ArrayList<BuddyBean> array_buddybean = new ArrayList<BuddyBean>();
		for (String string : choiceList) {
			BuddyBean buddyBean = new BuddyBean();
			buddyBean.setBuddyName(string);
			buddyBean.setSelect(false);
			array_buddybean.add(buddyBean);
		}
		return array_buddybean;

	}

	public ArrayList<BuddyBean> getBuddyBeanEdit(String name) {
		CallDispatcher callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");
		String[] choiceList = callDisp.getmyBuddysQA();
		String[] arrayList = null;
		HashMap<String, String> buddy = new HashMap<String, String>();
		if (name.contains(",")) {
			arrayList = name.split(",");
			for (int i = 0; i < arrayList.length; i++) {
				buddy.put(arrayList[i], arrayList[i]);
			}

		}
		ArrayList<BuddyBean> array_buddybean = new ArrayList<BuddyBean>();
		for (String string : choiceList)

		{
			if (arrayList != null && arrayList.length > 0) {
				Log.i("IOS", "arraylist not null=====>" + string);
				BuddyBean buddyBean = new BuddyBean();

				if (buddy.containsKey(string)) {
					buddyBean.setBuddyName(string);
					buddyBean.setSelect(true);
				} else {
					buddyBean.setBuddyName(string);
					buddyBean.setSelect(false);
				}
				array_buddybean.add(buddyBean);

			} else {
				BuddyBean buddyBean = new BuddyBean();

				if (name.equalsIgnoreCase(string)) {
					buddyBean.setBuddyName(string);
					buddyBean.setSelect(true);
				} else {
					buddyBean.setBuddyName(string);
					buddyBean.setSelect(false);
				}
				array_buddybean.add(buddyBean);
			}
		}
		Log.i("IOS", "sizeeee=====>" + array_buddybean.size());

		return array_buddybean;

	}

	public void call_setting(String frequency, String time, String Timemode) {
		try {
			Intent intent = new Intent(context, QuickActionCallsSchedule.class);
			intent.putExtra("mode", "Edit");
			intent.putExtra("spinnerfield", Timemode);
			intent.putExtra("frequencyfield", frequency);
			intent.putExtra("datefield", time);
			context.startActivity(intent);
			Log.i("welcome", "bean.getFrequncy()---" + frequency);
			Log.i("welcome", "bean.getTime()---" + time);
			Log.i("welcome", "bean.gettimeMode()---" + Timemode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void uploadConfiguredNote(String filepath) {
	}

	void showprgress() {

		try {
			progDailog = ProgressDialog.show(context, "Progress ",
					"Form Creating..", true);
			progDailog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated
					// method
					// stub
					if (progDailog != null) {
						progDailog.dismiss();
					}

				}
			});

			progDailog.setCancelable(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// // TODO Auto-generated method stub
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// AlertDialog.Builder buider = new AlertDialog.Builder(context);
	// buider.setMessage(
	// "Are you sure, You want to Send this application to Background ?")
	// .setPositiveButton("Yes",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// // finish();
	// moveTaskToBack(true);
	// // return true;
	//
	// }
	// })
	// .setNegativeButton("No",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// // TODO Auto-generated method stub
	// dialog.cancel();
	// }
	// });
	// AlertDialog alert = buider.create();
	// alert.show();
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }

	// public void notifyProfilepictureDownloaded() {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (slidemenu != null) {
	// if (slidemenu.isMenuShowing())
	// slidemenu.refreshItem();
	// }
	// }
	// });
	//
	// }
	//
	// @Override
	// public void onSlideMenuItemClick(int itemId, View v, Context context) {
	// // TODO Auto-generated method stub
	// switch (itemId) {
	// case WebServiceReferences.CONTACTS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.USERPROFILE:
	//
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.UTILITY:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.NOTES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.APPS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.CLONE:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.SETTINGS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	//
	// case WebServiceReferences.QUICK_ACTION:
	// loadBussinesDatas();
	// break;
	// case WebServiceReferences.FORMS:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// if (CallDispatcher.LoginUser != null)
	// finish();
	// break;
	// case WebServiceReferences.FEEDBACK:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// case WebServiceReferences.EXCHANGES:
	// callDisp.onSlideMenuItemClick(itemId, v, context);
	// finish();
	// break;
	// default:
	// break;
	// }
	// }

	public void isFromMenu(boolean fromMenu) {
		this.fromMenu = fromMenu;
	}

}
