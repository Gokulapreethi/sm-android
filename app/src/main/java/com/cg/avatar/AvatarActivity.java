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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.CloneTextinput;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.commongui.TestHTML5WebView;
import com.cg.files.CompleteListView;
import com.cg.ftpprocessor.FTPBean;
import com.image.utils.ImageLoader;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.SettingsFragment;
import com.main.ViewProfileFragment;
import com.process.MemoryProcessor;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;
import com.util.Utils;
import com.util.VideoPlayer;

public class AvatarActivity extends Fragment {

	public ListView listView;

	public static SetAvatarAdapter adapter;

	public static synchronized SetAvatarAdapter getAvatarAdapter() {

		return adapter;
	}

	// public static ArrayList<OfflineRequestConfigBean> staticlist = new
	// ArrayList<OfflineRequestConfigBean>();

	private View view = null;

	private static Context context;
	

	private static AvatarActivity avatarActivity;

	private Button plusBtn, setting;

	private Button response, settings, pending;

	private Button btn_patch;

	private ScrollView pending_container, allresponsecontainer;

	private LinearLayout allresponseview_container;

	private LinearLayout pendingclone_container;

	public String getComponentPath;

	public ArrayList<OfflineRequestConfigBean> staticlist = new ArrayList<OfflineRequestConfigBean>();

	public ArrayList<OfflineRequestConfigBean> slist = new ArrayList<OfflineRequestConfigBean>();

	private String image_path;
	
	private String temp;

	CallDispatcher callDisp;

	public ArrayList<OfflineRequestConfigBean> list = new ArrayList<OfflineRequestConfigBean>();

	private HashMap<Integer, String> messageMap = new HashMap<Integer, String>();

	private HashMap<Integer, String> path_map = new HashMap<Integer, String>();

	private HashMap<Integer, String> old_path_map = new HashMap<Integer, String>();

	boolean TEXT = false;

	boolean AUDIO, VIDEO, CAMERA, GALLERY, KITKAT = false;

	private String strIPath;

	Bitmap img;

	private Bitmap bitmap = null;

	ImageLoader imageLoader = null;

	private AlertDialog msg_dialog = null;

	private ArrayList<OfflineRequestConfigBean> defalut_lis = null;

	private ArrayList<OfflineRequestConfigBean> buddy_list = null;

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

	private boolean iscleared = false;

	private String avatarpage = "settings";

	private OfflineRequestConfigBean send_bean;

	private HashMap<Integer, String> type_map = new HashMap<Integer, String>();

	private HashMap<String, ArrayList<String>> removedpath_map = new HashMap<String, ArrayList<String>>();

	private OfflineRequestConfigBean offlineRequestConfigBean = null;

	private HashMap<Integer, OfflineRequestConfigBean> bean_map = new HashMap<Integer, OfflineRequestConfigBean>();

	private HashMap<Integer, OfflineRequestConfigBean> updatemap = new HashMap<Integer, OfflineRequestConfigBean>();

	private Handler handler = new Handler();

	public int viewPosition = 0;

	private int fragPosition = 0;

	private String type = null;

	private ProgressDialog dialog = null;

	private boolean update, updatemessage;

	private OfflineRequestConfigBean forupdate;

	OfflineRequestConfigBean saveBean = new OfflineRequestConfigBean();

	private ArrayList<OfflineRequestConfigBean> allresponse_list = new ArrayList<OfflineRequestConfigBean>();

	private HashMap<Integer, OfflineRequestConfigBean> responsebean_map = new HashMap<Integer, OfflineRequestConfigBean>();

	private HashMap<Integer, OfflineRequestConfigBean> clone_bean = new HashMap<Integer, OfflineRequestConfigBean>();

	private HashMap<Integer, String> responsepath_map = new HashMap<Integer, String>();

	private String profile_buddy;

	private String profile_buddystatus;

	private boolean isrecording = false;

	private boolean isPlaying = false;

	private MediaPlayer chatplayer;

	private LinearLayout llayTimer;

	boolean fragResult = false;

	TextView hint;

	public static boolean BGProcessorProgress = false;

	private static OfflineRequestConfigBean uploadConfigBean = new OfflineRequestConfigBean();

	// private LinearLayout mainLayout;

	private static Pattern VALID_PHONE_NUMBER = Pattern
			.compile("^[0-9.()-]{10,25}$");

