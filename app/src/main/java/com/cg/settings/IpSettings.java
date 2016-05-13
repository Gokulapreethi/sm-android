package com.cg.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.audio.AudioProperties;
import org.lib.model.FlightDetails;
import org.lib.model.SignalingBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.CompleteListView;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.ActionItem1;
import com.cg.instancemessage.IMNotifier;
import com.cg.instancemessage.QuickAction1;
import com.im.xml.XMLComposer;
import com.main.AppMainActivity;
import com.main.LoginPageFragment;
import com.util.SingleInstance;

/**
 * To show the setting ui .By the user settings we can make the connection with
 * our application call service network
 * 
 * 
 */
public class IpSettings extends Activity implements IMNotifier {

	// @Override
	Context context;
	private Toast toast;
	private Button btnReceiveCall;
	/**
	 * Used to update Buddy Status and show Requests.
	 */
	public static Button Buddy;
	// private Button btnfbconfig = null;
	private Handler handleRegistration = null;
	private ProgressDialog progDailog = null;
	private boolean isfbConnected = false;
	private String type = null;
	boolean isreject = false;
	/**
	 * Used to indicate IM received.
	 */
	public static Button IMRequest;
	ImageView iv = null;

	static int buddyStatus = 0;
	CallDispatcher callDisp;
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	String record_path = null;
	private XMLComposer myXMLWriter = null;
	private Utility utility = new Utility();

	SharedPreferences SharedPreferences = null;

	AudioProperties audioProperties = null;
	LinearLayout llImage = null;
	private Handler viewHandler = new Handler();
	private Button btn_notification = null;
	private Handler handler = new Handler();
	private AlertDialog confirmation = null;
	String screen = null;
	TextView titles = null;
	String Screen = null;

	// TextView title = null;
	LinearLayout ipsett, fbconfig;
	Button btn_cancel = null;

