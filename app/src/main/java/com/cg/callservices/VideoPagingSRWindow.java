package com.cg.callservices;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import org.audio.AudioProperties;
import org.core.VideoCallback;
import org.lib.model.BuddyInformationBean;
import org.lib.model.SignalingBean;
import org.util.GraphicsUtil;
import org.util.Queue;
import org.util.Utility;
import org.video.Preview;
import org.video.PreviewFrameSink;
import org.video.VideoFrameRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.callHistory.CallHistoryActivity;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.util.SingleInstance;

public class VideoPagingSRWindow extends Activity implements VideoCallback,
		com.cg.callservices.AnimationListener {

	private RelativeLayout layout;

	private ImageView[] view = null;

	private String receiver = null;

	private Timer timer = null;

	private Handler handler = null;

	private LinearLayout linearLayout;

	private RelativeLayout relativeLayout;

	private int orientation = 0;

	private int i = 0;

	private int c1 = 0;

	private boolean isDiscarded = false;

	private boolean isreject = false;

	private boolean mReceiveVideo = false;

	private FrameLayout.LayoutParams layoutParamsf;

	private String[] touserarray;

	private String[] autocallarray;

	private String[] starttimearray;

	private String[] endtimearray;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	private String record_path = null;

	private Utility utility = new Utility();

	private AlertDialog alert = null;

	private int noScrHeightSender;

	private String sessionid = null;

	private Handler videocall = null;

	private Preview pv = null;

	private boolean micmute = false;

	private Context context = null;

	private String strAlert;

	private CallDispatcher objCallDispatcher;

	public static Handler videoHandlerFullScreen = new Handler();

	private AudioProperties audioProperties = null;

	private boolean speaker = true;

	private String callType = null;

	private String mode;

	private Queue videoQueue;

	private VideoThread videoThread;

	private FrameLayout videopreview = null;

	private FrameLayout callControls = null;

	private FrameLayout receiverCallControls = null;

	public Handler videoHandler = new Handler();

	private Button loudSpeaker;

	private GLSurfaceView gls = null;

	private GLSurfaceView glSurfaceView = null;

	private VideoFrameRenderer frameRenderer = null;

	private PreviewFrameSink frameSink = null;

	private byte[] frame = null;

	private ByteBuffer frameBuffer = null;

	private boolean hasRemoved = false;

	private Handler videoBroadCastHandler;

	private String callerName;

	private KeyguardManager keyguardManager;

	private KeyguardLock lock;

	protected PowerManager.WakeLock mWakeLock;

	private HashSet<String> hsCallMembers = new HashSet<String>();

	private String strStartTime;

	private boolean selfHangup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

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

		handler = new Handler();

		keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
		lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();

		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"My Tag");
		this.mWakeLock.acquire();

		try {

			Log.d("test", "video paging window loaded");

			strStartTime = getCurrentDateTime();
			SingleInstance.instanceTable.put("callscreen", this);

			objCallDispatcher.startPlayer(context);

			mode = (String) getIntent().getStringExtra("mode");
			sessionid = (String) getIntent().getStringExtra("sessionid");
			callType = (String) getIntent().getStringExtra("calltype");
			receiver = (String) getIntent().getStringExtra("receive");
			callerName = (String) getIntent().getStringExtra("buddy");
			CallDispatcher.currentSessionid = sessionid;
			if (callType.equals("SS")) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			if (mode.equals("0")) {// Receiver
				gls = getGLSurfaceView(VideoPagingSRWindow.this);

				// Make the Message msg null after processing it
				// Make the Bundle bun null after processing it
				videoHandler = new Handler() {

					public void handleMessage(Message result) {
						// Log.d("one", "videocall");
						super.handleMessage(result);
						Bundle bundle = (Bundle) result.obj;
						try {
							if (bundle.containsKey("img")) {

								Log.d("video",
										"******************* From img *******************");
								frameSink.getFrameLock().lock();
								frameBuffer.position(0);
								// frameRenderer.setPreviewFrameWidth(obtainWidth);
								// frameRenderer.setPreviewFrameHeight(obtainHeight);

								frameBuffer.put(bundle.getByteArray("img"));
								frameBuffer.position(0);
								frameSink.setNextFrame(frameBuffer);
								frameSink.getFrameLock().unlock();
								glSurfaceView.requestRender();
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				};

				videoQueue = new Queue();
				videoThread = new VideoThread(videoQueue);
				videoThread.setHandler(videoHandler);
				videoThread.start();
				AppMainActivity.commEngine.setVideoCallback(this);

				receiversideView();

			} else if (mode.equals("1")) {// Sender

				sendersideView();

			}

			//
			videoHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Log.i("thread", "Came to handler.....");
						Log.i("thread", "Came to handler.....preview" + pv);

						if (pv != null) {
							Log.i("thread", "Came to handler.....pview status"
									+ pv.getPreviewStatus());
							if (!pv.getPreviewStatus()) {
								// Log.d("CAM", "close call");
								try {
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.unable_send_video),
											Toast.LENGTH_LONG).show();
									//
									Message msg = new Message();

									Bundle bun = new Bundle();
									bun.putString("action", "leave");
									msg.obj = bun;
									if (videoBroadCastHandler != null) {
										videoBroadCastHandler.sendMessage(msg);
									}
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

								//
							} else {
								// Log.d("CAM", "Donot call");
							}

						} else {
							// Log.d("CAM", "preview is null");
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}, 5000);

			Log.i("thread", "conf menbers size...."
					+ CallDispatcher.conferenceMembers.size());
			Log.i("thread", "is hangup received"
					+ objCallDispatcher.isHangUpReceived);
			// Log.d("RACE", " Race CAse.. on create ,.,....");
			if (objCallDispatcher.isHangUpReceived) {
				// Log.d("RACE",
				// " Race CAse.. Condition before new concept ,.,....");
				receivedHangUp();
				// finish();

			}

			else if (receiver.equalsIgnoreCase("true")
					&& CallDispatcher.conferenceMembers.size() == 0) {
				// Log.d("RACE", " Race CAse.. new concept Execcuted,.,....");
				receivedHangUp();
			}

			Log.d("test", "video paging window loaded");

			//
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// }
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
								Log.e("thread", "@@@@@@@@@@@@@@ finish10");
								finish();
								return;
							}
						});
		alert = builder.create();
		alert.show();
	}

	private String getCurrentDateTime() {
		Date curDate = new Date();
		// SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		Log.d("Test",
				"VideoPaging Date time+++________   " + sdf.format(curDate));

		return sdf.format(curDate).toString();
	}

	public void addBroadcastingMembers(String name) {
		try {
			if (!hsCallMembers.contains(name))
				hsCallMembers.add(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void receivedHangUp() {
		// Log.d("RACE", "Handling Race CAse..######## ");
		Log.i("thread", "came to received hangup....");

		//
		try {
			// if (WebServiceReferences.contextTable.containsKey("callscreen"))
			// {
			// WebServiceReferences.contextTable.remove("callscreen");
			// }
			// finish();
			objCallDispatcher.currentSessionid = null;
			CallDispatcher.conferenceMembers.clear();

			if (pv != null) {
				pv.stopPreview();
				pv = null;
			}

			//
			objCallDispatcher.isHangUpReceived = false;

			enterCallHistory();
			Log.e("thread", "@@@@@@@@@@@@@@ finish1");
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public GLSurfaceView getGLSurfaceView(Context context) {
		glSurfaceView = new GLSurfaceView(context);
		frameRenderer = new VideoFrameRenderer();
		frameRenderer.setPreviewFrameSize(512,
				WebServiceReferences.VIDEO_WIDTH,
				WebServiceReferences.VIDEO_HEIGHT);
		glSurfaceView.setRenderer(frameRenderer);
		frameSink = frameRenderer;
		frame = new byte[WebServiceReferences.VIDEO_WIDTH
				* WebServiceReferences.VIDEO_WIDTH * 3];
		frameBuffer = GraphicsUtil.makeByteBuffer(frame.length);
		return glSurfaceView;
	}

	private void sendersideView() {
		try {
			CallDispatcher.openSenderScreen = true;
			if (callType.equals("VP")) {
				// displayName.setText("Video Talkie with");
				String buddy = CallDispatcher.conferenceMembers.get(0);
				hsCallMembers.add(buddy);
				// tvTitle.setText(buddy);
			} else {
				String buddy = CallDispatcher.conferenceMembers.get(0);
				hsCallMembers.add(buddy);
				// displayName.setText("Video Broadcast");
				// tvTitle.setText("");
			}

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			// int noScrHeight = displaymetrics.heightPixels;
			//
			// Log.i("video", "noScrHeight:" + noScrHeight);

			int nosHeight = displaymetrics.heightPixels;
			int nosWidth = displaymetrics.widthPixels;

			// Log.i("video", "no Height:" + nosHeight);
			// Log.i("video", "no Width:" + nosWidth);

			if (nosHeight > nosWidth) {
				noScrHeightSender = nosWidth;
			} else {
				noScrHeightSender = nosHeight;
			}

			audioProperties = new AudioProperties(this);
			context = this;
			WebServiceReferences.contextTable.put("VideoPagingSender", context);

			LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout screen = (RelativeLayout) inflateLayout.inflate(
					R.layout.videopagesender, null);
			screen.setGravity(Gravity.CENTER);
			callControls = (FrameLayout) screen.findViewById(R.id.preview);
			LinearLayout icons = (LinearLayout) screen
					.findViewById(R.id.icons1);

			pv = AppMainActivity.commEngine.getVideoPreview(this);
			//
			double c = noScrHeightSender * 1.1;
			int c1 = (int) c;
			// Log.d("NN", "height width " + c1);
			FrameLayout.LayoutParams layoutParamsf = new FrameLayout.LayoutParams(
					c1, LayoutParams.WRAP_CONTENT);

			callControls.addView(pv, 0, layoutParamsf);
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// Toast.makeText(
					// context,
					// "Preview Width :" + pv.getPreviewWidth()
					// + " Preview Height :"
					// + pv.getPreviewHeight(), Toast.LENGTH_LONG)
					// .show();
				}
			}, 1000);

			Button add = (Button) screen.findViewById(R.id.add);

			if (callType.equals("VP")) {
				add.setVisibility(View.GONE);
			}

			add.setKeepScreenOn(true);

			add.setBackgroundResource(R.drawable.add_icon_video);
			add.setPadding(0, 0, 20, 0);
			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Log.d("Add", "Add Buddies will show...");
					if (alert == null) {
						// Log.d("Add", "call show buddies on null");
						ShowOnlineBuddies("VC");
					} else if (!alert.isShowing()) {
						// Log.d("Add",
						// "call show buddies on not showing alert");
						ShowOnlineBuddies("VC");
					} else {
						// Log
						// .d("Add",
						// "call show buddies on not showing because alert is already showing");
					}

				}
			});

			Button hangup = (Button) screen.findViewById(R.id.stop);

			hangup.setBackgroundResource(R.drawable.hangupx);
			hangup.setPadding(0, 0, 20, 0);
			hangup.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					showHangUpAlert();
				}
			});

			final Button mic = (Button) screen.findViewById(R.id.mic);

			// mic.setBackgroundResource(R.drawable.mic_unmute);
			mic.setBackgroundResource(R.drawable.mic_video);
			speaker = true;
			// audioProperties.setSpeakerphoneOn(speaker);

			mic.setPadding(0, 0, 20, 0);
			mic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// String title = mic.getHint().toString();
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
					}

				}
			});

			final Button buddies = (Button) screen.findViewById(R.id.buddies);

			buddies.setBackgroundResource(R.drawable.buddies_video);
			buddies.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (alert == null) {

						showConnectedBuddies();
					} else if (!alert.isShowing()) {
						showConnectedBuddies();
					}

				}
			});
			if (callType.equals("VP")) {
				icons.removeView(buddies);
				icons.removeView(add);
			}

			videocall = new Handler() {
				public void handleMessage(Message result) {
					super.handleMessage(result);
					/* Code Clean up */
					// Bundle bundle = (Bundle) result.obj;

					try {
						if (pv != null) {
							pv.stopPreview();
							pv = null;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					Log.e("thread", "@@@@@@@@@@@@@@ finish2");
					finish();

				}
			};

			// Make the Message msg null after processing it
			// Make the Bundle bun null after processing it

			videoBroadCastHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					Bundle bun = (Bundle) msg.obj;

					String action = bun.getString("action");
					try {

						if (action.equals("leave")) {

							//
							try {

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
									sb.setCallType("VBC");

									AppMainActivity.commEngine.hangupCall(sb);
								}
								/*
								 * members.clear();
								 */

								// Log.e("callInfo",
								// "Video call Screen Leave received");
								// WebServiceReferences.contextTable.remove("callscreen");
								// finish();
								objCallDispatcher.currentSessionid = null;
								CallDispatcher.conferenceMembers.clear();

							} catch (Exception e) {
								// TODO: handle exception
							}
							if (pv != null) {
								pv.stopPreview();
								pv = null;
							}
							enterCallHistory();
							Log.e("thread", "@@@@@@@@@@@@@@ finish3");
							finish();

						} else if (action.equals("update")) {
							try {

								String buddy = bun.getString("buddy");
								boolean isConferencemembers = CallDispatcher.conferenceMembers
										.contains(buddy);

								CallDispatcher.conferenceMembers.remove(buddy);
								CallDispatcher.buddySignall.remove(buddy);

								if (strAlert != null) {

									if (strAlert.equals("showConnectedBuddies")) {

										if (alert.isShowing()) {
											alert.dismiss();
											showConnectedBuddies();
										}
									}

								}

								// used to send Update to server...

								if (CallDispatcher.conferenceMembers.size() > 0) {
									if (!isConferencemembers) {
										objCallDispatcher
												.notifyCallHistoryToServer(
														objCallDispatcher.LoginUser,
														buddy,
														"213",
														objCallDispatcher.sessId,
														getCurrentDateTime(),
														getCurrentDateTime());
									} else {

										enterCallHistoryOnSingleuser(buddy);

									}
								}

							} catch (Exception e) {
								// TODO: handle exception
								CallDispatcher.conferenceMembers.clear();
								CallDispatcher.buddySignall.clear();

							}

							receivedHangUpx();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			};

			setContentView(screen);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void receivedHangUpx() {
		Log.e("thread", "@@@@@@@@@@@@@@ came to received hangupx");
		videoHandler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					Log.e("thread", "@@@@@@@@@@@@@@ came to video handler");
					if (mode.equals("1")) {
						if (CallDispatcher.conferenceMembers.size() == 0
								&& CallDispatcher.conferenceRequest.size() == 0) {
							if (pv != null) {
								pv.stopPreview();
								pv = null;
							}
							// WebServiceReferences.contextTable.remove("callscreen");
							// finish();
							objCallDispatcher.currentSessionid = null;
							enterCallHistory();
							Log.e("thread", "@@@@@@@@@@@@@@ finish4");
							finish();
						}
					} else {
						//
						// Log.e("callInfo",
						// "Video call Screen Leave received");
						// WebServiceReferences.contextTable.remove("callscreen");
						objCallDispatcher.currentSessionid = null;
						CallDispatcher.conferenceMembers.clear();
						CallDispatcher.buddySignall.clear();
						if (pv != null) {
							pv.stopPreview();
							pv = null;
						}
						enterCallHistory();
						Log.e("thread", "@@@@@@@@@@@@@@ finish5");
						finish();
						//

					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});

	}

	private String[] returnBuddies() {

		String arr[] = CallDispatcher.conferenceMembers
				.toArray(new String[CallDispatcher.conferenceMembers.size()]);
		return arr;
	}

	private void showConnectedBuddies() {
		try {

			strAlert = "showConnectedBuddies";

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.video_bc_people));

			final CharSequence[] choiceList = returnBuddies();
			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setItems(choiceList, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			alert = builder.create();
			alert.show();

			alert.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// isaddPeopleOpened = false;
					strAlert = null;
				}
			});

			alert.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					// isaddPeopleOpened = false;
					strAlert = null;

				}
			});
		} catch (Exception e) {
			// Log.e("Exc", "On Show OnLine buddy Exception:" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void forceHAngUp() {
		videoHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isDiscarded = true;
				Message msg = new Message();

				Bundle bun = new Bundle();
				bun.putString("action", "leave");
				msg.obj = bun;
				if (videoBroadCastHandler != null)
					videoBroadCastHandler.sendMessage(msg);

			}
		});
	}

	public void ShowOnlineBuddies(final String callType) {
		try {

			// isaddPeopleOpened = true;
			String[] members = objCallDispatcher.getOnlineBuddys();
			ArrayList<String> membersList = new ArrayList<String>();
			for (int i = 0; i < members.length; i++) {
				if (!CallDispatcher.conferenceMembers.contains(members[i])) {
					membersList.add(members[i]);
				}
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();
			builder.setTitle(SingleInstance.mainContext.getResources()
					.getString(R.string.add_people));

			// Log.d("SOB", "showonline buddies");
			final CharSequence[] choiceList = membersList
					.toArray(new String[membersList.size()]);
			if (choiceList == null) {
				// Log.d("SOB", "showonline buddies null");
			} else {
				// Log.d("SOB", "showonline buddies not null");
			}
			// System.out.println(choiceList);

			int selected = -1; // does not select anything

			builder.setItems(choiceList, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					BuddyInformationBean bib = null;

					for (BuddyInformationBean temp : ContactsFragment
							.getBuddyList()) {
						if (!temp.isTitle()) {
							if (temp.getName().equalsIgnoreCase(
									choiceList[which].toString())) {
								bib = temp;
								break;
							}
						}
					}
					if (bib != null) {
						SignalingBean sb = new SignalingBean();
						/* Static Clean up */
						sb.setFrom(objCallDispatcher.LoginUser);
						sb.setSessionid(sessionid);
						sb.setTo(choiceList[which].toString());
						sb.setType("0");
						sb.setToSignalPort(bib.getSignalingPort());
						sb.setResult("0");
						sb.setTopublicip(bib.getExternalipaddress());
						sb.setTolocalip(bib.getLocalipaddress());
						sb.setToSignalPort(bib.getSignalingPort());
						// finish();
						sb.setCallType("VBC");
						// CallDispatcher.conferenceMembers
						// .add(choiceList[which].toString());
						// CallDispatcher.buddySignall.put(choiceList[which]
						// .toString(), sb);
						AppMainActivity.commEngine.makeCall(sb);
						CallDispatcher.conferenceRequest.put(
								choiceList[which].toString(), sb);
						//
					}
					alert.dismiss();

				}
			});
			alert = builder.create();

			if (membersList.size() > 0) {
				alert.show();
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.sorry_no_online_users), 2000).show();
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
		} catch (Exception e) {
			// Log.e("Exc", "On Show OnLine buddy Exception:" + e.getMessage());
			e.printStackTrace();
		}

	}

	private void receiversideView() {
		try {

			Log.e("thread", "@@@@@@@@@@@@@@ came to receiver side view...");
			layout = getCallConnectingView();
			try {
				if (callType.equals("VP")) {
					// displayName.setText("Video Talkie with");

					String buddy = CallDispatcher.conferenceMembers.get(0);
					hsCallMembers.add(buddy);

					// tvTitle.setText(buddy);
				} else {
					// displayName.setText("Video Broadcast from");

					String buddy = CallDispatcher.conferenceMembers.get(0);
					hsCallMembers.add(buddy);

					// tvTitle.setText(buddy);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

			int nosHeight = displaymetrics.heightPixels;
			/* Code Clean up */
			int nosWidth = displaymetrics.widthPixels;
			// Log.i("video", "no Height:" + nosHeight);
			// Log.i("video", "no Width:" + nosWidth);

			int noScrHeight;
			if (nosHeight > nosWidth) {
				noScrHeight = nosWidth;
			} else {
				noScrHeight = nosHeight;
			}

			audioProperties = new AudioProperties(this);
			context = this;
			WebServiceReferences.contextTable.put("VideoPagingReceiver",
					context);
			sessionid = (String) getIntent().getStringExtra("sessionid");

			LayoutInflater inflateLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout screen = (RelativeLayout) inflateLayout.inflate(
					R.layout.videopagereceiver, null);
			screen.setGravity(Gravity.CENTER);

			/*
			 * FrameLayout callControls = (FrameLayout) screen
			 * .findViewById(R.id.Video);
			 */

			callControls = (FrameLayout) screen.findViewById(R.id.Video);

			/* Code Clean up */
			// LinearLayout icons = (LinearLayout)
			// screen.findViewById(R.id.icons);
			//
			gls = getGLSurfaceView(VideoPagingSRWindow.this);

			glSurfaceView.setZOrderOnTop(false);
			double c = noScrHeight * 1.1;
			c1 = (int) c;
			// Log.d("NN", "height width " + c1);
			layoutParamsf = new FrameLayout.LayoutParams(c1,
					LayoutParams.FILL_PARENT);
			// callControls.addView(layout);
			AnimationThread animationThread = new AnimationThread(
					VideoPagingSRWindow.this);
			timer.schedule(animationThread, 500, 500);
			callControls.addView(layout, 0, layoutParamsf);

			Button hangUp = (Button) screen.findViewById(R.id.stop);
			hangUp.setBackgroundResource(R.drawable.hangupx);
			hangUp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Log.d("test", "login "
					// + CallDispatcher.conferenceMembers.size());

					showHangUpAlert();
				}
			});

			loudSpeaker = (Button) screen.findViewById(R.id.loud);
			loudSpeaker.setBackgroundResource(R.drawable.loudspeaker_video);
			// speaker = true;
			// audioProperties.setSpeakerphoneOn(speaker);
			loudSpeaker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if (!speaker) {
							speaker = true;
							loudSpeaker
									.setBackgroundResource(R.drawable.loudspeaker_video);
						} else if (speaker) {

							speaker = false;
							loudSpeaker
									.setBackgroundResource(R.drawable.headphone_video);

						}
						audioProperties.setSpeakerphoneOn(speaker);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});
			if (!audioProperties.isHeadsetOn()) {
				audioProperties.setSpeakerphoneOn(speaker);
			} else {
				// audioProperties.setSpeakerphoneOn(speaker);
				// loudSpeaker.setBackgroundResource(R.drawable.headset);
				loudSpeaker.setBackgroundResource(R.drawable.loudspeaker_video);
			}

			videocall = new Handler() {
				public void handleMessage(Message result) {
					super.handleMessage(result);
					/* Code Clean up */
					// Bundle bundle = (Bundle) result.obj;

					Log.e("thread",
							"@@@@@@@@@@@@@@ came to receiver side view video call handler....");
					if (pv != null) {
						pv.stopPreview();
						pv = null;
					}
					enterCallHistory();
					Log.e("thread", "@@@@@@@@@@@@@@ finish6");
					finish();

				}
			};

			videoHandlerFullScreen = new Handler() {

				public void handleMessage(Message result) {
					// Log.d("one", "videocall");
					super.handleMessage(result);
					Bundle bundle = (Bundle) result.obj;
					try {
						// Log.d("spe", "member  videocall");
						if (bundle.containsKey("spemute")) {
							// Log.d("spe", "member ");
							// Log.d("spe", "member " + sessionid);
							String strSessionID = bundle.getString("id");
							boolean spkmute = bundle.getBoolean("speaker");
							for (int i = 0; i < CallDispatcher.conferenceMembers
									.size(); i++) {
								// Log.d("spe", "member "
								// + CallDispatcher.conferenceMembers.get(i));
								// Log.d("spe", "member " + strSessionID);
								// Log.d("spe", "member " + spkmute);
								AppMainActivity.commEngine
										.speakerMute(
												CallDispatcher.conferenceMembers
														.get(i), strSessionID,
												spkmute);
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			};

			// mal.addView(callControls, new MyAbsoluteLayout.LayoutParams(
			// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 0, 0));

			videoBroadCastHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					// Log.e("callInfo", "Video call Screen Leave received");

					//

					Bundle bun = (Bundle) msg.obj;

					String action = bun.getString("action");

					if (action != null && action.equals("leave")) {

						//
						try {

							// Log.d("test", "From leave/bye");
							for (int i = 0; i < CallDispatcher.conferenceMembers
									.size(); i++) {
								// Log.d("login", "login");

								String buddy = CallDispatcher.conferenceMembers
										.get(i);
								// Log.d("test", "members " + buddy);
								SignalingBean sb = CallDispatcher.buddySignall
										.get(buddy);
								// Log.d("login", "loginuser"
								// + objCallDispatcher.LoginUser);
								sb.setFrom(objCallDispatcher.LoginUser);
								sb.setTo(buddy);
								sb.setType("3");
								sb.setCallType("VBC");
								AppMainActivity.commEngine.hangupCall(sb);
							}
							/*
							 * members.clear();
							 */

							// Log.e("callInfo",
							// "Video call Screen Leave received");
							// WebServiceReferences.contextTable.remove("callscreen");
							// // finish();
							// CallDispatcher.currentSessionid = null;
							// CallDispatcher.conferenceMembers.clear();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					//
					// WebServiceReferences.contextTable.remove("callscreen");
					objCallDispatcher.currentSessionid = null;
					CallDispatcher.conferenceMembers.clear();
					CallDispatcher.buddySignall.clear();
					if (pv != null) {
						pv.stopPreview();
						pv = null;
					}
					enterCallHistory();
					Log.e("thread", "@@@@@@@@@@@@@@ finish7");
					finish();

				}
			};

			setContentView(screen);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void enterCallHistory() {

		try {

			if (mode.equals("0")) {// Receiver

				String membConnected = getmembersConnected();

				if (callType.equals("VP")) {
					/* Static Clean up */
					String callTypeToServer = "202";
					if (isDiscarded) {
						callTypeToServer = "232";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, touserarray, callTypeToServer,
							sessionid, starttimearray, endtimearray,
							autocallarray);

				} else if (callType.equals("VBC")) {
					/* Static Clean up */
					String callTypeToServer = "203";
					if (isDiscarded) {
						callTypeToServer = "233";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, touserarray, callTypeToServer,
							sessionid, starttimearray, endtimearray,
							autocallarray);
				}
			} else {

				if (callType.equals("VP")) {
					/* Static Clean up */
					String callTypeToServer = "202";
					if (isDiscarded) {
						callTypeToServer = "232";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							objCallDispatcher.LoginUser, touserarray,
							callTypeToServer, sessionid, starttimearray,
							endtimearray, autocallarray);

				} else if (callType.equals("VBC")) {
					/* Static Clean up */
					String callTypeToServer = "203";
					if (isDiscarded) {
						callTypeToServer = "233";
					}

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							objCallDispatcher.LoginUser, touserarray,
							callTypeToServer, sessionid, starttimearray,
							endtimearray, autocallarray);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void enterMissedCall() {
		// Log.d("WIFI", "enterMissedCall() ");
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
						objCallDispatcher.LoginUser, toarray, "213", sessionid,
						timearray, timearray, autocallarray);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void enterCallHistoryOnSingleuser(String buddy) {

		hsCallMembers.remove(buddy);

		String[] toarray = new String[1];
		toarray[0] = buddy;
		String[] starttimearray = new String[1];
		String[] endtimearray = new String[1];
		endtimearray[0] = getCurrentDateTime();
		String[] autocallarray = new String[1];
		autocallarray[0] = "0";

		try {

			starttimearray[0] = CallDispatcher.conferenceMembersTime.get(buddy);
			if (starttimearray[0] == null) {
				starttimearray[0] = strStartTime;
			}

			if (mode.equals("0")) {// Receiver

				// String membConnected=getmembersConnected();

				if (callType.equals("VBC")) {
					/* Static Clean up */

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							callerName, toarray, "203", sessionid,
							starttimearray, endtimearray, autocallarray);
				}
			} else {

				if (callType.equals("VBC")) {
					/* Static Clean up */

					objCallDispatcher.notifyCallHistoryToServerfromcallscreen(
							objCallDispatcher.LoginUser, toarray, "203",
							sessionid, starttimearray, endtimearray,
							autocallarray);
				}

			}

		} catch (Exception e) {
		}

	}

	public void resetCallDuration() {
		// Log.d("RCD", "call duraion reseted");
		strStartTime = getCurrentDateTime();

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
				endtimearray[0] = getCurrentDateTime();

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

	public RelativeLayout getCallConnectingView() {

		try {
			timer = new Timer();
			handler = new Handler();
			relativeLayout = new RelativeLayout(this);
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
			rightImage.setLayoutParams(new LayoutParams(100, 300));
			rightImage.setImageResource(R.drawable.videocall);
			lr.setId(2);
			lr.addView(rightImage);
			/*
			 * TextView v=new TextView(this); v.setText("PC"); lr.addView(v);
			 */
			relativeLayout.addView(lr, layoutParams2);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return relativeLayout;

	}

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
								//
								if (callType.equalsIgnoreCase("VP")) {
									try {
										// Receiver
										selfHangup = true;
										if (selfHangup) {
											CallDispatcher.sb
													.setEndTime(getCurrentDateTime());
											CallDispatcher.sb
													.setCallDuration(SingleInstance.mainContext.getCallDuration(
															CallDispatcher.sb
																	.getStartTime(),
															CallDispatcher.sb
																	.getEndTime()));

											Log.d("Test",
													"@@@getEndTimeDuration@@______-->"
															+ getCurrentDateTime());
											Log.d("Test",
													"@@@getCallDuration@@______-->"
															+ getCallDuration(
																	CallDispatcher.sb
																			.getStartTime(),
																	CallDispatcher.sb
																			.getEndTime()));

											DBAccess.getdbHeler()
													.saveOrUpdateRecordtransactiondetails(
															CallDispatcher.sb);
											Intent intentComponent = new Intent(
													context,
													CallHistoryActivity.class);
											intentComponent
													.putExtra("buddyname",
															CallDispatcher.sb
																	.getFrom());
											intentComponent.putExtra(
													"individual", true);
											intentComponent.putExtra(
													"sessionid",
													CallDispatcher.sb
															.getSignalid());
											context.startActivity(intentComponent);
										}
										objCallDispatcher.accepted_users
												.clear();
										String buddy = CallDispatcher.conferenceMembers
												.get(0);
										// Log.d("test", "size " + buddy);
										// Log.d("test", "members " + buddy);
										SignalingBean sb = CallDispatcher.buddySignall
												.get(buddy);

										/* Static Clean up */
										sb.setFrom(objCallDispatcher.LoginUser);
										sb.setTo(buddy);
										sb.setType("3");
										sb.setCallType("VP");

										AppMainActivity.commEngine
												.hangupCall(sb);

										// VideoClose();

										if (pv != null) {
											pv.stopPreview();
											pv = null;
										}

									} catch (Exception e) {
										// TODO: handle exception
									}
									enterCallHistory();
									Log.e("thread", "@@@@@@@@@@@@@@ finish8");
									finish();

								} else {

									try {
										if (pv != null) {
											pv.stopPreview();
											pv = null;
										}
									} catch (Exception e) {
										// TODO: handle exception
									}
									try {
										objCallDispatcher.accepted_users
												.clear();
										// Log.d("test", "From leave/bye");
										for (int i = 0; i < CallDispatcher.conferenceMembers
												.size(); i++) {
											// Log.d("login", "login");

											// System.out.println("conferenceMembers :"
											// +
											// CallDispatcher.conferenceMembers);
											// System.out.println("buddySignal :"
											// + CallDispatcher.buddySignall);

											String buddy = CallDispatcher.conferenceMembers
													.get(i);
											// Log.d("test", "members " +
											// buddy);
											SignalingBean sb = CallDispatcher.buddySignall
													.get(buddy);
											// System.out.println("1 "+sb.getSignalport());
											// System.out.println("2 "+sb.getToSignalPort());
											// System.out.println("Buddy :" +
											// buddy);
											/* Static Clean up */
											// Log.d("login", "loginuser"
											// + objCallDispatcher.LoginUser);
											sb.setFrom(objCallDispatcher.LoginUser);
											sb.setTo(buddy);
											sb.setType("3");
											sb.setCallType("VBC");
											AppMainActivity.commEngine
													.hangupCall(sb);
										}
										if (CallDispatcher.conferenceRequest
												.size() > 0) {
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
													Log.d("hang",
															"loginuser"
																	+ objCallDispatcher.LoginUser);
													sb.setFrom(objCallDispatcher.LoginUser);
													sb.setTo(buddy);
													sb.setType("3");
													sb.setCallType("VBC");
													AppMainActivity.commEngine
															.hangupCall(sb);

												}
											} catch (Exception e) {
												// TODO: handle exception
											}
										}

										// Log.d("test", "sended bye ");
										/*
										 * members.clear();
										 */

										// Log.e("callInfo",
										// "Video call Screen Leave received");
										// WebServiceReferences.contextTable
										// .remove("callscreen");
										// finish();

										enterMissedCall();

										objCallDispatcher.currentSessionid = null;
										CallDispatcher.conferenceMembers
												.clear();
										CallDispatcher.conferenceRequest
												.clear();
										selfHangup = true;
										if (selfHangup) {
											DBAccess.getdbHeler()
													.saveOrUpdateRecordtransactiondetails(
															CallDispatcher.sb);
											Intent intentComponent = new Intent(
													context,
													CallHistoryActivity.class);
											intentComponent
													.putExtra("buddyname",
															CallDispatcher.sb
																	.getFrom());
											intentComponent.putExtra(
													"individual", true);
											intentComponent.putExtra(
													"sessionid",
													CallDispatcher.sb
															.getSignalid());
											context.startActivity(intentComponent);
										}
									} catch (Exception e) {
										// TODO: handle exception
										// Log.d("test", "sended bye " +
										// e.toString());
										e.printStackTrace();
									}

									try {
										if (pv != null) {
											pv.stopPreview();
											pv = null;
										}
									} catch (Exception e) {

									}
									enterCallHistory();
									Log.e("thread", "@@@@@@@@@@@@@@ finish9");
									finish();

								}

								//
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
		}
		return circleImage;
	}

	public void notifyHeadsetPlugin(final boolean is) {
		try {
			videoHandler.post(new Runnable() {

				@Override
				public void run() {
					if (mode.equals("0")) {
						//
						try {
							if (is) {
								// loudSpeaker
								// .setBackgroundResource(R.drawable.headset);
								audioProperties.setSpeakerphoneOn(false);
							} else {

								// loudSpeaker
								// .setBackgroundResource(R.drawable.speaker_unmute);
								audioProperties.setSpeakerphoneOn(true);

							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						//
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
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

	@Override
	public void notifyDecodedVideoCallback(byte[] data, long ssrc) {
		// Log.e("test", "Comes to Receive video data");
		try {
			if (mReceiveVideo) {

			} else {

				videocall.post(new Runnable() {

					@Override
					public void run() {

						if (timer != null) {
							timer.cancel();
						}

						if (layout != null) {
							callControls.removeView(layout);
						}

						callControls.addView(glSurfaceView, 0, layoutParamsf);
					}
				});

				mReceiveVideo = true;
			}

			if (videoQueue.getSize() < 1) {
				// Log.d("list", "two");
				videoQueue.addMsg(data);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void UpdateConferenceMembers(String from, boolean b) {

		// Log.d("RACE", " Race CAse..AP  having screen received hangup ");
		try {
			Message msg = new Message();

			Bundle bun = new Bundle();
			bun.putString("buddy", from);
			bun.putString("action", "update");

			msg.obj = bun;
			if (videoBroadCastHandler != null) {
				// Log.d("RACE", " Race CAse..AP  having screen update if ");
				videoBroadCastHandler.sendMessage(msg);
			} else {
				// Log.d("RACE", " Race CAse..AP  having screen update else ");
				VideoPagingSRWindow.videoHandlerFullScreen.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						receivedHangUp();
					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void updateHistoryPeople(String name) {
		try {
			addBroadcastingMembers(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void onStop() {
		// TODO Auto-generated method stub
		// Log.d("video", "videostop");
		//
		hasRemoved = true;
		super.onStop();
		// WebServiceReferences.contextTable.remove("")
	}

	@Override
	protected void onDestroy() {

		Log.i("thread", ">>>.Activity Destroyed");
		// RenderBitmap.count=0;
		CallDispatcher.isCallInitiate = false;
		CallDispatcher.openSenderScreen = false;

		try {
			this.mWakeLock.release();
			lock.reenableKeyguard();

			objCallDispatcher.startPlayer(context);

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			objCallDispatcher.stopRingTone();
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			CallDispatcher.currentSessionid = null;
			objCallDispatcher.isHangUpReceived = false;
			/*
			 * CallDispatcher.conferenceMembers.clear();
			 * CallDispatcher.conferenceMembersTime.clear();
			 */
			if (SingleInstance.instanceTable.containsKey("callscreen")) {
				SingleInstance.instanceTable.remove("callscreen");
			}
			CallDispatcher.videoScreenVisibleState = false;
			CallDispatcher.isCallInProgress = false;

		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (pv != null) {
				pv.stopPreview();
				pv = null;
			}
		} catch (Exception e) {

		}
		try {
			if (audioProperties != null) {
				audioProperties.setSpeakerphoneOn(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
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

	protected void onResume() {
		try {
            super.onResume();
            AppMainActivity.inActivity = this;

			if (AppMainActivity.commEngine != null) {
				AppMainActivity.commEngine.setmDecodeFrame(true);
			}
			if (WebServiceReferences.contextTable.containsKey("connection")) {
				CallConnectingScreen screen = (CallConnectingScreen) WebServiceReferences.contextTable
						.get("connection");
				screen.finish();
			}

			if (hasRemoved && mode.equals("1")) {
				hasRemoved = false;

				CallDispatcher.videoSpeakerMute(false);
				try {
					callControls.removeView(pv);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				try {
					pv.stopPreview();
					pv = AppMainActivity.commEngine.getVideoPreview(this);
					// callControls.addView(pv, 0);
					//
					double c = noScrHeightSender * 1.1;
					int c1 = (int) c;
					// Log.d("NN", "height width " + c1);
					FrameLayout.LayoutParams layoutParamsf = new FrameLayout.LayoutParams(
							c1, LayoutParams.WRAP_CONTENT);

					callControls.addView(pv, 0, layoutParamsf);
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
					//

				} catch (Exception e) {
					e.printStackTrace();

				}

			}
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}


	}

	@Override
	public void onTimerElapsed() {
		try {
			final LinearLayout ly = (LinearLayout) layout.getChildAt(1);

			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						if (i <= 10) {

							ly.addView(getDots());
							i++;
						} else {
							i = 0;
							ly.removeAllViews();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	protected void onPause() {

		if (AppMainActivity.commEngine != null) {
			AppMainActivity.commEngine.setmDecodeFrame(false);
		}

		super.onPause();
	}

	public void notifyMediaFailure(String buddyVideo) {
		// remove surface view

		try {
			if (layout != null) {
				callControls.removeView(glSurfaceView);
			}
			showVideoFailure();

		} catch (Exception e) {
			// TODO: handle exception
		}
		// videosurface.add

	}

	private void showVideoFailure() {
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

			layoutParamsf = new FrameLayout.LayoutParams(c1,
					LayoutParams.FILL_PARENT);

			callControls.addView(ll, 0, layoutParamsf);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void notifyResolution(int w, int h) {
		// TODO Auto-generated method stub

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

	public String getCallDuration(String startTime, String endTime) {
		try {
			Log.d("Test", "Start time getcallduration$$------>" + startTime);
			Log.d("Test", "End time getcallduration$$------>" + endTime);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
			Date d1 = null;
			Date d2 = null;
			d1 = sdf.parse(startTime);
			d2 = sdf.parse(endTime);
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			Log.d("Test",
					"duration : " + String.valueOf(diffHours) + ":"
							+ String.valueOf(diffMinutes) + ":"
							+ String.valueOf(diffSeconds));
			return String.valueOf(diffHours) + ":"
					+ String.valueOf(diffMinutes) + ":"
					+ String.valueOf(diffSeconds);
			// return "0:"
			// + String.valueOf(diffMinutes) + ":"
			// + String.valueOf(diffSeconds);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void notifyGSMCallAccepted() {
		objCallDispatcher.accepted_users.clear();
		String buddy = CallDispatcher.conferenceMembers.get(0);
		// Log.d("test", "size " + buddy);
		// Log.d("test", "members " + buddy);
		SignalingBean sb = CallDispatcher.buddySignall.get(buddy);

		/* Static Clean up */
		sb.setFrom(objCallDispatcher.LoginUser);
		sb.setTo(buddy);
		sb.setType("3");
		sb.setCallType("VP");
		AppMainActivity.commEngine.hangupCall(sb);

		// VideoClose();

		if (pv != null) {
			pv.stopPreview();
			pv = null;
		}
	}

}
