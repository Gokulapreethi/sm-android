package com.cg.callservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
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
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.service.ChatHeadDrawerService;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class VideoCallScreen extends Fragment implements VideoCallback,
		AnimationListener {

	// ImageView[] view = null;
	private boolean isShowingMediaFailureIcon = false;
	Timer timer = null;
	Handler handler = null;
//	LinearLayout bottom_videowindows , top_videoWindows;
//	RelativeLayout relativeLayout;
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
	private Handler mHandler;

	private HashMap<String, Integer> videoSSRC = new HashMap<String, Integer>();
	private Vector<BuddyInformationBean> total_buddyList;
	private ImageLoader imageLoader;
	private int numOfCam;
	String record_path = null;

	AlertDialog alert = null;
	private String sessionid = null;
	private String receiver = null;
	private Handler videocall;
	private boolean hasRemoved = false;
	private boolean hasRemoved2 = false;
	Preview pv = null;
	FrameLayout preview_frameLayout , buddyframelayout01,  buddyframelayout02, buddyframelayout03;
	Button flipcamera;
	public static Context context = null;
	boolean mReceiveVideo = false;
	boolean mReceiveVideo2 = false;
	// private AudioProperties audioProperties=null;
	RelativeLayout layout , video_layouts, profile_view1, profile_view2, profile_view3;

	private  int video_viewWidth , video_viewHeight;

	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
	int mPlayingPosition = 0;
	//
	int obtainWidth = 0;
	int obtainHeight = 0;
	String callTypeForServer = "201";
//	private ArrayList<UserBean> membersList=new ArrayList<UserBean>();

	// test
	private CallDispatcher objCallDispatcher = (CallDispatcher) WebServiceReferences.callDispatch
			.get("calldispatch");

//	GLSurfaceView gls = null;
	private static GLSurfaceView glSurfaceView1 = null;
	private static GLSurfaceView glSurfaceView2 = null;
	private static GLSurfaceView glSurfaceView3 = null;

	TextView on_off1,onoff_preview,participant_name1,participant_name12,participant_name2,participant_name3,ownername;
	ImageView buddyimageview1, buddyimageview12, buddyimageview2, buddyimageview3, ownimageview ,on_off12,on_off2,on_off3;
	Timer buddytimer1 = null;
	VideoOnOffTimerTask buddyTimerTask1 = null;

	private PreviewFrameSink frameSink = null, frameSink12 = null, frameSink2 = null, frameSink3 = null;

	private ByteBuffer frameBuffer = null, frameBuffer12 = null, frameBuffer2 = null, frameBuffer3 = null;

	private boolean preview_hided = false;
	private String buddyName;
	private Queue videoQueue;
	private String host;
	private VideoThreadMultiWindow videoThread;

	public Handler videoHandler;

	final MediaPlayer mPlayer = new MediaPlayer();

	private ArrayList<String> strALCallMembers = new ArrayList<String>();

	private ArrayList<String> hsCallMembers = new ArrayList<String>();

	private String strCallStartTime = "";

//	private FrameLayout videosurface, videosurface2, videosurface3;

	private AudioProperties audioProperties = null;
	private boolean speaker = false;
	private boolean micmute = false;
	private String strStartTime;
	private Chronometer chTimer;
	private ImageView mic;
	private ImageView loudSpeaker;
	// TextView tvTitle;
	String tvTitlename = null;

	private FrameLayout.LayoutParams surfaceview_layoutParams;

	String conferenceWindowTitle = "Conference People";
	String title = conferenceWindowTitle;

	private KeyguardManager keyguardManager;
	private KeyguardLock lock;

	private PowerManager.WakeLock mWakeLock;

	private boolean selfHangup = false;
	private boolean isMinimize=false;
	TextView member_count;

	MediaPlayer mediaPlayer;
	boolean video_1_shown= true, video_2_shown= true;

	private static VideoCallScreen videoCallScreen;
	public View rootView;
	Bundle bundlevalues;
	RelativeLayout mainHeader;

//	private Animation animSlideDown, animSlideUp;

	public static VideoCallScreen getInstance(Context maincontext) {
		try {
			if (videoCallScreen == null) {

				context = maincontext;
				videoCallScreen = new VideoCallScreen();

			}

			return videoCallScreen;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return videoCallScreen;
		}
	}
	private int activityOrientation;
	@Override
	public void onAttach(Activity arg0) {
		super.onAttach(arg0);
		activityOrientation = arg0.getRequestedOrientation();

	}

	/**
	 * Called when the activity is starting. Also used to Initialize the members
	 * of the class. Show the videoCall view and their controls.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		try {
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			context = this;
//			WebServiceReferences.contextTable.put("callscreen", this);
			Log.i("VideoCall","VideoCallScreen onCreate");
			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			if(rootView==null){
			SingleInstance.instanceTable.put("callscreen",videoCallScreen);
            bundlevalues=getArguments();
			videocall = new Handler();
//			videoHandler = new Handler();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
			objCallDispatcher.startPlayer(context);

				preview_hided = false;

			CallDispatcher.networkState = objCallDispatcher.connectivityType();
			mainHeader=(RelativeLayout)getActivity().findViewById(R.id.mainheader);
			mainHeader.setVisibility(View.GONE);

			keyguardManager = (KeyguardManager) context.getSystemService(Activity.KEYGUARD_SERVICE);
			lock = keyguardManager.newKeyguardLock(context.KEYGUARD_SERVICE);
			lock.disableKeyguard();

			CallDispatcher.networkState = objCallDispatcher.connectivityType();
			handler = new Handler();

			// //////////////
			// Make the Message msg null after processing it
			// Make the Bundle bun null after processing it
//			if(rootView==null){

			videoHandler = new Handler() {

				public void handleMessage(Message result) {
					// Log.d("one", "videocall");
//					super.handleMessage(result);
					final Bundle bundle = (Bundle) result.obj;
					try {
						if (bundle.containsKey("newbuddy")) {
							if (!strALCallMembers.contains(bundle
									.getString("newbuddy"))) {
								hsCallMembers.add(bundle.getString("newbuddy"));
								strALCallMembers.add(bundle
										.getString("newbuddy"));
							}
							switchVideo("");
							// if (ad != null) {
							// Log.e("cover", "339");
							// ad.notifyDataSetChanged();
							// }
							// switchVideo(bundle.getString("newbuddy"));
							member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {

									RecordTransactionBean transactionBean = new RecordTransactionBean();
									transactionBean.setSessionid(sessionid);
									transactionBean.setHost(CallDispatcher.LoginUser);
									transactionBean.setParticipants("");
									if (preview_hided) {
										transactionBean.setDisableVideo("yes");
									} else {
										transactionBean.setDisableVideo("no");
									}
									processCallRequest(2, transactionBean, "disablevideo");
								}
							}, 2000);
						} else if (bundle.containsKey("newrequest")) {
							SignalingBean bean = (SignalingBean) bundle
									.getSerializable("newrequest");

						} else if (bundle.containsKey("leave")) {
							// if (CallDispatcher.sb.getBs_parentid() != null) {
							chTimer.stop();
							AppMainActivity.cvtimer.stop();
//							video_minimize.setVisibility(View.GONE);
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

							if(CallDispatcher.removed_current_conf_members!=null && CallDispatcher.removed_current_conf_members.size()>0){
								for(String name:CallDispatcher.removed_current_conf_members){
									if(!name.equalsIgnoreCase(host)){
										if(participant==null){
											participant=name;
										}else{
											participant=participant+","+name;
										}

									}
								}
							}

							if(!host.equalsIgnoreCase(CallDispatcher.LoginUser)){
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
							Log.i("callentry", "db entry 8");
//							DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//							DBAccess.getdbHeler()
//									.saveOrUpdateRecordtransactiondetails(
//											CallDispatcher.sb);

							if(CallDispatcher.callHistoryDetails != null) {
								SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
								hist_bean.setParticipant_name(participant);
								hist_bean.setEndTime(objCallDispatcher.getCurrentDateandTime());
								hist_bean.setCallDuration(SingleInstance.mainContext
										.getCallDuration(hist_bean.getStartTime(),
												hist_bean.getEndTime()));
								hist_bean.setCallstatus("callattended");
								DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
								int row=DBAccess.getdbHeler().insertGroupCallChat(hist_bean);
								if(AppReference.mainContext != null && row==1) {
									AppReference.mainContext.CallEntryToServer(hist_bean);
								}
							}
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
							member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
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
								member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
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
								resetVideoViews(!preview_hided);
//								switchVideo(CallDispatcher.conferenceMembers
//										.get(0));
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
							try {
								Log.i("VideoCall","came to bundle.containsKey(\"img\") : window id is : "+bundle.getInt("windowid")+" videossrc : "+bundle.getInt("videossrc"));
								if (bundle.getInt("windowid") == 0) {

//							if(video_1_shown) {
//								Log.i("NotesVideo","video_1_shown");
								frameSink.getFrameLock().lock();
								frameBuffer.position(0);
								// frameRenderer.setPreviewFrameWidth(obtainWidth);
								// frameRenderer.setPreviewFrameHeight(obtainHeight);

								frameBuffer.put(bundle.getByteArray("img"));
								frameBuffer.position(0);
								frameSink.setNextFrame(frameBuffer);
								frameSink.getFrameLock().unlock();

								glSurfaceView1.requestRender();

//							}
							}  else if(bundle.getInt("windowid") == 1) {
//							if(video_2_shown) {
								Log.i("NotesVideo","video_2_shown");
								frameSink2.getFrameLock().lock();
								frameBuffer2.position(0);
								// frameRenderer.setPreviewFrameWidth(obtainWidth);
								// frameRenderer.setPreviewFrameHeight(obtainHeight);

								frameBuffer2.put(bundle.getByteArray("img"));
								frameBuffer2.position(0);
								frameSink2.setNextFrame(frameBuffer2);
								frameSink2.getFrameLock().unlock();
								glSurfaceView2.requestRender();
//							}
							} else if(bundle.getInt("windowid") == 2) {

								frameSink3.getFrameLock().lock();
								frameBuffer3.position(0);
								// frameRenderer.setPreviewFrameWidth(obtainWidth);
								// frameRenderer.setPreviewFrameHeight(obtainHeight);

								frameBuffer3.put(bundle.getByteArray("img"));
								frameBuffer3.position(0);
								frameSink3.setNextFrame(frameBuffer3);
								frameSink3.getFrameLock().unlock();
								glSurfaceView3.requestRender();
                                }
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (bundle.containsKey("video")) {
							// Log.d("video", "videopre");

						} else if (bundle.containsKey("hangupfullscreen")) {

							// Log.d("hang", "hangup full Screen");

							isHangedUp = true;
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

								if(CallDispatcher.removed_current_conf_members!=null && CallDispatcher.removed_current_conf_members.size()>0){
									for(String name:CallDispatcher.removed_current_conf_members){
										if(!name.equalsIgnoreCase(host)){
											if(participant==null){
												participant=name;
											}else{
												participant=participant+","+name;
											}

										}
									}
								}

								if(!host.equalsIgnoreCase(CallDispatcher.LoginUser)){
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
								Log.i("callentry", "db entry 9");
//								DBAccess.getdbHeler().insertGroupCallChat(CallDispatcher.sb);
//								DBAccess.getdbHeler()
//										.saveOrUpdateRecordtransactiondetails(
//												CallDispatcher.sb);
								if(CallDispatcher.callHistoryDetails != null) {
									SignalingBean hist_bean = CallDispatcher.callHistoryDetails;
									hist_bean.setParticipant_name(participant);
									hist_bean.setEndTime(objCallDispatcher.getCurrentDateandTime());
									hist_bean.setCallDuration(SingleInstance.mainContext
											.getCallDuration(hist_bean.getStartTime(),
													hist_bean.getEndTime()));
									hist_bean.setCallstatus("callattended");
									DBAccess.getdbHeler().insertOrUpdateCallHistory(hist_bean);
									int row=DBAccess.getdbHeler().insertGroupCallChat(hist_bean);
									if(AppReference.mainContext != null && row==1) {
										AppReference.mainContext.CallEntryToServer(hist_bean);
									}
								}
								showCallHistory();
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
//							finish();
 							finishVideocallScreen();

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			};

			strStartTime = getCurrentDateTime();

			// audioProperties=new AudioProperties(this);

//			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			layoutParams.setMargins(20, 0, 0, 15);

//			context = this;
			WebServiceReferences.contextTable.put("VideoCallscreen", context);
//			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

			if (savedInstanceState == null) {

				sessionid = (String) bundlevalues.getString("sessionid");

				receiver = (String) bundlevalues.getString("receive");

				buddyName = (String) bundlevalues.getString("buddyName");
			} else {
				sessionid = (String) savedInstanceState.getString("sessionid");

				receiver = (String) savedInstanceState.getString("receive");

				buddyName = (String) savedInstanceState.getString("buddyName");

			}

			if(bundlevalues.getString("host") != null) {
				host = (String)bundlevalues.getString("host");
			}

			Log.i("NOTES","sessionid : "+sessionid);
			CallDispatcher.currentSessionid =sessionid;
			tvTitlename = buddyName;

			// tvTitle.setText(buddyName);

			hsCallMembers.add(buddyName);

			// Log.e("test", "************ Buddy Name :" + buddyName);

			strALCallMembers.add(buddyName);

			strCallStartTime = objCallDispatcher.getCurrentDateTime();

			audioProperties = new AudioProperties(context);
			LayoutInflater inflateLayout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			if(rootView==null) {
				rootView = inflater.inflate(R.layout.videocall, null);
				Button minimize_btn = (Button) rootView.findViewById(R.id.minimize_btn);


//			Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_xml);
//			rotation.setRepeatCount(Animation.INFINITE);
//			rootView.startAnimation(rotation);
//
//				animSlideDown = AnimationUtils.loadAnimation(context,
//						R.anim.slide_down);
//				animSlideUp = AnimationUtils.loadAnimation(context,
//						R.anim.slide_up);
				video_layouts = (RelativeLayout)rootView.findViewById(R.id.video_views);

				preview_frameLayout = new FrameLayout(context);
				preview_frameLayout.setId(0);
				buddyframelayout01 = new FrameLayout(context);
				buddyframelayout01.setId(1);
				buddyframelayout02 = new FrameLayout(context);
				buddyframelayout02.setId(2);
				buddyframelayout03 = new FrameLayout(context);
				buddyframelayout03.setId(3);

				profile_view1 = new RelativeLayout(context);
				profile_view1.setId(1);
				profile_view2 = new RelativeLayout(context);
				profile_view2.setId(2);
				profile_view3 = new RelativeLayout(context);
				profile_view3.setId(3);

				LayoutParams layoutParams_flipcamera=new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER_VERTICAL);

				flipcamera = new Button(context);
				flipcamera.setLayoutParams(layoutParams_flipcamera);
				flipcamera.setBackgroundResource(R.drawable.call_flip_camera);

				chTimer = (Chronometer)rootView.findViewById(R.id.call_timer);
				chTimer.setVisibility(View.VISIBLE);
				chTimer.start();
				AppMainActivity.cvtimer.setBase(SystemClock.elapsedRealtime());
				AppMainActivity.cvtimer.start();
				chTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
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

				final ImageView btn_video = (ImageView) rootView.findViewById(R.id.btn_video);

//				onoff_preview = (TextView) rootView.findViewById(R.id.onoffpreview);
				btn_video.setTag(true);

				ownername = (TextView) rootView.findViewById(R.id.name1);
//				own_video_layout = (RelativeLayout) rootView.findViewById(R.id.ownvideolayout);
//				top_videoWindows = (LinearLayout) rootView.findViewById(R.id.top_videowindowlayouts);
//				bottom_videowindows = (LinearLayout) rootView.findViewById(R.id.bottom_videowindowlayout);
				ViewTreeObserver viewTreeObserver = video_layouts.getViewTreeObserver();
				if (viewTreeObserver.isAlive()) {
					viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							video_layouts.getViewTreeObserver().removeGlobalOnLayoutListener(this);
							video_viewWidth = video_layouts.getWidth();
							video_viewHeight = video_layouts.getHeight();
//						Log.i(TAG,"video_viewWidth : "+video_viewWidth+" video_viewHeight : "+video_viewHeight);
						}
					});
				}

				total_buddyList = ContactsFragment.getBuddyList();
				imageLoader = new ImageLoader(context);

				numOfCam = Camera.getNumberOfCameras();

				CallDispatcher.videoScreenVisibleState = true;

				ImageView add = (ImageView) rootView.findViewById(R.id.add);

				member_count = (TextView) rootView.findViewById(R.id.members_count);
				Button members = (Button) rootView.findViewById(R.id.members);
				add.setKeepScreenOn(true);
				// add.setText("ADD");

				add.setImageResource(R.drawable.call_add);
				add.setPadding(0, 20, 0, 0);

				minimize_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						addShowHideListener();

					}
				});

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
				member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size()+1));
				members.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size() + 1));
						Intent i = new Intent(AppReference.mainContext, CallActiveMembersList.class);
						i.putExtra("timer", chTimer.getText().toString());
						i.putExtra("sessionId", sessionid);
						i.putExtra("calltype","VC");
						i.putExtra("host", host);
						i.putExtra("fromscreen","videocallscreen");
						i.putExtra("precalltype","VC");
						if((boolean) btn_video.getTag() && preview_hided) {
							i.putExtra("previewdiabled", preview_hided);
						} else {
							i.putExtra("previewdiabled", false);
						}
						AppReference.mainContext.startActivity(i);
					}
				});
				ImageView hangup = (ImageView) rootView.findViewById(R.id.hang);

				hangup.setImageResource(R.drawable.call_reject);

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

				loudSpeaker = (ImageView) rootView.findViewById(R.id.loudspeaker);

				loudSpeaker.setImageResource(R.drawable.call_speaker);
				mic = (ImageView) rootView.findViewById(R.id.speaker);

				// mic.setBackgroundDrawable(micUnmuteState());
				mic.setImageResource(R.drawable.call_mic);
				speaker = false;

				if (SingleInstance.mainContext.isAutoAcceptEnabled(
						CallDispatcher.LoginUser, buddyName)) {
					speaker = true;
					loudSpeaker.setImageResource(R.drawable.call_speaker_active);

				} else {
					speaker = false;
					loudSpeaker.setImageResource(R.drawable.call_speaker);
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
										.setImageResource(R.drawable.call_speaker_active);
							} else if (speaker) {
								loudSpeaker
										.setImageResource(R.drawable.call_speaker);
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
								mic.setImageResource(R.drawable.call_mic);
							} else if (!micmute) {
								micmute = true;
								// mic.setBackgroundResource(R.drawable.mic_mute);
								mic.setImageResource(R.drawable.call_mic_active);
							}

							AppMainActivity.commEngine.micMute(micmute);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				});

//			final Button buddies = new Button(this);
				ImageView buddies = (ImageView) rootView.findViewById(R.id.switchuser);
				// buddies.setHint("off");
				buddies.setImageResource(R.drawable.buddies_video);
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
				ImageView btn_loc = (ImageView) rootView.findViewById(R.id.location);
				btn_loc.setImageResource(R.drawable.loc_pin);

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


				videoQueue = new Queue();

				AppMainActivity.commEngine.setVideoCallback(this);
				getGLSurfaceView(context);
				getGLSurfaceView2(context);
				getGLSurfaceView3(context);

				assignOnOffIcons(context);
				surfaceview_layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
				on_off1.setTag(true);
				on_off12.setTag(true);
				on_off2.setTag(true);
				on_off3.setTag(true);

				RelativeLayout.LayoutParams buddyimageview_params = new RelativeLayout.LayoutParams(130,
						130);
				buddyimageview_params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				buddyimageview1 = new org.cryse.android.controls.RoundedImageView(context);
				buddyimageview1.setId(1);
				buddyimageview2 = new org.cryse.android.controls.RoundedImageView(context);
				buddyimageview2.setId(2);
				buddyimageview3 = new org.cryse.android.controls.RoundedImageView(context);
				buddyimageview3.setId(3);
				buddyimageview1.setLayoutParams(buddyimageview_params);
				buddyimageview2.setLayoutParams(buddyimageview_params);
				buddyimageview3.setLayoutParams(buddyimageview_params);

				participant_name1 = new TextView(context);
				participant_name2 = new TextView(context);
				participant_name3 = new TextView(context);
				RelativeLayout.LayoutParams participant_name_params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
				participant_name_params1.addRule(RelativeLayout.BELOW, buddyimageview1.getId());
				participant_name_params1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
				RelativeLayout.LayoutParams participant_name_params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
				participant_name_params2.addRule(RelativeLayout.BELOW, buddyimageview2.getId());
				participant_name_params2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
				RelativeLayout.LayoutParams participant_name_params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
				participant_name_params3.addRule(RelativeLayout.BELOW, buddyimageview3.getId());
				participant_name_params3.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
				participant_name1.setLayoutParams(participant_name_params1);
				participant_name2.setLayoutParams(participant_name_params2);
				participant_name3.setLayoutParams(participant_name_params3);

				boolean hasFrontCamera = context.getPackageManager()
						.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
				if (Integer.parseInt(Build.VERSION.SDK) > 8) {
					WebServiceReferences.CAMERA_ID = 1;
					if (!hasFrontCamera) {
						WebServiceReferences.CAMERA_ID = 0;
					}

				} else {

					WebServiceReferences.CAMERA_ID = 2;

				}

				AppMainActivity.commEngine.setVideoParameters(
						WebServiceReferences.VIDEO_WIDTH,
						WebServiceReferences.VIDEO_HEIGHT,
						WebServiceReferences.CAMERA_ID);

				pv = AppMainActivity.commEngine.getVideoPreview(context);

				pv.setZOrderOnTop(false);

				preview_frameLayout.addView(pv);

				glSurfaceView1.setZOrderOnTop(false);
				glSurfaceView2.setZOrderOnTop(false);
				glSurfaceView3.setZOrderOnTop(false);

				double c = nosHeight * 1.1;
				c1 = (int) c;

//				int percentage;
//				int remSizeforButton;
//
//				if (nosHeight > nosWidth) {
//
//					remSizeforButton = nosHeight - (144 + c1);
//					percentage = (int) (nosHeight * 0.15);
//
//				} else {
//					remSizeforButton = nosWidth - (144 + c1);
//					percentage = (int) (nosWidth * 0.15);
//				}
//
//				if (percentage > remSizeforButton) {
////				llControls.setVisibility(View.GONE);
//				}

				// Log.d("NN", "height width " + c1);
				layout = getCallConnectingView();

				// layout.setLayoutParams(new
				// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

				AnimationThread animationThread = new AnimationThread(
						VideoCallScreen.this);
				timer.schedule(animationThread, 500, 500);


//				final Runnable runnable = new Runnable() {
//
//					@Override
//					public void run() {

						// showBuddies.setVisibility(View.INVISIBLE);
//					}
//				};

//			glSurfaceView1.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					try {
//						showBuddies.bringToFront();
//
//						showBuddies.postDelayed(runnable, 5000);
//
//					} catch (Exception e) {
//						// TODO: handle exception
//						e.printStackTrace();
//					}
//
//					return false;
//				}
//			});


				videoThread = new VideoThreadMultiWindow(videoQueue);
				videoThread.setHandler(videoHandler);
				videoThread.start();


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

//								pv.setZOrderOnTop(false);

									preview_frameLayout.addView(pv, 0);
								}
							}
						}

					}
				});

				AppMainActivity.commEngine.enable_disable_VideoPreview(true);

				btn_video.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean shown = (boolean) btn_video.getTag();
						AppMainActivity.commEngine.enable_disable_VideoPreview(!shown);

						if (shown) {
							preview_hided = true;
							RecordTransactionBean transactionBean = new RecordTransactionBean();
							transactionBean.setSessionid(sessionid);
							transactionBean.setHost(CallDispatcher.LoginUser);
							transactionBean.setParticipants("");
							transactionBean.setDisableVideo("yes");
							processCallRequest(2, transactionBean, "disablevideo");
							btn_video.setImageResource(R.drawable.call_video);
						} else {
							preview_hided = false;
							btn_video.setImageResource(R.drawable.call_video_active);
							RecordTransactionBean transactionBean = new RecordTransactionBean();
							transactionBean.setSessionid(sessionid);
							transactionBean.setHost(CallDispatcher.LoginUser);
							transactionBean.setParticipants("");
							transactionBean.setDisableVideo("no");
							processCallRequest(2, transactionBean, "disablevideo");
//							ownername.setVisibility(View.GONE);
//							ownimageview.setVisibility(View.GONE);
//							videopreview.setVisibility(View.VISIBLE);
//							flipcamera.setVisibility(View.VISIBLE);
						}
//						preview_hided =!preview_hided;
						btn_video.setTag(!shown);
						resetVideoViews(!preview_hided);
					}
				});
				onoff_preview.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Log.i("VideoCall","onoff_preview.setOnClickListener");
						preview_hided = true;
						resetVideoViews(!preview_hided);
					}
				});

				on_off1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean shown = (boolean) on_off1.getTag();

						onOffVideoForSelectedUser(0, !shown);
						resetVideoViews(!preview_hided);
					}
				});

				on_off12.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean shown = (boolean) on_off12.getTag();
						onOffVideoForSelectedUser(1, !shown);
						resetVideoViews(!preview_hided);
					}
				});

				on_off2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean shown = (boolean) on_off2.getTag();
						onOffVideoForSelectedUser(1, !shown);
						resetVideoViews(!preview_hided);
					}
				});

				on_off3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						boolean shown = (boolean) on_off3.getTag();
						onOffVideoForSelectedUser(2, !shown);
						resetVideoViews(!preview_hided);
					}
				});

				final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
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
			}
//			AppReference.mainContext.startRecording();
//			startVideoRecording();
//			recording_start = true;
//			onToggleScreenShare(recording_start);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// }
		//
		return rootView;

	}

	private void onOffVideoForSelectedUser(final int selectedposition, final boolean onoff){
		if(WebServiceReferences.videoSSRC_total_list != null && WebServiceReferences.videoSSRC_total_list.size() > selectedposition){
			final int size = WebServiceReferences.videoSSRC_total_list.size();
			int turn_ssrc =	WebServiceReferences.videoSSRC_total_list.get(selectedposition);

			WebServiceReferences.removed_videoSSRC_list.add(turn_ssrc);
			WebServiceReferences.videoSSRC_total_list.remove(selectedposition);

			final String buddy_name = (WebServiceReferences.videoSSRC_total.get(turn_ssrc)).getMember_name();
			WebServiceReferences.videoSSRC_total.get(turn_ssrc).setVideoRemoved(!onoff);
			onOffVideo(buddy_name,onoff);
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.i("VideoCall","came to onOffVideoForSelectedUser : onoff : "+onoff);
					if (onoff) {

					} else {

					}
				}
			});
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
					"yyyy-MM-dd HH:mm:ss aa");
			return sdf.format(curDate).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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
//				VideoThreadBean threadBean = WebServiceReferences.videoSSRC_total.get(requiredKey);
//				threadBean.setVideoDisabled(true);

//				if(WebServiceReferences.videoSSRC_total_list != null && WebServiceReferences.videoSSRC_total_list.contains(requiredKey)){
//					final int selectedposition =	WebServiceReferences.videoSSRC_total_list.indexOf(requiredKey);
//					final String buddy_name = (WebServiceReferences.videoSSRC_total.get(turn_ssrc)).getMember_name();
					final int finalRequiredKey = requiredKey;
					handler.post(new Runnable() {
						@Override
						public void run() {
							VideoThreadBean threadBean = WebServiceReferences.videoSSRC_total.get(finalRequiredKey);
//
							if (vidsignBean.getVideoStoped() == null || vidsignBean.getVideoStoped().equalsIgnoreCase("no")) {
								threadBean.setVideoDisabled(false);
//								if(selectedposition == 0) {
//									buddyimageview1.setVisibility(View.GONE);
//									glSurfaceView1.setVisibility(View.VISIBLE);
//
//								} else if(selectedposition == 1) {
//									buddyimageview2.setVisibility(View.GONE);
//									glSurfaceView2.setVisibility(View.VISIBLE);
//								} else if(selectedposition == 2) {
//									buddyimageview3.setVisibility(View.GONE);
//									glSurfaceView3.setVisibility(View.VISIBLE);
//								}
							} else {
								threadBean.setVideoDisabled(true);
//								boolean have_image = false;
//								for (BuddyInformationBean buddyInformationBean : total_buddyList) {
//									Log.i("Join","tb image : "+buddyInformationBean.getName()+" cb image : "+vidsignBean.getFrom());
//									if (buddyInformationBean.getName().equalsIgnoreCase(vidsignBean.getFrom())) {
//										String pic_path = buddyInformationBean.getProfile_picpath();
//										Log.i("Join","pic_path 1 : "+pic_path);
//										if (pic_path == null || pic_path.length() == 0) {
//											ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(vidsignBean.getFrom());
//											if(pBean != null) {
//												pic_path = pBean.getPhoto();
//												Log.i("Join","pic_path 2 : "+pic_path);
//											}
//										}
//
//										if (pic_path != null && pic_path.length() > 0) {
//											if (!pic_path.contains("COMMedia")) {
//												pic_path = Environment
//														.getExternalStorageDirectory()
//														+ "/COMMedia/" + pic_path;
//											}
//											have_image = true;
//											if (selectedposition == 0) {
//												imageLoader.DisplayImage(pic_path, buddyimageview1, R.drawable.icon_buddy_aoffline);
//											} else if (selectedposition == 1) {
//												imageLoader.DisplayImage(pic_path, buddyimageview2, R.drawable.icon_buddy_aoffline);
//											} else if (selectedposition == 2) {
//												imageLoader.DisplayImage(pic_path, buddyimageview3, R.drawable.icon_buddy_aoffline);
//											}
//										}
//									}
//								}
//								if(selectedposition == 0) {
//									if (!have_image) {
//										imageLoader.DisplayImage("", buddyimageview1, R.drawable.icon_buddy_aoffline);
//									}
//									buddyimageview1.setVisibility(View.VISIBLE);
//									glSurfaceView1.setVisibility(View.GONE);
//								} else if(selectedposition == 1) {
//									if (!have_image) {
//										imageLoader.DisplayImage("", buddyimageview2, R.drawable.icon_buddy_aoffline);
//									}
//									buddyimageview2.setVisibility(View.VISIBLE);
//									glSurfaceView2.setVisibility(View.GONE);
//								} else if(selectedposition == 2) {
//									if (!have_image) {
//										imageLoader.DisplayImage("", buddyimageview3, R.drawable.icon_buddy_aoffline);
//									}
//									buddyimageview3.setVisibility(View.VISIBLE);
//									glSurfaceView3.setVisibility(View.GONE);
//								}
							}
							notifyNumFeedChanged();
						}
					});
//				}
			}
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
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
                        SingleInstance.mainContext.getResources().getString(
                                R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//								finish();
                                finishVideocallScreen();
                                return;
                            }
                        });
		alert = builder.create();
		alert.show();
	}

	// Used to save Activity State...
	public void onSaveInstanceState(Bundle outState) {
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

	public void receiveHAngUpx(final SignalingBean sb) {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {
					try {
						// TODO Auto-generated method stub
						if (CallDispatcher.conferenceMembers.size() == 1) {

							Log.i("callscreenfinish", "1 conferenceMembers.size()==1 name-->" + CallDispatcher.conferenceMembers.get(0));
							Log.i("callscreenfinish", "1 sb.name-->" + sb.getFrom());
							if (CallDispatcher.conferenceMembers.get(0).equalsIgnoreCase(sb.getFrom())) {
								WebServiceReferences.contextTable.remove("VideoCall");
								// Log.e("hang",
								// "Comes to message bye received in Video call Screen");
								objCallDispatcher.currentSessionid = null;
								chTimer.stop();
								AppMainActivity.cvtimer.stop();
//								video_minimize.setVisibility(View.GONE);
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
//							finish();
									finishVideocallScreen();

									//
								}
							}
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
//		finish();
		finishVideocallScreen();

	}

	public void updateBuddyNames(final String buddyName) {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {

					try {
						// tvTitle.setText(buddyName);
                        CallDispatcher.sb.setStartTime(objCallDispatcher.getCurrentDateandTime());
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

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
//                                        recording_start = false;
//										onToggleScreenShare(recording_start);
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

	public void hangupCallFromCallActiveMembers(){
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
			ArrayList<String> memberslist = new ArrayList<String>();
			for (int i = 0; i < members.length; i++) {
				if (!CallDispatcher.conferenceMembers.contains(members[i])) {
					memberslist.add(members[i]);
				}
			}
			if (memberslist.size() > 0) {
				Intent intent = new Intent(context,
						AddGroupMembers.class);
				intent.putExtra("fromcall",true);
				intent.putStringArrayListExtra("buddylist", memberslist);
				startActivityForResult(intent, 3);
//				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//				builder.create();
//				builder.setTitle(SingleInstance.mainContext.getResources()
//						.getString(R.string.add_people));
//				final String[] choiceList = membersList
//						.toArray(new String[membersList.size()]);
//				Log.d("SIZE", "Size of the OnLine Buddies " + choiceList.length);
//
//				builder.setItems(choiceList,
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//
//								SignalingBean sb = objCallDispatcher
//										.callconfernceUpdate(
//												choiceList[which].toString(),
//												callType, sessionid);
//
//								CallDispatcher.conferenceRequest.put(
//										choiceList[which].toString(), sb);
//
//								alert.dismiss();
//
//							}
//						});
//				alert = builder.create();
//
//				alert.show();

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.sorry_no_online_users), Toast.LENGTH_SHORT).show();
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
		 Log.d("NotesVideo", "came to switchVideo " + buddyVideo);
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
						sb.setGmember(buddy);
						sb.setCallType("VC");
						sb.setSessionid(sessionid);
						Log.d("NotesVideo", "came to switchVideo 1" + buddy);
						AppMainActivity.commEngine.makeCall(sb);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}).start();

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
						sb.setSessionid(sessionid);
						AppMainActivity.commEngine.turnOnOffVideo(sb);
//					}
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
//			// showHangUpAlert();
//
//		}
//		return super.onKeyDown(keyCode, event);
//
//	}

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
	public void onStop() {
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
	public void onDestroy() {

		// RenderBitmap.count=0;
		// HomeTabViewScreen home =null;
		try {
			final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//			AppMainActivity.commEngine.enable_disable_VideoPreview(true);
//			WebServiceReferences.videoSSRC_total.clear();
//			WebServiceReferences.videoSSRC_total_list.clear();
//			CallDispatcher.conConference.clear();
//			CallDispatcher.issecMadeConference = false;
//			CallDispatcher.isCallInitiate = false;

//			if (this.mWakeLock != null && this.mWakeLock.isHeld())
//				this.mWakeLock.release();
//			lock.reenableKeyguard();
//
//			objCallDispatcher.startPlayer(context);
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

//		if (videoThread != null) {
//			if (Build.VERSION.SDK_INT < 10) {
//				videoThread.stop();
//			} else {
//				videoThread.stopVideoThread();
//			}
//
//		}
//		if (videoQueue != null) {
//			videoQueue.clear();
//		}

		super.onDestroy();
	}

	private void assignOnOffIcons(Context context){

		LayoutParams layoutParams=new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		onoff_preview = new TextView(context);
		onoff_preview.setLayoutParams(layoutParams);
		onoff_preview.setBackgroundResource(R.drawable.call_close_video);

		on_off1= new TextView(context);
		on_off1.setLayoutParams(layoutParams);
		on_off1.setBackgroundResource(R.drawable.call_close_video);

		on_off12= new ImageView(context);
		on_off12.setLayoutParams(layoutParams);
		on_off12.setBackgroundResource(R.drawable.call_close_video);

		on_off2= new ImageView(context);
		on_off2.setLayoutParams(layoutParams);
		on_off2.setBackgroundResource(R.drawable.call_close_video);

		on_off3= new ImageView(context);
		on_off3.setLayoutParams(layoutParams);
		on_off3.setBackgroundResource(R.drawable.call_close_video);
	}

	public GLSurfaceView getGLSurfaceView(Context context) {
		 glSurfaceView1 = new GLSurfaceView(context);
		try {
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			glSurfaceView1.setRenderer(frameRenderer);
			frameSink = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return glSurfaceView1;
	}


	public GLSurfaceView getGLSurfaceView2(Context context) {
		glSurfaceView2 = new GLSurfaceView(context);
		try {
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			glSurfaceView2.setRenderer(frameRenderer);
			frameSink2 = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer2 = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return glSurfaceView2;
	}

	public GLSurfaceView getGLSurfaceView3(Context context) {
		glSurfaceView3 = new GLSurfaceView(context);
		try {
			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
			frameRenderer.setPreviewFrameSize(512,
					WebServiceReferences.VIDEO_WIDTH,
					WebServiceReferences.VIDEO_HEIGHT);
			glSurfaceView3.setRenderer(frameRenderer);
			frameSink3 = frameRenderer;
			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
					* WebServiceReferences.VIDEO_WIDTH * 3];
			frameBuffer3 = GraphicsUtil.makeByteBuffer(frame.length);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return glSurfaceView3;
	}
//	private void resumeGlSurface() {
//		try {
//			VideoFrameRenderer frameRenderer = new VideoFrameRenderer();
//			frameRenderer.setPreviewFrameSize(512,
//					WebServiceReferences.VIDEO_WIDTH,
//					WebServiceReferences.VIDEO_HEIGHT);
////			glSurfaceView1.setRenderer(frameRenderer);
//
//			frameSink = frameRenderer;
//			byte[] frame = new byte[WebServiceReferences.VIDEO_WIDTH
//					* WebServiceReferences.VIDEO_WIDTH * 3];
//			frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}

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
					Log.i("Promote","list.size : "+list.size());
					HashMap<String, UserBean> membersMap = new HashMap<String, UserBean>();
					Vector<UserBean> membersList=new Vector<UserBean>();
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

							if (objCallDispatcher != null) {
								SignalingBean sb = objCallDispatcher.callconfernceUpdate(
										bib.getBuddyName(),
										"VC", sessionid,"");
								// june04-Implementation
								CallDispatcher.conferenceRequest
										.put(bib.getBuddyName(), sb);
							}
							member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size()+1));
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
	int count=0,vidid = 0;
	String STORE_DIRECTORY;

	@Override
	public void notifyDecodedVideoCallback(final byte[] data, final long ssrc) {
		Log.i("NotesVideo", "came to notifyDecodedVideoCallback : ssrc :"+ssrc+" mReceiveVideo : "+mReceiveVideo+" isSwitched : "+isSwitched);

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
							isShowingMediaFailureIcon = false;
							ismediaNotified = false;

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
							isShowingMediaFailureIcon = false;
							ismediaNotified = false;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				isSwitched = false;
			}

//			if (videoQueue.getSize() < 1) {
				if (mRenderFrame) {

						videocall.post(new Runnable() {

							@Override
							public void run() {

								try {
									member_count.setText(String.valueOf(CallDispatcher.conferenceMembers.size()+1));
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
						});

//					if(vidid == 0) {
//						vidid = 1;
//						STORE_DIRECTORY = Environment.getExternalStorageDirectory()
//								.getAbsolutePath() + "/screenshots/";
//						File vid = new File(STORE_DIRECTORY
//								+ "/myscreen_" + "vid1"
//								+ ".mp4");
//						vid.createNewFile();
//
//						DataOutputStream fos1 = new DataOutputStream(new FileOutputStream(vid));
//						fos1.write(data);
//						fos1.flush();
//						fos1.close();
//					}

						int size = WebServiceReferences.videoSSRC_total_list.size();
						VideoThreadBean videoThreadBean = new VideoThreadBean();
						videoThreadBean.setData(data);

//						if ((size == 2) && (WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc) == 1)) {
//							videoThreadBean.setWindow(12);
//						} else {
							videoThreadBean.setWindow(WebServiceReferences.videoSSRC_total_list.indexOf((int) (long) ssrc));
//						}
						if(WebServiceReferences.videoSSRC_total.containsKey((int) (long) ssrc)){
							videoThreadBean.setMember_name((WebServiceReferences.videoSSRC_total.get((int) (long) ssrc)).getMember_name());
						} else {
							videoThreadBean.setMember_name("");
						}
						videoThreadBean.setVideoSssrc((int) (long) ssrc);
						videoQueue.addMsg(videoThreadBean);
				} else {

					// System.out.println("SKIPPED");
				}
//			}

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
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
//									try {
//										if (!ismediaNotified) {
//											isSwitched = true;
//											videosurface
//													.removeView(glSurfaceView1);
//											layout = getCallConnectingView();
//											if (timer != null) {
//												// timer.schedule(;, when)
//												AnimationThread animationThread = new AnimationThread(
//														VideoCallScreen.this);
//												timer.schedule(animationThread,
//														500, 500);
//											}
//
//											// getGLSurfaceView(context);
//											if (layout != null) {
//												// Log.d("NDVC1",
//												// "Video on Switch not null if");
//												videosurface.addView(layout,
//														layoutParamsf);
//											}
//										} else {
//											// isSwitched=true;
//											videosurface.removeView(layout);
//											isShowingMediaFailureIcon = false;
//											layout = getCallConnectingView();
//											if (timer != null) {
//												// timer.schedule(;, when)
//												AnimationThread animationThread = new AnimationThread(
//														VideoCallScreen.this);
//												timer.schedule(animationThread,
//														500, 500);
//											}
//											// getGLSurfaceView(context);
//											if (layout != null) {
//												videosurface.addView(layout,
//														layoutParamsf);
//												// Log.d("NDVC1",
//												// "Video on Switch not null else");
//											}
//
//										}
//									} catch (Exception e) {
//										// TODO: handle exception
//										e.printStackTrace();
//
//										// Log.d("EXC",
//										// "Exception on switch Video");
//
//									}

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
						if(from.equalsIgnoreCase(value.getMember_name())){
							if(WebServiceReferences.videoSSRC_total_list.contains(me.getKey())){
								WebServiceReferences.videoSSRC_total_list.remove(me.getKey());
							}
						}
					}
				}
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
		RelativeLayout	relativeLayout = new RelativeLayout(context);
		try {
			timer = new Timer();
			handler = new Handler();

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

			LinearLayout lf = new LinearLayout(context);
			lf.setOrientation(LinearLayout.VERTICAL);
			lf.setGravity(Gravity.CENTER);

			ImageView leftImage = new ImageView(context);
			leftImage.setLayoutParams(new LayoutParams(100, 300));
			leftImage.setImageResource(R.drawable.videocall);
			lf.setId(1);
			lf.addView(leftImage);
			/*
			 * TextView view=new TextView(this); view.setText("Android");
			 * lf.addView(view);
			 */
			relativeLayout.addView(lf, layoutParams1);

			LinearLayout lc = new LinearLayout(context);

			LayoutParams lcp = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			lc.setLayoutParams(lcp);

			for (int i = 0; i < 15; i++) {
				lc.addView(getDots());

			}

			relativeLayout.addView(lc, layoutParams3);

			LinearLayout lr = new LinearLayout(context);
			lr.setOrientation(LinearLayout.VERTICAL);
			lr.setGravity(Gravity.CENTER);

			ImageView rightImage = new ImageView(context);
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

		ImageView circleImage = new ImageView(context);
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
	public void onPause() {
		Log.i("NotesVideo","onPause");
//		if (AppMainActivity.commEngine != null) {
//			AppMainActivity.commEngine.setmDecodeFrame(false);
//		}

		onIsOnpause = true;

		if (glSurfaceView1 != null) {
			glSurfaceView1.onPause();
		}

		if (glSurfaceView2 != null) {
			glSurfaceView2.onPause();
		}

		if(glSurfaceView3 != null){
			glSurfaceView3.onPause();
		}
		super.onPause();
	}

	boolean onIsOnpause = false;

	public void onResume() {
		Log.i("NotesVideo","onResume");
		// if(ApplicationState.getApplicationCurrentState()!=ApplicationState.APPLICATION_STATE_ONLINE)
		// {
		// Toast.makeText(VideoCallScreen.this,getResources().getString(R.string.error),5000).show();
		//
		// }
		// else{
			AppReference.mainContext.stopService(new Intent(AppReference.mainContext, ChatHeadDrawerService.class));
		resetVideoViews(!preview_hided);
		if(mainHeader != null) {
			mainHeader.setVisibility(View.GONE);
		}

//		Activity parent = getActivity();
//		if(parent != null){
////			video_minimize.setVisibility(View.GONE);
//			if(min_outcall != null) {
//				min_outcall.setVisibility(View.GONE);
//			}
//
//			if(min_incall != null) {
//				min_incall.setVisibility(View.GONE);
//			}
//		}

			AppMainActivity.inActivity = context;
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
					isShowingMediaFailureIcon = false;
				}

				CallDispatcher.videoSpeakerMute(false);


				if (timer != null) {
					// Log.d("NDVC1", "On Resume....TIMER not null");
					timer.cancel();
//					layout = getCallConnectingView();
//					videosurface.addView(layout, layoutParamsf);
//					videosurface2.addView(relative_layout2, layoutParamsf2);
//					videosurface3.addView(relative_layout3, layoutParamsf3);
					// Log.d("NDVC1", "Added call connecting view..");
//					AnimationThread animationThread = new AnimationThread(
//							VideoCallScreen.this);
//					timer.schedule(animationThread, 500, 500);

					mReceiveVideo = false;
					mReceiveVideo2= false;
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

	private void resetVideoViews(boolean havepreview) {
		try {
				if (glSurfaceView1 != null) {
					glSurfaceView1.onPause();
				}

				if (glSurfaceView2 != null) {
					glSurfaceView2.onPause();
				}

				if (glSurfaceView3 != null) {
					glSurfaceView3.onPause();
				}
				remove_all_video_views();
				//			preview_frameLayout.removeView(pv);
				pv.stopPreview();
				pv = AppMainActivity.commEngine.getVideoPreview(context);
				pv.setZOrderOnTop(false);
				preview_frameLayout.addView(pv, 0);

				//			buddyframelayout01.removeView(buddysurfaceview_01);
				//			buddyframelayout0102.removeView(buddysurfaceview_0102);
				//			buddyframelayout02.removeView(buddysurfaceview_02);
				//			buddyframelayout03.removeView(buddysurfaceview_03);
				//
				//			buddyframelayout01.removeView(on_off1);
				//			buddyframelayout0102.removeView(on_off2);
				//			buddyframelayout02.removeView(on_off12);
				//			buddyframelayout03.removeView(on_off3);

			glSurfaceView1.destroyDrawingCache();
//                buddysurfaceview_0102.destroyDrawingCache();
			glSurfaceView2.destroyDrawingCache();
			glSurfaceView3.destroyDrawingCache();

				getGLSurfaceView(context);
//                getGLSurfaceView1(context);
				getGLSurfaceView2(context);
				getGLSurfaceView3(context);

				//			buddyframelayout01.addView(buddysurfaceview_01, surfaceview_layoutParams);
				//			buddyframelayout0102.addView(buddysurfaceview_0102, surfaceview_layoutParams);
				//			buddyframelayout02.addView(buddysurfaceview_02, surfaceview_layoutParams);
				//			buddyframelayout03.addView(buddysurfaceview_03, surfaceview_layoutParams);
				//
				//			buddyframelayout01.addView(on_off1);
				//			buddyframelayout0102.addView(on_off2);
				//			buddyframelayout02.addView(on_off12);
				//			buddyframelayout03.addView(on_off3);
				arrange_video_views(havepreview, WebServiceReferences.videoSSRC_total_list.size());
			glSurfaceView1.onResume();
//                buddysurfaceview_0102.onResume();
			glSurfaceView2.onResume();
			glSurfaceView3.onResume();

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	}

	private RelativeLayout showVideoFailure() {
		RelativeLayout	relativeLayout = new RelativeLayout(context);
		try {
//			relativeLayout = new RelativeLayout(this);
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
			FrameLayout.LayoutParams layoutParamsf_temp = new FrameLayout.LayoutParams(c1,
					LayoutParams.FILL_PARENT);

			relativeLayout.addView(ll, layoutParamsf_temp);

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
		Log.i("VideoCall", "width : " + w + " height :" + h);
	}

	@SuppressWarnings("unused")
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
                                                callType, sessionid,"");

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
								R.string.no_near_contacts), Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//

	public void showReminderAlert() {

		String ask = SingleInstance.mainContext.getResources().getString(
				R.string.need_call_hangup);

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

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}


	public void notifyVideoSSRC(String buddy, int ssrc){
		Log.i("NotesVideo", "came to notifyVideoSSRC : buddy => " + buddy + " ssrc => " + ssrc);
		if(videoSSRC.containsKey(buddy)){
			Log.i("NotesVideo","inside if");
			videoSSRC.put(buddy, ssrc);
			Log.i("NotesVideo", "videoSSRC size : " + videoSSRC.size());
		}
	}

	public void notifyNewSSRC(int ssrc){
		Log.i("NotesVideo","came to notifyNewSSRC");
		if(!WebServiceReferences.videoSSRC_total_list.contains(ssrc)) {
			WebServiceReferences.videoSSRC_total_list.add(ssrc);
		}

		handler.post(new Runnable() {
			@Override
			public void run() {
					resetVideoViews(!preview_hided);
			}
		});
	}

	public void notifyNumFeedChanged(){
		Log.i("NotesVideo","Came to notifyNumFeedChanged");
		handler.post(new Runnable() {
			@Override
			public void run() {
//				resetVideoViews();
//				refreshBuddyProfiles();
				resetVideoViews(!preview_hided);
			}
		});

	}

	public class VideoOnOffTimerTask extends TimerTask {

		private String buddy_name;
		private boolean onoff;
		public VideoOnOffTimerTask(String buddy_name, boolean on_off) {
			this.buddy_name = buddy_name;
			this.onoff = on_off;
		}

		@Override
		public void run() {
			Log.i("OnOff","VideoOnOffTimerTask run : "+buddy_name);
			onOffVideo(buddy_name,onoff);
		}
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

					for (BuddyInformationBean temp : total_buddyList) {
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

					if (state != null && state.equalsIgnoreCase("Offline")
							|| state.equals("Stealth")
							|| state.equalsIgnoreCase("pending")
							|| state.equalsIgnoreCase("Virtual")
							|| state.equalsIgnoreCase("airport")) {
						if (WebServiceReferences.running) {
							CallDispatcher.pdialog = new ProgressDialog(context);
							objCallDispatcher.showprogress(CallDispatcher.pdialog, context);

							String[] res_info = new String[3];
							res_info[0] = CallDispatcher.LoginUser;
							res_info[1] = user;
							if (state.equals("Offline") || state.equals("Stealth"))
								res_info[2] = objCallDispatcher
										.getdbHeler(context)
										.getwheninfo(
                                                "select cid from clonemaster where cdescription='Offline'");
							else
								res_info[2] = "";

							WebServiceReferences.webServiceClient
									.OfflineCallResponse(res_info);
						}

					} else {
						if (state != null && !state.equalsIgnoreCase("pending")) {
//							SingleInstance.parentId = record_TransactionBean.getParentID();
							objCallDispatcher.MakeCallFromCallHistory(caseid,
									user, context, record_TransactionBean, 2, feature);
							con_scr_opened = con_scr_opened + 1;
						}

					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void finishVideocallScreen() {
		Log.i("Recording", "finishVideocallScreen");
		try {
//			AppReference.mainContext.startRecording();
				AppReference.mainContext.stopService(new Intent(AppReference.mainContext, ChatHeadDrawerService.class));
			if (SingleInstance.instanceTable.containsKey("callactivememberslist")) {
				CallActiveMembersList activeMembersList = (CallActiveMembersList) SingleInstance.instanceTable.get("callactivememberslist");
				activeMembersList.finishActivity();
			}

			if (SingleInstance.instanceTable.containsKey("callscreen")) {
				SingleInstance.instanceTable.remove("callscreen");
				Log.e("note", "Call screen instance removed ACS!!");
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
			preview_hided = false;
			CallDispatcher.isCallInitiate = false;
			CallDispatcher.videoScreenVisibleState = false;
			AppMainActivity.commEngine.enable_disable_VideoPreview(true);
			WebServiceReferences.videoSSRC_total.clear();
			WebServiceReferences.videoSSRC_total_list.clear();
			WebServiceReferences.removed_videoSSRC_list.clear();

			CallDispatcher.conConference.clear();
			CallDispatcher.issecMadeConference = false;
			rootView = null;

			objCallDispatcher.stopRingTone();
			objCallDispatcher.isHangUpReceived = false;
			objCallDispatcher.alConferenceRequest.clear();
			CallDispatcher.conferenceMembersTime.clear();
			CallDispatcher.currentSessionid = null;

//		final DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

			if (this.mWakeLock != null && this.mWakeLock.isHeld())
				this.mWakeLock.release();
			lock.reenableKeyguard();

			objCallDispatcher.startPlayer(context);

			FragmentManager fm =
					AppReference.mainContext.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
//		ContactsFragment contactsFragment = ContactsFragment
//				.getInstance(context);
			ft.replace(R.id.activity_main_content_fragment,
					AppReference.bacgroundFragment);
			ft.commitAllowingStateLoss();
//			video_minimize.setVisibility(View.GONE);
			AppReference.mainContext.openNonClosedActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showCallHistory()
	{
		objCallDispatcher.showCallHistoryWithoutRecord(sessionid, "VC");
//		objCallDispatcher.showCallHistory(sessionid , "VC");
//		try {
//			mHandler=new Handler();
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
//			String file = Environment
//					.getExternalStorageDirectory()
//					+ "/COMMedia/CallRecording/"
//					+ sessionid + ".wav";
//			Log.d("Stringpath", "mediapath--->" + file);
//					File newfile=new File(file);
//
//			if (mPlayer.isPlaying()) {
//				mPlayer.pause();
//				play_button.setBackgroundResource(R.drawable.play);
//			} else {
//				play_button.setBackgroundResource(R.drawable.audiopause);
//				if(newfile.exists())
//				playAudio(file, 0);
//
//			}
//
//			int position=0;
//					if(newfile.exists()) {
//						if (position == mPlayingPosition) {
//							mProgressUpdater.mBarToUpdate = seekBar1;
//							mProgressUpdater.tvToUpdate = txt_time;
//							mHandler.postDelayed(mProgressUpdater, 100);
//						} else {
//
//							try {
//								Log.d("Stringpath", "mediapath--->");
//								seekBar1.setProgress(0);
//								MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//								mmr.setDataSource(file);
//								String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//								mmr.release();
//								String min, sec;
//								min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
//								sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
//								if (Integer.parseInt(min) < 10) {
//									min = 0 + String.valueOf(min);
//								}
//								if (Integer.parseInt(sec) < 10) {
//									sec = 0 + String.valueOf(sec);
//								}
//								txt_time.setText(min + ":" + sec);
////                            audio_tv.setText(duration);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//							seekBar1.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//							seekBar1.setProgress(0);
//							if (mProgressUpdater.mBarToUpdate == seekBar1) {
//								//this progress would be updated, but this is the wrong position
//								mProgressUpdater.mBarToUpdate = null;
//							}
//						}
//					}
//		}
//
//	});
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
//					intentComponent.putExtra("audiocall",false);
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
//					intentComponent.putExtra("audiocall",false);
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

	void addShowHideListener() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//		} else {
			Intent serviceIntent = new Intent(getActivity(), ChatHeadDrawerService.class);
			serviceIntent.putExtra("sview", 2);
			serviceIntent.putExtra("callscreen", "VCS");
			getActivity().startService(serviceIntent);
//		}
		isMinimize=true;
		FragmentManager fm =
				AppReference.mainContext.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		ContactsFragment contactsFragment = ContactsFragment
//				.getInstance(context);
		ft.replace(R.id.activity_main_content_fragment,
				AppReference.bacgroundFragment);
		ft.commitAllowingStateLoss();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			video_minimize.setVisibility(View.VISIBLE);
//		}
		mainHeader.setVisibility(View.VISIBLE);
		AppReference.mainContext.openNonClosedActivity();
	}
    class PlaybackUpdater implements Runnable {
	public SeekBar mBarToUpdate = null;
	public TextView tvToUpdate = null;

	@Override
	public void run() {
		if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
			Log.d("Mposition", "seekbar---->");
			double tElapsed = mPlayer.getCurrentPosition();
			int fTime = mPlayer.getDuration();
			double timeRemaining = fTime - tElapsed;
			double sTime = mPlayer.getCurrentPosition();

			String min, sec;
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

	public void re_get_buddyViews(BuddyInformationBean bib) {
		try {
			if(bib.getFirstname().equalsIgnoreCase(CallDispatcher.LoginUser)) {
				preview_hided = false;
			} else {
				WebServiceReferences.removed_videoSSRC_list.remove(Integer.valueOf(Integer.parseInt(bib.getVideo_ssrc())));
				WebServiceReferences.videoSSRC_total_list.add(Integer.parseInt(bib.getVideo_ssrc()));

				VideoThreadBean v_bean = WebServiceReferences.videoSSRC_total.get(Integer.parseInt(bib.getVideo_ssrc()));
				v_bean.setVideoRemoved(false);
				onOffVideo(v_bean.getMember_name(), true);
			}

			resetVideoViews(!preview_hided);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public void assignMediaStop(){
		hasRemoved = true;
		mRenderFrame = false;
	}

	private void remove_all_video_views(){
		try {
			if (((RelativeLayout) video_layouts).getChildCount() > 0) {
//				if(video_layouts.getChildCount() > 0) {
//					int child_count =video_layouts.getChildCount();
//					Log.i("Notes","child_count : "+child_count);
//					for(int i=child_count;i>0;i--) {
//						video_layouts.startAnimation(animSlideUp);
//						video_layouts.removeViewAt(i-1);
//					}
//				}
				((RelativeLayout) video_layouts).removeAllViews();
			}

			if (((FrameLayout) buddyframelayout01).getChildCount() > 0) {
				((FrameLayout) buddyframelayout01).removeAllViews();
			}

			if (((FrameLayout) buddyframelayout02).getChildCount() > 0) {
				((FrameLayout) buddyframelayout02).removeAllViews();
			}
			if (((FrameLayout) buddyframelayout03).getChildCount() > 0) {
				((FrameLayout) buddyframelayout03).removeAllViews();
			}
			if (((FrameLayout) preview_frameLayout).getChildCount() > 0) {
				((FrameLayout) preview_frameLayout).removeAllViews();
			}

			if(profile_view1.getChildCount()>0) {
				profile_view1.removeAllViews();
			}
			if(profile_view2.getChildCount()>0) {
				profile_view2.removeAllViews();
			}
			if(profile_view3.getChildCount()>0) {
				profile_view3.removeAllViews();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void arrange_video_views(boolean have_preview ,int totalviews){
		try {
			ViewGroup.LayoutParams onoff_layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			RelativeLayout.LayoutParams onoff_layoutParams_relative=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			onoff_layoutParams_relative.addRule(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			onoff_layoutParams_relative.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.ALIGN_RIGHT);
			if(have_preview) {
				if(chTimer != null) {
					chTimer.setVisibility(View.GONE);
				}
				if (totalviews == 0) {
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT);
					preview_frameLayout.setLayoutParams(lp);
					video_layouts.addView(preview_frameLayout);
//					preview_frameLayout.startAnimation(animSlideDown);
					preview_frameLayout.addView(onoff_preview);
					preview_frameLayout.addView(flipcamera);
				} else if (totalviews == 1) {
					RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							video_viewHeight/2);

					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							video_viewHeight/2);
					lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//					lp.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
					//                        lp.addRule(RelativeLayout.ALIGN_BASELINE, buddyframelayout01.getId());

//					preview_frameLayout.setLayoutParams(lp);

					if(have_video_frames(0)) {
						video_layouts.addView(buddyframelayout01, relative_layoutParams1);
//						buddyframelayout01.startAnimation(animSlideDown);
						buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
						buddyframelayout01.addView(on_off1,onoff_layoutParams);
						lp.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
					} else {
						video_layouts.addView(profile_view1,relative_layoutParams1);
//						profile_view1.startAnimation(animSlideDown);
						profile_view1.addView(buddyimageview1);
						profile_view1.addView(participant_name1);
						profile_view1.addView(on_off1,onoff_layoutParams_relative);
						lp.addRule(RelativeLayout.BELOW, profile_view1.getId());
						set_profile_image(0);
					}
					preview_frameLayout.setLayoutParams(lp);
					video_layouts.addView(preview_frameLayout);
//					preview_frameLayout.startAnimation(animSlideDown);
					preview_frameLayout.addView(onoff_preview);
					preview_frameLayout.addView(flipcamera);
				} else if (totalviews == 2) {
					RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							video_viewHeight/3);
					RelativeLayout.LayoutParams relative_layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							video_viewHeight/3);
//					relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
					RelativeLayout.LayoutParams relative_layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							video_viewHeight/3);
//					relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());


					if(have_video_frames(0)) {
						video_layouts.addView(buddyframelayout01, relative_layoutParams1);
						buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
						buddyframelayout01.addView(on_off1,onoff_layoutParams);
						relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
					} else {
						video_layouts.addView(profile_view1, relative_layoutParams1);
						profile_view1.addView(buddyimageview1);
						profile_view1.addView(participant_name1);
						profile_view1.addView(on_off1,onoff_layoutParams_relative);
						relative_layoutParams2.addRule(RelativeLayout.BELOW, profile_view1.getId());
						set_profile_image(0);
					}

					if(have_video_frames(1)) {
						video_layouts.addView(buddyframelayout02, relative_layoutParams2);
						buddyframelayout02.addView(glSurfaceView2,surfaceview_layoutParams);
						buddyframelayout02.addView(on_off2,onoff_layoutParams);
						relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());
					} else {
						video_layouts.addView(profile_view2,relative_layoutParams2);
						profile_view2.addView(buddyimageview2);
						profile_view2.addView(participant_name2);
						profile_view2.addView(on_off2,onoff_layoutParams_relative);
						relative_layoutParams3.addRule(RelativeLayout.BELOW, profile_view2.getId());
						set_profile_image(1);
					}

					video_layouts.addView(preview_frameLayout, relative_layoutParams3);
					preview_frameLayout.addView(onoff_preview);
					preview_frameLayout.addView(flipcamera);

				} else if (totalviews == 3) {
					RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(video_viewWidth/2,
							video_viewHeight/2);
					RelativeLayout.LayoutParams relative_layoutParams2 = new RelativeLayout.LayoutParams(video_viewWidth/2,
							video_viewHeight/2);
//					relative_layoutParams2.addRule(RelativeLayout.RIGHT_OF, buddyframelayout01.getId());
					RelativeLayout.LayoutParams relative_layoutParams3 = new RelativeLayout.LayoutParams(video_viewWidth/2,
							video_viewHeight/2);
//					relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
					RelativeLayout.LayoutParams relative_layoutParams4 = new RelativeLayout.LayoutParams(video_viewWidth/2,
							video_viewHeight/2);
//					relative_layoutParams4.addRule(RelativeLayout.RIGHT_OF, buddyframelayout03.getId());
//					relative_layoutParams4.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());

					if(have_video_frames(0)) {
						video_layouts.addView(buddyframelayout01, relative_layoutParams1);
						buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
						buddyframelayout01.addView(on_off1,onoff_layoutParams);
						relative_layoutParams2.addRule(RelativeLayout.RIGHT_OF, buddyframelayout01.getId());
					} else {
						video_layouts.addView(profile_view1,relative_layoutParams1);
						profile_view1.addView(buddyimageview1);
						profile_view1.addView(participant_name1);
						profile_view1.addView(on_off1,onoff_layoutParams_relative);
						relative_layoutParams2.addRule(RelativeLayout.RIGHT_OF, profile_view1.getId());
						set_profile_image(0);
					}

					if(have_video_frames(1)) {
						video_layouts.addView(buddyframelayout02, relative_layoutParams2);
						buddyframelayout02.addView(glSurfaceView2, surfaceview_layoutParams);
						buddyframelayout02.addView(on_off2,onoff_layoutParams);
						relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());
					} else {
						video_layouts.addView(profile_view2,relative_layoutParams2);
						profile_view2.addView(buddyimageview2);
						profile_view2.addView(participant_name2);
						profile_view2.addView(on_off2,onoff_layoutParams_relative);
						relative_layoutParams3.addRule(RelativeLayout.BELOW, profile_view2.getId());
						set_profile_image(1);
					}

					video_layouts.addView(preview_frameLayout, relative_layoutParams3);
					preview_frameLayout.addView(onoff_preview);
					preview_frameLayout.addView(flipcamera);

					relative_layoutParams4.addRule(RelativeLayout.RIGHT_OF, preview_frameLayout.getId());
					relative_layoutParams4.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());
					relative_layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

					if(have_video_frames(2)) {
						video_layouts.addView(buddyframelayout03, relative_layoutParams4);
						buddyframelayout03.addView(glSurfaceView3, surfaceview_layoutParams);
						buddyframelayout03.addView(on_off3,onoff_layoutParams);
					} else {
						video_layouts.addView(profile_view3,relative_layoutParams4);
						profile_view3.addView(buddyimageview3);
						profile_view3.addView(participant_name3);
						profile_view3.addView(on_off3,onoff_layoutParams_relative);
						set_profile_image(2);
					}

//					video_layouts.addView(preview_frameLayout, relative_layoutParams4);
//					preview_frameLayout.addView(onoff_preview);
//					preview_frameLayout.addView(flipcamera);
				}
			} else {
				if(totalviews == 0) {
					if(chTimer != null) {
						chTimer.setVisibility(View.VISIBLE);
					}
				} else {
					if(chTimer != null) {
						chTimer.setVisibility(View.GONE);
					}
					if (totalviews == 1) {
						RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT);
						if (have_video_frames(0)) {
							video_layouts.addView(buddyframelayout01, relative_layoutParams1);
//							buddyframelayout01.startAnimation(animSlideDown);
							buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
							buddyframelayout01.addView(on_off1, onoff_layoutParams);
						} else {
							video_layouts.addView(profile_view1, relative_layoutParams1);
//							profile_view1.startAnimation(animSlideDown);
							profile_view1.addView(buddyimageview1);
							profile_view1.addView(participant_name1);
							profile_view1.addView(on_off1, onoff_layoutParams_relative);
							set_profile_image(0);
						}

					} else if (totalviews == 2) {
						RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								video_viewHeight / 2);
						RelativeLayout.LayoutParams relative_layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								video_viewHeight / 2);
						relative_layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//						relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
						//                        lp.addRule(RelativeLayout.ALIGN_BASELINE, buddyframelayout01.getId());

						if (have_video_frames(0)) {
							video_layouts.addView(buddyframelayout01, relative_layoutParams1);
							buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
							buddyframelayout01.addView(on_off1,onoff_layoutParams);
							relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
						} else {
							video_layouts.addView(profile_view1, relative_layoutParams1);
							profile_view1.addView(buddyimageview1);
							profile_view1.addView(participant_name1);
							profile_view1.addView(on_off1,onoff_layoutParams_relative);
							relative_layoutParams2.addRule(RelativeLayout.BELOW, profile_view1.getId());
							set_profile_image(0);
						}

						if (have_video_frames(1)) {
							video_layouts.addView(buddyframelayout02, relative_layoutParams2);
							buddyframelayout02.addView(glSurfaceView2, surfaceview_layoutParams);
							buddyframelayout02.addView(on_off2,onoff_layoutParams);
						} else {
							video_layouts.addView(profile_view2, relative_layoutParams2);
							profile_view2.addView(buddyimageview2);
							profile_view2.addView(participant_name2);
							profile_view2.addView(on_off2,onoff_layoutParams_relative);
							set_profile_image(1);
						}

					} else if (totalviews == 3) {
						RelativeLayout.LayoutParams relative_layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								video_viewHeight / 3);
						RelativeLayout.LayoutParams relative_layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								video_viewHeight / 3);
//						relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
						RelativeLayout.LayoutParams relative_layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
								video_viewHeight / 3);
//						relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());

						if (have_video_frames(0)) {
							video_layouts.addView(buddyframelayout01, relative_layoutParams1);
							buddyframelayout01.addView(glSurfaceView1, surfaceview_layoutParams);
							buddyframelayout01.addView(on_off1,onoff_layoutParams);
							relative_layoutParams2.addRule(RelativeLayout.BELOW, buddyframelayout01.getId());
						} else {
							video_layouts.addView(profile_view1, relative_layoutParams1);
							profile_view1.addView(buddyimageview1);
							profile_view1.addView(participant_name1);
							profile_view1.addView(on_off1,onoff_layoutParams_relative);
							relative_layoutParams2.addRule(RelativeLayout.BELOW, profile_view1.getId());
							set_profile_image(0);
						}

						if (have_video_frames(1)) {
							video_layouts.addView(buddyframelayout02, relative_layoutParams2);
							buddyframelayout02.addView(glSurfaceView2, surfaceview_layoutParams);
							buddyframelayout02.addView(on_off2,onoff_layoutParams);
							relative_layoutParams3.addRule(RelativeLayout.BELOW, buddyframelayout02.getId());
						} else {
							video_layouts.addView(profile_view2, relative_layoutParams2);
							profile_view2.addView(buddyimageview2);
							profile_view2.addView(participant_name2);
							profile_view2.addView(on_off2,onoff_layoutParams_relative);
							relative_layoutParams3.addRule(RelativeLayout.BELOW, profile_view2.getId());
							set_profile_image(1);
						}

						if (have_video_frames(2)) {
							video_layouts.addView(buddyframelayout03, relative_layoutParams3);
							buddyframelayout03.addView(glSurfaceView3, surfaceview_layoutParams);
							buddyframelayout03.addView(on_off3,onoff_layoutParams);
						} else {
							video_layouts.addView(profile_view3, relative_layoutParams3);
							profile_view3.addView(buddyimageview3);
							profile_view3.addView(participant_name3);
							profile_view3.addView(on_off3,onoff_layoutParams_relative);
							set_profile_image(2);
						}
					}
					RelativeLayout.LayoutParams preview_layoutParams = new RelativeLayout.LayoutParams(1,
							1);
					video_layouts.addView(preview_frameLayout, preview_layoutParams);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean have_video_frames(int i){
		int turn_ssrc = WebServiceReferences.videoSSRC_total_list.get(i);
		final int total_size = WebServiceReferences.videoSSRC_total_list.size();

		VideoThreadBean videoThreadBean = WebServiceReferences.videoSSRC_total.get(turn_ssrc);
		if (videoThreadBean != null && videoThreadBean.isVideoDisabled()) {
			return false;
		} else {
			return true;
		}
	}

	private void set_profile_image(int i){
		int turn_ssrc = WebServiceReferences.videoSSRC_total_list.get(i);
		final int total_size = WebServiceReferences.videoSSRC_total_list.size();

		VideoThreadBean videoThreadBean = WebServiceReferences.videoSSRC_total.get(turn_ssrc);
		if (videoThreadBean != null && videoThreadBean.isVideoDisabled()) {
			final String buddy_name = (WebServiceReferences.videoSSRC_total.get(turn_ssrc)).getMember_name();
			String full_name = buddy_name;
			boolean have_image = false;
			for (BuddyInformationBean buddyInformationBean : total_buddyList) {
				if (buddyInformationBean.getName().equalsIgnoreCase(buddy_name)) {
					String pic_path = buddyInformationBean.getProfile_picpath();
					if(buddyInformationBean.getFirstname() != null && buddyInformationBean.getLastname()!= null) {
						full_name =	buddyInformationBean.getFirstname() + " " + buddyInformationBean.getLastname();
					} else if (buddyInformationBean.getFirstname() != null){
						full_name =	buddyInformationBean.getFirstname();
					}

					if (pic_path == null || pic_path.length() == 0) {
						ProfileBean pBean = DBAccess.getdbHeler().getProfileDetails(buddy_name);
						if (pBean != null) {
							pic_path = pBean.getPhoto();
							Log.i("Join", "pic_path 2 : " + pic_path);
						}
					}
					if (pic_path != null && pic_path.length() > 0) {
						if (!pic_path.contains("COMMedia")) {
							pic_path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + pic_path;
						}
						have_image = true;
						if (i == 0) {
							imageLoader.DisplayImage(pic_path, buddyimageview1, R.drawable.icon_buddy_aoffline);
						} else if (i == 1) {
							imageLoader.DisplayImage(pic_path, buddyimageview2, R.drawable.icon_buddy_aoffline);
						} else if (i == 2) {
							imageLoader.DisplayImage(pic_path, buddyimageview3, R.drawable.icon_buddy_aoffline);
						}
					}
				}
			}
			if (i == 0) {
				if (!have_image) {
					imageLoader.DisplayImage("", buddyimageview1, R.drawable.icon_buddy_aoffline);
				}
				buddyimageview1.setVisibility(View.VISIBLE);
				participant_name1.setText(full_name);
			} else if (i == 1) {
				if (!have_image) {
					imageLoader.DisplayImage("", buddyimageview2, R.drawable.icon_buddy_aoffline);
				}
				buddyimageview2.setVisibility(View.VISIBLE);
				participant_name2.setText(full_name);
			} else if (i == 2) {
				if (!have_image) {
					imageLoader.DisplayImage("", buddyimageview3, R.drawable.icon_buddy_aoffline);
				}
				buddyimageview3.setVisibility(View.VISIBLE);
				participant_name3.setText(full_name);
			}
		}
	}
}
