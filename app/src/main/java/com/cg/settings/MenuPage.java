package com.cg.settings;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.lib.model.FieldTemplateBean;
import org.lib.model.KeepAliveBean;
import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface.OnSlideMenuItemClickListener;
import com.cg.SlideMenu.Slidebean;
import com.cg.account.ChangePassword;
import com.cg.snazmed.R;
import com.cg.account.buddyView1;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.files.CompleteListView;
import com.cg.instancemessage.IMNotifier;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class MenuPage extends Activity implements OnClickListener,
		OnSlideMenuItemClickListener, IMNotifier {

	private Context context;

	private AlertDialog confirmation = null;

	private Button btn_Settings, btn_about;

	private RelativeLayout btn_networks, btn_dateformat;

	private ToggleButton btn_location, btn_autoplay, btn_sharetone,
			btn_autologin, btn_rempass, btn_show_loc;

	private CallDispatcher callDisp = null;

	private SlideMenu slidemenu;

	private String owner;

	private SharedPreferences p;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	private TextView status = null;

	private Handler handler = new Handler();

	private String strIPath;

	private Bitmap bitmap = null;

	private Button IMRequest;

	private ImageView userIcon = null;

	private TextView username = null;

	private Button logOut, ChangePictue;

	private final Handler handlerSeek = new Handler();

	private AlertDialog alert = null;

	private boolean rememberpassword;

	private boolean autologinstate;

	private TextView status1;

	private Handler handleLogin = new Handler();

	private TextView tv_selecttone, tv_date;

	private RadioButton dateFormat = null;

	private UserSettingsBean bean = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		WebServiceReferences.contextTable.put("IM", context);
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
		bean = callDisp.getdbHeler(context).getUserSettings();
		callDisp.setSettings(bean);

		ShowList();
		setContentView(R.layout.settings_title);

		WebServiceReferences.contextTable.put("menupage", this);
		CallDispatcher.pdialog = new ProgressDialog(context);
		p = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		rememberpassword = p.getBoolean("remember", false);
		autologinstate = p.getBoolean("autologin", false);
		btn_Settings = (Button) findViewById(R.id.btn_Settings);
		btn_Settings.setOnClickListener(this);
		logOut = (Button) findViewById(R.id.btn_logout);
		logOut.setOnClickListener(this);
		ChangePictue = (Button) findViewById(R.id.btn_changeimg);
		ChangePictue.setOnClickListener(this);
		btn_location = (ToggleButton) findViewById(R.id.btn_location);
		btn_location.setOnClickListener(this);
		btn_show_loc = (ToggleButton) findViewById(R.id.btn_show_loc);

		btn_show_loc.setOnClickListener(this);
		IMRequest = (Button) findViewById(R.id.menuim);
		btn_autologin = (ToggleButton) findViewById(R.id.btn_autologin);
		btn_autologin.setOnClickListener(this);

		btn_rempass = (ToggleButton) findViewById(R.id.btn_rempass);
		btn_rempass.setOnClickListener(this);

		if (CallDispatcher.LoginUser == null) {
			logOut.setText(SingleInstance.mainContext.getResources().getString(
					R.string.login));
		}

		IMRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callDisp.openReceivedIm(v, context);
			}

		});
		IMRequest.setVisibility(View.INVISIBLE);

		if (CallDispatcher.LoginUser != null) {
			btn_autologin.setClickable(true);
			btn_rempass.setClickable(true);

		} else {
			btn_autologin.setClickable(false);
			btn_rempass.setClickable(false);
		}

		btn_autoplay = (ToggleButton) findViewById(R.id.btn_autoplay);
		btn_autoplay.setOnClickListener(this);
		btn_sharetone = (ToggleButton) findViewById(R.id.btn_sharetone);
		btn_sharetone.setOnClickListener(this);
		tv_selecttone = (TextView) findViewById(R.id.tv_sharetone);
		tv_selecttone.setOnClickListener(this);

		callDisp.setToneEnabled(false);
		callDisp.isLocationServiceEnabled = false;
		callDisp.latConfigure = 0;
		callDisp.longConfigure = 0;
		callDisp.location_Service = "";
		callDisp.serviceType = 5;
		CallDispatcher.action = 0;

		userIcon = (ImageView) findViewById(R.id.user_icon);
		String profilePic = callDisp.getdbHeler(context).getProfilePic(
				CallDispatcher.LoginUser);
		if (profilePic != null && profilePic.length() > 0) {
			userIcon.setImageBitmap(callDisp.setProfilePicture(profilePic,
					R.drawable.icon_buddy_aoffline));
		}

		username = (TextView) findViewById(R.id.usernameTxtVw);
		username.setText(CallDispatcher.LoginUser);

		status = (TextView) findViewById(R.id.statusTxt);
		status1 = (TextView) findViewById(R.id.statusTxt1);

		RelativeLayout status = (RelativeLayout) findViewById(R.id.statusLay);

		status.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				Log.i("welcome", "On TouchEvent");
				if (CallDispatcher.LoginUser != null) {
					ShowView(v);
				} else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.error_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.kindly_login));
				}

				return false;
			}
		});
		RelativeLayout status1 = (RelativeLayout) findViewById(R.id.statuslay2);
		status1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				Log.i("welcome", "On TouchEvent");
				if (CallDispatcher.LoginUser != null) {
					ShowView(v);
				} else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.error_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.kindly_login));
				}

				return false;
			}
		});

		Button btn_changepassword = (Button) findViewById(R.id.btn_changepassword);

		btn_changepassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intentSettingsc = new Intent(context,
//						ChangePassword.class);
//				startActivity(intentSettingsc);

			}
		});

		btn_networks = (RelativeLayout) findViewById(R.id.btn_networks);

		btn_networks.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Intent intenti = new Intent(context, IpSettings.class);
				intenti.putExtra("Screen", "ip");
				startActivity(intenti);
				return false;
			}
		});

		btn_dateformat = (RelativeLayout) findViewById(R.id.rl_datefrmt);
		tv_date = (TextView) findViewById(R.id.tv_dateset);
		btn_dateformat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDateFormat();
			}
		});

		ImageView btn_aboutpg = (ImageView) findViewById(R.id.btn_aboutpg);
		btn_aboutpg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CallDispatcher.LoginUser != null) {
					Intent intentSettings = new Intent(context, About.class);
					startActivity(intentSettings);
				} else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.error_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.kindly_login));
				}
			}
		});

		loadCurrentStatus();
		loadSettings();

	}

	public void saveSettings() {
		Editor editor = p.edit();
		editor.putBoolean("remember", btn_rempass.isChecked());
		editor.putBoolean("location", btn_location.isChecked());
		editor.putBoolean("autologin", btn_autologin.isChecked());
		editor.putString("dateformate", tv_date.getText().toString().trim());
		editor.putBoolean("autoplay", btn_autoplay.isChecked());
		editor.putBoolean("tone", btn_sharetone.isChecked());
		editor.commit();

	}

	private void loadSettings() {
		if (p != null) {
			btn_location.setChecked(p.getBoolean("location", false));
			btn_rempass.setChecked(p.getBoolean("remember", false));
			btn_autologin.setChecked(p.getBoolean("autologin", false));
			btn_autoplay.setChecked(p.getBoolean("autoplay", false));
			btn_sharetone.setChecked(p.getBoolean("tone", false));
			tv_date.setText(p.getString(SingleInstance.mainContext
					.getResources().getString(R.string.date_format),
					"MM-dd-yyyy"));
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == btn_Settings.getId()) {

			slidemenu.show();
		} else if (v.getId() == tv_selecttone.getId()) {
			if (CallDispatcher.LoginUser != null) {
				if (btn_sharetone.isChecked()) {
					Intent intents = new Intent(context, ShareSettings.class);
					startActivity(intents);
				}
			} else {
				btn_sharetone.setChecked(false);
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));
			}
		}

		else if (v.getId() == btn_location.getId()) {
			if (btn_location.isChecked()) {
				if (CallDispatcher.LoginUser != null) {
					Intent intentl = new Intent(context,
							LocandBusySettings.class);
					intentl.putExtra("Screen", "location");
					startActivity(intentl);
				}

				else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.error_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.kindly_login));
				}

			}
		} else if (v.getId() == btn_show_loc.getId()) {
			Log.d("loc", "Inside show hide location");

			if (CallDispatcher.LoginUser != null) {
				String[] loc = new String[5];
				loc[0] = CallDispatcher.LoginUser;
				if (btn_show_loc.isChecked()) {
					loc[1] = "1";
					CallDispatcher.checkMyLocation = "yes";
				} else {
					loc[1] = "0";
					CallDispatcher.checkMyLocation = "no";
				}

			}

			else {
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));
			}

		} else if (v.getId() == ChangePictue.getId()) {
			if (CallDispatcher.LoginUser != null) {
				profilePic();
			} else {
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));

			}
		} else if (v.getId() == logOut.getId()) {
			if (CallDispatcher.LoginUser != null) {
				showdialog();
			} else {
				login();
			}
		} else if (v.getId() == btn_autologin.getId()) {
			Log.i("check", "button clik");
			if (CallDispatcher.LoginUser != null) {

				if (btn_autologin.isChecked()) {
					btn_rempass.setChecked(true);

					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					final String username = sharedPreferences.getString(
							"uname", null);
					final String password = sharedPreferences.getString(
							"pword", null);

					Editor editor = p.edit();
					editor.putBoolean("autologin", true);
					editor.putBoolean("remember", true);

					editor.putString("pword", password);
					editor.putString("uname", username);
					editor.commit();

				} else {
					btn_autologin.setChecked(false);
					if (autologinstate) {
						Editor editor = p.edit();
						editor.putBoolean("autologin", false);
						editor.commit();
					}

				}
			} else {
				btn_autologin.setClickable(false);
				Log.i("check", "button un-clik");
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));
			}
		} else if (v.getId() == btn_rempass.getId()) {
			Log.i("check", "button clik");
			if (CallDispatcher.LoginUser != null) {

				if (btn_rempass.isChecked()) {

					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context);
					final String username = sharedPreferences.getString(
							"uname", null);
					final String password = sharedPreferences.getString(
							"pword", null);
					Editor editor = p.edit();
					editor.putBoolean("remember", true);
					editor.putString("uname", username);
					editor.putString("pword", password);
					editor.commit();

				} else {
					btn_rempass.setChecked(false);
					btn_autologin.setChecked(false);
					Log.i("welcome", "ELSE PART IN REMEMBERPASSWORD UNCHECK");

					if (rememberpassword) {
						Log.i("welcome",
								"ELSE PART IN REMEMBERPASSWORD UNCHECK IF REMEMBERPASSWORD TRUE");

						Editor editor = p.edit();
						editor.putBoolean("remember", false);

						editor.putBoolean("autologin", false);
						editor.commit();

					}

				}
			}

			else {
				btn_rempass.setClickable(false);

				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));

			}
		}

		// else if (v.getId() == btn_busyres.getId()) {
		// if (CallDispatcher.LoginUser != null) {
		// Intent intentb = new Intent(context, LocandBusySettings.class);
		// intentb.putExtra("Screen", "busy");
		// startActivity(intentb);
		// } else {
		// ShowError("Error", "Kindly Login");
		// }
		// }

		else if (v.getId() == btn_autoplay.getId()) {

			if (btn_autoplay.isChecked()) {
				enableAutoplay(true);
			} else {
				enableAutoplay(false);
			}

		} else if (v.getId() == btn_sharetone.getId()) {
			if (btn_sharetone.isChecked()) {
				if (CallDispatcher.LoginUser != null) {
					if (btn_sharetone.isChecked()) {
						Intent intents = new Intent(context,
								ShareSettings.class);
						startActivity(intents);
					}
				} else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.error_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.kindly_login));
				}
			} else {
				callDisp.setToneEnabled(false);

			}

		}

		else if (v.getId() == btn_about.getId()) {
			if (CallDispatcher.LoginUser != null) {
				Intent intentSettings = new Intent(context, About.class);
				startActivity(intentSettings);
			} else {
				ShowError(
						SingleInstance.mainContext.getResources().getString(
								R.string.error_err), SingleInstance.mainContext
								.getResources()
								.getString(R.string.kindly_login));
			}

		}

	}

	public void enableAutoplay(boolean val) {
		Log.i("thread", "################# came to enable autoplay" + val);

		if (CallDispatcher.LoginUser != null) {
			UserSettingsBean bean = callDisp.getSettings();
			if (val) {
				WebServiceReferences.isAutoPlay = true;
				if (callDisp.getdbHeler(context).isRecordExists(
						"select * from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='autoplay'")) {
					String updatequerry = "update Settings set service='1' where settings='autoplay' and username='"
							+ CallDispatcher.LoginUser + "'";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							updatequerry);

				} else {
					String insertQuery = "insert into Settings(settings,service,username)values('autoplay','1','"
							+ CallDispatcher.LoginUser + "')";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							insertQuery);

				}
				if (bean != null) {
					bean.setAutoplayService("1");
				} else {
					bean = callDisp.getdbHeler(context).getUserSettings();
					bean.setAutoplayService("1");
					callDisp.setSettings(bean);
				}
			} else {
				WebServiceReferences.isAutoPlay = false;
				if (callDisp.getdbHeler(context).isRecordExists(
						"select * from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='autoplay'")) {
					String updatequerry = "delete from Settings where username="
							+ CallDispatcher.LoginUser
							+ "' and settings='autoplay'";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							updatequerry);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				}
				/**
				 * Autoplay
				 */
				if (bean != null) {
					bean.setAutoplayService("0");
				} else {
					bean = callDisp.getdbHeler(context).getUserSettings();
					bean.setAutoplayService("0");
					callDisp.setSettings(bean);
				}

			}

		} else {
			ShowError(
					SingleInstance.mainContext.getResources().getString(
							R.string.error_err), SingleInstance.mainContext
							.getResources().getString(R.string.kindly_login));
		}

	}

	public void ShowList() {
		// TODO Auto-generated method stub
		setContentView(R.layout.history_container);

		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
		callDisp.composeList(datas);
		slidemenu.init(MenuPage.this, datas, MenuPage.this, 100);

	}

	private void ShowError(String Title, String Message) {
		// TODO Auto-generated method stub

		Log.e("logout", "show error() " + Title + "....." + Message);
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
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;

		if (WebServiceReferences.Imcollection.size() <= 0) {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.INVISIBLE);
			}
		} else {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.VISIBLE);
			}
		}

	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	@Override
	protected void onDestroy() {

		try {
			Log.e("lg", "on destroy of buddyview1????????????????");
			saveSettings();
			if (WebServiceReferences.contextTable.containsKey("menupage")) {
				WebServiceReferences.contextTable.remove("menupage");
			}
			if (WebServiceReferences.contextTable.containsKey("IM")) {
				WebServiceReferences.contextTable.remove("IM");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	protected void ShowView(final View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuPage.this);
		builder.create();

		builder.setTitle(SingleInstance.mainContext.getResources().getString(
				R.string.change_status));
		final CharSequence[] choiceList = {
				SingleInstance.mainContext.getResources().getString(
						R.string.online),
				SingleInstance.mainContext.getResources().getString(
						R.string.busy),
				SingleInstance.mainContext.getResources().getString(
						R.string.away), SingleInstance.mainContext.getResources().getString(R.string.stealth) };

		builder.setItems(choiceList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				changeFieldType(choiceList[which].toString(), v);
				alert.cancel();
			}
		});
		alert = builder.create();
		alert.show();
	}

	public void loadCurrentStatus() {
		if (status != null) {
			if (callDisp == null)
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			if (CallDispatcher.myStatus.equals("1")) {
				status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.online));
				status1.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.online));
			} else if (CallDispatcher.myStatus.equals("3")) {
				status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.away));
				status1.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.away));
			} else if (CallDispatcher.myStatus.equals("4")) {
				status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.stealth));
				status1.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.stealth));
			} else if (CallDispatcher.myStatus.equals("0")) {
				status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.offline));
				status1.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.offline));
			} else if (CallDispatcher.myStatus.equals("2")) {
				status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.busy));
				status1.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.busy));
			}

		}
	}

	private void changeFieldType(String type, View v) {

		status.setText(type);
		status1.setText(type);

		if (CallDispatcher.isConnected) {

			if (type.equals("Online")) {

				// ValueHandler.buddyStatus = 0;
				status.setText(type);
				status1.setText(type);
				CallDispatcher.myStatus = "1";
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.receive_all_services), 1).show();

			} else if (type.equals("Away")) {

				// ValueHandler.buddyStatus = 1;
				status.setText(type);
				status1.setText(type);
				CallDispatcher.myStatus = "3";

				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					Log.d("lg", "............"
							+ WebServiceReferences.contextTable.get("MAIN"));

				}
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_services), 1).show();

			} else if (type.equals("Stealth")) {

				// ValueHandler.buddyStatus = 2;
				status.setText(type);
				status1.setText(type);
				CallDispatcher.myStatus = "4";

				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					Log.d("lg", "............"
							+ WebServiceReferences.contextTable.get("MAIN"));

				}
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_msg_services), 1)
						.show();

			} else if (type.equals("Offline")) {
				// ValueHandler.buddyStatus = 3;
				status.setText(type);
				status1.setText(type);
				CallDispatcher.myStatus = "0";

				if (WebServiceReferences.contextTable.containsKey("MAIN")) {
					Log.d("lg", "............"
							+ WebServiceReferences.contextTable.get("MAIN"));

				}
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.not_receive_call_broadcast_chat), 1)
						.show();
			} else if (type.equals("Busy")) {
				// ValueHandler.buddyStatus = 3;
				status.setText(type);
				status1.setText(type);
				CallDispatcher.myStatus = "2";

				if (WebServiceReferences.contextTable.containsKey("MAIN")) {

					Log.d("lg", "............"
							+ WebServiceReferences.contextTable.get("MAIN"));

				}
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.receive_all_services), 1).show();
			}
			if (callDisp == null) {
				callDisp = new CallDispatcher(context);
			}
			KeepAliveBean aliveBean = callDisp.getKeepAliveBean();
			aliveBean.setKey("0");

			if (!WebServiceReferences.running) {
				callDisp.startWebService(
						getResources().getString(R.string.service_url), "80");
			}
			Log.i("b", "**********Buddies List class ********");
			WebServiceReferences.webServiceClient.heartBeat(aliveBean);

			if (slidemenu.menuShown)
				slidemenu.chageMyStatus();

		} else {
			status.setText(SingleInstance.mainContext.getResources().getString(
					R.string.offline));
			status1.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.offline));
			CallDispatcher.myStatus = "0";

			Toast.makeText(
					context,
					SingleInstance.mainContext.getResources().getString(
							R.string.network_error), 1).show();
		}

	}

	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
		Bitmap result = null;
		try {
			result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);

			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, 200, 200);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawCircle(50, 50, 50, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

		} catch (NullPointerException e) {
		} catch (OutOfMemoryError o) {
		}
		return result;
	}

	private void showdialog() {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(MenuPage.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.logout_dialog);
		// dialog.setTitle("Custom Profile Dialog");
		LinearLayout layout_query = (LinearLayout) dialog
				.findViewById(R.id.logout_lay);

		Button shareProfile = (Button) layout_query.findViewById(R.id.log_yes);
		Button closedialog = (Button) layout_query.findViewById(R.id.log_no);

		closedialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				p = PreferenceManager.getDefaultSharedPreferences(context);
				Editor editor = p.edit();
				editor.putBoolean("isfirstlaunch", true);

				editor.commit();
				try {
					// callDisp.logout(true);
				} catch (Exception e) {
					// TODO Auto-generated catch blockkTrace();
				}
				dialog.dismiss();
				callDisp.showprogress(CallDispatcher.pdialog, context);

			}
		});
		shareProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	private void dissableUserSettings() {
		// ValueHandler.isAutoPlay = false;
		callDisp.setToneEnabled(false);
		callDisp.isLocationServiceEnabled = false;
		callDisp.latConfigure = 0;
		callDisp.longConfigure = 0;
		callDisp.location_Service = "";
		callDisp.serviceType = 5;
		// callDisp.setSettings(null);
		CallDispatcher.action = 0;
	}

	private String getUsernameFromPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String username = sharedPreferences.getString("uname", null);
		if (username == null)
			username = "";

		return username;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				AlertDialog.Builder buider = new AlertDialog.Builder(context);
				buider.setMessage(
						SingleInstance.mainContext.getResources().getString(
								R.string.app_background))
						.setPositiveButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.yes),
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
						.setNegativeButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.no),
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

	private void profilePic() {

		final String[] items = new String[] {
				SingleInstance.mainContext.getResources().getString(
						R.string.gallery),
				SingleInstance.mainContext.getResources().getString(
						R.string.photo) };
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				changeprofilePic(which);
			}

		});

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		builder.show();
	}

	private void changeprofilePic(int strMMType) {
		Log.i("clone", "===> inside message response");
		// strMMType = 0 - Gallery and 1 - photo
		try {
			if (strMMType == 0) {
				Log.i("clone", "====> inside gallery");
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, 30);
				} else {
					Log.i("img", "sdk is above 19");
					Log.i("clone", "====> inside gallery");
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent, 31);

				}

			} else if (strMMType == 1) {
				Log.i("clone", "====> inside photo");
				Long free_size = callDisp.getExternalMemorySize();
				Log.d("IM", free_size.toString());
				if (free_size > 0 && free_size >= 5120) {

					// Intent i = new Intent(context, Custom_Camara.class);

					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MPD_" + callDisp.getFileName()
							+ ".jpg";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", 32);
					intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("createOrOpen", "create");
					Log.d("File Path", strIPath);
					startActivityForResult(intent, 32);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.insufficient_memory));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=======>" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			Log.i("onresult123", "Received");
			// super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == 30) {

				if (resultCode != Activity.RESULT_CANCELED) {
					Uri selectedImageUri = data.getData();
					strIPath = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {
						bitmap = callDisp.ResizeImage(strIPath);

						if (bitmap != null) {
							String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ "MPD_"
									+ CompleteListView.getFileName() + ".jpg";
							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(path)));
								bitmap.compress(CompressFormat.JPEG, 100,
										stream);
								strIPath = path;

							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);

							if (bitmap != null)
								userIcon.setImageBitmap(bitmap);
							// value_img.setVisibility(View.VISIBLE);
							// value_img.setTag(new File(path).getName());
							// value_img.setPadding(10, 10, 10, 10);
							// ProgressDialog dialog = new
							// ProgressDialog(context);
							// callDisp.showprogress(dialog, context);
							updateProile(strIPath);
							callDisp.uploadofflineResponse(strIPath, false, 0,
									"menupage");
						} else {
							strIPath = null;
						}
					}
				}

			} else if (requestCode == 31) {

				if (resultCode == Activity.RESULT_CANCELED) {

				} else {

					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					getContentResolver().takePersistableUriPermission(
							selectedImageUri, takeFlags);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + "MPD_" + callDisp.getFileName()
							+ ".jpg";

					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						new bitmaploader().execute(selectedImageUri);
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.large_image));
					}

				}
			} else if (requestCode == 32) {

				if (resultCode == Activity.RESULT_CANCELED) {

				} else {
					File fileCheck = new File(strIPath);
					if (fileCheck.exists()) {
						bitmap = callDisp.ResizeImage(strIPath);
						callDisp.changemyPictureOrientation(bitmap, strIPath);
						if (bitmap != null && !bitmap.isRecycled())
							bitmap.recycle();
						bitmap = null;
						bitmap = callDisp.ResizeImage(strIPath);
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);

						if (bitmap != null) {
							userIcon.setImageBitmap(bitmap);
							// value_img.setTag(new File(strIPath).getName());
							// value_img.setPadding(10, 10, 10, 10);

							updateProile(strIPath);
							callDisp.uploadofflineResponse(strIPath, false, 0,
									"menupage");
						}
					}
				}

			}
		} catch (Exception e) {

		}

	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				callDisp.cancelDialog();
				if (strIPath != null)
					bitmap = callDisp.ResizeImage(strIPath);
				if (bitmap != null) {

					// File fle = new File(strIPath);
					// fle.createNewFile();
					// FileOutputStream fout = new FileOutputStream(strIPath);
					// img.compress(CompressFormat.JPEG, 75, fout);

					// img = Bitmap.createScaledBitmap(img, 100, 75, false);
					Log.d("OnActivity", "_____On Activity Called______");

					// bitmap = null;
					//
					// bitmap = callDisp.ResizeImage(strIPath);
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);

					ImageView value_img;

					value_img = (ImageView) findViewById(R.id.user_icon);

					if (bitmap != null) {
						value_img.setVisibility(View.VISIBLE);
						value_img.setImageBitmap(bitmap);
						value_img.setPadding(10, 10, 10, 10);
						// callDisp.uploadConfiguredNote(strIPath,
						// OperationType.UTILITY, null);

					}
				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			callDisp.showprogress(CallDispatcher.pdialog, context);
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + callDisp.getFileName() + ".jpg";
					FileInputStream fin = (FileInputStream) getContentResolver()
							.openInputStream(uri);
					ByteArrayOutputStream straam = new ByteArrayOutputStream();
					byte[] content = new byte[1024];
					int bytesread;
					while ((bytesread = fin.read(content)) != -1) {
						straam.write(content, 0, bytesread);
					}
					byte[] bytes = straam.toByteArray();
					FileOutputStream fout = new FileOutputStream(strIPath);
					straam.flush();
					straam.close();
					straam = null;
					fin.close();
					fin = null;
					fout.write(bytes);
					fout.flush();
					fout.close();
					fout = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	public void notifyUploadFailed() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				callDisp.cancelDialog();
				showToast(SingleInstance.mainContext.getResources().getString(
						R.string.cannot_upload));
			}
		});
	}

	// public void notifyUpload(final String filePath) {
	// handler.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// try {
	// int fieldId = callDisp.getdbHeler(context)
	// .getProfilePicFieldId("Picture");
	// if (fieldId > 0) {
	// ContentValues cv = new ContentValues();
	// Vector<FieldTemplateBean> fields_list = callDisp
	// .getdbHeler(context).getProfileFields();
	// for (FieldTemplateBean fieldTemplateBean : fields_list) {
	// if (fieldId == Integer.parseInt(fieldTemplateBean
	// .getFieldId())) {
	// cv.put("fieldvalue", filePath);
	// fieldTemplateBean.setFiledvalue(new File(
	// filePath).getName());
	//
	// cv.put("fieldid",
	// fieldTemplateBean.getFieldId());
	// cv.put("userid", CallDispatcher.LoginUser);
	// if (!callDisp
	// .getdbHeler(context)
	// .isRecordExists(
	// "select * from profilefieldvalues where fieldid="
	// + fieldTemplateBean
	// .getFieldId()
	// + " and userid='"
	// + CallDispatcher.LoginUser
	// + "'")) {
	// callDisp.getdbHeler(context)
	// .insertProfileFieldValues(cv);
	// } else {
	// cv.remove("fieldid");
	// cv.remove("userid");
	// callDisp.getdbHeler(context)
	// .updateProfileFieldValues(
	// cv,
	// "fieldid="
	// + fieldTemplateBean
	// .getFieldId()
	// + " and userid='"
	// + CallDispatcher.LoginUser
	// + "'");
	// }
	// break;
	// }
	// }
	//
	// Vector<FieldTemplateBean> fields_list1 = callDisp
	// .getdbHeler(context).getProfileFields();
	//
	// String[] params = new String[2];
	// params[0] = CallDispatcher.LoginUser;
	// params[1] = "5";
	//
	// WebServiceReferences.webServiceClient
	// .setStandardProfileFieldValues(params,
	// fields_list1);
	// } else {
	// Toast.makeText(context, "Please create profile",
	// Toast.LENGTH_LONG).show();
	// }
	// callDisp.cancelDialog();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

	private void login() {

		p = PreferenceManager.getDefaultSharedPreferences(context);

		final String username = p.getString("uname", null);
		final String password = p.getString("pword", null);
		boolean rememberpassword = p.getBoolean("remember", false);
		boolean autologinstate = p.getBoolean("autologin", false);
		boolean isfirst_launch = p.getBoolean("isfirstlaunch", false);

		if (rememberpassword || autologinstate) {

			// Alert.writeBoolean(context, Alert.heartbeat, true);
			String urlx = p.getString("url", null);
			String portx = p.getString("port", null);
			Editor editor = p.edit();
			editor.putBoolean("isfirstlaunch", false);

			editor.commit();

			if (urlx != null && portx != null) {

				handleLogin.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method
						// stub

						try {

							String myPublicIp = CallDispatcher
									.getPublicipaddress();

							Log.d("call", "print public ip " + myPublicIp);
							// if (callDisp.mainbuddylist != null) {
							// callDisp.mainbuddylist.clear();
							// }

							// String username =
							// edUserName.getText()
							// .toString().trim().toLowerCase();
							// String password =
							// edPswd.getText().toString()
							// .trim();
							Log.d("welcome", "^^^^^^^^^^^^^^^^user login");
							// Alert.writeString(context, Alert.password,
							// password.trim());
							if (WebServiceReferences.contextTable
									.containsKey("buddyView1")) {
								((buddyView1) WebServiceReferences.contextTable
										.get("buddyView1")).login(username,
										password);

							} else {
								Intent intent = new Intent(context,
										buddyView1.class);
								context.startActivity(intent);
							}

						} catch (Exception e) {
							Log.d("signn", "started progress " + e);
							e.printStackTrace();
						}
						//

					}
				});

				//
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.please_check_settings),
						Toast.LENGTH_LONG).show();

			}
			// a
		} else {
			if (!WebServiceReferences.contextTable.containsKey("buddyView1")) {
				Intent intent = new Intent(context, buddyView1.class);
				startActivity(intent);
			}
		}
		// Intent notes = new Intent(context, CompleteListView.class);
		// startActivity(notes);
		// finish();

	}

	public void notifyProfilePictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String profilePic = callDisp.getdbHeler(context).getProfilePic(
						CallDispatcher.LoginUser);
				if (profilePic != null && profilePic.length() > 0) {
					userIcon.setImageBitmap(callDisp.setProfilePicture(
							profilePic, R.drawable.icon_buddy_aoffline));
					notifyProfilepictureDownloaded();
				}
			}
		});

	}

	public void notifyProfilepictureDownloaded() {
		if (slidemenu != null) {
			if (slidemenu.isMenuShowing())
				slidemenu.refreshItem();
		}
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
			finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.CLONE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.SETTINGS:

			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
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

	private void showDateFormat() {

		// TODO Auto-generated method stub
		final String[] date_items = { "MM/dd/yyyy", "dd-MM-yyyy" };
		int position = 0;
		if (tv_date.getText().toString().equals("dd-MM-yyyy"))
			position = 1;

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setSingleChoiceItems(date_items, position,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tv_date.setText(date_items[which]);
						Editor editor = p.edit();
						editor.putString("dateformate", date_items[which]);
						editor.commit();
						CallDispatcher.dateFormat = date_items[which];
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IMRequest.setVisibility(View.VISIBLE);
				IMRequest.setEnabled(true);

				IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

	private void updateProile(String filePath) {

		// TODO Auto-generated method stub
		try {
			int fieldId = callDisp.getdbHeler(context).getProfilePicFieldId(
					"Picture");
			if (fieldId > 0) {
				ContentValues cv = new ContentValues();
				Vector<FieldTemplateBean> fields_list = callDisp.getdbHeler(
						context).getProfileFields();
				for (FieldTemplateBean fieldTemplateBean : fields_list) {
					if (fieldId == Integer.parseInt(fieldTemplateBean
							.getFieldId())) {
						cv.put("fieldvalue", filePath);
						fieldTemplateBean.setFiledvalue(new File(filePath)
								.getName());

						cv.put("fieldid", fieldTemplateBean.getFieldId());
						cv.put("userid", CallDispatcher.LoginUser);
						// if (!callDisp.getdbHeler(context).isRecordExists(
						// "select * from profilefieldvalues where fieldid="
						// + fieldTemplateBean.getFieldId()
						// + " and userid='"
						// + CallDispatcher.LoginUser + "'")) {
						// callDisp.getdbHeler(context)
						// .insertProfileFieldValues(cv);
						// } else {
						// cv.remove("fieldid");
						// cv.remove("userid");
						callDisp.getdbHeler(context).updateProfileFieldValues(
								cv,
								"fieldid=" + fieldTemplateBean.getFieldId()
										+ " and userid='"
										+ CallDispatcher.LoginUser + "'");
						// }
						break;
					}
				}

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.pls_create_profile), Toast.LENGTH_LONG)
						.show();
			}
			callDisp.cancelDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
