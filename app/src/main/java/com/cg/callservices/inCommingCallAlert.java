package com.cg.callservices;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.service.ChatHeadDrawerService;
import com.util.SingleInstance;

import org.audio.AudioProperties;
import org.lib.model.SignalingBean;

import java.util.ArrayList;
import java.util.HashMap;

public class inCommingCallAlert extends Fragment {

	private ImageView accept = null;

	private ImageView reject = null, ignore = null;

	private TextView tv_title = null;

	private TextView call_type = null;

	private String strPrevScreen = null;

	private CallDispatcher callDisp = null;

	private ImageView profilePicture = null;

	private String from = null;

	private String to = null;

	private SignalingBean sbaen = null;

	private static Context context = null;

	private KeyguardManager keyguardManager;
	RelativeLayout mainHeader;
//	ImageView min_incall;

	private KeyguardLock lock;
	private static inCommingCallAlert incommingCallAlert;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	private ImageLoader imageLoader;
	private ProfileBean bean;
	public View rootView;
	Bundle bundlevalues;

	public static inCommingCallAlert getInstance(Context maincontext) {
		try {
			if (incommingCallAlert == null) {

				context = maincontext;
				incommingCallAlert = new inCommingCallAlert();

			}

			return incommingCallAlert;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return incommingCallAlert;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.callalertscreen);
		try {
			if(rootView==null) {
				SingleInstance.instanceTable.put("alertscreen", incommingCallAlert);
				keyguardManager = (KeyguardManager) getActivity().getSystemService(Activity.KEYGUARD_SERVICE);
				lock = keyguardManager.newKeyguardLock(context.KEYGUARD_SERVICE);
				lock.disableKeyguard();
				//		context = this;

				//		DisplayMetrics displaymetrics = new DisplayMetrics();
				//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				//		int noScrHeight = displaymetrics.heightPixels;
				//		int noScrWidth = displaymetrics.widthPixels;
				//		this.setFinishOnTouchOutside(false);
				//		RelativeLayout ll = (RelativeLayout) findViewById(R.id.callalert_lay);
				//		Window window=getWindow();
				//		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					callDisp = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					callDisp = new CallDispatcher(context);

				//		callDisp.setNoScrHeight(noScrHeight);
				//		callDisp.setNoScrWidth(noScrWidth);
				//		displaymetrics = null;
				bundlevalues = getArguments();
				final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
				mainHeader.setVisibility(View.GONE);
//				min_incall = (ImageView) getActivity().findViewById(R.id.min_incall);
//				min_incall.setVisibility(View.GONE);
				//		if(rootView==null) {
				rootView = inflater.inflate(R.layout.callalertscreen, null);

				accept = (ImageView) rootView.findViewById(R.id.tv_accept);
				reject = (ImageView) rootView.findViewById(R.id.tv_decline);
				ignore = (ImageView) rootView.findViewById(R.id.tv_ignore);
				Button minimize = (Button) rootView.findViewById(R.id.minimize_btn);

				tv_title = (TextView) rootView.findViewById(R.id.caller_name);
				call_type = (TextView) rootView.findViewById(R.id.call_type);
				profilePicture = (ImageView) rootView.findViewById(R.id.profile_pic);
				sbaen = (SignalingBean) bundlevalues.getSerializable("bean");
				CallDispatcher.sb = sbaen;
				CallDispatcher.notify_sb = sbaen;
				bean = DBAccess.getdbHeler().getProfileDetails(sbaen.getFrom());
				changeTextalert();
				Log.i("thread", ">>>>>>>>>>>> incoming call on create");
				imageLoader = new ImageLoader(SingleInstance.mainContext);
				if (bean.getPhoto() != null) {
					String profilePic = bean.getPhoto();
					Log.i("AAAA", "MYACCOUNT " + profilePic);
					if (profilePic != null && profilePic.length() > 0) {
						if (!profilePic.contains("COMMedia")) {
							profilePic = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + profilePic;
						}
						Log.i("AAAA", "MYACCOUNT " + profilePic);
						imageLoader.DisplayImage(profilePic, profilePicture,
								R.drawable.img_user);
					}
				}
				minimize.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
//							Intent serviceIntent = new Intent(getActivity(),FloatingCallService.class);
//							serviceIntent.putExtra("sview",0);
//							getActivity().startService(serviceIntent);

							FragmentManager fm =
                                    AppReference.mainContext.getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							//					ContactsFragment contactsFragment = ContactsFragment
							//							.getInstance(context);
							ft.replace(R.id.activity_main_content_fragment,
                                    AppReference.bacgroundFragment);
							ft.commitAllowingStateLoss();
							AppReference.mainContext.openNonClosedActivity();
//							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//								min_incall.setVisibility(View.VISIBLE);
//							} else {
								Intent serviceIntent = new Intent(getActivity(), ChatHeadDrawerService.class);
								serviceIntent.putExtra("sview", 0);
								getActivity().startService(serviceIntent);
//							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				if (WebServiceReferences.contextTable
						.containsKey("multimediautils"))
					((MultimediaUtils) WebServiceReferences.contextTable
							.get("multimediautils")).StopAudioPlay();
				accept.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (WebServiceReferences.missedcallCount.containsKey(sbaen
								.getFrom()))
							WebServiceReferences.missedcallCount.remove(sbaen.getFrom());

						if (CallDispatcher.LoginUser != null) {
							acceptCall(sbaen.getFrom());
						} else {
							callDisp.hangUpCall();
						}
					}
				});

				reject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (WebServiceReferences.missedcallCount.containsKey(sbaen
								.getFrom()))
							WebServiceReferences.missedcallCount.remove(sbaen.getFrom());

						rejectCall();
					}
				});

				ignore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						CallDispatcher.isCallignored = true;
						callDisp.stopRingTone();
						finishactivity();
					}
				});

				SharedPreferences sPreferences = PreferenceManager
						.getDefaultSharedPreferences(SingleInstance.mainContext
								.getApplicationContext());
				boolean isAutoAccept = sPreferences.getBoolean("autoaccept", false);
				if (isAutoAccept
						&& SingleInstance.mainContext.isAutoAcceptEnabled(
						CallDispatcher.LoginUser,
						CallDispatcher.getUser(sbaen.getFrom(), sbaen.getTo()))) {
					acceptCall(sbaen.getFrom());
					finishactivity();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();

//		if(min_outcall != null) {
//			min_outcall.setVisibility(View.GONE);
//		}

//		if(min_incall != null) {
//			min_incall.setVisibility(View.GONE);
//		}
    }

	public void changeTextalert() {
		CallDispatcher.isCallAcceptRejectOpened = true;
		CallDispatcher.isIncomingAlert = true;
//		SingleInstance.instanceTable.put("alertscreen", incommingCallAlert);

		Log.i("ACal", "showIncomingAlert will be viewed");
		from = sbaen.getFrom();
		to = sbaen.getTo();

//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		int noScrHeight = displaymetrics.heightPixels;
//		int noScrWidth = displaymetrics.widthPixels;
//
//		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
//			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
//					.get("calldisp");
//		else
//			callDisp = new CallDispatcher(context);
//
//		callDisp.setNoScrHeight(noScrHeight);
//		callDisp.setNoScrWidth(noScrWidth);
//		displaymetrics = null;
		String CallerName=bean.getFirstname()+" "+bean.getLastname();

		if (sbaen.getCallType().equals("AC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_call));
		} else if (sbaen.getCallType().equals("VC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_call));
		} else if (sbaen.getCallType().equals("ABC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_broadcast_bc));
		} else if (sbaen.getCallType().equals("VBC")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_broadcast_bc));
		} else if (sbaen.getCallType().equals("AP")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.audio_unicast));
		} else if (sbaen.getCallType().equals("VP")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.video_unicast));
		} else if (sbaen.getCallType().equals("SS")) {
			tv_title.setText(CallerName);
			call_type.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.screen_sharing));
		}

	}

	public void rejectCall() {
		try {

			CallDispatcher.isCallInitiate = false;
			CallDispatcher.isCallAcceptRejectOpened = false;

			CallDispatcher.isIncomingCall = false;
			CallDispatcher.isIncomingAlert = false;
			if (CallDispatcher.LoginUser != null) {

				callDisp.rejectInComingCall(sbaen);

				//For HangUp
				CallDispatcher.sb.setEndTime(callDisp.getCurrentDateandTime());
				Object objCallScreen = SingleInstance.instanceTable
						.get("callscreen");
				if (objCallScreen == null) {
					CallDispatcher.sb.setCallDuration("00:00:00");
				}else
					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(
											CallDispatcher.sb.getStartTime(),
											CallDispatcher.sb.getEndTime()));
				Log.d("Test","TimeDuration inside callDispatcher"+CallDispatcher.sb.getStartTime()+""+CallDispatcher.sb.getEndTime());

				//For Callhistory host and participant name entry
				//Start
				CallDispatcher.sb.setHost_name(CallDispatcher.sb.getHost());
				String participant=null;
				if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
					for(String name:CallDispatcher.conferenceMembers){
						if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())){
							if(participant==null){
								participant=name;
							}else{
								participant=participant+","+name;
							}

						}
					}
				}

				if(CallDispatcher.removed_current_conf_members!=null && CallDispatcher.removed_current_conf_members.size()>0){
					for(String name:CallDispatcher.removed_current_conf_members){
						if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())){
							if(participant==null){
								participant=name;
							}else{
								participant=participant+","+name;
							}

						}
					}
				}

				if(!CallDispatcher.sb.getHost().equalsIgnoreCase(CallDispatcher.LoginUser)){
					if(participant==null){
						participant=CallDispatcher.LoginUser;
					}else{
						participant=participant+","+CallDispatcher.LoginUser;
					}
				}

				if(participant!=null){
					CallDispatcher.sb.setParticipant_name(participant);
				}
				//end

				CallDispatcher.sb.setCallstatus("callattended");
				Log.i("callentry", "db entry 7");