	/**
	 * When activity is started initialize the ui to produce the settings screen
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		WebServiceReferences.contextTable.put("a_play", this);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_title1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title1);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

		IMRequest = (Button) findViewById(R.id.im);
		IMRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				callDisp.openReceivedIm(arg0, context);
			}
		});

		btnReceiveCall = (Button) findViewById(R.id.btncomp);
		btnReceiveCall.setVisibility(View.INVISIBLE);
		Log.e("IM", "IM Collection Size");
		IMRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d("imclick", "imarray button====================="
						+ WebServiceReferences.Imcollection.size());
				if (WebServiceReferences.Imcollection.size() > 0) {
					ArrayList<String> al = new ArrayList<String>();
					HashMap<String, String> hsIMS = new HashMap<String, String>();

					int[] ix = new int[WebServiceReferences.Imcollection.size()];
					Iterator myVeryOwnIterator = WebServiceReferences.Imcollection
							.keySet().iterator();
					int i = 0;
					while (myVeryOwnIterator.hasNext()) {
						String key = (String) myVeryOwnIterator.next();
						ArrayList<SignalingBean> value = (ArrayList<SignalingBean>) WebServiceReferences.Imcollection
								.get(key);
						ix[i] = value.size();
						SignalingBean sx = value.get(0);
						String srtNames = sx.getFrom();
						al.add(srtNames);
						hsIMS.put(srtNames, sx.getSessionid());
						i += 1;
					}
					ShowImMembers(al, v, ix, hsIMS);

				} else {
					Toast.makeText(context, "No IM", Toast.LENGTH_SHORT).show();
				}
				Log.d("imclick", "imarray button finished");

			}

		});

		btn_cancel = (Button) findViewById(R.id.settings);
		btn_cancel.setBackgroundResource(R.drawable.ic_action_back);
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		IMRequest.setVisibility(View.INVISIBLE);
		IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);
		// IMRequest.setWidth(70);
		WebServiceReferences.contextTable.put("IM", this);
		WebServiceReferences.contextTable.put("IpSettings", this);
		btn_notification = (Button) findViewById(R.id.notification);

		btn_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		SharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		setContentView(R.layout.settingsscreen);
		Screen = getIntent().getStringExtra("Screen");
		if (Screen.equals("ip")) {
			titles = (TextView) findViewById(R.id.note_date);
			titles.setText(SingleInstance.mainContext.getResources().getString(R.string.network_settings));
		} else {
			titles = (TextView) findViewById(R.id.note_date);
			titles.setText(SingleInstance.mainContext.getResources().getString(R.string.link_to_facebook));
		}
		final EditText edUrl = (EditText) findViewById(R.id.etSettingsIp);

		edUrl.setText(getResources().getString(R.string.service_url));

		edUrl.setFocusable(true);

		final EditText edPort = new EditText(this);
		edPort.setText("80");
		ipsett = (LinearLayout) findViewById(R.id.iplayout);
		screen = getIntent().getStringExtra("Screen");
		if (!WebServiceReferences.callDispatch.containsKey("calldispatch")) {
			callDisp = new CallDispatcher(this);
		} else {
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldispatch");
		}

		if (callDisp != null) {
			callDisp.isConnected = callDisp.isOnLine(this);
		}
		WebServiceReferences.callDispatch.put("calldispatch", callDisp);

		String url = SharedPreferences.getString("url", null);
		if (url != null) {
			edUrl.setText(url.trim());
		}

		Button save = (Button) findViewById(R.id.btnSaveSettings);
		save.setText(SingleInstance.mainContext.getResources().getString(R.string.save));

		Button cancel = (Button) findViewById(R.id.btnCancelSettings);
		cancel.setText(SingleInstance.mainContext.getResources().getString(R.string.cancel));

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isURLnotchanged = false;

				final String strUrl = edUrl.getText().toString();
				final String strPort = edPort.getText().toString();

				if (strUrl.length() != 0 && strPort.length() != 0) {
					if (CallDispatcher.LoginUser != null) {

						String getURL = (String) context
								.getString(R.string.service_url);

						if (!getURL.equalsIgnoreCase(strUrl)) {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							String strMsg = SingleInstance.mainContext.getResources().getString(R.string.save_the_changes_logged);

							builder.setTitle(SingleInstance.mainContext.getResources().getString(R.string.save_settings));

							builder.setMessage(strMsg)
									.setCancelable(false)
									.setPositiveButton(
											SingleInstance.mainContext.getResources().getString(R.string.yes),
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													try {
														
														// Intent i = new
														// Intent(
														// context,
														// buddyView1.class);
														// startActivity(i);
														AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
																.get("MAIN");
														try {
															if (AppReference.isWriteInFile)
																AppReference.logger
																		.debug("LOGOUT : FROM IPSETTINGS");
														} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														appMainActivity.logout(true);
														LoginPageFragment loginPageFragment = LoginPageFragment.newInstance(context);
														loginPageFragment.setSilentLogin(false);
														FragmentManager fragmentManager = appMainActivity
																.getSupportFragmentManager();
														FragmentTransaction fragmentTransaction = fragmentManager
																.beginTransaction();
														fragmentTransaction
																.replace(
																		R.id.activity_main_content_fragment,
																		loginPageFragment,"loginfragment");
														fragmentTransaction
																.commit();
													} catch (Exception e) {
														// TODO
														// Auto-generated
														// catch block
														e.printStackTrace();
													}

													Editor editor = SharedPreferences
															.edit();
													editor.putString("url",
															edUrl.getText()
																	.toString());
													editor.putString("port",
															edPort.getText()
																	.toString());
													editor.commit();
													toast = Toast.makeText(
															context,
															SingleInstance.mainContext.getResources().getString(R.string.settings_saved),
															3000);
													toast.show();

													changeSettings(strUrl,
															strPort);
													Log.e("pos",
															"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM from from positive button");
													finish();
												}
											})
									.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.cancel();
												}
											});

							builder.show();
						} else {
							// Not doing any process
							isURLnotchanged = true;
							finish();
						}

					} else {
						Editor editor = SharedPreferences.edit();
						editor.putString("url", edUrl.getText().toString());
						editor.putString("port", edPort.getText().toString());
						editor.commit();
						toast = Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.settings_saved), 3000);
						toast.show();
						changeSettings(strUrl, strPort);
						Log.e("pos",
								"MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM from from else part");
						if (CallDispatcher.action != 3) {
							finish();
						}

					}
				} else {
					toast = Toast.makeText(context,
							SingleInstance.mainContext.getResources().getString(R.string.please_fill_fields_completely),
							Toast.LENGTH_LONG);
					toast.show();
				}

			}

		});

	}

	public void ShowImMembers(final ArrayList al, View v, final int[] ix,
			final HashMap<String, String> hs) {

		final QuickAction1 quickAction = new QuickAction1(this);

		for (int i = 0; i < al.size(); i++) {
			ActionItem1 dashboard = new ActionItem1();
			// dashboard.setTitle("My name is Test");
			String size = Integer.toString(ix[i]);
			dashboard.setTitle((String) al.get(i) + "   " + size);
			// dashboard.
			dashboard.setIcon(getResources().getDrawable(R.drawable.kontak));
			quickAction.addActionItem(dashboard);

		}
		quickAction
				.setOnActionItemClickListener(new QuickAction1.OnActionItemClickListener() {
					@Override
					public void onItemClick(int pos) {

						try {
							Log.e("IM", "Postition :" + pos);
							Log.e("IM", "NAME :" + al.get(pos));

							Log.e("IM", "Session :" + hs.get(al.get(pos)));
							String name = (String) al.get(pos);
							String session = (String) hs.get(al.get(pos));

							showActiveSession(session, name);

							al.remove(pos);
							if (al.size() <= 0) {
								IMRequest.setVisibility(View.INVISIBLE);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						// ShowIm();
						// IMRequest.setVisibility(View.INVISIBLE);
					}
				});

		quickAction.show(v);
	}

	public void showActiveSession(String session, String selectedBuddy) {
		Log.d("session",
				"reloadCurrentSession................................."
						+ session);
		Log.d("session",
				"reloadCurrentSession................................."
						+ selectedBuddy);

//		if (WebServiceReferences.instantMessageScreen.containsKey(session)) {
//			InstantMessageScreen imscreen = (InstantMessageScreen) WebServiceReferences.instantMessageScreen
//					.get(session);
//			imscreen.finish();
//		}
		
		
		
		SignalingBean bean = new SignalingBean();
		bean.setSessionid(session);
		bean.setFrom(CallDispatcher.LoginUser);
		bean.setTo(selectedBuddy);
		bean.setConferencemember("");
		bean.setMessage("");
		bean.setCallType("MSG");
//		Intent intent = new Intent(context, IMTabScreen.class);
//		intent.putExtra("sb", bean);
//		intent.putExtra("fromto", true);
//		startActivity(intent);

	}

	/**
	 * To show the list of incoming im chat message in a Quick action dialog
	 * 
	 * @param al
	 *            - list of incoming sender names
	 * @param v
	 *            - for which view we have to show the quick action dialog
	 * @param ix
	 *            number of messages from whom
	 */

