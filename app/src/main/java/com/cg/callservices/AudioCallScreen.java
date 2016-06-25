package com.cg.callservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ContactAdapter;
import com.bean.ProfileBean;
import com.bean.UserBean;
import com.cg.DB.DBAccess;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.cg.snazmed.R;
import com.group.AddGroupMembers;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

import org.audio.AudioProperties;
import org.core.VideoCallback;
import org.lib.model.BuddyInformationBean;
import org.lib.model.RecordTransactionBean;
import org.lib.model.SignalingBean;
import org.util.GraphicsUtil;
import org.util.Queue;
import org.video.Preview;
import org.video.PreviewFrameSink;
import org.video.VideoFrameRenderer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class AudioCallScreen extends Fragment implements VideoCallback {

	private TextView tvBuddies,tv_name,tv_status;

	private TextView tvCallTime;
	private double timeElapsed = 0;
	Handler history_handler;

	private Chronometer chTimer;
	private int numOfCam;

	private boolean isTimer_running = false;

	private String strPrevScreen = null;

	private String[] touserarray;

	private String[] autocallarray;

	private String[] starttimearray;

	private String[] endtimearray;

	private boolean isHangedUp = false;
	TextView member_count;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	private Button btnHangup, btnemergency;
	
	private ImageView  btn_onlineb,btnSpeaker_video, btn_onlineb_video, btnMic_video, btnHangup_video;

	private Button btnMic,promote_call;

	private Button btnSpeaker;

	private AlertDialog alert = null;

	private Handler handler;

	private Queue videoQueue;

	private VideoThreadMultiWindow videoThread;

	private SignalingBean signBean;
	private int mPlayingPosition = 0;
	String calltype="AC";
	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
	private MediaPlayer mPlayer = new MediaPlayer();

	public String strSessionId;
	int finalTime, startTime;

	private boolean micmute, speaker = false;

	private String receiver = null;

	private String callerName = null;

	private String host = null;

	private String callTypeForServer = "101";

	private HashSet<String> hsCallMembers = new HashSet<String>();

	private String strStartTime;
	private Handler mHandler;

	public String failedUser = null;

	private AudioProperties audioProperties = null;

	public static Context context = null;

	private CallDispatcher objCallDispatcher = null;
	private static ContactAdapter contactAdapter;

	FrameLayout preview_frameLayout , buddyframelayout01, buddyframelayout0102, buddyframelayout02, buddyframelayout03;

	GLSurfaceView buddysurfaceview_01, buddysurfaceview_0102, buddysurfaceview_02, buddysurfaceview_03;

	TextView on_off1,on_off12,on_off2,on_off3,onoff_preview,participant_name1,participant_name12,participant_name2,participant_name3;

	ImageView buddyimageview1, buddyimageview2, buddyimageview3, ownimageview;

	private FrameLayout.LayoutParams surfaceview_layoutParams;

	Preview pv = null;
	Button flipcamera;

	private String currentcall_type = "AC";

	private PreviewFrameSink frameSink = null, frameSink12 = null ,frameSink2 = null, frameSink3 = null;

	private ByteBuffer frameBuffer = null, frameBuffer12 = null ,frameBuffer2 = null, frameBuffer3 = null;

	private int activityOrientation;

	LinearLayout  bottom_videowindows , top_videoWindows,audio_button_layouts,video_lay;

	RelativeLayout own_video_layout, video_layouts,  audio_host_layout, audio_participant_layout;

	View llayAudioCall;
	private Bundle bun = null;

	private boolean isReceiver = false;

	private boolean selfHangup = false;
	
	private boolean isbuddyaudio=false;

	private boolean isBuddyinCall = false;

	private boolean preview_hided = false;

	private int position=0;
	private Vector<UserBean> membersList=new Vector<UserBean>();

	public static synchronized ContactAdapter getContactAdapter() {

		return contactAdapter;
	}

	//For this is used to profilepictures for owner and buddies
	private Vector<BuddyInformationBean> buddyList;
	ImageLoader imageLoader;
	ImageView iv_owner,iv_buddy,min_outcall,min_incall;
	String buddyimage;
	String ownerimage;
	private Button minimize;

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

	private static AudioCallScreen audioCallScreen;
	public View rootView;
	Bundle bundlevalues;
	RelativeLayout mainHeader,audio_minimize;

	public static AudioCallScreen getInstance(Context maincontext) {
		try {
			if (audioCallScreen == null) {

				context = maincontext;
				audioCallScreen = new AudioCallScreen();

			}

			return audioCallScreen;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return audioCallScreen;
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);

		try {
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			if (SingleInstance.mainContext
//					.getResources()
//					.getString(R.string.screenshot)
//					.equalsIgnoreCase(
//							SingleInstance.mainContext.getResources()
//									.getString(R.string.yes))) {
//				getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//						WindowManager.LayoutParams.FLAG_SECURE);
//			}
//			final Window win = getWindow();
//			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//			win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			if(rootView==null) {
				if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}

				bundlevalues = getArguments();
//			bun = getIntent().getBundleExtra("signal");
				receiver = bundlevalues.getString("receive");
				callerName = bundlevalues.getString("buddy");
				host = bundlevalues.getString("host");
				isReceiver = bundlevalues.getBoolean("isreceiver", false);
				Log.d("Audio", "Is receiver --->" + isReceiver);
				mHandler = new Handler();
//			context = this;
				preview_hided = false;
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int noScrHeight = displaymetrics.heightPixels;
				int noScrWidth = displaymetrics.widthPixels;

				if (WebServiceReferences.callDispatch.containsKey("calldisp"))
					objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
							.get("calldisp");
				else
					objCallDispatcher = new CallDispatcher(context);
				mainHeader = (RelativeLayout) getActivity().findViewById(R.id.mainheader);
				mainHeader.setVisibility(View.GONE);
				audio_minimize = (RelativeLayout) getActivity().findViewById(R.id.audio_minimize);
				audio_minimize.setVisibility(View.GONE);

				objCallDispatcher.setNoScrHeight(noScrHeight);
				objCallDispatcher.setNoScrWidth(noScrWidth);
				displaymetrics = null;
				CallDispatcher.networkState = objCallDispatcher.connectivityType();
				objCallDispatcher.isCalledAudiocallScreen = false;

				SingleInstance.instanceTable.put("callscreen", audioCallScreen);
				objCallDispatcher.startPlayer(context);
				if (objCallDispatcher.isHangUpReceived)
					receivedHangUp();

				else if (receiver.equalsIgnoreCase("true")
						&& CallDispatcher.conferenceMembers.size() == 0)
					receivedHangUp();

				audioProperties = new AudioProperties(context);
				 min_outcall = (ImageView) getActivity().findViewById(R.id.min_outcall);
				min_outcall.setVisibility(View.GONE);
				min_incall = (ImageView) getActivity().findViewById(R.id.min_incall);
				min_incall.setVisibility(View.GONE);
//			setContentView(ShowaudioCallScreen());
//			if(rootView==null)
				rootView = ShowaudioCallScreen(inflater);
				strStartTime = objCallDispatcher.getCurrentDateTime();
				signBean = (SignalingBean) bundlevalues.getSerializable("signal");
				strSessionId = signBean.getSessionid();
				CallDispatcher.currentSessionid = strSessionId;
				handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						Bundle bun = (Bundle) msg.obj;
						try {

							String action = bun.getString("action");
							if (action != null) {
								if (action.equals("leave")) {
									Log.d("test", "Received Audio hang ");
									objCallDispatcher.accepted_users.clear();
									chTimer.stop();
									AppMainActivity.ctimer.stop();
									audio_minimize.setVisibility(View.GONE);
									//
									isHangedUp = true;

									enterCallHistory();
									// if (CallDispatcher.sb.getBs_parentid() != null) {
									CallDispatcher.sb
											.setEndTime(objCallDispatcher.getCurrentDateandTime());
									CallDispatcher.sb
											.setCallDuration(SingleInstance.mainContext
													.getCallDuration(CallDispatcher.sb
																	.getStartTime(),
															CallDispatcher.sb
																	.getEndTime()));
									CallDispatcher.sb.setCallstatus("callattended");

									//For Callhistory host and participant name entry
									//Start
									CallDispatcher.sb.setHost_name(host);
									String participant=null;
									if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
										for(String name:CallDispatcher.conferenceMembers){
											if(!name.equalsIgnoreCase(host)){
												if(participant==null){
													participant=name;
												}else{
													participant=participant+","+name;
												}

											}
										}
									}
									if(participant!=null){
										CallDispatcher.sb.setParticipant_name(participant);
									}
									//end

									if (selfHangup) {
										DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
										DBAccess.getdbHeler()
												.saveOrUpdateRecordtransactiondetails(
														CallDispatcher.sb);

										showCallHistory();


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

//								finish();
										finishAudiocallScreen();
									} else {

										try {
											CallDispatcher.currentSessionid = null;
											CallDispatcher.conferenceMembers.clear();

										} catch (Exception e) {
											// TODO: handle exception
										}

//								finish();
										finishAudiocallScreen();

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
											CallDispatcher.isCallInitiate = false;
											chTimer.stop();
											AppMainActivity.ctimer.stop();
											audio_minimize.setVisibility(View.GONE);

											if (!isHangedUp) {


												enterCallHistory();

											}

											CallDispatcher.currentSessionid = null;
											CallDispatcher.conferenceMembers.clear();
											finishAudiocallScreen();
//											receiveHangUpx();

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

											tvBuddies.setText(getNames());

										}
									}
									member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));

								}
							} else if (bun.containsKey("img")) {
								if (bun.getInt("windowid") == 0) {

									frameSink.getFrameLock().lock();
									frameBuffer.position(0);
									frameBuffer.put(bun.getByteArray("img"));
									frameBuffer.position(0);
									frameSink.setNextFrame(frameBuffer);
									frameSink.getFrameLock().unlock();

									buddysurfaceview_01.requestRender();

								} else if (bun.getInt("windowid") == 12) {
									frameSink12.getFrameLock().lock();
									frameBuffer12.position(0);
									frameBuffer12.put(bun.getByteArray("img"));
									frameBuffer12.position(0);
									frameSink12.setNextFrame(frameBuffer12);
									frameSink12.getFrameLock().unlock();
									buddysurfaceview_0102.requestRender();
								} else if (bun.getInt("windowid") == 1) {

									frameSink2.getFrameLock().lock();
									frameBuffer2.position(0);
									// frameRenderer.setPreviewFrameWidth(obtainWidth);
									// frameRenderer.setPreviewFrameHeight(obtainHeight);

									frameBuffer2.put(bun.getByteArray("img"));
									frameBuffer2.position(0);
									frameSink2.setNextFrame(frameBuffer2);
									frameSink2.getFrameLock().unlock();
									buddysurfaceview_02.requestRender();

								} else if (bun.getInt("windowid") == 2) {

									frameSink3.getFrameLock().lock();
									frameBuffer3.position(0);

									frameBuffer3.put(bun.getByteArray("img"));
									frameBuffer3.position(0);
									frameSink3.setNextFrame(frameBuffer3);
									frameSink3.getFrameLock().unlock();
									buddysurfaceview_03.requestRender();
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				};

				handler.postDelayed(runnable, 1000);
				videoQueue = new Queue();
				videoThread = new VideoThreadMultiWindow(videoQueue);
				videoThread.setHandler(handler);
				videoThread.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d("test", "Audio call error " + e);
		}

		return rootView;

	}


    @Override
    public void onResume() {
        super.onResume();
		try {
			Log.i("Minimise","AudioCallScreen OnResume");
			AppMainActivity.inActivity = context;
			if (AppMainActivity.commEngine != null) {
                AppMainActivity.commEngine.setmDecodeFrame(true);
            }

			Activity parent = getActivity();
			if(parent != null){
				audio_minimize.setVisibility(View.GONE);
				if(min_outcall != null) {
					min_outcall.setVisibility(View.GONE);
				}

				if(min_incall != null) {
					min_incall.setVisibility(View.GONE);
				}
			}

			if(currentcall_type.equalsIgnoreCase("VC")){
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                preview_frameLayout.removeView(pv);
                pv.stopPreview();
                pv = AppMainActivity.commEngine.getVideoPreview(context);
                preview_frameLayout.addView(pv, 0);

				buddyframelayout01.removeView(buddysurfaceview_01);
				buddyframelayout0102.removeView(buddysurfaceview_0102);
				buddyframelayout02.removeView(buddysurfaceview_02);
				buddyframelayout03.removeView(buddysurfaceview_03);

				buddyframelayout01.removeView(on_off1);
				buddyframelayout0102.removeView(on_off2);
				buddyframelayout02.removeView(on_off12);
				buddyframelayout03.removeView(on_off3);

				buddysurfaceview_01.destroyDrawingCache();
				buddysurfaceview_0102.destroyDrawingCache();
				buddysurfaceview_02.destroyDrawingCache();
				buddysurfaceview_03.destroyDrawingCache();

				getGLSurfaceView(context);
				getGLSurfaceView1(context);
				getGLSurfaceView2(context);
				getGLSurfaceView3(context);

				buddyframelayout01.addView(buddysurfaceview_01, surfaceview_layoutParams);
				buddyframelayout0102.addView(buddysurfaceview_0102, surfaceview_layoutParams);
				buddyframelayout02.addView(buddysurfaceview_02, surfaceview_layoutParams);
				buddyframelayout03.addView(buddysurfaceview_03, surfaceview_layoutParams);

				buddyframelayout01.addView(on_off1);
				buddyframelayout0102.addView(on_off2);
				buddyframelayout02.addView(on_off12);
				buddyframelayout03.addView(on_off3);

				buddysurfaceview_01.onResume();
				buddysurfaceview_0102.onResume();
				buddysurfaceview_02.onResume();
				buddysurfaceview_03.onResume();

            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		if (AppMainActivity.commEngine != null) {
			AppMainActivity.commEngine.setmDecodeFrame(true);
		}
		if (buddysurfaceview_01 != null) {
			buddysurfaceview_01.onPause();
		}

		if (buddysurfaceview_0102 != null) {
			buddysurfaceview_0102.onPause();
		}

		if (buddysurfaceview_02 != null) {
			buddysurfaceview_02.onPause();
		}

		if(buddysurfaceview_03 != null){
			buddysurfaceview_03.onPause();
		}

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

	public View ShowaudioCallScreen(LayoutInflater inflater) {
		View llayAudioCall = null;

		try {
//		final RelativeLayout  llayAudioCall = ( RelativeLayout ) View.inflate(
//				context, R.layout.call_connecting, null);
			llayAudioCall = inflater.inflate(R.layout.audiocallscreen, null);
//			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			audio_minimize = (RelativeLayout) getActivity().findViewById(R.id.audio_minimize);
			audio_minimize.setVisibility(View.GONE);
//		getActivity().getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			TextView tv = (TextView) llayAudioCall.findViewById(R.id.status);
			LinearLayout member_lay=(LinearLayout)llayAudioCall.findViewById(R.id.member_lay);
			member_count=(TextView)llayAudioCall.findViewById(R.id.members_count);
			Button members=(Button)llayAudioCall.findViewById(R.id.members);
			Button btn_video=(Button)llayAudioCall.findViewById(R.id.btn_video);
			 minimize=(Button)llayAudioCall.findViewById(R.id.minimize_btn);
			members.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
				}
			}, 100);
			member_lay.setVisibility(View.VISIBLE);
			btn_video.setVisibility(View.VISIBLE);
			tv.setText(SingleInstance.mainContext.getResources().getString(
					R.string.auconnected));
			tv.setVisibility(View.GONE);
			tvBuddies = (TextView) llayAudioCall.findViewById(R.id.my_userinfo_tv);
			ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(getNames());
			String Callee=pBean.getFirstname()+" "+pBean.getLastname();
			tvBuddies.setText(Callee);
//		tvBuddies.setText(getNames());
			tvBuddies.setVisibility(View.GONE);
			tvCallTime = (TextView) llayAudioCall.findViewById(R.id.datetime);
			tvCallTime.setVisibility(View.VISIBLE);
			final String strCallDate = objCallDispatcher.getCurrentDateTime();
			tvCallTime.setText(strCallDate);
			tvCallTime.setVisibility(View.GONE);
			btnMic = (Button) llayAudioCall.findViewById(R.id.mic);
			audio_button_layouts = (LinearLayout)llayAudioCall.findViewById(R.id.call_controls);
			audio_host_layout = (RelativeLayout)llayAudioCall.findViewById(R.id.myrelative_layout);
			own_video_layout = (RelativeLayout)llayAudioCall.findViewById(R.id.preview_layout);
			audio_participant_layout = (RelativeLayout)llayAudioCall.findViewById(R.id.myrelative_layout1);
			bottom_videowindows =(LinearLayout)llayAudioCall.findViewById(R.id.bottom_videowindowlayout);
			video_layouts = (RelativeLayout)llayAudioCall.findViewById(R.id.video_views);
			video_lay = (LinearLayout)llayAudioCall.findViewById(R.id.video_lay);

			preview_frameLayout = (FrameLayout)llayAudioCall.findViewById(R.id.VideoView01);
			buddyframelayout01 = (FrameLayout)llayAudioCall.findViewById(R.id.buddyvideoview01);
			buddyframelayout0102 = (FrameLayout)llayAudioCall.findViewById(R.id.buddyvideoview0102);
			buddyframelayout02 = (FrameLayout)llayAudioCall.findViewById(R.id.buddyvideoview02);
			buddyframelayout03 = (FrameLayout)llayAudioCall.findViewById(R.id.buddyvideoview03);

//			buddysurfaceview_01	= (GLSurfaceView)llayAudioCall.findViewById(R.id.buddysurfaceview01);
//			buddysurfaceview_0102 = (GLSurfaceView)llayAudioCall.findViewById(R.id.buddysurfaceview0102);
//			buddysurfaceview_02	= (GLSurfaceView)llayAudioCall.findViewById(R.id.buddysurfaceview02);
//			buddysurfaceview_03	= (GLSurfaceView)llayAudioCall.findViewById(R.id.buddysurfaceview03);


			flipcamera = (Button) llayAudioCall.findViewById(R.id.flipcamera);

			getGLSurfaceView(context);
			getGLSurfaceView1(context);
			getGLSurfaceView2(context);
			getGLSurfaceView3(context);

			assignOnOffIcons(context);

			surfaceview_layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

			buddyframelayout01.addView(buddysurfaceview_01, surfaceview_layoutParams);
			buddyframelayout0102.addView(buddysurfaceview_0102, surfaceview_layoutParams);
			buddyframelayout02.addView(buddysurfaceview_02, surfaceview_layoutParams);
			buddyframelayout03.addView(buddysurfaceview_03, surfaceview_layoutParams);

			buddyframelayout01.addView(on_off1);
			buddyframelayout0102.addView(on_off2);
			buddyframelayout02.addView(on_off12);
			buddyframelayout03.addView(on_off3);

			onoff_preview = (TextView)llayAudioCall.findViewById(R.id.onoffpreview);
			onoff_preview.setTag(true);
//			on_off1 = (TextView)llayAudioCall.findViewById(R.id.onoff2);
			on_off1.setTag(true);
//			on_off12 = (TextView)llayAudioCall.findViewById(R.id.onoff23);
			on_off12.setTag(true);
//			on_off2 = (TextView)llayAudioCall.findViewById(R.id.onoff3);
			on_off2.setTag(true);
//			on_off3 = (TextView)llayAudioCall.findViewById(R.id.onoff4);
			on_off3.setTag(true);

			participant_name1 = (TextView)llayAudioCall.findViewById(R.id.name2);
			participant_name12 = (TextView)llayAudioCall.findViewById(R.id.name23);
			participant_name2 = (TextView)llayAudioCall.findViewById(R.id.name3);
			participant_name3 = (TextView)llayAudioCall.findViewById(R.id.name4);

			ownimageview = (ImageView)llayAudioCall.findViewById(R.id.own_image);
			buddyimageview1 = (ImageView)llayAudioCall.findViewById(R.id.user_image2);
			buddyimageview2 = (ImageView)llayAudioCall.findViewById(R.id.user_image3);
			buddyimageview3 = (ImageView)llayAudioCall.findViewById(R.id.user_image4);

			AppMainActivity.commEngine.setVideoCallback(this);

			btnMic = (Button) llayAudioCall.findViewById(R.id.mic);


			//tvCallTime.setVisibility(View.VISIBLE);

			btnMic.setVisibility(View.VISIBLE);
			minimize.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addShowHideListener();
				}
			});
			flipcamera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (numOfCam > 1) {
						if (Integer.parseInt(Build.VERSION.SDK) > 8) {

							if (WebServiceReferences.CAMERA_ID == 1) {
								WebServiceReferences.CAMERA_ID = 0;
							} else if (WebServiceReferences.CAMERA_ID == 0) {
								WebServiceReferences.CAMERA_ID = 1;
							}

							if (pv != null) {
								pv.stopPreview();
								preview_frameLayout.removeView(pv);
								pv = null;
								AppMainActivity.commEngine.setVideoParameters(
										WebServiceReferences.VIDEO_WIDTH,
										WebServiceReferences.VIDEO_HEIGHT,
										WebServiceReferences.CAMERA_ID);
								pv = AppMainActivity.commEngine.getVideoPreview(context);

								preview_frameLayout.addView(pv, 0);
							}
						}
					}

				}
			});

			members.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
					Intent i = new Intent(AppReference.mainContext, CallActiveMembersList.class);
					i.putExtra("timer", chTimer.getText().toString());
					i.putExtra("calltype",currentcall_type);
					i.putExtra("sessionId", strSessionId);
					i.putExtra("host", host);
					i.putExtra("fromscreen","audiocallscreen");
					AppReference.mainContext.startActivity(i);

				}
			});

			btnMic.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        if (!micmute) {
                            micmute = true;
                            btnMic.setBackgroundResource(R.drawable.call_mic_active);

                        } else if (micmute) {
                            micmute = false;
                            btnMic.setBackgroundResource(R.drawable.call_mic);

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
                btnSpeaker.setBackgroundResource(R.drawable.call_speaker_active);

            } else {
                speaker = false;
                btnSpeaker.setBackgroundResource(R.drawable.call_speaker);
            }
			btnSpeaker.setVisibility(View.VISIBLE);
			audioProperties.setSpeakerphoneOn(speaker);
			btnSpeaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("thread", "################## speaker" + speaker);
					if (!speaker) {
						speaker = true;
						btnSpeaker.setBackgroundResource(R.drawable.call_speaker_active);
					} else if (speaker) {
						btnSpeaker.setBackgroundResource(R.drawable.call_speaker);
						speaker = false;

					}
					audioProperties.setSpeakerphoneOn(speaker);
				}
			});

			chTimer = (Chronometer) llayAudioCall.findViewById(R.id.call_timer);
			chTimer.setVisibility(View.VISIBLE);
