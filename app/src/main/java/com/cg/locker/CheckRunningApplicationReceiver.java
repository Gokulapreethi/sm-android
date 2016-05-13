package com.cg.locker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.main.AppMainActivity;
import com.util.SingleInstance;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

public class CheckRunningApplicationReceiver extends BroadcastReceiver {
	public final String TAG = "CRAR"; // CheckRunningApplicationReceiver
	ActivityManager am;

	@Override
	public void onReceive(Context aContext, Intent intent1) {
		// TODO Auto-generated method stub

		try {

			am = (ActivityManager) aContext
					.getSystemService(Context.ACTIVITY_SERVICE);

			List<ActivityManager.RunningTaskInfo> alltasks = am
					.getRunningTasks(1);

			ActivityManager.RunningTaskInfo ar = alltasks.get(0);

			String[] activePackages;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
				activePackages = getActivePackages();
			} else {
				activePackages = getActivePackagesCompat();
			}

			if (activePackages != null) {
				for (String activePackage : activePackages) {
					if (activePackage.equals("com.google.android.calendar")) {
						// Calendar app is launched, do something
					}
				}
			}
			String mPackageName = "";
			if (Build.VERSION.SDK_INT > 20) {
				mPackageName = am.getRunningAppProcesses().get(0).processName;
			} else {
				mPackageName = am.getRunningTasks(1).get(0).topActivity
						.getPackageName();
			}

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			ResolveInfo defaultLauncher = aContext.getPackageManager()
					.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			String nameOfLauncherPkg = defaultLauncher.activityInfo.packageName;

			Log.i("locker123", "@@@nameOfLauncherPkg --------->"
					+ nameOfLauncherPkg);

			Log.i("locker123", "mPackageName " + mPackageName);
			if (!mPackageName.equals("com.cg.snazmed")
					&& !mPackageName.contains("com.android.launcher")
					&& !mPackageName.contains("com.sec.android.app.launcher")
					&& !mPackageName.equals(nameOfLauncherPkg)
					&& !ar.topActivity.toString().contains(
							"recent.RecentsActivity")) {
				Log.i("locker123", "nameOfLauncherPkg 1 " + mPackageName);
				((AppMainActivity) AppLocker.context).killApp(0);
				if (SingleInstance.contextTable.containsKey("MAIN")) {
					Log.i("locker123", "nameOfLauncherPkg 2" + mPackageName);
					((AppMainActivity) SingleInstance.contextTable.get("MAIN"))
							.killApp(0);

				}

			}

		} catch (Throwable t) {
			Log.i(TAG, "Throwable caught: " + t.getMessage(), t);
			// Intent startHomescreen=new Intent(aContext,Main.class);
			//
			// aContext.startActivity(startHomescreen);
		}

	}

	String[] getActivePackagesCompat() {
		final List<ActivityManager.RunningTaskInfo> taskInfo = am
				.getRunningTasks(1);
		final ComponentName componentName = taskInfo.get(0).topActivity;
		final String[] activePackages = new String[1];
		activePackages[0] = componentName.getPackageName();
		return activePackages;
	}

	String[] getActivePackages() {
		final Set<String> activePackages = new HashSet<String>();
		final List<ActivityManager.RunningAppProcessInfo> processInfos = am
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
			if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				activePackages.addAll(Arrays.asList(processInfo.pkgList));
			}
		}
		return activePackages.toArray(new String[activePackages.size()]);
	}

}