//				DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//				DBAccess.getdbHeler().saveOrUpdateRecordtransactiondetails(
//						CallDispatcher.sb);

				if(CallDispatcher.callHistoryDetails != null) {
					SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
					hist_bean.setParticipant_name(participant);
					hist_bean.setEndTime(callDisp.getCurrentDateandTime());
					hist_bean.setCallDuration(SingleInstance.mainContext
							.getCallDuration(hist_bean.getStartTime(),
									hist_bean.getEndTime()));
					hist_bean.setCallstatus("callattended");
					DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
					int row=DBAccess.getdbHeler().insertGroupCallChat(hist_bean,false);
					if(AppReference.mainContext != null && row==1) {
						AppReference.mainContext.CallEntryToServer(hist_bean);
					}
				}
			}
			CallDispatcher.currentSessionid = null;
			callDisp.isHangUpReceived = false;

			CallDispatcher.conferenceMembers.clear();
			CallDispatcher.buddySignall.clear();
			//

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			callDisp.stopRingTone();
			finishactivity();
		}
	}

	public void acceptCall(String strBuddyName) {
		try {
			callDisp.isHangUpReceived = false;
			callDisp.stopRingTone();
			if (CallDispatcher.LoginUser != null) {

				if (sbaen.getCallType().equals("AC")) {

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);

					Log.d("test", "open Adding "
							+ CallDispatcher.conferenceMembers.size());
					Log.i("ACal", "Audio Screen will be displayedd");
					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);

					CallDispatcher.conferenceMember_Details = new HashMap<String,SignalingBean>();
					sb.setRunningcallstate("Connecting");
					CallDispatcher.conferenceMember_Details.put(strBuddyName,sb);
//					finishactivity();

				} else if (sbaen.getCallType().equals("VC")) {
					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();
					Log.i("ACal", "Video Screen will be displayed");

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					CallDispatcher.conferenceMembers = new ArrayList<String>();

					CallDispatcher.conferenceMembers.add(strBuddyName);

					Log.d("test", "on video call received ^^^^^^^^^"
							+ CallDispatcher.conferenceMembers.size());
					CallDispatcher.buddySignall.put(strBuddyName,
							CallDispatcher.sb);
					// openVideoCallScreen();
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();

					CallDispatcher.conferenceMember_Details = new HashMap<String,SignalingBean>();
					sb.setRunningcallstate("Connecting");
					CallDispatcher.conferenceMember_Details.put(strBuddyName,sb);

					ShowConnectionScreen(sb);
//					finishactivity();

				}

				else if (sbaen.getCallType().equals("ABC")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());

					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);
					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
