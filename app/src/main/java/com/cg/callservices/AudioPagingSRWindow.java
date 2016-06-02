package com.cg.callservices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.audio.AudioProperties;
import org.lib.model.BuddyInformationBean;
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
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class AudioPagingSRWindow extends Activity {

	private String receiver = null;

	private TextView tvCallTime;

	private Chronometer chTimer;

	private Button btnHangup;

	private Button btnAdd;

	private Button btnMic;

	private Button btnSpeaker;

	private String[] touserarray;

	private String[] autocallarray;

	private String[] starttimearray;

	private String[] endtimearray;

	private AlertDialog alert = null;

	private boolean isDiscarded = false;

	private Handler audioHanler = new Handler();

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	public String strSessionId;

	private boolean micmute, speaker;

	private String mode;

	private String sessionid;

	private String callType;

	private String callerName;

	private HashSet<String> hsCallMembers = new HashSet<String>();

	private String strStartTime;

	private AudioProperties audioProperties = null;

	private CallDispatcher objCallDispatcher = null;

	private String strPrevScreen = null;

	private Button btn_connectedbuddies1 = null;

	private boolean isTimer_running = false;

	private Context context;

	private boolean selfHangup = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

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

			final Window win = getWindow();
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

			strStartTime = objCallDispatcher.getCurrentDateTime();
			audioProperties = new AudioProperties(this);

			if (savedInstanceState == null) {
				mode = (String) getIntent().getStringExtra("mode");
				sessionid = (String) getIntent().getStringExtra("sessionid");
				callType = (String) getIntent().getStringExtra("calltype");
				receiver = (String) getIntent().getStringExtra("receive");
				callerName = (String) getIntent().getStringExtra("buddy");
			} else {
				mode = (String) savedInstanceState.getString("mode");
				sessionid = (String) savedInstanceState.getString("sessionid");
				callType = (String) savedInstanceState.getString("calltype");
				receiver = (String) savedInstanceState.getString("receive");
				callerName = (String) savedInstanceState.getString("buddy");
			}
			CallDispatcher.currentSessionid = sessionid;
			Log.i("call", "Assigned session" + sessionid);

			SingleInstance.instanceTable.put("callscreen", this);

			objCallDispatcher.startPlayer(context);
			Log.d("test", "before paging init");

			setContentView(ShowAudioPagingWindow());

			initFields();

//			WebServiceReferences.contextTable.put("callscreen", this);
			try {

				if (!audioProperties.isHeadsetOn()) {
					audioProperties.setSpeakerphoneOn(true);
				} else {
					audioProperties.setSpeakerphoneOn(false);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			while (!CallDispatcher.callQueue.isEmpty()) {
				try {
					String buddy = (String) CallDispatcher.callQueue.getMsg();
					UpdateConferenceMembers(buddy, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//
			Log.d("RACE", " Race CAse.. on create ,.,....");
			if (objCallDispatcher.isHangUpReceived) {
				Log.d("RACE",
						" Race CAse.. Condition before new concept ,.,....");
				receivedHangUp();
			} else if (receiver.equalsIgnoreCase("true")
					&& CallDispatcher.conferenceMembers.size() == 0) {
				Log.d("RACE", " Race CAse.. new concept Execcuted,.,....");
				receivedHangUp();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("test", "The paging resulted exception " + e.toString());
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void enterCallHistory() {

		try {
			

			if (mode.equals("0")) {
				String membConnected = getmembersConnected();
				if (callType.equals("AP")) {
					String callTypeToServer = "102";
					if (isDiscarded) {
						callTypeToServer = "132";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, touserarray, callTypeToServer,
							sessionid, starttimearray, endtimearray,
							autocallarray);
				} else if (callType.equals("ABC")) {
					String callTypeToServer = "103";
					if (isDiscarded) {
						callTypeToServer = "133";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, touserarray, callTypeToServer,
							sessionid, starttimearray, endtimearray,
							autocallarray);
				}

			} else {
				if (callType.equals("AP")) {
					String callTypeToServer = "102";
					if (isDiscarded) {
						callTypeToServer = "132";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							CallDispatcher.LoginUser, touserarray,
							callTypeToServer, sessionid, starttimearray,
							endtimearray, autocallarray);
				} else if (callType.equals("ABC")) {
					String callTypeToServer = "103";
					if (isDiscarded) {
						callTypeToServer = "133";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							CallDispatcher.LoginUser, touserarray,
							callTypeToServer, sessionid, starttimearray,
							endtimearray, autocallarray);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void enterMissedCall() {
		try {

			Set<String> set = CallDispatcher.conferenceRequest.keySet();

			String buddyname = null;
			Iterator<String> iterator = set.iterator();

			while (iterator.hasNext()) {

				buddyname = iterator.next();

				String[] toarray = new String[1];
				toarray[0] = buddyname;
				String[] timearray = new String[1];
				timearray[0] = objCallDispatcher.getCurrentDateTime();
				String[] autocallarray = new String[1];
				autocallarray[0] = "0";

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						CallDispatcher.LoginUser, toarray, "113", sessionid,
						timearray, timearray, autocallarray);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	//
	private void enterCallHistoryOnSingleuser(String buddy) {
		hsCallMembers.remove(buddy);

		String[] toarray = new String[1];
		toarray[0] = buddy;
		String[] starttimearray = new String[1];
		String[] endtimearray = new String[1];
		endtimearray[0] = objCallDispatcher.getCurrentDateTime();
		String[] autocallarray = new String[1];
		autocallarray[0] = "0";

		try {

			starttimearray[0] = CallDispatcher.conferenceMembersTime.get(buddy);
			if (starttimearray[0] == null) {
				starttimearray[0] = strStartTime;
			}

			// mode 0 Receiver...
			if (mode.equals("0")) {
				//

				if (callType.equals("ABC")) {

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, toarray, "103", sessionid,
							starttimearray, endtimearray, autocallarray);
				}
			} else {

				if (callType.equals("ABC")) {

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							CallDispatcher.LoginUser, toarray, "103",
							sessionid, starttimearray, endtimearray,
							autocallarray);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void resetCallDuration() {
		// Log.d("RCD", "call duraion reseted");
		strStartTime = objCallDispatcher.getCurrentDateTime();

	}

	//

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

			CallDispatcher.conferenceMembers.clear();
			objCallDispatcher.isHangUpReceived = false;
			if (selfHangup) {
				CallDispatcher.sb.setCallstatus("callattended");
				DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
						CallDispatcher.sb);
				Intent intentComponent = new Intent(context,
						CallHistoryActivity.class);
				intentComponent.putExtra("buddyname",
						CallDispatcher.sb.getFrom());
				intentComponent.putExtra("individual", true);
				intentComponent.putExtra("sessionid",
						CallDispatcher.sb.getSignalid());
				context.startActivity(intentComponent);
			}
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	// Have to Remove the References Associated with the Activity...
	protected void onDestroy() {

		try {
			CallDispatcher.isCallInitiate = false;
			objCallDispatcher.stopRingTone();

			objCallDispatcher.startPlayer(context);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			CallDispatcher.currentSessionid = null;
			objCallDispatcher.isHangUpReceived = false;
			CallDispatcher.conferenceMembers.clear();
			CallDispatcher.conferenceMembersTime.clear();
			if (SingleInstance.instanceTable.containsKey("callscreen")) {
				SingleInstance.instanceTable.remove("callscreen");

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (audioProperties != null) {
				audioProperties.setSpeakerphoneOn(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}

	public void forceHAngUp() {
		audioHanler.post(new Runnable() {

			@Override
			public void run() {
				try {
					isDiscarded = true;
					// TODO Auto-generated method stub
					Message msg = new Message();
					Bundle bun = new Bundle();
					bun.putString("action", "leave");
					msg.obj = bun;
					audioHanler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	public void UpdateConferenceMembers(String strBuddyName, boolean isadd) {
		try {
			Message msg = new Message();
			Bundle bun = new Bundle();
			bun.putString("action", "buddy");
			bun.putBoolean("state", isadd);
			bun.putString("buddy", strBuddyName);
			msg.obj = bun;
			// Log.d("test", "state " + isadd + "  buddy " + strBuddyName);
			audioHanler.sendMessage(msg);

		} catch (Exception e) {
			// TODO: handle exception
		}
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
									Log.i("call", "add member session"
											+ sessionid);
									BuddyInformationBean bib = null;

									for (BuddyInformationBean temp : ContactsFragment
											.getBuddyList()) {
										if (!temp.isTitle()) {
											if (temp.getName()
													.equalsIgnoreCase(
															choiceList[which]
																	.toString())) {
												bib = temp;
												break;
											}
										}
									}
									// bib = WebServiceReferences.buddyList
									// .get(choiceList[which].toString());

									if (bib != null) {
										SignalingBean sb = new SignalingBean();
										sb.setFrom(CallDispatcher.LoginUser);
										sb.setSessionid(sessionid);
										sb.setTo(choiceList[which].toString());
										sb.setType("0");
										sb.setToSignalPort(bib
												.getSignalingPort());
										sb.setResult("0");
										sb.setTopublicip(bib
												.getExternalipaddress());
										sb.setTolocalip(bib.getLocalipaddress());
										sb.setToSignalPort(bib
												.getSignalingPort());

										sb.setCallType(callType);

										AppMainActivity.commEngine.makeCall(sb);
										CallDispatcher.conferenceRequest.put(
												choiceList[which].toString(),
												sb);
									}

									alert.dismiss();

								}
							});
				alert = builder.create();

				alert.show();
			} else {
				Toast.makeText(
						this,
						SingleInstance.mainContext.getResources().getString(
								R.string.sorry_no_online_users), 2000).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Added newly...

	public RelativeLayout ShowAudioPagingWindow() {
		try {
			// CallDispatcher.isCallInProgress = true;
			CallDispatcher.isAudioCallWindowOpened = true;

			final RelativeLayout llayAudioCall = (RelativeLayout) View.inflate(
					this, R.layout.call_connecting, null);

			TextView tv = (TextView) llayAudioCall.findViewById(R.id.status);
			if (callType.equals("AP")) {
				if (!mode.equals("0")) {
					tv.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.out_audio_uc));
				} else if (mode.equals("0")) {
					tv.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.in_audio_uc));
				}
			} else {
				if (!mode.equals("0")) {
					tv.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.out_audio_bc));
				} else if (mode.equals("0")) {
					tv.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.in_audio_bc));
				}
			}

			Button emergencyButton = (Button) llayAudioCall
					.findViewById(R.id.btn_emergencybuddy);
			emergencyButton.setVisibility(View.GONE);
			final TextView tv_BuddyName = (TextView) llayAudioCall
					.findViewById(R.id.my_userinfo_tv);
			ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(callerName);
            String UserinCall=pBean.getFirstname()+" "+pBean.getLastname();
			tv_BuddyName.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.connected_buddies) + " " + UserinCall);

			tvCallTime = (TextView) llayAudioCall.findViewById(R.id.datetime);
            tvCallTime.setVisibility(View.VISIBLE);
			final String strCallDate = objCallDispatcher.getCurrentDateTime();
			tvCallTime.setText(strCallDate);

			btnSpeaker = (Button) llayAudioCall.findViewById(R.id.loudspeaker);
            btnSpeaker.setVisibility(View.VISIBLE);
			btnSpeaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!speaker) {
						speaker = true;
						// loudSpeaker.setHint("off");
						btnSpeaker
								.setBackgroundResource(R.drawable.loudspeakerr);
					} else if (speaker) {
						btnSpeaker.setBackgroundResource(R.drawable.headphonee);
						speaker = false;
						// loudSpeaker.setHint("on");
					}

					audioProperties.setSpeakerphoneOn(speaker);

					Log.d("SPE",
							"Manager state "
									+ audioProperties.isSpeakerphoneOn());

				}
			});

			chTimer = (Chronometer) llayAudioCall.findViewById(R.id.call_timer);
            chTimer.setVisibility(View.VISIBLE);
			if (receiver != null) {
				if (receiver.equalsIgnoreCase("true")) {
					chTimer.start();
					isTimer_running = true;
				}
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
			btnHangup.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (alert == null) {

						showHangUpAlert();

					} else if (!alert.isShowing()) {
						showHangUpAlert();
					}

				}
			});

			//
			btnMic = (Button) llayAudioCall.findViewById(R.id.mic);
            btnMic.setVisibility(View.VISIBLE);
			btnMic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!micmute) {
						Log.d("mic", "mute");
						micmute = true;

						btnMic.setBackgroundResource(R.drawable.mic_mutex);
					} else if (micmute) {
						Log.d("mic", "unmute");
						micmute = false;

						btnMic.setBackgroundResource(R.drawable.mic);
					}
					Log.d("mic", "click");
					AppMainActivity.commEngine.micMute(micmute);
				}
			});
			btn_connectedbuddies1 = (Button) llayAudioCall
					.findViewById(R.id.btn_connectedbuddies);

			btn_connectedbuddies1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showConnectedBuddies();
				}
			});

			btnAdd = (Button) llayAudioCall.findViewById(R.id.btn_addbuddy1);
            btnAdd.setVisibility(View.VISIBLE);
			btnAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("Add", "Add Buddies will show...");
					if (alert == null) {
						Log.d("Add", "call show buddies on null");
						ShowOnlineBuddies("ABC");
					} else if (!alert.isShowing()) {
						Log.d("Add", "call show buddies on not showing alert");
						ShowOnlineBuddies("ABC");
					} else {
						Log.d("Add",
								"call show buddies on not showing because alert is already showing");
					}
				}
			});

			return llayAudioCall;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void showConnectedBuddies() {

		Log.e("hang", "Conference Memners :" + CallDispatcher.conferenceMembers);

		try {

			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.create();
			// builder.setTitle("Connected members");
			//
			// final CharSequence[] choiceList = returnBuddies();
			// // System.out.println(choiceList);
			//
			// int selected = -1; // does not select anything
			//
			// builder.setSingleChoiceItems(choiceList, selected,
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			//
			// }
			// });
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

		Log.i("thread", "##################### paging size"
				+ CallDispatcher.pagingMembers.size());
		String arr[] = CallDispatcher.conferenceMembers
				.toArray(new String[CallDispatcher.conferenceMembers.size()]);
		return arr;
	}

	//

	private void initFields() {
		try {

			if (mode.equals("0")) {// Receiver

				if (callType.equalsIgnoreCase("AP")) {

				} else if (callType.equalsIgnoreCase("ABC")) {

				}

				btnAdd.setVisibility(View.GONE);
				btnMic.setVisibility(View.GONE);

				audioHanler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						Bundle bun = (Bundle) msg.obj;

						String action = bun.getString("action");

						if (action.equals("leave")) {

							try {
								objCallDispatcher.accepted_users.clear();
								Log.e("test",
										"leaveeeeeeee "
												+ CallDispatcher.conferenceMembers
														.size());

								String buddyName = CallDispatcher.conferenceMembers
										.get(0);
								Log.e("test",
										"Going to Hangup ^^^^^^^^^^^^^^^^^:"
												+ buddyName);
								SignalingBean sb1 = CallDispatcher.buddySignall
										.get(buddyName);

								if (sb1 != null) {

									/* Static Clean up */
									sb1.setFrom(CallDispatcher.LoginUser);
									sb1.setTo(buddyName);
									sb1.setType("3");
									if (callType.equalsIgnoreCase("ABC")) {
										sb1.setCallType("ABC");
									} else {
										sb1.setCallType("AP");
									}

									AppMainActivity.commEngine.hangupCall(sb1);
								} else {
									// Log.e("hang", "SignalingBean is Null");
									Log.e("test",
											"Going to Hangup ^^^^^^^^^^^ NULLLLLLL");
								}

								CallDispatcher.conferenceMembers.clear();
								// CallDispatcher.buddySignall.clear();

							} catch (Exception e) {
								// TODO: handle exception
							}
							enterCallHistory();
							
							CallDispatcher.sb
							.setEndTime(getCurrentDateandTime());
					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(CallDispatcher.sb
											.getStartTime(),
											CallDispatcher.sb
													.getEndTime()));

					
							finish();

						} else if (action.equals("buddy")) {

							receivedHangUpx();
						}

					}
				};
				btnHangup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {

							if (alert == null) {

								showHangUpAlert();

							} else if (!alert.isShowing()) {
								showHangUpAlert();
							}

						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});
				//
				if (!audioProperties.isHeadsetOn()) {
					audioProperties.setSpeakerphoneOn(true);
				}

			} else if (mode.equals("1")) {// sender
				if (callType.equals("AP")) {
					btnAdd.setVisibility(View.GONE);
					btn_connectedbuddies1.setVisibility(View.GONE);
				}
				btnSpeaker.setVisibility(View.GONE);

				btnHangup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (alert == null) {

							showHangUpAlert();

						} else if (!alert.isShowing()) {
							showHangUpAlert();
						}

					}
				});

				audioHanler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						Bundle bun = (Bundle) msg.obj;

						String action = bun.getString("action");

						if (action.equals("leave")) {

							Log.d("test", "Received paging Hangup "
									+ CallDispatcher.conferenceMembers.size());

							chTimer.stop();

							if (CallDispatcher.conferenceRequest.size() > 0) {
								try {
									Set<String> set = CallDispatcher.conferenceRequest
											.keySet();

									Iterator<String> iterator = set.iterator();
									while (iterator.hasNext()) {

										String buddy = (String) iterator.next();
										SignalingBean sb = CallDispatcher.conferenceRequest
												.get(buddy);

										sb.setFrom(CallDispatcher.LoginUser);
										sb.setTo(buddy);
										sb.setType("3");
										sb.setCallType("ABC");
										AppMainActivity.commEngine
												.hangupCall(sb);

									}
									enterMissedCall();

								} catch (Exception e) {
									// TODO: handle exception
								}
							}

							//
							if (CallDispatcher.conferenceMembers.size() > 0) {
								for (int i = 0; i < CallDispatcher.conferenceMembers
										.size(); i++) {

									String buddyName = CallDispatcher.conferenceMembers
											.get(i);

									SignalingBean sb1 = CallDispatcher.buddySignall
											.get(buddyName);

									if (sb1 != null) {

										sb1.setFrom(CallDispatcher.LoginUser);
										sb1.setTo(buddyName);
										sb1.setType("3");

										if (callType.equalsIgnoreCase("ABC")) {
											sb1.setCallType("ABC");
										} else {
											sb1.setCallType("AP");
										}
										AppMainActivity.commEngine
												.hangupCall(sb1);
									} else {
										Log.e("hang", "SignalingBean is Null");
									}

								}

								CallDispatcher.conferenceMembers.clear();

								enterCallHistory();
								
								finish();
							}

						} else if (action.equals("buddy")) {
							String buddy = bun.getString("buddy");
							boolean state = bun.getBoolean("state");
							if (!state) {

								boolean isConferencemembers = CallDispatcher.conferenceMembers
										.contains(buddy);

								CallDispatcher.buddySignall.remove(buddy);
								CallDispatcher.conferenceMembers.remove(buddy);
								if (CallDispatcher.conferenceMembers.size() == 0
										&& CallDispatcher.conferenceRequest
												.size() == 0) {
									receivedHangUpx();

								} else {

									if (!isConferencemembers) {
										objCallDispatcher
												.notifyCallHistoryToServer(
														CallDispatcher.LoginUser,
														buddy,
														"113",
														objCallDispatcher.sessId,
														objCallDispatcher
																.getCurrentDateTime(),
														objCallDispatcher
																.getCurrentDateTime());
										CallDispatcher.sb
										.setEndTime(getCurrentDateandTime());
								CallDispatcher.sb
										.setCallDuration(SingleInstance.mainContext
												.getCallDuration(CallDispatcher.sb
														.getStartTime(),
														CallDispatcher.sb
																.getEndTime()));
									} else {
										enterCallHistoryOnSingleuser(buddy);
									}

								}
							}

						}

					}
				};

			}
		} catch (Exception e) {

			Log.d("test", "EXCE " + e.toString());
			e.printStackTrace();
		}

	}

	public void receivedHangUpx() {
		audioHanler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//
				if (mode.equals("0")) {
					try {

						chTimer.stop();

						enterCallHistory();

						CallDispatcher.buddySignall.clear();
						CallDispatcher.conferenceMembers.clear();

						finish();
						Log.e("test",
								"Hang up received and close the Audio broad cast receiver screen");

						//
					} catch (Exception e) {
						// TODO: handle exception
					}
					// TODO: handle exception
				}

				else if (mode.equals("1")) {
					try {

						chTimer.stop();

						enterCallHistory();

						finish();

					} catch (Exception e) {
						// TODO: handle exception
					}
					//

				}

			}
		});

	}

	public void notifyHeadsetPlugin(final boolean is) {
		try {
			audioHanler.post(new Runnable() {

				@Override
				public void run() {
					if (mode.equals("0")) {
						//
						try {
							if (is) {

								audioProperties.setSpeakerphoneOn(false);
							} else {

								audioProperties.setSpeakerphoneOn(true);

							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void closeCall() {

		CallDispatcher.conferenceMembers.clear();

		finish();
	}

	void showHangUpAlert() {

		//
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

								try {
									selfHangup = true;
									CallDispatcher.sb
											.setEndTime(getCurrentDateandTime());
									CallDispatcher.sb
											.setCallDuration(SingleInstance.mainContext.getCallDuration(
													CallDispatcher.sb
															.getStartTime(),
													CallDispatcher.sb
															.getEndTime()));
									if (selfHangup) {


										CallDispatcher.sb.setCallstatus("callattended");
										DBAccess.getdbHeler()
												.saveOrUpdateRecordtransactiondetails(
														(SignalingBean)CallDispatcher.sb.clone());
										Intent intentComponent = new Intent(
												context,
												CallHistoryActivity.class);
										intentComponent.putExtra("buddyname",
												CallDispatcher.sb.getFrom());
										intentComponent.putExtra("individual",
												true);
										intentComponent
												.putExtra("sessionid",
														CallDispatcher.sb
																.getSignalid());
										context.startActivity(intentComponent);
									}
									Message msg = new Message();
									Bundle bun = new Bundle();
									bun.putString("action", "leave");
									msg.obj = bun;
									audioHanler.sendMessage(msg);
									Log.e("test",
											"Going to Hangup ^^^^^^^^^^^^^^^^^:");
								} catch (Exception e) {
									// TODO: handle exception
									Log.d("test",
											"????????? hang " + e.toString());
									e.printStackTrace();
								}

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

	private String getConnectedMembersName() {
		String strReturnText = "";
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

				//
				starttimearray[i] = CallDispatcher.conferenceMembersTime
						.get(strARMembers[i]);
				if (starttimearray[i] == null) {
					starttimearray[i] = strStartTime;
				}
				autocallarray[i] = "0";

				if (i == (strARMembers.length - 1)) {
					strReturnText += strARMembers[i];
				} else {
					strReturnText += strARMembers[i] + ",";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strReturnText;
	}

	private String getmembersConnected() {
		String strReturnText = "";
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

					//
					starttimearray[i] = CallDispatcher.conferenceMembersTime
							.get(strARMembers[i]);
					if (starttimearray[i] == null) {
						starttimearray[i] = endtimearray[0];
					}
					autocallarray[i] = "0";

					if (i == (strARMembers.length - 1)) {
						strReturnText += strARMembers[i];
					} else {
						strReturnText += strARMembers[i] + ",";
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {

		}
		return strReturnText;
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

	public void showWifiStateChangedAlert(String message) {
		Log.d("wifi", "callDialScreen");
		AlertDialog.Builder alertCall = new AlertDialog.Builder(
				AudioPagingSRWindow.this);
		alertCall
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.hangup),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {

									AudioPagingSRWindow.this.forceHAngUp();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
		alertCall.show();
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
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

		outState.putString("mode", mode);
		outState.putString("sessionid", sessionid);
		outState.putString("calltype", callType);
		outState.putString("receive", receiver);
		outState.putString("buddy", callerName);

		super.onSaveInstanceState(outState);
	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	public void notifyType2Received() {
		audioHanler.post(new Runnable() {

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

	public void notifyGSMCallAccepted() {
		Message msg = new Message();
		Bundle bun = new Bundle();
		bun.putString("action", "leave");
		msg.obj = bun;
		audioHanler.sendMessage(msg);
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

}
