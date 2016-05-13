package com.cg.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.lib.model.BuddyInformationBean;
import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cg.snazmed.R;
import com.cg.account.ShareByProfile;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.instancemessage.IMNotifier;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.FilesFragment;
import com.util.SingleInstance;

public class sendershare extends Activity implements TextWatcher, IMNotifier {
	private Context context;
	CallDispatcher calldisp;
	private CompleteListBean cmp;
	String filename;
	String componenttype;
	String comments;
	int compId;
	int type;
	private String reminderdatetime;
	String settimezone;
	private boolean isReminderTimePickerOpened;
	private AlertDialog alert = null;

	LinearLayout llay1 = null;
	LinearLayout llay2 = null;
	Button cancel = null;
	Button send = null;
	EditText buddieslist = null;
	EditText reminderzone = null;
	EditText setreminder = null;
	TextView tvv = null;
	TextView manbuddy, manrtime, mantz, manrtype;
	String username = "no";
	private Handler viewHandler = new Handler();
	final String[] choiceLists = { SingleInstance.mainContext.getResources().getString(
			R.string.send_to_a_contact), SingleInstance.mainContext.getResources().getString(
					R.string.share_by_profile_use),
					SingleInstance.mainContext.getResources()
					.getString(R.string.cancel) };

	private ArrayList<String> buddyList = new ArrayList<String>();
	private ArrayList<String> SendbuddyList = new ArrayList<String>();

