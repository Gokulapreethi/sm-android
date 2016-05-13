package com.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.cg.commonclass.Logger;
import com.cg.hostedconf.AppReference;
import com.crashlytics.android.Crashlytics;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class LoginService extends Service {

	public boolean isRunning = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isRunning = true;
		Crashlytics.start(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (isRunning) {
			try {
				Log.i("logservice", "inside start command");
				String dir_path = Environment.getExternalStorageDirectory()
						+ "/COMMedia";
				File directory = new File(dir_path);
				if (!directory.exists())
					directory.mkdir();
				if (AppReference.isLogEnabled && AppReference.isWriteInFile) {
					String log_path = dir_path + "/log.txt";
					File logFile = new File(log_path);
					if (!logFile.exists())
						logFile.createNewFile();

					String logfilepath = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/log.txt";
					Logger logger = new Logger("File", logfilepath, true, true,
							true, true, true);
					AppReference.logger = logger;
					Log.i("logservice", "folder created successfully");
				}

				directory = null;
				dir_path = null;
				SingleInstance.getChatProcesser();
				SingleInstance.getChatHistoryWriter();
				Intent i = new Intent(getApplicationContext(),
						AppMainActivity.class);
				i.putExtra("silentlogin", true);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				// LoginPageFragment fragment = LoginPageFragment
				// .newInstance(this);
				// fragment.setSilentLogin(true);
				// String username = intent.getStringExtra("uname");
				// String password = intent.getStringExtra("pwd");
				// fragment.login(username, password);

			} catch (Exception e) {
				SingleInstance.printLog(null, e.getMessage(), null, e);
			}
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