//			if (isReceiver) {
                chTimer.start();
			AppMainActivity.ctimer.setBase(SystemClock.elapsedRealtime());
			AppMainActivity.ctimer.start();
                isTimer_running = true;
//            }
			chTimer.setOnChronometerTickListener(new OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer arg0) {

                    CharSequence text = chTimer.getText();
                    if (text.length() == 5) {
                        chTimer.setText( text);
                    } else if (text.length() == 7) {

                        chTimer.setText( text);
                    }

                }
            });
			btnHangup = (Button) llayAudioCall.findViewById(R.id.btn_han);
			btnHangup_video =(ImageView)llayAudioCall.findViewById(R.id.hang_video);

			btn_onlineb_video = (ImageView) llayAudioCall.findViewById(R.id.add_videousers);
			btnMic_video = (ImageView) llayAudioCall.findViewById(R.id.speaker_video);
			btnSpeaker_video = (ImageView) llayAudioCall.findViewById(R.id.loudspeaker_video);
			final ImageView videoEnableBtn = (ImageView) llayAudioCall.findViewById(R.id.promotecall_video);
			videoEnableBtn.setTag(true);

			btnMic_video.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    try {
                        if (!micmute) {
                            micmute = true;
                            btnMic_video.setImageResource(R.drawable.call_mic_active);

                        } else if (micmute) {
                            micmute = false;
                            btnMic_video.setImageResource(R.drawable.call_mic);

                        }

                        AppMainActivity.commEngine.micMute(micmute);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });

			btnSpeaker_video.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("thread", "################## speaker" + speaker);
                    if (!speaker) {
                        speaker = true;
                        btnSpeaker_video.setImageResource(R.drawable.call_speaker_active);
                    } else if (speaker) {
                        btnSpeaker_video.setImageResource(R.drawable.call_speaker);
                        speaker = false;

                    }
                    audioProperties.setSpeakerphoneOn(speaker);
                }
            });

			promote_call = (Button) llayAudioCall.findViewById(R.id.btn_video);
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
			numOfCam = Camera.getNumberOfCameras();

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

			btnHangup_video.setOnClickListener(new OnClickListener() {

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

			btn_onlineb_video.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ShowOnlineBuddies("VC");
                }
            });

			promote_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					try {
						video_layouts.setVisibility(View.VISIBLE);
						video_lay.setVisibility(View.VISIBLE);
						audio_button_layouts.setVisibility(View.GONE);
//						audio_host_layout.setVisibility(View.GONE);
//						audio_participant_layout.setVisibility(View.GONE);
//						initializevideoIntent();
						getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						currentcall_type = "VC";
						pv = AppMainActivity.commEngine.getVideoPreview(context);
						pv.setZOrderOnTop(false);
						preview_frameLayout.addView(pv);

						RecordTransactionBean transactionBean = new RecordTransactionBean();
						transactionBean.setSessionid(strSessionId);
						transactionBean.setHost(CallDispatcher.LoginUser);
						transactionBean.setParticipants(CallDispatcher.sb.getParticipants());
						transactionBean.setChatid(CallDispatcher.sb.getChatid());
						processCallRequest(2, transactionBean, "promote");

						for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {
							String user = CallDispatcher.conferenceMembers.get(i);
							onOffVideo(user, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});


			videoEnableBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shown = (boolean) videoEnableBtn.getTag();
                    AppMainActivity.commEngine.enable_disable_VideoPreview(!shown);

                    if(shown){

						videoEnableBtn.setImageResource(R.drawable.call_video);
						RecordTransactionBean transactionBean = new RecordTransactionBean();
						transactionBean.setSessionid(strSessionId);
						transactionBean.setHost(CallDispatcher.LoginUser);
						transactionBean.setParticipants("");
						transactionBean.setDisableVideo("yes");
						processCallRequest(2, transactionBean, "disablevideo");

                        ownimageview.setVisibility(View.VISIBLE);
                        preview_frameLayout.setVisibility(View.GONE);
                        boolean have_image = false;
                        for (BuddyInformationBean buddyInformationBean : buddyList) {
                            if (buddyInformationBean.getName().equalsIgnoreCase(objCallDispatcher.LoginUser)) {
                                String pic_path = buddyInformationBean.getProfile_picpath();
                                if (pic_path != null) {
                                    have_image = true;

                                    imageLoader.DisplayImage(pic_path, ownimageview, R.drawable.icon_buddy_aoffline);

                                }
                            }
                        }
                        if (!have_image) {
                            imageLoader.DisplayImage("", ownimageview, R.drawable.icon_buddy_aoffline);
                        }
                    } else {
						videoEnableBtn.setImageResource(R.drawable.call_video_active);

						RecordTransactionBean transactionBean = new RecordTransactionBean();
						transactionBean.setSessionid(strSessionId);
						transactionBean.setHost(CallDispatcher.LoginUser);
						transactionBean.setParticipants("");
						transactionBean.setDisableVideo("no");
						processCallRequest(2, transactionBean,"disablevideo");

                        ownimageview.setVisibility(View.GONE);
                        preview_frameLayout.setVisibility(View.VISIBLE);
                    }
                    videoEnableBtn.setTag(!shown);
                }
            });
			onoff_preview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					preview_hided = true;