	public ArrayList<String> uploadDatas = new ArrayList<String>();
	private Button IMRequest;
	private Button btn_notification = null;
	private Handler handler = new Handler();
	RelativeLayout rlayout = null;
	ToggleButton stream_toggle = null;
	private CheckBox by_date = null;
	private CheckBox by_time = null;
	private TextView ttl_value = null;
	private TextView ttl_result = null;
	private Spinner time_spinner = null;
	private RelativeLayout time_info = null;
	private RelativeLayout time_container = null;
	private EditText time_input = null;
	private ArrayList<String> uploadFilesList = null;
	private SharedPreferences preferences;
	private CompleteListBean cmpBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			context = this;
			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.custom_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.custom_title1);
			WebServiceReferences.contextTable.put("IM", this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				calldisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				calldisp = new CallDispatcher(context);

			calldisp.setNoScrHeight(noScrHeight);
			calldisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;

			Button btn = (Button) findViewById(R.id.btncomp);
			btn.setVisibility(View.INVISIBLE);

			btn_notification = (Button) findViewById(R.id.notification);
			uploadFilesList = new ArrayList<String>();
			preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			btn_notification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			Button btn_cancel = (Button) findViewById(R.id.settings);
			btn_cancel.setVisibility(View.GONE);

			setContentView(R.layout.sharereminderscreen);

			IMRequest = (Button) findViewById(R.id.share_im);
			IMRequest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					calldisp.openReceivedIm(arg0, context);
				}
			});
			IMRequest.setVisibility(View.INVISIBLE);

			rlayout = (RelativeLayout) findViewById(R.id.sharetogglelay);
			stream_toggle = (ToggleButton) findViewById(R.id.btnstreamManual);

			WebServiceReferences.contextTable.put("sendershare", this);
			final SharedPreferences SharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);

			String[] choiceList = calldisp.getmyBuddys();
			buddyList = new ArrayList<String>(Arrays.asList(choiceList));
			if (!calldisp.send_multiple) {
				filename = getIntent().getStringExtra("filename");
				componenttype = getIntent().getStringExtra("compname");
				username = getIntent().getStringExtra("username");
				comments = getIntent().getStringExtra("comments");

				Log.e("message", "*******" + filename + componenttype);

				if (componenttype != null
						&& !componenttype.equalsIgnoreCase("video")) {
					rlayout.setVisibility(View.GONE);
				}
			}
			cmpBean = (CompleteListBean) getIntent().getSerializableExtra(
					"cmpBean");
			llay1 = (LinearLayout) findViewById(R.id.showview);
			llay2 = (LinearLayout) findViewById(R.id.showview1);
			cancel = (Button) findViewById(R.id.cancelbtn);
			by_date = (CheckBox) findViewById(R.id.ttl_by_date);
			by_time = (CheckBox) findViewById(R.id.ttl_by_time);
			ttl_value = (TextView) findViewById(R.id.ttl_value);
			ttl_result = (TextView) findViewById(R.id.ttl_result);
			time_spinner = (Spinner) findViewById(R.id.time_spinner);
			time_container = (RelativeLayout) findViewById(R.id.time_container);
			time_info = (RelativeLayout) findViewById(R.id.ttl_lay2);
			time_input = (EditText) findViewById(R.id.time_input);
			time_input.addTextChangedListener(this);
			ArrayList<String> list = new ArrayList<String>();
			list.add("----Select-----");
			list.add("Hour");

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			time_spinner.setAdapter(dataAdapter);
			manbuddy = (TextView) findViewById(R.id.tvbuddyIdentity);
			manrtime = (TextView) findViewById(R.id.tvrtimeIdentity);
			mantz = (TextView) findViewById(R.id.tvtzoneIdentity);
			manrtype = (TextView) findViewById(R.id.tvrtypeIdentity);

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (buddieslist.getText().toString() != null) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							builder.setMessage(
									SingleInstance.mainContext.getResources().getString(R.string.do_you_want_to_leave_before_sharing_the_note))

									.setCancelable(false)
									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													finish();
												}
											})
									.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													dialog.cancel();

												}
											});
							AlertDialog alert = builder.create();
							alert.show();

						} else {
							finish();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			send = (Button) findViewById(R.id.btndone);

			buddieslist = (EditText) findViewById(R.id.selbuddies);
			buddieslist.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (CallDispatcher.LoginUser != null) {
						showSingleSelectBuddy(choiceLists, "");
					} else {
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.please_login_to_the_system),
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			setreminder = (EditText) findViewById(R.id.remindertime);
			setreminder.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {

						if (event.getAction() == MotionEvent.ACTION_UP) {
							Button btnSet;
							final DatePicker dp;
							final TimePicker tp;
							final AlertDialog alertReminder = new AlertDialog.Builder(
									context).create();

							ScrollView tblDTPicker = (ScrollView) View.inflate(
									context, R.layout.date_time_picker, null);

							btnSet = (Button) tblDTPicker
									.findViewById(R.id.btnSetDateTime);
							dp = (DatePicker) tblDTPicker
									.findViewById(R.id.datePicker);
							tp = (TimePicker) tblDTPicker
									.findViewById(R.id.timePicker);
							btnSet.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									isReminderTimePickerOpened = true;

									final String strDateTime = dp.getYear()
											+ "-"
											+ WebServiceReferences
													.setLength2((dp.getMonth() + 1))
											+ "-"
											+ WebServiceReferences
													.setLength2(dp
															.getDayOfMonth())
											+ " "
											+ WebServiceReferences
													.setLength2(tp
															.getCurrentHour())
											+ ":"
											+ WebServiceReferences.setLength2(tp
													.getCurrentMinute());

									if (CompleteListView
											.CheckReminderIsValid(strDateTime)) {

										Toast.makeText(context,
												"Reminder notes added !",
												Toast.LENGTH_SHORT).show();

										setreminder.setText(strDateTime);
										reminderdatetime = strDateTime;
										alertReminder.cancel();
									} else {

										Toast.makeText(
												context,
												"Please assign future date and time",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
							alertReminder.setTitle("Reminder");
							alertReminder.setView(tblDTPicker);
							alertReminder.show();
						}
						return false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}

				}
			});

			reminderzone = (EditText) findViewById(R.id.remindrzone);

			reminderzone.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub

					if (event.getAction() == MotionEvent.ACTION_UP) {
						Intent intent = new Intent(context,
								TestFilterListView.class);
						startActivityForResult(intent, 4);

					}
					return false;

				}
			});

			by_date.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					try {
						// TODO Auto-generated method stub
						if (isChecked) {
							by_time.setChecked(false);
							// time_container.setVisibility(View.GONE);
							Button btnSet;
							final DatePicker dp;
							final TimePicker tp;
							final AlertDialog alertReminder = new AlertDialog.Builder(
									sendershare.this).create();

							ScrollView tblDTPicker = (ScrollView) View.inflate(
									sendershare.this, R.layout.date_time_picker, null);

							btnSet = (Button) tblDTPicker
									.findViewById(R.id.btnSetDateTime);
							dp = (DatePicker) tblDTPicker
									.findViewById(R.id.datePicker);
							tp = (TimePicker) tblDTPicker
									.findViewById(R.id.timePicker);
							btnSet.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									isReminderTimePickerOpened = true;

									final String strDateTime = WebServiceReferences
											.setLength2(dp.getDayOfMonth())
											+ "/"
											+ WebServiceReferences
													.setLength2((dp.getMonth() + 1))
											+ "/"
											+ dp.getYear()
											+ " "
											+ WebServiceReferences
													.setLength2(tp
															.getCurrentHour())
											+ ":"
											+ WebServiceReferences.setLength2(tp
													.getCurrentMinute());

									Log.e("timemessage", "@@@@@@@"
											+ strDateTime);
									SimpleDateFormat inPutFormatter = new SimpleDateFormat(
											"dd/MM/yyyy HH:mm");
									SimpleDateFormat outPutFormatter = new SimpleDateFormat(
											"dd/MM/yyyy hh:mm aa");
									DateFormatSymbols dfSym = new DateFormatSymbols();
									dfSym.setAmPmStrings(new String[] { "am",
											"pm" });
									outPutFormatter.setDateFormatSymbols(dfSym);
									Date date = null;
									try {
										date = inPutFormatter
												.parse(strDateTime);

									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									String newDate = outPutFormatter
											.format(date);

									if (CompleteListView
											.CheckReminderIsValid(newDate)) {

										Toast.makeText(context, "TTL added !",
												Toast.LENGTH_SHORT).show();

										time_info.setVisibility(View.VISIBLE);
										ttl_value.setText(newDate);
										ttl_result
												.setText("File Will Be Deleted at "
														+ newDate);

										alertReminder.cancel();
									} else {

										Toast.makeText(
												context,
												"Please assign future date and time",
												Toast.LENGTH_SHORT).show();
									}

								}
							});
							alertReminder.setTitle("TTL");
							alertReminder.setView(tblDTPicker);
							alertReminder.show();

						} else {
							ttl_value.setText("");
							time_container.setVisibility(View.GONE);
							time_info.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			by_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						by_date.setChecked(false);
						ttl_result.setText(SingleInstance.mainContext.getResources().getString(R.string.file_will_be_deleted_after)

								+ time_input.getText().toString()
								+ SingleInstance.mainContext.getResources().getString(R.string.hr_from_the_time_of_file_received)
);

						time_info.setVisibility(View.VISIBLE);
						time_container.setVisibility(View.VISIBLE);
					} else {
						ttl_value.setText("");
						time_info.setVisibility(View.GONE);
						time_container.setVisibility(View.GONE);
					}
				}
			});

			// reminderzone.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// Intent intent = new Intent(context, TestFilterListView.class);
			// startActivityForResult(intent, 4);
			// }
			// });

			final Spinner spinner1 = (Spinner) findViewById(R.id.sharespinner1);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.note_array,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(adapter);

			send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String value = spinner1.getSelectedItem().toString();
					if (value.contains("--Please Select--"))
						value = "";
					if (calldisp.send_multiple) {
						sendMultipleShares(value);
					} else {

						Log.e("sendershare",
								"*********................................"
										+ value + value.length());
						if (SendbuddyList.size() > 0) {
							Log.e("sendershare",
									"*********................................ array >"
											+ buddyList.size());
							if (reminderdatetime != null) {
								Log.e("sendershare",
										"*********................................ reminder >"
												+ reminderdatetime);
								if (WebServiceReferences.running) {
									uploadDatas.add(componenttype);
									uploadDatas.add("true");
									uploadDatas.add(reminderdatetime);
									uploadDatas.add("");

									if (settimezone != null) {
										uploadDatas.add(settimezone);
									} else {
										uploadDatas.add("");
									}
									uploadDatas.add("auto");
									Log.d("sendershare", "" + uploadDatas);
									sendshare(true);
								}
							} else {
								if (spinner1.getSelectedItem().toString()
										.length() != 0) {
									/*
									 * Toast.makeText(context,
									 * "Set Date and Time for Response Type",
									 * Toast.LENGTH_LONG).show();
									 */

									if (WebServiceReferences.running) {
										uploadDatas.add(componenttype);
										uploadDatas.add("false");
										uploadDatas.add("");
										uploadDatas.add(value);
										uploadDatas.add("");
										uploadDatas.add("auto");
										sendshare(true);
									}

								} else if (settimezone != null) {
									Toast.makeText(
											context,
											"Reminder is not set Timezone will be ignored",
											Toast.LENGTH_SHORT).show();
									if (WebServiceReferences.running) {
										uploadDatas.add(componenttype);
										uploadDatas.add("false");
										uploadDatas.add("");
										uploadDatas.add("");
										uploadDatas.add("auto");
										Log.d("sendershare", "" + uploadDatas);
										sendshare(true);
									}

								} else if ((spinner1.getSelectedItem()
										.toString().length() == 0)
										&& (settimezone == null)) {
									Log.e("sendershare",
											"*********................................ came to else333333333333");
									if (WebServiceReferences.running) {
										uploadDatas.add(componenttype);
										uploadDatas.add("false");
										uploadDatas.add("");
										uploadDatas.add("");
										uploadDatas.add("auto");
										Log.d("sendershare", "" + uploadDatas);
										sendshare(true);
									}

								}

							}

						} else {
							Toast.makeText(context,
									SingleInstance.mainContext.getResources().getString(R.string.kindly_select_any_buddies),
									Toast.LENGTH_LONG).show();

						}

						// sendShare(spinner1.getSelectedItem().toString());

					}
				}

			});

			if (CallDispatcher.LoginUser != null) {
				// TODO Auto-generated method stub

				if (username.equalsIgnoreCase("No")) {
					showMultiselectBuddys();

				} else {
					buddieslist.setText(username);
					String[] buddiesname = username.split(",");
					for (int i = 0; i < buddiesname.length; i++) {
						if (!buddiesname[i]
								.equalsIgnoreCase(CallDispatcher.LoginUser)) {
							SendbuddyList.add(buddiesname[i]);

						}

					}
				}

			} else {
				Toast.makeText(context, "Kindly login before share!",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMultipleShares(String item_type) {

		try {
			for (CompleteListBean bean : calldisp.multiple_componentlist) {
				calldisp.cmp = bean;
				filename = bean.getContentpath();
				componenttype = bean.getcomponentType();
				compId = bean.getComponentId();
				username = CallDispatcher.LoginUser;
				comments = bean.getContent();
				if (uploadDatas != null)
					uploadDatas.clear();
				if (bean.getcomponentType().equalsIgnoreCase("sketch"))
					componenttype = "handsketch";

				Log.e("sendershare",
						"*********................................ array >"
								+ buddyList.size());
				if (reminderdatetime != null) {
					Log.e("sendershare",
							"*********................................ reminder >"
									+ reminderdatetime);
					if (WebServiceReferences.running) {
						uploadDatas.add(componenttype);
						uploadDatas.add("true");
						uploadDatas.add(reminderdatetime);
						if (item_type.trim().length() != 0) {
							uploadDatas.add(item_type);
						} else {
							uploadDatas.add("");
						}
						if (settimezone != null) {
							uploadDatas.add(settimezone);
						} else {
							uploadDatas.add("");
						}
						uploadDatas.add("auto");
						Log.d("sendershare", "" + uploadDatas);
						sendshare(false);
					}
				} else {
					if (item_type.trim().length() != 0) {
						/*
						 * Toast.makeText(context,
						 * "Set Date and Time for Response Type",
						 * Toast.LENGTH_LONG).show();
						 */

						if (WebServiceReferences.running) {
							uploadDatas.add(componenttype);
							uploadDatas.add("false");
							uploadDatas.add("");
							if (item_type.trim().length() != 0) {
								uploadDatas.add(item_type);
							} else {
								uploadDatas.add("");
							}
							uploadDatas.add("");
							uploadDatas.add("auto");
							sendshare(false);
						}
					} else if (settimezone != null) {
						Toast.makeText(context,
								"Reminder is not set Timezone will be ignored",
								Toast.LENGTH_SHORT).show();
						if (WebServiceReferences.running) {
							uploadDatas.add(componenttype);
							uploadDatas.add("false");
							uploadDatas.add("");
							uploadDatas.add("");
							uploadDatas.add("auto");
							Log.d("sendershare", "" + uploadDatas);
							sendshare(false);
						}

					} else if ((item_type.trim().length() == 0)
							&& (settimezone == null)) {
						Log.e("sendershare",
								"*********................................ came to else333333333333");
						if (WebServiceReferences.running) {
							uploadDatas.add(componenttype);
							uploadDatas.add("false");
							uploadDatas.add("");
							uploadDatas.add("");
							uploadDatas.add("auto");
							Log.d("sendershare", "" + uploadDatas);
							sendshare(false);
						}
					}

				}

			}
			calldisp.multiple_componentlist.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * To send file to buddies (file sharing)
	 */
	private void sendshare(boolean flag) {
		try {
			String username = CallDispatcher.LoginUser;
			String password = CallDispatcher.Password;
			ArrayList<String> b_list = new ArrayList<String>();
			b_list.addAll(SendbuddyList);
			Collections.sort(b_list);
			ArrayList<String> u_list = new ArrayList<String>();
			u_list.addAll(uploadDatas);
			calldisp.uploadData.add(filename);
			uploadFilesList.add(filename);

			/*
			 * To upload files using webservice 
			 */
			 String path = filename;
            Log.i("FileUpload", "type--->" + componenttype);

            if(componenttype.equalsIgnoreCase("photo")||componenttype.equalsIgnoreCase("handsketch")) {
                Log.i("FileUpload", "IF PHOTO||Handsketch--->");

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                String base64 = encodeTobase64(bitmap);
                String fname = filename.split("/")[5];
                Log.i("FileUpload", "fname--->" + fname);
                Log.i("FileUpload", "uname--->" + username);
                Log.i("FileUpload", "password--->" + password);
                Log.i("FileUpload", "type--->" + componenttype);
                Log.i("FileUpload", "base64--->" + base64);
                calldisp.uploadFile(username, password, componenttype, fname, base64,filename,sendershare.this);


			}else if(componenttype.equalsIgnoreCase("audio")||componenttype.equalsIgnoreCase("video"))
            {
//                encodeAudioVideoToBase64


                Log.i("FileUpload", "ELSE IF AUDIO||Video--->" );
				Log.i("FileUploadIM", "path" +filename);
                String base64 = encodeAudioVideoToBase64(path);
				if(componenttype.equalsIgnoreCase("video"))
					base64 = encodeAudioVideoToBase64(path+".mp4");

                String fname = filename.split("/")[3];
                Log.i("FileUpload", "fname--->" + fname);
                Log.i("FileUpload", "uname--->" + username);
                Log.i("FileUpload", "password--->" + password);
                Log.i("FileUpload", "type--->" + componenttype);
                Log.i("FileUpload", "base64--->" + base64);
				if(componenttype.equalsIgnoreCase("video"))
                	calldisp.uploadFile(username, password, componenttype, fname+".mp4", base64,filename,sendershare.this);
				else
					calldisp.uploadFile(username, password, componenttype, fname, base64,filename,sendershare.this);
            }
			ProgressDialog dialog = new ProgressDialog(context);
			calldisp.showprogress(dialog, context);

			/*
			 * 
			 */
//			if(componenttype.equalsIgnoreCase("note") || componenttype.equalsIgnoreCase("document"))
//				calldisp.sendshare(flag, username, password, buddieslist.getText()
//						.toString(), b_list, u_list, componenttype, comments,
//						filename, "sendFiles", by_time, time_spinner, ttl_result,
//						ttl_value, time_input, stream_toggle.isChecked(), cmpBean);

			FilesFragment fragment = FilesFragment.newInstance(context);
			if (fragment.filesList != null) {
				for (CompleteListBean cbean : fragment.filesList) {
					cbean.setSelected(false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendFile(){
		calldisp.cancelDialog();
		ArrayList<String> b_list = new ArrayList<String>();
		b_list.addAll(SendbuddyList);
		Collections.sort(b_list);
		ArrayList<String> u_list = new ArrayList<String>();
		u_list.addAll(uploadDatas);
		calldisp.uploadData.add(filename);
		uploadFilesList.add(filename);
		calldisp.sendshare(true, username, CallDispatcher.Password, buddieslist.getText()
						.toString(), b_list, u_list, componenttype, comments,
				filename, "sendFiles", by_time, time_spinner, ttl_result,
				ttl_value, time_input, stream_toggle.isChecked(), cmpBean);
	}

	void showSingleSelectBuddy(final String[] choiceList, final String edit) {

		try

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.create();
			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (choiceList[which]
									.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(
											R.string.send_to_a_contact))) {
								showMultiselectBuddys();
							} else if (choiceList[which]
									.equalsIgnoreCase(SingleInstance.mainContext.getResources().getString(
											R.string.share_by_profile_use))) {
								if (CallDispatcher.LoginUser != null) {
									Intent intent = new Intent(context,
											ShareByProfile.class);
									intent.putExtra("activity", "note");

									startActivityForResult(intent, 8);
								} else
									Toast.makeText(context, "Kindly Login Before you share", Toast.LENGTH_SHORT)
											.show();
							} else if (choiceList[which]
									.equalsIgnoreCase("cancel")) {
								alert.dismiss();

							}

							alert.dismiss();
						}
					});
			alert = builder.create();
			if (choiceList != null) {
				if (choiceList.length != 0) {
					alert.show();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void showMultiselectBuddys() {
		try {
			SendbuddyList.clear();
			buddieslist.setText("");

			AlertDialog.Builder d = new AlertDialog.Builder(context);
			// builder.setTitle("Add");

			ArrayList<String> total_names = new ArrayList<String>();
			total_names.add(SingleInstance.mainContext.getResources().getString(R.string.select_all_filess2));
			// total_names.addAll(Pro_names);
			for (BuddyInformationBean cBean : ContactsFragment.getBuddyList()) {
				if (!cBean.isTitle()) {
					if (!cBean.getStatus().equalsIgnoreCase("new")
							&& !cBean.getStatus().equalsIgnoreCase("Pending")) {
						total_names.add(cBean.getName());
					}
				}
			}

			// ArrayList<String> Pro_names = getOnlineBuddysAsList();

			String[] stringArray = total_names.toArray(new String[total_names
					.size()]);
			final CharSequence[] choiceList = stringArray;
			d.setTitle(SingleInstance.mainContext.getResources().getString(R.string.buddies_list)
);
			d.setMultiChoiceItems(choiceList, null,
					new OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int position, boolean isChecked) {
							if (isChecked) {
								Log.i("Share", "===>" + position);
								if (position == 0) {
									ListView list = ((AlertDialog) dialog)
											.getListView();
									for (int i = 0; i < list.getCount(); i++) {
										list.setItemChecked(i, true);

									}
								}

							} else {
								if (position == 0) {
									ListView list = ((AlertDialog) dialog)
											.getListView();
									for (int i = 0; i < list.getCount(); i++) {
										list.setItemChecked(i, false);

									}
								}
							}
						}
					});

			d.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					ListView list = ((AlertDialog) dialog).getListView();

					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < list.getCount(); i++) {
						boolean checked = list.isItemChecked(i);

						if (checked) {

							if (sb.length() > 0)

								sb.append(",");
							sb.append(list.getItemAtPosition(i));
							SendbuddyList.add(list.getItemAtPosition(i)
									.toString());
						}
					}
					String buddynames = sb.toString();
					if (buddynames.endsWith(",")) {
						buddynames = buddynames.substring(0,
								buddynames.length() - 1);
					}
					buddieslist.setText(buddynames);

				}

			});
			d.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (data != null) {

				if (requestCode == 4) {
					TimeZoneListBean retObj = (TimeZoneListBean) data
							.getSerializableExtra("tz");

					int rawOffset = retObj.getOffsetTime();
					int hour = rawOffset / (60 * 60 * 1000);
					int min = Math.abs(rawOffset / (60 * 1000)) % 60;

					String strHour = null;
					if (hour >= 0) {
						strHour = (hour < 10 ? "0" : "") + hour;
					} else if (hour > -10) {
						strHour = (hour < 0 && hour > -10 ? "-0" : "")
								+ (-1 * hour);
					} else {
						strHour = "" + hour;
					}

					strHour = (hour >= 0 ? "+" : "") + strHour;

					String strMin = (min < 10 ? "0" : "") + min;

					String strKey = "GMT" + strHour + ":" + strMin;
					reminderzone.setText(strKey + retObj.getId());
					settimezone = reminderzone.getText().toString();

				}

			}

			if ((requestCode == 8) && (resultCode == -1)) {
				Log.i("IOS", "INSIDE ONACTIVITY RESULT=====>");
				Bundle bun = data.getBundleExtra("share");
				if (bun != null) {
					String[] userid = bun.getStringArray("userid");
					StringBuffer sbf = new StringBuffer();

					if (userid.length > 0) {

						sbf.append(userid[0]);
						for (int i = 1; i < userid.length; i++) {
							sbf.append(",").append(userid[i]);
						}
						buddieslist.setText(sbf.toString());

					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyfileSent(boolean result) {

		try {
			Log.e("tag", "" + result);
			if (!result) {
				Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(context, "successfully send", Toast.LENGTH_SHORT)
						.show();
			}

			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ShowError(String Title, String Message) {
		try {
			AlertDialog confirmation = new AlertDialog.Builder(this).create();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (buddieslist.getText().toString() != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setMessage(SingleInstance.mainContext.getResources().getString(R.string.do_you_want_to_go_back_before_sending)
)
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											finish();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.cancel();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				} else {
					finish();
				}
				if (ttl_value.getVisibility() != View.VISIBLE) {
					by_date.setChecked(false);
					by_time.setChecked(false);
				}
			}

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub
			if (WebServiceReferences.contextTable.containsKey("sendershare")) {
				WebServiceReferences.contextTable.remove("sendershare");
			}
			super.onDestroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;

		try {
			Log.d("lg", ".............. on resume called");
			if (WebServiceReferences.Imcollection.size() == 0)
				IMRequest.setVisibility(View.GONE);
			else
				IMRequest.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<String> getOnlineBuddysAsList() {
		try {

			ArrayList<String> buddies = new ArrayList<String>();
			for (BuddyInformationBean bean : ContactsFragment.getBuddyList()) {
				if (!bean.isTitle()) {
					if (!bean.getStatus().equalsIgnoreCase("Pending")) {
						buddies.add(bean.getName());
						if (buddies.contains(CallDispatcher.LoginUser)) {
							buddies.remove(CallDispatcher.LoginUser);
						}
					}
				}
			}
			Collections.sort(buddies);
			return buddies;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void notifymultipleupload() {
		try {
			if (uploadDatas.size() > 0) {
				uploadDatas.clear();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		try {
			// TODO Auto-generated method stub

			ttl_value.setText(time_input.getText().toString());

			ttl_result.setText(SingleInstance.mainContext.getResources().getString(R.string.file_will_be_deleted_after)

					+ time_input.getText().toString()
					+ SingleInstance.mainContext.getResources().getString(R.string.hr_from_the_time_of_file_received)
);
			if (time_input.getText().toString().trim().length() > 0) {
				time_spinner.setSelection(1);
			} else {
				time_spinner.setSelection(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

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

				if (!calldisp.getdbHeler(context).userChatting(sb.getFrom())) {
					calldisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}
	private String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}
	private String encodeAudioVideoToBase64(String path){
        String strFile=null;
        File file=new File(path);
        try {
            FileInputStream file1=new FileInputStream(file);
            byte[] Bytearray=new byte[(int)file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.NO_WRAP);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("FileUpload", "audioVideoEncode========"+strFile);
        return strFile;
    }
	private static String encodeFileToBase64(String fileName) throws IOException {
		File file=new File(fileName);
		byte[] bytes = new byte[(int)file.length()];
		String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
		return encodedString;
	}

}
