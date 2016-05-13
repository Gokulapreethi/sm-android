package com.cg.forms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;



public class DateBetween extends Activity {

	private Button back, submit;
	private EditText textValue1, textValue2;
	private Context context = null;
	private String starttime;
	private String endtime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.number_input);
		back = (Button) findViewById(R.id.back);
		submit = (Button) findViewById(R.id.submit);
		textValue1 = (EditText) findViewById(R.id.edit_num1);
		textValue2 = (EditText) findViewById(R.id.edit_num2);
		textValue1.setFocusableInTouchMode(false);
		textValue2.setFocusableInTouchMode(false);
		textValue1.setHint(SingleInstance.mainContext.getResources().getString(R.string.start_date_and_time));
		textValue2.setHint(SingleInstance.mainContext.getResources().getString(R.string.end_date_and_time));
		context = this;
		if (CallDispatcher.numberMode.equalsIgnoreCase("BW")) {
			textValue2.setVisibility(View.VISIBLE);
		} else {
			textValue2.setVisibility(View.GONE);
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		textValue1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

				btnSet.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						starttime = +dp.getYear()
								+ "-"
								+ WebServiceReferences.setLength2((dp.getMonth() + 1))
								+ "-"
								+ WebServiceReferences.setLength2(dp.getDayOfMonth())
								+ " "
								+ WebServiceReferences.setLength2(tp.getCurrentHour())
								+ ":"
								+ WebServiceReferences.setLength2(tp.getCurrentMinute());

						alertReminder.dismiss();

						textValue1.setText(starttime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();
			}
		});
		textValue2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btnSet;
				final DatePicker dp;
				final TimePicker tp;
				final AlertDialog alertReminder = new AlertDialog.Builder(
						context).create();

				ScrollView tblDTPicker = (ScrollView) View.inflate(context,
						R.layout.date_time_picker, null);

				btnSet = (Button) tblDTPicker.findViewById(R.id.btnSetDateTime);
				dp = (DatePicker) tblDTPicker.findViewById(R.id.datePicker);
				tp = (TimePicker) tblDTPicker.findViewById(R.id.timePicker);

				btnSet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						endtime = +dp.getYear()
								+ "-"
								+ WebServiceReferences.setLength2((dp.getMonth() + 1))
								+ "-"
								+ WebServiceReferences.setLength2(dp.getDayOfMonth())
								+ " "
								+ WebServiceReferences.setLength2(tp.getCurrentHour())
								+ ":"
								+ WebServiceReferences.setLength2(tp.getCurrentMinute());

						alertReminder.dismiss();

						textValue2.setText(endtime);

					}
				});
				alertReminder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.date_and_time));
				alertReminder.setView(tblDTPicker);
				alertReminder.show();
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			String value = "";
			String number = "";
			String number2 = "";

			@Override
			public void onClick(View v) {
				if (textValue1.getText().toString().length() > 0) {
					if (CallDispatcher.numberMode.equalsIgnoreCase("BW")) {
						number = textValue1.getText().toString().trim();
						number2 = textValue2.getText().toString().trim();
						value = "BW " + textValue1.getText().toString().trim()
								+ " and "
								+ textValue2.getText().toString().trim();
					}
					if ((CallDispatcher.numberMode.equalsIgnoreCase("BW"))
							&& (textValue1.getText().toString().length() == 0 || textValue2
									.getText().toString().length() == 0)) {
						Toast.makeText(context, "Please enter values",
								Toast.LENGTH_LONG).show();
					} else if ((!(CallDispatcher.numberMode
							.equalsIgnoreCase("BW")))
							&& (textValue1.getText().toString().trim() == null || textValue1
									.getText().toString().trim() == "")) {
						Toast.makeText(getApplicationContext(),
								"Please enter value", Toast.LENGTH_LONG).show();
					} else {
						CallDispatcher.numberMode = "";
						Intent i = new Intent();
						Bundle bun = new Bundle();
						bun.putString("numeric", value);
						bun.putString("dValue", number);
						bun.putString("dValue2", number2);
						i.putExtra("share", bun);
						setResult(-11, i);
						finish();
					}

				} else {
					Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.please_enter_values),
							Toast.LENGTH_LONG).show();
				}
			}

		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }
}
