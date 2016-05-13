package com.cg.avatar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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

import org.lib.model.BuddyInformationBean;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.cg.ftpprocessor.FTPBean;
import com.cg.instancemessage.IMNotifier;
import com.cg.profiles.ViewProfiles;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.process.BGProcessor;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class AnsweringMachineActivity extends Activity implements IMNotifier {
	private LinearLayout pendingclone_container;

	private Context context;

	private String strIPath;

	private Bitmap bitmap, img = null;

	private String type;

	private MediaPlayer chatplayer;

	private LinearLayout llayTimer;

	private CallDispatcher callDisp;

	private HashMap<Integer, OfflineRequestConfigBean> clone_bean = new HashMap<Integer, OfflineRequestConfigBean>();

	private HashMap<Integer, String> responsepath_map = new HashMap<Integer, String>();

	public String getComponentPath;

	private int position;

	private String buddy_name;

	private Handler handler = new Handler();

	private MediaRecorder media_recorder = null;

	private MediaPlayer media_player = null;

	private boolean isrecording = false;

	private boolean isPlaying = false;

	private ImageButton currentplay_icon = null;

	private Button btn_back = null;

	private Button btn_patch;

	private TextView tv_tile;

	private int AUDIO;

	private Button btn_im;

	private ArrayList<OfflineRequestConfigBean> avatar_list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pendingclones);

		context = this;
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

		tv_tile = (TextView) findViewById(R.id.form_heading);
		btn_im = (Button) findViewById(R.id.ans_im);
		btn_im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				callDisp.openReceivedIm(arg0, context);
			}
		});

		pendingclone_container = (LinearLayout) findViewById(R.id.pending_clonescontainer);

		btn_back = (Button) findViewById(R.id.btn_back);
		pendingclone_container.setTag(0);
		WebServiceReferences.contextTable.put("answeringmachine", context);
		WebServiceReferences.contextTable.put("IM", context);
		buddy_name = getIntent().getStringExtra("buddy");

		tv_tile.setText(buddy_name
				+ SingleInstance.mainContext.getResources().getString(
						R.string.avatar));
		btn_patch = (Button) findViewById(R.id.clone_patch);
		callDisp.cancelDialog();
		ContactsFragment contactsFragment = (ContactsFragment) SingleInstance.instanceTable
				.get("contactspage");
		if (contactsFragment != null) {
			contactsFragment.cancelDialog();
		}
		populatePendingclones();
		btn_patch.setText(Integer.toString(pendingclone_container
				.getChildCount()));

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pendingclone_container.getChildCount() > 0)
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(R.string.move_to_pending_avtaar),
							Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	public void notifyViewProfile() {
		try {
			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(buddy_name);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				ViewProfiles viewProfiles = (ViewProfiles) WebServiceReferences.contextTable
						.get("viewprofileactivity");
				if (viewProfiles == null) {
					viewProfile(buddy_name);
				} else {
					viewProfiles.initView();
				}

			} else
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_profile_assigned_for_this_user),
						Toast.LENGTH_SHORT).show();
			cancelDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String profile_buddy;

	private String profile_buddystatus;

	private void viewProfile(String buddy) {
		// ViewProfileFragment viewProfileFragment = ViewProfileFragment
		// .newInstance(context);
		// Bundle bundle = new Bundle();
		// bundle.putString("buddyname", profile_buddy);
		// viewProfileFragment.setArguments(bundle);
		// viewProfileFragment.setBuddyName(profile_buddy);
		// AppMainActivity appMainActivity = (AppMainActivity)
		// SingleInstance.contextTable
		// .get("MAIN");
		// FragmentManager fragmentManager = appMainActivity
		// .getSupportFragmentManager();
		// FragmentTransaction fragmentTransaction = fragmentManager
		// .beginTransaction();
		// fragmentTransaction.replace(R.id.activity_main_content_fragment,
		// viewProfileFragment);
		// fragmentTransaction.commit();
		Intent intent = new Intent(context, ViewProfiles.class);
		intent.putExtra("buddyname", buddy);
		startActivity(intent);

	}

	// void doViewProfile(boolean accept, String buddy, String status) {
	//
	// try {
	// profile_buddy = buddy;
	//
	// profile_buddystatus = status;
	//
	// ArrayList<String> profileList = callDisp.getdbHeler(context)
	// .getProfile(buddy);
	// Log.i("profile", "size of arrayList--->" + profileList.size());
	//
	// if (profileList.size() > 0) {
	//
	// // Intent intent = new Intent(context, ViewProfiles.class);
	// // intent.putExtra("buddyname", buddy);
	// // intent.putExtra("buddystatus", status);
	// // intent.putExtra("close", true);
	// // startActivity(intent);
	// ViewProfileFragment viewProfileFragment = ViewProfileFragment
	// .newInstance(context);
	// // Bundle bundle = new Bundle();
	// // bundle.putString("buddyname", buddy);
	// // viewProfileFragment.setArguments(bundle);
	// viewProfileFragment.setBuddyName(buddy);
	// AppMainActivity appMainActivity = (AppMainActivity)
	// SingleInstance.contextTable
	// .get("MAIN");
	// FragmentManager fragmentManager = appMainActivity
	// .getSupportFragmentManager();
	// FragmentTransaction fragmentTransaction = fragmentManager
	// .beginTransaction();
	// fragmentTransaction.replace(
	// R.id.activity_main_content_fragment,
	// viewProfileFragment);
	// fragmentTransaction.commitAllowingStateLoss();
	//
	// } else {
	// Log.i("profile", "VIEW PROFILE------>" + buddy
	// + "---->GetProfileDetails");
	// // callDisp.showprogress(CallDispatcher.pdialog, context);
	// showprogress();
	// CallDispatcher.isFromCallDisp = false;
	// String modifiedDate = callDisp.getdbHeler(context)
	// .getModifiedDate("5");
	// if (modifiedDate == null) {
	// modifiedDate = "";
	// } else if (modifiedDate.trim().length() == 0) {
	// modifiedDate = "";
	// }
	// String[] profileDetails = new String[3];
	// profileDetails[0] = buddy;
	// profileDetails[1] = "5";
	// profileDetails[2] = modifiedDate;
	// WebServiceReferences.webServiceClient
	// .getStandardProfilefieldvalues(profileDetails);
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	void doViewProfile(final String buddy) {

		try {

			ArrayList<String> profileList = DBAccess.getdbHeler().getProfile(
					buddy);
			final AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			if (profileList.size() > 0) {
				// ViewProfileFragment viewProfileFragment = ViewProfileFragment
				// .newInstance(context);
				// viewProfileFragment.setBuddyName(buddy);
				// viewProfileFragment.setFromActivity("groupchat");
				// FragmentManager fragmentManager = appMainActivity
				// .getSupportFragmentManager();
				// FragmentTransaction fragmentTransaction = fragmentManager
				// .beginTransaction();
				// fragmentTransaction.replace(
				// R.id.activity_main_content_fragment,
				// viewProfileFragment);
				// fragmentTransaction.commitAllowingStateLoss();
				viewProfile(buddy);
			} else {
				if (appMainActivity.isNetworkConnectionAvailable()) {
					showprogress();
					CallDispatcher.isFromCallDisp = false;
					String modifiedDate = DBAccess.getdbHeler()
							.getModifiedDate(
									"select max(modifieddate) from profilefieldvalues where userid='"
											+ buddy + "'");
					if (modifiedDate == null) {
						modifiedDate = "";
					} else if (modifiedDate.trim().length() == 0) {
						modifiedDate = "";
					}
					String[] profileDetails = new String[3];
					profileDetails[0] = buddy;
					profileDetails[1] = "5";
					profileDetails[2] = modifiedDate;
					// WebServiceReferences.webServiceClient
					// .getStandardProfilefieldvalues(profileDetails);
					BGProcessor.getInstance().getStandardProfilefieldvalues(
							profileDetails);

				} else
					Toast.makeText(
							context,
							SingleInstance.mainContext.getResources()
									.getString(
											R.string.kindly_check_your_network),
							Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (WebServiceReferences.contextTable.containsKey("answeringmachine"))
			WebServiceReferences.contextTable.remove("answeringmachine");
		super.onDestroy();
	}

	public void populatePendingclones() {
		try {
			// CallDispatcher.pdialog = new ProgressDialog(context);
			// callDisp.showprogress(CallDispatcher.pdialog, context);
			clone_bean.clear();
			avatar_list = (ArrayList<OfflineRequestConfigBean>) getIntent()
					.getSerializableExtra("avatarlist");
			int i = 0;
			for (Object object : avatar_list) {
				OfflineRequestConfigBean offlineRequestConfigBean = (OfflineRequestConfigBean) object;
				inflatePendingClones(i, offlineRequestConfigBean);
				clone_bean.put(i, offlineRequestConfigBean);
				i += 1;
			}

			// callDisp.cancelDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void inflatePendingClones(int tag,
			final OfflineRequestConfigBean req_bean) {
		try {
			Log.d("NOTES", "----> inflateAllresponseUI");
			View view = getLayoutInflater()
					.inflate(R.layout.custompendingclones,
							pendingclone_container, false);
			view.setTag(tag);

			TextView tv_fromuser = (TextView) view.findViewById(R.id.from_name);
			tv_fromuser.setText(req_bean.getUserId());
			TextView tv_msg = (TextView) view.findViewById(R.id.from_message);
			tv_msg.setText(req_bean.getMessageTitle());
			TextView tv_url = (TextView) view.findViewById(R.id.view_url);
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

							if (bean.getMessagetype().equalsIgnoreCase(
									"call forwarding")) {

								// BuddyInformationBean bib =
								// (BuddyInformationBean)
								// ContactsFragment.buddyList
								// .get(bean.getMessage());

								if (bib.getName().equalsIgnoreCase(
										bean.getMessage())) {
									if (bib.getStatus().startsWith("Onli")) {
										CallDispatcher.conConference
												.add(bib.getName());
										callDisp.ConMadeConference("VC",
												context);
									}

								}

								if (bib == null) {
									Toast.makeText(
											context,
											bib.getName()
													+ SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.is_on_offline),
											Toast.LENGTH_LONG).show();
								}
							} else if (bean.getMessagetype().equalsIgnoreCase(
									"conference call")) {

								String[] str = bean.getMessage().split(",");
								String offlinenames = null;
								for (int i = 0; i < str.length; i++) {

									for (BuddyInformationBean biBean : ContactsFragment
											.getBuddyList()) {
										if (biBean.getName().equals(str[i])) {
											if (biBean.getStatus().startsWith(
													"Onli")) {
												CallDispatcher.conConference
														.add(str[i]);
											} else {
												if (offlinenames == null) {
													offlinenames = str[i];
												} else {
													offlinenames = offlinenames
															+ "," + str[i];
												}
											}
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
									Intent dial = new Intent(
											Intent.ACTION_CALL, number);
									startActivity(dial);

								}
							}
						}
					}

				}
			});

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
			TextView tv_replyType = (TextView) view.findViewById(R.id.reply_by);
			tv_replyType.setTag(tag);
			TextView tv_sendreply = (TextView) view
					.findViewById(R.id.resType_from);
			tv_sendreply.setTag(tag);
			TextView tv_restxt = (TextView) view
					.findViewById(R.id.res_txt_from);
			ImageView image_resimage = (ImageView) view
					.findViewById(R.id.res_img_from);

			if (req_bean.getSendStatus() != null) {
				if (req_bean.getResponse() == null) {
					response_layout.setVisibility(View.GONE);
					btn_sendreply.setVisibility(View.GONE);
				} else if (req_bean.getResponseType().trim().length() == 0) {
					response_layout.setVisibility(View.GONE);
					btn_sendreply.setVisibility(View.GONE);
				} else if (req_bean.getSendStatus().equals("0"))
					btn_sendreply.setVisibility(View.VISIBLE);
				else
					btn_sendreply.setVisibility(View.GONE);
			} else if (req_bean.getResponseType() == null)
				btn_sendreply.setVisibility(View.GONE);
			else if (req_bean.getResponseType().trim().length() == 0)
				btn_sendreply.setVisibility(View.GONE);

			tv_viewprofile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("clone", "====>inside view profile");
					int tag = (Integer) v.getTag();
					if (clone_bean.containsKey(tag)) {

						for (BuddyInformationBean biBean : ContactsFragment
								.getBuddyList()) {
							if (!biBean.isTitle()) {
								if (biBean.getName().equalsIgnoreCase(
										req_bean.getUserId())) {
									doViewProfile(req_bean.getUserId());
									// doViewProfile(false,
									// req_bean.getUserId(),
									// biBean.getStatus());
									break;
								}
							}
						}
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
													true, position);
										} else
											showToast(SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.kindly_give_your_response));
									}
									break;
								}
							}

						}
					}
				}
			});
			delete_config.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					int tag = (Integer) v.getTag();
					deletePendingclone(tag);
					deleteFile(tag);

				}
			});

			pendingclone_container.addView(view);
			getPendingMessageTypeView(req_bean.getMessagetype(),
					req_bean.getMessage(), tag, req_bean.getId(), true);
			if (req_bean.getResponseType() != null) {
				if (!req_bean.getResponseType().equals("''")) {
					if (req_bean.getResponseType().trim().length() > 0) {
						setResponseMessageTypeView(req_bean.getResponseType(),
								tv_restxt, tv_sendreply, tv_replyType,
								image_resimage, req_bean.getResponse(), tag);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deletePendingclone(int tag) {
		if (clone_bean.containsKey(tag)) {
			Log.d("Avataar", "Delete tag ---->" + tag);
			Log.d("Avataar", "Delete clone ID ---->"
					+ clone_bean.get(tag).getId());
			callDisp.getdbHeler(context).deleteOfflinePendingClones(
					clone_bean.get(tag).getId());
		}
		deleteFile(tag);
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
			if (!callDisp.getdbHeler(context).isfileisinuse(bean.getMessage())) {
				String path = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + bean.getMessage();
				File fle = new File(path);
				if (fle.exists())
					fle.delete();
			}
			if (bean.getResponse() != null) {
				if (!callDisp.getdbHeler(context).isfileisinuse(
						bean.getResponse())) {
					String rpath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + bean.getResponse();
					File rfle = new File(rpath);
					if (rfle.exists())
						rfle.delete();
				}
			}
		}
	}

	public void notifyDownloadStatus(final String id, final String filename,
			final boolean status) {

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int tag;
				OfflineRequestConfigBean bean = null;
				for (Map.Entry<Integer, OfflineRequestConfigBean> entry : clone_bean
						.entrySet()) {
					tag = entry.getKey();
					bean = entry.getValue();
					Log.i("avatar", "Id :: " + id);
					Log.i("avatar", "bean id :: " + bean.getConfig_id());
					if (bean.getConfig_id().equals(id)) {
						position = tag;
						getPendingMessageTypeView(bean.getMessagetype(),
								bean.getMessage(), position,
								bean.getConfig_id(), false);
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
		});

	}

	public void notifyUploadStatus(final int tag, final String filename,
			final boolean status) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (status) {
					OfflineRequestConfigBean bean = clone_bean.get(tag);
					File file = new File(responsepath_map.get(tag));
					bean.setResponse(file.getName());
					bean.setBuddyId(bean.getFrom());
					if (WebServiceReferences.running) {
						String user_id = bean.getUserId();
						bean.setBuddyId(user_id);
						bean.setUserId(CallDispatcher.LoginUser);
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

	public void getPendingMessageTypeView(String messageType,
			final String path, int position1, final String id, boolean progress) {
		Log.d("clone", "############### position" + position1);
		LinearLayout parent_view = null;
		for (int i = 0; i < pendingclone_container.getChildCount(); i++) {
			parent_view = (LinearLayout) pendingclone_container.getChildAt(i);
			if ((Integer) parent_view.getTag() == position1)
				break;
		}

		ImageView value_img = (ImageView) parent_view
				.findViewById(R.id.value_img_from);
		TextView value_text = (TextView) parent_view
				.findViewById(R.id.value_txt_from);

		ProgressBar progressBar = (ProgressBar) parent_view
				.findViewById(R.id.progress);

		if (progress == true) {
			progressBar.setVisibility(View.VISIBLE);

		} else if (progress == false) {
			progressBar.setVisibility(View.GONE);

		}
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
					progressBar.setVisibility(View.GONE);
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
			}
		} else if (messageType.equalsIgnoreCase("Photo")) {

			strIPath = Environment.getExternalStorageDirectory() + "/COMMedia/"
					+ path;
			Log.d("clone", " photo path" + strIPath);
			File selected_file = new File(strIPath);
			if (selected_file.exists()) {
				progressBar.setVisibility(View.GONE);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);

				if (length <= 2) {

					if (img != null) {
						if (!img.isRecycled()) {
							img.recycle();
							// System.gc();
						}
					}
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
			} else {
				progressBar.setVisibility(View.VISIBLE);
			}
		} else if (messageType.equalsIgnoreCase("audio")) {
			if (value_text.getVisibility() == View.VISIBLE) {
				value_text.setVisibility(View.GONE);
			}

			value_img.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			value_img.setTag(0);
			value_img.setPadding(30, 10, 30, 0);
			value_img.setImageDrawable(this.getResources().getDrawable(
					R.drawable.v_play));

			Log.i("bug", "INSIDE PHOTO TRUE");

			value_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (chatplayer == null)
						chatplayer = new MediaPlayer();
					String filepath = null;
					final ImageView image_view = (ImageView) v;
					LinearLayout lay_parent = (LinearLayout) image_view
							.getParent();
					LinearLayout parent_view = (LinearLayout) lay_parent
							.getParent();
					int pos = (Integer) parent_view.getTag();
					if (clone_bean.containsKey(pos))
						filepath = Environment.getExternalStorageDirectory()
								+ "/COMMedia/"
								+ clone_bean.get(pos).getMessage();
					if (filepath != null) {
						final File vfileCheck = new File(filepath);
						if (vfileCheck.exists()) {
							try {

								if ((Integer) image_view.getTag() == 0) {
									// if (!chatplayer.isPlaying()) {
									// chatplayer.setDataSource(filepath);
									// chatplayer.setLooping(false);
									// chatplayer.prepare();
									// chatplayer.start();
									// image_view
									// .setImageDrawable(getResources()
									// .getDrawable(
									// R.drawable.v_stop));
									// image_view.setTag(1);
									// } else
									// showToast("Kindly stop Audio player");
									Intent intent = new Intent(context,
											MultimediaUtils.class);
									intent.putExtra("filePath", filepath);
									intent.putExtra("requestCode", AUDIO);
									intent.putExtra("action", "audio");
									intent.putExtra("createOrOpen", "open");
									startActivity(intent);
								}

							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						} else {
							Toast.makeText(
									getApplicationContext(),
									SingleInstance.mainContext.getResources()
											.getString(
													R.string.file_downloading),
									Toast.LENGTH_LONG).show();
						}
					}

				}
			});
			// scroll_opt.setVisibility(View.GONE);
		} else if (messageType.equalsIgnoreCase("video")) {
			if (value_text.getVisibility() == View.VISIBLE) {
				value_text.setVisibility(View.GONE);
			}
			value_img.setVisibility(View.VISIBLE);
			Log.i("bug", "INSIDE PHOTO TRUE");
			progressBar.setVisibility(View.GONE);
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

					if (callDisp.getdbHeler(context).getDownloadStatus(id) == 0) {
						showToast("File is downloading");
					} else if (callDisp.getdbHeler(context).getDownloadStatus(
							id) == 1) {
						showToast(SingleInstance.mainContext.getResources()
								.getString(R.string.file_downloading));
					} else {
						Log.d("clone", "video path-->");
						String v_path = (String) v.getTag();
						File file = new File(v_path);
						if (file.exists()) {
							if (v_path.endsWith(".mp4"))
								v_path = v_path.substring(0,
										v_path.lastIndexOf(".mp4"));
							Log.d("clone", "video path-->" + v_path);
							final File vfileCheck = new File(v_path + ".mp4");
							if (vfileCheck.exists()) {

								if (chatplayer != null) {
									if (!chatplayer.isPlaying()) {
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// v_path + ".mp4");
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video", path);
										startActivity(intentVPlayer);
									} else
										showToast("Sorry!already audio is playing");
								} else {
									Intent intentVPlayer = new Intent(context,
											VideoPlayer.class);
									// intentVPlayer.putExtra("File_Path",
									// v_path
									// + ".mp4");
									// intentVPlayer.putExtra("Player_Type",
									// "Video Player");
									intentVPlayer.putExtra("video", v_path
											+ ".mp4");
									startActivity(intentVPlayer);
								}
							} else {
								Toast.makeText(context, "File Downloading", 1)
										.show();
							}

						} else {
							showToast(SingleInstance.mainContext.getResources()
									.getString(R.string.file_downloading));
						}
					}
				}

			});
			// scroll_opt.setVisibility(View.GONE);
		} else {
			if (messageType != null) {
				if (messageType.equalsIgnoreCase("call forwarding")
						|| (messageType.equalsIgnoreCase("conference call"))
						|| messageType.equalsIgnoreCase("call forward to gsm")) {
					if (messageType.equalsIgnoreCase("call forward to gsm"))
						value_text.setText("Call to " + path);
					else
						value_text.setText(messageType + " " + path);
					value_text.setVisibility(View.VISIBLE);
					value_img.setVisibility(View.GONE);
				}
			}
		}

	}

	private void photochat() {

		final String[] items = new String[] {
				SingleInstance.mainContext.getResources().getString(
						R.string.from_gallery), SingleInstance.mainContext.getResources().getString(R.string.from_camera) };
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
				Long free_size = getAvailableExternalMemorySize();
				Log.d("IM", free_size.toString());
				if (free_size > 0 && free_size >= 5120) {
					if (llayTimer != null) {
						llayTimer.removeAllViews();
					}

					// Intent i = new Intent(context, Custom_Camara.class);
					//
					// strIPath = "/sdcard/COMMedia/" + getFileName() + ".jpg";
					// i.putExtra("Image_Name", strIPath);
					// Log.d("File Path", strIPath);
					// startActivityForResult(i, 32);
					strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + callDisp.getFileName()
							+ ".jpg";
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
					startActivityForResult(intent, 32);

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.insufficient_memory));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("clone", "=======>" + e.getMessage());
			e.printStackTrace();
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

	private void ph_au_Sender(String strMMType) {
		System.out
				.println("----------------------- ph_au_Sender-----------------------");

		if (strMMType.equals("Gallery")) {

			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 2);
			} else {
				Log.i("img", "sdk is above 19");

				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, 19);

			}

		} else if (strMMType.equals("Photo")) {

			Long free_size = getAvailableExternalMemorySize();
			Log.d("IM", free_size.toString());
			if (free_size > 0 && free_size >= 5120) {
				if (llayTimer != null) {
					llayTimer.removeAllViews();
				}

				// Intent i = new Intent(context, Custom_Camara.class);
				//
				// strIPath = "/sdcard/COMMedia/" + getFileName() + ".jpg";
				// i.putExtra("Image_Name", strIPath);
				// Log.d("File Path", strIPath);
				// startActivityForResult(i, 0);

				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/MPD_" + callDisp.getFileName() + ".jpg";
				// Intent intent = new Intent(context, MultimediaUtils.class);
				// intent.putExtra("filePath", strIPath);
				// intent.putExtra("requestCode", 0);
				// intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				// startActivity(intent);
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", strIPath);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, 0);

			} else {
				showToast(SingleInstance.mainContext.getResources().getString(
						R.string.insufficient_memory));
			}
		}
	}

	public static long getAvailableExternalMemorySize() {
		Log.d("IM", "MEmory check called");
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			Log.d("Im", path.toString());
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return (availableBlocks * blockSize) / 1024;
		} else {
			return -1;
		}
	}

	static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	private static String getFileName() {
		String strFilename;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHH_mm_ss");
		Date date = new Date();
		strFilename = dateFormat.format(date);
		return strFilename;
	}

	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);

		LinearLayout parent_view = null;
		for (int i = 0; i < pendingclone_container.getChildCount(); i++) {

			parent_view = (LinearLayout) pendingclone_container.getChildAt(i);
			if ((Integer) parent_view.getTag() == position)
				break;
		}
		ImageView value_img;
		TextView value_text;
		value_img = (ImageView) parent_view.findViewById(R.id.res_img_from);
		value_text = (TextView) parent_view.findViewById(R.id.res_txt_from);
		if (requestCode == 30) {

			if (resultCode == Activity.RESULT_CANCELED) {
				value_img = (ImageView) findViewById(R.id.res_img_from);
				value_img.setVisibility(View.GONE);

			} else {
				Uri selectedImageUri = data.getData();
				strIPath = callDisp.getRealPathFromURI(selectedImageUri);
				File selected_file = new File(strIPath);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);

				if (length <= 2) {

					img = callDisp.ResizeImage(strIPath);
					img = Bitmap.createScaledBitmap(img, 100, 75, false);

					if (img != null) {

						bitmap = callDisp.ResizeImage(strIPath);
						String path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + callDisp.getFileName()
								+ ".jpg";

						BufferedOutputStream stream;
						try {
							stream = new BufferedOutputStream(
									new FileOutputStream(new File(path)));
							bitmap.compress(CompressFormat.JPEG, 100, stream);

							getComponentPath = path;

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
						DeleteExistingfile(position);
						responsepath_map.put(position, path);

						// }

						if (parent_view != null) {
							value_img = (ImageView) parent_view
									.findViewById(R.id.res_img_from);
							value_text = (TextView) parent_view
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
				value_img = (ImageView) findViewById(R.id.res_img_from);
				value_img.setVisibility(View.GONE);

			} else {

				Uri selectedImageUri = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				getContentResolver().takePersistableUriPermission(
						selectedImageUri, takeFlags);
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + getFileName() + ".jpg";
				DeleteExistingfile(position);
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
					// img = null;

				} else {
					showToast(SingleInstance.mainContext.getResources()
							.getString(R.string.large_image));
				}

			}
		} else if (requestCode == 32) {

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
					DeleteExistingfile(position);
					responsepath_map.put(position, strIPath);

					if (parent_view != null) {
						value_img = (ImageView) parent_view
								.findViewById(R.id.res_img_from);
						value_text = (TextView) parent_view
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

				if (parent_view != null) {
					value_img = (ImageView) parent_view
							.findViewById(R.id.res_img_from);
					value_text = (TextView) parent_view
							.findViewById(R.id.res_txt_from);

					Log.i("clone", "File ====>" + getComponentPath);
					if (type.equalsIgnoreCase("text")) {
						if (value_img.getVisibility() == View.VISIBLE) {
							value_img.setVisibility(View.GONE);

						}
						DeleteExistingfile(position);
						responsepath_map.put(position, getComponentPath);
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
						value_img.setTag(getComponentPath);

						final ImageView img = value_img;
						DeleteExistingfile(position);
						responsepath_map.put(position, getComponentPath);

						Log.i("bug", "INSIDE PHOTO TRUE");
						final File vfileCheck = new File(getComponentPath);
						chatplayer = new MediaPlayer();

						value_img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (vfileCheck.exists()) {

									try {
										getComponentPath = (String) v.getTag();
										chatplayer
												.setDataSource(getComponentPath);
										chatplayer.setLooping(false);

										chatplayer.prepare();
										chatplayer.start();

										// if (bitmap != null) {
										// if (!bitmap.isRecycled()) {
										// bitmap.recycle();
										// }
										// }
										bitmap = BitmapFactory.decodeResource(
												getResources(),
												R.drawable.v_stop);
										img.setImageBitmap(bitmap);

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
														// if (bitmap != null) {
														// if (!bitmap
														// .isRecycled()) {
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

					}
					if (type.equalsIgnoreCase("video")) {
						if (resultCode == Activity.RESULT_CANCELED) {
							Log.d("Avataar", "Result canceled");

						} else {
							if (value_text.getVisibility() == View.VISIBLE) {
								value_text.setVisibility(View.GONE);
							}
							value_img.setVisibility(View.VISIBLE);
							Log.i("bug", "INSIDE PHOTO TRUE");
							final File vfileCheck;
							DeleteExistingfile(position);
							responsepath_map.put(position, getComponentPath);
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
									if (vfileCheck.exists()) {
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// path);
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video", path);
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
		}

	}

	private void uploadofflineResponse(String path, boolean pendingClone,
			int position) {
		// TODO Auto-generated method stub
		AppMainActivity appMainActivity = (AppMainActivity) SingleInstance.contextTable
				.get("MAIN");
		if (path != null) {
			if (CallDispatcher.LoginUser != null) {
				FTPBean ftpBean = new FTPBean();
				ftpBean.setServer_ip(appMainActivity.cBean.getRouter().split(
						":")[0]);
				ftpBean.setServer_port(40400);
				ftpBean.setFtp_username(callDisp.getFTPUsername());
				ftpBean.setFtp_password(callDisp.getFTPpassword());
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
				ftpBean.setReq_object(position);
				ftpBean.setRequest_from("answering machine");
				if (appMainActivity.getFtpNotifier() != null)
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
			}
		}
	}

	public void NotifyresponsecallConfig(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				callDisp.cancelDialog();
				if (obj instanceof String[]) {

					String[] response = (String[]) obj;
					if (response[1].trim().equalsIgnoreCase(
							"Responsed successfully")) {
						if (clone_bean.containsKey(position)) {
							deletePendingclone(position);
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

	public void setResponseMessageTypeView(String messageType,
			TextView value_text, TextView response, TextView replyType,
			ImageView value_image, final String path, int tag_id) {

		if (messageType.equalsIgnoreCase("Photo")) {
			response.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.click_here_to_response));
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_photo_message));
			if (path != null) {
				response.setVisibility(View.GONE);
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				Log.d("clone", " photo path" + strIPath);
				File selected_file = new File(strIPath);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);

				if (length <= 2) {
					img = null;
					img = callDisp.ResizeImage(strIPath);
					img = Bitmap.createScaledBitmap(img, 100, 75, false);

					if (img != null) {

						// bitmap = null;
						bitmap = callDisp.ResizeImage(strIPath);
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
						if (bitmap != null)
							value_image.setImageBitmap(bitmap);
						value_image.setVisibility(View.VISIBLE);
						value_image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent in = new Intent(context,
										PhotoZoomActivity.class);
								in.putExtra("Photo_path", strIPath);
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
		} else if (messageType.equalsIgnoreCase("Audio")) {
			value_text.setVisibility(View.GONE);
			// response.setVisibility(View.GONE);
			response.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.click_here_to_response));
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_audio_message));

			if (path != null) {
				if (!path.equals("''") && path.trim().length() != 0)
					playresponseAudio(tag_id);
				else {
					// recordresponseAudio(tag_id);
				}
			}
			// else
			// recordresponseAudio(tag_id);
		} else if (messageType.equalsIgnoreCase("Video")) {
			response.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.click_here_to_response));
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_video_message));
			if (value_text.getVisibility() == View.VISIBLE) {
				value_text.setVisibility(View.GONE);
			}

			if (path != null) {
				response.setVisibility(View.GONE);
				String filepath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + path;
				File fle = null;
				if (!filepath.endsWith(".mp4"))
					fle = new File(filepath + ".mp4");
				else
					fle = new File(filepath);
				Log.d("clone", "--->" + fle.exists());

				if (fle.exists())
					value_image.setVisibility(View.VISIBLE);
				else
					value_image.setVisibility(View.INVISIBLE);

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
			}

		} else if (messageType.equalsIgnoreCase("Text")) {
			response.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.click_here_to_response));
			replyType.setText(SingleInstance.mainContext.getResources().getString(R.string.reply_back_by_text_message));
			value_image.setVisibility(View.GONE);
			StringBuffer buffer = new StringBuffer();
			String filepath = Environment.getExternalStorageDirectory()
					+ "/COMMedia/" + path;

			if (path != null) {
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
				response.setVisibility(View.GONE);
				value_text.setVisibility(View.VISIBLE);
				value_text.setText(buffer.toString());
			}
		} else {

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
				// recordresponseAudio(tag);
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + getFileName() + ".mp4";
				getComponentPath = strIPath;
				Intent intent = new Intent(context, MultimediaUtils.class);
				Log.i("avatar098", "path====:" + strIPath);
				// AUDIO=true;
				intent.putExtra("filePath", strIPath);
				intent.putExtra("requestCode", 33);
				intent.putExtra("action", "audio");
				intent.putExtra("createOrOpen", "create");
				// startActivityForResult(intent, 33);
				// startActivityForResult(intent, 33);
				startActivity(intent);
			} else if (rtype.equalsIgnoreCase("video")) {
				type = "video";
				position = tag;
				msg_type = "Video";
				type = "video";
				strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/" + getFileName() + ".mp4";
				getComponentPath = strIPath;
				// Intent intent = new Intent(context, CamsampleActivity.class);
				// intent.putExtra("video_name",
				// strIPath.substring(0, strIPath.length() - 4));
				// intent.putExtra("auto", false);
				// startActivityForResult(intent, 33);

				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", strIPath);
				// intent.putExtra("requestCode", 33);
				// intent.putExtra("action", MediaStore.ACTION_VIDEO_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				startActivityForResult(intent, 33);
			}
		}

	}

	// private void recordresponseAudio(int tag) {
	// View view = getLayoutInflater().inflate(R.layout.audio_recording,
	// pendingclone_container, false);
	// LinearLayout rec_container = (LinearLayout) view
	// .findViewById(R.id.audio_rec_container);
	// rec_container.setTag(tag);
	// final ImageButton btn_startrec = (ImageButton) view
	// .findViewById(R.id.rec_start);
	// btn_startrec.setTag(0);
	// ProgressBar progress = (ProgressBar) view
	// .findViewById(R.id.progressBar_dwn);
	// progress.setVisibility(View.GONE);
	// LinearLayout player_container = (LinearLayout) view
	// .findViewById(R.id.audio_player_container);
	// player_container.setTag(tag);
	// ImageButton btn_play = (ImageButton) view.findViewById(R.id.play_start);
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
	//
	// audio_layout.removeAllViews();
	// audio_layout.addView(view);
	// audio_layout.setVisibility(View.VISIBLE);
	//
	// btn_startrec.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// // TODO Auto-generated method stub
	// int tag = (Integer) v.getTag();
	// LinearLayout parent_layot = (LinearLayout) v.getParent();
	// ProgressBar progress = (ProgressBar) parent_layot
	// .getChildAt(1);
	// if (tag == 0) {
	//
	// media_recorder = new MediaRecorder();
	//
	// int p_tag = (Integer) parent_layot.getTag();
	// ImageButton recorder_button = (ImageButton) v;
	// recorder_button.setTag(1);
	// // recorder_button
	// // .setBackgroundResource(R.drawable.stop);
	// // media_recorder
	// // .setAudioSource(MediaRecorder.AudioSource.MIC);
	// // media_recorder
	// // .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	// // media_recorder
	// // .setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
	// String audio_path = Environment
	// .getExternalStorageDirectory()
	// + "/COMMedia/"
	// + getFileName() + ".mp4";
	// responsepath_map.put(p_tag, audio_path);
	//
	// // media_recorder.setOutputFile(audio_path);
	// // media_recorder.prepare();
	// // media_recorder.start();
	// // progress.setVisibility(View.VISIBLE);
	//
	// Intent intent = new Intent(context,
	// MultimediaUtils.class);
	// intent.putExtra("filePath", audio_path);
	// intent.putExtra("requestCode", AUDIO);
	// intent.putExtra("action", "audio");
	// intent.putExtra("createOrOpen", "create");
	// startActivity(intent);
	//
	// isrecording = true;
	//
	// LinearLayout lay = (LinearLayout) parent_layot
	// .getParent();
	// LinearLayout l_child = (LinearLayout) lay.getChildAt(1);
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
	// LinearLayout parent_layot = (LinearLayout) v.getParent();
	// int p_tag = (Integer) parent_layot.getTag();
	// int view_tag = (Integer) v.getTag();
	// ImageButton btn_play = (ImageButton) v;
	// // LayoutParams lp = new
	// // LayoutParams(LayoutParams.WRAP_CONTENT,
	// // LayoutParams.WRAP_CONTENT);
	// // btn_play.setLayoutParams(lp);
	// if (view_tag == 0) {
	//
	// if (responsepath_map.containsKey(p_tag)) {
	//
	// btn_play.setTag(1);
	// String path = responsepath_map.get(p_tag);
	// Intent intent = new Intent(context,
	// MultimediaUtils.class);
	// intent.putExtra("filePath", path);
	// intent.putExtra("requestCode", AUDIO);
	// intent.putExtra("action", "audio");
	// intent.putExtra("createOrOpen", "open");
	// startActivity(intent);
	// isPlaying = false;
	// isrecording = false;
	// btn_play.setTag(0);
	//
	// } else
	// Log.d("LM", "----path not available-----");
	//
	// }
	// // else {
	// // btn_play.setBackgroundResource(R.drawable.v_play);
	// // btn_play.setTag(0);
	// // isPlaying = false;
	// //
	// // }
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
	//
	// }

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

					} else {
						btn_play.setBackgroundResource(R.drawable.v_play);
						btn_play.setTag(0);

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

	private void DeleteExistingfile(int pos) {
		String original_path = "";
		original_path = responsepath_map.get(pos);
		if (original_path != null) {
			File fle = new File(original_path);
			if (fle.exists())
				fle.delete();
			fle = null;
		}
		original_path = null;

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
        super.onResume();
        AppMainActivity.inActivity = this;
		if (WebServiceReferences.Imcollection.size() == 0)
			btn_im.setVisibility(View.GONE);
		else
			btn_im.setVisibility(View.VISIBLE);
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

				btn_im.setBackgroundResource(R.drawable.small_blue_balloon);

				if (!callDisp.getdbHeler(context).userChatting(sb.getFrom())) {
					callDisp.PutImEntry(sb.getSessionid(), sb.getFrom(),
							CallDispatcher.LoginUser, 1,
							CallDispatcher.LoginUser);
				}

			}
		});

	}

}