	/**
	 * After the setting changes start the web service from the respective
	 * screen
	 * 
	 * @param strURL
	 *            - web service connection url
	 * @param strPort
	 *            - web service connection port number
	 */
	private static void changeSettings(String strURL, String strPort) {

		Object obj = (Object) WebServiceReferences.contextTable.get("MAIN");
		try {

			if (obj instanceof CompleteListView) {
				Log.d("settings", "listall");
				if (WebServiceReferences.running) {
					WebServiceReferences.webServiceClient.stop();
					WebServiceReferences.running = false;
					WebServiceReferences.webServiceClient = null;
					CompleteListView libraryTest = (CompleteListView) WebServiceReferences.contextTable
							.get("MAIN");
					libraryTest.startWebService(strURL, strPort);
					FlightDetails f = new FlightDetails();

				} else {

					CompleteListView libraryTest = (CompleteListView) WebServiceReferences.contextTable
							.get("MAIN");
					libraryTest.startWebService(strURL, strPort);

				}

			}

		} catch (Exception e) {
			Log.d("settings", "catch" + e);

		}

	}

	/**
	 * Perform any final cleanup before an activity is destroyed
	 */
	@Override
	protected void onDestroy() {

		try {
			Log.d("lg", "On Destroy");
			if (WebServiceReferences.contextTable.containsKey("IpSettings")) {
				Log.d("lg", "Key contains");
				WebServiceReferences.contextTable.remove("IpSettings");
			}
			if (WebServiceReferences.contextTable.containsKey("IM")) {
				WebServiceReferences.contextTable.remove("IM");
			}
			if (WebServiceReferences.contextTable.containsKey("a_play")) {
				WebServiceReferences.contextTable.remove("a_play");
			}
			toast.cancel();
			if (img != null) {
				if (!img.isRecycled())
					img.recycle();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	public void ShowError(String Title, String Message,
			final boolean hastofinish) {
		confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (hastofinish) {
					finish();
				}
			}
		});

		confirmation.show();
	}

