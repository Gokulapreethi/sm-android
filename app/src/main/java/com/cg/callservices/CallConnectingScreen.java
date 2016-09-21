package com.cg.callservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.ProfileBean;
import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.service.ChatHeadDrawerService;
import com.util.SingleInstance;

import org.lib.model.SignalingBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class CallConnectingScreen extends Fragment {

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

	private static Context context;

	private FrameLayout frameLayout;

	private ImageView profilePic1,profilePic2,profilePic3;

	private boolean isBConf = false;
	private String callerName;
	private ImageLoader imageLoader;
	private ImageView profilePicture;
	private ArrayList<String> confMembers=new ArrayList<String>();
	private static CallConnectingScreen callConnectingScreen;
	public View rootView;
	RelativeLayout mainHeader;

	public static CallConnectingScreen getInstance(Context maincontext) {
		try {
			if (callConnectingScreen == null) {

				context = maincontext;
				callConnectingScreen = new CallConnectingScreen();

			}

			return callConnectingScreen;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return callConnectingScreen;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		context = this;
//		setContentView(R.layout.call_connecting);
//		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		try {
			if(rootView==null) {
            final Window win = getActivity().getWindow();
            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

    //		this.setFinishOnTouchOutside(false);
            mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
            mainHeader.setVisibility(View.GONE);
            final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            min_outcall=(ImageView)getActivity().findViewById(R.id.min_outcall);
//            min_outcall.setVisibility(View.GONE);
//             min_incall=(ImageView)getActivity().findViewById(R.id.min_incall);
//            min_incall.setVisibility(View.GONE);
            SingleInstance.instanceTable.put("connection", callConnectingScreen);
    //		if(rootView==null) {
                rootView = inflater.inflate(R.layout.call_connecting, null);
                Bundle bndl = getArguments();
                calltype = bndl.getString("type");
                UserId = bndl.getString("name");
                profilePicture = (ImageView) rootView.findViewById(R.id.profilePic);
                ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(UserId);
                imageLoader = new ImageLoader(SingleInstance.mainContext);
                if (pBean.getPhoto() != null) {
                    String profilePic = pBean.getPhoto();
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
                callerName = pBean.getFirstname() + " " + pBean.getLastname();
                iscoonecting = bndl.getBoolean("status");
                isBConf = bndl.getBoolean("bconf");
                bean = (SignalingBean) bndl.getSerializable("bean");
                tilte = (TextView)rootView. findViewById(R.id.callscreen);
                tv_name = (TextView)rootView. findViewById(R.id.my_userinfo_tv);
                tv_status = (TextView)rootView. findViewById(R.id.status);
                btn_hangup = (Button) rootView.findViewById(R.id.btn_han);
                frameLayout = (FrameLayout)rootView. findViewById(R.id.frame_lay);
                profilePic1 = (ImageView)rootView. findViewById(R.id.profilePic1);
                profilePic2 = (ImageView) rootView.findViewById(R.id.profilePic2);
                profilePic3 = (ImageView) rootView.findViewById(R.id.profilePic3);
                Button minimize=(Button)rootView.findViewById(R.id.minimize_btn);
                minimize.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
						try {
//							Intent serviceIntent = new Intent(getActivity(),FloatingCallService.class);
//							serviceIntent.putExtra("sview",1);
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
//								min_outcall.setVisibility(View.VISIBLE);
//							} else {
								Intent serviceIntent = new Intent(getActivity(), ChatHeadDrawerService.class);
								serviceIntent.putExtra("sview", 1);
								getActivity().startService(serviceIntent);
//							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
                });

                if (WebServiceReferences.callDispatch.containsKey("calldisp"))
                    callDisp = (CallDispatcher) WebServiceReferences.callDispatch
                            .get("calldisp");
                else
                    callDisp = new CallDispatcher(context);


                if (isBConf && (calltype.equalsIgnoreCase("VC") || calltype.equalsIgnoreCase("AC"))) {
                    setTitle();
                    frameLayout.setVisibility(View.VISIBLE);
                    profilePicture.setVisibility(View.INVISIBLE);
                    int i = 0;
                    Log.i("onlineuser", "confereence  " + confMembers.size());
                    for (String user : confMembers) {
                        Log.i("onlineuser", "confereence members " + user);
                        ProfileBean bean = DBAccess.getdbHeler().getProfileDetails(user);
                        if (bean.getPhoto() != null) {
                            Log.i("onlineuser", "confereence members profile ");
                            String profilePic = bean.getPhoto();
                            if (!profilePic.contains("COMMedia")) {
                                profilePic = Environment.getExternalStorageDirectory()
                                        + "/COMMedia/" + profilePic;
                            }
                            if (i == 0) {
                                profilePic3.setVisibility(View.VISIBLE);
                                imageLoader.DisplayImage(profilePic, profilePic3,
                                        R.drawable.img_user);
                            } else if (i == 1) {
                                profilePic2.setVisibility(View.VISIBLE);
                                imageLoader.DisplayImage(profilePic, profilePic2,
                                        R.drawable.img_user);
                            } else {
                                profilePic1.setVisibility(View.VISIBLE);
                                imageLoader.DisplayImage(profilePic, profilePic1,
                                        R.drawable.img_user);
                            }
                        }
                        i++;
                    }
                    CallDispatcher.conConference.clear();

                }

                btn_hangup.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.d("lddd", "Button Clicked.....######################");
                        if (SingleInstance.instanceTable.containsKey("connection")) {
                            Log.i("audiocall","context table remove connection");
                            SingleInstance.instanceTable.remove("connection");
                        }
                        Log.i("audiocall","btn_hangup--->"+SingleInstance.instanceTable.containsKey("connection")+" isBConf : "+isBConf);
                        if (isBConf) {
                            disconnectConfMembers();

                        } else {
                            HangupCall();
                        }
                    }
                });
                setNameandTitle();
                if (isBConf)
                    setTitle();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();
		Log.i("Minimise","CallConnectingScreen OnResume");
//		if(min_outcall != null) {
//			min_outcall.setVisibility(View.GONE);
//		}

//		if(min_incall != null) {
//			min_incall.setVisibility(View.GONE);
//		}
    }

	@Override
	public void onPause() {
		super.onPause();
		Log.i("Minimise", "CallConnectingScreen OnPause");
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

			if (CallDispatcher.callType != null) {
				CallDispatcher.callType = null;
			}

			CallDispatcher.isCallInProgress = false;
			CallDispatcher.isCallInitiate = false;

			CallDispatcher.currentSessionid = null;

			CallDispatcher.sb.setCallstatus("callattended");
			CallDispatcher.sb.setStartTime(callDisp.getCurrentDateandTime());
			CallDispatcher.sb.setEndTime(callDisp.getCurrentDateandTime());
			CallDispatcher.sb
					.setCallDuration(SingleInstance.mainContext
							.getCallDuration(CallDispatcher.sb
											.getStartTime(),
									CallDispatcher.sb
											.getEndTime()));
			Log.i("callentry", "db entry 4");
//			DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//			DBAccess.getdbHeler()
//					.saveOrUpdateRecordtransactiondetails(
//							CallDispatcher.sb);

			if(CallDispatcher.callHistoryDetails != null) {
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
				SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
				Log.i("callentry","time 1--->"+hist_bean.getStartTime());
				if(hist_bean.getStartTime()==null){
					hist_bean.setStartTime(callDisp.getCurrentDateandTime());
				}
				hist_bean.setParticipant_name(participant);
				hist_bean.setEndTime(hist_bean.getStartTime());
				hist_bean.setCallDuration(SingleInstance.mainContext
						.getCallDuration(hist_bean.getStartTime(),
								hist_bean.getEndTime()));
				hist_bean.setCallstatus("callattended");
				DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
				DBAccess.getdbHeler().insertGroupCallChat(hist_bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("stattus", "completed  vcvcv " + e);
		} finally {
			finishConnectingScreen();
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
	public void setTitle(String Title){
		Temp=Title;
	}

	public void setTitle() {
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

					CallDispatcher.sb.setCallstatus("callattended");
					CallDispatcher.sb.setStartTime(callDisp.getCurrentDateandTime());
					CallDispatcher.sb.setEndTime(callDisp.getCurrentDateandTime());
					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(CallDispatcher.sb
													.getStartTime(),
											CallDispatcher.sb
													.getEndTime()));
					Log.i("callentry", "db entry 5");
//					DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//					DBAccess.getdbHeler()
//							.saveOrUpdateRecordtransactiondetails(
//									CallDispatcher.sb);

					if(CallDispatcher.callHistoryDetails != null) {
						String participant = null;
						if (CallDispatcher.conferenceMembers != null && CallDispatcher.conferenceMembers.size() > 0) {
							for (String name : CallDispatcher.conferenceMembers) {
								if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())) {
									if (participant == null) {
										participant = name;
									} else {
										participant = participant + "," + name;
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
						if (participant != null) {
							CallDispatcher.sb.setParticipant_name(participant);
						}
						SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
						if(hist_bean.getStartTime()==null){
							hist_bean.setStartTime(callDisp.getCurrentDateandTime());
						}
						Log.i("callentry","time 2--->"+hist_bean.getStartTime());
						hist_bean.setParticipant_name(participant);
						hist_bean.setEndTime(hist_bean.getStartTime());
						hist_bean.setCallDuration(SingleInstance.mainContext
								.getCallDuration(hist_bean.getStartTime(),
										hist_bean.getEndTime()));
						hist_bean.setCallstatus("callattended");
						DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
						DBAccess.getdbHeler().insertGroupCallChat(hist_bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finishConnectingScreen();
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

					CallDispatcher.sb.setCallstatus("callattended");
					CallDispatcher.sb.setStartTime(callDisp.getCurrentDateandTime());
					CallDispatcher.sb.setEndTime(callDisp.getCurrentDateandTime());


					CallDispatcher.sb
							.setCallDuration(SingleInstance.mainContext
									.getCallDuration(CallDispatcher.sb
													.getStartTime(),
											CallDispatcher.sb
													.getEndTime()));
					Log.i("callentry", "db entry 6");
//					DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//					DBAccess.getdbHeler()
//							.saveOrUpdateRecordtransactiondetails(
//									CallDispatcher.sb);
					if(CallDispatcher.callHistoryDetails != null) {
						String participant = null;
						if (CallDispatcher.conferenceMembers != null && CallDispatcher.conferenceMembers.size() > 0) {
							for (String name : CallDispatcher.conferenceMembers) {
								if(!name.equalsIgnoreCase(CallDispatcher.sb.getHost())) {
									if (participant == null) {
										participant = name;
									} else {
										participant = participant + "," + name;
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

						if (participant != null) {
							CallDispatcher.sb.setParticipant_name(participant);
						}
						SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
						Log.i("callentry","time 3--->"+hist_bean.getStartTime());
						if(hist_bean.getStartTime()==null){
							hist_bean.setStartTime(callDisp.getCurrentDateandTime());
						}
						hist_bean.setParticipant_name(participant);
						hist_bean.setEndTime(hist_bean.getStartTime());
						hist_bean.setCallDuration(SingleInstance.mainContext
								.getCallDuration(hist_bean.getStartTime(),
										hist_bean.getEndTime()));
						hist_bean.setCallstatus("callattended");
						DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
						DBAccess.getdbHeler().insertGroupCallChat(hist_bean);
					}
					Log.d("hang", "Hang up from the call");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finishConnectingScreen();
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
					finishConnectingScreen();
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
					finishConnectingScreen();
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
	public void onDestroy() {
		super.onDestroy();
		final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//		if (SingleInstance.instanceTable.containsKey("connection")) {
//			SingleInstance.instanceTable.remove("connection");
//		}
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



	private void hangUpAlert() {
		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.need_call_hangup);
		AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
		CallDispatcher.callHistoryDetails.setStartTime(callDisp.getCurrentDateandTime());
		final String from = sbean.getTo();
//		if (SingleInstance.contextTable.containsKey("groupchat")) {
//			GroupChatActivity groupChatActivity = (GroupChatActivity) SingleInstance.contextTable.get("groupchat");
//			groupChatActivity.finish();
//		}
		if (sbean.getCallType().equals("AC") || (sbean.getPreviouscalltype() != null && sbean.getPreviouscalltype().equalsIgnoreCase("AC"))) {

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
			AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			appMainActivity.closingActivity();
		//	min_outcall.setVisibility(View.GONE);

			if (SingleInstance.instanceTable.containsKey("connection")) {
				SingleInstance.instanceTable.remove("connection");
			}
			rootView=null;
			FragmentManager fm =
					AppReference.mainContext.getSupportFragmentManager();
			Bundle bun = new Bundle();
			bun.putSerializable("signal", sbean);
			bun.putString("buddy", from);
			bun.putString("receive", "true");
			bun.putString("host",from);
//			bun.putExtra("signal", bun);
			bun.putBoolean("isreceiver", true);
			if(sbean.getPreviouscalltype() != null && sbean.getPreviouscalltype().equalsIgnoreCase("AC")){
				bun.putString("previouscalltype", "AC");
				bun.putString("currentcalltype", "VC");
			}
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
			Intent i = new Intent(SingleInstance.mainContext,
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
			Intent i = new Intent(SingleInstance.mainContext,
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
			Intent i = new Intent(SingleInstance.mainContext,
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

			Intent i = new Intent(SingleInstance.mainContext,
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
			Intent i = new Intent(SingleInstance.mainContext,
					VideoPagingSRWindow.class);
			i.putExtras(bundle);
			startActivity(i);

		} else if (sbean.getCallType().equals("VC")) {

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
				AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
						.get("MAIN");
				appMainActivity.closingActivity();
				if (SingleInstance.instanceTable.containsKey("connection")) {
					SingleInstance.instanceTable.remove("connection");
				}
				rootView=null;
				FragmentManager fm =
						AppReference.mainContext.getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("sessionid", sbean.getSessionid());
				bundle.putString("buddyName", from);
				bundle.putString("receive", "true");
				bundle.putString("host",from);
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
	public void finishConnectingScreen()
	{
		Log.i("AudioCall","CallCOnnectingScreen finishConnectingScreen");
		if (SingleInstance.instanceTable.containsKey("connection")) {
			SingleInstance.instanceTable.remove("connection");
		}
		rootView=null;
		FragmentManager fm =
				AppReference.mainContext.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ContactsFragment contactsFragment = ContactsFragment
//				.getInstance(context);
		ft.replace(R.id.activity_main_content_fragment,
				AppReference.bacgroundFragment);
		ft.commitAllowingStateLoss();
		mainHeader.setVisibility(View.VISIBLE);
//		min_outcall.setVisibility(View.GONE);
	}

	public void removeInstance(){
		Log.i("AudioCall","CallCOnnectingScreen removeInstance");
		if (SingleInstance.instanceTable.containsKey("connection")) {
			SingleInstance.instanceTable.remove("connection");
		}
		if(rootView != null)
			rootView=null;
	}
}
