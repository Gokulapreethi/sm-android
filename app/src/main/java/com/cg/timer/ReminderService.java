package com.cg.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.cg.DB.DBAccess;
import com.cg.commonclass.WebServiceReferences;
import com.cg.files.Components;
import com.util.SingleInstance;

/**
 * ReminderService is run in the main thread of their hosting process. If any
 * notes(Components) reminder time is match with the current time we have to
 * show the reminder notification
 * 
 * 
 */
@SuppressWarnings("unused")
public class ReminderService extends Service {

	NotificationManager mNM;

	private int NOTIFICATION_ID = 4321;
	private String strData;

	public static int count = 0;
	public static Handler reminderHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Called by the system when the service is first created.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Log.i("MS", "Service Created");

		Thread thr = new Thread(null, mTask, "AlarmService_Service");
		thr.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("MS", "Service Destroyed");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.i("MS", "Service Started");
	}

	/**
	 * The function that runs in our worker thread
	 */
	Runnable mTask = new Runnable() {
		public void run() {
			try {
				long endTime = 60 * 1000;
				while (true) {
					synchronized (mBinder) {
						try {
							mBinder.wait(endTime);
							Log.i("test", "checking Date: " + "Test");

							Calendar calendar = new GregorianCalendar();
							int year = calendar.get(Calendar.YEAR);
							int month = calendar.get(Calendar.MONTH) + 1;
							int date = calendar.get(Calendar.DAY_OF_MONTH);
							int minute = calendar.get(Calendar.MINUTE);
							int hour = calendar.get(Calendar.HOUR_OF_DAY);
							Log.i("test", "checking Date: " + "Test" + year + "ss"
									+ month + "ss" + date + "ss" + minute + "ss"
									+ hour + "xx");

							String strDate = year + "-"
									+ WebServiceReferences.setLength2(month) + "-"
									+ WebServiceReferences.setLength2(date) + " "
									+ WebServiceReferences.setLength2(hour) + ":"
									+ WebServiceReferences.setLength2(minute);

							ReminderRetrieve remdata = DBAccess.getdbHeler()
									.getReminderDetail(strDate);

							ArrayList<Integer> alRDetails = remdata
									.getReminderList();

							if (alRDetails.size() != 0) {
								Log.i("REM", "alRDetails size:" + alRDetails.size());
								try {
									Log.i("test", "checking Dateaiarmmset: try");
									for (int i = 0; i < alRDetails.size(); i++) {
										Components component = DBAccess
												.getdbHeler()
												.getComponent(
														"select * from component where componentid="
																+ alRDetails.get(i));
										if (!WebServiceReferences.reminderMap
												.containsKey(Integer
														.toString(alRDetails.get(i)))) {
											WebServiceReferences.reminderMap.put(
													Integer.toString(alRDetails
															.get(i)), component);
											WebServiceReferences.reminderQueue
													.add(component);
										}
									}

									if (!SingleInstance.instanceTable
											.containsKey("callscreen")) {
										if (!WebServiceReferences.isReminderOpened) {
											Components comp = (Components) WebServiceReferences.reminderQueue
													.removeFirst();
											WebServiceReferences.reminderMap
													.remove(Integer.toString(comp
															.getComponentId()));
											Intent i = new Intent(
													ReminderService.this,
													ReminderManager.class);
											i.putExtra("component", comp);
											i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											getApplication().startActivity(i);
										}
									} else {

										Components comp = (Components) WebServiceReferences.reminderQueue
												.removeFirst();
										WebServiceReferences.reminderMap
												.remove(Integer.toString(comp
														.getComponentId()));

										Object objIns = SingleInstance.instanceTable
												.get("callscreen");
										// if (objIns instanceof VideoCallScreen) {
										// VideoCallScreen vcal = (VideoCallScreen)
										// WebServiceReferences.contextTable
										// .get("callscreen");
										// vcal.showReminderAlert();
										// } else if (objIns instanceof
										// AudioCallScreen) {
										// AudioCallScreen vcal = (AudioCallScreen)
										// WebServiceReferences.contextTable
										// .get("callscreen");
										// } else if (objIns instanceof
										// AudioCallScreen) {
										// AudioCallScreen apaging =
										// (AudioCallScreen)
										// WebServiceReferences.contextTable
										// .get("callscreen");
										// apaging.showReminderAlert();
										// } else if (objIns instanceof
										// AudioPagingSRWindow) {
										// AudioPagingSRWindow apaging =
										// (AudioPagingSRWindow)
										// WebServiceReferences.contextTable
										// .get("callscreen");
										// apaging.showReminderAlert();
										// } else if (objIns instanceof
										// VideoPagingSRWindow) {
										// VideoPagingSRWindow apaging =
										// (VideoPagingSRWindow)
										// WebServiceReferences.contextTable
										// .get("callscreen");
										// apaging.showReminderAlert();
										// }

									}
								} catch (Exception e) {
									Log.e("REM", e.getMessage());
								}

								Uri alert = RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
								if (alert != null) {
									MediaPlayer mp = new MediaPlayer();
									mp.reset();
									mp.setDataSource(ReminderService.this, alert);
									mp.prepare();
									mp.start();
								}
							}
							Log.i("test", strData);

						} catch (Exception e) {
							Log.e("test", "Exception:" + e);
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	/**
	 * Show a notification while this service is running.
	 */

	/**
	 * This is the object that receives interactions from clients.
	 */
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

}