//					own_video_layout.setVisibility(View.GONE);

					own_video_layout.getLayoutParams().height = 1;  // replace 100 with your dimensions
					own_video_layout.getLayoutParams().width = 1;
//						own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(1, 1, 1.0f));
//						own_video_layout.setVisibility(View.GONE);
//						videopreview.setVisibility(View.GONE);
					hideOwnVideo();
				}
			});

			on_off1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shown = (boolean) on_off1.getTag();

                    onOffVideoForSelectedUser(0, !shown);
//                    on_off1.setTag(!shown);
					if(preview_hided){
						hideOwnVideo();
					}
                }
            });
			on_off12.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					boolean shown = (boolean) on_off12.getTag();

					onOffVideoForSelectedUser(1, !shown);
//					on_off12.setTag(!shown);
					if(preview_hided){
						hideOwnVideo();
					}
				}
			});

			on_off2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shown = (boolean) on_off2.getTag();
                    onOffVideoForSelectedUser(1, !shown);
//                    on_off2.setTag(!shown);
					if(preview_hided){
						hideOwnVideo();
					}
                }
            });

			on_off3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shown = (boolean) on_off3.getTag();
                    onOffVideoForSelectedUser(2, !shown);
//                    on_off3.setTag(!shown);
					if(preview_hided){
						hideOwnVideo();
					}
                }
            });
			//For this used set Profile picture for owner and buddies
			//start
			buddyList= ContactsFragment.getBuddyList();
			imageLoader = new ImageLoader(context);