//					finishactivity();
				} else if (sbaen.getCallType().equals("VBC")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());
					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);
					callDisp.stopRingTone();

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					CallDispatcher.isCallInProgress = true;

					CallDispatcher.hsAddedBuddyNameFromConferenceCall = new HashMap<String, SignalingBean>();
					if (CallDispatcher.callType == null) {
						CallDispatcher.callType = "VBC";
					}
					if (CallDispatcher.audioProperties == null) {
						CallDispatcher.audioProperties = new AudioProperties(
								context);
					}
					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
//					finishactivity();
				} else if (sbaen.getCallType().equalsIgnoreCase("AP")) {

					//
					CallDispatcher.sb = sbaen;

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(from);

					callDisp.stopRingTone();
					callDisp.isHangUpReceived = false;
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;
					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
//					finishactivity();
					//

				} else if (sbaen.getCallType().equalsIgnoreCase("VP")) {

					Log.i("thread", "came to listall notification.....");

					CallDispatcher.sb = sbaen;
					callDisp.isHangUpReceived = false;

					Log.d("call", "start alert");
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine
									.acceptCall(CallDispatcher.sb);
						}
					}).start();

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(from);

					callDisp.stopRingTone();
					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = true;

					SignalingBean sb = (SignalingBean) CallDispatcher.sb
							.clone();
					ShowConnectionScreen(sb);
