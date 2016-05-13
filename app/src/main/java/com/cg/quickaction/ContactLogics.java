package com.cg.quickaction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.adapters.BusAdapter;
import com.cg.avatar.AnsweringMachineActivity;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListBean;
import com.cg.files.CompleteListView;
import com.cg.forms.Alert;
import com.cg.forms.FormsActivity;
import com.cg.ftpprocessor.FTPBean;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class ContactLogics extends Activity implements
		OnCheckedChangeListener,
		SlideMenuInterface.OnSlideMenuItemClickListener {

	LinearLayout llContainer = null;
	Context context = null;
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
	Button btn_notification = null;
	Button IMRequest = null;
	Button btn_cancel = null;
	Button btnReceiveCall = null;
	Button btn_delete = null;
	SegmentedRadioGroup group = null;
	CallDispatcher callDisp;
	AlertDialog.Builder builder;
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
	private TextView title = null;	
	ArrayAdapter<String> adapter;
	List<String> menusList = new ArrayList<String>();
	String strDateTime, id = null;
	boolean entry = false;
	String type = null;
	String[] myServices = {
			SingleInstance.mainContext.getResources().getString(
					R.string.share_photo),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_audio),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_text), SingleInstance.mainContext.getResources().getString(
							R.string.share_video), SingleInstance.mainContext.getResources().getString(
									R.string.share_handsketch),
									SingleInstance.mainContext.getResources().getString(
											R.string.audio_call),SingleInstance.mainContext.getResources().getString(
													R.string.video_call), SingleInstance.mainContext.getResources().getString(
									R.string.audio_broadcast_bc), SingleInstance.mainContext.getResources().getString(
											R.string.video_broadcast_bc),
											SingleInstance.mainContext.getResources().getString(
													R.string.audio_conference), SingleInstance.mainContext.getResources().getString(
															R.string.video_conference), SingleInstance.mainContext.getResources().getString(
																	R.string.show_results_form) };

	ArrayList<String> arrayList = new ArrayList<String>();;
	ArrayList<String> usersList = new ArrayList<String>();
	boolean ismultiview = false;
	String[] users = null;

	private SlideMenu slidemenu;
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

	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			qabuilder = QuickActionBuilder
					.getInstance(CallDispatcher.LoginUser);
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			context = this;
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			WebServiceReferences.contextTable.put("SecContatct", this);
			builder = new AlertDialog.Builder(this);
			title = (TextView) findViewById(R.id.note_date);
			title.setText(SingleInstance.mainContext.getResources().getString(
									R.string.quick_actions));
			menusList.add("Run");
			menusList.add("Edit");
			menusList.add("Delete");
			CallDispatcher.pdialog = new ProgressDialog(context);
			btn_cancel = (Button) findViewById(R.id.settings);
			btn_cancel.setBackgroundResource(R.drawable.icon_sidemenu);
			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					slidemenu.show();
				}
			});
			btn_notification = (Button) findViewById(R.id.notification);
			btn_notification.setVisibility(View.GONE);

			IMRequest = (Button) findViewById(R.id.im);
			IMRequest.setVisibility(View.GONE);

			IMRequest.setBackgroundResource(R.drawable.one);

			if (CallDispatcher.LoginUser == null) {
				Intent notes = new Intent(context, CompleteListView.class);
				startActivity(notes);
			}

			ShowList();

			// filter_text = (EditText) findViewById(R.id.filter_text);

			btnReceiveCall = (Button) findViewById(R.id.btncomp);
			btnReceiveCall.setBackgroundResource(R.drawable.icon_add_notes);

			btnReceiveCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						designSettingsScreen(null, 0, 0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btn_delete = (Button) findViewById(R.id.trash);
			btn_delete.setVisibility(View.INVISIBLE);

			setContentView(R.layout.buslogics);
			addanaction = (Button) findViewById(R.id.addanaction);
			buss_lay = (RelativeLayout) findViewById(R.id.buss_lay);
			checkboxfield = getIntent().getStringExtra("checkboxfield");

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

			if (Alert.readBoolean(getApplicationContext(), Alert.satus, true)) {
				
				String condition = Alert.readString(context, Alert.query, null);
				if (condition != null) {
					condition = condition.replace("&", "'");
					String resultexists = callDisp.getdbHeler(context)
							.isQueryContainResult(condition,
									CallDispatcher.LoginUser);
					String message = null;
					if (condition.startsWith("No trigger configured")
							|| condition.length() == 0
							|| resultexists.equalsIgnoreCase("true")) {

						showAlert();
						Alert.writeBoolean(getApplicationContext(),
								Alert.satus, false);

					} else {
						if (resultexists.equalsIgnoreCase("false")) {
							message = SingleInstance.mainContext.getResources().getString(R.string.there_is_no_record_exists);
							showAlertMessage(message);

						} else if (resultexists.contains("SQLException")) {
							message = SingleInstance.mainContext.getResources().getString(R.string.invalid_query);
							showAlertMessage(message);

						}

					}

					/**
					 * Ends here
					 */
				}
			}
			llContainer = (LinearLayout) findViewById(R.id.footer);
			llContainer.setOrientation(LinearLayout.VERTICAL);
			group = (SegmentedRadioGroup) findViewById(R.id.segment_text);
			group.setOnCheckedChangeListener(this);
			tv_username = (TextView) findViewById(R.id.buss_txtusr);
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

	}

	protected void ShowList() {
		try {
			// TODO Auto-generated method stub
			setContentView(R.layout.history_container);

			slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
			ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
			callDisp.composeList(datas);
			slidemenu.init(ContactLogics.this, datas, ContactLogics.this,
					100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showAlert() {

		try {
			// Context appContext = this.getApplicationContext();
			// AlertDialog alertDialog = new AlertDialog.Builder(

			Log.i("name", "typessss" + type);

			String from = Alert.readString(context, Alert.fromuser, null);
			String to = Alert.readString(context, Alert.touser, null);

			String ftppath2 = Alert.readString(context, Alert.ftppath, null);

			String content = Alert.readString(context, Alert.content, null);

			String type2 = Alert.readString(context, Alert.type, null);
			String qry = Alert.readString(context, Alert.query, null);

			callDisp.showToast(SipNotificationListener.getCurrentContext(),
					SingleInstance.mainContext.getResources().getString(R.string.quick_action_executed));

			callDisp.doAction(content, from, to, ftppath2, content, type2, qry);
			// Setting Dialog Title
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

		try {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO: handle exception
		}
		buss_lay.setVisibility(View.GONE);

		llContainer.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout rlSecControls = (RelativeLayout) inflater.inflate(
				R.layout.bus_settingss, null);

		llContainer.addView(rlSecControls);

		checkbox = (CheckBox) rlSecControls.findViewById(R.id.checkbox);
		calll = (ImageView) rlSecControls.findViewById(R.id.call);

		username = (TextView) rlSecControls.findViewById(R.id.username);
		call = (LinearLayout) rlSecControls.findViewById(R.id.lcall);
		broadcast = (LinearLayout) rlSecControls
				.findViewById(R.id.lbroadcast);
		share = (LinearLayout) rlSecControls.findViewById(R.id.lshare);
		report = (LinearLayout) rlSecControls.findViewById(R.id.lreport);

		viewflipper = (ViewFlipper) rlSecControls
				.findViewById(R.id.viewflipper);
		audiocall = (RelativeLayout) rlSecControls
				.findViewById(R.id.audiocall);
		videocall = (RelativeLayout) rlSecControls
				.findViewById(R.id.videocall);
		audioconference = (RelativeLayout) rlSecControls
				.findViewById(R.id.audioconference);
		videoconference = (RelativeLayout) rlSecControls
				.findViewById(R.id.videoconference);
		hostedconference = (RelativeLayout) rlSecControls
				.findViewById(R.id.hostedconference);
		audiobroadcast = (RelativeLayout) findViewById(R.id.audiobroadcast);
		videobroadcast = (RelativeLayout) findViewById(R.id.videobroadcast);
		generatereport = (RelativeLayout) rlSecControls
				.findViewById(R.id.generatereport);

		textnote = (RelativeLayout) rlSecControls
				.findViewById(R.id.sharetext);
		audionote = (RelativeLayout) rlSecControls
				.findViewById(R.id.shareaudio);
		videonote = (RelativeLayout) rlSecControls
				.findViewById(R.id.sharevideo);
		photonote = (RelativeLayout) rlSecControls
				.findViewById(R.id.sharephoto);
		handsketch = (RelativeLayout) rlSecControls
				.findViewById(R.id.sharehandsketch);

		et_description = (EditText) rlSecControls
				.findViewById(R.id.et_description);

		audiocal = (TextView) rlSecControls.findViewById(R.id.tv_audio);
		videocal = (TextView) rlSecControls.findViewById(R.id.tv_video);
		audioconferen = (TextView) rlSecControls
				.findViewById(R.id.tv_audioconferen);
		videoconferenc = (TextView) rlSecControls
				.findViewById(R.id.tv_videoconferen);
		hostedconferen = (TextView) rlSecControls
				.findViewById(R.id.tv_hostedconferen);
		menu = (LinearLayout) rlSecControls.findViewById(R.id.menu);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				TextView tv_title = (TextView) findViewById(R.id.tv_audiobrca);
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

				TextView tv_title = (TextView) findViewById(R.id.tv_videocast);
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

				tv_sharetext = (TextView) findViewById(R.id.tv_sharetext);
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
				tv_shareaudio = (TextView) findViewById(R.id.tv_shareaudio);

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

				tv_videonote = (TextView) findViewById(R.id.tv_videonote);

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
				tv_sharephoto = (TextView) findViewById(R.id.tv_sharephoto);

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
				tv_sharehandsketch = (TextView) findViewById(R.id.tv_sharehandsketch);

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
				tv_generatereport = (TextView) findViewById(R.id.tv_generatereport);

				String title = tv_generatereport.getText().toString().trim();
				// boolean isValid =
				// getIntent().getExtras().getBoolean("isvalid");
				// Intent intent = new Intent(context,
				// QuickActionSelectcalls.class);
				ContactLogicbean beanObj = QuickActionBuilder
						.getSecLogicbean(CallDispatcher.LoginUser,
								QuickActionType.REPORTREPORTCODE);
				beanObj.setFromuser(CallDispatcher.LoginUser);
				beanObj.setAction(SingleInstance.mainContext.getResources().getString(R.string.show_results_form));

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

	public void changeCheckedBtn(int pos) {
		if (pos == 0) {
			if (group != null) {
				RadioButton rbtn = (RadioButton) group
						.findViewById(R.id.button_one);
				rbtn.setChecked(true);
			}
		} else {
			if (group != null) {
				RadioButton rbtn = (RadioButton) group
						.findViewById(R.id.button_two);
				rbtn.setChecked(true);
			}
		}
	}

	public void schedule_detail(String runoption) {
		checkboxfield = runoption;

		Log.i("welcome", "Checkbox field-->" + checkboxfield);

	}

	public void loadBussinesDatas() {
		try {
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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

			if (group != null) {
				group.changeCheckedItem(0);
			}
			if (datasToLoad.size() == 0) {
				buss_lay.setVisibility(View.VISIBLE);
			}

			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					int Id = adapterToShow.getId(position);
					userid = position;
					// doDeleteQA(Id, context);
					return true;
				}
			});

			registerForContextMenu(lv);
			llContainer.addView(lv);
		} catch (Exception e) {
			// TODO: handle exception
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
				loadBussinesDatas();

			} else if (checkedId == R.id.button_two) {
				designSettingsScreen(null, 0, 0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub
			super.onDestroy();
			WebServiceReferences.contextTable.remove("SecContatct");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyWebServiceResponse1(final Object obj) {

		try {
			Log.i("test", "inside webresponce" + obj.toString());
			wservice_handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
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
										showAlert(SingleInstance.mainContext.getResources().getString(R.string.do_you_want_to_see_forms));
									}
								}

							} else {
								Log.i("test", "falseeeee");
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						Log.i("name", "details..." + service_bean.getText());
						finish();
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

										showAlert(SingleInstance.mainContext.getResources().getString(R.string.do_you_want_to_see_forms));

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
										showAlert(SingleInstance.mainContext.getResources().getString(R.string.do_you_want_to_see_forms));

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
		// TODO Auto-generated method stub

		Log.i("ne",
				"*************inside user access quick action***************user"
						+ user);
		if (user != null && !user.isEmpty()) {

			// WebServiceReferences.webServiceClient.SaveAccessForm(
			// CallDispatcher.LoginUser, formid, user, "A04", "S02", "",
			// "", "new");

		} else {
			showAlert("Do you want to see forms");
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

										String insertQueryinfotbl = "insert into forminfo(tablename,column,entrymode,validdata,defaultvalue,instruction,errortip)"
												+ "values('"
												+ "["
												+ formname
												+ "]"
												+ "','"
												+ "["
												+ callDisp.con.get(a)
												+ "]"
												+ "','"
												+ fieldvalue[1]
												+ "','"
												+ fieldvalue[2]
												+ "','"
												+ fieldvalue[3]
												+ "','"
												+ ss
												+ "','" + "" + "')";
										Log.i("name", "FORMS INFO CREATED  "
												+ insertQueryinfotbl);
										if (callDisp.getdbHeler(context)
												.ExecuteQuery(
														insertQueryinfotbl)) {

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

					for (int i = 0; i < m_fields.length - 3; i++) {

						arrayList.add(records[i + 2]);
						Log.i("ne", "fields:" + m_fields[i]);
						Log.i("ne", "records:" + records[i]);

					}
					arrayList.add(CallDispatcher.LoginUser);
					arrayList.add(CallDispatcher.LoginUser);
					arrayList.add(getNoteCreateTimes());
				}

				list = (String[]) arrayList
						.toArray(new String[arrayList.size()]);

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
							field_columns.add(column_field[i].replace("[", "")
									.replace("]", ""));

						}

						// WebServiceReferences.webServiceClient.addFormRecords(
						// CallDispatcher.LoginUser, id, field_columns
						// .toArray(new String[field_columns
						// .size()]), list);
					}
				}

			}

		} else {

			if (!Alert.readString(context, Alert.ToUsers, null).equals(null)
					|| !Alert.readString(context, Alert.ToUsers, null).equals(
							"")) {
				users = Alert.readString(context, Alert.ToUsers, null).split(
						",");

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

	}

	private void syncquery(String id) {
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

				list = (String[]) arrayList
						.toArray(new String[arrayList.size()]);

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

					// WebServiceReferences.webServiceClient.addFormRecords(
					// CallDispatcher.LoginUser, id, fields, list);

				}

			}

		} else {

			if (!Alert.readString(context, Alert.ToUsers, null).equals(null)
					|| !Alert.readString(context, Alert.ToUsers, null).equals(
							"")) {
				users = Alert.readString(context, Alert.ToUsers, null).split(
						",");

				Log.i("ne", "*********" + users.toString());
				for (int i = 0; i < users.length; i++) {
					Log.i("ne", "*********inside loop" + users[i]);

					usersList.add(users[i]);

				}
			}
			// WebServiceReferences.webServiceClient.SaveAccessForm(
			// CallDispatcher.LoginUser, formid, CallDispatcher.LoginUser,
			// "A04", "S02", "", "", "new");

		}

	}

	public void notifyWebServiceResponseUDP(final Object obj) {
		// TODO Auto-generated method stub
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (obj instanceof String[]) {
					String[] response = (String[]) obj;
					Log.i("settings", "inside FORM PERMISSIONVIEWER form"
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

					Log.i("settings", "$$$$$$$$$$$$fsid fsid" + fsid);

					int count = callDisp.getdbHeler(context).getreocrdcountUDP(
							formid, fsid, owner, buddy);

					Log.i("settings", "counttttttttttttttttt" + count);
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
							Log.i("settings", "insertion query" + insertQuery1);

							Log.i("forms", "querryyyy" + insertQuery1);

							callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1);
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
									+ createddate + "','" + modifieddate + "')";
							Log.i("settings", "insertion query" + insertQuery1);

							Log.i("forms", "querryyyy" + insertQuery1);

							callDisp.getdbHeler(context).ExecuteQuery(
									insertQuery1);
						}

					} else {

						Log.i("settings", "$$$$$$$$$$$$update else" + fsid);

						ContentValues cv = new ContentValues();

						Log.i("settings", "inside if loop ");

						cv.put("accesscode", permissionid);
						cv.put("synccode", syncid);
						cv.put("syncquery", syncquery);
						cv.put("datecreated", createddate);
						cv.put("datemodified", modifieddate);

						callDisp.getdbHeler(context).updates(fsid, cv, formid);

					}

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean service_bean = (WebServiceBean) obj;
					// showToast(service_bean.getText());

					showAlert(service_bean.getText());
				}
			}
		});
	}

	// public void doDeleteQA(final int Id, final Context context) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	//
	// builder.setMessage("Are you sure you want to delete?")
	// .setCancelable(false)
	// .setPositiveButton("Yes",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// String query = null;
	// String runMode = callDisp.getdbHeler(context)
	// .getRunMode(Id);
	// if (runMode.equalsIgnoreCase("RP")) {
	// if (CallDispatcher.quickActionMap
	// .containsKey(Id))
	// CallDispatcher.quickActionMap.get(Id)
	// .cancel();
	// CallDispatcher.quickActionMap.remove(Id);
	// }
	// query = "delete from CustomAction where Id="
	// + Id;
	// Log.i("QAA", "delete query : " + query);
	// boolean delete = callDisp.getdbHeler(context)
	// .ExecuteQuery(query);
	// if (delete) {
	// adapterToShow.deleteRecord(userid);
	// adapterToShow.notifyDataSetChanged();
	// if (WebServiceReferences.contextTable
	// .containsKey("QuickActionTitlecalls")) {
	// ((QuickActionTitlecalls) WebServiceReferences.contextTable
	// .get("QuickActionTitlecalls"))
	// .finish();
	// }
	// if (WebServiceReferences.contextTable
	// .containsKey("QuickActionSelectcalls")) {
	// ((QuickActionSelectcalls) WebServiceReferences.contextTable
	// .get("QuickActionSelectcalls"))
	// .finish();
	// }
	// }
	// }
	// })
	// .setNegativeButton("No", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	// dialog.cancel();
	// }
	// });
	// AlertDialog alert1 = builder.create();
	// alert1.show();
	// }

	public void showalert() {
		builder.setMessage(
				SingleInstance.mainContext.getResources().getString(R.string.do_u_want_to_go_back_without_saving_your_changes))
				.setCancelable(false)
				.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									loadBussinesDatas();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		// filter_text.setVisibility(View.VISIBLE);

	}

	public ArrayList<OfflineRequestConfigBean> config_list;

	public void notifyOfflineCallResponse(final Object obj) {
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
								cv.put("messagetitle", offlineRequestConfigBean
										.getMessageTitle());
								cv.put("messagetype", offlineRequestConfigBean
										.getMessagetype());
								cv.put("responsetype", offlineRequestConfigBean
										.getResponseType());
								cv.put("response", "''");
								cv.put("url", offlineRequestConfigBean.getUrl());
								cv.put("receivedtime",
										CompleteListView.getNoteCreateTime());
								cv.put("sendstatus", "0");
								cv.put("username", CallDispatcher.LoginUser);

								cv.put("message",
										offlineRequestConfigBean.getMessage());
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
									File message_file = new File(message_path);
									if (!message_file.exists()) {
										offlineRequestConfigBean.setStatus(1);
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
						if (!WebServiceReferences.contextTable
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
	}

	private void downloadOfflineresponse(OfflineRequestConfigBean bean) {
		try {
			if (bean != null) {
				if (CallDispatcher.LoginUser != null) {
					ContentValues cv = new ContentValues();
					cv.put("status", 1);
					callDisp.getdbHeler(context)
							.updateOfflineCallPendingClones(cv,
									"id=" + bean.getId());
					cv = null;
					bean.setStatus(1);
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(callDisp.getRouter().split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username("ftpadmin");
					ftpBean.setFtp_password("ftppassword");
					ftpBean.setFile_path(bean.getMessage());
					ftpBean.setOperation_type(2);
					ftpBean.setReq_object(bean.getId());
					ftpBean.setRequest_from("answering machine");
					callDisp.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showAlertMessage(String message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
				ContactLogics.this);
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
	}

	private void showAlert(String message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
				ContactLogics.this);
		myAlertDialog.setTitle(SingleInstance.mainContext.getResources().getString(R.string.response_car));
		myAlertDialog.setMessage(message);
		myAlertDialog.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						// do something when the OK button is clicked

						// FormsActivity
						// frm_creator=(FormsActivity)WebServiceReferences.contextTable.get("formactivity");
						// String[] tb=tablename.split("_");
						// frm_creator.notifylistChanged(tb[0], tb[1],
						// CallDispatcher.LoginUser);
						Intent intent = new Intent(context, FormsActivity.class);
						startActivity(intent);
						finish();

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
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
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
		Intent intent = new Intent(context, QuickActionCallsSchedule.class);
		intent.putExtra("mode", "Edit");
		intent.putExtra("spinnerfield", Timemode);
		intent.putExtra("frequencyfield", frequency);
		intent.putExtra("datefield", time);
		startActivity(intent);
		Log.i("welcome", "bean.getFrequncy()---" + frequency);
		Log.i("welcome", "bean.getTime()---" + time);
		Log.i("welcome", "bean.gettimeMode()---" + Timemode);

	}

	public void uploadConfiguredNote(String filepath) {
	}

	void showprgress() {

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

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				AlertDialog.Builder buider = new AlertDialog.Builder(context);
				buider.setMessage(
						SingleInstance.mainContext.getResources().getString(R.string.app_background))
						.setPositiveButton(SingleInstance.mainContext.getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// finish();
										moveTaskToBack(true);
										// return true;

									}
								})
						.setNegativeButton(SingleInstance.mainContext.getResources().getString(R.string.no),
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
		}
		return super.onKeyDown(keyCode, event);

	}

	public void notifyProfilepictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (slidemenu != null) {
					if (slidemenu.isMenuShowing())
						slidemenu.refreshItem();
				}
			}
		});

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:

			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			loadBussinesDatas();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		default:
			break;
		}
	}

}