//			ownerimage= SingleInstance.mainContext.getOwnerProfilepicture();
//			iv_owner=(ImageView)llayAudioCall.findViewById(R.id.owner_icon);
//			imageLoader.DisplayImage(ownerimage, iv_owner, R.drawable.icon_buddy_aoffline);
//			iv_buddy=(ImageView)llayAudioCall.findViewById(R.id.buddy_icon);
//			for(BuddyInformationBean bean:buddyList){
//                if(bean.getName().equalsIgnoreCase(getNames())){
//                    buddyimage=bean.getProfile_picpath();
//                    break;
//                }
//            }
//			imageLoader.DisplayImage(buddyimage, iv_buddy, R.drawable.icon_buddy_aoffline);
			//end
		} catch (Resources.NotFoundException e) {
			e.printStackTrace();
		}

		return llayAudioCall;
	}

	public void getGLSurfaceView(Context context) {
		try {
			buddysurfaceview_01 = new GLSurfaceView(context);
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			buddysurfaceview_01.setRenderer(frameRenderer);
			frameSink = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void getGLSurfaceView1(Context context) {
		try {
			buddysurfaceview_0102 = new GLSurfaceView(context);
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			buddysurfaceview_0102.setRenderer(frameRenderer);
			frameSink12 = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer12 = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void getGLSurfaceView2(Context context) {
		try {
			buddysurfaceview_02 = new GLSurfaceView(context);
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			buddysurfaceview_02.setRenderer(frameRenderer);
			frameSink2 = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer2 = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void getGLSurfaceView3(Context context) {
		try {
			buddysurfaceview_03 = new GLSurfaceView(context);
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			buddysurfaceview_03.setRenderer(frameRenderer);
			frameSink3 = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer3 = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void assignOnOffIcons(Context context){

		ViewGroup.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		on_off1= new TextView(context);
		on_off1.setLayoutParams(layoutParams);
		on_off1.setBackgroundResource(R.drawable.call_close_video);

		on_off12= new TextView(context);
		on_off12.setLayoutParams(layoutParams);
		on_off12.setBackgroundResource(R.drawable.call_close_video);

		on_off2= new TextView(context);
		on_off2.setLayoutParams(layoutParams);
		on_off2.setBackgroundResource(R.drawable.call_close_video);

		on_off3= new TextView(context);
		on_off3.setLayoutParams(layoutParams);
		on_off3.setBackgroundResource(R.drawable.call_close_video);
	}


	public void onOffVideo(final String buddyVideo, final boolean addOrRemove) {
		Log.d("NotesVideo", "came to switchVideo " + buddyVideo);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {

//					for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {
//						String buddy = CallDispatcher.conferenceMembers.get(i);

					SignalingBean sb = CallDispatcher.buddySignall
							.get(buddyVideo);
					sb.setFrom(objCallDispatcher.LoginUser);
					sb.setTo(buddyVideo);
					sb.setType("9");
					if(addOrRemove) {
						sb.setGmember(buddyVideo);
					} else {
						sb.setGmember(objCallDispatcher.LoginUser);
					}
					sb.setCallType("VC");
					sb.setSessionid(strSessionId);
					AppMainActivity.commEngine.turnOnOffVideo(sb);
//					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}).start();

	}

	private void hideOwnVideo(){
		if(WebServiceReferences.videoSSRC_total_list.size() == 0) {
			own_video_layout.getLayoutParams().height = 1;
			own_video_layout.getLayoutParams().width = 1;
			own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(1, 1, 1.0f));
		}
	}

	private void onOffVideoForSelectedUser(final int selectedposition, final boolean onoff){
		if(WebServiceReferences.videoSSRC_total_list != null && WebServiceReferences.videoSSRC_total_list.size() > selectedposition){
			int turn_ssrc =	WebServiceReferences.videoSSRC_total_list.get(selectedposition);
			final int size = WebServiceReferences.videoSSRC_total_list.size();

			WebServiceReferences.removed_videoSSRC_list.add(turn_ssrc);
			WebServiceReferences.videoSSRC_total_list.remove(selectedposition);

			final String buddy_name = (WebServiceReferences.videoSSRC_total.get(turn_ssrc)).getMember_name();
			onOffVideo(buddy_name,onoff);
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (onoff) {
						if (selectedposition == 0) {

							buddyframelayout01.setVisibility(View.GONE);
							buddysurfaceview_01.setVisibility(View.GONE);

						} else if (selectedposition == 1) {
							if (size == 2) {
								buddyframelayout02.setVisibility(View.GONE);
								buddysurfaceview_02.setVisibility(View.GONE);
								buddyframelayout02.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
								own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
							} else {
								buddysurfaceview_0102.setVisibility(View.GONE);
								buddyframelayout0102.setVisibility(View.GONE);
								buddyframelayout0102.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
								own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
							}
						} else if (selectedposition == 2) {
							buddyframelayout03.setVisibility(View.GONE);
							buddysurfaceview_03.setVisibility(View.GONE);
						}
					} else {

						int changed_size = WebServiceReferences.videoSSRC_total_list.size();
						if (changed_size == 0) {
							bottom_videowindows.setVisibility(View.GONE);
							buddysurfaceview_01.setVisibility(View.GONE);
							buddyframelayout01.setVisibility(View.GONE);

							buddysurfaceview_0102.setVisibility(View.GONE);
							buddysurfaceview_02.setVisibility(View.GONE);
							buddysurfaceview_03.setVisibility(View.GONE);
							buddyframelayout0102.setVisibility(View.GONE);
							buddyframelayout02.setVisibility(View.GONE);
							buddyframelayout03.setVisibility(View.GONE);
							//							videosurface.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 1.5f));
							//							own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT, 1.5f));
						} else if (changed_size == 1) {
							buddysurfaceview_01.setVisibility(View.VISIBLE);
							bottom_videowindows.setVisibility(View.GONE);
							buddyframelayout01.setVisibility(View.VISIBLE);
							buddysurfaceview_0102.setVisibility(View.GONE);
							buddysurfaceview_02.setVisibility(View.GONE);
							buddysurfaceview_03.setVisibility(View.GONE);
							buddyframelayout0102.setVisibility(View.GONE);
							buddyframelayout02.setVisibility(View.GONE);
							buddyframelayout03.setVisibility(View.GONE);
							buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
							own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
						} else if (changed_size == 2) {
							buddysurfaceview_01.setVisibility(View.VISIBLE);
							bottom_videowindows.setVisibility(View.GONE);
							buddyframelayout01.setVisibility(View.VISIBLE);
							buddysurfaceview_0102.setVisibility(View.VISIBLE);
							buddysurfaceview_02.setVisibility(View.GONE);
							buddysurfaceview_03.setVisibility(View.GONE);
							buddyframelayout0102.setVisibility(View.VISIBLE);
							buddyframelayout02.setVisibility(View.GONE);
							buddyframelayout03.setVisibility(View.GONE);
							buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
							own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
						} else if (changed_size == 3) {
							buddysurfaceview_01.setVisibility(View.VISIBLE);
							buddysurfaceview_02.setVisibility(View.VISIBLE);
							buddysurfaceview_03.setVisibility(View.VISIBLE);
							buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
							own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
							buddyframelayout03.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
							buddysurfaceview_0102.setVisibility(View.GONE);
							bottom_videowindows.setVisibility(View.VISIBLE);
							buddyframelayout01.setVisibility(View.VISIBLE);
							buddyframelayout0102.setVisibility(View.GONE);
							buddyframelayout03.setVisibility(View.VISIBLE);
							buddyframelayout02.setVisibility(View.VISIBLE);
						}
					}
//						boolean have_image = false;
//						for (BuddyInformationBean buddyInformationBean : buddyList) {
//							if (buddyInformationBean.getName().equalsIgnoreCase(buddy_name)) {
//								String pic_path = buddyInformationBean.getProfile_picpath();
//								if (pic_path != null) {
//									have_image = true;
//									if(selectedposition == 0) {
//										imageLoader.DisplayImage(pic_path, buddyimageview1, R.drawable.icon_buddy_aoffline);
//									} else if(selectedposition == 1) {
//										imageLoader.DisplayImage(pic_path, buddyimageview2, R.drawable.icon_buddy_aoffline);
//									} else if(selectedposition == 2) {
//										imageLoader.DisplayImage(pic_path, buddyimageview3, R.drawable.icon_buddy_aoffline);
//									}
//								}
//							}
//						}
//						if(selectedposition == 0) {
//							if (!have_image) {
//								imageLoader.DisplayImage("", buddyimageview1, R.drawable.icon_buddy_aoffline);
//							}
//							buddyimageview1.setVisibility(View.VISIBLE);
//							buddysurfaceview_01.setVisibility(View.GONE);
//						} else if(selectedposition == 1) {
//							if (!have_image) {
//								imageLoader.DisplayImage("", buddyimageview2, R.drawable.icon_buddy_aoffline);
//							}
//							buddyimageview2.setVisibility(View.VISIBLE);
//							buddysurfaceview_02.setVisibility(View.GONE);
//						} else if(selectedposition == 2) {
//							if (!have_image) {
//								imageLoader.DisplayImage("", buddyimageview3, R.drawable.icon_buddy_aoffline);
//							}
//							buddyimageview3.setVisibility(View.VISIBLE);
//							buddysurfaceview_03.setVisibility(View.GONE);
//						}
//					}
				}
			});
		}

	}

	@Override
	public void notifyDecodedVideoCallback(final byte[] data, final long ssrc) {
		Log.i("NotesVideo", "came to notifyDecodedVideoCallback AudioCallScreen : ssrc :" + ssrc);

		try {

			if (videoQueue.getSize() < 1) {

					handler.post(new Runnable() {

						@Override
						public void run() {

							try {
								member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size()+1));
								if (WebServiceReferences.videoSSRC_total_list.size() > 0) {
									int size = WebServiceReferences.videoSSRC_total_list.size();
									if (size == 1) {
										bottom_videowindows.setVisibility(View.GONE);
										buddyframelayout01.setVisibility(View.VISIBLE);
										buddyframelayout0102.setVisibility(View.GONE);
										buddyframelayout02.setVisibility(View.GONE);
										buddyframelayout03.setVisibility(View.GONE);
										buddysurfaceview_0102.setVisibility(View.GONE);
//										buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//										own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
										if(preview_hided) {
											own_video_layout.getLayoutParams().height = 1;
											own_video_layout.getLayoutParams().width = 1;
											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.0f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f));
										} else {
											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//
										}
									} else if (size == 2) {
										bottom_videowindows.setVisibility(View.GONE);
										buddyframelayout01.setVisibility(View.VISIBLE);
										buddyframelayout0102.setVisibility(View.VISIBLE);
										buddyframelayout02.setVisibility(View.VISIBLE);
										buddyframelayout03.setVisibility(View.GONE);
										buddysurfaceview_0102.setVisibility(View.VISIBLE);
//										buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
//										own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
										if(preview_hided ) {
											own_video_layout.getLayoutParams().height = 1;  // replace 100 with your dimensions
											own_video_layout.getLayoutParams().width = 1;

											buddyframelayout0102.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.75f));
											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.75f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
										} else {
											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
											buddyframelayout0102.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
										}
									} else if (size == 3) {
										bottom_videowindows.setVisibility(View.VISIBLE);
										buddyframelayout01.setVisibility(View.VISIBLE);
										buddyframelayout0102.setVisibility(View.GONE);
										buddyframelayout02.setVisibility(View.VISIBLE);
										buddyframelayout03.setVisibility(View.VISIBLE);
										buddysurfaceview_0102.setVisibility(View.GONE);
//										buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//										own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
										if(preview_hided ) {
											own_video_layout.getLayoutParams().height = 1;  // replace 100 with your dimensions
											own_video_layout.getLayoutParams().width = 1;

											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.0f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3.0f));
										} else {
											buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
											own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
										}
									}
//									if(on_off1.getTag()==false) {
//										buddyframelayout01.setVisibility(View.GONE);
//										buddysurfaceview_0102.setVisibility(View.GONE);
//										buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//										own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//									}if(on_off2.getTag()==false) {
//										buddyframelayout02.setVisibility(View.GONE);
//										buddysurfaceview_02.setVisibility(View.GONE);
//									}if(on_off3.getTag()==false) {
//										buddyframelayout03.setVisibility(View.GONE);
//										buddysurfaceview_03.setVisibility(View.GONE);
//									}if(on_off12.getTag()==false) {
//										buddyframelayout0102.setVisibility(View.GONE);
//										buddysurfaceview_0102.setVisibility(View.GONE);
//										buddyframelayout01.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//										own_video_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
//									}
									String mem_name = "";
									if (WebServiceReferences.videoSSRC_total.containsKey((int) (long) ssrc)) {
//										mem_name = WebServiceReferences.videoSSRC_total.get((int) (long) ssrc);
									}
									if (WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 0) {

//										participant_name1.setText(mem_name);
//										if (buddyTimerTask1 != null) {
//											Log.i("OnOff", "VideoOnOffTimerTask != null : " + ssrc);
//											buddyTimerTask1.cancel();
//											buddyTimerTask1 = null;
//											if (buddytimer1 != null) {
//												buddytimer1.cancel();
//												buddytimer1 = null;
//											}
//										}
									}
									if (WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 1) {
//										participant_name2.setText(mem_name);
									}
									if (WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 2) {
//										participant_name3.setText(mem_name);
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					});

				int size = WebServiceReferences.videoSSRC_total_list.size();

				VideoThreadBean videoThreadBean = new VideoThreadBean();
					videoThreadBean.setData(data);
					if ((size == 2) && (WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 1)) {
						videoThreadBean.setWindow(12);
					} else {
						videoThreadBean.setWindow(WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc));
					}
					if(WebServiceReferences.videoSSRC_total.containsKey((int) (long) ssrc)){
						videoThreadBean.setMember_name((WebServiceReferences.videoSSRC_total.get((int) (long) ssrc)).getMember_name());
					} else {
						videoThreadBean.setMember_name("");
					}
					videoThreadBean.setVideoSssrc((int) (long) ssrc);
					videoQueue.addMsg(videoThreadBean);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}



	@Override
	public void notifyResolution(int w, int h) {

	}

	public void notifyCallvIDEOPromotionRequest(SignalingBean vidsignBean){
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					if(getActivity() != null)
						getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

					currentcall_type = "VC";
					video_layouts.setVisibility(View.VISIBLE);
					video_lay.setVisibility(View.VISIBLE);
					audio_button_layouts.setVisibility(View.GONE);
//					audio_host_layout.setVisibility(View.GONE);
//					audio_participant_layout.setVisibility(View.GONE);
//					initializevideoIntent();
					pv = AppMainActivity.commEngine.getVideoPreview(context);
					pv.setZOrderOnTop(false);
					preview_frameLayout.addView(pv);

					for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {
                        String user = CallDispatcher.conferenceMembers.get(i);
                        onOffVideo(user,true);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void notifyVideoStoped(final SignalingBean vidsignBean){
		if(vidsignBean != null && WebServiceReferences.videoSSRC_total != null && WebServiceReferences.videoSSRC_total.size()>0){

			int requiredKey = 0;
			Set set = WebServiceReferences.videoSSRC_total.entrySet();
			Iterator i = set.iterator();

			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				Log.i("Join", " key :" + me.getKey() + ": ");
				Log.i("Join", " value :" + me.getValue());
				VideoThreadBean value = (VideoThreadBean) me.getValue();
				Log.i("Join", " name :" + value.getMember_name()+ " vidsignBean.getFrom()  : "+vidsignBean.getFrom());
				if(vidsignBean.getFrom().equalsIgnoreCase(value.getMember_name())){
					requiredKey = (int) me.getKey();
				}
			}

			if(requiredKey != 0){
				VideoThreadBean threadBean = WebServiceReferences.videoSSRC_total.get(requiredKey);
				threadBean.setVideoDisabled(true);

				if(WebServiceReferences.videoSSRC_total_list != null && WebServiceReferences.videoSSRC_total_list.contains(requiredKey)){
					final int selectedposition =	WebServiceReferences.videoSSRC_total_list.indexOf(requiredKey);
//					final String buddy_name = (WebServiceReferences.videoSSRC_total.get(turn_ssrc)).getMember_name();
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (vidsignBean.getVideoStoped() == null || vidsignBean.getVideoStoped().equalsIgnoreCase("no")) {
								if(selectedposition == 0) {
									buddyimageview1.setVisibility(View.GONE);
									buddysurfaceview_01.setVisibility(View.VISIBLE);

								} else if(selectedposition == 1) {
									buddyimageview2.setVisibility(View.GONE);
									buddysurfaceview_02.setVisibility(View.VISIBLE);
								} else if(selectedposition == 2) {
									buddyimageview3.setVisibility(View.GONE);
									buddysurfaceview_03.setVisibility(View.VISIBLE);
								}
							} else {
								boolean have_image = false;
								for (BuddyInformationBean buddyInformationBean : buddyList) {
									if (buddyInformationBean.getName().equalsIgnoreCase(vidsignBean.getFrom())) {
										String pic_path = buddyInformationBean.getProfile_picpath();

										if (pic_path == null || pic_path.length() == 0) {
											ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(vidsignBean.getFrom());
											if(pBean != null) {
												pic_path = pBean.getPhoto();
												Log.i("Join","pic_path 2 : "+pic_path);
											}
										}
										if (pic_path != null && pic_path.length() > 0) {
											if (!pic_path.contains("COMMedia")) {
												pic_path = Environment
														.getExternalStorageDirectory()
														+ "/COMMedia/" + pic_path;
											}
											have_image = true;
											if(selectedposition == 0) {
												imageLoader.DisplayImage(pic_path, buddyimageview1, R.drawable.icon_buddy_aoffline);
											} else if(selectedposition == 1) {
												imageLoader.DisplayImage(pic_path, buddyimageview2, R.drawable.icon_buddy_aoffline);
											} else if(selectedposition == 2) {
												imageLoader.DisplayImage(pic_path, buddyimageview3, R.drawable.icon_buddy_aoffline);
											}
										}
									}
								}
								if(selectedposition == 0) {
									if (!have_image) {
										imageLoader.DisplayImage("", buddyimageview1, R.drawable.icon_buddy_aoffline);
									}
									buddyimageview1.setVisibility(View.VISIBLE);
									buddysurfaceview_01.setVisibility(View.GONE);
								} else if(selectedposition == 1) {
									if (!have_image) {
										imageLoader.DisplayImage("", buddyimageview2, R.drawable.icon_buddy_aoffline);
									}
									buddyimageview2.setVisibility(View.VISIBLE);
									buddysurfaceview_02.setVisibility(View.GONE);
								} else if(selectedposition == 2) {
									if (!have_image) {
										imageLoader.DisplayImage("", buddyimageview3, R.drawable.icon_buddy_aoffline);
									}
									buddyimageview3.setVisibility(View.VISIBLE);
									buddysurfaceview_03.setVisibility(View.GONE);
								}
							}
						}
					});
				}
			}
		}

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

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
//								finish();
								finishAudiocallScreen();
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
//		finish();
		finishAudiocallScreen();

	}

	// make the AlertDialog.Builder as private and make it null before Create
	// it...
	void showHangUpAlert() {
		handler.post(new Runnable() {
			@Override
			public void run() {


				AlertDialog.Builder builder = new AlertDialog.Builder(AppReference.mainContext);
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
											Message msg = new Message();
											Bundle bun = new Bundle();
											bun.putString("action", "leave");
											msg.obj = bun;
											selfHangup = true;
											if (selfHangup) {

                                                Log.i("AudioCall","StartTime :"+CallDispatcher.sb
														.getStartTime());
                                                CallDispatcher.sb
														.setEndTime(objCallDispatcher.getCurrentDateandTime());
                                                CallDispatcher.sb
                                                        .setCallDuration(SingleInstance.mainContext
																.getCallDuration(CallDispatcher.sb
																				.getStartTime(),
																		CallDispatcher.sb
																				.getEndTime()));
                                                CallDispatcher.sb.setCallstatus("callattended");

                                                //For Callhistory host and participant name entry
                                                //Start
                                                CallDispatcher.sb.setHost_name(host);
                                                String participant=null;
                                                if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
                                                    for(String name:CallDispatcher.conferenceMembers){
                                                        if(!name.equalsIgnoreCase(host)){
                                                            if(participant==null){
                                                                participant=name;
                                                            }else{
                                                                participant=participant+","+name;
                                                            }

                                                        }
                                                    }
                                                }
                                                if(participant!=null){
                                                    CallDispatcher.sb.setParticipant_name(participant);
                                                }
                                                //end

                                                DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
                                                DBAccess.getdbHeler()
                                                        .saveOrUpdateRecordtransactiondetails(
																CallDispatcher.sb);

                                                showCallHistory();


                                            }
											final String[] choiceList = returnBuddies();
											if (choiceList.length != 0) {
                                                isBuddyinCall = true;
                                                selfHangup = false;
                                            }
											handler.sendMessage(msg);
										} catch (Exception e) {
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

			}
		});

	}


	public void hangupCallFromCallActiveMembers(){
		try {
			Message msg = new Message();
			Bundle bun = new Bundle();
			bun.putString("action", "leave");
			msg.obj = bun;
			selfHangup = true;
			if (selfHangup) {
                CallDispatcher.sb
						.setEndTime(objCallDispatcher.getCurrentDateandTime());
                CallDispatcher.sb
                        .setCallDuration(SingleInstance.mainContext
								.getCallDuration(CallDispatcher.sb
												.getStartTime(),
										CallDispatcher.sb
												.getEndTime()));
                CallDispatcher.sb.setCallstatus("callattended");

				//For Callhistory host and participant name entry
				//Start
				CallDispatcher.sb.setHost_name(host);
				String participant=null;
				if(CallDispatcher.conferenceMembers!=null && CallDispatcher.conferenceMembers.size()>0){
					for(String name:CallDispatcher.conferenceMembers){
						if(!name.equalsIgnoreCase(host)){
							if(participant==null){
								participant=name;
							}else{
								participant=participant+","+name;
							}

						}
					}
				}
				if(participant!=null){
					CallDispatcher.sb.setParticipant_name(participant);
				}
				//end

                DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
                DBAccess.getdbHeler()
                        .saveOrUpdateRecordtransactiondetails(
								CallDispatcher.sb);

                showCallHistory();


            }
			final String[] choiceList = returnBuddies();
			if (choiceList.length != 0) {
                isBuddyinCall = true;
                selfHangup = false;
            }
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyGSMCallAccepted() {
		Message msg = new Message();
		Bundle bun = new Bundle();
		bun.putString("action", "leave");
		msg.obj = bun;
		handler.sendMessage(msg);
	}

	public void receiveHangUpx(final SignalingBean sb) {
		// Log.e("test", "Call Rejected @#@##@#@#      6");
		if (handler != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (CallDispatcher.conferenceMembers.size() == 1) {

							Log.i("callscreenfinish", "2 conferenceMembers.size()==1 name-->" + CallDispatcher.conferenceMembers.get(0));
							Log.i("callscreenfinish", "2 sb.name-->" + sb.getFrom());
							if (CallDispatcher.conferenceMembers.get(0).equalsIgnoreCase(sb.getFrom())) {
								Log.i("callscreenfinish", "2.1 sb.name-->" + sb.getFrom());
								CallDispatcher.isCallInitiate = false;
								chTimer.stop();
								AppMainActivity.ctimer.stop();
								audio_minimize.setVisibility(View.GONE);

								if (!isHangedUp) {


									enterCallHistory();

								}

								CallDispatcher.currentSessionid = null;
								CallDispatcher.conferenceMembers.clear();
								finishAudiocallScreen();
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

//					finish();

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
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
			ArrayList<String> memberslist = new ArrayList<String>();
			for (int i = 0; i < members.length; i++) {
				if (!CallDispatcher.conferenceMembers.contains(members[i])) {
					memberslist.add(members[i]);
				}
			}
			calltype=callType;

			if (memberslist.size() > 0) {
				Intent intent = new Intent(context,
						AddGroupMembers.class);
				intent.putExtra("fromcall",true);
				intent.putExtra("calltype",callType);
				intent.putStringArrayListExtra("buddylist", memberslist);
				startActivityForResult(intent, 3);
//				Intent i = new Intent(getActivity(), CallAddMemberslist.class);
//				i.putStringArrayListExtra("list", membersList);
//				startActivity(i);

//				AlertDialog.Builder builder = new AlertDialog.Builder(AppReference.mainContext);
//				builder.create();
//				builder.setTitle(SingleInstance.mainContext.getResources()
//						.getString(R.string.add_people));
//				final String[] choiceList = membersList
//						.toArray(new String[membersList.size()]);
//				for (int i = 0; i < choiceList.length; i++)
//					// Log.i("buddy", "Name :" + choiceList[i]);
//
//					builder.setItems(choiceList,
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//

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
				if(WebServiceReferences.videoSSRC_total != null && WebServiceReferences.videoSSRC_total.size() > 0){

//					for ( int key : WebServiceReferences.videoSSRC_total.entrySet() ) {
//						Log.i("Join"," key : "+ key );
//						if(from.equalsIgnoreCase(key))
//					}

					Set set = WebServiceReferences.videoSSRC_total.entrySet();
					Iterator i = set.iterator();
					// Display elements
					while(i.hasNext()) {
						Map.Entry me = (Map.Entry)i.next();
						Log.i("Join", " key :" + me.getKey() + ": ");
						Log.i("Join", " value :" + me.getValue());
						VideoThreadBean value = (VideoThreadBean) me.getValue();
						if(strBuddyName.equalsIgnoreCase(value.getMember_name())){
							if(WebServiceReferences.videoSSRC_total_list.contains(me.getKey())){
								WebServiceReferences.videoSSRC_total_list.remove(me.getKey());
							}
						}
					}
				}
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
	public void onAttach(Activity arg0) {
		super.onAttach(arg0);
//		activityOrientation = arg0.getRequestedOrientation();

//		arg0.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	// Have to remove the references associated with the Activity and also
	// Activity...
	// Also have to remove the Handler references too...
	public void onDestroy() {
		// HomeTabViewScreen home =null;
		try {
			Log.d("AudioCall", "----->callscreenactivity destroy<-----");
			Activity parent = getActivity();
			if(parent != null) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}
//			if (SingleInstance.instanceTable.containsKey("callscreen")) {
//				SingleInstance.instanceTable.remove("callscreen");
//			}
//			if (handler != null)
//				handler.removeCallbacks(runnable);

			mainHeader.setVisibility(View.VISIBLE);
			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);



		} catch (Exception e) {
			e.printStackTrace();

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

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//
//			if (alert == null) {
//
//				showHangUpAlert();
//
//			} else if (!alert.isShowing()) {
//				showHangUpAlert();
//			}
//
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}

	private void makeEmergencyCall(final String callType, ArrayList<String> list) {

		final String[] choiceList = list.toArray(new String[list.size()]);

		try {
			if (choiceList != null && choiceList.length > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
								R.string.no_near_contacts), Toast.LENGTH_SHORT).show();
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
						AppMainActivity.ctimer.setBase(SystemClock.elapsedRealtime());
						AppMainActivity.ctimer.start();
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

	public void finishAudiocallScreen()
	{
		try {
			Log.i("AudioCall","Came to finishAudiocallScreen in AudioCallScreen");
			if (SingleInstance.instanceTable.containsKey("callactivememberslist")) {
				CallActiveMembersList activeMembersList = (CallActiveMembersList)SingleInstance.instanceTable.get("callactivememberslist");
				activeMembersList.finishActivity();
			}

			preview_hided = false;
			currentcall_type = "AC";
			objCallDispatcher.stopRingTone();
			CallDispatcher.currentSessionid = null;
			objCallDispatcher.alConferenceRequest.clear();
			CallDispatcher.conferenceMembersTime.clear();
			WebServiceReferences.videoSSRC_total.clear();
			WebServiceReferences.videoSSRC_total_list.clear();
			WebServiceReferences.removed_videoSSRC_list.clear();
			CallDispatcher.conConference.clear();
			CallDispatcher.issecMadeConference = false;
			objCallDispatcher.whenCallHangedUp();

			if(videoQueue != null){
                videoQueue.clear();
            }
			if (pv != null) {
                pv.stopPreview();
            }

			if (SingleInstance.instanceTable.containsKey("callscreen")) {
                SingleInstance.instanceTable.remove("callscreen");
                Log.e("note", "Call screen instance removed ACS!!");
            }

			objCallDispatcher.startPlayer(context);
			if (WebServiceReferences.contextTable.containsKey("audiocall")) {
                WebServiceReferences.contextTable.remove("audiocall");
            }
			try {
                objCallDispatcher.isHangUpReceived = false;
                if (audioProperties != null) {
                    audioProperties.setSpeakerphoneOn(false);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }


			if (handler != null) {
				handler.removeCallbacks(runnable);
			}

			Activity parent = getActivity();
			if(parent != null) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}
			rootView=null;
			CallDispatcher.isCallInitiate = false;
			FragmentManager fm =
                    AppReference.mainContext.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
//			ContactsFragment contactsFragment = ContactsFragment
//                    .getInstance(context);
			ft.replace(R.id.activity_main_content_fragment,
					AppReference.bacgroundFragment);
			ft.commitAllowingStateLoss();
			audio_minimize.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			// check if the request code is same as what is passed here it is 2
			if (requestCode == 3) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					ArrayList<UserBean> list = (ArrayList<UserBean>) bundle
							.get("list");
					String calltype=bundle.getString("calltype");
					HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
					for (UserBean userBean : membersList) {
						membersMap.put(userBean.getBuddyName(), userBean);
					}
					for (UserBean userBean : list) {
						if (!membersMap.containsKey(userBean.getBuddyName())) {
							membersList.add(userBean);
						}
					}
					for(UserBean bib:membersList){
						if (CallDispatcher.conferenceMembers.size() < 3) {

							member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size()+1));

							if (objCallDispatcher != null) {
								SignalingBean sb = objCallDispatcher.callconfernceUpdate(
										bib.getBuddyName(),
										calltype, strSessionId);
								// june04-Implementation
								CallDispatcher.conferenceRequest
										.put(bib.getBuddyName(), sb);
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
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	String file = "";
	private void showCallHistory()
	{
		objCallDispatcher.showCallHistory(strSessionId , calltype);
//		try {
//			final Dialog dialog = new Dialog(SingleInstance.mainContext);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.setContentView(R.layout.call_record_dialog);
//			dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//			dialog.getWindow().setBackgroundDrawableResource(R.color.black2);
//			dialog.show();
//			Button save = (Button) dialog.findViewById(R.id.save);
//			Button delete = (Button) dialog.findViewById(R.id.delete);
//			final ImageView play_button = (ImageView) dialog.findViewById(R.id.play_button);
//			final SeekBar seekBar1 = (SeekBar) dialog.findViewById(R.id.seekBar1);
//			final TextView txt_time= (TextView)dialog.findViewById(R.id.txt_time);
//			play_button.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					file = Environment
//							.getExternalStorageDirectory()
//							+ "/COMMedia/CallRecording/"
//							+ strSessionId + ".wav";
//					Log.d("Stringpath", "mediapath--->" + file);
//					File newfile=new File(file);
//
//					if (mPlayer.isPlaying()) {
//						mPlayer.pause();
//						play_button.setBackgroundResource(R.drawable.play);
//					} else {
//						play_button.setBackgroundResource(R.drawable.audiopause);
//						if(newfile.exists())
//						playAudio(file, 0);
//
//					}
//					if(newfile.exists()){
//
//		if (position == mPlayingPosition) {
//			mProgressUpdater.mBarToUpdate = seekBar1;
//			mProgressUpdater.tvToUpdate = txt_time;
//			mHandler.postDelayed(mProgressUpdater, 100);
//		} else {
//
//			try {
//				Log.d("Stringpath", "mediapath--->");
//				seekBar1.setProgress(0);
//				MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//				mmr.setDataSource(file);
//				String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//				mmr.release();
//				String min, sec;
//				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
//				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
//				if (Integer.parseInt(min) < 10) {
//					min = 0 + String.valueOf(min);
//				}
//				if (Integer.parseInt(sec) < 10) {
//					sec = 0 + String.valueOf(sec);
//				}
//				txt_time.setText(min + ":" + sec);
////                            audio_tv.setText(duration);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			seekBar1.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//			seekBar1.setProgress(0);
//			if (mProgressUpdater.mBarToUpdate == seekBar1) {
//				//this progress would be updated, but this is the wrong position
//				mProgressUpdater.mBarToUpdate = null;
//			}
//		}
//		}
//				}
//
//			});
//
//
//
//			save.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					Intent intentComponent = new Intent(context,
//							CallHistoryActivity.class);
//					intentComponent.putExtra("buddyname",
//							CallDispatcher.sb.getFrom());
//					intentComponent.putExtra("individual", true);
//					intentComponent.putExtra("isDelete", false);
//					intentComponent.putExtra("sessionid",
//							CallDispatcher.sb.getSessionid());
//					context.startActivity(intentComponent);
//					mPlayer.stop();
//				}
//			});
//			delete.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					Intent intentComponent = new Intent(context,
//							CallHistoryActivity.class);
//					intentComponent.putExtra("buddyname",
//							CallDispatcher.sb.getFrom());
//					intentComponent.putExtra("isDelete", true);
//					intentComponent.putExtra("individual", true);
//					intentComponent.putExtra("sessionid",
//							CallDispatcher.sb.getSessionid());
//					context.startActivity(intentComponent);
//					mPlayer.stop();
//				}
//			});
//		}catch (Exception e){
//			e.printStackTrace();
//		}

	}
	private class PlaybackUpdater implements Runnable {
		public SeekBar mBarToUpdate = null;
		public TextView tvToUpdate = null;

		@Override
		public void run() {
			if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
				Log.d("Mposition","seekbar---->");
				double tElapsed = mPlayer.getCurrentPosition();
				int fTime = mPlayer.getDuration();
				double timeRemaining = fTime - tElapsed;
				double sTime = mPlayer.getCurrentPosition();

				String min, sec;
				//for decreasing
//                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
//                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

				//for increasing
				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
				if (Integer.parseInt(min) < 10) {
					min = 0 + String.valueOf(min);
				}
				if (Integer.parseInt(sec) < 10) {
					sec = 0 + String.valueOf(sec);
				}
				tvToUpdate.setText(min + ":" + sec);
				mBarToUpdate.setProgress((100 * mPlayer.getCurrentPosition() / mPlayer.getDuration()));
//                tvToUpdate.setText(String.format("%d:%d ",TimeUnit.MILLISECONDS.toMinutes((long) fTime),TimeUnit.MILLISECONDS.toSeconds((long) fTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) fTime))));
				mHandler.postDelayed(this, 500);

			} else {
				//not playing so stop updating
			}
		}
	}

	private void stopPlayback() {
		mPlayingPosition = 0;
		mProgressUpdater.mBarToUpdate = null;
		mProgressUpdater.tvToUpdate = null;
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.stop();
	}

	public void playAudio(String fname,  int position) {
		try {
			mPlayer.reset();
			mPlayer.setDataSource(fname);
			mPlayer.prepare();
			mPlayer.start();
			mPlayingPosition = position;

			mHandler.postDelayed(mProgressUpdater, 500);

		} catch (IOException e) {

			e.printStackTrace();
			stopPlayback();
		}
	}



	private Runnable UpdateSongTime = new Runnable() {
		public void run() {
			startTime = mPlayer.getCurrentPosition();
//            seekBar.setProgress((int) startTime);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					startPlayProgressUpdater();

				}
			}, 100);

			history_handler.postDelayed(this, 100);
		}
	};


	private void startPlayProgressUpdater() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				Log.d("lg", "play progress().....");
				long milliseconds = mPlayer.getCurrentPosition();
				timeElapsed = mPlayer.getCurrentPosition();

				double timeRemaining = finalTime - timeElapsed;
				String min, sec;
				min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining));
				sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)));

				if (Integer.parseInt(min) < 10) {
					min = 0 + String.valueOf(min);
				}
				if (Integer.parseInt(sec) < 10) {
					sec = 0 + String.valueOf(sec);
				}
//                txt_time.setText(min + ":" + sec);
			}
		}
	}
	void addShowHideListener() {
		Log.i("Minimise", "AudioCallScreen minimise");
		FragmentManager fm =
				AppReference.mainContext.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ContactsFragment contactsFragment = ContactsFragment
//				.getInstance(context);
		ft.replace(R.id.activity_main_content_fragment,
				AppReference.bacgroundFragment);
		ft.commitAllowingStateLoss();
		audio_minimize.setVisibility(View.VISIBLE);
		mainHeader.setVisibility(View.VISIBLE);
	}

	public void processCallRequest(int caseid, RecordTransactionBean record_TransactionBean, String feature) {
		try {
//			String user = "amuthan2";
			for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {
				String user = CallDispatcher.conferenceMembers.get(i);
				String state = null;
				BuddyInformationBean oldBuddyBean = null;

				int con_scr_opened = 0;

				if (user != null && !user.equalsIgnoreCase(CallDispatcher.LoginUser)) {
					Log.i("Join", " call initiate user : " + user);

					for (BuddyInformationBean temp : buddyList) {
						if (!temp.isTitle()) {
							if (temp.getName().equalsIgnoreCase(
									user)) {
								oldBuddyBean = temp;
								state = oldBuddyBean.getStatus();
								break;
							}
						}
					}


					Log.d("Join", "call status--->" + state);

//					if (state != null && state.equalsIgnoreCase("Offline")
//							|| state.equals("Stealth")
//							|| state.equalsIgnoreCase("pending")
//							|| state.equalsIgnoreCase("Virtual")
//							|| state.equalsIgnoreCase("airport")) {
//						if (WebServiceReferences.running) {
//							CallDispatcher.pdialog = new ProgressDialog(context);
//							objCallDispatcher.showprogress(CallDispatcher.pdialog, context);
//
//							String[] res_info = new String[3];
//							res_info[0] = CallDispatcher.LoginUser;
//							res_info[1] = user;
//							if (state.equals("Offline") || state.equals("Stealth"))
//								res_info[2] = objCallDispatcher
//										.getdbHeler(context)
//										.getwheninfo(
//												"select cid from clonemaster where cdescription='Offline'");
//							else
//								res_info[2] = "";
//
//							WebServiceReferences.webServiceClient
//									.OfflineCallResponse(res_info);
//						}
//
//					} else {
//						if (state != null && !state.equalsIgnoreCase("pending")) {
//							SingleInstance.parentId = record_TransactionBean.getParentID();
							objCallDispatcher.MakeCallFromCallHistory(caseid,
									user, context, record_TransactionBean, 2, feature);
							con_scr_opened = con_scr_opened + 1;
//						}
//
//					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

