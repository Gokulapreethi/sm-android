package com.cg.forms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lib.model.FormsBean;
import org.lib.model.WebServiceBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.snazmed.R;
import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.PhotoZoomActivity;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.main.FormsFragment;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class FormViewer extends Activity implements OnClickListener {

	private Button btn_cancel = null;
	private Button btn_add = null;
	private Button btn_viewcall = null;
	private String offlinedeleteid = null;
	private LinearLayout lay_viewer = null;
	private ArrayList<String> list = null;
	private HashMap<String, String> dtype = new HashMap<String, String>();
	private ArrayList<String[]> rec_list = null;
	private ArrayList<String[]> rec_listAccess = new ArrayList<String[]>();
	private HashMap<String, Object> xmlmap = new HashMap<String, Object>();
	private String tbl_name = null;
	private Context cntxt = null;
	private String tbl_id = null;
	private TextView txt_title, formdescription = null;
	private HashMap<String, String> types = null;
	private String tbl_owner = null;
	private AlertDialog alert = null;
	private ProgressDialog dialog = null;
	private Handler wservice_handler = null;
	private CallDispatcher callDisp = null;
	private ArrayList<Object> bmp_container = null;
	private base64Decoder decode_task = null;
	private String syncaccess = null;
	private ArrayList<String> filenames = null;
	private Bitmap bitmap, bitmap2, icon = null;
	private String filepath = null;
	private ImageView formIcon = null;
	boolean isreject = false;
	private MediaPlayer chatplayer = new MediaPlayer();
	private Button IMRequest;
	private Button btn_notification = null;
	private String[] accessRights = null;
	private String[] formDetails = null;
	private String username = null;
	private TextView update;
	boolean isAudioPlaying = false;
	private String buddyNames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.forms_viewer);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			btn_cancel = (Button) findViewById(R.id.btn_viewrecback);
			btn_notification = (Button) findViewById(R.id.viewnotification);
			cntxt = this;

			IMRequest = (Button) findViewById(R.id.viewim);

			IMRequest.setVisibility(View.GONE);
			btn_cancel.setOnClickListener(this);
			btn_add = (Button) findViewById(R.id.btn_viewaddanother);
			txt_title = (TextView) findViewById(R.id.tv_viewrectilte);
			txt_title.setEllipsize(TruncateAt.END);
			txt_title.setMaxLines(1);
			btn_viewcall = (Button) findViewById(R.id.btn_viewcall);
			btn_viewcall.setVisibility(View.GONE);
			btn_add.setOnClickListener(this);
			btn_viewcall.setOnClickListener(this);
			formIcon = (ImageView) findViewById(R.id.formicon_img);
			formdescription = (TextView) findViewById(R.id.tv_frmdescription);
			lay_viewer = (LinearLayout) findViewById(R.id.llayout_viewholder);
			filenames = new ArrayList<String>();
			tbl_name = (String) getIntent().getStringExtra("name");
			tbl_id = (String) getIntent().getStringExtra("id");
			update = (TextView) findViewById(R.id.tv_update);
			update.setVisibility(View.GONE);
			txt_title.setText(tbl_name);
			types = (HashMap<String, String>) getIntent().getSerializableExtra(
					"types");
			tbl_owner = getIntent().getStringExtra("owner");

			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int noScrHeight = displaymetrics.heightPixels;
			int noScrWidth = displaymetrics.widthPixels;

			if (WebServiceReferences.callDispatch.containsKey("calldisp"))
				callDisp = (CallDispatcher) WebServiceReferences.callDispatch
						.get("calldisp");
			else
				callDisp = new CallDispatcher(cntxt);

			callDisp.setNoScrHeight(noScrHeight);
			callDisp.setNoScrWidth(noScrWidth);

			displaymetrics = null;
			if (tbl_owner.equalsIgnoreCase(CallDispatcher.LoginUser)) {
				btn_notification.setVisibility(View.VISIBLE);

			} else {
				btn_notification.setVisibility(View.GONE);

			}
			btn_notification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					callDisp.getdbHeler(cntxt).getColumnTypes(
							tbl_name + "_" + tbl_id);
					rec_listAccess = callDisp.getdbHeler(cntxt)
							.getRecordsofSettingtbl(tbl_id);
					if (chatplayer != null) {
						chatplayer.stop();
						chatplayer.reset();
					}
					if (rec_listAccess.size() == 0) {

						Intent viewer_intent = new Intent(FormViewer.this,
								AccessAndSync.class);
						viewer_intent.putExtra("id", tbl_id);
						viewer_intent.putExtra("owner", tbl_owner);
						startActivity(viewer_intent);

					} else {

						Intent viewer_intent = new Intent(FormViewer.this,
								FormPermissionViewer.class);
						viewer_intent.putExtra("name", tbl_name);
						viewer_intent.putExtra("id", tbl_id);
						viewer_intent.putExtra("types", dtype);
						viewer_intent.putExtra("owner", tbl_owner);
						startActivity(viewer_intent);

					}

				}
			});

			formDetails = callDisp.getdbHeler(cntxt).getFormDetailsRecords(
					tbl_id);
			if (formDetails[0].length() > 0) {

				setIconImage(formDetails[0]);
			}
			if (formDetails[1].length() > 0) {

				formdescription.setText(formDetails[1]);
			} else {

				formdescription.setText("Description");

			}

			callDisp.getdbHeler(cntxt).getColumnofAcess(tbl_owner, tbl_id);

			if (CallDispatcher.LoginUser != null)
				username = CallDispatcher.LoginUser;
			else {
				SharedPreferences pref = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				username = pref.getString("uname", "");
				btn_viewcall.setVisibility(View.GONE);
			}

			cntxt = this;

			WebServiceReferences.contextTable.put("frmviewer", cntxt);
			list = callDisp.getdbHeler(cntxt).getColumnNames(
					tbl_name + "_" + tbl_id);
			dtype = callDisp.getdbHeler(cntxt).getColumnTypes(
					tbl_name + "_" + tbl_id);
			String[] values = callDisp.getdbHeler(cntxt)
					.getColumnofSync(tbl_id);
			syncaccess = values[1];
			accessRights = callDisp.getdbHeler(cntxt).getAccessRights(tbl_id,
					username);

			if (accessRights[0].equalsIgnoreCase("A05")) {
				rec_list = callDisp.getdbHeler(cntxt)
						.getRecordsofreadAndAddOwn(tbl_name + "_" + tbl_id,
								types);
			} else {
				rec_list = callDisp.getdbHeler(cntxt).getRecordsofFormtbl(
						tbl_name + "_" + tbl_id, types);

			}

			bmp_container = new ArrayList<Object>();
			wservice_handler = new Handler();

			loadRecords();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void setIconImage(String name) {
		File extStore = Environment.getExternalStorageDirectory();
		File myFile = new File(extStore.getAbsolutePath() + "/COMMedia/" + name);

		if (!myFile.exists()) {

			downloadConfiguredNote(name);

		} else {

			setImage(name);
		}

	}

	private void setImage(String name) {

		String blob_path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/COMMedia/" + name;
		if (name.startsWith("FormIcon_")) {

			icon = callDisp.ResizeImage(blob_path);
			if (icon != null) {
				icon = Bitmap.createScaledBitmap(icon, 200, 150, false);
				formIcon.setImageBitmap(icon);
			} else {
				icon = BitmapFactory.decodeResource(getResources(),
						R.drawable.form_icon);
				formIcon.setImageBitmap(icon);

			}
		}

	}

	String[] rec = null;

	@SuppressWarnings("unchecked")
	public void loadRecords() {

		try {
			lay_viewer.removeAllViews();

			RelativeLayout.LayoutParams r_params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			r_params.topMargin = 10;
			LayoutParams iv_params = new LayoutParams(96, 96);
			LayoutInflater layoutInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < rec_list.size(); i++) {

				final String[] records = rec_list.get(i);
				rec = rec_list.get(i);

				RelativeLayout relayout_parent = (RelativeLayout) layoutInflater
						.inflate(R.layout.from_viewerrow, null);
				relayout_parent.setId(Integer
						.parseInt(records[records.length - 1]));
				LinearLayout rec_layout = (LinearLayout) relayout_parent
						.findViewById(R.id.llay_rwcontainer);

				ImageView iv_delete = (ImageView) relayout_parent
						.findViewById(R.id.iview_delrw);
				iv_delete.setBackgroundResource(R.drawable.row_delete);
				r_params.addRule(RelativeLayout.LEFT_OF, iv_delete.getId());
				rec_layout.setLayoutParams(r_params);
				rec_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						accessRights = callDisp.getdbHeler(cntxt)
								.getAccessRights(tbl_id, username);

						if (accessRights != null) {

							for (int idx = 0; idx < list.size(); idx++) {
								if (list.get(idx).equalsIgnoreCase("uuid")) {
									if ((accessRights[0].toString()
											.equalsIgnoreCase("A05") && username
											.equalsIgnoreCase(records[idx]))
											|| (accessRights[0].toString()
													.equalsIgnoreCase("A04"))) {
										if (!chatplayer.isPlaying()) {

											RelativeLayout rlayout = (RelativeLayout) v
													.getParent();
											String id = Integer
													.toString(rlayout.getId());

											ArrayList<String> creator_list = (ArrayList<String>) list
													.clone();
											creator_list.remove("uuid");
											creator_list.remove("euuid");
											creator_list.remove("uudate");
											creator_list.remove("recorddate");
											creator_list.remove("status");

											Intent rec_intent = new Intent(
													cntxt,
													FormRecordsCreators.class);
											rec_intent.putExtra("update", true);
											rec_intent.putExtra(
													"addwithsipcall", false);
											rec_intent
													.putExtra("call_list", "");

											rec_intent.putExtra("list",
													creator_list);
											rec_intent.putExtra("recordlist",
													records);
											rec_intent.putExtra("updateformid",
													tbl_id);
											rec_intent.putExtra(
													"updatetableid", id);
											rec_intent.putExtra("title",
													tbl_name);
											rec_intent.putExtra("types", types);
											startActivity(rec_intent);

										} else {

											Toast.makeText(
													cntxt,
													SingleInstance.mainContext
															.getResources()
															.getString(
																	R.string.audio_is_playing_kindly_stop_that_first),
													1).show();
										}

									} else {
										Toast.makeText(
												cntxt,
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.no_permission_to_edit_the_records),
												1).show();

									}
								}
							}

						} else {
							Toast.makeText(
									cntxt,
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.no_permission_to_edit_the_records),
									1).show();
						}

					}
				});

				iv_delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (!CallDispatcher.isWifiClosed) {
							String[] accessSetting = callDisp.getdbHeler(cntxt)
									.getAccessRights(tbl_id, username);

							RelativeLayout rlayout = (RelativeLayout) v
									.getParent();
							String id = Integer.toString(rlayout.getId());
							String[] params = { username, tbl_id, id };

							offlinedeleteid = id;
							for (int idx = 0; idx < list.size(); idx++) {
								if (list.get(idx).equalsIgnoreCase("uuid")) {
									if ((accessSetting[0].toString()
											.equalsIgnoreCase("A05") && username
											.equalsIgnoreCase(records[idx]))
											|| (accessSetting[0].toString()
													.equalsIgnoreCase("A04"))) {
										showDeleteAlert(params);
									} else {
										Toast.makeText(
												cntxt,
												SingleInstance.mainContext
														.getResources()
														.getString(
																R.string.not_have_permission_to_delete),
												Toast.LENGTH_LONG).show();
									}
								}
							}

						}

						else {
							ShowError(
									SingleInstance.mainContext.getResources()
											.getString(R.string.network_err),
									SingleInstance.mainContext
											.getResources()
											.getString(
													R.string.network_unreachable));
						}

					}
				});

				if (list.size() == records.length - 1) {

					for (int idx = 0; idx < list.size(); idx++) {
						if (list.get(idx).contains("blob_")) {

							LinearLayout blob_layout = new LinearLayout(this);
							blob_layout.setOrientation(LinearLayout.HORIZONTAL);
							TextView tview = new TextView(this);
							tview.setText(list.get(idx).replace("blob_", "")
									+ " : ");
							tview.setTextColor(Color.BLACK);
							tview.setTextSize(13);
							tview.setPadding(10, 10, 0, 10);
							blob_layout.addView(tview);

							final String filename = records[idx];
							filenames.add(filename);
							final ImageView i_view = new ImageView(cntxt);
							final ImageView i_view2 = new ImageView(cntxt);
							i_view.setPadding(10, 10, 0, 10);
							i_view.setLayoutParams(iv_params);
							i_view2.setPadding(10, 10, 0, 10);
							i_view2.setLayoutParams(iv_params);
							i_view2.setVisibility(View.GONE);
							bitmap = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_play);
							i_view2.setImageBitmap(bitmap);
							bitmap2 = BitmapFactory.decodeResource(
									getResources(), R.drawable.v_stop);
							i_view2.setImageBitmap(bitmap2);

							if (records[idx].length() > 0) {
								ArrayList<Object> list = new ArrayList<Object>();
								list.add(records[idx]);
								list.add(i_view);
								list.add(i_view2);
								decode_task = new base64Decoder();
								decode_task.execute(list);
								blob_layout.addView(i_view);
								blob_layout.addView(i_view2);
								rec_layout.addView(blob_layout);

							} else {

								blob_layout.addView(i_view);
								blob_layout.addView(i_view2);
								rec_layout.addView(blob_layout);
								blob_layout.setVisibility(View.VISIBLE);
							}
							i_view2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									if (chatplayer != null) {
										chatplayer.stop();
										chatplayer.reset();
										isAudioPlaying = false;

										i_view.setVisibility(View.VISIBLE);
										i_view2.setVisibility(View.GONE);
									}

								}
							});
							i_view.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									if (!filename.contains("MPD_")
											&& !filename
													.contains("Instruction_image")) {

										filepath = "/sdcard/COMMedia/"
												+ filename;

										if (filename.contains("MVD_")
												&& !filename.contains(".mp4")) {

											filepath = "/sdcard/COMMedia/"
													+ filename + ".mp4";

										}
										final File vfileCheck = new File(
												filepath);
										if (vfileCheck.exists()) {
											if (filename.contains("MAD_")) {
												if (!isAudioPlaying) {

													chatplayer
															.setLooping(false);

													try {
														chatplayer
																.setDataSource(filepath);
														isAudioPlaying = true;
														chatplayer.prepare();
														chatplayer.start();
														i_view.setVisibility(View.GONE);
														i_view2.setVisibility(View.VISIBLE);

													} catch (IllegalArgumentException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);

														e.printStackTrace();
													} catch (IllegalStateException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();

													} catch (IOException e) {
														if (AppReference.isWriteInFile)
															AppReference.logger.error(
																	e.getMessage(),
																	e);
														e.printStackTrace();
													}

													chatplayer
															.setOnCompletionListener(new OnCompletionListener() {

																@Override
																public void onCompletion(
																		MediaPlayer mp) {

																	try {
																		chatplayer
																				.reset();
																		i_view.setVisibility(View.VISIBLE);
																		i_view2.setVisibility(View.GONE);
																		isAudioPlaying = false;

																	} catch (IllegalArgumentException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	} catch (IllegalStateException e) {
																		if (AppReference.isWriteInFile)
																			AppReference.logger
																					.error(e.getMessage(),
																							e);
																		e.printStackTrace();
																	}

																}
															});
												} else {

													Toast.makeText(
															cntxt,
															SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.audio_is_playing_kindly_stop_that_first),
															Toast.LENGTH_LONG)
															.show();
												}

											} else {
												if (!isAudioPlaying) {

													Intent intentVPlayer = new Intent(
															cntxt,
															VideoPlayer.class);
													intentVPlayer.putExtra(
															"File_Path",
															filepath);
													intentVPlayer.putExtra(
															"Player_Type",
															"Video Player");
													startActivity(intentVPlayer);
												} else {

													Toast.makeText(
															cntxt,
															SingleInstance.mainContext
																	.getResources()
																	.getString(
																			R.string.audio_is_playing_kindly_stop_that_first),
															Toast.LENGTH_LONG)
															.show();
												}
											}
										} else {
											Toast.makeText(cntxt,
													"File Downloading", 1)
													.show();

											downloadConfiguredNote(filename);

										}

									} else if (filename.contains("MPD_")
											|| filename
													.contains("Instruction_image")) {
										try {
											Intent in = new Intent(cntxt,
													PhotoZoomActivity.class);

											in.putExtra("type", false);

											filepath = Environment
													.getExternalStorageDirectory()
													+ "/COMMedia/" + filename;
											in.putExtra("Photo_path", filepath);
											in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(in);
										} catch (Exception e) {
											if (AppReference.isWriteInFile)
												AppReference.logger.error(
														e.getMessage(), e);
										}
									}
								}
							});

						} else {
							if (list.get(idx).equalsIgnoreCase("uuid")) {
								TextView tview = new TextView(this);
								tview.setText("Created By : " + records[idx]);
								tview.setVisibility(View.VISIBLE);

								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							} else if (list.get(idx).equalsIgnoreCase("uudate")) {
								TextView tview = new TextView(this);
								tview.setText("Created Date : " + records[idx]);
								tview.setVisibility(View.GONE);

								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							} else if (list.get(idx).equalsIgnoreCase(
									"recorddate")) {
								TextView tview = new TextView(this);
								tview.setText("Created Date : " + records[idx]);
								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setVisibility(View.GONE);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							} else if (list.get(idx).equalsIgnoreCase("euuid")) {
								TextView tview = new TextView(this);
								tview.setText("Created Date : " + records[idx]);
								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setVisibility(View.GONE);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							} else if (list.get(idx).equalsIgnoreCase("status")) {
								TextView tview = new TextView(this);
								tview.setText("Created Date : " + records[idx]);
								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setVisibility(View.GONE);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							} else {
								TextView tview = new TextView(this);

								tview.setText(list.get(idx) + " : "
										+ records[idx]);

								tview.setTextColor(Color.BLACK);
								tview.setTextSize(13);
								tview.setPadding(10, 10, 0, 10);
								rec_layout.addView(tview);
							}
						}
					}
				}

				lay_viewer.addView(relayout_parent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == btn_cancel.getId()) {
			if (chatplayer != null) {
				chatplayer.stop();
				isAudioPlaying = false;
				chatplayer.reset();
			}
			finish();
		}

		else if (v.getId() == btn_add.getId()) {

			if (!isAudioPlaying) {
				if (!CallDispatcher.isWifiClosed) {
					if (chatplayer != null) {
						chatplayer.stop();
						chatplayer.reset();
						isAudioPlaying = false;
					}
					doAddRecords(false);

				}

				else {
					ShowError(SingleInstance.mainContext.getResources()
							.getString(R.string.network_err),
							SingleInstance.mainContext.getResources()
									.getString(R.string.network_unreachable));
				}
			} else {
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext
								.getResources()
								.getString(
										R.string.audio_is_playing_kindly_stop_that_first),
						Toast.LENGTH_LONG).show();
			}

		}

		else if (v.getId() == btn_viewcall.getId()) {

			if (chatplayer != null) {
				chatplayer.stop();
				chatplayer.reset();
				isAudioPlaying = false;
			}

			String[] accessSetting = callDisp.getdbHeler(cntxt)
					.getAccessRights(tbl_id, username);

			if (accessSetting != null) {

				if (((accessSetting[0].toString().equalsIgnoreCase("A04"))
						|| (accessSetting[0].toString().equalsIgnoreCase("A03")) || accessSetting[0]
						.toString().equalsIgnoreCase("A05"))) {
					showSingleSelectBuddy();
				} else {
					Toast.makeText(
							cntxt,
							SingleInstance.mainContext
									.getResources()
									.getString(
											R.string.no_permission_to_add_with_call),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_permission_to_add_with_call),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void doAddRecords(boolean addwithcall) {

		accessRights = callDisp.getdbHeler(cntxt).getAccessRights(tbl_id,
				username);
		if (accessRights != null) {

			if (((accessRights[0].toString().equalsIgnoreCase("A03"))
					|| (accessRights[0].toString().equalsIgnoreCase("A04")) || (accessRights[0]
						.toString().equalsIgnoreCase("A05")))) {
				ArrayList<String> creator_list = (ArrayList<String>) list
						.clone();
				creator_list.remove("uuid");
				creator_list.remove("euuid");
				creator_list.remove("uudate");
				creator_list.remove("recorddate");
				creator_list.remove("status");
				if (addwithcall) {
					Intent rec_intent = new Intent(cntxt,
							FormRecordsCreators.class);
					rec_intent.putExtra("update", false);
					rec_intent.putExtra("addwithsipcall", addwithcall);
					// rec_intent.putExtra("call_list",
					// AppReference.process_members);

					rec_intent.putExtra("list", creator_list);
					rec_intent.putExtra("title", tbl_name);
					rec_intent.putExtra("id", tbl_id);
					rec_intent.putExtra("types", dtype);
					rec_intent.putExtra("buddyname", buddyNames);
					startActivity(rec_intent);
				} else {
					Intent rec_intent = new Intent(cntxt,
							FormRecordsCreators.class);
					rec_intent.putExtra("update", false);
					rec_intent.putExtra("addwithsipcall", addwithcall);
					rec_intent.putExtra("call_list", "");

					rec_intent.putExtra("list", creator_list);
					rec_intent.putExtra("title", tbl_name);
					rec_intent.putExtra("id", tbl_id);
					rec_intent.putExtra("types", dtype);
					startActivity(rec_intent);

				}
			} else {
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext.getResources().getString(
								R.string.no_permission_to_add_the_records),
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					cntxt,
					SingleInstance.mainContext.getResources().getString(
							R.string.no_permission_to_add_the_records),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		if (syncaccess != null) {

			if (syncaccess.contains("1")) {

				for (int i = 0; i < filenames.size(); i++) {
					String path = "/sdcard/COMMedia/" + filenames.get(i);
					File file = new File(path);
					file.delete();
				}
				WebServiceReferences.contextTable.remove("frmviewer");
				if (bmp_container.size() > 0) {
					for (Object bmp : bmp_container) {
						Bitmap b = (Bitmap) bmp;
						if (!b.isRecycled())
							b.recycle();
						b = null;

					}
				}

			}
		}

		else {

			WebServiceReferences.contextTable.remove("frmviewer");
			if (bmp_container.size() > 0) {
				for (Object bmp : bmp_container) {
					Bitmap b = (Bitmap) bmp;
					if (!b.isRecycled())
						b.recycle();
					b = null;

				}
			}
		}

		if (WebServiceReferences.contextTable.containsKey("frmviewer")) {

			WebServiceReferences.contextTable.remove("frmviewer");

		}

	}

	public void refreshList() {

		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				rec_list.clear();
				lay_viewer.removeAllViews();
				accessRights = callDisp.getdbHeler(cntxt).getAccessRights(
						tbl_id, username);
				if (accessRights[0].equalsIgnoreCase("A05")) {
					rec_list = callDisp.getdbHeler(cntxt)
							.getRecordsofreadAndAddOwn(tbl_name + "_" + tbl_id,
									types);

				} else if (accessRights[0].equalsIgnoreCase("A01")) {
					Toast.makeText(cntxt, "No Access to view this form ",
							Toast.LENGTH_LONG).show();
					finish();

					if (WebServiceReferences.contextTable
							.containsKey("frmreccreator")) {
						FormRecordsCreators frm_activity = (FormRecordsCreators) WebServiceReferences.contextTable
								.get("frmreccreator");
						frm_activity.showToast("No access to add records this");
						frm_activity.finish();
					}

				}

				else {
					rec_list = callDisp.getdbHeler(cntxt).getRecordsofFormtbl(
							tbl_name + "_" + tbl_id, types);

				}
				if (accessRights[0].equalsIgnoreCase("A02")) {

					if (WebServiceReferences.contextTable
							.containsKey("frmreccreator")) {
						FormRecordsCreators frm_activity = (FormRecordsCreators) WebServiceReferences.contextTable
								.get("frmreccreator");
						frm_activity.showToast("No access to add records");
						frm_activity.finish();
					}

				}
				if (bmp_container.size() > 0) {
					for (Object bmp : bmp_container) {
						Bitmap b = (Bitmap) bmp;
						if (!b.isRecycled())
							b.recycle();
						b = null;

					}
					bmp_container.clear();
				}

				loadRecords();
			}
		});
	}

	public void notifyFileDownloadError() {

		wservice_handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext.getResources().getString(
								R.string.downloading_failed), Toast.LENGTH_LONG)
						.show();

			}
		});
	}

	void showSingleSelectBuddy() {

		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.create();

			final CharSequence[] choiceList = callDisp.getOnlineBuddys();

			int selected = -1; // does not select anything

			builder.setSingleChoiceItems(choiceList, selected,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// AppReference.process_members.clear();
							// String To = choiceList[which].toString();
							// CallerBean callerBean = new CallerBean();
							// callerBean.setUserName(To);
							// callerBean.setToNnumber(To);
							// callerBean.setPresense("Connecting");
							// callerBean.setHold(0);
							// callerBean.setMute(0);
							// callerBean.setCall_id(-1);
							// AppReference.process_members.add(callerBean);
							buddyNames = choiceList[which].toString();

						}
					});

			builder.setPositiveButton(SingleInstance.mainContext.getResources()
					.getString(R.string.ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (buddyNames != null) {

								doAddRecords(true);
								alert.dismiss();

							} else {

								Toast.makeText(
										cntxt,
										SingleInstance.mainContext
												.getResources().getString(
														R.string.select_buddy),
										Toast.LENGTH_LONG).show();
							}

						}
					});
			alert = builder.create();
			if (choiceList != null) {
				if (choiceList.length != 0) {
					alert.show();
				} else {
					Toast.makeText(
							cntxt,
							SingleInstance.mainContext.getResources()
									.getString(R.string.sorry_no_online_users),
							Toast.LENGTH_LONG).show();

				}
			}

		} catch (Exception e) {
			if (AppReference.isWriteInFile)
				AppReference.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void showConfirmation(final String owner, final String id,
			final String formtime, final boolean isDeleted) {

		if (tbl_id.equalsIgnoreCase(id)) {

			if (!isDeleted) {
				if (WebServiceReferences.running) {

					WebServiceReferences.webServiceClient.Getcontent(owner, id,
							formtime, SingleInstance.mainContext);
				} else {
					callDisp.startWebService(
							getResources().getString(R.string.service_url),
							"80");
					WebServiceReferences.webServiceClient.Getcontent(owner, id,
							formtime, SingleInstance.mainContext);
				}
				refreshList();
			} else {

				rec_list.clear();
				lay_viewer.removeAllViews();
				rec_list = callDisp.getdbHeler(cntxt).getRecordsofFormtbl(
						tbl_name + "_" + tbl_id, types);
				if (bmp_container.size() > 0) {
					for (Object bmp : bmp_container) {
						Bitmap b = (Bitmap) bmp;
						if (!b.isRecycled())
							b.recycle();
						b = null;

					}
					bmp_container.clear();
				}
				loadRecords();
				cancelDialog(true);
			}

		}
	}

	public boolean isShowingCurrentForm(String frm_id) {
		if (tbl_id.equals(frm_id))
			return true;
		else
			return false;
	}

	private void showprogress() {

		dialog = new ProgressDialog(cntxt);
		dialog.setCancelable(false);
		dialog.setMessage("Progress ...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setProgress(0);
		dialog.setMax(100);
		dialog.show();
	}

	public void cancelDialog(boolean flag) {

		if (flag) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;

			}
		} else {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
				Toast.makeText(
						cntxt,
						SingleInstance.mainContext.getResources().getString(
								R.string.unable_to_connect_server),
						Toast.LENGTH_LONG).show();
			}

		}
	}

	public void notifyWebServiceResponse(final Object obj) {
		wservice_handler.post(new Runnable() {

			@Override
			public void run() {

				if (obj instanceof FormsBean) {

					cancelDialog(true);
					rec_list.clear();
					lay_viewer.removeAllViews();
					rec_list = callDisp.getdbHeler(cntxt).getRecordsofFormtbl(
							"[" + tbl_name + "_" + tbl_id + "]", types);
					if (bmp_container.size() > 0) {
						for (Object bmp : bmp_container) {
							Bitmap b = (Bitmap) bmp;
							if (!b.isRecycled())
								b.recycle();
							b = null;

						}
						bmp_container.clear();
					}
					loadRecords();
				} else if (obj instanceof WebServiceBean) {
					cancelDialog(true);
					WebServiceBean service_bean = (WebServiceBean) obj;
					Toast.makeText(cntxt, service_bean.getText(),
							Toast.LENGTH_LONG);
					finish();
				}
			}
		});
	}

	public String getId() {
		return this.tbl_id;
	}

	public class base64Decoder extends
			AsyncTask<ArrayList<Object>, Void, Bitmap> {
		ImageView iview;

		@Override
		protected Bitmap doInBackground(ArrayList<Object>... params) {

			ArrayList<Object> list = params[0];
			String bmp_array = (String) list.get(0);
			iview = (ImageView) list.get(1);
			String blob_path = "/sdcard/COMMedia/" + bmp_array;

			if (blob_path.contains("MPD_")) {
				bitmap = callDisp.ResizeImage(blob_path);
				if (bitmap != null)
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);

			}

			else if (blob_path.contains("MVD_"))

			{

				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.v_play);

			} else if (blob_path.contains("MAD_"))

			{

				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.v_play);

			} else if (blob_path.contains("Instruction_image")) {
				bitmap = callDisp.ResizeImage(blob_path);
				if (bitmap != null)
					bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150, false);

			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {

			if (result != null)
				iview.setImageBitmap(result);
			super.onPostExecute(result);
		}

	}

	private void downloadConfiguredNote(String path) {

		if (path != null) {
			if (username != null) {
				callDisp.downloadOfflineresponse(path, "", "forms", "");

			}
		}
	}

	private void showDeleteAlert(final String[] args) {
		AlertDialog.Builder buider = new AlertDialog.Builder(cntxt);
		buider.setMessage(
				SingleInstance.mainContext.getResources().getString(
						R.string.are_you_sure_you_want_to_delete))
				.setPositiveButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (!CallDispatcher.isWifiClosed) {

									showprogress();
									if (username != null) {

										String strUpdateCount = "update " + "["
												+ tbl_name + "_" + tbl_id + "]"
												+ " set status='" + "3"
												+ "' where tableid='"
												+ offlinedeleteid + "'";

										callDisp.getdbHeler(cntxt)
												.ExecuteQuery(strUpdateCount);
										{

											String strQuery = "update formslookup set status='"
													+ "0"
													+ "' where tableid="
													+ tbl_id;
											callDisp.getdbHeler(cntxt)
													.ExecuteQuery(strQuery);
											{
												if (WebServiceReferences.running) {
													WebServiceReferences.webServiceClient
															.deleteFormRecords(
																	args, cntxt);
												} else {
													callDisp.startWebService(
															getResources()
																	.getString(
																			R.string.service_url),
															"80");
													WebServiceReferences.webServiceClient
															.deleteFormRecords(
																	args, cntxt);
												}

											}

										}

									} else {

										String strUpdateCount = "update " + "["
												+ tbl_name + "_" + tbl_id + "]"
												+ " set status='" + "3"
												+ "' where tableid='"
												+ offlinedeleteid + "'";

										callDisp.getdbHeler(cntxt)
												.ExecuteQuery(strUpdateCount);
										{

											cancelDialog(true);
											String strQuery = "update formslookup set status='"
													+ "0"
													+ "' where tableid="
													+ tbl_id;
											callDisp.getdbHeler(cntxt)
													.ExecuteQuery(strQuery);
											{
												finish();

											}

										}

									}

								} else {
									ShowError(
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.network_err),
											SingleInstance.mainContext
													.getResources()
													.getString(
															R.string.network_unreachable));

								}
							}
						})
				.setNegativeButton(
						SingleInstance.mainContext.getResources().getString(
								R.string.no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
							}
						});
		AlertDialog alert = buider.create();
		alert.show();
	}

	public void ShowError(String Title, String Message) {
		AlertDialog confirmation = new AlertDialog.Builder(this).create();
		confirmation.setTitle(Title);
		confirmation.setMessage(Message);
		confirmation.setCancelable(true);
		confirmation.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		confirmation.show();
	}

	public void notifyFormDeleteRecords(final Object obj) {

		wservice_handler.post(new Runnable() {

			@Override
			public void run() {

				cancelDialog(true);
				if (obj instanceof FormsBean) {
					FormsBean bean = (FormsBean) obj;

					if (callDisp.getdbHeler(cntxt).isFormtableExists(
							bean.getFormName() + "_" + bean.getFormId())) {
						String tbl_name = "[" + bean.getFormName() + "_"
								+ bean.getFormId() + "]";
						String del_row = "delete from " + tbl_name
								+ " where tableid='"
								+ bean.getDeletedRecordId() + "'";
						if (callDisp.getdbHeler(cntxt).ExecuteQuery(del_row)) {
							refreshList();

							String strUpdateCount = "update formslookup set rowcount='"
									+ bean.getcount()
									+ "',status='"
									+ "1"
									+ "' where tableid='" + tbl_id + "'";
							callDisp.getdbHeler(cntxt).ExecuteQuery(
									strUpdateCount);

						}
					} else {
						refreshList();
					}

					if (SingleInstance.contextTable.containsKey("forms")) {
						FormsFragment quickActionFragment = FormsFragment
								.newInstance(cntxt);

						quickActionFragment.populateLists();

					}

					// if (WebServiceReferences.contextTable
					// .containsKey("appsview")) {
					// AppsView frm_activity = (AppsView)
					// WebServiceReferences.contextTable
					// .get("appsview");
					// frm_activity.recreate();
					// }

				} else if (obj instanceof WebServiceBean) {
					WebServiceBean bn = (WebServiceBean) obj;
					Toast.makeText(cntxt, bn.getText(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
        AppMainActivity.inActivity = this;
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
