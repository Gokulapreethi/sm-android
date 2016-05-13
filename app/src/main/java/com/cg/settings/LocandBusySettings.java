package com.cg.settings;

import java.util.ArrayList;
import java.util.HashMap;

import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.instancemessage.ActionItem1;
import com.cg.instancemessage.IMNotifier;
import com.cg.instancemessage.NotePickerScreen;
import com.cg.instancemessage.QuickAction1;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class LocandBusySettings extends Activity implements IMNotifier {

	private String Screen = null;

	private TextView title = null;

	private LinearLayout busy_layout = null, loc_layout = null;

	private Button IMRequest = null;

	private Button btn_notification = null;

	private Context context = null;

	private Button btn_save = null;

	private Button btn_cancel = null;

	private Spinner selfSpinner = null;

	private String[] self = new String[1];

	private EditText et_latitude, et_longitude, et_address;

	private ToggleButton btnManual;

	private Spinner statusSpinner = null;

	private Spinner forwardSpinner = null;

	private Spinner restypeSpinner = null;

	private EditText etRedirecturl = null, edsharepath = null;

	private TextView tvforwardspinner = null, tvredirecturl = null,
			tvspath = null, tvrestype = null;
	private CallDispatcher callDisp = null;

	private String[] restypes = {
			"",
			SingleInstance.mainContext.getResources().getString(R.string.notes),
			SingleInstance.mainContext.getResources().getString(R.string.audio),
			SingleInstance.mainContext.getResources().getString(R.string.video),
			SingleInstance.mainContext.getResources().getString(R.string.image) };

	private String type = null;

	private String[] redirectoptions = {
			SingleInstance.mainContext.getResources().getString(
					R.string.select_option),
			SingleInstance.mainContext.getResources().getString(
					R.string.redirect_to_url),
			SingleInstance.mainContext.getResources().getString(
					R.string.call_forwarding),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_a_text_note),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_an_audio_note),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_photo_note),
			SingleInstance.mainContext.getResources().getString(
					R.string.share_a_video_note) };

	// private String[] serv_types = { "Audio Unicast", "Video Unicast",
	// "Video Call", "Audio Call" };
	private String[] serv_types = {
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_unicast),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_unicast),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_call),
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_call) };

	private ToggleButton clear_loc = null;

	private ToggleButton tgl_enableserv = null;

	private Spinner init_service = null;

	private Button btn_cancel1 = null;

	private boolean isfirstselection = true;

	private Button btn_cmp;

	private String component_id = null;

	private boolean ispathChanged = false;

	private RelativeLayout r_layout1 = null;

	private RelativeLayout r_layout2 = null;

	private UserSettingsBean sett_bean = null;

	private AlertDialog confirmation = null;

	private TextView titles = null;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.context = this;
		WebServiceReferences.contextTable.put("a_play", this);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		callDisp = (CallDispatcher) WebServiceReferences.callDispatch
				.get("calldisp");

		IMRequest = (Button) findViewById(R.id.im);

		IMRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callDisp.openReceivedIm(v, context);
			}

		});

		btn_cancel1 = (Button) findViewById(R.id.settings);
		btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);
		btn_cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_cmp = (Button) findViewById(R.id.btncomp);
		btn_cmp.setVisibility(View.INVISIBLE);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
		IMRequest.setWidth(70);

		WebServiceReferences.contextTable.put("IM", this);
		WebServiceReferences.contextTable.put("locbusy", this);
		btn_notification = (Button) findViewById(R.id.notification);
		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		callDisp.setSettings(callDisp.getdbHeler(context).getUserSettings());
		sett_bean = callDisp.getSettings();
		setContentView(R.layout.settings_config);

		Screen = getIntent().getStringExtra("Screen");
		if (Screen.equals("location")) {
			titles = (TextView) findViewById(R.id.note_date);
			titles.setText(getResources().getString(R.string.location));
		} else {
			titles = (TextView) findViewById(R.id.note_date);
			titles.setText(getResources().getString(R.string.busyresponse));
		}
		title = (TextView) findViewById(R.id.txtView02);
		loc_layout = (LinearLayout) findViewById(R.id.locllsett1);
		busy_layout = (LinearLayout) findViewById(R.id.busyllsett2);

		btn_cancel = (Button) findViewById(R.id.btnCancelSettings);
		btn_save = (Button) findViewById(R.id.btnSaveSettings);

		r_layout1 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		r_layout2 = (RelativeLayout) findViewById(R.id.relativeLayout3);

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("lat", "####################"
						+ et_latitude.getText().toString().trim().length());
				Log.i("lat", "####################"
						+ et_longitude.getText().toString().trim().length());
				if (Screen.equalsIgnoreCase("location")) {
					if (et_latitude.getText().toString().trim().length() > 0
							&& et_longitude.getText().toString().trim()
									.length() > 0) {
						callDisp.latConfigure = Double.parseDouble(et_latitude
								.getText().toString().trim());

						callDisp.longConfigure = Double
								.parseDouble(et_longitude.getText().toString()
										.trim());
						callDisp.serviceType = 0;
						callDisp.location_Service = init_service
								.getSelectedItem().toString();

						saveLatLong(et_latitude.getText().toString().trim(),
								et_longitude.getText().toString().trim(), null,
								et_address.getText().toString().trim());

						finish();
					} else {

						if (et_latitude.getText().toString().trim().length() == 0
								&& et_longitude.getText().toString().trim()
										.length() == 0) {
							tgl_enableserv.setChecked(false);
							r_layout1.setVisibility(View.GONE);
							r_layout2.setVisibility(View.GONE);
							clearLatLong();
							finish();
						}

						else if (et_latitude.getText().toString().trim()
								.length() < 0) {
							showToast(SingleInstance.mainContext.getResources().getString(R.string.kindly_give_the_latitude));
						} else if (et_longitude.getText().toString().trim()
								.length() < 0) {
							showToast(SingleInstance.mainContext.getResources().getString(R.string.kindly_give_the_longitude));
						}

					}
				} else if (Screen.equalsIgnoreCase("busy")) {
					changeAction();
					finish();
				}
			}

		});

		if (CallDispatcher.LoginUser != null) {
			self[0] = CallDispatcher.LoginUser;
		} else {
			self[0] = "";
		}

		selfSpinner = (Spinner) findViewById(R.id.self_Spinner);
		ArrayAdapter<String> adself = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, self);
		adself.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selfSpinner.setAdapter(adself);
		Log.d("BL", "going to set option");
		selfSpinner.setSelection(0);
		selfSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String obj = CallDispatcher.LoginUser;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		et_latitude = (EditText) findViewById(R.id.et_lat);
		et_longitude = (EditText) findViewById(R.id.et_long);
		et_address = (EditText) findViewById(R.id.ed_address);
		et_address.setKeyListener(null);
		btnManual = (ToggleButton) findViewById(R.id.btnLocManual);
		clear_loc = (ToggleButton) findViewById(R.id.btnLocClear);
		tgl_enableserv = (ToggleButton) findViewById(R.id.btnLocservEnabled);
		init_service = (Spinner) findViewById(R.id.locservice_spinner);

		tgl_enableserv
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (callDisp.getdbHeler(context).isRecordExists(
									"select * from Settings where username='"
											+ CallDispatcher.LoginUser
											+ "' and settings='locationset'")) {
								String updatequerry = "update Settings set service='1' where settings='locationset' and username='"
										+ CallDispatcher.LoginUser + "'";
								boolean res = callDisp.getdbHeler(context)
										.ExecuteQuery(updatequerry);
								Log.i("thread",
										"@@@@@@@@@@@@@@@@@@@ querry result"
												+ res);

							} else {
								String insertQuery = "insert into Settings(settings,service,username)values('locationset','1','"
										+ CallDispatcher.LoginUser + "')";
								boolean res = callDisp.getdbHeler(context)
										.ExecuteQuery(insertQuery);
								Log.i("thread",
										"@@@@@@@@@@@@@@@@@@@ querry result"
												+ res);
							}

							callDisp.isLocationServiceEnabled = true;
							if (sett_bean != null)
								sett_bean.setLocationservEnabled("1");
							else {
								sett_bean = callDisp.getdbHeler(context)
										.getUserSettings();
								sett_bean.setLocationservEnabled("1");
								callDisp.setSettings(sett_bean);
							}
						} else {
							if (callDisp.getdbHeler(context).isRecordExists(
									"select * from Settings where username='"
											+ CallDispatcher.LoginUser
											+ "' and settings='locationset'")) {
								String updatequerry = "delete from Settings where username='"
										+ CallDispatcher.LoginUser
										+ "' and settings='locationset'";
								boolean res = callDisp.getdbHeler(context)
										.ExecuteQuery(updatequerry);
								Log.i("thread",
										"@@@@@@@@@@@@@@@@@@@ querry result"
												+ res);

							}
							callDisp.isLocationServiceEnabled = false;
							if (sett_bean != null)
								sett_bean.setLocationservEnabled("0");
							else {
								sett_bean = callDisp.getdbHeler(context)
										.getUserSettings();
								sett_bean.setLocationservEnabled("0");
								callDisp.setSettings(sett_bean);
							}
						}

					}
				});

		ArrayAdapter<String> serv_spin = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, serv_types);
		serv_spin
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		init_service.setAdapter(serv_spin);
		init_service.setSelection(0);
		Log.d("BL", "going to set option");

		init_service.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// init_service.setSelection(parent.getItemAtPosition(position));
				Log.i("thread",
						"@@@@@@@@@@@@@@@@@@@@" + init_service.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		clear_loc.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				et_latitude.setText("");
				et_longitude.setText("");
				et_address.setText("");
				btnManual.setChecked(false);
				tgl_enableserv.setChecked(false);
				r_layout1.setVisibility(View.GONE);
				r_layout2.setVisibility(View.GONE);
				clearLatLong();
			}
		});

		et_address.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (!btnManual.isChecked()) {
						Intent i = new Intent(context,
								LocationPickerMapView.class);
						startActivityForResult(i, 3);
					}
					return true;
				} else {
					return false;
				}
			}
		});
		et_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (et_address.getText().toString().trim().length() == 0) {
					r_layout1.setVisibility(View.GONE);
					r_layout2.setVisibility(View.GONE);
				} else {
					r_layout1.setVisibility(View.VISIBLE);
					r_layout2.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		et_latitude.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (et_latitude.getText().toString().trim().length() == 0
						&& et_longitude.getText().toString().trim().length() == 0) {
					r_layout1.setVisibility(View.GONE);
					r_layout2.setVisibility(View.GONE);
				} else {
					r_layout1.setVisibility(View.VISIBLE);
					r_layout2.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		et_longitude.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (et_latitude.getText().toString().trim().length() == 0
						&& et_longitude.getText().toString().trim().length() == 0) {
					r_layout1.setVisibility(View.GONE);
					r_layout2.setVisibility(View.GONE);
				} else {
					r_layout1.setVisibility(View.VISIBLE);
					r_layout2.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		btnManual.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				editTextSettings(isChecked);

			}
		});

		if (Screen.equalsIgnoreCase("location")) {
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.automatic_messages_initiate_calls));
			busy_layout.setVisibility(View.GONE);
			loc_layout.setVisibility(View.VISIBLE);
			loadLocationValues();

		} else if (Screen.equalsIgnoreCase("busy")) {
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.unable_to_answer_a_call));
			busy_layout.setVisibility(View.VISIBLE);
			loc_layout.setVisibility(View.GONE);
		}

		statusSpinner = (Spinner) findViewById(R.id.forward_spinner);
		forwardSpinner = (Spinner) findViewById(R.id.sp_forward);
		etRedirecturl = (EditText) findViewById(R.id.etredirect);

		restypeSpinner = (Spinner) findViewById(R.id.restype_spinner);
		tvforwardspinner = (TextView) findViewById(R.id.tv_forward);
		tvforwardspinner.setText("Forward");
		tvredirecturl = (TextView) findViewById(R.id.tvredirect);

		tvspath = (TextView) findViewById(R.id.tvsharepath);
		tvrestype = (TextView) findViewById(R.id.tvrestype);
		edsharepath = (EditText) findViewById(R.id.etsharepath);

		edsharepath.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				ispathChanged = true;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		Log.d("text", "....................." + etRedirecturl);
		Log.d("text", "....................." + callDisp);
		Log.d("text", "....................." + callDisp.url);
		etRedirecturl.setText(callDisp.url);

		ArrayAdapter<String> restypeadapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, restypes);
		restypeadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		restypeSpinner.setAdapter(restypeadapter);
		if (sett_bean != null) {
			String restype = sett_bean.getactionValue2();
			if (restype.trim().length() != 0) {
				if (restype.equalsIgnoreCase("12")) {
					restypeSpinner.setSelection(1);
				} else if (restype.equalsIgnoreCase("10")) {
					restypeSpinner.setSelection(2);
				} else if (restype.equalsIgnoreCase("11")) {
					restypeSpinner.setSelection(3);
				} else if (restype.equalsIgnoreCase("13")) {
					restypeSpinner.setSelection(4);
				}
			} else {
				restypeSpinner.setSelection(0);
			}
		} else {
			restypeSpinner.setSelection(0);
		}

		edsharepath.setFocusable(false);
		edsharepath.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edsharepath.getWindowToken(), 0);

				Log.d("pos", "MMMMMMMMMMMMMMMMMMMMMMMMMMMMM" + type);

				Intent i = new Intent(context, NotePickerScreen.class);
				i.putExtra("note", type);
				startActivityForResult(i, 100);
			}
		});

		ArrayAdapter<String> ad = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, redirectoptions);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinner.setAdapter(ad);
		Log.d("BL", "going to set option");
		statusSpinner.setSelection(CallDispatcher.action);
		if (CallDispatcher.action == 3) {
			if (sett_bean != null) {

				String c_type = sett_bean.getActionSevices();
				if (c_type.equals("105") || c_type.equals("106")
						&& c_type.equals("107") || c_type.equals("108")) {

					if (c_type.equalsIgnoreCase("105")) {
						statusSpinner.setSelection(3);
					} else if (c_type.equalsIgnoreCase("106")) {
						statusSpinner.setSelection(4);
					} else if (c_type.equalsIgnoreCase("107")) {
						statusSpinner.setSelection(6);
					} else if (c_type.equalsIgnoreCase("108")) {
						statusSpinner.setSelection(5);
					}
				}
			}
		}

		statusSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(LocandBusySettings.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(
								statusSpinner.getWindowToken(), 0);
						Log.e("psss",
								"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL came to share spinne#######################");
						Log.e("psss",
								"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL came to share spinne#######################"
										+ CallDispatcher.action);

						String obj = (String) statusSpinner
								.getItemAtPosition(arg2);
						Log.e("psss",
								"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL came to share spinne#######################"
										+ obj);
						if (obj.startsWith("Can")) {
							CallDispatcher.action = 0;
							// forwardSpinner.setEnabled(false);
							// etRedirecturl.setEnabled(false);
							tvforwardspinner.setVisibility(View.GONE);
							tvredirecturl.setVisibility(View.GONE);

							forwardSpinner.setVisibility(View.GONE);
							etRedirecturl.setVisibility(View.GONE);

							tvspath.setVisibility(View.GONE);
							edsharepath.setVisibility(View.GONE);
							tvrestype.setVisibility(View.GONE);
							restypeSpinner.setVisibility(View.GONE);
							/* changeAction(); */

						} else if (obj.startsWith("Re")) {
							CallDispatcher.action = 1;
							// forwardSpinner.setEnabled(false);
							// etRedirecturl.setEnabled(true);
							tvforwardspinner.setVisibility(View.GONE);
							forwardSpinner.setVisibility(View.GONE);

							tvredirecturl.setVisibility(View.VISIBLE);
							etRedirecturl.setVisibility(View.VISIBLE);
							tvspath.setVisibility(View.GONE);
							edsharepath.setVisibility(View.GONE);
							tvrestype.setVisibility(View.GONE);
							restypeSpinner.setVisibility(View.GONE);
						} else if (obj.startsWith("Call")) {
							CallDispatcher.action = 2;
							// forwardSpinner.setEnabled(true);
							// etRedirecturl.setEnabled(false);
							tvforwardspinner.setVisibility(View.VISIBLE);
							forwardSpinner.setVisibility(View.VISIBLE);
							tvredirecturl.setVisibility(View.GONE);
							etRedirecturl.setVisibility(View.GONE);

							tvspath.setVisibility(View.GONE);
							edsharepath.setVisibility(View.GONE);
							tvrestype.setVisibility(View.GONE);
							restypeSpinner.setVisibility(View.GONE);

						} else if (obj.startsWith("Share")) {
							Log.e("psss",
									"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL came to share spinne#######################");

							CallDispatcher.action = 3;
							if (obj.equalsIgnoreCase("share text note")) {
								type = "note";
							} else if (obj.equalsIgnoreCase("share audio note")) {
								type = "audio";
							} else if (obj.equalsIgnoreCase("share photo note")) {
								type = "photo";
							} else if (obj.equalsIgnoreCase("share video note")) {
								type = "video";
							}
							if (!isfirstselection) {
								edsharepath.setText("");
							} else {
								isfirstselection = false;
							}
							tvspath.setVisibility(View.VISIBLE);
							edsharepath.setVisibility(View.VISIBLE);
							tvrestype.setVisibility(View.VISIBLE);
							restypeSpinner.setVisibility(View.VISIBLE);
							tvforwardspinner.setVisibility(View.GONE);
							forwardSpinner.setVisibility(View.GONE);
							tvredirecturl.setVisibility(View.GONE);
							etRedirecturl.setVisibility(View.GONE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		if (sett_bean != null) {
			if (sett_bean.getAction1().trim().length() != 0)
				Log.d("bool", "MMMMMMMMMMMMMMMM"
						+ sett_bean.getAction1().trim());
			if (!sett_bean.getActionSevices().equals("109")
					&& !sett_bean.getActionSevices().equals("110"))
				edsharepath.setText(sett_bean.getAction1().trim());
		}

		String[] buddies = callDisp.getmyBuddys();
		ArrayAdapter<String> ad1 = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, buddies);
		ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forwardSpinner.setAdapter(ad1);
		Log.d("BL", "going to set option");
		forwardSpinner.setSelection(CallDispatcher.forward);
		forwardSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String obj = (String) forwardSpinner
								.getItemAtPosition(arg2);

						callDisp.callToBeForward = obj;
						CallDispatcher.forward = arg2;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		if (CallDispatcher.action == 0) {
			// forwardSpinner.setEnabled(false);
			// etRedirecturl.setEnabled(false);
			tvforwardspinner.setVisibility(View.GONE);
			tvredirecturl.setVisibility(View.GONE);

			forwardSpinner.setVisibility(View.GONE);
			etRedirecturl.setVisibility(View.GONE);

			tvspath.setVisibility(View.GONE);
			edsharepath.setVisibility(View.GONE);
			tvrestype.setVisibility(View.GONE);
			restypeSpinner.setVisibility(View.GONE);
			/* changeAction(); */
		} else if (CallDispatcher.action == 1) {
			tvforwardspinner.setVisibility(View.GONE);
			forwardSpinner.setVisibility(View.GONE);

			tvredirecturl.setVisibility(View.VISIBLE);
			etRedirecturl.setVisibility(View.VISIBLE);
			tvspath.setVisibility(View.GONE);
			edsharepath.setVisibility(View.GONE);
			tvrestype.setVisibility(View.GONE);
			restypeSpinner.setVisibility(View.GONE);

		} else if (CallDispatcher.action == 2) {
			CallDispatcher.action = 2;

			tvforwardspinner.setVisibility(View.VISIBLE);
			forwardSpinner.setVisibility(View.VISIBLE);
			tvredirecturl.setVisibility(View.GONE);
			etRedirecturl.setVisibility(View.GONE);

			tvspath.setVisibility(View.GONE);
			edsharepath.setVisibility(View.GONE);
			tvrestype.setVisibility(View.GONE);
			restypeSpinner.setVisibility(View.GONE);
		} else if (CallDispatcher.action == 3) {
			CallDispatcher.action = 3;

			tvspath.setVisibility(View.VISIBLE);
			edsharepath.setVisibility(View.VISIBLE);
			tvrestype.setVisibility(View.VISIBLE);
			restypeSpinner.setVisibility(View.VISIBLE);
			tvforwardspinner.setVisibility(View.GONE);
			forwardSpinner.setVisibility(View.GONE);
			tvredirecturl.setVisibility(View.GONE);
			etRedirecturl.setVisibility(View.GONE);
		}

	}

	private void editTextSettings(boolean isChecked) {
		Log.e("note", "Have to modify");
		if (isChecked) {
			et_address.setText("");
			et_latitude.setText("");
			et_longitude.setText("");
			et_address.setVisibility(View.GONE);
			et_latitude.setVisibility(View.VISIBLE);
			et_longitude.setVisibility(View.VISIBLE);
		} else {
			et_address.setText("");
			et_latitude.setText("");
			et_longitude.setText("");
			et_address.setVisibility(View.VISIBLE);
			et_latitude.setVisibility(View.GONE);
			et_longitude.setVisibility(View.GONE);
		}
	}

	void changeAction() {
		try {

			Log.e("pos",
					"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcame to changeaction "
							+ CallDispatcher.action);
			if (CallDispatcher.action == 1) {

				String service_id = callDisp.getdbHeler(context).getServiceId(
						"Redirect to url");
				Log.i("thread", "@@@@@@@@@@@@@@@@@@@@ service id" + service_id);
				if (callDisp.getdbHeler(context).isRecordExists(
						"select * from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='busyresponse'")) {
					String updatequerry = "update Settings set service='"
							+ service_id
							+ "',value1='"
							+ etRedirecturl.getText().toString()
							+ "',value2='',remoteaddress='' where settings='busyresponse' and username='"
							+ CallDispatcher.LoginUser + "'";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							updatequerry);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				} else {
					String insertQuery = "insert into Settings(settings,service,username,value1,value2,remoteaddress)values('busyresponse','"
							+ service_id
							+ "','"
							+ CallDispatcher.LoginUser
							+ "','"
							+ etRedirecturl.getText().toString()
							+ "','','')";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							insertQuery);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				}
				if (sett_bean != null) {
					sett_bean.setActionServices(service_id);
					sett_bean.setActionValue1(etRedirecturl.getText()
							.toString());
					callDisp.setSettings(sett_bean);
				} else {
					sett_bean = callDisp.getdbHeler(context).getUserSettings();
					if (sett_bean != null) {
						sett_bean.setActionServices(service_id);
						sett_bean.setActionValue1(etRedirecturl.getText()
								.toString());
						callDisp.setSettings(sett_bean);
					}
				}
			} else if (CallDispatcher.action == 2
					&& callDisp.callToBeForward != null) {

				String service_id = callDisp.getdbHeler(context).getServiceId(
						"Forward");
				Log.i("thread", "@@@@@@@@@@@@@@@@@@@@ service id" + service_id);
				if (callDisp.getdbHeler(context).isRecordExists(
						"select * from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='busyresponse'")) {
					String updatequerry = "update Settings set service='"
							+ service_id
							+ "',value1='"
							+ callDisp.callToBeForward
							+ "',value2='',remoteaddress='' where settings='busyresponse' and username='"
							+ CallDispatcher.LoginUser + "'";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							updatequerry);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				} else {
					String insertQuery = "insert into Settings(settings,service,username,value1,value2,remoteaddress)values('busyresponse','"
							+ service_id
							+ "','"
							+ CallDispatcher.LoginUser
							+ "','" + callDisp.callToBeForward + "','','')";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							insertQuery);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				}
				if (sett_bean != null) {
					sett_bean.setActionServices(service_id);
					sett_bean.setActionValue1(callDisp.callToBeForward);
					callDisp.setSettings(sett_bean);
				} else {
					sett_bean = callDisp.getdbHeler(context).getUserSettings();
					if (sett_bean != null) {
						sett_bean.setActionServices(service_id);
						sett_bean.setActionValue1(callDisp.callToBeForward);
						callDisp.setSettings(sett_bean);
					}
				}

			} else if (CallDispatcher.action == 3) {
				Log.e("pos",
						"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcame to action 3");
				Log.e("pos",
						"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcame to action "
								+ edsharepath.getText().toString().trim());
				Log.e("pos",
						"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMcame to action "
								+ component_id);

				String service_id = null;

				if (type.equalsIgnoreCase("note"))
					service_id = callDisp.getdbHeler(context).getServiceId(
							SingleInstance.mainContext.getResources().getString(R.string.share_a_text_note));
				else if (type.equalsIgnoreCase("audio"))
					service_id = callDisp.getdbHeler(context).getServiceId(
							SingleInstance.mainContext.getResources().getString(R.string.share_audio_note));
				else if (type.equalsIgnoreCase("video"))
					service_id = callDisp.getdbHeler(context).getServiceId(
							SingleInstance.mainContext.getResources().getString(R.string.share_a_video_note));
				else if (type.equalsIgnoreCase("photo"))
					service_id = callDisp.getdbHeler(context).getServiceId(
							SingleInstance.mainContext.getResources().getString(R.string.share_a_photo_note));

				String restype = restypeSpinner.getSelectedItem().toString()
						.trim();
				if (restype.trim().length() != 0) {
					if (restype.equalsIgnoreCase("note"))
						restype = callDisp.getdbHeler(context).getServiceId(
								SingleInstance.mainContext.getResources().getString(R.string.notes));
					else if (restype.equalsIgnoreCase("audio"))
						restype = callDisp.getdbHeler(context).getServiceId(
								SingleInstance.mainContext.getResources().getString(R.string.audio));
					else if (restype.equalsIgnoreCase("video"))
						restype = callDisp.getdbHeler(context).getServiceId(
								SingleInstance.mainContext.getResources().getString(R.string.video));
					else if (restype.equalsIgnoreCase("image"))
						restype = callDisp.getdbHeler(context).getServiceId(
								SingleInstance.mainContext.getResources().getString(R.string.image));
				}

				Log.i("thread", "@@@@@@@@@@@@@@@@@@@@ service id" + service_id);
				Log.i("thread", "@@@@@@@@@@@@@@@@@@@@ service id" + restype);

				if (callDisp.getdbHeler(context).isRecordExists(
						"select * from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='busyresponse'")) {
					String updatequerry = "update Settings set service='"
							+ service_id
							+ "',value1='"
							+ edsharepath.getText().toString().trim()
							+ "',value2='"
							+ restype
							+ "',remoteaddress='' where settings='busyresponse' and username='"
							+ CallDispatcher.LoginUser + "'";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							updatequerry);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				} else {
					String insertQuery = "insert into Settings(settings,service,username,value1,value2,remoteaddress)values('busyresponse','"
							+ service_id
							+ "','"
							+ CallDispatcher.LoginUser
							+ "','"
							+ edsharepath.getText().toString().trim()
							+ "','" + restype + "','')";
					boolean res = callDisp.getdbHeler(context).ExecuteQuery(
							insertQuery);
					Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
				}
				if (sett_bean != null) {
					sett_bean.setActionServices(service_id);
					sett_bean.setActionValue1(edsharepath.getText().toString()
							.trim());
					sett_bean.setActionvalue2(restype);
					callDisp.setSettings(sett_bean);
				} else {
					sett_bean = callDisp.getdbHeler(context).getUserSettings();
					if (sett_bean != null) {
						sett_bean.setActionServices(service_id);
						sett_bean.setActionValue1(edsharepath.getText()
								.toString().trim());
						sett_bean.setActionvalue2(restype);
						callDisp.setSettings(sett_bean);
					}
				}
				if (ispathChanged)
					callDisp.uploadofflineResponse(edsharepath.getText()
							.toString(), false, 0, "settings");

			} else {
				try {
					if (callDisp.getdbHeler(context).isRecordExists(
							"select * from Settings where username='"
									+ CallDispatcher.LoginUser
									+ "' and settings='busyresponse'")) {
						String updatequerry = "delete from Settings where username='"
								+ CallDispatcher.LoginUser
								+ "' and settings='busyresponse'";
						boolean res = callDisp.getdbHeler(context)
								.ExecuteQuery(updatequerry);
						Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result"
								+ res);

					}

					if (sett_bean != null) {
						sett_bean.setActionServices("");
						sett_bean.setActionValue1("");
						sett_bean.setActionvalue2("");
						sett_bean.setFtpAddress("");
						callDisp.setSettings(sett_bean);
					}
				} catch (Exception e) {

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void ShowImMembers(final ArrayList al, View v, final int[] ix,
			final HashMap<String, String> hs) {

		final QuickAction1 quickAction = new QuickAction1(this);

		for (int i = 0; i < al.size(); i++) {
			ActionItem1 dashboard = new ActionItem1();
			// dashboard.setTitle("My name is Test");
			String size = Integer.toString(ix[i]);
			dashboard.setTitle((String) al.get(i) + "   " + size);
			// dashboard.
			dashboard.setIcon(getResources().getDrawable(R.drawable.kontak));
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

							showActiveSession(session, name);

							al.remove(pos);
							if (al.size() <= 0) {
								IMRequest.setVisibility(View.INVISIBLE);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						// ShowIm();
						// IMRequest.setVisibility(View.INVISIBLE);
					}
				});

		quickAction.show(v);
	}

	public void showActiveSession(String session, String selectedBuddy) {
		Log.d("session",
				"reloadCurrentSession................................."
						+ session);
		Log.d("session",
				"reloadCurrentSession................................."
						+ selectedBuddy);

		// if (WebServiceReferences.instantMessageScreen.containsKey(session)) {
		// InstantMessageScreen imscreen = (InstantMessageScreen)
		// WebServiceReferences.instantMessageScreen
		// .get(session);
		// imscreen.finish();
		// }
		SignalingBean bean = new SignalingBean();
		bean.setSessionid(session);
		bean.setFrom(CallDispatcher.LoginUser);
		bean.setTo(selectedBuddy);
		bean.setConferencemember("");
		bean.setMessage("");
		bean.setCallType("MSG");
		// Intent intent = new Intent(context, IMTabScreen.class);
		// intent.putExtra("sb", bean);
		// intent.putExtra("fromto", true);
		// startActivity(intent);

	}

	void saveLatLong(String lat, String longi, String imagePath, String address) {
		try {

			String service_id = callDisp.getdbHeler(context).getServiceId(
					init_service.getSelectedItem().toString().trim());
			Log.i("thread", "@@@@@@@@@@@@@@@@@@@@ service id" + service_id);

			if (callDisp.getdbHeler(context).isRecordExists(
					"select * from Settings where username='"
							+ CallDispatcher.LoginUser
							+ "' and settings='locationservice'")) {
				String updatequerry = "update Settings set service='"
						+ service_id + "',value1='" + lat + "',value2='"
						+ longi + "',remoteaddress='" + address
						+ "' where settings='locationservice' and username='"
						+ CallDispatcher.LoginUser + "'";
				boolean res = callDisp.getdbHeler(context).ExecuteQuery(
						updatequerry);
				Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);

			} else {
				String insertQuery = "insert into Settings(settings,service,username,value1,value2,remoteaddress)values('locationservice','"
						+ service_id
						+ "','"
						+ CallDispatcher.LoginUser
						+ "','"
						+ lat + "','" + longi + "','" + address + "')";
				boolean res = callDisp.getdbHeler(context).ExecuteQuery(
						insertQuery);
				Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
			}

			if (sett_bean != null) {
				sett_bean.setLcationValue1(lat);
				sett_bean.setLocationValue2(longi);
				sett_bean.setgeoRemoteAddree(address);
				sett_bean.setLocationService(service_id);
				callDisp.setSettings(sett_bean);
			} else {
				sett_bean = callDisp.getdbHeler(context).getUserSettings();
				sett_bean.setLcationValue1(lat);
				sett_bean.setLocationValue2(longi);
				sett_bean.setgeoRemoteAddree(address);
				sett_bean.setLocationService(service_id);
				callDisp.setSettings(sett_bean);
			}

		}

		catch (Exception e) {
			e.printStackTrace();

		}
	}

	void clearLatLong() {

		Log.i("lat", "################ came to clear latlong");
		if (callDisp.getdbHeler(context).isRecordExists(
				"select * from Settings where username='"
						+ CallDispatcher.LoginUser
						+ "' and settings='locationservice'")) {
			String delete = "delete from Settings where username='"
					+ CallDispatcher.LoginUser
					+ "' and settings='locationservice'";
			boolean res = callDisp.getdbHeler(context).ExecuteQuery(delete);
			Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
		}

		if (callDisp.getdbHeler(context).isRecordExists(
				"select * from Settings where username='"
						+ CallDispatcher.LoginUser
						+ "' and settings='locationset'")) {
			String dquerry = "delete from Settings where username='"
					+ CallDispatcher.LoginUser + "' and settings='locationset'";
			boolean res = callDisp.getdbHeler(context).ExecuteQuery(dquerry);
			Log.i("thread", "@@@@@@@@@@@@@@@@@@@ querry result" + res);
		}
		callDisp.isLocationServiceEnabled = false;
		if (sett_bean != null) {
			sett_bean.setLocationservEnabled("0");
			sett_bean.setLocationService("");
			sett_bean.setLocationValue2("");
			sett_bean.setLcationValue1("");
			callDisp.setSettings(sett_bean);
		} else {
			sett_bean = callDisp.getdbHeler(context).getUserSettings();
			if (sett_bean != null) {
				sett_bean.setLocationservEnabled("0");
				sett_bean.setLocationService("");
				sett_bean.setLocationValue2("");
				sett_bean.setLcationValue1("");
				callDisp.setSettings(sett_bean);
			}
		}

		callDisp.latConfigure = 0;
		callDisp.longConfigure = 0;
		callDisp.serviceType = 5;
		callDisp.location_Service = "";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3) {

			try {
				Bundle bun = data.getBundleExtra("location");
				String lnt = bun.getString("longitude");
				String lt = bun.getString("latitude");
				String address = bun.getString("address");
				if (address != null && lt != null && lnt != null) {
					et_address.setText(address);
					et_latitude.setText(lt);
					et_longitude.setText(lnt);
				}
			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		} else if ((requestCode == 100) && (resultCode == RESULT_OK)) {
			try {
				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String path = bun.getString("filepath");
					edsharepath.setText(path);
					component_id = bun.getString("id");
				}

			} catch (NullPointerException e) {

			} catch (Exception e) {

			}
		}
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	private void loadLocationValues() {
		if (sett_bean != null) {
			String address = sett_bean.getRemoteaddress();
			address = address.trim();

			String strLat = sett_bean.getLocationValue1();
			strLat = strLat.trim();
			String strLong = sett_bean.getLocationValue2();
			strLong = strLong.trim();
			String serviceid = sett_bean.getLocationService();
			String service = callDisp.getdbHeler(context).getServiceName(
					serviceid);

			Log.e("note", "Address :" + address);
			Log.e("note", "strLat :" + strLat);
			Log.e("note", "strLong :" + strLong);
			Log.e("note", "service id :" + serviceid);
			Log.e("note", "service :" + service);

			if (address.length() != 0) {
				Log.e("note", "First area");
				et_address.setText(address);
				btnManual.setChecked(false);
				editTextTextSettings(btnManual.isChecked());
				if (service.equalsIgnoreCase("video unicast")) {
					init_service.setSelection(1);
				} else if (service.equalsIgnoreCase("Audio unicast")) {
					init_service.setSelection(0);
				} else if (service.equalsIgnoreCase("Video Call")) {
					init_service.setSelection(2);
				} else if (service.equalsIgnoreCase("Audio Call")) {
					init_service.setSelection(3);
				}
			} else if (strLat.length() != 0 && strLong.length() != 0) {
				btnManual.setChecked(true);
				editTextTextSettings(btnManual.isChecked());
				et_latitude.setText(strLat);
				et_longitude.setText(strLong);
				if (service.equalsIgnoreCase("video unicast")) {
					init_service.setSelection(1);
				} else if (service.equalsIgnoreCase("Audio unicast")) {
					init_service.setSelection(0);
				} else if (service.equalsIgnoreCase("Video Call")) {
					init_service.setSelection(2);
				} else if (service.equalsIgnoreCase("Audio Call")) {
					init_service.setSelection(3);
				}

				Log.e("note", " LAT LONG Area");
			} else {
				r_layout1.setVisibility(View.GONE);
				r_layout2.setVisibility(View.GONE);
			}
			if (sett_bean.getLocationserviceEnabled().equals("1"))
				tgl_enableserv.setChecked(true);

		} else {
			et_latitude.setText("");
			et_latitude.setText("");
			et_address.setText("");
			r_layout1.setVisibility(View.GONE);
			r_layout2.setVisibility(View.GONE);
		}
	}

	private void editTextTextSettings(boolean isChecked) {
		Log.e("note", "test area");
		if (isChecked) {
			et_address.setVisibility(View.GONE);
			et_latitude.setVisibility(View.VISIBLE);
			et_longitude.setVisibility(View.VISIBLE);
		} else {
			et_address.setVisibility(View.VISIBLE);
			et_latitude.setVisibility(View.GONE);
			et_longitude.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (WebServiceReferences.contextTable.containsKey("a_play")) {
			WebServiceReferences.contextTable.remove("a_play");
		}
		if (WebServiceReferences.contextTable.containsKey("IM")) {
			WebServiceReferences.contextTable.remove("IM");
		}
		if (WebServiceReferences.contextTable.containsKey("locbusy")) {
			WebServiceReferences.contextTable.remove("locbusy");
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;

		Log.d("lg", "########### Show IM Called"
				+ WebServiceReferences.Imcollection.size());

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

	public void ShowError(String Title, String Message) {

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

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void notifyReceivedIM(SignalingBean sb) {
		// TODO Auto-generated method stub

	}

}
