package com.cg.instancemessage;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import org.lib.model.BuddyInformationBean;
//import org.lib.model.PermissionBean;
//import org.lib.model.SignalingBean;
//import org.util.Utility;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.ActivityManager.MemoryInfo;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.pm.Signature;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Typeface;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.support.v4.app.FragmentActivity;
//import android.text.Html;
//import android.text.format.DateFormat;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewTreeObserver.OnGlobalLayoutListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.BounceInterpolator;
//import android.view.animation.TranslateAnimation;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.Chronometer;
//import android.widget.Chronometer.OnChronometerTickListener;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.cg.account.R;
//import com.cg.callservices.MyAbsoluteLayout;
//import com.cg.commonclass.CallDispatcher;
//import com.cg.commonclass.Databean;
//import com.cg.commonclass.TextNoteDatas;
//import com.cg.commonclass.WebServiceReferences;
//import com.cg.commongui.HandSketchActivity;
//import com.cg.commongui.MultimediaUtils;
//import com.cg.commongui.PhotoZoomActivity;
//import com.cg.files.CompleteListBean;
//import com.cg.files.CompleteListView;
//import com.cg.ftpprocessor.FTPBean;
//import com.cg.hostedconf.AppReference;
//import com.cg.profiles.ViewProfiles;
//import com.im.xml.AudioChatBean;
//import com.im.xml.ImageChatBean;
//import com.im.xml.TextChatBean;
//import com.im.xml.VideoChatBean;
//import com.im.xml.XMLComposer;
//import com.im.xml.XMLParser;
//import com.util.VideoPlayer;
//
///**
// * To produce the ui when user send / received any message from/to them
// * buddies.This title will animate when user received the im from them buddies
// * 
// * 
// * 
// */
//@SuppressLint("CutPasteId")
//public class InstantMessageScreen extends FragmentActivity {
//
//	private LinearLayout chattingList = null;
//
//	private AlertDialog.Builder builder = null;
//
//	private String sessionid = null;
//
//	private ArrayList<String> members = null;
//
//	private boolean groupmode = false;
//
//	private Handler updateHandler = new Handler();
//
//	private ScrollView sview = null;
//
//	private AlertDialog alert = null;
//
//	public String buddy = null;
//
//	private String initializer = null;
//
//	private String status = "Online";
//
//	private String record_path = null;
//
//	private Handler mHandler = new Handler();
//
//	private Handler audioHandler = null;
//
//	private Handler videorecorder = null;
//
//	private Handler videohandler1 = null;
//
//	private Handler videoHandler2 = null;
//
//	private boolean isaudio_imported = false;
//
//	private boolean isimported = false;
//
//	private boolean isrobo = false;
//
//	private String importedFile = null;
//
//	private String messagetype = null;
//
//	private ScrollView scroll = null;
//
//	private Handler handler = new Handler();
//
//	private AlertDialog msg_dialog = null;
//
//	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
//
//	private boolean fromexist = false;
//
//	private boolean isProfileRequested = false;
//
//	// private ProgressBar progressBar = null;
//
//	private String confmembers = null;
//
//	private EditText myMessage;
//
//	private Context context = null;
//
//	private IMTabScreen imtab = null;
//
//	private TextView tvTabName;
//
//	private LinearLayout mmlay = null;
//
//	private String strIPath;
//
//	private ImageView ivPhoto, ivPhoto1;
//
//	private FrameLayout frm_photo;
//
//	private boolean isrecording = false;
//
//	private boolean isvideoplay = false;
//
//	private TextView tvtimer = null;
//
//	private MediaPlayer player = null;
//
//	private XMLComposer myXMLWriter = null;
//
//	private XMLParser myXMLParser = null;
//
//	private XMLParser parser = null;
//
//	private CallDispatcher callDisp;
//
//	private boolean isdownload;
//
//	private LinearLayout llayStart;
//
//	private LinearLayout llayTimer;
//
//	private boolean isAudioRecording1 = false;
//
//	private boolean isAudioPlaying = false;
//
//	private boolean isVideoRecord = false;
//
//	private boolean isvideobuddyVideoPlaying = false;
//
//	private Button photo = null;
//
//	private Bitmap senderBitmap, receiverBitmap = null;
//
//	private Object current_audio, current_video, current_aplayer,
//			current_vplayer;
//
//	private TextView iv_imort = null;
//
//	private Chronometer stopWatch = null;
//
//	private ArrayList<Object> childMap = new ArrayList<Object>();
//
//	private HashMap<String, Object> roboMap = new HashMap<String, Object>();
//
//	private CharSequence[] m_type = { "Audio Call", "Video Call",
//			"Audio Unicast", "Video Unicast" };
//	private CharSequence[] b_type = { "View Profile", "ClearAll", "Cancel" };
//
//	private Utility utility = new Utility();
//
//	private ArrayList<Bitmap> bitmaplist = new ArrayList<Bitmap>();
//
//	/**
//	 * Called when the activity is starting.When activity stared initialize the
//	 * basic ui to handle the IM send process and initialize the handlers to
//	 * produce ui for the received im
//	 */
//
//	private Handler apSendHandler = new Handler();
//
//	private boolean isplaying = false;
//
//	private boolean isForceLogout = false;
//
//	private Button back;
//
//	public TextView buddyName = null;
//
//	private ImageView buddyPic = null;
//
//	public TextView buddyStatus = null;
//
//	public int view = 0;
//
//	private boolean keyboard = false;
//
//	private Bitmap playBitmap;
//
//	private Bitmap pauseBitmap;
//
//	protected void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		try {
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			setContentView(R.layout.imscreen);
//			context = this;
//
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//			int noScrHeight = displaymetrics.heightPixels;
//			int noScrWidth = displaymetrics.widthPixels;
//
//			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
//				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
//						.get("calldisp");
//			else
//				callDisp = new CallDispatcher(context);
//
//			callDisp.setNoScrHeight(noScrHeight);
//			callDisp.setNoScrWidth(noScrWidth);
//			displaymetrics = null;
//
////			if (AppReference.sip_accountID != -1
////					&& !AppReference.call_mode.equalsIgnoreCase("oneway")) {
////
////				CommunicationBean bean = new CommunicationBean();
////				bean.setOperationType(sip_operation_types.MODIFY_ACCOUNT);
////				bean.setRealm(callDisp.getFS());
////				bean.setSipEndpoint(AppReference.sip_registeredid);
////				bean.setMode("oneway");
////				AppReference.call_mode = "oneway";
////				if (AppReference.sipQueue != null)
////					AppReference.sipQueue.addMsg(bean);
////			}
//
//			members = new ArrayList<String>();
//			buddy = getIntent().getStringExtra("buddy");
//			status = getIntent().getStringExtra("status");
//			initializer = CallDispatcher.LoginUser;
//			getIntent().getStringExtra("msg");
//			confmembers = getIntent().getStringExtra("confmembers");
//			sessionid = getIntent().getStringExtra("sessionid");
//			WebServiceReferences.session = sessionid;
//			WebServiceReferences.SelectedBuddy = buddy;
//			mmlay = (LinearLayout) findViewById(R.id.lay_mm_operation);
//			WebServiceReferences.contextTable.put("imscreen", context);
//			mmlay.setBackgroundDrawable(null);
//			imtab = (IMTabScreen) WebServiceReferences.contextTable
//					.get("imtabs");
//			ivPhoto = new ImageView(context);
//			ivPhoto.setBackgroundDrawable(null);
//			tvtimer = new TextView(context);
//			CompleteListView.checkDir();
//			myXMLWriter = new XMLComposer("/sdcard/COMMedia/" + sessionid);
//			myXMLParser = new XMLParser("/sdcard/COMMedia/" + sessionid);
//			back = (Button) findViewById(R.id.btn_cancel);
//			buddyName = (TextView) findViewById(R.id.buddyname);
//			buddyName.setText(buddy);
//			CallDispatcher.buddyName = buddy;
//			buddyPic = (ImageView) findViewById(R.id.buddypic);
//			buddyPic.setTag(buddy);
//			buddyStatus = (TextView) findViewById(R.id.buddystatus);
//			refreshStatus(status);
//			scroll = (ScrollView) findViewById(R.id.scroll);
//			playBitmap = callDisp.Resizeresource(R.drawable.v_play, context);
//			pauseBitmap = callDisp.Resizeresource(R.drawable.v_pause, context);
////			if (CallDispatcher.adapterToShow != null
////					&& CallDispatcher.adapterToShow.getCount() > 0)
////				CallDispatcher.adapterToShow.notifyDataSetChanged();
//
//			String profilePic = callDisp.getdbHeler(context).getProfilePic(
//					buddy);
//			try {
//				if (profilePic != null && profilePic.length() > 0) {
//					Bitmap profle_bitmap = callDisp.setProfilePicture(
//							profilePic, R.drawable.icon_buddy_aoffline);
//					buddyPic.setImageBitmap(profle_bitmap);
//
//				}
//			} catch (Exception e) {
//
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//			back.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (!AppReference.issipchatinitiated) {
//						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//						inputManager.hideSoftInputFromWindow(
//								myMessage.getWindowToken(), 0);
//						finish();
//					} else
//						showcancelAlert();
//
//				}
//			});
//			final View activityRootView = findViewById(R.id.imscreen);
//			activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
//					new OnGlobalLayoutListener() {
//
//						@Override
//						public void onGlobalLayout() {
//							// TODO Auto-generated method stub
//							int heightDiff = activityRootView.getRootView()
//									.getHeight() - activityRootView.getHeight();
//							// if more than 100 pixels, its probably a
//							// keyboard...
//							if (heightDiff > 100) {
//								if (scroll != null) {
//									scroll.post(new Runnable() {
//
//										@Override
//										public void run() {
//											scroll.fullScroll(ScrollView.FOCUS_DOWN);
//										}
//									});
//								}
//								if (keyboard) {
//									((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//											.showSoftInput(
//													myMessage,
//													InputMethodManager.SHOW_FORCED);
//								}
//							} else {
//								// keyboard is not visible, do something here
//							}
//						}
//
//					});
//
//			buddyPic.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					showBuddyDialog();
//				}
//			});
//
//			iv_imort = (TextView) findViewById(R.id.iview_importtxt);
//			callDisp.putxmlobj(buddy, myXMLWriter);
//			Signature[] sigs = null;
//
//			try {
//				sigs = context.getPackageManager()
//						.getPackageInfo(context.getPackageName(),
//								PackageManager.GET_SIGNATURES).signatures;
//			} catch (NameNotFoundException e1) {
//				// TODO Auto-generated catch block
//
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				e1.printStackTrace();
//			}
//			for (Signature sig : sigs) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.info("Signature hashcode : "
//							+ sig.hashCode());
//				Log.i("MyApp", "Signature hashcode : " + sig.hashCode());
//			}
//
//			tvTabName = (TextView) imtab.tabHost.getCurrentTabView()
//					.findViewById(android.R.id.title); // for Selected Tab
//			imtab.tabHost.getCurrentTabView().setVisibility(View.GONE);
//			tvTabName.setTextColor(Color.GREEN);
//
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("mysession id " + sessionid);
//			Log.d("im", "mysession id " + sessionid);
//			boolean fromMe = getIntent().getBooleanExtra("fromme", false);
//			boolean toCreate = getIntent().getBooleanExtra("tocreate", false);
//			sview = (ScrollView) findViewById(R.id.scroll);
//			sview.fullScroll(ScrollView.FOCUS_DOWN);
//
//			Log.d("see", "im webservicereference ");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.info("im webservicereference " + sessionid);
//			Log.d("see", "im webservicereference " + sessionid);
//
//			WebServiceReferences.instantMessageScreen.put(sessionid, this);
//			senderBitmap = BitmapFactory.decodeResource(getResources(),
//					R.drawable.icon_buddy_aoffline);
//			receiverBitmap = BitmapFactory.decodeResource(getResources(),
//					R.drawable.icon_buddy_aoffline);
//
//			if (toCreate)
//				chattingList = (LinearLayout) findViewById(R.id.linearlay);
//			else {
//
//				chattingList = (LinearLayout) findViewById(R.id.linearlay);
//
//				myXMLWriter = new XMLComposer("/sdcard/COMMedia/" + sessionid);
//				myXMLParser = new XMLParser("/sdcard/COMMedia/" + sessionid);
//				callDisp.putxmlobj(buddy, myXMLWriter);
//			}
//
//			if (confmembers != null) {
//				if (confmembers.trim().length() > 0) {
//					String str[] = confmembers.split(",");
//					for (int i = 0; i < str.length; i++) {
//						groupmode = true;
//						if (!str[i].equals(CallDispatcher.LoginUser))
//							members.add(str[i]);
//					}
//
//				} else {
//					members.add(buddy);
//				}
//			} else {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.info("conf mem null");
//				Log.d("imscreen", "conf mem null");
//				members.add(buddy);
//			}
//
//			myMessage = (EditText) findViewById(R.id.edtSend);
//			myMessage.requestFocus();
//			sview.fullScroll(ScrollView.FOCUS_DOWN);
//
//			myMessage.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					// TODO Auto-generated method stub
//					if (event.getAction() == MotionEvent.ACTION_DOWN) {
//						sview.fullScroll(ScrollView.FOCUS_DOWN);
//						keyboard = true;
//						return true;
//					} else {
//						return false;
//					}
//				}
//
//			});
//			myMessage
//					.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//						@Override
//						public void onFocusChange(View v, boolean hasFocus) {
//							if (hasFocus) {
//								((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//										.showSoftInput(myMessage,
//												InputMethodManager.SHOW_FORCED);
//							}
//						}
//					});
//
//			imtab.tabHost.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//				@Override
//				public void onFocusChange(View v, boolean hasFocus) {
//
//					if (hasFocus) {
//						tvTabName.setTextColor(Color.GREEN);
//						tvTabName.clearAnimation();
//					} else {
//						tvTabName = (TextView) imtab.tabHost
//								.getCurrentTabView().findViewById(
//										android.R.id.title);
//						tvTabName.clearAnimation();
//						tvTabName.setTextColor(Color.GREEN);
//					}
//					MemoryInfo info = new MemoryInfo();
//					ActivityManager mgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//					mgr.getMemoryInfo(info);
//
//				}
//			});
//
//			Button call = (Button) findViewById(R.id.settingnotification);
//			call.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					showMessageDialog();
//
//				}
//			});
//
//			iv_imort.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (AppReference.sip_accountID != -1) {
//
//					}
//
//					openOptionsMenu();
//
//				}
//			});
//
//			Button send = (Button) findViewById(R.id.btnSend);
//
//			send.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (CallDispatcher.LoginUser != null) {
//						sendTextMsg(myMessage.getText().toString().trim(), "");
//						myMessage.setFocusable(true);
//						myMessage.requestFocus();
//						((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//								.showSoftInput(myMessage,
//										InputMethodManager.SHOW_FORCED);
//						if (scroll != null) {
//							scroll.post(new Runnable() {
//
//								@Override
//								public void run() {
//									scroll.fullScroll(ScrollView.FOCUS_DOWN);
//								}
//							});
//						}
//
//					} else {
//						showToast("Sorry! can not send Message");
//					}
//				}
//			});
//
//			CheckMIM();
//			if (!fromMe) {
//
//				SignalingBean sb = (SignalingBean) getIntent()
//						.getSerializableExtra("Signal");
//				if (sb.getCallType().equals("MPP")) {
//
//					notifyImageDownloaded(sb.getIsDownloadSuccess(),
//							(FTPBean) sb.getFtpObj(), null);
//
//				} else if (sb.getCallType().equals("MAP")) {
//
//					notifyAudioDownloaded(sb.getIsDownloadSuccess(),
//							(FTPBean) sb.getFtpObj(), null);
//				} else if (sb.getCallType().equals("MVP")) {
//
//					notifyVideoDownloaded(sb.getIsDownloadSuccess(),
//							(FTPBean) sb.getFtpObj(), null);
//				} else if (sb.getCallType().equals("MVP")) {
//
//					notifyVideoDownloaded(sb.getIsDownloadSuccess(),
//							(FTPBean) sb.getFtpObj(), null);
//				} else if (sb.getCallType().equals("MHP")) {
//
//					notifyFPDownloaded(sb.getIsDownloadSuccess(),
//							(FTPBean) sb.getFtpObj(), null);
//				}
//
//				for (int i = 0; i < members.size(); i++) {
//
//					if (!members.get(i).equals(CallDispatcher.LoginUser)) {
//						sview.fullScroll(ScrollView.FOCUS_DOWN);
//					}
//				}
//
//			}
//
//			if (WebServiceReferences.Imcollection.size() > 0) {
//
//				if (WebServiceReferences.Imcollection.containsKey(sessionid)) {
//					ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
//							.get(sessionid);
//					if (WebServiceReferences.tempIMCount != 0) {
//						WebServiceReferences.tempIMCount = WebServiceReferences.tempIMCount
//								- value.size();
//					}
//
//					for (int i = 0; i < value.size(); i++) {
//						new Intent(context, IMTabScreen.class);
//						SignalingBean sb1 = value.get(i);
//						if (!sb1.getCallType().equalsIgnoreCase("MTP")
//								&& (!sb1.getMessage().startsWith("#"))) {
//							updateMsg(sb1.getConferencemember(),
//									sb1.getMessage(), sb1.getFrom(), sb1);
//						}
//
//					}
//					value.clear();
//					if (WebServiceReferences.Imcollection
//							.containsKey(sessionid)) {
//						WebServiceReferences.Imcollection.remove(sessionid);
//					}
//				}
//
//				else if (WebServiceReferences.activeSession.containsKey(buddy)) {
//					String key = WebServiceReferences.activeSession.get(buddy);
//
//					if (WebServiceReferences.Imcollection.containsKey(key)) {
//						ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
//								.get(key);
//						value.clear();
//						if (WebServiceReferences.Imcollection.containsKey(key)) {
//							WebServiceReferences.Imcollection.remove(key);
//						}
//					}
//				}
////				CallDispatcher.adapterToShow.notifyDataSetChanged();
//			}
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error("Exception raised -->"
//						+ e.getMessage());
//			Log.e("imscreen", "====> " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	private void ph_au_Sender(String strMMType) {
//		try {
//
//			if (strMMType.equals("Gallery")) {
//
//				if (Build.VERSION.SDK_INT < 19) {
//					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//					intent.setType("image/jpeg");
//					startActivityForResult(intent, 2);
//				} else {
//
//					Intent intent = new Intent();
//					intent.setType("image/jpeg");
//					intent.setAction(Intent.ACTION_GET_CONTENT);
//					startActivityForResult(
//							Intent.createChooser(intent, "Select Picture"), 19);
//
//				}
//
//			} else if (strMMType.equals("Photo")) {
//
//				Long free_size = callDisp.getExternalMemorySize();
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug(free_size.toString());
//				Log.d("IM", free_size.toString());
//				if (free_size > 0 && free_size >= 5120) {
//					if (llayTimer != null) {
//						llayTimer.removeAllViews();
//					}
//
//					// Intent i = new Intent(context, Custom_Camara.class);
//
//					// i.putExtra("Image_Name", strIPath);
//					// Log.d("File Path", strIPath);
//					// startActivityForResult(i, 0);
//					strIPath = "/sdcard/COMMedia/" + getFileName() + ".jpg";
//					Intent intent = new Intent(context, MultimediaUtils.class);
//					intent.putExtra("filePath", strIPath);
//					intent.putExtra("requestCode", 0);
//					intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
//					intent.putExtra("createOrOpen", "create");
//					startActivity(intent);
//
//				} else {
//					showToast("InSufficient Memory...");
//				}
//			}
//
//			else if (strMMType.equalsIgnoreCase("existing")) {
//				Intent i = new Intent(context, NotePickerScreen.class);
//				i.putExtra("note", "photo");
//				i.putExtra("forms", "form");
//
//				startActivityForResult(i, 100);
//
//			}
//
//			else if (strMMType.equalsIgnoreCase("Sketch")) {
//				Intent intent = new Intent(context, HandSketchActivity.class);
//				startActivityForResult(intent, 101);
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private void RecordSipaudiochat(String MMType) {
//
//		try {
//			Long free_size = callDisp.getExternalMemorySize();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug(free_size.toString());
//			Log.d("MM", free_size.toString());
//			if ((free_size > 0 && free_size >= 5120)
//					&& (mmlay.getChildCount() == 0)) {
//				final RelativeLayout rlSender = new RelativeLayout(context);
//				rlSender.setLayoutParams(new LayoutParams(
//						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//				rlSender.setBackgroundResource(R.drawable.corner);
//
//				RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//				RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//				llayStart = new LinearLayout(context);
//				llayStart.setGravity(Gravity.CENTER);
//				// llayStart.setOrientation(0);
//				llayTimer = new LinearLayout(context);
//				llayTimer.setGravity(Gravity.CENTER);
//
//				final Button btn_record = new Button(context);
//				btn_record.setGravity(Gravity.CENTER);
//				btn_record.setBackgroundResource(R.drawable.hangupx);
//				llayStart.addView(btn_record);
//
//				// progressBar = new ProgressBar(context);
//				//
//				// progressBar.setIndeterminate(true);
//				// llayTimer.addView(progressBar);
//				// llayTimer.setGravity(Gravity.CENTER);
//
//				rlSender.addView(llayTimer, layRight);
//				rlSender.addView(llayStart, layLeft);
//				mmlay.addView(rlSender);
//
//				btn_record.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
////						if (AppReference.issipchatinitiated) {
////							if (AppReference.chatcall_id != -1) {
////								CommunicationBean bean1 = new CommunicationBean();
////								bean1.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
////								bean1.setCall_id(AppReference.chatcall_id);
////								AppReference.sipQueue.addMsg(bean1);
////							}
////
////						}
//
//						isrecording = false;
//						if (buddy != null) {
//
//							BuddyInformationBean bib = WebServiceReferences.buddyList
//									.get(buddy);
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("recording status====>"
//												+ AppReference.isRecordingStarted);
//							Log.d("record", "recording status====>"
//									+ AppReference.isRecordingStarted);
////							if (AppReference.isRecordingStarted) {
////
////								CommunicationBean call_bean = new CommunicationBean();
////								call_bean
////										.setOperationType(sip_operation_types.STOP_RECORDING);
////								AppReference.sipQueue.addMsg(call_bean);
////
////								AppReference.isRecordingStarted = false;
////								isrecording = false;
////
////								FTPBean bean = new FTPBean();
////								bean.setFtp_username(CallDispatcher.LoginUser);
////								bean.setFtp_password(CallDispatcher.Password);
////								bean.setOperation_type(1);
////								bean.setComment(sessionid);
////								bean.setServer_ip(callDisp.getRouter().split(
////										":")[0]);
////								bean.setRequest_from("MAP");
////								bean.setFile_path(strIPath);
////								notifyFTPServerConnected1(true, bean);
////
////							}
//
//							if (bib != null && !isForceLogout) {
//								if (CallDispatcher.LoginUser == null) {
//									showToast("Sorry! can not send This Audio");
//								} else if (!bib.getStatus().equals("Online")) {
//									showToast("The User is not in Online state");
//								}
//							} else
//								showToast("Sorry! can not send This Audio");
//						} else {
//							if (isForceLogout) {
//								showToast("Sorry the user is not in Online");
//							}
//						}
//
//						// stopWatch.stop();
//						llayStart.removeAllViews();
//						llayTimer.removeAllViews();
//						mmlay.removeAllViews();
//
//					}
//				});
//
//			} else {
//				showToast("InSufficient Memory...");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	public void clearChatUI(String error_messsage) {
//		try {
//			if (mmlay.getChildCount() != 0) {
//				showToast(error_messsage);
//				llayStart.removeAllViews();
//				llayTimer.removeAllViews();
//				mmlay.removeAllViews();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void RecordAudio(String MMType) {
//		try {
//			Long free_size = callDisp.getExternalMemorySize();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug(free_size.toString());
//			Log.d("MM", free_size.toString());
//			if ((free_size > 0 && free_size >= 5120)
//					&& (mmlay.getChildCount() == 0)) {
//				final RelativeLayout rlSender = new RelativeLayout(context);
//				rlSender.setLayoutParams(new LayoutParams(
//						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//				rlSender.setBackgroundResource(R.drawable.corner);
//
//				RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//				RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.WRAP_CONTENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//				llayStart = new LinearLayout(context);
//				llayStart.setGravity(Gravity.CENTER);
//				// llayStart.setOrientation(0);
//				llayTimer = new LinearLayout(context);
//				llayTimer.setGravity(Gravity.CENTER);
//
//				final Button btn_record = new Button(context);
//				btn_record.setGravity(Gravity.CENTER);
//				btn_record.setText("Record");
//				llayStart.addView(btn_record);
//
//				ImageView import_audio = new ImageView(context);
//				import_audio.setBackgroundResource(R.drawable.audionotesnew);
//				llayStart.addView(import_audio);
//
//				import_audio.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if (!isrecording) {
//							isaudio_imported = true;
//							Intent i = new Intent(context,
//									NotePickerScreen.class);
//							i.putExtra("note", "audio");
//							startActivityForResult(i, 100);
//						} else {
//							showToast("Audio Recording is in Progress,Kindly stop that first.");
//						}
//					}
//				});
//
//				stopWatch = new Chronometer(context);
//				stopWatch.setText("00:00:00");
//				stopWatch
//						.setOnChronometerTickListener(new OnChronometerTickListener() {
//							@Override
//							public void onChronometerTick(Chronometer arg0) {
//								if (isrecording) {
//									CharSequence text = stopWatch.getText();
//									if (text.length() == 5) {
//										stopWatch.setText("00:" + text);
//									} else if (text.length() == 7) {
//										stopWatch.setText("0" + text);
//									}
//									tvtimer.setText(stopWatch.getText()
//											.toString());
//								} else
//
//								{
//									long milliseconds = player
//											.getCurrentPosition();
//									String seconds = WebServiceReferences.setLength2((int) (Math
//											.round((double) milliseconds / 1000) % 60));
//									String minutes = WebServiceReferences
//											.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
//									String hours = WebServiceReferences
//											.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
//									String asText = hours + ":" + minutes + ":"
//											+ seconds;
//									tvtimer.setText(asText);
//								}
//							}
//						});
//				stopWatch.setVisibility(View.INVISIBLE);
//				llayTimer.addView(tvtimer);
//				llayTimer.addView(stopWatch);
//				llayTimer.setGravity(Gravity.CENTER);
//				rlSender.addView(llayTimer, layRight);
//				rlSender.addView(llayStart, layLeft);
//				mmlay.addView(rlSender);
//				btn_record.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						Message msgv = new Message();
//						audioHandler.sendMessage(msgv);
//					}
//				});
//				audioHandler = new Handler() {
//
//					@Override
//					public void handleMessage(Message msg) {
//						// TODO Auto-generated method stub
//						final String state = android.os.Environment
//								.getExternalStorageState();
//						if (!isaudio_imported) {
//							if (btn_record.getText().equals("Record")) {
//								isrecording = true;
//								btn_record.setText("Stop");
//								if (!state
//										.equals(android.os.Environment.MEDIA_MOUNTED)) {
//									showToast("Media Recorder can not be accessed");
//								} else {
//									strIPath = "/sdcard/COMMedia/"
//											+ getFileName() + ".mp4";
//									if (AppReference.isWriteInFile)
//										AppReference.logger
//												.debug("Recorder path -->"
//														+ strIPath);
//									Log.d("Recorder", "@@@@@@@@@@@@@@@@"
//											+ strIPath);
//								}
//								try {
//									Intent intent = new Intent(context,
//											MultimediaUtils.class);
//									intent.putExtra("filePath", strIPath);
//									intent.putExtra("requestCode", 32);
//									intent.putExtra("action", "audio");
//									intent.putExtra("createOrOpen", "create");
//									startActivity(intent);
//								} catch (Exception e) {
//									// TODO Auto-generated catch block\
//									if (AppReference.isWriteInFile)
//										AppReference.logger.error(
//												e.getMessage(), e);
//									e.printStackTrace();
//								}
//							}
//						} else {
//							isrecording = false;
//							tvtimer.setText("00:00:00");
//							llayStart.removeAllViews();
//							isaudio_imported = false;
//							final Button btn_play = new Button(context);
//							btn_play.setText("Play");
//							llayStart.addView(btn_play);
//							try {
//								if (player != null) {
//									player.reset();
//									player.release();
//									player = null;
//								}
//
//								player = new MediaPlayer();
//								player.setDataSource(strIPath);
//								player.prepare();
//								player.setLooping(false);
//							} catch (IllegalArgumentException e1) {
//								// TODO Auto-generated catch block
//								if (AppReference.isWriteInFile)
//									AppReference.logger.error(e1.getMessage(),
//											e1);
//								e1.printStackTrace();
//							} catch (IllegalStateException e1) {
//								// TODO Auto-generated catch block
//								if (AppReference.isWriteInFile)
//									AppReference.logger.error(e1.getMessage(),
//											e1);
//								e1.printStackTrace();
//							} catch (IOException e1) {
//								// TODO Auto-generated catch block
//								if (AppReference.isWriteInFile)
//									AppReference.logger.error(e1.getMessage(),
//											e1);
//								e1.printStackTrace();
//							}
//
//							btn_play.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									if (btn_play.getText().equals("Play")) {
//										isplaying = true;
//										btn_play.setText("Pause");
//
//										try {
//											if (player != null) {
//												player.reset();
//												player.release();
//												player = null;
//											}
//
//											player = new MediaPlayer();
//											player.setDataSource(strIPath);
//											player.prepare();
//											player.setLooping(false);
//										} catch (IllegalArgumentException e) {
//											// TODO Auto-generated catch block
//											if (AppReference.isWriteInFile)
//												AppReference.logger.error(
//														e.getMessage(), e);
//											e.printStackTrace();
//										} catch (IllegalStateException e) {
//											// TODO Auto-generated catch block
//											if (AppReference.isWriteInFile)
//												AppReference.logger.error(
//														e.getMessage(), e);
//											e.printStackTrace();
//										} catch (IOException e) {
//											// TODO Auto-generated catch block
//											if (AppReference.isWriteInFile)
//												AppReference.logger.error(
//														e.getMessage(), e);
//											e.printStackTrace();
//										}
//
//										player.start();
//										stopWatch.setText("00:00:00");
//										stopWatch.start();
//
//										player.setOnCompletionListener(new OnCompletionListener() {
//											@Override
//											public void onCompletion(
//													MediaPlayer mp) {
//												// TODO Auto-generated method
//												// stub
//												isplaying = false;
//												btn_play.setText("Play");
//												stopWatch.stop();
//												player.reset();
//												player.release();
//												player = null;
//												try {
//
//												} catch (IllegalArgumentException e) {
//													// TODO Auto-generated catch
//													// block
//													if (AppReference.isWriteInFile)
//														AppReference.logger.error(
//																e.getMessage(),
//																e);
//													e.printStackTrace();
//												} catch (IllegalStateException e) {
//													// TODO Auto-generated catch
//													// block
//													if (AppReference.isWriteInFile)
//														AppReference.logger.error(
//																e.getMessage(),
//																e);
//													e.printStackTrace();
//												}
//												stopWatch.setText("00:00:00");
//											}
//										});
//
//									} else if (btn_play.getText().equals(
//											"Pause")) {
//										if (player != null) {
//											player.pause();
//											btn_play.setText("Play");
//											stopWatch.stop();
//										}
//
//									}
//								}
//
//							});
//
//							Button audio_send = new Button(context);
//							audio_send.setText("Send");
//							audio_send.setGravity(Gravity.CENTER);
//							llayStart.addView(audio_send);
//							audio_send
//									.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											Log.d("MM",
//													"@@@@@@@@@Send Clicked@@@@@@@@");
//											if (player != null) {
//
//												if (player.isPlaying()) {
//													player.stop();
//													player.reset();
//													player.release();
//													player = null;
//												} else if (isplaying) {
//													player.stop();
//													player.reset();
//													player.release();
//													player = null;
//												} else {
//													player.reset();
//													player.release();
//													player = null;
//												}
//												isplaying = false;
//											}
//											if (buddy != null) {
//
//												BuddyInformationBean bib = WebServiceReferences.buddyList
//														.get(buddy);
//
//												// audio.setEnabled(true);
//												isrecording = false;
//												mmlay.setBackgroundDrawable(null);
//												mmlay.removeAllViews();
//												FTPBean bean = new FTPBean();
//												bean.setFtp_username(CallDispatcher.LoginUser);
//												bean.setFtp_password(CallDispatcher.Password);
//												bean.setOperation_type(1);
//												bean.setServer_port(40400);
//												bean.setComment(sessionid);
//												bean.setServer_ip(callDisp
//														.getRouter().split(":")[0]);
//												bean.setRequest_from("MAP");
//												bean.setFile_path(strIPath);
//
//												notifyFTPServerConnected(true,
//														bean);
//												if (CallDispatcher.LoginUser == null) {
//													showToast("Sorry! can not send This Photo");
//												} else if (!bib.getStatus()
//														.equals("Online")) {
//													showToast("The User is not in Online state");
//												}
//
//											}
//										}
//									});
//
//							Button exit = new Button(context);
//							exit.setText("Exit");
//							exit.setGravity(Gravity.CENTER);
//							llayStart.addView(exit);
//							exit.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									// audio.setEnabled(true);
//									isrecording = false;
//
//									if (player != null) {
//
//										if (player.isPlaying()) {
//											player.stop();
//											player.reset();
//											player.release();
//											player = null;
//										} else if (isplaying) {
//											player.stop();
//											player.reset();
//											player.release();
//											player = null;
//
//										} else {
//											player.reset();
//											player.release();
//											player = null;
//										}
//										isplaying = false;
//									}
//									llayStart.removeAllViews();
//									llayTimer.removeAllViews();
//									mmlay.removeAllViews();
//
//									tvtimer.setText("00:00:00");
//
//								}
//							});
//
//						}
//						super.handleMessage(msg);
//					}
//
//				};
//			} else {
//				showToast("InSufficient Memory...");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	public void showToast(String msg) {
//		try {
//			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private static String getFileName() {
//		try {
//			String strFilename;
//			SimpleDateFormat dateFormat = new SimpleDateFormat(
//					"ddMMyyyyHH_mm_ss");
//			Date date = new Date();
//			strFilename = dateFormat.format(date);
//			return strFilename;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * When activity is resumed produce and load the already received message's
//	 * ui
//	 */
//	protected void onResume() {
//		try {
//			super.onResume();
//
//			if (callDisp != null) {
//				if (callDisp.notifier != null) {
//					callDisp.notifier.removeNoTification();
//				}
//				callDisp.notifier = null;
//			}
//			loadPendingMessges();
//			scrollDown();
//			CallDispatcher.currentIMSession = sessionid;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Generate the ui to the already received messages
//	 */
//	public void loadPendingMessges() {
//		try {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("Came to load pending messages");
//			Log.d("rmk", "came to load pending msg......");
//			if (WebServiceReferences.Imcollection.size() > 0) {
//				if (WebServiceReferences.Imcollection.containsKey(sessionid)) {
//					ArrayList<SignalingBean> al = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
//							.get(sessionid);
//					for (int i = 0; i < al.size(); i++) {
//						SignalingBean sb = al.get(i);
//
//						if (!sb.getMessage().startsWith("#")) {
//							updateMsg(sb.getConferencemember(),
//									sb.getMessage(), sb.getFrom(), sb);
//						}
//
//					}
//					al.clear();
//				}
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	protected static String getNoteCreateTime() {
//		try {
//			Date curDate = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm a");
//			return sdf.format(curDate);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private void saveAsNote(String strAuthor, String type, String path,
//			String message) {
//
//		try {
//			CompleteListView listall = (CompleteListView) WebServiceReferences.contextTable
//					.get("MAIN");
//
//			CompleteListBean cbean = null;
//
//			if (strAuthor.equalsIgnoreCase(CallDispatcher.LoginUser)) {
//				if (type.equals("note")) {
//					cbean = listall.putDBEntry(1, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Text";
//				} else if (type.equals("photo")) {
//					cbean = listall.putDBEntry(2, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Photo";
//
//				} else if (type.equals("audio")) {
//					cbean = listall.putDBEntry(4, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Audio";
//
//				} else if (type.equals("video")) {
//					cbean = listall.putDBEntry(5, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Video";
//
//				}
//
//			} else {
//				if (type.equals("note")) {
//					cbean = listall.putDBEntry(1, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Text";
//				} else if (type.equals("photo")) {
//					cbean = listall.putDBEntry(2, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Photo";
//				} else if (type.equals("audio")) {
//					cbean = listall.putDBEntry(4, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Audio";
//				} else if (type.equals("video")) {
//					cbean = listall.putDBEntry(5, path, getNoteCreateTime(),
//							message.trim().replaceAll("\n", ""));
//					messagetype = "Video";
//				}
//
//			}
//
//			if (cbean != null) {
//				if (messagetype != null) {
//					if (messagetype.equals("Text")) {
//						Toast.makeText(context, "Text Note saved",
//								Toast.LENGTH_SHORT).show();
//					} else if (messagetype.equals("Photo")) {
//						Toast.makeText(context, "Photo Note saved",
//								Toast.LENGTH_SHORT).show();
//
//					} else if (messagetype.equals("Audio")) {
//						Toast.makeText(context, "Audio Note saved",
//								Toast.LENGTH_SHORT).show();
//
//					} else if (messagetype.equals("Video")) {
//						Toast.makeText(context, "Video Note saved",
//								Toast.LENGTH_SHORT).show();
//
//					}
//				}
//
//			} else {
//				Toast.makeText(context, "Unable to save message",
//						Toast.LENGTH_SHORT).show();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	@SuppressWarnings("finally")
//	private String creteTextFile(String message) {
//		String FilePath = null;
//
//		try {
//
//			FilePath = "/sdcard/COMMedia/" + getFileName() + ".txt";
//
//			TextNoteDatas textnotes = new TextNoteDatas();
//
//			if (textnotes.checkAndCreate(FilePath, message)) {
//
//			} else {
//				FilePath = null;
//			}
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		} finally {
//			return FilePath;
//		}
//	}
//
//	/**
//	 * Creation of sender side UI and return that to its parent view
//	 * 
//	 * @param message
//	 *            - Messae to send them buddies
//	 * @param time
//	 *            - when the user send the message
//	 * @param strSignalId
//	 *            - signal id of the sending message
//	 * @return RelativeLayout
//	 */
//
//	private RelativeLayout imFromME(final String message, String time,
//			String strSignalId, final int flag, final boolean hastoshow,
//			int btnid) {
//
//		try {
//			tvTabName.setTextColor(Color.parseColor("#F5330F"));
//			setAnimation();
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			final LinearLayout callControls = (LinearLayout) inflateLayout1
//					.inflate(R.layout.im_chat_rec, null);
//
//			LinearLayout llayContent = (LinearLayout) callControls
//					.findViewById(R.id.chat_content);
//
//			/**
//			 * For new Implementation as on 17-10-14_8.50AM
//			 */
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(CallDispatcher.LoginUser));
//			// callControls1.setOnLongClickListener(new OnLongClickListener() {
//			//
//			// @Override
//			// public boolean onLongClick(View v) {
//			//
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// saveAsNote(CallDispatcher.LoginUser, "note", path,
//			// message);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("Save my text note");
//			// Log.e("save", "Save my Text Note ");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// return false;
//			// }
//			// });
//			//
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(btnid);
//			// save.setVisibility(View.VISIBLE);
//			//
//			// LinearLayout llayContent = (LinearLayout) callControls1
//			// .findViewById(R.id.rl_content);
//			//
//			// ProgressBar pbSendProgress = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//			// if (hastoshow) {
//			// pbSendProgress.setVisibility(View.VISIBLE);
//			// WebServiceReferences.hsIMProgress.put(strSignalId,
//			// pbSendProgress);
//			// } else {
//			// pbSendProgress.setVisibility(View.INVISIBLE);
//			// }
//			// TextView tvMessage = (TextView) callControls1
//			// .findViewById(R.id.txt_chat_msg);
//			//
//			// tvMessage.setTextColor(Color.BLACK);
//			// tvMessage.setText(message);
//			// tvMessage.setSingleLine(false);
//			// tvMessage.setMaxLines(3);
//			// llayContent.removeView(tvMessage);
//			//
//			// TextView tvMsg = new TextView(context);
//			// tvMsg.setTextColor(Color.BLACK);
//			// tvMsg.setText(message);
//			//
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			// llayContent.addView(tvMsg, paramspa);
//			//
//			// if (message.startsWith("Latitude")) {
//			// // map_container.setVisibility(View.VISIBLE);
//			// llayContent.setGravity(Gravity.CENTER);
//			//
//			// frm_photo = new FrameLayout(context);
//			// frm_photo.setLayoutParams(new LayoutParams(50, 50));
//			//
//			// android.widget.FrameLayout.LayoutParams paramsPrg = new
//			// FrameLayout.LayoutParams(
//			// 35, 35);
//			// paramsPrg.gravity = Gravity.CENTER;
//			// paramsPrg.setMargins(5, 5, 5, 5);
//			// ivPhoto1 = new ImageView(context);
//			// ivPhoto1.setImageResource(R.drawable.location_icon);
//			// ivPhoto1.setLayoutParams(paramsPrg);
//			//
//			// frm_photo.addView(ivPhoto1);
//			// ivPhoto1.setTag(message);
//			// frm_photo.setId(100);
//			// llayContent.addView(frm_photo);
//			// ivPhoto1.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			// // TODO Auto-generated method stub
//			// Intent intent = new Intent(context, buddyLocation.class);
//			// String locs[] = callDisp.getBuddyLocation(v.getTag()
//			// .toString());
//			// intent.putExtra("latitude", locs[0]);
//			// intent.putExtra("longitude", locs[1]);
//			// startActivity(intent);
//			// }
//			// });
//			//
//			// TextView taptoSee = new TextView(context);
//			// taptoSee.setTextColor(getResources().getColor(R.color.im_grey));
//			// taptoSee.setTextSize(10);
//			// taptoSee.setPadding(5, 0, 5, 0);
//			// taptoSee.setText("Tap here to see a map");
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			// paramspas.addRule(Gravity.LEFT);
//			// llayContent.addView(taptoSee, paramspas);
//			//
//			// }
//			//
//			// ProgressBar bar = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//			//
//			// save.setOnClickListener(new OnClickListener() {
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("Save button id -->" + id);
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof TextChatBean) {
//			// TextChatBean bean = (TextChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldFileName();
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("Old Path -->"
//			// + oldpath);
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFileName();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			//
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("New Path --->"
//			// + newpath);
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "note", path, message);
//			// myXMLWriter.updateXMLContent(bean);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my text note");
//			// Log.e("save", "Save my Text Note ");
//			//
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "note", path, message);
//			// myXMLWriter.updateXMLContent(bean);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .error("Save my text note");
//			// Log.e("save", "Save my Text Note ");
//			//
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Came to else part -->"
//			// + bean.getFileName());
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFileName());
//			// if ((bean.getFileName().trim().length() == 0)
//			// && (!bean.getFileName().equalsIgnoreCase(
//			// "empty"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Savemy text note -->"
//			// + bean.getId());
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "note", path, message);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// String qry = "insert into MMChat(sessionid, filepath)"
//			// + "values('"
//			// + sessionid
//			// + "','"
//			// + path + "')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Entry created");
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFileName() + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "note", path, message);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// String qry = "insert into MMChat(sessionid, filepath)"
//			// + "values('"
//			// + sessionid
//			// + "','"
//			// + path + "')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// }
//			// });
//			//
//			// // llaySave.addView(btnSave);
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			// tvTime.setText(time);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			// paramspas.addRule(Gravity.LEFT);
//			// llayContent.addView(tvTime, paramspas);
//			//
//			// final ImageView imgView = new ImageView(context);
//			// if (flag == 0) {
//			//
//			// }
//			// imgView.setImageResource(R.drawable.chatsent);
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.setMargins(0, 0, 5, 5);
//			// params.gravity = Gravity.RIGHT;
//			// if (hastoshow) {
//			// imgView.setImageResource(R.drawable.warning);
//			// WebServiceReferences.hsIMImageView.put(strSignalId, imgView);
//			// } else {
//			// imgView.setImageResource(R.drawable.chatsent);
//			// }
//			//
//			// imgView.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (imgView.getContentDescription() != null
//			// && imgView.getContentDescription().equals("false")) {
//			//
//			// // // TODO Auto-generated method stub
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			//
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			//
//			// ImageView btn = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method
//			// // stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			//
//			// }
//			// });
//			// llayContent.addView(imgView, params);
//			TextView textMessage = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#ff4d4d>"
//					+ CallDispatcher.LoginUser + " : "
//					+ "</font> <font color=#6666FF>" + message + "</font>";
//			textMessage.setText(Html.fromHtml(Chatmessage));
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF,
//					chattingList.getChildCount());
//			rlParent.addView(callControls, parmsContent);
//
//			return rlParent;
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	private Bitmap setUserIcon(String buddy) {
//		try {
//			Bitmap useIcon = null;
//
//			if (buddy.equalsIgnoreCase(CallDispatcher.LoginUser))
//				useIcon = senderBitmap;
//			else
//				useIcon = receiverBitmap;
//
//			return useIcon;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private RelativeLayout imFromMyBuddy(final String strBuddyName,
//			final String message, String time) {
//		try {
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.MATCH_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//
//			/**
//			 * For new Implementation as on 17-10-14_8.50AM
//			 */
//
//			// callControls1.setOnLongClickListener(new OnLongClickListener() {
//			//
//			// @Override
//			// public boolean onLongClick(View v) {
//			//
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// saveAsNote(strBuddyName, "note", path, "Note from "
//			// + strBuddyName);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save Im Frommy text note");
//			// Log.e("save", "IM from my Buddy Text note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// return false;
//			// }
//			// });
//
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(chattingList.getChildCount());
//			//
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			// LinearLayout llayContent = (LinearLayout) callControls1
//			// .findViewById(R.id.chat_content);
//			//
//			// llayContent.removeAllViews();
//			// TextView tvMsg = new TextView(context);
//			// tvMsg.setTextColor(Color.BLACK);
//			// tvMsg.setText(message);
//			//
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvMsg, paramspa);
//			// if (message.startsWith("Latitude")) {
//			// Bitmap bitmap = null;
//			// llayContent.setGravity(Gravity.CENTER);
//			//
//			// frm_photo = new FrameLayout(context);
//			// frm_photo.setLayoutParams(new LayoutParams(50, 50));
//			//
//			// android.widget.FrameLayout.LayoutParams paramsPrg = new
//			// FrameLayout.LayoutParams(
//			// 35, 35);
//			// paramsPrg.gravity = Gravity.CENTER;
//			// paramsPrg.setMargins(5, 5, 5, 5);
//			// ivPhoto1 = new ImageView(context);
//			// ivPhoto1.setImageResource(R.drawable.location_icon);
//			// ivPhoto1.setLayoutParams(paramsPrg);
//			//
//			// frm_photo.addView(ivPhoto1);
//			// ivPhoto1.setTag(message);
//			// frm_photo.setId(100);
//			// llayContent.addView(frm_photo);
//			// ivPhoto1.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			// // TODO Auto-generated method stub
//			// Intent intent = new Intent(context, buddyLocation.class);
//			// String locs[] = callDisp.getBuddyLocation(v.getTag()
//			// .toString());
//			// intent.putExtra("latitude", locs[0]);
//			// intent.putExtra("longitude", locs[1]);
//			// startActivity(intent);
//			// }
//			// });
//			//
//			// TextView taptoSee = new TextView(context);
//			// taptoSee.setTextColor(getResources().getColor(R.color.im_grey));
//			// taptoSee.setTextSize(10);
//			// taptoSee.setPadding(5, 0, 5, 0);
//			// taptoSee.setText("Tap here to see a map");
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			// paramspas.addRule(Gravity.LEFT);
//			// llayContent.addView(taptoSee, paramspas);
//			//
//			// }
//			//
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//			//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// if (childMap.get(id) instanceof TextChatBean) {
//			// TextChatBean bean = (TextChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldFileName();
//			// String newpath = bean.getFileName();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(strBuddyName, "note", path,
//			// "Note from " + strBuddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// Log.e("save", "Save my Text Note ");
//			// String qry =
//			// "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//			// + "values('"
//			// + sessionid
//			// + "','"
//			// + path + "','','')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("entry created...");
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(strBuddyName, "note",
//			// path, "Note from "
//			// + strBuddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// Log.e("save", "Save my Text Note ");
//			// String qry =
//			// "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//			// + "values('"
//			// + sessionid
//			// + "','" + path + "','','')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("entry created...");
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			//
//			// if ((bean.getFileName().trim().length() == 0)
//			// && (!bean.getFileName().equalsIgnoreCase(
//			// "empty"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .error("Save my Text Note "
//			// + path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(strBuddyName, "note", path,
//			// "Note from " + strBuddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .error("Save my Text Note "
//			// + path);
//			// String qry =
//			// "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//			// + "values('"
//			// + sessionid
//			// + "','"
//			// + path + "','','')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .error("entry created..."
//			// + path);
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFileName() + "'"))) {
//			// String path = creteTextFile(message);
//			// if (path != null) {
//			// bean.setFileName(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(strBuddyName, "note", path,
//			// "Note from " + strBuddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// String qry =
//			// "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//			// + "values('"
//			// + sessionid
//			// + "','','')";
//			// if (callDisp.getdbHeler(context)
//			// .ExecuteQuery(qry)) {
//			// Log.i("ary", "entry created...");
//			// }
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// }
//			// });
//
//			TextView textMessage = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#4dffa6>" + strBuddyName + " : "
//					+ "</font> <font color=#6666FF>" + message + "</font>";
//			textMessage.setText(Html.fromHtml(Chatmessage));
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF,
//					chattingList.getChildCount());
//			rlParent.addView(callControls1, parmsContent);
//
//			return rlParent;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * To create and return that received message ui to its parent view
//	 * 
//	 * @param message
//	 *            - Message to send them buddies
//	 * @param time
//	 *            - when the user send the message
//	 * @param strSignalId
//	 *            - signal id of the sending message
//	 * @return RelativeLayout
//	 */
//
//	/**
//	 * Start the Tab title animation when user received the message from them
//	 * buddies.
//	 */
//	private void setAnimation() {
//
//		try {
//			tvTabName.setTypeface(null, Typeface.BOLD);
//
//			TranslateAnimation _tAnim = new TranslateAnimation(0, 0, -100, 0);
//			_tAnim.setInterpolator(new BounceInterpolator());
//			_tAnim.setDuration(3000);
//
//			_tAnim.start();
//
//			_tAnim.setAnimationListener(new AnimationListener() {
//
//				@Override
//				public void onAnimationEnd(Animation animation) {
//
//					animation.start();
//				}
//
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void onAnimationStart(Animation animation) {
//					// TODO Auto-generated method stub
//
//				}
//
//			});
//
//			tvTabName.setAnimation(_tAnim);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * To get the list of group chat members
//	 * 
//	 * @return
//	 */
//	public String getMembers() {
//		try {
//			String str = "";
//
//			members.remove(CallDispatcher.LoginUser);
//
//			for (int i = 0; i < members.size(); i++) {
//
//				str = str + members.get(i) + ",";
//
//			}
//
//			String stx[] = str.split(",");
//			Collections.sort(Arrays.asList(stx));
//			String strtoReturn = "";
//			for (String name : stx) {
//
//				strtoReturn = strtoReturn + name + ",";
//				Log.d("members", "ss " + name);
//			}
//
//			return strtoReturn;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * All the received messages will come to this method from here we have to
//	 * produce the UI
//	 * 
//	 * @param conferenceMembers
//	 * @param message
//	 * @param fromname
//	 */
//	public void updateMsg(final String conferenceMembers, final String message,
//			final String fromname, final SignalingBean sb) {
//
//		try {
//			Log.d("IM", "%*****% Tab Count :"
//					+ imtab.tabHost.getTabWidget().getTabCount()
//					+ " %********%");
//
//			Log.d("im", "im msg");
//			updateHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					Log.d("im", "im msgggggg");
//					String membername = "";
//					for (int i = 0; i < members.size(); i++) {
//
//						if (!members.get(i).equals(CallDispatcher.LoginUser)) {
//
//							membername += members.get(i);
//
//						}
//					}
//					if (myXMLWriter == null) {
//						Log.d("XML", "############### null");
//						myXMLWriter = new XMLComposer("/sdcard/COMMedia/"
//								+ sessionid);
//						myXMLParser = new XMLParser("/sdcard/COMMedia/"
//								+ sessionid);
//						CheckMIM();
//					}
//
//					if (sb.getCallType().equals("MTP")) {
//						TextChatBean txtbean = new TextChatBean();
//						txtbean.setBuddyName(buddy);
//						txtbean.setChatTime(time());
//						txtbean.setColor("0.000000,1.000000,0.000000,0.000000");
//						txtbean.setFace("Helvetica");
//						txtbean.setMessage(message);
//						txtbean.setSize("12.000000");
//						txtbean.setStyle("Normal");
//						txtbean.setType(2);
//						txtbean.setUserName(CallDispatcher.LoginUser);
//						txtbean.setSignalId(sb.getSignalid());
//						appendChatDatatoXML(txtbean);
//						txtbean.setId(childMap.size());
//
//						Log.d("MIM",
//								"@@@@@@@@@@@ hashmap size..." + childMap.size());
//						chattingList.addView(imFromMyBuddy(fromname, message,
//								time()));
//						childMap.add(childMap.size(), txtbean);
//
//						Log.e("rmk", "suspect 1");
//						notifyIM(sb.getMessage(), sb.getFrom());
//						scrollDown();
//					} else {
//						Log.d("IM", "QQQQQQQQQQQQQQQQQQQQ" + message);
//						if (sb.getCallType().toString().equals("MPP")) {
//							Log.d("MPP", "Message type is MPP");
//							/*
//							 * Message msg = new Message(); Bundle bundle = new
//							 * Bundle(); bundle.putString("action", "download");
//							 * bundle.putSerializable("bean", sb); msg.obj =
//							 * bundle; Log.d("thread", "..................." +
//							 * strIPath); Log.d("thread", "..................."
//							 * + sb.getFilePath());
//							 */
//
//							// strIPath = "/sdcard/COMMedia/" +
//							// sb.getFilePath();
//							Log.d("thread", "..................." + strIPath);
//							// DownloadFromFTP(msg);
//							notifyImageDownloaded(sb.getIsDownloadSuccess(),
//									(FTPBean) sb.getFtpObj(), null);
//							notifyIM("Picture message", sb.getFrom());
//
//						} else if (sb.getCallType().toString().equals("MHP")) {
//							Log.d("MPP", "Message type is MHP");
//							/*
//							 * Message msg = new Message(); Bundle bundle = new
//							 * Bundle(); bundle.putString("action", "download");
//							 * bundle.putSerializable("bean", sb); msg.obj =
//							 * bundle; Log.d("thread", "..................." +
//							 * strIPath); Log.d("thread", "..................."
//							 * + sb.getFilePath());
//							 */
//							// strIPath = "/sdcard/COMMedia/" +
//							// sb.getFilePath();
//							Log.d("thread", "..................." + strIPath);
//							// DownloadFromFTP(msg);
//							// notifyImageDownloaded(sb.getIsDownloadSuccess(),(ftpBean)sb.getFtpObj());
//							notifyFPDownloaded(sb.getIsDownloadSuccess(),
//									(FTPBean) sb.getFtpObj(), null);
//							// notifyImageDownloaded(sb.getIsDownloadSuccess(),(ftpBean)sb.getFtpObj());
//							notifyIM("sketch message", sb.getFrom());
//
//						}
//
//						else if (sb.getCallType().toString().equals("MAP")) {
//							Log.d("MPP", "Message type is MAP");
//							/*
//							 * Message msg = new Message(); Bundle bundle = new
//							 * Bundle(); bundle.putString("action", "download");
//							 * bundle.putSerializable("bean", sb); msg.obj =
//							 * bundle; Log.d("thread", "..................." +
//							 * strIPath); Log.d("thread", "..................."
//							 * + sb.getFilePath());
//							 */
//							// strIPath = "/sdcard/COMMedia/" +
//							// sb.getFilePath();
//							/*
//							 * Log.d("thread", "..................." +
//							 * strIPath);
//							 * 
//							 * DownloadFromFTP(msg);
//							 */
//							notifyAudioDownloaded(sb.getIsDownloadSuccess(),
//									(FTPBean) sb.getFtpObj(), null);
//							notifyIM("Audio File Received", sb.getFrom());
//						} else if (sb.getCallType().equals("MVP")) {
//							notifyVideoDownloaded(sb.getIsDownloadSuccess(),
//									(FTPBean) sb.getFtpObj(), null);
//							notifyIM("Video File Receeeived", sb.getFrom());
//						}
//					}
//					if (conferenceMembers != null) {
//
//						if (conferenceMembers.trim().length() > 0) {
//							String values[] = conferenceMembers.split(",");
//
//							for (int i = 0; i < values.length; i++) {
//								if (!members.contains(values[i])) {
//									members.add(values[i]);
//
//								}
//							}
//						}
//					}
//					sview.post(new Runnable() {
//						@Override
//						public void run() {
//							sview.fullScroll(ScrollView.FOCUS_DOWN);
//						}
//					});
//
//					IMTabScreen imScreen = (IMTabScreen) WebServiceReferences.contextTable
//							.get("imtabs");
//					String comembers = imScreen
//							.getArrangedMembers(conferenceMembers);
//					String comembers1 = imScreen
//							.getArrangedMembers(confmembers);
//					if (comembers.length() > comembers1.length()) {
//						confmembers = conferenceMembers;
//						String update = "update component set comment='"
//								+ comembers + "' where componentpath='"
//								+ sessionid + "'";
//						callDisp.getdbHeler(context).ExecuteQuery(update);
//
//					}
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	public void DownloadFromFTP(Message msg) {
//		// TODO Auto-generated method stub
//		try {
//			Log.d("MPP", "Comes to mppReceiveHandler ");
//
//			Bundle bun = (Bundle) msg.obj;
//
//			bun.getString("action");
//			SignalingBean sb = (SignalingBean) bun.getSerializable("bean");
//
//			if (sb.getCallType().equals("MPP")) {
//				showToast("Downloading Picture from" + sb.getFrom());
//
//				if (CallDispatcher.LoginUser != null) {
//					isdownload = true;
//					FTPBean bean = new FTPBean();
//					bean.setFtp_username(sb.getFtpUser());
//					bean.setFtp_password(sb.getFtppassword());
//					bean.setOperation_type(2);
//					bean.setComment(sessionid);
//					bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//					bean.setReq_object(sb);
//					bean.setRequest_from("MPP");
//					notifyFTPServerConnected(true, bean);
//				}
//
//			}
//			if (sb.getCallType().equals("MHP")) {
//				showToast("Downloading Picture from" + sb.getFrom());
//
//				if (CallDispatcher.LoginUser != null) {
//					isdownload = true;
//					FTPBean bean = new FTPBean();
//					bean.setFtp_username(sb.getFtpUser());
//					bean.setFtp_password(sb.getFtppassword());
//					bean.setOperation_type(2);
//					bean.setComment(sessionid);
//					bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//					bean.setReq_object(sb);
//					bean.setRequest_from("MPP");
//					notifyFTPServerConnected(true, bean);
//				}
//
//			} else if (sb.getCallType().equals("MAP")) {
//
//				showToast("Downloading Audio from" + sb.getFrom());
//				if (CallDispatcher.LoginUser != null) {
//					isdownload = true;
//					FTPBean bean = new FTPBean();
//					bean.setFtp_username(sb.getFtpUser());
//					bean.setFtp_password(sb.getFtppassword());
//					bean.setOperation_type(2);
//					bean.setComment(sessionid);
//					bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//					bean.setReq_object(sb);
//					bean.setRequest_from("MAP");
//					notifyFTPServerConnected(true, bean);
//				}
//
//			} else if (sb.getCallType().equals("MVP")) {
//				showToast("Downloading Video from" + sb.getFrom());
//				if (CallDispatcher.LoginUser != null) {
//					isdownload = true;
//					FTPBean bean = new FTPBean();
//					bean.setFtp_username(sb.getFtpUser());
//					bean.setFtp_password(sb.getFtppassword());
//					bean.setOperation_type(2);
//					bean.setComment(sessionid);
//					bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//					bean.setReq_object(sb);
//					bean.setRequest_from("MVP");
//					notifyFTPServerConnected(true, bean);
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * get the send / received time of the IM
//	 * 
//	 * @return
//	 */
//	private String time() {
//		try {
//			String delegate = CallDispatcher.dateFormat + " hh:mm:ss aaa";
//			return (String) DateFormat.format(delegate, Calendar.getInstance()
//					.getTime());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * Perform any final cleanup before an activity is destroyed
//	 */
//	protected void onDestroy() {
//		try {
//			// TODO Auto-generated method stub
//
//			if (!callDisp.isApplicationInBackground(context)) {
//				if (WebServiceReferences.Imcollection.containsKey(sessionid)) {
//					WebServiceReferences.Imcollection.remove(sessionid);
//				}
//			}
//
//			if (WebServiceReferences.contextTable.containsKey("imscreen")) {
//				WebServiceReferences.contextTable.remove("imscreen");
//			}
//
//			WebServiceReferences.instantMessageScreen.remove(sessionid);
//			if (sview != null && chattingList != null)
//				sview.removeView(chattingList);
//			super.onDestroy();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * After the chat process and activity close if user received any im from
//	 * them user, at that time load the existing IM messages
//	 * 
//	 * @param chat
//	 * @param imbeans
//	 */
//
//	/**
//	 * To show the list of online buddies.
//	 * 
//	 * @return
//	 */
//	public String[] getOnlineBuddys() {
//		try {
//			Set set = WebServiceReferences.buddyList.keySet();
//			List<String> buddies = new ArrayList<String>();
//			Iterator<String> itr = set.iterator();
//			while (itr.hasNext()) {
//				String buddy = itr.next();
//				BuddyInformationBean bean = WebServiceReferences.buddyList
//						.get(buddy);
//				if (!bean.getStatus().equals("Offline")) {
//					if (!members.contains(buddy)) {
//						buddies.add(buddy);
//					}
//				}
//			}
//			String arr[] = buddies.toArray(new String[buddies.size()]);
//			return arr;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	/**
//	 * Show the dialog with list of online buddies.User can add the member to
//	 * the group chat
//	 */
//	public void showDialog() {
//
//		try {
//			builder = new AlertDialog.Builder(this);
//			builder.create();
//			builder.setTitle("Add People");
//
//			final CharSequence[] choiceList = getOnlineBuddys();
//			int selected = -1; // does not select anything
//
//			builder.setSingleChoiceItems(choiceList, selected,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Toast.makeText(context,
//									"Select " + choiceList[which],
//									Toast.LENGTH_SHORT).show();
//							SignalingBean sb = null;
//							// System.out.println("########## " + getMembers()
//							// + (String) choiceList[which]);
//							String str[] = (getMembers() + (String) choiceList[which])
//									.split(",");
//							// System.out.println("str size :" + str.length);
//							Utility utility = new Utility();
//							String ssid = null;
//
//							if (groupmode) {
//								Log.d("sdialog", "in group mode");
//								ssid = sessionid;
//								Log.d("sdialog", "group mode confmems "
//										+ confmembers);
//								confmembers = confmembers + ","
//										+ (String) choiceList[which] + ",";
//								Log.d("sdialog", "group mode confmems "
//										+ confmembers);
//							} else {
//								Log.d("sdialog", "not in group mode");
//								ssid = utility.getSessionID();
//							}
//
//							for (int i = 0; i < str.length; i++) {
//								BuddyInformationBean bib = null;
//								// System.out.println("str[i] :" + "i=" + i +
//								// " "
//								// + str[i]);
//								Log.d("sdialog", "for in group mode");
//								bib = WebServiceReferences.buddyList
//										.get(str[i]);
//								sb = new SignalingBean();
//								sb.setFrom(CallDispatcher.LoginUser);
//								sb.setTolocalip(bib.getLocalipaddress());
//								sb.setTopublicip(bib.getExternalipaddress());
//								sb.setTo(str[i]);
//								sb.setType("11");
//
//								sb.setSessionid(ssid);
//
//								sb.setResult("0");
//								sb.setCallType("MSG");
//								sb.setMessage("Hi");
//								sb.setToSignalPort(bib.getSignalingPort());
//								// System.out
//								// .println("$$$$$$$$$$$44 :" + getMembers());
//								// System.out
//								// .println("#############  CONFERENCE MEMBERS  "
//								// + getMembers()
//								// + (String) choiceList[which]
//								// + ","
//								// + CallDispatcher.LoginUser);
//								sb.setConferencemember(getMembers()
//										+ (String) choiceList[which] + ","
//										+ CallDispatcher.LoginUser);
//								// sb.setMessage(getMembers()+(String)choiceList[which]+","+CallDispatcher.LoginUser);
////								CallDispatcher.commEngine.makeIM(sb);
//							}
//
//							if (!groupmode) {
//								Log.d("sdialog", "create in group mode");
//								imtab.addTab(sb, true);
//
//							}
//
//							if (groupmode) {
//								Log.d("sdialog", "neglect create in group mode");
//								members.add(choiceList[which].toString());
//								updateUsersInTable();
//
//							}
//							/*
//							 * Utility utility=new Utility(); Intent i=new
//							 * Intent(context,InstantMessage.class);
//							 * i.putExtra("msg","Conference IM ");
//							 * i.putExtra("buddy",choiceList[which]);
//							 * i.putExtra("sessionid",utility.getSessionID());
//							 * context.startActivity(i);
//							 */
//							alert.dismiss();
//						}
//					});
//			alert = builder.create();
//			alert.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//
//		}
//	}
//
//	Bitmap img = null, bitmap = null;
//	private ProgressDialog dialog = null;
//
//	private void showprogress() {
//
//		try {
//			dialog = new ProgressDialog(context);
//			dialog.setCancelable(true);
//			dialog.setMessage("Progress ...");
//			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			dialog.setProgress(0);
//			dialog.setMax(100);
//			dialog.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private void cancelDialog() {
//		try {
//			if (dialog != null) {
//				dialog.dismiss();
//				dialog = null;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	public class bitmaploader extends AsyncTask<Uri, Void, Void> {
//
//		@Override
//		protected void onPostExecute(Void result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			try {
//				Log.d("image", "came to post execute for image");
//				cancelDialog();
//				if (strIPath != null)
//					img = callDisp.ResizeImage(strIPath);
//				if (img != null) {
//
//					File fle = new File(strIPath);
//					fle.createNewFile();
//					FileOutputStream fout = new FileOutputStream(strIPath);
//					img.compress(CompressFormat.JPEG, 75, fout);
//
//					img = Bitmap.createScaledBitmap(img, 100, 75, false);
//					Log.d("OnActivity", "_____On Activity Called______");
//					final RelativeLayout rlSender = new RelativeLayout(context);
//					rlSender.setLayoutParams(new LayoutParams(
//							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//					rlSender.setBackgroundResource(R.drawable.corner);
//
//					RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//							RelativeLayout.LayoutParams.WRAP_CONTENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//					RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//							RelativeLayout.LayoutParams.WRAP_CONTENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//					MyAbsoluteLayout llayOption = new MyAbsoluteLayout(context);
//					llayOption.setLayoutParams(new LayoutParams(
//							LinearLayout.LayoutParams.FILL_PARENT,
//							LinearLayout.LayoutParams.WRAP_CONTENT));
//
//					final LinearLayout llayPhoto = new LinearLayout(context);
//					llayPhoto.setGravity(Gravity.CENTER);
//					final LinearLayout llayPhoto1 = new LinearLayout(context);
//					llayPhoto1.setGravity(Gravity.CENTER);
//
//					rlSender.addView(llayPhoto1, layRight);
//					rlSender.addView(llayPhoto, layLeft);
//
//					LinearLayout llayPOpt = new LinearLayout(context);
//					llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//					final Button btnSendPhoto = new Button(context);
//					new Button(context);
//					final Button btnView = new Button(context);
//					final Button btnExit = new Button(context);
//					btnExit.setText("Exit");
//
//					btnExit.setLayoutParams(new LayoutParams(100, 30));
//					btnView.setText("ViewPhoto");
//					btnView.setLayoutParams(new LayoutParams(100, 30));
//
//					btnSendPhoto.setText("Send");
//					btnSendPhoto.setGravity(Gravity.CENTER);
//					btnSendPhoto.setLayoutParams(new LayoutParams(
//							LayoutParams.WRAP_CONTENT,
//							LayoutParams.WRAP_CONTENT));
//					btnSendPhoto.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							if (buddy != null) {
//
//								BuddyInformationBean bib = WebServiceReferences.buddyList
//										.get(buddy);
//								FTPBean bean = new FTPBean();
//								bean.setFtp_username(CallDispatcher.LoginUser);
//								bean.setFtp_password(CallDispatcher.Password);
//								bean.setServer_port(40400);
//								bean.setOperation_type(1);
//								bean.setComment(sessionid);
//								bean.setServer_ip(callDisp.getRouter().split(
//										":")[0]);
//								bean.setReq_object(llayPhoto);
//								bean.setRequest_from("MPP");
//								bean.setFile_path(strIPath);
//								notifyFTPServerConnected(true, bean);
//								if (!isForceLogout && bib != null) {
//									if (!bib.getStatus().equals("Online")) {
//										showToast("Sorry! can not send Message");
//									} else if (CallDispatcher.LoginUser == null) {
//										showToast("The buddy is no in Online state");
//									}
//								} else {
//									showToast("Sorry! can not send Message");
//								}
//							}
//
//						}
//					});
//
//					llayPOpt.addView(btnSendPhoto);
//					/*
//					 * llayPOpt.addView(btnRetake); llayPOpt.addView(btnExit);
//					 */
//					llayPhoto1.addView(llayPOpt);
//					if (mmlay.getChildCount() == 0) {
//						mmlay.addView(rlSender);
//					} else {
//						mmlay.removeAllViews();
//						if (llayTimer != null) {
//							llayTimer.removeAllViews();
//						}
//						mmlay.addView(rlSender);
//					}
//
//					Button exit = new Button(context);
//					exit.setText("Exit");
//					exit.setGravity(Gravity.CENTER);
//					llayPOpt.addView(exit);
//					exit.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							// photo.setEnabled(true);
//							llayPhoto.removeAllViews();
//							llayPhoto1.removeAllViews();
//							mmlay.removeAllViews();
//							File fl = new File(strIPath);
//							if (fl.exists()) {
//								fl.delete();
//							}
//						}
//					});
//
//					bitmap = null;
//
//					// if (fileCheck.exists()) {
//					bitmap = callDisp.ResizeImage(strIPath);
//					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
//					// }
//					if (bitmap != null)
//						ivPhoto.setImageBitmap(bitmap);
//
//					ivPhoto.setPadding(10, 10, 10, 10);
//
//					if (llayPhoto.getChildCount() == 0) {
//						llayPhoto.addView(ivPhoto);
//					}
//					Log.d("IM", "************8Layout Inserted************");
//
//				} else {
//					strIPath = null;
//					// photo.setEnabled(true);
//					showToast("Kindly choose image files");
//				}
//
//			} catch (Exception e) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			showprogress();
//		}
//
//		@Override
//		protected Void doInBackground(Uri... params) {
//			// TODO Auto-generated method stub
//			try {
//				for (Uri uri : params) {
//					Log.d("image", "came to doin backgroung for image");
//					strIPath = Environment.getExternalStorageDirectory()
//							+ "/COMMedia/" + CompleteListView.getFileName()
//							+ ".jpg";
//					FileInputStream fin = (FileInputStream) getContentResolver()
//							.openInputStream(uri);
//					ByteArrayOutputStream straam = new ByteArrayOutputStream();
//					byte[] content = new byte[1024];
//					int bytesread;
//					while ((bytesread = fin.read(content)) != -1) {
//						straam.write(content, 0, bytesread);
//					}
//					byte[] bytes = straam.toByteArray();
//					FileOutputStream fout = new FileOutputStream(strIPath);
//					straam.flush();
//					straam.close();
//					straam = null;
//					fin.close();
//					fin = null;
//					fout.write(bytes);
//					fout.flush();
//					fout.close();
//					fout = null;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		try {
//			super.onActivityResult(requestCode, resultCode, data);
//			Log.d("lggg", "@@@@@@@@@@@@@@@@@" + requestCode);
//			Log.d("lggg", "@@@@@@@@@@@@@@@@@" + resultCode);
//			if (requestCode == 2) {
//				if (resultCode == Activity.RESULT_CANCELED) {
//					if (photo != null) {
//						// photo.setEnabled(true);
//					}
//				} else {
//					Uri selectedImageUri = data.getData();
//					strIPath = callDisp.getRealPathFromURI(selectedImageUri);
//					File selected_file = new File(strIPath);
//					int length = (int) selected_file.length() / 1048576;
//					Log.d("busy", "........ size is------------->" + length);
//					if (length <= 2) {
//
//						img = null;
//						img = callDisp.ResizeImage(strIPath);
//						img = Bitmap.createScaledBitmap(img, 100, 75, false);
//
//						if (img != null) {
//
//							Log.d("OnActivity", "_____On Activity Called______");
//							final RelativeLayout rlSender = new RelativeLayout(
//									context);
//							rlSender.setLayoutParams(new LayoutParams(
//									LayoutParams.FILL_PARENT,
//									LayoutParams.WRAP_CONTENT));
//							rlSender.setBackgroundResource(R.drawable.corner);
//
//							RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//									RelativeLayout.LayoutParams.WRAP_CONTENT,
//									RelativeLayout.LayoutParams.WRAP_CONTENT);
//							layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//							RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//									RelativeLayout.LayoutParams.WRAP_CONTENT,
//									RelativeLayout.LayoutParams.WRAP_CONTENT);
//							layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//							MyAbsoluteLayout llayOption = new MyAbsoluteLayout(
//									context);
//							llayOption.setLayoutParams(new LayoutParams(
//									LinearLayout.LayoutParams.FILL_PARENT,
//									LinearLayout.LayoutParams.WRAP_CONTENT));
//
//							final LinearLayout llayPhoto = new LinearLayout(
//									context);
//							llayPhoto.setGravity(Gravity.CENTER);
//							final LinearLayout llayPhoto1 = new LinearLayout(
//									context);
//							llayPhoto1.setGravity(Gravity.CENTER);
//
//							rlSender.addView(llayPhoto1, layRight);
//							rlSender.addView(llayPhoto, layLeft);
//
//							LinearLayout llayPOpt = new LinearLayout(context);
//							llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//							final Button btnSendPhoto = new Button(context);
//							new Button(context);
//							final Button btnView = new Button(context);
//							final Button btnExit = new Button(context);
//							btnExit.setText("Exit");
//
//							btnExit.setLayoutParams(new LayoutParams(100, 30));
//							btnView.setText("ViewPhoto");
//							btnView.setLayoutParams(new LayoutParams(100, 30));
//
//							btnSendPhoto.setText("Send");
//							btnSendPhoto.setGravity(Gravity.CENTER);
//							btnSendPhoto.setLayoutParams(new LayoutParams(
//									LayoutParams.WRAP_CONTENT,
//									LayoutParams.WRAP_CONTENT));
//							btnSendPhoto
//									.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											if (buddy != null) {
//
//												BuddyInformationBean bib = WebServiceReferences.buddyList
//														.get(buddy);
//												FTPBean bean = new FTPBean();
//												bean.setFtp_username(CallDispatcher.LoginUser);
//												bean.setFtp_password(CallDispatcher.Password);
//												bean.setOperation_type(1);
//												bean.setComment(sessionid);
//												bean.setServer_ip(callDisp
//														.getRouter().split(":")[0]);
//												bean.setReq_object(llayPhoto);
//												bean.setRequest_from("MPP");
//												bean.setServer_port(40400);
//												bean.setFile_path(strIPath);
//												notifyFTPServerConnected(true,
//														bean);
//
//												if (!isForceLogout
//														&& bib != null) {
//													if (!bib.getStatus()
//															.equals("Online")) {
//														showToast("Sorry! can not send Message");
//													} else if (CallDispatcher.LoginUser == null) {
//														showToast("The buddy is no in Online state");
//													}
//												} else {
//													showToast("Sorry! can not send Message");
//												}
//											}
//
//										}
//									});
//
//							llayPOpt.addView(btnSendPhoto);
//							llayPhoto1.addView(llayPOpt);
//							if (mmlay.getChildCount() == 0) {
//								mmlay.addView(rlSender);
//							} else {
//								mmlay.removeAllViews();
//								if (llayTimer != null) {
//									llayTimer.removeAllViews();
//								}
//								mmlay.addView(rlSender);
//							}
//
//							Button exit = new Button(context);
//							exit.setText("Exit");
//							exit.setGravity(Gravity.CENTER);
//							llayPOpt.addView(exit);
//							exit.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									// photo.setEnabled(true);
//									llayPhoto.removeAllViews();
//									llayPhoto1.removeAllViews();
//									mmlay.removeAllViews();
//									File fl = new File(strIPath);
//									if (fl.exists()) {
//										fl.delete();
//									}
//								}
//							});
//
//							bitmap = null;
//
//							// if (fileCheck.exists()) {
//							bitmap = callDisp.ResizeImage(strIPath);
//							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
//									150, false);
//							// }
//							if (bitmap != null)
//								ivPhoto.setImageBitmap(bitmap);
//
//							ivPhoto.setPadding(10, 10, 10, 10);
//
//							if (llayPhoto.getChildCount() == 0) {
//								llayPhoto.addView(ivPhoto);
//							}
//							Log.d("IM",
//									"************8Layout Inserted************");
//
//						} else {
//							strIPath = null;
//							// photo.setEnabled(true);
//							showToast("Kindly choose image files");
//						}
//					}
//
//				}
//			} else if (requestCode == 19) {
//
//				if (resultCode == Activity.RESULT_CANCELED) {
//					if (photo != null) {
//						// photo.setEnabled(true);
//					}
//
//				} else {
//
//					Uri selectedImageUri = data.getData();
//					// final int takeFlags = data.getFlags()
//					// & (Intent.FLAG_GRANT_READ_URI_PERMISSION |
//					// Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//					// getContentResolver().takePersistableUriPermission(
//					// selectedImageUri, takeFlags);
//					getContentResolver().openInputStream(selectedImageUri);
//					strIPath = Environment.getExternalStorageDirectory()
//							+ "/COMMedia/" + getFileName() + ".jpg";
//					File selected_file = new File(strIPath);
//					int length = (int) selected_file.length() / 1048576;
//					Log.d("busy", "........ size is------------->" + length);
//					if (length <= 2) {
//
//						img = null;
//						new bitmaploader().execute(selectedImageUri);
//					} else {
//						showToast("Kindly Select someother image,this image is too large");
//					}
//
//				}
//
//			} else if (requestCode == 101) {
//
//				if (resultCode == Activity.RESULT_CANCELED) {
//					if (photo != null) {
//						// photo.setEnabled(true);
//					}
//
//				} else {
//					Bundle bun = data.getBundleExtra("sketch");
//
//					strIPath = bun.getString("path");
//
//					img = null;
//					img = callDisp.ResizeImage(strIPath);
//					img = Bitmap.createScaledBitmap(img, 100, 75, false);
//					if (img != null) {
//
//						Log.d("OnActivity", "_____On Activity Called______");
//						final RelativeLayout rlSender = new RelativeLayout(
//								context);
//						rlSender.setLayoutParams(new LayoutParams(
//								LayoutParams.FILL_PARENT,
//								LayoutParams.WRAP_CONTENT));
//						rlSender.setBackgroundResource(R.drawable.corner);
//
//						RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//								RelativeLayout.LayoutParams.WRAP_CONTENT,
//								RelativeLayout.LayoutParams.WRAP_CONTENT);
//						layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//						RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//								RelativeLayout.LayoutParams.WRAP_CONTENT,
//								RelativeLayout.LayoutParams.WRAP_CONTENT);
//						layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//						MyAbsoluteLayout llayOption = new MyAbsoluteLayout(
//								context);
//						llayOption.setLayoutParams(new LayoutParams(
//								LinearLayout.LayoutParams.FILL_PARENT,
//								LinearLayout.LayoutParams.WRAP_CONTENT));
//
//						final LinearLayout llayPhoto = new LinearLayout(context);
//						llayPhoto.setGravity(Gravity.CENTER);
//						final LinearLayout llayPhoto1 = new LinearLayout(
//								context);
//						llayPhoto1.setGravity(Gravity.CENTER);
//
//						rlSender.addView(llayPhoto1, layRight);
//						rlSender.addView(llayPhoto, layLeft);
//
//						LinearLayout llayPOpt = new LinearLayout(context);
//						llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//						final Button btnSendPhoto = new Button(context);
//						new Button(context);
//						final Button btnView = new Button(context);
//						final Button btnExit = new Button(context);
//						btnExit.setText("Exit");
//
//						btnExit.setLayoutParams(new LayoutParams(100, 30));
//						btnView.setText("ViewPhoto");
//						btnView.setLayoutParams(new LayoutParams(100, 30));
//
//						btnSendPhoto.setText("Send");
//						btnSendPhoto.setGravity(Gravity.CENTER);
//						btnSendPhoto.setLayoutParams(new LayoutParams(
//								LayoutParams.WRAP_CONTENT,
//								LayoutParams.WRAP_CONTENT));
//						btnSendPhoto.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								if (buddy != null) {
//									BuddyInformationBean bib = WebServiceReferences.buddyList
//											.get(buddy);
//									FTPBean bean = new FTPBean();
//									bean.setFtp_username(callDisp.LoginUser);
//									bean.setFtp_password(callDisp.Password);
//									bean.setOperation_type(1);
//									bean.setComment(sessionid);
//									bean.setServer_ip(callDisp.getRouter()
//											.split(":")[0]);
//									bean.setServer_port(40400);
//									bean.setReq_object(llayPhoto);
//									bean.setRequest_from("MHP");
//									bean.setFile_path(strIPath);
//
//									notifyFTPServerConnected(true, bean);
//									if (!isForceLogout && bib != null) {
//										if (!bib.getStatus().equals("Online")) {
//											showToast("Sorry! can not send Message");
//										} else if (CallDispatcher.LoginUser == null) {
//											showToast("The buddy is no in Online state");
//										}
//									} else {
//										showToast("Sorry! can not send Message");
//									}
//								}
//
//							}
//						});
//
//						llayPOpt.addView(btnSendPhoto);
//						/*
//						 * llayPOpt.addView(btnRetake);
//						 * llayPOpt.addView(btnExit);
//						 */
//						llayPhoto1.addView(llayPOpt);
//						if (mmlay.getChildCount() == 0) {
//							mmlay.addView(rlSender);
//						} else {
//							mmlay.removeAllViews();
//							if (llayTimer != null) {
//								llayTimer.removeAllViews();
//							}
//							mmlay.addView(rlSender);
//						}
//
//						Button exit = new Button(context);
//						exit.setText("Exit");
//						exit.setGravity(Gravity.CENTER);
//						llayPOpt.addView(exit);
//						exit.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								// photo.setEnabled(true);
//								llayPhoto.removeAllViews();
//								llayPhoto1.removeAllViews();
//								mmlay.removeAllViews();
//								File fl = new File(strIPath);
//								if (fl.exists()) {
//									fl.delete();
//								}
//							}
//						});
//
//						bitmap = null;
//
//						// if (fileCheck.exists()) {
//						bitmap = callDisp.ResizeImage(strIPath);
//						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
//								false);
//						// }
//						if (bitmap != null)
//							ivPhoto.setImageBitmap(bitmap);
//						ivPhoto.setPadding(10, 10, 10, 10);
//						if (llayPhoto.getChildCount() == 0) {
//							llayPhoto.addView(ivPhoto);
//						}
//						Log.d("IM", "************8Layout Inserted************");
//
//					} else {
//						strIPath = null;
//						// photo.setEnabled(true);
//						showToast("Kindly choose image files");
//					}
//
//				}
//
//			}
//
//			else if (requestCode == 0) {
//
//				File fileCheck = new File(strIPath);
//
//				if (fileCheck.exists()) {
//					Log.d("OnActivity", "_____On Activity Called______");
//					final RelativeLayout rlSender = new RelativeLayout(context);
//					rlSender.setLayoutParams(new LayoutParams(
//							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//					rlSender.setBackgroundResource(R.drawable.corner);
//
//					RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//							RelativeLayout.LayoutParams.WRAP_CONTENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//					RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//							RelativeLayout.LayoutParams.WRAP_CONTENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//					MyAbsoluteLayout llayOption = new MyAbsoluteLayout(context);
//					llayOption.setLayoutParams(new LayoutParams(
//							LinearLayout.LayoutParams.FILL_PARENT,
//							LinearLayout.LayoutParams.WRAP_CONTENT));
//
//					final LinearLayout llayPhoto = new LinearLayout(context);
//					llayPhoto.setGravity(Gravity.CENTER);
//					final LinearLayout llayPhoto1 = new LinearLayout(context);
//					llayPhoto1.setGravity(Gravity.CENTER);
//
//					rlSender.addView(llayPhoto1, layRight);
//					rlSender.addView(llayPhoto, layLeft);
//
//					LinearLayout llayPOpt = new LinearLayout(context);
//					llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//					final Button btnSendPhoto = new Button(context);
//					new Button(context);
//					final Button btnView = new Button(context);
//					final Button btnExit = new Button(context);
//					btnExit.setText("Exit");
//
//					btnExit.setLayoutParams(new LayoutParams(100, 30));
//					btnView.setText("ViewPhoto");
//					btnView.setLayoutParams(new LayoutParams(100, 30));
//
//					btnSendPhoto.setText("Send");
//					btnSendPhoto.setGravity(Gravity.CENTER);
//					btnSendPhoto.setLayoutParams(new LayoutParams(
//							LayoutParams.WRAP_CONTENT,
//							LayoutParams.WRAP_CONTENT));
//					btnSendPhoto.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							if (buddy != null) {
//
//								BuddyInformationBean bib = WebServiceReferences.buddyList
//										.get(buddy);
//								FTPBean bean = new FTPBean();
//								bean.setFtp_username(CallDispatcher.LoginUser);
//								bean.setFtp_password(CallDispatcher.Password);
//								bean.setOperation_type(1);
//								bean.setComment(sessionid);
//								bean.setServer_ip(callDisp.getRouter().split(
//										":")[0]);
//								bean.setServer_port(40400);
//								bean.setReq_object(llayPhoto);
//								bean.setRequest_from("MPP");
//								bean.setFile_path(strIPath);
//								notifyFTPServerConnected(true, bean);
//								if (!isForceLogout && bib != null) {
//									if (CallDispatcher.LoginUser == null) {
//										showToast("Sorry! can not send Message");
//									} else if (!bib.getStatus()
//											.equals("Online")) {
//										showToast("The buddy is no in Online state");
//									}
//								} else {
//									showToast("Sorry! can not send Message");
//								}
//							}
//
//						}
//					});
//
//					llayPOpt.addView(btnSendPhoto);
//					/*
//					 * llayPOpt.addView(btnRetake); llayPOpt.addView(btnExit);
//					 */
//					llayPhoto1.addView(llayPOpt);
//					if (mmlay.getChildCount() == 0) {
//						mmlay.addView(rlSender);
//					} else {
//						mmlay.removeAllViews();
//						if (llayTimer != null) {
//							llayTimer.removeAllViews();
//						}
//						mmlay.addView(rlSender);
//					}
//
//					Button exit = new Button(context);
//					exit.setText("Exit");
//					exit.setGravity(Gravity.CENTER);
//					llayPOpt.addView(exit);
//					exit.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							// photo.setEnabled(true);
//							llayPhoto.removeAllViews();
//							llayPhoto1.removeAllViews();
//							mmlay.removeAllViews();
//							File fl = new File(strIPath);
//							if (fl.exists()) {
//								fl.delete();
//							}
//						}
//					});
//
//					bitmap = null;
//					// if (fileCheck.exists()) {
//					bitmap = callDisp.ResizeImage(strIPath);
//					callDisp.changemyPictureOrientation(bitmap, strIPath);
//					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
//					// }
//					if (bitmap != null && !bitmap.isRecycled())
//						bitmap.recycle();
//					bitmap = null;
//					bitmap = callDisp.ResizeImage(strIPath);
//					if (bitmap != null)
//						ivPhoto.setImageBitmap(bitmap);
//					ivPhoto.setPadding(10, 10, 10, 10);
//					if (llayPhoto.getChildCount() == 0) {
//						llayPhoto.addView(ivPhoto);
//					}
//					Log.d("IM", "************8Layout Inserted************");
//				} else {
//					// photo.setEnabled(true);
//				}
//			} else if (requestCode == 1) {
//				if (requestCode == 1) {
//
//					isVideoRecord = false;
//
//					mmlay.setBackgroundDrawable(null);
//					if (llayTimer != null) {
//						llayTimer.removeAllViews();
//					}
//					mmlay.removeAllViews();
//					Log.d("NOTES", "path in original--->" + strIPath);
//
//					// Bundle bndl = data.getExtras();
//					// if (bndl != null)
//					// strIPath = bndl.getString("video_name");
//
//					Log.d("NOTES", "path in bundle--->" + strIPath);
//
//					// llayMMOption.addView(tabscreenview.Videochatscreen(context,
//					// strIPath));Videochat
//					// llayMMOption.addView(tabscreenview.Videochat(strIPath+".mp4",
//					// context));
//
//					if (strIPath != null) {
//
//						System.out
//								.println("--------onActivityResult-----video-----"
//										+ strIPath + ".mp4");
//						File f = new File(strIPath);
//
//						if (f.exists()) {
//							if (f.length() != 0) {
//								if (mmlay.getChildCount() == 0) {
//									CreateVideoThumbnail(strIPath.substring(0,
//											strIPath.lastIndexOf(".mp4")));
//									mmlay.addView(Videochat(strIPath));
//								}
//							} else {
//								f.delete();
//								// video.setEnabled(true);
//							}
//						}
//
//					}
//
//				}
//
//			} else if (requestCode == 100 && resultCode == -10)
//
//			{
//				Log.d("lgg", "came to onactivity result....");
//				Bundle bun = data.getBundleExtra("share");
//				Log.d("lgg", "came to onactivity result...." + bun);
//				if (bun != null) {
//					String path = bun.getString("filepath");
//					String type = bun.getString("type");
//					if (type.equalsIgnoreCase("note")) {
//						if (path != null) {
//							File fl = new File(path);
//							if (fl.exists()) {
//								importedFile = path;
//								isimported = true;
//								TextNoteDatas datas = new TextNoteDatas();
//								String message = datas.getInformation(path);
//								myMessage.setText(message);
//								datas = null;
//
//							} else {
//								myMessage.setText("");
//							}
//						} else {
//							myMessage.setText("");
//						}
//					} else if (type.equalsIgnoreCase("audio")) {
//						if (path != null) {
//							File a_file = new File(path);
//							if (a_file.exists()) {
//
//								importedFile = path;
//								isimported = true;
//								strIPath = path;
//
//								FTPBean bean = new FTPBean();
//								bean.setFtp_username(callDisp.LoginUser);
//								bean.setFtp_password(callDisp.Password);
//								bean.setOperation_type(1);
//								bean.setComment(sessionid);
//								bean.setServer_ip(callDisp.getRouter().split(
//										":")[0]);
//								bean.setRequest_from("MAP");
//								bean.setFile_path(strIPath);
//
//								/*
//								 * if(callDisp.clnt==null) { callDisp.clnt=new
//								 * ftpClient(); } callDisp .clnt.
//								 * dooperation(bean,"MMChat" );
//								 */
//								fromexist = true;
//								notifyFTPServerConnected(true, bean);
//
//							} else {
//								isaudio_imported = false;
//							}
//						}
//					} else if (type.equalsIgnoreCase("video")) {
//						Log.d("lggg", "NNNNNNNNNNNNNNNNNNNNNN" + path);
//						if (path != null) {
//							File v_file = new File(path + ".mp4");
//							if (v_file.exists()) {
//
//								importedFile = path;
//								isimported = true;
//								isVideoRecord = false;
//								strIPath = path;
//								mmlay.setBackgroundDrawable(null);
//								if (llayTimer != null) {
//									llayTimer.removeAllViews();
//								}
//								mmlay.removeAllViews();
//
//								// llayMMOption.addView(tabscreenview.Videochatscreen(context,
//								// strIPath));Videochat
//								// llayMMOption.addView(tabscreenview.Videochat(strIPath+".mp4",
//								// context));
//
//								if (strIPath != null) {
//
//									System.out
//											.println("--------onActivityResult-----video-----"
//													+ strIPath + ".mp4");
//									File f = new File(strIPath + ".mp4");
//
//									if (f.exists()) {
//										if (f.length() != 0) {
//											if (mmlay.getChildCount() == 0) {
//												CreateVideoThumbnail(strIPath);
//												mmlay.addView(Videochat(strIPath
//														+ ".mp4"));
//											}
//										} else {
//
//											// video.setEnabled(true);
//										}
//									} else {
//										// video.setEnabled(true);
//									}
//
//								}
//
//							}
//						} else {
//							// video.setEnabled(true);
//						}
//
//					}
//
//					else if (type.equalsIgnoreCase("sketch")) {
//						if (path != null) {
//							File ph_fle = new File(path);
//							if (ph_fle.exists()) {
//
//								importedFile = path;
//								isimported = true;
//								strIPath = path;
//
//								img = null;
//
//								img = callDisp.ResizeImage(strIPath);
//								img = Bitmap.createScaledBitmap(img, 100, 75,
//										false);
//								if (img != null) {
//
//									Log.d("OnActivity",
//											"_____On Activity Called______");
//									final RelativeLayout rlSender = new RelativeLayout(
//											context);
//									rlSender.setLayoutParams(new LayoutParams(
//											LayoutParams.FILL_PARENT,
//											LayoutParams.WRAP_CONTENT));
//									rlSender.setBackgroundResource(R.drawable.corner);
//
//									RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//											RelativeLayout.LayoutParams.WRAP_CONTENT,
//											RelativeLayout.LayoutParams.WRAP_CONTENT);
//									layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//									RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//											RelativeLayout.LayoutParams.WRAP_CONTENT,
//											RelativeLayout.LayoutParams.WRAP_CONTENT);
//									layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//									MyAbsoluteLayout llayOption = new MyAbsoluteLayout(
//											context);
//									llayOption
//											.setLayoutParams(new LayoutParams(
//													LinearLayout.LayoutParams.FILL_PARENT,
//													LinearLayout.LayoutParams.WRAP_CONTENT));
//
//									final LinearLayout llayPhoto = new LinearLayout(
//											context);
//									llayPhoto.setGravity(Gravity.CENTER);
//									final LinearLayout llayPhoto1 = new LinearLayout(
//											context);
//									llayPhoto1.setGravity(Gravity.CENTER);
//
//									rlSender.addView(llayPhoto1, layRight);
//									rlSender.addView(llayPhoto, layLeft);
//
//									LinearLayout llayPOpt = new LinearLayout(
//											context);
//									llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//									final Button btnSendPhoto = new Button(
//											context);
//									new Button(context);
//									final Button btnView = new Button(context);
//									final Button btnExit = new Button(context);
//									btnExit.setText("Exit");
//
//									btnExit.setLayoutParams(new LayoutParams(
//											100, 30));
//									btnView.setText("ViewPhoto");
//									btnView.setLayoutParams(new LayoutParams(
//											100, 30));
//
//									btnSendPhoto.setText("Send");
//									btnSendPhoto.setGravity(Gravity.CENTER);
//									btnSendPhoto
//											.setLayoutParams(new LayoutParams(
//													LayoutParams.WRAP_CONTENT,
//													LayoutParams.WRAP_CONTENT));
//									btnSendPhoto
//											.setOnClickListener(new OnClickListener() {
//
//												@Override
//												public void onClick(View v) {
//													// TODO Auto-generated
//													// method
//													// stub
//													if (buddy != null) {
//
//														BuddyInformationBean bib = WebServiceReferences.buddyList
//																.get(buddy);
//
//														FTPBean bean = new FTPBean();
//														bean.setFtp_username(CallDispatcher.LoginUser);
//														bean.setFtp_password(CallDispatcher.Password);
//														bean.setOperation_type(1);
//														bean.setComment(sessionid);
//														bean.setServer_ip(callDisp
//																.getRouter()
//																.split(":")[0]);
//														bean.setServer_port(40400);
//														bean.setReq_object(llayPhoto);
//														bean.setRequest_from("MPP");
//														bean.setFile_path(strIPath);
//
//														notifyFTPServerConnected(
//																true, bean);
//														if (!isForceLogout
//																&& bib != null) {
//															if (CallDispatcher.LoginUser == null) {
//																showToast("Sorry! can not send This Photo");
//															} else if (!bib
//																	.getStatus()
//																	.equals("Online")) {
//																showToast("The User is not in Online state");
//															}
//														} else
//															showToast("Sorry! can not send This Photo");
//													}
//
//												}
//											});
//
//									llayPOpt.addView(btnSendPhoto);
//									/*
//									 * llayPOpt.addView(btnRetake);
//									 * llayPOpt.addView(btnExit);
//									 */
//									llayPhoto1.addView(llayPOpt);
//									if (mmlay.getChildCount() == 0) {
//										mmlay.addView(rlSender);
//									} else {
//										mmlay.removeAllViews();
//										if (llayTimer != null) {
//											llayTimer.removeAllViews();
//										}
//										mmlay.addView(rlSender);
//									}
//									Button exit = new Button(context);
//									exit.setText("Exit");
//									exit.setGravity(Gravity.CENTER);
//									llayPOpt.addView(exit);
//									exit.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											isimported = false;
//											// photo.setEnabled(true);
//											llayPhoto.removeAllViews();
//											llayPhoto1.removeAllViews();
//											mmlay.removeAllViews();
//
//										}
//									});
//
//									bitmap = null;
//
//									// if (fileCheck.exists()) {
//									bitmap = callDisp.ResizeImage(strIPath);
//									bitmap = Bitmap.createScaledBitmap(bitmap,
//											200, 150, false);
//									// }
//									if (bitmap != null)
//										ivPhoto.setImageBitmap(bitmap);
//									ivPhoto.setPadding(10, 10, 10, 10);
//									if (llayPhoto.getChildCount() == 0) {
//										llayPhoto.addView(ivPhoto);
//									}
//									Log.d("IM",
//											"************8Layout Inserted************");
//
//								} else {
//									strIPath = null;
//									// photo.setEnabled(true);
//									showToast("Kindly choose an image");
//								}
//
//							} else {
//								if (photo != null) {
//									photo.setEnabled(true);
//								}
//							}
//						}
//					} else if (type.equalsIgnoreCase("photo")) {
//						if (path != null) {
//							File ph_fle = new File(path);
//							if (ph_fle.exists()) {
//
//								importedFile = path;
//								isimported = true;
//								strIPath = path;
//
//								img = null;
//
//								img = callDisp.ResizeImage(strIPath);
//								img = Bitmap.createScaledBitmap(img, 100, 75,
//										false);
//								if (img != null) {
//
//									Log.d("OnActivity",
//											"_____On Activity Called______");
//									final RelativeLayout rlSender = new RelativeLayout(
//											context);
//									rlSender.setLayoutParams(new LayoutParams(
//											LayoutParams.FILL_PARENT,
//											LayoutParams.WRAP_CONTENT));
//									rlSender.setBackgroundResource(R.drawable.corner);
//
//									RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//											RelativeLayout.LayoutParams.WRAP_CONTENT,
//											RelativeLayout.LayoutParams.WRAP_CONTENT);
//									layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//									RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//											RelativeLayout.LayoutParams.WRAP_CONTENT,
//											RelativeLayout.LayoutParams.WRAP_CONTENT);
//									layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//									MyAbsoluteLayout llayOption = new MyAbsoluteLayout(
//											context);
//									llayOption
//											.setLayoutParams(new LayoutParams(
//													LinearLayout.LayoutParams.FILL_PARENT,
//													LinearLayout.LayoutParams.WRAP_CONTENT));
//
//									final LinearLayout llayPhoto = new LinearLayout(
//											context);
//									llayPhoto.setGravity(Gravity.CENTER);
//									final LinearLayout llayPhoto1 = new LinearLayout(
//											context);
//									llayPhoto1.setGravity(Gravity.CENTER);
//
//									rlSender.addView(llayPhoto1, layRight);
//									rlSender.addView(llayPhoto, layLeft);
//
//									LinearLayout llayPOpt = new LinearLayout(
//											context);
//									llayPOpt.setOrientation(LinearLayout.HORIZONTAL);
//
//									final Button btnSendPhoto = new Button(
//											context);
//									new Button(context);
//									final Button btnView = new Button(context);
//									final Button btnExit = new Button(context);
//									btnExit.setText("Exit");
//
//									btnExit.setLayoutParams(new LayoutParams(
//											100, 30));
//									btnView.setText("ViewPhoto");
//									btnView.setLayoutParams(new LayoutParams(
//											100, 30));
//
//									btnSendPhoto.setText("Send");
//									btnSendPhoto.setGravity(Gravity.CENTER);
//									btnSendPhoto
//											.setLayoutParams(new LayoutParams(
//													LayoutParams.WRAP_CONTENT,
//													LayoutParams.WRAP_CONTENT));
//									btnSendPhoto
//											.setOnClickListener(new OnClickListener() {
//
//												@Override
//												public void onClick(View v) {
//													// TODO Auto-generated
//													// method
//													// stub
//													if (buddy != null) {
//
//														BuddyInformationBean bib = WebServiceReferences.buddyList
//																.get(buddy);
//
//														FTPBean bean = new FTPBean();
//														bean.setFtp_username(CallDispatcher.LoginUser);
//														bean.setFtp_password(CallDispatcher.Password);
//														bean.setOperation_type(1);
//														bean.setComment(sessionid);
//														bean.setServer_ip(callDisp
//																.getRouter()
//																.split(":")[0]);
//														bean.setReq_object(llayPhoto);
//														bean.setServer_port(40400);
//														bean.setRequest_from("MPP");
//														bean.setFile_path(strIPath);
//
//														notifyFTPServerConnected(
//																true, bean);
//
//														if (!isForceLogout
//																&& bib != null) {
//															if (CallDispatcher.LoginUser == null) {
//																showToast("Sorry! can not send This Photo");
//															} else if (!bib
//																	.getStatus()
//																	.equals("Online")) {
//																showToast("The User is not in Online state");
//															}
//														} else
//															showToast("Sorry! can not send This Photo");
//													}
//
//												}
//											});
//
//									llayPOpt.addView(btnSendPhoto);
//									llayPhoto1.addView(llayPOpt);
//									if (mmlay.getChildCount() == 0) {
//										mmlay.addView(rlSender);
//									} else {
//										mmlay.removeAllViews();
//										if (llayTimer != null) {
//											llayTimer.removeAllViews();
//										}
//										mmlay.addView(rlSender);
//									}
//									Button exit = new Button(context);
//									exit.setText("Exit");
//									exit.setGravity(Gravity.CENTER);
//									llayPOpt.addView(exit);
//									exit.setOnClickListener(new OnClickListener() {
//
//										@Override
//										public void onClick(View v) {
//											// TODO Auto-generated method stub
//											isimported = false;
//											// photo.setEnabled(true);
//											llayPhoto.removeAllViews();
//											llayPhoto1.removeAllViews();
//											mmlay.removeAllViews();
//
//										}
//									});
//
//									bitmap = null;
//
//									// if (fileCheck.exists()) {
//									bitmap = callDisp.ResizeImage(strIPath);
//									bitmap = Bitmap.createScaledBitmap(bitmap,
//											200, 150, false);
//									// }
//									if (bitmap != null)
//										ivPhoto.setImageBitmap(bitmap);
//									ivPhoto.setPadding(10, 10, 10, 10);
//									if (llayPhoto.getChildCount() == 0) {
//										llayPhoto.addView(ivPhoto);
//									}
//									Log.d("IM",
//											"************8Layout Inserted************");
//
//								} else {
//									strIPath = null;
//									// photo.setEnabled(true);
//									showToast("Kindly choose an image");
//								}
//
//							} else {
//								if (photo != null) {
//									// photo.setEnabled(true);
//								}
//							}
//						}
//					}
//
//				}
//			} else if (requestCode == 32) {
//				afterRecoder();
//			} else {
//
//				// if (!photo.isEnabled()) {
//				//
//				// photo.setEnabled(true);
//				// }
//				if (isaudio_imported) {
//
//					isaudio_imported = false;
//					// audio.setEnabled(true);
//					llayStart.removeAllViews();
//					llayTimer.removeAllViews();
//					mmlay.removeAllViews();
//
//					tvtimer.setText("00:00:00");
//				}
//				// if (!video.isEnabled()) {
//				// video.setEnabled(true);
//				// }
//			}
//			isvideoplay = false;
//			isVideoRecord = false;
//			isvideobuddyVideoPlaying = false;
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private RelativeLayout Videochat(final String videopath) {
//		// TODO Auto-generated method stub
//
//		final RelativeLayout rlSender = new RelativeLayout(context);
//
//		rlSender.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		rlSender.setBackgroundResource(R.drawable.corner);
//
//		final RelativeLayout.LayoutParams layLeft = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		layLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//		RelativeLayout.LayoutParams layRight = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.WRAP_CONTENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		layRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//		MyAbsoluteLayout llayOption = new MyAbsoluteLayout(context);
//		llayOption.setLayoutParams(new LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//
//		final LinearLayout llaySoundControl = new LinearLayout(context);
//		llaySoundControl.setLayoutParams(new LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//
//		final LinearLayout llaySendControl1 = new LinearLayout(context);
//		llaySendControl1.setLayoutParams(new LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//
//		final Button btnSendPhoto = new Button(context);
//		btnSendPhoto.setText("Send");
//		btnSendPhoto.setGravity(Gravity.CENTER);
//		btnSendPhoto.setLayoutParams(new LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		// btnSendPhoto.setVisibility(View.INVISIBLE);
//
//		llaySendControl1.addView(btnSendPhoto);
//		llaySendControl1.setGravity(Gravity.CENTER);
//		rlSender.addView(llaySendControl1, layRight);
//
//		// final Button btnPlay = new Button(context);
//		// btnPlay.setText("Play");
//		// btnPlay.setBackgroundResource(R.drawable.v_play1);
//
//		// llaySoundControl.addView(btnPlay);
//		rlSender.addView(llaySoundControl, layLeft);
//		final ImageView imageview = new ImageView(context);
//		imageview.setLayoutParams(new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		final File vfileCheck = new File(videopath);
//		Bitmap bitmapThumb = null;
//		final File fileCheckV = new File(strIPath + ".jpg");
//
//		if (fileCheckV.exists())
//			// bitmapThumb = ResizeImage(component
//			// .getContentThumbnail());
//			bitmapThumb = callDisp.ResizeImage(strIPath + ".jpg");
//
//		Log.e("list", ">>>>>>>>>>>>>>>" + bitmapThumb);
//		Log.e("list", ">>>>>>>>>>>>>>>" + vfileCheck.exists());
//		imageview.setImageBitmap(bitmapThumb);
//		// rlSender.addView(imageview, layLeft);
//		llaySoundControl.addView(imageview);
//
//		final VideoView videoView = new VideoView(context);
//		imageview.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				llaySoundControl.removeAllViews();
//				rlSender.removeView(llaySoundControl);
//				videoView.setLayoutParams(new LinearLayout.LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				videoView.setVideoPath(videopath);
//				rlSender.addView(videoView, layLeft);
//				videoView.setZOrderOnTop(true);
//				videoView.start();
//				isvideoplay = true;
//				videoView
//						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//							@Override
//							public void onCompletion(MediaPlayer mp) {
//								// not playVideo
//								// playVideo();
//								// rlSender.removeView(videoView);
//								// rlSender.addView(btnPlay, layLeft);
//								// llaySoundControl.removeView(videoView);
//								// llaySoundControl.removeAllViews();
//								rlSender.removeView(videoView);
//								// llaySoundControl.addView(btnPlay);
//								llaySoundControl.addView(imageview);
//								rlSender.addView(llaySoundControl, layLeft);
//
//								isvideoplay = false;
//							}
//						});
//
//			}
//		});
//
//		btnSendPhoto.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (isvideoplay) {
//					if (videoView != null) {
//						if (videoView.isPlaying()) {
//							videoView.stopPlayback();
//							isvideoplay = false;
//						}
//					}
//				}
//				if (buddy != null) {
//
//					BuddyInformationBean bib = WebServiceReferences.buddyList
//							.get(buddy);
//
//					FTPBean bean = new FTPBean();
//					bean.setFtp_username(CallDispatcher.LoginUser);
//					bean.setFtp_password(CallDispatcher.Password);
//					bean.setOperation_type(1);
//					bean.setComment(sessionid);
//					bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//
//					bean.setRequest_from("MVP");
//					bean.setFile_path(strIPath);
//
//					notifyFTPServerConnected(true, bean);
//					if (bib != null && !isForceLogout) {
//						if (CallDispatcher.LoginUser == null) {
//							showToast("Sorry! can not send This Photo");
//						} else if (!bib.getStatus().equals("Online")) {
//							showToast("The User is not in Online state");
//						}
//					} else
//						showToast("Sorry! can not send This Photo");
//
//				}
//
//			}
//		});
//		Button exit = new Button(context);
//		exit.setText("Exit");
//		exit.setGravity(Gravity.CENTER);
//		llaySendControl1.addView(exit);
//		exit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (isvideoplay) {
//					if (videoView != null) {
//						if (videoView.isPlaying()) {
//							videoView.stopPlayback();
//							isvideoplay = false;
//						}
//					}
//				}
//				// video.setEnabled(true);
//				llaySendControl1.removeAllViews();
//				llaySoundControl.removeAllViews();
//				mmlay.removeAllViews();
//				File fl = new File(strIPath);
//				if (fl.exists()) {
//					fl.delete();
//				}
//			}
//		});
//
//		rlSender.setGravity(Gravity.CENTER);
//
//		return rlSender;
//	}
//
//	protected void makevideoPaging(String buddy2, String fileName,
//			String signalid) {
//		// TODO Auto-generated method stub
//		System.out.println("----------------btnSendPhoto------------------"
//				+ buddy2);
//		try {
//
//			BuddyInformationBean bib = null;
//			bib = WebServiceReferences.buddyList.get(buddy2);
//			SignalingBean sb = new SignalingBean();
//			sb.setFrom(CallDispatcher.LoginUser);
//			sb.setTo(buddy2);
//			sb.setType("11");
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setResult("0");
//			sb.setTopublicip(bib.getExternalipaddress());
//			sb.setTolocalip(bib.getLocalipaddress());
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setCallType("MVP");
//			sb.setFileId("007");
//			sb.setFtpUser(CallDispatcher.LoginUser);
//			sb.setFtppassword(CallDispatcher.Password);
//			sb.setFilePath(fileName);
//			sb.setSessionid(sessionid);
//			sb.setSignalid(signalid);
//			sb.setisRobo("");
//			if (groupmode) {
//				sb.setConferencemember(getMembers() + CallDispatcher.LoginUser);
//			}
//			System.out.println("----------------btnSendPhoto------------------"
//					+ fileName);
//
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putString("action", "upload");
//			bundle.putSerializable("bean", sb);
//			msg.obj = bundle;
//			// apSendHandler.sendMessage(msg);
//			mmlay.setBackgroundDrawable(null);
//			mmlay.removeAllViews();
//
//			// chattingList.addView(AudioView(sb.getSessionid(),
//			// CallDispatcher.LoginUser, context, 3,
//			// "/sdcard/COMMedia/"+audiopath));
//
//			Audioupload(msg);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private void makePhotoPaging(String selectedBuddy, String fileName,
//			String signalid) {
//		try {
//			System.out
//					.println("----------------btnSendPhoto------------------");
//
//			BuddyInformationBean bib = null;
//			bib = WebServiceReferences.buddyList.get(selectedBuddy);
//			SignalingBean sb = new SignalingBean();
//			sb.setFrom(CallDispatcher.LoginUser);
//			sb.setTo(selectedBuddy);
//			sb.setType("11");
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setResult("0");
//			sb.setTopublicip(bib.getExternalipaddress());
//			sb.setTolocalip(bib.getLocalipaddress());
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setCallType("MPP");
//			sb.setFileId("007");
//			sb.setFilePath(fileName);
//			sb.setSessionid(sessionid);
//			sb.setSignalid(signalid);
//			sb.setFtpUser(CallDispatcher.LoginUser);
//			sb.setFtppassword(CallDispatcher.Password);
//			sb.setisRobo("");
//			if (groupmode) {
//				sb.setConferencemember(getMembers() + CallDispatcher.LoginUser);
//			}
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putString("action", "upload");
//			bundle.putSerializable("bean", sb);
//			msg.obj = bundle;
//			apSendHandler.sendMessage(msg);
//			Audioupload(msg);
//			System.out.println("----------------btnSendPhoto------------------"
//					+ sessionid);
//			// CallDispatcher.commEngine.makeCall(sb);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void makeFPPaging(String selectedBuddy, String fileName,
//			String signalid) {
//		try {
//			System.out
//					.println("----------------btnSendPhoto------------------");
//
//			BuddyInformationBean bib = null;
//			bib = WebServiceReferences.buddyList.get(selectedBuddy);
//			SignalingBean sb = new SignalingBean();
//			sb.setFrom(CallDispatcher.LoginUser);
//			sb.setTo(selectedBuddy);
//			sb.setType("11");
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setResult("0");
//			sb.setTopublicip(bib.getExternalipaddress());
//			sb.setTolocalip(bib.getLocalipaddress());
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setCallType("MHP");
//			sb.setFileId("007");
//			sb.setFilePath(fileName);
//			sb.setSessionid(sessionid);
//			sb.setSignalid(signalid);
//			sb.setFtpUser(CallDispatcher.LoginUser);
//			sb.setFtppassword(CallDispatcher.Password);
//			sb.setisRobo("");
//			if (groupmode) {
//				sb.setConferencemember(getMembers() + CallDispatcher.LoginUser);
//			}
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putString("action", "upload");
//			bundle.putSerializable("bean", sb);
//			msg.obj = bundle;
//			apSendHandler.sendMessage(msg);
//			Audioupload(msg);
//			System.out.println("----------------btnSendPhoto------------------"
//					+ sessionid);
//			// CallDispatcher.commEngine.makeCall(sb);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void MakeAudioPaging(String selectedBuddy, String fileName,
//			String signalid) {
//		try {
//			System.out
//					.println("----------------btnSendPhoto------------------");
//
//			BuddyInformationBean bib = null;
//			bib = WebServiceReferences.buddyList.get(selectedBuddy);
//			SignalingBean sb = new SignalingBean();
//			sb.setFrom(CallDispatcher.LoginUser);
//			sb.setTo(selectedBuddy);
//			sb.setType("11");
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setResult("0");
//			sb.setTopublicip(bib.getExternalipaddress());
//			sb.setTolocalip(bib.getLocalipaddress());
//			sb.setToSignalPort(bib.getSignalingPort());
//			sb.setCallType("MAP");
//			sb.setFileId("007");
//			sb.setFilePath(fileName);
//			sb.setSessionid(sessionid);
//			sb.setSignalid(signalid);
//			sb.setFtpUser(CallDispatcher.LoginUser);
//			sb.setFtppassword(CallDispatcher.Password);
//			sb.setisRobo("");
//			if (groupmode) {
//				sb.setConferencemember(getMembers() + CallDispatcher.LoginUser);
//			}
//			Message msg = new Message();
//			Bundle bundle = new Bundle();
//			bundle.putString("action", "upload");
//			bundle.putSerializable("bean", sb);
//			msg.obj = bundle;
//			apSendHandler.sendMessage(msg);
//			Audioupload(msg);
//			System.out.println("----------------btnSendPhoto------------------"
//					+ sessionid);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	public void Audioupload(Message msg) {
//		// TODO Auto-generated method stub
//		try {
//			Bundle bun = (Bundle) msg.obj;
//
//			final SignalingBean sb = (SignalingBean) bun
//					.getSerializable("bean");
//			sb.getFrom();
//			sb.getTo();
//
//			Log.d("Dispatcher", "@@@@@@@@@@@@@" + callDisp);
//
//			Log.d("MPP", "Audio upload --: "
//					+ callDisp.getRouter().split(":")[0] + "----full----"
//					+ callDisp.getRouter());
//			String filename = sb.getFilePath();
//
//			Log.e("MAP", "File Name:" + filename);
//			System.out.println("----------------upload--------------");
//			if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//
//				MMBean obj = WebServiceReferences.hsMPPBean.get(filename);
//				System.out.println("----------------upload--------------");
//
//				FTPBean bean = new FTPBean();
//				bean.setFtp_username(CallDispatcher.LoginUser);
//				bean.setFtp_password(CallDispatcher.Password);
//				bean.setOperation_type(1);
//				bean.setServer_port(40400);
//				bean.setComment(sessionid);
//				bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//				bean.setReq_object(sb);
//				bean.setRequest_from("MPP");
//				bean.setFile_path(obj.getFilePath());
//				callDisp.getFtpNotifier().addTasktoExecutor(bean);
//
//			} else {
//				System.out
//						.println("----------------elseeeeeeeeeeeeeeeeee--------------");
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private RelativeLayout AudioChatFromME(final String path, String time,
//			String status, String to, int viewmode, final int flag, int btnid) {
//		try {
//			Log.d("IM", "Image from buddy called");
//			Log.d("thread",
//					"************************************ came to audi chat from me"
//							+ path);
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.MATCH_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			final LinearLayout callControls1 = (LinearLayout) inflateLayout1
//					.inflate(R.layout.im_chat_rec, null);
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(CallDispatcher.LoginUser));
//			// ProgressBar prog = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//
//			if (!AppReference.issipchatinitiated) {
//				if (status.equals("upload")) {
//					Log.i("IMMSG", "Uploading-----?");
//					// prog.setVisibility(View.VISIBLE);
//					// WebServiceReferences.hsIMProgress.put(path, prog);
//				} else {
//					Log.i("IMMSG", "Not Uploading-----?");
//				}
//			}
//
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setVisibility(View.VISIBLE);
//			// ImageView iv_resend = (ImageView) callControls1
//			// .findViewById(R.id.imageView1);
//			// iv_resend.setTag(btnid);
//			// save.setTag(btnid);
//
//			/* iv_resend.setVisibility(View.GONE); */
//			// if (flag == 0) {
//			// iv_resend.setBackgroundResource(R.drawable.warning);
//			// iv_resend.setVisibility(View.VISIBLE);
//			// } else {
//			// iv_resend.setVisibility(View.VISIBLE);
//			// iv_resend.setBackgroundResource(R.drawable.chatsent);
//			// }
//			// iv_resend.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (flag == 0) {
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			//
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			// ImageView btn = (ImageView) rlay
//			// .getChildAt(0);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//
//			// if (strSignalId.length() != 0)
//			// WebServiceReferences.hsIMProgress.put(strSignalId,
//			// pbSendProgress);
//
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//
//			// llayContent.setGravity(Gravity.CENTER);
//			// TextView tvMessage = new TextView(context);
//			//
//			// tvMessage.setTextColor(Color.BLACK);
//			// tvMessage.setText("");
//			// llayContent.removeView(tvMessage);
//			//
//			// TextView tvMsg = new TextView(context);
//			// tvMsg.setTextColor(Color.BLACK);
//			// tvMsg.setText("Audio");
//
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvMsg, paramspa);
//			TextView audioMsg = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#ff4d4d>"
//					+ CallDispatcher.LoginUser + " : " + "</font>";
//			audioMsg.setText(Html.fromHtml(Chatmessage));
//			final ImageView play = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			play.setId(100);
//			play.setTag("0");
//			play.setImageBitmap(playBitmap);
//
//			final MediaPlayer chatplayer = new MediaPlayer();
//			chatplayer.setLooping(false);
//			play.setVisibility(View.INVISIBLE);
//
//			// TextView tvTime = (TextView) callControls1
//			// .findViewById(R.id.txt_chat_time);
//			// tvTime.setText(time);
//
//			// if (viewmode != 5) {
//			// Log.i("IMMSG","View Mode-----?"+viewmode);
//			//
//			// prog.setVisibility(View.INVISIBLE);
//			// } else {
//			// Log.i("IMMSG","View Mode-----?"+viewmode);
//			//
//			// prog.setVisibility(View.VISIBLE);
//			// }
//			final File fileCheck = new File(path);
//			Log.d("MM", "######################" + path);
//			if (fileCheck.exists()) {
//				Log.d("MM", "######################" + path);
//
//				try {
//					chatplayer.setDataSource(path);
//					if (viewmode != 5) {
//						chatplayer.prepare();
//					}
//
//					play.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							if (!chatplayer.isPlaying()) {
//								if (!isplaying) {
//
//									if (fileCheck.exists()) {
//
//										if (play.getTag().toString()
//												.equals("0")) {
//											play.setTag("1");
//											play.setImageBitmap(pauseBitmap);
//
//											try {
//												current_audio = play;
//												current_aplayer = chatplayer;
//												isplaying = true;
//												isAudioRecording1 = true;
//												chatplayer.start();
//											} catch (IllegalArgumentException e) {
//												if (AppReference.isWriteInFile)
//													AppReference.logger.error(
//															e.getMessage(), e);
//												// TODO Auto-generated catch
//												// block
//												e.printStackTrace();
//											} catch (IllegalStateException e) {
//												if (AppReference.isWriteInFile)
//													AppReference.logger.error(
//															e.getMessage(), e);
//												// TODO Auto-generated catch
//												// block
//												e.printStackTrace();
//											}
//										} else if (play.getTag().toString()
//												.equals("1")) {
//
//											play.setTag("0");
//											play.setImageBitmap(playBitmap);
//
//											current_audio = play;
//											current_aplayer = chatplayer;
//											isplaying = false;
//											chatplayer.pause();
//
//										}
//
//									} else {
//										ShowError("Player Error",
//												"Can not play this Audio");
//									}
//								}
//
//								else {
//									showToast("Kindly Stop the playing audio first");
//								}
//							} else {
//								if (play.getTag().toString().equals("1")) {
//									play.setTag("0");
//									play.setImageBitmap(playBitmap);
//									current_audio = play;
//									current_aplayer = chatplayer;
//									isplaying = false;
//									chatplayer.pause();
//								}
//							}
//						}
//					});
//					chatplayer
//							.setOnCompletionListener(new OnCompletionListener() {
//
//								@Override
//								public void onCompletion(MediaPlayer mp) {
//									// TODO Auto-generated method stub
//									isAudioRecording1 = false;
//									isplaying = false;
//									play.setImageBitmap(playBitmap);
//
//									play.setTag("0");
//									chatplayer.reset();
//									try {
//										chatplayer.setDataSource(path);
//										chatplayer.setLooping(false);
//										chatplayer.prepare();
//									} catch (IllegalArgumentException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (IllegalStateException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (IOException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//
//								}
//							});
//					play.setVisibility(View.VISIBLE);
//
//					// AudioChatBean bean = new AudioChatBean();
//					// bean.setBuddyName(sigb.getFrom());
//					// bean.setChatTime(time());
//					// bean.setFilePath(path);
//					// bean.setType(2);
//					// bean.setUserName(CallDispatcher.LoginUser);
//					// appendChatDatatoXML(bean);
//				} catch (IllegalArgumentException e) {
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalStateException e) {
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					// TODO Auto-generated catch block
//					if (viewmode == 5) {
//						play.setVisibility(View.VISIBLE);
//					}
//					e.printStackTrace();
//				} catch (IOException e) {
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//			//
//			// final ImageView imgView = new ImageView(context);
//			// imgView.setImageResource(R.drawable.warning);
//			// if (status == "") {
//			// imgView.setImageResource(R.drawable.chatsent);
//			// prog.setVisibility(View.INVISIBLE);
//			// }
//			if (!AppReference.issipchatinitiated) {
//				if (status.equals("upload")) {
//					Log.i("imscreen", "Uploading-----?");
//					// // prog.setVisibility(View.VISIBLE);
//					// WebServiceReferences.hsIMImageView.put(path, imgView);
//				} else {
//					Log.i("imscreen", "Not Uploading-----?");
//
//				}
//
//			}
//
//			// if (status.equals("quickchat")) {
//			// imgView.setImageResource(R.drawable.chatsent);
//			// prog.setVisibility(View.INVISIBLE);
//			// }
//			// imgView.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (imgView.getContentDescription() != null
//			// && imgView.getContentDescription().equals("false")) {
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			// ImageView btn = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method
//			// // stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.setMargins(0, 0, 5, 5);
//			// params.gravity = Gravity.RIGHT;
//			// llayContent.addView(imgView, params);
//
//			// LinearLayout llaySave = new LinearLayout(context);
//			// llaySave.setId(100);
//			// // llaySave.setBackgroundColor(Color.WHITE);
//			//
//			// LinearLayout.LayoutParams lprms = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// // lprms.gravity = Gravity.CENTER_VERTICAL;
//			// // lprms.setMargins(10, 30, 10, 10);
//			//
//			// llaySave.setLayoutParams(lprms);
//			//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof AudioChatBean) {
//			// AudioChatBean bean = (AudioChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "audio", path, "Audio");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "audio", path, "Audio");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "audio", path, "Audio");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "audio", path, "Audio");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (path != null) { if(fileCheck.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"+path+"'"))
//			// * { saveAsNote(CallDispatcher.LoginUser, "audio", path,
//			// * "Audio"); } else {
//			// * showToast("This Conversation already Saved!"); } } else {
//			// * showToast("unable to create file"); } Log.e("save",
//			// * "IM from my Buddy Photo note"); } else {
//			// * showToast("unable to create file"); }
//			// */
//			//
//			// }
//			// });
//			//
//			// llaySave.addView(btnSave);
//			//
//			// RelativeLayout.LayoutParams parmsSave = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// parmsSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			// rlParent.addView(llaySave, parmsSave);
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.LEFT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//
//			return rlParent;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private RelativeLayout VideoChatFromME(final String filepath, String time,
//			String status, String to, int viewmode, final int flag, int btn_id) {
//
//		try {
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			final LinearLayout callControls1 = (LinearLayout) inflateLayout1
//					.inflate(R.layout.im_chat_rec, null);
//
//			// if (strSignalId.length() != 0)
//			// WebServiceReferences.hsIMProgress.put(strSignalId,
//			// pbSendProgress);
//
//			// ProgressBar prog = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(CallDispatcher.LoginUser));
//			// if (status.equals("upload")) {
//			// prog.setVisibility(View.VISIBLE);
//			// WebServiceReferences.hsIMProgress.put(filepath, prog);
//			// }
//
//			final String path = filepath;
//			final LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//
//			final File fileCheck = new File(path);
//
//			// llayContent.setGravity(Gravity.CENTER);
//
//			// TextView tvMsg = new TextView(context);
//			// tvMsg.setTextColor(Color.BLACK);
//			// tvMsg.setText("Video");
//			//
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvMsg, paramspa);
//			TextView videoMsg = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#ff4d4d>"
//					+ CallDispatcher.LoginUser + " : " + "</font>";
//			videoMsg.setText(Html.fromHtml(Chatmessage));
//			final ImageView btnPlay = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			// play.setText("Play");
//			btnPlay.setTag("0");
//			btnPlay.setImageBitmap(playBitmap);
//			btnPlay.setVisibility(View.VISIBLE);
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (path != null) {
//			// saveAsNote(CallDispatcher.LoginUser, "video",
//			// path.substring(0, path.lastIndexOf('.')), "Video");
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//
//			// RelativeLayout.LayoutParams params = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// params.addRule(RelativeLayout.RIGHT_OF, btnPlay.getId());
//			//
//			// llayContent.addView(btnSave, params);
//
//			// TextView tvTime = (TextView) callControls1
//			// .findViewById(R.id.txt_chat_time);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.txt_chat_bname);
//			// tvBuddy.setText(to + " (me)");
//
//			// ProgressBar bar = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//			// if (viewmode != 5) {
//			// bar.setVisibility(View.INVISIBLE);
//			// } else {
//			// bar.setVisibility(View.VISIBLE);
//			// }
//			// chattingList.addView(callControls1);
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(btn_id);
//			// ImageView iv_resend = (ImageView) callControls1
//			// .findViewById(R.id.imageView1);
//			// iv_resend.setTag(btn_id);
//			/* iv_resend.setVisibility(View.GONE); */
//			// if (flag == 0) {
//			// iv_resend.setBackgroundResource(R.drawable.warning);
//			// iv_resend.setVisibility(View.VISIBLE);
//			// } else {
//			// iv_resend.setVisibility(View.VISIBLE);
//			// iv_resend.setBackgroundResource(R.drawable.chatsent);
//			// }
//			// iv_resend.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (flag == 0) {
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			//
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			// ImageView btn = (ImageView) rlay
//			// .getChildAt(0);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//
//			Log.d("MM", "######################" + path);
//
//			Log.d("MM", "######################" + path);
//
//			btnPlay.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Log.d("MM", "######################" + path);
//					isvideobuddyVideoPlaying = true;
//					Intent intentVPlayer = new Intent(context,
//							VideoPlayer.class);
//					String filePath = path;
//					if (!filePath.endsWith(".mp4"))
//						filePath = path + ".mp4";
//					intentVPlayer.putExtra("File_Path", filePath);
//					intentVPlayer.putExtra("Player_Type", "Video Player");
//					startActivity(intentVPlayer);
//				}
//			});
//			videoHandler2 = new Handler() {
//
//				@Override
//				public void handleMessage(Message msg) {
//					// TODO Auto-generated method stub
//
//					super.handleMessage(msg);
//				}
//
//			};
//
//			// VideoChatBean bean = new VideoChatBean();
//			// bean.setBuddyName(sigb.getFrom());
//			// bean.setChatTime(time());
//			// bean.setFilePath(path);
//			// bean.setType(2);
//			// bean.setUserName(CallDispatcher.LoginUser);
//			// appendChatDatatoXML(bean);
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//			//
//			// final ImageView imgView = new ImageView(context);
//			//
//			// imgView.setImageResource(R.drawable.warning);
//
//			// if (status == "") {
//			// imgView.setImageResource(R.drawable.chatsent);
//			// prog.setVisibility(View.INVISIBLE);
//			// }
//			if (!AppReference.issipchatinitiated) {
//				if (status.equals("upload")) {
//					Log.i("imscreen", "Uploading-----?");
//					// prog.setVisibility(View.VISIBLE);
//					// WebServiceReferences.hsIMImageView.put(path, imgView);
//				} else {
//					Log.i("imscreen", "Not Uploading-----?");
//
//				}
//
//			}
//
//			// imgView.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (imgView.getContentDescription() != null
//			// && imgView.getContentDescription().equals("false")) {
//			//
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			// ImageView btn = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method
//			// // stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.setMargins(0, 0, 5, 5);
//			// params.gravity = Gravity.RIGHT;
//			// llayContent.addView(imgView, params);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof VideoChatBean) {
//			// VideoChatBean bean = (VideoChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getoldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// } else {
//			// if (newpath.contains(".mp4")) {
//			// newpath = newpath.substring(0,
//			// newpath.length() - 4);
//			// }
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// {
//			// if (oldpath.contains(".mp4")) {
//			// oldpath = oldpath.substring(0,
//			// oldpath.length() - 4);
//			// }
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + path);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "video",
//			// path.substring(0,
//			// path.lastIndexOf('.')),
//			// "Video");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "video",
//			// path.substring(0, path
//			// .lastIndexOf('.')),
//			// "Video");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + strIPath);
//			// if (bean.getFilePath().contains(".mp4")) {
//			// bean.setFilePath(bean.getFilePath().substring(
//			// 0, bean.getFilePath().length() - 4));
//			// }
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "video",
//			// path.substring(0,
//			// path.lastIndexOf('.')),
//			// "Video");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "video",
//			// path.substring(0,
//			// path.lastIndexOf('.')),
//			// "Video");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (path != null) {
//			// *
//			// * if(fileCheck.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists
//			// * ("select * from component where ContentPath='"
//			// * +path.substring(0, path.lastIndexOf('.'))+"'")) {
//			// * saveAsNote(CallDispatcher.LoginUser, "video",
//			// * path.substring(0, path.lastIndexOf('.')), "Video"); }
//			// * else { showToast("This Conversation already Saved!"); } }
//			// * else { showToast("unable to create file"); }
//			// *
//			// * Log.e("save", "IM from my Buddy Photo note"); } else {
//			// * showToast("unable to create file"); }
//			// */
//			// }
//			// });
//
//			// llaySave.addView(btnSave);
//			//
//			// RelativeLayout.LayoutParams parmsSave = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// parmsSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			// rlParent.addView(llaySave, parmsSave);
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.LEFT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//
//			return rlParent;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//	/**
//	 * Update the chat member details into the database
//	 * 
//	 * @return
//	 */
//	private boolean updateUsersInTable() {
//
//		try {
//			updateHandler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					IMTabScreen imScreen = (IMTabScreen) WebServiceReferences.contextTable
//							.get("imtabs");
//					String comembers = imScreen.getArrangedMembers(confmembers);
//					String update = "update component set comment='"
//							+ comembers + "' where PropertyId='" + sessionid
//							+ "'";
//					callDisp.getdbHeler(context).ExecuteQuery(update);
//					// String
//					// query="update component set Content='"+comembers+"' where PropertyId='"+sessionid+"'";
//					//
//					Log.d("sdialog", "ne " + update);
//					// return bool;
//				}
//			});
//			return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return false;
//		}
//
//	}
//
//	public RelativeLayout imageChatFromME(final String path, final String time,
//			String status, String buddyName, int viewmode, final int flag,
//			int btnid) {
//
//		try {
//			// final String filepath, String time,
//			// String to
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			Log.d("IM", "Image from buddy called");
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			final LinearLayout callControls1 = (LinearLayout) inflateLayout1
//					.inflate(R.layout.im_chat_rec, null);
//
//			// ProgressBar prog = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//
//			if (!AppReference.issipchatinitiated) {
//				if (status.equals("upload")) {
//					// prog.setVisibility(View.VISIBLE);
//					// WebServiceReferences.hsIMProgress.put(path, prog);
//				}
//			}
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//
//			// icon.setImageBitmap(setUserIcon(CallDispatcher.LoginUser));
//			// ImageView iv_resend = (ImageView) callControls1
//			// .findViewById(R.id.imageView1);
//			// iv_resend.setTag(btnid);
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(btnid);
//
//			/* iv_resend.setVisibility(View.GONE); */
//			// if (flag == 0) {
//			// iv_resend.setBackgroundResource(R.drawable.warning);
//			// iv_resend.setVisibility(View.VISIBLE);
//			// } else {
//			// iv_resend.setBackgroundResource(R.drawable.chatsent);
//			// iv_resend.setVisibility(View.VISIBLE);
//			// }
//			//
//			// iv_resend.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (flag == 0) {
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			//
//			// LinearLayout rlay = (LinearLayout) v
//			// .getParent();
//			// LinearLayout rparent = (LinearLayout) rlay
//			// .getParent();
//			// LinearLayout llay = (LinearLayout) rparent
//			// .getChildAt(0);
//			// ImageView btn = (ImageView) rlay
//			// .getChildAt(0);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//
//			// Log.d("up_prog", " ********* Signal ID :" + strSignalId);
//
//			// if (sigb.getCallType().equals("MPP")) {
//
//			// final String path = "/sdcard/COMMedia/" + sigb.getFilePath();
//			Bitmap bitmap = null;
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//
//			// llayContent.setGravity(Gravity.CENTER);
//
//			// frm_photo = new FrameLayout(context);
//			// frm_photo.setLayoutParams(new LayoutParams(100, 100));
//			//
//			// android.widget.FrameLayout.LayoutParams paramsPrg = new
//			// FrameLayout.LayoutParams(
//			// 90, 90);
//			// paramsPrg.gravity = Gravity.CENTER;
//			// paramsPrg.setMargins(5, 5, 5, 5);
//			// ivPhoto1 = new ImageView(context);
//			//
//			// ivPhoto1.setLayoutParams(paramsPrg);
//			//
//			// frm_photo.addView(ivPhoto1);
//			//
//			// frm_photo.setId(100);
//			//
//			// llayContent.addView(frm_photo);
//			ImageView imgView = (ImageView) llayContent
//					.findViewById(R.id.multimedia_img);
//			imgView.setVisibility(View.VISIBLE);
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (path != null) {
//			// saveAsNote(CallDispatcher.LoginUser, "photo", path, "Photo");
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//
//			// RelativeLayout.LayoutParams paramsp = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramsp.addRule(RelativeLayout.RIGHT_OF, frm_photo.getId());
//			//
//			// llayContent.addView(btnSave, paramsp);
//
//			TextView message = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#ff4d4d>"
//					+ CallDispatcher.LoginUser + " : " + "</font>";
//			message.setText(Html.fromHtml(Chatmessage));
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.txt_chat_bname);
//			// tvBuddy.setText(CallDispatcher.LoginUser + "(me)");
//
//			// ProgressBar bar = (ProgressBar) callControls1
//			// .findViewById(R.id.send_progress1);
//			// if (viewmode != 5) {
//			// bar.setVisibility(View.INVISIBLE);
//			// } else {
//			// bar.setVisibility(View.VISIBLE);
//			// }
//
//			// chattingList.addView(callControls1);
//			final File fileCheck = new File(path);
//			Log.d("MM", "######################" + path);
//
//			if (fileCheck.exists()) {
//				Log.d("MM", "###################### file exists" + path);
//				bitmap = callDisp.ResizeImage(path);
//				bitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
//				bitmaplist.add(bitmap);
//			}
//
//			imgView.setBackgroundDrawable(null);
//			if (bitmap != null) {
//				Log.d("MM", "###################### bmp not null..." + path);
//				imgView.setImageBitmap(bitmap);
//			} else {
//				imgView.setBackgroundResource(R.drawable.broken);
//			}
//			imgView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (fileCheck.exists()) {
//						try {
//							Intent in = new Intent(context,
//									PhotoZoomActivity.class);
//							in.putExtra("Photo_path", path);
//							in.putExtra("type", "true");
//							in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(in);
//						} catch (WindowManager.BadTokenException e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							Log.e("Log", "Bad Tocken:" + e.toString());
//						} catch (Exception e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							// TODO: handle exception
//						}
//					} else {
//						showToast("File is not available!");
//					}
//				}
//			});
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//			//
//			// final ImageView imgView = new ImageView(context);
//			//
//			// imgView.setImageResource(R.drawable.warning);
//			// if (status == "") {
//			// imgView.setImageResource(R.drawable.chatsent);
//			// prog.setVisibility(View.INVISIBLE);
//			// }
//
//			Log.i("imscreen", "===> hs path" + path + "id " + btnid);
//			if (!AppReference.issipchatinitiated) {
//				if (status.equals("upload")) {
//					Log.i("imscreen", "Uploading-----?");
//					// prog.setVisibility(View.VISIBLE);
//					// WebServiceReferences.hsIMImageView.put(path, imgView);
//				} else {
//					Log.i("imscreen", "Not Uploading-----?");
//
//				}
//
//			}
//
//			// imgView.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(final View v) {
//			// // TODO Auto-generated method stub
//			// if (flag == 0) {
//			// AlertDialog.Builder buider = new AlertDialog.Builder(
//			// context);
//			// buider.setMessage(
//			// "Are you sure, do you want to resend this conversation?")
//			// .setPositiveButton("Yes",
//			// new DialogInterface.OnClickListener() {
//			//
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			//
//			// ImageView btn = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// int id = Integer.parseInt(btn
//			// .getTag().toString());
//			// resend(childMap.get(id), id);
//			//
//			// }
//			// })
//			// .setNegativeButton("No",
//			// new DialogInterface.OnClickListener() {
//			// @Override
//			// public void onClick(
//			// DialogInterface dialog,
//			// int which) {
//			// // TODO Auto-generated method
//			// // stub
//			// dialog.cancel();
//			// }
//			// });
//			// AlertDialog alert = buider.create();
//			// alert.show();
//			// }
//			// }
//			// });
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.setMargins(0, 0, 5, 5);
//			// params.gravity = Gravity.RIGHT;
//			// llayContent.addView(imgView, params);
//			//
//			// Log.d("MIM",
//			// "@@@@@@@@@@@ save button id" + chattingList.getChildCount());
//
//			// save.setOnClickListener(new OnClickListener() {
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof ImageChatBean) {
//			// ImageChatBean bean = (ImageChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldfileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "photo", path, "Photo");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// CallDispatcher.LoginUser,
//			// "photo", path, "Photo");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "photo", path, "Photo");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (path != null) {
//			// bean.setFilePath(path);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(CallDispatcher.LoginUser,
//			// "photo", path, "Photo");
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (path != null) {
//			// *
//			// * File fl=new File(path); if(fl.exists()) {
//			// *
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"+path+"'"))
//			// * { saveAsNote(CallDispatcher.LoginUser, "photo", path,
//			// * "Photo"); } else {
//			// * showToast("This Conversation already Saved!"); }
//			// *
//			// *
//			// * Log.e("save", "IM from my Buddy Photo note"); } else { //
//			// * unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); } } else {
//			// * // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// */
//			//
//			// }
//			// });
//
//			// llaySave.addView(btnSave);
//			//
//			// RelativeLayout.LayoutParams parmsSave = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// parmsSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			// rlParent.addView(llaySave, parmsSave);
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.LEFT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//
//			return rlParent;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public void imageChatFromMYBuddy(final String buddyName, final String time,
//			String strSignalId, final SignalingBean sigb) {
//
//		try {
//			// TODO Auto-generated method stub
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//			// String strPath = filepath;
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(chattingList.getChildCount());
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			final String filepath = "/sdcard/COMMedia/" + sigb.getFilePath();
//			llayContent.setGravity(Gravity.CENTER);
//			Bitmap bitmap = null;
//
//			final File fileCheck = new File(filepath);
//			if (fileCheck.exists()) {
//				bitmap = callDisp.ResizeImage(filepath);
//				bitmap = Bitmap.createScaledBitmap(bitmap, 100, 75, false);
//				bitmaplist.add(bitmap);
//			}
//			ImageView ivPhoto = (ImageView) llayContent
//					.findViewById(R.id.multimedia_img);
//			ivPhoto.setVisibility(View.VISIBLE);
//			if (bitmap != null) {
//				ivPhoto.setImageBitmap(bitmap);
//			} else {
//				ivPhoto.setBackgroundResource(R.drawable.broken);
//			}
//
//			ivPhoto.setPadding(10, 10, 10, 10);
//			// ivPhoto.setBackgroundColor(R.drawable.corner);
//			ivPhoto.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (fileCheck.exists()) {
//						try {
//							Intent in = new Intent(context,
//									PhotoZoomActivity.class);
//							in.putExtra("Photo_path", filepath);
//							in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(in);
//						} catch (WindowManager.BadTokenException e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							Log.e("Log", "Bad Tocken:" + e.toString());
//						} catch (Exception e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							// TODO: handle exception
//						}
//					} else {
//						showToast("File is not available!");
//					}
//
//				}
//			});
//
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.gravity = Gravity.CENTER;
//			//
//			// ivPhoto.setLayoutParams(params);
//			//
//			// llayContent.addView(ivPhoto);
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (filepath != null) {
//			// saveAsNote(buddyName, "photo", filepath, "Note from "
//			// + buddyName);
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//			//
//			// llayContent.addView(btnSave);
//
//			// TextView tvTime = (TextView)
//			// callControls1.findViewById(R.id.textView2);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.textView1);
//			// tvBuddy.setText(buddyName);
//			TextView imgMsg = (TextView) llayContent.findViewById(R.id.message);
//			TextView textMessage = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#4dffa6>" + buddyName + " : "
//					+ "</font>";
//			textMessage.setText(Html.fromHtml(Chatmessage));
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//
//			// llaySave.setBackgroundColor(Color.WHITE);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof ImageChatBean) {
//			// ImageChatBean bean = (ImageChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldfileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "photo", filepath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "photo",
//			// filepath, "Note from "
//			// + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "photo", filepath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "photo", filepath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (filepath != null) {
//			// *
//			// * File fl=new File(filepath); if(fl.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"
//			// * +filepath+"'")) { saveAsNote(buddyName, "photo",
//			// * filepath, "Note from " + buddyName); } else {
//			// * showToast("This Conversation already Saved!"); }
//			// * Log.e("save", "IM from my Buddy Photo note"); } else { //
//			// * unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// *
//			// * } else { // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// */
//			// }
//			// });
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF,
//					chattingList.getChildCount());
//			rlParent.addView(callControls1, parmsContent);
//
//			if (bitmap != null) {
//				chattingList.addView(rlParent);
//				scrollDown();
//				WebServiceReferences.buddyList.get(sigb.getFrom());
//
//				ImageChatBean bean = new ImageChatBean();
//				bean.setBuddyName(sigb.getFrom());
//				bean.setChatTime(time());
//				bean.setFilePath(filepath);
//				bean.setType(2);
//				bean.setFlag(1);
//				bean.setSignalId(sigb.getSignalid());
//				bean.setUserName(CallDispatcher.LoginUser);
//				appendChatDatatoXML(bean);
//				Log.d("MIM", "@@@@@@@@@@@ hashmap size..." + childMap.size());
//				bean.setId(childMap.size());
//				childMap.add(childMap.size(), bean);
//				String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//						+ "values('"
//						+ sigb.getSessionid()
//						+ "','"
//						+ filepath
//						+ "','"
//						+ sigb.getSignalid()
//						+ "','"
//						+ sigb.getFilePath() + "')";
//				if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//					Log.i("ary", "entry created...");
//				}
//
//			} else {
//				Log.e("IM", "Image data is not available !");
//			}
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}
//
//	}
//
//	private void scrollDown() {
//		try {
//			sview.post(new Runnable() {
//
//				@Override
//				public void run() {
//					sview.fullScroll(ScrollView.FOCUS_DOWN);
//				}
//			});
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void AudioChatFromBuddy(final String buddyName, final String time,
//			final String strSignalId, final SignalingBean sigb) {
//
//		try {
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//			/**
//			 * For new Implementation as on 17-10-14 10.10AM
//			 */
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(chattingList.getChildCount());
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			/**
//			 * Ends
//			 */
//			final String strPath = Environment.getExternalStorageDirectory()
//					.getPath() + "/COMMedia/" + sigb.getFilePath();
//
//			llayContent.setGravity(Gravity.LEFT);
//			final File fileCheck = new File(strPath);
//			TextView textMessage = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#4dffa6>" + buddyName + " : "
//					+ "</font>";
//			textMessage.setText(Html.fromHtml(Chatmessage));
//			final ImageView play = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			play.setId(100);
//			play.setTag("0");
//			play.setImageBitmap(playBitmap);
//			play.setVisibility(View.VISIBLE);
//
//			final MediaPlayer chatplayer = new MediaPlayer();
//			try {
//
//				chatplayer.setDataSource(strPath);
//				chatplayer.setLooping(false);
//				chatplayer.prepare();
//
//			} catch (IllegalArgumentException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalStateException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			play.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (!chatplayer.isPlaying()) {
//						if (!isplaying) {
//
//							if (fileCheck.exists()) {
//
//								if (play.getTag().toString().equals("0")) {
//									play.setTag("1");
//									play.setImageBitmap(pauseBitmap);
//
//									try {
//										current_audio = play;
//										current_aplayer = chatplayer;
//										isplaying = true;
//										isAudioRecording1 = true;
//										chatplayer.start();
//									} catch (IllegalArgumentException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (IllegalStateException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//								} else if (play.getTag().toString().equals("1")) {
//
//									play.setTag("0");
//									play.setImageBitmap(playBitmap);
//
//									current_audio = play;
//									current_aplayer = chatplayer;
//									isplaying = false;
//									chatplayer.pause();
//
//								}
//
//							} else {
//								ShowError("Player Error",
//										"Can not play this Audio");
//							}
//						}
//
//						else {
//							showToast("Kindly Stop the playing audio first");
//						}
//					} else {
//						if (play.getTag().toString().equals("1")) {
//							play.setTag("0");
//							play.setImageBitmap(playBitmap);
//							current_audio = play;
//							current_aplayer = chatplayer;
//							isplaying = false;
//							chatplayer.pause();
//						}
//					}
//				}
//			});
//			chatplayer.setOnCompletionListener(new OnCompletionListener() {
//
//				@Override
//				public void onCompletion(MediaPlayer mp) {
//					// TODO Auto-generated method stub
//					isAudioRecording1 = false;
//					isplaying = false;
//					play.setImageBitmap(playBitmap);
//
//					play.setTag("0");
//					chatplayer.reset();
//					try {
//						chatplayer.setDataSource(strPath);
//						chatplayer.setLooping(false);
//						chatplayer.prepare();
//					} catch (IllegalArgumentException e) {
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error(e.getMessage(), e);
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalStateException e) {
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error(e.getMessage(), e);
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error(e.getMessage(), e);
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			});
//			/**
//			 * For new Implementation as on 17-10-14 10.10AM
//			 */
//			// TextView tvTime = (TextView)
//			// callControls1.findViewById(R.id.textView2);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.textView1);
//			// tvBuddy.setText(buddyName);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof AudioChatBean) {
//			// AudioChatBean bean = (AudioChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "audio", strPath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "audio",
//			// strPath, "Note from "
//			// + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "audio", strPath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddyName, "audio", strPath,
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (strPath != null) { File fl=new File(strPath);
//			// * if(fl.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists
//			// * ("select * from component where ContentPath='"
//			// * +strPath+"'")) { saveAsNote(buddyName, "audio", strPath,
//			// * "Note from " + buddyName); } else {
//			// * showToast("This Conversation already Saved!"); } } else {
//			// * // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// * Log.e("save", "IM from my Buddy Photo note");
//			// *
//			// * } else { // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// */
//			// }
//			// });
//
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//			/**
//			 * Ends here
//			 */
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.ALIGN_RIGHT,
//					chattingList.getChildCount());
//			rlParent.addView(callControls1, parmsContent);
//			chattingList.addView(rlParent);
//
//			scrollDown();
//			try {
//				AudioChatBean bean = new AudioChatBean();
//				bean.setBuddyName(buddyName);
//				bean.setChatTime(time);
//				bean.setFilePath(strPath);
//				bean.setType(2);
//				bean.setUserName(CallDispatcher.LoginUser);
////				for (Databean dbean : CallDispatcher.adapterToShow
////						.getListofBuddy()) {
////					if (dbean.getname().equalsIgnoreCase(
////							CallDispatcher.buddyName)) {
////						status = dbean.getStatus();
////					}
////				}
//				if (status.equalsIgnoreCase("offline")) {
//					bean.setFlag(0);
//				} else {
//					bean.setFlag(1);
//				}
//				bean.setSignalId(sigb.getSignalid());
//				appendChatDatatoXML(bean);
//				bean.setId(childMap.size());
//				Log.d("MIM", "@@@@@@@@@@@ hashmap size..." + childMap.size());
//				childMap.add(childMap.size(), bean);
//				String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//						+ "values('"
//						+ sigb.getSessionid()
//						+ "','"
//						+ strIPath
//						+ "','"
//						+ sigb.getSignalid()
//						+ "','"
//						+ sigb.getFilePath() + "')";
//				if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//					Log.i("ary", "entry created...");
//				}
//
//			} catch (Exception e) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		} catch (SecurityException e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private void videoChatFromBuddy(final String buddyName, final String time,
//			String strSignalId, final SignalingBean sigb) {
//
//		try {
//			// final String path = "/sdcard/COMMedia/" + sigb.getFilePath();
//
//			// Log.d("rpath", "_________________" + filepath);
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//
//			final LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//
//			// llayContent.setBackgroundResource(R.drawable.corner);
//			final String strPath = "/sdcard/COMMedia/" + sigb.getFilePath();
//			// llayContent.setGravity(Gravity.CENTER);
//			TextView textMessage = (TextView) llayContent
//					.findViewById(R.id.message);
//			String Chatmessage = "<font color=#4dffa6>"
//					+ CallDispatcher.LoginUser + " : " + "</font>";
//			textMessage.setText(Html.fromHtml(Chatmessage));
//			final ImageView btnPlay = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setTag(chattingList.getChildCount());
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//			// llayContent.addView(tvMsg, paramspa);
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			// String s = strPath.substring(0, strPath.lastIndexOf('.'));
//			// File f = new File(s + ".jpg");
//			// if (!f.exists()) {
//			//
//			// Bitmap imgbmp = null;
//			//
//			// File thumbfile = new File(s + ".jpg");
//			// if (thumbfile.exists()) {
//			// imgbmp = BitmapFactory.decodeFile(s + ".jpg");
//			// } else {
//			// imgbmp = CreateVideoThumbnail(strPath.substring(0,
//			// strPath.lastIndexOf('.')));
//			// }
//			//
//			// if (imgbmp != null) {
//			// btnPlay.setImageBitmap(imgbmp);
//			// bitmaplist.add(imgbmp);
//			// } else {
//			// btnPlay.setBackgroundResource(R.drawable.broken);
//			// }
//			// } else {
//			// Bitmap bmp = null;
//			// bmp = BitmapFactory.decodeFile(s + ".jpg");
//			// if (bmp != null) {
//			// btnPlay.setImageBitmap(bmp);
//			// bitmaplist.add(bmp);
//			// } else {
//			// btnPlay.setBackgroundResource(R.drawable.broken);
//			// }
//			// }
//			// btnPlay.setBackgroundResource(R.drawable.v_play1);
//			// play.setText("Play");
//			// btnPlay.setLayoutParams(new
//			// LayoutParams(LayoutParams.WRAP_CONTENT,
//			// LayoutParams.WRAP_CONTENT));
//			btnPlay.setImageBitmap(playBitmap);
//			btnPlay.setVisibility(View.VISIBLE);
//			btnPlay.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					Intent intentVPlayer = new Intent(context,
//							VideoPlayer.class);
//					Log.d("FLE", "My File path--->" + strPath);
//					intentVPlayer.putExtra("File_Path", strPath);
//					intentVPlayer.putExtra("Player_Type", "Video Player");
//					startActivity(intentVPlayer);
//				}
//			});
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (strPath != null) {
//			// saveAsNote(buddyName, "video",
//			// strPath.substring(0, strPath.lastIndexOf('.')),
//			// "Note from " + buddyName);
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//			//
//			// llayContent.addView(btnSave);
//
//			videohandler1 = new Handler() {
//
//				@Override
//				public void handleMessage(Message msg) {
//					// TODO Auto-generated method stub
//
//					super.handleMessage(msg);
//				}
//
//			};
//
//			// TextView tvTime = (TextView)
//			// callControls1.findViewById(R.id.textView2);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.textView1);
//			// tvBuddy.setText(buddyName);
//
//			try {
//				VideoChatBean bean = new VideoChatBean();
//				bean.setBuddyName(buddyName);
//				bean.setChatTime(time());
//				bean.setFilePath(strPath);
//				bean.setType(2);
//				bean.setFlag(1);
//				bean.setSignalId(sigb.getSignalid());
//				bean.setUserName(CallDispatcher.LoginUser);
//				appendChatDatatoXML(bean);
//				bean.setId(childMap.size());
//				Log.d("MIM", "@@@@@@@@@@@ hashmap size..." + childMap.size());
//				childMap.add(childMap.size(), bean);
//				String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//						+ "values('"
//						+ sigb.getSessionid()
//						+ "','"
//						+ strPath.substring(0, strPath.indexOf("."))
//						+ "','"
//						+ sigb.getSignalid()
//						+ "','"
//						+ sigb.getFilePath()
//						+ "')";
//				if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//					Log.i("ary", "entry created...");
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//			}
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (childMap.get(id) instanceof VideoChatBean) {
//			// VideoChatBean bean = (VideoChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getoldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// } else {
//			// if (newpath.contains(".mp4")) {
//			// newpath = newpath.substring(0,
//			// newpath.length() - 4);
//			// }
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// {
//			// if (oldpath.contains(".mp4")) {
//			// oldpath = oldpath.substring(0,
//			// oldpath.length() - 4);
//			// }
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + strPath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddyName,
//			// "video",
//			// strPath.substring(0,
//			// strPath.lastIndexOf('.')),
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddyName,
//			// "video",
//			// strPath.substring(
//			// 0,
//			// strPath.lastIndexOf('.')),
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + strPath);
//			// if (bean.getFilePath().contains(".mp4")) {
//			// bean.setFilePath(bean.getFilePath().substring(
//			// 0, bean.getFilePath().length() - 4));
//			// }
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddyName,
//			// "video",
//			// strPath.substring(0,
//			// strPath.lastIndexOf('.')),
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddyName,
//			// "video",
//			// strPath.substring(0,
//			// strPath.lastIndexOf('.')),
//			// "Note from " + buddyName);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (strPath != null) {
//			// *
//			// * File fl=new File(strPath.substring(0,
//			// * strPath.lastIndexOf('.'))+".mp4"); if(fl.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"
//			// * +strPath.substring(0, strPath.lastIndexOf('.'))+"'")) {
//			// * saveAsNote(buddyName, "video", strPath.substring(0,
//			// * strPath.lastIndexOf('.')), "Note from " + buddyName); }
//			// * else { showToast("This Conversation already Saved!"); } }
//			// * else { showToast("unable to create file"); }
//			// *
//			// * Log.e("save", "IM from my Buddy Photo note"); } else { //
//			// * unable to create file showToast("unable to create file");
//			// * }
//			// */
//			//
//			// }
//			// });
//
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF,
//					chattingList.getChildCount());
//			rlParent.addView(callControls1, parmsContent);
//			chattingList.addView(rlParent);
//
//			scrollDown();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private Bitmap CreateVideoThumbnail(String strThumbPath) {
//		try {
//			System.out.println("create thumbnail");
//			System.out.println("Thumb path" + strThumbPath);
//
//			MediaPlayer m = new MediaPlayer();
//			m.setDataSource(strThumbPath + ".mp4");
//			m.prepare();
//
//			long milliseconds = (long) m.getDuration();
//			String seconds = WebServiceReferences.setLength2((int) (Math
//					.round((double) milliseconds / 1000) % 60));
//			String minutes = WebServiceReferences
//					.setLength2((int) ((milliseconds / (1000 * 60)) % 60));
//			String hours = WebServiceReferences
//					.setLength2((int) ((milliseconds / (1000 * 60 * 60)) % 24));
//
//			String asText = hours + ":" + minutes + ":" + seconds;
//			Log.e("myLog", "Duration :" + asText);
//
//			Bitmap thumbImage = ThumbnailUtils.createVideoThumbnail(
//					strThumbPath + ".mp4",
//					MediaStore.Images.Thumbnails.MINI_KIND);
//			Log.e("lg", "?????????????????Thumb image" + thumbImage);
//			if (thumbImage != null) {
//				thumbImage = ResizeVideoImage(thumbImage);
//
//				Log.i("myLog", "Image Height :" + thumbImage.getHeight());
//				Log.i("myLog", "Image Width  :" + thumbImage.getWidth());
//
//				Bitmap thumb = combineImages(thumbImage, asText);
//
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				thumb.compress(CompressFormat.JPEG, 75, bos);
//				FileOutputStream fos = new FileOutputStream(strThumbPath
//						+ ".jpg");
//				bos.writeTo(fos);
//				bos.close();
//				fos.close();
//				return thumb;
//			} else {
//				File fl = new File(strThumbPath + ".mp4");
//				if (fl.exists()) {
//
//					Bitmap bmp = BitmapFactory.decodeResource(getResources(),
//							R.drawable.broken);
//					bmp = ResizeVideoImage(bmp);
//					Log.i("myLog", "Image Height :" + bmp.getHeight());
//					Log.i("myLog", "Image Width  :" + bmp.getWidth());
//					Bitmap thumb1 = combineImages(bmp, asText);
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					thumb1.compress(CompressFormat.JPEG, 75, bos);
//					FileOutputStream fos = new FileOutputStream(strThumbPath
//							+ ".jpg");
//					bos.writeTo(fos);
//					bos.close();
//					fos.close();
//					return thumb1;
//				} else {
//					return null;
//				}
//			}
//
//		} catch (Exception ex) {
//			// Log.e("Exc", "FileNotFoundException : " + ex);
//			Log.d("fdstatus", "filenot");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(ex.getMessage(), ex);
//			return null;
//		}
//
//	}
//
//	@SuppressWarnings("finally")
//	private Bitmap combineImages(Bitmap c, String time) {
//		try {
//			Bitmap cs = null;
//
//			Bitmap bitmapPlay = BitmapFactory.decodeResource(getResources(),
//					R.drawable.vplay1);
//			int width, height = 0;
//			width = c.getWidth();
//			height = c.getHeight();
//			cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//			Canvas comboImage = new Canvas(cs);
//			comboImage.drawBitmap(c, 0f, 0f, null);
//			comboImage.drawBitmap(bitmapPlay,
//					(c.getWidth() / 2 - bitmapPlay.getWidth() / 2),
//					(c.getHeight() / 2 - bitmapPlay.getHeight() / 2), null);
//			Paint paint = new Paint();
//			paint.setColor(Color.RED);
//			paint.setTypeface(Typeface.SERIF);
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
//			paint.setTextSize(30);
//			comboImage.drawText(time, (c.getWidth() / 2 - bitmapPlay.getWidth()
//					/ 2 + 40),
//					(c.getHeight() / 1 - bitmapPlay.getHeight() / 1 + 100),
//					paint);
//
//			return cs;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private Bitmap ResizeVideoImage(Bitmap bitmap) {
//
//		try {
//			int outWidth = bitmap.getWidth();
//			int outHeight = bitmap.getHeight();
//
//			if (outWidth == outHeight) {
//				bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
//			} else {
//
//				if (outHeight == 200) {
//					bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
//							outHeight, true);
//				} else {
//					double ratio;
//					// Log.i("IMG", "Demo ratio:" + ((double) 2 / (double) 3));
//					if (outHeight < outWidth) {
//						ratio = (double) outWidth / (double) outHeight;
//
//						bitmap = Bitmap.createScaledBitmap(bitmap,
//								(int) (200 * ratio), 200, true);
//						// Log.e("IMG", "ratio:" + ratio);
//					} else {
//						// Log.i("IMG", "IMG Height:" + opts.outHeight);
//						// Log.i("IMG", "IMG Width:" + opts.outWidth);
//						ratio = (double) outWidth / (double) outHeight;
//
//						bitmap = Bitmap.createScaledBitmap(bitmap,
//								(int) Math.round(200 / ratio), 200, true);
//					}
//
//				}
//
//			}
//			return bitmap;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private void appendChatDatatoXML(Object chatBean) {
//
//		try {
//			Log.d("XML", "################ method called");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("################ method called");
//			myXMLWriter.addChildNode(chatBean);
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			// Log.e("X", e.getLocalizedMessage());
//			if (AppReference.isWriteInFile)
//				AppReference.logger
//						.error("Excetion raised" + e.getMessage(), e);
//		}
//	}
//
//	private void CheckMIM() {
//		try {
//			Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM called");
//			ArrayList<Object> list = new ArrayList<Object>();
//			list = myXMLParser.getParseList();
//			Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called" + list.size());
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM called"
//						+ list.size());
//			childMap.clear();
//			Log.d("INSTANT",
//					"@@@@@@@@@@@ChaeckMIM called"
//							+ chattingList.getChildCount());
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM called"
//						+ chattingList.getChildCount());
//
//			if (chattingList.getChildCount() > 0) {
//				Log.d("INSTANT",
//						"@@@@@@@@@@@ChaeckMIM called"
//								+ chattingList.getChildCount());
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM called"
//							+ chattingList.getChildCount());
//
//				chattingList.removeAllViewsInLayout();
//			}
//			for (int i = 0; i < list.size(); i++) {
//				Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called");
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM called");
//				Object bean = list.get(i);
//				Log.d("MIM", "@@@@@@@@@@@ going to add in hash map" + i);
//				if (AppReference.isWriteInFile)
//					AppReference.logger
//							.debug("@@@@@@@@@@@ going to add in hash map" + i);
//
//				if (bean instanceof TextChatBean) {
//					Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called Text");
//					if (AppReference.isWriteInFile)
//						AppReference.logger
//								.debug("@@@@@@@@@@@ChaeckMIM called Text");
//					TextChatBean bn = (TextChatBean) bean;
//					Log.d("MIM",
//							"@@@@@@@@@@@ChaeckMIM called Text" + bn.getType());
//					if (AppReference.isWriteInFile)
//						AppReference.logger
//								.debug("@@@@@@@@@@@ChaeckMIM called Text"
//										+ bn.getType());
//					Log.d("MIM",
//							"@@@@@@@@@@@ChaeckMIM called Text" + bn.getFlag());
//					if (AppReference.isWriteInFile)
//						AppReference.logger
//								.debug("@@@@@@@@@@@ChaeckMIM called Text"
//										+ bn.getFlag());
//
//					if (bn.getType() == 1) {
//						Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called if part");
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ChaeckMIM called if part");
//						chattingList.addView(imFromME(bn.getMessage(),
//								bn.getChatTime(), "", bn.getFlag(), false, i));
//					} else if (bn.getType() == 2) {
//						Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called else part");
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ChaeckMIM called else part");
//						chattingList.addView(imFromMyBuddy(bn.getBuddyName(),
//								bn.getMessage(), bn.getChatTime()));
//					}
//					bn.setId(childMap.size());
//					childMap.add(childMap.size(), bn);
//
//				} else if (bean instanceof ImageChatBean) {
//					ImageChatBean bn = (ImageChatBean) bean;
//					Log.d("MIM", "@@@@@@@@@@@ChaeckMIM image");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM image");
//					Log.d("MIM", "@@@@@@@@@@@ChaeckMIM image" + bn.getFlag());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM image"
//								+ bn.getFlag());
//					if (bn.getType() == 1) {
//						Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called if part");
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ChaeckMIM called if part");
//						if (bn.getIsRobo()) {
//							int viewmode = 0;
//
//							chattingList.addView(imageChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									bn.getBuddyName(), viewmode, bn.getFlag(),
//									i));
//							if (viewmode == 5) {
//								roboMap.put(chattingList.getChildCount() + " "
//										+ bn.getFilePath(), chattingList
//										.getChildAt(chattingList
//												.getChildCount() - 1));
//							}
//						} else {
//							chattingList.addView(imageChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									bn.getBuddyName(), 0, bn.getFlag(), i));
//						}
//					} else if (bn.getType() == 2) {
//						Log.d("MIM", "@@@@@@@@@@@ChaeckMIM called else part");
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ChaeckMIM called else part");
//						UpdateImageChat(bn.getFilePath(), bn.getBuddyName(),
//								bn.getChatTime());
//					}
//					bn.setId(childMap.size());
//					childMap.add(childMap.size(), bn);
//				}
//
//				else if (bean instanceof AudioChatBean) {
//					AudioChatBean bn = (AudioChatBean) bean;
//					Log.d("MIM", "@@@@@@@@@@@ChaeckMIM audio" + bn.getFlag());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM video"
//								+ bn.getFlag());
//					if (bn.getType() == 1) {
//						if (bn.getIsRobo()) {
//							int viewmode = 0;
//							chattingList.addView(AudioChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									bn.getBuddyName(), viewmode, bn.getFlag(),
//									i));
//							if (viewmode == 5) {
//								roboMap.put(chattingList.getChildCount() + " "
//										+ bn.getFilePath(), chattingList
//										.getChildAt(chattingList
//												.getChildCount() - 1));
//							}
//						} else {
//							chattingList.addView(AudioChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									bn.getBuddyName(), 0, bn.getFlag(), i));
//						}
//					} else if (bn.getType() == 2) {
//						updateAudioChat(bn.getFilePath(), bn.getBuddyName(),
//								bn.getChatTime());
//					}
//					bn.setId(childMap.size());
//					childMap.add(childMap.size(), bn);
//				}
//
//				else if (bean instanceof VideoChatBean) {
//					VideoChatBean bn = (VideoChatBean) bean;
//					Log.d("MIM", "@@@@@@@@@@@ChaeckMIM video" + bn.getFlag());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("@@@@@@@@@@@ChaeckMIM video"
//								+ bn.getFlag());
//					if (bn.getType() == 1) {
//						if (bn.getIsRobo()) {
//							int viewmode = 0;
//							chattingList.addView(VideoChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									CallDispatcher.LoginUser, viewmode,
//									bn.getFlag(), i));
//							if (viewmode == 5) {
//								roboMap.put(chattingList.getChildCount() + " "
//										+ bn.getFilePath(), chattingList
//										.getChildAt(chattingList
//												.getChildCount() - 1));
//							}
//						} else {
//							chattingList.addView(VideoChatFromME(
//									bn.getFilePath(), bn.getChatTime(), "",
//									CallDispatcher.LoginUser, 0, bn.getFlag(),
//									i));
//						}
//					} else if (bn.getType() == 2) {
//						updateVideoChat(bn.getFilePath(), bn.getBuddyName(),
//								bn.getChatTime());
//					}
//					bn.setId(childMap.size());
//					childMap.add(childMap.size(), bn);
//				}
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void UpdateImageChat(final String filepath, final String buddy,
//			String time) {
//
//		try {
//			// TODO Auto-generated method stub
//
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//			final String strPath = filepath;
//			// llayContent.setGravity(Gravity.CENTER);
//			Bitmap bitmap = null;
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setVisibility(View.VISIBLE);
//			// save.setTag(chattingList.getChildCount());
//			final File fileCheck = new File(strPath);
//			if (fileCheck.exists()) {
//				bitmap = callDisp.ResizeImage(strPath);
//				bitmap = Bitmap.createScaledBitmap(bitmap, 100, 75, false);
//
//				bitmaplist.add(bitmap);
//			}
//			ImageView ivPhoto = (ImageView) llayContent
//					.findViewById(R.id.multimedia_img);
//			ivPhoto.setVisibility(View.VISIBLE);
//			if (bitmap != null) {
//				ivPhoto.setImageBitmap(bitmap);
//			} else {
//				ivPhoto.setBackgroundResource(R.drawable.broken);
//			}
//			ivPhoto.setPadding(10, 10, 10, 10);
//			// ivPhoto.setBackgroundColor(R.drawable.corner);
//			ivPhoto.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (fileCheck.exists()) {
//						try {
//							Intent in = new Intent(context,
//									PhotoZoomActivity.class);
//							in.putExtra("Photo_path", filepath);
//							in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							startActivity(in);
//						} catch (WindowManager.BadTokenException e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Bad Tocken:"
//										+ e.toString());
//						} catch (Exception e) {
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							// TODO: handle exception
//						}
//					} else {
//						showToast("File is not available!");
//					}
//
//				}
//			});
//
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// LinearLayout.LayoutParams.WRAP_CONTENT,
//			// LinearLayout.LayoutParams.WRAP_CONTENT);
//			// params.gravity = Gravity.CENTER;
//			//
//			// ivPhoto.setLayoutParams(params);
//			//
//			// llayContent.addView(ivPhoto);
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (filepath != null) {
//			// saveAsNote(buddy, "photo", strPath, "Note from " + buddy);
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//			//
//			// llayContent.addView(btnSave);
//			TextView imgMsg = (TextView) llayContent.findViewById(R.id.message);
//			if (buddy.equalsIgnoreCase(CallDispatcher.LoginUser)) {
//				String Chatmessage = "<font color=#ff4d4d>" + buddy + " : "
//						+ "</font>";
//				imgMsg.setText(Html.fromHtml(Chatmessage));
//			} else {
//				String Chatmessage = "<font color=#4dffa6>" + buddy + " : "
//						+ "</font>";
//				imgMsg.setText(Html.fromHtml(Chatmessage));
//			}
//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//
//			// llaySave.setBackgroundColor(Color.WHITE);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			// ;
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ save button id"
//			// + id);
//			// if (childMap.get(id) instanceof ImageChatBean) {
//			// ImageChatBean bean = (ImageChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldfileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ oldpath"
//			// + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "photo", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "photo", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "photo", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (filepath != null) {
//			// bean.setFilePath(filepath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "photo", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (filepath != null) {
//			// *
//			// * File fl=new File(filepath); if(fl.exists()) {
//			// *
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"
//			// * +filepath+"'")) { saveAsNote(buddy, "photo", strPath,
//			// * "Note from " + buddy); Log.e("save",
//			// * "IM from my Buddy Photo note"); } else {
//			// * showToast("This Conversation already Saved!"); }
//			// *
//			// *
//			// * Log.e("save", "IM from my Buddy Photo note"); } else { //
//			// * unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); } } else {
//			// * // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// */
//			// }
//			// });
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//			chattingList.addView(rlParent);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void updateAudioChat(final String filepath, final String buddy,
//			String time) {
//
//		try {
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setVisibility(View.VISIBLE);
//			// save.setTag(chattingList.getChildCount());
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//
//			final String strPath = filepath;
//			llayContent.setGravity(Gravity.CENTER);
//			final File fileCheck = new File(strPath);
//			if (fileCheck.exists()) {
//				Log.i("IMMSG", "Exists chat player ====>" + strPath);
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("Exists chat player ====>"
//							+ strPath);
//
//				callDisp.ResizeImage(strPath, 200);
//			} else {
//				Log.i("IMMSG", "Not Exists====>" + strPath);
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("Not Exists====>" + strPath);
//
//			}
//
//			TextView audioMsg = (TextView) llayContent
//					.findViewById(R.id.message);
//			if (buddy.equalsIgnoreCase(CallDispatcher.LoginUser)) {
//				String Chatmessage = "<font color=#ff4d4d>" + buddy + " : "
//						+ "</font>";
//				audioMsg.setText(Html.fromHtml(Chatmessage));
//			} else {
//				String Chatmessage = "<font color=#4dffa6>" + buddy + " : "
//						+ "</font>";
//				audioMsg.setText(Html.fromHtml(Chatmessage));
//			}
//
//			final ImageView play = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			play.setId(100);
//			play.setTag("0");
//			play.setImageBitmap(playBitmap);
//			play.setVisibility(View.VISIBLE);
//			final MediaPlayer chatplayer = new MediaPlayer();
//			try {
//				Log.i("IMMSG", "chat player ====>" + filepath);
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("chat player ====>" + filepath);
//				chatplayer.setDataSource(filepath);
//				chatplayer.setLooping(false);
//				chatplayer.prepare();
//
//			} catch (IllegalArgumentException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalStateException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e1.getMessage(), e1);
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			play.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					File fle = new File(filepath);
//					if (fle.exists()) {
//						if (play.getTag().toString().equals("0")) {
//							play.setTag("1");
//							play.setImageBitmap(pauseBitmap);
//
//							try {
//								current_audio = play;
//								current_aplayer = chatplayer;
//								isplaying = true;
//								isAudioRecording1 = true;
//								chatplayer.start();
//							} catch (IllegalArgumentException e) {
//								if (AppReference.isWriteInFile)
//									AppReference.logger.error(e.getMessage(), e);
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (IllegalStateException e) {
//								if (AppReference.isWriteInFile)
//									AppReference.logger.error(e.getMessage(), e);
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						} else if (play.getTag().toString().equals("1")) {
//
//							play.setTag("0");
//							play.setImageBitmap(playBitmap);
//
//							current_audio = play;
//							current_aplayer = chatplayer;
//							isplaying = false;
//							chatplayer.pause();
//
//						}
//					} else {
//						ShowError("Player Error",
//								"Sorry can not play this Audio");
//					}
//					chatplayer
//							.setOnCompletionListener(new OnCompletionListener() {
//
//								@Override
//								public void onCompletion(MediaPlayer mp) {
//									// TODO Auto-generated method stub
//									play.setImageBitmap(playBitmap);
//									play.setTag("0");
//									chatplayer.reset();
//									try {
//										chatplayer.setDataSource(filepath);
//										chatplayer.setLooping(false);
//										chatplayer.prepare();
//									} catch (IllegalArgumentException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (IllegalStateException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} catch (IOException e) {
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//
//								}
//							});
//				}
//			});
//
//			// TextView tvTime = (TextView)
//			// callControls1.findViewById(R.id.textView2);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.textView1);
//			// tvBuddy.setText(buddy);
//
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//			//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ save button id"
//			// + id);
//			// if (childMap.get(id) instanceof AudioChatBean) {
//			// AudioChatBean bean = (AudioChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getOldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ oldpath"
//			// + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("@@@@@@@@@@@ new path" + newpath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "audio", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "audio", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "audio", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "audio", strPath,
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (strPath != null) { File fl=new File(strPath);
//			// * if(fl.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists
//			// * ("select * from component where ContentPath='"
//			// * +strPath+"'")) { saveAsNote(buddy, "audio", strPath,
//			// * "Note from " + buddy); } else {
//			// * showToast("This Conversation already Saved!"); } } else {
//			// * // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// * Log.e("save", "IM from my Buddy Photo note");
//			// *
//			// * } else { // unable to create file
//			// *
//			// * showToast("unable to Save this Conversation"); }
//			// */
//			//
//			// }
//			// });
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//			chattingList.addView(rlParent);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void updateVideoChat(final String filepath, final String buddy,
//			String time) {
//		try {
//			RelativeLayout rlParent = new RelativeLayout(context);
//			rlParent.setLayoutParams(new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.FILL_PARENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//
//			Log.d("rpath", "_________________" + filepath);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("_________________" + filepath);
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			LinearLayout callControls1 = (LinearLayout) inflateLayout1.inflate(
//					R.layout.im_chat_rec, null);
//
//			final LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.chat_content);
//			// llayContent.removeAllViews();
//			// final ImageView icon = (ImageView) callControls1
//			// .findViewById(R.id.usericons);
//			//
//			// icon.setImageBitmap(setUserIcon(buddy));
//			// llayContent.setBackgroundResource(R.drawable.corner);
//			final String strPath = filepath;
//			// llayContent.setGravity(Gravity.CENTER);
//			TextView tvMsg = (TextView) llayContent.findViewById(R.id.message);
//			if (buddy.equalsIgnoreCase(CallDispatcher.LoginUser)) {
//				String Chatmessage = "<font color=#ff4d4d>" + buddy + " : "
//						+ "</font>";
//				tvMsg.setText(Html.fromHtml(Chatmessage));
//			} else {
//				String Chatmessage = "<font color=#4dffa6>" + buddy + " : "
//						+ "</font>";
//				tvMsg.setText(Html.fromHtml(Chatmessage));
//			}
//			// final ImageView save = (ImageView) callControls1
//			// .findViewById(R.id.imageView23);
//			// save.setVisibility(View.VISIBLE);
//			// save.setTag(chattingList.getChildCount());
//			// RelativeLayout.LayoutParams paramspa = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspa.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//			// llayContent.addView(tvMsg, paramspa);
//			final ImageView btnPlay = (ImageView) llayContent
//					.findViewById(R.id.multimedia_btn);
//			// btnPlay.setLayoutParams(new
//			// LayoutParams(LayoutParams.WRAP_CONTENT,
//			// LayoutParams.WRAP_CONTENT));
//			btnPlay.setImageBitmap(playBitmap);
//			btnPlay.setVisibility(View.VISIBLE);
//
//			// llayContent.addView(btnPlay);
//			// btnPlay.setBackgroundResource(R.drawable.v_play1);
//
//			btnPlay.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intentVPlayer = new Intent(context,
//							VideoPlayer.class);
//					String filePath = strPath;
//					if (filePath.endsWith(".mp4"))
//						filePath = strPath + ".mp4";
//					intentVPlayer.putExtra("File_Path", filePath);
//					intentVPlayer.putExtra("Player_Type", "Video Player");
//					startActivity(intentVPlayer);
//				}
//			});
//
//			// Button btnSave = new Button(context);
//			// btnSave.setBackgroundResource(R.drawable.save_as_note);
//			// btnSave.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// if (strPath != null) {
//			// saveAsNote(buddy, "video",
//			// filepath.substring(0, filepath.lastIndexOf('.')),
//			// "Note from " + buddy);
//			// Log.e("save", "IM from my Buddy Photo note");
//			// } else {
//			// // unable to create file
//			// }
//			//
//			// }
//			// });
//			//
//			// llayContent.addView(btnSave);
//			videohandler1 = new Handler() {
//
//				@Override
//				public void handleMessage(Message msg) {
//					// TODO Auto-generated method stub
//
//					Intent intentVPlayer = new Intent(context,
//							VideoPlayer.class);
//					String filePath = strPath;
//					if (filePath.endsWith(".mp4"))
//						filePath = filePath + ".mp4";
//					intentVPlayer.putExtra("File_Path", filePath);
//					intentVPlayer.putExtra("Player_Type", "Video Player");
//					startActivity(intentVPlayer);
//
//					super.handleMessage(msg);
//				}
//
//			};
//
//			// TextView tvTime = (TextView)
//			// callControls1.findViewById(R.id.textView2);
//			// tvTime.setText(time);
//			// TextView tvBuddy = (TextView) callControls1
//			// .findViewById(R.id.textView1);
//			// tvBuddy.setText(buddy);
//			// TextView tvTime = new TextView(context);
//			// tvTime.setTextColor(getResources().getColor(R.color.im_grey));
//			// tvTime.setText(time);
//			// tvTime.setTextSize(10);
//			// tvTime.setPadding(5, 0, 5, 0);
//
//			// RelativeLayout.LayoutParams paramspas = new
//			// RelativeLayout.LayoutParams(
//			// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			// paramspas.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			//
//			// llayContent.addView(tvTime, paramspas);
//
//			// llaySave.setBackgroundColor(Color.WHITE);
//
//			// save.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			//
//			// int id = Integer.parseInt(save.getTag().toString());
//			//
//			// Log.d("MIM", "@@@@@@@@@@@ save button id" + id);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ save button id"
//			// + id);
//			// if (childMap.get(id) instanceof VideoChatBean) {
//			// VideoChatBean bean = (VideoChatBean) childMap.get(id);
//			// if ((bean.getisImported()) || bean.getIsRobo()) {
//			// String oldpath = bean.getoldFileName();
//			// Log.d("MIM", "@@@@@@@@@@@ oldpath" + oldpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger.debug("@@@@@@@@@@@ oldpath"
//			// + oldpath);
//			// String newpath = bean.getFilePath();
//			// if (newpath.trim().length() == 0) {
//			// newpath = "empty";
//			// } else {
//			// if (newpath.contains(".mp4")) {
//			// newpath = newpath.substring(0,
//			// newpath.length() - 4);
//			// }
//			// }
//			// if (oldpath.trim().length() == 0) {
//			// oldpath = "empty";
//			// }
//			// {
//			// if (oldpath.contains(".mp4")) {
//			// oldpath = oldpath.substring(0,
//			// oldpath.length() - 4);
//			// }
//			// }
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + newpath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("@@@@@@@@@@@ new path" + newpath);
//			// Log.d("MIM", "@@@@@@@@@@@ new path" + strPath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("@@@@@@@@@@@ new path" + strPath);
//			//
//			// if ((!callDisp.getdbHeler(context).isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))
//			// && (!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddy,
//			// "video",
//			// filepath.substring(0,
//			// filepath.lastIndexOf('.')),
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + oldpath + "'"))) {
//			// if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + newpath + "'"))) {
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(buddy, "video", filepath
//			// .substring(0, filepath
//			// .lastIndexOf('.')),
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			//
//			// }
//			// }
//			//
//			// else {
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee");
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee"
//			// + bean.getFilePath());
//			// Log.i("lggg",
//			// "########### came to elseeeeeeeeeeeeeeee"
//			// + strPath);
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("########### came to elseeeeeeeeeeeeeeee"
//			// + strPath);
//			// if (bean.getFilePath().contains(".mp4")) {
//			// bean.setFilePath(bean.getFilePath().substring(
//			// 0, bean.getFilePath().length() - 4));
//			// }
//			// if ((bean.getFilePath().trim().length() == 0)
//			// && (!bean.getFilePath().equalsIgnoreCase(
//			// "empty"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddy,
//			// "video",
//			// filepath.substring(0,
//			// filepath.lastIndexOf('.')),
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else if ((!callDisp.getdbHeler(context)
//			// .isRecordExists(
//			// "select * from component where ContentPath='"
//			// + bean.getFilePath() + "'"))) {
//			//
//			// if (strPath != null) {
//			// bean.setFilePath(strPath);
//			// Log.e("save",
//			// "Save my Text Note " + bean.getId());
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note "
//			// + bean.getId());
//			// childMap.remove(id);
//			// childMap.add(id, bean);
//			// saveAsNote(
//			// buddy,
//			// "video",
//			// filepath.substring(0,
//			// filepath.lastIndexOf('.')),
//			// "Note from " + buddy);
//			// myXMLWriter.updateXMLContent(bean);
//			// Log.e("save", "Save my Text Note ");
//			// if (AppReference.isWriteInFile)
//			// AppReference.logger
//			// .debug("Save my Text Note ");
//			// }
//			// } else {
//			// showToast("This conversation is already saved!");
//			// }
//			// }
//			// }
//			//
//			// /*
//			// * if (strPath != null) {
//			// *
//			// * File fl=new File(strPath.substring(0,
//			// * strPath.lastIndexOf('.'))+".mp4"); if(fl.exists()) {
//			// * if(!callDisp.getdbHeler(context).isRecordExists(
//			// * "select * from component where ContentPath='"
//			// * +strPath.substring(0, strPath.lastIndexOf('.'))+"'")) {
//			// * saveAsNote(buddy, "video", filepath.substring(0,
//			// * filepath.lastIndexOf('.')), "Note from " + buddy); } else
//			// * { showToast("This Conversation already Saved!"); } } else
//			// * { showToast("unable to create file"); }
//			// *
//			// * Log.e("save", "IM from my Buddy Photo note"); } else { //
//			// * unable to create file showToast("unable to create file");
//			// * }
//			// */
//			// }
//			// });
//
//			RelativeLayout.LayoutParams parmsContent = new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//
//			parmsContent.addRule(RelativeLayout.RIGHT_OF, 100);
//			rlParent.addView(callControls1, parmsContent);
//
//			chattingList.addView(rlParent);
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private void updateVideoChat1(final String path, String buddy, String time) {
//		try {
//			LayoutInflater inflateLayout1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			RelativeLayout callControls1 = (RelativeLayout) inflateLayout1
//					.inflate(R.layout.im_chat_msg, null);
//
//			// if (strSignalId.length() != 0)
//			// WebServiceReferences.hsIMProgress.put(strSignalId,
//			// pbSendProgress);
//
//			final LinearLayout llayContent = (LinearLayout) callControls1
//					.findViewById(R.id.rl_content);
//			llayContent.removeAllViews();
//			// llayContent.setBackgroundResource(R.drawable.corner);
//
//			llayContent.setGravity(Gravity.CENTER);
//			final VideoView videoView = new VideoView(context);
//			videoView.setVideoPath(path);
//			final ImageView btnPlay = new ImageView(context);
//
//			btnPlay.setVisibility(View.VISIBLE);
//			llayContent.addView(btnPlay);
//			Log.d("Path", "QQQQQQQQQQQQQQQ" + path);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("QQQQQQQQQQQQQQQ" + path);
//			String s = path.substring(0, path.lastIndexOf('.'));
//			Log.d("Path", "@@@@@@@@@@@@@@@@@@" + s);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("@@@@@@@@@@@@@@@@@@" + s);
//			Bitmap bmp = null;
//			File f = new File(s + ".jpg");
//			if (f.exists()) {
//				Log.d("Path", "QQQQQQQQQQQQQQQ Condition satisfied");
//				if (AppReference.isWriteInFile)
//					AppReference.logger
//							.debug("QQQQQQQQQQQQQQQ Condition satisfied");
//				bmp = callDisp.ResizeImage(f.getAbsolutePath());
//				btnPlay.setImageBitmap(bmp);
//			}
//
//			btnPlay.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					/*
//					 * llayContent.removeView(btnPlay);
//					 * 
//					 * final LinearLayout layout=new LinearLayout(context);
//					 * layout.setLayoutParams(new
//					 * LinearLayout.LayoutParams(200,200));
//					 * layout.setGravity(Gravity.CENTER); videoView
//					 * .setLayoutParams(new
//					 * LinearLayout.LayoutParams(LayoutParams.FILL_PARENT
//					 * ,LayoutParams.FILL_PARENT));
//					 * videoView.setPadding(10,10,10,10);
//					 * 
//					 * videoView.setZOrderOnTop(true); videoView.start();
//					 * layout.addView(videoView); llayContent.addView(layout);
//					 * videoView.setOnCompletionListener(new
//					 * MediaPlayer.OnCompletionListener() {
//					 * 
//					 * @Override public void onCompletion(MediaPlayer mp) { //
//					 * not playVideo // playVideo(); layout.removeAllViews();
//					 * llayContent.removeView(layout);
//					 * llayContent.addView(btnPlay);
//					 * 
//					 * } });
//					 */
//					Intent intentVPlayer = new Intent(context,
//							VideoPlayer.class);
//					intentVPlayer.putExtra("File_Path", path);
//					intentVPlayer.putExtra("Player_Type", "Video Player");
//					startActivity(intentVPlayer);
//				}
//			});
//
//			chattingList.addView(callControls1);
//		} catch (Exception e) {
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void changeSessionHash(SignalingBean sb) {
//		try {
//			/*
//			 * if (WebServiceReferences.instantMessageScreen.size() == 1) {
//			 * WebServiceReferences.instantMessageScreen.clear();
//			 * WebServiceReferences.instantMessageScreen.put(sb.getSessionid(),
//			 * context); WebServiceReferences.session = ""; sessionid =
//			 * sb.getSessionid();
//			 * 
//			 * }
//			 */
//
//			if (WebServiceReferences.instantMessageScreen
//					.containsKey(sessionid)) {
//				WebServiceReferences.instantMessageScreen.remove(sessionid);
//				WebServiceReferences.instantMessageScreen.put(
//						sb.getSessionid(), context);
//				WebServiceReferences.activeSession.remove(sb.getFrom());
//				WebServiceReferences.activeSession.put(sb.getFrom(),
//						sb.getSessionid());
//				sessionid = sb.getSessionid();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void notifyFTPServerConnected1(boolean res, FTPBean bn) {
//		try {
//			if (res) {
//				if (!isdownload) {
//					if (strIPath != null) {
//						String signalId = Long.toString(
//								utility.getRandomMediaID()).trim();
//
//						MMBean obj = new MMBean();
//						obj.setImagePath(strIPath);
//						obj.setSessionId(sessionid);
//
//						WebServiceReferences.hsMPPBean.put(obj.getFileName(),
//								obj);
//						System.out
//								.println("----------------btnSendPhoto------------------"
//										+ obj.getFileName());
//
//						llayStart.removeAllViews();
//						llayTimer.removeAllViews();
//						mmlay.removeAllViews();
//
//						// Add to ui at the time of click Send
//						String path = obj.getFilePath();
//						String time = time();
//						String touser = buddy;
//
//						AudioChatBean audiobean = new AudioChatBean();
//						audiobean.setBuddyName(touser);
//						audiobean.setChatTime(time());
//						audiobean.setFilePath(path);
//						audiobean.setType(1);
//						audiobean.setFlag(1);
//						audiobean.setSignalId(signalId);
//						audiobean.setUserName(initializer);
//						if (isimported) {
//							audiobean.setisImported(true);
//							isimported = false;
//						}
//						appendChatDatatoXML(audiobean);
//
//						scrollDown();
//						audiobean.setId(childMap.size());
//						Log.d("MIM",
//								"@@@@@@@@@@@ hashmap size..." + childMap.size());
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ hashmap size..."
//											+ childMap.size());
//						childMap.add(childMap.size(), audiobean);
//						chattingList.addView(AudioChatFromME(path, time,
//								"quickchat", touser, 0, 1, audiobean.getId()));
//
//						/*
//						 * childMap.put(Integer.toString(childMap.size()),audiobean
//						 * );
//						 */
//
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void notifyFTPServerConnected(boolean res, FTPBean bn) {
//		try {
//			Log.d("lg", "MMChat" + res);
//			if (res) {
//				if (!isdownload) {
//					String signalId = Long.toString(utility.getRandomMediaID())
//							.trim();
//					if (bn.getRequest_from().equals("MPP")) {
//						try {
//							Log.d("MM", "Going to upload !!!!");
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("Going to upload !!!!");
//
//							if (bn.getFile_path() != null) {
//								LinearLayout llayPhoto = null;
//								if (bn.getReq_object() instanceof LinearLayout) {
//									llayPhoto = (LinearLayout) bn
//											.getReq_object();
//									llayPhoto.removeView(ivPhoto);
//								}
//								MMBean bean = new MMBean();
//								bean.setImagePath(bn.getFile_path());
//								bean.setSessionId(sessionid);
//								WebServiceReferences.hsMPPBean.put(
//										bean.getFileName(), bean);
//								System.out
//										.println("----------------btnSendPhoto------------------"
//												+ bean.getFileName());
//
//								mmlay.setBackgroundDrawable(null);
//								mmlay.removeAllViews();
//								// Add to ui at the time of click Send
//								String path = bean.getFilePath();
//								String time = time();
//								String touser = buddy;
//
//								ImageChatBean imgbean = new ImageChatBean();
//								imgbean.setBuddyName(touser);
//								imgbean.setChatTime(time);
//								imgbean.setFilePath(path);
//								imgbean.setType(1);
//								imgbean.setFlag(0);
//								imgbean.setSignalId(signalId);
//								imgbean.setUserName(initializer);
//								if (isimported) {
//									imgbean.setisImported(true);
//									isimported = false;
//								}
//								appendChatDatatoXML(imgbean);
//
//								scrollDown();
//								imgbean.setId(childMap.size());
//								Log.d("MIM", "@@@@@@@@@@@ hashmap size..."
//										+ childMap.size());
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.debug("@@@@@@@@@@@ hashmap size..."
//													+ childMap.size());
//								childMap.add(childMap.size(), imgbean);
//								Log.i("imscreen", "===> hs path" + path + "id "
//										+ imgbean.getId());
//								if (AppReference.isWriteInFile)
//									AppReference.logger.debug("===> hs path"
//											+ path + "id " + imgbean.getId());
//
//								chattingList.addView(imageChatFromME(path,
//										time, "upload", touser, 0, 1,
//										imgbean.getId()));
//								if (!isForceLogout
//										&& CallDispatcher.LoginUser != null) {
//									makePhotoPaging(buddy, bean.getFileName(),
//											signalId);
//								}
//
//							}
//						} catch (Exception e) {
//							// TODO: handle exception
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//						}
//					} else if (bn.getRequest_from().equals("MVP")) {
//
//						MMBean obj = new MMBean();
//						obj.setImagePath(bn.getFile_path());
//						obj.setSessionId(sessionid);
//						WebServiceReferences.hsMPPBean.put(obj.getFileName(),
//								obj);
//						System.out
//								.println("----------------btnSendVideo------------------");
//
//						mmlay.setBackgroundDrawable(null);
//						mmlay.removeAllViews();
//
//						String path = obj.getFilePath();
//						String time = time();
//						String touser = buddy;
//
//						VideoChatBean videobean = new VideoChatBean();
//						videobean.setBuddyName(touser);
//						videobean.setChatTime(time());
//						videobean.setFilePath(path);
//						videobean.setType(1);
//						videobean.setFlag(0);
//						videobean.setSignalId(signalId);
//						videobean.setUserName(initializer);
//						if (isimported) {
//							videobean.setisImported(true);
//							isimported = false;
//						}
//						appendChatDatatoXML(videobean);
//
//						scrollDown();
//						videobean.setId(childMap.size());
//						chattingList.addView(VideoChatFromME(path, time,
//								"upload", CallDispatcher.LoginUser, 0, 1,
//								videobean.getId()));
//						Log.d("MIM",
//								"@@@@@@@@@@@ hashmap size..." + childMap.size());
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("@@@@@@@@@@@ hashmap size..."
//											+ childMap.size());
//						childMap.add(childMap.size(), videobean);
//						if (!isForceLogout && CallDispatcher.LoginUser != null) {
//							makevideoPaging(buddy, obj.getFileName(), signalId);
//						}
//
//					} else if (bn.getRequest_from().equals("MAP")) {
//						if (strIPath != null) {
//
//							MMBean obj = new MMBean();
//							obj.setImagePath(strIPath);
//							obj.setSessionId(sessionid);
//
//							WebServiceReferences.hsMPPBean.put(
//									obj.getFileName(), obj);
//							System.out
//									.println("----------------btnSendPhoto------------------"
//											+ obj.getFileName());
//							if (!fromexist) {
//								llayStart.removeAllViews();
//								llayTimer.removeAllViews();
//								mmlay.removeAllViews();
//
//							}
//							// Add to ui at the time of click Send
//							String path = obj.getFilePath();
//							String time = time();
//							String touser = buddy;
//
//							AudioChatBean audiobean = new AudioChatBean();
//							audiobean.setBuddyName(touser);
//							audiobean.setChatTime(time());
//							audiobean.setFilePath(path);
//							audiobean.setType(1);
//							audiobean.setFlag(0);
//							audiobean.setSignalId(signalId);
//							audiobean.setUserName(initializer);
//							if (isimported) {
//								audiobean.setisImported(true);
//								isimported = false;
//							}
//							appendChatDatatoXML(audiobean);
//
//							scrollDown();
//							audiobean.setId(childMap.size());
//							Log.d("MIM", "@@@@@@@@@@@ hashmap size..."
//									+ childMap.size());
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("@@@@@@@@@@@ hashmap size..."
//												+ childMap.size());
//							childMap.add(childMap.size(), audiobean);
//							chattingList.addView(AudioChatFromME(path, time,
//									"upload", touser, 0, 1, audiobean.getId()));
//							if (!isForceLogout
//									&& CallDispatcher.LoginUser != null) {
//
//								MakeAudioPaging(buddy, obj.getFileName(),
//										signalId);
//
//							}
//
//						}
//					} else if (bn.getRequest_from().equals("MHP")) {
//						try {
//
//							if (bn.getFile_path() != null) {
//								LinearLayout llayPhoto = null;
//								if (bn.getReq_object() instanceof LinearLayout) {
//									llayPhoto = (LinearLayout) bn
//											.getReq_object();
//									llayPhoto.removeView(ivPhoto);
//								}
//								MMBean bean = new MMBean();
//								bean.setImagePath(bn.getFile_path());
//								bean.setSessionId(sessionid);
//
//								WebServiceReferences.hsMPPBean.put(
//										bean.getFileName(), bean);
//
//								mmlay.setBackgroundDrawable(null);
//								mmlay.removeAllViews();
//								// Add to ui at the time of click Send
//								String path = bean.getFilePath();
//								String time = time();
//								String touser = buddy;
//
//								ImageChatBean imgbean = new ImageChatBean();
//								imgbean.setBuddyName(touser);
//								imgbean.setChatTime(time);
//								imgbean.setFilePath(path);
//								imgbean.setType(1);
//								imgbean.setFlag(0);
//								imgbean.setSignalId(signalId);
//								imgbean.setUserName(initializer);
//								if (isimported) {
//									imgbean.setisImported(true);
//									isimported = false;
//								}
//								appendChatDatatoXML(imgbean);
//
//								scrollDown();
//								imgbean.setId(childMap.size());
//								Log.d("MIM", "@@@@@@@@@@@ hashmap size..."
//										+ childMap.size());
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.debug("@@@@@@@@@@@ hashmap size..."
//													+ childMap.size());
//								childMap.add(childMap.size(), imgbean);
//								chattingList.addView(imageChatFromME(path,
//										time, "upload", touser, 0, 1,
//										imgbean.getId()));
//								if (!isForceLogout
//										&& CallDispatcher.LoginUser != null) {
//									makeFPPaging(buddy, bean.getFileName(),
//											signalId);
//								}
//
//							}
//						} catch (Exception e) {
//							// TODO: handle exception
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//						}
//					}
//
//				} else
//
//				{
//					isdownload = false;
//					SignalingBean bn1 = null;
//					if (bn.getReq_object() instanceof SignalingBean) {
//						bn1 = (SignalingBean) bn.getReq_object();
//					}
//					bn.setOperation_type(2);
//					bn.setFile_path(bn1.getFilePath());
//					callDisp.getFtpNotifier().addTasktoExecutor(bn);
//
//				}
//			} else {
//				showToast("Connection to the Server was unsuccesfull");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyImageUploaded(boolean response, FTPBean bean) {
//		try {
//			SignalingBean sb = (SignalingBean) bean.getReq_object();
//			String filename = sb.getFilePath();
//			sb.getFrom();
//			sb.getTo();
//			sb.setFilePath(bean.getFilename());
//			Log.e("MAP", "File Name:" + filename + " Upload Response :"
//					+ response);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("File Name:" + filename
//						+ " Upload Response :" + response);
//			System.out.println("----------------upload--------------");
//			if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//				// MMBean obj = WebServiceReferences.hsMPPBean.get(filename);
//				if (response) {
//
//					if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//						Log.i("SEND", "Send User Name:" + sb.getFtpUser());
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("Send User Name:"
//									+ sb.getFtpUser());
//						Log.i("SEND", "Send Password:" + sb.getFtppassword());
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("Send User Name:"
//									+ sb.getFtpUser());
//						if (!callDisp.getdbHeler(context)
//								.isRecordExists(
//										"select * from MMChat where signalid='"
//												+ sb.getSignalid()
//												+ "' and sessionid='"
//												+ sessionid + "'")) {
//							String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//									+ "values('"
//									+ sb.getSessionid()
//									+ "','"
//									+ bean.getFile_path()
//									+ "','"
//									+ sb.getSignalid()
//									+ "','"
//									+ sb.getFilePath() + "')";
//							if (callDisp.getdbHeler(context).ExecuteQuery(qry))
//
//							{
//								Log.i("ary", "entry created...");
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.debug("entry created...");
//							}
//						}
//						String strSignalID = null;
////						if (CallDispatcher.commEngine != null)
////							strSignalID = CallDispatcher.commEngine.makeIM(sb);
//
//						ProgressBar prog = WebServiceReferences.hsIMProgress
//								.get(bean.getFile_path());
//						ImageView imgView = WebServiceReferences.hsIMImageView
//								.get(bean.getFile_path());
//						if (prog != null)
//							WebServiceReferences.hsIMProgress.put(strSignalID,
//									prog);
//						if (imgView != null)
//							WebServiceReferences.hsIMImageView.put(strSignalID,
//									imgView);
//
//						BuddyInformationBean bib = null;
//						bib = WebServiceReferences.buddyList.get(sb.getTo());
//						if (bib.getStatus().equalsIgnoreCase("offline")) {
//							//
//							// prog = WebServiceReferences.hsIMProgress.get(bean
//							// .getFile_path());
//							// prog.setVisibility(View.GONE);
//							// imgView = WebServiceReferences.hsIMImageView
//							// .get(bean.getFile_path());
//							// imgView.setImageResource(R.drawable.chatsending);
//							// imgView.setContentDescription("false");
//						}
//					}
//				} else {
//					// ProgressBar prog = WebServiceReferences.hsIMProgress
//					// .get(bean.getFile_path());
//					// prog.setVisibility(View.GONE);
//					// ImageView imgView = WebServiceReferences.hsIMImageView
//					// .get(bean.getFile_path());
//					// imgView.setImageResource(R.drawable.chatsending);
//					// imgView.setContentDescription("false");
//					Toast.makeText(context, "Upload failed", 1).show();
//					Log.e("up_prog", "Upload failed !!");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Upload failed !!");
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyFPImageUploaded(boolean response, FTPBean bean) {
//
//		try {
//			Log.i("log", "notifyFPImageUploaded");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("notifyFPImageUploaded");
//			SignalingBean sb = (SignalingBean) bean.getReq_object();
//			String filename = sb.getFilePath();
//			sb.getFrom();
//			sb.getTo();
//			sb.setFilePath(bean.getFilename());
//			Log.e("MAP", "File Name:" + filename + " Upload Response :"
//					+ response);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("File Name:" + filename
//						+ " Upload Response :" + response);
//			System.out.println("----------------upload--------------");
//			if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//				// MMBean obj = WebServiceReferences.hsMPPBean.get(filename);
//				if (response) {
//					Log.i("log", "notifyFPImageUploaded inside if");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Send User Name:"
//								+ sb.getFtpUser());
//					if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//						Log.i("SEND", "Send User Name:" + sb.getFtpUser());
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("Send User Name:"
//									+ sb.getFtpUser());
//						Log.i("SEND", "Send Password:" + sb.getFtppassword());
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("Send Password:"
//									+ sb.getFtppassword());
//						if (!callDisp.getdbHeler(context)
//								.isRecordExists(
//										"select * from MMChat where signalid='"
//												+ sb.getSignalid()
//												+ "' and sessionid='"
//												+ sessionid + "'")) {
//							String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//									+ "values('"
//									+ sb.getSessionid()
//									+ "','"
//									+ bean.getFile_path()
//									+ "','"
//									+ sb.getSignalid()
//									+ "','"
//									+ sb.getFilePath() + "')";
//							if (callDisp.getdbHeler(context).ExecuteQuery(qry))
//
//							{
//								Log.i("ary", "entry created...");
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.debug("entry created...");
//							}
//						}
//						String strSignalID = null;
////						if (CallDispatcher.commEngine != null)
////							strSignalID = CallDispatcher.commEngine.makeIM(sb);
//
//						ProgressBar prog = WebServiceReferences.hsIMProgress
//								.get(bean.getFile_path());
//						if (prog != null)
//							WebServiceReferences.hsIMProgress.put(strSignalID,
//									prog);
//
//						Log.e("up_prog", "Signal ID :" + strSignalID);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("Signal ID :"
//									+ strSignalID);
//					}
//				} else {
//					Log.e("up_prog", "Upload failed !!");
//					Log.i("log", "failed");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Upload failed !!");
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyAudioUploaded(boolean response, FTPBean bean) {
//		try {
//			SignalingBean sb = (SignalingBean) bean.getReq_object();
//			String filename = sb.getFilePath();
//			sb.getFrom();
//			sb.getTo();
//			sb.setFilePath(bean.getFilename());
//			Log.e("MAP", "File Name:" + filename);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("File Name:" + filename);
//			System.out.println("----------------upload--------------");
//			if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//				WebServiceReferences.hsMPPBean.get(filename);
//				if (response) {
//					// showToast("File Sent");
//					Log.i("SEND", "Send User Name:" + sb.getFtpUser());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Send User Name:"
//								+ sb.getFtpUser());
//					Log.i("SEND", "Send Password:" + sb.getFtppassword());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Send Password:"
//								+ sb.getFtppassword());
//					if (!callDisp.getdbHeler(context).isRecordExists(
//							"select * from MMChat where signalid='"
//									+ sb.getSignalid() + "' and sessionid='"
//									+ sessionid + "'")) {
//						String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//								+ "values('"
//								+ sb.getSessionid()
//								+ "','"
//								+ bean.getFile_path()
//								+ "','"
//								+ sb.getSignalid()
//								+ "','"
//								+ sb.getFilePath()
//								+ "')";
//						if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//							Log.i("ary", "entry created...");
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("entry created...");
//						}
//					}
//					String strSignalID = null;
////					if (CallDispatcher.commEngine != null)
////						strSignalID = CallDispatcher.commEngine.makeIM(sb);
//					ProgressBar prog = WebServiceReferences.hsIMProgress
//							.get(bean.getFile_path());
//					ImageView imgView = WebServiceReferences.hsIMImageView
//							.get(bean.getFile_path());
//
//					if (prog != null)
//						WebServiceReferences.hsIMProgress
//								.put(strSignalID, prog);
//					if (imgView != null)
//						WebServiceReferences.hsIMImageView.put(strSignalID,
//								imgView);
//
//					BuddyInformationBean bib = null;
//					bib = WebServiceReferences.buddyList.get(sb.getTo());
//					if (bib.getStatus().equalsIgnoreCase("offline")) {
//
//						prog = WebServiceReferences.hsIMProgress.get(bean
//								.getFile_path());
//						prog.setVisibility(View.GONE);
//						imgView = WebServiceReferences.hsIMImageView.get(bean
//								.getFile_path());
//						imgView.setImageResource(R.drawable.chatsending);
//						imgView.setContentDescription("false");
//					}
//
//				} else {
//					ProgressBar prog = WebServiceReferences.hsIMProgress
//							.get(bean.getFile_path());
//					prog.setVisibility(View.GONE);
//					ImageView imgView = WebServiceReferences.hsIMImageView
//							.get(bean.getFile_path());
//					imgView.setImageResource(R.drawable.chatsending);
//					imgView.setContentDescription("false");
//					Toast.makeText(context, "Upload failed", 1).show();
//					Log.e("up_prog", "Upload failed !!");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Upload failed !!");
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyVideoUploaded(boolean response, FTPBean bean) {
//		try {
//			SignalingBean sb = (SignalingBean) bean.getReq_object();
//			String filename = sb.getFilePath();
//			sb.getFrom();
//			sb.getTo();
//			sb.setFilePath(bean.getFilename());
//			Log.e("MAP", "File Name:" + filename);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("File Name:" + filename);
//			Log.i("thread", "came to video uploaded");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("came to video uploaded");
//			Log.i("thread", "came to video uploaded"
//					+ WebServiceReferences.hsMPPBean.containsKey(filename));
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("came to video uploaded"
//						+ WebServiceReferences.hsMPPBean.containsKey(filename));
//			Log.i("thread", "came to video uploaded response" + response);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("came to video uploaded response"
//						+ response);
//			System.out.println("----------------upload--------------");
//			if (WebServiceReferences.hsMPPBean.containsKey(filename)) {
//				WebServiceReferences.hsMPPBean.get(filename);
//				if (response) {
//
//					// showToast("File Sent");
//					// Log.i("SEND", "Send User Name:" + sb.getFtpUser());
//					// Log.i("SEND", "Send Password:" + sb.getFtppassword());
//					//
//					// CallDispatcher.commEngine.makeIM(sb);
//
//					Log.i("SEND", "Send User Name:" + sb.getFtpUser());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Send Password:"
//								+ sb.getFtppassword());
//					Log.i("SEND", "Send Password:" + sb.getFtppassword());
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Send Password:"
//								+ sb.getFtppassword());
//					if (!callDisp.getdbHeler(context).isRecordExists(
//							"select * from MMChat where signalid='"
//									+ sb.getSignalid() + "' and sessionid='"
//									+ sessionid + "'")) {
//						String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//								+ "values('"
//								+ sb.getSessionid()
//								+ "','"
//								+ bean.getFile_path()
//								+ "','"
//								+ sb.getSignalid()
//								+ "','"
//								+ sb.getFilePath()
//								+ "')";
//						if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//							Log.i("ary", "entry created...");
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("entry created...");
//						}
//					}
//
//					String strSignalID = null;
////					if (CallDispatcher.commEngine != null)
////						strSignalID = CallDispatcher.commEngine.makeIM(sb);
//
//					ProgressBar prog = WebServiceReferences.hsIMProgress
//							.get(bean.getFile_path());
//					ImageView imgView = WebServiceReferences.hsIMImageView
//							.get(bean.getFile_path());
//
//					if (prog != null)
//						WebServiceReferences.hsIMProgress
//								.put(strSignalID, prog);
//					if (imgView != null)
//						WebServiceReferences.hsIMImageView.put(strSignalID,
//								imgView);
//					BuddyInformationBean bib = null;
//					bib = WebServiceReferences.buddyList.get(sb.getTo());
//					if (bib.getStatus().equalsIgnoreCase("offline")) {
//
//						prog = WebServiceReferences.hsIMProgress.get(bean
//								.getFile_path());
//						prog.setVisibility(View.GONE);
//						imgView = WebServiceReferences.hsIMImageView.get(bean
//								.getFile_path());
//						imgView.setImageResource(R.drawable.chatsending);
//						imgView.setContentDescription("false");
//					}
//
//				} else {
//					ProgressBar prog = WebServiceReferences.hsIMProgress
//							.get(bean.getFile_path());
//					prog.setVisibility(View.GONE);
//					ImageView imgView = WebServiceReferences.hsIMImageView
//							.get(bean.getFile_path());
//					imgView.setImageResource(R.drawable.chatsending);
//					imgView.setContentDescription("false");
//					Toast.makeText(context, "Upload failed", 1).show();
//					Log.e("up_prog", "Upload failed !!");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("Upload failed !!");
//				}
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void notifyImageDownloaded(boolean response, FTPBean bean,
//			String message) {
//
//		try {
//			if (response) {
//				SignalingBean sb = null;
//				if (bean.getReq_object() instanceof SignalingBean) {
//					sb = (SignalingBean) bean.getReq_object();
//					sb.setFilePath(bean.getFile_path());
//				}
//				Log.e("IM", "notifyImageDownloaded");
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("notifyImageDownloaded");
//				imageChatFromMYBuddy(buddy, time(), "Not Yet Get", sb);
//				Log.e("thread", "came o notification" + sb.getisRobo());
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("came o notification"
//							+ sb.getisRobo());
//
//			} else {
//				ShowError("Download Failure", message);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void notifyFPDownloaded(boolean response, FTPBean bean,
//			String message) {
//
//		try {
//			if (response) {
//				SignalingBean sb = null;
//				if (bean.getReq_object() instanceof SignalingBean) {
//					sb = (SignalingBean) bean.getReq_object();
//					sb.setFilePath(bean.getFile_path());
//				}
//
//				imageChatFromMYBuddy(buddy, time(), "Not Yet Get", sb);
//
//			} else {
//				ShowError("Download Failure", message);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void notifyAudioDownloaded(boolean response, FTPBean bean,
//			String message) {
//		try {
//			if (response) {
//				SignalingBean sb = null;
//				if (bean.getReq_object() instanceof SignalingBean) {
//					sb = (SignalingBean) bean.getReq_object();
//					sb.setFilePath(bean.getFile_path());
//				}
//				AudioChatFromBuddy(buddy, time(), "Not Yet Get", sb);
//
//			} else {
//				ShowError("Download Failure", message);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void notifyVideoDownloaded(boolean response, FTPBean bean,
//			String message) {
//		try {
//			if (response) {
//				SignalingBean sb = null;
//				if (bean.getReq_object() instanceof SignalingBean) {
//					sb = (SignalingBean) bean.getReq_object();
//					sb.setFilePath(bean.getFile_path());
//				}
//				videoChatFromBuddy(buddy, time(), "Not Yet Get", sb);
//
//			} else {
//				ShowError("Download Failure", message);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		try {
//			// TODO Auto-generated method stub
//
//			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputManager.hideSoftInputFromWindow(myMessage.getWindowToken(), 0);
//			super.onPause();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyIM(String Message, String GetFrom) {
//		try {
//			if (callDisp.isApplicationInBackground(context)) {
//				if (callDisp.notifier == null) {
//					callDisp.notifier = new BackgroundNotification(
//							InstantMessageScreen.this);
//					if (WebServiceReferences.Imcollection.size() == 1) {
//						callDisp.notifier.ShowNotification(Message, GetFrom
//								+ ":" + WebServiceReferences.Imcont, "im");
//					} else {
//						callDisp.notifier
//								.ShowNotification(Message, "Conversations :"
//										+ WebServiceReferences.Imcont, "im");
//					}
//					WebServiceReferences.Imcont += 1;
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		try {
//			// TODO Auto-generated method stub
//			if (keyCode == KeyEvent.KEYCODE_BACK) {
//				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.hideSoftInputFromWindow(
//						myMessage.getWindowToken(), 0);
//				if (AppReference.issipchatinitiated)
//					showcancelAlert();
//			}
//			return super.onKeyDown(keyCode, event);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			return false;
//		}
//	}
//
//	private void showcancelAlert() {
//
//		try {
//			AlertDialog.Builder buider = new AlertDialog.Builder(context);
//			buider.setMessage(
//					"Quick audio recording is going on,do you want to stop is now?")
//					.setPositiveButton("Stop Recording",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
////									if (AppReference.issipchatinitiated) {
////										if (AppReference.chatcall_id != -1) {
////											CommunicationBean bean1 = new CommunicationBean();
////											bean1.setOperationType(sip_operation_types.HANGUP_SINGLE_CALL);
////											bean1.setCall_id(AppReference.chatcall_id);
////											AppReference.sipQueue.addMsg(bean1);
////										}
////
////									}
//									finish();
//
//								}
//							})
//					.setNegativeButton("Cancel",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//									dialog.cancel();
//								}
//							});
//			AlertDialog alert = buider.create();
//			alert.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//
//	}
//
//	public void ShowError(String Title, String Message) {
//		try {
//			if (Title
//					.equals("Some other user with same user name has logged in")) {
//				isForceLogout = true;
//			}
//
//			AlertDialog confirmation = new AlertDialog.Builder(this).create();
//			confirmation.setTitle(Title);
//			confirmation.setMessage(Message);
//			confirmation.setCancelable(true);
//			confirmation.setButton("OK", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//
//			confirmation.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//		}
//	}
//
//	public void notifyRoboDownloaded(final String path) {
//		updateHandler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					if (roboMap.size() > 0) {
//						Log.d("thread",
//								"************************************ came to imtabs");
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.debug("************************************ came to imtabs");
//						Set set = roboMap.keySet();
//						Iterator<String> itr = set.iterator();
//						while (itr.hasNext()) {
//							String key = itr.next();
//							Log.e("thread", "@@@@@@@@@@@@@@@@@@@" + key);
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("@@@@@@@@@@@@@@@@@@@2" + key);
//							Log.e("thread", "@@@@@@@@@@@@@@@@@@@" + path);
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("@@@@@@@@@@@@@@@@@@@2" + path);
//							String[] arr = key.split(" ");
//							Log.e("thread", "@@@@@@@@@@@@@@@@@@@" + arr[0]);
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("@@@@@@@@@@@@@@@@@@@2" + arr[0]);
//							Log.e("thread", "@@@@@@@@@@@@@@@@@@@" + arr[1]);
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.debug("@@@@@@@@@@@@@@@@@@@2" + arr[1]);
//							if (arr[1].trim().equals(path.trim())) {
//								Log.e("thread", "@@@@@@@@@@@@@@@@@@@1");
//								Object obj = childMap.get(Integer
//										.parseInt(arr[0]) - 1);
//								Log.e("thread", "@@@@@@@@@@@@@@@@@@@2" + obj);
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.debug("@@@@@@@@@@@@@@@@@@@2" + obj);
//								if (obj instanceof ImageChatBean) {
//									ImageChatBean bn = (ImageChatBean) obj;
//									chattingList.removeViewAt(Integer
//											.parseInt(arr[0]) - 1);
//									chattingList.addView(
//											imageChatFromME(bn.getFilePath(),
//													bn.getChatTime(), "",
//													bn.getBuddyName(), 0, 1,
//													bn.getId()), Integer
//													.parseInt(arr[0]) - 1);
//								} else if (obj instanceof AudioChatBean) {
//									Log.e("thread",
//											"@@@@@@@@@@@@@@@@@@@ con sat");
//									AudioChatBean bn = (AudioChatBean) obj;
//									chattingList.removeViewAt(Integer
//											.parseInt(arr[0]) - 1);
//									chattingList.addView(
//											AudioChatFromME(bn.getFilePath(),
//													bn.getChatTime(), "",
//													bn.getBuddyName(), 0, 1,
//													bn.getId()), Integer
//													.parseInt(arr[0]) - 1);
//								} else if (obj instanceof VideoChatBean) {
//									VideoChatBean bn = (VideoChatBean) obj;
//									chattingList.removeViewAt(Integer
//											.parseInt(arr[0]) - 1);
//									chattingList.addView(
//											VideoChatFromME(bn.getFilePath(),
//													bn.getChatTime(), "",
//													CallDispatcher.LoginUser,
//													0, 1, bn.getId()), Integer
//													.parseInt(arr[0]) - 1);
//								}
//							}
//							roboMap.remove(path);
//						}
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//					Log.d("thread",
//							"************************************ came to imtabs exec"
//									+ e.toString());
//					if (AppReference.isWriteInFile)
//						AppReference.logger
//								.debug("************************************ came to imtabs exec"
//										+ e.toString());
//				}
//			}
//		});
//
//	}
//
//	public void MakeCall(int operation) {
//		try {
//			BuddyInformationBean bib = null;
//			if (buddyStatus.getText().toString() != null
//					&& buddyStatus.getText().toString()
//							.equalsIgnoreCase("Offline")
//					|| buddyStatus.getText().toString().equals("Stealth")
//					|| buddyStatus.getText().toString()
//							.equalsIgnoreCase("pending")
//					|| buddyStatus.getText().toString()
//							.equalsIgnoreCase("Virtual")
//					|| buddyStatus.getText().toString()
//							.equalsIgnoreCase("airport")) {
//				if (WebServiceReferences.running) {
//					CallDispatcher.pdialog = new ProgressDialog(context);
//					callDisp.showprogress(CallDispatcher.pdialog, context);
//
//					String[] res_info = new String[3];
//					res_info[0] = CallDispatcher.LoginUser;
//					res_info[1] = buddyName.getText().toString();
//					if (buddyStatus.getText().toString().equals("Offline")
//							|| buddyStatus.getText().toString()
//									.equals("Stealth"))
//						res_info[2] = callDisp
//								.getdbHeler(context)
//								.getwheninfo(
//										"select cid from clonemaster where cdescription='Offline'");
//					else
//						res_info[2] = "";
//
//					WebServiceReferences.webServiceClient
//							.OfflineCallResponse(res_info);
//				}
//
//			} else {
//				if (!buddyStatus.getText().toString()
//						.equalsIgnoreCase("pending")) {
//
//					switch (operation) {
//					case 1:
//
//						bib = WebServiceReferences.buddyList.get(buddy);
//						CallDispatcher.sb = new SignalingBean();
//						Log.e("ACal", "user (Who is log on) "
//								+ CallDispatcher.LoginUser);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("user (Who is log on) "
//									+ CallDispatcher.LoginUser);
//						// sb=new SignalingBean();
//						CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
//
//						CallDispatcher.sb.setTo(buddy);
//
//						CallDispatcher.sb.setType("0");
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//						CallDispatcher.sb.setResult("0");
//						CallDispatcher.sb.setTopublicip(bib
//								.getExternalipaddress());
//						CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//						CallDispatcher.sb.setCallType("AC");
//
//						CallDispatcher.dialChecker = true;
//
//						callDisp.ShowConnectionScreen(
//								(SignalingBean) CallDispatcher.sb.clone(),
//								buddy, context, false);
//
//						CallDispatcher.isCallInitiate = true;
////						CallDispatcher.commEngine.makeCall(CallDispatcher.sb);
//
//						break;
//
//					case 2:
//
//						bib = WebServiceReferences.buddyList.get(buddy);
//						CallDispatcher.sb = new SignalingBean();
//						Log.d("call", "user " + CallDispatcher.LoginUser);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("User"
//									+ CallDispatcher.LoginUser);
//						// sb=new SignalingBean();
//						CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
//
//						CallDispatcher.sb.setTo(buddy);
//
//						CallDispatcher.sb.setType("0");
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//						CallDispatcher.sb.setResult("0");
//						CallDispatcher.sb.setTopublicip(bib
//								.getExternalipaddress());
//						CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//
//						// dialogBuddySelectionn.dismiss();
//						// ....................
//
//						CallDispatcher.sb.setCallType("VC");
//
//						callDisp.ShowConnectionScreen(
//								(SignalingBean) CallDispatcher.sb.clone(),
//								buddy, context, false);
//
//						CallDispatcher.isCallInitiate = true;
////						CallDispatcher.commEngine.makeCall(CallDispatcher.sb);
//
//						break;
//
//					case 3:
//
//						bib = WebServiceReferences.buddyList.get(buddy);
//						CallDispatcher.sb = new SignalingBean();
//						Log.d("Paging", "user " + CallDispatcher.LoginUser);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("User"
//									+ CallDispatcher.LoginUser);
//						// sb=new SignalingBean();
//						CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
//
//						CallDispatcher.sb.setTo(buddy);
//
//						CallDispatcher.sb.setType("0");
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//						CallDispatcher.sb.setResult("0");
//						CallDispatcher.sb.setTopublicip(bib
//								.getExternalipaddress());
//						CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//
//						// dialogBuddySelectionn.dismiss();
//						// ....................
//
//						CallDispatcher.sb.setCallType("AP");
//
//						CallDispatcher.dialChecker = true;
//
//						callDisp.ShowConnectionScreen(
//								(SignalingBean) CallDispatcher.sb.clone(),
//								buddy, context, false);
//
//						CallDispatcher.isCallInitiate = true;
////						CallDispatcher.commEngine.makeCall(CallDispatcher.sb);
//
//						break;
//
//					case 4:
//
//						bib = WebServiceReferences.buddyList.get(buddy);
//						CallDispatcher.sb = new SignalingBean();
//						Log.d("call", "user " + CallDispatcher.LoginUser);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("user"
//									+ CallDispatcher.LoginUser);
//						// sb=new SignalingBean();
//						CallDispatcher.sb.setFrom(CallDispatcher.LoginUser);
//
//						CallDispatcher.sb.setTo(buddy);
//
//						CallDispatcher.sb.setType("0");
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//						CallDispatcher.sb.setResult("0");
//						CallDispatcher.sb.setTopublicip(bib
//								.getExternalipaddress());
//						CallDispatcher.sb.setTolocalip(bib.getLocalipaddress());
//						CallDispatcher.sb.setToSignalPort(bib
//								.getSignalingPort());
//
//						// dialogBuddySelectionn.dismiss();
//						// ....................
//
//						CallDispatcher.sb.setCallType("VP");
//
//						CallDispatcher.dialChecker = true;
//
//						callDisp.ShowConnectionScreen(
//								(SignalingBean) CallDispatcher.sb.clone(),
//								buddy, context, false);
//
//						CallDispatcher.isCallInitiate = true;
////						CallDispatcher.commEngine.makeCall(CallDispatcher.sb);
//
//						break;
//
//					default:
//
//						break;
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void resend(Object bean, int id) {
//
//		try {
//			Log.i("lgg", "################# bean" + bean);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("################# bean" + bean);
//			Log.i("lgg", "################# id" + id);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("################# id" + id);
//			if (bean instanceof TextChatBean) {
//				TextChatBean tbean = (TextChatBean) bean;
//				BuddyInformationBean bib = null;
//				bib = WebServiceReferences.buddyList.get(buddy);
//
//				SignalingBean sb = new SignalingBean();
//				sb.setFrom(CallDispatcher.LoginUser);
//				sb.setTolocalip(bib.getLocalipaddress());
//				Log.d("imscreen", bib.getLocalipaddress());
//				sb.setTopublicip(bib.getExternalipaddress());
//				Log.d("imscreen", bib.getExternalipaddress());
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug(bib.getExternalipaddress());
//				sb.setTo(buddy);
//				sb.setType("11");
//				sb.setSessionid(sessionid);
//				Log.d("imscreen", "cc" + sessionid);
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("cc" + sessionid);
//				sb.setResult("0");
//				sb.setCallType("MTP");
//				sb.setisRobo("");
//				sb.setSignalid(tbean.getSignalId());
//				sb.setMessage(tbean.getMessage());
//				sb.setToSignalPort(bib.getSignalingPort());
//				sb.setConferencemember("");
//				Log.d("imscreen", "if finished");
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("if finished");
////				CallDispatcher.commEngine.makeIM(sb);
//				chattingList.removeViewAt(id);
//				chattingList.addView(
//						imFromME(tbean.getMessage(), tbean.getChatTime(),
//								tbean.getSignalId(), 1, true, id), id);
//
//			} else if (bean instanceof ImageChatBean) {
//				ImageChatBean ibean = (ImageChatBean) bean;
//				BuddyInformationBean bib = null;
//				bib = WebServiceReferences.buddyList.get(buddy);
//				int viewmode = 0;
//				if (callDisp.getdbHeler(context).isRecordExists(
//						"select * from MMChat where signalid='"
//								+ ibean.getSignalId() + "' and sessionid='"
//								+ sessionid + "'")) {
//
//					chattingList.removeViewAt(ibean.getId());
//					chattingList.addView(
//							imageChatFromME(ibean.getFilePath(),
//									ibean.getChatTime(), "upload", buddy,
//									viewmode, 1, ibean.getId()), ibean.getId());
//
//					String ftppath = callDisp.getdbHeler(context).getFtppath(
//							ibean.getSignalId(), sessionid);
//					if (ftppath != null) {
//
//						SignalingBean sb = new SignalingBean();
//						sb.setFrom(CallDispatcher.LoginUser);
//						sb.setTo(buddy);
//						sb.setType("11");
//						sb.setToSignalPort(bib.getSignalingPort());
//						sb.setResult("0");
//						sb.setTopublicip(bib.getExternalipaddress());
//						sb.setTolocalip(bib.getLocalipaddress());
//						sb.setToSignalPort(bib.getSignalingPort());
//						if (ibean.getIsRobo()) {
//							sb.setisRobo("yes");
//						} else {
//							sb.setisRobo("");
//						}
//						sb.setCallType("MPP");
//						sb.setFileId("007");
//						sb.setFilePath(ftppath);
//						sb.setSessionid(sessionid);
//						sb.setSignalid(ibean.getSignalId());
//						sb.setMessage("");
//						sb.setFtpUser(CallDispatcher.LoginUser);
//						sb.setFtppassword(CallDispatcher.Password);
//						if (groupmode) {
//							sb.setConferencemember(getMembers()
//									+ CallDispatcher.LoginUser);
//						}
//
//						ProgressBar prog = WebServiceReferences.hsIMProgress
//								.get(ibean.getFilePath());
//						if (prog != null) {
//							WebServiceReferences.hsIMProgress.put(
//									ibean.getSignalId(), prog);
//
//							Log.e("up_prog",
//									"Signal ID :" + ibean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ ibean.getSignalId());
//						}
//						ImageView imgView = WebServiceReferences.hsIMImageView
//								.get(ibean.getFilePath());
//						if (imgView != null) {
//							WebServiceReferences.hsIMImageView.put(
//									ibean.getSignalId(), imgView);
//
//							Log.e("up_prog",
//									"Signal ID :" + ibean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ ibean.getSignalId());
//						}
//
////						callDisp.commEngine.makeIM(sb);
//
//						/* makePhotoPaging(buddy, bean.getFileName(),signalId); */
//					} else {
//						MMBean mbean = new MMBean();
//						mbean.setImagePath(ibean.getFilePath());
//						mbean.setSessionId(sessionid);
//						// Random rm = new Random();
//						// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//						//
//						System.out.println("*********************"
//								+ ibean.getFilePath()
//								+ "**********************");
//						System.out.println("*********************"
//								+ mbean.getFileName()
//								+ "**********************");
//						WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//								mbean);
//
//						makePhotoPaging(buddy, mbean.getFileName(),
//								ibean.getSignalId());
//					}
//				} else {
//					chattingList.removeViewAt(ibean.getId());
//					chattingList.addView(imageChatFromME(ibean.getFilePath(),
//							ibean.getChatTime(), "upload", buddy, viewmode, 1,
//							ibean.getId()));
//
//					MMBean mbean = new MMBean();
//					mbean.setImagePath(ibean.getFilePath());
//					mbean.setSessionId(sessionid);
//					// Random rm = new Random();
//					// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//					//
//
//					System.out.println("*********************"
//							+ ibean.getFilePath() + "**********************");
//					WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//							mbean);
//
//					makePhotoPaging(buddy, mbean.getFileName(),
//							ibean.getSignalId());
//				}
//			}
//
//			else if (bean instanceof AudioChatBean) {
//				AudioChatBean abean = (AudioChatBean) bean;
//				BuddyInformationBean bib = null;
//				bib = WebServiceReferences.buddyList.get(buddy);
//				int viewmode = 0;
//				if (callDisp.getdbHeler(context).isRecordExists(
//						"select * from MMChat where signalid='"
//								+ abean.getSignalId() + "' and sessionid='"
//								+ sessionid + "'")) {
//					chattingList.removeViewAt(abean.getId());
//					chattingList.addView(
//							AudioChatFromME(abean.getFilePath(),
//									abean.getChatTime(), "upload", buddy,
//									viewmode, 1, abean.getId()), abean.getId());
//
//					String ftppath = callDisp.getdbHeler(context).getFtppath(
//							abean.getSignalId(), sessionid);
//					if (ftppath != null) {
//						SignalingBean sb = new SignalingBean();
//						sb.setFrom(CallDispatcher.LoginUser);
//						sb.setTo(buddy);
//						sb.setType("11");
//						sb.setToSignalPort(bib.getSignalingPort());
//						sb.setResult("0");
//						sb.setTopublicip(bib.getExternalipaddress());
//						sb.setTolocalip(bib.getLocalipaddress());
//						sb.setToSignalPort(bib.getSignalingPort());
//						if (abean.getIsRobo()) {
//							sb.setisRobo("yes");
//						} else {
//							sb.setisRobo("");
//						}
//						sb.setCallType("MAP");
//						sb.setFileId("007");
//						sb.setFilePath(ftppath);
//						sb.setSessionid(sessionid);
//						sb.setSignalid(abean.getSignalId());
//						sb.setMessage("");
//						sb.setFtpUser(CallDispatcher.LoginUser);
//						sb.setFtppassword(CallDispatcher.Password);
//						if (groupmode) {
//							sb.setConferencemember(getMembers()
//									+ CallDispatcher.LoginUser);
//						}
//
//						ProgressBar prog = WebServiceReferences.hsIMProgress
//								.get(abean.getFilePath());
//						if (prog != null) {
//							WebServiceReferences.hsIMProgress.put(
//									abean.getSignalId(), prog);
//
//							Log.e("up_prog",
//									"Signal ID :" + abean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ abean.getSignalId());
//						}
//						ImageView imgView = WebServiceReferences.hsIMImageView
//								.get(abean.getFilePath());
//						if (imgView != null) {
//							WebServiceReferences.hsIMImageView.put(
//									abean.getSignalId(), imgView);
//
//							Log.e("up_prog",
//									"Signal ID :" + abean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ abean.getSignalId());
//
//						}
////						callDisp.commEngine.makeIM(sb);
//
//						/* makePhotoPaging(buddy, bean.getFileName(),signalId); */
//					} else {
//						MMBean mbean = new MMBean();
//						mbean.setImagePath(abean.getFilePath());
//						mbean.setSessionId(sessionid);
//						// Random rm = new Random();
//						// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//						//
//						WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//								mbean);
//						MakeAudioPaging(buddy, mbean.getFileName(),
//								abean.getSignalId());
//					}
//				} else {
//					chattingList.removeViewAt(abean.getId());
//					chattingList.addView(
//							AudioChatFromME(abean.getFilePath(),
//									abean.getChatTime(), "upload", buddy,
//									viewmode, 1, abean.getId()), abean.getId());
//
//					MMBean mbean = new MMBean();
//					mbean.setImagePath(abean.getFilePath());
//					mbean.setSessionId(sessionid);
//					// Random rm = new Random();
//					// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//					//
//					WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//							mbean);
//					MakeAudioPaging(buddy, mbean.getFileName(),
//							abean.getSignalId());
//				}
//			} else if (bean instanceof VideoChatBean) {
//				VideoChatBean vbean = (VideoChatBean) bean;
//				BuddyInformationBean bib = null;
//				bib = WebServiceReferences.buddyList.get(buddy);
//				int viewmode = 0;
//				if (callDisp.getdbHeler(context).isRecordExists(
//						"select * from MMChat where signalid='"
//								+ vbean.getSignalId() + "' and sessionid='"
//								+ sessionid + "'")) {
//					chattingList.removeViewAt(vbean.getId());
//					chattingList.addView(
//							VideoChatFromME(vbean.getFilePath(),
//									vbean.getChatTime(), "upload", buddy,
//									viewmode, 1, vbean.getId()), vbean.getId());
//
//					String ftppath = callDisp.getdbHeler(context).getFtppath(
//							vbean.getSignalId(), sessionid);
//					if (ftppath != null) {
//						SignalingBean sb = new SignalingBean();
//						sb.setFrom(CallDispatcher.LoginUser);
//						sb.setTo(buddy);
//						sb.setType("11");
//						sb.setToSignalPort(bib.getSignalingPort());
//						sb.setResult("0");
//						sb.setTopublicip(bib.getExternalipaddress());
//						sb.setTolocalip(bib.getLocalipaddress());
//						sb.setToSignalPort(bib.getSignalingPort());
//						if (vbean.getIsRobo()) {
//							sb.setisRobo("yes");
//						} else {
//							sb.setisRobo("");
//						}
//						sb.setCallType("MVP");
//						sb.setFileId("007");
//						sb.setFilePath(ftppath);
//						sb.setSessionid(sessionid);
//						sb.setSignalid(vbean.getSignalId());
//						sb.setMessage("");
//						sb.setFtpUser(CallDispatcher.LoginUser);
//						sb.setFtppassword(CallDispatcher.Password);
//						if (groupmode) {
//							sb.setConferencemember(getMembers()
//									+ CallDispatcher.LoginUser);
//						}
//						ProgressBar prog = WebServiceReferences.hsIMProgress
//								.get(vbean.getFilePath());
//						if (prog != null) {
//							WebServiceReferences.hsIMProgress.put(
//									vbean.getSignalId(), prog);
//
//							Log.e("up_prog",
//									"Signal ID :" + vbean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ vbean.getSignalId());
//						}
//
//						ImageView imgView = WebServiceReferences.hsIMImageView
//								.get(vbean.getFilePath());
//						if (imgView != null) {
//							WebServiceReferences.hsIMImageView.put(
//									vbean.getSignalId(), imgView);
//
//							Log.e("up_prog",
//									"Signal ID :" + vbean.getSignalId());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("Signal ID :"
//										+ vbean.getSignalId());
//						}
////						callDisp.commEngine.makeIM(sb);
//
//						/* makePhotoPaging(buddy, bean.getFileName(),signalId); */
//					} else {
//						MMBean mbean = new MMBean();
//						mbean.setImagePath(vbean.getFilePath());
//						mbean.setSessionId(sessionid);
//						// Random rm = new Random();
//						// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//						//
//						WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//								mbean);
//						makevideoPaging(buddy, mbean.getFileName(),
//								vbean.getSignalId());
//					}
//				} else {
//					chattingList.removeViewAt(vbean.getId());
//					chattingList.addView(
//							VideoChatFromME(vbean.getFilePath(),
//									vbean.getChatTime(), "upload", buddy,
//									viewmode, 1, vbean.getId()), vbean.getId());
//
//					MMBean mbean = new MMBean();
//					mbean.setImagePath(vbean.getFilePath());
//					mbean.setSessionId(sessionid);
//					// Random rm = new Random();
//					// bean.setUploadId(Integer.toString(rm.nextInt(10000)));
//					//
//					WebServiceReferences.hsMPPBean.put(mbean.getFileName(),
//							mbean);
//					makevideoPaging(buddy, mbean.getFileName(),
//							vbean.getSignalId());
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		try {
//			switch (item.getItemId()) {
//			case R.id.photo:
//				photochat();
//				return true;
//			case R.id.audio:
//				// TODO Auto-generated method stub
//				CompleteListView.checkDir();
//				Log.d("IM", "audio Event Triggered");
//				if (AppReference.isWriteInFile)
//					AppReference.logger.debug("audio Event Triggered");
//
//				showMessageDialog1();
//
//				return true;
//			case R.id.video:
//				vodeoChat();
//				return true;
//			case R.id.handsketch:
//				CompleteListView.checkDir();
//				ph_au_Sender("Sketch");
//				return true;
//			case R.id.location:
//				if (CallDispatcher.LoginUser != null) {
//					if (CallDispatcher.latitude == 0.0
//							&& CallDispatcher.longitude == 0.0) {
//						showToast("Sorry! Turn On Location Service ");
//					} else {
//						sendTextMsg("Latitude:" + CallDispatcher.latitude + ","
//								+ "Longitude:" + CallDispatcher.longitude, "");
//						scrollDown();
//					}
//
//				} else {
//					showToast("Sorry! can not send Message");
//				}
//				return true;
//			case R.id.sip:
//				try {
//					BuddyInformationBean bean = (BuddyInformationBean) WebServiceReferences.buddyList
//							.get(buddy);
//					Log.d("lmcm", "my buddy bean----->" + bean
//							+ "***** budy name********" + buddy);
//					if (AppReference.isWriteInFile)
//						AppReference.logger.debug("my buddy bean----->" + bean
//								+ "***** budy name********" + buddy);
//					if (bean != null) {
//						if (!bean.getStatus().equalsIgnoreCase("offline")
//								&& !CallDispatcher.myStatus
//										.equalsIgnoreCase("0"))
//							if (AppReference.sip_accountID != -1)
//								audioChat();
//							else
//								showToast("Please check your SIP presence before make call");
//						else {
//							if (bean.getStatus().equalsIgnoreCase("offline"))
//								showToast("Sorry buddy is not in online");
//							else
//								showToast("Sorry you are not in online");
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//					return false;
//				}
//
//				return true;
//			}
//			return false;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		try {
//			super.onCreateOptionsMenu(menu);
//			MenuInflater inflater = getMenuInflater();
//			inflater.inflate(R.menu.menus, menu);
//			return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public void putxmlobj(String key, Object obj) {
//		try {
//			if (xmlmap.containsKey(key)) {
//				xmlmap.remove(key);
//				xmlmap.put(key, obj);
//			} else {
//				xmlmap.put(key, obj);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//
//		}
//	}
//
//	void audioChat() {
//
//		try {
//			// TODO Auto-generated method stub
//
//			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//
//			if (!isrecording) {
//				if (!isAudioPlaying) {
//					if (!isvideoplay) {
//
//						InputMethodManager imm1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//						imm1.hideSoftInputFromWindow(getCurrentFocus()
//								.getWindowToken(), 0);
//						// audio.setEnabled(true);
//
//						Log.i("record", "sip chat staus----->"
//								+ AppReference.issipchatinitiated);
//						if (AppReference.isWriteInFile)
//							AppReference.logger.debug("sip chat staus----->"
//									+ AppReference.issipchatinitiated);
//						if (!AppReference.issipchatinitiated) {
//							String directory_path = Environment
//									.getExternalStorageDirectory()
//									+ "/COMMedia/";
//							File dir = new File(directory_path);
//							if (!dir.exists())
//								dir.mkdir();
//
//							record_path = CompleteListView.getFileName();
//							strIPath = directory_path + record_path + ".wav";
//
//							Log.i("call1234", "record path---->" + record_path);
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("record path---->"
//										+ record_path);
//							Log.i("call1234", "file  path---->" + strIPath);
//							if (AppReference.isWriteInFile)
//								AppReference.logger.debug("file  path---->"
//										+ strIPath);
//
////							CommunicationBean call_bean = new CommunicationBean();
////							call_bean.setFilePath(strIPath);
////							call_bean
////									.setOperationType(sip_operation_types.SET_RECORDFILE);
////							AppReference.sipQueue.addMsg(call_bean);
////
////							AppReference.issipchatinitiated = true;
////
////							CommunicationBean bean = new CommunicationBean();
////							bean.setOperationType(sip_operation_types.MAKE_CALL);
////							bean.setAcc_id(AppReference.sip_accountID);
////							bean.setRealm(callDisp.getFS());
////							bean.setTonumber(buddy);
////							AppReference.sipQueue.addMsg(bean);
////							RecordSipaudiochat("Audiochat");
////							AppReference.sipcahtContext = InstantMessageScreen.this;
////							AppReference.chat_username = buddy;
//						} else {
//
//							showToast("Kindly hangup existing one way chat");
//						}
//
//					} else {
//						showToast("Video is Playing .Kindly Stop that first");
//					}
//				} else {
//					showToast("Audio is Playing .Kindly Stop that first");
//				}
//			} else {
//				showToast("Audio Recording is Going on .Kindly Stop that first");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	void vodeoChat() {
//		try {
//			final String[] items = new String[] { "New Video", "Existing Video" };
//			CompleteListView.checkDir();
//			Log.d("IM", "video Event Triggered");
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("video Event Triggered");
//
//			final Long free_size = callDisp.getExternalMemorySize();
//			Log.d("IM", free_size.toString());
//			if (AppReference.isWriteInFile)
//				AppReference.logger.debug("Free size -->" + free_size);
//
//			AlertDialog.Builder bldr = new AlertDialog.Builder(context);
//			bldr.setItems(items, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//					if (free_size > 0 && free_size >= 5120) {
//
//						try {
//							if (!isrecording) {
//								if (!isAudioPlaying) {
//									if (!isvideoplay) {
//										Log.i("QAA",
//												"InSufficient Memory...tryyy");
//
//										if (AppReference.isWriteInFile)
//											AppReference.logger
//													.error("InSufficient Memory...tryyy");
//										InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//										imm.hideSoftInputFromWindow(
//												getCurrentFocus()
//														.getWindowToken(), 0);
//										File folder = new File(
//												"/sdcard/COMMedia/");
//										if (which == 0) {
//											Log.i("QAA",
//													"InSufficient Memory...tryyy which==0");
//											if (AppReference.isWriteInFile)
//												AppReference.logger
//														.error("InSufficient Memory...tryyy which==0");
//
//											Message msg = new Message();
//											videorecorder.sendMessage(msg);
//										} else if (which == 1) {
//											Log.i("QAA",
//													"InSufficient Memory...tryyy which==1");
//											if (AppReference.isWriteInFile)
//												AppReference.logger
//														.error("InSufficient Memory...tryyy which==1");
//
//											Intent i = new Intent(context,
//													NotePickerScreen.class);
//											i.putExtra("note", "video");
//											startActivityForResult(i, 100);
//										}
//									} else {
//										showToast("Video is Playing .Kindly Stop that first");
//									}
//								} else {
//									showToast("Audio is Playing .Kindly Stop that first");
//								}
//							} else {
//								showToast("Recording is going on kindly stop the Recording");
//							}
//
//						} catch (Exception e) {
//							Log.i("QAA",
//									"InSufficient Memory..." + e.getMessage());
//							if (AppReference.isWriteInFile)
//								AppReference.logger
//										.error("InSufficient Memory..."
//												+ e.getMessage());
//
//							// TODO Auto-generated catch block
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error(e.getMessage(), e);
//							e.printStackTrace();
//						}
//					} else {
//
//						Log.i("QAA", "InSufficient Memory...");
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error("InSufficient Memory...");
//						showToast("InSufficient Memory...");
//					}
//
//				}
//
//			});
//			videorecorder = new Handler() {
//
//				@Override
//				public void handleMessage(Message msg) {
//					// TODO Auto-generated method stub
//					isVideoRecord = true;
//					String videoname = getFileName();
//					strIPath = "/sdcard/COMMedia/" + videoname + ".mp4";
//					// System.out.println("---video view start---");
//					// Intent intent = new Intent(context,
//					// CamsampleActivity.class);
//					// intent.putExtra("video_name", strIPath);
//					// intent.putExtra("auto", false);
//					// startActivityForResult(intent, 1);
//
//					Intent intent = new Intent(context, MultimediaUtils.class);
//					intent.putExtra("filePath", strIPath);
//					intent.putExtra("requestCode", 1);
//					intent.putExtra("action", MediaStore.ACTION_VIDEO_CAPTURE);
//					intent.putExtra("createOrOpen", "create");
//					startActivity(intent);
//
//					super.handleMessage(msg);
//				}
//
//			};
//			bldr.setOnCancelListener(new OnCancelListener() {
//
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					// TODO Auto-generated method stub
//				}
//			});
//
//			bldr.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	void photochat() {
//
//		try {
//			final String[] items = new String[] { "New Photo", "Open Gallery",
//					"Hand Sketch" };
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//			builder.setItems(items, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(getCurrentFocus()
//							.getWindowToken(), 0);
//
//					if (!isrecording) {
//						if (!isAudioPlaying) {
//							if (!isvideoplay) {
//
//								if (which == 1) {
//									ph_au_Sender("Gallery");
//								} else if (which == 0) {
//									CompleteListView.checkDir();
//									ph_au_Sender("Photo");
//								} else if (which == 2) {
//									CompleteListView.checkDir();
//									ph_au_Sender("Sketch");
//								}
//
//							} else {
//								showToast("Video is Playing .Kindly Stop that first");
//							}
//						} else {
//							showToast("Audio is Playing .Kindly Stop that first");
//						}
//					} else {
//						showToast("Audio Recording is Going on .Kindly Stop that first");
//					}
//
//				}
//
//			});
//
//			builder.setOnCancelListener(new OnCancelListener() {
//
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					// photo.setEnabled(true);
//				}
//			});
//			builder.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	private void sendTextMsg(String Message, String localpath) {
//		try {
//			if (Message.trim().length() != 0 || localpath.trim().length() != 0) {
//				// chattingList.addView(imFromMe(myMessage.getText()
//				// .toString(), time()));
//				// send the message to engine
//				try {
//					File file = new File(localpath);
//					String st = "";
//					StringBuilder bldr = new StringBuilder();
//
//					BuddyInformationBean bib = null;
//					Log.d("imscreen", "send button");
//					Log.d("mmchat", "Send Clicked");
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error("Send Clicked");
//					String membersnames = "";
//					String strSignalId = Long.toString(
//							utility.getRandomMediaID()).trim();
//					for (int i = 0; i < members.size(); i++) {
//						Log.d("imscreen", "for loop");
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error("for loop");
//
//						// System.out
//						// .println("members.get(i) ######################### :"
//						// + members.get(i));
//
//						if (!members.get(i).equals(CallDispatcher.LoginUser)) {
//							Log.d("imscreen", "if");
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("if");
//							if (WebServiceReferences.buddyList
//									.containsKey(members.get(i))) {
//								Log.d("imscreen", "ej contains buddy name");
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.error("ej contains buddy name");
//								bib = WebServiceReferences.buddyList
//										.get(members.get(i));
//							}
//
//							Log.d("imscreen", "if passess " + members.get(i));
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("imscreen"
//										+ members.get(i));
//							SignalingBean sb = new SignalingBean();
//							sb.setFrom(CallDispatcher.LoginUser);
//							sb.setTolocalip(bib.getLocalipaddress());
//							Log.d("imscreen", bib.getLocalipaddress());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("imscreen"
//										+ bib.getLocalipaddress());
//							sb.setTopublicip(bib.getExternalipaddress());
//							Log.d("imscreen", bib.getExternalipaddress());
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("imscreen"
//										+ bib.getExternalipaddress());
//							sb.setTo(members.get(i));
//							membersnames += members.get(i);
//							Log.d("imscreen", members.get(i));
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("imscreen"
//										+ members.get(i));
//							sb.setType("11");
//							sb.setSessionid(sessionid);
//							Log.d("imscreen", "cc" + sessionid);
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("cc" + sessionid);
//							sb.setResult("0");
//							sb.setCallType("MTP");
//							sb.setisRobo("");
//							sb.setSignalid(strSignalId);
//
//							String myMessageToSend = null;
//							if (Message.trim().length() != 0) {
//								myMessageToSend = Message;
//							} else {
//
//								try {
//									FileInputStream fis = new FileInputStream(
//											file);
//									BufferedReader rdr = new BufferedReader(
//											new InputStreamReader(fis));
//
//									try {
//										while ((st = rdr.readLine()) != null) {
//											Log.d("File",
//													"#######################"
//															+ st);
//											if (AppReference.isWriteInFile)
//												AppReference.logger
//														.error("#######################"
//																+ st);
//											bldr.append(st);
//											bldr.append('\n');
//
//										}
//									} catch (IOException e) {
//										// TODO Auto-generated catch block
//										if (AppReference.isWriteInFile)
//											AppReference.logger.error(
//													e.getMessage(), e);
//										e.printStackTrace();
//									}
//								} catch (FileNotFoundException e) {
//									// TODO Auto-generated catch block
//									if (AppReference.isWriteInFile)
//										AppReference.logger.error(
//												e.getMessage(), e);
//									e.printStackTrace();
//								}
//								myMessageToSend = bldr.toString();
//							}
//							// used to Convert special character
//							if (myMessageToSend.contains("&")) {
//								myMessageToSend = myMessageToSend.replace("&",
//										"&amp;");
//							}
//							if (myMessageToSend.contains("<")) {
//								myMessageToSend = myMessageToSend.replace("<",
//										"&lt;");
//							}
//							if (myMessageToSend.contains(">")) {
//								myMessageToSend = myMessageToSend.replace(">",
//										"&gt;");
//							}
//							if (myMessageToSend.contains("'")) {
//								myMessageToSend = myMessageToSend.replace("'",
//										"&apos;");
//							}
//							if (myMessageToSend.contains("\"")) {
//								// String d = "\"";
//								myMessageToSend = myMessageToSend.replace("\"",
//										"&quot;");
//							}
//							Log.d("BL", "IM " + myMessageToSend);
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("IM"
//										+ myMessageToSend);
//							//
//							sb.setMessage(myMessageToSend);
//							sb.setToSignalPort(bib.getSignalingPort());
//							if (groupmode) {
//								sb.setConferencemember(getMembers()
//										+ CallDispatcher.LoginUser);
//							}
//							Log.d("imscreen", "if finished");
//							if (AppReference.isWriteInFile)
//								AppReference.logger.error("if finished");
////							CallDispatcher.commEngine.makeIM(sb);
//						}
//						if (!bib.getStatus().equals("Online")) {
//							showToast("Sorry the message not sent.Selected Buddy is not in Online");
//						} else if (CallDispatcher.LoginUser == null) {
//							showToast("Sorry the message not sent.User is not in Online");
//						}
//					}
//
//					if (strSignalId.length() != 0) {
//						TextChatBean txtbean = new TextChatBean();
//						txtbean.setBuddyName(buddy);
//						txtbean.setChatTime(time());
//						txtbean.setColor("0.000000,1.000000,0.000000,0.000000");
//						txtbean.setFace("Helvetica");
//						if (Message.trim().length() != 0) {
//							txtbean.setMessage(Message);
//						} else {
//							txtbean.setMessage(bldr.toString());
//						}
//
//						txtbean.setSize("12.000000");
//						txtbean.setStyle("Normal");
//						txtbean.setType(1);
//						txtbean.setFlag(0);
//						txtbean.setSignalId(strSignalId);
//						txtbean.setUserName(initializer);
//						if (isimported) {
//							txtbean.setisImported(true);
//							txtbean.setOldFileName(importedFile);
//							isimported = false;
//							String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//									+ "values('"
//									+ sessionid
//									+ "','"
//									+ importedFile
//									+ "'m'"
//									+ strSignalId
//									+ "','')";
//							if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//								Log.i("ary", "entry created...");
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.error("entry created...");
//							}
//						}
//						if (isrobo) {
//							txtbean.setOldFileName(localpath);
//							txtbean.setIsRobo(true);
//							isrobo = false;
//							String qry = "insert into MMChat(sessionid, filepath,signalid,ftppath)"
//									+ "values('"
//									+ sessionid
//									+ "','"
//									+ localpath + "','" + strSignalId + "','')";
//							if (callDisp.getdbHeler(context).ExecuteQuery(qry)) {
//								Log.i("ary", "entry created...");
//								if (AppReference.isWriteInFile)
//									AppReference.logger
//											.error("entry created...");
//							}
//						}
//
//						appendChatDatatoXML(txtbean);
//						txtbean.setId(childMap.size());
//						if (AppReference.isWriteInFile)
//							AppReference.logger
//									.error("@@@@@@@@@@@ hashmap size..."
//											+ childMap.size());
//						Log.d("MIM",
//								"@@@@@@@@@@@ hashmap size..." + childMap.size());
//						childMap.add(childMap.size(), txtbean);
//
//						if (Message.trim().length() == 0) {
//							chattingList.addView(imFromME(bldr.toString(),
//									time(), strSignalId.trim(), 1, true,
//									txtbean.getId()));
//
//							CallDispatcher.message_map.put(buddy,
//									bldr.toString());
//						} else {
//							chattingList.addView(imFromME(Message, time(),
//									strSignalId.trim(), 1, true,
//									txtbean.getId()));
//							CallDispatcher.message_map.put(buddy, Message);
//						}
//					}
//					if (!groupmode) {
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error("In singlemode");
//						Log.d("imscreen", "singlemode");
//						/*
//						 * insertImDetails(CallDispatcher.LoginUser,
//						 * membersnames, buddy, myMessage.getText() .toString(),
//						 * sessionid, 1);
//						 */
//					} else {
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error("In Groupmode");
//						Log.d("imscreen", "Groupmode");
//						/*
//						 * insertImDetails(CallDispatcher.LoginUser,
//						 * getMembers(), getMembers(), myMessage
//						 * .getText().toString(), sessionid, 1);
//						 */
//					}
//
//					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//					inputManager.hideSoftInputFromWindow(getCurrentFocus()
//							.getWindowToken(),
//							InputMethodManager.HIDE_NOT_ALWAYS);
//					sview.fullScroll(ScrollView.FOCUS_DOWN);
//					myMessage.setText("");
//
//				} catch (Exception e) {
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//				}
//
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//
//		}
//	}
//
//	private void showMessageDialog1() {
//		try {
//			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
//			String[] type = { "New Audio", "Existing Audio" };
//
//			alert_builder.setSingleChoiceItems(type, 0,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int pos) {
//							// TODO Auto-generated method stub
//							if (pos == 0) {
//
//								if (!isrecording) {
//									fromexist = false;
//
//									AppReference.issipchatinitiated = false;
//									InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//									imm.hideSoftInputFromWindow(
//											getCurrentFocus().getWindowToken(),
//											0);
//									// audio.setEnabled(true);
//									RecordAudio("Audio");
//									msg_dialog.dismiss();
//								} else {
//									showToast("Recording is going on kindly stop the Recording");
//								}
//
//							} else if (pos == 1) {
//								Intent i = new Intent(context,
//										NotePickerScreen.class);
//								i.putExtra("note", "audio");
//
//								startActivityForResult(i, 100);
//								msg_dialog.dismiss();
//
//							}
//						}
//					});
//			msg_dialog = alert_builder.create();
//			msg_dialog.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void showMessageDialog() {
//		try {
//			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
//			alert_builder.setTitle("Select Call Type");
//			final PermissionBean permissionBean = callDisp.getdbHeler(context)
//					.selectPermissions(
//							"select * from setpermission where userid='"
//									+ buddyName.getText().toString()
//									+ "' and buddyid='"
//									+ CallDispatcher.LoginUser + "'",
//							buddyName.getText().toString(),
//							CallDispatcher.LoginUser);
//			alert_builder.setSingleChoiceItems(m_type, 0,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int pos) {
//							// TODO Auto-generated method stub
//							if (pos == 0) {
//								msg_dialog.cancel();
//								if (permissionBean.getAudio_call().equals("1"))
//									MakeCall(1);
//								else
//									callDisp.showAlert("Response",
//											"Access Denied");
//							} else if (pos == 1) {
//								msg_dialog.cancel();
//								if (permissionBean.getVideo_call().equals("1"))
//									MakeCall(2);
//								else
//									callDisp.showAlert("Response",
//											"Access Denied");
//
//							} else if (pos == 2) {
//								msg_dialog.cancel();
//								if (permissionBean.getAUC().equals("1"))
//									MakeCall(3);
//								else
//									callDisp.showAlert("Response",
//											"Access Denied");
//							} else if (pos == 3) {
//								msg_dialog.cancel();
//								if (permissionBean.getVUC().equals("1"))
//									MakeCall(4);
//								else
//									callDisp.showAlert("Response",
//											"Access Denied");
//
//							}
//						}
//					});
//			msg_dialog = alert_builder.create();
//			msg_dialog.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void showBuddyDialog() {
//		try {
//			AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
//			alert_builder.setTitle("Select Buddy Info");
//			alert_builder.setSingleChoiceItems(b_type, 0,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int pos) {
//							// TODO Auto-generated method stub
//							if (pos == 0) {
//								msg_dialog.cancel();
//								doViewProfile(buddy);
//							} else if (pos == 1) {
//								msg_dialog.cancel();
//								clearAllAlert();
//							} else if (pos == 2) {
//								msg_dialog.cancel();
//
//							}
//						}
//					});
//			msg_dialog = alert_builder.create();
//			msg_dialog.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void doViewProfile(String buddy) {
//
//		try {
//			ArrayList<String> profileList = callDisp.getdbHeler(context)
//					.getProfile(buddy);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error("size of arrayList--->"
//						+ profileList.size());
//			Log.i("profile", "size of arrayList--->" + profileList.size());
//
//			if (profileList.size() > 0) {
//				Intent intent = new Intent(context, ViewProfiles.class);
//				intent.putExtra("buddyname", buddy);
//				startActivity(intent);
//			} else {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error("View Profile --->" + buddy
//							+ " -->getprofiledetails");
//				Log.i("profile", "VIEW PROFILE------>" + buddy
//						+ "---->GetProfileDetails");
//				CallDispatcher.pdialog = new ProgressDialog(context);
//				callDisp.showprogress(CallDispatcher.pdialog, context);
//				CallDispatcher.isFromCallDisp = false;
//				String modifiedDate = callDisp.getdbHeler(context)
//						.getModifiedDate(
//								"select max(modifieddate) from profilefieldvalues where userid='"
//										+ buddy + "'");
//				if (modifiedDate == null) {
//					modifiedDate = "";
//				} else if (modifiedDate.trim().length() == 0) {
//					modifiedDate = "";
//				}
//				String[] profileDetails = new String[3];
//				profileDetails[0] = buddy;
//				profileDetails[1] = "5";
//				profileDetails[2] = modifiedDate;
//				view = 1;
//				isProfileRequested = true;
//				WebServiceReferences.webServiceClient
//						.getStandardProfilefieldvalues(profileDetails);
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	public void notifyViewProfile() {
//		try {
//			String[] profileList = callDisp.getdbHeler(context)
//					.getProfileValues(buddy);
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error("Size of Array list -->"
//						+ profileList.length);
//			Log.i("profile", "size of arrayList--->" + profileList.length);
//			if (isProfileRequested) {
//				isProfileRequested = false;
//				if (profileList.length > 0 && view == 1) {
//					if (!WebServiceReferences.contextTable
//							.containsKey("viewprofile")) {
//						Intent intent = new Intent(context, ViewProfiles.class);
//						intent.putExtra("buddyname", buddy);
//						if (WebServiceReferences.buddyList.containsKey(buddy)) {
//							BuddyInformationBean bean = WebServiceReferences.buddyList
//									.get(buddy);
//							intent.putExtra("buddystatus", bean.getStatus());
//						}
//						view = 0;
//						startActivity(intent);
//					} else {
//						((ViewProfiles) WebServiceReferences.contextTable
//								.get("viewprofile")).initView();
//					}
//				} else
//					Toast.makeText(context,
//							"No profile assigned for this user",
//							Toast.LENGTH_SHORT).show();
//
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
//
//	private void clearAllAlert() {
//
//		try {
//			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
//			myAlertDialog.setTitle("Clear Chat History");
//			myAlertDialog
//					.setMessage("Are you sure, you want to clear chat history of "
//							+ buddy + "?");
//			myAlertDialog.setPositiveButton("OK",
//					new DialogInterface.OnClickListener() {
//
//						public void onClick(DialogInterface arg0, int arg1) {
//							clearAll();
//						}
//					});
//			myAlertDialog.setNegativeButton("Cancel",
//					new DialogInterface.OnClickListener() {
//
//						public void onClick(DialogInterface dialog, int arg1) {
//							// do something when the Cancel button is clicked
//
//							dialog.cancel();
//						}
//					});
//			myAlertDialog.show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	public void clearAll() {
//
//		// TODO Auto-generated method stub
//		if (AppReference.isWriteInFile)
//			AppReference.logger.error("===> chatting list"
//					+ chattingList.getChildCount());
//		Log.i("imscreen", "===> chatting list" + chattingList.getChildCount());
//		File file = new File(Environment.getExternalStorageDirectory()
//				+ "/COMMedia/" + sessionid);
//		if (file.exists()) {
//			try {
//				CallDispatcher.pdialog = new ProgressDialog(context);
//				callDisp.showprogress(CallDispatcher.pdialog, context);
//				file.delete();
//				callDisp.cancelDialog();
//			} catch (Exception e) {
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error("Exception Raised"
//							+ e.getMessage());
//				Log.e("imscreen", "===> exception " + e.getMessage());
//				if (AppReference.isWriteInFile)
//					AppReference.logger.error(e.getMessage(), e);
//				e.printStackTrace();
//			}
//		}
//		myXMLWriter = new XMLComposer(Environment.getExternalStorageDirectory()
//				+ "/COMMedia/" + sessionid);
//		mHandler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (chattingList.getChildCount() > 0) {
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error("Check MIM Called "
//								+ chattingList.getChildCount());
//					Log.d("INSTANT", "@@@@@@@@@@@ChaeckMIM called"
//							+ chattingList.getChildCount());
//
//					chattingList.removeViews(0, chattingList.getChildCount());
//				}
//			}
//		});
//
//	}
//
//	public void refreshStatus(final String status) {
//		try {
//			handler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					try {
//						if (status != null && status.length() > 0)
//							buddyStatus.setText(status.trim());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						if (AppReference.isWriteInFile)
//							AppReference.logger.error(e.getMessage(), e);
//						e.printStackTrace();
//					}
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			if (AppReference.isWriteInFile)
//				AppReference.logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		}
//
//	}
//
//	public void notifyroflePictureDownloaded(final String buddyname,
//			final String image_path) {
//		handler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (buddyname != null) {
//					if (buddyname.equals(buddy)) {
//						if (image_path != null && image_path.length() > 0) {
//							Bitmap profle_bitmap = callDisp.setProfilePicture(
//									image_path, R.drawable.icon_buddy_aoffline);
//							buddyPic.setImageBitmap(profle_bitmap);
//
//						}
//					}
//				}
//			}
//		});
//	}
//
//	public void notifyBuddydeleted(final String buddy_name) {
//		handler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (buddy_name != null) {
//					if (buddy_name.equals(buddy)) {
//						showToast("This buddy deleted from your buddies list");
//						finish();
//					}
//
//				}
//			}
//		});
//	}
//
//	private void afterRecoder() {
//
//		isrecording = false;
//
//		llayStart.removeAllViews();
//		final Button btn_play = new Button(context);
//		btn_play.setText("Play");
//		btn_play.setTag(strIPath);
//		llayStart.addView(btn_play);
//
//		btn_play.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				File file = new File(strIPath);
//
//				if (file.exists()) {
//					Intent intent = new Intent(context, MultimediaUtils.class);
//					intent.putExtra("filePath", v.getTag().toString());
//					intent.putExtra("requestCode", 32);
//					intent.putExtra("action", "audio");
//					intent.putExtra("createOrOpen", "open");
//					startActivity(intent);
//				}
//			}
//
//		});
//
//		Button audio_send = new Button(context);
//		audio_send.setText("Send");
//		audio_send.setGravity(Gravity.CENTER);
//		llayStart.addView(audio_send);
//		audio_send.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				try {
//					// TODO Auto-generated
//					// method
//					// stub
//					if (buddy != null) {
//
//						BuddyInformationBean bib = WebServiceReferences.buddyList
//								.get(buddy);
//
//						// audio.setEnabled(true);
//						isrecording = false;
//						mmlay.setBackgroundDrawable(null);
//						mmlay.removeAllViews();
//						FTPBean bean = new FTPBean();
//						bean.setFtp_username(callDisp.LoginUser);
//						bean.setFtp_password(callDisp.Password);
//						bean.setOperation_type(1);
//						bean.setServer_port(40400);
//						bean.setComment(sessionid);
//						bean.setServer_ip(callDisp.getRouter().split(":")[0]);
//						bean.setRequest_from("MAP");
//						bean.setFile_path(strIPath);
//						notifyFTPServerConnected(true, bean);
//						if (bib != null && !isForceLogout) {
//							if (CallDispatcher.LoginUser == null) {
//								showToast("Sorry! can not send This Audio");
//							} else if (!bib.getStatus().equals("Online")) {
//								showToast("The User is not in Online state");
//							}
//						} else
//							showToast("Sorry! can not send This Audio");
//
//					} else {
//						if (isForceLogout) {
//							showToast("Sorry the user is not in Online");
//						}
//					}
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch
//					// block
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//				}
//			}
//		});
//
//		Button exit = new Button(context);
//		exit.setText("Exit");
//		exit.setGravity(Gravity.CENTER);
//		llayStart.addView(exit);
//		exit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				try {
//					// TODO Auto-generated method stub
//					// audio.setEnabled(true);
//					isrecording = false;
//					llayStart.removeAllViews();
//					llayTimer.removeAllViews();
//					mmlay.removeAllViews();
//					File fl = new File(strIPath);
//					if (fl.exists()) {
//						fl.delete();
//						tvtimer.setText("00:00:00");
//					}
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					if (AppReference.isWriteInFile)
//						AppReference.logger.error(e.getMessage(), e);
//					e.printStackTrace();
//				}
//
//			}
//		});
//
//	}
//
//
//}
