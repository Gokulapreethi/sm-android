package com.cg.avatar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lib.model.BuddyInformationBean;
import org.lib.model.OfflineConfigResponseBean;
import org.lib.model.OfflineConfigurationBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.SignalingBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.SlideMenu.SlideMenu;
import com.cg.SlideMenu.SlideMenuInterface;
import com.cg.SlideMenu.Slidebean;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.CloneTextinput;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.commongui.TestHTML5WebView;
import com.cg.ftpprocessor.FTPBean;
import com.cg.hostedconf.AppReference;
import com.cg.instancemessage.IMNotifier;
import com.main.AppMainActivity;
import com.main.ViewProfileFragment;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class CloneActivity extends Activity implements
		SlideMenuInterface.OnSlideMenuItemClickListener, IMNotifier {

	private Context context = null;

	private Handler handler = new Handler();

	private HashMap<Integer, String> path_map = new HashMap<Integer, String>();

	private HashMap<String, ArrayList<String>> removedpath_map = new HashMap<String, ArrayList<String>>();

	public HashMap<Integer, Button> values;

	public HashMap<Integer, ImageView> imageMap;

	private String type = null;

	OfflineRequestConfigBean offlineRequestConfigBean = null;

	private Button IMRequest;

	private MediaPlayer chatplayer;

	private Button btn__save;

	private Spinner input_to;

	private TextView resType, message_val, touser_title, value_txt;

	public String getComponentPath;

	boolean isreject = false;

	private SlideMenu slidemenu;

	private CallDispatcher callDisp;

	private Bitmap bitmap, img = null;

	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();

	private LinearLayout mainLayout, responselay;

	private Button settings, response, pending, clonenew, btn_sendreply,
			deletebtn;

	private ScrollView pending_container, settingscontainer,
			allresponsecontainer;

	private ImageView value_img;

	private String strIPath;

	private Spinner responseType;

	private LinearLayout llayTimer;

	private ArrayAdapter<String> dataAdapter = null;

	private ArrayAdapter<String> buddyAdapter = null;

	private int position = 0;

	private HashMap<Integer, String> type_map = new HashMap<Integer, String>();

	private HashMap<Integer, OfflineRequestConfigBean> bean_map = new HashMap<Integer, OfflineRequestConfigBean>();

	private ProgressDialog progress_dialog = null;

	private String username_list = "";

	private OfflineRequestConfigBean send_baen;

	private ArrayList<String> list;

	private EditText ed_title = null;

	private EditText ed_msgurl = null;

	private LinearLayout allresponseview_container;

	private LinearLayout view_container;

	private LinearLayout pendingclone_container;

	private HashMap<Integer, OfflineRequestConfigBean> clone_bean = new HashMap<Integer, OfflineRequestConfigBean>();

	private HashMap<Integer, String> responsepath_map = new HashMap<Integer, String>();

	private ArrayList<OfflineRequestConfigBean> allresponse_list = new ArrayList<OfflineRequestConfigBean>();

	private HashMap<Integer, OfflineRequestConfigBean> responsebean_map = new HashMap<Integer, OfflineRequestConfigBean>();

	private AlertDialog msg_dialog = null;

	private TextView tv_actionbuddies;

	private TextView tv_touser = null;

	private String[] m_type = {
			SingleInstance.mainContext.getResources().getString(R.string.text),
			SingleInstance.mainContext.getResources().getString(
					R.string.from_gallery),
			SingleInstance.mainContext.getResources().getString(
					R.string.from_camera),
			SingleInstance.mainContext.getResources().getString(R.string.audio),
			SingleInstance.mainContext.getResources().getString(R.string.video),
			SingleInstance.mainContext.getResources().getString(
					R.string.call_forwarding),
			SingleInstance.mainContext.getResources().getString(
					R.string.conference_call),
			SingleInstance.mainContext.getResources().getString(
					R.string.call_forward_to_gsm) };

	private boolean isrecording = false;

	private boolean isPlaying = false;

	private TextView tv_when = null;

	private TextView tv_whenselected;

	private static Pattern VALID_PHONE_NUMBER = Pattern
			.compile("^[0-9.()-]{10,25}$");

	private Button btn_patch;

	private Button btn_clearAll = null;

	private String avatarpage = SingleInstance.mainContext.getResources()
			.getString(R.string.settings);

	private boolean iscleared = false;

	private int AUDIO = 6;

	private Button btn_im;

	private ArrayList<OfflineRequestConfigBean> defalut_lis = null;

	private ArrayList<OfflineRequestConfigBean> buddy_list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			context = this;
			WebServiceReferences.contextTable.put("IM", context);
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
			ShowList();
			setContentView(R.layout.cloneview);

			WebServiceReferences.contextTable.put("clone", context);
			LinearLayout layout_maincontainers = (LinearLayout) findViewById(R.id.layout_maincontainers);
			settings = (Button) layout_maincontainers
					.findViewById(R.id.settingsbtn);
			btn_im = (Button) findViewById(R.id.clone_im);
			btn_im.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					callDisp.openReceivedIm(arg0, context);
				}
			});
			response = (Button) findViewById(R.id.responsebtn);
			pending = (Button) layout_maincontainers
					.findViewById(R.id.pendingbtn);
			btn_clearAll = (Button) findViewById(R.id.clearall);
			mainLayout = (LinearLayout) findViewById(R.id.container);
			tv_touser = (TextView) findViewById(R.id.touser_title);
			value_txt = (TextView) findViewById(R.id.value_txt);
			value_txt.setTag(0);
			input_to = (Spinner) findViewById(R.id.input_to);

			// buddyAdapter = new ArrayAdapter<String>(this,
			// android.R.layout.simple_spinner_item,
			// callDisp.getMyBuddies());
			// buddyAdapter
			// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			input_to.setAdapter(buddyAdapter);
			clonenew = (Button) findViewById(R.id.clonenew);
			values = new HashMap<Integer, Button>();
			btn_sendreply = (Button) findViewById(R.id.replybtn);
			value_txt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					showMessageDialog(tag);
				}
			});
			btn_sendreply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (bean_map.containsKey(0)) {
						String config_id = bean_map.get(0).getId();
						if (callDisp.getdbHeler(context)
								.getOfflineCallResponseDetails(
										"where configid=" + "\"" + config_id
												+ "\"") != null
								&& callDisp
										.getdbHeler(context)
										.getOfflineCallResponseDetails(
												"where configid=" + "\""
														+ config_id + "\"")
										.size() > 0) {
							settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
							settings.setTextColor(getResources().getColor(
									R.color.tabcolor));
							pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
							pending.setTextColor(getResources().getColor(
									R.color.tabcolor));
							response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
							response.setTextColor(getResources().getColor(
									R.color.white));
							allresponsecontainer.setVisibility(View.VISIBLE);
							allresponseview_container
									.setVisibility(View.VISIBLE);
							pending_container.setVisibility(View.GONE);
							settingscontainer.setVisibility(View.GONE);
							Log.i("clone", "=====>Inside1 pending");
							allresponse_list.clear();
							allresponseview_container.removeAllViews();
							allresponse_list.addAll(callDisp
									.getdbHeler(context)
									.getOfflineCallResponseDetails(
											"where configid=" + "\""
													+ config_id + "\""));
							Log.d("clone", "Respose count--->"
									+ allresponse_list.size());
							for (OfflineRequestConfigBean offlineRequestConfigBean : allresponse_list) {
								responsebean_map.put(allresponseview_container
										.getChildCount(),
										offlineRequestConfigBean);
								inflateAllresponseUI(allresponseview_container
										.getChildCount(),
										offlineRequestConfigBean);
							}

						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.no_response_to_view));
						}
					}
				}
			});
			tv_touser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					input_to.performClick();
				}
			});
			allresponsecontainer = (ScrollView) findViewById(R.id.allresponsecontainer);

			view_container = (LinearLayout) findViewById(R.id.view_container);
			view_container.setTag(0);

			allresponseview_container = (LinearLayout) findViewById(R.id.allresponseview_container);

			pendingclone_container = (LinearLayout) findViewById(R.id.pending_clonescontainer);
			pendingclone_container.setTag(0);

			ed_title = (EditText) findViewById(R.id.title);

			ed_msgurl = (EditText) findViewById(R.id.url);

			btn__save = (Button) findViewById(R.id.savebtn);
			btn__save.setTag(0);
			deletebtn = (Button) findViewById(R.id.deletebtn);
			deletebtn.setTag(0);
			resType = (TextView) findViewById(R.id.resType);
			IMRequest = (Button) findViewById(R.id.appsim);
			responseType = (Spinner) findViewById(R.id.responseType);
			message_val = (TextView) findViewById(R.id.message_val);
			message_val.setTag(0);
			touser_title = (TextView) findViewById(R.id.touser_title);
			pending_container = (ScrollView) findViewById(R.id.pending_container);
			settingscontainer = (ScrollView) findViewById(R.id.settings_container);
			responselay = (LinearLayout) findViewById(R.id.responselay);
			responselay.setTag(0);

			btn_patch = (Button) findViewById(R.id.clone_patch);

			value_img = (ImageView) findViewById(R.id.value_img);

			list = new ArrayList<String>();
			list.add("--Please Select--");
			list.add(SingleInstance.mainContext.getResources().getString(
					R.string.text));
			list.add(SingleInstance.mainContext.getResources().getString(
					R.string.photo));
			list.add(SingleInstance.mainContext.getResources().getString(
					R.string.audio));
			list.add(SingleInstance.mainContext.getResources().getString(
					R.string.video));

			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			responseType.setAdapter(dataAdapter);

			btn__save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					resignKeypad(ed_title);
					resignKeypad(ed_msgurl);
					position = (Integer) arg0.getTag();
					if (bean_map.containsKey(position))
						SaveConfig((Integer) arg0.getTag(), "edit");
					else
						SaveConfig((Integer) arg0.getTag(), "new");
					;
				}
			});

			clonenew.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					inflateNewUI(null, 0);
					settingscontainer.setVisibility(View.VISIBLE);
					if (settingscontainer != null) {
						settingscontainer.post(new Runnable() {

							@Override
							public void run() {
								settingscontainer
										.fullScroll(ScrollView.FOCUS_DOWN);
							}
						});
					}
					btn_patch.setText(Integer.toString(mainLayout
							.getChildCount()));

				}
			});
			btn_clearAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (avatarpage.equalsIgnoreCase("settings")) {
						showclearallAlert(SingleInstance.mainContext
								.getResources().getString(R.string.settings));
					} else if (avatarpage.equalsIgnoreCase("response")) {
						showclearallAlert(SingleInstance.mainContext
								.getResources().getString(R.string.response));
					} else if (avatarpage.equalsIgnoreCase("pending")) {
						showclearallAlert(SingleInstance.mainContext
								.getResources().getString(
										R.string.pending_avtaar));
					}
				}

			});
			deletebtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					position = 0;
					DeleteConfig(0);
					btn_patch.setText(Integer.toString(mainLayout
							.getChildCount()));

				}
			});

			IMRequest.setVisibility(View.INVISIBLE);
			// IMRequest.setBackgroundResource(R.drawable.one);

			Button btn_Settings = (Button) findViewById(R.id.btn_Settings);

			btn_Settings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					slidemenu.show();
				}
			});

			tv_actionbuddies = (TextView) findViewById(R.id.tv_buddyselection);
			tv_actionbuddies.setTag(0);
			tv_actionbuddies.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					Log.d("clone",
							"-----> buddy selection"
									+ type_map.containsKey(tag));
					if (type_map.containsKey(tag)) {
						String type = type_map.get(tag);
						if (type != null) {
							if (type.equalsIgnoreCase("Call Forwarding"))
								showSingelSelection(tag);
							else if (type.equalsIgnoreCase("Conference Call"))
								showmultiSelection(tag);
						}
					}
				}
			});
			tv_when = (TextView) findViewById(R.id.tv_when);
			tv_when.setTag(0);
			tv_when.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					showwhenOption(tag, tv_whenselected.getText().toString()
							.trim());
				}
			});
			tv_whenselected = (TextView) findViewById(R.id.tv_whenselected);
			tv_whenselected.setTag(0);
			tv_whenselected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					showwhenOption(tag, tv_whenselected.getText().toString()
							.trim());
				}
			});

			settings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "=====>Inside settings");
					avatarpage = "settings";
					if (settingscontainer.getVisibility() == View.GONE) {
						if (mainLayout.getChildCount() == 0) {
							inflateNewUI(null, 0);
						}

						settingscontainer.setVisibility(View.VISIBLE);
						pending_container.setVisibility(View.GONE);
						clonenew.setVisibility(View.VISIBLE);
						allresponsecontainer.setVisibility(View.GONE);
						settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
						settings.setTextColor(getResources().getColor(
								R.color.white));
						response.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						response.setTextColor(getResources().getColor(
								R.color.tabcolor));
						pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						pending.setTextColor(getResources().getColor(
								R.color.tabcolor));
						btn_patch.setText(Integer.toString(mainLayout
								.getChildCount()));

						Log.i("clone", "=====>Inside1 settings1");
						if (settingscontainer.getChildCount() > 0) {
							btn_clearAll.setVisibility(View.VISIBLE);
						} else {
							btn_clearAll.setVisibility(View.GONE);
						}
					}
				}
			});
			message_val.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "inside title");
					int tag = (Integer) v.getTag();
					showMessageDialog(tag);

				}
			});
			resType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					responseType.performClick();

				}
			});
			response.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					allresponseview_container.removeAllViews();
					avatarpage = "response";
					allresponsecontainer.setVisibility(View.VISIBLE);
					allresponseview_container.setVisibility(View.VISIBLE);
					pending_container.setVisibility(View.GONE);
					clonenew.setVisibility(View.INVISIBLE);
					settingscontainer.setVisibility(View.GONE);
					Log.i("clone", "=====>Inside1 pending");
					response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
					response.setTextColor(getResources()
							.getColor(R.color.white));
					settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					settings.setTextColor(getResources().getColor(
							R.color.tabcolor));
					pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					pending.setTextColor(getResources().getColor(
							R.color.tabcolor));
					populateAllResponses();

					if (allresponseview_container.getChildCount() > 0) {
						btn_clearAll.setVisibility(View.VISIBLE);
					} else {
						btn_clearAll.setVisibility(View.GONE);
					}
				}
			});

			pending.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "=====>Inside pending");
					avatarpage = "pending";
					if (pending_container.getVisibility() == View.GONE) {
						pending_container.setVisibility(View.VISIBLE);
						settingscontainer.setVisibility(View.GONE);
						clonenew.setVisibility(View.INVISIBLE);
						Log.i("clone", "=====>Inside1 pending");
						allresponsecontainer.setVisibility(View.GONE);
						pendingclone_container.removeAllViews();
						pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
						pending.setTextColor(getResources().getColor(
								R.color.white));
						settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						settings.setTextColor(getResources().getColor(
								R.color.tabcolor));
						response.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						response.setTextColor(getResources().getColor(
								R.color.tabcolor));
						populatePendingclones();
					}
				}
			});
			loadConfigs();
			if (pending_container.getChildCount() > 0) {
				btn_clearAll.setVisibility(View.VISIBLE);
			} else {
				btn_clearAll.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
		} catch (OutOfMemoryError o) {
			// TODO: handle exception
			showToast("Out of Memory");
			if (AppReference.isWriteInFile)
				AppReference.logger.error(o.getMessage(), o);

		}

	}

	public void loadConfigs() {
		try {
			ArrayList<OfflineRequestConfigBean> config_list = new ArrayList<OfflineRequestConfigBean>();
			if (config_list.size() > 0) {
				config_list.clear();
			}
			config_list = callDisp.getdbHeler(context)
					.getOfflineSettingDetails(
							"where userid=" + "\"" + CallDispatcher.LoginUser
									+ "\"");

			if (config_list != null) {
				bean_map.clear();
				for (int i = 0; i < config_list.size(); i++) {

					Log.d("clone", "List size is -->" + config_list.size()
							+ " .. i value--->" + i);
					OfflineRequestConfigBean bean = (OfflineRequestConfigBean) config_list
							.get(i);

					if (i == 0) {

						if (bean != null) {
							if (bean.getResponseType() != null)
								responseType
										.setSelection(getSelectedtypePosition(bean
												.getResponseType()));
							if (bean.getBuddyId() != null)
								input_to.setSelection(getBuddyPosition(bean
										.getBuddyId()));
							input_to.setEnabled(false);
							touser_title.setEnabled(false);

							if (bean.getMessageTitle() != null)
								ed_title.setText(bean.getMessageTitle());

							if (bean.getUrl() != null) {
								if (!bean.getUrl().equals("''"))
									ed_msgurl.setText(bean.getUrl());
							}
						}
						String when = "";
						if (bean.getWhen() != null) {
							String[] when_ids = bean.getWhen().split(",");
							for (String when_id : when_ids) {
								String c_description = callDisp.getdbHeler(
										context).getwheninfo(
										"select cdescription from clonemaster where cid=\""
												+ when_id + "\"");
								if (when.trim().length() == 0)
									when = c_description;
								else
									when = when + "," + c_description;
							}
						} else if (bean.getWhen() == null) {
							when = "All";
						}
						tv_whenselected.setText(when);
						getMessageTypeView(bean.getMessagetype(),
								bean.getMessage(), i);
						String path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + bean.getMessage();
						if (bean.getMessagetype().equalsIgnoreCase("text")
								|| bean.getMessagetype().equalsIgnoreCase(
										"audio")
								|| bean.getMessagetype().equalsIgnoreCase(
										"video")
								|| bean.getMessagetype().equalsIgnoreCase(
										"photo"))
							path_map.put(i, path);
						else
							path_map.put(i, bean.getMessage());

						btn__save.setText(SingleInstance.mainContext
								.getResources().getString(R.string.update));
						// type_map.put(i, bean.getResponseType());

					} else {
						String path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + bean.getMessage();

						if (bean.getMessagetype().equalsIgnoreCase("text")
								|| bean.getMessagetype().equalsIgnoreCase(
										"audio")
								|| bean.getMessagetype().equalsIgnoreCase(
										"video")
								|| bean.getMessagetype().equalsIgnoreCase(
										"photo"))
							path_map.put(i, path);
						else
							path_map.put(i, bean.getMessage());
						// type_map.put(i, bean.getResponseType());
						inflateNewUI(bean, 1);
						// inflateNewUI(bean, 0);
					}

					bean_map.put(i, bean);

				}
			}
			btn_patch.setText(Integer.toString(mainLayout.getChildCount()));
		} catch (Exception e) {
			Log.e("clone", "=====> exception in load config" + e.getMessage());
		}
	}

	private ProgressDialog dialog = null;

	private void showprogress() {

		dialog = new ProgressDialog(context);
		dialog.setCancelable(true);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();

	}

	private void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("NOTES", "Selected positiob--->" + position);
		Log.d("NOTES", "Path--->" + getComponentPath);
		LinearLayout parent_view = (LinearLayout) mainLayout
				.getChildAt(position);
		if (requestCode == 2) {

			if (resultCode == Activity.RESULT_CANCELED) {
				if (bitmap != null) {
					value_img.setImageBitmap(bitmap);
					value_img.setVisibility(View.VISIBLE);
					value_img.setPadding(10, 10, 10, 10);
				} else {
					value_img.setVisibility(View.GONE);
				}

			} else if (data != null) {

				Uri selectedImageUri = data.getData();
				strIPath = callDisp.getRealPathFromURI(selectedImageUri);
				File selected_file = new File(strIPath);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);

				if (length <= 2) {

					if (img != null) {

					}
					img = null;
					img = callDisp.ResizeImage(strIPath);

					if (img != null) {
						img = Bitmap.createScaledBitmap(img, 100, 75, false);
						if (bitmap != null) {

						}

						bitmap = null;

						bitmap = callDisp.ResizeImage(strIPath);
						final String path = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ callDisp.getFileName() + ".jpg";

						BufferedOutputStream stream;
						try {
							stream = new BufferedOutputStream(
									new FileOutputStream(new File(path)));
							if (bitmap != null) {
								bitmap.compress(CompressFormat.JPEG, 100,
										stream);
							}
							getComponentPath = path;

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (bitmap != null) {
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
						}
						addRemovedFiles(position, "settings_");
						path_map.put(position, path);

						// }
						final ImageView value_img;
						TextView value_text, select_buddies;
						EditText ed_gsm;

						if (parent_view != null) {
							value_img = (ImageView) parent_view
									.findViewById(R.id.value_img);
							value_text = (TextView) parent_view
									.findViewById(R.id.value_txt);
							LinearLayout audio_layout = (LinearLayout) parent_view
									.findViewById(R.id.audio_layout);
							select_buddies = (TextView) parent_view
									.findViewById(R.id.tv_buddyselection);
							select_buddies.setVisibility(View.GONE);

							ed_gsm = (EditText) parent_view
									.findViewById(R.id.ed_gsmno);
							ed_gsm.setVisibility(View.GONE);

							if (bitmap != null)
								value_img.setImageBitmap(bitmap);
							value_img.setVisibility(View.VISIBLE);
							value_img.setPadding(10, 10, 10, 10);
							value_img.setTag(path);
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
							if (audio_layout != null) {
								if (audio_layout.getVisibility() == View.VISIBLE) {
									audio_layout.removeAllViews();
									audio_layout.setVisibility(View.GONE);
								}
							}
							final File vFile = new File(path);
							value_img.setTag(path);
							value_img.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (vFile.exists()) {
										Intent in = new Intent(context,
												PhotoZoomActivity.class);
										in.putExtra("Photo_path", value_img
												.getTag().toString());
										in.putExtra("type", false);
										in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(in);
									}
								}
							});

						} else {
							strIPath = null;
						}
					}
				}

			}

		}

		else if (requestCode == 19) {

			if (resultCode == Activity.RESULT_CANCELED) {
				value_img.setVisibility(View.GONE);

			} else if (data != null) {

				Uri selectedImageUri = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				getContentResolver().takePersistableUriPermission(
						selectedImageUri, takeFlags);
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + getFileName() + ".jpg";
				addRemovedFiles(position, "settings_");
				path_map.put(position, strIPath);
				File selected_file = new File(strIPath);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);
				if (length <= 2) {
					if (img != null) {

					}
					img = null;
					new bitmaploader().execute(selectedImageUri);
				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.large_image));
				}

			}

		} else if (requestCode == 0) {
			if (resultCode == Activity.RESULT_CANCELED) {
				value_img.setVisibility(View.GONE);

			} else {
				File fileCheck = new File(strIPath);
				if (fileCheck.exists()) {
					bitmap = callDisp.ResizeImage(strIPath);
					callDisp.changemyPictureOrientation(bitmap, strIPath);
					if (bitmap != null && !bitmap.isRecycled())
						bitmap.recycle();
					bitmap = null;
					bitmap = callDisp.ResizeImage(strIPath);
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
					// }
					addRemovedFiles(position, "settings_");
					path_map.put(position, strIPath);
					LinearLayout parent_view1 = (LinearLayout) mainLayout
							.getChildAt(position);
					ImageView value_img;
					TextView value_text, select_buddies;
					EditText ed_gsm;

					if (parent_view1 != null) {
						value_img = (ImageView) parent_view1
								.findViewById(R.id.value_img);
						value_text = (TextView) parent_view1
								.findViewById(R.id.value_txt);
						LinearLayout audio_layout = (LinearLayout) parent_view
								.findViewById(R.id.audio_layout);
						select_buddies = (TextView) parent_view
								.findViewById(R.id.tv_buddyselection);
						select_buddies.setVisibility(View.GONE);
						ed_gsm = (EditText) parent_view
								.findViewById(R.id.ed_gsmno);
						ed_gsm.setVisibility(View.GONE);

						if (bitmap != null) {
							value_img.setVisibility(View.VISIBLE);
							value_img.setImageBitmap(bitmap);
							value_img.setPadding(10, 10, 10, 10);
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}

							if (audio_layout != null) {
								if (audio_layout.getVisibility() == View.VISIBLE) {
									audio_layout.removeAllViews();
									audio_layout.setVisibility(View.GONE);
								}
							}
						}
					}
				}
			}
		} else if (requestCode == 25) {

			if (getComponentPath != null) {
				Log.i("clone", "===> + " + getComponentPath.length());
				addRemovedFiles(position, "settings_");
				path_map.put(position, getComponentPath);
				LinearLayout parent_view3 = (LinearLayout) mainLayout
						.getChildAt(position);
				ImageView value_img;
				TextView value_text, select_buddies;
				LinearLayout audio_layout;
				EditText ed_gsm;

				if (parent_view3 != null) {
					value_img = (ImageView) parent_view3
							.findViewById(R.id.value_img);
					value_text = (TextView) parent_view3
							.findViewById(R.id.value_txt);
					audio_layout = (LinearLayout) parent_view
							.findViewById(R.id.audio_layout);

					select_buddies = (TextView) parent_view
							.findViewById(R.id.tv_buddyselection);
					select_buddies.setVisibility(View.GONE);

					ed_gsm = (EditText) parent_view.findViewById(R.id.ed_gsmno);
					ed_gsm.setVisibility(View.GONE);

					Log.i("clone", "File ====>" + getComponentPath);
					if (type.equalsIgnoreCase("text")) {
						if (getComponentPath.endsWith(".txt")) {
							if (value_img.getVisibility() == View.VISIBLE) {
								value_img.setVisibility(View.GONE);
							}
							if (audio_layout != null) {
								if (audio_layout.getVisibility() == View.VISIBLE) {
									audio_layout.removeAllViews();
									audio_layout.setVisibility(View.GONE);
								}
							}

							File txt_file = new File(getComponentPath);
							if (txt_file.exists()) {
								try {
									FileInputStream stream = new FileInputStream(
											txt_file);
									BufferedReader reader = new BufferedReader(
											new InputStreamReader(stream));
									String line;
									StringBuffer buffer = new StringBuffer();
									while ((line = reader.readLine()) != null) {
										buffer.append(line);
										buffer.append("\n");

									}
									value_text.setText(buffer.toString());
									value_text.setTextColor(Color.BLACK);
									value_text.setVisibility(View.VISIBLE);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					}
					if (type.equalsIgnoreCase("audio")) {
						if (value_text.getVisibility() == View.VISIBLE) {
							value_text.setVisibility(View.GONE);
						}
						value_img.setVisibility(View.VISIBLE);
						value_img.setPadding(30, 10, 30, 0);
						value_img.setImageDrawable(this.getResources()
								.getDrawable(R.drawable.v_play));

						final ImageView img = value_img;

						Log.i("bug", "INSIDE PHOTO TRUE");
						final File vfileCheck = new File(getComponentPath);
						chatplayer = new MediaPlayer();

						value_img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (vfileCheck.exists()) {

									try {
										Intent intent = new Intent(context,
												MultimediaUtils.class);
										intent.putExtra("filePath",
												getComponentPath);
										intent.putExtra("requestCode", 25);
										intent.putExtra("action", "audio");
										intent.putExtra("createOrOpen", "open");
										startActivity(intent);
									} catch (IllegalArgumentException e) {
										// TODO Auto-generated catch
										// block
										e.printStackTrace();
									} catch (IllegalStateException e) {
										// TODO Auto-generated catch
										// block
										e.printStackTrace();
									}

									chatplayer
											.setOnCompletionListener(new OnCompletionListener() {

												@Override
												public void onCompletion(
														MediaPlayer mp) {
													// TODO
													// Auto-generated
													// method stub
													Log.i("log",
															"on completion listener start");
													try {
														chatplayer.reset();
														if (bitmap != null) {
															// if (!bitmap
															// .isRecycled())
															// {
															// bitmap.recycle();
															// }
														}

														bitmap = BitmapFactory
																.decodeResource(
																		getResources(),
																		R.drawable.v_play);
														img.setImageBitmap(bitmap);

													} catch (IllegalArgumentException e) {
														// TODO
														// Auto-generated
														// catch
														// block
														e.printStackTrace();
													} catch (IllegalStateException e) {
														// TODO
														// Auto-generated
														// catch
														// block
														e.printStackTrace();
													}

												}
											});

								} else
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.file_downloading),
											1).show();

							}
						});

					}
					if (type.equalsIgnoreCase("video")) {
						if (value_text.getVisibility() == View.VISIBLE) {
							value_text.setVisibility(View.GONE);
						}
						if (audio_layout != null) {
							if (audio_layout.getVisibility() == View.VISIBLE) {
								audio_layout.removeAllViews();
								audio_layout.setVisibility(View.GONE);
							}
						}
						if (resultCode == Activity.RESULT_CANCELED)
							value_img.setVisibility(View.GONE);
						else
							value_img.setVisibility(View.VISIBLE);

						Log.i("bug", "INSIDE PHOTO TRUE");

						value_img.setPadding(30, 10, 30, 0);
						value_img.setImageDrawable(this.getResources()
								.getDrawable(R.drawable.v_play));

						final ImageView im = value_img;
						im.setTag(getComponentPath);
						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String path = (String) v.getTag();
								final File vfileCheck = new File(path);
								Log.d("clone", "File path--->" + path);
								Log.d("clone",
										"file exists--->" + vfileCheck.exists());

								if (vfileCheck.exists()) {
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
									intentVPlayer.putExtra("video", path);
									// intentVPlayer.putExtra("Player_Type",
									// "Video Player");
									startActivity(intentVPlayer);
								} else {

									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.file_downloading),
											1).show();

								}

							}
						});

					}
				}

			}
		}
		if (requestCode >= 30) {
			LinearLayout parent_view1 = null;
			for (int i = 0; i < pendingclone_container.getChildCount(); i++) {

				parent_view1 = (LinearLayout) pendingclone_container
						.getChildAt(i);
				if (parent_view != null) {
					if ((Integer) parent_view.getTag() == position)
						break;
				}
			}
			ImageView value_img;
			TextView value_text;

			value_img = (ImageView) parent_view1.findViewById(R.id.value_img);
			value_text = (TextView) parent_view1.findViewById(R.id.value_txt);
			if (requestCode == 30) {

				if (resultCode == Activity.RESULT_CANCELED) {
					Log.d("Avataar", "Rsult canceled");
				} else if (data != null) {

					Uri selectedImageUri = data.getData();
					strIPath = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {

						if (img != null) {
							// if (!img.isRecycled()) {
							// img.recycle();
							// System.gc();
							// }
						}
						img = null;
						img = callDisp.ResizeImage(strIPath);
						callDisp.changemyPictureOrientation(img, strIPath);
						img = Bitmap.createScaledBitmap(img, 100, 75, false);

						if (img != null) {

							// if (bitmap != null) {
							// if (!bitmap.isRecycled()) {
							// bitmap.recycle();
							// }
							// }

							bitmap = null;

							// if (fileCheck.exists()) {
							bitmap = callDisp.ResizeImage(strIPath);
							String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ callDisp.getFileName()
									+ ".jpg";

							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(path)));
								bitmap.compress(CompressFormat.JPEG, 100,
										stream);

								getComponentPath = path;

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							DeleteExistingpendingclonefile(position);
							responsepath_map.put(position, path);

							// }

							if (parent_view1 != null) {
								value_img = (ImageView) parent_view1
										.findViewById(R.id.res_img_from);
								value_text = (TextView) parent_view1
										.findViewById(R.id.res_txt_from);

								if (bitmap != null)
									value_img.setImageBitmap(bitmap);
								value_img.setVisibility(View.VISIBLE);
								value_img.setPadding(10, 10, 10, 10);
								if (value_text.getVisibility() == View.VISIBLE) {
									value_text.setVisibility(View.GONE);
								}

							} else {
								strIPath = null;
							}
						}
					}

				}

			} else if (requestCode == 31) {

				if (resultCode == Activity.RESULT_CANCELED) {

				} else if (data != null) {

					Uri selectedImageUri = data.getData();
					final int takeFlags = data.getFlags()
							& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					getContentResolver().takePersistableUriPermission(
							selectedImageUri, takeFlags);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + getFileName() + ".jpg";
					DeleteExistingpendingclonefile(position);
					responsepath_map.put(position, strIPath);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						// if (img != null) {
						// if (!img.isRecycled()) {
						// img.recycle();
						// System.gc();
						// }
						// }
						img = null;
						new bitmaploader().execute(selectedImageUri);
					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.large_image));
					}

				}
			} else if (requestCode == 32) {

				if (resultCode == Activity.RESULT_CANCELED) {

				} else {
					File fileCheck = new File(strIPath);
					if (fileCheck.exists()) {
						callDisp.changemyPictureOrientation(bitmap, strIPath);
						bitmap = callDisp.ResizeImage(strIPath);
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
						// }
						DeleteExistingpendingclonefile(position);
						responsepath_map.put(position, strIPath);

						if (parent_view1 != null) {
							value_img = (ImageView) parent_view1
									.findViewById(R.id.res_img_from);
							value_text = (TextView) parent_view1
									.findViewById(R.id.res_txt_from);

							if (bitmap != null) {
								value_img.setVisibility(View.VISIBLE);
								value_img.setImageBitmap(bitmap);

								value_img.setPadding(10, 10, 10, 10);
								if (value_text.getVisibility() == View.VISIBLE) {
									value_text.setVisibility(View.GONE);
								}
							}
						}
					}
				}

			} else if (requestCode == 33) {

				if (getComponentPath != null) {

					DeleteExistingpendingclonefile(position);
					responsepath_map.put(position, getComponentPath);

					if (parent_view1 != null) {
						value_img = (ImageView) parent_view1
								.findViewById(R.id.res_img_from);
						value_text = (TextView) parent_view1
								.findViewById(R.id.res_txt_from);

						Log.i("clone", "File ====>" + getComponentPath);
						if (type.equalsIgnoreCase("text")) {
							if (value_img.getVisibility() == View.VISIBLE) {
								value_img.setVisibility(View.GONE);

							}
							File txt_file = new File(getComponentPath);
							if (txt_file.exists()) {
								try {
									FileInputStream stream = new FileInputStream(
											txt_file);
									BufferedReader reader = new BufferedReader(
											new InputStreamReader(stream));
									String line;
									StringBuffer buffer = new StringBuffer();
									while ((line = reader.readLine()) != null) {
										buffer.append(line);
										buffer.append("\n");

									}
									value_text.setText(buffer.toString());
									value_text.setTextColor(Color.BLACK);
									value_text.setVisibility(View.VISIBLE);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
						if (type.equalsIgnoreCase("audio")) {
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
							value_img.setVisibility(View.VISIBLE);
							value_img.setPadding(30, 10, 30, 0);
							value_img.setImageDrawable(this.getResources()
									.getDrawable(R.drawable.v_play));

							final ImageView img = value_img;

							Log.i("bug", "INSIDE PHOTO TRUE");
							final File vfileCheck = new File(getComponentPath);
							chatplayer = new MediaPlayer();

							value_img.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method
									// stub
									if (vfileCheck.exists()) {

										try {
											Intent intent = new Intent(context,
													MultimediaUtils.class);
											intent.putExtra("filePath",
													getComponentPath);
											intent.putExtra("requestCode", 33);
											intent.putExtra("action", "audio");
											intent.putExtra("createOrOpen",
													"open");
											startActivity(intent);

										} catch (IllegalArgumentException e) {
											// TODO Auto-generated
											// catch
											// block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO Auto-generated
											// catch
											// block
											e.printStackTrace();
										}

										chatplayer
												.setOnCompletionListener(new OnCompletionListener() {

													@Override
													public void onCompletion(
															MediaPlayer mp) {
														// TODO
														// Auto-generated
														// method
														// stub
														Log.i("log",
																"on completion listener start");
														try {
															chatplayer.reset();
															// if
															// (bitmap
															// !=
															// null)
															// {
															// if
															// (!bitmap
															// .isRecycled())
															// {
															// bitmap.recycle();
															// }
															// }

															bitmap = BitmapFactory
																	.decodeResource(
																			getResources(),
																			R.drawable.v_play);
															img.setImageBitmap(bitmap);

														} catch (IllegalArgumentException e) {
															// TODO
															// Auto-generated
															// catch
															// block
															e.printStackTrace();
														} catch (IllegalStateException e) {
															// TODO
															// Auto-generated
															// catch
															// block
															e.printStackTrace();
														}

													}
												});

									} else
										Toast.makeText(
												context,
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.file_downloading),
												1).show();

								}
							});

						}
						if (type.equalsIgnoreCase("video")) {
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
							value_img.setVisibility(View.VISIBLE);
							Log.i("bug", "INSIDE PHOTO TRUE");
							if (!getComponentPath.endsWith(".mp4")) {
								getComponentPath = getComponentPath + ".mp4";
							}
							final File vfileCheck = new File(getComponentPath);
							value_img.setPadding(30, 10, 30, 0);
							value_img.setImageDrawable(this.getResources()
									.getDrawable(R.drawable.v_play));

							final ImageView im = value_img;

							im.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (vfileCheck.exists()) {
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// getComponentPath);
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video",
												getComponentPath);
										startActivity(intentVPlayer);
									} else

										Toast.makeText(
												context,
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.file_downloading),
												1).show();
								}
							});
						}
					}

				}
			}

		}

	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				cancelDialog();
				if (strIPath != null)
					img = callDisp.ResizeImage(strIPath);
				if (img != null) {

					File fle = new File(strIPath);
					fle.createNewFile();
					FileOutputStream fout = new FileOutputStream(strIPath);
					img.compress(CompressFormat.JPEG, 75, fout);

					img = Bitmap.createScaledBitmap(img, 100, 75, false);
					Log.d("OnActivity", "_____On Activity Called______");

					bitmap = null;

					bitmap = callDisp.ResizeImage(strIPath);
					if (bitmap != null) {
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
					}
					LinearLayout parent_view = (LinearLayout) mainLayout
							.getChildAt(position);
					ImageView value_img;
					TextView value_text;

					if (parent_view != null) {
						value_img = (ImageView) parent_view
								.findViewById(R.id.value_img);
						value_text = (TextView) parent_view
								.findViewById(R.id.value_txt);

						if (bitmap != null) {
							value_img.setVisibility(View.VISIBLE);
							value_img.setImageBitmap(bitmap);
							value_img.setPadding(10, 10, 10, 10);
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
							LinearLayout audio_layout = (LinearLayout) parent_view
									.findViewById(R.id.audio_layout);
							if (audio_layout != null) {
								if (audio_layout.getVisibility() == View.VISIBLE) {
									audio_layout.removeAllViews();
									audio_layout.setVisibility(View.GONE);
								}
							}
						}
					}
				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showprogress();
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + callDisp.getFileName() + ".jpg";
					FileInputStream fin = (FileInputStream) getContentResolver()
							.openInputStream(uri);
					ByteArrayOutputStream straam = new ByteArrayOutputStream();
					byte[] content = new byte[1024];
					int bytesread;
					while ((bytesread = fin.read(content)) != -1) {
						straam.write(content, 0, bytesread);
					}
					byte[] bytes = straam.toByteArray();
					FileOutputStream fout = new FileOutputStream(strIPath);
					straam.flush();
					straam.close();
					straam = null;
					fin.close();
					fin = null;
					fout.write(bytes);
					fout.flush();
					fout.close();
					fout = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

	public String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
		if (WebServiceReferences.Imcollection.size() == 0)
			btn_im.setVisibility(View.GONE);
		else
			btn_im.setVisibility(View.VISIBLE);
	}

	public void ShowImMembers(final ArrayList al, View v, final int[] ix,
			final HashMap<String, String> hs) {

	}

	protected void ShowList() {
		try {
			// TODO Auto-generated method stub

			setContentView(R.layout.history_container);

			slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
			ArrayList<Slidebean> datas = new ArrayList<Slidebean>();
			callDisp.composeList(datas);
			slidemenu.init(CloneActivity.this, datas, CloneActivity.this, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyServiceError(String msg) {
		Log.d("Notes", "came to notifyServiceError");
		if (progress_dialog != null) {
			if (progress_dialog.isShowing())
				callDisp.cancelDialog();
		}
		ShowError("Error", msg);
	}

	public void notifyWebresponse(Object obj) {
		Log.d("Notes", "came to notifywebresponse");
		if (progress_dialog != null)
			callDisp.cancelDialog();

		if (obj instanceof OfflineConfigurationBean) {
			OfflineConfigurationBean result1 = (OfflineConfigurationBean) obj;
			if (result1.getList().size() > 0) {
				OfflineConfigResponseBean result = result1.getList().get(0);
				if (result.getText().endsWith("Successfully")) {
					if (iscleared) {
						if (avatarpage != null) {
							if (avatarpage.equalsIgnoreCase("settings")) {
								for (Entry<Integer, OfflineRequestConfigBean> set : bean_map
										.entrySet()) {
									OfflineRequestConfigBean bean = set
											.getValue();
									callDisp.getdbHeler(context)
											.deleteOfflineCallSettingDetails(
													bean.getId());
									String filepath = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/" + bean.getMessage();
									File fle = new File(filepath);
									if (fle.exists())
										fle.delete();
									type_map.remove(set.getKey());
									path_map.remove(set.getKey());
									DeleteExistingfile(set.getKey(),
											"settings_");
								}
								bean_map.clear();
								btn_patch.setText("0");
							}
						}
						iscleared = false;
					} else {
						Log.i("clone", "Came to if" + send_baen);
						if (send_baen != null) {
							if (send_baen.getMode().equals("edit")) {
								ContentValues cv = new ContentValues();
								cv.put("userid", CallDispatcher.LoginUser);
								if (send_baen.getBuddyId().equals(
										CallDispatcher.LoginUser))
									cv.put("buddyid", "default");
								else
									cv.put("buddyid", send_baen.getBuddyId());

								cv.put("message_title",
										send_baen.getMessageTitle());
								cv.put("message_type",
										send_baen.getMessagetype());
								cv.put("message", send_baen.getMessage());
								cv.put("when_action", send_baen.getWhen());
								if (send_baen.getResponseType() != null) {
									if (send_baen.getResponseType().trim()
											.length() != 0)
										cv.put("response_type",
												send_baen.getResponseType());
									else
										cv.put("response_type", "''");
								} else
									cv.put("response_type", "''");

								if (send_baen.getUrl() == null)
									cv.put("url", "''");
								else
									cv.put("url", send_baen.getUrl());

								if (result.getRecordTime() == null)
									cv.put("record_time", "''");
								else
									cv.put("record_time",
											result.getRecordTime());

								int row_id = callDisp.getdbHeler(context)
										.updateOfflineCallSettingsDetails(cv,
												"Id=" + result.getId());

								Log.d("NOTES", "Inserted row id--->" + row_id);
								showToast(result.getText());

								send_baen.setId(result.getId());
								bean_map.put(position, send_baen);
								String oldMessage = path_map.get(position);
								if (!offlineRequestConfigBean.getMessage()
										.equalsIgnoreCase(oldMessage)) {
									uploadofflineResponse(
											offlineRequestConfigBean
													.getMessage(),
											false, send_baen, "call_settings",
											send_baen.getMode());
									DeleteExistingfile(position, "settings_");

								}
								if (values.containsKey(position)) {
									Button save = values.get(position);
									save.setText("Update");
								}

							} else if (send_baen.getMode().equals("delete")) {
								if (CallDispatcher.clearAll) {
									callDisp.getdbHeler(context)
											.deleteOfflineCallSettingDetails(
													send_baen.getId());
									for (int i = 0; i < mainLayout
											.getChildCount(); i++) {
										LinearLayout parent_view = (LinearLayout) mainLayout
												.getChildAt(i);
										Button btn = (Button) parent_view
												.findViewById(R.id.deletebtn);
										int tag = (Integer) btn.getTag();
										Log.d("NOTES", "---->" + tag);
										Log.d("NOTES", "---->" + i);
										Log.d("NOTES", "received tg---->"
												+ position);
										if (tag == position) {
											mainLayout.removeViewAt(i);
											break;
										}
									}
									String filepath = Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/"
											+ send_baen.getMessage();
									File fle = new File(filepath);
									if (fle.exists())
										fle.delete();
									bean_map.remove(position);
									type_map.remove(position);
									path_map.remove(position);
									btn_patch.setText(Integer
											.toString(mainLayout
													.getChildCount()));
									DeleteExistingfile(position, "settings_");
								}
							} else if (send_baen.getMode().equals("new")) {
								ContentValues cv = new ContentValues();
								cv.put("Id", result.getId());
								cv.put("userid", CallDispatcher.LoginUser);
								if (send_baen.getBuddyId().equals(
										CallDispatcher.LoginUser))
									cv.put("buddyid", "default");
								else
									cv.put("buddyid", send_baen.getBuddyId());

								cv.put("message_title",
										send_baen.getMessageTitle());
								cv.put("message_type",
										send_baen.getMessagetype());
								cv.put("message", send_baen.getMessage());
								cv.put("when_action", send_baen.getWhen());
								if (send_baen.getResponseType() != null) {
									if (send_baen.getResponseType().trim()
											.length() != 0)
										cv.put("response_type",
												send_baen.getResponseType());
									else
										cv.put("response_type", "''");
								} else
									cv.put("response_type", "''");

								if (send_baen.getUrl() == null)
									cv.put("url", "''");
								else
									cv.put("url", send_baen.getUrl());
								if (result.getRecordTime() == null)
									cv.put("record_time", "''");
								else
									cv.put("record_time",
											result.getRecordTime());
								int row_id = callDisp.getdbHeler(context)
										.insertOfflineCallSettingDetails(cv);

								Log.d("NOTES", "Inserted row id--->" + row_id);
								showToast(result.getText());
								send_baen.setId(result.getId());
								bean_map.put(position, send_baen);
								if (values.containsKey(position)) {
									Button save = values.get(position);
									save.setText("Update");
								}

								if (send_baen.getMessagetype()
										.equalsIgnoreCase("text")
										|| send_baen.getMessagetype()
												.equalsIgnoreCase("audio")
										|| send_baen.getMessagetype()
												.equalsIgnoreCase("video")
										|| send_baen.getMessagetype()
												.equalsIgnoreCase("photo")) {
									if (offlineRequestConfigBean.getMessage() != null
											&& offlineRequestConfigBean
													.getMessage().length() > 0)
										uploadofflineResponse(
												offlineRequestConfigBean
														.getMessage(),
												false, send_baen,
												"call_settings", send_baen
														.getMode());
								}

							}
						}
					}

				} else
					showToast(result.getText());
			}

		} else if (obj instanceof WebServiceBean) {
			WebServiceBean ws_bean = (WebServiceBean) obj;
			ShowError("Error", ws_bean.getText());
			// DeleteExistingfile(position, "settings_");
		}

	}

	private String profile_buddy;

	private String profile_buddystatus;

	public void notifyViewProfile() {
		if (profile_buddy != null) {
			callDisp.cancelDialog();
			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(profile_buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				if (!WebServiceReferences.contextTable
						.containsKey("viewprofile")) {
					// Intent intent = new Intent(context, ViewProfiles.class);
					// intent.putExtra("buddyname", profile_buddy);
					// intent.putExtra("close", true);
					// intent.putExtra("buddystatus", profile_buddystatus);
					// startActivity(intent);
					viewProfile();

				} else {
					(ViewProfileFragment.newInstance(context)).records_container
							.removeAllViews();
					(ViewProfileFragment.newInstance(context)).initView();
				}
			} else
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_profile_assigned_for_this_user),
						Toast.LENGTH_SHORT).show();
		}
	}

	private void viewProfile() {
		ViewProfileFragment viewProfileFragment = ViewProfileFragment
				.newInstance(context);
		Bundle bundle = new Bundle();
		bundle.putString("buddyname", profile_buddy);
		viewProfileFragment.setArguments(bundle);
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");
		FragmentManager fragmentManager = appMainActivity
				.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.activity_main_content_fragment,
				viewProfileFragment);
		fragmentTransaction.commit();
	}

	void doViewProfile(boolean accept, String buddy, String status) {

		try {

			profile_buddy = buddy;

			profile_buddystatus = status;

			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				// Intent intent = new Intent(context, ViewProfiles.class);
				// intent.putExtra("buddyname", buddy);
				// intent.putExtra("buddystatus", status);
				// intent.putExtra("close", true);
				// startActivity(intent);
				viewProfile();

			} else {
				Log.i("profile", "VIEW PROFILE------>" + buddy
						+ "---->GetProfileDetails");
				CallDispatcher.pdialog = new ProgressDialog(context);
				callDisp.showprogress(CallDispatcher.pdialog, context);
				CallDispatcher.isFromCallDisp = false;
				String modifiedDate = callDisp.getdbHeler(context)
						.getModifiedDate("5");
				if (modifiedDate == null) {
					modifiedDate = "";
				} else if (modifiedDate.trim().length() == 0) {
					modifiedDate = "";
				}
				String[] profileDetails = new String[3];
				profileDetails[0] = buddy;
				profileDetails[1] = "5";
				profileDetails[2] = modifiedDate;
				WebServiceReferences.webServiceClient
						.getStandardProfilefieldvalues(profileDetails);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyDownloadStatus(final String id, final String filename,
			final boolean status, final String from) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("Debug", "notifyDownloadStatus --->" + id + " -->"
						+ filename + " -->" + status + " -->" + from);

				if (from.equals("offline response")) {
					int tag;
					OfflineRequestConfigBean bean = null;
					for (Map.Entry<Integer, OfflineRequestConfigBean> entry : responsebean_map
							.entrySet()) {
						tag = entry.getKey();
						bean = entry.getValue();
						if (bean.getId().equals(id)) {
							if (status) {
								for (int i = 0; i < responselay.getChildCount(); i++) {
									LinearLayout layout = (LinearLayout) allresponseview_container
											.getChildAt(i);
									if ((Integer) layout.getTag() == tag) {
										ImageView res_image = (ImageView) layout
												.findViewById(R.id.response_value_img_from);
										TextView tv_resvalue = (TextView) layout
												.findViewById(R.id.response_value_txt_from);

										setallResponseMessageTypeView(
												bean.getResponseType(),
												tv_resvalue, res_image,
												bean.getResponse(), tag);

										break;
									}
								}
							}

							break;
						}
					}
					if (bean != null) {
						if (status) {
							bean.setStatus(2);
						} else {
							bean.setStatus(0);
						}
						ContentValues cv = new ContentValues();
						cv.put("status", bean.getStatus());
						callDisp.getdbHeler(context)
								.updateOfflineCallResponseDetails(cv,
										"id=" + bean.getId());
						cv = null;
					}
				} else if (from.equals("offline settings")) {
					int tag;
					OfflineRequestConfigBean bean = null;
					Log.d("Debug",
							"Insde offline call settings " + clone_bean.size());
					for (Map.Entry<Integer, OfflineRequestConfigBean> entry : bean_map
							.entrySet()) {
						tag = entry.getKey();
						bean = entry.getValue();
						Log.d("Debug",
								"Insde offline call settings " + bean.getId()
										+ " -->" + id);
						if (bean.getId().equals(id)) {
							Log.d("Debug",
									"Insde offline call settings "
											+ bean.getMessagetype() + " -->"
											+ bean.getMessage() + " -->" + tag);
							position = tag;
							getMessageTypeView(bean.getMessagetype(),
									bean.getMessage(), position);

							break;
						}
					}
					if (bean != null) {
						if (status) {
							bean.setStatus(2);
						} else {
							bean.setStatus(0);
						}
						ContentValues cv = new ContentValues();
						cv.put("status", bean.getStatus());
						callDisp.getdbHeler(context)
								.updateOfflineCallSettingsDetails(cv,
										"id=" + bean.getId());
						cv = null;
					}

				} else if (from.equals("answering machine")) {
					int tag;
					OfflineRequestConfigBean bean = null;
					for (Map.Entry<Integer, OfflineRequestConfigBean> entry : clone_bean
							.entrySet()) {
						tag = entry.getKey();
						bean = entry.getValue();
						if (bean.getId().equals(id)) {
							position = tag;
							getPendingMessageTypeView(bean.getMessagetype(),
									bean.getMessage(), position);
							break;
						}
					}
					if (bean != null) {
						if (status) {
							bean.setStatus(2);
						} else {
							bean.setStatus(0);
						}
						ContentValues cv = new ContentValues();
						cv.put("status", bean.getStatus());
						callDisp.getdbHeler(context)
								.updateOfflineCallPendingClones(cv,
										"id=" + bean.getId());
						cv = null;
					}

				}

			}
		});

	}

	@Override
	protected void onDestroy() {

		try {
			Log.e("lg", "on destroy of buddyview1????????????????");
			if (WebServiceReferences.contextTable.containsKey("clone")) {
				WebServiceReferences.contextTable.remove("clone");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	AlertDialog confirmation = null;

	public void ShowError(String Title, String Message) {
		if (confirmation != null) {
			confirmation.dismiss();
		}
		confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		confirmation.show();
	}

	public void putxmlobj(String key, Object obj) {
		if (xmlmap.containsKey(key)) {
			xmlmap.remove(key);
			xmlmap.put(key, obj);
		} else {
			xmlmap.put(key, obj);
		}
	}

	private void showclearallAlert(String message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle(SingleInstance.mainContext.getResources()
				.getString(R.string.clear_all));
		myAlertDialog.setMessage(SingleInstance.mainContext.getResources()
				.getString(R.string.do_you_want_to_clear_all) + message + " ?");
		myAlertDialog.setPositiveButton(SingleInstance.mainContext
				.getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {

						// TODO Auto-generated method stub
						iscleared = true;
						if (avatarpage.equalsIgnoreCase("settings")) {
							ArrayList<OfflineRequestConfigBean> configList = callDisp
									.getdbHeler(context)
									.getOfflineCallSettingsDetailsForDelete();
							if (configList != null && configList.size() > 0) {
								// WebServiceReferences.webServiceClient
								// .offlineconfiguration(
								// CallDispatcher.LoginUser,
								// configList, null);
								callDisp.getdbHeler(context)
										.deleteOfflineCallSettingDetails(null);
							}

							mainLayout.removeAllViews();
							btn_patch.setText(Integer.toString(mainLayout
									.getChildCount()));
							btn_clearAll.setVisibility(View.GONE);
						} else if (avatarpage.equalsIgnoreCase("response")) {
							for (int j = 0; j < allresponseview_container
									.getChildCount(); j++) {
								callDisp.getdbHeler(context)
										.deleteOfflineCallResponseDetails(
												responsebean_map.get(j).getId());
							}
							allresponseview_container.removeAllViews();

							btn_patch.setText(Integer
									.toString(allresponsecontainer
											.getChildCount()));

						} else if (avatarpage.equalsIgnoreCase("pending")) {

							for (int j = 0; j < pendingclone_container
									.getChildCount(); j++) {
								callDisp.getdbHeler(context)
										.deleteOfflinePendingClones(
												clone_bean.get(j).getId());
							}
							pendingclone_container.removeAllViews();
							btn_patch.setText(Integer
									.toString(pendingclone_container
											.getChildCount()));
						}
						CallDispatcher.clearAll = false;

					}
				});
		myAlertDialog.setNegativeButton(SingleInstance.mainContext
				.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int arg1) {
						// do something when the Cancel button is clicked

						dialog.cancel();
					}
				});
		myAlertDialog.show();
	}

	private void showAlert(String message, final int tag) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle(SingleInstance.mainContext.getResources()
				.getString(R.string.delete_avatar));
		myAlertDialog.setMessage(message);
		myAlertDialog.setPositiveButton(SingleInstance.mainContext
				.getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						deleteConfiguration("delete", tag);
					}
				});
		myAlertDialog.setNegativeButton(SingleInstance.mainContext
				.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int arg1) {
						// do something when the Cancel button is clicked

						dialog.cancel();
					}
				});
		myAlertDialog.show();
	}

	public void DeleteConfig(int position) {
		OfflineRequestConfigBean bean = (OfflineRequestConfigBean) bean_map
				.get(position);
		String id = null;
		if (bean != null)
			id = bean.getId();

		if (id != null)
			showAlert(
					SingleInstance.mainContext.getResources().getString(
							R.string.are_you_sure_you_want_to_delete), position);
		else {

			for (int idx = 0; idx < mainLayout.getChildCount(); idx++) {
				LinearLayout parent_view = (LinearLayout) mainLayout
						.getChildAt(idx);
				Button btn = (Button) parent_view.findViewById(R.id.deletebtn);
				int btn_tag = (Integer) btn.getTag();
				Log.d("NOTES", "---->" + btn_tag);
				Log.d("NOTES", "---->" + idx);
				Log.d("NOTES", "received tg---->" + position);
				if (btn_tag == position) {
					mainLayout.removeViewAt(idx);
					break;

				}
			}
			if (path_map.containsKey(position)) {

				File fle = new File(path_map.get(position));
				if (fle.exists())
					fle.delete();

			}
			bean_map.remove(position);
			type_map.remove(position);
			path_map.remove(position);
		}

	}

	private void SaveConfig(int tag, String mode) {
		try {

			boolean iscanceld = false;

			boolean isalready_exists = false;

			LinearLayout layout = (LinearLayout) mainLayout.getChildAt(tag);
			Spinner to_spinner = (Spinner) layout.findViewById(R.id.input_to);
			to_spinner.setEnabled(false);
			TextView spinner_title = (TextView) layout
					.findViewById(R.id.touser_title);
			spinner_title.setEnabled(false);
			spinner_title.setClickable(false);
			EditText ed_msgtitle = (EditText) layout.findViewById(R.id.title);
			Spinner res_type = (Spinner) layout.findViewById(R.id.responseType);
			EditText ed_url = (EditText) layout.findViewById(R.id.url);
			EditText ed_gsm = (EditText) layout.findViewById(R.id.ed_gsmno);
			TextView tv_when = (TextView) layout
					.findViewById(R.id.tv_whenselected);
			Button btn__save = (Button) findViewById(R.id.savebtn);
			ImageView image = (ImageView) layout.findViewById(R.id.value_img);

			OfflineRequestConfigBean bean = new OfflineRequestConfigBean();

			bean.setBuddyId(to_spinner.getSelectedItem().toString());

			if (!isalready_exists) {
				progress_dialog = new ProgressDialog(context);
				// callDisp.showprogress(progress_dialog, context);

				if (ed_msgtitle.getText().toString().trim().length() != 0)
					bean.setMessageTitle(ed_msgtitle.getText().toString()
							.trim());
				else {
					// callDisp.cancelDialog();
					showToast("Kindly Enter  subject");
					iscanceld = true;
					return;
				}
				if (type_map.containsKey(tag))
					bean.setMessagetype(type_map.get(tag));
				else if (mode.equals("edit")) {
					if (bean_map.containsKey(tag))
						bean.setMessagetype(((OfflineRequestConfigBean) bean_map
								.get(tag)).getMessagetype());
				}
				Log.d("NOTES", "Position not availble for message type");
				String message = "";
				if (bean.getMessagetype() != null) {
					if (path_map.containsKey(tag)) {
						if (bean.getMessagetype().equalsIgnoreCase(
								"call forwarding")) {
							String buddy_name = path_map.get(tag);
							if (buddy_name.equalsIgnoreCase("select buddies")) {
								callDisp.cancelDialog();
								iscanceld = true;
								showToast("Kindly choose  buddy for call forwarding");
								return;
							}
							if (!buddy_name.trim().equals(bean.getBuddyId()))
								bean.setMessage(buddy_name);
							else {
								callDisp.cancelDialog();
								iscanceld = true;
								showToast("Kindly choose someother buddy for call forwarding");
								return;
							}

						} else if (bean.getMessagetype().equalsIgnoreCase(
								"conference call")) {
							String selected_buddies = path_map.get(tag);
							String[] buddies = selected_buddies.split(",");
							for (String buddy : buddies) {
								if (buddy.equalsIgnoreCase("select buddies")) {
									callDisp.cancelDialog();
									iscanceld = true;
									showToast("Kindly choose  buddy for call forwarding");
									return;
								} else if (buddy.trim().equals(
										bean.getBuddyId())) {
									callDisp.cancelDialog();
									iscanceld = true;
									showToast("conference member name is same with to user name");
									return;
								}
							}
							bean.setMessage(selected_buddies);

						} else if (bean.getMessagetype().equalsIgnoreCase(
								"call forward to gsm")) {
							if (ed_gsm.getText().toString().trim().length() == 0) {
								callDisp.cancelDialog();
								showToast("Enter GSM number for call forwarding");
								iscanceld = true;
								return;
							} else {
								if (!isValidPhoneNumber(ed_gsm.getText()
										.toString().trim())) {
									callDisp.cancelDialog();
									showToast("Enter valid number for call forwarding");
									iscanceld = true;
									return;
								} else
									bean.setMessage(ed_gsm.getText().toString()
											.trim());
							}

						} else {
							File fle = new File(path_map.get(tag));

							String path = path_map.get(tag);
							path = path.substring(path.lastIndexOf("/") + 1);
							Log.d("clone", "--->path is" + path);
							Log.d("clone", "--->file name is" + fle.getName());
							bean.setMessage(path);
							message = path;

						}

					} else {
						if (bean.getMessagetype().equalsIgnoreCase(
								"call forward to gsm")) {
							if (ed_gsm.getText().toString().trim().length() == 0) {
								callDisp.cancelDialog();
								showToast("Enter GSM number for call forwarding");
								iscanceld = true;
								return;
							} else {
								if (!isValidPhoneNumber(ed_gsm.getText()
										.toString().trim())) {
									callDisp.cancelDialog();
									showToast("Enter valid number for call forwarding");
									iscanceld = true;
									return;
								} else
									bean.setMessage(ed_gsm.getText().toString()
											.trim());
							}

						} else {
							callDisp.cancelDialog();
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.select_message_type));
							iscanceld = true;
							return;
						}
					}
				} else {
					callDisp.cancelDialog();
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.select_message_type));
					iscanceld = true;
					return;
				}
				if (res_type.getSelectedItemPosition() != 0)
					bean.setResponseType(res_type.getSelectedItem().toString());

				if (ed_url.getText().toString().trim().length() != 0)
					bean.setUrl(ed_url.getText().toString().trim());

				if (tv_when.getText().toString().trim().length() != 0
						|| !tv_when.getText().toString().trim()
								.equalsIgnoreCase("All")) {
					String when_id = "";
					String whne_action = tv_when.getText().toString().trim();
					String[] when = whne_action.split(",");
					for (String id : when) {
						Log.d("clone", "--->when<----" + id);
						String cid = callDisp.getdbHeler(context).getwheninfo(
								"select cid from clonemaster where cdescription=\""
										+ id + "\"");
						if (when_id.trim().length() == 0)
							when_id = cid;
						else
							when_id = when_id + "," + cid;
						Log.d("clone", "--->when id <----" + when_id);
					}
					bean.setWhen(when_id);

				} else
					bean.setWhen(null);

				OfflineRequestConfigBean o_bean = bean_map.get(tag);

				values.put(tag, btn__save);
				if (mode.equals("new")) {
					bean.setId("0");
				} else if (mode.equals("edit")) {
					if (o_bean != null)
						bean.setId(o_bean.getId());
					bean.setMode("edit");

				}
				bean.setMode(mode);
				if (bean_map.containsKey(tag))
					bean.setId(bean_map.get(tag).getId());

				defalut_lis = new ArrayList<OfflineRequestConfigBean>();
				buddy_list = new ArrayList<OfflineRequestConfigBean>();
				send_baen = bean;
				offlineRequestConfigBean = send_baen.clone();
				offlineRequestConfigBean.setMessage(path_map.get(tag));

				if (!iscanceld) {
					if (WebServiceReferences.running) {
						username_list = username_list + "," + bean.getBuddyId();
						if (to_spinner.getSelectedItemPosition() == 0) {
							bean.setBuddyId(CallDispatcher.LoginUser);
							defalut_lis.add(bean);
							bean.setDefalut_list(defalut_lis);

						} else {
							buddy_list.add(bean);
							bean.setBuddy_list(buddy_list);
						}
						Log.d("NOTES", "Defaukt list-->" + defalut_lis.size());
						Log.d("NOTES", "Buddy list--->" + buddy_list.size());
						if (bean.getMessagetype().equalsIgnoreCase("text")
								|| bean.getMessagetype().equalsIgnoreCase(
										"audio")
								|| bean.getMessagetype().equalsIgnoreCase(
										"photo")
								|| bean.getMessagetype().equalsIgnoreCase(
										"video")) {
							// uploadofflineResponse(
							// Environment.getExternalStorageDirectory()
							// + "/COMMedia/" + bean.getMessage(),
							// false, bean, "call_settings", mode);
							bean.setMessage("");

						}
						// WebServiceReferences.webServiceClient
						// .offlineconfiguration(CallDispatcher.LoginUser,
						// defalut_lis, buddy_list);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteConfiguration(String mode, int tag) {
		progress_dialog = new ProgressDialog(context);
		callDisp.showprogress(progress_dialog, context);
		for (int i = 0; i < mainLayout.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) mainLayout.getChildAt(i);
			Log.d("clone", "---->" + layout);
			Log.d("clone", "---->" + layout.getTag());
			int l_tag = (Integer) layout.getTag();

			if (l_tag == tag) {
				Spinner to_spinner = (Spinner) layout
						.findViewById(R.id.input_to);
				EditText ed_msgtitle = (EditText) layout
						.findViewById(R.id.title);
				Spinner res_type = (Spinner) layout
						.findViewById(R.id.responseType);
				EditText ed_url = (EditText) layout.findViewById(R.id.url);

				OfflineRequestConfigBean bean = new OfflineRequestConfigBean();
				if (mode.equals("delete")) {
					CallDispatcher.clearAll = true;
					if (username_list.trim().length() != 0) {
						String[] name_lst = username_list.split(",");
						username_list = "";
						for (String string : name_lst) {
							if (!string.trim().equals(
									to_spinner.getSelectedItem().toString()
											.trim())) {

								if (username_list.trim().length() == 0)
									username_list = string;
								else
									username_list = username_list + ","
											+ string;
							}
						}
					}
				}
				bean.setBuddyId(to_spinner.getSelectedItem().toString());

				if (ed_msgtitle.getText().toString().trim().length() != 0)
					bean.setMessageTitle(ed_msgtitle.getText().toString()
							.trim());

				if (type_map.containsKey(tag))
					bean.setMessagetype(type_map.get(tag));
				else
					Log.d("NOTES", "Position not availble for message type");

				if (path_map.containsKey(tag)) {
					File fle = new File(path_map.get(tag));
					bean.setMessage(fle.getName());
				}

				if (res_type.getSelectedItemPosition() != 0)
					bean.setResponseType(res_type.getSelectedItem().toString());

				if (ed_url.getText().toString().trim().length() != 0)
					bean.setUrl(ed_url.getText().toString().trim());

				bean.setMode(mode);
				if (bean_map.containsKey(tag))
					bean.setId(bean_map.get(tag).getId());

				ArrayList<OfflineRequestConfigBean> defalut_lis = new ArrayList<OfflineRequestConfigBean>();
				ArrayList<OfflineRequestConfigBean> buddy_list = new ArrayList<OfflineRequestConfigBean>();
				send_baen = bean;

				if (WebServiceReferences.running) {
					username_list = username_list + "," + bean.getBuddyId();
					if (to_spinner.getSelectedItemPosition() == 0) {
						bean.setBuddyId(CallDispatcher.LoginUser);
						defalut_lis.add(bean);
					} else
						buddy_list.add(bean);

					Log.d("NOTES", "Defaukt list-->" + defalut_lis.size());
					Log.d("NOTES", "Buddy list--->" + buddy_list.size());

					// WebServiceReferences.webServiceClient.offlineconfiguration(
					// CallDispatcher.LoginUser, defalut_lis, buddy_list);
				}

			}
		}

	}

	public void inflateNewUI(OfflineRequestConfigBean bean, int mode) {
		try {
			View view = getLayoutInflater().inflate(R.layout.clonecustom,
					mainLayout, false);
			view.setTag(mainLayout.getChildCount());
			Button btn_delete = (Button) view.findViewById(R.id.deletebtn);
			Log.d("NOTES", "View child count---->" + mainLayout.getChildCount());
			btn_delete.setTag(mainLayout.getChildCount());
			Log.i("No. of Child",
					"Childs :: " + "Count :: " + mainLayout.getChildCount());
			btn_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int received_tag = (Integer) v.getTag();
					position = received_tag;
					DeleteConfig(position);
					btn_patch.setText(Integer.toString(mainLayout
							.getChildCount()));
				}
			});

			Button btn_response = (Button) view.findViewById(R.id.replybtn);
			btn_response.setTag(mainLayout.getChildCount());
			btn_response.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					if (bean_map.containsKey(tag)) {

						String config_id = bean_map.get(tag).getId();
						if (callDisp.getdbHeler(context)
								.getOfflineCallResponseDetails(
										"where configid=" + "\"" + config_id
												+ "\"") != null
								&& callDisp
										.getdbHeler(context)
										.getOfflineCallResponseDetails(
												"where configid=" + "\""
														+ config_id + "\"")
										.size() > 0) {

							if (allresponsecontainer.getVisibility() == View.GONE) {
								settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
								settings.setTextColor(getResources().getColor(
										R.color.tabcolor));
								pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
								pending.setTextColor(getResources().getColor(
										R.color.tabcolor));
								response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
								response.setTextColor(getResources().getColor(
										R.color.white));
								allresponsecontainer
										.setVisibility(View.VISIBLE);
								allresponseview_container
										.setVisibility(View.VISIBLE);
								pending_container.setVisibility(View.GONE);
								settingscontainer.setVisibility(View.GONE);
								Log.i("clone", "=====>Inside1 pending");

								Log.d("clone", "configid--->" + config_id);
								allresponse_list.clear();
								allresponseview_container.removeAllViews();
								allresponse_list.addAll(callDisp.getdbHeler(
										context).getOfflineCallResponseDetails(
										"where configid=" + "\"" + config_id
												+ "\""));
								Log.d("clone", "Respose count--->"
										+ allresponse_list.size());
								for (OfflineRequestConfigBean offlineRequestConfigBean : allresponse_list) {
									responsebean_map.put(
											allresponseview_container
													.getChildCount(),
											offlineRequestConfigBean);
									inflateAllresponseUI(
											allresponseview_container
													.getChildCount(),
											offlineRequestConfigBean);

								}
								btn_patch.setText(Integer
										.toString(allresponseview_container
												.getChildCount()));

							}
						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.no_response_to_view));

						}
					}
				}
			});

			Button btn_savechild = (Button) view.findViewById(R.id.savebtn);
			btn_savechild.setTag(mainLayout.getChildCount());
			btn_savechild.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int tag = (Integer) arg0.getTag();
					Log.d("clone", "----->tag value " + tag);
					position = tag;

					if (bean_map.containsKey(tag))
						SaveConfig((Integer) arg0.getTag(), "edit");
					else
						SaveConfig((Integer) arg0.getTag(), "new");

					values.put(position, (Button) arg0);
				}
			});

			final Spinner res_type = (Spinner) view
					.findViewById(R.id.responseType);
			TextView resType = (TextView) view.findViewById(R.id.resType);
			res_type.setAdapter(dataAdapter);

			final Spinner send_to = (Spinner) view.findViewById(R.id.input_to);
			send_to.setAdapter(buddyAdapter);

			TextView tv_titletxt = (TextView) view
					.findViewById(R.id.touser_title);
			tv_titletxt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					send_to.performClick();
				}
			});
			TextView value_txt = (TextView) view.findViewById(R.id.value_txt);
			value_txt.setTag(mainLayout.getChildCount());
			value_txt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					showMessageDialog(tag);
				}
			});
			resType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					res_type.performClick();
				}
			});

			EditText ed_title = (EditText) view.findViewById(R.id.title);
			TextView tv_when = (TextView) view.findViewById(R.id.tv_when);
			final TextView tv_whenselected = (TextView) view
					.findViewById(R.id.tv_whenselected);

			if (bean != null) {
				String when = "";
				if (bean.getWhen() != null) {
					if (bean.getWhen().trim().length() > 0) {
						String[] when_ids = bean.getWhen().split(",");
						for (String when_id : when_ids) {
							String c_description = callDisp.getdbHeler(context)
									.getwheninfo(
											"select cdescription from clonemaster where cid=\""
													+ when_id + "\"");
							if (when.trim().length() == 0)
								when = c_description;
							else
								when = when + "," + c_description;
						}
						if (when_ids.length == 0)
							when = "All";
					} else
						when = "All";

				} else
					when = "All";

				tv_whenselected.setText(when);
			}

			tv_when.setTag(mainLayout.getChildCount());
			tv_when.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int tag = (Integer) arg0.getTag();
					position = tag;
					showwhenOption(tag, tv_whenselected.getText().toString()
							.trim());
				}
			});
			tv_whenselected.setTag(mainLayout.getChildCount());
			tv_whenselected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					position = tag;
					showwhenOption(tag, tv_whenselected.getText().toString()
							.trim());
				}
			});

			TextView tv_selectbuddies = (TextView) view
					.findViewById(R.id.tv_buddyselection);
			tv_selectbuddies.setTag(mainLayout.getChildCount());
			tv_selectbuddies.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int tag = (Integer) arg0.getTag();
					position = tag;
					Log.d("clone",
							"-----> buddy selection"
									+ type_map.containsKey(tag));
					if (type_map.containsKey(tag)) {
						String type = type_map.get(tag);
						if (type != null) {
							if (type.equalsIgnoreCase("Call Forwarding"))
								showSingelSelection(tag);
							else if (type.equalsIgnoreCase("Conference Call"))
								showmultiSelection(tag);

						}
					}

				}
			});

			EditText ed_msgurl = (EditText) view.findViewById(R.id.url);
			if (mode != 0) {
				if (bean != null) {
					if (bean.getResponseType() != null)

						res_type.setSelection(getSelectedtypePosition(bean
								.getResponseType()));
					if (bean.getBuddyId() != null) {
						send_to.setSelection(getBuddyPosition(bean.getBuddyId()));
						send_to.setEnabled(false);
						tv_titletxt.setEnabled(false);

					}
					if (bean.getMessageTitle() != null)
						ed_title.setText(bean.getMessageTitle());
					if (bean.getUrl() != null) {
						if (!bean.getUrl().equals("''"))
							ed_msgurl.setText(bean.getUrl());
					}
				}
				btn_savechild.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.update));
			}

			TextView tv_msg = (TextView) view.findViewById(R.id.message_val);
			tv_msg.setTag(mainLayout.getChildCount());
			tv_msg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					int tag = (Integer) view.getTag();
					showMessageDialog(tag);

				}
			});
			int item_position = mainLayout.getChildCount();
			mainLayout.addView(view);

			if (mode != 0)
				getMessageTypeView(bean.getMessagetype(), bean.getMessage(),
						item_position);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=====> exception in inflateNewUI " + e.getMessage());
			e.printStackTrace();
		}
		if (settingscontainer.getChildCount() > 0) {
			btn_clearAll.setVisibility(View.VISIBLE);
		} else {
			btn_clearAll.setVisibility(View.GONE);
		}
	}

	private void showSingelSelection(final int tag) {
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		alert_builder.setTitle("Select Buddy");
		final String[] buddies = callDisp.getmyBuddys();
		alert_builder.setSingleChoiceItems(buddies, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int pos) {
						for (int i = 0; i < mainLayout.getChildCount(); i++) {

							LinearLayout parent_view = (LinearLayout) mainLayout
									.getChildAt(i);
							Integer p_tag = (Integer) parent_view.getTag();

							if (p_tag == tag) {

								TextView tv_uddy = (TextView) parent_view
										.findViewById(R.id.tv_buddyselection);
								tv_uddy.setText(buddies[pos]);
								path_map.put(tag, buddies[pos]);
								msg_dialog.cancel();
							}
						}

					}
				});
		msg_dialog = alert_builder.create();
		msg_dialog.show();

	}

	private void showmultiSelection(final int tag) {

		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		alert_builder.setTitle("Select Buddy");
		final String[] mybuddies = callDisp.getmyBuddys();
		alert_builder.setMultiChoiceItems(mybuddies, null,
				new OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						Log.d("clone", "---> ischeckd :" + isChecked
								+ "  item--->" + mybuddies[which]);

					}
				});
		alert_builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						ListView list = ((AlertDialog) dialog).getListView();

						StringBuilder sb = new StringBuilder();

						for (int i = 0; i < list.getCount(); i++) {
							boolean checked = list.isItemChecked(i);

							if (checked) {

								if (sb.length() > 0)
									sb.append(",");
								sb.append(list.getItemAtPosition(i));

							}
						}

						for (int i = 0; i < mainLayout.getChildCount(); i++) {

							LinearLayout parent_view = (LinearLayout) mainLayout
									.getChildAt(i);
							Integer p_tag = (Integer) parent_view.getTag();

							if (p_tag == tag) {
								TextView tv_uddy = (TextView) parent_view
										.findViewById(R.id.tv_buddyselection);
								tv_uddy.setText(sb.toString());
								path_map.put(tag, sb.toString());
								msg_dialog.cancel();
							}
						}
					}

				});

		msg_dialog = alert_builder.create();
		msg_dialog.show();

	}

	private void showwhenOption(final int tag, final String selectedValue) {

		final String[] when_option = {
				SingleInstance.mainContext.getResources().getString(
						R.string.no_answers),
				SingleInstance.mainContext.getResources().getString(
						R.string.unable_to_connect),
				SingleInstance.mainContext.getResources().getString(
						R.string.call_dropped),
				SingleInstance.mainContext.getResources().getString(
						R.string.call_disconnected),
				SingleInstance.mainContext.getResources().getString(
						R.string.offline),
				SingleInstance.mainContext.getResources().getString(
						R.string.call_rejected) };

		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		alert_builder.setTitle(SingleInstance.mainContext.getResources()
				.getString(R.string.select_when));
		boolean[] selections = null;

		if (selectedValue != null) {
			selections = new boolean[when_option.length];
			for (int i = 0; i < when_option.length; i++) {
				selections[i] = false;
			}
			String[] selected_options = selectedValue.split(",");
			for (String options : selected_options) {
				for (int i = 0; i < when_option.length; i++) {
					if (options.equalsIgnoreCase(when_option[i]))
						selections[i] = true;
				}

			}

		}

		alert_builder.setMultiChoiceItems(when_option, selections,
				new OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						Log.d("clone", "---> ischeckd :" + isChecked
								+ "  item--->" + when_option[which]);
					}
				});
		alert_builder.setPositiveButton(SingleInstance.mainContext
				.getResources().getString(R.string.done),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						ListView list = ((AlertDialog) dialog).getListView();

						StringBuilder sb = new StringBuilder();

						for (int i = 0; i < list.getCount(); i++) {
							boolean checked = list.isItemChecked(i);

							if (checked) {

								if (sb.length() > 0)
									sb.append(",");
								sb.append(list.getItemAtPosition(i));

							}
						}
						for (int i = 0; i < mainLayout.getChildCount(); i++) {

							LinearLayout parent_view = (LinearLayout) mainLayout
									.getChildAt(i);
							Integer p_tag = (Integer) parent_view.getTag();

							if (p_tag == tag) {
								TextView tv_uddy = (TextView) parent_view
										.findViewById(R.id.tv_whenselected);
								if (sb.length() > 0) {
									tv_uddy.setText(sb.toString());
								} else
									tv_uddy.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.all));

								msg_dialog.cancel();
							}
						}

					}

				});

		msg_dialog = alert_builder.create();
		msg_dialog.show();

	}

	private void showMessageDialog(final int tag) {
		AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
		alert_builder.setTitle(SingleInstance.mainContext.getResources()
				.getString(R.string.select_message_type));
		alert_builder.setSingleChoiceItems(m_type, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int pos) {
						// TODO Auto-generated method stub
						position = tag;
						String msg_type;
						if (pos == 0) {
							msg_type = "Text";
							type = "text";
							type_map.put(position, msg_type);
							msg_dialog.cancel();
							Intent intent = new Intent(context,
									CloneTextinput.class);
							startActivityForResult(intent, 25);
						} else if (pos == 1) {
							position = tag;
							msg_type = "Photo";
							type = "photo";
							type_map.put(position, msg_type);
							msg_dialog.cancel();
							if (Build.VERSION.SDK_INT < 19) {
								Intent intent = new Intent(
										Intent.ACTION_GET_CONTENT);
								intent.setType("image/*");
								startActivityForResult(intent, 2);
							} else {
								Intent intent = new Intent(
										Intent.ACTION_OPEN_DOCUMENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setType("image/*");
								startActivityForResult(intent, 19);
							}
						} else if (pos == 2) {
							position = tag;
							msg_type = "Photo";
							type = "photo";
							type_map.put(position, msg_type);
							msg_dialog.cancel();
							strIPath = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + getFileName() + ".jpg";
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", strIPath);
							intent.putExtra("requestCode", 0);
							intent.putExtra("action",
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra("createOrOpen", "create");
							startActivity(intent);
						} else if (pos == 3) {
							position = tag;
							msg_type = "Audio";
							type = "Audio";
							type_map.put(position, msg_type);
							msg_dialog.cancel();
							strIPath = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ "MAD_"
									+ callDisp.getFileName() + ".mp4";
							getComponentPath = strIPath;
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", strIPath);
							intent.putExtra("requestCode", 25);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "create");
							startActivity(intent);

						} else if (pos == 4) {
							type = "video";
							position = tag;
							msg_type = "Video";
							type_map.put(position, msg_type);
							msg_dialog.cancel();
							strIPath = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + getFileName() + ".mp4";
							getComponentPath = strIPath;
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", strIPath);
							intent.putExtra("requestCode", 25);
							intent.putExtra("action",
									MediaStore.ACTION_VIDEO_CAPTURE);
							intent.putExtra("createOrOpen", "create");
							startActivity(intent);
						} else if (pos == 5) {
							msg_dialog.cancel();
							type_map.put(position, m_type[pos]);
							setActionType(m_type[pos]);
						} else if (pos == 6) {
							msg_dialog.cancel();
							type_map.put(position, m_type[pos]);
							setActionType(m_type[pos]);
						} else if (pos == 7) {
							msg_dialog.cancel();
							type_map.put(position, m_type[pos]);
							setActionType(m_type[pos]);
						}

					}
				});
		msg_dialog = alert_builder.create();
		msg_dialog.show();
	}

	private void setActionType(String type) {

		for (int i = 0; i < mainLayout.getChildCount(); i++) {

			LinearLayout parent_view = (LinearLayout) mainLayout.getChildAt(i);
			Integer p_tag = (Integer) parent_view.getTag();

			if (p_tag == position) {
				ImageView value_img = (ImageView) parent_view
						.findViewById(R.id.value_img);
				TextView value_text = (TextView) parent_view
						.findViewById(R.id.value_txt);
				TextView tv_uddy = (TextView) parent_view
						.findViewById(R.id.tv_buddyselection);
				EditText ed_gsmno = (EditText) parent_view
						.findViewById(R.id.ed_gsmno);
				tv_uddy.setText("Select buddies");
				ed_gsmno.setText("");
				LinearLayout audioLayout = (LinearLayout) parent_view
						.findViewById(R.id.audio_layout);
				value_img.setVisibility(View.GONE);
				audioLayout.setVisibility(View.GONE);
				value_text.setVisibility(View.GONE);
				if (type.equalsIgnoreCase("call forward to gsm")) {
					tv_uddy.setVisibility(View.GONE);
					ed_gsmno.setVisibility(View.VISIBLE);
				} else {
					tv_uddy.setVisibility(View.VISIBLE);
					ed_gsmno.setVisibility(View.GONE);
				}
			}
		}

	}

	private void showResponseDialog(final int tag, String rtype) {
		if (rtype != null) {
			position = tag;
			String msg_type;
			Log.d("clone", "----came to showresponse dialog---- tag = " + tag
					+ " rtype = " + rtype);
			if (rtype.equalsIgnoreCase("text")) {
				msg_type = "Text";
				type = "text";
				Intent intent = new Intent(context, CloneTextinput.class);
				startActivityForResult(intent, 33);
			} else if (rtype.equalsIgnoreCase("photo")) {
				position = tag;
				type = "photo";
				msg_type = "Photo";
				photochat();
			} else if (rtype.equalsIgnoreCase("audio")) {
				position = tag;
				msg_type = "Audio";
				type = "audio";
				recordresponseAudio(tag);
			} else if (rtype.equalsIgnoreCase("video")) {
				type = "video";
				position = tag;
				msg_type = "Video";
				type = "video";
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + getFileName() + ".mp4";
				getComponentPath = strIPath;
				Intent intent = new Intent(context, MultimediaUtils.class);
				intent.putExtra("filePath", strIPath);
				intent.putExtra("requestCode", 33);
				intent.putExtra("action", MediaStore.ACTION_VIDEO_CAPTURE);
				intent.putExtra("createOrOpen", "create");
				startActivity(intent);
			}
		}

	}

	private void playAudio(int tag) {

		Log.d("clone", "tag value---->" + tag);
		View view = getLayoutInflater().inflate(R.layout.audio_recording,
				mainLayout, false);
		LinearLayout rec_container = (LinearLayout) view
				.findViewById(R.id.audio_rec_container);
		rec_container.setTag(tag);
		ImageButton btn_startrec = (ImageButton) view
				.findViewById(R.id.rec_start);
		btn_startrec.setTag(0);
		ProgressBar progress = (ProgressBar) view
				.findViewById(R.id.progressBar_dwn);
		progress.setVisibility(View.GONE);
		LinearLayout player_container = (LinearLayout) view
				.findViewById(R.id.audio_player_container);
		player_container.setTag(tag);
		ImageButton btn_play = (ImageButton) view.findViewById(R.id.play_start);
		btn_play.setTag(0);

		rec_container.setVisibility(View.GONE);
		player_container.setVisibility(View.VISIBLE);

		LinearLayout clone_settings = (LinearLayout) mainLayout.getChildAt(tag);
		LinearLayout audio_layout = (LinearLayout) clone_settings
				.findViewById(R.id.audio_layout);
		ImageView iv_play = (ImageView) clone_settings
				.findViewById(R.id.value_img);
		iv_play.setVisibility(View.GONE);
		TextView tv_msg = (TextView) clone_settings
				.findViewById(R.id.value_txt);
		tv_msg.setVisibility(View.GONE);
		audio_layout.removeAllViews();
		audio_layout.addView(view);
		audio_layout.setVisibility(View.VISIBLE);

		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					LinearLayout parent_layot = (LinearLayout) v.getParent();
					int p_tag = (Integer) parent_layot.getTag();
					int view_tag = (Integer) v.getTag();
					ImageButton btn_play = (ImageButton) v;
					if (view_tag == 0) {
						if (!isrecording && !isPlaying) {

							if (path_map.containsKey(p_tag)) {
								btn_play.setBackgroundResource(R.drawable.v_stop);
								btn_play.setTag(1);
								String path = path_map.get(p_tag);
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath", path);
								intent.putExtra("requestCode", AUDIO);
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "open");
								startActivity(intent);

							} else
								Log.d("LM", "----path not available-----");

						} else {
							if (isrecording)
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.audio_stop));
							else if (isPlaying)
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.please_stop_audio));
						}
					} else {
						btn_play.setBackgroundResource(R.drawable.v_play);
						btn_play.setTag(0);
						isPlaying = false;

					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void playallresponseAudio(int tag) {

		View view = getLayoutInflater().inflate(R.layout.audio_recording,
				pendingclone_container, false);
		LinearLayout rec_container = (LinearLayout) view
				.findViewById(R.id.audio_rec_container);
		rec_container.setTag(tag);
		ImageButton btn_startrec = (ImageButton) view
				.findViewById(R.id.rec_start);
		btn_startrec.setTag(0);
		ProgressBar progress = (ProgressBar) view
				.findViewById(R.id.progressBar_dwn);
		progress.setVisibility(View.GONE);
		LinearLayout player_container = (LinearLayout) view
				.findViewById(R.id.audio_player_container);
		player_container.setTag(tag);
		ImageButton btn_play = (ImageButton) view.findViewById(R.id.play_start);
		btn_play.setTag(0);
		rec_container.setVisibility(View.GONE);
		player_container.setVisibility(View.VISIBLE);
		LinearLayout clone_settings = (LinearLayout) allresponseview_container
				.getChildAt(tag);
		Log.d("clone", "------>" + clone_settings);
		LinearLayout audio_layout = (LinearLayout) clone_settings
				.findViewById(R.id.audio_layout);
		ImageView iv_play = (ImageView) clone_settings
				.findViewById(R.id.response_value_img_from);
		iv_play.setVisibility(View.GONE);
		TextView tv_msg = (TextView) clone_settings
				.findViewById(R.id.response_value_txt_from);
		tv_msg.setVisibility(View.GONE);

		audio_layout.removeAllViews();
		audio_layout.addView(view);
		audio_layout.setVisibility(View.VISIBLE);

		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// // TODO Auto-generated method stub
					LinearLayout parent_layot = (LinearLayout) v.getParent();
					int p_tag = (Integer) parent_layot.getTag();
					int view_tag = (Integer) v.getTag();
					ImageButton btn_play = (ImageButton) v;
					if (view_tag == 0) {
						if (responsebean_map.containsKey(p_tag)) {
							btn_play.setBackgroundResource(R.drawable.v_stop);
							btn_play.setTag(1);
							String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ responsebean_map.get(p_tag).getResponse();

							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", path);
							intent.putExtra("requestCode", AUDIO);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "open");
							startActivity(intent);

						} else
							Log.d("LM", "----path not available-----");

					}

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void playresponseAudio(int tag) {
		View view = getLayoutInflater().inflate(R.layout.audio_recording,
				pendingclone_container, false);
		LinearLayout rec_container = (LinearLayout) view
				.findViewById(R.id.audio_rec_container);
		rec_container.setTag(tag);
		ImageButton btn_startrec = (ImageButton) view
				.findViewById(R.id.rec_start);
		btn_startrec.setTag(0);
		ProgressBar progress = (ProgressBar) view
				.findViewById(R.id.progressBar_dwn);
		progress.setVisibility(View.GONE);
		LinearLayout player_container = (LinearLayout) view
				.findViewById(R.id.audio_player_container);
		player_container.setTag(tag);
		ImageButton btn_play = (ImageButton) view.findViewById(R.id.play_start);
		btn_play.setTag(0);
		rec_container.setVisibility(View.GONE);
		player_container.setVisibility(View.VISIBLE);
		LinearLayout clone_settings = (LinearLayout) pendingclone_container
				.getChildAt(tag);
		Log.d("clone", "------>" + clone_settings);
		LinearLayout audio_layout = (LinearLayout) clone_settings
				.findViewById(R.id.audio_layout);
		ImageView iv_play = (ImageView) clone_settings
				.findViewById(R.id.res_img_from);
		iv_play.setVisibility(View.GONE);
		TextView tv_msg = (TextView) clone_settings
				.findViewById(R.id.res_txt_from);
		tv_msg.setVisibility(View.GONE);

		audio_layout.removeAllViews();
		audio_layout.addView(view);
		audio_layout.setVisibility(View.VISIBLE);

		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					LinearLayout parent_layot = (LinearLayout) v.getParent();
					int p_tag = (Integer) parent_layot.getTag();
					int view_tag = (Integer) v.getTag();
					ImageButton btn_play = (ImageButton) v;
					if (view_tag == 0) {
						if (responsepath_map.containsKey(p_tag)) {
							btn_play.setBackgroundResource(R.drawable.v_stop);
							btn_play.setTag(1);
							String path = responsepath_map.get(p_tag);
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", path);
							intent.putExtra("requestCode", AUDIO);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "open");
							startActivity(intent);

						} else
							Log.d("LM", "----path not available-----");

					}

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void recordresponseAudio(int tag) {
		View view = getLayoutInflater().inflate(R.layout.audio_recording,
				pendingclone_container, false);
		LinearLayout rec_container = (LinearLayout) view
				.findViewById(R.id.audio_rec_container);
		rec_container.setTag(tag);
		ImageButton btn_startrec = (ImageButton) view
				.findViewById(R.id.rec_start);
		btn_startrec.setTag(0);
		ProgressBar progress = (ProgressBar) view
				.findViewById(R.id.progressBar_dwn);
		progress.setVisibility(View.GONE);
		LinearLayout player_container = (LinearLayout) view
				.findViewById(R.id.audio_player_container);
		player_container.setTag(tag);
		ImageButton btn_play = (ImageButton) view.findViewById(R.id.play_start);
		btn_play.setTag(0);

		rec_container.setVisibility(View.VISIBLE);
		player_container.setVisibility(View.GONE);

		LinearLayout clone_settings = (LinearLayout) pendingclone_container
				.getChildAt(tag);
		LinearLayout audio_layout = (LinearLayout) clone_settings
				.findViewById(R.id.audio_layout);
		ImageView iv_play = (ImageView) clone_settings
				.findViewById(R.id.res_img_from);
		iv_play.setVisibility(View.GONE);
		TextView tv_msg = (TextView) clone_settings
				.findViewById(R.id.res_txt_from);
		tv_msg.setVisibility(View.GONE);
		audio_layout.removeAllViews();
		audio_layout.addView(view);
		audio_layout.setVisibility(View.VISIBLE);

		btn_startrec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					LinearLayout parent_layot = (LinearLayout) v.getParent();
					ProgressBar progress = (ProgressBar) parent_layot
							.getChildAt(1);
					if (tag == 0) {
						if (!isrecording && !isPlaying) {
							int p_tag = (Integer) parent_layot.getTag();
							ImageButton recorder_button = (ImageButton) v;
							recorder_button.setTag(1);
							recorder_button
									.setBackgroundResource(R.drawable.v_play);
							String audio_path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + getFileName() + ".mp4";
							DeleteExistingpendingclonefile(position);
							responsepath_map.put(p_tag, audio_path);
							isrecording = true;
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", audio_path);
							intent.putExtra("requestCode", AUDIO);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "create");
							startActivity(intent);

						} else {
							if (isrecording)
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.audio_stop));
							else if (isPlaying)
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.please_stop_audio));
						}
					} else {
						v.setTag(0);
						progress.setVisibility(View.GONE);
						isrecording = false;
						LinearLayout lay = (LinearLayout) parent_layot
								.getParent();
						LinearLayout l_child = (LinearLayout) lay.getChildAt(1);
						l_child.setVisibility(View.VISIBLE);
						parent_layot.setVisibility(View.GONE);

					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// TODO Auto-generated method stub
					LinearLayout parent_layot = (LinearLayout) v.getParent();
					int p_tag = (Integer) parent_layot.getTag();
					int view_tag = (Integer) v.getTag();
					ImageButton btn_play = (ImageButton) v;
					if (view_tag == 0) {
						if (!isrecording && !isPlaying) {

							if (responsepath_map.containsKey(p_tag)) {
								btn_play.setBackgroundResource(R.drawable.v_stop);
								btn_play.setTag(1);
								String path = responsepath_map.get(p_tag);
								Intent intent = new Intent(context,
										MultimediaUtils.class);
								intent.putExtra("filePath", path);
								intent.putExtra("requestCode", AUDIO);
								intent.putExtra("action", "audio");
								intent.putExtra("createOrOpen", "open");
								startActivity(intent);

							} else
								Log.d("LM", "----path not available-----");

						}

					}

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public int getBuddyPosition(String b_name) {
		Log.d("clone", "---->" + b_name);
		if (b_name != null) {
			if (b_name.equals("default"))
				b_name = "All Users";
		}
		int i = 0;
		for (i = 0; i < buddyAdapter.getCount(); i++) {
			if (buddyAdapter.getItem(i).trim().equals(b_name.trim())) {
				Log.d("clone",
						"------> i value in adapter" + buddyAdapter.getItem(i));
				break;
			}
		}
		Log.d("clone", "------> i value in buddy" + i);
		return i;
	}

	public int getSelectedtypePosition(String type) {
		Log.i("clone", "--->" + type);

		int i = 0;
		if (type != null) {
			if (!type.trim().equals("''")) {
				if (type.trim().length() != 0) {
					for (i = 0; i < list.size(); i++) {
						if (type.equals(list.get(i).trim()))
							break;

					}
				}
			}
		}
		Log.d("clone", "------> i value in response type" + i);
		return i;
	}

	public void getMessageTypeView(String messageType, final String path,
			int position1) {
		try {
			Log.d("clone", "############### position" + position1);
			LinearLayout parent_view = (LinearLayout) mainLayout
					.getChildAt(position1);
			ImageView value_img = (ImageView) parent_view
					.findViewById(R.id.value_img);
			TextView value_text = (TextView) parent_view
					.findViewById(R.id.value_txt);
			LinearLayout audio_layout = (LinearLayout) parent_view
					.findViewById(R.id.audio_layout);
			TextView tv_buddies = (TextView) parent_view
					.findViewById(R.id.tv_buddyselection);
			EditText ed_gsm_no = (EditText) parent_view
					.findViewById(R.id.ed_gsmno);

			if (messageType.equalsIgnoreCase("Text")) {

				if (value_img.getVisibility() == View.VISIBLE) {
					value_img.setVisibility(View.GONE);

				}

				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				StringBuffer buffer = new StringBuffer();
				File file = new File(filepath);
				if (file.exists()) {
					try {
						FileInputStream fis = new FileInputStream(filepath);
						BufferedReader rdr = new BufferedReader(
								new InputStreamReader(fis));
						String st;
						try {
							while ((st = rdr.readLine()) != null) {
								Log.d("File", "#######################" + st);
								buffer.append(st);
								buffer.append('\n');
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				value_text.setText(buffer.toString());
				value_text.setVisibility(View.VISIBLE);
				value_img.setVisibility(View.GONE);
				audio_layout.removeAllViews();
				audio_layout.setVisibility(View.GONE);
				tv_buddies.setVisibility(View.GONE);
				ed_gsm_no.setVisibility(View.GONE);

			} else if (messageType.equalsIgnoreCase("Photo")) {

				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				Log.d("clone", " photo path" + strIPath);
				File selected_file = new File(strIPath);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);
				if (selected_file.exists()) {
					if (length <= 2) {

						img = null;
						Log.i("clone", "==============> " + strIPath);
						img = callDisp.ResizeImage(strIPath);
						Log.i("clone", "==========> " + img);

						if (img != null) {
							img = Bitmap
									.createScaledBitmap(img, 100, 75, false);
							bitmap = null;

							// if (fileCheck.exists()) {
							bitmap = callDisp.ResizeImage(strIPath);
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							// }

							if (parent_view != null) {
								value_img = (ImageView) parent_view
										.findViewById(R.id.value_img);
								value_text = (TextView) parent_view
										.findViewById(R.id.value_txt);

								if (bitmap != null)
									value_img.setImageBitmap(bitmap);
								value_img.setVisibility(View.VISIBLE);

								value_img.setPadding(10, 10, 10, 10);
								if (value_text.getVisibility() == View.VISIBLE) {
									value_text.setVisibility(View.GONE);
								}
								audio_layout.removeAllViews();
								audio_layout.setVisibility(View.GONE);
								tv_buddies.setVisibility(View.GONE);
								ed_gsm_no.setVisibility(View.GONE);

							} else {
								strIPath = null;
							}
						}
					}
				}
			} else if (messageType.equalsIgnoreCase("audio")) {

				playAudio(position1);
				value_text.setVisibility(View.GONE);
				value_img.setVisibility(View.GONE);
				tv_buddies.setVisibility(View.GONE);
				ed_gsm_no.setVisibility(View.GONE);

			} else if (messageType.equalsIgnoreCase("video")) {
				if (value_text.getVisibility() == View.VISIBLE) {
					value_text.setVisibility(View.GONE);
				}
				tv_buddies.setVisibility(View.GONE);
				ed_gsm_no.setVisibility(View.GONE);
				value_text.setVisibility(View.GONE);
				audio_layout.removeAllViews();
				audio_layout.setVisibility(View.GONE);
				value_img.setVisibility(View.VISIBLE);
				Log.i("bug", "INSIDE PHOTO TRUE");
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				Log.d("clone", "video path-->" + filepath);

				value_img.setPadding(30, 10, 30, 0);
				value_img.setTag(filepath);
				value_img.setImageDrawable(this.getResources().getDrawable(
						R.drawable.v_play));

				final ImageView im = value_img;
				im.setTag(filepath);
				im.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String v_path = (String) v.getTag();
						Log.d("clone", "video path-->" + v_path);
						final File vfileCheck = new File(v_path);
						Log.d("clone", "video path-->" + vfileCheck.exists());
						if (vfileCheck.exists()) {
							Intent intentVPlayer = new Intent(context,
									VideoPlayer.class);
							// intentVPlayer.putExtra("File_Path", v_path);
							// intentVPlayer.putExtra("Player_Type",
							// "Video Player");
							intentVPlayer.putExtra("video", v_path);
							startActivity(intentVPlayer);
						} else {

							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources()
											.getString(
													R.string.file_downloading),
									1).show();

						}

					}
				});

			} else if (messageType.equalsIgnoreCase("Call forward to GSM")) {
				value_text.setVisibility(View.GONE);
				value_img.setVisibility(View.GONE);
				audio_layout.removeAllViews();
				audio_layout.setVisibility(View.GONE);
				tv_buddies.setVisibility(View.GONE);
				ed_gsm_no.setVisibility(View.VISIBLE);
				ed_gsm_no.setText(path);

			} else {
				value_text.setVisibility(View.GONE);
				value_img.setVisibility(View.GONE);
				audio_layout.removeAllViews();
				audio_layout.setVisibility(View.GONE);
				tv_buddies.setVisibility(View.VISIBLE);
				tv_buddies.setText(path);
				ed_gsm_no.setVisibility(View.GONE);

			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populatePendingclones() {
		clone_bean.clear();
		ArrayList<OfflineRequestConfigBean> allresponse_list = new ArrayList<OfflineRequestConfigBean>();
		allresponse_list.addAll(callDisp.getdbHeler(context)
				.getOfflinePendingClones(
						"where username=" + "\"" + CallDispatcher.LoginUser
								+ "\""));
		for (int i = 0; i < allresponse_list.size(); i++) {
			inflatePendingClones(i, allresponse_list.get(i));
			clone_bean.put(i, allresponse_list.get(i));
		}

		btn_patch.setText(Integer.toString(pendingclone_container
				.getChildCount()));
	}

	public void populateAllResponses() {
		allresponse_list.clear();
		allresponse_list.addAll(callDisp.getdbHeler(context)
				.getOfflineCallResponseDetails(
						"where userid=" + "\"" + CallDispatcher.LoginUser
								+ "\""));
		Log.d("clone", "Respose count--->" + allresponse_list.size());
		responsebean_map.clear();
		allresponseview_container.removeAllViews();
		for (OfflineRequestConfigBean offlineRequestConfigBean : allresponse_list) {
			responsebean_map.put(allresponseview_container.getChildCount(),
					offlineRequestConfigBean);
			inflateAllresponseUI(allresponseview_container.getChildCount(),
					offlineRequestConfigBean);
		}
		btn_patch.setText(Integer.toString(allresponseview_container
				.getChildCount()));

	}

	public void inflatePendingClones(int tag, OfflineRequestConfigBean req_bean) {
		try {
			Log.d("NOTES", "----> inflateAllresponseUI");
			View view = getLayoutInflater()
					.inflate(R.layout.custompendingclones,
							pendingclone_container, false);
			view.setTag(tag);
			TextView tv_fromuser = (TextView) view.findViewById(R.id.from_name);
			tv_fromuser.setText(req_bean.getFrom());
			TextView tv_msg = (TextView) view.findViewById(R.id.from_message);
			tv_msg.setText(req_bean.getMessageTitle());
			TextView tv_url = (TextView) view.findViewById(R.id.view_url);
			TextView actionValue = (TextView) view
					.findViewById(R.id.response_title);

			LinearLayout response_layout = (LinearLayout) view
					.findViewById(R.id.response_layout);
			LinearLayout url_layout = (LinearLayout) view
					.findViewById(R.id.url_layout);
			Button btn_sendreply = (Button) view.findViewById(R.id.sendbtn);
			btn_sendreply.setTag(tag);
			Button delete_config = (Button) view
					.findViewById(R.id.pending_deletebtn);
			delete_config.setTag(tag);
			Log.d("clone", "my response type--->" + req_bean.getResponseType());

			if (req_bean.getResponseType() == null) {
				response_layout.setVisibility(View.GONE);
				btn_sendreply.setVisibility(View.GONE);
			} else if (req_bean.getResponseType().trim().length() == 0) {
				response_layout.setVisibility(View.GONE);
				btn_sendreply.setVisibility(View.GONE);
			}

			if (req_bean.getUrl() == null)
				url_layout.setVisibility(View.GONE);
			else if (req_bean.getUrl().trim().length() == 0)
				url_layout.setVisibility(View.GONE);

			if (req_bean.getUrl() != null) {
				tv_url.setVisibility(View.VISIBLE);
				tv_url.setText(req_bean.getUrl());
				tv_url.setTag(req_bean.getUrl());
			} else
				tv_url.setVisibility(View.GONE);
			TextView tv_date = (TextView) view.findViewById(R.id.receive_date);
			tv_date.setText(req_bean.getReceivedTime());
			TextView tv_viewprofile = (TextView) view
					.findViewById(R.id.view_profile);
			tv_viewprofile.setTag(tag);

			TextView tv_sendreply = (TextView) view
					.findViewById(R.id.resType_from);
			tv_sendreply.setTag(tag);
			TextView tv_replyType = (TextView) view.findViewById(R.id.reply_by);
			tv_replyType.setTag(tag);
			TextView tv_restxt = (TextView) view
					.findViewById(R.id.res_txt_from);
			ImageView image_resimage = (ImageView) view
					.findViewById(R.id.res_img_from);

			if (req_bean.getSendStatus() != null) {
				if (req_bean.getResponse() == null)
					btn_sendreply.setVisibility(View.GONE);
				else if (req_bean.getResponseType().trim().length() == 0)
					btn_sendreply.setVisibility(View.GONE);
				else if (req_bean.getSendStatus().equals("0"))
					btn_sendreply.setVisibility(View.VISIBLE);
				else
					btn_sendreply.setVisibility(View.GONE);
			} else if (req_bean.getResponseType() == null)
				btn_sendreply.setVisibility(View.GONE);
			else if (req_bean.getResponseType().trim().length() == 0)
				btn_sendreply.setVisibility(View.GONE);

			if (!req_bean.getResponse().equals("''")) {
				String path = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + req_bean.getResponse();
				DeleteExistingpendingclonefile(position);
				responsepath_map.put(tag, path);
			}

			tv_viewprofile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "====>inside view profile");
					int tag = (Integer) v.getTag();
					if (clone_bean.containsKey(tag)) {
						// OfflineRequestConfigBean bean = clone_bean.get(tag);
						// if (WebServiceReferences.buddyList.containsKey(bean
						// .getFrom())) {
						// BuddyInformationBean info =
						// WebServiceReferences.buddyList
						// .get(bean.getFrom());
						// doViewProfile(false, bean.getFrom(),
						// info.getStatus());
						// }
					}
				}
			});
			tv_sendreply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int tag = (Integer) v.getTag();
					position = tag;
					if (clone_bean.containsKey(tag)) {
						String fromResponse = clone_bean.get(tag)
								.getResponseType();
						showResponseDialog(tag, fromResponse);
					}

				}
			});

			tv_url.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, TestHTML5WebView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", v.getTag().toString());
					i.putExtras(bundle);
					startActivity(i);
				}
			});

			TextView tv_textval = (TextView) view
					.findViewById(R.id.value_txt_from);
			tv_textval.setTag(tag);
			tv_textval.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					if (clone_bean.containsKey(tag)) {
						OfflineRequestConfigBean bean = (OfflineRequestConfigBean) clone_bean
								.get(tag);
						if (bean.getMessagetype().equalsIgnoreCase(
								"call forwarding")) {
							

						} else if (bean.getMessagetype().equalsIgnoreCase(
								"conference call")) {

							String[] str = bean.getMessage().split(",");
							String offlinenames = null;
							for (int i = 0; i < str.length; i++) {
								BuddyInformationBean bib = null;
								// bib =
								// WebServiceReferences.buddyList.get(str[i]);
								if (bib.getStatus().startsWith("Onli")) {
									CallDispatcher.conConference
											.add(str[i]);
								} else {
									if (offlinenames == null) {
										offlinenames = str[i];
									} else {
										offlinenames = offlinenames + ","
												+ str[i];
									}
								}

							}
							callDisp.ConMadeConference("VC", context);
							if (offlinenames != null) {
								Toast.makeText(
										context,
										offlinenames
												+ SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.is_on_offline),
										Toast.LENGTH_LONG).show();
							}

						} else if (bean.getMessagetype().equalsIgnoreCase(
								"call forward to gsm")) {

							if (!bean.getMessage().equals("")) {
								Uri number = Uri.parse("tel:"
										+ bean.getMessage());
								Intent dial = new Intent(Intent.ACTION_CALL,
										number);
								startActivity(dial);

							}
						}
					}

				}
			});
			btn_sendreply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!isPlaying && !isrecording) {
						int tag = (Integer) arg0.getTag();
						Log.d("clone", "--->" + tag);
						position = tag;
						if (clone_bean.containsKey(tag)) {

							for (int i = 0; i < pendingclone_container
									.getChildCount(); i++) {
								LinearLayout lay = (LinearLayout) pendingclone_container
										.getChildAt(i);
								int view_tag = (Integer) lay.getTag();
								if (tag == view_tag) {
									if (clone_bean.containsKey(tag)) {
										if (responsepath_map.containsKey(tag)) {
											CallDispatcher.pdialog = new ProgressDialog(
													context);
											callDisp.showprogress(
													CallDispatcher.pdialog,
													context);
											uploadofflineResponse(
													responsepath_map.get(tag),
													true, clone_bean.get(tag),
													"pending clone", "");
										} else
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.kindly_give_your_response));
									}
								}

							}
						}
					} else {
						if (isPlaying)
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.stop_audio_response));

						if (isrecording)
							showToast(SingleInstance.mainContext
									.getResources()
									.getString(R.string.stop_recording_response));

					}
				}
			});
			delete_config.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int tag = (Integer) v.getTag();
					deletePendingClones(tag);
					deleteFile(tag);
				}
			});

			pendingclone_container.addView(view);
			getPendingMessageTypeView(req_bean.getMessagetype(),
					req_bean.getMessage(), tag);
			setResponseMessageTypeView(req_bean.getResponseType(),
					tv_replyType, tv_sendreply, tv_restxt, image_resimage,
					req_bean.getResponse(), tag);
			if (pending_container.getChildCount() > 0) {
				btn_clearAll.setVisibility(View.VISIBLE);
			} else {
				btn_clearAll.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyUploadStatus(final OfflineRequestConfigBean bean,
			final String filename, final boolean status) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (status) {
					bean.setResponse(filename);
					if (WebServiceReferences.running) {
						bean.setBuddyId(bean.getFrom());
						bean.setUserId(CallDispatcher.LoginUser);
						send_baen = bean;
						WebServiceReferences.webServiceClient
								.ResponseForCallConfiguration(bean);
					} else {
						callDisp.cancelDialog();
						showToast("Web service not running");
					}
				} else {
					callDisp.cancelDialog();
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.file_uploading_failed));
				}
			}
		});
	}

	public void deletePendingClones(int tag) {
		if (clone_bean.containsKey(tag)) {
			callDisp.getdbHeler(context).deleteOfflinePendingClones(
					clone_bean.get(tag).getId());
		}
		for (int i = 0; i < pendingclone_container.getChildCount(); i++) {
			LinearLayout lay = (LinearLayout) pendingclone_container
					.getChildAt(i);
			int view_tag = (Integer) lay.getTag();
			if (tag == view_tag) {
				pendingclone_container.removeViewAt(i);
				break;
			}

		}
		btn_patch.setText(Integer.toString(pendingclone_container
				.getChildCount()));

	}

	public void deleteFile(int tag) {
		OfflineRequestConfigBean bean = clone_bean.get(tag);
		if (bean != null) {
			String path = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + bean.getMessage();
			File fle = new File(path);
			if (fle.exists())
				fle.delete();
			if (bean.getResponse() != null) {
				String rpath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + bean.getResponse();
				File rfle = new File(rpath);
				if (rfle.exists())
					rfle.delete();
			}
		}
	}

	private void DeleteExistingpendingclonefile(int pos) {
		String original_path = "";
		original_path = responsepath_map.get(pos);
		if (original_path != null) {
			File fle = new File(original_path);
			if (!callDisp.getdbHeler(context).isfileisinuse(fle.getName())) {
				if (fle.exists())
					fle.delete();
			}
			fle = null;
		}
		original_path = null;

	}

	public void inflateAllresponseUI(int tag, OfflineRequestConfigBean req_bean) {
		try {
			Log.d("NOTES", "----> inflateAllresponseUI");
			View view = getLayoutInflater().inflate(R.layout.customallresponse,
					allresponseview_container, false);
			view.setTag(tag);
			TextView tv_from = (TextView) view
					.findViewById(R.id.response_from_name);
			tv_from.setText(req_bean.getUserId());
			TextView tv_received_time = (TextView) view
					.findViewById(R.id.value_receive_date);
			tv_received_time.setText(req_bean.getReceivedTime());
			Button delete_response = (Button) view
					.findViewById(R.id.response_deletebtn);
			delete_response.setTag(tag);
			ImageView res_image = (ImageView) view
					.findViewById(R.id.response_value_img_from);
			TextView tv_msgtitle = (TextView) view
					.findViewById(R.id.message_value);
			tv_msgtitle.setText(req_bean.getMessageTitle());

			TextView tv_resvalue = (TextView) view
					.findViewById(R.id.response_value_txt_from);

			tv_received_time.setText(req_bean.getReceivedTime());

			delete_response.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int my_tag = (Integer) arg0.getTag();
					position = my_tag;
					for (int i = 0; i < allresponseview_container
							.getChildCount(); i++) {
						LinearLayout parent_view = (LinearLayout) allresponseview_container
								.getChildAt(i);
						Button btn = (Button) parent_view
								.findViewById(R.id.response_deletebtn);
						int tag = (Integer) btn.getTag();

						Log.d("NOTES", "---->" + tag);
						Log.d("NOTES", "---->" + i);
						Log.d("NOTES", "received tg---->" + position);
						Log.d("NOTES", "received tg---->" + my_tag);

						if (tag == my_tag) {
							{
								if (responsebean_map.containsKey(tag)) {
									if (WebServiceReferences.running) {
										if (position == tag) {
											allresponseview_container
													.removeViewAt(i);
											Log.d("clone",
													"contains"
															+ responsebean_map
																	.get(position)
																	.getId());
											if (responsebean_map
													.containsKey(position)) {
												Log.d("clone",
														"contains"
																+ responsebean_map
																		.get(position)
																		.getId());
												callDisp.getdbHeler(context)
														.deleteOfflineCallResponseDetails(
																responsebean_map
																		.get(position)
																		.getId());

											}

										}

									}
								}
							}
							break;
						}

					}
					btn_patch.setText(Integer
							.toString(allresponseview_container.getChildCount()));
				}

			});

			allresponseview_container.addView(view);
			if (allresponsecontainer.getChildCount() > 0) {
				btn_clearAll.setVisibility(View.VISIBLE);
			} else {
				btn_clearAll.setVisibility(View.GONE);
			}

			setallResponseMessageTypeView(req_bean.getResponseType(),
					tv_resvalue, res_image, req_bean.getResponse(), tag);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void uploadofflineResponse(String path, boolean pendingClone,
			OfflineRequestConfigBean bean, String from, String mode) {
		// TODO Auto-generated method stub
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");

		if (path != null) {
			if (CallDispatcher.LoginUser != null) {
				FTPBean ftpBean = new FTPBean();
				ftpBean.setServer_ip(appMainActivity.cBean.getRouter().split(
						":")[0]);
				ftpBean.setServer_port(40400);
				// ftpBean.setFtp_username(callDisp.getFTPUsername());
				// ftpBean.setFtp_password(callDisp.getFTPpassword());
				ftpBean.setShowInNotificationBar(true);
				if (buddy_list != null)
					ftpBean.setBuddy_list(buddy_list);
				if (defalut_lis != null)
					ftpBean.setDefalut_lis(defalut_lis);
				if (from.equalsIgnoreCase("call_settings"))
					ftpBean.setMode(mode);

				if (path.contains("MVD_")) {
					if (path.contains(".mp4"))
						ftpBean.setFile_path(path);
					else
						ftpBean.setFile_path(path + ".mp4");
				} else if (path.contains("MAD_"))
					ftpBean.setFile_path(path);
				else
					ftpBean.setFile_path(path);

				ftpBean.setOperation_type(1);
				ftpBean.setReq_object(bean);
				ftpBean.setRequest_from(from);
				if (appMainActivity.getFtpNotifier() != null)
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				AlertDialog.Builder buider = new AlertDialog.Builder(context);
				buider.setMessage(
						SingleInstance.mainContext.getResources().getString(
								R.string.app_background))
						.setPositiveButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.yes),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// finish();
										moveTaskToBack(true);
										// return true;

									}
								})
						.setNegativeButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.no),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
				AlertDialog alert = buider.create();
				alert.show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void getPendingMessageTypeView(String messageType,
			final String path, int position1) {
		Log.d("clone", "############### position" + position1);
		LinearLayout parent_view = (LinearLayout) pendingclone_container
				.getChildAt(position1);
		ImageView value_img = (ImageView) parent_view
				.findViewById(R.id.value_img_from);
		TextView value_text = (TextView) parent_view
				.findViewById(R.id.value_txt_from);
		TextView actionValue = (TextView) parent_view
				.findViewById(R.id.response_title);

		if (messageType.equalsIgnoreCase("Text")) {
			actionValue.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.please_see_below_for_text_message));
			if (value_img.getVisibility() == View.VISIBLE) {
				value_img.setVisibility(View.GONE);

			}

			String filepath = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + path;
			StringBuffer buffer = new StringBuffer();

			try {
				FileInputStream fis = new FileInputStream(filepath);
				BufferedReader rdr = new BufferedReader(new InputStreamReader(
						fis));
				String st;
				try {
					while ((st = rdr.readLine()) != null) {
						Log.d("File", "#######################" + st);
						buffer.append(st);
						buffer.append('\n');
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			value_text.setText(buffer.toString());
			value_text.setVisibility(View.VISIBLE);

		} else if (messageType.equalsIgnoreCase("Photo")) {
			actionValue.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.please_see_below_for_photo_message));
			strIPath = Environment.getExternalStorageDirectory() + "/COMMedia/"
					+ path;
			Log.d("clone", " photo path" + strIPath);
			File selected_file = new File(strIPath);
			int length = (int) selected_file.length() / 1048576;
			Log.d("busy", "........ size is------------->" + length);

			if (length <= 2) {

				// if (img != null) {
				// if (!img.isRecycled()) {
				// img.recycle();
				// // System.gc();
				// }
				// }
				img = null;
				img = callDisp.ResizeImage(strIPath);
				img = Bitmap.createScaledBitmap(img, 100, 75, false);

				if (img != null) {

					// if (bitmap != null) {
					// if (!bitmap.isRecycled()) {
					// bitmap.recycle();
					// }
					// }

					bitmap = null;

					// if (fileCheck.exists()) {
					bitmap = callDisp.ResizeImage(strIPath);
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);
					// }

					if (parent_view != null) {
						value_img = (ImageView) parent_view
								.findViewById(R.id.value_img_from);
						value_text = (TextView) parent_view
								.findViewById(R.id.value_txt_from);
						// scroll_opt = (ScrollView) parent_view
						// .findViewById(R.id.scroller_opt);

						if (bitmap != null)
							value_img.setImageBitmap(bitmap);
						value_img.setVisibility(View.VISIBLE);
						// scroll_opt.setVisibility(View.GONE);
						value_img.setTag(strIPath);
						value_img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path", v.getTag().toString());
								in.putExtra("type", false);
								in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(in);

							}
						});
						value_img.setPadding(10, 10, 10, 10);
						if (value_text.getVisibility() == View.VISIBLE) {
							value_text.setVisibility(View.GONE);
						}

					} else {
						strIPath = null;
					}
				}
			}
		} else if (messageType.equalsIgnoreCase("audio")) {
			actionValue.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.click_play_audio));
			if (value_text.getVisibility() == View.VISIBLE) {
				value_text.setVisibility(View.GONE);
			}
			value_img.setVisibility(View.VISIBLE);
			String filepath = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + path;
			value_img.setTag(filepath);
			value_img.setPadding(30, 10, 30, 0);
			value_img.setImageDrawable(this.getResources().getDrawable(
					R.drawable.v_play));
			value_img.setContentDescription("play");

			final ImageView img = value_img;

			Log.i("bug", "INSIDE PHOTO TRUE");

			chatplayer = new MediaPlayer();

			value_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String f_path = (String) v.getTag();
					final File vfileCheck = new File(f_path);
					if (vfileCheck.exists()) {
						if (v.getContentDescription().toString()
								.equalsIgnoreCase("stop")) {
							bitmap = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_play);
							img.setImageBitmap(bitmap);
							v.setContentDescription("play");
							chatplayer.reset();

						} else {
							try {
								chatplayer.setDataSource(f_path);
								chatplayer.setLooping(false);

								chatplayer.prepare();
								chatplayer.start();

								// if (bitmap != null) {
								// if (!bitmap.isRecycled()) {
								// bitmap.recycle();
								// }
								// }
								bitmap = BitmapFactory.decodeResource(
										getResources(), R.drawable.v_stop);
								img.setImageBitmap(bitmap);
								v.setContentDescription("stop");
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}
						chatplayer
								.setOnCompletionListener(new OnCompletionListener() {

									@Override
									public void onCompletion(MediaPlayer mp) {
										// TODO
										// Auto-generated
										// method stub
										Log.i("log",
												"on completion listener start");
										try {
											chatplayer.reset();
											// if (bitmap != null) {
											// if (!bitmap.isRecycled()) {
											// bitmap.recycle();
											// }
											// }

											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_play);
											img.setImageBitmap(bitmap);

										} catch (IllegalArgumentException e) {
											// TODO
											// Auto-generated
											// catch
											// block
											e.printStackTrace();
										} catch (IllegalStateException e) {
											// TODO
											// Auto-generated
											// catch
											// block
											e.printStackTrace();
										}

									}
								});

					}

				}
			});
			// scroll_opt.setVisibility(View.GONE);
		} else if (messageType.equalsIgnoreCase("video")) {
			actionValue.setText("Click to play video");
			if (value_text.getVisibility() == View.VISIBLE) {
				value_text.setVisibility(View.GONE);
			}
			value_img.setVisibility(View.VISIBLE);
			Log.i("bug", "INSIDE PHOTO TRUE");
			String filepath = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + path;

			Log.d("clone", "video path-->" + filepath);

			value_img.setPadding(30, 10, 30, 0);
			value_img.setTag(filepath);
			value_img.setImageDrawable(this.getResources().getDrawable(
					R.drawable.v_play));

			final ImageView im = value_img;
			im.setTag(filepath);
			im.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String v_path = (String) v.getTag();
					if (v_path.endsWith(".mp4"))
						v_path = v_path.substring(0, v_path.lastIndexOf(".mp4"));
					Log.d("clone", "video path-->" + v_path);
					final File vfileCheck = new File(v_path + ".mp4");
					if (vfileCheck.exists()) {

						Intent intentVPlayer = new Intent(context,
								VideoPlayer.class);
						// intentVPlayer.putExtra("File_Path", v_path + ".mp4");
						// intentVPlayer.putExtra("Player_Type",
						// "Video Player");
						intentVPlayer.putExtra("video", v_path + ".mp4");
						startActivity(intentVPlayer);
					} else {
						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(R.string.file_downloading),
								1).show();
					}

				}
			});
			// scroll_opt.setVisibility(View.GONE);
		} else {
			value_text.setText(path);
		}
	}

	private void messageResponseType(String strMMType) {
		Log.i("clone", "===> inside message response");

		try {
			if (strMMType.equals("Gallery")) {
				Log.i("clone", "====> inside gallery");
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, 30);
				} else {
					Log.i("img", "sdk is above 19");
					Log.i("clone", "====> inside gallery");
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent, 31);

				}

			} else if (strMMType.equals("Photo")) {
				Log.i("clone", "====> inside photo");
				Long free_size = callDisp.getExternalMemorySize();
				Log.d("IM", free_size.toString());
				if (free_size > 0 && free_size >= 5120) {
					if (llayTimer != null) {
						llayTimer.removeAllViews();
					}
					strIPath = "/sdcard/COMMedia/" + getFileName() + ".jpg";
					Intent intent = new Intent(context, MultimediaUtils.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", 32);
					intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("createOrOpen", "create");
					startActivity(intent);
				} else {
					showToast("InSufficient Memory...");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=======>" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void photochat() {

		final String[] items = new String[] {
				SingleInstance.mainContext.getResources().getString(
						R.string.from_gallery),SingleInstance.mainContext.getResources().getString(R.string.from_camera) };
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				// 0);

				if (which == 0) {
					messageResponseType(items[0]);
				} else if (which == 1) {
					messageResponseType(items[1]);
				}

			}

		});

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		builder.show();
	}

	public void NotifyresponsecallConfig(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d("clone",
						"--->came to NotifyresponsecallConfig answer machine");
				callDisp.cancelDialog();
				if (obj instanceof String[]) {
					String[] response = (String[]) obj;
					if (response[1].trim().equalsIgnoreCase(
							"Responsed successfully")) {
						if (clone_bean.containsKey(position)) {
							deletePendingClones(position);

							showToast(response[1]);

						}
					} else
						showToast(response[1]);

				} else if (obj instanceof WebServiceBean) {
					showToast(((WebServiceBean) obj).getText());
				}
			}
		});
	}

	public void setallResponseMessageTypeView(String messageType,
			TextView value_text, ImageView value_image, final String path,
			int tag_id) {

		if (messageType.equalsIgnoreCase("Photo")) {
			if (path != null) {
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				Log.d("clone", " photo path" + strIPath);
				File selected_file = new File(strIPath);
				if (selected_file.exists()) {
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						// if (img != null) {
						// if (!img.isRecycled()) {
						// img.recycle();
						// // System.gc();
						// }
						// }
						img = null;
						img = callDisp.ResizeImage(strIPath);
						if (img != null)
							img = Bitmap
									.createScaledBitmap(img, 100, 75, false);

						if (img != null) {

							bitmap = null;
							bitmap = callDisp.ResizeImage(strIPath);
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							if (bitmap != null)
								value_image.setImageBitmap(bitmap);
							value_image.setVisibility(View.VISIBLE);
							value_image.setPadding(10, 10, 10, 10);
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}

						}
					}
				}
			}
		} else if (messageType.equalsIgnoreCase("Audio")) {
			if (!path.equals("''"))
				playallresponseAudio(tag_id);

		} else if (messageType.equalsIgnoreCase("Video")) {
			if (!path.equals("''")) {

				if (value_text.getVisibility() == View.VISIBLE) {
					value_text.setVisibility(View.GONE);
				}
				value_image.setVisibility(View.VISIBLE);
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;

				Log.d("clone", "video path-->" + filepath);
				value_image.setPadding(30, 10, 30, 0);
				value_image.setTag(filepath);
				value_image.setImageDrawable(this.getResources().getDrawable(
						R.drawable.v_play));

				final ImageView im = value_image;
				im.setTag(filepath);

				im.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d("clone", "on click paly");
						String v_path = (String) v.getTag();
						final File vfileCheck = new File(v_path);
						if (vfileCheck.exists()) {
							Intent intentVPlayer = new Intent(context,
									VideoPlayer.class);
							// intentVPlayer.putExtra("File_Path", v_path);
							// intentVPlayer.putExtra("Player_Type",
							// "Video Player");
							intentVPlayer.putExtra("video", v_path);
							startActivity(intentVPlayer);
						} else

							Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.file_downloading), 1)
									.show();
					}
				});
			}
		} else if (messageType.equalsIgnoreCase("Text")) {
			value_image.setVisibility(View.GONE);
			if (path != null) {
				StringBuffer buffer = new StringBuffer();
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				File fle = new File(filepath);
				if (fle.exists()) {
					try {
						FileInputStream fis = new FileInputStream(filepath);
						BufferedReader rdr = new BufferedReader(
								new InputStreamReader(fis));
						String st;
						try {
							while ((st = rdr.readLine()) != null) {
								Log.d("File", "#######################" + st);
								buffer.append(st);
								buffer.append('\n');
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					value_text.setVisibility(View.VISIBLE);
					value_text.setText(buffer.toString());
				}
				fle = null;
			}
		}
	}

	public void setResponseMessageTypeView(String messageType,
			TextView replyType, TextView respone, TextView value_text,
			ImageView value_image, final String path, int tag_id) {

		if (messageType.equalsIgnoreCase("Photo")) {
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_photo_message));
			respone.setText(SingleInstance.mainContext.getResources().getString(R.string.click_here_to_response));
			if (path != null) {
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				File fle = new File(strIPath);
				if (fle.exists()) {
					Log.d("clone", " photo path" + strIPath);
					File selected_file = new File(strIPath);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);

					if (length <= 2) {
						// if (img != null) {
						// if (!img.isRecycled()) {
						// img.recycle();
						// // System.gc();
						// }
						// }
						img = null;
						img = callDisp.ResizeImage(strIPath);
						img = Bitmap.createScaledBitmap(img, 100, 75, false);

						if (img != null) {

							bitmap = null;
							bitmap = callDisp.ResizeImage(strIPath);
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							if (bitmap != null)
								value_image.setImageBitmap(bitmap);
							value_image.setVisibility(View.VISIBLE);
							value_image.setTag(strIPath);
							value_image
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stubo
											Intent in = new Intent(context,
													PhotoZoomActivity.class);
											in.putExtra("Photo_path", v
													.getTag().toString());
											in.putExtra("type", false);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(in);

										}
									});
							value_image.setPadding(10, 10, 10, 10);
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
						}
					}
				}
				fle = null;
			}
		} else if (messageType.equalsIgnoreCase("Audio")) {
			respone.setVisibility(View.GONE);
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_photo_message));
			if (path != null) {
				if (!path.equals("''") && path.trim().length() != 0)
					playresponseAudio(tag_id);
				else
					recordresponseAudio(tag_id);
			} else
				recordresponseAudio(tag_id);

		} else if (messageType.equalsIgnoreCase("Video")) {
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_video_message));
			respone.setText(SingleInstance.mainContext.getResources().getString(R.string.click_here_to_response));
			if (path != null) {
				if (!path.equals("''")) {

					if (value_text.getVisibility() == View.VISIBLE) {
						value_text.setVisibility(View.GONE);
					}
					value_image.setVisibility(View.VISIBLE);
					String filepath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + path;
					File fle = new File(filepath);
					if (fle.exists()) {

						Log.d("clone", "video path-->" + filepath);
						value_image.setPadding(30, 10, 30, 0);
						value_image.setTag(filepath);
						value_image.setImageDrawable(this.getResources()
								.getDrawable(R.drawable.v_play));

						final ImageView im = value_image;
						im.setTag(filepath);

						im.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Log.d("clone", "on click paly");
								String v_path = (String) v.getTag();
								final File vfileCheck = new File(v_path);
								if (vfileCheck.exists()) {
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
									// intentVPlayer.putExtra("File_Path",
									// v_path);
									// intentVPlayer.putExtra("Player_Type",
									// "Video Player");
									intentVPlayer.putExtra("video", v_path);
									startActivity(intentVPlayer);
								} else {

									Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.file_downloading),
											1).show();

								}

							}
						});
					}
					fle = null;
				}
			}
		} else if (messageType.equalsIgnoreCase("Text")) {
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_text_message));
			respone.setText(SingleInstance.mainContext.getResources().getString(R.string.click_here_to_response));
			value_image.setVisibility(View.GONE);
			if (path != null) {
				StringBuffer buffer = new StringBuffer();
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				File fle = new File(filepath);
				if (fle.exists()) {
					try {
						FileInputStream fis = new FileInputStream(filepath);
						BufferedReader rdr = new BufferedReader(
								new InputStreamReader(fis));
						String st;
						try {
							while ((st = rdr.readLine()) != null) {
								Log.d("File", "#######################" + st);
								buffer.append(st);
								buffer.append('\n');
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					value_text.setVisibility(View.VISIBLE);
					value_text.setText(buffer.toString());
				}
				fle = null;
			}
		}
	}

	public void populateAllresponses() {
		if (allresponsecontainer.getVisibility() == View.VISIBLE) {
			populateAllResponses();
		}
	}

	public boolean isValidPhoneNumber(String s) {

		Matcher m = VALID_PHONE_NUMBER.matcher(s);

		return m.matches();
	}

	private void addRemovedFiles(int pos, String from) {
		if (path_map.containsKey(pos)) {
			if (removedpath_map.containsKey(from + Integer.toString(pos))) {
				ArrayList<String> removed_list = removedpath_map.get(from
						+ Integer.toString(pos));
				removed_list.add(path_map.remove(pos));
				removedpath_map.put(from + Integer.toString(pos), removed_list);
			} else {
				ArrayList<String> removed_list = new ArrayList<String>();
				removed_list.add(path_map.remove(pos));
				removedpath_map.put(from + Integer.toString(pos), removed_list);
			}
		}
	}

	private void DeleteExistingfile(int pos, String from) {
		String original_path = "";
		original_path = path_map.get(pos);
		if (original_path == null)
			original_path = "";
		if (removedpath_map.containsKey(from + Integer.toString(pos))) {
			ArrayList<String> path_list = removedpath_map.get(from
					+ Integer.toString(pos));
			if (path_list != null) {
				for (String path : path_list) {
					if (!original_path.equals(path)) {
						File remove_path = new File(path);
						if (remove_path.exists())
							remove_path.delete();
						remove_path = null;
					}
				}

			}
		}
	}

	private void resignKeypad(EditText view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public void notifyProfilepictureDownloaded() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (slidemenu != null) {
					if (slidemenu.menuShown)
						slidemenu.refreshItem();
				}
			}
		});

	}

	@Override
	public void onSlideMenuItemClick(int itemId, View v, Context context) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case WebServiceReferences.CONTACTS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.USERPROFILE:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.UTILITY:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.NOTES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.APPS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.CLONE:

			break;
		case WebServiceReferences.SETTINGS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;

		case WebServiceReferences.QUICK_ACTION:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			if (CallDispatcher.LoginUser != null)
				finish();
			break;
		case WebServiceReferences.FORMS:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.FEEDBACK:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		case WebServiceReferences.EXCHANGES:
			callDisp.onSlideMenuItemClick(itemId, v, context);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void notifyReceivedIM(final SignalingBean sb) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				btn_im.setVisibility(View.VISIBLE);
				btn_im.setEnabled(true);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});
	}

	private int saveSettings(OfflineRequestConfigBean bean) {

		// bean.setId("0");
		ContentValues cv = new ContentValues();
		// cv.put("Id", result.getId());
		cv.put("userid", CallDispatcher.LoginUser);
		if (bean.getBuddyId().equals(CallDispatcher.LoginUser))
			cv.put("buddyid", "default");
		else
			cv.put("buddyid", bean.getBuddyId());

		cv.put("message_title", bean.getMessageTitle());
		cv.put("message_type", bean.getMessagetype());
		cv.put("message", bean.getMessage());
		cv.put("when_action", bean.getWhen());
		if (bean.getResponseType() != null) {
			if (bean.getResponseType().trim().length() != 0)
				cv.put("response_type", bean.getResponseType());
			else
				cv.put("response_type", "''");
		} else
			cv.put("response_type", "''");

		if (bean.getUrl() == null)
			cv.put("url", "''");
		else
			cv.put("url", bean.getUrl());
		// if (result.getRecordTime() == null)
		// cv.put("record_time", "''");
		// else
		// cv.put("record_time", result.getRecordTime());
		int row_id = callDisp.getdbHeler(context)
				.insertOfflineCallSettingDetails(cv);

		Log.d("NOTES", "Inserted row id--->" + row_id);
		// showToast(result.getText());
		// send_baen.setId(result.getId());
		bean_map.put(position, bean);
		if (values.containsKey(position)) {
			Button save = values.get(position);
			save.setText("Update");
		}
		DeleteExistingfile(position, "settings_");

		return row_id;

	}

}
