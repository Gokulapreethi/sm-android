package com.cg.account;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.cg.snazmed.R;
import com.main.AppMainActivity;
import com.service.LoginService;
import com.util.SingleInstance;

import java.util.List;

/**
 * Before opening the application tab screen or today list screen show the image
 * about the application in a full screen view and close it and then open the
 * Tab screen or Today list screen
 * 
 * 
 * 
 */
public class SplashScreen extends Activity {

	private Context context = null;

	private final int STOPSPLASH = 0;

	private final long SPLASHTIME = 3000;

	private boolean needStartApp() {
		final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1024);

		if (!tasksInfo.isEmpty()) {
			final String ourAppPackageName = getPackageName();
			RunningTaskInfo taskInfo;
			final int size = tasksInfo.size();
			for (int i = 0; i < size; i++) {
				taskInfo = tasksInfo.get(i);
				if (ourAppPackageName.equals(taskInfo.baseActivity
						.getPackageName())) {
					return taskInfo.numActivities == 1;
				}
			}
		}
		return true;
	}

	private Handler splashHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				Log.i("startapp", "inside stop splash");
				if (!isMyServiceRunning(LoginService.class)) {
					Intent loginintent = new Intent(context,
							AppMainActivity.class);
					loginintent.putExtra("splash", true);
					loginintent.putExtra("silentlogin", false);
					startActivity(loginintent);
					finish();
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Crashlytics.start(this);
		Log.i("startapp", "need to start : " + needStartApp());
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			this.context = this;
			Log.i("startapp", "need to start : " + needStartApp());
			if (!needStartApp()) {
				SplashScreen.this.finish();
				return;
			}
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.splash_screen);
			Message msg = new Message();
			msg.what = STOPSPLASH;
			Log.i("localeservice", "inside stop splash");
			SingleInstance.Localefromdevice=false;
			splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		} catch (Exception ex) {
			ex.printStackTrace();
		} catch (OutOfMemoryError O) {
			O.printStackTrace();
			Intent loginintent = new Intent(context, AppMainActivity.class);
			loginintent.putExtra("splash", true);
			startActivity(loginintent);
			finish();

		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
			System.exit(0);
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// TODO: handle exception

		}
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
