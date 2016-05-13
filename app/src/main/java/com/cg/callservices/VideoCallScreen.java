package com.cg.callservices;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import org.audio.AudioProperties;
import org.core.VideoCallback;
import org.lib.model.SignalingBean;
import org.util.GraphicsUtil;
import org.util.Queue;
import org.video.Preview;
import org.video.PreviewFrameSink;
import org.video.VideoFrameRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.util.SingleInstance;

public class VideoCallScreen extends Activity implements VideoCallback,
		AnimationListener {

	// ImageView[] view = null;
	private boolean isShowingMediaFailureIcon = false;
	Timer timer = null;
	Handler handler = null;
	LinearLayout linearLayout;
	RelativeLayout relativeLayout;
	int orientation = 0;
	int i = 0;
	boolean isreject = false;

	int c1 = 0;
	private String strCurrentScreen = null;
	private boolean isHangedUp = false;

	String[] touserarray;
	String[] autocallarray;
	String[] starttimearray;
	String[] endtimearray;

	boolean isSwitched = false;
	boolean ismediaNotified = false;
	boolean mRenderFrame = false;
	public String failedUser = null;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	String record_path = null;

	AlertDialog alert = null;
	private String sessionid = null;
	private String receiver = null;
	private Handler videocall = null;
	private boolean hasRemoved = false;
	Preview pv = null;
	FrameLayout videopreview = null;
	static Context context = null;
	boolean mReceiveVideo = false;
	// private AudioProperties audioProperties=null;
	RelativeLayout layout;
	//
	int obtainWidth = 0;
	int obtainHeight = 0;
	String callTypeForServer = "201";

	// test
	private CallDispatcher objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
			.get("calldispatch");

	GLSurfaceView gls = null;
	private static GLSurfaceView glSurfaceView = null;
	private VideoFrameRenderer frameRenderer = null;
	private PreviewFrameSink frameSink = null;
	private byte[] frame = null;
	private ByteBuffer frameBuffer = null;

	// public ImageAdapter ad = null;
	private String buddyName;
	private Queue videoQueue;
	private VideoThread videoThread;

	public Handler videoHandler = new Handler();

	private ArrayList<String> strALCallMembers = new ArrayList<String>();

	private ArrayList<String> hsCallMembers = new ArrayList<String>();

	private String strCallStartTime = "";

	private FrameLayout videosurface;

	private AudioProperties audioProperties = null;
	private boolean speaker = false;
	private boolean micmute = false;
	private String strStartTime;
	private Button mic;
	private Button loudSpeaker;
	// TextView tvTitle;
	String tvTitlename = null;
	FrameLayout.LayoutParams layoutParamsf;
	String conferenceWindowTitle = "Conference People";
	String title = conferenceWindowTitle;
	// RenderBitmap renderBitmap = null;
	private KeyguardManager keyguardManager;
	private KeyguardLock lock;

	private PowerManager.WakeLock mWakeLock;

	private boolean selfHangup = false;

	// String acStatus=null;

	/**
	 * Called when the activity is starting. Also used to Initialize the members
	 * of the class. Show the videoCall view and their controls.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			context = this;
			WebServiceReferences.contextTable.put("callscreen", this);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int nosHeight = displaymetrics.heightPixels;
			int nosWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				objCallDispatcher = new CallDispatcher(context);

			objCallDispatcher.setNoScrHeight(nosHeight);
			objCallDispatcher.setNoScrWidth(nosWidth);
			displaymetrics = null;
			objCallDispatcher.startPlayer(this);

			CallDispatcher.networkState = objCallDispatcher.connectivityType();

			keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
			lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
			lock.disableKeyguard();

			CallDispatcher.networkState = objCallDispatcher.connectivityType();
			handler = new Handler();

			// //////////////
			// Make the Message msg null after processing it
			// Make the Bundle bun null after processing it

			videoHandler = new Handler() {

				public void handleMessage(Message result) {
					// Log.d("one", "videocall");
					super.handleMessage(result);
					Bundle bundle = (Bundle) result.obj;
					try {
						if (bundle.containsKey("newbuddy")) {
							if (!strALCallMembers.contains(bundle
									.getString("newbuddy"))) {
								hsCallMembers.add(bundle.getString("newbuddy"));
								strALCallMembers.add(bundle
										.getString("newbuddy"));
							}
							// if (ad != null) {
							// Log.e("cover", "339");
							// ad.notifyDataSetChanged();
							// }
							// switchVideo(bundle.getString("newbuddy"));

						} else if (bundle.containsKey("newrequest")) {
							SignalingBean bean = (SignalingBean) bundle
									.getSerializable("newrequest");

						} else if (bundle.containsKey("leave")) {
							// if (CallDispatcher.sb.getBs_parentid() != null) {
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
							
							// }
							// Log.d("test", "From leave/bye");
							for (int i = 0; i < CallDispatcher.conferenceMembers
									.size(); i++) {
								// Log.d("login", "login");

								String buddy = CallDispatcher.conferenceMembers
										.get(i);
								// Log.d("test", "members " + buddy);
								SignalingBean sb = CallDispatcher.buddySignall
										.get(buddy);
								/* Static Clean up */
								// Log.d("login", "loginuser"
								// + objCallDispatcher.LoginUser);
								sb.setFrom(objCallDispatcher.LoginUser);
								sb.setTo(buddy);
								sb.setType("3");
								sb.setCallType("VC");
								sb.getVideossrc();
								AppMainActivity.commEngine.hangupCall(sb);
							}

							/*
							 * members.clear();
							 */

							// Log.e("callInfo",
							// "Video call Screen Leave received");
							WebServiceReferences.contextTable
									.remove("VideoCall");
							// finish();
							objCallDispatcher.currentSessionid = null;
							/* Static Clean up */
							objCallDispatcher.LoginUser = null;
							CallDispatcher.conferenceMembers.clear();

						}

						else if (bundle.containsKey("hangup")) {
							// Log.i("callInfo",
							// "One person is disconnect his call....");
							CallDispatcher.conferenceMembers.remove(bundle
									.getString("hangup"));
							/*
							 * videoConferenceMembers.remove(0);
							 */
							// if (ad != null) {
							// Log.e("cover", "391");
							// ad.notifyDataSetChanged();
							// }
						} else if (bundle.containsKey("bye")) {
							// Log.d("test", "VC bye ");

							boolean isConferenceMembers = CallDispatcher.conferenceMembers
									.contains(bundle.getString("bye"));

							Log.d("test", "Size "
									+ CallDispatcher.conferenceMembers.size());
							Log.d("test", "removed " + bundle.getString("bye"));
							if (CallDispatcher.conferenceMembers
									.contains(bundle.getString("bye"))) {

								Log.d("test", "removed");
								CallDispatcher.conferenceMembers.remove(bundle
										.getString("bye"));
							}

							// Log.d("", "removed user");

							// Log.d("test",
							// "CallDispatcher.conferenceMembers.size()  "
							// + CallDispatcher.conferenceMembers.size());

							if (CallDispatcher.conferenceMembers.size() == 0
									&& CallDispatcher.conferenceRequest.size() == 0) {

								receivedHangUp();

							}

							if (CallDispatcher.conferenceMembers.size() > 0) {
								switchVideo(CallDispatcher.conferenceMembers
										.get(0));
								if (bundle.getString("bye").equals(
										tvTitlename.trim())) {
									tvTitlename = CallDispatcher.conferenceMembers
											.get(0);

								}
								// used to send Remove status to server...

								if (isConferenceMembers) {
									enterCallHistoryOnSingleuser(bundle
											.getString("bye"));
								} else {
									objCallDispatcher
											.notifyCallHistoryToServer(
													objCallDispatcher.LoginUser,
													bundle.getString("bye"),
													"211",
													objCallDispatcher.sessId,
													getCurrentDateTime(),
													getCurrentDateTime());
								}

								if (alert != null && alert.isShowing()) {
									alert.dismiss();
								}

							} else if (CallDispatcher.conferenceRequest.size() > 0) {
								//
								enterCallHistoryOnSingleuser(bundle
										.getString("bye"));

							}

							// System.out
							// .println("notifyDataSetChanged.....................");
						} else if (bundle.containsKey("img")) {

							// Log.d("video",
							// "******************* From img *******************");
							frameSink.getFrameLock().lock();
							frameBuffer.position(0);
							// frameRenderer.setPreviewFrameWidth(obtainWidth);
							// frameRenderer.setPreviewFrameHeight(obtainHeight);

							frameBuffer.put(bundle.getByteArray("img"));
							frameBuffer.position(0);
							frameSink.setNextFrame(frameBuffer);
							frameSink.getFrameLock().unlock();
							glSurfaceView.requestRender();

						} else if (bundle.containsKey("video")) {
							// Log.d("video", "videopre");

						} else if (bundle.containsKey("hangupfullscreen")) {

							// Log.d("hang", "hangup full Screen");

							isHangedUp = true;
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
								Log.d("Test","Inside Videocallscreen SelfHangUp@@@@@");
							}
							try {
								for (int i = 0; i < CallDispatcher.conferenceMembers
										.size(); i++) {
									// Log.d("login", "login");

									String buddy = CallDispatcher.conferenceMembers
											.get(i);
									// Log.d("hang", "Buddy " + i + ": " +
									// buddy);
									SignalingBean sb = CallDispatcher.buddySignall
											.get(buddy);
									// Log.d("hang", "loginuser" +
									// objCallDispatcher.LoginUser);
									sb.setFrom(objCallDispatcher.LoginUser);
									sb.setTo(buddy);
									sb.setType("3");
									sb.setCallType("VC");
									AppMainActivity.commEngine.hangupCall(sb);
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {

								if (CallDispatcher.conferenceRequest.size() > 0) {
									try {
										Set<String> set = CallDispatcher.conferenceRequest
												.keySet();

										int i = 0;
										Iterator<String> iterator = set
												.iterator();
										while (iterator.hasNext()) {

											String buddy = (String) iterator
													.next();
											SignalingBean sb = CallDispatcher.conferenceRequest
													.get(buddy);
											// Log.d("hang", "loginuser"
											// + objCallDispatcher.LoginUser);
											sb.setFrom(objCallDispatcher.LoginUser);
											sb.setTo(buddy);
											sb.setType("3");
											sb.setCallType("VC");
											AppMainActivity.commEngine
													.hangupCall(sb);

										}
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

							/*
							 * members.clear();
							 */

							// Log.e("test",
							// "Comes to message received hangupfullscreen");
							WebServiceReferences.contextTable
									.remove("VideoCall");

							CallDispatcher.buddySignall.clear();
							CallDispatcher.conferenceMembers.clear();
							if (CallDispatcher.conferenceRequest.size() > 0) {
								enterMissedCall();
							}

							CallDispatcher.conferenceRequest.clear();

							objCallDispatcher.currentSessionid = null;

							// Log.d("hang", "Hang up from the call");
							setDataForOnActivityResult();

							try {

								objCallDispatcher.currentSessionid = null;
								CallDispatcher.conferenceMembers.clear();
								// 28july

								// if (WebServiceReferences.contextTable
								// .containsKey("callscreen")) {
								// WebServiceReferences.contextTable
								// .remove("callscreen");
								// }
								CallDispatcher.videoScreenVisibleState = false;
								CallDispatcher.isCallInProgress = false;

								/*
								 * String duration = ValueHandler
								 * .diffBetweenTwoTimes(strStartTime,
								 * ValueHandler .getCurrentDateTime());
								 */
								/* Static Clean up */
								// HomeTabViewScreen home = (HomeTabViewScreen)
								// WebServiceReferences.contextTable
								// .get("tabhome");
								// home.PutDatabaseEntry(getConnectedMembersName(),
								// objCallDispatcher.LoginUser, strStartTime,
								// duration, "Video Chat", sessionid,
								// strStartTime, getCurrentDateTime());
								//
								// home.notifyCallHistoryToServer(objCallDispatcher.LoginUser,
								// getConnectedMembersName(), "Video Chat",
								// sessionid, strStartTime,
								// getCurrentDateTime());
								enterCallHistory();

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							//
							try {
								if (pv != null) {
									pv.stopPreview();
									pv = null;
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							//
							clearResources();
							finish();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			};

			strStartTime = getCurrentDateTime();
			// audioProperties=new AudioProperties(this);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(20, 0, 0, 15);

			context = this;
			WebServiceReferences.contextTable.put("VideoCallscreen", context);

			if (savedInstanceState == null) {

				sessionid = (String) getIntent().getStringExtra("sessionid");

				receiver = (String) getIntent().getStringExtra("receive");

				buddyName = (String) getIntent().getStringExtra("buddyName");
			} else {
				sessionid = (String) savedInstanceState.getString("sessionid");

				receiver = (String) savedInstanceState.getString("receive");

				buddyName = (String) savedInstanceState.getString("buddyName");

			}

			tvTitlename = buddyName;

			// tvTitle.setText(buddyName);

			hsCallMembers.add(buddyName);

			// Log.e("test", "************ Buddy Name :" + buddyName);

			strALCallMembers.add(buddyName);

			strCallStartTime = objCallDispatcher.getCurrentDateTime();

			audioProperties = new AudioProperties(this);
			LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout callControls = (RelativeLayout) inflateLayout
					.inflate(R.layout.videocall, null);
			videopreview = (FrameLayout) callControls
					.findViewById(R.id.VideoView01);
			videosurface = (FrameLayout) callControls
					.findViewById(R.id.VideoView02);

			LinearLayout llCallControl = (LinearLayout) callControls
					.findViewById(R.id.LinearLayout01);

			CallDispatcher.videoScreenVisibleState = true;
			ScrollView llControlss = new ScrollView(context);
			llControlss.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			llControlss.setVerticalScrollBarEnabled(false);
			LinearLayout llControls = new LinearLayout(context);
			llControls.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			llControls.setOrientation(LinearLayout.VERTICAL);

			Button add = new Button(this);
			add.setKeepScreenOn(true);
			// add.setText("ADD");

			add.setBackgroundResource(R.drawable.add_icon_video);
			add.setPadding(0, 20, 0, 0);
			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {

						if (CallDispatcher.conferenceMembers.size() < 3) {
							if (alert == null) {

								ShowOnlineBuddies("VC");
							} else if (!alert.isShowing()) {
								ShowOnlineBuddies("VC");
							}

						} else {

							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources()
											.getString(
													R.string.max_conf_members),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});
			Button hangup = new Button(context);

			hangup.setBackgroundResource(R.drawable.hang);

			hangup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (alert == null) {

						showHangUpAlert();
					} else if (!alert.isShowing()) {
						showHangUpAlert();
					}

				}
			});

			loudSpeaker = new Button(this);

			loudSpeaker.setBackgroundResource(R.drawable.headphone_video);
			mic = new Button(this);

			// mic.setBackgroundDrawable(micUnmuteState());
			mic.setBackgroundResource(R.drawable.mic_video);
			speaker = false;

			if (SingleInstance.mainContext.isAutoAcceptEnabled(
					CallDispatcher.LoginUser, buddyName)) {
				speaker = true;
				loudSpeaker.setBackgroundResource(R.drawable.loudspeaker_video);

			} else {
				speaker = false;
				loudSpeaker.setBackgroundResource(R.drawable.headphone_video);
			}
			audioProperties.setSpeakerphoneOn(speaker);
			loudSpeaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						/*
						 * if (!speaker) { speaker = true; //
						 * loudSpeaker.setHint("off"); loudSpeaker
						 * .setBackgroundResource(R.drawable.headphonee); } else
						 * if (speaker) {
						 * loudSpeaker.setBackgroundResource(R.drawable
						 * .loudspeakerr); speaker = false; //
						 * loudSpeaker.setHint("on"); }
						 * audioProperties.setSpeakerphoneOn(speaker);
						 */

						if (!speaker) {
							speaker = true;
							// loudSpeaker.setHint("off");
							loudSpeaker
									.setBackgroundResource(R.drawable.loudspeaker_video);
						} else if (speaker) {
							loudSpeaker
									.setBackgroundResource(R.drawable.headphone_video);
							speaker = false;
							// loudSpeaker.setHint("on");
						}
						audioProperties.setSpeakerphoneOn(speaker);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});

			mic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (micmute) {
							micmute = false;
							// mic.setBackgroundResource(R.drawable.mic_unmute);
							mic.setBackgroundResource(R.drawable.mic_video);
						} else if (!micmute) {
							micmute = true;
							// mic.setBackgroundResource(R.drawable.mic_mute);
							mic.setBackgroundResource(R.drawable.mic_mutex_video);
						}

						AppMainActivity.commEngine.micMute(micmute);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});

			final Button buddies = new Button(this);

			// buddies.setHint("off");
			buddies.setBackgroundResource(R.drawable.buddies_video);
			buddies.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// if (alertForConnectedBuddies == null) {
					// Log.d("Add", "call show buddies on null");
					try {

						// if (alert == null) {
						showConnectedBuddies();
						// } else if (!alert.isShowing()) {
						// showConnectedBuddies();
						// }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});
			Button btn_loc = new Button(this);
			btn_loc.setBackgroundResource(R.drawable.loc_pin);

			if (!receiver.equalsIgnoreCase("true")) {
				btn_loc.setVisibility(View.GONE);
			}

			btn_loc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						Log.i("thread", "on click of btn_loc");
						ArrayList<String> close_buddies = objCallDispatcher
								.calculateNearestLocations(buddyName.trim());

						if (CallDispatcher.conferenceMembers.size() < 3) {
							if (alert == null) {

								makeEmergencyCall("VC", close_buddies);
							} else if (!alert.isShowing()) {
								makeEmergencyCall("VC", close_buddies);
							}

						} else {

							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources()
											.getString(
													R.string.max_conf_members),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}
			});

			llControls.addView(add, layoutParams);
			llControls.addView(mic, layoutParams);
			llControls.addView(loudSpeaker, layoutParams);
			llControls.addView(hangup, layoutParams);
			llControls.addView(buddies, layoutParams);
			llControls.addView(btn_loc, layoutParams);
			llControlss.addView(llControls);

			RelativeLayout rlForAlignment = new RelativeLayout(this);
			rlForAlignment.setLayoutParams(new LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT));

			RelativeLayout.LayoutParams lpAlignCenter = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lpAlignCenter.addRule(RelativeLayout.CENTER_IN_PARENT);

			rlForAlignment.addView(llControlss, lpAlignCenter);

			llCallControl.addView(rlForAlignment);
			// 2222222222

			final LinearLayout showBuddies = new LinearLayout(this);
			showBuddies.setBackgroundColor(Color.TRANSPARENT);
			/* Code Clean up */
			showBuddies.setOrientation(LinearLayout.VERTICAL);
			showBuddies.setGravity(Gravity.CENTER);
			showBuddies.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, 50));
			MyAbsoluteLayout mal = new MyAbsoluteLayout(this);

			TextView tv = new TextView(context);
			tv.setText("Test");
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);
			// showBuddies.addView(tv);
			showBuddies.setVisibility(View.VISIBLE);
			// showBuddies.setVisibility(View.INVISIBLE);

			videoQueue = new Queue();

			AppMainActivity.commEngine.setVideoCallback(this);
			gls = getGLSurfaceView(context);

			pv = AppMainActivity.commEngine.getVideoPreview(this);

			pv.setZOrderOnTop(true);

			videopreview.addView(pv);
			// ......

			glSurfaceView.setZOrderOnTop(false);

			double c = nosHeight * 1.1;
			c1 = (int) c;

			int percentage;
			int remSizeforButton;

			if (nosHeight > nosWidth) {

				remSizeforButton = nosHeight - (144 + c1);
				percentage = (int) (nosHeight * 0.15);

			} else {
				remSizeforButton = nosWidth - (144 + c1);
				percentage = (int) (nosWidth * 0.15);
			}

			if (percentage > remSizeforButton) {
				llControls.setVisibility(View.GONE);
			}

			// Log.d("NN", "height width " + c1);
			layoutParamsf = new FrameLayout.LayoutParams(c1,
					LayoutParams.FILL_PARENT);
			layout = getCallConnectingView();

			// layout.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

			// videosurface.addView(glSurfaceView, layoutParamsf);
			videosurface.addView(layout, layoutParamsf);

			AnimationThread animationThread = new AnimationThread(
					VideoCallScreen.this);
			timer.schedule(animationThread, 500, 500);

			// videosurface.addView(layout, layoutParamsf);

			final Runnable runnable = new Runnable() {

				@Override
				public void run() {

					// showBuddies.setVisibility(View.INVISIBLE);
				}
			};

			glSurfaceView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						showBuddies.bringToFront();

						showBuddies.postDelayed(runnable, 5000);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					return false;
				}
			});

			videocall = new Handler() {
				public void handleMessage(Message result) {
					super.handleMessage(result);
					/* Code Clean up */
					// Bundle bundle = (Bundle) result.obj;

				}
			};
			mal.addView(callControls, new MyAbsoluteLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0, 0));

			mal.addView(showBuddies, new MyAbsoluteLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0,
					nosHeight - 200));

			videoThread = new VideoThread(videoQueue);
			videoThread.setHandler(videoHandler);
			videoThread.start();

			setContentView(mal);

			final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
					"My Tag");
			this.mWakeLock.acquire();

			//
			videoHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						if (pv != null) {
							Log.d("video", "Preview width---->" + pv.getWidth());
							Log.d("video",
									"Preview height---->" + pv.getHeight());

							if (!pv.getPreviewStatus()) {
								// Log.d("CAM", "close call");
								Toast.makeText(
										context,
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.unable_send_video),
										Toast.LENGTH_LONG).show();
								//
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putString("hangupfullscreen",
										"hangupfullscreen");
								message.obj = bundle;
								// Log.e("h_bye", "570");
								videoHandler.sendMessage(message);
								//
							} else {
								// Log.d("CAM", "Donot call");
							}
						} else {
							// Log.d("CAM", "preview is null");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}, 5000);

			// Race case
			// Log.d("RACE", " Race CAse.. on Activity "
			// + objCallDispatcher.isHangUpReceived);
			// Log.d("RACE", " Race CAse.. on Activity "
			// + CallDispatcher.conferenceMembers.size());

			if (objCallDispatcher.isHangUpReceived) {
				receivedHangUp();
				// finish();

			} else if (receiver.equalsIgnoreCase("true")
					&& CallDispatcher.conferenceMembers.size() == 0) {
				// Log.d("RACE", " Race CAse.. new concept Execcuted,.,....");
				receivedHangUp();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// }
		//

	}

	public String getCurrentDateandTime() {
		try {
			// Calendar c = Calendar.getInstance();
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
			// String strDate = sdf.format(c.getTime());

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss aa");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void enterCallHistory() {

		try {

			if (failedUser != null) {
				callTypeForServer = "221";
			}

			if (receiver.equalsIgnoreCase("true")) {
				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						buddyName, touserarray, callTypeForServer, sessionid,
						starttimearray, endtimearray, autocallarray);

			}

			else {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						objCallDispatcher.LoginUser, touserarray,
						callTypeForServer, sessionid, starttimearray,
						endtimearray, autocallarray);

			}

		} catch (Exception e) {

		}
		failedUser = null;
	}

	private void enterCallHistoryOnSingleuser(String buddy) {
		hsCallMembers.remove(buddy);
		// String ac="0";

		String[] toarray = new String[1];
		toarray[0] = buddy;
		String[] starttimearray = new String[1];
		String[] endtimearray = new String[1];
		endtimearray[0] = getCurrentDateTime();
		String[] autocallarray = new String[1];
		autocallarray[0] = "0";

		String calltypee = "201";
		if (failedUser != null) {
			if (failedUser.equals(buddy)) {
				calltypee = "221";
			}
		}
		if (objCallDispatcher.alConferenceRequest.contains(buddy)) {
			autocallarray[0] = "1";
			objCallDispatcher.alConferenceRequest.remove(buddy);
		}

		try {

			starttimearray[0] = CallDispatcher.conferenceMembersTime.get(buddy);
			if (starttimearray[0] == null) {
				starttimearray[0] = strStartTime;
			}

			if (receiver.equalsIgnoreCase("true")) {
				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						buddyName, toarray, calltypee, sessionid,
						starttimearray, endtimearray, autocallarray);

			} else {

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						objCallDispatcher.LoginUser, toarray, calltypee,
						sessionid, starttimearray, endtimearray, autocallarray);

			}

		} catch (Exception e) {

		}
		failedUser = null;

	}

	/*
	 * Enter call history for call requested members
	 */
	private void enterMissedCall() {
		try {

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
				timearray[0] = getCurrentDateTime();
				String[] autocallarray = new String[1];
				autocallarray[0] = "0";

				objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
						objCallDispatcher.LoginUser, toarray, "211", sessionid,
						timearray, timearray, autocallarray);

			}

			//

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void resetCallDuration() {
		// Log.d("RCD", "call duraion reseted");
		strStartTime = getCurrentDateTime();

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

	// Used to save Activity State...
	protected void onSaveInstanceState(Bundle outState) {
		// outState.putString("test", "value");
		// Used to save data on Bundle..
		if (outState == null) {
			outState = new Bundle();
		}
		// Bundle bundle = new Bundle();
		outState.putString("sessionid", sessionid);
		outState.putString("buddyName", buddyName);
		outState.putString("receive", receiver);

		super.onSaveInstanceState(outState);
	}

	/**
	 * Clear Resources on Receive HAng Up
	 */

	public void receiveHAngUpx() {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub

						WebServiceReferences.contextTable.remove("VideoCall");
						// Log.e("hang",
						// "Comes to message bye received in Video call Screen");
						objCallDispatcher.currentSessionid = null;
						if (CallDispatcher.videoScreenVisibleState) {
							//

							setDataForOnActivityResult();
							try {

								CallDispatcher.currentSessionid = null;
								CallDispatcher.conferenceMembers.clear();

								CallDispatcher.videoScreenVisibleState = false;
								CallDispatcher.isCallInProgress = false;

								enterCallHistory();

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							try {
								if (pv != null) {
									pv.stopPreview();
									pv = null;
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							clearResources();
							finish();

							//
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// Handle Race CAse
	private void receivedHangUp() {
		// Log.d("RACE", "Handling Race CAse..######## ");

		//
		if (WebServiceReferences.contextTable.containsKey("VideoCall")) {
			WebServiceReferences.contextTable.remove("VideoCall");
		}
		try {

			CallDispatcher.buddySignall.clear();
			CallDispatcher.conferenceMembers.clear();

			objCallDispatcher.currentSessionid = null;
			objCallDispatcher.isIncomingAlert = false;

			// Log.d("hang", "Hang up from the call");
			setDataForOnActivityResult();

			CallDispatcher.videoScreenVisibleState = false;
			CallDispatcher.isCallInProgress = false;

			if (!isHangedUp) {
				enterCallHistory();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//
		try {
			if (pv != null) {
				pv.stopPreview();
				pv = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		//
		objCallDispatcher.isHangUpReceived = false;
		clearResources();
		finish();

	}

	public void updateBuddyNames(final String buddyName) {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {

					try {
						// tvTitle.setText(buddyName);
                        CallDispatcher.sb.setStartTime(getCurrentDateandTime());
						tvTitlename = buddyName;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	void showHangUpAlert() {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String ask = SingleInstance.mainContext.getResources().getString(
					R.string.need_call_hangup);

			builder.setMessage(ask)
					.setCancelable(false)
					.setPositiveButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										selfHangup = true;
										Message message = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("hangupfullscreen",
												"hangupfullscreen");
										message.obj = bundle;
										// Log.e("h_bye", "");
										videoHandler.sendMessage(message);
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}

								}
							})
					.setNegativeButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			alert = builder.create();

			alert.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

		//
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

	//
	public void notifyHeadsetPlugin(final boolean is) {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {

					//
					try {
						if (is) {

							audioProperties.setSpeakerphoneOn(false);
							// loudSpeaker.setBackgroundResource(R.drawable.headset);

						} else {

							// btnSpeaker
							// .setBackgroundResource(R.drawable.speaker_mute);
							// audioProperties.setSpeakerphoneOn(true);

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					//

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void clearResources() {
		try {
			objCallDispatcher.currentSessionid = null;
			objCallDispatcher.isHangUpReceived = false;
			CallDispatcher.conferenceMembers.clear();
			CallDispatcher.isCallInProgress = false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//

	public void forceHAngUp() {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						callTypeForServer = "231";
						Message message = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("hangupfullscreen", "hangupfullscreen");
						message.obj = bundle;
						// Log.e("h_bye", "570");
						videoHandler.sendMessage(message);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private String getCurrentDateTime() {
		Date curDate = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(curDate);
	}

	/**
	 * Show the list of Online Buddies to add them on the conference.Alert view
	 * is used to show the Buddy List.
	 * 
	 * @param callType
	 *            VideoCall.
	 */
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
				Log.d("SIZE", "Size of the OnLine Buddies " + choiceList.length);

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SignalingBean sb = objCallDispatcher
										.callconfernceUpdate(
												choiceList[which].toString(),
												callType, sessionid);

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
								R.string.sorry_no_online_users), 2000).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Used to select the Particular user To view the Users Videos on the
	 * Screen.(Connected Users).
	 * 
	 * @param buddyVideo
	 *            BuddyName.
	 */
	public void switchVideo(final String buddyVideo) {
		// Log.d("name", "one " + " two " + buddyVideo);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {

					for (int i = 0; i < CallDispatcher.conferenceMembers.size(); i++) {
						String buddy = CallDispatcher.conferenceMembers.get(i);

						// Log.d("nBuddy", "one " + buddy + " two " +
						// buddyVideo);
						SignalingBean sb = CallDispatcher.buddySignall
								.get(buddy);
						sb.setFrom(objCallDispatcher.LoginUser);
						sb.setTo(buddy);
						sb.setType("9");
						sb.setGmember(buddyVideo);
						sb.setCallType("VC");
						sb.setSessionid(sessionid);
						AppMainActivity.commEngine.makeCall(sb);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}).start();

	}

	/**
	 * Called when a key was pressed down and not handled by any of the views
	 * inside of the activity. Also used to remove the hangUp the call and Close
	 * the Window.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			if (alert == null) {

				showHangUpAlert();

			} else if (!alert.isShowing()) {
				showHangUpAlert();
			}

			// showHangUpAlert();

		}
		return super.onKeyDown(keyCode, event);

	}

	public void notifyGSMCallAccepted() {
		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("hangupfullscreen", "hangupfullscreen");
		message.obj = bundle;
		// Log.e("h_bye", "");
		videoHandler.sendMessage(message);
	}

	/**
	 * Called when you are no longer visible to the user. Also used to remove
	 * the preview and GLSurface View from the Screen.
	 */
	protected void onStop() {
		// TODO Auto-generated method stub
		// Log.d("video", "videostop");
		//
		hasRemoved = true;
		mRenderFrame = false;
		super.onStop();
		// WebServiceReferences.contextTable.remove("")
	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.videooptions, menu);
	// return true;
	// }
	//
	// public boolean onOptionsItemSelected(MenuItem item) {
	// try{
	// switch (item.getItemId()) {
	// case R.id.add:
	// try{
	// // Used to limit the conference members on Conference...
	// // Log.d("CM", "Conference members "
	// // + CallDispatcher.conferenceMembers.size());
	// if (CallDispatcher.conferenceMembers.size() < 3) {
	// if (alert == null) {
	//
	// ShowOnlineBuddies("VC");
	// } else if (!alert.isShowing()) {
	// ShowOnlineBuddies("VC");
	// }
	//
	// } else {
	//
	// Toast.makeText(context,
	// "You can add only 3 contacts to a conference",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	//
	//
	// break;
	// case R.id.hang:
	//
	// if (alert == null) {
	//
	// showHangUpAlert();
	// } else if (!alert.isShowing()) {
	// showHangUpAlert();
	// }
	//
	//
	//
	//
	//
	// break;
	// case R.id.mic:
	// try{
	// if (micmute) {
	// micmute = false;
	// // mic.setBackgroundResource(R.drawable.mic_unmute);
	// mic.setBackgroundDrawable(micUnmuteState());
	// } else if (!micmute) {
	// micmute = true;
	// // mic.setBackgroundResource(R.drawable.mic_mute);
	// mic.setBackgroundDrawable(micMuteState());
	// }
	//
	// CallDispatcher.commEngine.micMute(micmute);
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// break;
	// case R.id.loud:
	//
	// try{
	// if (!speaker) {
	// speaker = true;
	// // loudSpeaker.setHint("off");
	// loudSpeaker
	// .setBackgroundResource(R.drawable.speaker_unmute);
	// } else if (speaker) {
	// loudSpeaker.setBackgroundResource(R.drawable.headset);
	// speaker = false;
	// // loudSpeaker.setHint("on");
	// }
	// audioProperties.setSpeakerphoneOn(speaker);
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	//
	// break;
	// case R.id.switchuser:
	// try{
	//
	// if (alert == null) {
	// showConnectedBuddies();
	// } else if (!alert.isShowing()) {
	// showConnectedBuddies();
	// }
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	//
	// break;
	//
	// }
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * Perform any final cleanup before an activity is destroyed.
	 * 
	 */
	protected void onDestroy() {

		// RenderBitmap.count=0;
		// HomeTabViewScreen home =null;
		try {
			CallDispatcher.conConference.clear();
			CallDispatcher.issecMadeConference = false;
			CallDispatcher.isCallInitiate = false;

			if (this.mWakeLock != null && this.mWakeLock.isHeld())
				this.mWakeLock.release();
			lock.reenableKeyguard();

			objCallDispatcher.startPlayer(context);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			objCallDispatcher.stopRingTone();
			objCallDispatcher.isHangUpReceived = false;
			objCallDispatcher.alConferenceRequest.clear();
			CallDispatcher.conferenceMembersTime.clear();
			CallDispatcher.currentSessionid = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {
			if (WebServiceReferences.contextTable.containsKey("connection")) {
				CallConnectingScreen screen = (CallConnectingScreen) WebServiceReferences.contextTable
						.get("connection");
				screen.finish();
			}

			if (WebServiceReferences.contextTable.containsKey("callscreen")) {
				WebServiceReferences.contextTable.remove("callscreen");
			}
			CallDispatcher.videoScreenVisibleState = false;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			if (pv != null) {
				pv.stopPreview();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		if (audioProperties != null) {
			audioProperties.setSpeakerphoneOn(false);
		}

		if (videoThread != null) {
			if (Build.VERSION.SDK_INT < 10) {
				videoThread.stop();
			} else {
				videoThread.stopVideoThread();
			}

		}
		if (videoQueue != null) {
			videoQueue.clear();
		}

		super.onDestroy();
	}

	public GLSurfaceView getGLSurfaceView(Context context) {
		glSurfaceView = new GLSurfaceView(context);
		try {
			frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			glSurfaceView.setRenderer(frameRenderer);
			frameSink = frameRenderer;
			frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return glSurfaceView;
	}

	private void resumeGlSurface() {
		try {
			frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			glSurfaceView.setRenderer(frameRenderer);
			frameSink = frameRenderer;
			frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void notifyDecodedVideoCallback(byte[] data, long ssrc) {
		// Log.d("NDVC", "Video Received mreceive "
		// +mReceiveVideo+" isswitched "+isSwitched+" mRenderFrame "+mRenderFrame);

		// System.out.println("VIDEO");
		try {
			if (mReceiveVideo) {

			} else {

				videocall.post(new Runnable() {

					@Override
					public void run() {
						try {
							// Log.d("NDVC1",
							// "started videocall.post(new Runnable()mReceiveVideo else");
							if (timer != null) {
								timer.cancel();
							}
							videosurface.removeView(layout);
							isShowingMediaFailureIcon = false;
							videosurface.addView(glSurfaceView, layoutParamsf);
							ismediaNotified = false;
							// Log.d("NDVC1", "started completed");
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				});

				mReceiveVideo = true;
				isSwitched = false;
			}

			if (isSwitched) {
				// Log.d("GL", "On changing viewgggggggggggg");
				videocall.post(new Runnable() {

					@Override
					public void run() {

						// Log.d("NDVC1",
						// "started videocall.post(new Runnable() on isSwitched");

						if (timer != null) {
							timer.cancel();
						}
						try {
							// if(layout.)
							// {
							videosurface.removeView(layout);
							isShowingMediaFailureIcon = false;
							getGLSurfaceView(context);
							videosurface.addView(glSurfaceView, layoutParamsf);
							ismediaNotified = false;
							// Log.d("NDVC1", "started completed");
							// }
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				});

				isSwitched = false;
			}

			if (videoQueue.getSize() < 1) {
				if (mRenderFrame) {
					videoQueue.addMsg(data);
				} else {

					// System.out.println("SKIPPED");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void setDataForOnActivityResult() {
		try {
			// Intent intent = new Intent();
			//
			// String members = "";
			// for (int i = 0; i < strALCallMembers.size(); i++) {
			// members += strALCallMembers.get(i) + ",";
			// }
			// strALCallMembers.clear();
			// String duration = objCallDispatcher.diffBetweenTwoTimes(
			// strCallStartTime, objCallDispatcher.getCurrentDateTime());
			//
			// Bundle bunCallDetail = new Bundle();
			// bunCallDetail.putString("screen", "full");
			// bunCallDetail.putString("members", members);
			// bunCallDetail.putString("duration", duration);
			// intent.putExtra("returnedData", bunCallDetail);
			// setResult(RESULT_OK, intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private String[] returnBuddies() {

		// System.out.println("buddies :" + CallDispatcher.conferenceMembers);
		String arr[] = CallDispatcher.conferenceMembers
				.toArray(new String[CallDispatcher.conferenceMembers.size()]);
		String connected_buddies = "";
		for (String names : arr) {
			if (!tvTitlename.trim().equals(names)) {
				if (connected_buddies.trim().length() == 0)
					connected_buddies = names;
				else
					connected_buddies = connected_buddies + "," + names;
			}
		}
		String[] buddies = null;
		if (connected_buddies.length() > 0) {
			buddies = connected_buddies.split(",");
		}
		return buddies;
	}

	private void showConnectedBuddies() {
		try {

			final CharSequence[] choiceList = returnBuddies();
			if (choiceList != null && choiceList.length > 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.create();
				// builder.setTitle("Conference People");
				builder.setTitle(title);

				// System.out.println(choiceList);

				int selected = -1; // does not select anything

				builder.setItems(choiceList,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// System.out.println("choiceList[which] :"
								// + choiceList[which]);

								// Log.d("NDVC1", "Video on Switch");
								if (!tvTitlename.trim().equals(
										choiceList[which].toString().trim())) {
									Log.d("NDVC1", "on selected ....");

									switchVideo(choiceList[which].toString());
									tvTitlename = choiceList[which].toString();
									// New Implement
									// Log.d("NDVC1",
									// "Video on Switch "+ismediaNotified);
									try {
										if (!ismediaNotified) {
											isSwitched = true;
											videosurface
													.removeView(glSurfaceView);
											layout = getCallConnectingView();
											if (timer != null) {
												// timer.schedule(;, when)
												AnimationThread animationThread = new AnimationThread(
														VideoCallScreen.this);
												timer.schedule(animationThread,
														500, 500);
											}

											// getGLSurfaceView(context);
											if (layout != null) {
												// Log.d("NDVC1",
												// "Video on Switch not null if");
												videosurface.addView(layout,
														layoutParamsf);
											}
										} else {
											// isSwitched=true;
											videosurface.removeView(layout);
											isShowingMediaFailureIcon = false;
											layout = getCallConnectingView();
											if (timer != null) {
												// timer.schedule(;, when)
												AnimationThread animationThread = new AnimationThread(
														VideoCallScreen.this);
												timer.schedule(animationThread,
														500, 500);
											}
											// getGLSurfaceView(context);
											if (layout != null) {
												videosurface.addView(layout,
														layoutParamsf);
												// Log.d("NDVC1",
												// "Video on Switch not null else");
											}

										}
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();

										// Log.d("EXC",
										// "Exception on switch Video");

									}

									//

								}
								alert.dismiss();

							}
						});
				alert = builder.create();
				if (choiceList.length > 0) {
					alert.show();
				}

				else {
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(R.string.no_active_participants),
							Toast.LENGTH_LONG).show();
				}

				alert.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// isaddPeopleOpened = false;
					}
				});

				alert.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						// isaddPeopleOpened = false;

					}
				});
			} else
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_active_participants),
						Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exc", "On Show OnLine buddy Exception:" + e.getMessage());
		}

	}

	// s
	String[] removeCurrentDisplayUser(String userToRemove, String[] arr) {
		boolean[] deleteItem = new boolean[arr.length];
		int size = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(userToRemove)) {
				deleteItem[i] = true;
			} else {
				deleteItem[i] = false;
				size++;
			}
		}
		String[] newArr = new String[size];
		int index = 0;
		for (int i = 0; i < arr.length; i++) {
			if (!deleteItem[i]) {
				newArr[index++] = arr[i];
			}
		}

		return newArr;

	}

	// AlertDialog alertForConnectedBuddies = null;

	public void UpdateConferenceMembers(String from, boolean b) {
		try {
			// Log.d("test", "From " + from + " b " + b);
			if (!b) {

				Message messagex = new Message();
				Bundle bunndle = new Bundle();
				bunndle.putString("bye", from);
				messagex.obj = bunndle;
				videoHandler.sendMessage(messagex);
			} else {
				Message messagex = new Message();
				Bundle bunndle = new Bundle();
				bunndle.putString("newbuddy", from);
				messagex.obj = bunndle;
				videoHandler.sendMessage(messagex);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
			endtimearray[0] = getCurrentDateTime();

			for (int i = 0; i < strARMembers.length; i++) {

				touserarray[i] = strARMembers[i];

				//
				starttimearray[i] = CallDispatcher.conferenceMembersTime
						.get(strARMembers[i]);
				if (starttimearray[i] == null) {
					starttimearray[i] = strStartTime;
				}
				//

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
			e.printStackTrace();
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
				endtimearray[0] = getCurrentDateTime();

				for (int i = 0; i < strARMembers.length; i++) {

					touserarray[i] = strARMembers[i];

					//
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

	// private StateListDrawable micMuteState() {
	// StateListDrawable states = new StateListDrawable();
	// try{
	// states.addState(new int[] { android.R.attr.state_pressed },
	// getResources().getDrawable(R.drawable.mic_mute_click));
	// states.addState(new int[] { android.R.attr.state_focused },
	// getResources().getDrawable(R.drawable.mic_mute_mouseover));
	// states.addState(new int[] {}, getResources().getDrawable(
	// R.drawable.mic_mute));
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return states;
	// }

	// used to show call failure Alert
	/* Code Clean up */
	// private void CallFailed() {
	//
	// }

	public RelativeLayout getCallConnectingView() {
		try {
			timer = new Timer();
			handler = new Handler();
			relativeLayout = new RelativeLayout(this);
			// relativeLayout.setBackgroundColor(Color.BLUE);

			relativeLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			relativeLayout.setGravity(Gravity.CENTER);

			RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);

			RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);

			RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams3.addRule(RelativeLayout.RIGHT_OF, 1);
			layoutParams3.addRule(RelativeLayout.LEFT_OF, 2);
			layoutParams3.addRule(RelativeLayout.CENTER_VERTICAL);

			LinearLayout lf = new LinearLayout(this);
			lf.setOrientation(LinearLayout.VERTICAL);
			lf.setGravity(Gravity.CENTER);

			ImageView leftImage = new ImageView(this);
			leftImage.setLayoutParams(new LayoutParams(100, 300));
			leftImage.setImageResource(R.drawable.videocall);
			lf.setId(1);
			lf.addView(leftImage);
			/*
			 * TextView view=new TextView(this); view.setText("Android");
			 * lf.addView(view);
			 */
			relativeLayout.addView(lf, layoutParams1);

			LinearLayout lc = new LinearLayout(this);

			LayoutParams lcp = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			lc.setLayoutParams(lcp);

			for (int i = 0; i < 15; i++) {
				lc.addView(getDots());

			}

			relativeLayout.addView(lc, layoutParams3);

			LinearLayout lr = new LinearLayout(this);
			lr.setOrientation(LinearLayout.VERTICAL);
			lr.setGravity(Gravity.CENTER);

			ImageView rightImage = new ImageView(this);
			// rightImage.setPadding(0, 100, 0, 0);
			rightImage.setLayoutParams(new LayoutParams(100, 300));
			rightImage.setImageResource(R.drawable.videocall);
			lr.setId(2);
			lr.addView(rightImage);
			/*
			 * TextView v=new TextView(this); v.setText("PC"); lr.addView(v);
			 */
			relativeLayout.addView(lr, layoutParams2);
			// relativeLayout.setPadding(0, 100, 0, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return relativeLayout;

	}

	public ImageView getDots() {

		ImageView circleImage = new ImageView(this);
		try {
			LinearLayout.LayoutParams lcp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lcp.setMargins(0, 0, 50, 0);
			circleImage.setLayoutParams(lcp);
			circleImage.setImageResource(R.drawable.grey);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return circleImage;
	}

	@Override
	protected void onPause() {

		if (AppMainActivity.commEngine != null) {
			AppMainActivity.commEngine.setmDecodeFrame(false);
		}

		onIsOnpause = true;

		if (glSurfaceView != null) {
			glSurfaceView.onPause();
		}
		super.onPause();
	}

	boolean onIsOnpause = false;

	//
	protected void onResume() {
		// Log.d("NDVC1", "On Resume....");
		// if(ApplicationState.getApplicationCurrentState()!=ApplicationState.APPLICATION_STATE_ONLINE)
		// {
		// Toast.makeText(VideoCallScreen.this,getResources().getString(R.string.error),5000).show();
		//
		// }
		// else{
			AppMainActivity.inActivity = this;
		if (AppMainActivity.commEngine != null) {
			AppMainActivity.commEngine.setmDecodeFrame(true);
		}

		mRenderFrame = true;
		onIsOnpause = false;
		// Toast.makeText(context, "has removed --->" + hasRemoved,
		// Toast.LENGTH_SHORT).show();

		if (hasRemoved) {
			hasRemoved = false;
			// used to remove Yellow icon...
			try {
				if (ismediaNotified) {
					videosurface.removeView(layout);
					isShowingMediaFailureIcon = false;
				}
			} catch (Exception e) {
				// TODO: handle exception

			}
			//

			if (WebServiceReferences.contextTable.containsKey("connection")) {
				CallConnectingScreen screen = (CallConnectingScreen) WebServiceReferences.contextTable
						.get("connection");
				screen.finish();
			}

			CallDispatcher.videoSpeakerMute(false);
			try {
				videopreview.removeView(pv);
				videosurface.removeView(glSurfaceView);
				glSurfaceView.destroyDrawingCache();
				// Log.d("NDVC1", "glSurfaceView.destroyDrawingCache();");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();

			}
			try {
				pv.stopPreview();
				pv = AppMainActivity.commEngine.getVideoPreview(this);
				videopreview.addView(pv, 0);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// Toast.makeText(
						// context,
						// "Preview Width :" + pv.getPreviewWidth()
						// + " Preview Height :"
						// + pv.getPreviewHeight(),
						// Toast.LENGTH_LONG).show();
					}
				}, 1000);
				getGLSurfaceView(context);
				// videosurface.addView(glSurfaceView, layoutParamsf);
				glSurfaceView.onResume();
				if (timer != null) {
					// Log.d("NDVC1", "On Resume....TIMER not null");
					timer.cancel();
					layout = getCallConnectingView();
					videosurface.addView(layout, layoutParamsf);
					// Log.d("NDVC1", "Added call connecting view..");
					AnimationThread animationThread = new AnimationThread(
							VideoCallScreen.this);
					timer.schedule(animationThread, 500, 500);

					mReceiveVideo = false;
					// Log.d("NDVC1", "scheduled");
				}

				//
			} catch (Exception e) {
				e.printStackTrace();

			}

		}
		// }

		super.onResume();

	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTimerElapsed() {
		try {
			final LinearLayout ly = (LinearLayout) layout.getChildAt(1);

			handler.post(new Runnable() {

				@Override
				public void run() {
					try {
						if (i <= 7) {

							ly.addView(getDots());
							i++;
						} else {
							i = 0;
							ly.removeAllViews();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * Used to notify Media Failure
	 */
	public void notifyMediaFailure(String buddyVideo) {
		// remove surface view

		try {
			if (buddyVideo.equalsIgnoreCase(tvTitlename.trim())) {
				if (!isShowingMediaFailureIcon) {
					isShowingMediaFailureIcon = true;
					isSwitched = true;
					ismediaNotified = true;
					videosurface.removeView(glSurfaceView);
					layout = showVideoFailure();
					videosurface.addView(layout, layoutParamsf);
					isShowingMediaFailureIcon = true;

					if (timer != null) {
						timer.cancel();
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// videosurface.add

	}

	private RelativeLayout showVideoFailure() {
		try {
			relativeLayout = new RelativeLayout(this);
			// relativeLayout.setBackgroundColor(Color.BLUE);

			relativeLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			relativeLayout.setGravity(Gravity.CENTER);

			FrameLayout ll = new FrameLayout(context);
			ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.FILL_PARENT));
			ImageView img = new ImageView(context);

			img.setBackgroundResource(R.drawable.exclamation);

			ll.addView(img);

			// Log.d("NN", "height width " + c1);
			layoutParamsf = new FrameLayout.LayoutParams(c1,
					LayoutParams.FILL_PARENT);

			relativeLayout.addView(ll, layoutParamsf);

			// relativeLayout.setPadding(0, 100, 0, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return relativeLayout;

	}

	@Override
	public void notifyResolution(int w, int h) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
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
												callType, sessionid);

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

	//

	public void showReminderAlert() {

		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.need_call_hangup);

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

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

}
