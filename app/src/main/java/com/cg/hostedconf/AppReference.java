package com.cg.hostedconf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.cg.commonclass.Logger;
import com.cg.commonclass.SipNotificationListener;
import com.image.utils.ImageUtils;
import com.main.AppMainActivity;
import com.thread.SipCommunicator;
import com.thread.SipQueue;

import org.lib.model.ShareReminder;
import org.lib.webservice.WSRunner;


public class AppReference {

	public static int sip_accountID = -1;

	// public static SipCommunicator sipCommunicator = null;

	// public static SipQueue sipQueue = null;

	public static boolean isreject = false;

	public static boolean twoWayCall = false;

	public static int acc_id;
	
	public static AppMainActivity mainContext;

	public static HashMap<String, Context> context_map = new HashMap<String, Context>();

	public static String sip_registeredid = null;

	public static ArrayList<CallerBean> process_members = new ArrayList<CallerBean>();

	public static SipNotificationListener sipnotifier = null;

	public static boolean issipchatinitiated = false;

	public static boolean addWithCall = false;

	public static int chatcall_id = -1;

	public static boolean isRecordingStarted = false;

	public static Context sipcahtContext = null;

	public static String chat_username;

	public static int twoway_callid = -1;

	public static boolean isRegistered = false;

	public static boolean issrtpenabled = false;

	public static String call_mode = "";

	public static Logger logger = null;

	public static boolean isLogEnabled = true;

	public static boolean isWriteInFile = true;

	public static boolean isFormloaded = false;
	
	public static boolean dbInsideApp = false;
	
	public static SipQueue sipQueue = null;
	
	public static volatile SipCommunicator sip_communicator = null;
	
	public static Boolean isHost = false;

	public static Logger getLogger() {
		return logger;
	}

	public static HashMap<String,ShareReminder> filedownload=new HashMap<String,ShareReminder>();

	public static HashMap<String,Object> getValuesinChat=new HashMap<>();



	@SuppressLint("ShowToast")
	public static void toastdisp(Context context, String name) {
		Toast.makeText(
				context,
				"Missed Call from "
						+ name.replace(name.substring(name.length() - 1), ""),
				Toast.LENGTH_LONG).show();

	}
	private static int isApplication = 1;

	public static Fragment bacgroundFragment=null;

	public static boolean fileOpen=false;

	public static class imageOrientation extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try {
				String changeorientation=urls[0];
				Log.i("profiledownload", "changeorientation--->"+changeorientation);
				Log.i("profiledownload", "my profile download");
				Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(urls[1], 320, 240);
				int orientation = ImageUtils.resolveBitmapOrientation(urls[1]);
				Log.i("profiledownload", "orientation--->"+orientation);
				bitmap = ImageUtils.applyOrientation(bitmap, Integer.parseInt(changeorientation));
				File file = new File(urls[1]);
				if (file.exists())
					file.delete();
				FileOutputStream fOut = null;
				try {
					Log.d("size", "........6------------->");
					fOut = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					fOut.flush();
					fOut.close();
					Log.d("size", "........ after file write is------------->");
				} catch (Exception e) {
					Log.d("size", "........7------------->");
					e.printStackTrace();
				}
				bitmap=null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}
	}
}
