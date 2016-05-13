package com.cg.utilities;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.lib.model.BuddyInformationBean;
import org.lib.model.OfflineRequestConfigBean;
import org.lib.model.PermissionBean;
import org.lib.model.SignalingBean;
import org.lib.model.UtilityBean;
import org.lib.model.UtilityResponse;
import org.lib.model.WebServiceBean;
import org.util.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.SipNotificationListener;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.files.CompleteListView;
import com.cg.files.ComponentCreator;
import com.cg.ftpprocessor.FTPBean;
import com.cg.locations.buddyLocation;
import com.cg.profiles.ViewProfiles;
import com.cg.settings.LocationPickerMapView;
import com.group.chat.GroupChatActivity;
import com.main.AppMainActivity;
import com.main.ContactsFragment;
import com.main.ViewProfileFragment;
import com.util.CustomVideoCamera;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class UtilitySeller extends Activity implements OnClickListener {

	private Typeface tf_regular = null;

	private Typeface tf_bold = null;

	private TextView tv_title, tv_addlist;

	private Context context;

	private LinearLayout parent_view;

	private ScrollView scrl_view;

	private Vector<UtilityBean> sellers_list = null;

	private HashMap<String, String> buddyDistanceMap = new HashMap<String, String>();

	private ImageView btn_back;

	private HashMap<Integer, UtilitypathBean> multimedia_info;
	private HashMap<Integer, UtilityBean> saveUpdate;

	private HashMap<String, ArrayList<String>> result_slidepath;

	private HashMap<Integer, String> location_info;

	private int FROMCAMERA = 1;

	private int FROMGALLERY = 2;

	private int FORKITKAT = 3;

	private int VIDEO = 4;

	private int LOCATION = 5;

	private int AUDIO = 6;

	private String image_path;

	private int selected_position = 0;

	private CallDispatcher callDisp;

	private HashMap<Integer, UtilityBean> utility_items;

	private UtilityBean selected_utility;

	private int isPosted = 2;

	private boolean isresultrequested = false;

	private HashMap<Integer, ArrayList<UtilityBean>> selected_results;

	private int selected_resultpos = 0;

	private String[] settings_option = {
			SingleInstance.mainContext.getResources().getString(
					R.string.block_buddy),
			SingleInstance.mainContext.getResources().getString(
					R.string.block_this_ad) };

	private String[] settings_buddyopt = {
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_call),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_call),
			SingleInstance.mainContext.getResources()
					.getString(R.string.mmchat),
			SingleInstance.mainContext.getResources().getString(
					R.string.photo_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.sketch_message) };

	private String[] common_options = {
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_conference),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_conference),
			SingleInstance.mainContext.getResources().getString(
					R.string.photo_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.audio_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.video_message),
			SingleInstance.mainContext.getResources().getString(
					R.string.sketch_message) };

	private HashMap<String, UtilityBean> resposne_items = null;

	private String latitude = null;

	private String longitude = null;

	private String location = null;

	private ArrayList<String> buddyList = null;

	private Button btn_block = null;

	private String block_buddyname;

	private Handler handler = new Handler();

	private ScrollView child_scroll;

	private SharedPreferences preferences;

	private AppMainActivity appMainActivity = null;
	private boolean update = false;
	private Button saveAllBtn = null;
	private Vector<UtilityBean> utilityList = null;
	private HashMap<String, UtilityBean> utilityMap = null;
	private HashMap<Integer, UtilityBean> selectedUtilityMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			context = this;
			multimedia_info = new HashMap<Integer, UtilitypathBean>();
			saveUpdate = new HashMap<Integer, UtilityBean>();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;
			appMainActivity = (AppMainActivity) SingleInstance.contextTable
					.get("MAIN");
			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(context);

			preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);
			displaymetrics = null;
			utility_items = new HashMap<Integer, UtilityBean>();
			location_info = new HashMap<Integer, String>();
			result_slidepath = new HashMap<String, ArrayList<String>>();
			selected_results = new HashMap<Integer, ArrayList<UtilityBean>>();
			resposne_items = new HashMap<String, UtilityBean>();
			WebServiceReferences.contextTable.put("utilityseller", context);
			sellers_list = new Vector<UtilityBean>();
			tf_regular = Typeface.createFromAsset(context.getAssets(),
					"fonts/ARIAL.TTF");
			tf_bold = Typeface.createFromAsset(context.getAssets(),
					"fonts/ARIALBD.TTF");
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.utility_title1);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.utility_title1);
			utilityList = new Vector<UtilityBean>();
			utilityMap = new HashMap<String, UtilityBean>();
			selectedUtilityMap = new HashMap<Integer, UtilityBean>();
			tv_title = (TextView) findViewById(R.id.tv_title);
			tv_title.setTypeface(tf_bold);
			tv_addlist = (TextView) findViewById(R.id.ib_addlist);
			tv_addlist.setTypeface(tf_bold);
			tv_addlist.setOnClickListener(this);
			btn_back = (ImageView) findViewById(R.id.iv_utility_back);
			btn_back.setOnClickListener(this);
			saveAllBtn = (Button) findViewById(R.id.save_button);
			saveAllBtn.setOnClickListener(this);
			buddyList = new ArrayList<String>();
			btn_block = (Button) findViewById(R.id.block_button);
			btn_block.setVisibility(View.VISIBLE);
			btn_block.setOnClickListener(this);
			setContentView(R.layout.utility);
			parent_view = (LinearLayout) findViewById(R.id.utility_container);
			scrl_view = (ScrollView) findViewById(R.id.scrl_view);
			scrl_view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (child_scroll != null)
						child_scroll.requestDisallowInterceptTouchEvent(true);

					return false;
				}
			});
			sellers_list = callDisp.getdbHeler(context).SelectUtilityRecords(
					"select * from utility where userid='"
							+ CallDispatcher.LoginUser
							+ "' and utility_name='sell'");
			if (sellers_list.size() > 0) {
				for (UtilityBean utilityBean : sellers_list) {
					saveUpdate.put(utilityBean.getId(), utilityBean);
					inflateNewView(1, utilityBean);
				}
			} else
				inflateNewView(0, null);
		} catch (Exception e) {
			Log.i("seller", "===== > " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateBuddyStatus(String username, String status) {

		LinearLayout result_container = (LinearLayout) findViewById(R.id.llay_resultcontainer);
		TextView tv_username = null;
		ImageView iv_status = null;
		RelativeLayout view = null;
		for (int i = 0; i <= result_container.getChildCount(); i++) {
			view = (RelativeLayout) result_container.getChildAt(i);
			view.setTag(i);
			if (view != null) {
				// if ((Integer) view.getTag() == fragPosition) {
				// fragPosition++;
				tv_username = (TextView) view.findViewById(R.id.tv_resusername);
				iv_status = (ImageView) view.findViewById(R.id.iv_status);
				tv_username.setTypeface(tf_regular);
				String uName = (String) tv_username.getText();

				if (uName.equalsIgnoreCase(username)) {

					if (status != null) {
						if (status.equalsIgnoreCase("1")) {
							iv_status
									.setBackgroundResource(R.drawable.n_online);
						} else if (status.equalsIgnoreCase("3")) {
							iv_status.setBackgroundResource(R.drawable.m_away);
						} else if (status.equalsIgnoreCase("4")) {
							iv_status
									.setBackgroundResource(R.drawable.m_offline);
						} else if (status.equalsIgnoreCase("2")) {
							iv_status
									.setBackgroundResource(R.drawable.m_airport);
						}

						break;

					}
					iv_status.invalidate();
					iv_status.notify();

				}

				// }
			}
		}
	}

	protected void onResume() {
		try {
			super.onResume();
            AppMainActivity.inActivity = this;
			{
				LinearLayout result_container = (LinearLayout) findViewById(R.id.llay_resultcontainer);
				;
				View view = getLayoutInflater().inflate(
						R.layout.utility_result, null, false);
				view.setTag(result_container.getChildCount());
				result_container.setFocusableInTouchMode(true);
				result_container.setFocusable(true);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inflateRsultView(LinearLayout result_container,
			final UtilityBean result_bean) {

		try {
			View view = getLayoutInflater().inflate(R.layout.utility_result,
					null, false);
			view.setTag(result_container.getChildCount());

			TextView tv_posteddate, tv_username, tv_slidecount, tv_invite, tv_quantitylbl, tv_quantity, tv_pricelbl, tv_price;
			ImageView iv_ressettings, iv_slide, iv_forward, iv_backward, iv_status, iv_viewprofile, iv_menu, iv_loc;
			CheckBox chk_selectres;
			EditText resultText;
			// ScrollView ch_scroll;
			tv_posteddate = (TextView) view.findViewById(R.id.tv_resdate);
			tv_posteddate.setTypeface(tf_regular);
			// tv_productname, tv_address, tv_email, tv_contactno, tv_city,
			// tv_state, tv_country, tv_pincode
			// ch_scroll = (ScrollView) view.findViewById(R.id.child_scroll);
			// ch_scroll.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// // TODO Auto-generated method stub
			// if (scrl_view != null) {
			// child_scroll = (ScrollView) v;
			// scrl_view.requestDisallowInterceptTouchEvent(true);
			//
			// }
			// return false;
			// }
			// });
			resultText = (EditText) view.findViewById(R.id.child_scroll);
			tv_username = (TextView) view.findViewById(R.id.tv_resusername);
			tv_username.setTypeface(tf_regular);
			tv_slidecount = (TextView) view.findViewById(R.id.tv_slidecnt);
			tv_slidecount.setTypeface(tf_bold);
			tv_slidecount.setVisibility(View.INVISIBLE);
			// tv_productname = (TextView) view
			// .findViewById(R.id.tv_resproductname);
			// tv_productname.setTypeface(tf_bold);
			// tv_address = (TextView) view.findViewById(R.id.tv_reslocation);
			// tv_address.setTypeface(tf_regular);
			// tv_email = (TextView) view.findViewById(R.id.tv_resemail);
			// tv_email.setTypeface(tf_regular);
			tv_invite = (TextView) view.findViewById(R.id.tv_ressendinvite);
			tv_invite.setTypeface(tf_bold);
			// tv_contactno = (TextView) view.findViewById(R.id.tv_resphone);
			// tv_contactno.setTypeface(tf_regular);
			// tv_city = (TextView) view.findViewById(R.id.tv_rescity);
			// tv_city.setTypeface(tf_regular);
			// tv_state = (TextView) view.findViewById(R.id.tv_resstate);
			// tv_state.setTypeface(tf_regular);
			// tv_country = (TextView) view.findViewById(R.id.tv_rescountry);
			// tv_country.setTypeface(tf_regular);
			// tv_pincode = (TextView) view.findViewById(R.id.tv_respin);
			// tv_pincode.setTypeface(tf_regular);

			tv_invite.setTag(result_container.getChildCount());
			tv_invite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout lay = (LinearLayout) v.getParent();
					RelativeLayout parent = (RelativeLayout) lay.getParent();
					RelativeLayout child_lay = (RelativeLayout) parent
							.getParent();
					LinearLayout parent_lay = (LinearLayout) child_lay
							.getParent();
					LinearLayout parent_lay1 = (LinearLayout) parent_lay
							.getParent();
					LinearLayout parent_lay2 = (LinearLayout) parent_lay1
							.getParent();
					selected_position = (Integer) parent_lay2.getTag();
					selected_resultpos = (Integer) v.getTag();
					if (resposne_items.containsKey(Integer
							.toString(selected_position)
							+ "_"
							+ Integer.toString(selected_resultpos))) {
						Log.d("utiliy", "Item exists");
						UtilityBean bean = resposne_items.get(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_resultpos));
						if (bean != null) {
							if (WebServiceReferences.running) {
								WebServiceReferences.webServiceClient
										.addPeople(CallDispatcher.LoginUser,
												bean.getUsername(), context);
								v.setVisibility(View.INVISIBLE);
							}

							for (java.util.Map.Entry<String, UtilityBean> entry_set : resposne_items
									.entrySet()) {
								String key = entry_set.getKey();
								UtilityBean utilityBean = entry_set.getValue();
								if (utilityBean != null) {
									if (utilityBean.getUsername().equals(
											bean.getUsername())) {
										String[] position = key.split("_");
										if (position[1] != null) {
											RelativeLayout child = (RelativeLayout) parent_lay.getChildAt(Integer
													.parseInt(position[1]));
											TextView tv_sendinvite = (TextView) child
													.findViewById(R.id.tv_ressendinvite);
											tv_sendinvite
													.setVisibility(View.GONE);
										}
									}
								}
							}
						}
					}
				}
			});
			tv_quantitylbl = (TextView) view.findViewById(R.id.tv_resqty);
			tv_quantitylbl.setTypeface(tf_bold);
			tv_quantity = (TextView) view.findViewById(R.id.tv_resqtyval);
			tv_quantity.setTypeface(tf_bold);
			tv_pricelbl = (TextView) view.findViewById(R.id.tv_resprice);
			tv_pricelbl.setTypeface(tf_bold);
			tv_price = (TextView) view.findViewById(R.id.tv_respriceval);
			tv_price.setTypeface(tf_bold);
			iv_status = (ImageView) view.findViewById(R.id.iv_status);

			iv_viewprofile = (ImageView) view.findViewById(R.id.iv_viewprofile);
			iv_viewprofile.setTag(result_container.getChildCount());
			iv_viewprofile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						RelativeLayout lay = (RelativeLayout) v.getParent();
						RelativeLayout parent = (RelativeLayout) lay.getParent();
						LinearLayout lay_parent = (LinearLayout) parent.getParent();
						LinearLayout lay_parent1 = (LinearLayout) lay_parent
								.getParent();
						LinearLayout lay_parent2 = (LinearLayout) lay_parent1
								.getParent();
						selected_position = (Integer) lay_parent2.getTag();
						Log.d("utility", "parent position---->" + selected_position);
						selected_resultpos = (Integer) v.getTag();
						Log.d("utility", "child position---->" + selected_resultpos);
						if (resposne_items.containsKey(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_resultpos))) {
							Log.d("utiliy", "Item exists");
							UtilityBean bean = resposne_items.get(Integer
									.toString(selected_position)
									+ "_"
									+ Integer.toString(selected_resultpos));
							Log.d("utility", "my bean object");
							if (bean != null) {

								boolean isAvailable = false;
								for (BuddyInformationBean bib : ContactsFragment
										.getBuddyList()) {
									if (!bib.isTitle()) {
										if (bib.getName().equalsIgnoreCase(
												bean.getUsername())) {
											doViewProfile(false,
													bean.getUsername(),
													bib.getStatus());
											CallDispatcher.profileRequested = true;

											isAvailable = true;
											break;
										}
									}
								}

								if (!isAvailable) {
									Log.d("utility", "came to else 123");
									doViewProfile(false, bean.getUsername(), "");
								}
								// if
								// (WebServiceReferences.buddyList.containsKey(bean
								// .getUsername())) {
								// Log.d("utility", "came to if 123");
								// BuddyInformationBean infobean =
								// WebServiceReferences.buddyList
								// .get(bean.getUsername());
								// doViewProfile(false, bean.getUsername(),
								// infobean.getStatus());
								// } else {
								// Log.d("utility", "came to else 123");
								// doViewProfile(false, bean.getUsername(), "");
								// }
							}
						}
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			});

			iv_ressettings = (ImageView) view.findViewById(R.id.iv_settingsres);
			iv_ressettings.setTag(result_container.getChildCount());
			iv_ressettings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout lay = (RelativeLayout) v.getParent();
					RelativeLayout parent = (RelativeLayout) lay.getParent();
					LinearLayout child_lay = (LinearLayout) parent.getParent();
					LinearLayout parent_lay = (LinearLayout) child_lay
							.getParent();
					LinearLayout parent_lay1 = (LinearLayout) parent_lay
							.getParent();
					selected_position = (Integer) parent_lay1.getTag();
					selected_resultpos = (Integer) v.getTag();
					if (resposne_items.containsKey(Integer
							.toString(selected_position)
							+ "_"
							+ Integer.toString(selected_resultpos))) {
						UtilityBean bean = resposne_items.get(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_resultpos));

						boolean isblocked = false;
						for (String names : callDisp.blocked_buddies) {
							if (names.equals(bean.getUsername())) {
								isblocked = true;
								break;
							}
						}
						showSettinsOption(bean, child_lay, isblocked);
					}

				}
			});

			iv_slide = (ImageView) view.findViewById(R.id.img_slide);
			iv_slide.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String path = (String) v.getTag();
					Log.d("utility", "Path is ---->" + path);
					if (path != null) {
						if (path.contains("MPD_")) {
							Intent in = new Intent(context,
									PhotoZoomActivity.class);
							in.putExtra("Photo_path", path);
							in.putExtra("type", false);
							in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(in);
						} else if (path.contains("MAD_")) {
							Intent intent = new Intent(context,
									MultimediaUtils.class);
							intent.putExtra("filePath", path);
							intent.putExtra("requestCode", AUDIO);
							intent.putExtra("action", "audio");
							intent.putExtra("createOrOpen", "open");
							startActivity(intent);

						} else if (path.contains("MVD_")) {
							Intent intentVPlayer = new Intent(context,
									VideoPlayer.class);
							// intentVPlayer.putExtra("File_Path", path);
							// intentVPlayer.putExtra("Player_Type",
							// "Video Player");
							// intentVPlayer.putExtra("time", 0);
							intentVPlayer.putExtra("video", path);

							startActivity(intentVPlayer);
						}
					}
				}
			});
			chk_selectres = (CheckBox) view.findViewById(R.id.iv_selectres);
			chk_selectres.setTag(result_container.getChildCount());
			chk_selectres
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							RelativeLayout lay = (RelativeLayout) buttonView
									.getParent();
							RelativeLayout parent = (RelativeLayout) lay
									.getParent();
							LinearLayout child_lay = (LinearLayout) parent
									.getParent();
							LinearLayout parent_lay = (LinearLayout) child_lay
									.getParent();
							LinearLayout parent_lay1 = (LinearLayout) parent_lay
									.getParent();
							selected_position = (Integer) parent_lay1.getTag();
							selected_resultpos = (Integer) parent.getTag();

							if (isChecked) {
								if (resposne_items.containsKey(Integer
										.toString(selected_position)
										+ "_"
										+ Integer.toString(selected_resultpos))) {
									UtilityBean bean = resposne_items.get(Integer
											.toString(selected_position)
											+ "_"
											+ Integer
													.toString(selected_resultpos));

									BuddyInformationBean info = null;
									for (BuddyInformationBean temp : ContactsFragment
											.getBuddyList()) {
										if (!temp.isTitle()) {
											if (temp.getName()
													.equalsIgnoreCase(
															bean.getUsername())) {
												info = temp;
												break;
											}
										}
									}

									if (info != null) {
										// BuddyInformationBean info =
										// WebServiceReferences.buddyList
										// .get(bean.getUsername());
										if (!info.getStatus().equalsIgnoreCase(
												"offline")
												&& !info.getStatus()
														.equalsIgnoreCase(
																"pending")) {
											if (selected_results
													.containsKey(selected_position)) {
												ArrayList<UtilityBean> list = selected_results
														.get(selected_position);
												list.add(bean);
											} else {
												ArrayList<UtilityBean> list = new ArrayList<UtilityBean>();
												list.add(bean);
												selected_results
														.put(selected_position,
																list);
											}
											Button btn_menu = (Button) parent_lay1
													.findViewById(R.id.common_menu);
											btn_menu.setVisibility(View.VISIBLE);

										} else {

											buttonView.setChecked(false);
											if (info.getStatus()
													.equalsIgnoreCase("offline"))
												Toast.makeText(
														context,
														bean.getUsername()
																+ SingleInstance.mainContext
																		.getResources()
																		.getString(
																				R.string.is_on_offline),
														Toast.LENGTH_SHORT)
														.show();
											else if (info
													.getStatus()
													.equalsIgnoreCase("pending"))
												Toast.makeText(
														context,
														bean.getUsername()
																+ " is Pending",
														Toast.LENGTH_SHORT)
														.show();
										}

									} else {
										buttonView.setChecked(false);
										Toast.makeText(
												context,
												bean.getUsername()
														+ SingleInstance.mainContext
																.getResources()
																.getString(
																		R.string.is_not_in_your_buddies_list),
												Toast.LENGTH_SHORT).show();
									}
								}

							} else {
								if (resposne_items.containsKey(Integer
										.toString(selected_position)
										+ "_"
										+ Integer.toString(selected_resultpos))) {
									UtilityBean bean = resposne_items.get(Integer
											.toString(selected_position)
											+ "_"
											+ Integer
													.toString(selected_resultpos));
									if (selected_results
											.containsKey(selected_position)) {
										ArrayList<UtilityBean> list = selected_results
												.get(selected_position);
										for (UtilityBean utilityBean : list) {
											if (utilityBean.getId() == bean
													.getId()) {
												list.remove(utilityBean);
												break;
											}
										}
										if (list.size() == 0) {
											selected_results
													.remove(selected_position);
											Button btn_menu = (Button) parent_lay1
													.findViewById(R.id.common_menu);
											btn_menu.setVisibility(View.GONE);
										}
									}
								}

							}

						}
					});
			iv_forward = (ImageView) view.findViewById(R.id.iv_fwd);
			iv_forward.setTag(result_container.getChildCount());
			iv_forward.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout parent = (LinearLayout) v.getParent();
					TextView tv_slideno = (TextView) parent
							.findViewById(R.id.tv_slidecnt);
					ImageView iv_back = (ImageView) parent
							.findViewById(R.id.iv_bwd);
					RelativeLayout lay = (RelativeLayout) parent.getParent();
					LinearLayout child_lay = (LinearLayout) lay.getParent();
					RelativeLayout parent_lay = (RelativeLayout) child_lay
							.getParent();
					LinearLayout container = (LinearLayout) parent_lay
							.getParent();
					LinearLayout container1 = (LinearLayout) container
							.getParent();
					LinearLayout container2 = (LinearLayout) container1
							.getParent();

					int selected_position = (Integer) container2.getTag();
					int selected_result = (Integer) v.getTag();
					if (result_slidepath.containsKey(Integer
							.toString(selected_position)
							+ "_"
							+ Integer.toString(selected_result))) {
						if (resposne_items.containsKey(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_result))) {

							UtilityBean bean = resposne_items.get(Integer
									.toString(selected_position)
									+ "_"
									+ Integer.toString(selected_result));

							ArrayList<String> path_list = result_slidepath
									.get(Integer.toString(selected_position)
											+ "_"
											+ Integer.toString(selected_result));

							forwardbackwardaction(
									parent_lay,
									result_slidepath.get(Integer
											.toString(selected_position)
											+ "_"
											+ Integer.toString(selected_result)),

									bean.getSlideposition(), false, false);
							bean.setSlideposition(bean.getSlideposition() + 1);
							// tv_slideno.setText(Integer.toString(bean
							// .getSlideposition()));
							if (bean.getSlideposition() == path_list.size()) {
								v.setVisibility(View.INVISIBLE);
								bean.setSlideposition(bean.getSlideposition() - 1);
							}
							iv_back.setVisibility(View.VISIBLE);
						}

					}
				}
			});
			iv_backward = (ImageView) view.findViewById(R.id.iv_bwd);
			iv_backward.setTag(result_container.getChildCount());
			iv_backward.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LinearLayout parent = (LinearLayout) v.getParent();
					TextView tv_slideno = (TextView) parent
							.findViewById(R.id.tv_slidecnt);
					ImageView iv_back = (ImageView) parent
							.findViewById(R.id.iv_fwd);
					RelativeLayout lay = (RelativeLayout) parent.getParent();
					LinearLayout child_lay = (LinearLayout) lay.getParent();
					RelativeLayout parent_lay = (RelativeLayout) child_lay
							.getParent();
					LinearLayout container = (LinearLayout) parent_lay
							.getParent();
					LinearLayout container1 = (LinearLayout) container
							.getParent();
					LinearLayout container2 = (LinearLayout) container1
							.getParent();

					int selected_position = (Integer) container2.getTag();
					int selected_result = (Integer) v.getTag();
					if (result_slidepath.containsKey(Integer
							.toString(selected_position)
							+ "_"
							+ Integer.toString(selected_result))) {
						if (resposne_items.containsKey(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_result))) {

							UtilityBean bean = resposne_items.get(Integer
									.toString(selected_position)
									+ "_"
									+ Integer.toString(selected_result));

							ArrayList<String> path_list = result_slidepath
									.get(Integer.toString(selected_position)
											+ "_"
											+ Integer.toString(selected_result));
							bean.setSlideposition(bean.getSlideposition() - 1);
							forwardbackwardaction(
									parent_lay,
									result_slidepath.get(Integer
											.toString(selected_position)
											+ "_"
											+ Integer.toString(selected_result)),
									bean.getSlideposition(), false, false);
							// tv_slideno.setText(Integer.toString(bean
							// .getSlideposition()));

							if (bean.getSlideposition() == 0) {
								v.setVisibility(View.INVISIBLE);
								bean.setSlideposition(bean.getSlideposition() + 1);
							}
							iv_back.setVisibility(View.VISIBLE);
						}

					}
				}
			});
			iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
			iv_menu.setTag(result_container.getChildCount());
			iv_loc = (ImageView) view.findViewById(R.id.iv_loc);
			if (result_bean != null) {
				if (result_bean.getPosted_date() != null)
					tv_posteddate.setText(result_bean.getPosted_date());
				if (result_bean.getUsername() != null)
					tv_username.setText(result_bean.getUsername());
				if (result_bean.getQty() != null)
					tv_quantity.setText(result_bean.getQty());
				else {
					tv_quantitylbl.setVisibility(View.INVISIBLE);
					tv_quantity.setVisibility(View.INVISIBLE);
				}
				if (result_bean.getPrice() != null)
					tv_price.setText(result_bean.getPrice());
				else
					tv_price.setText("0.0");
				StringBuffer infoText = new StringBuffer();
				if (result_bean.getNameororg() != null)
					// tv_productname.setText(result_bean.getNameororg());
					infoText.append("<b>" + result_bean.getNameororg()
							+ "</b><br/>");
				if (result_bean.getAddress() != null) {
					if (result_bean.getAddress().trim().length() > 0) {
						// tv_address.setText(result_bean.getAddress());
						infoText.append(result_bean.getAddress() + "<br/>");
					} else {
						// tv_address.setVisibility(View.GONE);
					}
				} else {
					// tv_address.setVisibility(View.GONE);
				}
				if (result_bean.getEmail() != null) {
					if (result_bean.getEmail().trim().length() > 0) {
						// tv_email.setText(result_bean.getEmail());
						infoText.append(result_bean.getEmail() + "<br/>");
					} else {
						// tv_email.setVisibility(View.GONE);
					}
				} else {
					// tv_email.setVisibility(View.GONE);
				}
				if (result_bean.getC_no() != null) {
					if (result_bean.getC_no().trim().length() > 0) {
						// tv_contactno.setText(result_bean.getC_no());
						infoText.append(result_bean.getC_no() + "<br/>");
					} else {
						// tv_contactno.setVisibility(View.GONE);
					}
				} else {
					// tv_contactno.setVisibility(View.GONE);
				}
				if (result_bean.getCityordist() != null) {
					if (result_bean.getCityordist().trim().length() > 0) {
						// tv_city.setText(result_bean.getCityordist());
						infoText.append(result_bean.getCityordist() + "<br/>");
					} else {
						// tv_city.setVisibility(View.GONE);
					}
				} else {
					// tv_city.setVisibility(View.GONE);
				}
				if (result_bean.getState() != null) {
					if (result_bean.getState().trim().length() > 0) {
						// tv_state.setText(result_bean.getState());
						infoText.append(result_bean.getState() + "<br/>");
					} else {
						// tv_state.setVisibility(View.GONE);
					}
				} else {
					// tv_state.setVisibility(View.GONE);
				}
				if (result_bean.getCountry() != null) {
					if (result_bean.getCountry().trim().length() > 0) {
						// tv_country.setText(result_bean.getCountry());
						infoText.append(result_bean.getCountry() + "<br/>");
					} else {
						// tv_country.setVisibility(View.GONE);
					}
				} else {
					// tv_country.setVisibility(View.GONE);
				}
				if (result_bean.getPin() != null
						&& Integer.parseInt(result_bean.getPin()) > 0) {
					if (result_bean.getPin().trim().length() > 0) {
						// tv_pincode.setText(result_bean.getPin());
						infoText.append(result_bean.getPin() + "<br/>");
					} else {
						// tv_pincode.setVisibility(View.GONE);
					}
				} else {
					// tv_pincode.setVisibility(View.GONE);
				}

				resultText.setText(Html.fromHtml(infoText.toString()));

				if (result_bean.getLocation() != null) {
					if (result_bean.getLocation().trim().length() > 0) {
						iv_loc.setVisibility(View.VISIBLE);
						iv_loc.setTag(result_bean.getLocation());
					} else
						iv_loc.setVisibility(View.GONE);
				} else {
					iv_loc.setVisibility(View.GONE);
				}

				if (result_bean.getUsername() != null) {

					BuddyInformationBean bean = null;
					for (BuddyInformationBean temp : ContactsFragment
							.getBuddyList()) {
						if (!temp.isTitle()) {
							if (temp.getName().equalsIgnoreCase(
									result_bean.getUsername())) {
								bean = temp;
								break;
							}
						}
					}

					if (bean != null) {
						tv_invite.setVisibility(View.INVISIBLE);
						iv_status.setVisibility(View.VISIBLE);
						iv_viewprofile.setVisibility(View.VISIBLE);
						// BuddyInformationBean bean = (BuddyInformationBean)
						// WebServiceReferences.buddyList
						// .get(result_bean.getUsername());
						if (bean != null) {
							if (bean.getStatus().startsWith("Onli")) {
								iv_status
										.setBackgroundResource(R.drawable.n_online);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (bean.getStatus().startsWith("Away")) {
								iv_status
										.setBackgroundResource(R.drawable.m_away);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (bean.getStatus().startsWith("Ste")) {
								iv_status
										.setBackgroundResource(R.drawable.m_offline);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (bean.getStatus().startsWith("Airport")) {
								iv_status
										.setBackgroundResource(R.drawable.m_airport);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (bean.getStatus().equalsIgnoreCase(
									"Pending")) {
								iv_status
										.setBackgroundResource(R.drawable.n_offline);
								iv_menu.setVisibility(View.GONE);

							} else if (bean.getStatus().equalsIgnoreCase(
									"offline")) {
								iv_status
										.setBackgroundResource(R.drawable.n_offline);
								iv_menu.setVisibility(View.VISIBLE);

							}
						}
					} else if (WebServiceReferences.reqbuddyList
							.containsKey(result_bean.getUsername())) {
						tv_invite.setVisibility(View.INVISIBLE);
						iv_status.setVisibility(View.VISIBLE);
						BuddyInformationBean req_bean = (BuddyInformationBean) WebServiceReferences.reqbuddyList
								.get(result_bean.getUsername());
						if (req_bean != null) {
							if (req_bean.getStatus().startsWith("Onli")) {
								iv_status
										.setBackgroundResource(R.drawable.n_online);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (req_bean.getStatus().startsWith("Away")) {
								iv_status
										.setBackgroundResource(R.drawable.m_away);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (req_bean.getStatus().startsWith("Ste")) {
								iv_status
										.setBackgroundResource(R.drawable.m_offline);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (req_bean.getStatus().startsWith(
									"Airport")) {
								iv_status
										.setBackgroundResource(R.drawable.m_airport);
								iv_menu.setVisibility(View.VISIBLE);
							} else if (req_bean.getStatus().equalsIgnoreCase(
									"Pending")) {
								iv_status
										.setBackgroundResource(R.drawable.n_offline);
								iv_menu.setVisibility(View.GONE);
							} else if (req_bean.getStatus().equalsIgnoreCase(
									"offline")) {
								iv_status
										.setBackgroundResource(R.drawable.n_offline);
								iv_menu.setVisibility(View.VISIBLE);
							}
						}
					} else {
						tv_invite.setVisibility(View.VISIBLE);
						iv_status.setVisibility(View.INVISIBLE);
						iv_menu.setVisibility(View.GONE);
						iv_viewprofile.setVisibility(View.VISIBLE);

					}

				}
			}

			RelativeLayout slider_lay = (RelativeLayout) view
					.findViewById(R.id.lay_resimgcontainer);
			slider_lay.setFocusableInTouchMode(true);
			slider_lay.requestFocus();
			if (result_slidepath.containsKey(Integer
					.toString(selected_position)
					+ "_"
					+ Integer.toString(result_container.getChildCount()))) {
				ArrayList<String> path_list = result_slidepath.get(Integer
						.toString(selected_position)
						+ "_"
						+ Integer.toString(result_container.getChildCount()));
				forwardbackwardaction(slider_lay, path_list, 0, true,
						result_bean.isDownloading());
			} else
				forwardbackwardaction(slider_lay, null, 0, true,
						result_bean.isDownloading());
			iv_menu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					RelativeLayout lay = (RelativeLayout) v.getParent();
					RelativeLayout parent = (RelativeLayout) lay.getParent();
					LinearLayout child_lay = (LinearLayout) parent.getParent();
					LinearLayout parent_lay = (LinearLayout) child_lay
							.getParent();
					LinearLayout parent_lay1 = (LinearLayout) parent_lay
							.getParent();
					selected_position = (Integer) parent_lay1.getTag();
					selected_resultpos = (Integer) v.getTag();
					if (resposne_items.containsKey(Integer
							.toString(selected_position)
							+ "_"
							+ Integer.toString(selected_resultpos))) {
						UtilityBean bean = resposne_items.get(Integer
								.toString(selected_position)
								+ "_"
								+ Integer.toString(selected_resultpos));
						showbuddySettinsOption(bean);
					}
				}
			});
			iv_loc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent location_intent = new Intent(context,
							LocationPickerMapView.class);
					if (v.getTag().toString() != null) {
						location_intent.putExtra("loc", v.getTag().toString());
					}
					startActivity(location_intent);
				}
			});

			if (result_container != null)
				result_container.addView(view);
			result_container.setVisibility(View.VISIBLE);
			int count = result_container.getChildCount();
			if (count >= 2) {
				result_container.requestFocus();
				result_container.setFocusable(true);
				result_container.setFocusableInTouchMode(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showSettinsOption(final UtilityBean bean,
			final LinearLayout result_parent, final boolean block) {
		try {
			if (!block)
				settings_option[0] = "Block " + bean.getUsername();
			else
				settings_option[0] = "UnBlock " + bean.getUsername();

			new AlertDialog.Builder(this)
					.setSingleChoiceItems(settings_option, 0, null)
					.setPositiveButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									int selectedPosition = ((AlertDialog) dialog)
											.getListView()
											.getCheckedItemPosition();
									switch (selectedPosition) {
									case 0:
										if (WebServiceReferences.running) {
											CallDispatcher.pdialog = new ProgressDialog(
													context);
											callDisp.showprogress(
													CallDispatcher.pdialog,
													context);
											String[] block_params = new String[3];
											block_params[0] = CallDispatcher.LoginUser;
											block_params[1] = bean
													.getUsername();
											if (!block)
												block_params[2] = "1";
											else
												block_params[2] = "0";

											block_buddyname = bean
													.getUsername();

											WebServiceReferences.webServiceClient
													.blockUnblock(block_params);
										}

										break;

									case 1:
										for (int i = 0; i < result_parent
												.getChildCount(); i++) {
											RelativeLayout r_lay = (RelativeLayout) result_parent
													.getChildAt(i);
											int tag = (Integer) r_lay.getTag();
											if (tag == selected_resultpos) {
												result_parent.removeViewAt(i);
												break;
											}
										}

										break;

									default:
										break;
									}
								}
							})
					.setNegativeButton(
							SingleInstance.mainContext.getResources()
									.getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();

								}
							}).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inflateNewView(int mode, UtilityBean bean) {
		try {
			View view = getLayoutInflater().inflate(R.layout.seller_form, null,
					false);
			view.setTag(parent_view.getChildCount());
			TextView tv_name, tv_pname, tv_qty, tv_price, tv_add, tv_country, tv_state, tv_city, tv_pin, tv_mail, tv_cno;
			final EditText ed_name;
			EditText ed_pname, ed_qty, ed_price, ed_add, ed_country, ed_state, ed_city, ed_pin, ed_mail, ed_code, ed_no;
			final EditText ed_gps;

			TextView tv_date, tv_edit, tv_delete, tv_results, tv_post, tv_image, tv_otherinfo, tv_gps;

			RelativeLayout lay_parentopt, rl_video, rl_photo, rl_gps, rl_ph1, rl_ph2, rl_ph3, rl_ph4;

			ImageView iv_videoclose, iv_gps;

			final Button patchIcon, iv_commoncall, refresh, distance;

			LinearLayout form_container;

			tv_name = (TextView) view.findViewById(R.id.tv_username);
			tv_name.setTypeface(tf_bold);
			tv_pname = (TextView) view.findViewById(R.id.tv_prdctname);
			tv_pname.setTypeface(tf_bold);
			tv_qty = (TextView) view.findViewById(R.id.tv_quantity);
			tv_qty.setTypeface(tf_bold);
			tv_price = (TextView) view.findViewById(R.id.tv_prdctprice);
			tv_price.setTypeface(tf_bold);
			tv_add = (TextView) view.findViewById(R.id.tv_address);
			tv_add.setTypeface(tf_bold);
			tv_country = (TextView) view.findViewById(R.id.tv_country);
			tv_country.setTypeface(tf_bold);
			tv_state = (TextView) view.findViewById(R.id.tv_state);
			tv_state.setTypeface(tf_bold);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
			tv_city.setTypeface(tf_bold);
			tv_pin = (TextView) view.findViewById(R.id.tv_zip);
			tv_pin.setTypeface(tf_bold);
			tv_mail = (TextView) view.findViewById(R.id.tv_mail);
			tv_mail.setTypeface(tf_bold);
			tv_cno = (TextView) view.findViewById(R.id.tv_mobile);
			tv_cno.setTypeface(tf_bold);
			tv_date = (TextView) view.findViewById(R.id.tv_date);
			tv_date.setTypeface(tf_bold);
			tv_date.setText(getCurrentDateTime());
			tv_image = (TextView) view.findViewById(R.id.tv_addimages);
			tv_image.setTypeface(tf_bold);
			tv_otherinfo = (TextView) view.findViewById(R.id.tv_addotherinfo);
			tv_otherinfo.setTypeface(tf_bold);
			distance = (Button) view.findViewById(R.id.btn_distance);
			iv_gps = (ImageView) view.findViewById(R.id.iv_gps);
			iv_gps.setTag(parent_view.getChildCount());
			form_container = (LinearLayout) view
					.findViewById(R.id.forms_container);
			iv_gps.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selected_position = (Integer) v.getTag();
					for (int i = 0; i < parent_view.getChildCount(); i++) {
						LinearLayout lay_parent = (LinearLayout) parent_view
								.getChildAt(i);
						int tag = (Integer) lay_parent.getTag();
						if (selected_position == tag) {
							EditText ed_gps = (EditText) lay_parent
									.findViewById(R.id.ed_gps);
							Intent location_intent = new Intent(context,
									LocationPickerMapView.class);
							if (ed_gps.getText().toString() != null) {
								location_intent.putExtra("loc", ed_gps
										.getText().toString());
							}
							startActivityForResult(location_intent, LOCATION);
							break;
						}
					}
				}
			});

			iv_commoncall = (Button) view.findViewById(R.id.common_menu);
			iv_commoncall.setTag(parent_view.getChildCount());
			iv_commoncall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = (Integer) v.getTag();
					if (selected_results.containsKey(position)) {
						ArrayList<UtilityBean> list = selected_results
								.get(position);
						doCommonMenuAction(list);
					} else
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.kindly_select_any_result),
								Toast.LENGTH_SHORT).show();
				}
			});
			tv_gps = (TextView) view.findViewById(R.id.tv_gps);
			tv_gps.setTypeface(tf_bold);
			ed_gps = (EditText) view.findViewById(R.id.ed_gps);
			ed_gps.setTypeface(tf_regular);
			patchIcon = (Button) view.findViewById(R.id.patch_view);
			iv_videoclose = (ImageView) view.findViewById(R.id.iv_videoclose);
			iv_videoclose.setTag(parent_view.getChildCount());
			iv_videoclose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					ImageView iv_videoclose = (ImageView) v;
					RelativeLayout rl_parent = (RelativeLayout) iv_videoclose
							.getParent();
					ImageView iv_video = (ImageView) rl_parent.getChildAt(0);
					if (iv_video.getTag() != null) {
						File fle = new File((String) iv_video.getTag());
						if (fle.exists())
							fle.delete();
						iv_video.setTag(null);
					}
					iv_video.setBackgroundResource(R.drawable.ic_action_video_low);
					iv_videoclose.setVisibility(View.GONE);

				}
			});

			rl_gps = (RelativeLayout) view.findViewById(R.id.rel_gps);
			rl_gps.setTag(parent_view.getChildCount());

			rl_video = (RelativeLayout) view.findViewById(R.id.rel_video);
			rl_video.setTag(parent_view.getChildCount());
			rl_video.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selected_position = (Integer) v.getTag();
					RelativeLayout rel = (RelativeLayout) v;
					ImageView iv_video = (ImageView) rel.getChildAt(0);
					if (iv_video.getTag() == null) {
						image_path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/MVD_"
								+ CompleteListView.getFileName();
						image_path = Environment.getExternalStorageDirectory()
								+ "/COMMedia/MVD_"
								+ CompleteListView.getFileName();
						Intent intent = new Intent(context,
								CustomVideoCamera.class);
						intent.putExtra("filePath", image_path);
						// intent.putExtra("requestCode", VIDEO);
						// intent.putExtra("action",
						// MediaStore.ACTION_VIDEO_CAPTURE);
						// intent.putExtra("createOrOpen", "create");
						startActivityForResult(intent, 4);
					} else {
						String video_path = (String) iv_video.getTag();
						Intent intentVPlayer = new Intent(context,
								VideoPlayer.class);
						// intentVPlayer.putExtra("File_Path", video_path);
						// intentVPlayer.putExtra("Player_Type",
						// "Video Player");
						// intentVPlayer.putExtra("time", 0);
						intentVPlayer.putExtra("video", video_path);

						startActivity(intentVPlayer);
					}
				}
			});

			rl_photo = (RelativeLayout) view.findViewById(R.id.rel_ph1);
			rl_photo.setTag(parent_view.getChildCount());
			rl_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selected_position = (Integer) v.getTag();
					showMenu();
				}
			});
			rl_ph1 = (RelativeLayout) view.findViewById(R.id.rel_ph2);
			rl_ph1.setTag(parent_view.getChildCount());
			rl_ph1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selected_position = (Integer) v.getTag();
					rearrangeimages(v);
				}
			});
			rl_ph2 = (RelativeLayout) view.findViewById(R.id.rel_ph3);
			rl_ph2.setTag(parent_view.getChildCount());
			rl_ph2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selected_position = (Integer) v.getTag();
					rearrangeimages(v);
				}
			});
			rl_ph3 = (RelativeLayout) view.findViewById(R.id.rel_ph4);
			rl_ph3.setTag(parent_view.getChildCount());
			rl_ph3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selected_position = (Integer) v.getTag();
					rearrangeimages(v);
				}
			});
			rl_ph4 = (RelativeLayout) view.findViewById(R.id.rel_ph5);
			rl_ph4.setTag(parent_view.getChildCount());
			rl_ph4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selected_position = (Integer) v.getTag();
					rearrangeimages(v);
				}
			});

			lay_parentopt = (RelativeLayout) view
					.findViewById(R.id.datemenu_container);
			lay_parentopt.setTag(mode);
			tv_post = (TextView) view.findViewById(R.id.tv_psthide);
			tv_edit = (TextView) view.findViewById(R.id.tv_edit);
			tv_edit.setTypeface(tf_bold);
			tv_edit.setTag(parent_view.getChildCount());
			tv_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("utility", "save utility clicked");
					selected_position = (Integer) v.getTag();
					isPosted = 1;
					isresultrequested = false;
					saveUtility(false, isPosted);
				}
			});
			if (mode == 0) {
				update = true;
				tv_edit.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.save_ut));
			} else {
				tv_edit.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.update_sf));
				tv_post.setText(SingleInstance.mainContext.getResources()
						.getString(R.string.hide));
			}

			tv_delete = (TextView) view.findViewById(R.id.tv_delete);
			tv_delete.setTypeface(tf_bold);
			tv_delete.setTag(parent_view.getChildCount());
			tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					selected_position = (Integer) v.getTag();
					if (utility_items.containsKey(selected_position)) {
						showDeleteAlert();
					} else {
						RelativeLayout parent_lay = (RelativeLayout) v
								.getParent();
						int view_mode = (Integer) parent_lay.getTag();
						for (int i = 0; i < parent_view.getChildCount(); i++) {
							LinearLayout inflated_view = (LinearLayout) parent_view
									.getChildAt(i);
							int view_tag = (Integer) inflated_view.getTag();
							if (selected_position == view_tag) {
								parent_view.removeViewAt(i);
							}
						}
					}

				}
			});

			iv_gps.setTag(parent_view.getChildCount());
			iv_gps.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//
					if (callDisp.getcurrentLocation() != null) {
						showprogress();
						String[] values = callDisp.getcurrentLocation();
						ed_gps.setText(values[0] + "," + values[1]);
						Intent intent = new Intent(UtilitySeller.this,
								buddyLocation.class);
						intent.putExtra("latitude", values[0]);
						intent.putExtra("longitude", values[1]);
						startActivity(intent);
					}
				}
			});
			tv_results = (TextView) view.findViewById(R.id.tv_results);
			refresh = (Button) view.findViewById(R.id.btn_refresh);
			tv_results.setTypeface(tf_bold);
			tv_results.setTag(parent_view.getChildCount());
			tv_results.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv_res = (TextView) v;
					if (tv_res.getText().toString()
							.equalsIgnoreCase("show result(s)")) {
						selected_position = (Integer) v.getTag();
						isPosted = 0;
						isresultrequested = true;
						update = true;
						saveUtility(true, isPosted);
					} else if (tv_res.getText().toString()
							.equalsIgnoreCase("hide result(s)")) {
						selected_position = (Integer) v.getTag();
						RelativeLayout rl = (RelativeLayout) v.getParent();
						LinearLayout lay_child1 = (LinearLayout) rl.getParent();
						LinearLayout lay_parent = (LinearLayout) lay_child1
								.getParent();
						LinearLayout lay_res = (LinearLayout) lay_parent
								.findViewById(R.id.llay_resultcontainer);
						for (int i = 0; i < lay_res.getChildCount(); i++) {
							if (selected_results.containsKey(Integer
									.toString(selected_position)
									+ "_"
									+ Integer.toString(i)))
								selected_results.remove(Integer
										.toString(selected_position)
										+ "_"
										+ Integer.toString(i));
						}
						lay_res.removeAllViews();
						lay_res.setVisibility(View.GONE);
						patchIcon.setVisibility(View.GONE);
						refresh.setVisibility(View.GONE);
						tv_res.setText(SingleInstance.mainContext
								.getResources().getString(R.string.show_Result));
					}

				}
			});
			refresh.setTag(parent_view.getChildCount());
			refresh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button refresh = (Button) v;
					if (refresh.getContentDescription().toString()
							.equalsIgnoreCase("show result(s)")) {
						selected_position = (Integer) v.getTag();
						isPosted = 0;
						isresultrequested = true;
						update = true;
						saveUtility(true, isPosted);
					} else if (refresh.getContentDescription().toString()
							.equalsIgnoreCase("hide result(s)")) {
						refresh.setContentDescription("Show Result(s)");
					}
				}
			});
			tv_post = (TextView) view.findViewById(R.id.tv_psthide);
			tv_post.setTag(parent_view.getChildCount());
			tv_post.setTypeface(tf_bold);
			tv_post.setText(SingleInstance.mainContext.getResources()
					.getString(R.string.post));
			tv_post.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv_postorhide = (TextView) v;
					selected_position = (Integer) v.getTag();
					isresultrequested = false;

					if (tv_postorhide.getText().toString()
							.equalsIgnoreCase("post"))
						isPosted = 1;
					else
						isPosted = 2;
					update = true;
					saveUtility(false, isPosted);
				}
			});

			distance.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (buddyDistanceMap.size() > 0) {
						Intent intent = new Intent(context,
								UtilityDistanceActivity.class);
						intent.putExtra("distance", buddyDistanceMap);
						startActivity(intent);
					} else {
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.buddies_locaton_distance_not_available),
								1).show();
					}

				}
			});

			ed_name = (EditText) view.findViewById(R.id.ed_username);
			ed_name.setTypeface(tf_regular);
			ed_pname = (EditText) view.findViewById(R.id.ed_prdctname);
			ed_pname.setTypeface(tf_regular);
			ed_qty = (EditText) view.findViewById(R.id.ed_quantity);
			ed_qty.setTypeface(tf_regular);
			ed_price = (EditText) view.findViewById(R.id.ed_prdctprice);
			ed_price.setTypeface(tf_regular);
			ed_add = (EditText) view.findViewById(R.id.ed_address);
			ed_add.setTypeface(tf_regular);
			ed_country = (EditText) view.findViewById(R.id.ed_country);
			ed_country.setTypeface(tf_regular);
			ed_state = (EditText) view.findViewById(R.id.ed_state);
			ed_state.setTypeface(tf_regular);
			ed_city = (EditText) view.findViewById(R.id.ed_city);
			ed_city.setTypeface(tf_regular);
			ed_pin = (EditText) view.findViewById(R.id.ed_pin);
			ed_pin.setTypeface(tf_regular);
			ed_mail = (EditText) view.findViewById(R.id.ed_mail);
			ed_mail.setTypeface(tf_regular);
			ed_code = (EditText) view.findViewById(R.id.ed_code);
			ed_code.setTypeface(tf_regular);
			ed_no = (EditText) view.findViewById(R.id.ed_number);
			ed_no.setTypeface(tf_regular);

			if (mode != 0) {
				if (bean != null) {
					if (bean.getPosted_date() != null)
						tv_date.setText(bean.getPosted_date());
					else
						tv_date.setText(getCurrentDateTime());

					if (bean.getNameororg() != null)
						ed_name.setText(bean.getNameororg());
					if (bean.getProduct_name() != null) {
						ed_pname.setText(bean.getProduct_name());
					}
					if (bean.getQty() != null)
						ed_qty.setText(bean.getQty());
					if (bean.getPrice() != null)
						ed_price.setText(bean.getPrice());
					if (bean.getAddress() != null)
						ed_add.setText(bean.getAddress());
					if (bean.getCountry() != null)
						ed_country.setText(bean.getCountry());
					if (bean.getState() != null)
						ed_state.setText(bean.getState());
					if (bean.getCityordist() != null)
						ed_city.setText(bean.getCityordist());
					if (bean.getPin() != null)
						ed_pin.setText(bean.getPin());
					if (bean.getEmail() != null)
						ed_mail.setText(bean.getEmail());
					if (bean.getC_no() != null) {
						String[] no = bean.getC_no().split("-");
						if (no.length > 1) {
							if (no[0] != null)
								ed_code.setText(no[0]);
							if (no[1] != null)
								ed_no.setText(no[1]);
						} else if (no[0] != null)
							ed_no.setText(no[0]);
					}

					if (bean.getLocation() != null)
						ed_gps.setText(bean.getLocation());

					if (bean.getMode() == 1) {
						tv_post.setText(SingleInstance.mainContext
								.getResources().getString(R.string.hide));
						form_container.setBackgroundResource(R.color.white);
					} else {
						tv_post.setText(SingleInstance.mainContext
								.getResources().getString(R.string.post));
						form_container.setBackgroundResource(R.color.hidepost);
					}
					utility_items.put(parent_view.getChildCount(), bean);
				}
			} else
				tv_post.setVisibility(View.GONE);
			parent_view.addView(view);
			if (mode == 1) {
				if (bean != null) {
					if (bean.getImag_filename() != null) {
						if (bean.getImag_filename().trim().length() > 0) {
							String[] filepath = bean.getImag_filename().split(
									",");
							for (String string : filepath) {
								selected_position = parent_view.getChildCount() - 1;
								image_path = Environment
										.getExternalStorageDirectory()
										+ "/COMMedia/" + string;
								projectImage();
							}
						}

					}
					if (bean.getVideofilename() != null) {
						if (bean.getVideofilename().trim().length() > 0) {
							selected_position = parent_view.getChildCount() - 1;
							image_path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/" + bean.getVideofilename();
							projectVideo();
						}
					}

					if (bean.getLocation() != null) {
						if (bean.getLocation().trim().length() > 0) {
							selected_position = parent_view.getChildCount() - 1;
							setLocation();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean validateEmail(String emailId) {
		if (emailId.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
			return true;
		} else {
			return false;
		}
	}

	private void saveUtility(boolean withresult, int post) {

		UtilityBean se_utility = new UtilityBean();
		try {
			for (int i = 0; i < parent_view.getChildCount(); i++) {
				LinearLayout view_parent = (LinearLayout) parent_view
						.getChildAt(i);
				int tag = (Integer) view_parent.getTag();
				if (tag == selected_position) {
					EditText ed_productname = (EditText) view_parent
							.findViewById(R.id.ed_prdctname);
					EditText org_name = (EditText) view_parent
							.findViewById(R.id.ed_username);
					EditText ed_pname = (EditText) view_parent
							.findViewById(R.id.ed_prdctname);
					EditText ed_price = (EditText) view_parent
							.findViewById(R.id.ed_prdctprice);
					EditText ed_qty = (EditText) view_parent
							.findViewById(R.id.ed_quantity);
					EditText ed_address = (EditText) view_parent
							.findViewById(R.id.ed_address);
					EditText ed_country = (EditText) view_parent
							.findViewById(R.id.ed_country);
					EditText ed_state = (EditText) view_parent
							.findViewById(R.id.ed_state);
					EditText ed_city = (EditText) view_parent
							.findViewById(R.id.ed_city);
					EditText ed_pin = (EditText) view_parent
							.findViewById(R.id.ed_pin);
					EditText ed_mail = (EditText) view_parent
							.findViewById(R.id.ed_mail);
					EditText ed_code = (EditText) view_parent
							.findViewById(R.id.ed_code);
					EditText ed_cno = (EditText) view_parent
							.findViewById(R.id.ed_number);

					EditText ed_location = (EditText) view_parent
							.findViewById(R.id.ed_gps);
					TextView tv_pstorhide = (TextView) view_parent
							.findViewById(R.id.tv_psthide);
					String email = ed_mail.getText().toString().trim();
					if (ed_productname.getText().toString().trim().length() == 0) {
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.kindly_enter_product_name),
								Toast.LENGTH_LONG).show();

					} else if (ed_mail.length() > 0 && !validateEmail(email)) {
						ed_mail.requestFocus();
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.please_enter_valid_email_address),
								Toast.LENGTH_LONG).show();

					}

					else if (ed_cno.getText().toString().length() > 0
							&& ed_cno.getText().toString().length() < 10
							|| ed_cno.getText().toString().length() > 19) {
						ed_cno.requestFocus();
						Toast.makeText(
								context,
								SingleInstance.mainContext
										.getResources()
										.getString(
												R.string.phone_number_must_be_10_15_characters),
								Toast.LENGTH_SHORT).show();

					} else {
						UtilityBean bean = new UtilityBean();
						bean.setUsername(CallDispatcher.LoginUser);
						bean.setUtility_name("sell");

						if (withresult)
							bean.setResult("1");
						else
							bean.setResult("0");

						if (utility_items.containsKey(selected_position)) {
							UtilityBean edit_bean = utility_items
									.get(selected_position);
							bean.setType("edit");
							bean.setId(edit_bean.getId());

						} else {
							bean.setType("new");
							update = true;
							bean.setId(0);
							bean.setPosted_date(getCurrentDateTime());
						}
						if (org_name.getText().toString().trim().length() > 0)
							bean.setNameororg(org_name.getText().toString()
									.trim());
						if (ed_pname.getText().toString().trim().length() > 0)
							bean.setProduct_name(ed_productname.getText()
									.toString().trim());
						if (ed_price.getText().toString().trim().length() > 0)
							bean.setPrice(ed_price.getText().toString().trim());

						if (ed_qty.getText().toString().trim().length() > 0)
							bean.setQty(ed_qty.getText().toString().trim());

						if (ed_address.getText().toString().trim().length() > 0)
							bean.setAddress(ed_address.getText().toString()
									.trim());

						if (ed_country.getText().toString().trim().length() > 0)
							bean.setCountry(ed_country.getText().toString()
									.trim());

						if (ed_state.getText().toString().trim().length() > 0)
							bean.setState(ed_state.getText().toString().trim());

						if (ed_city.getText().toString().trim().length() > 0)
							bean.setCityordist(ed_city.getText().toString()
									.trim());

						if (ed_pin.getText().toString().trim().length() > 0)
							bean.setPin(ed_pin.getText().toString().trim());

						if (ed_mail.getText().toString().trim().length() > 0)
							bean.setEmail(ed_mail.getText().toString().trim());

						if (ed_code.getText().toString().trim().length() > 0
								&& ed_cno.getText().toString().trim().length() > 0)
							bean.setC_no(ed_code.getText().toString().trim()
									+ "-" + ed_cno.getText().toString().trim());
						else if (ed_cno.getText().toString().trim().length() > 0)
							bean.setC_no(ed_cno.getText().toString().trim());

						if (multimedia_info.containsKey(selected_position)) {
							UtilitypathBean path_bean = multimedia_info
									.get(selected_position);
							if (path_bean.getVideo_path() != null) {
								File v_file = new File(
										path_bean.getVideo_path());
								bean.setVideofilename(v_file.getName());

								if (!callDisp.getdbHeler(context)
										.isRecordExists(
												"select * from utility where video_file ='"
														+ v_file.getName()
														+ "'")) {
									uploadConfiguredNote(
											path_bean.getVideo_path(), bean);
								}
							}

							if (path_bean.getLocation() != null)
								bean.setLocation(path_bean.getLocation());
							else if (ed_location.getText().toString().trim()
									.length() > 0)
								bean.setLocation(ed_location.getText()
										.toString().trim());

							if (path_bean.getImagepath() != null) {
								String[] img_path = path_bean.getImagepath();
								String image_path = "";
								for (String string : img_path) {
									if (string != null) {
										if (image_path.trim().length() == 0) {
											File fle = new File(string);
											image_path = fle.getName();
										} else {
											File fle = new File(string);
											image_path = image_path + ","
													+ fle.getName();
										}
										if (!callDisp.getdbHeler(context)
												.isRecordExists(
														"select * from utility where img_file ='"
																+ image_path
																+ "'")) {
											uploadConfiguredNote(string, bean);
										}
									}
								}
								if (image_path.trim().length() > 0)
									bean.setImag_filename(image_path);
							}
						}
						bean.setPosted_date(getCurrentDateTime());

						if (tv_pstorhide.getText().toString()
								.equalsIgnoreCase("hide")) {
							if (isPosted == 2)
								bean.setMode(0);
							else
								bean.setMode(1);
						} else if (tv_pstorhide.getText().toString()
								.equalsIgnoreCase("post")) {
							if (isPosted == 1)
								bean.setMode(1);
							else
								bean.setMode(0);
						}

						selected_utility = bean;
						sellers_list = callDisp.getdbHeler(context)
								.SelectUtilityRecords(
										"select * from utility where userid='"
												+ CallDispatcher.LoginUser
												+ "' and utility_name='sell'");
						for (UtilityBean utilityBean : sellers_list) {
							saveUpdate.put(utilityBean.getId(), utilityBean);
							se_utility = saveUpdate.get(bean.getId());
						}

						if (bean.getType().equals("new")) {
							update = true;
							saveUpdate.remove(bean.getId());
							saveUpdate.put(bean.getId(), bean);

						} else {
							String existingAddress = "";
							String newAddress = "";
							if (se_utility.getAddress() != null) {
								existingAddress = se_utility.getAddress();
							}
							if (selected_utility.getAddress() != null) {
								newAddress = selected_utility.getAddress();
							}

							String existinggetAudfilename = "";
							String newgetAudiofilename = "";
							if (se_utility.getAudiofilename() != null) {

								existinggetAudfilename = se_utility
										.getAudiofilename();
								if (existinggetAudfilename.equals("''")) {
									existinggetAudfilename = "";
								}
							}
							if (selected_utility.getAudiofilename() != null) {
								newgetAudiofilename = selected_utility
										.getAudiofilename();
							}
							String existinggetVideofilename = "";
							String newgetVideofilename = "";
							if (se_utility.getVideofilename() != null) {
								existinggetVideofilename = se_utility
										.getVideofilename();
							}
							if (selected_utility.getVideofilename() != null) {
								newgetVideofilename = selected_utility
										.getVideofilename();
							}
							String existinggetNameororg = "";
							String newgetNameororg = "";
							if (se_utility.getNameororg() != null) {
								existinggetNameororg = se_utility
										.getNameororg();
							}
							if (selected_utility.getNameororg() != null) {
								newgetNameororg = selected_utility
										.getNameororg();
							}

							String existinggetState = "";
							String newgetState = "";
							if (se_utility.getState() != null) {
								existinggetState = se_utility.getState();
							}
							if (selected_utility.getState() != null) {
								newgetState = selected_utility.getState();
							}

							String existinggetC_no = "";
							String newgetC_no = "";
							if (se_utility.getC_no() != null) {
								existinggetC_no = se_utility.getC_no();
							}
							if (selected_utility.getC_no() != null) {
								newgetC_no = selected_utility.getC_no();
							}

							String existinggetCityordist = "";
							String newgetCityordist = "";
							if (se_utility.getCityordist() != null) {
								existinggetCityordist = se_utility
										.getCityordist();
							}
							if (selected_utility.getCityordist() != null) {
								newgetCityordist = selected_utility
										.getCityordist();
							}

							String existinggetCountry = "";
							String newgetCountry = "";
							if (se_utility.getCountry() != null) {
								existinggetCountry = se_utility.getCountry();
							}
							if (selected_utility.getCountry() != null) {
								newgetCountry = selected_utility.getCountry();
							}

							String existinggetEmail = "";
							String newgetEmail = "";
							if (se_utility.getEmail() != null) {
								existinggetEmail = se_utility.getEmail();
							}
							if (selected_utility.getEmail() != null) {
								newgetEmail = selected_utility.getEmail();
							}

							String existinggetPin = "";
							String newgetPin = "";
							if (se_utility.getPin() != null) {
								existinggetPin = se_utility.getPin();
							}
							if (selected_utility.getPin() != null) {
								newgetPin = selected_utility.getPin();
							}

							String existinggetPrice = "";
							String newgetPrice = "";

							if (se_utility.getPrice() != null) {
								existinggetPrice = se_utility.getPrice();
							}
							if (selected_utility.getPrice() != null) {
								newgetPrice = selected_utility.getPrice();
							}

							String existinggetQty = "";
							String newgetQty = "";

							if (se_utility.getQty() != null) {
								existinggetQty = se_utility.getQty();
							}
							if (selected_utility.getQty() != null) {
								newgetQty = selected_utility.getQty();
							}

							String existinggetCnCode = "";
							String newgetCnCode = "";

							if (se_utility.getCnCode() != null) {
								existinggetCnCode = se_utility.getCnCode();
							}
							if (selected_utility.getCnCode() != null) {
								newgetCnCode = selected_utility.getCnCode();
							}

							String existinggetImag_filename = "";
							String newgetImag_filename = "";

							if (se_utility.getImag_filename() != null) {
								existinggetImag_filename = se_utility
										.getImag_filename();
							}

							if (selected_utility.getImag_filename() != null) {
								newgetImag_filename = selected_utility
										.getImag_filename();
							}

							String existinggetLocation = "";
							String newgetLocation = "";

							if (se_utility.getLocation() != null) {
								existinggetLocation = se_utility.getLocation();
							}
							if (selected_utility.getLocation() != null) {
								newgetLocation = selected_utility.getLocation();
							}

							if (!existinggetNameororg
									.equalsIgnoreCase(newgetNameororg)
									|| !existingAddress
											.equalsIgnoreCase(newAddress)
									|| !existinggetState
											.equalsIgnoreCase(newgetState)
									|| !existinggetC_no
											.equalsIgnoreCase(newgetC_no)
									|| !existinggetCityordist
											.equalsIgnoreCase(newgetCityordist)
									|| !existinggetCountry
											.equalsIgnoreCase(newgetCountry)
									|| !existinggetEmail
											.equalsIgnoreCase(newgetEmail)
									|| !existinggetPin
											.equalsIgnoreCase(newgetPin)
									|| !existinggetPrice
											.equalsIgnoreCase(newgetPrice)
									|| !existinggetQty
											.equalsIgnoreCase(newgetQty)

									|| !existinggetLocation
											.equalsIgnoreCase(newgetLocation)
									|| !existinggetImag_filename
											.equalsIgnoreCase(newgetImag_filename)
									|| !existinggetCnCode
											.equalsIgnoreCase(newgetCnCode)
									|| !existinggetVideofilename
											.equalsIgnoreCase(newgetVideofilename)
									|| !existinggetAudfilename
											.equalsIgnoreCase(newgetAudiofilename)
									|| !se_utility.getProduct_name()
											.equalsIgnoreCase(
													selected_utility
															.getProduct_name())) {

								update = true;
								saveUpdate.remove(bean.getId());
								saveUpdate.put(bean.getId(), bean);

							}
						}

						if (update) {
							// saveUpdate.remove(bean.getId());
							// saveUpdate.put(bean.getId(), bean);
							update = false;
							if (WebServiceReferences.running) {
								CallDispatcher.pdialog = new ProgressDialog(
										context);
								callDisp.showprogress(CallDispatcher.pdialog,
										context);

								WebServiceReferences.webServiceClient
										.getsetUtility(bean, null);
							}

						} else {

							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_updates_available),
									Toast.LENGTH_SHORT).show();

						}

					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void rearrangeimages(View v) {
		try {
			if (multimedia_info.containsKey(selected_position)) {
				UtilitypathBean bean = multimedia_info.get(selected_position);
				RelativeLayout rl = (RelativeLayout) v;
				ImageView iv = (ImageView) rl.getChildAt(0);
				String actual_path = (String) iv.getTag();
				if (bean.getImagepath() != null) {
					String[] path = bean.getImagepath();
					for (int i = 0; i < path.length; i++) {
						if (path[i] != null) {
							if (path[i].equals(actual_path)) {
								path[i] = null;
								break;
							}
						}
					}
					String[] rearraged_path = new String[4];
					int count = 0;
					for (String string : path) {
						if (string != null) {
							rearraged_path[count] = string;
							count += 1;
						}
					}
					bean.setImagepath(rearraged_path);
				}
				v.setVisibility(View.GONE);
				LinearLayout lay = (LinearLayout) v.getParent();
				for (int i = 0; i < lay.getChildCount(); i++) {
					RelativeLayout rel_child = (RelativeLayout) lay
							.getChildAt(i);
					if (i == 0)
						rel_child.setVisibility(View.VISIBLE);
					else
						rel_child.setVisibility(View.GONE);
				}
				image_path = null;
				projectImage();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		try {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ib_addlist:
				inflateNewView(0, null);
				scrollDown();
				break;
			case R.id.iv_utility_back:
				if (utility_items.size() > 0) {
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					if (imm != null && getCurrentFocus() != null)
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
				}
				finish();
				break;
			case R.id.block_button:
				if (callDisp.blocked_buddies.size() > 0) {
					Intent block_intent = new Intent(context,
							Blocked_list.class);
					startActivity(block_intent);
				} else
					Toast.makeText(
							context,
							SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.you_are_not_blocked_any_buddy),
							Toast.LENGTH_SHORT).show();
				break;
			case R.id.save_button:
				saveAllUtilities();
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scrollDown() {
		try {
			if (scrl_view != null) {
				scrl_view.post(new Runnable() {

					@Override
					public void run() {
						scrl_view.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getCurrentDateTime() {
		try {
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			return sdf.format(curDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void showMenu() {
		try {
			final CharSequence[] items = {
					SingleInstance.mainContext.getResources().getString(
							R.string.from_gallery),
					SingleInstance.mainContext.getResources().getString(
							R.string.from_camera) };
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0)
						openGalery();
					else if (item == 1)
						openCamera();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openGalery() {

		try {
			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, FROMGALLERY);
			} else {
				Log.i("img", "sdk is above 19");
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, FORKITKAT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openCamera() {

		try {
			Long free_size = callDisp.getExternalMemorySize();
			if (free_size > 0 && free_size >= 5120) {
				image_path = CompleteListView.getFileName() + ".jpg";
				String strIPath = Environment.getExternalStorageDirectory()
						+ "/COMMedia/MPD_" + image_path;
				image_path = strIPath;
				// Intent intent = new Intent(context, MultimediaUtils.class);
				// intent.putExtra("filePath", image_path);
				// intent.putExtra("requestCode", FROMCAMERA);
				// intent.putExtra("action", MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra("createOrOpen", "create");
				// startActivity(intent);
				Intent intent = new Intent(context, CustomVideoCamera.class);
				intent.putExtra("filePath", image_path);
				intent.putExtra("isPhoto", true);
				startActivityForResult(intent, FROMCAMERA);
			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.insufficient_memory),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == LOCATION) {
				try {
					Bundle bun = data.getBundleExtra("location");
					longitude = bun.getString("longitude");
					latitude = bun.getString("latitude");
					location = bun.getString("address");
					if (latitude.length() > 16) {
						latitude = latitude.substring(0, 16);
					}
					if (longitude.length() > 16) {
						longitude = longitude.substring(0, 16);
					}
					if (location != null && latitude != null
							&& longitude != null) {
						Log.i("utility", "========> lat :: " + latitude);
						Log.i("utility", "========> lon :: " + longitude);
						Log.i("utility", "========> address :: " + location);
						setLocation();
					}
				} catch (NullPointerException e) {

				} catch (Exception e) {

				}
			}
			if (requestCode == FROMGALLERY) {
				if (resultCode == RESULT_CANCELED) {
					Toast.makeText(context, "No Photo selected",
							Toast.LENGTH_SHORT).show();
				} else {
					Uri selectedImageUri = data.getData();
					image_path = callDisp.getRealPathFromURI(selectedImageUri);
					File selected_file = new File(image_path);
					int length = (int) selected_file.length() / 1048576;
					Log.d("busy", "........ size is------------->" + length);
					if (length <= 2) {
						Bitmap bmp = callDisp.ResizeImage(image_path);
						if (bmp != null) {
							final String path = Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/MPD_"
									+ CompleteListView.getFileName() + ".jpg";

							BufferedOutputStream stream;
							try {
								stream = new BufferedOutputStream(
										new FileOutputStream(new File(path)));
								bmp.compress(CompressFormat.JPEG, 100, stream);

								image_path = path;
								bmp.recycle();
								bmp = null;

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						projectImage();
					} else
						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(R.string.large_image),
								Toast.LENGTH_SHORT).show();

				}
			} else if (requestCode == FORKITKAT) {
				Uri selectedImageUri = data.getData();
				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				getContentResolver().takePersistableUriPermission(
						selectedImageUri, takeFlags);
				image_path = selectedImageUri.getPath();

				File selected_file = new File(image_path);
				int length = (int) selected_file.length() / 1048576;
				Log.d("busy", "........ size is------------->" + length);

				if (length <= 2) {
					new bitmaploader().execute(selectedImageUri);
				} else {
					Toast.makeText(context, "Result canceled",
							Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == FROMCAMERA) {
				if (resultCode == RESULT_CANCELED) {
					Toast.makeText(context, "Result canceled",
							Toast.LENGTH_SHORT).show();
				} else {
					Bitmap bmp = callDisp.ResizeImage(image_path);
					callDisp.changemyPictureOrientation(bmp, image_path);
					if (bmp != null && !bmp.isRecycled())
						bmp.recycle();
					projectImage();
				}

			} else if (requestCode == VIDEO) {
				if (resultCode == RESULT_CANCELED) {
					Toast.makeText(context, "Result canceled",
							Toast.LENGTH_SHORT).show();
				} else {
					projectVideo();
				}
			}
		} catch (Exception e) {
			Log.i("seller", "====> " + e.getMessage());
			e.printStackTrace();

		}
	}

	private void projectVideo() {
		try {
			for (int i = 0; i < parent_view.getChildCount(); i++) {
				LinearLayout child_layout = (LinearLayout) parent_view
						.getChildAt(i);
				int tag = (Integer) child_layout.getTag();
				if (selected_position == tag) {
					LinearLayout photolayout = (LinearLayout) child_layout
							.findViewById(R.id.forms_otherinfo);
					if (multimedia_info.containsKey(tag)) {
						UtilitypathBean bean = multimedia_info.get(tag);
						if (this.image_path != null) {
							if (!image_path.endsWith(".mp4"))
								image_path = image_path + ".mp4";
							RelativeLayout rel_photo = (RelativeLayout) photolayout
									.getChildAt(0);
							ImageView iview = (ImageView) rel_photo
									.getChildAt(0);
							iview.setTag(this.image_path);
							ImageView iv_close = (ImageView) rel_photo
									.getChildAt(1);
							iv_close.setTag(this.image_path);
							iview.setBackgroundResource(R.drawable.v_play);
							iv_close.setVisibility(View.VISIBLE);
							rel_photo.setVisibility(View.VISIBLE);
							bean.setVideo_path(image_path);
						}

					} else {
						if (image_path != null) {
							if (!image_path.endsWith(".mp4"))
								image_path = image_path + ".mp4";
							UtilitypathBean bean = new UtilitypathBean();
							RelativeLayout rel_photo = (RelativeLayout) photolayout
									.getChildAt(0);
							ImageView iview = (ImageView) rel_photo
									.getChildAt(0);
							iview.setTag(this.image_path);
							ImageView iv_close = (ImageView) rel_photo
									.getChildAt(1);
							iv_close.setTag(image_path);
							iview.setBackgroundResource(R.drawable.v_play);
							iv_close.setVisibility(View.VISIBLE);
							bean.setVideo_path(image_path);
							rel_photo.setVisibility(View.VISIBLE);
							multimedia_info.put(selected_position, bean);

						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void projectImage() {
		try {
			for (int i = 0; i < parent_view.getChildCount(); i++) {
				LinearLayout child_layout = (LinearLayout) parent_view
						.getChildAt(i);
				int tag = (Integer) child_layout.getTag();
				if (selected_position == tag) {
					LinearLayout photolayout = (LinearLayout) child_layout
							.findViewById(R.id.forms_photocontainer1);
					if (multimedia_info.containsKey(tag)) {
						UtilitypathBean bean = multimedia_info.get(tag);
						String[] image_path = bean.getImagepath();
						if (image_path != null) {
							int count = 1;
							for (String img_path : image_path) {
								if (img_path != null) {
									RelativeLayout rel_photo = (RelativeLayout) photolayout
											.getChildAt(count);
									if (rel_photo.getVisibility() != View.VISIBLE) {
										ImageView iview = (ImageView) rel_photo
												.getChildAt(0);
										iview.setTag(img_path);
										ImageView iv_close = (ImageView) rel_photo
												.getChildAt(1);
										Bitmap img_bmp = callDisp
												.ResizeImage(img_path);
										if (img_bmp != null)
											iview.setImageBitmap(img_bmp);
										iv_close.setVisibility(View.VISIBLE);
										rel_photo.setVisibility(View.VISIBLE);
									}
								} else {
									if (this.image_path != null) {
										RelativeLayout rel_photo = (RelativeLayout) photolayout
												.getChildAt(count);
										if (rel_photo.getVisibility() != View.VISIBLE) {
											ImageView iview = (ImageView) rel_photo
													.getChildAt(0);
											iview.setTag(this.image_path);
											ImageView iv_close = (ImageView) rel_photo
													.getChildAt(1);
											Bitmap img_bmp = callDisp
													.ResizeImage(this.image_path);
											if (img_bmp != null)
												iview.setImageBitmap(img_bmp);
											iv_close.setVisibility(View.VISIBLE);
											image_path[count - 1] = this.image_path;
											rel_photo
													.setVisibility(View.VISIBLE);
											break;
										}
									}

								}
								count += 1;
							}
							if (count == 4) {
								RelativeLayout rel_photo = (RelativeLayout) photolayout
										.getChildAt(0);
								rel_photo.setVisibility(View.GONE);
							}
						} else {
							if (this.image_path != null) {
								image_path = new String[4];
								image_path[0] = this.image_path;
								RelativeLayout rel_photo = (RelativeLayout) photolayout
										.getChildAt(1);
								ImageView iview = (ImageView) rel_photo
										.getChildAt(0);
								iview.setTag(this.image_path);
								ImageView iv_close = (ImageView) rel_photo
										.getChildAt(1);
								Bitmap img_bmp = callDisp
										.ResizeImage(this.image_path);
								if (img_bmp != null)
									iview.setImageBitmap(img_bmp);
								iv_close.setVisibility(View.VISIBLE);
								rel_photo.setVisibility(View.VISIBLE);
								bean.setImagepath(image_path);
							}
						}
					} else {
						if (image_path != null) {
							UtilitypathBean bean = new UtilitypathBean();
							String[] image_path = new String[4];
							image_path[0] = this.image_path;
							RelativeLayout rel_photo = (RelativeLayout) photolayout
									.getChildAt(1);
							ImageView iview = (ImageView) rel_photo
									.getChildAt(0);
							iview.setTag(this.image_path);
							ImageView iv_close = (ImageView) rel_photo
									.getChildAt(1);
							Bitmap img_bmp = callDisp
									.ResizeImage(this.image_path);
							if (img_bmp != null)
								iview.setImageBitmap(img_bmp);
							iv_close.setVisibility(View.VISIBLE);
							bean.setImagepath(image_path);
							rel_photo.setVisibility(View.VISIBLE);
							multimedia_info.put(selected_position, bean);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertorupdateutility(UtilityBean bean) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("userid", bean.getUsername());
			cv.put("org_name", bean.getNameororg());
			cv.put("product_name", bean.getProduct_name());
			cv.put("quantity", bean.getQty());
			cv.put("price", bean.getPrice());
			cv.put("video_file", bean.getVideofilename());
			cv.put("img_file", bean.getImag_filename());
			cv.put("voice", "'" + "'");
			cv.put("location", bean.getLocation());
			cv.put("address", bean.getAddress());
			cv.put("country", bean.getCountry());
			cv.put("state", bean.getState());
			cv.put("city", bean.getCityordist());
			cv.put("pin", bean.getPin());
			cv.put("email", bean.getEmail());
			cv.put("c_no", bean.getC_no());
			cv.put("entry_mode", bean.getMode());
			cv.put("utility_name", "sell");
			cv.put("posted_date", bean.getPosted_date());
			cv.put("id", bean.getId());
			if (!utility_items.containsKey(selected_position)) {
				long id = callDisp.getdbHeler(context).insertUtility(cv);
				Log.d("utility", "Inserted row id-->" + id);
			} else {
				UtilityBean edit_utility = utility_items.get(selected_position);
				long id = callDisp.getdbHeler(context).UpdateUtility(cv,
						"posted_date='" + edit_utility.getPosted_date() + "'");
				Log.d("utility", "updated row id-->" + id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cancelDialog();
		}
	}

	public void insertorUpdateUtilityAllItems(UtilityBean bean) {
		ContentValues cv = new ContentValues();
		cv.put("userid", bean.getUsername());
		cv.put("org_name", bean.getNameororg());
		cv.put("product_name", bean.getProduct_name());
		cv.put("quantity", bean.getQty());
		cv.put("price", bean.getPrice());
		cv.put("video_file", bean.getVideofilename());
		cv.put("img_file", bean.getImag_filename());
		cv.put("voice", "'" + "'");
		cv.put("location", bean.getLocation());
		cv.put("address", bean.getAddress());
		cv.put("country", bean.getCountry());
		cv.put("state", bean.getState());
		cv.put("city", bean.getCityordist());
		cv.put("pin", bean.getPin());
		cv.put("email", bean.getEmail());
		cv.put("c_no", bean.getC_no());
		cv.put("entry_mode", bean.getMode());
		cv.put("utility_name", "sell");
		cv.put("posted_date", bean.getPosted_date());
		cv.put("id", bean.getId());
		if (DBAccess.getdbHeler().isRecordExists(
				"select * from utility where product_name='"
						+ bean.getProduct_name() + "' and posted_date='"
						+ bean.getPosted_date() + "'")) {
			DBAccess.getdbHeler().UpdateUtility(
					cv,
					"product_name='" + bean.getProduct_name()
							+ "' and posted_date='" + bean.getPosted_date()
							+ "'");
		} else {
			long id = DBAccess.getdbHeler().insertUtility(cv);
		}

	}

	public void notifywebserviceReponse(Object obj) {
		try {
			if (obj instanceof UtilityResponse) {
				UtilityResponse response = (UtilityResponse) obj;
				if (response != null) {
					if (response.getDeletedutility_id() != null) {
						callDisp.getdbHeler(context).deleteUtility(
								response.getDeletedutility_id());
						for (int i = 0; i < parent_view.getChildCount(); i++) {
							LinearLayout inflated_view = (LinearLayout) parent_view
									.getChildAt(i);
							int view_tag = (Integer) inflated_view.getTag();
							if (selected_position == view_tag) {
								parent_view.removeViewAt(i);
							}
						}
						if (utility_items.containsKey(selected_position))
							utility_items.remove(selected_position);
						Toast.makeText(context, response.getMessage(),
								Toast.LENGTH_SHORT).show();
						if (multimedia_info.containsKey(selected_position)) {
							UtilitypathBean bean = multimedia_info
									.remove(selected_position);
							if (bean.getVideo_path() != null) {
								File video_file = new File(bean.getVideo_path());
								if (video_file.exists())
									video_file.exists();
							}
							if (bean.getImagepath() != null) {
								String[] img_path = bean.getImagepath();
								for (String string : img_path) {
									if (string != null) {
										File video_file = new File(string);
										if (video_file.exists())
											video_file.exists();
									}
								}
							}
						}
					} else if (response.getEditedutility_id() != null) {
						selected_utility.setPosted_date(response
								.getPosted_date());
						selected_utility.setId(Integer.parseInt(response
								.getEditedutility_id()));
						insertorupdateutility(selected_utility);
						Log.d("utility",
								"Received id is" + response.getUtility_id());
						if (!isresultrequested) {
							if (isPosted == 2)
								Toast.makeText(context,
										"Your Add hided succesfully",
										Toast.LENGTH_SHORT).show();
							else if (isPosted == 1)
								Toast.makeText(context,
										"Your Add posted succesfully",
										Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(context, "Updated succesfully",
										Toast.LENGTH_SHORT).show();
						} else {

							isresultrequested = false;
							if (response.getResult_list() != null) {
								if (response.getResult_list().size() > 0) {
									LinearLayout lay_result = null;
									if (buddyDistanceMap.size() > 0) {
										buddyDistanceMap.clear();
									}
									for (int i = 0; i < parent_view
											.getChildCount(); i++) {
										LinearLayout inflated_view = (LinearLayout) parent_view
												.getChildAt(i);

										int view_tag = (Integer) inflated_view
												.getTag();
										if (selected_position == view_tag) {
											lay_result = (LinearLayout) inflated_view
													.findViewById(R.id.llay_resultcontainer);
											TextView tv_res = (TextView) inflated_view
													.findViewById(R.id.tv_results);
											tv_res.setText("Hide Result(s)");
											Button refresh = (Button) inflated_view
													.findViewById(R.id.btn_refresh);
											Button distance = (Button) inflated_view
													.findViewById(R.id.btn_distance);
											refresh.setContentDescription("Hide Result(s)");
											refresh.setVisibility(View.VISIBLE);
											distance.setVisibility(View.GONE);
											break;
										}
									}
									for (int i = 0; i < parent_view
											.getChildCount(); i++) {
										LinearLayout inflated_view = (LinearLayout) parent_view
												.getChildAt(i);
										LinearLayout lay_res = (LinearLayout) inflated_view
												.findViewById(R.id.llay_resultcontainer);
										lay_res.removeAllViews();
									}

									if (callDisp != null)
										callDisp.compareutilityresponse(
												response.getResult_list(),
												context, lay_result);

								} else
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.no_buyer_found_for_this_add),
											Toast.LENGTH_SHORT).show();
							} else
								Toast.makeText(
										context,
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.no_buyer_found_for_this_add),
										Toast.LENGTH_SHORT).show();
						}

						for (int i = 0; i < parent_view.getChildCount(); i++) {
							LinearLayout lay_child = (LinearLayout) parent_view
									.getChildAt(i);
							int tag = (Integer) lay_child.getTag();
							if (tag == selected_position) {
								TextView tv_edit = (TextView) lay_child
										.findViewById(R.id.tv_edit);
								tv_edit.setText(SingleInstance.mainContext
										.getResources().getString(
												R.string.update_sf));

								TextView tv_date = (TextView) lay_child
										.findViewById(R.id.tv_date);
								LinearLayout form_container = (LinearLayout) lay_child
										.findViewById(R.id.forms_container);
								tv_date.setText(response.getPosted_date());

								if (isPosted == 2) {
									TextView tv_post = (TextView) lay_child
											.findViewById(R.id.tv_psthide);
									tv_post.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.post));
									form_container
											.setBackgroundResource(R.color.hidepost);
								} else if (isPosted == 1) {
									TextView tv_post = (TextView) lay_child
											.findViewById(R.id.tv_psthide);
									tv_post.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.hide));
									form_container
											.setBackgroundResource(R.color.white);
								}
								isPosted = 0;

								break;
							}
						}

						Vector<UtilityBean> item = callDisp.getdbHeler(context)
								.SelectUtilityRecords(
										"select * from utility where id="
												+ response
														.getEditedutility_id()
												+ " and posted_date='"
												+ response.getPosted_date()
												+ "'");
						UtilityBean bean = null;
						Log.d("utility", "Item size--->" + item.size());
						if (item.size() == 1)
							bean = item.get(0);

						if (bean != null)
							utility_items.put(selected_position, bean);

					} else {
						selected_utility.setPosted_date(response
								.getPosted_date());
						selected_utility.setId(Integer.parseInt(response
								.getUtility_id()));
						insertorupdateutility(selected_utility);

						if (!isresultrequested) {
							Toast.makeText(
									context,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.saved_successfully),
									Toast.LENGTH_SHORT).show();
						} else {

							isresultrequested = false;
							if (response.getResult_list() != null) {
								if (response.getResult_list().size() > 0) {
									LinearLayout lay_result = null;

									for (int i = 0; i < parent_view
											.getChildCount(); i++) {
										LinearLayout inflated_view = (LinearLayout) parent_view
												.getChildAt(i);
										int view_tag = (Integer) inflated_view
												.getTag();
										Button patchIcon = (Button) inflated_view
												.findViewById(R.id.patch_view);
										patchIcon.setVisibility(View.VISIBLE);
										patchIcon.setText(Integer
												.toString(response
														.getResult_list()
														.size()));

										if (selected_position == view_tag) {
											lay_result = (LinearLayout) inflated_view
													.findViewById(R.id.llay_resultcontainer);
											TextView tv_res = (TextView) inflated_view
													.findViewById(R.id.tv_results);
											tv_res.setText("Hide Result(s)");
											Button refresh = (Button) inflated_view
													.findViewById(R.id.btn_refresh);
											refresh.setContentDescription("Hide Result(s)");
											refresh.setVisibility(View.VISIBLE);
											Button distance = (Button) inflated_view
													.findViewById(R.id.btn_distance);
											distance.setVisibility(View.GONE);
											break;
										}
									}
									for (int i = 0; i < parent_view
											.getChildCount(); i++) {
										LinearLayout inflated_view = (LinearLayout) parent_view
												.getChildAt(i);
										LinearLayout lay_res = (LinearLayout) inflated_view
												.findViewById(R.id.llay_resultcontainer);
										lay_res.removeAllViews();
									}
									if (callDisp != null)
										callDisp.compareutilityresponse(
												response.getResult_list(),
												context, lay_result);

								} else
									Toast.makeText(
											context,
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.no_buyer_found_for_this_add),
											Toast.LENGTH_SHORT).show();
							} else
								Toast.makeText(
										context,
										SingleInstance.mainContext
												.getResources()
												.getString(
														R.string.no_buyer_found_for_this_add),
										Toast.LENGTH_SHORT).show();
						}

						for (int i = 0; i < parent_view.getChildCount(); i++) {
							LinearLayout lay_child = (LinearLayout) parent_view
									.getChildAt(i);
							int tag = (Integer) lay_child.getTag();
							if (tag == selected_position) {
								TextView tv_edit = (TextView) lay_child
										.findViewById(R.id.tv_edit);
								tv_edit.setText(SingleInstance.mainContext
										.getResources().getString(
												R.string.update_sf));
								TextView tv_date = (TextView) lay_child
										.findViewById(R.id.tv_date);
								tv_date.setText(response.getPosted_date());
								TextView tv_post = (TextView) lay_child
										.findViewById(R.id.tv_psthide);
								tv_post.setText(SingleInstance.mainContext
										.getResources()
										.getString(R.string.hide));
								isPosted = 0;
								tv_post.setVisibility(View.VISIBLE);
								break;
							}
						}
						if (!utility_items.containsKey(selected_position)) {
							Vector<UtilityBean> item = callDisp.getdbHeler(
									context).SelectUtilityRecords(
									"select * from utility where id="
											+ response.getUtility_id()
											+ " and posted_date='"
											+ response.getPosted_date() + "'");
							UtilityBean bean = null;
							if (item.size() == 1)
								bean = item.get(0);

							if (bean != null)
								utility_items.put(selected_position, bean);
						}
					}

				}
			} else if (obj instanceof WebServiceBean) {
				WebServiceBean service_bean = (WebServiceBean) obj;
				Toast.makeText(context, service_bean.getText(),
						Toast.LENGTH_SHORT).show();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		try {
			SingleInstance.audioComponent = false;
			// TODO Auto-generated method stub
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm != null && getCurrentFocus() != null)
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						0);

			if (WebServiceReferences.contextTable.containsKey("utilityseller"))
				WebServiceReferences.contextTable.remove("utilityseller");
			super.onDestroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showDeleteAlert() {
		try {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
			myAlertDialog.setTitle("Delete Add");
			myAlertDialog.setMessage(SingleInstance.mainContext.getResources()
					.getString(R.string.are_you_sure_you_want_to_delete));
			myAlertDialog.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							if (utility_items.containsKey(selected_position)) {
								UtilityBean item = utility_items
										.get(selected_position);
								UtilityBean bean = new UtilityBean();
								bean.setType("delete");
								bean.setUsername(CallDispatcher.LoginUser);
								bean.setId(item.getId());
								bean.setUtility_name("sell");
								bean.setProduct_name(item.getProduct_name());
								bean.setPosted_date(item.getPosted_date());
								if (WebServiceReferences.running) {
									CallDispatcher.pdialog = new ProgressDialog(
											context);
									callDisp.showprogress(
											CallDispatcher.pdialog, context);
									WebServiceReferences.webServiceClient
											.getsetUtility(bean, null);
								}
							}
						}
					});
			myAlertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {
							// do something when the Cancel button is clicked

							dialog.cancel();
						}
					});
			myAlertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showbuddySettinsOption(final UtilityBean bean) {
		try {
			new AlertDialog.Builder(this)
					.setSingleChoiceItems(settings_buddyopt, 0, null)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
									SingleInstance.audioComponent = true;
									int selectedPosition = ((AlertDialog) dialog)
											.getListView()
											.getCheckedItemPosition();
									final PermissionBean permissionBean = callDisp
											.getdbHeler(context)
											.selectPermissions(
													"select * from setpermission where userid='"
															+ bean.getUsername()
															+ "' and buddyid='"
															+ CallDispatcher.LoginUser
															+ "'",
													bean.getUsername(),
													CallDispatcher.LoginUser);
									switch (selectedPosition) {
									case 0:
										if (permissionBean.getAudio_call()
												.equals("1")) {

											BuddyInformationBean bib = null;
											for (BuddyInformationBean temp : ContactsFragment
													.getBuddyList()) {
												if (!temp.isTitle()) {
													if (temp.getName()
															.equalsIgnoreCase(
																	bean.getUsername())) {
														bib = temp;
														break;
													}
												}
											}
											// BuddyInformationBean bib =
											// WebServiceReferences.buddyList
											// .get(bean.getUsername());
											if (bib != null)
												processCallRequest(
														bean.getUsername(),
														bib.getStatus(), 1);
										} else
											Toast.makeText(
													context,
													SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.access_denied),
													Toast.LENGTH_SHORT).show();
										break;
									case 1:
										if (permissionBean.getVideo_call()
												.equals("1")) {

											BuddyInformationBean bib2 = null;
											for (BuddyInformationBean temp : ContactsFragment
													.getBuddyList()) {
												if (!temp.isTitle()) {
													if (temp.getName()
															.equalsIgnoreCase(
																	bean.getUsername())) {
														bib2 = temp;
														break;
													}
												}
											}
											// BuddyInformationBean bib2 =
											// WebServiceReferences.buddyList
											// .get(bean.getUsername());
											if (bib2 != null)
												processCallRequest(
														bean.getUsername(),
														bib2.getStatus(), 2);
										} else
											Toast.makeText(
													context,
													SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.access_denied),
													Toast.LENGTH_SHORT).show();
										break;
									case 2:
										if (permissionBean.getMMchat().equals(
												"1")) {

											BuddyInformationBean bib = null;
											for (BuddyInformationBean temp : ContactsFragment
													.getBuddyList()) {
												if (!temp.isTitle()) {
													if (temp.getName()
															.equalsIgnoreCase(
																	bean.getUsername())) {
														bib = temp;
														break;
													}
												}
											}
											// BuddyInformationBean bib =
											// WebServiceReferences.buddyList
											// .get(bean.getUsername());
											if (bib != null)
												doMultiMMChat(bean
														.getUsername());
										} else
											callDisp.showAlert("Response",
													"Access Denied");
										break;
									case 3:
										if (permissionBean.getPhotoMessage()
												.equals("1")) {
											Intent photoInent = new Intent(
													context,
													ComponentCreator.class);
											Bundle photoBndl = new Bundle();
											photoBndl
													.putString("type", "photo");
											photoBndl
													.putBoolean("action", true);
											photoBndl
													.putBoolean("forms", false);
											photoBndl.putString("buddyname",
													bean.getUsername());
											photoBndl.putBoolean("send", true);
											photoInent.putExtras(photoBndl);
											startActivity(photoInent);
										} else
											callDisp.showAlert("Response",
													"Access Denied");
										break;
									case 4:
										if (permissionBean.getAudioMessage()
												.equals("1")) {
											Intent intentComponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle bndl = new Bundle();
											bndl.putString("type", "audio");
											bndl.putBoolean("action", true);
											bndl.putBoolean("forms", false);
											bndl.putString("buddyname",
													bean.getUsername());
											bndl.putBoolean("send", true);
											intentComponent.putExtras(bndl);
											startActivity(intentComponent);
										} else
											callDisp.showAlert("Response",
													"Access Denied");
										break;
									case 5:
										if (permissionBean.getVideoMessage()
												.equals("1")) {
											Intent audio_component = new Intent(
													context,
													ComponentCreator.class);
											Bundle audio_bndl = new Bundle();
											audio_bndl.putString("type",
													"video");
											audio_bndl.putBoolean("action",
													true);
											audio_bndl.putBoolean("forms",
													false);
											audio_bndl.putString("buddyname",
													bean.getUsername());
											audio_bndl.putBoolean("send", true);
											audio_component
													.putExtras(audio_bndl);
											startActivity(audio_component);
										} else
											callDisp.showAlert("Response",
													"Access Denied");
										break;
									case 6:
										if (permissionBean.getSketchMessage()
												.equals("1")) {
											Intent sketchcomponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle sketch_bndl = new Bundle();
											sketch_bndl.putString("type",
													"handsketch");
											sketch_bndl.putBoolean("action",
													true);
											sketch_bndl.putBoolean("forms",
													false);
											sketch_bndl.putString("buddyname",
													bean.getUsername());
											sketch_bndl
													.putBoolean("send", true);
											sketchcomponent
													.putExtras(sketch_bndl);
											startActivity(sketchcomponent);
										} else
											callDisp.showAlert("Response",
													"Access Denied");
										break;

									default:
										break;
									}

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processCallRequest(String username, String state, int caseid) {

		try {
			if (username != null && state.equalsIgnoreCase("Offline")
					|| state.equals("Stealth")
					|| state.equalsIgnoreCase("pending")
					|| state.equalsIgnoreCase("Virtual")
					|| state.equalsIgnoreCase("airport")) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(context);
					callDisp.showprogress(CallDispatcher.pdialog, context);

					String[] res_info = new String[3];
					res_info[0] = CallDispatcher.LoginUser;
					res_info[1] = username;
					if (state.equals("Offline") || state.equals("Stealth"))
						res_info[2] = callDisp
								.getdbHeler(context)
								.getwheninfo(
										"select cid from clonemaster where cdescription='Offline'");
					else
						res_info[2] = "";

					WebServiceReferences.webServiceClient
							.OfflineCallResponse(res_info);
				}

			} else {
				if (!state.equalsIgnoreCase("pending")) {
					callDisp.MakeCall(caseid, username, context);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String profile_buddy;

	private String profile_buddystatus;

	private ProgressDialog pDialog;

	void doViewProfile(boolean accept, String buddy, String status) {

		try {

			profile_buddy = buddy;

			profile_buddystatus = status;
			ArrayList<String> profileList = DBAccess.getdbHeler().getProfile(
					buddy);
			// ArrayList<String> profileList = callDisp.getdbHeler(context)
			// .getProfile(buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				// Intent intent = new Intent(context, ViewProfiles.class);
				// intent.putExtra("buddyname", buddy);
				// intent.putExtra("buddystatus", status);
				// startActivity(intent);
				viewProfile(buddy);

			} else {
				Log.i("profile", "VIEW PROFILE------>" + buddy
						+ "---->GetProfileDetails");
				CallDispatcher.pdialog = new ProgressDialog(context);
				callDisp.showprogress(CallDispatcher.pdialog, context);
				CallDispatcher.isFromCallDisp = false;
				String modifiedDate = callDisp.getdbHeler(context)
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
				WebServiceReferences.webServiceClient
						.getStandardProfilefieldvalues(profileDetails);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void viewProfile(String buddy) {
		// ViewProfileFragment viewProfileFragment = ViewProfileFragment
		// .newInstance(context);
		// // Bundle bundle = new Bundle();
		// // bundle.putString("buddyname", buddy);
		// // viewProfileFragment.setArguments(bundle);
		// viewProfileFragment.setBuddyName(buddy);
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

	public void notifyViewProfile() {
		try {
			ArrayList<String> profileList = callDisp.getdbHeler(context)
					.getProfile(profile_buddy);
			Log.i("profile", "size of arrayList--->" + profileList.size());

			if (profileList.size() > 0) {
				if (!WebServiceReferences.contextTable
						.containsKey("viewprofile")) {
					// Intent intent = new Intent(context, ViewProfiles.class);
					// intent.putExtra("buddyname", profile_buddy);
					// intent.putExtra("buddystatus", profile_buddystatus);
					// startActivity(intent);
					viewProfile(profile_buddy);
				} else {
					(ViewProfileFragment.newInstance(context)).initView();
				}

			} else
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_profile_assigned_for_this_user),
						Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setLocation() {
		try {
			// TODO Auto-generated method stub
			if (location != null) {
				for (int i = 0; i < parent_view.getChildCount(); i++) {
					LinearLayout lay_parent = (LinearLayout) parent_view
							.getChildAt(i);
					int tag = (Integer) lay_parent.getTag();
					if (selected_position == tag) {
						EditText ed_gps;
						if (lay_parent != null) {
							ed_gps = (EditText) lay_parent
									.findViewById(R.id.ed_gps);
							ed_gps.setText(latitude + "," + longitude);
						}
						if (multimedia_info.containsKey(selected_position)) {
							UtilitypathBean bean = multimedia_info
									.get(selected_position);
							bean.setLocation(latitude + "," + longitude);

						} else {
							UtilitypathBean bean = new UtilitypathBean();
							bean.setLocation(latitude + "," + longitude);
							multimedia_info.put(selected_position, bean);
						}
						break;
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void downloadConfiguredNote(String path, int selected_position,
			int res_position) {
		try {
			// TODO Auto-generated method stub

			if (CallDispatcher.LoginUser != null) {
				if (path != null && path.trim().length() > 0
						&& !path.equalsIgnoreCase("null")) {
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username("ftpadmin");
					ftpBean.setFtp_password("ftppassword");
					ftpBean.setFile_path(path);
					ftpBean.setOperation_type(2);
					ftpBean.setReq_object(Integer.toString(selected_position)
							+ "_" + Integer.toString(res_position));
					ftpBean.setRequest_from("utility_seller");
					appMainActivity.getFtpNotifier().addTasktoExecutor(ftpBean);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadConfiguredNote(String path, UtilityBean bean) {
		try {
			// TODO Auto-generated method stub

			if (path != null) {
				if (CallDispatcher.LoginUser != null) {
					String username = preferences
							.getString("ftpusername", null);
					String password = preferences
							.getString("ftppassword", null);
					FTPBean ftpBean = new FTPBean();
					ftpBean.setServer_ip(appMainActivity.cBean.getRouter()
							.split(":")[0]);
					ftpBean.setServer_port(40400);
					ftpBean.setFtp_username(username);
					ftpBean.setFtp_password(password);
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
					ftpBean.setRequest_from("utility_seller");
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

	public class bitmaploader extends AsyncTask<Uri, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				Log.d("image", "came to post execute for image");
				callDisp.cancelDialog();
				projectImage();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			CallDispatcher.pdialog = new ProgressDialog(context);
			callDisp.showprogress(CallDispatcher.pdialog, context);
		}

		@Override
		protected Void doInBackground(Uri... params) {
			// TODO Auto-generated method stub
			try {
				for (Uri uri : params) {
					Log.d("image", "came to doin backgroung for image");
					String strIPath = Environment.getExternalStorageDirectory()
							+ "/COMMedia/MPD_" + CompleteListView.getFileName()
							+ ".jpg";
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
					image_path = strIPath;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}

	private void doMultiMMChat(String selectedBuddy) {

		try {
			if (callDisp.isConnected) {
				BuddyInformationBean buddyBean = null;
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(
								selectedBuddy.trim())) {
							buddyBean = temp;
							break;
						}
					}
				}
				Log.d("MIM",
						"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ condition satisfied");
				Utility utility = new Utility();
				WebServiceReferences.SelectedBuddy = selectedBuddy;
				SignalingBean bean = new SignalingBean();
				bean.setSessionid(utility.getSessionID());
				bean.setFrom(CallDispatcher.LoginUser);
				bean.setTo(selectedBuddy);
				bean.setConferencemember("");
				bean.setMessage("");
				bean.setCallType("MSG");
				Intent intent = new Intent(context, GroupChatActivity.class);
				intent.putExtra("buddy", selectedBuddy);
				intent.putExtra("status", buddyBean.getStatus());
				intent.putExtra("sessionid", utility.getSessionID());
				// CallDispatcher.commEngine.makeCall(CallDispatcher.sb);
				context.startActivity(intent);
				// Intent intent = new Intent(context, IMTabScreen.class);
				// intent.putExtra("sb", bean);
				// intent.putExtra("fromto", true);
				// context.startActivity(intent);
				// CallDispatcher.commEngine.makeCall(CallDispatcher.sb);

			} else {
				Toast.makeText(
						context,
						SingleInstance.mainContext.getResources().getString(
								R.string.network_err), Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyResultDownloaded(FTPBean bean) {
		try {
			if (bean != null) {
				String ids = null;
				if (bean.getReq_object() != null)
					ids = (String) bean.getReq_object();
				{
					String[] id_list = ids.split("_");
					int selected_position = Integer.parseInt(id_list[0]);
					int selected_rsultposition = Integer.parseInt(id_list[1]);
					if (result_slidepath.containsKey(selected_position + "_"
							+ selected_rsultposition)) {
						ArrayList<String> path_info = result_slidepath
								.get(selected_position + "_"
										+ selected_rsultposition);
						path_info.add(Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + bean.getFile_path());
					} else {
						ArrayList<String> path_list = new ArrayList<String>();
						path_list.add(Environment.getExternalStorageDirectory()
								+ "/COMMedia/" + bean.getFile_path());
						result_slidepath.put(selected_position + "_"
								+ selected_rsultposition, path_list);
					}
					for (int i = 0; i < parent_view.getChildCount(); i++) {
						LinearLayout parent = (LinearLayout) parent_view
								.getChildAt(i);
						int tag = (Integer) parent.getTag();
						if (selected_position == tag) {
							LinearLayout result_viewcontainer = (LinearLayout) parent
									.findViewById(R.id.llay_resultcontainer);
							for (int j = 0; j < result_viewcontainer
									.getChildCount(); i++) {
								RelativeLayout rl_result = (RelativeLayout) result_viewcontainer
										.getChildAt(j);
								int child_tag = (Integer) rl_result.getTag();
								if (child_tag == selected_rsultposition) {
									ImageView iv_resultimg = (ImageView) result_viewcontainer
											.findViewById(R.id.img_slide);
									LinearLayout lay = (LinearLayout) result_viewcontainer
											.findViewById(R.id.lay_fwdbwd);
									ArrayList<String> list = result_slidepath
											.get(selected_position + "_"
													+ selected_rsultposition);
									if (list != null) {
										if (list.size() > 0) {
											String path = list.get(0);
											if (path.contains("MPD")) {
												Bitmap bmp = callDisp
														.ResizeImage(path);
												iv_resultimg
														.setImageBitmap(bmp);
												iv_resultimg.setTag(path);

											} else {
												Bitmap bmp = BitmapFactory
														.decodeResource(
																getResources(),
																R.drawable.v_play);
												iv_resultimg
														.setImageBitmap(bmp);
												iv_resultimg.setTag(path);
											}
										}
										if (list.size() > 1)
											lay.setVisibility(View.VISIBLE);
									}
									break;
								}
							}
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void forwardbackwardaction(RelativeLayout result_parent,
			ArrayList<String> path_list, int postion, boolean isinitial,
			boolean isdownloading) {
		try {
			ImageView iv_resultimg = (ImageView) result_parent
					.findViewById(R.id.img_slide);
			LinearLayout lay_out = (LinearLayout) result_parent
					.findViewById(R.id.lay_fwdbwd);
			ImageView iv_bwd = (ImageView) result_parent
					.findViewById(R.id.iv_bwd);
			if (isinitial) {
				if (path_list != null) {
					if (path_list.size() > 0) {
						String path = path_list.get(0);
						if (path.contains("MPD")) {
							Bitmap bmp = callDisp.ResizeImage(path);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);

						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_play);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);
						}
						if (path_list.size() > 1) {
							lay_out.setVisibility(View.VISIBLE);
							iv_bwd.setVisibility(View.INVISIBLE);
						} else
							lay_out.setVisibility(View.INVISIBLE);
					} else {
						if (isdownloading) {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.download_result);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(null);
						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.no_image);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(null);
						}
						lay_out.setVisibility(View.INVISIBLE);
					}
				} else {
					if (isdownloading) {
						Bitmap bmp = BitmapFactory.decodeResource(
								getResources(), R.drawable.download_result);
						iv_resultimg.setImageBitmap(bmp);
						iv_resultimg.setTag(null);
					} else {
						Bitmap bmp = BitmapFactory.decodeResource(
								getResources(), R.drawable.no_image);
						iv_resultimg.setImageBitmap(bmp);
						iv_resultimg.setTag(null);
					}
					lay_out.setVisibility(View.INVISIBLE);
				}
			} else {
				if (path_list != null) {
					if (path_list.size() > 0) {
						String path = path_list.get(postion);
						File fle = new File(path);
						if (fle.exists()) {
							if (path.contains("MPD")) {
								Bitmap bmp = callDisp.ResizeImage(path);
								iv_resultimg.setImageBitmap(bmp);
								iv_resultimg.setTag(path);

							} else {
								Bitmap bmp = BitmapFactory.decodeResource(
										getResources(), R.drawable.v_play);
								iv_resultimg.setImageBitmap(bmp);
								iv_resultimg.setTag(path);
							}
						} else {
							Bitmap bmp = BitmapFactory.decodeResource(
									getResources(), R.drawable.broken);
							iv_resultimg.setImageBitmap(bmp);
							iv_resultimg.setTag(path);
						}

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void notifyBuddyblockunblock(Object res) {
		try {
			callDisp.cancelDialog();
			if (res instanceof String[]) {
				String[] result = (String[]) res;
				if (res != null) {
					if (result[3] != null) {
						if (result[3].equals("Unblocked successfully")) {
							for (int i = 0; i < callDisp.blocked_buddies.size(); i++) {
								if (callDisp.blocked_buddies.get(i).equals(
										result[1])) {
									callDisp.blocked_buddies.remove(i);
									break;
								}
							}
						} else if (result[3].equals("Blocked successfully")) {
							callDisp.blocked_buddies.add(result[1]);
							for (Entry<String, UtilityBean> set : resposne_items
									.entrySet()) {
								String key = set.getKey();
								UtilityBean value = set.getValue();
								if (value.getUsername().equalsIgnoreCase(
										result[1])) {
									String[] ids = key.split("_");
									int parent_tag = Integer.parseInt(ids[0]);
									int result_tag = Integer.parseInt(ids[1]);
									for (int i = 0; i < parent_view
											.getChildCount(); i++) {
										LinearLayout child_lay = (LinearLayout) parent_view
												.getChildAt(i);
										int tag = (Integer) child_lay.getTag();
										if (parent_tag == tag) {
											LinearLayout result_container = (LinearLayout) child_lay
													.findViewById(R.id.llay_resultcontainer);
											for (int j = 0; j < result_container
													.getChildCount(); j++) {
												RelativeLayout result_layout = (RelativeLayout) result_container
														.getChildAt(j);
												int result_id = (Integer) result_layout
														.getTag();
												if (result_tag == result_id) {
													result_container
															.removeViewAt(j);
													break;
												}
											}
										}
									}
								}
							}
						} else
							Toast.makeText(context, result[3],
									Toast.LENGTH_SHORT).show();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doCommonMenuAction(ArrayList<UtilityBean> buddyList) {
		try {
			if (buddyList != null) {
				final StringBuffer sb = new StringBuffer();
				for (UtilityBean bean : buddyList) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(bean.getUsername());

				}
				new AlertDialog.Builder(this)
						.setSingleChoiceItems(common_options, 0, null)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										int selectedPosition = ((AlertDialog) dialog)
												.getListView()
												.getCheckedItemPosition();

										switch (selectedPosition) {
										case 0:
											doConferenceCall(sb.toString(),
													"AC");
											break;
										case 1:
											doConferenceCall(sb.toString(),
													"VC");
											break;
										case 2:
											Intent photoInent = new Intent(
													context,
													ComponentCreator.class);
											Bundle photoBndl = new Bundle();
											photoBndl
													.putString("type", "photo");
											photoBndl
													.putBoolean("action", true);
											photoBndl
													.putBoolean("forms", false);
											photoBndl.putString("buddyname",
													sb.toString());
											photoBndl.putBoolean("send", true);
											photoInent.putExtras(photoBndl);
											startActivity(photoInent);
											break;
										case 3:
											Intent intentComponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle bndl = new Bundle();
											bndl.putString("type", "audio");
											bndl.putBoolean("action", true);
											bndl.putBoolean("forms", false);
											bndl.putString("buddyname",
													sb.toString());
											bndl.putBoolean("send", true);
											intentComponent.putExtras(bndl);
											startActivity(intentComponent);

											break;
										case 4:
											Intent videoIntent = new Intent(
													context,
													ComponentCreator.class);
											Bundle videoBndl = new Bundle();
											videoBndl
													.putString("type", "video");
											videoBndl
													.putBoolean("action", true);
											videoBndl
													.putBoolean("forms", false);
											videoBndl.putString("buddyname",
													sb.toString());
											videoBndl.putBoolean("send", true);
											videoIntent.putExtras(videoBndl);
											startActivity(videoIntent);
											break;
										case 5:
											Intent sketchcomponent = new Intent(
													context,
													ComponentCreator.class);
											Bundle sketchBndl = new Bundle();
											sketchBndl.putString("type",
													"handsketch");
											sketchBndl.putBoolean("action",
													true);
											sketchBndl.putBoolean("forms",
													false);
											sketchBndl.putString("buddyname",
													sb.toString());
											sketchBndl.putBoolean("send", true);
											sketchcomponent
													.putExtras(sketchBndl);
											startActivity(sketchcomponent);
											break;

										default:
											break;
										}
									}
								}).show();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doConferenceCall(String buddyNames, String callType) {
		try {
			String[] str = null;

			if (buddyNames.contains(",")) {
				str = buddyNames.split(",");
			} else {
				str = new String[1];
				str[0] = buddyNames;
			}
			for (int i = 0; i < str.length; i++) {
				BuddyInformationBean bib = null;
				for (BuddyInformationBean temp : ContactsFragment
						.getBuddyList()) {
					if (!temp.isTitle()) {
						if (temp.getName().equalsIgnoreCase(str[i])) {
							bib = temp;
							break;
						}
					}
				}

				// BuddyInformationBean bib = WebServiceReferences.buddyList
				// .get(str[i]);
				if (bib.getStatus().startsWith("Onli")) {
					CallDispatcher.conConference.add(str[i]);
					callDisp.ConMadeConference(callType,
							SipNotificationListener.getCurrentContext());
				} else {
					if (WebServiceReferences.running) {
						CallDispatcher.pdialog = new ProgressDialog(context);
						callDisp.showprogress(CallDispatcher.pdialog, context);

						String[] res_info = new String[3];
						res_info[0] = CallDispatcher.LoginUser;
						res_info[1] = bib.getName();
						if (bib.getStatus().equals("Offline")
								|| bib.getStatus().equals("Stealth"))
							res_info[2] = callDisp
									.getdbHeler(context)
									.getwheninfo(
											"select cid from clonemaster where cdescription='Offline'");
						else
							res_info[2] = "";

						WebServiceReferences.webServiceClient
								.OfflineCallResponse(res_info);
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyOfflineCallResponse(final Object obj) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					callDisp.cancelDialog();
					if (obj instanceof ArrayList) {
						ArrayList<Object> callresponse_list = (ArrayList<Object>) obj;
						if (callresponse_list.size() == 3) {
							String user_id = null;
							String buddy_id = null;
							if (callresponse_list.get(0) instanceof String)
								user_id = (String) callresponse_list.get(0);
							if (callresponse_list.get(1) instanceof String)
								buddy_id = (String) callresponse_list.get(1);
							if (callresponse_list.get(2) instanceof ArrayList)
								;
							ArrayList<OfflineRequestConfigBean> config_list = (ArrayList<OfflineRequestConfigBean>) callresponse_list
									.get(2);

							if (config_list != null) {
								for (OfflineRequestConfigBean offlineRequestConfigBean : config_list) {
									ContentValues cv = new ContentValues();
									cv.put("config_id",
											offlineRequestConfigBean.getId());
									cv.put("fromuser", offlineRequestConfigBean
											.getBuddyId());
									cv.put("messagetitle",
											offlineRequestConfigBean
													.getMessageTitle());
									cv.put("messagetype",
											offlineRequestConfigBean
													.getMessagetype());
									cv.put("message", offlineRequestConfigBean
											.getMessage());
									cv.put("responsetype",
											offlineRequestConfigBean
													.getResponseType());
									cv.put("response", "''");
									cv.put("url",
											offlineRequestConfigBean.getUrl());
									cv.put("receivedtime", CompleteListView
											.getNoteCreateTimeForFiles());
									cv.put("sendstatus", "0");
									cv.put("username", CallDispatcher.LoginUser);

									callDisp.getdbHeler(context)
											.insertOfflinePendingClones(cv);
									callDisp.downloadOfflineresponse(
											offlineRequestConfigBean
													.getMessage(),
											offlineRequestConfigBean.getId(),
											"answering machine", null);
								}
							}

						}

					} else if (obj instanceof WebServiceBean) {
						WebServiceBean service_bean = (WebServiceBean) obj;
						showAlert(service_bean.getText());
						callDisp.cancelDialog();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void showAlert(String message) {
		try {
			final AlertDialog alertDialog = new AlertDialog.Builder(context)
					.create();

			// Setting Dialog Title
			alertDialog.setTitle("Response");

			// Setting Dialog Message
			alertDialog.setMessage(message);

			// Setting Icon to Dialog
			// alertDialog.setIcon(R.drawable.tick);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					alertDialog.dismiss();
					Log.i("profile", "INSIDE insertRecords------? ");

					// CustomAddOnFragment addOnFragment=new
					// CustomAddOnFragment();
					// addOnFragment.insertRecords();
					//
					callDisp.cancelDialog();

					alertDialog.dismiss();
					// finish();
				}
			});

			// Showing Alert Message
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyResultSorted(ArrayList<UtilityBean> response,
			LinearLayout lay_result) {
		try {
			for (UtilityBean utilityBean : response) {
				resposne_items.put(Integer.toString(selected_position) + "_"
						+ lay_result.getChildCount(), utilityBean);

				Log.i("buyer", "buddy name ===> " + utilityBean.getUsername());
				Log.i("buyer", "distance ====> " + utilityBean.getDistance());
				if (utilityBean.getDistance() != null) {
					buddyDistanceMap.put(utilityBean.getUsername(),
							utilityBean.getDistance());
				}
				Log.i("buyer", " buyer latlong ===>" + utilityBean.getLatlong());
				ArrayList<String> path_list = new ArrayList<String>();
				if (utilityBean.getImag_filename() != null) {
					String[] img_filename = utilityBean.getImag_filename()
							.split(",");
					for (String string : img_filename) {
						if (string != null) {
							if (string.trim().length() > 0) {
								File img_file = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/COMMedia/" + string);
								if (!img_file.exists()) {
									utilityBean.setDownloading(true);
									downloadConfiguredNote(string,
											selected_position,
											lay_result.getChildCount());
								} else
									path_list.add(Environment
											.getExternalStorageDirectory()
											+ "/COMMedia/" + string);
							}
						}
					}
				}
				if (utilityBean.getAudiofilename() != null) {
					if (utilityBean.getAudiofilename().trim().length() > 0) {
						File audio_file = new File(
								Environment.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ utilityBean.getAudiofilename());
						if (!audio_file.exists()) {
							utilityBean.setDownloading(true);
							downloadConfiguredNote(
									utilityBean.getAudiofilename(),
									selected_position,
									lay_result.getChildCount());
						} else
							path_list.add(Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ utilityBean.getAudiofilename());
					}
				}

				if (utilityBean.getVideofilename() != null) {
					if (utilityBean.getVideofilename().trim().length() > 0) {
						File video_file = new File(
								Environment.getExternalStorageDirectory()
										+ "/COMMedia/"
										+ utilityBean.getVideofilename());
						if (!video_file.exists()) {
							utilityBean.setDownloading(true);
							downloadConfiguredNote(
									utilityBean.getVideofilename(),
									selected_position,
									lay_result.getChildCount());
						} else
							path_list.add(Environment
									.getExternalStorageDirectory()
									+ "/COMMedia/"
									+ utilityBean.getVideofilename());
					}
				}
				result_slidepath.put(Integer.toString(selected_position) + "_"
						+ Integer.toString(lay_result.getChildCount()),
						path_list);
				inflateRsultView(lay_result, utilityBean);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (utility_items.size() > 0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				if (imm != null && getCurrentFocus() != null)
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(), 0);
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	private void showprogress() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(context);
		pDialog.setCancelable(false);
		pDialog.setMessage("Progress ...");
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setProgress(0);
		pDialog.setMax(100);
		pDialog.show();

	}

	public void cancelDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	public void notifySaveAllUtilityItems(Object object) {
		try {
			if (object instanceof Vector) {
				Vector<UtilityResponse> responseList = (Vector<UtilityResponse>) object;
				final HashMap<String, String> dateMap = new HashMap<String, String>();
				if (responseList != null && responseList.size() > 0) {
					for (UtilityResponse utilityResponse : responseList) {
						if (utilityMap.containsKey(utilityResponse
								.getProductName())) {
							UtilityBean utilityBean = utilityMap
									.get(utilityResponse.getProductName());
							utilityBean.setId(Integer.parseInt(utilityResponse
									.getUtility_id()));
							utilityBean.setPosted_date(utilityResponse
									.getPosted_date());
							dateMap.put(utilityResponse.getProductName(),
									utilityResponse.getPosted_date());
							insertorUpdateUtilityAllItems(utilityBean);

						}
					}
					for (int i = 0; i < parent_view.getChildCount(); i++) {
						LinearLayout lay_child = (LinearLayout) parent_view
								.getChildAt(i);
						int view_tag = (Integer) lay_child.getTag();
						if (selectedUtilityMap.containsKey(view_tag)) {
							final EditText ed_productname = (EditText) lay_child
									.findViewById(R.id.ed_prdctname);
							final TextView tv_edit = (TextView) lay_child
									.findViewById(R.id.tv_edit);
							final TextView tv_date = (TextView) lay_child
									.findViewById(R.id.tv_date);
							final LinearLayout form_container = (LinearLayout) lay_child
									.findViewById(R.id.forms_container);
							// tv_date.setText(response.getPosted_date());
							final TextView tv_post = (TextView) lay_child
									.findViewById(R.id.tv_psthide);
							handler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									if (dateMap.containsKey(ed_productname
											.getText().toString())) {
										String postedDate = dateMap
												.get(ed_productname.getText()
														.toString());
										tv_date.setText(postedDate);
									}
									tv_edit.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.update_sf));
									tv_post.setText(SingleInstance.mainContext
											.getResources().getString(
													R.string.post));
									form_container
											.setBackgroundResource(R.color.hidepost);
									tv_post.setVisibility(View.VISIBLE);
								}
							});

						}
					}

				}
			} else if (object instanceof WebServiceBean) {
				WebServiceBean service_bean = (WebServiceBean) object;
				Toast.makeText(context, service_bean.getText(),
						Toast.LENGTH_SHORT).show();
			} else if (object instanceof String) {
				Toast.makeText(context, (String) object, Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cancelDialog();
		} finally {
			cancelDialog();
		}
	}

	private void saveAllUtilities() {
		try {
			utilityList.clear();
			utilityMap.clear();
			selectedUtilityMap.clear();
			for (int i = 0; i < parent_view.getChildCount(); i++) {
				LinearLayout view_parent = (LinearLayout) parent_view
						.getChildAt(i);
				EditText ed_productname = (EditText) view_parent
						.findViewById(R.id.ed_prdctname);
				EditText org_name = (EditText) view_parent
						.findViewById(R.id.ed_username);
				EditText ed_pname = (EditText) view_parent
						.findViewById(R.id.ed_prdctname);
				EditText ed_price = (EditText) view_parent
						.findViewById(R.id.ed_prdctprice);
				EditText ed_qty = (EditText) view_parent
						.findViewById(R.id.ed_quantity);
				EditText ed_address = (EditText) view_parent
						.findViewById(R.id.ed_address);
				EditText ed_country = (EditText) view_parent
						.findViewById(R.id.ed_country);
				EditText ed_state = (EditText) view_parent
						.findViewById(R.id.ed_state);
				EditText ed_city = (EditText) view_parent
						.findViewById(R.id.ed_city);
				EditText ed_pin = (EditText) view_parent
						.findViewById(R.id.ed_pin);
				EditText ed_mail = (EditText) view_parent
						.findViewById(R.id.ed_mail);
				EditText ed_code = (EditText) view_parent
						.findViewById(R.id.ed_code);
				EditText ed_cno = (EditText) view_parent
						.findViewById(R.id.ed_number);

				EditText ed_location = (EditText) view_parent
						.findViewById(R.id.ed_gps);
				TextView tv_pstorhide = (TextView) view_parent
						.findViewById(R.id.tv_psthide);
				String email = ed_mail.getText().toString().trim();
				if (ed_productname.getText().toString().trim().length() == 0) {
					Toast.makeText(context, "Kindly Enter Product name",
							Toast.LENGTH_LONG).show();

				} else if (ed_mail.length() > 0 && !validateEmail(email)) {
					ed_mail.requestFocus();
					Toast.makeText(context, "Kindly Enter valid Email",
							Toast.LENGTH_LONG).show();

				}

				else if (ed_cno.getText().toString().length() > 0
						&& ed_cno.getText().toString().length() < 10
						|| ed_cno.getText().toString().length() > 19) {
					ed_cno.requestFocus();
					Toast.makeText(context,
							"Phone Number must be 10-19 characters",
							Toast.LENGTH_SHORT).show();

				} else {
					UtilityBean bean = new UtilityBean();
					bean.setUsername(CallDispatcher.LoginUser);
					bean.setUtility_name("sell");

					bean.setResult("0");
					int selected_position = (Integer) view_parent.getTag();
					if (utility_items.containsKey(selected_position)) {
						UtilityBean edit_bean = utility_items
								.get(selected_position);
						bean.setType("edit");
						bean.setId(edit_bean.getId());

					} else {
						bean.setType("new");
						update = true;
						bean.setId(0);
						bean.setPosted_date(getCurrentDateTime());
					}
					if (org_name.getText().toString().trim().length() > 0)
						bean.setNameororg(org_name.getText().toString().trim());
					if (ed_pname.getText().toString().trim().length() > 0)
						bean.setProduct_name(ed_productname.getText()
								.toString().trim());
					if (ed_price.getText().toString().trim().length() > 0)
						bean.setPrice(ed_price.getText().toString().trim());

					if (ed_qty.getText().toString().trim().length() > 0)
						bean.setQty(ed_qty.getText().toString().trim());

					if (ed_address.getText().toString().trim().length() > 0)
						bean.setAddress(ed_address.getText().toString().trim());

					if (ed_country.getText().toString().trim().length() > 0)
						bean.setCountry(ed_country.getText().toString().trim());

					if (ed_state.getText().toString().trim().length() > 0)
						bean.setState(ed_state.getText().toString().trim());

					if (ed_city.getText().toString().trim().length() > 0)
						bean.setCityordist(ed_city.getText().toString().trim());

					if (ed_pin.getText().toString().trim().length() > 0)
						bean.setPin(ed_pin.getText().toString().trim());

					if (ed_mail.getText().toString().trim().length() > 0)
						bean.setEmail(ed_mail.getText().toString().trim());

					if (ed_code.getText().toString().trim().length() > 0
							&& ed_cno.getText().toString().trim().length() > 0)
						bean.setC_no(ed_code.getText().toString().trim() + "-"
								+ ed_cno.getText().toString().trim());
					else if (ed_cno.getText().toString().trim().length() > 0)
						bean.setC_no(ed_cno.getText().toString().trim());

					if (multimedia_info.containsKey(selected_position)) {
						UtilitypathBean path_bean = multimedia_info
								.get(selected_position);
						if (path_bean.getVideo_path() != null) {
							File v_file = new File(path_bean.getVideo_path());
							bean.setVideofilename(v_file.getName());

							if (!callDisp.getdbHeler(context).isRecordExists(
									"select * from utility where video_file ='"
											+ v_file.getName() + "'")) {
								uploadConfiguredNote(path_bean.getVideo_path(),
										bean);
							}
						}

						if (path_bean.getLocation() != null)
							bean.setLocation(path_bean.getLocation());
						else if (ed_location.getText().toString().trim()
								.length() > 0)
							bean.setLocation(ed_location.getText().toString()
									.trim());

						if (path_bean.getImagepath() != null) {
							String[] img_path = path_bean.getImagepath();
							String image_path = "";
							for (String string : img_path) {
								if (string != null) {
									if (image_path.trim().length() == 0) {
										File fle = new File(string);
										image_path = fle.getName();
									} else {
										File fle = new File(string);
										image_path = image_path + ","
												+ fle.getName();
									}
									if (!callDisp.getdbHeler(context)
											.isRecordExists(
													"select * from utility where img_file ='"
															+ image_path + "'")) {
										uploadConfiguredNote(string, bean);
									}
								}
							}
							if (image_path.trim().length() > 0)
								bean.setImag_filename(image_path);
						}
					}
					bean.setPosted_date(getCurrentDateTime());

					if (tv_pstorhide.getText().toString()
							.equalsIgnoreCase("hide")) {
						if (isPosted == 2)
							bean.setMode(0);
						else
							bean.setMode(1);
					} else if (tv_pstorhide.getText().toString()
							.equalsIgnoreCase("post")) {
						if (isPosted == 1)
							bean.setMode(1);
						else
							bean.setMode(0);
					}

					selected_utility = bean;
					sellers_list = callDisp.getdbHeler(context)
							.SelectUtilityRecords(
									"select * from utility where userid='"
											+ CallDispatcher.LoginUser
											+ "' and utility_name='sell'");
					for (UtilityBean utilityBean : sellers_list) {
						saveUpdate.put(utilityBean.getId(), utilityBean);
						// se_utility = saveUpdate.get(bean.getId());
					}

					if (bean.getType().equals("new")) {
						update = true;
						saveUpdate.remove(bean.getId());
						saveUpdate.put(bean.getId(), bean);
						utilityList.add(bean);
						utilityMap.put(bean.getProduct_name(), bean);

					} else {
						//
						// String existingAddress = "";
						// String newAddress = "";
						// if (se_utility.getAddress() != null) {
						// existingAddress = se_utility.getAddress();
						// }
						// if (selected_utility.getAddress() != null) {
						// newAddress = selected_utility.getAddress();
						// }
						//
						// String existinggetAudfilename = "";
						// String newgetAudiofilename = "";
						// if (se_utility.getAudiofilename() != null) {
						//
						// existinggetAudfilename =
						// se_utility.getAudiofilename();
						// if (existinggetAudfilename.equals("''")) {
						// existinggetAudfilename = "";
						// }
						// }
						// if (selected_utility.getAudiofilename() != null) {
						// newgetAudiofilename = selected_utility
						// .getAudiofilename();
						// }
						// String existinggetVideofilename = "";
						// String newgetVideofilename = "";
						// if (se_utility.getVideofilename() != null) {
						// existinggetVideofilename = se_utility
						// .getVideofilename();
						// }
						// if (selected_utility.getVideofilename() != null) {
						// newgetVideofilename = selected_utility
						// .getVideofilename();
						// }
						// String existinggetNameororg = "";
						// String newgetNameororg = "";
						// if (se_utility.getNameororg() != null) {
						// existinggetNameororg = se_utility.getNameororg();
						// }
						// if (selected_utility.getNameororg() != null) {
						// newgetNameororg = selected_utility.getNameororg();
						// }
						//
						// String existinggetState = "";
						// String newgetState = "";
						// if (se_utility.getState() != null) {
						// existinggetState = se_utility.getState();
						// }
						// if (selected_utility.getState() != null) {
						// newgetState = selected_utility.getState();
						// }
						//
						// String existinggetC_no = "";
						// String newgetC_no = "";
						// if (se_utility.getC_no() != null) {
						// existinggetC_no = se_utility.getC_no();
						// }
						// if (selected_utility.getC_no() != null) {
						// newgetC_no = selected_utility.getC_no();
						// }
						//
						// String existinggetCityordist = "";
						// String newgetCityordist = "";
						// if (se_utility.getCityordist() != null) {
						// existinggetCityordist = se_utility.getCityordist();
						// }
						// if (selected_utility.getCityordist() != null) {
						// newgetCityordist = selected_utility.getCityordist();
						// }
						//
						// String existinggetCountry = "";
						// String newgetCountry = "";
						// if (se_utility.getCountry() != null) {
						// existinggetCountry = se_utility.getCountry();
						// }
						// if (selected_utility.getCountry() != null) {
						// newgetCountry = selected_utility.getCountry();
						// }
						//
						// String existinggetEmail = "";
						// String newgetEmail = "";
						// if (se_utility.getEmail() != null) {
						// existinggetEmail = se_utility.getEmail();
						// }
						// if (selected_utility.getEmail() != null) {
						// newgetEmail = selected_utility.getEmail();
						// }
						//
						// String existinggetPin = "";
						// String newgetPin = "";
						// if (se_utility.getPin() != null) {
						// existinggetPin = se_utility.getPin();
						// }
						// if (selected_utility.getPin() != null) {
						// newgetPin = selected_utility.getPin();
						// }
						//
						// String existinggetPrice = "";
						// String newgetPrice = "";
						//
						// if (se_utility.getPrice() != null) {
						// existinggetPrice = se_utility.getPrice();
						// }
						// if (selected_utility.getPrice() != null) {
						// newgetPrice = selected_utility.getPrice();
						// }
						//
						// String existinggetQty = "";
						// String newgetQty = "";
						//
						// if (se_utility.getQty() != null) {
						// existinggetQty = se_utility.getQty();
						// }
						// if (selected_utility.getQty() != null) {
						// newgetQty = selected_utility.getQty();
						// }
						//
						// String existinggetCnCode = "";
						// String newgetCnCode = "";
						//
						// if (se_utility.getCnCode() != null) {
						// existinggetCnCode = se_utility.getCnCode();
						// }
						// if (selected_utility.getCnCode() != null) {
						// newgetCnCode = selected_utility.getCnCode();
						// }
						//
						// String existinggetImag_filename = "";
						// String newgetImag_filename = "";
						//
						// if (se_utility.getImag_filename() != null) {
						// existinggetImag_filename = se_utility
						// .getImag_filename();
						// }
						//
						// if (selected_utility.getImag_filename() != null) {
						// newgetImag_filename = selected_utility
						// .getImag_filename();
						// }
						//
						// String existinggetLocation = "";
						// String newgetLocation = "";
						//
						// if (se_utility.getLocation() != null) {
						// existinggetLocation = se_utility.getLocation();
						// }
						// if (selected_utility.getLocation() != null) {
						// newgetLocation = selected_utility.getLocation();
						// }
						//
						// if
						// (!existinggetNameororg.equalsIgnoreCase(newgetNameororg)
						// || !existingAddress.equalsIgnoreCase(newAddress)
						// || !existinggetState.equalsIgnoreCase(newgetState)
						// || !existinggetC_no.equalsIgnoreCase(newgetC_no)
						// || !existinggetCityordist
						// .equalsIgnoreCase(newgetCityordist)
						// || !existinggetCountry
						// .equalsIgnoreCase(newgetCountry)
						// || !existinggetEmail.equalsIgnoreCase(newgetEmail)
						// || !existinggetPin.equalsIgnoreCase(newgetPin)
						// || !existinggetPrice.equalsIgnoreCase(newgetPrice)
						// || !existinggetQty.equalsIgnoreCase(newgetQty)
						//
						// || !existinggetLocation
						// .equalsIgnoreCase(newgetLocation)
						// || !existinggetImag_filename
						// .equalsIgnoreCase(newgetImag_filename)
						// || !existinggetCnCode
						// .equalsIgnoreCase(newgetCnCode)
						// || !existinggetVideofilename
						// .equalsIgnoreCase(newgetVideofilename)
						// || !existinggetAudfilename
						// .equalsIgnoreCase(newgetAudiofilename)
						// || !se_utility.getProduct_name().equalsIgnoreCase(
						// selected_utility.getProduct_name())) {
						//
						// update = true;
						// saveUpdate.remove(bean.getId());
						// saveUpdate.put(bean.getId(), bean);
						// // utilityList.add(bean);
						//
						// }
					}

					if (update) {
						// saveUpdate.remove(bean.getId());
						// saveUpdate.put(bean.getId(), bean);
						update = false;

					} else {

						Toast.makeText(
								context,
								SingleInstance.mainContext.getResources()
										.getString(
												R.string.no_updates_available),
								Toast.LENGTH_SHORT).show();

					}

				}

			}
			if (utilityList.size() > 0) {
				if (WebServiceReferences.running) {
					CallDispatcher.pdialog = new ProgressDialog(context);
					showprogress();
					WebServiceReferences.webServiceClient.setUtilityServices(
							utilityList, context);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
