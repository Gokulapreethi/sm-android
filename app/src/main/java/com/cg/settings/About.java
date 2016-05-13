package com.cg.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;

public class About extends Activity {
	TextView version;
	private Button btnCancel = null;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.aboutpage);
		context = this;
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		WebServiceReferences.contextTable.put("a_play", this);
		WebServiceReferences.contextTable.put("about", this);
		Log.i("welcome", "coming the about page");
		version = (TextView) findViewById(R.id.textView1);
		version.setText(this.getString(R.string.app_version));
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		WebServiceReferences.contextTable.remove("about");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        AppMainActivity.inActivity = this;
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
