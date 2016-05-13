package com.cg.profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.lib.model.FieldTemplateBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.cg.commonclass.UpperCaseParse;
import com.cg.commonclass.WebServiceReferences;
import com.cg.commongui.MultimediaUtils;
import com.cg.commongui.PhotoZoomActivity;
import com.main.AppMainActivity;
import com.main.CreateProfileFragment;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.VideoPlayer;

public class ViewProfiles extends Activity {

	private Context context = null;

	private Button btn_notification = null;

	private Button IMRequest = null;

	private Button btn_cancel = null;

	private TextView title = null;

	private Handler viewHandler = new Handler();

	public LinearLayout records_container = null;

	private String buddyname = null;

	private Bitmap bitmap = null;

	private CallDispatcher callDisp = null;

	private base64Decoder decode_task = null;

	private Vector<FieldTemplateBean> OtherDetails = new Vector<FieldTemplateBean>();

	private Button btn_viewEdit;

	// private SlideMenu slidemenu = null;

	private MediaPlayer chatplayer = null;

	private boolean isPlaying = false;

	private Button btn_share = null;

//	SipQueue sipQueue;

//	SipCommunicator sipCommunicator;

	private RelativeLayout titleLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			requestWindowFeature(Window.FEATURE_NO_TITLE);

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
			setContentView(R.layout.profiletitle);
			context = this;
			WebServiceReferences.contextTable.put("IM", this);
			WebServiceReferences.contextTable.put("viewprofileactivity",
					context);
			chatplayer = new MediaPlayer();
			CallDispatcher.pdialog = new ProgressDialog(context);
			titleLayout = (RelativeLayout) findViewById(R.id.rlay_tilte);
			titleLayout.setVisibility(View.VISIBLE);
			buddyname = getIntent().getStringExtra("buddyname");
			title = (TextView) findViewById(R.id.heading);
			btn_share = (Button) findViewById(R.id.share_btn);
			if (!buddyname.equalsIgnoreCase(CallDispatcher.LoginUser))
				btn_share.setVisibility(View.GONE);
			btn_notification = (Button) findViewById(R.id.appnotificarion);
			btn_notification.setVisibility(View.GONE);
			btn_notification.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					ProgressDialog dialog = new ProgressDialog(context);
					callDisp.showprogress(dialog, context);

				}
			});
			btn_viewEdit = (Button) findViewById(R.id.edit);
			btn_viewEdit.setTag("view");
			btn_viewEdit.setText(SingleInstance.mainContext.getResources().getString(R.string.edit));
			if (buddyname.equalsIgnoreCase(CallDispatcher.LoginUser)) {
				btn_viewEdit.setVisibility(View.VISIBLE);
				title.setText(SingleInstance.mainContext.getResources().getString(R.string.my_profile));
			} else {
				btn_viewEdit.setVisibility(View.GONE);
				title.setText(buddyname + "'s Profile");
			}
			btn_viewEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!isPlaying) {
						
						CreateProfileFragment createProfileFragment = CreateProfileFragment
								.newInstance(context);
						finish();
					} else {
						showToast("Please stop audio before editing profile");
					}
				}
			});

			IMRequest = (Button) findViewById(R.id.appsim);
			IMRequest.setVisibility(View.GONE);

			IMRequest.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					callDisp.openReceivedIm(v, context);
				}
			});

			btn_cancel = (Button) findViewById(R.id.btn_Settings);

			btn_cancel.setBackgroundResource(R.drawable.ic_action_back);

			btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						finish();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			btn_share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuffer buffer = new StringBuffer();
					Vector<FieldTemplateBean> profileFieldList = callDisp
							.getdbHeler(context).getProfileFields();
					if (profileFieldList != null && profileFieldList.size() > 0) {
						for (FieldTemplateBean fieldtemplate : profileFieldList) {
							if (fieldtemplate.getFiledvalue() != null
									&& fieldtemplate.getFiledvalue().length() > 0
									&& !fieldtemplate.getFieldType()
											.equalsIgnoreCase("multimedia")
									&& !fieldtemplate.getFieldType()
											.equalsIgnoreCase("photo")
									&& !fieldtemplate.getFieldType()
											.equalsIgnoreCase("video")
									&& !fieldtemplate.getFieldType()
											.equalsIgnoreCase("audio")) {
								String fieldname = fieldtemplate.getFieldName();
								String fieldValue = fieldtemplate
										.getFiledvalue();
								buffer.append(fieldname + " : " + fieldValue);
								buffer.append("\n\n");
							}

						}
					}
					if (buffer.toString().length() > 0) {
						Intent shareIntent = new Intent(context,
								InviteProfile.class);
						shareIntent.putExtra("profilevalues", buffer.toString());
						startActivity(shareIntent);
					} else
						showToast("No profile text values to share profile");
				}
			});

			WebServiceReferences.contextTable.put("viewprofile", context);
			initView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("viewprofile", "===> " + e.getMessage());
			e.printStackTrace();
			SingleInstance.printLog("View Profile", " Oncreate", "ERROR", e);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		try {
			super.onDestroy();
			WebServiceReferences.contextTable.remove("viewprofile");
			WebServiceReferences.contextTable.remove("viewprofileactivity");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void notifyfileDownloaded(final String fieldid,
			final String ownername) {
		viewHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (buddyname.equals(ownername)) {
					for (int i = 0; i < records_container.getChildCount(); i++) {
						View view = (View) records_container.getChildAt(i);
						String field_id = (String) view.getTag();
						if (fieldid.equals(field_id)) {
							FieldTemplateBean bean = (FieldTemplateBean) OtherDetails
									.get(i);
							initView();
							break;
						}
					}
				}
			}
		});

	}

	public void refreshView(FieldTemplateBean fields, int pos) {

		String groupname = "";
		LayoutParams iv_params = new LayoutParams(96, 96);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout layout = (LinearLayout) layoutInflater.inflate(
				R.layout.viewprofiles, null);
		layout.setTag(fields.getFieldId());

		TextView tv1 = (TextView) layout.findViewById(R.id.Heading);
		tv1.setText(UpperCaseParse.captionTextForUpperCaseString(fields
				.getGroupName()));
		if (groupname.trim().length() == 0) {
			tv1.setVisibility(View.VISIBLE);
			groupname = fields.getGroupName();
		} else {
			if (!groupname.equals(fields.getGroupName())) {
				tv1.setVisibility(View.VISIBLE);
				groupname = fields.getGroupName();
			} else
				tv1.setVisibility(View.GONE);

		}
		LinearLayout viewProf = (LinearLayout) layout
				.findViewById(R.id.viewprof_lay);
		String fieldname = fields.getFieldName();
		String type = fields.getFieldType();
		final String value = fields.getFiledvalue();

		if (type.equalsIgnoreCase("multimedia")
				|| type.equalsIgnoreCase("audio")
				|| type.equalsIgnoreCase("video")
				|| type.equalsIgnoreCase("photo")) {
			callDisp.setProfilePicture(value, R.drawable.videonotesnew);
			LinearLayout blob_layout = new LinearLayout(this);
			blob_layout.setOrientation(LinearLayout.VERTICAL);
			blob_layout.setGravity(Gravity.LEFT);
			View view = new View(context);
			view.setPadding(0, 10, 0, 0);
			view.setLayoutParams(iv_params);
			view.setBackgroundColor(Color.parseColor("#C0C0C0"));
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			params1.setMargins(0, 0, 0, 0);
			blob_layout.addView(view, params1);
			final ImageView i_view = new ImageView(context);
			i_view.setPadding(0, 10, 0, 0);
			i_view.setLayoutParams(iv_params);
			LayoutParams params = new LayoutParams(64, 64);

			params.setMargins(20, 5, 0, 0);
			blob_layout.addView(i_view, params);

			TextView tview = new TextView(this);
			tview.setText(UpperCaseParse
					.captionTextForUpperCaseString(fieldname));
			tview.setTextColor(Color.LTGRAY);
			tview.setTextSize(13);
			tview.setPadding(0, 10, 0, 0);
			tview.setTypeface(null, Typeface.BOLD_ITALIC);
			LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			params2.setMargins(20, 5, 0, 0);
			blob_layout.addView(tview, params2);
			ArrayList<Object> list1 = new ArrayList<Object>();
			list1.add(value);
			list1.add(i_view);
			if (value != null) {
				if (value.trim().length() != 0) {
					decode_task = new base64Decoder();
					decode_task.execute(list1);
				} else {
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.videonotesnew);
					i_view.setImageBitmap(bitmap);
				}
			} else {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.videonotesnew);
				i_view.setImageBitmap(bitmap);
			}
			layout.addView(blob_layout);

			chatplayer.setLooping(false);
			i_view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (value.trim().length() > 0) {
						if (!value.contains("MPD_")) {

							String filepath = "/sdcard/COMMedia/" + value;
							Log.i("a", "this is pathhhhh" + filepath);

							if (value.contains("MVD_")
									&& !value.contains(".mp4")) {

								Log.i("a", "inside  MVD Andd mp4");
								filepath = "/sdcard/COMMedia/" + value + ".mp4";

							}
							final File vfileCheck = new File(filepath);
							if (vfileCheck.exists()) {
								if (value.contains("MAD_")) {

									if (chatplayer.isPlaying()) {
										chatplayer.stop();
										chatplayer.reset();
										bitmap = BitmapFactory.decodeResource(
												getResources(),
												R.drawable.v_play);
										i_view.setImageBitmap(bitmap);

									} else {
										try {
											chatplayer.setDataSource(filepath);
											chatplayer.prepare();
											chatplayer.start();
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.v_stop);
											i_view.setImageBitmap(bitmap);

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
										} catch (IOException e) {
											// TODO
											// Auto-generated
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
															bitmap = BitmapFactory
																	.decodeResource(
																			getResources(),
																			R.drawable.v_play);
															i_view.setImageBitmap(bitmap);

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
								} else {
									Log.i("a", "file exixts trueeee");

									try {
										Intent intentVPlayer = new Intent(
												context, VideoPlayer.class);
										// intentVPlayer.putExtra("File_Path",
										// filepath);
										// intentVPlayer.putExtra("Player_Type",
										// "Video Player");
										intentVPlayer.putExtra("video",
												filepath);

										startActivity(intentVPlayer);
									} catch (Exception e) {
										// TODO Auto-generated
										// catch block
										Log.i("viewprofile",
												"===> " + e.getMessage());
										e.printStackTrace();
									}
								}
							} else {
								Toast.makeText(context,
										"Sorry file not available", 1).show();

							}

						} else if (value.contains("MPD_")) {
							try {

								String path = Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/COMMedia/" + value;
								final File vfileCheck = new File(path);
								if (vfileCheck.exists()) {
									Intent in = new Intent(context,
											PhotoZoomActivity.class);
									in.putExtra("type", false);
									in.putExtra("Photo_path", path);
									in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(in);
								} else {
									Toast.makeText(context, "File Downloading",
											1).show();
								}
							} catch (Exception e) {
								Log.e("Error", "===>" + e.getMessage());
							}
						}
					} else
						Toast.makeText(context, "Sorry! No multimedia file",
								Toast.LENGTH_SHORT).show();
				}

			});

		} else {
			try {
				LinearLayout view = (LinearLayout) layoutInflater.inflate(
						R.layout.customview, null);
				TextView fieldnames = (TextView) view
						.findViewById(R.id.fieldname);
				TextView fieldvalue = (TextView) view
						.findViewById(R.id.fieldvalue);
				final ImageView email = (ImageView) view
						.findViewById(R.id.email);
				ImageView phone = (ImageView) view.findViewById(R.id.phone);

				email.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Intent emailIntent = new Intent(
								android.content.Intent.ACTION_SEND);

						emailIntent.setType("plain/text");

						emailIntent.putExtra(
								android.content.Intent.EXTRA_EMAIL,
								new String[] { email.getContentDescription()
										.toString() });

						emailIntent
								.putExtra(android.content.Intent.EXTRA_SUBJECT,
										"Profile");

						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"");

						startActivity(Intent.createChooser(emailIntent,
								"Send mail..."));

					}
				});

				phone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_DIAL);
						startActivity(intent);

					}
				});

				fieldnames.setText(UpperCaseParse
						.captionTextForUpperCaseString(fields.getFieldName()));
				if (fields.getFiledvalue() != null)
					fieldvalue.setText(UpperCaseParse
							.captionTextForUpperCaseString(fields
									.getFiledvalue().toString()));

				if (type.equalsIgnoreCase("phone")) {
					phone.setContentDescription(fields.getFiledvalue());

					phone.setVisibility(View.VISIBLE);
					email.setVisibility(View.GONE);

				}

				viewProf.addView(view);

				layout.setId(records_container.getChildCount());
				Log.i("thread", "Layout id" + layout.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("viewprofile", "====> " + e.getMessage());
				e.printStackTrace();
			}
		}

		records_container.addView(layout);

	}

	@SuppressWarnings("unchecked")
	public void initView() {
		try {
			records_container = (LinearLayout) findViewById(R.id.llayout_holder);
			OtherDetails = callDisp.getdbHeler(context).getProfileFieldsValues(
					buddyname);

			if (records_container != null)
				records_container.removeAllViews();

			LayoutParams iv_params = new LayoutParams(96, 96);
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			String groupname = "";
			for (FieldTemplateBean fields : OtherDetails) {

				Log.i("PROFILELOGS",
						"HashprofileFields===>" + fields.getGroupName());
				if (fields.getFiledvalue() != null) {
					if (fields.getFiledvalue().trim().length() > 0) {
						LinearLayout layout = (LinearLayout) layoutInflater
								.inflate(R.layout.viewprofiles, null);
						layout.setTag(fields.getFieldId());

						TextView tv1 = (TextView) layout
								.findViewById(R.id.Heading);
						tv1.setText(UpperCaseParse
								.captionTextForUpperCaseString(fields
										.getGroupName()));
						if (groupname.trim().length() == 0) {
							tv1.setVisibility(View.VISIBLE);
							groupname = fields.getGroupName();
						} else {
							if (!groupname.equals(fields.getGroupName())) {
								tv1.setVisibility(View.VISIBLE);
								groupname = fields.getGroupName();
							} else
								tv1.setVisibility(View.GONE);

						}
						LinearLayout viewProf = (LinearLayout) layout
								.findViewById(R.id.viewprof_lay);
						final String fieldname = fields.getFieldName();
						String type = fields.getFieldType();
						final String value = fields.getFiledvalue();

						if (type.equalsIgnoreCase("multimedia")
								|| type.equalsIgnoreCase("audio")
								|| type.equalsIgnoreCase("video")
								|| type.equalsIgnoreCase("photo")) {
							bitmap = callDisp.setProfilePicture(value,
									R.drawable.videonotesnew);
							LinearLayout blob_layout = new LinearLayout(this);
							blob_layout.setOrientation(LinearLayout.VERTICAL);
							blob_layout.setGravity(Gravity.LEFT);
							View view = new View(context);
							view.setPadding(0, 10, 0, 0);
							view.setLayoutParams(iv_params);
							view.setBackgroundColor(Color.parseColor("#C0C0C0"));
							LayoutParams params1 = new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);

							params1.setMargins(0, 0, 0, 0);
							blob_layout.addView(view, params1);
							final ImageView i_view = new ImageView(context);
							i_view.setTag(0);
							i_view.setPadding(0, 10, 0, 0);
							i_view.setLayoutParams(iv_params);
							LayoutParams params = new LayoutParams(64, 64);

							params.setMargins(20, 5, 0, 0);
							blob_layout.addView(i_view, params);

							TextView tview = new TextView(this);
							tview.setText(UpperCaseParse
									.captionTextForUpperCaseString(fieldname));
							tview.setTextColor(Color.LTGRAY);
							tview.setTextSize(13);
							tview.setPadding(0, 10, 0, 0);
							tview.setTypeface(null, Typeface.BOLD_ITALIC);
							LayoutParams params2 = new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);

							params2.setMargins(20, 5, 0, 0);
							blob_layout.addView(tview, params2);
							ArrayList<Object> list1 = new ArrayList<Object>();
							list1.add(value);
							list1.add(i_view);
							if (value != null) {
								if (value.trim().length() != 0) {
									decode_task = new base64Decoder();
									decode_task.execute(list1);
								} else {
									bitmap = BitmapFactory.decodeResource(
											getResources(),
											R.drawable.videonotesnew);
									i_view.setImageBitmap(bitmap);
								}
							} else {
								bitmap = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.videonotesnew);
								i_view.setImageBitmap(bitmap);
							}
							layout.addView(blob_layout);

							chatplayer.setLooping(false);
							i_view.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final ImageView iv_play = (ImageView) v;
									int tag = (Integer) v.getTag();
									SingleInstance.printLog("View Profile",
											" Field name : " + fieldname
													+ " Multimeda File Name : "
													+ value, "DEBUG", null);

									if (value.trim().length() > 0) {
										String filepath = callDisp
												.containsLocalPath(value);
										if (!value.contains("MPD_")) {
											if (filepath.contains("MVD_")
													&& !filepath
															.contains(".mp4")) {
												Log.i("a",
														"inside  MVD Andd mp4");
												filepath = filepath + ".mp4";
												SingleInstance.printLog(
														"View Profile",
														" Append .mp4 if not contains in video file "
																+ filepath,
														"DEBUG", null);

											}
											final File vfileCheck = new File(
													filepath);
											if (vfileCheck.exists()) {
												if (filepath.contains("MAD_")) {
													SingleInstance.printLog(
															"View Profile",
															"Audio File"
																	+ filepath,
															"DEBUG", null);
													Intent intent = new Intent(
															context,
															MultimediaUtils.class);
													intent.putExtra("filePath",
															filepath);
													intent.putExtra(
															"requestCode",
															"audio");
													intent.putExtra("action",
															"audio");
													intent.putExtra(
															"createOrOpen",
															"open");
													startActivity(intent);

												} else {
													SingleInstance.printLog(
															"View Profile",
															"Video File"
																	+ filepath,
															"DEBUG", null);
													try {
														Intent intentVPlayer = new Intent(
																context,
																VideoPlayer.class);
														// intentVPlayer.putExtra(
														// "File_Path",
														// filepath);
														// intentVPlayer.putExtra(
														// "Player_Type",
														// "Video Player");
														intentVPlayer.putExtra(
																"video",
																filepath);

														startActivity(intentVPlayer);
													} catch (Exception e) {
														// TODO Auto-generated
														// catch block
														Log.i("viewprofile",
																"===> "
																		+ e.getMessage());
														e.printStackTrace();
													}
												}
											} else {
												Toast.makeText(
														context,
														"Sorry File not available",
														1).show();

											}

										} else if (filepath.contains("MPD_")) {
											try {
												SingleInstance.printLog(
														"View Profile",
														" Onclick on Image File"
																+ filepath,
														"DEBUG", null);
												final File vfileCheck = new File(
														filepath);
												if (vfileCheck.exists()) {
													SingleInstance
															.printLog(
																	"View Profile",
																	" Image File"
																			+ filepath
																			+ " is exists",
																	"DEBUG",
																	null);
													// Intent in = new Intent(
													// context,
													// PhotoZoomActivity.class);
													// in.putExtra("type",
													// false);
													// in.putExtra("Photo_path",
													// filepath);
													// in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													// startActivity(in);
													Intent intent = new Intent(
															context,
															FullScreenImage.class);
													intent.putExtra("image",
															filepath);
													intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivity(intent);
												} else {

													Toast.makeText(context,
															"File Downloading",
															1).show();
												}
											} catch (Exception e) {
												Log.e("Error",
														"===>" + e.getMessage());
												SingleInstance
														.printLog(
																"View Profile",
																" Image File"
																		+ filepath
																		+ " Onclick on image",
																"ERROR", e);
											}
										}
									} else
										Toast.makeText(context,
												"Sorry! No multimedia file",
												Toast.LENGTH_SHORT).show();
								}

							});

						} else {
							try {
								LinearLayout view = (LinearLayout) layoutInflater
										.inflate(R.layout.customview, null);
								TextView fieldnames = (TextView) view
										.findViewById(R.id.fieldname);
								TextView fieldvalue = (TextView) view
										.findViewById(R.id.fieldvalue);
								final ImageView email = (ImageView) view
										.findViewById(R.id.email);
								ImageView phone = (ImageView) view
										.findViewById(R.id.phone);

								email.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										final Intent emailIntent = new Intent(
												android.content.Intent.ACTION_SEND);

										emailIntent.setType("plain/text");

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_EMAIL,
														new String[] { email
																.getContentDescription()
																.toString() });

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_SUBJECT,
														"Profile");

										emailIntent
												.putExtra(
														android.content.Intent.EXTRA_TEXT,
														"");

										startActivity(Intent.createChooser(
												emailIntent, "Send mail..."));

									}
								});

								phone.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												Intent.ACTION_DIAL);
										startActivity(intent);

									}
								});

								fieldnames.setText(UpperCaseParse
										.captionTextForUpperCaseString(fields
												.getFieldName()));
								if (fields.getFiledvalue() != null)
									fieldvalue
											.setText(UpperCaseParse
													.captionTextForUpperCaseString(fields
															.getFiledvalue()
															.toString()));

								if (type.equalsIgnoreCase("phone")) {
									phone.setContentDescription(fields
											.getFiledvalue());

									phone.setVisibility(View.VISIBLE);
									email.setVisibility(View.GONE);

								}

								viewProf.addView(view);

								layout.setId(records_container.getChildCount());
								Log.i("thread", "Layout id" + layout.getId());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.e("viewprofile", "====> " + e.getMessage());
								e.printStackTrace();
							}
						}

						records_container.addView(layout);
					}

				}
			}
			callDisp.cancelDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("profile", "====> " + e.getMessage());
			e.printStackTrace();
		}
	}

	public class base64Decoder extends
			AsyncTask<ArrayList<Object>, Void, Bitmap> {
		ImageView iview;

		@Override
		protected Bitmap doInBackground(ArrayList<Object>... params) {
			try {
				// TODO Auto-generated method stub
				Log.e("task", "************ camr to do in background");

				ArrayList<Object> list = params[0];
				String bmp_array = (String) list.get(0);
				iview = (ImageView) list.get(1);
				if (!bmp_array.contains(Environment
						.getExternalStorageDirectory() + "/COMMedia/")) {
					bmp_array = Environment.getExternalStorageDirectory()
							+ "/COMMedia/" + bmp_array;
				}
				String blob_path = bmp_array;
				if (blob_path.contains("MPD_")) {
					bitmap = callDisp.ResizeImage(blob_path);
					if (bitmap != null)
						bitmap = Bitmap.createScaledBitmap(bitmap, 200, 150,
								false);
					else
						bitmap = BitmapFactory.decodeResource(getResources(),
								R.drawable.videonotesnew);
					Log.i("BLOB", "inside png formate");
				}

				else if (blob_path.contains("MVD_"))

				{

					Log.i("BLOB", "inside Video file formate");

					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.v_play);

				} else if (blob_path.contains("MAD_"))

				{
					Log.i("BLOB", "inside Audio file formate");

					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.v_play);

				}
				return bitmap;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("viewprofile", "===> " + e.getMessage());
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			try {
				// TODO Auto-generated method stub
				if (result != null)
					Log.i("BLOB", "inside imageview set formate formate");
				iview.setImageBitmap(result);
				super.onPostExecute(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("viewprofile", "===> " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			// TODO Auto-generated method stub
			if ((keyCode == KeyEvent.KEYCODE_BACK))
				callDisp.showKeyDownAlert(context);

			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("viewprofile", "===> " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public String getBuddyname() {
		return this.buddyname;
	}

	@Override
	protected void onResume() {
		try {
			super.onResume();
            AppMainActivity.inActivity = this;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setBuddyname(String buddyname) {
		this.buddyname = buddyname;
	}

}
