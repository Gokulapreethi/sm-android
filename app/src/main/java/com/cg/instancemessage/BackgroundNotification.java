package com.cg.instancemessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cg.commonclass.WebServiceReferences;
import com.cg.snazmed.R;

public class BackgroundNotification {

	private NotificationManager n_manager;

	private static final int NOTIFICATION_ID = 1;

	private static final int UD_NOTIFICATION = 2;

	private Context appContext;

	private Context ct;

	public BackgroundNotification(Context ctx) {
		ct = ctx;
		n_manager = (NotificationManager) ct
				.getSystemService(Context.NOTIFICATION_SERVICE);

	}

	public void ShowNotification(String msg, String Title, String type) {

		Notification notification = null;
		if (type.equalsIgnoreCase("im")) {
			notification = new Notification(R.drawable.app_icon,
					"Instant Message On Notes",
					java.lang.System.currentTimeMillis());

		} else {
			notification = new Notification(R.drawable.app_icon,
					"Share on Notes", java.lang.System.currentTimeMillis());
		}
		appContext = ct.getApplicationContext();
		Intent intent = new Intent(ct, ct.getClass());
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		WebServiceReferences.isnotified = true;
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(ct, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(ct, Title, msg, contentIntent);
//		n_manager.notify(NOTIFICATION_ID, notification);

	}


	public void showFtpNotification(boolean isupload, boolean completed,String message) {
		Notification notification = null;
		if (isupload) {
			if (!completed)
				notification = new Notification(R.drawable.uploadicon,
						message,
						java.lang.System.currentTimeMillis());
			else
				notification = new Notification(R.drawable.uploadicon,
						message,
						java.lang.System.currentTimeMillis());
		} else {
			if (!completed)
				notification = new Notification(R.drawable.downloadicon,
						message,
						java.lang.System.currentTimeMillis());
			else
				notification = new Notification(R.drawable.downloadicon,
						message,
						java.lang.System.currentTimeMillis());
		}

//		appContext = ct.getApplicationContext();
		Intent intent = new Intent(ct, ct.getClass());
//		intent.setAction("android.intent.action.MAIN");
//		intent.addCategory("android.intent.category.LAUNCHER");
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
		PendingIntent contentIntent = PendingIntent.getActivity(ct, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
//		if (isupload) {
//			notification.setLatestEventInfo(ct, "Uploading", "", contentIntent);
//		} else {
//			notification.setLatestEventInfo(ct, "Downloading", "",
//					contentIntent);
//		}
//		n_manager.notify(UD_NOTIFICATION, notification);
	}

	public void removeNoTification() {
		n_manager.cancel(NOTIFICATION_ID);

	}

	public void removeUDNotification() {
		n_manager.cancel(UD_NOTIFICATION);

	}

}
