package com.cg.callservices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.audio.AudioProperties;
import org.lib.model.SignalingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bean.ProfileBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class AudioCallScreen extends Activity {

	private TextView tvBuddies,tv_name,tv_status;

	private TextView tvCallTime;

	private Chronometer chTimer;

	private boolean isTimer_running = false;

	private String strPrevScreen = null;

	private String[] touserarray;

	private String[] autocallarray;

	private String[] starttimearray;

	private String[] endtimearray;

	private boolean isHangedUp = false;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	private Button btnHangup, btnemergency;

	private Button btnMic;

	private Button btnSpeaker;

	private AlertDialog alert = null;

	private Handler handler;

	private SignalingBean signBean;

	public String strSessionId;

	private boolean micmute, speaker = false;

	private String receiver = null;

	private String callerName = null;

	private String callTypeForServer = "101";

	private HashSet<String> hsCallMembers = new HashSet<String>();

	private String strStartTime;

	public String failedUser = null;

	private AudioProperties audioProperties = null;

	private Context context = null;

	private CallDispatcher objCallDispatcher = null;

	private Bundle bun = null;

	private boolean isReceiver = false;

	private boolean selfHangup = false;
	
	private boolean isBuddyinCall=false;

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (CallDispatcher.callQueue != null) {
				while (!CallDispatcher.callQueue.isEmpty()) {
					try {

						String buddy = (String) CallDispatcher.callQueue
								.getMsg();

						Log.d("LM", "inside runnable--->");
						UpdateConferenceMembers(buddy, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (SingleInstance.mainContext
					.getResources()
					.getString(R.string.screenshot)
					.equalsIgnoreCase(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes))) {
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
						WindowManager.LayoutParams.FLAG_SECURE);
			}
			final Window win = getWindow();
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

			bun = getIntent().getBundleExtra("signal");
			receiver = getIntent().getStringExtra("receive");
			callerName = getIntent().getStringExtra("buddy");
			isReceiver = getIntent().getBooleanExtra("isreceiver", false);
			Log.d("Audio", "Is receiver --->" + isReceiver);

			context = this;
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				objCallDispatcher = new CallDispatcher(context);

			objCallDispatcher.setNoScrHeight(noScrHeight);
			objCallDispatcher.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			CallDispatcher.networkState = objCallDispatcher.connectivityType();
			objCallDispatcher.isCalledAudiocallScreen = false;

			WebServiceReferences.contextTable.put("callscreen", this);
			objCallDispatcher.startPlayer(context);
			if (objCallDispatcher.isHangUpReceived)
				receivedHangUp();

			else if (receiver.equalsIgnoreCase("true")
					&& CallDispatcher.conferenceMembers.size() == 0)
				receivedHangUp();

			audioProperties = new AudioProperties(this);
			setContentView(ShowaudioCallScreen());
			strStartTime = objCallDispatcher.getCurrentDateTime();
			signBean = (SignalingBean) bun.getSerializable("signal");
			strSessionId = signBean.getSessionid();

			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					Bundle bun = (Bundle) msg.obj;
					try {

						String action = bun.getString("action");

						if (action.equals("leave")) {
							Log.d("test", "Received Audio hang ");
							objCallDispatcher.accepted_users.clear();
							chTimer.stop();
							//
							isHangedUp = true;

							enterCallHistory();
							// if (CallDispatcher.sb.getBs_parentid() != null) {
							CallDispatcher.sb
									.setEndTime(getCurrentDateandTime());
							CallDispatcher.sb
									.setCallDuration(SingleInstance.mainContext
											.getCallDuration(CallDispatcher.sb
													.getStartTime(),
													CallDispatcher.sb
															.getEndTime()));
							if (selfHangup) {
								DBAccess.getdbHeler()
										.saveOrUpdateRecordtransactiondetails(
												CallDispatcher.sb);
							
								Intent intentComponent = new Intent(context,
										CallHistoryActivity.class);
								intentComponent.putExtra("buddyname",
										CallDispatcher.sb.getFrom());
								intentComponent.putExtra("individual", true);
								intentComponent.putExtra("sessionid",
										CallDispatcher.sb.getSessionid());
								context.startActivity(intentComponent);
							
								
							}
							
							// }
							try {

								if (CallDispatcher.conferenceRequest.size() > 0) {
									try {
										Set<String> set = CallDispatcher.conferenceRequest
												.keySet();

										Iterator<String> iterator = set
												.iterator();
										while (iterator.hasNext()) {

											String buddy = (String) iterator
													.next();
											SignalingBean sb = CallDispatcher.conferenceRequest
													.get(buddy);

											sb.setFrom(CallDispatcher.LoginUser);
											sb.setTo(buddy);
											sb.setType("3");
											sb.setCallType("VC");
											AppMainActivity.commEngine
													.hangupCall(sb);

										}
									} catch (Exception e) {
										// TODO: handle exception
									}
									enterMissedCall();

								}
							} catch (Exception e) {

							}

							CallDispatcher.conferenceRequest.clear();

							Log.d("test", "Received Audio hang "
									+ CallDispatcher.conferenceMembers.size());
							;
							if (CallDispatcher.conferenceMembers.size() > 0) {
								for (int i = 0; i < CallDispatcher.conferenceMembers
										.size(); i++) {
									String buddyName = CallDispatcher.conferenceMembers
											.get(i);
									SignalingBean sb1 = CallDispatcher.buddySignall
											.get(buddyName);

									if (sb1 != null) {
										Log.e("test",
												"Going to Hangup the userssssss:"
														+ sb1.getLocalip());
										Log.e("test",
												"Going to Hangup the userssssss:"
														+ sb1.getPublicip());
										Log.e("test",
												"Going to Hangup the userssssss:"
														+ sb1.getTolocalip());
										Log.e("test",
												"Going to Hangup the userssssss:"
														+ sb1.getTopublicip());
										sb1.setFrom(CallDispatcher.LoginUser);
										sb1.setTo(buddyName);
										sb1.setType("3");
										sb1.setCallType("AC");
										AppMainActivity.commEngine
												.hangupCall(sb1);
										Log.d("test",
												"Received Audio hang send 33333333");

									}

								}

								try {
									CallDispatcher.currentSessionid = null;
									CallDispatcher.conferenceMembers.clear();

								} catch (Exception e) {
									// TODO: handle exception
								}

								finish();
							} else {

								try {
									CallDispatcher.currentSessionid = null;
									CallDispatcher.conferenceMembers.clear();

								} catch (Exception e) {
									// TODO: handle exception
								}

								finish();

							}

						} else if (action.equals("buddy")) {

							Log.d("LM", "----->inside handler");

							String buddy = bun.getString("buddy");
							boolean state = bun.getBoolean("state");

							Log.d("LM", "----->inside handler state " + state);

							if (state) {

								tvBuddies.setText(
										 getNames());

								showToast("Connected member :" + buddy);

							} else {
								// Log.e("test", "false state buddy");
								boolean isConferencemembers = CallDispatcher.conferenceMembers
										.contains(buddy);

								CallDispatcher.buddySignall.remove(buddy);
								CallDispatcher.conferenceMembers.remove(buddy);

								if (CallDispatcher.conferenceMembers.size() == 0
										&& CallDispatcher.conferenceRequest
												.size() == 0) {

									receiveHangUpx();

								} else {
									showToast("Disconnect  member :" + buddy);
									// Used to show disconnected Status and
									// message to server...
									if (!isConferencemembers) {
										// enterMissedCall();
										objCallDispatcher
												.notifyCallHistoryToServer(
														CallDispatcher.LoginUser,
														buddy,
														"111",
														objCallDispatcher.sessId,
														objCallDispatcher
																.getCurrentDateTime(),
														objCallDispatcher
																.getCurrentDateTime());
									} else {

										enterCallHistoryOnSingleuser(buddy);

									}

									tvBuddies.setText( getNames());

								}
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			};

			handler.postDelayed(runnable, 1000);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d("test", "Audio call error " + e);
		}

	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    public String getCurrentDateandTime() {
		try {
			// Calendar c = Calendar.getInstance();
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
			// String strDate = sdf.format(c.getTime());

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss ");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public RelativeLayout ShowaudioCallScreen() {

		final RelativeLayout  llayAudioCall = ( RelativeLayout ) View.inflate(
				this, R.layout.call_connecting, null);
		TextView tv = (TextView) llayAudioCall.findViewById(R.id.status);
		tv.setText(SingleInstance.mainContext.getResources().getString(
				R.string.auconnected));
		tvBuddies = (TextView) llayAudioCall.findViewById(R.id.my_userinfo_tv);
		ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(getNames());
		String Callee=pBean.getFirstname()+" "+pBean.getLastname();
		tvBuddies.setText(Callee);
//		tvBuddies.setText(getNames());
		tvCallTime = (TextView) llayAudioCall.findViewById(R.id.datetime);
		tvCallTime.setVisibility(View.VISIBLE);
		final String strCallDate = objCallDispatcher.getCurrentDateTime();
		tvCallTime.setText(strCallDate);

		btnMic = (Button) llayAudioCall.findViewById(R.id.mic);
	
		
		
		//tvCallTime.setVisibility(View.VISIBLE);
		
		btnMic.setVisibility(View.VISIBLE);
		
		btnMic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (!micmute) {
						micmute = true;
						btnMic.setBackgroundResource(R.drawable.mic_mutex);

					} else if (micmute) {
						micmute = false;
						btnMic.setBackgroundResource(R.drawable.mic);

					}

					AppMainActivity.commEngine.micMute(micmute);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		btnSpeaker = (Button) llayAudioCall.findViewById(R.id.loudspeaker);
		btnSpeaker.setVisibility(View.VISIBLE);
		if (SingleInstance.mainContext.isAutoAcceptEnabled(
				CallDispatcher.LoginUser, callerName)) {
			speaker = true;
			btnSpeaker.setBackgroundResource(R.drawable.loudspeakerr);

		} else {
			speaker = false;
			btnSpeaker.setBackgroundResource(R.drawable.headphonee);
		}
		btnSpeaker.setVisibility(View.VISIBLE);
		audioProperties.setSpeakerphoneOn(speaker);
		btnSpeaker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("thread", "################## speaker" + speaker);
				if (!speaker) {
					speaker = true;
					btnSpeaker.setBackgroundResource(R.drawable.loudspeakerr);
				} else if (speaker) {
					btnSpeaker.setBackgroundResource(R.drawable.headphonee);
					speaker = false;

				}
				audioProperties.setSpeakerphoneOn(speaker);
			}
		});

		chTimer = (Chronometer) llayAudioCall.findViewById(R.id.call_timer);
		chTimer.setVisibility(View.VISIBLE);
		if (isReceiver) {
			chTimer.start();
			isTimer_running = true;
		}
		chTimer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer arg0) {

				CharSequence text = chTimer.getText();
				if (text.length() == 5) {
					chTimer.setText("00:" + text);
				} else if (text.length() == 7) {

					chTimer.setText("0" + text);
				}

			}
		});
		btnHangup = (Button) llayAudioCall.findViewById(R.id.btn_han);
		btnemergency = (Button) llayAudioCall
				.findViewById(R.id.btn_emergencybuddy);
		btnemergency.setBackgroundResource(R.drawable.loc_pin);

		if (!receiver.equalsIgnoreCase("true")) {
			btnemergency.setVisibility(View.GONE);
		}
		btnemergency.setVisibility(View.GONE);
		btnemergency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Log.i("thread", "on click of btn_loc");
					ArrayList<String> close_buddies = objCallDispatcher
							.calculateNearestLocations(callerName.trim());

					if (CallDispatcher.conferenceMembers.size() < 3) {
						if (alert == null) {

							makeEmergencyCall("AC", close_buddies);
						} else if (!alert.isShowing()) {
							makeEmergencyCall("AC", close_buddies);
						}
					} else {

						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(R.string.max_conf_members),
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Log.i("thread",
							"::::::::::::::::::: exception" + e.toString());
				}

			}
		});

		btnHangup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Log.d("WIFI",
				// "size btnHangup  "+CallDispatcher.conferenceRequest.size());

				if (alert == null) {

					showHangUpAlert();

				} else if (!alert.isShowing()) {
					showHangUpAlert();
				}

			}
		});

		Button btn_con = (Button) llayAudioCall
				.findViewById(R.id.btn_connectedbuddies);

		Button btn_onlineb = (Button) llayAudioCall
				.findViewById(R.id.btn_addbuddy1);
		btn_onlineb.setVisibility(View.VISIBLE);
		
		btn_con.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showConnectedBuddies();

			}
		});

		btn_onlineb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowOnlineBuddies("AC");
			}
		});

		return llayAudioCall;
	}

	private void showConnectedBuddies() {

		Log.e("hang", "Conference Memners :" + CallDispatcher.conferenceMembers);

		try {

			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// AudioCallScreen.this);
			// builder.create();
			// builder.setTitle("Connected members");
			// final CharSequence[] choiceList = returnBuddies();
			// builder.setItems(choiceList, new
			// DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.cancel();
			// }
			// });
			//
			// alert = builder.create();
			// alert.show();

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.connected_buddies));
			final String[] choiceList = returnBuddies();
			for (int i = 0; i < choiceList.length; i++)
				// Log.i("buddy", "Name :" + choiceList[i]);

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
			alert = builder.create();
			alert.show();

		} catch (Exception e) {
			Log.e("check", "Exce :" + e);
			e.printStackTrace();
		}

	}

	private String[] returnBuddies() {

		String arr[] = CallDispatcher.conferenceMembers
				.toArray(new String[CallDispatcher.conferenceMembers.size()]);
		return arr;
	}

	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
								return;
							}
						});
		alert = builder.create();
		alert.show();
	}

	private void receivedHangUp() {

		try {
			CallDispatcher.currentSessionid = null;
			CallDispatcher.conferenceMembers.clear();

		} catch (Exception e) {
			// TODO: handle exception
		}
		objCallDispatcher.isHangUpReceived = false;
		finish();

	}

	// make the AlertDialog.Builder as private and make it null before Create
	// it...
	void showHangUpAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.need_call_hangup);

		builder.setMessage(ask)
				.setCancelable(false)
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Message msg = new Message();
								Bundle bun = new Bundle();
								bun.putString("action", "leave");
								msg.obj = bun;
								selfHangup = true;
								if (selfHangup) {
									CallDispatcher.sb
									.setEndTime(getCurrentDateandTime());
							CallDispatcher.sb
									.setCallDuration(SingleInstance.mainContext
											.getCallDuration(CallDispatcher.sb
													.getStartTime(),
													CallDispatcher.sb
															.getEndTime()));
									DBAccess.getdbHeler()
											.saveOrUpdateRecordtransactiondetails(
													CallDispatcher.sb);
								
									Intent intentComponent = new Intent(context,
											CallHistoryActivity.class);
									intentComponent.putExtra("buddyname",
											CallDispatcher.sb.getFrom());
									intentComponent.putExtra("individual", true);
									intentComponent.putExtra("sessionid",
											CallDispatcher.sb.getSessionid());
									context.startActivity(intentComponent);
								
									
								}
								final String[] choiceList = returnBuddies();
								if(choiceList.length!=0){
								isBuddyinCall=true;
								selfHangup=false;
								}
								handler.sendMessage(msg);
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

		//

	}

	public void notifyGSMCallAccepted() {
		Message msg = new Message();
		Bundle bun = new Bundle();
		bun.putString("action", "leave");
		msg.obj = bun;
		handler.sendMessage(msg);
	}

	public void receiveHangUpx() {
		// Log.e("test", "Call Rejected @#@##@#@#      6");
		if (handler != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						chTimer.stop();

						if (!isHangedUp) {


							enterCallHistory();

						}

						CallDispatcher.currentSessionid = null;
						CallDispatcher.conferenceMembers.clear();

					} catch (Exception e) {
						// TODO: handle exception
					}

					finish();
					// Log.e("test", "size Zero");

				}
			});
		}

	}

	private void enterCallHistory() {
		// Log.d("WIFI", "enterCallHistory() ");
		Log.d("Test","Inside audiocallscreen  enterCallHistory()@@@@");

		try {

			if (failedUser != null) {
				callTypeForServer = "121";
			}

			if (receiver.equalsIgnoreCase("true")) {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						callerName, touserarray, callTypeForServer,
						strSessionId, starttimearray, endtimearray,
						autocallarray);

			} else {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						CallDispatcher.LoginUser, touserarray,
						callTypeForServer, strSessionId, starttimearray,
						endtimearray, autocallarray);

			}
		} catch (Exception e) {

		}
		// Log.d("WIFI", "fin ");
		failedUser = null;

	}

	private void enterMissedCall() {

		try {
			Log.d("Test","Inside audiocallscreen actionLeave enterMissedCall()@@@@");


			Set<String> set = CallDispatcher.conferenceRequest.keySet();

			String buddyname = null;
			Iterator<String> iterator = set.iterator();
			// Log.d("RACE", "checkAndInsert " + buddyname);
			while (iterator.hasNext()) {
				// System.out.println("main screen alert");
				buddyname = iterator.next();

				String[] toarray = new String[1];
				toarray[0] = buddyname;
				String[] timearray = new String[1];
				timearray[0] = objCallDispatcher.getCurrentDateTime();
				String[] autocallarray = new String[1];
				autocallarray[0] = "0";

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						CallDispatcher.LoginUser, toarray, "111", strSessionId,
						timearray, timearray, autocallarray);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void resetCallDuration() {
		// Log.d("RCD", "call duraion reseted");
		strStartTime = objCallDispatcher.getCurrentDateTime();

	}

	private void enterCallHistoryOnSingleuser(String buddy) {
		Log.d("Test","Inside audiocallscreen callhistorysingleUser()@@@@");

		hsCallMembers.remove(buddy);
		String[] toarray = new String[1];
		toarray[0] = buddy;
		String[] starttimearray = new String[1];
		String[] endtimearray = new String[1];
		endtimearray[0] = objCallDispatcher.getCurrentDateTime();
		String[] autocallarray = new String[1];
		autocallarray[0] = "0";

		if (objCallDispatcher.alConferenceRequest.contains(buddy)) {
			autocallarray[0] = "1";
			objCallDispatcher.alConferenceRequest.remove(buddy);
		}

		String calltypee = "101";
		if (failedUser != null) {
			if (failedUser.equals(buddy)) {
				calltypee = "121";
			}
		}

		try {

			starttimearray[0] = CallDispatcher.conferenceMembersTime.get(buddy);
			if (starttimearray[0] == null) {
				starttimearray[0] = strStartTime;
			}

			if (receiver.equalsIgnoreCase("true")) {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						callerName, toarray, calltypee, strSessionId,
						starttimearray, endtimearray, autocallarray);

			} else {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						CallDispatcher.LoginUser, toarray, calltypee,
						strSessionId, starttimearray, endtimearray,
						autocallarray);

			}
		} catch (Exception e) {

		}

		failedUser = null;
	}

	//

	private void showToast(String msg) {
		if (msg != null) {
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	public void forceHAngUp() {
		try {

			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						callTypeForServer = "131";
						Message msg = new Message();
						Bundle bun = new Bundle();
						bun.putString("action", "leave");
						msg.obj = bun;
						handler.sendMessage(msg);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
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

									forceHAngUp();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
		alertCall.show();
	}

	public void ShowOnlineBuddies(final String callType) {

		try {
			String[] members = objCallDispatcher.getOnlineBuddys();
			ArrayList<String> membersList = new ArrayList<String>();
			for (int i = 0; i < members.length; i++) {
				if (!CallDispatcher.conferenceMembers.contains(members[i])) {
					membersList.add(members[i]);
				}
			}

			if (membersList.size() > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.create();
				builder.setTitle(SingleInstance.mainContext.getResources()
						.getString(R.string.add_people));
				final String[] choiceList = membersList
						.toArray(new String[membersList.size()]);
				for (int i = 0; i < choiceList.length; i++)
					// Log.i("buddy", "Name :" + choiceList[i]);

					builder.setItems(choiceList,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									if (CallDispatcher.conferenceMembers.size() < 3) {

										if (objCallDispatcher != null) {
											SignalingBean sb = objCallDispatcher.callconfernceUpdate(
													choiceList[which]
															.toString(),
													callType, strSessionId);
											// june04-Implementation
											CallDispatcher.conferenceRequest
													.put(choiceList[which]
															.toString(), sb);
											alert.dismiss();
										}
									} else
										Toast.makeText(
												context,
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.max_conf_members),
												Toast.LENGTH_SHORT).show();
								}
							});
				alert = builder.create();
				alert.show();
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.sorry_no_online_users), 2000).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void UpdateConferenceMembers(String strBuddyName, boolean isadd) {
		try {
			if (strBuddyName != null && handler != null) {
				Message msg = new Message();
				Bundle bun = new Bundle();
				bun.putString("action", "buddy");
				bun.putBoolean("state", isadd);
				bun.putString("buddy", strBuddyName);
				msg.obj = bun;
				handler.sendMessage(msg);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	// Have to remove the references associated with the Activity and also
	// Activity...
	// Also have to remove the Handler references too...
	protected void onDestroy() {
		// HomeTabViewScreen home =null;
		try {
			Log.d("ZZZ", "----->callscreenactivity destroy<-----");
			if (handler != null)
				handler.removeCallbacks(runnable);

			objCallDispatcher.stopRingTone();
			CallDispatcher.currentSessionid = null;
			objCallDispatcher.alConferenceRequest.clear();
			CallDispatcher.conferenceMembersTime.clear();
			CallDispatcher.isCallInitiate = false;
			if (WebServiceReferences.contextTable.containsKey("callscreen")) {
				WebServiceReferences.contextTable.remove("callscreen");
			}

			CallDispatcher.conConference.clear();
			CallDispatcher.issecMadeConference = false;

			if (WebServiceReferences.contextTable.containsKey("callscreen")) {
				WebServiceReferences.contextTable.remove("callscreen");
				Log.e("note", "Call screen instance removed ACS!!");
			}

			objCallDispatcher.startPlayer(context);
			if (WebServiceReferences.contextTable.containsKey("audiocall")) {
				WebServiceReferences.contextTable.remove("audiocall");
			}

			objCallDispatcher.whenCallHangedUp();

			if (WebServiceReferences.contextTable.containsKey("connection")) {
				CallConnectingScreen screen = (CallConnectingScreen) WebServiceReferences.contextTable
						.get("connection");
				screen.finish();
			}


				//

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			objCallDispatcher.isHangUpReceived = false;
			if (audioProperties != null) {
				audioProperties.setSpeakerphoneOn(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();

	}

	private String getNames() {

		String strReturnText = "";
		try {
			String[] strARMembers = (String[]) CallDispatcher.conferenceMembers
					.toArray(new String[CallDispatcher.conferenceMembers.size()]);

			if (strARMembers != null) {
				if (strARMembers.length == 0)
					strReturnText = callerName;
				else {
					for (int i = 0; i < strARMembers.length; i++) {

						hsCallMembers.add(strARMembers[i]);

						if (i == (strARMembers.length - 1)) {
							strReturnText += strARMembers[i];
						} else {
							strReturnText += strARMembers[i] + ",";
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strReturnText;
	}

	private String getConnectedMembersName() {
		String strReturnText = "";
		// acStatus="";

		try {
			String[] strARMembers = (String[]) hsCallMembers
					.toArray(new String[hsCallMembers.size()]);

			touserarray = new String[strARMembers.length];
			autocallarray = new String[strARMembers.length];
			starttimearray = new String[strARMembers.length];
			endtimearray = new String[strARMembers.length];
			endtimearray[0] = objCallDispatcher.getCurrentDateTime();

			for (int i = 0; i < strARMembers.length; i++) {

				touserarray[i] = strARMembers[i];
				starttimearray[i] = CallDispatcher.conferenceMembersTime
						.get(strARMembers[i]);
				if (starttimearray[i] == null) {
					starttimearray[i] = strStartTime;
				}

				if (i == (strARMembers.length - 1)) {
					strReturnText += strARMembers[i];
					if (objCallDispatcher.alConferenceRequest
							.contains(strARMembers[i])) {
						autocallarray[i] = "1";
					} else {
						autocallarray[i] = "0";
					}
				} else {
					strReturnText += strARMembers[i] + ",";
					if (objCallDispatcher.alConferenceRequest
							.contains(strARMembers[i])) {
						autocallarray[i] = "1";
					} else {
						autocallarray[i] = "0";
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strReturnText;
	}

	private String getmembersConnected() {
		String strReturnText = "";
		// acStatus="";
		try {
			try {
				String[] strARMembers = (String[]) hsCallMembers
						.toArray(new String[hsCallMembers.size()]);

				touserarray = new String[strARMembers.length];
				autocallarray = new String[strARMembers.length];
				starttimearray = new String[strARMembers.length];
				endtimearray = new String[strARMembers.length];
				endtimearray[0] = objCallDispatcher.getCurrentDateTime();

				for (int i = 0; i < strARMembers.length; i++) {

					touserarray[i] = strARMembers[i];
					starttimearray[i] = CallDispatcher.conferenceMembersTime
							.get(strARMembers[i]);
					if (starttimearray[i] == null) {
						starttimearray[i] = endtimearray[0];
					}

					if (i == (strARMembers.length - 1)) {
						strReturnText += strARMembers[i];
						if (objCallDispatcher.alConferenceRequest
								.contains(strARMembers[i])) {
							autocallarray[i] = "1";
						} else {
							autocallarray[i] = "0";
						}
					} else {
						strReturnText += strARMembers[i] + ",";
						if (objCallDispatcher.alConferenceRequest
								.contains(strARMembers[i])) {
							autocallarray[i] = "1";
						} else {
							autocallarray[i] = "0";
						}

					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {

		}
		return strReturnText;
	}

	public void showReminderAlert() {

		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.reminder_hangup);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(ask)
				.setCancelable(false)
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								forceHAngUp();
								dialog.cancel();

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
		AlertDialog alert1 = builder.create();
		alert1.show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			if (alert == null) {

				showHangUpAlert();

			} else if (!alert.isShowing()) {
				showHangUpAlert();
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	private void makeEmergencyCall(final String callType, ArrayList<String> list) {

		final String[] choiceList = list.toArray(new String[list.size()]);

		try {
			if (choiceList != null && choiceList.length > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.create();
				builder.setTitle(SingleInstance.mainContext.getResources()
						.getString(R.string.near_loc_buddies));

				Log.d("SIZE", "Size of the OnLine Buddies " + choiceList.length);
				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SignalingBean sb = objCallDispatcher
										.callconfernceUpdate(
												choiceList[which].toString(),
												callType, strSessionId);

								CallDispatcher.conferenceRequest.put(
										choiceList[which].toString(), sb);

								alert.dismiss();

							}
						});
				alert = builder.create();

				alert.show();

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_near_contacts), 2000).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void notifyType2Received() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("Audio", "type 2 received" + chTimer);
				if (chTimer != null) {
					if (!isTimer_running) {
						chTimer.setBase(SystemClock.elapsedRealtime());
						chTimer.start();
						isTimer_running = true;
					}
				}
			}
		});
	}

	/*
	 * @Override protected void onSaveInstanceState(Bundle outState) { // TODO
	 * Auto-generated method stub outState.putBundle("signal", bun);
	 * outState.putString("receive", receiver); outState.putString("buddy",
	 * callerName); super.onSaveInstanceState(outState); }
	 */

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

}