	public void notifyWebRes(String Result, String Response) {
		if (progDailog != null) {
			progDailog.dismiss();
			progDailog = null;
		}
		if (Result.trim().equals("1")) {
			isfbConnected = true;

			ShowError("Facebook Configuration", Response, false);
		}
	}

	void saveType(int x) {

		try {

			Editor editor = SharedPreferences.edit();

			editor.putInt("type", x);

			editor.commit();
		}

		catch (Exception e) {
			e.printStackTrace();

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
		Log.e("fh", "On Resume called in Settings");

		btn_notification.setVisibility(View.GONE);

		if (WebServiceReferences.Imcollection.size() <= 0) {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.INVISIBLE);
			}
		} else {
			if (IMRequest != null) {
				IMRequest.setVisibility(View.VISIBLE);
			}
		}

	}

	Bitmap img;

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d("DDD", "Request code " + requestCode + "    REsult code "
				+ resultCode);

		if (requestCode == 0 && resultCode == -1) {
			Uri selectedImageUri = data.getData();
			String ComponentPath = callDisp
					.getRealPathFromURI(selectedImageUri);
			Log.d("DDD", "real path " + ComponentPath);
			img = callDisp.ResizeImage(ComponentPath);
			if (img != null) {
				Log.d("code", ">>>>>>>>>>>>>>" + ComponentPath);
				if (iv != null) {
					try {
						llImage.removeView(iv);
					} catch (Exception e) {

					}
				}

				llImage.addView(createImageView(img, ComponentPath));
				// btnaudio.setEnabled(false);
				// btnVideo.setEnabled(false);

				File path = new File(ComponentPath);
				String file = path.getName();

				/*
				 * ftpBean bean=new ftpBean();
				 * 
				 * ftpClient ftp = new ftpClient(); CallDispatcher calldisp =
				 * (CallDispatcher) WebServiceReferences.callDispatch
				 * .get("calldisp");
				 * 
				 * ftp.intializeFtp(calldisp.getRouter().split(":")[0],
				 * CallDispatcher.LoginUser, CallDispatcher.Password);
				 * ftp.ftpUpload(ComponentPath, file, file);
				 */

			}

		}

	}

	private ImageView createImageView(Bitmap image, final String Path) {
		iv = new ImageView(context);
		Log.d("image", ".......................path" + Path);
		callDisp.imagepath = Path;
		File CheckFile = new File(Path);
		Log.d("image", ".......................path" + CheckFile.exists());
		if (CheckFile.exists()) {
			iv.setImageBitmap(image);
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent in = new Intent(context, PhotoZoomActivity.class);
						in.putExtra("Photo_path", Path);
						in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in);
					} catch (WindowManager.BadTokenException e) {
						// Log.e("Log", "Bad Tocken:" + e.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});

		} else {
			iv.setBackgroundResource(R.drawable.broken);
		}
		return iv;

	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IMRequest.setVisibility(View.VISIBLE);
				IMRequest.setEnabled(true);

				IMRequest.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();

			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
