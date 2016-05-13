package com.cg.quickaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.CompleteListView;
import com.cg.timer.ReminderService;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class QuickActionCallsSchedule extends Activity {

	Context context;
	TextView tv_title;
	Button btn_save;
	public Button IMRequest;
	Button btn_notification;
	ImageView bt_back;
	String title, action, To, label, description, strDateTime, time, filepath,
			type;
	EditText userInputFrequency;
	Spinner RepeatSpinner;
	TextView TimeDate, Repeat_Timedate;
	LinearLayout currentTimeDate, repeatCurrentby, repeatCurrentTimeDate;
	String value = "This task will run manually";
	ContactLogicbean beanObj;
	CheckBox ch_runmanually, ch_runonspecific, ch_repeat;
	String status, days, selecteditem, number;
	TextView tv_runmanually, tv_runonspecific, tv_repeat;
	String Times_mode, frequncy, runoption, mode;
	String checkboxfield = "";
	String spinnerfield = "";
	String frequencyfield = "";
	String datefield = "";
	RelativeLayout rl_runtheaction, rl_startat, rl_run;
	String DBTime = null;
	String DBFrequency = null;
	String DBFrequencyMode = null;
	boolean editSchedule;
	String runMode = null;
	public String owner;
	CallDispatcher callDisp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			context = this;
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.schedule);
			userInputFrequency = (EditText) findViewById(R.id.userinput);
			RepeatSpinner = (Spinner) findViewById(R.id.Repeat_spinner);

			WebServiceReferences.contextTable.put("QuickActionCallsSchedule",
					this);
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
			beanObj = (ContactLogicbean) getIntent().getParcelableExtra(
					"qabean");
			editSchedule = getIntent().getExtras().getBoolean("editSchedule");
			if (editSchedule) {
				runMode = beanObj.getRunMode();
			}
			btn_save = (Button) findViewById(R.id.btn_save);
			bt_back = (ImageView) findViewById(R.id.bt_back);
			TimeDate = (TextView) findViewById(R.id.timedate);
			Repeat_Timedate = (TextView) findViewById(R.id.Repeat_timedate);
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int min = c.get(Calendar.MINUTE);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DAY_OF_MONTH);
			int ds = c.get(Calendar.AM_PM);
			Date dates = null;
			SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dates = inFormat.parse(date + "-" + month + "-" + year);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
			outFormat.format(dates);

			if (ds == 0) {
			} else {
			}
			String CurrentDate = year + "-" + month + "-" + date + " " + hour
					+ ":" + min;
			TimeDate.setText(CurrentDate);
			Repeat_Timedate.setText(CurrentDate);
			Repeat_Timedate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub

					Button btnSet;
					final DatePicker dp;
					final TimePicker tp;
					final AlertDialog alertReminder = new AlertDialog.Builder(
							context).create();

					ScrollView tblDTPicker = (ScrollView) View.inflate(context,
							R.layout.date_time_picker, null);

					btnSet = (Button) tblDTPicker
							.findViewById(R.id.btnSetDateTime);
					dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
					tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

					btnSet.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							strDateTime = +dp.getYear()
									+ "-"
									+ WebServiceReferences.setLength2((dp
											.getMonth() + 1))
									+ "-"
									+ WebServiceReferences.setLength2(dp
											.getDayOfMonth())
									+ " "
									+ WebServiceReferences.setLength2(tp
											.getCurrentHour())
									+ ":"
									+ WebServiceReferences.setLength2(tp
											.getCurrentMinute());
							if (CompleteListView
									.CheckReminderIsValid(strDateTime)) {
								alertReminder.dismiss();
								Intent reminderIntent = new Intent(context,
										ReminderService.class);
								startService(reminderIntent);
								DBTime = strDateTime;
								DBFrequency = userInputFrequency.getText()
										.toString().trim();
								DBFrequencyMode = RepeatSpinner
										.getSelectedItem().toString();
								Repeat_Timedate.setText(strDateTime);
							} else {
								Toast.makeText(context,
										SingleInstance.mainContext.getResources().getString(R.string.pls_future_time), 1)
										.show();
							}
						}
					});
					alertReminder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.date_and_time));
					alertReminder.setView(tblDTPicker);
					alertReminder.show();

				}

			});

			TimeDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btnSet;
					final DatePicker dp;
					final TimePicker tp;
					final AlertDialog alertReminder = new AlertDialog.Builder(
							context).create();

					ScrollView tblDTPicker = (ScrollView) View.inflate(context,
							R.layout.date_time_picker, null);

					btnSet = (Button) tblDTPicker
							.findViewById(R.id.btnSetDateTime);
					dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
					tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

					btnSet.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							strDateTime = +dp.getYear()
									+ "-"
									+ WebServiceReferences.setLength2((dp
											.getMonth() + 1))
									+ "-"
									+ WebServiceReferences.setLength2(dp
											.getDayOfMonth())
									+ " "
									+ WebServiceReferences.setLength2(tp
											.getCurrentHour())
									+ ":"
									+ WebServiceReferences.setLength2(tp
											.getCurrentMinute());
							Log.i("StringDateTime :: ", "Time:: " + strDateTime);
							if (CompleteListView
									.CheckReminderIsValid(strDateTime)) {
								Log.i("thread",
										"################## time"
												+ tp.getCurrentHour());
								Log.i("thread",
										"################## time"
												+ tp.getCurrentMinute());
								alertReminder.dismiss();
								Intent reminderIntent = new Intent(context,
										ReminderService.class);
								startService(reminderIntent);
								TimeDate.setText(strDateTime);
							} else {
								Toast.makeText(context,
										SingleInstance.mainContext.getResources().getString(R.string.pls_future_time), 1)
										.show();
							}
						}
					});
					alertReminder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.reminder));
					alertReminder.setView(tblDTPicker);
					alertReminder.show();

				}

			});

			tv_runmanually = (TextView) findViewById(R.id.run_manually);
			tv_runonspecific = (TextView) findViewById(R.id.runanspecific);
			tv_repeat = (TextView) findViewById(R.id.repeat);

			checkboxfield = getIntent().getStringExtra("checkboxfield");
			spinnerfield = getIntent().getStringExtra("spinnerfield");
			frequencyfield = getIntent().getStringExtra("frequencyfield");
			datefield = getIntent().getStringExtra("datefield");

			ch_runmanually = (CheckBox) findViewById(R.id.ch_runmanually);
			ch_runonspecific = (CheckBox) findViewById(R.id.ch_runonspecific);
			ch_repeat = (CheckBox) findViewById(R.id.ch_repeat);

			currentTimeDate = (LinearLayout) findViewById(R.id.currentTimeandDate);
			repeatCurrentby = (LinearLayout) findViewById(R.id.Repeatby);
			repeatCurrentTimeDate = (LinearLayout) findViewById(R.id.Repeat_currentTimeandDate);

			rl_run = (RelativeLayout) findViewById(R.id.rl_run);

			ArrayList<String> list = new ArrayList<String>();
			list.add("----Select-----");
			list.add("Weeks");
			list.add("Months");
			list.add("Day");
			list.add("Hour");
			list.add("Minutes");
			// list.add("Stop Snooze");

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			RepeatSpinner.setAdapter(dataAdapter);

			RepeatSpinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							Object item = parent.getItemAtPosition(pos);

							Times_mode = (String) item;

							if (Times_mode.equalsIgnoreCase("Select")) {
								RepeatSpinner.setSelection(0);
							} else if (Times_mode.equalsIgnoreCase("Weeks")) {
								RepeatSpinner.setSelection(1);
							} else if (Times_mode.equalsIgnoreCase("Months")) {
								RepeatSpinner.setSelection(2);
							}

							else if (Times_mode.equalsIgnoreCase("Day")) {
								RepeatSpinner.setSelection(3);
							}

							else if (Times_mode.equalsIgnoreCase("Hour")) {
								RepeatSpinner.setSelection(4);
							}

							else if (Times_mode.equalsIgnoreCase("Minutes")) {
								RepeatSpinner.setSelection(5);
							}

							Log.i("welcome",
									"selected item-->" + item.toString());
						}

						public void onNothingSelected(AdapterView<?> parent) {
						}

					});

			if (editSchedule) {
				if (runMode.equals("RP")) {
					spinnerfield = beanObj.getModeTime();
					if (spinnerfield != null) {
						if (spinnerfield.equalsIgnoreCase("----Select-----")) {
							RepeatSpinner.setSelection(0);
						}

						else if (spinnerfield.equalsIgnoreCase("Weeks")) {
							RepeatSpinner.setSelection(1);
						} else if (spinnerfield.equalsIgnoreCase("Months")) {
							RepeatSpinner.setSelection(2);
						}

						else if (spinnerfield.equalsIgnoreCase("Day")) {
							RepeatSpinner.setSelection(3);
						}

						else if (spinnerfield.equalsIgnoreCase("Hour")) {
							RepeatSpinner.setSelection(4);
						}

						else if (spinnerfield.equalsIgnoreCase("Minutes")) {
							RepeatSpinner.setSelection(5);
						}
					}
				}
				if (runMode.equals("RM")) {
					ch_runmanually.setChecked(true);
					ch_runonspecific.setChecked(false);
					ch_repeat.setChecked(false);

					tv_runmanually.setTextColor(Color.parseColor("#13ABE1"));
					ch_runonspecific.setTextColor(Color.parseColor("#000000"));
					tv_repeat.setTextColor(Color.parseColor("#000000"));
					TimeDate.setText(beanObj.getEdittime());
					RepeatSpinner.setEnabled(false);

				} else if (runMode.equals("RS")) {
					ch_runonspecific.setChecked(true);
					ch_runmanually.setChecked(false);
					ch_repeat.setChecked(false);
					currentTimeDate.setVisibility(View.VISIBLE);
					TimeDate.setText(beanObj.getEdittime());
					tv_runonspecific.setTextColor(Color.parseColor("#13ABE1"));
					tv_runmanually.setTextColor(Color.parseColor("#000000"));
					tv_repeat.setTextColor(Color.parseColor("#000000"));
					RepeatSpinner.setEnabled(true);

				} else if (runMode.equals("RP")) {
					ch_repeat.setChecked(true);
					ch_runonspecific.setChecked(false);
					ch_runmanually.setChecked(false);
					repeatCurrentby.setVisibility(View.VISIBLE);
					Repeat_Timedate.setVisibility(View.VISIBLE);
					Repeat_Timedate.setText(beanObj.getEdittime());
					userInputFrequency.setText(beanObj.getFreqMode());
					tv_repeat.setTextColor(Color.parseColor("#13ABE1"));
					tv_runonspecific.setTextColor(Color.parseColor("#000000"));
					tv_runmanually.setTextColor(Color.parseColor("#000000"));

					RepeatSpinner.setEnabled(true);

				}
			}

			ch_runmanually
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								DBTime = "";
								currentTimeDate.setVisibility(View.GONE);
								repeatCurrentby.setVisibility(View.GONE);
								repeatCurrentTimeDate.setVisibility(View.GONE);
								tv_runmanually.setTextColor(Color
										.parseColor("#13ABE1"));
								tv_runonspecific.setTextColor(Color
										.parseColor("#000000"));
								tv_repeat.setTextColor(Color
										.parseColor("#000000"));
								ch_runonspecific.setChecked(false);
								ch_repeat.setChecked(false);
								runoption = "runmanually";

							}
						}
					});

			ch_runonspecific
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {

								currentTimeDate.setVisibility(View.VISIBLE);
								repeatCurrentby.setVisibility(View.GONE);
								repeatCurrentTimeDate.setVisibility(View.GONE);
								tv_runonspecific.setTextColor(Color
										.parseColor("#13ABE1"));
								tv_runmanually.setTextColor(Color
										.parseColor("#000000"));
								tv_repeat.setTextColor(Color
										.parseColor("#000000"));
								ch_runmanually.setChecked(false);
								ch_repeat.setChecked(false);
								RepeatSpinner.setEnabled(true);
								runoption = "runonspecificdate";

							} else {

								currentTimeDate.setVisibility(View.GONE);

							}
						}
					});

			ch_repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {

						currentTimeDate.setVisibility(View.GONE);
						repeatCurrentby.setVisibility(View.VISIBLE);
						repeatCurrentTimeDate.setVisibility(View.VISIBLE);
						Repeat_Timedate.setVisibility(View.VISIBLE);
						tv_repeat.setTextColor(Color.parseColor("#13ABE1"));
						tv_runonspecific.setTextColor(Color
								.parseColor("#000000"));
						tv_runmanually.setTextColor(Color.parseColor("#000000"));
						ch_runmanually.setChecked(false);
						ch_runonspecific.setChecked(false);
						RepeatSpinner.setEnabled(true);
						runoption = "repeat";

					} else {
						repeatCurrentby.setVisibility(View.GONE);

					}
				}
			});

			bt_back.setOnClickListener(new OnClickListener() {

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
					if (userInputFrequency.getText().toString() != ""
							|| userInputFrequency.getText().toString() != null) {
						if (ch_runmanually.isChecked()) {
							Intent i = new Intent();
							Bundle bun = new Bundle();
							bun.putString("schedule", value);
							CallDispatcher.timeMode = "manually";
							i.putExtra("quick", bun);
							setResult(-3, i);
							finish();
						} else if ((ch_runonspecific.isChecked())) {
							value = TimeDate.getText().toString();
							Log.i("Schedule Values", "Value :: " + value);
							if (CompleteListView.CheckReminderIsValid(value)) {
								Intent i = new Intent();
								Bundle bun = new Bundle();
								bun.putString("schedule", value);
								CallDispatcher.timeMode = "specific";
								i.putExtra("quick", bun);
								setResult(-3, i);
								finish();
							} else
								callDisp.showToast(SipNotificationListener
										.getCurrentContext(),
										SingleInstance.mainContext.getResources().getString(R.string.please_select_future_date_and_time));
						} else if ((ch_repeat.isChecked())) {

							value = Repeat_Timedate.getText().toString();
							DBFrequency = userInputFrequency.getText()
									.toString();
							DBFrequencyMode = RepeatSpinner.getSelectedItem()
									.toString();
							int freqTimes = 0;
							if (DBFrequency.length() > 0) {
								freqTimes = Integer.parseInt(DBFrequency);
							}
							if (DBFrequencyMode
									.equalsIgnoreCase("----Select-----")
									|| freqTimes <= 0) {
								Toast.makeText(
										context,
										SingleInstance.mainContext.getResources().getString(R.string.please_select_frequency_mode_or_enter_proper_interval),
										Toast.LENGTH_LONG).show();
							} else {
								if (CompleteListView
										.CheckReminderIsValid(value)) {
									Intent i = new Intent();
									Bundle bun = new Bundle();
									bun.putString("frequency", DBFrequency);
									bun.putString("mode", DBFrequencyMode);
									bun.putString("freq", DBFrequency);
									bun.putString("schedule", value);
									CallDispatcher.timeMode = "Repeat";
									i.putExtra("quick", bun);
									setResult(-3, i);
									finish();
								} else
									callDisp.showToast(SipNotificationListener
											.getCurrentContext(),
											SingleInstance.mainContext.getResources().getString(R.string.please_select_future_date_and_time));
							}
						} else {
							Toast.makeText(context,
									SingleInstance.mainContext.getResources().getString(R.string.please_select_any_one_of_the_run_option),
									Toast.LENGTH_SHORT).show();
						}

					}
				}
			});
			btn_notification = (Button) findViewById(R.id.notification);
			btn_notification.setVisibility(View.GONE);
			btn_notification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});

			IMRequest = (Button) findViewById(R.id.im);

			IMRequest.setVisibility(View.INVISIBLE);
			IMRequest.setBackgroundResource(R.drawable.one);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	protected void onDestroy() {
		try {
			// TODO Auto-generated method stub
			super.onDestroy();
			WebServiceReferences.contextTable
					.remove("QuickActionCallsSchedule");
			finish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Show the IMTabScreen
	 */

	public void showActiveSession(String session, String selectedBuddy) {
	}

}