	public static AvatarActivity newInstance(Context maincontext) {
		try {
			if (avatarActivity == null) {
				context = maincontext;
				avatarActivity = new AvatarActivity();

			}

			return avatarActivity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return avatarActivity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		list.clear();
		updatemap.clear();
		staticlist.clear();
		slist.clear();
		LinearLayout contact_layout=(LinearLayout) getActivity().findViewById(R.id.contact_layout);
		contact_layout.setVisibility(View.GONE);
		TextView title = (TextView) getActivity().findViewById(
				R.id.activity_main_content_title);
		title.setVisibility(View.VISIBLE);

		btn_patch = (Button) getActivity().findViewById(R.id.btn_brg);
		btn_patch.setVisibility(View.VISIBLE);
		btn_patch.setBackgroundResource(R.drawable.patch);
		btn_patch.setTextColor(R.color.title);

		setting = (Button) getActivity().findViewById(R.id.btn_settings);
		// setting.setVisibility(View.VISIBLE);
		setting.setText(SingleInstance.mainContext.getResources().getString(
				R.string.clear_all));
		setting.setTextColor(Color.parseColor("#ffffff"));
		setting.setBackgroundResource(R.color.title);

		plusBtn = (Button) getActivity().findViewById(R.id.add_group);
		plusBtn.setVisibility(View.VISIBLE);
		plusBtn.setText("");
		plusBtn.setBackgroundResource(R.drawable.toolbar_buttons_plus);
		Button backBtn = (Button) getActivity().findViewById(R.id.backbtn);
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SettingsFragment settingsFragment = SettingsFragment.newInstance(context);							
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(
						R.id.activity_main_content_fragment,
						settingsFragment);
				fragmentTransaction.commit();
			}
		});

		// setContentView(R.layout.avatar_set);
		view = inflater.inflate(R.layout.avatar_set, null);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		if (BGProcessorProgress) {
			cancelDialog();
		} else {
			showprogress();
		}
		if (WebServiceReferences.callDispatch.containsKey("calldisp"))
			callDisp = (CallDispatcher) WebServiceReferences.callDispatch
					.get("calldisp");
		else
			callDisp = new CallDispatcher(context);
		imageLoader = new ImageLoader(context);

		listView = (ListView) view.findViewById(R.id.listView);
		WebServiceReferences.contextTable.put("avatarset", context);
		SingleInstance.contextTable.put("avatar", context);

		// mainLayout = (LinearLayout) view.findViewById(R.id.container);
		settings = (Button) view.findViewById(R.id.settingsbtn);
		response = (Button) view.findViewById(R.id.responsebtn);
		hint = (TextView) view.findViewById(R.id.tv_hint);
		pending_container = (ScrollView) view
				.findViewById(R.id.pending_container);
		allresponsecontainer = (ScrollView) view
				.findViewById(R.id.allresponsecontainer);
		allresponseview_container = (LinearLayout) view
				.findViewById(R.id.allresponseview_container);
		pending = (Button) view.findViewById(R.id.pendingbtn);
		pendingclone_container = (LinearLayout) view
				.findViewById(R.id.pending_clonescontainer);

		adapter = new SetAvatarAdapter(context, list);
		listView.setAdapter(adapter);

		staticlist = DBAccess.getdbHeler(context).getOfflineSettingDetails(
				"where userid=" + "\"" + CallDispatcher.LoginUser + "\"");

		if (staticlist.size() > 0) {
			// for (OfflineRequestConfigBean dataBean : MainActivity.staticlist)
			// {
			for (int i = 0; i < staticlist.size(); i++) {

				OfflineRequestConfigBean saveBeanData = (OfflineRequestConfigBean) staticlist
						.get(i);
				saveBeanData.setListPosition(i);
				saveBeanData.setAddNewList(false);
				saveBeanData.setMode("edit");
				saveBeanData.setSaveButtonTitle(SingleInstance.mainContext
						.getResources().getString(R.string.update));
				Log.i("Avatar098", "bean_map");
				bean_map.put(i, saveBeanData);
				updatemap.put(i, saveBeanData);
				old_path_map.put(i, saveBeanData.getMessage());
				btn_patch.setText(Integer.toString(staticlist.size()));
				setting.setVisibility(View.VISIBLE);

				list.add(saveBeanData);
				try {
					saveBean = saveBeanData.clone();
					updatemap.put(i, saveBean);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// OfflineRequestConfigBean saveBean =
				// (OfflineRequestConfigBean) staticlist
				// .get(i);
				// saveBean.setListPosition(i);
				// saveBean.setAddNewList(false);
				// saveBean.setMode("edit");
				// Log.i("Avatar098", "bean_map");
				// // bean_map.put(i, saveBean);
				// updatemap.put(i, saveBean);

				adapter.notifyDataSetChanged();
			}

			// dataBean.setMode(1);
			// dataBean.setListPosition(listPosition);
			// list.add(dataBean);
			// }

			// for (int i = 0; i < slist.size(); i++) {
			// OfflineRequestConfigBean saveBean = (OfflineRequestConfigBean)
			// slist
			// .get(i);
			// saveBean.setListPosition(i);
			// saveBean.setAddNewList(false);
			// saveBean.setMode("edit");
			// Log.i("Avatar098", "bean_map");
			// // bean_map.put(i, saveBean);
			// updatemap.put(i, saveBean);
			//
			// }

		} else {
			btn_patch.setText(Integer.toString(staticlist.size()));
			setting.setVisibility(View.GONE);

			// Log.i("avatar_i", "AppMainActivity.staticlist.size() == 0");
			//
			// OfflineRequestConfigBean dataBean = new
			// OfflineRequestConfigBean();
			// dataBean.setMode(0);
			// dataBean.setListPosition(0);
			// dataBean.setTitle("");
			// dataBean.setUrl("");
			// dataBean.setAddNewList(true);
			//
			// list.add(dataBean);
			//
			// adapter.notifyDataSetChanged();
		}
		// adapter.notifyDataSetChanged();

		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// OfflineRequestConfigBean dataBean = new OfflineRequestConfigBean();
		// int listSize = AppMainActivity.staticlist.size();
		//
		// dataBean.setMode(0);
		// dataBean.setListPosition(listSize + 1);
		// dataBean.setAddNewList(true);
		// list.add(dataBean);
		//
		// adapter.notifyDataSetChanged();
		// }
		// });
		// btn_patch = (Button) view.findViewById(R.id.clone_patch);

		plusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					if (BGProcessorProgress) {

						OfflineRequestConfigBean dataBean = new OfflineRequestConfigBean();
						final int listSize = list.size();
						setting.setVisibility(View.VISIBLE);
						dataBean.setModeInt(0);
						dataBean.setListPosition(listSize + 1);
						dataBean.setAddNewList(true);
						dataBean.setMode("new");
						list.add(dataBean);
						btn_patch.setText(Integer.toString(list.size()));
						Log.i("avatar098",
								"btn_patch" + Integer.toString(list.size()));
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.notifyDataSetChanged();
							}
						});
						listView.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								listView.smoothScrollToPosition(list.size());
							}
						});
					} else {
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.wait_for_background_process),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showclearallAlert(SingleInstance.mainContext.getResources()
						.getString(R.string.settings));
				
			}

			private void showclearallAlert(String message) {
				try {
					AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
							getActivity());
					myAlertDialog.setTitle(SingleInstance.mainContext
							.getResources().getString(R.string.clear_all));
					myAlertDialog.setMessage(SingleInstance.mainContext
							.getResources().getString(
									R.string.do_you_want_to_clear_all));
					myAlertDialog.setPositiveButton(SingleInstance.mainContext
							.getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {

									// TODO Auto-generated method stub
									iscleared = true;
									Log.i("avatar_i", "ok");

									if (avatarpage.equalsIgnoreCase("settings")) {

										Log.i("avatar_i", "settings");
										ArrayList<OfflineRequestConfigBean> configList = DBAccess
												.getdbHeler(context)
												.getOfflineCallSettingsDetailsForDelete();
										if (configList != null
												&& configList.size() > 0) {
											showprogress();
											btn_patch.setText("0");
											setting.setVisibility(View.GONE);
											WebServiceReferences.webServiceClient
													.offlineconfiguration(
															CallDispatcher.LoginUser,
															configList, null,
															avatarActivity);

											DBAccess.getdbHeler(context)
													.deleteOfflineCallSettingDetails(
															null);
										} else if (list.size() > 0) {
											Log.i("avatar_i",
													"Else: configList != null&& configList.size() > 0");
											list.clear();
											btn_patch.setText("0");
											adapter.notifyDataSetChanged();
										}

										// mainLayout.removeAllViews();

									} else if (avatarpage
											.equalsIgnoreCase("response")) {
										Log.i("avatar_i", "response");

										for (int j = 0; j < allresponseview_container
												.getChildCount(); j++) {
											DBAccess.getdbHeler(context)
													.deleteOfflineCallResponseDetails(
															responsebean_map
																	.get(j)
																	.getId());
										}
										allresponseview_container
												.removeAllViews();

										btn_patch.setText(Integer
												.toString(allresponsecontainer
														.getChildCount()));

									} else if (avatarpage
											.equalsIgnoreCase("pending")) {
										Log.i("avatar_i", "pending");

										for (int j = 0; j < pendingclone_container
												.getChildCount(); j++) {
											DBAccess.getdbHeler(context)
													.deleteOfflinePendingClones(
															clone_bean.get(j)
																	.getId());
										}
										pendingclone_container.removeAllViews();
										btn_patch.setText(Integer
												.toString(pendingclone_container
														.getChildCount()));
									}
									CallDispatcher.clearAll = false;
									setting.setVisibility(View.GONE);

								}
							});
					myAlertDialog.setNegativeButton(SingleInstance.mainContext
							.getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int arg1) {
									// do something when the Cancel button is
									// clicked

									dialog.cancel();
								}
							});
					myAlertDialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					listView.setVisibility(View.VISIBLE);
					avatarpage = "settings";
					hint.setText(SingleInstance.mainContext.getResources()
							.getString(
									R.string.configure_avatar_when_you_needed));
					pending_container.setVisibility(View.GONE);
					plusBtn.setVisibility(View.VISIBLE);
					allresponsecontainer.setVisibility(View.GONE);
					settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
					settings.setTextColor(getResources()
							.getColor(R.color.white));
					response.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					response.setTextColor(getResources()
							.getColor(R.color.title));
					pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					pending.setTextColor(getResources().getColor(R.color.title));
					btn_patch.setText(Integer.toString(list.size()));
					if (list.size() > 0) {
						setting.setVisibility(View.VISIBLE);
					}else {
						setting.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		response.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allresponseview_container.removeAllViews();
				avatarpage = "response";
				hint.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.history_responses));
				listView.setVisibility(View.GONE);
				allresponsecontainer.setVisibility(View.VISIBLE);
				allresponseview_container.setVisibility(View.VISIBLE);
				pending_container.setVisibility(View.GONE);
				plusBtn.setVisibility(View.INVISIBLE);
				// settingscontainer.setVisibility(View.GONE);
				Log.i("clone", "=====>Inside1 pending");
				response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
				response.setTextColor(getResources().getColor(R.color.white));
				settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
				settings.setTextColor(getResources().getColor(R.color.title));
				pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
				pending.setTextColor(getResources().getColor(R.color.title));
				populateAllResponses();

				// if (allresponseview_container.getChildCount() > 0) {
				// btn_clearAll.setVisibility(View.VISIBLE);
				// } else {
				// btn_clearAll.setVisibility(View.GONE);
				// }
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
					// settingscontainer.setVisibility(View.GONE);
					listView.setVisibility(View.GONE);
					hint.setText(SingleInstance.mainContext.getResources()
							.getString(R.string.view_or_respond_back_avatar));
					plusBtn.setVisibility(View.INVISIBLE);
					Log.i("clone", "=====>Inside1 pending");
					allresponsecontainer.setVisibility(View.GONE);
					pendingclone_container.removeAllViews();
					pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
					pending.setTextColor(getResources().getColor(R.color.white));
					settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					settings.setTextColor(getResources()
							.getColor(R.color.title));
					response.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
					response.setTextColor(getResources()
							.getColor(R.color.title));
					populatePendingclones();
				}
			}
		});
		return view;

	}

	protected void populatePendingclones() {
		try {
			clone_bean.clear();
			ArrayList<OfflineRequestConfigBean> allresponse_list = new ArrayList<OfflineRequestConfigBean>();
			allresponse_list.addAll(DBAccess.getdbHeler(context)
					.getOfflinePendingClones(
							"where username=" + "\"" + CallDispatcher.LoginUser
									+ "\""));
			for (int i = 0; i < allresponse_list.size(); i++) {
				inflatePendingClones(i, allresponse_list.get(i));
				clone_bean.put(i, allresponse_list.get(i));
			}

			btn_patch.setText(Integer.toString(pendingclone_container
					.getChildCount()));
			if (pendingclone_container.getChildCount() > 0) {
				setting.setVisibility(View.VISIBLE);
			} else {
				setting.setVisibility(View.GONE);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inflatePendingClones(int tag, OfflineRequestConfigBean req_bean) {
		try {
			Log.d("NOTES", "----> inflateAllresponseUI");
			View view = getActivity().getLayoutInflater()
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
				DeleteExistingpendingclonefile(fragPosition);
				responsepath_map.put(tag, path);
			}

			tv_viewprofile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "====>inside view profile");
					int tag = (Integer) v.getTag();
					if (clone_bean.containsKey(tag)) {
						OfflineRequestConfigBean bean = clone_bean.get(tag);

						BuddyInformationBean bib = null;
						for (BuddyInformationBean temp : ContactsFragment
								.getBuddyList()) {
							if (!temp.isTitle()) {
								if (temp.getName().equalsIgnoreCase(
										bean.getFrom())) {
									bib = temp;
									break;
								}
							}
						}

						if (bib != null) {
							// BuddyInformationBean info =
							// WebServiceReferences.buddyList
							// .get(bean.getFrom());
							doViewProfile(false, bean.getFrom(),
									bib.getStatus());
						}
					}
				}

				private void doViewProfile(boolean accept, String buddy,
						String status) {

					try {

						profile_buddy = buddy;

						profile_buddystatus = status;

						ArrayList<String> profileList = DBAccess.getdbHeler(
								context).getProfile(buddy);
						Log.i("profile",
								"size of arrayList--->" + profileList.size());

						if (profileList.size() > 0) {
							// Intent intent = new Intent(context,
							// ViewProfiles.class);
							// intent.putExtra("buddyname", buddy);
							// intent.putExtra("buddystatus", status);
							// intent.putExtra("close", true);
							// startActivity(intent);
							viewProfile(buddy);

						} else {
							Log.i("profile", "VIEW PROFILE------>" + buddy
									+ "---->GetProfileDetails");
							// CallDispatcher.pdialog = new
							// ProgressDialog(context);
							// callDisp.showprogress(CallDispatcher.pdialog,
							// context);
							showprogress();
							CallDispatcher.isFromCallDisp = false;
							String modifiedDate = DBAccess.getdbHeler(context)
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

				private void viewProfile(String buddy) {
					ViewProfileFragment viewProfileFragment = ViewProfileFragment
							.newInstance(context);
					// Bundle bundle = new Bundle();
					// bundle.putString("buddyname", buddy);
					// viewProfileFragment.setArguments(bundle);
					viewProfileFragment.setBuddyName(buddy);
					FragmentManager fragmentManager = ((AppMainActivity) SingleInstance.contextTable
							.get("MAIN")).getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(
							R.id.activity_main_content_fragment,
							viewProfileFragment);
					fragmentTransaction.commit();

				}
			});
			tv_sendreply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int tag = (Integer) v.getTag();
					fragPosition = tag;
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

							BuddyInformationBean bib = null;
							for (BuddyInformationBean temp : ContactsFragment
									.getBuddyList()) {
								if (!temp.isTitle()) {
									if (temp.getName().equalsIgnoreCase(
											bean.getMessage())) {
										bib = temp;
										break;
									}
								}
							}

							if (bib != null) {
								// BuddyInformationBean bib =
								// (BuddyInformationBean)
								// WebServiceReferences.buddyList
								// .get(bean.getMessage());

								if (bib.getStatus().startsWith("Onli")) {
									CallDispatcher.conConference.add(bib
											.getName());
									callDisp.ConMadeConference("VC",
											context);
								}

								else {
									Toast.makeText(
											context,
											bib.getName()
													+ SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.is_on_offline),
											Toast.LENGTH_LONG).show();
								}

							}

						} else if (bean.getMessagetype().equalsIgnoreCase(
								"conference call")) {

							String[] str = bean.getMessage().split(",");
							String offlinenames = null;
							for (int i = 0; i < str.length; i++) {

								BuddyInformationBean bib = null;
								for (BuddyInformationBean temp : ContactsFragment
										.getBuddyList()) {
									if (temp.getName().equalsIgnoreCase(str[i])) {
										bib = temp;
										break;
									}
								}
								// BuddyInformationBean bib =
								// WebServiceReferences.buddyList
								// .get(str[i]);
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
						fragPosition = tag;
						if (clone_bean.containsKey(tag)) {

							for (int i = 0; i < pendingclone_container
									.getChildCount(); i++) {
								LinearLayout lay = (LinearLayout) pendingclone_container
										.getChildAt(i);
								int view_tag = (Integer) lay.getTag();
								if (tag == view_tag) {
									if (clone_bean.containsKey(tag)) {
										if (responsepath_map.containsKey(tag)) {
											// CallDispatcher.pdialog = new
											// ProgressDialog(
											// context);
											// callDisp.showprogress(
											// CallDispatcher.pdialog,
											// context);
											showprogress();

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
			// if (pending_container.getChildCount() > 0) {
			// btn_clearAll.setVisibility(View.VISIBLE);
			// } else {
			// btn_clearAll.setVisibility(View.GONE);
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setResponseMessageTypeView(String messageType,
			TextView replyType, TextView respone, TextView value_text,
			ImageView value_image, final String path, int tag_id) {

		try {
			if (messageType.equalsIgnoreCase("Photo")) {
				replyType.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.reply_back_by_photo_message));
				respone.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.click_here_to_response));
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
								value_image.setTag(strIPath);
								value_image
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stubo
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
				// respone.setVisibility(View.GONE);
				respone.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.click_here_to_response));

				replyType.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.reply_back_by_audio_message));
				if (path != null) {
					if (!path.equals("''") && path.trim().length() != 0)
						playresponseAudio(tag_id);
					else {
						// type = "audio";
						// fragResult = true;
						// strIPath = Environment.getExternalStorageDirectory()
						// + "/COMMedia/" + getFileName() + ".mp4";
						// getComponentPath = strIPath;
						// File file=new File(path);
						// if(file.exists())
						// Intent intent = new Intent(context,
						// MultimediaUtils.class);
						// Log.i("avatar098", "path====:"
						// + getComponentPath);
						// Log.i("avatar098", "path====:" + strIPath);
						//
						// intent.putExtra("filePath", strIPath);
						// intent.putExtra("requestCode", 33);
						// intent.putExtra("action", "audio");
						// intent.putExtra("createOrOpen", "create");
						// startActivityForResult(intent, 33);

					}
					// else
					// recordresponseAudio(tag_id);
				}
				// else
				// recordresponseAudio(tag_id);

			} else if (messageType.equalsIgnoreCase("Video")) {
				replyType.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.reply_back_by_video_message));
				respone.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.click_here_to_response));
				if (path != null) {
					if (!path.equals("''")) {

						if (value_text.getVisibility() == View.VISIBLE) {
							value_text.setVisibility(View.GONE);
						}
						value_image.setVisibility(View.VISIBLE);
						String filepath = Environment
								.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ path;
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
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// v_path);
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video", v_path);
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
						fle = null;
					}
				}
			} else if (messageType.equalsIgnoreCase("Text")) {
				replyType.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.reply_back_by_text_message));
				respone.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.click_here_to_response));
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
									Log.d("File", "#######################"
											+ st);
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
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void recordresponseAudio(int tag) {
	// try {
	// Log.i("avatar098", "recordresponseAudio-posittion" + tag);
	// View view = getActivity().getLayoutInflater().inflate(
	// R.layout.audio_recording, pendingclone_container, false);
	// final LinearLayout rec_container = (LinearLayout) view
	// .findViewById(R.id.audio_rec_container);
	// rec_container.setTag(tag);
	// ImageButton btn_startrec = (ImageButton) view
	// .findViewById(R.id.rec_start);
	// btn_startrec.setTag(0);
	// ProgressBar progress = (ProgressBar) view
	// .findViewById(R.id.progressBar_dwn);
	// progress.setVisibility(View.GONE);
	// final LinearLayout player_container = (LinearLayout) view
	// .findViewById(R.id.audio_player_container);
	// player_container.setTag(tag);
	// final ImageButton btn_play = (ImageButton) view
	// .findViewById(R.id.play_start);
	// btn_play.setTag(0);
	//
	// rec_container.setVisibility(View.VISIBLE);
	// player_container.setVisibility(View.GONE);
	//
	// LinearLayout clone_settings = (LinearLayout) pendingclone_container
	// .getChildAt(tag);
	// LinearLayout audio_layout = (LinearLayout) clone_settings
	// .findViewById(R.id.audio_layout);
	// ImageView iv_play = (ImageView) clone_settings
	// .findViewById(R.id.res_img_from);
	// iv_play.setVisibility(View.GONE);
	// TextView tv_msg = (TextView) clone_settings
	// .findViewById(R.id.res_txt_from);
	// tv_msg.setVisibility(View.GONE);
	// audio_layout.removeAllViews();
	// audio_layout.addView(view);
	// audio_layout.setVisibility(View.VISIBLE);
	// // fragPosition=tag;
	// btn_startrec.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// // TODO Auto-generated method stub
	// int tag = (Integer) v.getTag();
	// LinearLayout parent_layot = (LinearLayout) v
	// .getParent();
	// ProgressBar progress = (ProgressBar) parent_layot
	// .getChildAt(1);
	// if (tag == 0) {
	// if (!isrecording && !isPlaying) {
	// Log.i("avatar098", "!isrecording && !isPlaying");
	// int p_tag = (Integer) parent_layot.getTag();
	// ImageButton recorder_button = (ImageButton) v;
	// recorder_button.setTag(1);
	// // recorder_button
	// // .setBackgroundResource(R.drawable.v_play);
	// // recorder_button.setVisibility(View.GONE);
	// // rec_container.setVisibility(View.GONE);
	// // player_container.setVisibility(View.VISIBLE);
	// //btn_play.setVisibility(View.VISIBLE);
	//
	// String audio_path = Environment
	// .getExternalStorageDirectory()
	// + "/COMMedia/" + getFileName() + ".mp4";
	// DeleteExistingpendingclonefile(fragPosition);
	// responsepath_map.put(p_tag, audio_path);
	// // isrecording = true;
	// Intent intent = new Intent(context,
	// MultimediaUtils.class);
	// Log.i("avatar098", "path====:"
	// + getComponentPath);
	// Log.i("avatar098", "path====:" + audio_path);
	//
	// intent.putExtra("filePath", audio_path);
	// intent.putExtra("requestCode", 33);
	// intent.putExtra("action", "audio");
	// intent.putExtra("createOrOpen", "create");
	// type = "audio";
	// fragResult = true;
	// getComponentPath = audio_path;
	// // startActivityForResult(intent, 33);
	// startActivity(intent);
	//
	// } else {
	// if (isrecording)
	// showToast("Please Stop existing recording");
	// else if (isPlaying)
	// showToast("Kindly stop the audio player");
	// }
	// } else {
	// v.setTag(0);
	// progress.setVisibility(View.GONE);
	// isrecording = false;
	// LinearLayout lay = (LinearLayout) parent_layot
	// .getParent();
	// LinearLayout l_child = (LinearLayout) lay
	// .getChildAt(1);
	// l_child.setVisibility(View.VISIBLE);
	// parent_layot.setVisibility(View.GONE);
	//
	// }
	// } catch (IllegalStateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// });
	//
	// btn_play.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// // TODO Auto-generated method stub
	// LinearLayout parent_layot = (LinearLayout) v
	// .getParent();
	// int p_tag = (Integer) parent_layot.getTag();
	// int view_tag = (Integer) v.getTag();
	// ImageButton btn_play = (ImageButton) v;
	//
	// if (responsepath_map.containsKey(p_tag)) {
	// String path = responsepath_map.get(p_tag);
	// Intent intent = new Intent(context,
	// MultimediaUtils.class);
	// intent.putExtra("filePath", path);
	// intent.putExtra("requestCode", AUDIO);
	// intent.putExtra("action", "audio");
	// intent.putExtra("createOrOpen", "open");
	// startActivity(intent);
	//
	// } else
	// Log.d("LM", "----path not available-----");
	//
	// } catch (IllegalArgumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalStateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	private void playresponseAudio(int tag) {
		try {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.audio_recording, pendingclone_container, false);
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
			ImageButton btn_play = (ImageButton) view
					.findViewById(R.id.play_start);
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
						LinearLayout parent_layot = (LinearLayout) v
								.getParent();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getPendingMessageTypeView(String messageType,
			final String path, int position1) {
		try {
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

				value_text.setText(buffer.toString());
				value_text.setVisibility(View.VISIBLE);

			} else if (messageType.equalsIgnoreCase("Photo")) {
				actionValue
						.setText(SingleInstance.mainContext
								.getResources()
								.getString(
										R.string.please_see_below_for_photo_message));
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
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
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
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
									in.putExtra("Photo_path", v.getTag()
											.toString());
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
							v_path = v_path.substring(0,
									v_path.lastIndexOf(".mp4"));
						Log.d("clone", "video path-->" + v_path);
						final File vfileCheck = new File(v_path + ".mp4");
						if (vfileCheck.exists()) {

							Intent intentVPlayer = new Intent(context,
									VideoPlayer.class);
							// intentVPlayer
							// .putExtra("File_Path", v_path + ".mp4");
							// intentVPlayer.putExtra("Player_Type",
							// "Video Player");
							intentVPlayer.putExtra("video", v_path + ".mp4");
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
				// scroll_opt.setVisibility(View.GONE);
			} else {
				value_text.setText(path);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void deleteFile(int tag) {
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void deletePendingClones(int tag) {
		try {
			if (clone_bean.containsKey(tag)) {
				DBAccess.getdbHeler(context).deleteOfflinePendingClones(
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
			pendingclone_container.removeAllViews();
			populatePendingclones();
			btn_patch.setText(Integer.toString(pendingclone_container
					.getChildCount()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void showResponseDialog(final int tag, String rtype) {
		try {
			if (rtype != null) {
				fragPosition = tag;
				String msg_type;
				Log.d("clone", "----came to showresponse dialog---- tag = "
						+ tag + " rtype = " + rtype);
				if (rtype.equalsIgnoreCase("text")) {
					msg_type = "Text";
					type = "text";
					Intent intent = new Intent(context, CloneTextinput.class);
					fragResult = true;
					startActivityForResult(intent, 33);
				} else if (rtype.equalsIgnoreCase("photo")) {
					fragPosition = tag;
					type = "photo";
					msg_type = "Photo";
					photochat();
				} else if (rtype.equalsIgnoreCase("audio")) {
					fragPosition = tag;
					msg_type = "Audio";
					type = "audio";
					fragResult = true;
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + getFileName() + ".mp4";
					getComponentPath = strIPath;
					Intent intent = new Intent(context, MultimediaUtils.class);
					Log.i("avatar098", "path====:" + getComponentPath);
					Log.i("avatar098", "path====:" + strIPath);
					// AUDIO=true;
					intent.putExtra("filePath", strIPath);
					intent.putExtra("requestCode", 33);
					intent.putExtra("action", "audio");
					intent.putExtra("createOrOpen", "create");
					// startActivityForResult(intent, 33);
					startActivityForResult(intent, 33);

					// startActivity(intent);

					// recordresponseAudio(tag);
				} else if (rtype.equalsIgnoreCase("video")) {
					type = "video";
					fragPosition = tag;
					msg_type = "Video";
					type = "video";
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + getFileName() + ".mp4";
					getComponentPath = strIPath;
					Intent intent = new Intent(context, CustomVideoCamera.class);
					intent.putExtra("filePath", strIPath);
					// intent.putExtra("requestCode", 33);
					// intent.putExtra("action",
					// MediaStore.ACTION_VIDEO_CAPTURE);
					// intent.putExtra("createOrOpen", "create");
					fragResult = true;
					startActivityForResult(intent, 33);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void photochat() {

		try {
			final String[] items = new String[] {
					SingleInstance.mainContext.getResources().getString(
							R.string.gallery),
					SingleInstance.mainContext.getResources().getString(
							R.string.photo) };
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void messageResponseType(String strMMType) {
		Log.i("clone", "===> inside message response");

		try {
			if (strMMType.equals("Gallery")) {
				Log.i("clone", "====> inside gallery");
				if (Build.VERSION.SDK_INT < 19) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					fragResult = true;
					startActivityForResult(intent, 30);
				} else {
					Log.i("img", "sdk is above 19");
					Log.i("clone", "====> inside gallery");
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					fragResult = true;
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
					// Intent intent = new Intent(context,
					// MultimediaUtils.class);
					// intent.putExtra("filePath", strIPath);
					// intent.putExtra("requestCode", 32);
					// intent.putExtra("action",
					// MediaStore.ACTION_IMAGE_CAPTURE);
					// intent.putExtra("createOrOpen", "create");
					// startActivity(intent);
					Intent intent = new Intent(context, CustomVideoCamera.class);
					intent.putExtra("filePath", strIPath);
					intent.putExtra("isPhoto", true);
					fragResult = true;

					startActivityForResult(intent, 32);
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

	private void DeleteExistingpendingclonefile(int pos) {
		try {
			String original_path = "";
			original_path = responsepath_map.get(pos);
			if (original_path != null) {
				File fle = new File(original_path);
				if (!DBAccess.getdbHeler(context).isfileisinuse(fle.getName())) {
					if (fle.exists())
						fle.delete();
				}
				fle = null;
			}
			original_path = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void populateAllResponses() {
		try {
			allresponse_list.clear();
			allresponse_list.addAll(DBAccess.getdbHeler(context)
					.getOfflineCallResponseDetails(
							"where userid=" + "\"" + CallDispatcher.LoginUser
									+ "\""));
			Log.d("avatar123", "Respose count--->" + allresponse_list.size());
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
			if ((allresponseview_container.getChildCount()) > 0) {
				setting.setVisibility(View.VISIBLE);

			} else {
				setting.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void inflateAllresponseUI(int tag, OfflineRequestConfigBean req_bean) {
		try {
			Log.d("NOTES", "----> inflateAllresponseUI");
			Log.d("avatar123", "----> position : " + tag);
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.customallresponse, allresponseview_container,
					false);
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
			ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id.progress_customallresponse);

			tv_received_time.setText(req_bean.getReceivedTime());

			delete_response.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int my_tag = (Integer) arg0.getTag();
					fragPosition = my_tag;
					for (int i = 0; i < allresponseview_container
							.getChildCount(); i++) {
						LinearLayout parent_view = (LinearLayout) allresponseview_container
								.getChildAt(i);
						Button btn = (Button) parent_view
								.findViewById(R.id.response_deletebtn);
						int tag = (Integer) btn.getTag();

						Log.d("NOTES", "---->" + tag);
						Log.d("NOTES", "---->" + i);
						Log.d("NOTES", "received tg---->" + fragPosition);
						Log.d("NOTES", "received tg---->" + my_tag);

						if (tag == my_tag) {
							{
								if (responsebean_map.containsKey(tag)) {
									if (WebServiceReferences.running) {
										if (fragPosition == tag) {
											allresponseview_container
													.removeViewAt(i);
											Log.d("clone",
													"contains"
															+ responsebean_map
																	.get(fragPosition)
																	.getId());
											if (responsebean_map
													.containsKey(fragPosition)) {
												Log.d("clone",
														"contains"
																+ responsebean_map
																		.get(fragPosition)
																		.getId());
												DBAccess.getdbHeler(context)
														.deleteOfflineCallResponseDetails(
																responsebean_map
																		.get(fragPosition)
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
			// if (allresponsecontainer.getChildCount() > 0) {
			// btn_clearAll.setVisibility(View.VISIBLE);
			// } else {
			// btn_clearAll.setVisibility(View.GONE);
			// }

			setallResponseMessageTypeView(req_bean.getResponseType(),
					tv_resvalue, res_image, progressBar,
					req_bean.getResponse(), tag, true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setallResponseMessageTypeView(String messageType,
			TextView value_text, ImageView value_image, ProgressBar progressBa,
			final String path, int tag_id, boolean progress) {

		try {
			LinearLayout parent_view = null;
			for (int i = 0; i < allresponseview_container.getChildCount(); i++) {
				parent_view = (LinearLayout) allresponseview_container
						.getChildAt(i);
				if ((Integer) parent_view.getTag() == tag_id)
					break;
			}

			ProgressBar progressBar = (ProgressBar) parent_view
					.findViewById(R.id.progress_customallresponse);

			if (progress == true) {
				progressBar.setVisibility(View.VISIBLE);
				Log.i("avatar_test", "progress: 1 =" + progress);

			} else if (progress == false) {
				progressBar.setVisibility(View.GONE);
				Log.i("avatar_test", "progress: 0 =" + progress);
			}

			if (messageType.equalsIgnoreCase("Photo")) {
				if (path != null) {
					Log.i("avatar_test",
							"Came to if path != null progress: 1 =" + progress);

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
								img = Bitmap.createScaledBitmap(img, 100, 75,
										false);

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
								if (progressBar.getVisibility() == View.VISIBLE) {
									progressBar.setVisibility(View.GONE);
								}

							}
						}
					}
				}

			} else if (messageType.equalsIgnoreCase("Audio")) {
				if (!path.equals("''")) {
					progressBar.setVisibility(View.GONE);
					playallresponseAudio(tag_id, progress);
				}

			} else if (messageType.equalsIgnoreCase("Video")) {
				if (!path.equals("''")) {

					if (value_text.getVisibility() == View.VISIBLE) {
						value_text.setVisibility(View.GONE);
					}
					if (progress == false) {
						value_image.setVisibility(View.VISIBLE);
					}
					String filepath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + path;

					Log.d("clone", "video path-->" + filepath);
					value_image.setPadding(30, 10, 30, 0);
					value_image.setTag(filepath);
					value_image.setImageDrawable(this.getResources()
							.getDrawable(R.drawable.v_play));

					final ImageView im = value_image;
					File file = new File(filepath);

					if (file.exists()) {
						progressBar.setVisibility(View.GONE);
						value_image.setVisibility(View.VISIBLE);

					}
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

								Toast.makeText(
										context,
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.file_downloading),
										1).show();
						}
					});
				} else
					progressBar.setVisibility(View.VISIBLE);

			} else if (messageType.equalsIgnoreCase("Text")) {
				value_image.setVisibility(View.GONE);
				if (path != null) {
					StringBuffer buffer = new StringBuffer();
					String filepath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + path;
					File fle = new File(filepath);
					if (fle.exists()) {
						try {
							if (progressBar.getVisibility() == View.VISIBLE) {
								progressBar.setVisibility(View.GONE);
							}
							FileInputStream fis = new FileInputStream(filepath);
							BufferedReader rdr = new BufferedReader(
									new InputStreamReader(fis));
							String st;
							try {
								while ((st = rdr.readLine()) != null) {
									Log.d("File", "#######################"
											+ st);
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
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void playallresponseAudio(int tag, boolean pro) {

		try {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.audio_recording, pendingclone_container, false);
			ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id.progress_audio);
			LinearLayout audio_container = (LinearLayout) view
					.findViewById(R.id.audio_container);
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
			ImageButton btn_play = (ImageButton) view
					.findViewById(R.id.play_start);
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

			if (pro == false) {
				audio_container.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
			} else if (pro == true) {
				progressBar.setVisibility(View.VISIBLE);
				audio_container.setVisibility(View.GONE);

			}
			String path = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + responsebean_map.get(tag).getResponse();

			File file = new File(path);

			if (file.exists()) {
				Log.i("avatar_test", "file.exists()");
				progressBar.setVisibility(View.GONE);
				audio_container.setVisibility(View.VISIBLE);

			}

			btn_play.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// // TODO Auto-generated method stub
						LinearLayout parent_layot = (LinearLayout) v
								.getParent();
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
										+ responsebean_map.get(p_tag)
												.getResponse();

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// String position;
			Log.i("avatar_i", "onActivityResult");

			if (fragResult) {
				Log.i("avatar_i", "fragResult");

				// LinearLayout parent_view = (LinearLayout) mainLayout
				// .getChildAt(fragPosition);
				ImageView value_img = null;
				TextView value_text = null;
				LinearLayout parent_view1 = null;
				for (int i = 0; i < pendingclone_container.getChildCount(); i++) {

					parent_view1 = (LinearLayout) pendingclone_container
							.getChildAt(i);
					parent_view1.setTag(i);
					if (parent_view1 != null) {
						if ((Integer) parent_view1.getTag() == fragPosition) {

							Log.i("avatar098",
									"(Integer) parent_view1.getTag()"
											+ (Integer) parent_view1.getTag());
							Log.i("avatar098", "requestCode" + requestCode);
							Log.i("avatar098", "fragPosition" + fragPosition);

							value_img = (ImageView) parent_view1
									.findViewById(R.id.value_img);
							value_text = (TextView) parent_view1
									.findViewById(R.id.value_txt);
							break;
						}
					}
				}

				if (requestCode == 30) {

					if (resultCode == Activity.RESULT_CANCELED) {
						Log.d("Avataar", "Rsult canceled");
					} else if (data != null) {

						Uri selectedImageUri = data.getData();
						strIPath = callDisp
								.getRealPathFromURI(selectedImageUri);
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
							img = Bitmap
									.createScaledBitmap(img, 100, 75, false);

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
								DeleteExistingpendingclonefile(fragPosition);
								responsepath_map.put(fragPosition, path);

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
						context.getContentResolver()
								.takePersistableUriPermission(selectedImageUri,
										takeFlags);
						strIPath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + getFileName() + ".jpg";
						DeleteExistingpendingclonefile(fragPosition);
						responsepath_map.put(fragPosition, strIPath);
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
							callDisp.changemyPictureOrientation(bitmap,
									strIPath);
							bitmap = callDisp.ResizeImage(strIPath);
							bitmap = Bitmap.createScaledBitmap(bitmap, 200,
									150, false);
							// }
							DeleteExistingpendingclonefile(fragPosition);
							responsepath_map.put(fragPosition, strIPath);

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

						DeleteExistingpendingclonefile(fragPosition);
						if (responsepath_map.containsKey(fragPosition)) {
							responsepath_map.remove(fragPosition);
						}
						responsepath_map.put(fragPosition, getComponentPath);

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
								Log.i("avatar098", "audio" + "isPlaying :"
										+ isPlaying + " isrecording :"
										+ isrecording);
								isrecording = false;
								if (value_text.getVisibility() == View.VISIBLE) {
									value_text.setVisibility(View.GONE);
								}
								value_img.setVisibility(View.VISIBLE);
								value_img.setPadding(30, 10, 30, 0);
								value_img.setImageDrawable(this.getResources()
										.getDrawable(R.drawable.v_play));

								final ImageView img = value_img;

								Log.i("bug", "INSIDE PHOTO TRUE");
								final File vfileCheck = new File(
										getComponentPath);
								chatplayer = new MediaPlayer();

								value_img
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												if (vfileCheck.exists()) {

													try {
														Intent intent = new Intent(
																context,
																MultimediaUtils.class);
														intent.putExtra(
																"filePath",
																getComponentPath);
														intent.putExtra(
																"requestCode",
																33);
														intent.putExtra(
																"action",
																"audio");
														intent.putExtra(
																"createOrOpen",
																"open");
														startActivityForResult(
																intent, 33);

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
																		chatplayer
																				.reset();
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
													Toast.makeText(context,
															"File Downloading",
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
									getComponentPath = getComponentPath
											+ ".mp4";
								}
								final File vfileCheck = new File(
										getComponentPath);
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
											// intentVPlayer.putExtra(
											// "Player_Type",
											// "Video Player");
											intentVPlayer.putExtra("video",
													getComponentPath);
											startActivity(intentVPlayer);
										} else

											Toast.makeText(context,
													"File Downloading", 1)
													.show();
									}
								});
							}
						}

					}
				}
				fragResult = false;
			} else {
				if (GALLERY) {
					Log.i("avatar_i", "GALLERY");

					if (messageMap.containsKey(requestCode)
							&& resultCode == Activity.RESULT_OK) {
						Log.i("avatar_i",
								"messageMap.containsKey(requestCode && resultCode == Activity.RESULT_OK");

						OfflineRequestConfigBean bean = (OfflineRequestConfigBean) adapter
								.getItem(requestCode);
						if (KITKAT == false) {
							Log.i("avatar_i", "KITKAT==false");

							Uri selectedImageUri = data.getData();
							final int takeFlags = data.getFlags()
									& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
							Log.i("avatar_i", "selectedImageUri"
									+ selectedImageUri + "takeFlags"
									+ takeFlags);

							// context.getContentResolver()
							// .takePersistableUriPermission(selectedImageUri,
							// takeFlags);
							// strIPath =
							// Environment.getExternalStorageDirectory()
							// + "/COMMedia/" + getFileName() + ".jpg";

							strIPath = CompleteListView.getFileName() + ".jpg";

							strIPath = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/MPD_" + strIPath;
							path_map.put(requestCode, strIPath);
							messageMap.remove(requestCode);
							messageMap.put(requestCode, strIPath);
							// addRemovedFiles(requestCode, "settings_");

							bean.setMessage(strIPath);
							bean.setUri(selectedImageUri);
							bean.setRequestCode(requestCode);
							bean.setResultCode(resultCode);
							bean.setMessagetype("photo");

							// addRemovedFiles(requestCode, "settings_");
							// path_map.put(requestCode, strIPath);
							new bitmaploader().execute(selectedImageUri);

							list.set(requestCode, bean);
							adapter.notifyDataSetChanged();

							// handler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// // TODO Auto-generated method stub
							//
							// }
							// });

						} else {
							Log.i("avatar_i", "KITKAT");

							bean.setResultCode(resultCode);
							if (resultCode == Activity.RESULT_CANCELED) {
							} else if (data != null) {

								Uri selectedImageUri = data.getData();
								final int takeFlags = data.getFlags()
										& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
								context.getContentResolver()
										.takePersistableUriPermission(
												selectedImageUri, takeFlags);

								// strIPath = callDisp
								// .getRealPathFromURI(selectedImageUri);
								strIPath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ "MPD_"
										+ callDisp.getFileName() + ".jpg";
								path_map.put(requestCode, strIPath);
								bean.setMessage(strIPath);
								bean.setUri(selectedImageUri);
								bean.setRequestCode(requestCode);
								bean.setMessagetype("photo");
								// addRemovedFiles(requestCode, "settings_");

								new bitmaploader().execute(selectedImageUri);
								list.set(requestCode, bean);
								adapter.notifyDataSetChanged();
								// File selected_file = new File(strIPath);
								// int length = (int) selected_file.length() /
								// 1048576;
								// Log.d("busy",
								// "........ size is------------->"
								// + length);

							}

						}
					}
					GALLERY = false;
				} else if (AUDIO) {
					Log.i("avatar_i", "AUDIO");

					if (messageMap.containsKey(requestCode)) {
						Log.i("avatar_i", "messageMap.containsKey(requestCode)"
								+ messageMap.get(requestCode));
						OfflineRequestConfigBean bean = (OfflineRequestConfigBean) adapter
								.getItem(requestCode);
						bean.setMessagetype("audio");
						bean.setMessage(strIPath);
						Log.i("avatar_i", "strIPath" + strIPath);
						path_map.put(requestCode, strIPath);

						list.set(requestCode, bean);
						adapter.notifyDataSetChanged();

					}
					AUDIO = false;
				} else if (TEXT) {
					if (messageMap.containsKey(requestCode)) {
						if (getComponentPath == null) {
							getComponentPath = data.getStringExtra("filename");
							// position=data.getStringExtra("position");
							Log.i("avatar_i", "===>" + getComponentPath);
						}

						if (getComponentPath != null) {
							Log.i("avatar_i",
									"===> + " + getComponentPath.length());
							int pos = data.getIntExtra("position", 0);
							OfflineRequestConfigBean dataBean = (OfflineRequestConfigBean) adapter
									.getItem(pos);
							dataBean.setMessage(data.getStringExtra("filename"));
							dataBean.setMessagetype("text");
							path_map.put(pos, data.getStringExtra("filename"));
							list.set(pos, dataBean);
							adapter.notifyDataSetChanged();
						}
					}
					TEXT = false;
				} else if (CAMERA) {
					Log.i("avatar_i", "onActivityResult-----------0");
					if (messageMap.containsKey(requestCode)) {
						if (resultCode == Activity.RESULT_OK) {
							// File file = new File(image_path);
							// if (file.exists()) {
							int pos = data.getIntExtra("others", 0);
							String path = data.getStringExtra("filePath");
							Log.i("avatar_i", "path" + path);

							Log.i("avatar_i", "onActivityResult--pos" + pos);
							strIPath = path;
							OfflineRequestConfigBean dataBean = (OfflineRequestConfigBean) adapter
									.getItem(pos);
							dataBean.setMessage(strIPath);
							dataBean.setMessagetype("photo");
							path_map.put(pos, strIPath);
							// addRemovedFiles(requestCode, "settings_");

							list.set(pos, dataBean);
							adapter.notifyDataSetChanged();
							// }
						} else {
							Toast.makeText(context,SingleInstance.mainContext.getResources().getString(R.string.result_cancelled),
									Toast.LENGTH_SHORT).show();

						}
					}
					CAMERA = false;
				} else if (VIDEO) {
					if (messageMap.containsKey(requestCode)
							&& resultCode == Activity.RESULT_OK) {
						int pos = data.getIntExtra("others", 0);
						String path = data.getStringExtra("filePath");
						Log.i("avatar_i", "path" + path);

						Log.i("avatar_i", "onActivityResult--pos" + pos);
						image_path = path;
						OfflineRequestConfigBean dataBean = (OfflineRequestConfigBean) adapter
								.getItem(pos);
						// OfflineRequestConfigBean dataBean =
						// (OfflineRequestConfigBean) list.get(pos);
						dataBean.setMessage(image_path);
						dataBean.setMessagetype("video");
						dataBean.setResultCode(resultCode);
						path_map.put(pos, image_path);
						// addRemovedFiles(requestCode, "settings_");

						list.set(pos, dataBean);
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(context, "Result cancelled",
								Toast.LENGTH_SHORT).show();

					}

					VIDEO = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	@Override
	public void onDestroy() {
		if (btn_patch != null) {
			btn_patch.setVisibility(View.GONE);
		}
		super.onDestroy();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (WebServiceReferences.contextTable
							.containsKey("avatarset")) {
						WebServiceReferences.contextTable.remove("avatarset");
					}
					SingleInstance.contextTable.remove("avatar");
					if (view != null) {
						MemoryProcessor.getInstance().unbindDrawables(view);
					}
					view = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	// @Override
	// public void onResume(){
	// super.onResume();
	// // handler.post(new Runnable() {
	//
	// // @Override
	// // public void run() {
	// // TODO Auto-generated method stub
	//
	// try {
	// Log.i("avatar098", "onResume");
	// File file=new File(getComponentPath);
	// if(!file.exists()){
	// View view = getActivity().getLayoutInflater().inflate(
	// R.layout.audio_recording, pendingclone_container, false);
	// final LinearLayout rec_container = (LinearLayout) view
	// .findViewById(R.id.audio_rec_container);
	// rec_container.setVisibility(View.VISIBLE);
	// ImageButton btn_startrec = (ImageButton) view
	// .findViewById(R.id.rec_start);
	// Log.i("avatar098", "!file.exists()");
	//
	// btn_startrec.setVisibility(View.VISIBLE);
	//
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	//
	// // }
	// // });
	// // new Thread(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // try {
	// // Log.i("avatar098", "onResume");
	// // File file=new File(getComponentPath);
	// // if(!file.exists()){
	// // view = getActivity().getLayoutInflater().inflate(
	// // R.layout.audio_recording, pendingclone_container, false);
	// // final LinearLayout rec_container = (LinearLayout) view
	// // .findViewById(R.id.audio_rec_container);
	// // rec_container.setVisibility(View.VISIBLE);
	// // ImageButton btn_startrec = (ImageButton) view
	// // .findViewById(R.id.rec_start);
	// // btn_startrec.setVisibility(View.VISIBLE);
	// //
	// // }
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// //
	// // }
	// // }).start();
	//
	// }

	public class SetAvatarAdapter extends BaseAdapter {
		ArrayList<OfflineRequestConfigBean> list;
		Context context;

		// private ArrayAdapter<String> buddyAdapter = null;
		// CallDispatcher callDisp;
		// OfflineRequestConfigBean bean;

		// private Bitmap bitmap = null;

		public SetAvatarAdapter(Context context,
				ArrayList<OfflineRequestConfigBean> list) {
			// TODO Auto-generated constructor stub

			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			Log.i("avatar_i", "getItem" + arg0);

			OfflineRequestConfigBean bean = (OfflineRequestConfigBean) list
					.get(arg0);
			return bean;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			convertView = null;
			// viewPosition = position;
			if (convertView == null) {
				holder = new ViewHolder();

				try {
					Log.i("avatar_i", "convertView == null");

					holder.bean = (OfflineRequestConfigBean) list.get(position);
					Log.i("avatar_i", "===>" + holder.bean);

					btn_patch.setText(Integer.toString(list.size()));

					holder.inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = holder.inflater.inflate(R.layout.clonecustom,
							null);
					holder.touser_title = (TextView) convertView
							.findViewById(R.id.touser_title);
					holder.touser_title.setTag(position);

					holder.touser = (TextView) convertView
							.findViewById(R.id.touser_result);
					holder.touser.setTag(position);
					if (holder.bean.getBuddyId() != null) {

						if (holder.bean.getBuddyId().equals("default")
								|| holder.bean.getBuddyId().equalsIgnoreCase(
										CallDispatcher.LoginUser)) {
							holder.bean.setBuddyId("All Users");
						} else {
							holder.touser.setText(holder.bean.getBuddyId());
						}
					} else {
						holder.bean.setBuddyId(SingleInstance.mainContext
								.getResources().getString(R.string.all_users));
					}

					holder.title = (EditText) convertView
							.findViewById(R.id.title);
					holder.title.setTag(position);
					if (holder.bean.getMessageTitle() != null) {
						holder.title.setText(holder.bean.getMessageTitle()
								.toString());
					}

					holder.message_val = (TextView) convertView
							.findViewById(R.id.message_val);
					holder.message_val.setTag(position);

					// holder.reply_back_option = (Spinner) convertView
					// .findViewById(R.id.responseType);
					// holder.reply_back_option.setTag(position);

					holder.reply_back_tv = (TextView) convertView
							.findViewById(R.id.resType);
					holder.reply_back_tv.setTag(position);

					holder.reply_back_tv_result = (TextView) convertView
							.findViewById(R.id.resType_result);
					holder.reply_back_tv_result.setTag(position);
					if (holder.bean.getResponseType() != null
							&& (holder.bean.getResponseType().trim().length() > 0)) {
						Log.i("Avatar098", " " + holder.bean.getResponseType());
						if (holder.bean.getResponseType().trim().equals("''")) {
							holder.reply_back_tv_result
									.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.please_select));

						} else {
							holder.reply_back_tv_result.setText(holder.bean
									.getResponseType());
						}
					} else {
						Log.i("Avatar098",
								" else " + holder.bean.getResponseType());

						holder.reply_back_tv_result
								.setText(SingleInstance.mainContext
										.getResources().getString(
												R.string.please_select));
					}

					holder.url = (EditText) convertView.findViewById(R.id.url);
					holder.url.setTag(position);
					if (holder.bean.getUrl() != null
							&& !(holder.bean.getUrl().equals("''"))) {
						holder.url.setText(holder.bean.getUrl().toString());
					} else {
						holder.url.setText("");
					}

					holder.when = (TextView) convertView
							.findViewById(R.id.tv_when);
					holder.when.setTag(position);

					holder.tv_whenselected = (TextView) convertView
							.findViewById(R.id.tv_whenselected);
					holder.tv_whenselected.setTag(position);
					// if (holder.bean.getWhenreply() != null) {
					// holder.tv_whenselected.setText(holder.bean.getWhenreply()
					// .toString());
					// }
					// if (holder.bean.getWhen() != null) {
					// String getwhen = holder.bean.getWhen().trim();
					// if (getwhen.equals("1")) {
					// holder.tv_whenselected.setText("No Answer");
					//
					// } else if (getwhen.equals("2")) {
					// holder.tv_whenselected.setText("Unable TO Connect");
					//
					// } else if (getwhen.equals("3")) {
					// holder.tv_whenselected.setText("Call Dropped");
					//
					// } else if (getwhen.equals("4")) {
					// holder.tv_whenselected.setText("Call Disconnected");
					//
					// } else if (getwhen.equals("5")) {
					// holder.tv_whenselected.setText("Offline");
					//
					// } else if (getwhen.equals("6")) {
					// holder.tv_whenselected.setText("Call Rejected");
					//
					// }
					// }

					if (holder.bean != null) {
						String when = "";
						if (holder.bean.getWhen() != null) {
							if (holder.bean.getWhen().trim().length() > 0) {
								String[] when_ids = holder.bean.getWhen()
										.split(",");
								for (String when_id : when_ids) {
									String c_description = DBAccess.getdbHeler(
											context).getwheninfo(
											"select cdescription from clonemaster where cid=\""
													+ when_id + "\"");
									if (when.trim().length() == 0)
										when = c_description;
									else
										when = when + "," + c_description;
								}
								if (when_ids.length == 0)
									when = SingleInstance.mainContext
											.getResources().getString(
													R.string.avatar_mode);
							} else
								when = SingleInstance.mainContext
										.getResources().getString(R.string.avatar_mode);

						} else
							when = SingleInstance.mainContext.getResources()
									.getString(R.string.avatar_mode);

						holder.tv_whenselected.setText(when);
					}
					holder.message = (TextView) convertView
							.findViewById(R.id.value_txt);
					holder.message.setTag(position);
					// if (holder.bean.getMessage() != null) {
					// holder.message.setText(bean.getMessage().toString());
					// }

					holder.value_img = (ImageView) convertView
							.findViewById(R.id.value_img);
					holder.value_img.setTag(position);
					// holder.value_text = (TextView) convertView
					// .findViewById(R.id.value_txt);
					holder.audio_layout = (LinearLayout) convertView
							.findViewById(R.id.audio_layout);

					holder.select_buddies = (TextView) convertView
							.findViewById(R.id.tv_buddyselection);
					holder.select_buddies.setTag(position);

					holder.ed_gsm = (EditText) convertView
							.findViewById(R.id.ed_gsmno);
					holder.ed_gsm.setTag(position);
					Log.i("avatar098", "position : " + position + " message : "
							+ holder.bean.getMessage());
					if (holder.bean.getMessagetype() != null) {
						Log.i("avatar_i", "holder.bean.getMessageType()"
								+ holder.bean.getMessagetype() + " "
								+ holder.bean.getMessage());

						if (holder.bean.getMessagetype().equalsIgnoreCase(
								"text")) {
							holder.select_buddies.setVisibility(View.GONE);
							holder.ed_gsm.setVisibility(View.GONE);
							String getComponentPath = holder.bean.getMessage();

							Log.i("avatar_i",
									"holder.bean.getMessageType() =="
											+ holder.bean.getMessagetype()
											+ " " + getComponentPath + " "
											+ holder.bean.getMessage());

							if (holder.bean.getMessage() != null) {
								Log.i("avatar_i", "getComponentPath != null"
										+ getComponentPath);

								if (getComponentPath.endsWith(".txt")) {
									Log.i("avatar_i",
											"getComponentPath.endsWith("
													+ ".txt" + ")"
													+ getComponentPath);

									if (holder.value_img.getVisibility() == View.VISIBLE) {
										Log.i("avatar_i",
												"holder.value_img.getVisibility() == View.VISIBLE");
										holder.value_img
												.setVisibility(View.GONE);
									}
									if (holder.audio_layout != null) {
										Log.i("avatar_i",
												"holder.audio_layout != null");

										if (holder.audio_layout.getVisibility() == View.VISIBLE) {
											Log.i("avatar_i",
													"holder.audio_layout.getVisibility() == View.VISIBLE");
											holder.audio_layout
													.removeAllViews();
											holder.audio_layout
													.setVisibility(View.GONE);
										}
									}

									File txt_file = new File(getComponentPath);
									if (txt_file.exists()) {
										try {
											FileInputStream stream = new FileInputStream(
													txt_file);
											BufferedReader reader = new BufferedReader(
													new InputStreamReader(
															stream));
											String line;
											StringBuffer buffer = new StringBuffer();
											while ((line = reader.readLine()) != null) {
												buffer.append(line);
												buffer.append("\n");

											}
											holder.message.setText(buffer
													.toString());
											holder.message
													.setTextColor(Color.BLACK);
											holder.message
													.setVisibility(View.VISIBLE);
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									// notifyDataSetChanged();
								}
							}

						} else if (holder.bean.getMessagetype()
								.equalsIgnoreCase("photo")) {
							String getComponentPath = holder.bean.getMessage();
							Log.i("avatar_i", "getComponentPath"
									+ getComponentPath);
							File fileCheck = new File(getComponentPath);
							if (fileCheck.exists()) {
								imageLoader.DisplayImage(getComponentPath,
										holder.value_img, R.drawable.refresh);
								path_map.put(position, strIPath);
								holder.select_buddies.setVisibility(View.GONE);
								holder.ed_gsm.setVisibility(View.GONE);
								holder.value_img.setVisibility(View.VISIBLE);
								holder.value_img.setPadding(10, 10, 10, 10);
								if (holder.message.getVisibility() == View.VISIBLE) {
									holder.message.setVisibility(View.GONE);
								}

								if (holder.audio_layout != null) {
									if (holder.audio_layout.getVisibility() == View.VISIBLE) {
										holder.audio_layout.removeAllViews();
										holder.audio_layout
												.setVisibility(View.GONE);
									}
								}
							}
						} else if (holder.bean.getMessagetype()
								.equalsIgnoreCase("video")) {
							String getPath = holder.bean.getMessage();

							File txt_file = new File(getPath);
							if (txt_file.exists()) {

								if (holder.message.getVisibility() == View.VISIBLE) {
									holder.message.setVisibility(View.GONE);
								}
								if (holder.audio_layout != null) {
									if (holder.audio_layout.getVisibility() == View.VISIBLE) {
										holder.audio_layout.removeAllViews();
										holder.audio_layout
												.setVisibility(View.GONE);
									}
								}
								int resultCode = holder.bean.getResultCode();
								// if (resultCode == Activity.RESULT_CANCELED) {
								// holder.value_img.setVisibility(View.GONE);
								// } else {
								holder.value_img.setVisibility(View.VISIBLE);
								// }

								holder.value_img.setPadding(30, 10, 30, 0);
								holder.value_img.setImageDrawable(context
										.getResources().getDrawable(
												R.drawable.v_play));
								String getComponentPath = holder.bean
										.getMessage();
								final ImageView im = holder.value_img;
								im.setTag(getComponentPath);
								im.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										// String path = (String) v.getTag();
										String path = holder.bean.getMessage();
										final File vfileCheck = new File(path);
										Log.d("clone", "File path--->" + path);
										Log.d("clone", "file exists--->"
												+ vfileCheck.exists());

										if (vfileCheck.exists()) {
											Intent intentVPlayer = new Intent(
													context, VideoPlayer.class);
											// intentVPlayer.putExtra("File_Path",
											// path);
											// intentVPlayer.putExtra("Player_Type",
											// "Video Player");
											intentVPlayer.putExtra("video",
													path);
											startActivity(intentVPlayer);
										} else {

											Toast.makeText(context,
													"File Downloading", 1)
													.show();

										}

									}
								});
							}
						} else if (holder.bean.getMessagetype()
								.equalsIgnoreCase("gallery")) {

							Log.i("avatar_i", "gallery");
							String getComponentPath = holder.bean.getMessage();
							Log.i("avatar_i", "getComponentPath"
									+ getComponentPath);

							File file = new File(getComponentPath);
							if (file.exists()) {
								Log.i("avatar_i", "file.exists()");

								imageLoader.DisplayImage(
										holder.bean.getMessage(),
										holder.value_img, R.drawable.refresh);

								holder.select_buddies.setVisibility(View.GONE);
								holder.ed_gsm.setVisibility(View.GONE);
								holder.value_img.setVisibility(View.VISIBLE);
								holder.value_img.setPadding(10, 10, 10, 10);
								if (holder.message.getVisibility() == View.VISIBLE) {
									holder.message.setVisibility(View.GONE);
								}

								if (holder.audio_layout != null) {
									if (holder.audio_layout.getVisibility() == View.VISIBLE) {
										holder.audio_layout.removeAllViews();
										holder.audio_layout
												.setVisibility(View.GONE);
									}
								}

							}
							// notifyDataSetChanged();
						} else if (holder.bean.getMessagetype()
								.equalsIgnoreCase("audio")) {

							Log.i("avatar_i", "audio");
							String getComponentPath = holder.bean.getMessage();
							Log.i("avatar_i", "getComponentPath"
									+ getComponentPath);

							File file = new File(getComponentPath);
							if (file.exists()) {
								if (holder.message.getVisibility() == View.VISIBLE) {
									holder.message.setVisibility(View.GONE);
								}
								holder.value_img.setVisibility(View.VISIBLE);
								holder.value_img.setPadding(30, 10, 30, 0);
								holder.value_img.setImageDrawable(context
										.getResources().getDrawable(
												R.drawable.v_play));
								holder.value_img.setTag(position);
								holder.value_img
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View arg0) {
												// TODO Auto-generated method
												// stub
												int position = (Integer) arg0
														.getTag();
												if (holder.bean.getMessage() != null
														&& holder.bean
																.getMessage()
																.length() > 0) {
													Intent intent = new Intent(
															context,
															MultimediaUtils.class);
													intent.putExtra(
															"filePath",
															holder.bean
																	.getMessage());
													intent.putExtra("isAudio",
															true);
													intent.putExtra("action",
															"audio");
													intent.putExtra(
															"requestCode",
															position);
													intent.putExtra(
															"createOrOpen",
															"open");
													startActivity(intent);
												}
											}
										});

							}

						} else if (holder.bean.getMessagetype()
								.equalsIgnoreCase("call forward to gsm")) {
							if (holder.bean.getMessage() != null) {
								String gsmNum = Utils
										.removeFullPath(holder.bean
												.getMessage());
								holder.ed_gsm.setText(gsmNum);
							}
							holder.value_img.setVisibility(View.GONE);
							holder.audio_layout.setVisibility(View.GONE);
							holder.message.setVisibility(View.GONE);
							holder.select_buddies.setVisibility(View.GONE);
							holder.ed_gsm.setVisibility(View.VISIBLE);
						} else if (holder.bean.getMessagetype().equals(
								"Conference Call")
								|| holder.bean.getMessagetype().equals(
										"Call Forwarding")) {
							Log.i("avatar_i", "Conference Call"
									+ "Call Forwarding");
							if (holder.bean.getMessage() != null) {
								String callBuddy = Utils
										.removeFullPath(holder.bean
												.getMessage());
								holder.select_buddies.setText(callBuddy);

							}
							// holder.ed_gsm.setText("");
							holder.value_img.setVisibility(View.GONE);
							holder.audio_layout.setVisibility(View.GONE);
							holder.message.setVisibility(View.GONE);
							holder.select_buddies.setVisibility(View.VISIBLE);
							holder.ed_gsm.setVisibility(View.GONE);

						}

					}
					holder.save_button = (Button) convertView
							.findViewById(R.id.savebtn);
					holder.save_button.setTag(position);
					if (holder.bean.getSaveButtonTitle() != null) {
						holder.save_button.setText(holder.bean
								.getSaveButtonTitle());
					}
					if (holder.bean.getSaveButtonTitle() != null
							&& holder.bean.getSaveButtonTitle().length() > 0) {
						holder.save_button.setText(holder.bean
								.getSaveButtonTitle());
					}

					holder.delete_button = (Button) convertView
							.findViewById(R.id.deletebtn);
					holder.delete_button.setTag(position);

					holder.replybutton = (Button) convertView
							.findViewById(R.id.replybtn);
					holder.replybutton.setTag(position);
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				holder = (ViewHolder) convertView.getTag();
				Log.i("avatar_i", "convertView != null");
			}

			int touser_title_position = (Integer) holder.touser_title.getTag();
			holder.touser_title.setId(touser_title_position);

			int touser_position = (Integer) holder.touser.getTag();
			holder.touser.setId(touser_position);

			int title_position = (Integer) holder.title.getTag();
			holder.title.setId(title_position);

			// int input_to_position = (Integer) holder.input_to.getTag();
			// holder.input_to.setId(input_to_position);

			int message_position = (Integer) holder.message.getTag();
			holder.message.setId(message_position);

			int message_val_position = (Integer) holder.message_val.getTag();
			holder.message_val.setId(message_val_position);

			int select_buddy_position = (Integer) holder.select_buddies
					.getTag();
			holder.select_buddies.setId(select_buddy_position);

			// int value_img_position = (Integer) holder.value_img.getTag();
			// holder.value_img.setId(value_img_position);

			int url_position = (Integer) holder.url.getTag();
			holder.url.setId(url_position);

			// int reply_back_option_position = (Integer)
			// holder.reply_back_option
			// .getTag();
			// holder.reply_back_option.setId(reply_back_option_position);

			int reply_back_tv_result_position = (Integer) holder.reply_back_tv_result
					.getTag();
			holder.reply_back_tv_result.setId(reply_back_tv_result_position);

			int reply_back_tv_position = (Integer) holder.reply_back_tv
					.getTag();
			holder.reply_back_tv.setId(reply_back_tv_position);

			int when_position = (Integer) holder.when.getTag();
			holder.when.setId(when_position);

			int tv_whenselected_position = (Integer) holder.tv_whenselected
					.getTag();
			holder.tv_whenselected.setId(tv_whenselected_position);

			int save_position = (Integer) holder.save_button.getTag();
			holder.save_button.setId(save_position);

			int reply_button_position = (Integer) holder.replybutton.getTag();
			holder.replybutton.setId(reply_button_position);

			int ed_gsm_position = (Integer) holder.ed_gsm.getTag();
			holder.ed_gsm.setId(ed_gsm_position);

			TextWatcher textWatcher = new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					try {
						// TODO Auto-generated method stub
						// OfflineRequestConfigBean offReqConfig =
						// (OfflineRequestConfigBean) holder.bean
						// .clone();

						final int position1 = holder.title.getId();
						final EditText title = (EditText) holder.title;

						// OfflineRequestConfigBean offReqConfig;
						if (title.getText().toString().length() > 0) {
							holder.bean.setMessageTitle(title.getText()
									.toString());
							list.set(position1, holder.bean);
						}
						final int position2 = holder.url.getId();
						final EditText url = (EditText) holder.url;
						if (url.getText().toString().length() > 0) {
							holder.bean.setUrl(url.getText().toString());
							list.set(position2, holder.bean);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
				}
			};
			holder.title.addTextChangedListener(textWatcher);
			holder.url.addTextChangedListener(textWatcher);

			holder.touser_title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.touser.performClick();
				}
			});
			holder.touser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					// position = tag;
					showToUserOption(tag);

				}

				private void showToUserOption(final int tag) {
					try {
						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
								context);
						alert_builder.setTitle(SingleInstance.mainContext
								.getResources()
								.getString(R.string.select_buddy));

						ArrayList<String> buddy = new ArrayList<String>();
						buddy.add(SingleInstance.mainContext.getResources()
								.getString(R.string.all_users));
						for (BuddyInformationBean bib : ContactsFragment
								.getBuddyList()) {
							if (!bib.isTitle()) {
								if (!bib.getStatus()
										.equalsIgnoreCase("Pending")
										&& !bib.getStatus().equalsIgnoreCase(
												"new")) {
									if (!bib.getName().equalsIgnoreCase(
											CallDispatcher.LoginUser)) {
										buddy.add(bib.getName());
									}
								}
							}
						}

						// Log.e("123", "%%%%%%%%%%% came to getmyBuddies");
						final String buddies[] = buddy.toArray(new String[buddy
								.size()]);
						// final String[] buddies = callDisp.getmyBuddys();
						alert_builder.setSingleChoiceItems(buddies, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int pos) {
										holder.touser.setText(buddies[pos]);
										holder.bean.setBuddyId(buddies[pos]);

										path_map.put(tag, buddies[pos]);
										msg_dialog.cancel();
										notifyDataSetChanged();

									}
								});
						msg_dialog = alert_builder.create();
						msg_dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			holder.reply_back_tv_result
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// holder.reply_back_option.performClick();
							int tag = (Integer) v.getTag();
							// position = tag;
							showReplyBackOption(tag);

						}

						private void showReplyBackOption(final int tag) {

							try {

								AlertDialog.Builder alert_builder = new AlertDialog.Builder(
										context);
								alert_builder
										.setTitle(SingleInstance.mainContext
												.getResources().getString(
														R.string.buddy_reply));
								final String[] reply = {
										SingleInstance.mainContext
												.getResources().getString(
														R.string.text_avatar),
										SingleInstance.mainContext
												.getResources().getString(
														R.string.photo_avatar),
										SingleInstance.mainContext
												.getResources().getString(
														R.string.audio_avatar),
										SingleInstance.mainContext
												.getResources().getString(
														R.string.video_avatar) };
								alert_builder.setSingleChoiceItems(reply, 0,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int pos) {
												//holder.reply_back_tv_result
												//		.setText(reply[pos]);
												//holder.bean
												//		.setResponseType(reply[pos]);
												temp=reply[pos];												
												// path_map.remove(tag);
												// path_map.put(tag,
												// reply[pos]);
												//msg_dialog.cancel();
											//	notifyDataSetChanged();
											}
										});
								alert_builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										
										holder.reply_back_tv_result
												.setText(temp);
										
										holder.bean
												.setResponseType(temp);
										notifyDataSetChanged();
										msg_dialog.cancel();
									}
								  });
								alert_builder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										msg_dialog.cancel();
									}
								});
								msg_dialog = alert_builder.create();
								msg_dialog.show();

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
			// when
			// holder.when.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// holder.tv_whenselected.performClick();
			// }
			// });
			holder.tv_whenselected.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					// position = tag;
					showwhenOption(tag, holder.tv_whenselected.getText()
							.toString().trim());

				}

				private void showwhenOption(final int tag,
						final String selectedValue) {

					try {
						final String[] when_option = {								
								SingleInstance.mainContext.getResources()
										.getString(R.string.no_answers),
								SingleInstance.mainContext.getResources()
										.getString(R.string.unable_to_connect),
								SingleInstance.mainContext.getResources()
										.getString(R.string.call_dropped),
								SingleInstance.mainContext.getResources()
										.getString(R.string.call_disconnected),
								SingleInstance.mainContext.getResources()
										.getString(R.string.offline),
								SingleInstance.mainContext.getResources()
										.getString(R.string.call_rejected) };

						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
								context);
						alert_builder
								.setTitle(SingleInstance.mainContext
										.getResources().getString(
												R.string.select_when));
						boolean[] selections = null;

						if (selectedValue != null) {
							selections = new boolean[when_option.length];
							for (int i = 0; i < when_option.length; i++) {
								selections[i] = false;
							}
							String[] selected_options = selectedValue
									.split(",");
							for (String options : selected_options) {
								for (int i = 0; i < when_option.length; i++) {
									if (options
											.equalsIgnoreCase(when_option[i]))
										selections[i] = true;
								}

							}

						}

						alert_builder.setMultiChoiceItems(when_option,
								selections, new OnMultiChoiceClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										Log.d("clone", "---> ischeckd :"
												+ isChecked + "  item--->"
												+ when_option[which]);
									}
								});
						alert_builder.setPositiveButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.done),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										ListView list = ((AlertDialog) dialog)
												.getListView();

										StringBuilder sb = new StringBuilder();
										StringBuilder sbNo = new StringBuilder();

										for (int i = 0; i < list.getCount(); i++) {
											boolean checked = list
													.isItemChecked(i);

											if (checked) {

												if (sb.length() > 0) {
													sb.append(",");
													sbNo.append(",");
												}
												sb.append(list
														.getItemAtPosition(i));
												sbNo.append(i + 1);
											}
										}
										// for (int i = 0; i <
										// mainLayout.getChildCount(); i++) {
										//
										// LinearLayout parent_view =
										// (LinearLayout)
										// mainLayout
										// .getChildAt(i);
										// Integer p_tag = (Integer)
										// parent_view.getTag();

										// if (p_tag == tag) {
										// TextView tv_uddy =
										// (TextView)findViewById(R.id.tv_whenselected);
										if (sb.length() > 0) {
											holder.tv_whenselected.setText(sb
													.toString());
											holder.bean.setWhenreply(sb
													.toString());
											holder.bean.setWhen(sbNo.toString());
										} else {
											holder.tv_whenselected
													.setText(SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.avatar_mode));
											holder.bean.setWhenreply("All");
										}
										msg_dialog.cancel();
										// }

										// }
										notifyDataSetChanged();
									}

								});
						alert_builder.setNegativeButton("Back",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								msg_dialog.cancel();
							}
						});

						msg_dialog = alert_builder.create();
						msg_dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});

			// message
			holder.message_val.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (holder.bean.getMode().equals("edit")) {
						holder.message.performClick();
					}
				}
			});
			holder.message.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					holder.position = Integer.toString(tag);

					showMessageDialog(tag);
				}

				private void showMessageDialog(final int tag) {
					try {
						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
								context);
						alert_builder.setTitle(SingleInstance.mainContext
								.getResources().getString(
										R.string.select_message_type));
						alert_builder.setSingleChoiceItems(m_type, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int pos) {
										// TODO Auto-generated method stub
										// position = tag;
										String msg_type;
										if (pos == 0) {
											TEXT = true;
											msg_type = "text";
											// type = "text";
											// message.setText(type);
											// type_map.put(position, msg_type);
											messageMap.put(tag, "text");
											int listpos = holder.bean
													.getListPosition();
											// bean.setMessagetype("text");
											holder.bean
													.setListPosition(listpos);
											msg_dialog.cancel();
											Intent intent = new Intent(context,
													CloneTextinput.class);
											intent.putExtra("position", tag);
											startActivityForResult(intent, tag);
										} else if (pos == 1) {
											GALLERY = true;
											// position = tag;
											// msg_type = "Photo";
											// type = "photo";
											messageMap.put(tag, "");

											// message.setText(type);
											// type_map.put(position, msg_type);
											msg_dialog.cancel();
											if (Build.VERSION.SDK_INT < 19) {
												Intent intent = new Intent(
														Intent.ACTION_GET_CONTENT);
												intent.setType("image/*");
												startActivityForResult(intent,
														tag);
												// 2
											} else {
												KITKAT = true;
												Intent intent = new Intent(
														Intent.ACTION_OPEN_DOCUMENT);
												intent.addCategory(Intent.CATEGORY_OPENABLE);
												intent.setType("image/*");
												startActivityForResult(intent,
														tag);
												// 19
											}
										} else if (pos == 2) {

											try {
												CAMERA = true;
												messageMap.put(tag, "photo");

												// Long free_size =
												// callDisp.getExternalMemorySize();
												// if (free_size > 0 &&
												// free_size >=
												// 5120) {

												strIPath = Environment
														.getExternalStorageDirectory()
														+ "/COMMedia/MPD_"
														+ CompleteListView
																.getFileName()
														+ ".jpg";
												image_path = strIPath;
												msg_dialog.cancel();

												Intent intent = new Intent(
														context,
														CustomVideoCamera.class);
												intent.putExtra("filePath",
														strIPath);
												intent.putExtra("isPhoto", true);
												intent.putExtra("others", tag);
												startActivityForResult(intent,
														tag);

												// } else {
												// Toast.makeText(context,
												// "InSufficient Memory...",
												// Toast.LENGTH_SHORT).show();
												// }
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										} else if (pos == 3) {

											AUDIO = true;
											// position = tag;
											msg_type = "Audio";
											// type = "Audio";
											messageMap.put(tag, "audio");

											// message.setText(type);
											// type_map.put(position, msg_type);
											msg_dialog.cancel();
											strIPath = Environment
													.getExternalStorageDirectory()
													+ "/COMMedia/"
													+ "MAD_audio"
													+ CompleteListView
															.getFileName()
													+ ".mp4";

											getComponentPath = strIPath;

											Intent intent = new Intent(context,
													MultimediaUtils.class);
											intent.putExtra("filePath",
													strIPath);
											intent.putExtra("requestCode", tag);
											intent.putExtra("isAudio", true);
											intent.putExtra("action", "audio");
											intent.putExtra("createOrOpen",
													"create");
											messageMap.put(tag, strIPath);
											AUDIO = true;
											startActivityForResult(intent, tag);

										} else if (pos == 4) {
											VIDEO = true;
											// type = "video";
											// message.setText(type);
											// position = tag;
											msg_type = "Video";
											messageMap.put(tag, "video");

											// type_map.put(position, msg_type);
											msg_dialog.cancel();
											strIPath = Environment
													.getExternalStorageDirectory()
													+ "/COMMedia/"
													+ getFileName() + ".mp4";
											getComponentPath = strIPath;
											Intent intent = new Intent(context,
													CustomVideoCamera.class);
											intent.putExtra("others", tag);
											intent.putExtra("filePath",
													strIPath);
											// intent.putExtra("requestCode",
											// 25);
											// intent.putExtra("action",
											// MediaStore.ACTION_VIDEO_CAPTURE);
											// intent.putExtra("createOrOpen",
											// "create");
											startActivityForResult(intent, tag);

										} else if (pos == 5) {
											msg_dialog.cancel();
											// type_map.put(position,
											// m_type[pos]);
											messageMap.put(tag, m_type[pos]);
											setActionType(m_type[pos]);
										} else if (pos == 6) {
											msg_dialog.cancel();
											// type_map.put(position,
											// m_type[pos]);
											messageMap.put(tag, m_type[pos]);
											setActionType(m_type[pos]);
										} else if (pos == 7) {
											msg_dialog.cancel();
											// type_map.put(position,
											// m_type[pos]);
											messageMap.put(tag, m_type[pos]);
											setActionType(m_type[pos]);
										}

									}

									private void setActionType(String call) {

										try {
											if (call.equalsIgnoreCase("call forward to gsm")) {
												// holder.bean
												// .setCalltype("call forward to gsm");
												holder.bean
														.setMessagetype("call forward to gsm");
											} else if (call
													.equalsIgnoreCase("Conference Call")) {
												holder.select_buddies
														.setText(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.select_buddies));
												holder.bean
														.setCallBuddies("Select buddies");
												// holder.bean
												// .setCalltype("Conference Call");

												holder.bean
														.setMessagetype("Conference Call");
											} else if (call
													.equalsIgnoreCase("Call Forwarding")) {
												holder.select_buddies
														.setText(SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.select_buddies));
												holder.bean
														.setCallBuddies("Select buddies");
												// holder.bean
												// .setCalltype("Call Forwarding");
												holder.bean
														.setMessagetype("Call Forwarding");
											}

											// holder.bean.setMessagetype("call");
											adapter.notifyDataSetChanged();

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
						msg_dialog = alert_builder.create();
						msg_dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			holder.select_buddies.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					// position = tag;
					// Log.d("clone",
					// "-----> buddy selection"
					// + type_map.containsKey(tag));
					if (messageMap.containsKey(tag)) {
						String type = messageMap.get(tag);
						if (type != null) {
							if (type.equalsIgnoreCase("Call Forwarding"))
								showSingelSelection(tag);
							else if (type.equalsIgnoreCase("Conference Call"))
								showmultiSelection(tag);

						}
					}

				}

				private void showmultiSelection(final int tag) {

					try {
						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
								context);
						alert_builder.setTitle(SingleInstance.mainContext
								.getResources()
								.getString(R.string.select_buddy));
						final String[] mybuddies = callDisp.getmyBuddys();
						alert_builder.setMultiChoiceItems(mybuddies, null,
								new OnMultiChoiceClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										Log.d("clone", "---> ischeckd :"
												+ isChecked + "  item--->"
												+ mybuddies[which]);

									}
								});
						alert_builder.setPositiveButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										ListView list = ((AlertDialog) dialog)
												.getListView();

										StringBuilder sb = new StringBuilder();

										for (int i = 0; i < list.getCount(); i++) {
											boolean checked = list
													.isItemChecked(i);

											if (checked) {

												if (sb.length() > 0)
													sb.append(",");
												sb.append(list
														.getItemAtPosition(i));

											}
										}

										// for (int i = 0; i <
										// mainLayout.getChildCount(); i++) {
										//
										// LinearLayout parent_view =
										// (LinearLayout) mainLayout
										// .getChildAt(i);
										// Integer p_tag = (Integer)
										// parent_view.getTag();

										// if (p_tag == tag) {
										// TextView tv_uddy = (TextView)
										// parent_view
										// .findViewById(R.id.tv_buddyselection);
										holder.select_buddies.setText(sb
												.toString());
										holder.bean.setCallBuddies(sb
												.toString());
										holder.bean.setMessage(sb.toString());
										path_map.put(tag, sb.toString());
										msg_dialog.cancel();
										notifyDataSetChanged();

										// }
										// }
									}

								});

						msg_dialog = alert_builder.create();
						msg_dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				private void showSingelSelection(final int tag) {
					try {
						AlertDialog.Builder alert_builder = new AlertDialog.Builder(
								context);
						alert_builder.setTitle(SingleInstance.mainContext
								.getResources()
								.getString(R.string.select_buddy));
						final String[] buddies = callDisp.getmyBuddys();
						alert_builder.setSingleChoiceItems(buddies, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int pos) {
										// for (int i = 0; i <
										// mainLayout.getChildCount(); i++) {

										// LinearLayout parent_view =
										// (LinearLayout) mainLayout
										// .getChildAt(i);
										// Integer p_tag = (Integer)
										// parent_view.getTag();

										// if (p_tag == tag) {

										// TextView tv_uddy = (TextView)
										// parent_view
										// .findViewById(R.id.tv_buddyselection);
										holder.select_buddies
												.setText(buddies[pos]);
										holder.bean
												.setCallBuddies(buddies[pos]);
										holder.bean.setMessage(buddies[pos]);
										path_map.put(tag, buddies[pos]);
										msg_dialog.cancel();
										notifyDataSetChanged();
										// }
										// }

									}
								});
						msg_dialog = alert_builder.create();
						msg_dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			holder.save_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// SaveConfig((Integer) v.getTag(), "edit");
					// if (holder.reply_back_tv != null) {
					// bean.setReplyBack(holder.reply_back_option
					// .getSelectedItem().toString());
					// Log.i("avatar_i", "Reply back "
					// + holder.reply_back_option.getSelectedItem()
					// .toString());
					// }
					// if (holder.tv_whenselected != null) {
					// bean.setWhen(holder.tv_whenselected.getText()
					// .toString());
					// Log.i("avatar_i", "When "
					// + holder.tv_whenselected.getText().toString());
					// }
					// if (holder.message != null) {
					// bean.setMessagetype(holder.message.getText().toString());
					// Log.i("avatar_i", "When "
					// + holder.message.getText().toString());
					// }
					// if (!holder.bean.getListPosition().equals(0)
					// && holder.bean.isAddNewList() == false) {
					//
					// int pos = holder.bean.getListPosition();
					// AvatarActivity.staticlist.set(pos, holder.bean);
					//
					// } else if (!holder.bean.getListPosition().equals(0)
					// && holder.bean.isAddNewList() == true) {
					//
					// holder.bean.setAddNewList(false);
					// holder.bean.setListPosition(AvatarActivity.staticlist
					// .size());
					// Log.i("log",
					// "adapterSide"
					// + AvatarActivity.staticlist.size());
					// AvatarActivity.staticlist.add(holder.bean);
					//
					// } else if (holder.bean.getListPosition().equals(0)
					// && holder.bean.isAddNewList() == false) {
					//
					// int pos = holder.bean.getListPosition();
					// if (holder.bean != null) {
					// AvatarActivity.staticlist.set(pos, holder.bean);
					// }
					//
					// } else if (holder.bean.getListPosition().equals(0)
					// && holder.bean.isAddNewList() == true) {
					//
					// AvatarActivity.staticlist.add(holder.bean);
					// holder.bean.setAddNewList(false);
					// }

					saveConfig(position, holder.bean.getMode());
				}

				private void saveConfig(int tag, String mode) {
					// TODO Auto-generated method stub
					try {
						defalut_lis = new ArrayList<OfflineRequestConfigBean>();
						buddy_list = new ArrayList<OfflineRequestConfigBean>();

						if (holder.bean.getMessageTitle() == null
								|| holder.bean.getMessageTitle().toString()
										.length() == 0) {
							Toast.makeText(
									context,
									SingleInstance.mainContext.getResources()
											.getString(R.string.enter_subject),
									Toast.LENGTH_LONG).show();
							return;
						}
						if (holder.bean.getMessage() == null
								|| holder.bean.getMessage().length() == 0) {
							Toast.makeText(context, SingleInstance.mainContext.getResources().getString(R.string.select_message),
									Toast.LENGTH_LONG).show();
							return;
						}
						if (holder.bean.getMessagetype().equalsIgnoreCase(
								"call forward to gsm")) {

							if (!isValidPhoneNumber(holder.ed_gsm.getText()
									.toString().trim())) {
								// callDisp.cancelDialog();
								cancelDialog();
								showToast("Enter valid number for call forwarding");
								return;
							} else {
								holder.bean.setMessage(holder.ed_gsm.getText()
										.toString().trim());
							}
						}

						if (holder.tv_whenselected.getText().toString().trim()
								.equalsIgnoreCase("All")) {
							holder.bean.setWhen("1,2,3,4,5,6");
						}
						// else {
						// Log.d("clone", "when-----else----------");
						// }
						// if (path_map.containsKey(tag)) {
						// File fle = new File(path_map.get(tag));
						// holder.bean.setMessage(fle.getName());
						// }

						if (holder.bean.getMode().equalsIgnoreCase("new")) {
							holder.bean.setId("0");
							update = true;
							updatemessage = true;
							if (path_map.containsKey(position)) {
								old_path_map.put(position,
										path_map.get(position));
							}
							// holder.bean.setMode("edit");
							// if
							// (updateHashmap.containsKey(holder.bean.getId()))
							// {
							// updateHashmap.remove(holder.bean.getId());
							// }
							//
							// updateHashmap.put(bean.getId(), bean);
						} else if (holder.bean.getMode().equals("edit")) {

							// OfflineRequestConfigBean configBean = new
							// OfflineRequestConfigBean();
							// configBean = bean_map.get(position);
							holder.bean.setId(holder.bean.getId());
							if (holder.bean.getBuddyId().equalsIgnoreCase(
									"default")) {
								holder.bean
										.setBuddyId(SingleInstance.mainContext
												.getResources().getString(
														R.string.all_users));
							}
							OfflineRequestConfigBean offBean = new OfflineRequestConfigBean();
							offBean = updatemap.get(position);

							if (offBean == null) {
								Log.d("clone", "offlineRequestBean==null");
							} else {
								Log.d("clone", "offlineRequestBean!=null");
							}
							if (offBean.getBuddyId()
									.equalsIgnoreCase("default")) {
								offBean.setBuddyId("All Users");
							}
							// if (offBean.getBuddyId()
							// .equalsIgnoreCase(CallDispatcher.LoginUser)) {
							// offBean.setBuddyId("All Users");
							// }
							if (offBean.getWhen() == null) {
								offBean.setWhen("1,2,3,4,5,6");
							}
							String existingUrl = "";
							String newUrl = "";
							if (offBean.getUrl() != null) {
								existingUrl = offBean.getUrl();
							}
							if (holder.bean.getUrl() != null) {
								newUrl = holder.bean.getUrl();
							}
							String existingResType = "";
							String newResType = "";
							if (offBean.getResponseType() != null) {
								existingResType = offBean.getResponseType();
							}
							if (holder.bean.getResponseType() != null) {
								newResType = holder.bean.getResponseType();
							}
							Log.i("Avatar098",
									"old buddy ID " + offBean.getBuddyId()
											+ "new buddy ID"
											+ holder.bean.getBuddyId());
							Log.i("Avatar098",
									"old message title "
											+ offBean.getMessageTitle()
											+ "new message title"
											+ holder.bean.getMessageTitle());
							Log.i("Avatar098",
									"old type " + offBean.getMessagetype()
											+ "new type"
											+ holder.bean.getMessagetype());
							Log.i("Avatar098",
									"old message " + offBean.getMessage()
											+ "new message"
											+ holder.bean.getMessage());
							Log.i("Avatar098",
									"old res type " + offBean.getResponseType()
											+ "new res type"
											+ holder.bean.getResponseType());
							Log.i("Avatar098", "old when " + offBean.getWhen()
									+ "new when" + holder.bean.getWhen());
							Log.i("Avatar098", "old URL " + offBean.getUrl()
									+ "new URL" + holder.bean.getUrl());

							if (!offBean.getBuddyId().equalsIgnoreCase(
									holder.bean.getBuddyId())
									|| !offBean.getMessageTitle()
											.equalsIgnoreCase(
													holder.bean
															.getMessageTitle())
									|| !offBean.getMessagetype()
											.equalsIgnoreCase(
													holder.bean
															.getMessagetype())
									|| !offBean.getMessage().equalsIgnoreCase(
											holder.bean.getMessage())
									|| !existingResType
											.equalsIgnoreCase(newResType)
									|| !offBean.getWhen().equalsIgnoreCase(
											holder.bean.getWhen())
									|| !existingUrl.equalsIgnoreCase(newUrl)) {

								update = true;
								updatemessage = false;
								if (path_map.containsKey(position)) {
									path_map.remove(position);
								}
								OfflineRequestConfigBean configBean = new OfflineRequestConfigBean();
								configBean = holder.bean.clone();
								path_map.put(position, configBean.getMessage());
								updatemap.remove(position);
								updatemap.put(position, configBean);
							} else {
								update = false;
								Toast.makeText(
										getActivity(),
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.no_updates_available),
										Toast.LENGTH_LONG).show();
								cancelDialog();
							}
						}
						send_bean = holder.bean;
						offlineRequestConfigBean = holder.bean.clone();
						uploadConfigBean = holder.bean.clone();
						offlineRequestConfigBean.setMessage(path_map.get(tag));
						if (WebServiceReferences.running) {
							if (update) {
								if (holder.bean.getMode().equals("edit")) {
									holder.bean.setMessage(Utils
											.removeFullPath(holder.bean
													.getMessage()));
								}
								OfflineRequestConfigBean configurationBean = new OfflineRequestConfigBean();
								configurationBean = holder.bean;
								if (configurationBean
										.getBuddyId()
										.equalsIgnoreCase(
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.all_users))) {
									configurationBean
											.setBuddyId(CallDispatcher.LoginUser);
									defalut_lis.add(configurationBean);
									configurationBean
											.setDefalut_list(defalut_lis);

								} else {
									buddy_list.add(configurationBean);
									configurationBean.setBuddy_list(buddy_list);
								}
								if (updatemessage) {
									if (configurationBean.getMessagetype()
											.equalsIgnoreCase("text")
											|| configurationBean
													.getMessagetype()
													.equalsIgnoreCase("audio")
											|| configurationBean
													.getMessagetype()
													.equalsIgnoreCase("photo")
											|| configurationBean
													.getMessagetype()
													.equalsIgnoreCase("video")) {
										configurationBean.setMessage("");

									}
								}
								viewPosition = position;
								showprogress();
								update = false;
								Log.i("Avatar098",
										"mode" + holder.bean.getMode());
								WebServiceReferences.webServiceClient
										.offlineconfiguration(
												CallDispatcher.LoginUser,
												defalut_lis, buddy_list,
												avatarActivity);

								// Toast.makeText(context,
								// "Data's Saved Successfully",
								// Toast.LENGTH_SHORT).show();
							} else {
								showToast(SingleInstance.mainContext
										.getResources().getString(
												R.string.no_updates_available));
							}
						}
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				private boolean isValidPhoneNumber(String trim) {
					// TODO Auto-generated method stub
					Matcher m = VALID_PHONE_NUMBER.matcher(trim);

					return m.matches();
				}

			});

			holder.delete_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					DeleteConfig(position);

					Integer tag = (Integer) v.getTag();
					// if (holder.bean.getListPosition() == tag) {
					// list.remove(tag);
					// AvatarActivity.staticlist.remove(tag);
					// notifyDataSetChanged();
					// // Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT)
					// // .show();
					//
					// }
				}

				private void DeleteConfig(int position) {
					try {
						// OfflineRequestConfigBean bean =
						// (OfflineRequestConfigBean) bean_map
						// .get(position);
						String id = null;
						if (holder.bean != null)
							id = holder.bean.getId();

						if (id != null) {
							showAlert(
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.are_you_sure_you_want_to_delete),
									position);
						} else {
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.new_avatar_deleted),
									Toast.LENGTH_SHORT).show();
							list.remove(position);
							Log.i("Avatar098", "id null");
							notifyDataSetChanged();
						}
						// else {
						// removeViewAt(position);
						// break;
						// }
						// if (path_map.containsKey(position)) {
						//
						// File fle = new File(path_map.get(position));
						// if (fle.exists())
						// fle.delete();
						// }
						bean_map.remove(position);
						type_map.remove(position);
						path_map.remove(position);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				private void showAlert(String message, final int tag) {
					try {
						AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
								getActivity());
						myAlertDialog.setTitle(SingleInstance.mainContext
								.getResources().getString(
										R.string.delete_avatar));
						myAlertDialog.setMessage(message);
						myAlertDialog.setPositiveButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.ok),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
											int arg1) {
										showprogress();
										deleteConfiguration("delete", tag);
									}

									private void deleteConfiguration(
											String mode, int tag) {
										try {

											// if (type_map.containsKey(tag))
											// holder.bean
											// .setMessagetype(type_map
											// .get(tag));
											// else
											// Log.d("NOTES",
											// "Position not availble for message type");

											if (path_map.containsKey(tag)) {
												File fle = new File(path_map
														.get(tag));
												holder.bean.setMessage(fle
														.getName());
											}

											holder.bean.setMode(mode);
											// if (bean_map.containsKey(tag))
											// bean.setId(bean_map.get(tag)
											// .getId());
											send_bean = holder.bean;

											OfflineRequestConfigBean configBean = new OfflineRequestConfigBean();
											configBean = holder.bean;

											ArrayList<OfflineRequestConfigBean> defalut_lis = new ArrayList<OfflineRequestConfigBean>();
											ArrayList<OfflineRequestConfigBean> buddy_list = new ArrayList<OfflineRequestConfigBean>();

											if (WebServiceReferences.running) {
												// username_list = username_list
												// + ","
												// + bean.getBuddyId();
												if (configBean.getBuddyId()
														.equalsIgnoreCase(
																"All Users")
														|| configBean
																.getBuddyId()
																.equals("default")) {
													configBean
															.setBuddyId(CallDispatcher.LoginUser);
													defalut_lis.add(configBean);
												} else {
													buddy_list.add(configBean);
												}
												Log.d("NOTES",
														"Defaukt list-->"
																+ defalut_lis
																		.size());
												Log.d("NOTES", "Buddy list--->"
														+ buddy_list.size());
												viewPosition = position;

												WebServiceReferences.webServiceClient
														.offlineconfiguration(
																CallDispatcher.LoginUser,
																defalut_lis,
																buddy_list,
																avatarActivity);
											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});
						myAlertDialog.setNegativeButton(
								SingleInstance.mainContext.getResources()
										.getString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int arg1) {
										// do something when the Cancel button
										// is clicked

										dialog.cancel();
									}
								});
						myAlertDialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			holder.replybutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// if (bean_map.containsKey(0)) {
					// String config_id = bean_map.get(0).getId();
					String config_id = holder.bean.getId();
					if (DBAccess
							.getdbHeler(context)
							.getOfflineCallResponseDetails(
									"where configid=" + "\"" + config_id + "\"") != null
							&& DBAccess
									.getdbHeler(context)
									.getOfflineCallResponseDetails(
											"where configid=" + "\""
													+ config_id + "\"").size() > 0) {
						settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						settings.setTextColor(getResources().getColor(
								R.color.title));
						pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
						pending.setTextColor(getResources().getColor(
								R.color.title));
						response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
						response.setTextColor(getResources().getColor(
								R.color.white));
						allresponsecontainer.setVisibility(View.VISIBLE);
						allresponseview_container.setVisibility(View.VISIBLE);
						pending_container.setVisibility(View.GONE);
						// settingscontainer.setVisibility(View.GONE);
						listView.setVisibility(View.GONE);
						Log.i("clone", "=====>Inside1 pending");
						allresponse_list.clear();
						allresponseview_container.removeAllViews();
						allresponse_list.addAll(DBAccess.getdbHeler(context)
								.getOfflineCallResponseDetails(
										"where configid=" + "\"" + config_id
												+ "\""));
						Log.d("clone",
								"Respose count--->" + allresponse_list.size());
						for (OfflineRequestConfigBean offlineRequestConfigBean : allresponse_list) {
							responsebean_map.put(
									allresponseview_container.getChildCount(),
									offlineRequestConfigBean);
							inflateAllresponseUI(
									allresponseview_container.getChildCount(),
									offlineRequestConfigBean);
						}

					} else {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.no_response_to_view));
					}
					// }
				}
			});
			// holder.response.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			//
			// // String config_id = bean_map.get(0).getId();
			// String id=holder.bean.getId();
			// if (callDisp.getdbHeler(context)
			// .getOfflineCallResponseDetails(
			// "where configid=" + "\"" + id
			// + "\"") != null
			// && callDisp
			// .getdbHeler(context)
			// .getOfflineCallResponseDetails(
			// "where configid=" + "\""
			// + id + "\"")
			// .size() > 0) {
			// //
			// settings.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
			// // settings.setTextColor(getResources().getColor(
			// // R.color.title));
			// //
			// pending.setBackgroundResource(R.drawable.rounded_bordercolor_clone2);
			// // pending.setTextColor(getResources().getColor(
			// // R.color.title));
			// //
			// response.setBackgroundResource(R.drawable.rounded_bordercolor_clone);
			// // response.setTextColor(getResources().getColor(
			// // R.color.white));
			// allresponsecontainer.setVisibility(View.VISIBLE);
			// allresponseview_container
			// .setVisibility(View.VISIBLE);
			// pending_container.setVisibility(View.GONE);
			// settingscontainer.setVisibility(View.GONE);
			// Log.i("clone", "=====>Inside1 pending");
			// allresponse_list.clear();
			// allresponseview_container.removeAllViews();
			// allresponse_list.addAll(callDisp
			// .getdbHeler(context)
			// .getOfflineCallResponseDetails(
			// "where configid=" + "\""
			// + id + "\""));
			// Log.d("clone", "Respose count--->"
			// + allresponse_list.size());
			// for (OfflineRequestConfigBean offlineRequestConfigBean :
			// allresponse_list) {
			// responsebean_map.put(allresponseview_container
			// .getChildCount(),
			// offlineRequestConfigBean);
			// inflateAllresponseUI(allresponseview_container
			// .getChildCount(),
			// offlineRequestConfigBean);
			// }
			//
			// } else {
			// showToast("Sorry no response to view");
			// }
			//
			// }
			// });

			return convertView;
		}

		public class ViewHolder {
			LinearLayout linearLayout;
			OfflineRequestConfigBean bean;
			LayoutInflater inflater;
			EditText title, url;
			Button save_button, delete_button, replybutton;
			// Spinner reply_back_option, input_to;
			TextView touser, touser_title, reply_back_tv, reply_back_tv_result,
					when, tv_whenselected, message, message_val;
			// private ArrayAdapter<String> dataAdapter = null;
			// private AlertDialog msg_dialog = null;
			// private final ArrayList<String> reply_back_list = new
			// ArrayList<String>();
			public String getComponentPath;
			ImageView value_img;
			TextView select_buddies;
			LinearLayout audio_layout;
			EditText ed_gsm;
			String position;

		}

	}

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				// cancelDialog();
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

				} else {
					strIPath = null;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				// showprogress();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					// strIPath = Environment.getExternalStorageDirectory()
					// + "/COMMedia/" + callDisp.getFileName() + ".jpg";
					FileInputStream fin = (FileInputStream) context
							.getContentResolver().openInputStream(uri);
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

	public void notifyWebresponse(Object obj, int position) {
		Log.i("avatar_i", "came to notifywebresponse");
		try {
			if (obj instanceof OfflineConfigurationBean) {
				OfflineConfigurationBean result1 = (OfflineConfigurationBean) obj;
				if (result1.getList().size() > 0) {
					OfflineConfigResponseBean result = result1.getList().get(0);
					Log.i("avatar_i", "result" + result.getText());

					if (result.getText().endsWith("Successfully")) {
						Log.i("avatar_i", "result.endsWith(Successfully)"
								+ result.getText());
						if (iscleared) {
							if (avatarpage != null) {
								if (avatarpage.equalsIgnoreCase("settings")) {
									cancelDialog();
									for (Entry<Integer, OfflineRequestConfigBean> set : bean_map
											.entrySet()) {
										OfflineRequestConfigBean bean = set
												.getValue();
										DBAccess.getdbHeler(context)
												.deleteOfflineCallSettingDetails(
														bean.getId());
										String filepath = Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/"
												+ bean.getMessage();
										File fle = new File(filepath);
										if (fle.exists())
											fle.delete();
										type_map.remove(set.getKey());
										path_map.remove(set.getKey());
										DeleteExistingfile(set.getKey(),
												"settings_");
									}
									bean_map.clear();
									// btn_patch.setText("0");
									list.clear();
									// adapter.notifyDataSetChanged();
									handler.post(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											adapter.notifyDataSetChanged();
										}
									});
									iscleared = false;
								}

							}
						} else {
							if (send_bean.getMode().equalsIgnoreCase("new")) {

								try {
									ContentValues cv = new ContentValues();
									cv.put("Id", result.getId());
									cv.put("userid", CallDispatcher.LoginUser);
									if (send_bean.getBuddyId().equals(
											CallDispatcher.LoginUser))
										cv.put("buddyid", "default");
									else
										cv.put("buddyid",
												send_bean.getBuddyId());

									cv.put("message_title",
											send_bean.getMessageTitle());
									cv.put("message_type",
											send_bean.getMessagetype());
									cv.put("message", Utils
											.getFilePathString(send_bean
													.getMessage()));
									cv.put("when_action", send_bean.getWhen());
									if (send_bean.getResponseType() != null) {
										if (send_bean.getResponseType().trim()
												.length() != 0)
											cv.put("response_type",
													send_bean.getResponseType());
										else
											cv.put("response_type", "''");
									} else
										cv.put("response_type", "''");

									if (send_bean.getUrl() == null)
										cv.put("url", "''");
									else
										cv.put("url", send_bean.getUrl());
									if (result.getRecordTime() == null)
										cv.put("record_time", "''");
									else
										cv.put("record_time",
												result.getRecordTime());
									int row_id = DBAccess
											.getdbHeler()
											.insertOfflineCallSettingDetails(cv);

									Log.d("NOTES", "Inserted row id--->"
											+ row_id);
									showToast(result.getText());
									// Toast.makeText(context, result.getText(),
									// Toast.LENGTH_LONG).show();
									send_bean.setId(result.getId());
									bean_map.put(position, send_bean);
									if (send_bean.getMessagetype().equals(
											"call forward to gsm")
											|| send_bean.getMessagetype()
													.equals("Call Forwarding")
											|| send_bean.getMessagetype()
													.equals("Conference Call")) {
										cancelDialog();
										OfflineRequestConfigBean configBean = new OfflineRequestConfigBean();
										configBean = send_bean.clone();
										configBean
												.setMode(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.edit));
										configBean
												.setSaveButtonTitle(SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.update));
										offlineRequestConfigBean
												.setMode("edit");
										offlineRequestConfigBean
												.setSaveButtonTitle("Update");
										if (offlineRequestConfigBean
												.getBuddyId()
												.equals(CallDispatcher.LoginUser)) {
											offlineRequestConfigBean
													.setBuddyId("All Users");
										}
										if (updatemap.containsKey(position)) {
											updatemap.remove(position);
										}
										updatemap.put(position, configBean);
										Log.i("avatar098",
												"new avatar position : "
														+ position);
										Log.i("avatar098",
												"new avatar message : "
														+ configBean
																.getMessage());
										list.set(position,
												offlineRequestConfigBean);
										handler.post(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												adapter.notifyDataSetChanged();
											}
										});
									}
									// if (values.containsKey(position)) {
									// handler.post(new Runnable() {
									//
									// @Override
									// public void run() {
									// // TODO Auto-generated method
									// // stub
									// Button save = values
									// .get(position);
									// save.setText("Update");
									// }
									// });
									//
									// }
									// handler.post(new Runnable() {
									//
									// @Override
									// public void run() {
									// // TODO Auto-generated method stub
									// adapter.notifyDataSetChanged();
									// }
									// });

									// if (updateHashmap
									// .containsKey(send_bean.getId())) {
									// updateHashmap.remove(send_bean.getId());
									// }
									// updateHashmap.put(send_bean.getId(),
									// send_bean);
									update = false;
									if (send_bean.getMessagetype()
											.equalsIgnoreCase("text")
											|| send_bean.getMessagetype()
													.equalsIgnoreCase("audio")
											|| send_bean.getMessagetype()
													.equalsIgnoreCase("video")
											|| send_bean.getMessagetype()
													.equalsIgnoreCase("photo")) {
										if (offlineRequestConfigBean
												.getMessage() != null
												&& offlineRequestConfigBean
														.getMessage().length() > 0) {
											// showprogress();
											uploadofflineResponse(
													offlineRequestConfigBean
															.getMessage(),
													false, send_bean,
													"call_settings", send_bean
															.getMode());
										}
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else if (send_bean.getMode().equalsIgnoreCase(
									"edit")) {
								cancelDialog();

								ContentValues cv = new ContentValues();
								cv.put("userid", CallDispatcher.LoginUser);
								if (send_bean.getBuddyId().equals(
										CallDispatcher.LoginUser))
									cv.put("buddyid", "default");
								else
									cv.put("buddyid", send_bean.getBuddyId());

								cv.put("message_title",
										send_bean.getMessageTitle());
								cv.put("message_type",
										send_bean.getMessagetype());
								cv.put("message", Utils
										.getFilePathString(send_bean
												.getMessage()));
								cv.put("when_action", send_bean.getWhen());
								if (send_bean.getResponseType() != null) {
									if (send_bean.getResponseType().trim()
											.length() != 0)
										cv.put("response_type",
												send_bean.getResponseType());
									else
										cv.put("response_type", "''");
								} else
									cv.put("response_type", "''");

								if (send_bean.getUrl() == null)
									cv.put("url", "''");
								else
									cv.put("url", send_bean.getUrl());

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

								send_bean.setId(result.getId());
								bean_map.put(position, send_bean);
								String oldMessage = old_path_map.get(position);
								Log.i("Avatar098",
										"oldMessage in webservice response"
												+ old_path_map.get(position));
								Log.i("Avatar098",
										"newMessage in webservice response"
												+ offlineRequestConfigBean
														.getMessage());

								if (!offlineRequestConfigBean.getMessage()
										.equalsIgnoreCase(oldMessage)) {
									if (old_path_map.containsKey(position)) {
										old_path_map.remove(position);
									}
									old_path_map.put(position,
											offlineRequestConfigBean
													.getMessage());
									uploadofflineResponse(
											offlineRequestConfigBean
													.getMessage(),
											false, send_bean, "call_settings",
											send_bean.getMode());
									DeleteExistingfile(position, "settings_");

								}
								String savedid = result.getId();
								OfflineRequestConfigBean configBean = (OfflineRequestConfigBean) adapter
										.getItem(position);
								configBean.setSaveButtonTitle("Update");
								configBean.setMessage(offlineRequestConfigBean
										.getMessage());
								offlineRequestConfigBean
										.setSaveButtonTitle("Update");
								offlineRequestConfigBean.setMode("edit");
								offlineRequestConfigBean.setId(savedid);
								if (updatemap.containsKey(position)) {
									updatemap.remove(position);
								}
								configBean = offlineRequestConfigBean.clone();
								updatemap.put(position, configBean);
								Log.i("avatar098", "edit avatar position : "
										+ position);
								Log.i("avatar098", "edit avatar message : "
										+ configBean.getMessage());
								list.set(position, offlineRequestConfigBean);
								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										adapter.notifyDataSetChanged();
									}
								});

								// if (values.containsKey(viewPosition)) {
								//
								// handler.post(new Runnable() {
								//
								// @Override
								// public void run() {
								// // TODO Auto-generated method
								// // stub
								// Button save = values
								// .get(position);
								// save.setText("Update");
								// }
								// });
								//
								// }

							} else if (send_bean.getMode().equals("delete")) {

								Log.i("avatar_i", "delete");
								// if (CallDispatcher.clearAll) {
								DBAccess.getdbHeler(context)
										.deleteOfflineCallSettingDetails(
												send_bean.getId());
								// for (int i = 0; i < mainLayout
								// .getChildCount(); i++) {
								// final int j = i;
								// LinearLayout parent_view = (LinearLayout)
								// mainLayout
								// .getChildAt(i);
								// Button btn = (Button) parent_view
								// .findViewById(R.id.deletebtn);
								// int tag = (Integer) btn.getTag();
								// Log.d("NOTES", "---->" + tag);
								// Log.d("NOTES", "---->" + i);
								// Log.d("NOTES", "received tg---->"
								// + position);
								// if (tag == position) {
								// handler.post(new Runnable() {
								//
								// @Override
								// public void run() {
								// // TODO Auto-generated
								// // method stub
								// mainLayout
								// .removeViewAt(j);
								// }
								// });
								//
								// break;
								// }
								// }
								String filepath = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/" + send_bean.getMessage();
								File fle = new File(filepath);
								if (fle.exists())
									fle.delete();
								bean_map.remove(viewPosition);
								type_map.remove(viewPosition);
								path_map.remove(viewPosition);
								showToast(result.getText());
								cancelDialog();
								list.remove(viewPosition);
								// btn_patch
								// .setText(list.size());
								// adapter.notifyDataSetChanged();

								handler.post(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										adapter.notifyDataSetChanged();
									}
								});
								// btn_patch.setText(Integer
								// .toString(mainLayout
								// .getChildCount()));
								// DeleteExistingfile(viewPosition,
								// "settings_");
								// }
							}
						}
					} else {
						showToast(result.getText());
						cancelDialog();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void showToast(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void uploadofflineResponse(String path, boolean pendingClone,
			OfflineRequestConfigBean bean, String from, String mode) {
		try {
			// TODO Auto-generated method stub
			AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			callDisp = CallDispatcher.getCallDispatcher(context);
			if (path != null) {
				if (CallDispatcher.LoginUser != null) {
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username("ftpadmin");
					ftpBean.setFtp_password("ftppassword");
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
						appMainActivity.getFtpNotifier().addTasktoExecutor(
								ftpBean);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void addRemovedFiles(int pos, String from) {
	// try {
	// if (path_map.containsKey(pos)) {
	// if (removedpath_map.containsKey(from + Integer.toString(pos))) {
	// ArrayList<String> removed_list = removedpath_map.get(from
	// + Integer.toString(pos));
	// removed_list.add(path_map.remove(pos));
	// removedpath_map.put(from + Integer.toString(pos),
	// removed_list);
	// } else {
	// ArrayList<String> removed_list = new ArrayList<String>();
	// removed_list.add(path_map.remove(pos));
	// removedpath_map.put(from + Integer.toString(pos),
	// removed_list);
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	private void DeleteExistingfile(Integer pos, String from) {
		try {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showprogress() {

		try {
			dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(true);
			dialog.setMessage("Progress ...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setProgress(0);
			dialog.setMax(100);
			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void cancelDialog() {
		try {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyUploadStatus(final OfflineRequestConfigBean bean,
			final String filename, final boolean status) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (status) {
						bean.setResponse(filename);
						if (WebServiceReferences.running) {
							bean.setBuddyId(bean.getFrom());
							bean.setUserId(CallDispatcher.LoginUser);
							send_bean = bean;
							WebServiceReferences.webServiceClient
									.ResponseForCallConfiguration(bean);
						} else {
							// callDisp.cancelDialog();
							cancelDialog();
							showToast("Web service not running");
						}
					} else {
						// callDisp.cancelDialog();
						cancelDialog();
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.file_uploading_failed));
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void NotifyresponsecallConfig(final Object obj) {
		try {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d("clone",
							"--->came to NotifyresponsecallConfig answer machine");
					// callDisp.cancelDialog();
					cancelDialog();
					if (obj instanceof String[]) {
						String[] response = (String[]) obj;
						if (response[1].trim().equalsIgnoreCase(
								"Responsed successfully")) {
							if (clone_bean.containsKey(fragPosition)) {
								deletePendingClones(fragPosition);

								showToast(response[1]);

							}
						} else
							showToast(response[1]);

					} else if (obj instanceof WebServiceBean) {
						showToast(((WebServiceBean) obj).getText());
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyUI(ArrayList<OfflineRequestConfigBean> avatarList) {
		try {
			// adapter = new SetAvatarAdapter(context, avatarList);
			// listView.setAdapter(getAvatarAdapter());
			// adapter.notifyDataSetChanged();
			// listView.setSelection(0);
			FragmentManager manager = getActivity().getSupportFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			Fragment newFragment = this;
			this.onDestroy();
			ft.remove(this);
			ft.replace(R.id.activity_main_content_fragment, newFragment);
			// container is the ViewGroup of current fragment
			ft.addToBackStack(null);
			ft.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyDownloadStatus(final String id, final String filename,
			final boolean status, final String from) {

		try {
			Log.i("Avatar098", "notifyDownloadStatus");
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
									for (int i = 0; i < allresponsecontainer
											.getChildCount(); i++) {
										LinearLayout layout = (LinearLayout) allresponseview_container
												.getChildAt(i);
										if ((Integer) layout.getTag() == tag) {
											ImageView res_image = (ImageView) layout
													.findViewById(R.id.response_value_img_from);
											TextView tv_resvalue = (TextView) layout
													.findViewById(R.id.response_value_txt_from);
											ProgressBar progressBar = (ProgressBar) view
													.findViewById(R.id.progress_customallresponse);
											Log.i("avatar_test",
													"notifyDownloadStatus");

											setallResponseMessageTypeView(
													bean.getResponseType(),
													tv_resvalue, res_image,
													progressBar,
													bean.getResponse(), tag,
													false);

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
						Log.d("Debug", "Insde offline call settings "
								+ clone_bean.size());
						for (Map.Entry<Integer, OfflineRequestConfigBean> entry : bean_map
								.entrySet()) {
							tag = entry.getKey();
							bean = entry.getValue();
							Log.d("Debug", "Insde offline call settings "
									+ bean.getId() + " -->" + id);
							if (bean.getId().equals(id)) {
								Log.d("Debug",
										"Insde offline call settings "
												+ bean.getMessagetype()
												+ " -->" + bean.getMessage()
												+ " -->" + tag);
								fragPosition = tag;
								getMessageTypeView(bean.getMessagetype(),
										bean.getMessage(), fragPosition);

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
								fragPosition = tag;
								getPendingMessageTypeView(
										bean.getMessagetype(),
										bean.getMessage(), fragPosition);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void getMessageTypeView(String messageType, final String path,
			int position1) {
		try {
			Log.d("clone", "############### position" + position1);
			LinearLayout parent_view = (LinearLayout) allresponseview_container
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

	private void playAudio(int tag) {

		try {
			Log.d("clone", "tag value---->" + tag);
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.audio_recording, allresponseview_container, false);
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
			ImageButton btn_play = (ImageButton) view
					.findViewById(R.id.play_start);
			btn_play.setTag(0);

			rec_container.setVisibility(View.GONE);
			player_container.setVisibility(View.VISIBLE);

			LinearLayout clone_settings = (LinearLayout) allresponseview_container
					.getChildAt(tag);
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
						LinearLayout parent_layot = (LinearLayout) v
								.getParent();
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
									showToast(SingleInstance.mainContext.getResources().getString(R.string.stop_existing_recording));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void populateAllresponses() {
		try {
			if (allresponsecontainer.getVisibility() == View.VISIBLE) {
				populateAllResponses();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
