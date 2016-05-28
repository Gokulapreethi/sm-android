package com.cg.callservices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.group.chat.GroupChatActivity;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class CallConnectingScreen extends Activity {

	private String calltype = null;

	private String UserId = null;

	private TextView tilte = null;

	private TextView tv_name = null;

	private TextView tv_status = null;

	private Button btn_hangup = null;

	private SignalingBean bean = null;

	private CallDispatcher callDisp = null;

	private boolean iscoonecting = false;

	private boolean isForceHangUp = false;

	private Context context;

	private FrameLayout frameLayout;

	private ImageView profilePic1,profilePic2,profilePic3;

	private boolean isBConf = false;
	private String callerName;
	private ImageLoader imageLoader;
	private ImageView profilePicture;
	private ArrayList<String> confMembers=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.call_connecting);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		this.setFinishOnTouchOutside(false);
		Bundle bndl = getIntent().getExtras();
		calltype = bndl.getString("type");
		UserId = bndl.getString("name");
		profilePicture = (ImageView) findViewById(R.id.profilePic);
	ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(UserId);
		imageLoader = new ImageLoader(SingleInstance.mainContext);
		if(pBean.getPhoto()!=null){
			String profilePic=pBean.getPhoto();
			Log.i("AAAA", "MYACCOUNT "+profilePic);
			if (profilePic != null && profilePic.length() > 0) {
				if (!profilePic.contains("COMMedia")) {
					profilePic = Environment
							.getExternalStorageDirectory()
							+ "/COMMedia/" + profilePic;
				}
				Log.i("AAAA","MYACCOUNT "+profilePic);
				imageLoader.DisplayImage(profilePic, profilePicture,
						R.drawable.img_user);
			}
		}
	 callerName = pBean.getFirstname() + " " + pBean.getLastname();
		iscoonecting = bndl.getBoolean("status");
		isBConf = bndl.getBoolean("bconf");
		bean = (SignalingBean) bndl.getSerializable("bean");
		tilte = (TextView) findViewById(R.id.callscreen);
		tv_name = (TextView) findViewById(R.id.my_userinfo_tv);
		tv_status = (TextView) findViewById(R.id.status);
		btn_hangup = (Button) findViewById(R.id.btn_han);
		frameLayout=(FrameLayout)findViewById(R.id.frame_lay);
		profilePic1=(ImageView)findViewById(R.id.profilePic1);
		profilePic2=(ImageView)findViewById(R.id.profilePic2);
		profilePic3=(ImageView)findViewById(R.id.profilePic3);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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

		if(isBConf && calltype.equalsIgnoreCase("VC")){
			setTitle();
			frameLayout.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
			int i=0;
			Log.i("onlineuser","confereence  "+confMembers.size());
			for(String user:confMembers){
				Log.i("onlineuser","confereence members "+user);
				ProfileBean bean = DBAccess.getdbHeler().getProfileDetails(user);
				if(bean.getPhoto()!=null){
					Log.i("onlineuser","confereence members profile ");
					String profilePic=bean.getPhoto();
						if (!profilePic.contains("COMMedia")) {
							profilePic = Environment.getExternalStorageDirectory()
									+ "/COMMedia/" + profilePic;
						}
					if(i==0) {
						profilePic3.setVisibility(View.VISIBLE);
						imageLoader.DisplayImage(profilePic, profilePic3,
								R.drawable.img_user);
					}else if(i==1) {
						profilePic2.setVisibility(View.VISIBLE);
						imageLoader.DisplayImage(profilePic, profilePic2,
								R.drawable.img_user);
					}else {
						profilePic1.setVisibility(View.VISIBLE);
						imageLoader.DisplayImage(profilePic, profilePic1,
								R.drawable.img_user);
					}
				}
				i++;
			}
			CallDispatcher.conConference.clear();

		}

		WebServiceReferences.contextTable.put("connection", this);
		btn_hangup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("lddd", "Button Clicked.....######################");

				if (isBConf)
					disconnectConfMembers();

				else
					HangupCall();
			}
		});
		setNameandTitle();
		if(isBConf)
		setTitle();
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

	public void forceHangUp(boolean isForceHangUp) {
		this.isForceHangUp = isForceHangUp;

		HangupCall();
	}

	void disconnectConfMembers() {

		CallDispatcher.issecMadeConference = false;
		try {
			Set<String> set = CallDispatcher.contConferencemembers
					.keySet();

			String Key = null;
			Iterator<String> iterator = set.iterator();

			while (iterator.hasNext()) {

				Key = iterator.next();

				SignalingBean sbx = CallDispatcher.contConferencemembers
						.get(Key);

				callDisp.callHangUp1(sbx);

			}

		} catch (Exception e) {
			Log.d("stattus", "completed  vcvcv " + e);
		} finally {
			finish();
		}

	}

	private void setNameandTitle() {
		if (calltype != null && UserId != null) {
			if (calltype.equals("AC")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.audio_call));
			} else if (calltype.equals("VC")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.video_call));
			} else if (calltype.equals("AP")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.audio_unicast));
			} else if (calltype.equals("VP")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.video_unicast));
			} else if (calltype.equals("ABC")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.audio_broadcast_bc));
			} else if (calltype.equals("VBC")) {
				tilte.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.video_broadcast_bc));
			}

