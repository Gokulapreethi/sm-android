package com.cg.forms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;



public class HardCodeQuery extends Activity implements OnClickListener {

	private Button add, remove, submit;

	private LinearLayout mainLayout;

	private EditText textValue;

	private CallDispatcher callDisp = null;

	private Button IMRequest = null;

	private Button btn_cancel1 = null;

	private Button btn_cmp = null;

	private Context context = null;


	private boolean isRadio = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		context = this;

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
		isRadio = getIntent().getExtras().getBoolean("isRadio");
		IMRequest = (Button) findViewById(R.id.im);

		TextView title = (TextView) findViewById(R.id.note_date);
		if (isRadio) {
			title.setText(SingleInstance.mainContext.getResources().getString(R.string.radio_button));
		} else {
			title.setText("Hard Coded Query");
		}
		title.setTypeface(Typeface.DEFAULT);

		btn_cancel1 = (Button) findViewById(R.id.settings);

		btn_cancel1.setBackgroundResource(R.drawable.ic_action_back);

		btn_cmp = (Button) findViewById(R.id.btncomp);
		btn_cmp.setBackgroundResource(R.drawable.ic_action_save);

		btn_cmp.setVisibility(View.GONE);

		btn_cmp.setOnClickListener(this);
		btn_cancel1.setOnClickListener(this);

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

		WebServiceReferences.contextTable.put("locbusy", this);
		callDisp.startWebService(
				getResources()
				.getString(
						R.string.service_url),
		"80");
		setContentView(R.layout.hard_code);
		add = (Button) findViewById(R.id.add);
		remove = (Button) findViewById(R.id.remove);
		submit = (Button) findViewById(R.id.submit);
		textValue = (EditText) findViewById(R.id.edit_item_id);
		mainLayout = (LinearLayout) findViewById(R.id.container);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getLayoutInflater().inflate(
						R.layout.hard_code_custom, mainLayout, false);

				mainLayout.addView(view);

				if (mainLayout.getChildCount() > 1) {
					remove.setVisibility(View.VISIBLE);
				}
			}
		});

		remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int count = mainLayout.getChildCount();
				mainLayout.removeViewAt(count - 1);
				if (mainLayout.getChildCount() == 1) {
					remove.setVisibility(View.INVISIBLE);
				}

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mainLayout = (LinearLayout) findViewById(R.id.container);
				ArrayList<String> valueList = new ArrayList<String>();
				String value = "";
				if (mainLayout.getChildCount() > 1) {
					for (int i = 0; i < mainLayout.getChildCount(); i++) {
						RelativeLayout relativeLayout = (RelativeLayout) mainLayout
								.getChildAt(i);
						EditText text = (EditText) relativeLayout
								.findViewById(R.id.edit_item_id);
						String val = text.getText().toString().trim();
						valueList.add(val);
					}
					StringBuffer sBuffer = new StringBuffer();
					for (String finalValue : valueList) {
						sBuffer.append(finalValue);
						sBuffer.append(",");
					}
					value = sBuffer.toString().substring(0,
							sBuffer.toString().length() - 1);
				} else {
					value = textValue.getText().toString().trim();
				}
				Intent i = new Intent();
				Bundle bun = new Bundle();
				if (!isRadio) {
					bun.putString("hard", "H " + value);
				} else {
					bun.putString("hard", "R " + value);
				}
				i.putExtra("share", bun);
				setResult(-2, i);
				finish();
			}
		});

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    @Override
	public void onClick(View v) {
		if (v.getId() == btn_cancel1.getId()) {
			finish();
		}

	}



}