//					finishactivity();
				} else if (sbaen.getCallType().equals("SS")) {
					CallDispatcher.sb.setFrom(to);
					CallDispatcher.sb.setTo(from);
					CallDispatcher.sb.setType("1");
					CallDispatcher.sb.setResult("0");
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppMainActivity.commEngine.acceptCall(sbaen);
						}
					}).start();

					CallDispatcher.pagingMembers = new ArrayList<String>();

					CallDispatcher.pagingMembers.add(sbaen.getTo());
					Log.d("PM", "s " + sbaen.getTo());
					CallDispatcher.buddySignall.put(sbaen.getTo(), sbaen);

					CallDispatcher.conferenceMembers = new ArrayList<String>();
					CallDispatcher.conferenceMembers.add(strBuddyName);
					callDisp.stopRingTone();

					CallDispatcher.isIncomingCall = false;
					CallDispatcher.isIncomingAlert = false;

					CallDispatcher.isCallInProgress = true;

					CallDispatcher.hsAddedBuddyNameFromConferenceCall = new HashMap<String, SignalingBean>();
					if (CallDispatcher.callType == null) {
						CallDispatcher.callType = "SS";
					}
					if (CallDispatcher.audioProperties == null) {
						CallDispatcher.audioProperties = new AudioProperties(
								context);
					}
					SignalingBean sb = (SignalingBean) sbaen.clone();
					ShowConnectionScreen(sb);
//					finishactivity();
				}

			} else {
				finishactivity();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			callDisp.stopRingTone();
		}
	}

	public void finishactivity() {
		CallDispatcher.isCallInitiate = false;


		if (SingleInstance.instanceTable.containsKey("alertscreen")) {
			SingleInstance.instanceTable.remove("alertscreen");
		}

		rootView = null;
		FragmentManager fm =
				AppReference.mainContext.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ContactsFragment contactsFragment = ContactsFragment
//				.getInstance(context);
		ft.replace(R.id.activity_main_content_fragment,
				AppReference.bacgroundFragment);
		ft.commitAllowingStateLoss();
		AppReference.mainContext.openNonClosedActivity();
		mainHeader.setVisibility(View.VISIBLE);
//		min_incall.setVisibility(View.GONE);
	}

	public void removeInstance(){
		Log.i("AudioCall","incoming Alert removeInstance");
		if (SingleInstance.instanceTable.containsKey("alertscreen")) {
			SingleInstance.instanceTable.remove("alertscreen");
		}
		if(rootView != null)
			rootView=null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
//		if (SingleInstance.instanceTable.containsKey("alertscreen")) {
//			SingleInstance.instanceTable.remove("alertscreen");
//		}
		/* lock.reenableKeyguard(); */
		final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		if (callDisp.wl != null) {
			Log.v("a", "Releasing wakelock");
			try {

				callDisp.wl.release();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		} else {
			// should never happen during normal workflow
			Log.e("aa", "Wakelock reference is null");
		}

		super.onDestroy();
	}

	private void ShowConnectionScreen(final SignalingBean sbean) {
//		min_incall.setVisibility(View.GONE);
		final String from = sbean.getTo();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method
				// stub
				callDisp.stopRingTone();
			}
		}).start();

		try {
			if (SingleInstance.instanceTable.containsKey("alertscreen")) {
				SingleInstance.instanceTable.remove("alertscreen");
			}

			rootView=null;
			FragmentManager fm =
					AppReference.mainContext.getSupportFragmentManager();
			Bundle bundle = new Bundle();
			bundle.putString("name", from);
			bundle.putString("type", sbean.getCallType());
			bundle.putBoolean("status", true);
			bundle.putSerializable("bean", sbean);
			FragmentTransaction ft = fm.beginTransaction();
			CallConnectingScreen callConnectingScreen = CallConnectingScreen
					.getInstance(context);
			callConnectingScreen.setArguments(bundle);
			ft.replace(R.id.activity_main_content_fragment,
					callConnectingScreen);
			ft.commitAllowingStateLoss();
//			Intent intent = new Intent(context, CallConnectingScreen.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("name", from);
//			bundle.putString("type", sbean.getCallType());
//			bundle.putBoolean("status", true);
//			bundle.putSerializable("bean", sbean);
//			intent.putExtras(bundle);
//			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//
//			AlertDialog alert = null;
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//			String ask = SingleInstance.mainContext.getResources().getString(
//					R.string.need_call_hangup);
//
//			builder.setMessage(ask)
//					.setCancelable(false)
//					.setPositiveButton(
//							SingleInstance.mainContext.getResources()
//									.getString(R.string.yes),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//
//									rejectCall();
//								}
//							})
//					.setNegativeButton(
//							SingleInstance.mainContext.getResources()
//									.getString(R.string.no),
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									dialog.cancel();
//								}
//							});
//			alert = builder.create();
//			alert.show();
//
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

}