//			tv_name.setText(UserId);
			tv_name.setText(callerName);
			Log.i("AAAA", "CallConnectingScreen 1: " + UserId);

			if (iscoonecting) {
				tv_status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.connecting));
			} else {
				tv_status.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.calling));
			}
		}
	}
	String Temp = null;
	//Ste the caller id from the calldispatcher
	void setTitle(String Title){
		Temp=Title;
	}

	void setTitle() {
		String strReturnText = "";
		Set<String> set = CallDispatcher.contConferencemembers.keySet();

		String Key = null;
		Iterator<String> iterator = set.iterator();
		int i = 0;

		while (iterator.hasNext()) {
			// System.out.println("main screen alert");
			Key = iterator.next();
			if (i == (CallDispatcher.contConferencemembers.size() - 1)) {
				strReturnText += Key;
				confMembers.add(Key);
			} else {
				strReturnText += Key + ",";
				confMembers.add(Key);
			}
			i++;

		}

		tv_name.setText(strReturnText);
//		tv_name.setText(AppMainActivity.connectedbuddies);
		if(Temp!=null){
			tv_name.setText(Temp);
		}
	}

	public void HangupCall() {
		try {
			if (bean.getCallType().equals("AC")) {
				try {
					Log.d("hang", "hangup full Screen");
					CallDispatcher.mute = false;

					if (CallDispatcher.callType != null) {
						CallDispatcher.callType = null;
					}

					CallDispatcher.isCallInProgress = false;
					CallDispatcher.isCallInitiate = false;

					callDisp.callHangupFromScreen(bean);
					CallDispatcher.currentSessionid = null;
					CallDispatcher.isAudioCallWindowOpened = true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finish();
				}

			} else if (bean.getCallType().equals("VC")) {
				try {
					Log.d("hang", "hangup full Screen");

					if (CallDispatcher.callType != null) {
						CallDispatcher.callType = null;
					}
					CallDispatcher.isCallInitiate = false;

					callDisp.callHangupFromScreen(bean);
					Log.e("test", "Comes to message received hangupfullscreen");

					CallDispatcher.buddySignall.clear();
					CallDispatcher.conferenceMembers.clear();

					CallDispatcher.currentSessionid = null;

					Log.d("hang", "Hang up from the call");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finish();
				}

			} else if (bean.getCallType().equals("AP")
					|| bean.getCallType().equalsIgnoreCase("abc")) {
				try {
					CallDispatcher.mute = false;

					if (CallDispatcher.callType != null) {
						CallDispatcher.callType = null;
					}

					CallDispatcher.isCallInProgress = false;

					CallDispatcher.isCallInitiate = false;
					Log.v("call", "on receiving eeeeeeeeee"
							+ CallDispatcher.isCallInitiate);

					callDisp.callHangupFromScreen(bean);

					CallDispatcher.isAudioCallWindowOpened = true;

					CallDispatcher.buddySignall.clear();
					CallDispatcher.pagingMembers.clear();
					CallDispatcher.currentSessionid = null;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			} else if (bean.getCallType().equals("VP")
					|| bean.getCallType().equals("VBC")
					|| bean.getCallType().equals("SS")) {

				try {
					Log.d("hang", "hangup full Screen");
					Log.d("PG", "size " + CallDispatcher.pagingMembers.size());
					callDisp.callHangupFromScreen(bean);
					CallDispatcher.pagingMembers.clear();
					/*
					 * members.clear();
					 */
					Log.e("test", "Comes to message received hangupfullscreen");
					WebServiceReferences.contextTable.remove("VideoCall");
					CallDispatcher.buddySignall.clear();
					CallDispatcher.conferenceMembers.clear();
					CallDispatcher.currentSessionid = null;
					Log.d("hang", "Hang up from the call");
					CallDispatcher.isCallInProgress = false;
					Log.v("call", "on receiving callalert ggggggg "
							+ CallDispatcher.isCallInitiate);
					CallDispatcher.isCallInitiate = false;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finish();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private String getCurrentDateTime() {
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		return sdf.format(curDate);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (WebServiceReferences.contextTable.containsKey("connection")) {
			WebServiceReferences.contextTable.remove("connection");
		}
		if (isForceHangUp) {
			try {
				SingleInstance.mainContext.logout(false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void notifyType2Received() {
		OpenCallscreen(bean);
	}

	public void closeScreen() {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			hangUpAlert();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void hangUpAlert() {
		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.need_call_hangup);
		AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(ask)
				.setCancelable(false)
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								HangupCall();
							}
						})
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		alert = builder.create();
		alert.show();
	}

	public void updateAlert(String Status) {
		tv_status.setText(Status);
	}

	private void OpenCallscreen(final SignalingBean sbean) {
		final String from = sbean.getTo();
		if (SingleInstance.contextTable.containsKey("groupchat")) {
			GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable.get("groupchat");
			groupChatActivity.finish();
		}
		if (sbean.getCallType().equals("AC")) {

			CallDispatcher.isAudioCallWindowOpened = true;

//			Intent i = new Intent(CallConnectingScreen.this,
//					AudioCallScreen.class);
//
//			Bundle bun = new Bundle();
//			bun.putSerializable("signal", sbean);
//			i.putExtra("buddy", from);
//			i.putExtra("receive", "true");
//			i.putExtra("signal", bun);
//			i.putExtra("isreceiver", true);
//			startActivity(i);

			if (WebServiceReferences.contextTable.containsKey("ordermenuactivity")) {
				CallHistoryActivity callHistoryActivity = (CallHistoryActivity) WebServiceReferences.contextTable.get("ordermenuactivity");
				callHistoryActivity.finish();
			}


			finish();
			FragmentManager fm =
					AppReference.mainContext.getSupportFragmentManager();
			Bundle bun = new Bundle();
			bun.putSerializable("signal", sbean);
			bun.putString("buddy", from);
			bun.putString("receive", "true");
//			bun.putExtra("signal", bun);
			bun.putBoolean("isreceiver", true);
			FragmentTransaction ft = fm.beginTransaction();
			AudioCallScreen audioCallScreen = AudioCallScreen
					.getInstance(context);
			audioCallScreen.setArguments(bun);
			ft.replace(R.id.activity_main_content_fragment,
					audioCallScreen);
			ft.commitAllowingStateLoss();

			Log.d("test",
					"open AC ***** " + CallDispatcher.conferenceMembers.size());

		} else if (sbean.getCallType().equals("VBC")) {
			// callDisp.isHangUpReceived = false;
			/**
			 * For ScreenShare Test
			 * 
			 */
			// Log.d("RACE", " Race CAse.. assign on call window of Hometabview"
			// + callDisp.isHangUpReceived);

			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = true;
			Bundle bundle = new Bundle();
			bundle.putString("sessionid", sbean.getSessionid());
			bundle.putString("calltype", sbean.getCallType());
			bundle.putString("receive", "true");
			bundle.putString("buddy", from);

			bundle.putString("mode", "0");// Receiver
			Intent i = new Intent(CallConnectingScreen.this,
					VideoPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);
			/**
			 * 
			 */
			// ScreenSharingFragment screenSharingFragment =
			// ScreenSharingFragment
			// .newInstance(SingleInstance.mainContext);
			// screenSharingFragment.sendersideView();

		} else if (sbean.getCallType().equals("SS")) {
			// callDisp.isHangUpReceived = false;

			// Log.d("RACE", " Race CAse.. assign on call window of Hometabview"
			// + callDisp.isHangUpReceived);

			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = true;
			Bundle bundle = new Bundle();
			bundle.putString("sessionid", sbean.getSessionid());
			bundle.putString("calltype", sbean.getCallType());
			bundle.putString("receive", "true");
			bundle.putString("buddy", from);

			bundle.putString("mode", "0");// Receiver
			Intent i = new Intent(CallConnectingScreen.this,
					VideoPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);
			/**
			 * For ScreenShare Test
			 * 
			 */
			// runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// ScreenSharingFragment screenSharingFragment =
			// ScreenSharingFragment
			// .newInstance(SingleInstance.mainContext);
			// // Bundle bundle = new Bundle();
			// // bundle.putString("buddyname", CallDispatcher.LoginUser);
			// // viewProfileFragment.setArguments(bundle);
			//
			// screenSharingFragment.setInOrOut("in");
			// screenSharingFragment.setCallType(sbean.getCallType());
			// screenSharingFragment.setSessionID(sbean.getSessionid());
			// FragmentManager fragmentManager = SingleInstance.mainContext
			// .getSupportFragmentManager();
			// FragmentTransaction fragmentTransaction = fragmentManager
			// .beginTransaction();
			// fragmentTransaction.replace(
			// R.id.activity_main_content_fragment,
			// screenSharingFragment);
			// fragmentTransaction.commitAllowingStateLoss();
			// finish();
			// }
			// });
			/**
			 * Ends
			 */

		}

		else if (sbean.getCallType().equals("ABC")) {

			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = true;
			Bundle bundle = new Bundle();
			bundle.putString("sessionid", sbean.getSessionid());
			bundle.putString("calltype", sbean.getCallType());
			bundle.putString("buddy", from);
			bundle.putString("mode", "0");// Receiver
			bundle.putString("receive", "true");
			Intent i = new Intent(CallConnectingScreen.this,
					AudioPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);

		}

		else if (sbean.getCallType().equals("AP")) {
			CallDispatcher.isCallInitiate = false;
			callDisp.isHangUpReceived = false;
			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = true;

			Bundle bundle = new Bundle();
			bundle.putString("sessionid", sbean.getSessionid());
			bundle.putString("calltype", sbean.getCallType());
			bundle.putString("buddy", from);
			bundle.putString("mode", "0");// Receiver
			bundle.putString("receive", "true");

			Intent i = new Intent(CallConnectingScreen.this,
					AudioPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);

		} else if (sbean.getCallType().equals("VP")) {

			CallDispatcher.isCallInitiate = false;
			callDisp.isHangUpReceived = false;
			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = true;
			Bundle bundle = new Bundle();
			bundle.putString("sessionid", sbean.getSessionid());
			bundle.putString("calltype", sbean.getCallType());
			bundle.putString("mode", "0");// Receiver
			bundle.putString("receive", "true");
			bundle.putString("buddy", from);
			Intent i = new Intent(CallConnectingScreen.this,
					VideoPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);

		}

		//
		else if (sbean.getCallType().equals("VC")) {

			Log.d("RACE",
					" Race CAse.. assign on call window of Hometabview call videocall screen...."
							+ callDisp.isHangUpReceived);

			try {

				CallDispatcher.isIncomingCall = false;
				CallDispatcher.isIncomingAlert = true;

//				Intent i = new Intent(CallConnectingScreen.this,
//						VideoCallScreen.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("sessionid", sbean.getSessionid());
//				bundle.putString("buddyName", from);
//				bundle.putString("receive", "true");
//				i.putExtras(bundle);
//
//				startActivity(i);
				finish();
				FragmentManager fm =
						AppReference.mainContext.getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("sessionid", sbean.getSessionid());
				bundle.putString("buddyName", from);
				bundle.putString("receive", "true");
				FragmentTransaction ft = fm.beginTransaction();
				VideoCallScreen videoCallScreen = VideoCallScreen
						.getInstance(context);
				videoCallScreen.setArguments(bundle);
				ft.replace(R.id.activity_main_content_fragment,
						videoCallScreen);
				ft.commitAllowingStateLoss();
				Log.d("test", "open VC ***** "
						+ CallDispatcher.conferenceMembers.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			WebServiceReferences.contextTable.remove("connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();

		//
	}

	public void notifyState(String status) {
		if (tv_status != null) {
			String currentStatus = status;
			if (status.equalsIgnoreCase("Ringing")) {
				currentStatus = SingleInstance.mainContext.getResources()
						.getString(R.string.ringing);
			} else if (status.equalsIgnoreCase("Connecting")) {
				currentStatus = SingleInstance.mainContext.getResources()
						.getString(R.string.connecting);
			}
			tv_status.setText(currentStatus);
		}
	}

	public void showWifiStateChangedAlert(String message) {
		Log.d("wifi", "callDialScreen");
		AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
		alertCall
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.hangup),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									HangupCall();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
		alertCall.show();
	}
}
