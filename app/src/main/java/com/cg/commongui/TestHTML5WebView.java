package com.cg.commongui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.main.AppMainActivity;

public class TestHTML5WebView extends Activity {

	private HTML5WebView mWebView;

	private String url = null;

	private Button btnIMRequest = null;

	private TextView tvTitle = null;

	private Button btnBack = null;

	private Button btnDone = null;

	private Button btnEdit = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppMainActivity.inActivity = this;

		int ver = Build.VERSION.SDK_INT;
		if (ver >= 12) {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title2);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title2);

		btnIMRequest = (Button) findViewById(R.id.btn_im);
		btnDone = (Button) findViewById(R.id.btn_save_note);
		btnDone.setVisibility(View.INVISIBLE);

		// btnDone.setText("Done");
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setBackgroundResource(R.drawable.ic_action_back);
		btnEdit = (Button) findViewById(R.id.editnote);
		btnEdit.setVisibility(View.GONE);
		tvTitle = (TextView) findViewById(R.id.tv_note_title);
		tvTitle.setText("WebSite");
		tvTitle.setTextColor(Color.WHITE);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tvTitle = (TextView) findViewById(R.id.tv_note_title);
		tvTitle.setTextColor(Color.BLACK);
		tvTitle.setSingleLine();

		btnIMRequest.setVisibility(View.INVISIBLE);
		btnIMRequest.setBackgroundResource(R.drawable.one);
		btnIMRequest.setWidth(70);

		url = (String) getIntent().getStringExtra("url");
		Log.d("SIGNAL", "On screen " + url);
		if (!url.contains("http")) {
			url = "http://" + url;
		}
		mWebView = new HTML5WebView(this);

		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
		} else {

			mWebView.loadUrl(url);

		}

		setContentView(mWebView.getLayout());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Menu m_menu = menu;

		if (CallDispatcher.LoginUser != null) {
			m_menu.add(Menu.NONE, 1, 0, "Close");
		}

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			finish();
			break;
		}
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// mWebView.saveState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		mWebView.stopLoading();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("NAME", "close");

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.inCustomView()) {
				mWebView.hideCustomView();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}