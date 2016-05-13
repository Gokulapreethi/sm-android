package com.cg.forms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.main.AppMainActivity;
import com.util.SingleInstance;



public class NumberOperation extends Activity {

	private Button back, submit;
	private EditText textValue1, textValue2;
	private Context context = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.number_input);
		back = (Button) findViewById(R.id.back);
		submit = (Button) findViewById(R.id.submit);
		textValue1 = (EditText) findViewById(R.id.edit_num1);
		textValue2 = (EditText) findViewById(R.id.edit_num2);
		context = this;
		if (CallDispatcher.numberMode.equalsIgnoreCase("BT")) {
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

		submit.setOnClickListener(new OnClickListener() {
			String value = "";
			String number = "";
			String number2 = "";

			@Override
			public void onClick(View v) {
				if (textValue1.getText().toString().length() > 0) {
					if (CallDispatcher.numberMode.equalsIgnoreCase("LT")) {
						number = textValue1.getText().toString().trim();
						value = "LT " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode.equalsIgnoreCase("LE")) {
						number = textValue1.getText().toString().trim();
						value = "LE " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode.equalsIgnoreCase("GT")) {
						number = textValue1.getText().toString().trim();
						value = "GT " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode.equalsIgnoreCase("GE")) {
						number = textValue1.getText().toString().trim();
						value = "GE " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode.equalsIgnoreCase("EQ")) {
						number = textValue1.getText().toString().trim();
						value = "EQ " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode
							.equalsIgnoreCase("NEQ")) {
						number = textValue1.getText().toString().trim();
						value = "NEQ " + textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode
							.equalsIgnoreCase("Accuracy")) {
						number = textValue1.getText().toString().trim();
						value = "Accuracy "
								+ textValue1.getText().toString().trim();
					} else if (CallDispatcher.numberMode.equalsIgnoreCase("BT")) {
						number = textValue1.getText().toString().trim();
						number2 = textValue2.getText().toString().trim();
						value = "BT " + textValue1.getText().toString().trim()
								+ " and "
								+ textValue2.getText().toString().trim();
					}
					if ((CallDispatcher.numberMode.equalsIgnoreCase("BT"))
							&& (textValue1.getText().toString().length() == 0 || textValue2
									.getText().toString().length() == 0)) {
						Toast.makeText(context, "Please enter values",
								Toast.LENGTH_LONG).show();
					} else if ((!(CallDispatcher.numberMode
							.equalsIgnoreCase("BT")))
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
					Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.please_enter_values),
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
